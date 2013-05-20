var ProfileItemButton = Class.create(DomNode,{
	viewModePlugin:null,

	initialize:function($super,icon,profileName,viewModePlugin) {
		$super('div',profileName + '_button',{display:'block',backgroundImage:'url(' + icon + ')',width:'78px',height:'41px'});
		this.viewModePlugin = viewModePlugin;

		var thisClass = this;
		this.domElement.observe('click',function(event) {
			var currentProfileName = paellaPlayer.selectedProfile;
			if (profileName!=currentProfileName) {
				var currentButtonId = currentProfileName + '_button';
				var currentButton = $(currentButtonId);
				currentButton.style.backgroundPosition = "0px 0px";
				var newButtonId = profileName + '_button';
				var newButton = $(newButtonId);
				newButton.style.backgroundPosition = "-78px 0px";
//				paellaPlayer.setProfile(profileName);
				document.fire(paella.events.setProfile,{profileName:profileName});
				if (thisClass.viewModePlugin) {
					thisClass.viewModePlugin.viewModeContainer.domElement.hide();
					thisClass.viewModePlugin.button.toggle();
				}
			}
		});
	}
});

paella.plugins.ViewModePlugin = Class.create(paella.PlaybackPopUpPlugin,{
	viewModeContainer:'',
	button:'',

	getRootNode:function($super,id) {
		var thisClass = this;
		this.button = new Button(id + '_view_mode_button','showViewModeButton',function(event) { thisClass.viewModePress(); },true);
		return this.button;
	},
	
	getWidth:function() {
		return 45;
	},
	
	getPopUpContent:function($super,id) {
		var thisClass = this;
		this.viewModeContainer = new DomNode('div',id + '_viewmode_container',{display:'none'});
		paella.Profiles.loadProfileList(function(profiles) {
			for (var profile in profiles) {
				var profileData = profiles[profile];
				var imageUrl = 'config/profiles/resources/' + profileData.icon;
				thisClass.viewModeContainer.addNode(new ProfileItemButton(imageUrl,profile,thisClass));

				// Profile icon preload
				var image = new Image();
				image.src = imageUrl;
			}
		});
		return this.viewModeContainer;
	},
	
	viewModePress:function() {
		if (this.button.isToggled()) {
			this.viewModeContainer.domElement.show();
		}
		else {
			this.viewModeContainer.domElement.hide();
		}
	}
});

new paella.plugins.ViewModePlugin();