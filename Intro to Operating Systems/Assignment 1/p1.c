#include <stdio.h>
#include <errno.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
#include <sys/wait.h>
#include <limits.h>
#include <readline/readline.h>
#include <readline/history.h>

int num_processes = 0;

// Node struct for storing processes in bg list.
struct node {
    pid_t pid;
    struct node *next;
};
typedef struct node p_node;

p_node *head = NULL;

// getcwd() to obtain the current directory, getlogin() to get username, and gethostname() to find the name of the
// host running the program, strcat them together into the correct format for print out in terminal using readline.
char *get_shell_text() {
    char *shell_text = NULL;
    char host_name[1024];
	char *user_name;
    char current_dir[80];
	user_name = getlogin();
	gethostname(host_name, 1024);
    getcwd(current_dir, sizeof(current_dir));

    shell_text = malloc(strlen(user_name) + strlen(host_name) + strlen(current_dir) + 6);
    strcpy(shell_text, user_name);
    strcat(shell_text, "@");
    strcat(shell_text, host_name);
    strcat(shell_text, ": ");
    strcat(shell_text, current_dir);
    strcat(shell_text, " >");
	
    return(shell_text);
}

//Helper function to create new node for bg process.
p_node *new_node(pid_t pid, char **args, int num_args) {
    p_node *result = malloc(sizeof(p_node));
    result->pid = pid;
    result->next = NULL;

    return result;
}

// bg_list, prints out the linked list of running bg processes.
void print_list(p_node *head) {
    p_node *curr = head;
    while(curr != NULL) {
        printf("%d: FILE ARGS\n", curr->pid);
        curr = curr->next;
    }
    printf("\n");
}

// adds a new process to bg list.
void add_bg(pid_t pid, char **args, int num_args) {
    p_node *tmp;

    if(head == NULL){
	    num_processes++;
	    head = new_node(pid, args, num_args);
	} else {
	    num_processes++;

	    tmp = head;
	    while (tmp->next != NULL) { // iterate to end of list.
		    tmp = tmp->next;
	    }
	    tmp->next = new_node(pid, args, num_args);
	}
}

// check if any bg processes have finished/terminated
void check_bg_status(int num_p) {
    printf("there are currently %d bg processes running\n", num_p);
    if(num_processes >= 1) {
        pid_t c_pid = waitpid(0, NULL, WNOHANG);

        while(c_pid > 0) {
            if(head->pid == c_pid) {
                num_processes--;
                head = head->next;
            }
            else {
                p_node *tmp = head;
                while(tmp->next->pid != c_pid) { // look for c_pid in bg list.
                    tmp = tmp->next;
                }
                num_processes--;
                tmp->next = tmp->next->next;
            }
            c_pid = waitpid(0, NULL, WNOHANG);
        }
    }
}

void run_cmd(char *command) {
    char *token = strtok(command, " \n"); // split string by space or '\n' (IMPORTANT) there will be error without '\n'
    char *argument[1024];
    char **argumentCopy;
    int j; //counter for bg args.
    int i = 0; //counter for number of arguments passed in.
    
    while(token != NULL){ // make sure that args has a NULL pointer at the end
        argument[i] = token;
        i++;
        token = strtok(NULL, " \n");
    }
    argument[i] = NULL;

    if(argument[0] != NULL) {
        int bg = 0;
        if(strcmp(argument[0], "cd\0") == 0){
            if(i == 1 || strcmp(argument[1], "~\0") == 0) { // just "cd" so chdir to home directory.
                chdir(getenv("HOME"));
                return;
            }
            if(i == 2) {
                chdir(argument[1]);
                return;
            }
            else {
                printf("too many arguments for cd\n");
            }
        }else if(strcmp(argument[0], "bg\0") == 0) {
            argumentCopy = malloc(sizeof(char*) * 5);
            bg = 1;

            // removing "bg" from argument[] and copying to argumentCopy[]
            for(j = 0; j < i - 1; j++) {
	            argumentCopy[j] = malloc(sizeof(char) * (strlen(argument[j + 1]) + 5));
   	            strcpy(argumentCopy[j], argument[j + 1]); 
            }
            argumentCopy[i - 1] = NULL;

        }else if(strcmp(argument[0], "bglist\0") == 0) {
            print_list(head);
            check_bg_status(num_processes);
            return;
        }

        pid_t parent = getpid();
        pid_t pid = fork();

        if(bg == 1) { // execute in bg.
            add_bg(pid, argumentCopy, j);

            if(pid == 0) {
                if(execvp(argumentCopy[0], argumentCopy) == -1){
                    printf("command: %s not found\n", argumentCopy[0]);
	                exit(1);
                }
            }
            else if(pid == -1) {
                printf("Error in fork()");
            } else if(pid > 0) {
                waitpid(pid, NULL, WNOHANG);
            }
        }
        else { // execute in fg.
            if(pid == 0) {
                if(execvp(argument[0], argument) == -1){
                    printf("command: %s not found\n", argument[0]);
	                exit(1);
                }
            }
            else if(pid == -1) {
                printf("Error in fork()");
            } else if(pid > 0) {
                wait(NULL);
            }
        }
    }
}

int main(int argc, char *argv[]) {

    // FORMAT: username@hostname: /home/user >

    char *cmd = NULL;
    char *shell_text = NULL;

    while(1){
        shell_text = get_shell_text();
        cmd = readline(shell_text);
        free(shell_text);
        shell_text = NULL;

        if(strcmp(cmd, "exit") == 0 || strcmp(cmd, "exit()") == 0){
            printf("exiting\n");
            rl_clear_history();
            exit(0);
            break;
        }

        // parse command(s) and run them as needed.
        run_cmd(cmd);

        free(cmd);
        cmd = NULL;
    }
    return(0);
}