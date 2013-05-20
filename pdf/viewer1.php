<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
    <head>
        <title>Simple pdf.js page viewer</title>
	<!-- JQUERY-UI STYLE SHEET AND SCRIPT -->
	<link rel="stylesheet" href="themes/base/jquery.ui.all.css">
	<script src="jquery-1.7.2.js"></script>
	<script src="ui/jquery.ui.core.js"></script>
	<script src="ui/jquery.ui.widget.js"></script>
	<script src="ui/jquery.ui.mouse.js"></script>
	<script src="ui/jquery.ui.selectable.js"></script>
	<script src="ui/jquery.ui.button.js"></script>
	<script src="json2.js"></script>
	<script src="highlight.js"></script>

	<style>
	#feedback { font-size: 1.4em; }
	#selectable .ui-selecting { background: #FECA40; }
	#selectable .ui-selected { background: #F39814; color: white; }
	#selectable { list-style-type: none; margin: 0; padding: 0; }
	#selectable li { margin: 3px; padding: 1px; float: left; width: 100px; height: 40px; font-size: 1em; text-align: center; }
	
	#footerSlideContainer {
	position: fixed;
	bottom:0;
	width: 40%;
	}
	#footerSlideButton {
	background: url(pin.png) top left no-repeat transparent;
	position: absolute;
	top: -55px;
	right: 20px;
	width:50px;
	height:50px;
	border: none;
	cursor: pointer;
	}
	#footerSlideContent {
	width: 100%;
	height: 0px;
	background: #251b15;
	color: #CCCCCC;
	font-size: 0.8em;
	border: none;
	font-family: DejaVuSansBook, Sans-Serif;
	}
	#footerSlideText {
	padding: 5px 5px 5px 5px;
	}

	</style>
	
<?php
$media_id=$_GET['id'];
// Open a MySQL connection
$link = mysql_connect('localhost', 'root', '123');
if(!$link) {
die('Connection failed: ' . mysql_error());
}

// Select the database to work with
$db = mysql_select_db('matterhorn');
if(!$db) {
die('Selected database unavailable: ' . mysql_error());
}

$query = "SELECT pdf_name FROM docsyncupload WHERE id='$media_id'";
$result = mysql_query($query);

if (!$result) die ("Database access failed: " . mysql_error());

$rows = mysql_num_rows($result);

for ($j = 0 ; $j < $rows ; ++$j)
{
	$row = mysql_fetch_row($result);
	$pdf[$j] = $row[0];

}

