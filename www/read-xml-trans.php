<?php

	$q=$_GET['title'];

	$fname="ontology.xml";

	$xml=new DomDocument();
	$xml->Load($fname);

	$response=array();

	class Word
	{
		public $word;
		public $relevance;
		
	}

	function getTransitiveWords($word, $xml_file)
	{
		// Declare Queue of WORD Objects

		$Tq=new SplQueue();

		// Declare Array of WORD Objects called ResultArray

		$Ra=array();
		$Ta=array();

		// Push $word into Queue

		$Tq->enqueue($word);

		// While loop start
		// Test condition Sizeof Queue
		
		while($Tq->count()!=0)
		{
		
			// Pop Word V from Queue
			$word_to=$Tq->dequeue();		
			// Call function to retrieve list of child nodes satisfying condition
			// Input: Object V, XML
			// Output: Array of Object {V'}
			
			$Ta=retrieveChildNodes($word_to, $xml_file);			
	
			// Push {V'} into ResultArray
		
			foreach($Ta as $Tao)
			{
				$Ra[]=$Tao;
				// Push {V'} into Queue
				$Tq->enqueue($Tao);
			}		

			
		}
					
		// While Ends here
		return $Ra;
		// Return ResultArray
	}
	
	// Function to retrieve list of child nodes
	// Input: Object X, XML
	// Output: Array of Objects {K'}
	
	function retrieveChildNodes($wordt, $xml)
	
	{	

		// Create array to store child nodes
		$va=array();
	
		// Read child nodes {Y'} from XML for that particular word, if it exists
		$xpath = new DOMXPath($xml);

		$query = '//RelatedTerms/words/word/word_title[.="'.$wordt->word.'"]';

		
		$entries=$xpath->query($query);
	
		// For each child node Y in {Y'}
		
		foreach($entries as $entry)
		{	
			// If(min(relevance(Y), relevance(X)) > Threshold )
			
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

					if(min($wordt->relevance, $relevance)>=5)
					{
						// Create new Word K with relevance as min(relevance(Y),relevance(X)) and K.word = Y.word
						$word_s=new Word();
						$word_s->word=$prp_text;
						$word_s->relevance=min($wordt->relevance, $relevance);
						// Add K to {K'}
						$va[]=$word_s;
					}
				}
			}
		
			// For loop end
		}	
		
		return $va;			
	}
	
	$init_word=new Word();

	$init_word->word=$q;
	$init_word->relevance=10;
		
	$resultArray=array();

	$resultArray=getTransitiveWords($init_word, $xml);

	foreach($resultArray as $result)
	{

		$response[]=array('property_text'=>$result->word, 'relevance'=>$result->relevance);
		
	}

	$data=json_encode($response);
	//echo $data;
	echo $_GET['jsoncallback'] . '(' . $data . ');';


	
?>
