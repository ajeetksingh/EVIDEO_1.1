<?php

$id=$_GET['media_id'];
$title=$_GET['title'];





$start=$_GET['starttime'];
$stop=$_GET['stoptime'];
$user=$_GET['user'];
$description=$_GET['desc'];

$type = "bookmarks";

$con = mysql_connect("localhost","root","123");
if (!$con)
  {
  die('Could not connect: ' . mysql_error());
  }

mysql_select_db("matterhorn", $con);

if(mysql_query("INSERT INTO bookmarks(media_id, user_name, start, stop, title, description) VALUES  ('$id', '$user', '$start', '$stop', '$title', '$description')")){

	echo "Success";
	exec("python /var/www/frames/exFrames.py $id $start $type");

}


mysql_close($con);
 //header( "Location: http://evideo.iitj.ac.in/engage/ui/watch.html?id=$txt" ) ;
?>
