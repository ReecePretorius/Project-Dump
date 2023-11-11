.phony all:
all: p1 inf args

p1: p1.c
	gcc p1.c -lreadline -lhistory -ltermcap -o p1

inf: inf.c
	gcc inf.c -o inf

args: args.c
	gcc args.c -o args

.PHONY clean:
clean:
	-rm -rf *.o *.exe
