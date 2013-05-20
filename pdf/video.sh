#!/bin/sh

IFS=$'\n' arr=($(find /tmp/opencast/workspace/mediapackage/$1 -name '*.mp4')); 
echo "$arr"
