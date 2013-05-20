<?php

	$word=$_GET['title'];

	//$word="algo";

	$fname="ontology.xml";

	$xml=new DomDocument;
	$xml->Load($fname);
	
	$xpath = new DOMXPath($xml);

	$query = '//RelatedTerms/words/word/word_title[.="'.$word.'"]';

		
	$entries=$xpath->query($query);
	
	//$words=new array();

	$words=array();
	$response=array();
	foreach($entries as $entry)
	{
		$child=$entry->parentNode;
		if($child->hasChildNodes())
		{
			$childNodes=$child->childNodes;
			foreach($childNodes as $childNode)
			{
				if($childNode->nodeType!=3||(($childNode->nodeType == 3)&&(strlen(trim($childNode->wholeText))>=1)))
				{
					if($childNode->nodeName=="word_title")
					{
						continue;
					}
					else
					{					
						$words[]=$childNode->nodeName;
						
					}
				}
			}
		}
		
		$words2=array_unique($words);
		
		foreach($words2 as $word)
		{
		
			$isaValues=$entry->parentNode->getElementsByTagName($word);
			
			foreach($isaValues as $value)
			{
				$sample=$value->getElementsByTagName($word.'_title');
				//echo $sample->item(0)->nodeValue."\n";
				$prp_text=$sample->item(0)->nodeValue;
				
				$sample=$value->getElementsByTagName('relevance');
				//echo $sample->item(0)->nodeValue."\n";
				$relevance=$sample->item(0)->nodeValue;
				
				$response[]=array('property'=>$word, 'property_text'=>$prp_text, 'relevance'=>$relevance);
			}

		}

	}

	$data=json_encode($response);
	//echo $data;
	echo $_GET['jsoncallback'] . '(' . $data . ');';
	
	

?>
