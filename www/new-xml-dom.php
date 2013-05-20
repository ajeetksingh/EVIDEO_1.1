<?php

	$word_=$_GET['word'];
	//$word_="HTML";
	
	$property=$_GET['property'];
	//$property="isa";	
	
	$prp_text=$_GET['text'];
	//$prp_text="markup";	
	
	$relevance_=$_GET['relevance'];
	//$relevance="9";
	
	$fname="ontology.xml";	

	//var $i;

	$xml=new DomDocument;
	$xml->Load($fname);

	$xpath = new DOMXPath($xml);

	$query = '//RelatedTerms/words/word/word_title[.="'.$word_.'"]';

	$entries=$xpath->query($query);

	//$b=count($entries);
	//echo $b;

	$sample=$xml->getElementsByTagName('word_title');
	$nb=$sample->length;
      
	for($i=0;$i<$nb;$i=$i+1)
	{
		$child=$sample->item($i)->nodeValue;
		if($child==$word_)
		{
			//echo $i;
			break;
		}
	}

	$index=$i;	

	if($entries->length!=0)
	{	
		foreach($entries as $entry)
		{
			$wordElement=$xml->getElementsByTagName($entry->parentNode->nodeName)->item($index);
			
			if($property=="isa")
			{			
				$newIsaElement=$xml->createElement('isa');
				$newIsaTitleElement=$xml->createElement('isa_title',$prp_text);
				$newRelevanceTitle=$xml->createElement('relevance',$relevance_);
	
				$newIsaElement->appendChild($newIsaTitleElement);
				$newIsaElement->appendChild($newRelevanceTitle);
	
				//$newIsaTitleElement->appendChild($newIsaElement);
				$wordElement->appendChild($newIsaElement);
				$xml->save($fname);	
			}

			elseif($property=="hasa")
			{
				$newIsaElement=$xml->createElement('hasa');
				$newIsaTitleElement=$xml->createElement('hasa_title',$prp_text);
				$newRelevanceTitle=$xml->createElement('relevance',$relevance_);
	
				$newIsaElement->appendChild($newIsaTitleElement);
				$newIsaElement->appendChild($newRelevanceTitle);
	
				//$newIsaTitleElement->appendChild($newIsaElement);
				$wordElement->appendChild($newIsaElement);
				$xml->save($fname);
			}
					
			elseif($property=="akindof")
			{
				$newIsaElement=$xml->createElement('akindof');
				$newIsaTitleElement=$xml->createElement('akindof_title',$prp_text);
				$newRelevanceTitle=$xml->createElement('relevance',$relevance_);
	
				$newIsaElement->appendChild($newIsaTitleElement);
				$newIsaElement->appendChild($newRelevanceTitle);
	
				//$newIsaTitleElement->appendChild($newIsaElement);
				$wordElement->appendChild($newIsaElement);
				$xml->save($fname);
			}
	
			elseif($property=="partof")
			{
			
				$newIsaElement=$xml->createElement('partof');
				$newIsaTitleElement=$xml->createElement('partof_title',$prp_text);
				$newRelevanceTitle=$xml->createElement('relevance',$relevance_);
	
				$newIsaElement->appendChild($newIsaTitleElement);
				$newIsaElement->appendChild($newRelevanceTitle);
	
				//$newIsaTitleElement->appendChild($newIsaElement);
				$wordElement->appendChild($newIsaElement);
				$xml->save($fname);			

			}
			else
			{

				$newIsaElement=$xml->createElement($property);
				$newIsaTitleElement=$xml->createElement($property.'_title',$prp_text);
				$newRelevanceTitle=$xml->createElement('relevance',$relevance_);
	
				$newIsaElement->appendChild($newIsaTitleElement);
				$newIsaElement->appendChild($newRelevanceTitle);
	
				//$newIsaTitleElement->appendChild($newIsaElement);
				$wordElement->appendChild($newIsaElement);
				$xml->save($fname);			


			}	
		}
	}
	else
	{
		echo "In Here!";		
				
		$words=$xml->getElementsByTagName('words')->item(0);
		$newWordElement=$xml->createElement('word');
		$newWordTitleElement=$xml->createElement('word_title',$word_);
		
		if($property=="isa")
		{		
			$newIsaElement=$xml->createElement('isa');
			$newIsaTitleElement=$xml->createElement('isa_title',"$prp_text");
			$newRelevanceTitle=$xml->createElement('relevance',$relevance_);
						
			$newWordElement->appendChild($newWordTitleElement);
			$newIsaElement->appendChild($newIsaTitleElement);
			$newIsaElement->appendChild($newRelevanceTitle);
			$newWordElement->appendChild($newIsaElement);
			
			$words->appendChild($newWordElement);
		}
		
		elseif($property=="hasa")
		{
			$newIsaElement=$xml->createElement('hasa');
			$newIsaTitleElement=$xml->createElement('hasa_title',$prp_text);
			$newRelevanceTitle=$xml->createElement('relevance',$relevance_);
						
			$newWordElement->appendChild($newWordTitleElement);
			$newIsaElement->appendChild($newIsaTitleElement);
			$newIsaElement->appendChild($newRelevanceTitle);
			$newWordElement->appendChild($newIsaElement);
			
			$words->appendChild($newWordElement);	
		}

		elseif($property=="akindof")
		{
			$newIsaElement=$xml->createElement('akindof');
			$newIsaTitleElement=$xml->createElement('akindof_title',$prp_text);
			$newRelevanceTitle=$xml->createElement('relevance',$relevance_);
						
			$newWordElement->appendChild($newWordTitleElement);
			$newIsaElement->appendChild($newIsaTitleElement);
			$newIsaElement->appendChild($newRelevanceTitle);
			$newWordElement->appendChild($newIsaElement);
			
			$words->appendChild($newWordElement);
		}
		
		elseif($property=="partof")
		{
			$newIsaElement=$xml->createElement('apartof');
			$newIsaTitleElement=$xml->createElement('apartof_title',$prp_text);
			$newRelevanceTitle=$xml->createElement('relevance',$relevance_);
						
			$newWordElement->appendChild($newWordTitleElement);
			$newIsaElement->appendChild($newIsaTitleElement);
			$newIsaElement->appendChild($newRelevanceTitle);
			$newWordElement->appendChild($newIsaElement);
			
			$words->appendChild($newWordElement);
		}

		else
		{
			$newIsaElement=$xml->createElement($property);
			$newIsaTitleElement=$xml->createElement($property.'_title',$prp_text);
			$newRelevanceTitle=$xml->createElement('relevance',$relevance_);

			$newIsaElement->appendChild($newIsaTitleElement);
			$newIsaElement->appendChild($newRelevanceTitle);

			//$newIsaTitleElement->appendChild($newIsaElement);
			$wordElement->appendChild($newIsaElement);
			$xml->save($fname);			

		}	

		$xml->save($fname);
	}

?>
