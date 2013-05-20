<?php

	$word_=$_GET['word'];
	//echo $word;
	$property=$_GET['property'];
	//$isa_="algorithm";
	//echo $property;
	$prp_text=$_GET['text'];
	//echo $prp_text;
	$relevance_=$_GET['relevance'];
	//echo $relevance;	
	$fname="ontology.xml";	
		
	$d=new DomDocument('1.0','UTF-8');
	
	//$RelatedTerms=$d->createElement('RelatedTerms');	

	if(file_exists($fname))
	{

		$xml=new DomDocument;
		$xml->Load($fname);
		
		$sample=$xml->getElementsByTagName('word_title');
		$nb=$sample->length;
		echo "$nb";
		for($i=0;$i<$nb;$i++)
		{
			$child=$sample->item($i)->nodeValue;
			echo $child;
			if($child==$word_)
			{
				if($property=="isa")
				{
					$node=$sample->item($i)->parentNode->nodeName;	
					echo "Hello!";			
					echo "$node";					
					$words1=$xml->getElementsByTagName($node)->childNodes(0)->nodeValue;
					echo $words1;
					//$newWordElement=$xml->createElement('word');
					//$newWordTitleElement=$xml->createElement('word_title',$word_);
					/*$newIsaElement=$xml->createElement('isa');
					$newIsaTitleElement=$xml->createElement('isa_title',$prp_text);
					$newRelevanceTitle=$xml->createElement('relevance',$relevance_);
					//$newWordElement->appendChild($newWordTitleElement);
					$newIsaElement->appendChild($newIsaTitleElement);
					$newIsaElement->appendChild($newRelevanceTitle);
					//$sample->item($i)->parentNode->appendChild($newIsaElement);
					$words1->appendChild($newIsaElement);
					$xml->save($fname);
					break;*/
				}
				/*else
				{
					$words=$xml->getElementsByTagName('words')->item($i);
	
					$newWordElement=$xml->createElement('word');
					$newWordTitleElement=$xml->createElement('word_title',$word_);
					$newIsaElement=$xml->createElement('isa');
					$newIsaTitleElement=$xml->createElement('isa_title',$prp_text);
					$newRelevanceTitle=$xml->createElement('relevance',$relevance_);
					
					$newWordElement->appendChild($newWordTitleElement);
					$newIsaElement->appendChild($newIsaTitleElement);
					$newIsaElement->appendChild($newRelevanceTitle);
					$newWordElement->appendChild($newIsaElement);
			
					$words->appendChild($newWordElement);
					
					$xml->save($fname);
				}*/
			}
			else
			{
				if($property=="isa")
				{
					$words=$xml->getElementsByTagName('words')->item(0);
	
					$newWordElement=$xml->createElement('word');
					$newWordTitleElement=$xml->createElement('word_title',$word_);
					$newIsaElement=$xml->createElement('isa');
					$newIsaTitleElement=$xml->createElement('isa_title',$prp_text);
					$newRelevanceTitle=$xml->createElement('relevance',$relevance_);
					
					$newWordElement->appendChild($newWordTitleElement);
					$newIsaElement->appendChild($newIsaTitleElement);
					$newIsaElement->appendChild($newRelevanceTitle);
					$newWordElement->appendChild($newIsaElement);
			
					$words->appendChild($newWordElement);
					
					$xml->save($fname);
				}
			}

		}
		
		
		/*elseif($property=="hasa")
		{
			
			$sample=$xml->getElementsByTagName('word_title');
			echo "Hi!";
			$nb=$sample->length;
			//echo $nb;			
			for($i=0;$i<$nb;$i++)
			{
				//echo "for";				
				$child=$sample->item($i)->nodeValue;
				echo $child;
				//echo $word_;
				if($child==$word_)
				{	
					$node=$sample->item($i)->parentNode->nodeName;	
					echo "Hello!";			
					echo "$node";					
					$words1=$xml->getElementsByTagName($node)->item(0);
					//$newWordElement=$xml->createElement('word');
					//$newWordTitleElement=$xml->createElement('word_title',$word_);
					$newIsaElement=$xml->createElement('hasa');
					$newIsaTitleElement=$xml->createElement('hasa_title',$prp_text);
					$newRelevanceTitle=$xml->createElement('relevance',$relevance_);
			
					//$newWordElement->appendChild($newWordTitleElement);
					$newIsaElement->appendChild($newIsaTitleElement);
					$newIsaElement->appendChild($newRelevanceTitle);
					//$sample->item($i)->parentNode->appendChild($newIsaElement);
					$words1->appendChild($newIsaElement);
					$xml->save($fname);
				}
				else
				{
					$words=$xml->getElementsByTagName('words')->item(0);
	
					$newWordElement=$xml->createElement('word');
					$newWordTitleElement=$xml->createElement('word_title',$word_);
					$newIsaElement=$xml->createElement('hasa');
					$newIsaTitleElement=$xml->createElement('hasa_title',$prp_text);
					$newRelevanceTitle=$xml->createElement('relevance',$relevance_);
					
					$newWordElement->appendChild($newWordTitleElement);
					$newIsaElement->appendChild($newIsaTitleElement);
					$newIsaElement->appendChild($newRelevanceTitle);
					$newWordElement->appendChild($newIsaElement);
			
					$words->appendChild($newWordElement);
					
					$xml->save($fname);
				}
			}

		}

		elseif($property=="akindof")
		{

			$sample=$xml->getElementsByTagName('word_title');
			echo "Hi!";
			$nb=$sample->length;
			//echo $nb;			
			for($i=0;$i<$nb;$i++)
			{
				//echo "for";				
				$child=$sample->item($i)->nodeValue;
				echo $child;
				//echo $word_;
				if($child==$word_)
				{	
					$node=$sample->item($i)->parentNode->nodeName;	
					echo "Hello!";			
					echo "$node";					
					$words1=$xml->getElementsByTagName($node)->item(0);
					//$newWordElement=$xml->createElement('word');
					//$newWordTitleElement=$xml->createElement('word_title',$word_);
					$newIsaElement=$xml->createElement('akindof');
					$newIsaTitleElement=$xml->createElement('akindof_title',$prp_text);
					$newRelevanceTitle=$xml->createElement('relevance',$relevance_);
			
					//$newWordElement->appendChild($newWordTitleElement);
					$newIsaElement->appendChild($newIsaTitleElement);
					$newIsaElement->appendChild($newRelevanceTitle);
					//$sample->item($i)->parentNode->appendChild($newIsaElement);
					$words1->appendChild($newIsaElement);
					$xml->save($fname);
				}
				else
				{
					$words=$xml->getElementsByTagName('words')->item(0);
	
					$newWordElement=$xml->createElement('word');
					$newWordTitleElement=$xml->createElement('word_title',$word_);
					$newIsaElement=$xml->createElement('akindof');
					$newIsaTitleElement=$xml->createElement('akindof_title',$prp_text);
					$newRelevanceTitle=$xml->createElement('relevance',$relevance_);
					
					$newWordElement->appendChild($newWordTitleElement);
					$newIsaElement->appendChild($newIsaTitleElement);
					$newIsaElement->appendChild($newRelevanceTitle);
					$newWordElement->appendChild($newIsaElement);
			
					$words->appendChild($newWordElement);
					
					$xml->save($fname);
				}
			}
		}
	
		else
		{

			$sample=$xml->getElementsByTagName('word_title');
			echo "Hi!";
			$nb=$sample->length;
			//echo $nb;			
			for($i=0;$i<$nb;$i++)
			{
				//echo "for";				
				$child=$sample->item($i)->nodeValue;
				echo $child;
				//echo $word_;
				if($child==$word_)
				{	
					$node=$sample->item($i)->parentNode->nodeName;	
					echo "Hello!";			
					echo "$node";					
					$words1=$xml->getElementsByTagName($node)->item(0);
					//$newWordElement=$xml->createElement('word');
					//$newWordTitleElement=$xml->createElement('word_title',$word_);
					$newIsaElement=$xml->createElement('apartof');
					$newIsaTitleElement=$xml->createElement('apartof_title',$prp_text);
					$newRelevanceTitle=$xml->createElement('relevance',$relevance_);
			
					//$newWordElement->appendChild($newWordTitleElement);
					$newIsaElement->appendChild($newIsaTitleElement);
					$newIsaElement->appendChild($newRelevanceTitle);
					//$sample->item($i)->parentNode->appendChild($newIsaElement);
					$words1->appendChild($newIsaElement);
					$xml->save($fname);
				}
				else
				{
					$words=$xml->getElementsByTagName('words')->item(0);
	
					$newWordElement=$xml->createElement('word');
					$newWordTitleElement=$xml->createElement('word_title',$word_);
					$newIsaElement=$xml->createElement('apartof');
					$newIsaTitleElement=$xml->createElement('apartof_title',$prp_text);
					$newRelevanceTitle=$xml->createElement('relevance',$relevance_);
					
					$newWordElement->appendChild($newWordTitleElement);
					$newIsaElement->appendChild($newIsaTitleElement);
					$newIsaElement->appendChild($newRelevanceTitle);
					$newWordElement->appendChild($newIsaElement);
			
					$words->appendChild($newWordElement);
					
					$xml->save($fname);
				}
			}

		}*/
		
	}
	
	else{echo "hello;";}

	/*else
	{	

	$word=$d->createElement('word');

	$word_title=$d->createElement('word_title',$word_);
	
	$isa=$d->createElement('isa');

	$title=$d->createElement('title',$isa_);

	$isa->appendChild($title);
	
	$relevance=$d->createElement('relevance',$relevance_);

	$isa->appendChild($relevance);
	
	$word->appendChild($word_title);	
	
	$word->appendChild($isa);
	
	$RelatedTerms->appendChild($word);
	
	$d->appendChild($RelatedTerms);

	$d->save("ontology.xml");
	
	}*/
?>
