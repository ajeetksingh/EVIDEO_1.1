<?php

// This code example is based on code from the Echove project.
// For the entire PHP SDK, please visit http://echove.net/

// Turn on error reporting during development
ini_set('error_reporting', E_ALL);
ini_set('display_errors', TRUE);

// Instantiate the Brightcove class
$bc = new Brightcove(
	'[[READ_TOKEN]]',
	'[[WRITE_TOKEN]]'
);

// Set the data for the new video DTO
$metaData = array(
	'name' => $_POST['bcName'],
	'shortDescription' => $_POST['bcShortDescription']
);

// Rename the file to its original file name (instead of temp names like "a445ertd3")
$file = $_FILES['bcVideo'];
rename($file['tmp_name'], '/tmp/' . $file['name']);
$file = '/tmp/' . $file['name'];

// Send the file to Brightcove
echo $bc->createVideo($file, $metaData);

class Brightcove
{
	$this->token_read = '';
	$this->token_write = '';
	$this->read_url = 'http://api.brightcove.com/services/library?';
	$this->write_url = 'http://api.brightcove.com/services/post';

	public function __construct($token_read, $token_write = NULL )
	{
		$this->token_read = $token_read;
		$this->token_write = $token_write;
	}

	public function createVideo($file = NULL, $meta)
	{
		$request = array();
		$post = array();
		$params = array();
		$video = array();

		foreach($meta as $key => $value)
		{
			$video[$key] = $value;
		}

		$params['token'] = $this->token_write;
		$params['video'] = $video;

		$post['method'] = 'create_video';
		$post['params'] = $params;

		$request['json'] = json_encode($post) . "\n";

		if($file)
		{
			$request['file'] = '@' . $file;
		}

		$curl = curl_init();
		curl_setopt($curl, CURLOPT_URL, $this->write_url);
		curl_setopt($curl, CURLOPT_POST, 1);
		curl_setopt($curl, CURLOPT_POSTFIELDS, $request);
		curl_setopt($curl, CURLOPT_RETURNTRANSFER, 1);
		curl_setopt($curl, CURLOPT_VERBOSE, TRUE );
		curl_setopt($curl, CURLOPT_CONNECTTIMEOUT, 300);
		curl_setopt($curl, CURLOPT_TIMEOUT, 300);
		$response = curl_exec($curl);
                print_r($response);
		curl_close($curl);

		$json = json_decode($response);

		// Check request error code and re-call createVideo if request
                // returned a 213 error. A 213 error occurs when you have 
                // exceeded your allowed number of concurrent write requests
		if(isset($json->error))
		{
			if($json->error->code == 213)
			{
				return $this->createVideo($file, $meta);
			} else {
				return FALSE;
			}
		} else {
			return $response;
		}
	}
}

?>

