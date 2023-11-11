/**
 * p3
 * disklist.c
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

void printDirectory(struct dir_entry_t* root_block_entry, int type) {
    int year, month, day, hours, minutes, seconds = 0;
    unsigned char* name;

    printf("%c %10d %30s %d/%02d/%02d %02d:%02d:%02d\n",
        type,
        ntohl(root_block_entry->size),
        name = root_block_entry->filename,
        year = htons(root_block_entry->modify_time.year),
        month = root_block_entry->modify_time.month,
        day = root_block_entry->modify_time.day,
        hours = root_block_entry->modify_time.hour,
        minutes = root_block_entry->modify_time.minute,
        seconds = root_block_entry->modify_time.second
    );
}

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
    if (argc == 2 || strcmp(argv[2], "/") == 0) {
        for (int i = start_block; i <= start_block + block_size; i += increment) {
            root_block_entry = (struct dir_entry_t*) (address + i);
            int type = 0;

            if (ntohl(root_block_entry->size) == 0) {
                continue;
            }
            if (root_block_entry->status == 3) {
                type = 'F';
            }
            else if (root_block_entry->status == 5) {
                type = 'D';
            }
            printDirectory(root_block_entry, type);
        }
    }
    munmap(address, buffer.st_size);
    close(fd);
}