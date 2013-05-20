<?php 
$tablename=$_GET['run'];
$title=$_GET['title'];

$con = mysql_connect("localhost","root","123");
if (!$con)
  {
  die('Could not connect: ' . mysql_error());
  }

mysql_select_db("matterhorn", $con);
$response = array();
switch($tablename)
{
	case "annotations":
	//echo "hi";
	$sql=mysql_query("select * from search where title like '%$title%' && table_name='$tablename'"); 
	while($row=mysql_fetch_array($sql)) 
	{ 
		$titleId=$row['id'];
		$media_id=$row['media_id'];
		$annotations=$row['title'];
		$start=$row['start'];
		$stop=$row['stop']; 
		$table_name=$row['table_name'];
		$response[] = array('id'=> $titleId, 'media_id'=> $media_id, 'title'=>$annotations, 'start'=>$start, 'stop'=>$stop, 'table_name'=>$table_name);
	} 
	break;
	case "Link":
	$sql=mysql_query("select * from search where (title like '%$title%' or description like '%$title%') && table_name='$tablename'");
	while($row=mysql_fetch_array($sql)) 
	{ 
		$titleId=$row['id'];
		$media_id=$row['media_id'];
		$title=$row['title'];
		$url=$row['url'];
		$description=$row['description'];
		$start=$row['start'];
		$stop=$row['stop']; 
		$table_name=$row['table_name'];
		$response[] = array('id'=> $titleId, 'media_id'=> $media_id, 'title'=>$title, 'description'=>$description, 'start'=>$start, 'stop'=>$stop, 'url'=>$url, 'table_name'=>$table_name);
	}  
	break;
	case "bookmarks":
	$sql=mysql_query("select * from search where (title like '%$title%' or description like '%$title%') && table_name='$tablename'"); 
	while($row=mysql_fetch_array($sql)) 
	{ 
		$titleId=$row['id'];
		$media_id=$row['media_id'];
		$title=$row['title'];
		$start=$row['start'];
		$stop=$row['stop'];
		$description=$row['description']; 
		$table_name=$row['table_name'];
		$response[] = array('id'=> $titleId, 'media_id'=> $playlist_name, 'description'=>$description, 'title'=>$playlist_url, 'start'=>$start, 'stop'=>$stop, 'table_name'=>$table_name);
	} 
	break;
	case "all":
	$sql=mysql_query("select * from search where title like '%$title%' or description like '%$title%'");
	while($row=mysql_fetch_array($sql)) 
	{ 
		$titleId=$row['id'];
		$media_id=$row['media_id'];
		$title=$row['title'];
		$start=$row['start'];
		$stop=$row['stop'];
		$description=$row['description']; 
		$table_name=$row['table_name'];
		$url=$row['url'];
		$response[] = array('id'=> $titleId, 'media_id'=> $media_id, 'description'=>$description, 'title'=>$title, 'start'=>$start, 'stop'=>$stop, 'table_name'=>$table_name, 'url'=>$url);
	} 
	break;

}




$data=json_encode($response);

echo $_GET['jsoncallback'] . '(' . $data . ');';


?> 
