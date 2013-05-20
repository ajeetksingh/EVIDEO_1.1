var paella = {};

(function(){
    var initializing = false, fnTest = /xyz/.test(function(){xyz;}) ? /\b_super\b/ : /.*/;
    this.Class = function(){};
    
    Class.extend = function(prop) {
	var _super = this.prototype;
	
	initializing = true;
	var prototype = new this();
	initializing = false;
	
	for (var name in prop) {
	    prototype[name] = typeof prop[name] == "function" && 
		typeof _super[name] == "function" && fnTest.test(prop[name]) ?
		(function(name, fn){
		    return function() {
			var tmp = this._super;
			
			this._super = _super[name];

			var ret = fn.apply(this, arguments);        
			this._super = tmp;
			
			return ret;
		    };
		})(name, prop[name]) :
            prop[name];
	}
	
	function Class() {
	    if ( !initializing && this.initialize )
		this.initialize.apply(this, arguments);
	}
	
	Class.prototype = prototype;
	Class.prototype.constructor = Class;
	Class.extend = arguments.callee;
	
	return Class;
    };
})();

paella.TimerManager = Class.extend({
    timerArray:new Array(),
    lastId:0,

    setupTimer:function(timer,time) {
	this.lastId++;
	timer.timerId = this.lastId;
	timer.timeout = time;
	this.timerArray[this.lastId] = timer;
	timer.jsTimerId = setTimeout("paella.timerManager.executeTimerCallback(" + this.lastId + ")",time);
    },

    executeTimerCallback:function(timerId) {
	var timer = this.timerArray[timerId];
	if (timer && timer.callback) {
	    timer.callback(timer,timer.params);
	}
	if (timer.repeat) {
	    timer.jsTimerId = setTimeout("paella.timerManager.executeTimerCallback(" + timer.timerId + ")",timer.timeout);
	}
    }
});

paella.timerManager = new paella.TimerManager();

paella.Timer = Class.extend({
    timerId:0,
    callback:null,
    params:null,
    jsTimerId:0,
    repeat:false,
    timeout:0,
    
    initialize:function(callback,time,params) {
	this.callback = callback;
	this.params = params;
	paella.timerManager.setupTimer(this,time);
    },
    
    cancel:function() {
	clearTimeout(this.jsTimerId);
    }
});

paella.settings = {
    presenterVideo:{
	aspectRatio:16/9
    },
    compositions:{
//	slidePresenter:{slide:'left',presenter:'right'},
//	presenterSlide:{slide:'right',presenter:'left'},
//	slideOnly:{slide:'center',presenter:'none'},
//	presenterOnly:{slide:'none',presenter:'center'}
    }
};

paella.events = {
    play:'mhplay',
    playpause:'mhplaypause',
    pause:'mhpause',
    seekTo:'mhseekto',
    setVolume:'mhsetvolume',
    incVolume:'mhincvolume',
    decVolume:'mhdecvolume',
    timeupdate:'mhtimeupdate',
    seeking:'mhseeking',
    seeked:'mhseeked',
    setPlaybackRate:'mhsetplaybackrate',
    fullscreen: 'mhfullscreen'
};

paella.Node = Class.extend({
    identifier:'',
    nodeList:null,
    
    initialize:function(id) {
	this.nodeList = new Array();
	this.identifier = id;
    },
    
    addTo:function(parentNode) {
	parentNode.addNode(this);
    },
    
    addNode:function(childNode) {
	this.nodeList[childNode.identifier] = childNode;
	return childNode;
    },
    
    getNode:function(id) {
	return this.nodeList[id];
    }
});

paella.DomNode = paella.Node.extend({
    domElement:null,
    
    initialize:function(elementType,id,style,className) {
	this._super(id);
	this.domElement = document.createElement(elementType);
	this.domElement.id = id;
	this.domElement.className = className || "";
	$(this.domElement).css(style);
    },
    
    addNode:function(childNode) {
	var returnValue = this._super(childNode);
	this.domElement.appendChild(childNode.domElement);
	return returnValue;
    },

    onresize:function() {
    }
});

