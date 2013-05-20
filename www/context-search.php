<?php

	$MyFile="ontology.txt";
	//$key="protocol";
	$key=$_GET['title'];
	$fh=fopen($MyFile, 'r');
	$data=fread($fh, filesize($MyFile));
	//echo $data;
	/*$contents=file_get_contents($MyFile);
	$blocks=array();
	while(!feof($MyFile))
	{
		$blocks[]=fgets($MyFile,4096);
	}
	
	print_r($blocks);*/
		
	$lines=file($MyFile);
	//echo $lines;
	$values=array();
	foreach($lines as $line)
	{
		list($k,$v)=explode('|',$line);
		if($k==$key)
		{
			$values=explode(', ',$v);
		}
		$i=0;		
		
	}
	//$arr=array("options"=>$values);
	$data=json_encode($values);
	echo $_GET['jsoncallback'] . '(' . $data . ');';

?>
