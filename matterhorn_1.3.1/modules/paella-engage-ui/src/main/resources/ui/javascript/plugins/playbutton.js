paella.plugins.PlayPauseButtonPlugin = Class.create(paella.PlaybackControlPlugin,{
	playId:'',
	pauseId:'',
	containerId:'',
	container:null,

	initialize:function($super) {
		$super();
		var thisClass = this;
	},

	getRootNode:function($super,id) {
		this.playId = id + '_playButton';
		this.pauseId = id + '_pauseButton';
		this.containerId = id + '_container';
		var playPauseContainer = new DomNode('div',this.containerId,{'float':'left'});
		this.container = playPauseContainer;

		var thisClass = this;
		playPauseContainer.addNode(new Button(this.playId,'playButton',function(event) { thisClass.playButtonClick(); },false));
		var pauseButton = new Button(this.pauseId,'pauseButton',function(event) { thisClass.pauseButtonClick(); },false);
		playPauseContainer.addNode(pauseButton);
		pauseButton.domElement.hide();
		
		document.observe(paella.events.endVideo,function(event) {
			thisClass.playButton().show();
			thisClass.pauseButton().hide();
		});
		
		document.observe(paella.events.play,function() {
			thisClass.onPlay();
		});
		document.observe(paella.events.pause,function() {
			thisClass.onPause();
		});

		return playPauseContainer;		
	},
	
	getWidth:function() {
		return 50;
	},

	playButton:function() {
		return this.container.getNode(this.playId);
	},

	pauseButton:function() {
		return this.container.getNode(this.pauseId);
	},
	
	playButtonClick:function() {
		this.playButton().hide();
		this.pauseButton().show();
		document.fire(paella.events.play);
	},

	pauseButtonClick:function() {
		this.playButton().show();
		this.pauseButton().hide();
		document.fire(paella.events.pause);
	},
	
	onPlay:function() {
		if (this.playButton()) {
			this.playButton().hide();
			this.pauseButton().show();			
		}
	},
	
	onPause:function() {
		if (this.playButton()) {
			this.playButton().show();
			this.pauseButton().hide();			
		}
	}
});

new paella.plugins.PlayPauseButtonPlugin();