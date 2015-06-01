#!/bin/bash

expected="$(cat quine.bf)"
actual="$(./brainfuck.exe quine.bf)"

if [ "$expected" == "$actual" ]; then
    echo "Hooray, you're done, probably!"
else
    echo "Whoops, you're not done yet."
fi
