#!/bin/sh
find . -name *input.txt -exec zip -u --password $AOC_INPUT_DATA_PWD {}.zip {} \;
