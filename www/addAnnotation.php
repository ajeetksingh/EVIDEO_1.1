<?php

$id=$_GET['id'];
$title=$_GET['val'];
$start=$_GET['starttime'];
$stop=$_GET['stoptime'];
$user=$_GET['user'];
$type = "annotations";

$con = mysql_connect("localhost","root","123");
if (!$con)
  {
  die('Could not connect: ' . mysql_error());
  }

if(mysql_select_db("matterhorn", $con))
{
	echo "Database selected!";
}

if(mysql_query("INSERT INTO annotations(media_id, annotations, start, stop, username) VALUES ( '$id', '$title', '$start', '$stop', '$user')"))
{
	echo "Success!";
        exec("python /var/www/frames/exFrames.py $id $start $type");
}
else
{
	echo "Error!!".mysql_error();
}


mysql_close($con);
 //header( "Location: http://evideo.iitj.ac.in/engage/ui/watch.html?id=$txt" ) ;
?>
