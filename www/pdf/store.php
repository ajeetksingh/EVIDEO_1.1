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
$status = TRUE;
$indexi = $_GET['indexI'];
$indexf = $_GET['indexF'];
$pagestr = $_GET['page'];
$pdfname = $_GET['pdf'];
$pageNo = $_GET['pagen'];
$stime = $_GET['ST'];
$etime = $_GET['ET'];

$query1 = "SELECT index_initial, index_final from docsync where pagecontainer=".$pagestr." AND pdfname=".$pdfname;
$result1 = mysql_query($query1);
$rows = mysql_num_rows($result1);
	for ($j = 0 ; $j < $rows ; ++$j)
	{
	$row = mysql_fetch_row($result1);
	$start[$j] = $row[0];
	$end[$j] = $row[1];	
		if(($indexi>=$start[$j] && $indexi<=$end[$j]) || ($indexf>=$start[$j] && $indexf<=$end[$j]))
		{
			$status = FALSE;
			echo "Highlight cannot be stored";
			break;	}
	}

if($status)
{
echo "Highlights and Video time are successfully stored";
$query2 = "INSERT INTO docsync VALUES ($indexi, $indexf, $pagestr, $pdfname, $pageNo, $stime, $etime)";
$result2 = mysql_query($query2);
}

// Close the connection
mysql_close($link);
?>
