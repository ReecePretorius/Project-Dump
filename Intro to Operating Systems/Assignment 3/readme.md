# Simple File System

This project is designed to manipulate file system images through a multi-part utility. It encompasses various functionalities, including extracting essential file system information, displaying directory contents, copying files between the file system and the Linux directory, and enabling file transfer between the Linux directory and the file system. Part I involves extracting and displaying file system information derived from the super block and FAT, whereas Part II extends this to list the contents of the root directory or a specified sub-directory. Part III enables the copying of a file from the file system to the Linux directory, with error handling for missing files. Finally, Part IV facilitates the transfer of files from the Linux directory to the file system, maintaining directory integrity and handling missing files accordingly. Each part operates via distinct command line arguments, serving specific functionalities for efficient file system manipulation.

## Compile / Run

To build/compile the executables for parts 1-4 use:
```bash
make
```
Clean up using:
```bash
make clean
```

## Usage

### diskinfo:
description: displays information about the file system including various superblock and FAT information.

```bash
./diskinfo test.dmg
```

### disklist:
description: displays the content(s) of the root directory of the file system.
each entry will display information including: File/Directory(F/D), file size, file name, and creation date.

```bash
./disklist test.dmg
```

OR

```bash
./disklist test.dmg /
```

### diskget

description: copies a file from the file system to the current directory in Linux, outputs "File not found"
if there is no file with the specified name.

```bash
./diskget test.dmg /<file to copy> <output file name>
```

### diskput:
description: copies a file from the current Linux directory into the file system's root directory, outputs "File not found" if there is no file with the specified name.

```bash
./diskput test.dmg <file to copy> /<output file name or path>
```