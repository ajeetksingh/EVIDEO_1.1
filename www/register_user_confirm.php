<?php

$connection = @mysql_connect("localhost", "root", "123") or die(mysql_error());
$db = @mysql_select_db("matterhorn",$connection)or die(mysql_error());
$uname=$_GET['uname'];
//$uname="udit";
$role="ROLE_USER";
$organ="mh_default_org";
$pass=$_GET['pass'];
//$pass="roy";
//make query to database
$sql ="insert into MH_USER(USERNAME, ORGANIZATION, PASSWORD) values('$uname', '$organ', md5('$pass"."{".$uname."}'))";
$sql1 ="insert into MH_ROLE(USERNAME, ROLE) values('$uname', '$role')";

$response=array();
$result = @mysql_query($sql,$connection) or die(mysql_error());
$result1 = @mysql_query($sql1,$connection) or die(mysql_error());

if($result&&$result1)
{
	$response[] = array('response'=>1);
	$data=json_encode($response);
}
else
{
	$response[] = array('response'=>0);
	$data=json_encode($response);
}

echo $_GET['jsoncallback'] . '(' . $data . ');';

//get the number of rows in the result set
//$num = mysql_num_rows($result);

//checks it see if that username already exists
?>
