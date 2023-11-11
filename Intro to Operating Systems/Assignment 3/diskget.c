/**
 * p3
 * diskget.c
 * 2022-04-04
 */

#include <stdio.h>
#include <sys/mman.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <netinet/in.h>

#define increment 64

struct __attribute__((__packed__)) superblock_t {
    uint8_t   fs_id [8];
    uint16_t block_size;
    uint32_t file_system_block_count;
    uint32_t fat_start_block;
    uint32_t fat_block_count;
    uint32_t root_dir_start_block;
    uint32_t root_dir_block_count;
};

struct __attribute__((__packed__)) dir_entry_timedate_t {
    uint16_t year;
    uint8_t month;
    uint8_t day;
    uint8_t hour;
    uint8_t minute;
    uint8_t second;
};

struct __attribute__((__packed__)) dir_entry_t {
    uint8_t status;
    uint32_t starting_block;
    uint32_t block_count;
    uint32_t size;
    struct dir_entry_timedate_t create_time;
    struct dir_entry_timedate_t modify_time;
    uint8_t filename[31];
    uint8_t unused[6];
};

int main(int argc, char *argv[]) {
    int fd = open(argv[1], O_RDWR);
    struct stat buffer;

    int status = fstat(fd, &buffer);
    if (status < 0) {
        perror("error on fstat");
        return 1;
    }

    void* address = mmap(NULL, buffer.st_size, PROT_READ | PROT_WRITE, MAP_SHARED, fd, 0);

    struct superblock_t* sb;
    sb = (struct superblock_t*) address;

    int start_block = htons(sb->block_size) * ntohl(sb->root_dir_start_block);
    int block_size = htons(sb->block_size);

    struct dir_entry_t* root_block_entry;
    char* fileToCopyPath = argv[2];
    char* fileToWriteName = argv[3];
    char* fileToCopyName = strtok(fileToCopyPath, "/");

    for (int i = start_block; i < start_block + block_size; i += increment) {
        root_block_entry = (struct dir_entry_t*) (address + i);
        const char* name = (const char*)root_block_entry->filename;
        if (ntohl(root_block_entry->size) == 0) {
            printf("File not found.\n");
            munmap(address, buffer.st_size);
            close(fd);
            return 1;
        }
        if (strcmp(name, fileToCopyName) == 0) {
            FILE *fp;
            fp = fopen(fileToWriteName, "wb");
            fwrite(root_block_entry->filename, 1, block_size, fp);
            fclose(fp);
            break;
        }
    }
    munmap(address, buffer.st_size);
    close(fd);
    return 0;
}