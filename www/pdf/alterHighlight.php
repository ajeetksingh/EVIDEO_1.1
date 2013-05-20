<?php // alterHighlight.php
$callCode = $_GET['rCode'];
$message = "Error while performing action";

require_once 'login.php';
$db_server = mysql_connect($db_hostname, $db_username, $db_password);
if (!$db_server) die("Unable to connect to MySQL: " . mysql_error());

mysql_select_db($db_database)
or die("Unable to select database: " . mysql_error());

if($callCode == "alterH")
{
	$oldS=$_GET['oldInitial'];
	$oldF=$_GET['oldFinal'];
	$newS=$_GET['newInitial'];
	$newF=$_GET['newFinal'];
	
	$query = "UPDATE docsync SET index_initial=$newS, index_final=$newF WHERE index_initial=$oldS AND index_final=$oldF";
	mysql_query($query);
	$message = "Updation of highlighted text is completed";
}
else if($callCode == "alterT")
{
	$initialI=$_GET['sIndex'];
	$finalI=$_GET['fIndex'];
	$startTime=$_GET['sTime'];
	$endTime=$_GET['eTime'];
	
	$query = "UPDATE docsync SET startTime=$startTime, endTime=$endTime WHERE index_initial=$initialI AND index_final=$finalI";
	mysql_query($query);
	$message = "Updation of time related to highlighted text is completed";
}
else if($callCode == "remove")
{
	$initialI=$_GET['sIndex'];
	$finalI=$_GET['fIndex'];
	
	$query = "DELETE FROM docsync WHERE index_initial=$initialI AND index_final=$finalI";
	mysql_query($query);
	$message = "removal of highlight from text is completed";
}
?>
