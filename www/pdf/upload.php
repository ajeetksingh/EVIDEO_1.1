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

$pdffile = $_FILES["my_uploaded_file"]["name"];

if (($_FILES["my_uploaded_file"]["type"] == "application/pdf"))
  {
  if ($_FILES["my_uploaded_file"]["error"] > 0)
    {
    echo "Return Code: " . $_FILES["my_uploaded_file"]["error"] . "<br />";
    }
  else
    {
    
    if (file_exists($_FILES["my_uploaded_file"]["name"]))
      {
      echo $_FILES["my_uploaded_file"]["name"] . " already exists. ";
      }
    else
      {
      move_uploaded_file($_FILES["my_uploaded_file"]["tmp_name"], $_FILES["my_uploaded_file"]["name"]);
	
      }
    }
  }
else
  {
  echo "Invalid file";
  }
// Close the connection
mysql_close($link);
?>
