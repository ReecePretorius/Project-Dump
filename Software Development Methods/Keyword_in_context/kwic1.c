#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <ctype.h>

#define BUFFER_SIZE 71

//function prototypes
int get_data();//reads the data from stdin into approppriate arrays.
int tokenize(char*);
int sort(char*);//sorts the list of keywords alphabetically.
int strcmp_wrapper(const void*, const void*);
void print_sorted();//sorts the phrases, capitalizes the keywords in the sentence and prints them(no formatting). change to sort_phrases().
//void capitalize_keywords();//capitalizes the keywords in the sorted phrases and store in a new dynamically allocated 2d array.
//void format_lines();//take the sorted 2d array of phrases with capitalized keywords and format them to the correct column for printing.
//void print_final();//take the final 2d array of sorted, formated lines and print them.



//global variables to make not global somehow..
int j = 0;
int num_keys = 0;
int num_phrases = 0;
char exclude[BUFFER_SIZE][BUFFER_SIZE];
char temp[BUFFER_SIZE][BUFFER_SIZE];
char phrase[BUFFER_SIZE][BUFFER_SIZE];
char keywords[BUFFER_SIZE][BUFFER_SIZE];
char buffer[BUFFER_SIZE];
char buffer_2[BUFFER_SIZE];
char line[BUFFER_SIZE];

//main////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
int main(int argc, char* argv[]){

  get_data();

  for(int i = 0; i<num_phrases; i++){
      tokenize(phrase[i]);
  }

  //for(int i = 0; i < num_keys; i++){//buggy prints
  //       printf("unsorted: %s+\n", temp[i]);
  //}

  qsort(*temp, num_keys, BUFFER_SIZE*sizeof(char), strcmp_wrapper);//num is giving wrong value, correct value then the sort works.
  //for(int i = 0; i < num_keys; i++){//buggy prints
  //      printf("sorted: %s+\n", temp[i]);
  //}

  print_sorted();

}

/**
* gets data from stdin and puts them into appropriate arrays for excluded words
* and phrases
*/
int get_data(){/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
   int count = 0;
   int i = 0;
   int j = 0;

  for(int j = 0; j <= 2; j++){
    fgets(buffer, 60, stdin);
    strncpy(line, buffer, BUFFER_SIZE);
    //printf("T-%s-\n", line);
 }

  while(strcmp(buffer, "::\n") != 0){
     strncpy(exclude[i], buffer, BUFFER_SIZE);
     exclude[i][strlen(exclude[i]) - 1] = '\0';
     fgets(buffer, BUFFER_SIZE, stdin);
     //printf("_%s_\n", exclude[i]);
     i++;
     count++;
  }

  while(fgets(buffer, BUFFER_SIZE, stdin) > 0){//mostly working
     strncpy(phrase[j], buffer, BUFFER_SIZE);
     phrase[j][strlen(phrase[j]) - 1] = '\0';
     //printf("phrases: %s-\n", phrase[j]);
     j++;
     num_phrases++;
  }
  return(j);
}

/*
*tokenize takes the phrase[i] and tokenizes it, while it does this it campares each token to the list of exclusion words, if they are equal then a flag is set
*the flag is then used to determine if a word is a keyword, that word is then added to an array called temp. num_keys keeps track of how many keywords were added.
*/
int tokenize(char *phrase){/////////////////////////////////////////////////////////////////////////////////////////////////////////////
  int i = 0;
  int flag = 0;
  int num = sizeof(exclude)/sizeof(exclude[0]);

  strncpy(buffer, phrase, strlen(phrase) + 1);

  char* token = strtok(buffer, " ");
    while(token != NULL){
        flag =0;
        for(int i = 0; i <= num; i++){
            if(strcmp(token, exclude[i])==0){
                flag = 1;
            }
        }
        if(flag == 0){
             strncpy(temp[j], token, strlen(token)+1);
             j++;
             num_keys++;
        }
        token = strtok(NULL, " ");
    }
  return(0);
}
/*
*strcmp_wrapper is part of the qsort function that that i got from the code provided in class by zastre.
*/
  int strcmp_wrapper(const void *a, const void *b) {/////////////////////////////////////////////////////////////////////////////////
      char *sa = (char *)a;
      char *sb = (char *)b;

      return(strcmp(sa, sb));
  }

/*
*print_sorted is a function that searches for the keywords one at a time for every phrase, it then prints each phrase 1 token at a time. when it encounters the keyword
*that it is currently looking for it will capitalize it and print it as well.
*/
 void print_sorted(){

        for(int i = 0; i <= num_keys/*num of keywords*/; i++){

            for(int t = 0; t<= num_phrases/*num of phrases*/; t++){
                strncpy(buffer, phrase[t], strlen(phrase[t]) + 1);
                char* token = strtok(buffer, " ");

                while(token){
                    if(strcmp(temp[i], token)==0){
                        int j = 0;
                        strncpy(buffer_2, phrase[t], strlen(phrase[t]) + 1);
                        char* tok = strtok(buffer_2, " ");//tokenizes each phrase again and searches by strcmp for the keyword found in the keyword array temp[].
                        while(tok){

                            ////////////////////////////////////////////////////////////////////////////////////////////
                            if(strcmp(temp[i], tok)==0){
                                //capitalize keyword
                                while(tok[j]){
                                    putchar(toupper(tok[j]));//capitalizes aech character of the keyword and prints them.
                                    j++;
                                }
                                printf(" ");
                            /////////////////////////////////////////////////////////////////////////////////////////////////


                            }else{
                                printf("%s ", tok);//words
                            }
                            tok = strtok(NULL, " ");
                        }
                    }
                    token = strtok(NULL, " ");
                }
            }
            printf("\n");
        }
    }
