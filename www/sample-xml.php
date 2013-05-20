<?php

	$xdoc = new DomDocument;
  	$xdoc->Load('sample.xml');
	
	$xpath = new DOMXPath($xdoc);

	$query='//MyLinksCentral/Links/Link/title[.="SEO Miami"]';

	$entries=$xpath->query($query);

	foreach($entries as $entry)
	{
		echo $entry->nextSibling->nextSibling->nodeValue;
	}
 
        //$links = $xdoc->getElementsByTagName('Links')->item(0);
        //$newLinkElement = $xdoc ->createElement('Link');
        //$newTitleElement = $xdoc ->createElement('title');
       // $newURLElement = $xdoc ->createElement('url');
 
        //$urlNode = $xdoc ->createTextNode ('Google');
       // $titleNode = $xdoc ->createTextNode ('http://www.google.com');
 
       // $newTitleElement -> appendChild($titleNode);
      //  $newURLElement -> appendChild($urlNode);
 
        //$newLinkElement -> appendChild($newTitleElement);
       // $newLinkElement -> appendChild($newURLElement);
 
       // $links -> appendChild($newLinkElement);
 
	//$xdoc->save('sample.xml');
	//$wordnew=$xdoc ->createElement('word_title','time');

	//$links1 = $xdoc->getElementsByTagName('word_title');
	//$length=$links1->length;
	//echo $length;
	//for($i=0;$i<$length;$i++)
	//{
		//$child=$links1->item(0)->parentNode;
		//$child->appendChild($wordnew)
		//if($word)
		//echo $child;

	//}
	//echo $i;

	
	

?>
