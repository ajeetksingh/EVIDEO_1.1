paella.plugins.SocialPlugin = Class.create(paella.PlaybackPopUpPlugin,{
	socialContainer:null,
	container:null,
	button:null,

	getRootNode:function($super,id) {
		var thisClass = this;
		this.button = new Button(id + '_social_button','showSocialButton',function(event) { thisClass.showSocialPress(); },true);
		return this.button;
	},
	
	getWidth:function() {
		return 45;
	},
	
	getPopUpContent:function($super,id) {
		var thisClass = this;
		this.socialContainer = new DomNode('div',id + '_social_container',{display:'none'});
		this.socialContainer.addNode(new Button(id + '_social_facebook_button','socialButtonFacebook',function(event) { thisClass.facebookPress(); }));
		this.socialContainer.addNode(new Button(id + '_social_twitter_button','socialButtonTwitter',function(event) { thisClass.twitterPress(); }));
		return this.socialContainer;
	},

	showSocialButton:function() {
		return this.button;
	},
	
	showSocialPress:function() {
		if (this.showSocialButton().isToggled()) {
			this.socialContainer.domElement.show();
		}
		else {
			this.socialContainer.domElement.hide();
		}
	},
	
	facebookPress:function() {
		var url = this.getVideoUrl()
		window.open('http://www.facebook.com/sharer.php?u=' + url);
	},
	
	twitterPress:function() {
		var url = this.getVideoUrl();
		window.open('http://twitter.com/home?status=' + url);
	},
	
	getVideoUrl:function() {
		var url = "http://polimedia.upv.es/visor/?id=";
		url += paella.utils.parameters.get('id');
		return url;
	}
});

new paella.plugins.SocialPlugin();