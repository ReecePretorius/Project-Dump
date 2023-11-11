/**
 * p3
 * diskinfo.c
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

#define increment 4

struct __attribute__((__packed__)) superblock_t {
    uint8_t   fs_id [8];
    uint16_t block_size;
    uint32_t file_system_block_count;
    uint32_t fat_start_block;
    uint32_t fat_block_count;
    uint32_t root_dir_start_block;
    uint32_t root_dir_block_count;
};

void printSuperBlockInfo(struct superblock_t* sb) {
    printf("Super block information:\n");
    printf("Block size: %d\n", htons(sb->block_size));
    printf("Block count: %d\n", ntohl(sb->file_system_block_count));
    printf("FAT starts: %d\n", ntohl(sb->fat_start_block));
    printf("FAT blocks: %d\n", ntohl(sb->fat_block_count));
    printf("Root directory start: %d\n", ntohl(sb->root_dir_start_block));
    printf("Root directory blocks: %d\n\n", ntohl(sb->root_dir_block_count));
}

void printFatInformation(int free, int reserved, int allocated) {
    printf("FAT information:\n");
    printf("Free Blocks: %d\n", free);
    printf("Reserved Blocks: %d\n", reserved);
    printf("Allocated Blocks: %d\n", allocated);
}

int main(int argc, char *argv[]) {
    if (argc > 2 || argc < 2) {
        printf("Invalid arguments - Usage: ./diskinfo test.dmg\n");
        return 1;
    }
    
    int free_blocks = 0;
    int reserved_blocks = 0; 
    int allocated_blocks = 0;

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
    printSuperBlockInfo(sb);

    int start_block = htons(sb->block_size) * ntohl(sb->fat_start_block);
    int end_block = htons(sb->block_size) * ntohl(sb->fat_block_count);
    
    for (int i = start_block; i < start_block + end_block; i += increment) {
        int curr = 0;
        
        memcpy(&curr, address + i, increment);
        curr = htonl(curr);
        
        if (curr == 0) {
            free_blocks++;
        }
        else if (curr == 1) {
            reserved_blocks++;
        }
        else {
            allocated_blocks++;
        }
    }
    printFatInformation(free_blocks, reserved_blocks, allocated_blocks);

    munmap(address, buffer.st_size);
    close(fd);
    return 0;
}