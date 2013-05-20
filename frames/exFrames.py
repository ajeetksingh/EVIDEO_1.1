# Python Script for Creating Frames when Annotations, Links and Bookmarks are added to the video, online!

import sys
import subprocess
from decimal import *
from datetime import *

start_t = float(sys.argv[2])
mediaPackageID = sys.argv[1]
type_ = sys.argv[3]

file_f = ""

time = str(timedelta(seconds=start_t + 1))

print time


searchVideo_cmd = "find /opt/matterhorn/felix/work/opencast/downloads/"+mediaPackageID+" -type f -name '*.mp4'"

process = subprocess.Popen(searchVideo_cmd, shell=True, stdout=subprocess.PIPE, stderr=subprocess.PIPE)
out, err = process.communicate()
if(process.returncode==0):
  file_f = out
  print file_f
else:
  print err

ffmpeg_cmd = "ffmpeg -i "+file_f.rstrip('\r\n')+" -r 1 -vframes 1 -ss "+ time +" /var/www/frames/"+type_+"/"+sys.argv[1]+sys.argv[2]+".png"

print ffmpeg_cmd

process1 = subprocess.Popen(ffmpeg_cmd, shell=True, stdout=subprocess.PIPE, stderr=subprocess.PIPE)
out1, err1 = process1.communicate()
if(process.returncode==0):
  print out1
  print "Success"
else:
  print err1
