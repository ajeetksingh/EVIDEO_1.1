<?php // queryHighlight.php
$start_time = $_GET['stime'];
$media_id = $_GET['id'];

$db_server = mysql_connect("localhost","root","123");
if (!$db_server) die("Unable to connect to MySQL: " . mysql_error());

mysql_select_db("matterhorn", $db_server)
or die("Unable to select database: " . mysql_error());

$query = "SELECT annotations FROM annotations where media_id='".$media_id."' and start <=".$start_time." and stop >= ".$start_time;
	
$result = mysql_query($query);

if (!$result) die ("Database access failed: " . mysql_error());

$rows = mysql_num_rows($result);

for ($j = 0 ; $j < $rows ; ++$j)
{
	$row[$j] = mysql_fetch_assoc($result);
}

$data = json_encode($row);

echo $_GET['jsoncallback'] . '(' . $data . ');';

?>

