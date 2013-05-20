<?php // queryHighlight.php
$pdfn = $_GET['pname'];
$callCode = $_GET['cCode'];

require_once 'login.php';
$db_server = mysql_connect($db_hostname, $db_username, $db_password);
if (!$db_server) die("Unable to connect to MySQL: " . mysql_error());

mysql_select_db($db_database)
or die("Unable to select database: " . mysql_error());

	if($callCode == "highlight")
		{
		$page = $_GET['container'];
		$query = "SELECT index_initial, index_final, pagecontainer FROM docsync where pdfname=$pdfn AND pagecontainer=$page"; }
	
	else if($callCode == "click")
		{
		$pageC = $_GET['page'];
		$query = "SELECT index_initial, index_final, startTime FROM docsync where pdfname=".$pdfn." AND pagecontainer=".$pageC; }
	else if($callCode == "scroll")
		{
		$query = "SELECT * FROM docsync where pdfname=".$pdfn;}

$result = mysql_query($query);

if (!$result) die ("Database access failed: " . mysql_error());

$rows = mysql_num_rows($result);

for ($j = 0 ; $j < $rows ; ++$j)
{
	$row[$j] = mysql_fetch_assoc($result);
}

echo json_encode($row);
?>

