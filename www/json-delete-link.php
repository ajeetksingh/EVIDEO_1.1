<?php 
$mediaid=$_GET['media_id'];
$id=$_GET['id'];
$type = "links"


$con = mysql_connect("localhost","root","123");

if (!$con)
  {
  die('Could not connect: ' . mysql_error());
  }

mysql_select_db("matterhorn", $con);

$query = " SELECT * FROM Link WHERE id = $id and media_id = '$mediaid'";

$result = mysql_query($query);

$info = mysql_fetch_array($result);

$old_mediaid = $info['media_id'];
$old_start = $info['start'];

exec(" python /var/www/frames/delFrames.py $old_mediaid $old_start $type ");

$sql=mysql_query("delete from Link where media_id='$mediaid' and id =$id"); 
?> 
