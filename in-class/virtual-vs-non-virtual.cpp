#include <iostream>
using namespace std;
class Animal {
public:
    virtual void speak() = 0;
};
class Cow : public Animal {
public:
    void speak() {
        cout << "Moo" << endl;
    }
};
int main(int argc, char ** argv) {
    Animal * bessie = new Cow();
    Animal * sally;
    bessie->speak();
}
