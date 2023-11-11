/**
 * mts.c
 * 2022-03-14
 */

#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <unistd.h>
#include <pthread.h>

#define USLEEP_INTERVAL 50000

int num_trains = 0;
pthread_mutex_t station_mutex_e;
pthread_mutex_t station_mutex_w;
pthread_mutex_t track;

pthread_cond_t track_empty;
pthread_cond_t train_finished_loading;

struct timespec start, stop;
double accum;

int starter = 0;

struct Train_Thread {
    int id;
    char* direction;
    int priority;
    int time_to_load;
    int time_to_cross;
    pthread_cond_t *train_convar;
} trains[100];

// used idea for priority queue design from https://www.geeksforgeeks.org/priority-queue-using-linked-list/
typedef struct node {
    struct Train_Thread* data;
    int priority;
    struct node* next;
} Node;

Node* east_station_queue;
Node* west_station_queue;

Node* newNode(struct Train_Thread* d, int p) {
    Node* temp = (Node*)malloc(sizeof(Node));
    temp->data = d;
    temp->priority = p;
    temp->next = NULL;
 
    return temp;
}
 
// Return the value at head.
struct Train_Thread* peek(Node** head) {
    return (*head)->data;
}
 
// Removes the element with the highest priority.
void pop(Node** head) {
    Node* temp = *head;
    (*head) = (*head)->next;
    free(temp);
}

void push(Node** head, struct Train_Thread* d, int p) {
    Node* start = (*head);
 
    // Create new Node.
    Node* temp = newNode(d, p);

    if ((*head) == NULL) {
        (*head) = temp;
        return;
    }
    if ((*head)->priority < temp->priority || ((*head)->data->time_to_load == temp->data->time_to_load && (*head)->data->id > temp->data->id)) {
        // Insert New Node before head.
        temp->next = *head;
        (*head) = temp;
    }
    else {
        // Traverse the list and find a position to insert the new node.
        while (start->next != NULL && start->next->priority >= temp->priority) {
            if(start->next->data->time_to_load == temp->data->time_to_load && start->next->data->id > temp->data->id) {
                temp->next = start->next;
                start->next = temp;
                return;
            }
            start = start->next;
        }
        temp->next = start->next;
        start->next = temp;
    }
}

int isEmpty(Node** head) {
    return (*head) == NULL;
}

void read_file(struct Train_Thread* train, char* line) {
    int attributes[4];
	char buffer[128];
    char* direction;
	int i = 0;
	strcpy(buffer, line);

	char* token = strtok(buffer, " ");
    direction = token;
	
	// tokenize
	while(token != NULL) {
		attributes[i++] = atoi(token);
        token = strtok(NULL, " ");
    }

	train->time_to_load = attributes[1];
	train->time_to_cross = attributes[2];

    if(strcmp(direction,"e") == 0) {
        train->direction = "East";
        train->priority = 0;
    }
    else if(strcmp(direction,"E") == 0) {
        train->direction = "East";
        train->priority = 1;
    }
    else if(strcmp(direction,"w") == 0) {
        train->direction = "West";
        train->priority = 0;
    }
    else {
        train->direction = "West";
        train->priority = 1;
    }
}

void print_time() {
    if (clock_gettime(CLOCK_REALTIME, &stop) == -1) {
        perror("gettime error");
        exit(EXIT_FAILURE);
    }

    accum = (stop.tv_sec - start.tv_sec) + (stop.tv_nsec - start.tv_nsec) / 1000000000;
    int minutes = (int)accum / 60;
    int hours = (int)accum / 3600;
    printf("%02d:%02d:%04.1f ", hours, minutes, accum);
}

void lock_and_pop_east() {
    pthread_cond_signal(peek(&east_station_queue)->train_convar);
    pthread_mutex_lock(&station_mutex_e);
    pop(&east_station_queue);
    pthread_mutex_unlock(&station_mutex_e);
}

void lock_and_pop_west() {
    pthread_cond_signal(peek(&west_station_queue)->train_convar);
    pthread_mutex_lock(&station_mutex_w);
    pop(&west_station_queue);
    pthread_mutex_unlock(&station_mutex_w);
}

