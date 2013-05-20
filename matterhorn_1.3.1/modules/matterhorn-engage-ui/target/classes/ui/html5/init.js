(function (){

  function supports_video() {
      return !!document.createElement('video').canPlayType;
  }
  function supports_h264_baseline_video() {
      var v = document.createElement("video");
      return v.canPlayType('video/mp4; codecs="avc1.42E01E, mp4a.40.2"');
  }
 
  var alternateContent = 'Paella Player requires browsers supporting both the HTML5 &lt;video&gt; tag and the h.264 video codec.';
  var player = $("#oc_html-player");
  player.css({"text-align": "center", "height": "50px"});
  if (!supports_video()) {
      player.html(alternateContent);
      $("#oc_flash-player-loading").show();
      $('#initializing').html("HTML5 video not compatible");
      $('#loading-init').hide();
  } else if (!supports_h264_baseline_video()) {
      player.html(alternateContent);
      $("#oc_flash-player-loading").show();
      $('#initializing').html("H.264 video codec not compatible");
      $('#loading-init').hide();
  }else{
      Opencast.Watch.onPlayerReady();
  }
})();