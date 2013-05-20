<?php
$txt=$_GET['id'];
$txt1=$_GET['inp'];
$txt2=$_GET['oup'];
$txt3=$_GET['type'];
$txt4=$_GET['val'];
$txt5=$txt2-$txt1;
$con = mysql_connect("localhost","root","123");
if (!$con)
  {
  die('Could not connect: ' . mysql_error());
  }

mysql_select_db("matterhorn", $con);

mysql_query("INSERT INTO ANNOTATION (OUTPOINT, INPOINT, MEDIA_PACKAGE_ID, SESSION_ID, CREATED, USER_ID, LENGTH, ANNOTATION_VAL, ANNOTATION_TYPE ) VALUES ('$txt2', '$txt1', '$txt', 'abcdefgh', CURDATE() , 'admin', '$txt5', '$txt4', '$txt3')");


mysql_close($con);
 header( "Location: http://evideo.iitj.ac.in/engage/ui/watch.html?id=$txt" ) ;
?>