paella.Html5Video = paella.DomNode.extend({
    classNameBackup:'',

    initialize:function(id,style) {
	this._super('video',id,style);
    },
    
    paused:function() {
	if (this.domElement) {
	    return this.domElement.paused;
	}
	return true;
    },

    play:function() {
	if (this.domElement && this.domElement.play) {
	    this.domElement.play();
	}
    },
    
    pause:function() {
	if (this.domElement && this.domElement.pause) {
	    this.domElement.pause();
	}
    },
    
    duration:function() {
	if (this.domElement && this.domElement.duration) {
	    return this.domElement.duration;
	}
	return 0;
    },

    setCurrentTime:function(time) {
	//if (this.domElement && this.domElement.currentTime) { if currentTime is 0 
	if (this.domElement) {
	    this.domElement.currentTime = time;
	}
    },

    currentTime:function() {
	if (this.domElement && this.domElement.currentTime) {
	    return this.domElement.currentTime;
	}
	return 0;
    },
    
    setPlaybackRate:function(rate) {
	this.domElement.playbackRate && (this.domElement.playbackRate = rate);
    },
    
    playbackRate:function() {
	return this.domElement.playbackRate;
    },
    
    mute:function() {
	this.setVolume(0);
    },

    setVolume:function(volume) {
	if (volume==0) this.domElement.muted = true;
	else this.domElement.muted = false;
	this.domElement.volume = volume;
    },

    incVolume:function() {
	if (this.domElement.volume==0) this.domElement.muted = false;
	if (this.domElement.volume<0.95)  this.domElement.volume += 0.05;
	else this.domElement.volume = 1;
	
    },

    decVolume:function() {
	if (this.domElement.volume > 0.05)  this.domElement.volume -= 0.05;
	else {
	    this.domElement.volume = 0;
            this.domElement.muted = true;
        }
    },
    
    volume:function() {
	return this.domElement.volume;
    },
    
    addSource:function(sourceData) {
	var source = document.createElement('source');
	//Element.extend(source);
	source.src = sourceData.src;
	source.type = sourceData.type;
       if (source.type == "video/x-flv")
       {
               source.type = "video/mp4";
               var n = source.src.replace("flv","mp4");
               source.src = n;
       }
	
	if(source)
	this.domElement.appendChild(source);
    },

    setRect:function(rect,animate) {
	var style = {'max-width': rect['max-width'] || rect.width, position:'absolute'};
	if (rect.left) { style['left'] = rect.left }
	if (rect.right) { style['right'] = rect.right }

        //Error jQuery animate CSS width to auto
        if (rect.width) {
            style.width = rect.width;
        } else {
	    $(this.domElement).css('width', 'auto');
        }

	if (animate) {
	    $(this.domElement).animate(style,300);
	}
	else {
	    $(this.domElement).css(style);
	}
    },
    
    setVisible:function(visible,animate) {
	if (visible=="true" && animate) {
	    $(this.domElement).fadeIn(300);
	}
	else if (visible=="true" && !animate) {
	    $(this.domElement).show();
	}
	else if (visible=="false" && animate) {
	    $(this.domElement).fadeOut(300);
	}
	else if (visible=="false" && !animate) {
	    $(this.domElement).hide();
	}
    }
});

