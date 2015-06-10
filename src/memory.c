#include <stdio.h>

// Global allocation
int global = 42;

int foo(){
    // Stack allocation
    int bar = 42;
    // Heap allocation
    int *baz = (int*) malloc(sizeof(int));
    // bar is deallocated on foo() exit
}

int main(int argc, char** argv) {
    foo();
}