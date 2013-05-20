<?php

$id=$_GET['media_id'];
$title=$_GET['title'];
$url=$_GET['url'];

//$url1="http://".$url;

$description=$_GET['description'];
$start=$_GET['starttime'];
$stop=$_GET['stoptime'];
$user=$_GET['user'];

$type = "links";

$con = mysql_connect("localhost","root","123");
if (!$con)
  {
  die('Could not connect: ' . mysql_error());
  }

mysql_select_db("matterhorn", $con);

if(mysql_query("INSERT INTO Link(media_id, title, url, description, start, stop, username) VALUES  ('$id', '$title', '$url', '$description', '$start', '$stop', '$user')")){

	echo "Success";
        exec("python /var/www/frames/exFrames.py $id $start $type");

}


mysql_close($con);
 //header( "Location: http://evideo.iitj.ac.in/engage/ui/watch.html?id=$txt" ) ;
?>
