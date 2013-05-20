paella.plugins.RepeatButtonPlugin = Class.create(paella.PlaybackControlPlugin,{
	buttonId:'',
	button:null,
	
	getRootNode:function($super,id) {
		this.buttonId = id + '_repeat_button';
		var thisClass = this;
		this.button = new Button(this.buttonId,'repeatButton',function(event) { thisClass.repeatButtonClick() });
		return this.button;
	},
	
	getWidth:function() {
		return 50;
	},
	
	repeatButtonClick:function() {
		var currentTime = paellaPlayer.player.videoContainer().currentTime();
		var start = paellaPlayer.player.videoContainer().trimStart();
		var end = paellaPlayer.player.videoContainer().trimEnd();
		var duration = paellaPlayer.player.videoContainer().duration();
		if (paellaPlayer.player.videoContainer().trimEnabled()) {
			duration = end - start;			
		}
		currentTime = currentTime - start - 30;
		var seekTo = currentTime * 100 / duration;
		document.fire(paella.events.seekTo,{newPositionPercent:seekTo});
	}
});

new paella.plugins.RepeatButtonPlugin();