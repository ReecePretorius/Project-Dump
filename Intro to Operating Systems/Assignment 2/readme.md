# Multi-Thread Scheduling (MTS)

My original design involved 2 mutexes and 2 condition variables, I ended up changing this later on during implementation to have separate mutexes for East and West stations respectively.

This serves the same purpose as a single mutex for locking the stations/queues but makes it easier to understand what is going on in the individual queues.

## Function Descriptions:

### Main():
Opens input file and uses fgets() to get each line and passes the line and train_node to read_file() to create a node for each train listed in the file with the appropriate loading and crossing times.

main also inits all of the mutexes and condition variables and creates a thread for each train that was read in using pthread_create().

and finally the dispatcher() function is called to handle dispatching the trains based on the simulation rules provided in the assignment specification.

### void *Train(void *arg)

This is the main driver function for the train threads that are spawned, it signals when the trains are loaded and ready to dispatch, it also handles pushing the trains onto the queue and unlocks the track when it is empty. Prints out to the console the simulation time and status of given trains.

### void dispatcher()

As mentioned this handles the simulation rules stated in the assignment specification on train priorities and general rules of the track. Based on the rules it will lock the queue for the given station and pop the train from the queue. It does this with help from the lock and pop helper functions lock_and_pop_east() and lock_and_pop_west().

### void read_file()

takes in the line from fgets() and a new node containing a train struct which is used to store data related to a train.
It tokenizes the line and uses those tokens to populate the struct's id, direction, priority, time_to_load, and time_to_cross fields.

## Compile

To build/compile the executable:
```bash
make
```

## Usage

```bash
./mts <input.txt>
```
included are a few sample input files for testing:

```bash
./mts test_trains.txt
./mts test_trains2.txt
./mts test_trains3.txt
```