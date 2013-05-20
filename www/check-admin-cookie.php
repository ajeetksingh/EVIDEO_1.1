<?php

	$file=fopen("play/admin-cookie.fxyz","r");
	$read_file=fread($file, 5);
	if($read_file=="admin")
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

?>
