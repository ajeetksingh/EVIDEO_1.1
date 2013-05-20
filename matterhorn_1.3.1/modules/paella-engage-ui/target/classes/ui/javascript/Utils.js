var TimerManager = Class.create({
	timerArray:new Array(),
	lastId:0,

	setupTimer:function(timer,time) {
		this.lastId++;
		timer.timerId = this.lastId;
		timer.timeout = time;
		this.timerArray[this.lastId] = timer;
		timer.jsTimerId = setTimeout("timerManager.executeTimerCallback(" + this.lastId + ")",time);
	},

	executeTimerCallback:function(timerId) {
		var timer = this.timerArray[timerId];
		if (timer && timer.callback) {
			timer.callback(timer,timer.params);
		}
		if (timer.repeat) {
			timer.jsTimerId = setTimeout("timerManager.executeTimerCallback(" + timer.timerId + ")",timer.timeout);
		}
	}
});

var timerManager = new TimerManager();

var Timer = Class.create({
	timerId:0,
	callback:null,
	params:null,
	jsTimerId:0,
	repeat:false,
	timeout:0,
	
	initialize:function(callback,time,params) {
		this.callback = callback;
		this.params = params;
		timerManager.setupTimer(this,time);
	},
	
	cancel:function() {
		clearTimeout(this.jsTimerId);
	}
});