void *Train(void *arg){
    struct Train_Thread* train = arg;

    while(!starter) {}
    usleep((useconds_t)train->time_to_load * 100000);

    print_time();
    printf("Train %2d is ready to go %4s\n", train->id, train->direction);

    if(strcmp(train->direction,"East") == 0) {
        pthread_mutex_lock(&station_mutex_e);
        // printf("pushing onto east queue\n");
        push(&east_station_queue, train, train->priority);
        pthread_mutex_unlock(&station_mutex_e);
    }
    
    if(strcmp(train->direction,"West") == 0) {
        pthread_mutex_lock(&station_mutex_w);
        // printf("pushing onto west queue\n");
        push(&west_station_queue, train, train->priority);
        pthread_mutex_unlock(&station_mutex_w);
    }
    

    pthread_cond_signal(&train_finished_loading);
    pthread_cond_wait(train->train_convar, &track);
    
    print_time();
    printf("Train %2d is ON the main track going %4s\n", train->id, train->direction);
    
    usleep((useconds_t)train->time_to_cross * 100000);
    
    print_time();
    printf("Train %2d is OFF the main track after going %4s\n", train->id, train->direction);
    
    num_trains--;
    pthread_mutex_unlock(&track);
    pthread_cond_signal(&track_empty);
    pthread_cond_destroy(train->train_convar);

    pthread_exit(NULL);
}

void dispatcher() {
    int last_dir = 0;
    int first = 1;
    while (num_trains != 0) {
        pthread_mutex_lock(&track);

        if (isEmpty(&east_station_queue) && isEmpty(&west_station_queue)) {
            pthread_cond_wait(&train_finished_loading, &track);
        }
        sleep(1);

        if (!isEmpty(&east_station_queue) && !isEmpty(&west_station_queue)) {
            if (peek(&east_station_queue)->priority > peek(&west_station_queue)->priority) {
                last_dir = 0;
                lock_and_pop_east();
            } 
            else if (peek(&east_station_queue)->priority < peek(&west_station_queue)->priority) {
                last_dir = 1;
                lock_and_pop_west();
            } 
            else {
                if (last_dir == 1 || first) {
                    first = -1;
                    last_dir = 0;
                    lock_and_pop_east();
                }
                else {
                    last_dir = 1;
                    lock_and_pop_west();
                };
            }
        } 
        else if (!isEmpty(&east_station_queue) && isEmpty(&west_station_queue)) {
                last_dir = 0;
                lock_and_pop_east();
        } 
        else if (isEmpty(&east_station_queue) && !isEmpty(&west_station_queue)) {
                last_dir = 1;
                lock_and_pop_west();
        }
        pthread_cond_wait(&track_empty, &track);
        pthread_mutex_unlock(&track);
    }
}

int main(int argc, char* argv[]) {
    char line[256];

    const char* file = argv[1];
    FILE* fp = fopen(file, "r");

    if(!fp){
        perror("file open error");
        return 1;
    }

    // Reading file and creating/populating train nodes.
    while(fgets(line, sizeof(line), fp)){
        struct Train_Thread* train_node = &trains[num_trains];
        train_node->id = num_trains++;

        read_file(train_node, line);
    }
    fclose(fp);

    pthread_mutex_init(&station_mutex_e, NULL);
    pthread_mutex_init(&station_mutex_w, NULL);
    pthread_mutex_init(&track, NULL);
    pthread_cond_init(&track_empty, NULL);
    pthread_cond_init(&train_finished_loading, NULL);

    pthread_cond_t train_convars[num_trains];
    for (int i = 0; i < num_trains; i++) {
        pthread_cond_init(&train_convars[i], NULL);
        trains[i].train_convar = &train_convars[i];
    }

    pthread_t threads[num_trains];
    for (int i = 0; i < num_trains; i++) {
        pthread_create(&threads[i], NULL, Train, (void*)&trains[i]);
    }
    sleep(1);

    if(clock_gettime(CLOCK_REALTIME, &start) == -1) {
        perror("gettime error");
        exit(EXIT_FAILURE);
    }

    starter = 1;
    dispatcher();

    pthread_mutex_destroy(&station_mutex_e);
    pthread_mutex_destroy(&station_mutex_w);
    pthread_mutex_destroy(&track);

    return 0;
}