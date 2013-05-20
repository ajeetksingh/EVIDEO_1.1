<?php

	$admin="admin";
	
	$file=fopen("play/admin-cookie.fxyz","w") or die("Can't create the Cookie file!");

	fwrite($file,$admin);

	fclose($file);

?>
