# Operating system paging simulation - JAVA

### Simulation of a system with demand pagination with global frame allocation, second chance page replacement algorithm, and CPU Round-Robin mutual exclusion.




#### Project Specifications

- System has the following hardware features: Memory, Disk, CPU, everything connected by a bus.

- In this system, the CPU has a single core.

- In this system, there will always be disk space.

- The initial execution time is 0 seconds.

- There are **n** frames in main memory, identified with **idframe**, 0 ≤ **idframe** ≤ **nframes** - 1. The frames can be used by the processes that need to be executed and that queues and other core data structures of the operating system are in another memory.

- The system controls the list of free frames.

- In the initial state of the system, the seed for generating pseudo-random numbers is *seed*, all frame **nframes** are free, and all process page table entries have bit values valid/invalid at 0 and reference bit at 0.

- There are **n** user processes, identified with **id**, 0 ≤ **id** ≤ n - 1.

- Process Builder(Creator) creates each process **id** at the instant **tcid** seconds and then “puts” **id** in the process ready queue.

- Each process **id** needs **npid** pages, identified by idpageid, 0 ≤ idpageid ≤ **npid** - 1, and has CPU burst size set to tbid seconds.

- Each process will access your pages at random, and for simplicity will never access a page out of range 0 to **npid** - 1.

- The Round-Robin CPU Scheduler will use the Round-Robin CPU scheduling algorithm, with time quantum **tq** (input parameter) to choose and remove a **pk** process from the ready queue and send it to a Dispatcher.

- The variables mentioned up to this point are defined by the system user, from parameters input via command line.

- Each process **id** has a **pagetableid** page table.

- The system does not use TLB (Translation Lookaside Buffer).

- Each **pagetableid** entry has a frame **idframe** where the page is in memory, a valid / invalid bit, and a reference bit. In this T2 Work these bits can be implemented as boolean type or byte type.

- The Timer will warn the CPU Round-Robin Scheduler scheduler that the CPU burst of the process that is on the CPU has terminated or that it has already run for **tq** units of time.

- The Dispatcher, when given the indication of **pk**, will randomly generate from a seed , a page reference by drawing a page **idpagepk**.
    1. If the page has a valid bit, then there will be no page fault and the reference bit will be set to 1, and Dispatcher resets the Timer and frees the CPU to **pk**;
    2. If the page has an invalid bit, then page fault will occur and Dispatcher requests Pager to bring **idpagepk** into memory and waits until Pager warns that **idpagepk** is in memory, and when warned, Dispatcher resets the Timer and frees the CPU at **pk**.


- In the event of a page fault:
    1. If there is a free frame, then Pager allocates it to contain the requested **idpagepk** page, reads the requested page from disk, updates the page table and free frame list, and warns the Dispatcher.
    2. Otherwise, Pager uses the second chance page replacement algorithm considering global frame allocation to replace the page of a frame, reads the requested page from disk, and properly changes both the page table for the victim page idpageidvictim of the victim case **iidvictim** as to the page table referring to the page that was contemplated, and warns the Dispatcher.
 
- The **npagefaults** variable counts the number of page fault occurrences.

- When an **id** process has already run for tbid seconds, it is removed from the ready queue, and all frames allocated to the **id** process go back to the free frame list and **pagetableid** is destroyed.

- The Process Builder(Creator), Timer, CPU Round-Robin Scheduler, Dispatcher, and Pager components **are be implemented as threads** that communicate by shared memory with mutual exclusion guarantee where appropriate, especially when accessing the ready queue and List of free frames.

- During system simulation, the system time is used as a time stamp to show modification or occurrence of the following events, where **id** is the id of the process whose action is being displayed, **idpage** is the id of the page being replaced or brought from the disk, and idframe and the frame in which a page is being placed or taken from a victim page.

#### How to execute

The the input parameter values must be made via the command line using positive integers, separated by space in the following format.

  __seed__ __nframes__ __n__ __tq__ (data list for each process __id__ __np__ __tc__ __tb__).

An example of input parameter values is:

      345    5    3    2       1 5 5 15        0 7 1 5        2 6 2 10
