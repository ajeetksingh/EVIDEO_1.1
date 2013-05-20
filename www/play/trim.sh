#!/bin/sh

IFS=$'\n' arr=($(find /opt/matterhorn/felix/work/opencast/downloads/$1 -name '*.mp4')); 
echo "$arr"
#/usr/local/bin/ffmpeg -i $arr -vcodec libx264 -deinterlace -crf 25 -vpre hq -f mp4 -s hd480 -ab 128k -threads 0 -y "$arr#mp4.mp4"
/usr/local/bin/ffmpeg -i $arr -vcodec copy -acodec copy -ss $2 -t $3 "$4#trim.mp4"
/usr/local/bin/ffmpeg -i "$4#trim.mp4" -s 480x270 "$4#trim1.mp4"


