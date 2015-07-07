#include <stdio.h>

extern int foo();

int main(int argc, char** argv) {
    printf("foo returns %d\n", foo());
    return foo();
}