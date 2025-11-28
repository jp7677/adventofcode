#!/bin/sh
find . -name *input.txt.zip -exec unzip -P $AOC_INPUT_DATA_PWD -o {} \;
