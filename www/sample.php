<?php

$q=new SplQueue();

$q->enqueue('1');
$q->enqueue('2');

class sample
{
	public $word;
	public function setWord($wordf)
	{
		$this->word="Time's uP";
		echo "Hi!";
	}
	
	public function getWord()
	{
		return $this->word;
	}

}

$c=new sample();
$wordt="Hi!";
$c->setWord($wordt);

echo $c->getWord();

?>
