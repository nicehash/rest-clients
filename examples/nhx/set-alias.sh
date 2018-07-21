#!/bin/sh

called=$_
if [[ $called == $0 ]]; then
  fullpath="${BASH_SOURCE[@]}"
  echo "Error: This script has to be run as 'source $fullpath' or '. $fullpath'"
  exit 1
fi


fullpath="${BASH_SOURCE[@]}"
dir="$( cd "$(dirname $fullpath)" ; pwd -P )"

alias nhx="java -jar $dir/target/*-jar-with-dependencies.jar"