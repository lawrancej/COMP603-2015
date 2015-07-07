
// with shared objects, this is a problem
// because program A needs new foo
// program B needs old foo
// avoid shared objects? that's go's solution
// version information in the "name"
// why not link to the hash of the contents of the source of the thing you're linking against?
// shared objects just grow with every version
// the downside is massive shared objects
// to make this manageable, strip away unreferenced versions










// Foo: a library

// Version 1.0
// Suppose program A links against this version
// int foo() {
//    return 0;
// }

// Version 2.0
// Suppose program B links against this version
int foo() {
    return 42;
}
