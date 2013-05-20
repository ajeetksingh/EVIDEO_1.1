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

$result=mysql_query("SELECT ANNOTATION_VAL FROM ANNOTATION WHERE ID=1");
echo "$result";

//mysql_close($con);
// header( "Location: http://evideo.iitj.ac.in/engage/ui/watch.html?id=$txt" ) ;
?>
