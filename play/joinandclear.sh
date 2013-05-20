#!/bin/sh

cd $DIR
#/usr/local/bin/mencoder -ovc lavc -lavcopts vcodec=mpeg4:vbitrate=1800:vhq:keyint=250 -oac pcm copy -idx -o oa.mp4 $PAR

/usr/local/bin/mencoder $PAR -ovc lavc -lavcopts vcodec=mpeg4:vbitrate=1800 -oac mp3lame -lameopts cbr:br=128 -o joined.mp4
/usr/local/bin/ffmpeg -i joined.mp4 -vcodec libx264 -vpre hq -vpre ipod640 -b 250k -bt 50k -acodec libfaac -ab 56k -ac 2 -s 480x320 oa.mp4
rm -rf $PAR
rm -rf joined.mp4
cd ..

#/bin/rm -rf $1
#/bin/rm -rf "$1#1"



