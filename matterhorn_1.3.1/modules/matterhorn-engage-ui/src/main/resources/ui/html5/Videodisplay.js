var Videodisplay = Videodisplay || {};

/**
 * @namespace the global namespace Videodisplay
 */
Videodisplay = (function ()
{
  var mhPlayer;

  /**
     * @memberOf Videodisplay
     * @description play
     * @return false if something went wrong
     */
  function play()
  {
    $.log("HTML5 Videodisplay play"); 
    $(document).trigger(paella.events.play);
    return true;
  };

  /**
     * @memberOf Videodisplay
     * @description stop
     * @return false if something went wrong
     */
  function stop()
  {
    $.log("HTML5-TODO Videodisplay stop");
    return true;
  };

  /**
     * @memberOf Videodisplay
     * @description pause
     * @return false if something went wrong
     */
  function pause()
  {
    $.log("HTML5 Videodisplay pause");
    $(document).trigger(paella.events.pause);
    return true;
  };

  /**
     * @memberOf Videodisplay
     * @description skipBackward
     * @return false if something went wrong
     */
  function skipBackward()
  {
    $.log("HTML5-TODO Videodisplay skipBackward");
    return false;
  };

  /**
     * @memberOf Videodisplay
     * @description rewind
     * @return false if something went wrong
     */
  function rewind()
  {
    $.log("HTML5 Videodisplay rewind ");
    $(document).trigger(paella.events.seekTo,{relative:-30});
    return true;
  };

  /**
     * @memberOf Videodisplay
     * @description stopRewind
     * @return false if something went wrong
     */
  function stopRewind()
  {
    $.log("HTML5-TODO Videodisplay stopRewind");
    return false;
  };

  /**
     * @memberOf Videodisplay
     * @description fastForward
     * @return false if something went wrong
     */
  function fastForward()
  {
    $.log("HTML5 Videodisplay fastForward");
    $(document).trigger(paella.events.seekTo,{relative:30});
    return true;
  };

  /**
     * @memberOf Videodisplay
     * @description stopFastForward
     * @return false if something went wrong
     */
  function stopFastForward()
  {
    $.log("HTML5-TODO Videodisplay stopFastForward");
    return false;
  };

  /**
     * @memberOf Videodisplay
     * @description skipForward
     * @return false if something went wrong
     */
  function skipForward()
  {
    $.log("HTML5-TODO Videodisplay skipForward");
    return false;
  };

  /**
     * @memberOf Videodisplay
     * @description passCharCode
     * @param argInt
     * @return false if something went wrong
     */
  function passCharCode(argInt)
  {
    $.log("HTML5 Videodisplay passCharCode : " + argInt);

    if (argInt == 80 || argInt == 112) // P or p (Play or pause the video)
    {
      Opencast.Player.doTogglePlayPause();
    }
    if (argInt == 83 || argInt == 115) // S or s  (stop the video)
    {
      $(document).trigger(paella.events.pause);
      $(document).trigger(paella.events.seekTo,{second:0});
    }
    if (argInt == 77 || argInt == 109) // M or m (Mute the video)
    {
      Opencast.Player.doToggleMute();
    }
    if (argInt == 85 || argInt == 117) // U or u (Volume up)
    {
      $(document).trigger(paella.events.incVolume);
    }
    if (argInt == 68 || argInt == 100) // D or d (Volume down)
    {
      $(document).trigger(paella.events.decVolume);
    }
    if (argInt == 48) // 0 (Seek 0)
    {
      $(document).trigger(paella.events.seekTo,{percent:0.1}); // 0 se avalua como false
    }
    if (argInt == 49) // 1 (Seek 1)
    {
      $(document).trigger(paella.events.seekTo,{percent:10});
    }
    if (argInt == 50) // 2 (Seek 2)
    {
      $(document).trigger(paella.events.seekTo,{percent:20});
    }
    if (argInt == 51) // 3 (Seek 3)
    {
      $(document).trigger(paella.events.seekTo,{percent:30});
    }
    if (argInt == 52) // 4  (Seek 4)
    {
      $(document).trigger(paella.events.seekTo,{percent:40});
    }
    if (argInt == 53) // 5 (Seek 5)
    {
      $(document).trigger(paella.events.seekTo,{percent:50});
    }
    if (argInt == 54) // 6  ( Seek 6)
    {
      $(document).trigger(paella.events.seekTo,{percent:60});
    }
    if (argInt == 55) // 7 (Seek 7)
    {
      $(document).trigger(paella.events.seekTo,{percent:70});
    }
    if (argInt == 56) // 8 (Seek 8)
    {
      $(document).trigger(paella.events.seekTo,{percent:80});
    }
    if (argInt == 57) // 9 (Seek 9)
    {
      $(document).trigger(paella.events.seekTo,{percent:90});
    }
    if (argInt == 67 || argInt == 99) // C or c  (Closed Caption)
    {
            // TODO
            //Swiz.dispatchEvent(new VideoControlEvent(VideoControlEvent.CLOSEDCAPTIONS));
	
	
    }
    if (argInt == 82 || argInt == 114) // R or r (rewind)
    {
        $(document).trigger(paella.events.seekTo,{relative:-30});
    }
    if (argInt == 70 || argInt == 102) // F or f (Fast forward)
    {
        $(document).trigger(paella.events.seekTo,{relative:30});
    }
    if (argInt == 84 || argInt == 116) // T or t (time)
    {
        $(document).trigger(paella.events.pause);    
    }
    if (argInt == 73 || argInt == 105) // I or i (Information)
    {
    	Opencast.Player.doToggleShortcuts(undefined, 'Videodisplay');
    }

    return false;
  };
    
  /**
     * @memberOf Videodisplay
     * @description seek
     *              Note: pause()/resume() bug red5
     * @param argNumber
     * @return false if seek failed
     */
  function seek(argNumber)
  {
    $.log("HTML Videodisplay seek");
    $(document).trigger(paella.events.seekTo,{second:argNumber});
    return true;
  };

  /**
     * @memberOf Videodisplay
     * @description mute
     * @return false if something went wrong
     */
  function mute()
  {
    $.log("HTML5 Videodisplay mute");
    $(document).trigger(paella.events.setVolume, {volume: 0});
    return true;
  };

  /**
     * @memberOf Videodisplay
     * @description setVolumeSlider
     * @param argNumber
     * @return false if something went wrong
     */
  function setVolumeSlider(argNumber)
  {
    $.log("HTML5-TODO Videodisplay setVolumeSlider");
    return false;
  };

  /**
     * @memberOf Videodisplay
     * @description setVolumePlayer
     * @param argNumber
     * @return false if something went wrong
     */
  function setVolumePlayer(argNumber)
  {
    $.log("HTML5 Videodisplay setVolumePlayer");
    $(document).trigger(paella.events.setVolume, {volume: argNumber});
    return false;
  };

  /**
     * @memberOf Videodisplay
     * @description closedCaptions
     * @param argNumber
     * @return false if something went wrong
     */
  function closedCaptions()
  {
    $.log("HTML5-TODO Videodisplay closedCaptions");
    return false;
  };

  /**
     * @memberOf Videodisplay
     * @description setMediaURL
     * @param argCoverOne
     * @param argCoverTwo
     * @param argStringOne
     * @param argStringTwo
     * @param argMimetypeOne
     * @param argMimetypeTwo
     * @param argPlayerstyle
     * @param slideLength
     * @return false if something went wrong
     */
  function setMediaURL(argCoverOne, argCoverTwo, argStringOne, argStringTwo, argMimetypeOne, argMimetypeTwo, argPlayerstyle, slideLength)
  {
    $.log("HTML5 Videodisplay setMediaURL");
    mhPlayer = new paella.MatterhornPlayer('oc_html-player');
    mhPlayer.setupVideos({src:argStringOne, type:argMimetypeOne});
    return true;
  };

  /**
     * @memberOf Videodisplay
     * @description setCaptionsURL
     * @param argString
     * @return false if something went wrong
     */
  function setCaptionsURL(argString)
  {
    $.log("HTML5-TODO Videodisplay setCaptionsURL");
    return false;
  };

  /**
     * @memberOf Videodisplay
     * @description videoSizeControl
     * @param argSizeLeft
     * @param argSizeRight
     * @return false if something went wrong
     */
  function videoSizeControl(argSizeLeft, argSizeRight)
  {
    $.log("HTML5 Videodisplay videoSizeControl");
    $.log(argSizeLeft, argSizeRight);
    mhPlayer.setMhComposition(argSizeLeft, argSizeRight);
    
    return true;
  };

  /**
     * @memberOf Videodisplay
     * @description getViewState
     * @return false if something went wrong
     */
  function getViewState()
  {
    $.log("HTML5-TODO Videodisplay getViewState");
    return false;
  };

  /**
     * @memberOf Videodisplay
     * @description getViewState
     * @param argWidthMediaOne
     * @param argHeightMediaOne
     * @param argWidthMediaTwo
     * @param argHeightMediaTwo
     * @param argMultiMediaContainerLeft
     * @return false if something went wrong
     */
  function setMediaResolution(argWidthMediaOne, argHeightMediaOne, argWidthMediaTwo, argHeightMediaTwo, argMultiMediaContainerLeft)
  {
    $.log("HTML5-TODO Videodisplay setMediaResolution");
    $.log(argWidthMediaOne, argHeightMediaOne, argWidthMediaTwo, argHeightMediaTwo, argMultiMediaContainerLeft);
    return false;
  };
    
  return {
    play: play,
    stop: stop,
    pause: pause,
    skipBackward: skipBackward,
    rewind: rewind,
    stopRewind: stopRewind,
    fastForward: fastForward,
    stopFastForward: stopFastForward,
    skipForward: skipForward,
    passCharCode: passCharCode,
    seek: seek,
    mute: mute,
    setVolumeSlider: setVolumeSlider,
    setVolumePlayer: setVolumePlayer,
    closedCaptions: closedCaptions,
    setMediaURL: setMediaURL,
    setCaptionsURL: setCaptionsURL,
    videoSizeControl: videoSizeControl,
    getViewState: getViewState,
    setMediaResolution: setMediaResolution
  };
}());
