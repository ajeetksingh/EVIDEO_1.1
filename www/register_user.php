<?php

$connection = @mysql_connect("localhost", "root", "123") or die(mysql_error());
$db = @mysql_select_db("matterhorn",$connection)or die(mysql_error());
$uname=$_GET['uname'];
//make query to database
$sql ="SELECT * FROM MH_USER WHERE username='$uname'";
$response=array();
$result = @mysql_query($sql,$connection) or die(mysql_error());

//get the number of rows in the result set
$num = mysql_num_rows($result);

//checks it see if that username already exists
if ($num != 0){
	$response[] = array('response'=>0);
	
}
else
{
	$response[] = array('response'=>1);

}
$data=json_encode($response);
echo $_GET['jsoncallback'] . '(' . $data . ');';
?>
