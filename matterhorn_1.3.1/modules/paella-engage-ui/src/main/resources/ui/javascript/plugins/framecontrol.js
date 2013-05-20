paella.plugins.FrameControlPlugin = Class.create(paella.PlaybackPopUpPlugin,{
	framesControl:null,
	container:null,
	button:null,

	getRootNode:function($super,id) {
		var thisClass = this;
		this.container = new DomNode('div',id + '_frameControl_container');
		this.button = this.container.addNode(new Button(id + '_frameControl_button','showFramesButton',function(event) { thisClass.showFramesPress(); },true));
		return this.container;
	},
	
	getWidth:function() {
		return 45;
	},
	
	getPopUpContent:function($super,id) {
		var thisClass = this;
		this.framesControl = new FramesControl(id + '_frameContol_frames');
		document.observe(paella.events.seekToFrame,function(event){
			if (thisClass.showFramesButton().isToggled()) {
				thisClass.showFramesButton().toggleIcon();
			}
		});
		return this.framesControl;
	},

	showFramesButton:function() {
		return this.button;
	},
	
	showFramesPress:function() {
		if (this.showFramesButton().isToggled()) {
			this.framesControl.show();
		}
		else {
			this.framesControl.hide();
		}
	}
});

new paella.plugins.FrameControlPlugin();