paella.MatterhornPlayer = paella.DomNode.extend({
    parent:null,
    composition:null,
    //sliderVideoId:'',
    presenterVideoId:'',
    isfullscreen: false,

    initialize:function(parentId) {
	var parent = $('#' + parentId);
	var style = {width:'100%',height:'100%',backgroundColor:'black',position:'relative'};
	var id = parent.id + '_player_container';
	this._super('div',id,style,'');
	if (parent) {
	    //this.slideVideoId = 'paella_matterhornPlayer_slideVideo';
	    this.presenterVideoId = 'paella_matterhornPlayer_presenterVideo';
	    parent = parent[0];
	    parent.appendChild(this.domElement);
	    this.parent = parent;
	    paella.matterhornPlayer = this;
	    var thisClass = this;
	    this.initPlayAndFullscreenButtons();
	    $(document).bind(paella.events.play,function(event) { thisClass.play(); });
	    $(document).bind(paella.events.pause,function(event) { thisClass.pause(); });
	    $(document).bind(paella.events.playpause,function(event) { thisClass.playpause(); });
	    $(document).bind(paella.events.seekTo,function(event,params) { thisClass.seekTo(params); });
	    $(document).bind(paella.events.setVolume,function(event,params) { thisClass.setVolume(params); });
	    $(document).bind(paella.events.incVolume,function(event) { thisClass.incVolume(); });
	    $(document).bind(paella.events.decVolume,function(event) { thisClass.decVolume(); });
	    $(document).bind(paella.events.setPlaybackRate,function(event,params) { thisClass.setPlaybackRate(params.playbackRate)});
	    $(document).bind(paella.events.fullscreen,function(event) { thisClass.fullscreen()});
	    $(window).bind('resize',function() { thisClass.onResize(); });
	    new paella.Timer(function(timer) {
		thisClass.syncVideos();
		timer.repeat = true;
	    },2000);
	    this.onResize();
	}
    },
    
    initPlayAndFullscreenButtons:function() {
        var thisClass = this;
        var zDiv = this.addNode(new paella.DomNode('div', 'zId', 
						   {width: '100%', height: '100%', 'z-index': '2', cursor: 'pointer',
						    background: 'transparent url("html5/img/play.png") no-repeat 50% 50%', position: 'absolute'}));
        $(zDiv.domElement).hover(
	    function () {$(this).css({'background-image': 'url("html5/img/play-hover.png")'})},
	    function () {$(this).css({'background-image': 'url("html5/img/play.png")'})}
	);
	$(zDiv.domElement).bind('click',function(event) {
            thisClass.play();
        });


        var fullScreenDiv = this.addNode(new paella.DomNode('div', 'fullScreenId', 
							    {width: '30px', height: '30px', top: '5px', right: '5px', 'z-index': '2', cursor: 'pointer', 
							     background: 'transparent url("html5/img/fullscreen.png") no-repeat 0% 0%', position: 'absolute'}));
        $(fullScreenDiv.domElement).hover(
	    function () {$(this).css({'background-image': 'url("html5/img/fullscreen-hover.png")'})},
	    function () {$(this).css({'background-image': 'url("html5/img/fullscreen.png")'})}
	);
	$(fullScreenDiv.domElement).bind('click',function(event) {
            thisClass.fullscreen();
        });
    },

    play:function() {
	//var slideVideo = this.getNode(this.slideVideoId);
	var presenterVideo = this.getNode(this.presenterVideoId);
	if (presenterVideo) {
	   // slideVideo.play();
	    presenterVideo.play();
	}
	$('#zId').hide();
    },
    
    pause:function() {
	//var slideVideo = this.getNode(this.slideVideoId);
	var presenterVideo = this.getNode(this.presenterVideoId);
	if (presenterVideo) {
	   //slideVideo.pause();
	    presenterVideo.pause();
	}
	$('#zId').show();
    },

    playpause:function() {
	//var slideVideo = this.getNode(this.slideVideoId);
	var presenterVideo = this.getNode(this.presenterVideoId);
	if (presenterVideo) {
	    if (presenterVideo.paused()) {
		//slideVideo.play();
		presenterVideo.play();
	        $('#zId').hide();
            } else {
		//slideVideo.pause();
		presenterVideo.pause();
	        $('#zId').show();
            }
	}
    },

    fullscreen:function() {
        var docElm = this.parent;
        if (this.isfullscreen) {
            if (document.exitFullscreen) {
                document.exitFullscreen();
            } else if (document.mozCancelFullScreen) {
                document.mozCancelFullScreen();
            } else if (document.webkitCancelFullScreen) {
                document.webkitCancelFullScreen();
            }
    	    this.isfullscreen =  false;
        } else{
            if (docElm.requestFullscreen) {
    	        docElm.requestFullscreen();
    	    } else if (docElm.mozRequestFullScreen) {
    	        docElm.mozRequestFullScreen();
    	    } else if (docElm.webkitRequestFullScreen) {
    	        docElm.webkitRequestFullScreen();
    	    }
    	    this.isfullscreen =  true;
        }
    },

    seekTo:function(params) {
	//var slideVideo = this.getNode(this.slideVideoId);
	var presenterVideo = this.getNode(this.presenterVideoId);
	if (presenterVideo) {
	    var second = params.second;
	    var duration = presenterVideo.duration();
	    if (params.percent) {
		second = params.percent * duration / 100;
	    }
	    if (params.relative) {
		second = presenterVideo.currentTime() + params.relative
	    }
	    if (second) {
//		slideVideo.setCurrentTime(second);
		presenterVideo.setCurrentTime(second);
	    }			
	}
    },
    
    setVolume:function(params) {
	var slideVideo = this.getNode(this.slideVideoId);
	var presenterVideo = this.getNode(this.presenterVideoId);
	if (slideVideo && presenterVideo) {
	    if (!params.video) {
		presenterVideo.setVolume(params.volume);
		slideVideo.mute();
	    }
	    else if (params.video=='presenter' || params.video=='professor') {
		presenterVideo.setVolume(params.volume);
	    }
	    else if (params.video=='slide' || params.video=='presentation' || params.video=='screen') {
		slideVideo.setVolume(params.volume);
	    }
	}
    },

    incVolume:function() {
	var slideVideo = this.getNode(this.slideVideoId);
	var presenterVideo = this.getNode(this.presenterVideoId);
	if (slideVideo && presenterVideo) {
  	    presenterVideo.incVolume();
	}
    },

    decVolume:function() {
	var slideVideo = this.getNode(this.slideVideoId);
	var presenterVideo = this.getNode(this.presenterVideoId);
	if (slideVideo && presenterVideo) {
  	    presenterVideo.decVolume();
	}
    },
    
    setMhComposition:function(argSizeLeft, argSizeRight) {
	var slideVideo = this.getNode(this.slideVideoId);
	var presenterVideo = this.getNode(this.presenterVideoId);
	if (slideVideo && presenterVideo) {
	    this.setVideoPositionRight(argSizeLeft + "-" + argSizeRight, slideVideo,paella.settings.slideVideo.aspectRatio);
	    this.setVideoPositionLeft(argSizeLeft + "-" + argSizeRight, presenterVideo,paella.settings.presenterVideo.aspectRatio);
	}
    },
    
    setPlaybackRate:function(playbackRate) {
	var slideVideo = this.getNode(this.slideVideoId);
	var presenterVideo = this.getNode(this.presenterVideoId);
	if (slideVideo && presenterVideo) {
	    slideVideo.setPlaybackRate(playbackRate);
	    presenterVideo.setPlaybackRate(playbackRate);
	}
	
    },
    
    syncVideos:function() {
	var slideVideo = this.getNode(this.presenterVideoId);
	var presenterVideo = this.getNode(this.presenterVideoId);
	if (slideVideo && presenterVideo && !presenterVideo.paused()) {
	    var slideTime = slideVideo.currentTime();
	    var presenterTime = presenterVideo.currentTime();
	    var diff = Math.abs(presenterTime - slideTime);
	    if (diff>0.5) {
		console.log('Sync videos: ' + slideTime + ' - ' + presenterTime + ' = ' + (slideTime - presenterTime));
		//slideVideo.setCurrentTime(presenterVideo.currentTime());
	    }
	}
    },
    
    setVideoPositionLeft:function(position,video,aspectRatio) {
	switch(position) {
	case '100-0':
	    video.setRect({right:'0%',width:'100%'},true);
	    video.setVisible(true,true);
	    break;
	case '100-50':
	    video.setRect({right:'50%','max-width':'50%'},true);
	    video.setVisible(true,true);
	    break;
	case '100-100':
	    video.setRect({right:'50%','max-width':'50%'},true);
	    video.setVisible(true,true);
	    break;
	case '50-100':
	    video.setRect({right:'50%','max-width':'30%'},true);
	    video.setVisible(true,true);
	    break;
	case '0-100':
	    video.setRect({right:'0%','max-width':'0%'},true);
	    video.setVisible(false,true);
	    break;
	}
    },

    setVideoPositionRight:function(position,video,aspectRatio) {
	switch(position) {
	case '100-0':
	    video.setRect({left:'0%','max-width':'0%'},true);
	    video.setVisible(false,true);
	    break;
	case '100-50':
	    video.setRect({left:'50%','max-width':'30%'},true);
	    video.setVisible(true,true);
	    break;
	case '100-100':
	    video.setRect({left:'50%','max-width':'50%'},true);
	    video.setVisible(true,true);
	    break;
	case '50-100':
	    video.setRect({left:'50%','max-width':'50%'},true);
	    video.setVisible(true,true);
	    break;
	case '0-100':
	    video.setRect({left:'0%',width:'100%'},true);
	    video.setVisible(true,true);
	    break;

	}
    },

    // video data = {src:'source.mp4',type:'video/mp4'}
    setupVideos:function(presenter) {
//	var rect = {left:'50%',bottom:'0%','max-width':'50%','max-height':'100%',position:'absolute'};
//	var slideVideo = this.addNode(new paella.Html5Video(this.slideVideoId,rect));
//	slideVideo.addSource(slide);
//	slideVideo.mute();
	rect = {'width':'100%','height':'100%',position:'inherit'};
	var presenterVideo = this.addNode(new paella.Html5Video(this.presenterVideoId,rect));
	presenterVideo.addSource(presenter);
	$(presenterVideo.domElement).bind('timeupdate',function(event) { $(document).trigger(paella.events.timeupdate,{currentTime:presenterVideo.currentTime()}); });
	$(presenterVideo.domElement).bind('seeking',function(event) { $(document).trigger(paella.events.seeking,{currentTime:presenterVideo.currentTime()}); });
	$(presenterVideo.domElement).bind('seeked',function(event) { $(document).trigger(paella.events.seeked,{currentTime:presenterVideo.currentTime()}); });
        
        
	this.mhCallbacks();
    },
    

    mhCallbacks:function() {
	var presenterVideo = this.getNode(this.presenterVideoId);
	if (presenterVideo) {
	    $(presenterVideo.domElement).bind('play', function(e) {
                console.log("HTML5 Player onplay event");
		Opencast.Player.setPlayPauseState("pausing");
	    }, false);
	    $(presenterVideo.domElement).bind('pause', function(e) {
                console.log("HTML5 Player onpause event");
		Opencast.Player.setPlayPauseState("playing");
	    }, false);
	    $(presenterVideo.domElement).bind('timeupdate', function(e) {
                console.log("HTML5 Player ontimeupdate event");
	        Opencast.Player.setPlayhead(e.target.currentTime);
	        var d = new Date(0, 0, 0, 0, 0, e.target.currentTime);
	        Opencast.Player.setCurrentTime(d.toLocaleTimeString());
	    }, false);
	    $(presenterVideo.domElement).bind('canplaythrough', function(e) {
                console.log("HTML5 Player oncanplaythrough event ");
                Opencast.Player.setProgress(100);  //TODO
		Opencast.Initialize.setPlayerReady(true);
	    }, false);


	}
    },

    onResize:function() {
	if (this.parent) {
            var height;
	    //var slideAspectRatio = paella.settings.slideVideo.aspectRatio;
	    var presenterAspectRatio = paella.settings.presenterVideo.aspectRatio;
	    var parent = $(this.parent);
	    var width = parent.width();

	    //Fixed Bug with embed player
            var loc = window.location.href;
	    if(loc.search(/embed.html.+/g) != -1){ 
    		var maxAspectRatio = Math.max(presenterAspectRatio, presenterAspectRatio);
   		height = width * 1/maxAspectRatio;
            }else{
                var totalAspectRatio = presenterAspectRatio + presenterAspectRatio;
   		height = width * 1/totalAspectRatio;
            }
	    
	    // to calculate the embed flash height
            var iFrameHeight = document.documentElement.clientHeight;
            var otherDivHeight = 250;
            var flashHeight = iFrameHeight - otherDivHeight;

	    parent.css({height:Math.max(height, flashHeight) + 'px'});
	}
    }
});

