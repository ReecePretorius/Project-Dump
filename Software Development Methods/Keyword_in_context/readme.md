# Keyword in Context

This project focuses on developing a Keyword in Context (KWIC) indexing utility, aimed at assisting readers in identifying relevant references within the original context of a text. The utility will generate a KWIC index by showcasing occurrences of specific words in sentences while excluding common words. Initially, the utility will transform input text into associated KWIC output text, focusing solely on content within lines. Subsequent phases will include integrating left-hand column reference items. The ultimate goal is to create a user-friendly tool that streamlines the search for specific references within a text's context.

## Compile / Run

### C Version:

Compile:

```bash
gcc kwic1.c -o kwic1
```

2nd pipe is optional to check results against expected output,

```bash
cat tests/kwic1/in05.txt | ./kwic1 | diff tests/kwic1/out05.txt
```

otherwise just direct the output to a new text file

```bash
cat tests/kwic1/in05.txt | ./kwic1 > output.txt
```

### Python Version:

```bash
cat tests/kwic3/in05.txt | python ./kwic3.py | diff tests/kwic3/out05.txt
```

or without diff:

```bash
cat tests/kwic3/in05.txt | python ./kwic3.py > output.txt
```