<?php 
$mediaid=$_GET['media_id'];
$id=$_GET['id'];
$title=$_GET['val'];
$start=$_GET['starttime'];
$stop=$_GET['stoptime'];
$type = "annotations";

$con = mysql_connect("localhost","root","123");
if (!$con)
  {
  die('Could not connect: ' . mysql_error());
  }

mysql_select_db("matterhorn", $con);

$query = " SELECT * FROM annotations WHERE id = $id and media_id='$mediaid' ";

$result = mysql_query($query);

if (!$result) die ("Database Access Failed". mysql_error());

$info = mysql_fetch_array($result);

$old_mediaid = $info['mediaid'];
$old_start = $info['start'];

exec(" python /var/www/frames/delFrames.py $old_mediaid $old_start $type ");

$sql = mysql_query (" update annotations set annotations='$title', start=$start, stop=$stop where media_id='$mediaid' and id =$id " ); 

if ( $sql ){

  echo "Success";
  exec(" python /var/www/frames/exFrames.py $mediaid $start $type ");	

}
?> 
