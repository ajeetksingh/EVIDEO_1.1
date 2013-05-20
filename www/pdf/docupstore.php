<?php
// Open a MySQL connection
$link = mysql_connect('localhost', 'root', '123');
if(!$link) {
die('Connection failed: ' . mysql_error());
}

// Select the database to work with
$db = mysql_select_db('matterhorn');
if(!$db) {
die('Selected database unavailable: ' . mysql_error());
}
$pdfName = '"'.$_GET['pdf'].'"';
$mediaid = '"'.$_GET['id'].'"';
$videoName = '"'.$_GET['video'].'"';

//$query1 = "SELECT * from docsyncupload where id=$mediaid and video_name=$videoName and pdf_name=$pdfName";
//$result = mysql_query($query1);
	
//	if(!$result)
//	{
	$query = "INSERT INTO docsyncupload VALUES ($mediaid, $videoName, $pdfName)";
	mysql_query($query);
//	}

// Close the connection
mysql_close($link);
?>
