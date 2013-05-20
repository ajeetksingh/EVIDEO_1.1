# Python Script for Deleting the Frames already extracted when Annotations, Links or Bookmarks are 'updated' or 'deleted'.

import sys
import subprocess


media_id = sys.argv[1]
start_t = sys.argv[2]
type_ = sys.argv[3]

deleteFrame_cmd = "rm -rf /var/www/frames/"+type_+"/"+media_id+start_t+".png"

print deleteFrame_cmd

process = subprocess.Popen(deleteFrame_cmd, shell=True, stdout=subprocess.PIPE, stderr=subprocess.PIPE)
out, err = process.communicate()

if(process.returncode == 0):
	print out
	print "success"
else:
	print err