$firstPdf = $pdf[0];	
echo "<script>
	//defining variable with pdf name for first load
	kDefaultURL = '$firstPdf';

	//function for selectable multiple pdf
	$(function() {
		$('#selectable' ).selectable();";
for ($j = 0 ; $j < $rows ; ++$j)
{		
		$p = $pdf[$j];
		$k=$j+1;
		echo "$(\"#doc_$k\").click(function() { PDFView.open('$p'); });";
		
}		
	echo "});
	</script>";
?>
	<!-- clickable div script -->
	<script type="text/javascript" >

	$(function() {
        $('#viewer').on('click','.textLayer div',function () {
			var pdftitle = document.title;
			var myPlayer = _V_("example_video_1");
			var pageStr = String(PDFView.page);
			var containerStr = "#pageContainer".concat(pageStr);
			var divIndex = $(this).closest('.textLayer').find('div').index(this);
			clickHighlight(divIndex, containerStr, pdftitle, function(num){ myPlayer.pause(); myPlayer.currentTime(num); myPlayer.play(); });
			//myPlayer.currentTime(vTime); 
        		});
    	});
	
		
	</script>
	<!-- upload script -->
	<?php
	$media_id=$_GET['id'];
	$cmd3="find /opt/matterhorn/felix/work/opencast/workspace/mediapackage/$media_id -name '*.mp4' -o -name '*.flv'";
	$source3 = exec($cmd3);
	$file1 = basename($source3);

	echo "<script type='text/javascript' >

	function loadFile(videoname) {
	// Retrieve the FileList object from the referenced element ID
	var myFileList = document.getElementById('upload_file').files;
	
	//video filename	
	var filename = '$file1';
	var Mid = '$media_id';
 
	// Grab the first File Object from the FileList
	var myFile = myFileList[0];
 
	// Set some variables containing the three attributes of the file
	var pdfName = myFile.name;
	$.get('docupstore.php', { pdf: pdfName, id: Mid, video: filename }, function(data) { });
 
	// Let's upload the complete file object
	uploadFile(myFile);
	}

	function uploadFile(myFileObject) {
	// Open Our formData Object
	var formData = new FormData();
 
	// Append our file to the formData object
	// Notice the first argument 'file' and keep it in mind
	formData.append('my_uploaded_file', myFileObject);
 
	// Create our XMLHttpRequest Object
	var xhr = new XMLHttpRequest();
	
	// send video info
		
 
	// Open our connection using the POST method
	xhr.open('POST', 'upload.php');
 
	// Send the file
	xhr.send(formData);

	}	

	</script>"
	?>
	
	<!-- Drop down form extension -->

        <link rel="stylesheet" href="dropdownform.css"/>
	<script type="text/javascript" src="dropdownform.js"></script>

	<!-- PDFJSSCRIPT_INCLUDE_FIREFOX_EXTENSION -->

        <link rel="stylesheet" href="viewer.css"/>
	
        <script type="text/javascript" src="compatibility.js"></script> <!-- PDFJSSCRIPT_REMOVE_FIREFOX_EXTENSION -->

        <!-- PDFJSSCRIPT_INCLUDE_BUILD -->
        <script type="text/javascript" src="src/core.js"></script> <!-- PDFJSSCRIPT_REMOVE_CORE -->
        <script type="text/javascript" src="src/util.js"></script>  <!-- PDFJSSCRIPT_REMOVE_CORE -->
        <script type="text/javascript" src="src/canvas.js"></script>  <!-- PDFJSSCRIPT_REMOVE_CORE -->
        <script type="text/javascript" src="src/obj.js"></script>  <!-- PDFJSSCRIPT_REMOVE_CORE -->
        <script type="text/javascript" src="src/function.js"></script> <!-- PDFJSSCRIPT_REMOVE_CORE -->
        <script type="text/javascript" src="src/charsets.js"></script>  <!-- PDFJSSCRIPT_REMOVE_CORE -->
        <script type="text/javascript" src="src/cidmaps.js"></script>  <!-- PDFJSSCRIPT_REMOVE_CORE -->
        <script type="text/javascript" src="src/colorspace.js"></script>  <!-- PDFJSSCRIPT_REMOVE_CORE -->
        <script type="text/javascript" src="src/crypto.js"></script>  <!-- PDFJSSCRIPT_REMOVE_CORE -->
        <script type="text/javascript" src="src/evaluator.js"></script>  <!-- PDFJSSCRIPT_REMOVE_CORE -->
        <script type="text/javascript" src="src/fonts.js"></script>  <!-- PDFJSSCRIPT_REMOVE_CORE -->
        <script type="text/javascript" src="src/glyphlist.js"></script>  <!-- PDFJSSCRIPT_REMOVE_CORE -->
        <script type="text/javascript" src="src/image.js"></script>  <!-- PDFJSSCRIPT_REMOVE_CORE -->
        <script type="text/javascript" src="src/metrics.js"></script>  <!-- PDFJSSCRIPT_REMOVE_CORE -->
        <script type="text/javascript" src="src/parser.js"></script>  <!-- PDFJSSCRIPT_REMOVE_CORE -->
        <script type="text/javascript" src="src/pattern.js"></script>  <!-- PDFJSSCRIPT_REMOVE_CORE -->
        <script type="text/javascript" src="src/stream.js"></script>  <!-- PDFJSSCRIPT_REMOVE_CORE -->
        <script type="text/javascript" src="src/worker.js"></script>  <!-- PDFJSSCRIPT_REMOVE_CORE -->
        <script type="text/javascript" src="external/jpgjs/jpg.js"></script>  <!-- PDFJSSCRIPT_REMOVE_CORE -->
        <script type="text/javascript" src="src/jpx.js"></script>  <!-- PDFJSSCRIPT_REMOVE_CORE -->
        <script type="text/javascript" src="src/bidi.js"></script>  <!-- PDFJSSCRIPT_REMOVE_CORE -->
        <script type="text/javascript">PDFJS.workerSrc = 'src/worker_loader.js';</script> <!-- PDFJSSCRIPT_REMOVE_CORE -->
        <script type="text/javascript" src="debugger.js"></script>
        <script type="text/javascript" src="viewer.js"></script>
	
	<!-- POPUP BOX FOR TEXT SYNCRONIZATION -->
	<script type='text/javascript' charset='utf-8' src='popbox.min.js'></script>
        <link rel='stylesheet' href='popbox.css' type='text/css'>
	<script type='text/javascript'>
           $(document).ready(function(){
             $('.popbox').popbox();
           });
        </script>
	
	<!-- VIDEO.JS FILES -->
	<link href="video-js.css" rel="stylesheet" type="text/css">
	<script src="video.js"></script>

	<!-- scrolling file -->
	<script type="text/javascript" src="jquery.scrollTo.js"></script>

        <!-- SCROLL TO TOP -->
	<script type="text/javascript" src="scrolltopcontrol.js"> </script>




<script>
	var indexInital, indexFinal, pageStr, containerStr, txtContainer, txtDiv, pnum, stime, etime;
	//function for highlighting and storing values in database
	$(window).scroll(function(){
   	pageStr = String(PDFView.page);
	pnum = parseInt(PDFView.page);
	containerStr = "#pageContainer".concat(pageStr);
	window.setTimeout(loadHighlight(document.title, containerStr), 5000);
	});

	
	//working of removal slider menu
	$(function() {
	var open = false;
	$('#footerSlideButton').click(function() {
		if(open === false) {
			$('#footerSlideContent').animate({ height: '250px' });
			$(this).css('backgroundPosition', 'bottom left');
			open = true;
		} else {
			$('#footerSlideContent').animate({ height: '0px' });
			$(this).css('backgroundPosition', 'top left');
			open = false;
		}
		});

		
		$('#altHighlight').click(function() {
			
			if ($('#confirmTime').is(':disabled') == true && $('#confirmRemove').is(':disabled') == true)
			{  
				oldInitial = indexInitial;
				oldFinal = indexFinal;
				$('.highalter').attr('disabled', false);
				$('.alter').hide();
				$('#pHighlight').show();
				oldInitial = indexInitial;
				oldFinal = indexFinal;
			}

			else
			{
				$('.alter').hide();
				$('#rNotice').show();
			}
		});

		$('#altTime').click(function() {
			if ($('#confirmHighlight').is(':disabled') == true && $('#confirmRemove').is(':disabled') == true)
			{
				$('.timealter').attr('disabled', false); 
				$('.alter').hide();
				$('#pTime').show();
				oldInitial = indexInitial;
				oldFinal = indexFinal;
			}

			else
			{
				$('.alter').hide();
				$('#rNotice').show();
			}
		});

		$('#remove').click(function() {
			if ($('#confirmTime').is(':disabled') == true && $('#confirmHighlight').is(':disabled') == true)
			{
				$('.completeRemove').attr('disabled', false); 
				$('.alter').hide();
				$('#pRemove').show();
				oldInitial = indexInitial;
				oldFinal = indexFinal;
			}
			
			else
			{
				$('.alter').hide();
				$('#rNotice').show();
			}
		});
		
		$('#cancelHighlight').click(function() {
			$('.highalter').attr('disabled', true);
			$('.alter').hide();
			oldInitial = null; 
			indexInitial = null;
			oldFinal = null;
			indexFinal = null;
			
		});

		$('#cancelTime').click(function() {
			$('.timealter').attr('disabled', true);
			$('.alter').hide();
			oldInitial = null; 
			indexInitial = null;
			oldFinal = null;
			indexFinal = null;
			
		});

		$('#cancelRemove').click(function() {
			$('.completeRemove').attr('disabled', true);
			$('.alter').hide();
			oldInitial = null; 
			indexInitial = null;
			oldFinal = null;
			indexFinal = null;
			
		});
		
		$('#confirmHighlight').click(function() {
			newInitial=indexInitial;
			newFinal=indexFinal;
			colorRemove(oldInitial, oldFinal, containerStr);
			alterHighlight(oldInitial, oldFinal, newInitial, newFinal);
		});
		$('#altTimeStart').click(function() {
			var myPlayerSB = _V_("example_video_1");
			    stime = myPlayerSB.currentTime();
			var str = _V_.formatTime(stime, myPlayerSB.duration());
			$('#altStart').text(str);
		});
		$('#altTimeEnd').click(function() {
			var myPlayerSB = _V_("example_video_1");
			    etime = myPlayerSB.currentTime();
			var str = _V_.formatTime(etime, myPlayerSB.duration());
			$('#altEnd').text(str);
		});
		$('#confirmTime').click(function() {
			alterTime(oldInitial, oldFinal, stime, etime);
		});
		$('#confirmRemove').click(function() {
			colorRemove(oldInitial, oldFinal, containerStr);
			removeHighlight(oldInitial, oldFinal);
			
		});
	});
	

	$(function()
	{
		$('#viewer').on('mousedown', '.textLayer div' ,
			function () 
			{  
				indexInitial = $(this).closest('.textLayer').find('div').index(this);
		}).on('mouseup', '.textLayer div',
			function () 
			{ 
				indexFinal = $(this).closest('.textLayer').find('div').index(this); });
	
	$('#startbtn').click(function() 
			{ 
				var myPlayerSB = _V_("example_video_1");
			            stime = myPlayerSB.currentTime();
				var str = _V_.formatTime(stime, myPlayerSB.duration());
				$('#start').text(str);
			});
	
	$('#endbtn').click(function() 
			{ 
				var myPlayerSB = _V_("example_video_1");
				    etime = myPlayerSB.currentTime();
				var str = _V_.formatTime(etime, myPlayerSB.duration());
				$('#end').text(str);
			});
	

		
	$("#texthighlight").click(function()
					{ 

						var pdfname = String(document.title);
						$.get("store.php",
						{indexI: indexInitial, indexF: indexFinal, page: '\"'+containerStr+'\"', pdf: '\"'+pdfname+'\"', pagen: pnum, ST: stime, ET: etime},
						function(data) { alert(data);});

						txtDiv = $(containerStr + " .textLayer div");
						for(var i = indexInitial; i<=indexFinal; i++)
							{ 
								txtDiv[i].style.opacity = 0.35;
								txtDiv[i].style.backgroundColor = "lightblue"; }
	});	});
</script>


<script type="application/javascript"> 
  
// Function to scroll the page  
function scrollFunc()
{	
	var myPlayer = _V_("example_video_1");
	var time = myPlayer.currentTime();
	var pTitle = document.title;
	scrollpdf(pTitle,time);
	
};
 
	
   // Function to add event listener
	
   function load() {

	var myPlayer = _V_("example_video_1");   
	myPlayer.addEvent("timeupdate", scrollFunc);
	  
   };   
   
   document.addEventListener("DOMContentLoaded", load, false);  
</script>


	

</head>

<body style="background-image:url(Pattern02.gif); background-repeat:repeat" >





<div id="container" style="width:100%">
<div id="header" style="margin-top:15px;width:40%;position:fixed;">
<h1 style="margin-bottom:0;">Document Synchronization</h1></div>

<div id="menu" style="width:40%;height:900px;position:fixed;top:70px;">
<br/><br/>


<?php
$media_id=$_GET['id'];
$cmd="find /opt/matterhorn/felix/work/opencast/workspace/mediapackage/$media_id -name '*.mp4' -o -name '*.flv'";
$source = exec($cmd);
$cmd2 = "cp $source /var/www/html/pdf/";
$file = basename($source);
$file_parts = pathinfo($file);
$exten = $file_parts['extension'];
if (file_exists($file))
      {
      
      }
    else
      {
      	exec($cmd2);
      }


echo "<video id=\"example_video_1\" class=\"video-js vjs-default-skin\" controls width=\"760\" height=\"400\" preload=\"auto\" data-setup=\"{}\">
  <source type=\"video/$exten\" src=\"$file\">
</video>";
?>
<br></br>
<div class="demo">

<ol id="selectable">

<?php
	
$media_id=$_GET['id'];
// Open a MySQL connection
$link = mysql_connect('localhost', 'root', '123');
if(!$link) {
die('Connection failed: ' . mysql_error());
}

// Select the database to work with
$db = mysql_select_db('matterhorn');
if(!$db) {
die('Selected database unavailable: ' . mysql_error());
}

$query = "SELECT pdf_name FROM docsyncupload WHERE id='$media_id'";
$result = mysql_query($query);

if (!$result) die ("Database access failed: " . mysql_error());

$rows = mysql_num_rows($result);

for ($j = 0 ; $j < $rows ; ++$j)
{
	$row = mysql_fetch_row($result);
	$pdf = $row[0];
	$k=$j+1;
	echo "<li class=\"ui-state-default\" id=\"doc_$k\">$pdf</li>";

}
	
?>
</ol>
<br></br>
<br></br>

</div><!-- End demo -->

<div class='popbox' style="left: -25%; top: 3%;
      font-family:georgia;
      font-size:18px;
      text-shadow:1px 1px 1px #FFF;">
    <a class='open' href='#' style="color:#999; text-decoration:none;">
      <img src='plus.png' style='width:14px;position:relative;'> Highlight Text
    </a>

    <div class='collapse'>
      <div class='box'>
        <div class='arrow'></div>
        <div class='arrow-border'></div>
		
	<form>
          <p><small>Slide progess bar & click on buttons to get time</small></p>
          	<div class="input">
            	<input type="button" id="startbtn" value="Start Time" /><label id="start">0:00</label>
		</div>
		<div class="input">
		<input type="button" id="endbtn" value="End Time" /><label id="end">0:00</label>
		</div>
          <input type="button" id="texthighlight" value="Confirm" /> <a href="#" class="close">Cancel</a>
	</form>

      </div>
    </div>
  </div>
<nav style="left:10px;">
        <ul>
                <li id="pdfSync">
                        <a id="pdfSync-trigger" href="#">
                                Synchronize PDF <span>▼</span>
                        </a>
                        <div id="pdfSync-content">
                                <form action="" method="post" enctype="multipart/form-data" id="myFormId">
                                        <fieldset id="inputs">
						<input id="upload_file" type="file" name="upload_file"/>
                                        </fieldset>
                                        <fieldset id="actions">
                                                <input type="button" id="submit" value="Upload" onclick="loadFile()">
                                        </fieldset>
                                </form>
						<div class="progress">
        					<div class="bar"></div >
        					<div class="percent">0%</div >
    						</div>
    
    						<div id="status"></div>
                        </div>
                </li>
        </ul>
</nav>

<div id="footerSlideContainer">
	<div id="footerSlideButton"></div>
	<div id="footerSlideContent">
		<div id="footerSlideText">
			<h3>Highlight Removal or Alteration Menu</h3>
			<p>Please select the highlighted text before procceding</p>
			<table style="width: 100%;">
			<tr>
			<td style="width:33%; border:3px solid IndianRed;">
			<center>
			<font size="3">Highlight Alteration</font>
			<br /><br />
			<button type="button" id="altHighlight">Alter Highlight</button>
			<br /><br />
			<button type="button" class="highalter" disabled="disabled" id="confirmHighlight">Confirm</button><button type="button" class="highalter" disabled="disabled" id="cancelHighlight">Cancel</button>
			</center>
			</td>
			
			<td style="width:33%; border:3px solid IndianRed;">
			<center><font size="3">Time Alteration</font>
			<br />
			<button type="button" id="altTime">Alter Time</button>
			<br /><br />
			<button type="button" class="timealter" disabled="disabled" id="altTimeStart">Start Time</button><label id="altStart">0:00</label>
			<button type="button" class="timealter" disabled="disabled" id="altTimeEnd">End Time</button><label id="altEnd">0:00</label>
			<br /><br />
			<button type="button" class="timealter" disabled="disabled" id="confirmTime">Confirm</button><button type="button" class="timealter" disabled="disabled" id="cancelTime">Cancel</button>
			</center>
			</td>
			
			<td style="width:33%; border:3px solid IndianRed;">
			<center>
			<font size="3">Complete Removal<font>
			<br /><br />
			<button type="button" id="remove">Remove Completely</button>
			<br /><br />
			<button type="button" class="completeRemove" disabled="disabled" id="confirmRemove">Confirm</button><button type="button" class="completeRemove" disabled="disabled" id="cancelRemove">Cancel</button>
			</center>
			</td>
			</tr>
			</table>
			
			<p class="alter" id="pHighlight" style="display:none;">Now Select the new text to highlight than click confirm</p>
			<p class="alter" id="pTime" style="display:none;">Now select the start time and end time from player than click confirm</p>
			<p class="alter" id="pRemove" style="display:none;">This will remove highlight completely</p>
			<p class="alter" id="rNotice" style="display:none;"><bold>NOTICE:</bold> one of remove tool already activated cancel them first</p>
		</div>
	</div>
</div>


</div>




<div id="content" style="width:60%;float:right;z-index:1">

    <div id="controls">
      <button id="previous" onclick="PDFView.page--;" oncontextmenu="return false;">
        <img src="images/go-up.svg" align="top" height="16"/>
        Previous
      </button>

      <button id="next" onclick="PDFView.page++;" oncontextmenu="return false;">
        <img src="images/go-down.svg" align="top" height="16"/>
        Next
      </button>

      <div class="separator"></div>

      <input type="number" id="pageNumber" onchange="PDFView.page = this.value;" value="1" size="4" min="1" />

      <span>/</span>
      <span id="numPages">--</span>

      <div class="separator"></div>

      <button id="zoomOut" title="Zoom Out" onclick="PDFView.zoomOut();" oncontextmenu="return false;">
        <img src="images/zoom-out.svg" align="top" height="14"/>
      </button>
      <button id="zoomIn" title="Zoom In" onclick="PDFView.zoomIn();" oncontextmenu="return false;">
        <img src="images/zoom-in.svg" align="top" height="16"/>
      </button>

      <div class="separator"></div>

      <select id="scaleSelect" onchange="PDFView.parseScale(this.value);" oncontextmenu="return false;">
        <option id="customScaleOption" value="custom"></option>
        <option value="0.5">50%</option>
        <option value="0.75">75%</option>
        <option value="1">100%</option>
        <option value="1.25">125%</option>
        <option value="1.5">150%</option>
        <option value="2">200%</option>
        <option id="pageWidthOption" value="page-width">Page Width</option>
        <option id="pageFitOption" value="page-fit">Page Fit</option>
        <option id="pageAutoOption" value="auto" selected="selected">Auto</option>
      </select>

      <div class="separator"></div>

      <button id="print" onclick="window.print();" oncontextmenu="return false;">
        <img src="images/document-print.svg" align="top" height="16"/>
        Print
      </button>

      <button id="download" title="Download" onclick="PDFView.download();" oncontextmenu="return false;">
        <img src="images/download.svg" align="top" height="16"/>
        Download
      </button>

      <div class="separator"></div>

      <input id="fileInput" type="file" oncontextmenu="return false;"/>

      <div id="fileInputSeperator" class="separator"></div>

      <a href="#" id="viewBookmark" title="Bookmark (or copy) current location">
        <img src="images/bookmark.svg" alt="Bookmark" align="top" height="16"/>
      </a>

    </div>
    <div id="errorWrapper" hidden='true'>
      <div id="errorMessageLeft">
        <span id="errorMessage"></span>
        <button id="errorShowMore" onclick="" oncontextmenu="return false;">
          More Information
        </button>
        <button id="errorShowLess" onclick="" oncontextmenu="return false;" hidden='true'>
          Less Information
        </button>
      </div>
      <div id="errorMessageRight">
        <button id="errorClose" oncontextmenu="return false;">
          Close
        </button>
      </div>
      <div class="clearBoth"></div>
      <textarea id="errorMoreInfo" hidden='true' readonly="readonly"></textarea>
    </div>

    <div id="sidebar" >
      <div id="sidebarBox">
        <div id="pinIcon" onClick="PDFView.pinSidebar()"></div>
        <div id="sidebarScrollView">
          <div id="sidebarView" ></div>
        </div>
        <div id="outlineScrollView" hidden='true'>
          <div id="outlineView"></div>
        </div>
        <div id="sidebarControls" >
          <button id="thumbsSwitch" title="Show Thumbnails" onclick="PDFView.switchSidebarView('thumbs')" data-selected>
            <img src="images/nav-thumbs.svg" align="top" height="16" alt="Thumbs" />
          </button>
          <button id="outlineSwitch" title="Show Document Outline" onclick="PDFView.switchSidebarView('outline')" disabled>
            <img src="images/nav-outline.svg" align="top" height="16" alt="Document Outline" />
          </button>
        </div>
      </div>
    </div>
	


    <div id="loading">Loading... 0%</div>
    
    <div id="viewer" style="z-index:2"></div>
    
</div>


</div>

  </body>
</html>
