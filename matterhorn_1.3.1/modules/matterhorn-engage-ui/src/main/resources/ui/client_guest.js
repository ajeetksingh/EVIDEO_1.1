



function writeCookie(name,value,days) {
    var date, expires;
    if (days) {
        date = new Date();
        date.setTime(date.getTime()+(days*24*60*60*1000));
        expires = "; expires=" + date.toGMTString();
            }else{
        expires = "";
    }
    document.cookie = name + "=" + value + expires + "; path=/";
}
function readCookie(name) {
    var i, c, ca, nameEQ = name + "=";
    ca = document.cookie.split(';');
    for(i=0;i < ca.length;i++) {
        c = ca[i];
        while (c.charAt(0)==' ') {
            c = c.substring(1,c.length);
        }
        if (c.indexOf(nameEQ) == 0) {
            return c.substring(nameEQ.length,c.length);
        }
    }
    return '';
}
function getUsername()
{
	var str=document.referrer;
	var str1=str.split('=');
	console.log(str1[1]);
	return str1[1];
}
var QueryString = function () {
  	// This function is anonymous, is executed immediately and 
  	// the return value is assigned to QueryString!
  	var query_string = {};
  	var query = window.location.search.substring(1);
  	var vars = query.split("&");
  	for (var i=0;i<vars.length;i++) {
    var pair = vars[i].split("=");
        // If first entry with this name
    if (typeof query_string[pair[0]] === "undefined") {
      query_string[pair[0]] = pair[1];
        // If second entry with this name
    } else if (typeof query_string[pair[0]] === "string") {
      var arr = [ query_string[pair[0]], pair[1] ];
      query_string[pair[0]] = arr;
        // If third or later entry with this name
    } else {
      query_string[pair[0]].push(pair[1]);
    }
  } 
    return query_string;
} ();
var username=getUsername();
var videoid=QueryString.id;

var user;
if(username && username.length <= 25 )
{
	console.log("writing cookie");
	writeCookie('sessionId', username, 3);
	
}
user=readCookie('sessionId');
console.log(user);

function popupAdv()
{
	var user=readCookie('sessionId');
	console.log(user);
	popup('popUpAdvanced');
	
	

}


// Function to delete the Bookmark from database
function deleteBookmark(id)
{
	
	if(adminLogged || user != "NULL")
	{	
		var r=confirm("Do you really want to delete Bookmark ?");
		if (r==true)
  		{
			var xmlhttp;
			
			var mid= QueryString.id;
			// var mstart=document.form.start.value;
			// var mstop=document.form.stop.value;
			// var mtype=document.form.type.value;
			// var mval=document.form.val.value;
			 var str= "id="+id;
 			//alert(mstart);
			if (window.XMLHttpRequest)
			  {// code for IE7+, Firefox, Chrome, Opera, Safari
 				 xmlhttp=new XMLHttpRequest();
 			 }
			else
 				 {// code for IE6, IE5
  				xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
 			 }
			xmlhttp.onreadystatechange=function()
			  {
 			 	if (xmlhttp.readyState==4 && xmlhttp.status==200)
  			 	 {
 			 	  document.getElementById("wrapper").innerHTML=xmlhttp.responseText;
   			 	}
  				}
			xmlhttp.open("GET","http://evideo.iitj.ac.in:81/json-delete-bookmark.php?"+str+'&jsoncallback=?',true);
			xmlhttp.send();
			popup('popUpDeleteBookmark');
		}
	}
	else
	{
		alert("You don't have sufficient privileges to Delete the Bookmarks!");
	}

}

function searchAdvanced()
{
	var title=document.formadvanced.title1.value;
	console.log(title);
}


// Function to Delete Link from Database
function deleteLink(id)
{
 	
	if(adminLogged || user != "NULL")
	{	
		var r=confirm("Do you really want to delete Link ?");
		if (r==true)
  		{
			var xmlhttp;
			
			var mid= QueryString.id;
			// var mstart=document.form.start.value;
			// var mstop=document.form.stop.value;
			// var mtype=document.form.type.value;
			// var mval=document.form.val.value;
			 var str= "id="+id+"&media_id="+mid;
 			//alert(mstart);
			if (window.XMLHttpRequest)
			  {// code for IE7+, Firefox, Chrome, Opera, Safari
 				 xmlhttp=new XMLHttpRequest();
 			 }
			else
 			 {// code for IE6, IE5
  				xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
 			 }
			xmlhttp.onreadystatechange=function()
			  {
 			 	if (xmlhttp.readyState==4 && xmlhttp.status==200)
  			 	 {
 			 	  document.getElementById("wrapper").innerHTML=xmlhttp.responseText;
   			 	}
  			}
			xmlhttp.open("GET","http://evideo.iitj.ac.in:81/json-delete-link.php?"+str+'&jsoncallback=?',true);
			xmlhttp.send();
			popup('popUpDiv5');
		}
	}
	else
	{
		alert("You don't have sufficient privileges to delete the Links!");
	}
}	
	
// Function to delete Annotation from Database
function deleteAnnotation(id)
{
 	// alert(id);

	//alert(getUsername());
	
	if(adminLogged || user != "NULL")
	{
		var r=confirm("Do you really want to delete Annotation ?");
		if (r==true)
	  	{
	 
			var xmlhttp;
			
			 var mid= QueryString.id;
			// var mstart=document.form.start.value;
			// var mstop=document.form.stop.value;
			// var mtype=document.form.type.value;
			// var mval=document.form.val.value;
			 var str= "id="+id+"&media_id="+mid;
	 		//alert(mstart);
			if (window.XMLHttpRequest)
			  {// code for IE7+, Firefox, Chrome, Opera, Safari
	 			 xmlhttp=new XMLHttpRequest();
	 		 }
			else
	 		 {// code for IE6, IE5
	  			xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
	 		 }
			xmlhttp.onreadystatechange=function()
			  {
	 		 	if (xmlhttp.readyState==4 && xmlhttp.status==200)
	  		 	 {
	 		 	  document.getElementById("wrapper").innerHTML=xmlhttp.responseText;
	   		 	}
	  		}
			xmlhttp.open("GET","http://evideo.iitj.ac.in:81/json-delete-annotation.php?"+str+'&jsoncallback=?',true);
			xmlhttp.send();
			popup('popUpDiv6');
		}
	}
	else
	{
		alert("You don't have sufficient privileges to delete the Annotations!");
	}	
}
// End of Function deleteAnnotation


// Function to Retrieve the Annotation from database for Editing
function editAnnotation(id)
{
 	
	
		 var mid= QueryString.id;
		var title;
		var time;
		var stop;
		var titleid;
		 var str= "id="+id+"&media_id="+mid;
 				 $.getJSON('http://evideo.iitj.ac.in:81/json-retrieve-annotation.php?'+str+'&jsoncallback=?', function(r) {
 		console.log("Inside getJSON");
			//alert(r);
			r=JSON.stringify(r);
			var temp =JSON.parse(r); 
                      console.log(temp[0].title);
			title=temp[0].title;
			time=temp[0].time;
			stop=temp[0].stop;
			titleid=temp[0].titleid;
			console.log(title);
			document.editform.val.value=title;
			document.editform.start.value=time;
	document.editform.stop.value=stop;
	document.editform.id.value=titleid;
                      //create_sidebar(r,str);
			//create_sidebar1(r,str);
   		//console.log(JSON.stringify(data));
 
		});
	
	
	popup('popUpDiv7');
}
// End of Function

// Function to Retrieve the Annotation from database for Appending

	function checkAdmin()
	{
		$.getJSON('http://evideo.iitj.ac.in:81/check-admin-cookie.php?jsoncallback=?', function(r){

			console.log("Inside getJSON");
			r=JSON.stringify(r);
			var temp=json.parse(r);
			var response=temp.response;
			if(response)
			{
				return true;
			}
			else
			{
				return false;
			}

		});
	}

 var adminLogged=checkAdmin();

	function appendAnnotation(id)
	{
 		if(adminLogged || user!="NULL")
		{
			var mid= QueryString.id;
			var title;
			var time;
			var stop;
			var titleid;
			var str= "id="+id+"&media_id="+mid;
 			$.getJSON('http://evideo.iitj.ac.in:81/json-retrieve-annotation.php?'+str+'&jsoncallback=?', function(r) {
 				console.log("Inside getJSON");
				//alert(r);
				r=JSON.stringify(r);
				var temp =JSON.parse(r); 
        	        	console.log(temp[0].title);
				title=temp[0].title;
				time=temp[0].time;
				stop=temp[0].stop;
				titleid=temp[0].titleid;
				console.log(title);
				document.formAppend.annotation.value=title;
				document.formAppend.start.value=time;
				document.formAppend.stop.value=stop;
				document.formAppend.id.value=titleid;
        	        });
			popup('popUpDivAppend');
		}
		else
		{
			alert("You don't have sufficient privileges to Append to the Annotation!");
		}
	}
//End of Function


//Function to Seek the Video for Bookmarks
function seekVideoPage(id)
{
 	
	
		
		var title;
		var start;
		var stop;
		var media_id;
		var bid;
		var user=readCookie('sessionId');
		 var str= "id="+id+"&user="+user;
 				 $.getJSON('http://evideo.iitj.ac.in:81/json-retrieve-bookmark-nextid.php?'+str+'&jsoncallback=?', function(r) {
 		console.log("Inside getJSON");
			//alert(r);
			r=JSON.stringify(r);
			var temp =JSON.parse(r); 
                      console.log(temp[0].title);
			title=temp[0].title;
			start=temp[0].start;
			stop=temp[0].stop;
			media_id=temp[0].media_id;
			bid=temp[0].bid;
			if(media_id)
			{
			window.location = "http://evideo.iitj.ac.in/engage/ui/watch.html?id="+media_id+"&start="+start+"&stop="+stop+"&bid="+bid;
                      }
			else
			{
				alert("End of Bookmarks");
			}
		});
}
function seekVideo(id)
{
 	
		var runCommand='abcd('+id+')';
		console.log(id);
		var title;
		var start;
		var stop;
		var media_id;
		 var str= "id="+id;
 				 $.getJSON('http://evideo.iitj.ac.in:81/json-retrieve-bookmark.php?'+str+'&jsoncallback=?', function(r) {
 		console.log("Inside getJSON");
			//alert(r);
			r=JSON.stringify(r);
			var temp =JSON.parse(r); 
                      console.log(temp[0].title);
			title=temp[0].title;
			start=temp[0].start;
			stop=temp[0].stop;
			media_id=temp[0].media_id;
			if(media_id)
			{
			window.location = "http://evideo.iitj.ac.in/engage/ui/watch.html?id="+media_id+"&start="+start+"&stop="+stop+"&bid="+id;
                      }
			else
			{
				alert("End of Bookmarks");
			}
		});
}

function seekVideoForBookmark(id)
{
 	
		var runCommand='abcd('+id+')';
		console.log(id);
		var title;
		var start;
		var stop;
		var media_id;
		 var str= "id="+id;
 				 $.getJSON('http://evideo.iitj.ac.in:81/json-retrieve-bookmark.php?'+str+'&jsoncallback=?', function(r) {
 		console.log("Inside getJSON");
			//alert(r);
			r=JSON.stringify(r);
			var temp =JSON.parse(r); 
                      console.log(temp[0].title);
			title=temp[0].title;
			start=temp[0].start;
			stop=temp[0].stop;
			media_id=temp[0].media_id;
			if(media_id)
			{
			window.location = "http://evideo.iitj.ac.in/engage/ui/watch.html?id="+media_id+"&start="+start+"&bid="+id+"&run="+runCommand;
                      }
			
		});
}

function abcd(id)
{	
	popup('popUpBookmarkEdit');
	editBookmark(id);
}

function editBookmark(id)
				{

					var mid=QueryString.id;
					var title;
					var description;
					var start;
					var stop;
					var str= "id="+id;//+"&title="+mval+"&url="+murl+"&description="+mtype+"&start="+mstart+"&stop="+mstop;
					//alert(str);	
				        $.getJSON('http://evideo.iitj.ac.in:81/get-bookmark-data.php?'+str+'&jsoncallback=?', function(r) {
 					console.log("Inside getJSON");
                      			r=JSON.stringify(r);
                       			var temp =JSON.parse(r); 
                     			console.log(temp[0].title);
                       			title=temp[0].title;
                       			description=temp[0].description;
					start=temp[0].start;
					stop=temp[0].stop;
					document.formbookmarkedit.title1.value=title;
					//document.formbookmarkedit.url1.value=url;
					document.formbookmarkedit.description1.value=description;
					document.formbookmarkedit.start1.value=start;
					document.formbookmarkedit.stop1.value=stop;
					document.formbookmarkedit.id.value=id;
			//create_sidebar1(r,str);
   		//console.log(JSON.stringify(data));
 
		});	
				 		
				//popup('popUpDivUpdateLink');	
				
				}


function editLink(id)
				{

					var mid=QueryString.id;
					var title;
					var url;
					var description;
					var start;
					var stop;
					var str= "id="+id+"&media_id="+mid;//+"&title="+mval+"&url="+murl+"&description="+mtype+"&start="+mstart+"&stop="+mstop;
					//alert(str);	
				        $.getJSON('http://evideo.iitj.ac.in:81/get-link-data.php?'+str+'&jsoncallback=?', function(r) {
 					console.log("Inside getJSON");
                      			r=JSON.stringify(r);
                       			var temp =JSON.parse(r); 
                     			console.log(temp[0].title);
                       			title=temp[0].title;
                       			url=temp[0].url;
					description=temp[0].description;
					start=temp[0].start;
					stop=temp[0].stop;
					document.formlinkedit.title1.value=title;
					document.formlinkedit.url1.value=url;
					document.formlinkedit.description1.value=description;
					document.formlinkedit.start1.value=start;
					document.formlinkedit.stop1.value=stop;
					document.formlinkedit.id.value=id;
			//create_sidebar1(r,str);
   		//console.log(JSON.stringify(data));
 
		});	
				 		
				//popup('popUpDivUpdateLink');	
				
				}

		

		function showBookmarkDetails(id)
		{
		popup('popUpBookmarkDetails');
		detailsBookmark(id);		
			
		}


		function detailsBookmark(id)

		{
		
			var media_name;
			var media_creator;
			var duration;
			var description;
			var str="id="+id;
			//alert(str);
			$.getJSON('http://evideo.iitj.ac.in:81/BookmarkDetails.php?'+str+'&jsoncallback=?', function(r) {
 			console.log("Inside getJSON");
                      	r=JSON.stringify(r);
                       	var temp =JSON.parse(r); 
                     	console.log(temp[0].title);
                       	media_name=temp[0].title;
                       	duration=temp[0].start;
			description=temp[0].desc;
			media_creator=temp[0].media_creator;
			//stop=temp[0].stop;
			document.formbookmarkdetails.media_name.value=media_name;
			document.formbookmarkdetails.media_creator.value=media_creator;
			if(description==""){ document.formbookmarkdetails.description.value=media_name;}
			else {document.formbookmarkdetails.description.value=description;}
			document.formbookmarkdetails.duration.value=duration;});
						
		}


		function viewAnnDetails(id)
		{
			$('#popUpViewAnnDetails div').empty();			
			vwAnnDetails(id);
			popup('popUpViewAnnDetails');
		}

		function vwAnnDetails(id)
		{
			
			$.getJSON('http://evideo.iitj.ac.in:81/json-create-Annotation.php?media_id='+id+'&jsoncallback=?', function(r) {
				console.log("Inside getJSON for Annotation Show from Advanced Results");
				//alert(r);
				r=JSON.stringify(r);
				//alert(r);
				var arrObj = eval('(' +r+ ')');	
				var i=0;
				length1=arrObj.length;
				var temp =JSON.parse(r); 
	                      console.log(temp);
				for (i=0;i<length1;i++)
				{
					var title=temp[i].time;
					console.log(title);
					//var time=temp[i].stop;
					var stop=temp[i].title;
					console.log(stop);
					$('#annotationContainer').append("<div id=div"+i+"><label id=label"+i+">"+stop+"</label></div>");
				}
			});
			
			

		}


	function viewOCRDetails(id)
	{
	
		$('#popUpViewOCRDetails div').empty();
		console.log(Opencast.Watch.getSegmentsTextURL());
		showSegmentsTextVideo(id);
		popup('popUpViewOCRDetails');
	
	}


	function showSegmentsTextVideo(mediaPackageId)
	{
	        Opencast.Player.addEvent(Opencast.logging.SHOW_TEXT_SEGMENTS);
	        // Hide other Tabs
	        //Opencast.Description.hideDescription();
	        //Opencast.segments.hideSegments();
	        //Opencast.search.hideSearch();
	        // Change Tab Caption
	        //$('#oc_btn-slidetext').attr(
	        //{
	         //   title: SEGMENTS_TEXT_HIDE
	        //});
	        //$('#oc_btn-slidetext').html(SEGMENTS_TEXT_HIDE);
	        //$("#oc_btn-slidetext").attr('aria-pressed', 'true');
	        // Show a loading Image
	       // $('#oc_slidetext').show();
	       // $('#segments_text-loading').show();
	       // $('#oc-segments_text').hide();
	        //$('.oc-segments-preview').css('display', 'block');
	       // // If cashed data are available
	        if (Opencast.segments_text_Plugin.createSegmentsTextFromCashe())
	        {
	            $.log("Cashing segments text plugin: yes");
	            // Make visible
	            //$('#oc_slidetext').show();
	           // $('#segments_text-loading').hide();
	            //$('#oc-segments_text').show();
	            //$('.oc-segments-preview').css('display', 'block');
	        }
	        else
	        {
	            $.log("Cashing segments text plugin: no");
	            // Request JSONP data
	            $.ajax(
	            {
	                url: Opencast.Watch.getSegmentsTextURL(),
	                data: 'id=' + mediaPackageId,
	                dataType: 'jsonp',
	                jsonp: 'jsonp',
	                success: function (data)
	                {
	                    console.log("Data Fetching Successull!");
			    $.log("Segments Text AJAX call: Requesting data succeeded");
	                    // get rid of every '@' in the JSON data
	                    // data = $.parseJSON(JSON.stringify(data).replace(/@/g, ''));
	                    if ((data === undefined) || (data['search-results'] === undefined) || (data['search-results'].result === undefined) || (data['search-results'].result.segments === undefined))
	                    {
				console.log("Data not available!");	                        
				$.log("Segments Text AJAX call: Data not available");
	                    } else
	                    {
				console.log("Dat available!");	                        
				$.log("Segments Text AJAX call: Data available");
	                        data['search-results'].result.segments.currentTime = $.getTimeInMilliseconds(Opencast.Player.getCurrentTime());
	                        // Set Duration until this Segment ends
	                        var completeDuration = 0;
	                        $.each(data['search-results'].result.segments.segment, function (i, value)
	                        {
	                            // Set a Duration until the Beginning of this Segment
	                            data['search-results'].result.segments.segment[i].durationExcludingSegment = completeDuration;
	                            completeDuration += parseInt(data['search-results'].result.segments.segment[i].duration);
	                            // Set a Duration until the End of this Segment
	                            data['search-results'].result.segments.segment[i].durationIncludingSegment = completeDuration;
	                        });
	                        // Create Trimpath Template
	                        Opencast.segments_text_PluginforVideos.addAsPlugin2($('#OCRContainer'), data['search-results'].result.segments);
	                        // Make visible
	                       // $('#oc_slidetext').show();
	                        //$('#segments_text-loading').hide();
	                        //$('#oc-segments_text').show();
	                        //$('.oc-segments-preview').css('display', 'block');
	                    }
	                },
	                // If no data comes back
	                error: function (xhr, ajaxOptions, thrownError)
	                {
	                    console.log("Data Fetching Fail!");
			    $.log("Segments Text Ajax call: Requesting data failed");
	                    Opencast.Player.addEvent(Opencast.logging.SEGMENTS_TEXT_AJAX_FAILED);
	                   // $('#oc-segments_text').html('No Segment Text available');
	                    //$('#oc-segments_text').hide();
	                }
	            });
	        }
	}
	    


	function updateLink()
	{
		var xmlhttp;
		var mid=QueryString.id;
		var mstart=parseInt(ocument.formlinkedit.start1.value);
		var mstop=parseInt(document.formlinkedit.stop1.value);
		var mtype=document.formlinkedit.description1.value;
		var mval=document.formlinkedit.title1.value;
                var murl=document.formlinkedit.url1.value;
		var id=document.formlinkedit.id.value;
		if(mstart!=""&&mstop!=""&&mtype!=""&&mval!=""&&murl!="")
		{	
			murl_ok=murl.indexOf("http://");
			if(murl_ok!=-1)
			{			
				if(!isNaN(mstart)&&!isNaN(mstop))
				{			
					if(mstart>0 && mstop>0)					
					{
						if(mstart<mstop)
						{
							var str= "id="+id+"&media_id="+mid+"&title="+mval+"&url="+murl+"&description="+mtype+"&start="+mstart+"&stop="+mstop;
							if (window.XMLHttpRequest)
							{// code for IE7+, Firefox, Chrome, Opera, Safari
			 					xmlhttp=new XMLHttpRequest();
	 						}
							else
	 				 		{// code for IE6, IE5
	  							xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
	 				 		}
							xmlhttp.onreadystatechange=function()
					  		{
	 				 			if (xmlhttp.readyState==4 && xmlhttp.status==200)
	  				 			{
	 				 				alert("Data updated Successfully!");
								} 
	  						}
							xmlhttp.open("GET","http://evideo.iitj.ac.in:81/update-link-data.php?"+str+'&jsoncallback=?',true);
							xmlhttp.send();
							popup('popUpDivLinkEdit');		
							popup('popUpDivUpdateLink');
						}
						else
						{
							alert("Stop time should be later than start time!");
							document.formlinkdedit.start1.value="";
							document.formlinkedit.stop1.value="";
						}					
					}
					else
					{
						alert("Start and Stop times should be positive!");
						document.formlinkedit.start1.value="";
						document.formlinkedit.stop1.value-"";
					}	
				}
				else
				{
					alert("Start and Stop times should be Number!");
					document.formlinkedit.start1.value="";
					document.formlinkedit.stop1.value="";
				}	
			}
			else
			{
				alert("URL should contain 'http://' ..");
				document.formlinkedit.url1.value="";
			}	
		}
		else
		{
			alert("All the fields are necessary!");
		}
	}	

	function updateBookmark()
	{

		var xmlhttp;
	        var mid=QueryString.id;
		var mstart=parseInt(document.formbookmarkedit.start1.value);
		var mstop=parseInt(document.formbookmarkedit.stop1.value);
		var mtype=document.formbookmarkedit.description1.value;
		var mval=document.formbookmarkedit.title1.value;
               	var id=document.formbookmarkedit.id.value;
		if(mstart!=""&&mstop!=""&&mtype!=""&&mval!="")
		{
			if(!isNaN(mstart)&&!isNaN(mstop))
			{
				if(mstart>0 && mstop>0)
				{	
					if(mstart<mstop)
					{					
						var str= "id="+id+"&media_id="+mid+"&title="+mval+"&description="+mtype+"&start="+mstart+"&stop="+mstop;
						if (window.XMLHttpRequest)
						{// code for IE7+, Firefox, Chrome, Opera, Safari
			 				xmlhttp=new XMLHttpRequest();
			 			}
						else
			 			{// code for IE6, IE5
			  				xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
			 			}
						xmlhttp.onreadystatechange=function()
						{
			 				if (xmlhttp.readyState==4 && xmlhttp.status==200)
			  				{
			 					alert("Data updated Successfully!");
							} 
			  			}
						xmlhttp.open("GET","http://evideo.iitj.ac.in:81/update-bookmark-data.php?"+str+'&jsoncallback=?',true);
						xmlhttp.send();
						popup('popUpBookmarkEdit');		
						popup('popUpDivUpdateBookmark');
					}
					else
					{
						alert("Stop time should be later than Start time!");
						document.formbookmarkedit.start1.value="";
						document.formbookmarkedit.stop1.value="";
					}	
				}
				else
				{
					alert("Start and Stop time should be positive!");
					document.formbookmarkedit.start1.value="";
					document.formbookmarkedit.stop1.value="";
				}
			}
			else
			{
				alert("Start and Stop should be Numbers!");
				document.formbookmarkedit.start1.value="";
				document.formbookmarkedit.stop1.value="";
			}	
		}
		else
		{
			alert("All the fields are Essential to fill!");
		}
		
	}


function openBookmark()
{
	document.formbookmarkadd.id.value=QueryString.id;
	document.formbookmarkadd.username.value=user;
	popup('popUpBookmarkAdd');
}
function abc1()
{
	if(adminLogged||user!=NULL)
	{
		popup('popUpDiv3');
	}
	else
	{
		alert("You don't have sufficient privileges to add Links! ");
	}
}
function abc2(id)
{
 popup('popUpDivLinkEdit');
 editLink(id);
}

function abc3(id)
{	
	if(adminLogged || user!="NULL")
	{	
		seekVideoForBookmark(id);	
	}
	else
	{
		alert("You don't have sufficient privileges to edit the Bookmarks!");
	}
	
		
}

	function abc()
	{
		if(adminLogged || user!=NULL)
		{		
			popup('popUpDiv');
		}
		else
		{
			alert("You don't have sufficient privileges to Add the Annotations!");
		}
	}




if (jQuery) (function ($) {
    // http://stackoverflow.com/questions/1067464/need-to-cancel-click-mouseup-events-when-double-click-event-detected
    $.fn.fixClick =  function (click, dblclick) {

        var app = this;
        app.click = click;
        app.dblclick = dblclick;
        app.firstClick = false;
        app.timer = null;
        app.delay = 300;

        $(this).click(function (e){

        var ins = this;
        ins.e = e;

            if (app.firstClick == false) {
            app.timer = setTimeout(function () {
            app.click(ins.e);
            app.firstClick = false;
            }, app.delay);
            }
        app.firstClick = true;

        }).dblclick(function (e){
        clearTimeout(app.timer);
        app.firstClick = false;
        app.dblclick(e);
        });

        return this;
    };
})(jQuery);

(function ($) {
    var g = this;

    g.gevent = function (act, lab, val) {
        if (!act) {
            return;
        }

        if (typeof val == 'undefined') {
            if (typeof lab != 'undefined') {
                g._gaq.push(['vp._trackEvent', 'widget', act, lab]);
            }
            g._gaq.push(['vp._trackEvent', 'widget', act]);
        } else {
            if (typeof lab != 'undefined') {
                g._gaq.push(['vp._trackEvent', 'widget', act, lab, val]);
            }
            g._gaq.push(['vp._trackEvent', 'widget', act, val]);
        }
    };
})(jQuery);



(function ($) {
    var g = this,
        players = {},
        vp = {},
	vp1 = {},
	vp2 = {},
	vp3={},
        style = null,
        host,
        local;

    host = document.location.host;
    // host='evideo.iitj.ac.in';
    local = (host == 'evideo.iitj.ac.in');
    local = false;

    // if (local) {
    //     $('head').append('<link rel="stylesheet" href="http://evideo.iitj.ac.in:81/style/default.css" type="text/css" />');
    // } else {
        style = 'REPLACE_WITH_STYLE'; //todo: automate this
        style = '._vp_widget{ width: 400px;} .vp_header{ width:100%; text-align:right; background-color:#FDE7CE; border:1px solid gray; text-align:center}.vp_footer{ width:100%; text-align:right; background-color :#FDE7CE; border:1px solid gray}.vp_footer a{margin-right:5px;color:#A01A1A}.vp_list{ float:left; text-align:left; border:1px solid gray; background-color:white; width:100%; overflow:auto; position: relative}.vp_lst_even{background-color:white;padding:4px}.vp_lst_odd{background-color:#FDE7CE;padding:4px;}.vp_lst_active{background-color:#787676}.vp_time{ position:absolute; margin:0 3px 0 0; right:0px; background-color:gray; width:45px; text-align:center; border:1px solid darkgrey; color:ghostwhite; text-decoration:none}';
	 style += '._vp_widget1{ width: 400px;} .vp_header{ width:100%; text-align:right; background-color:#FDE7CE; border:1px solid gray; text-align:center}.vp_footer{ width:100%; text-align:right; background-color:#FDE7CE; border:1px solid gray}.vp_footer a{margin-right:5px;color:#A01A1A}.vp_list{ float:left; text-align:left; border:1px solid gray; background-color:white; width:100%; overflow:auto; position: relative}.vp_lst_even{background-color:white;padding:4px}.vp_lst_odd{background-color:#FDE7CE;padding:4px;}.vp_lst_active{background-color:#787676}.vp_time{ position:absolute; margin:0 3px 0 0; right:0px; background-color:gray; width:45px; text-align:center; border:1px solid darkgrey; color:ghostwhite; text-decoration:none}';
	style += '._vp_widget2{ width: 400px;} .vp_header{ width:100%; text-align:right; background-color:#FDE7CE; border:1px solid gray; text-align:center}.vp_footer{ width:100%; text-align:right; background-color:#FDE7CE; border:1px solid gray}.vp_footer a{margin-right:5px;color:#A01A1A}.vp_list{ float:left; text-align:left; border:1px solid gray; background-color:white; width:100%; overflow:auto; position: relative}.vp_lst_even{background-color:white;padding:4px}.vp_lst_odd{background-color:#FDE7CE;padding:4px;}.vp_lst_active{background-color:#787676}.vp_time{ position:absolute; margin:0 3px 0 0; right:0px; background-color:gray; width:45px; text-align:center; border:1px solid darkgrey; color:ghostwhite; text-decoration:none}';
	style += '._vp_widget3{ width: 400px;} .vp_header{ width:100%; text-align:right; background-color:#FDE7CE; border:1px solid gray; text-align:center}.vp_footer{ width:100%; text-align:right; background-color:#FDE7CE; border:1px solid gray}.vp_footer a{margin-right:5px;color:#A01A1A}.vp_list{ float:left; text-align:left; border:1px solid gray; background-color:white; width:100%; overflow:auto; position: relative}.vp_lst_even{background-color:white;padding:4px}.vp_lst_odd{background-color:#FDE7CE;padding:4px;}.vp_lst_active{background-color:#787676}.vp_time{ position:absolute; margin:0 3px 0 0; right:0px; background-color:gray; width:45px; text-align:center; border:1px solid darkgrey; color:ghostwhite; text-decoration:none}';
        // style += ".vp_list img{ border-style:none; padding: 0 2px;} .vp_list a img:hover{ background:gray;}";
        style += '.vp_list a:hover{ color:gray; }';
        // style += ".expand:hover, .collapse:hover {border-top:2px solid gray}";
        style += ".expand {background: url('data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAgAAAAICAQAAABuBnYAAAAAGUlEQVQImWNggIANQIgC8AlsQIOYAiQbCgAUMxNBUqWR0wAAAABJRU5ErkJggg==') left no-repeat; padding: 0 12px; text-decoration:none;}";
        style += ".collapse {background: url('data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAgAAAAICAQAAABuBnYAAAAAEklEQVQIW2NgIANsQIOYAiQDAOcmCwGcy16yAAAAAElFTkSuQmCC') left no-repeat; padding: 0 12px; text-decoration: none}";

        // style += ".vp_top_list:hover, .vp_sub_list:hover {border-top:2px solid gray}";
        // style += ".expand {background: url('data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAgAAAAICAQAAABuBnYAAAAAGUlEQVQImWNggIANQIgC8AlsQIOYAiQbCgAUMxNBUqWR0wAAAABJRU5ErkJggg==') center no-repeat; padding: 0 3px; text-decoration:none;}";
        // style += ".collapse {background: url('data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAgAAAAICAQAAABuBnYAAAAAEklEQVQIW2NgIANsQIOYAiQDAOcmCwGcy16yAAAAAElFTkSuQmCC') center no-repeat; padding: 0 3px; text-decoration: none}";
        style += ".vp_sub_list {padding: 0 12px;}";
        style += ".vp_entry {padding: 0 12px; }";
	//edited_Devanshu
	style +=".squareSelected ul { border: 2px solid #8B8378;position: absolute; width:47px; margin-left:350px; margin-top:40px; float:left; text-align: center; background-color:rgba(173, 255, 47, 1);}";
        // http://www.phpied.com/dynamic-script-and-style-elements-in-ie/
        // do we need this ?
	style +=".squareSelected li { margin-left:-40px;}";
	style +=".list {cursor:pointer;}";
	style +=".listSelected {cursor: default;  background-color:rgba(255, 127, 80, 1);}";
	style +="clear { /* generic container (i.e. div) for floating buttons */    overflow: hidden;    width: 100%;}a.button {    background: transparent url('bg_button_a.gif') no-repeat scroll top right;    color: #FFF;    display: block;    float: left;    font: normal 12px arial, sans-serif;    height: 24px;    margin-right: 6px;    padding-right: 18px; /* sliding doors padding */    text-decoration: none;}a.button span {    background: transparent url('bg_button_span.gif') no-repeat;    display: block;    line-height: 14px;    padding: 5px 0 5px 18px;} a.button:active {    background-position: bottom right;    color: #000;    outline: none; /* hide dotted outline in Firefox */}a.button:active span {    background-position: bottom left;    padding: 6px 0 4px 18px; /* push text down 1px */}";
	style+=".button1 {   border-top: 1px solid #b52b2b;   background: #A01A1A;   background: -webkit-gradient(linear, left top, left bottom, from(#A01A1A), to(#A01A1A));   background: -webkit-linear-gradient(top, #A01A1A,#A01A1A);   background: -moz-linear-gradient(-90deg, #A01A1A, #A01A1A);   background: -ms-linear-gradient(top, #A01A1A, #A01A1A);   background: -o-linear-gradient(top, #A01A1A, #A01A1A);   padding: 3px 6px;   -webkit-border-radius: 4px;   -moz-border-radius: 4px;   border-radius: 4px;   -webkit-box-shadow: rgba(0,0,0,1) 0 1px 0;   -moz-box-shadow: rgba(0,0,0,1) 0 1px 0;   box-shadow: rgba(0,0,0,1) 0 1px 0;   text-shadow: rgba(0,0,0,.4) 0 1px 0;  color: #ffffff;   font-size: 12px;   font-family:arial,helvetica,clean,sans-serif;   text-decoration: none;   vertical-align: middle;   } .button1:hover {   border-top-color: #b31616;   background: #b31616;   color: #f7f7f7;   } .button1:active {   border-top-color: #80111a;   background: #80111a;   } .button2{   margin: 5px;    text-decoration: none;    font: 14px 'Trebuchet MS',Arial, Helvetica; /*Change the em value to scale the button*/    display: inline-block;    text-align: center;    color: #fff;    border: 1px solid #9c9c9c; /* Fallback style */    border: 1px solid rgba(0, 0, 0, 0.3);               text-shadow: 0 1px 0 rgba(0,0,0,0.4);    box-shadow: 0 0 .05em rgba(0,0,0,0.4);    -moz-box-shadow: 0 0 .05em rgba(0,0,0,0.4);    -webkit-box-shadow: 0 0 .05em rgba(0,0,0,0.4);}.button2, .button2 span{    -moz-border-radius: .3em;    border-radius: .3em;}.button2 span{    border-top: 1px solid #fff; /* Fallback style */    border-top: 1px solid rgba(255, 255, 255, 0.5);    display: block;    padding: 0.5em 2.5em;    /* The background pattern */    background-image: -webkit-gradient(linear, 00,100% 100%, color-stop(.25, rgba(0, 0, 0, 0.05)), color-stop(.25, transparent), to(transparent)),-webkit-gradient(linear, 0 100%, 100% 0, color-stop(.25, rgba(0, 0, 0, 0.05)), color-stop(.25, transparent), to(transparent)),                      -webkit-gradient(linear, 0 0, 100% 100%, color-stop(.75, transparent), color-stop(.75, rgba(0, 0, 0, 0.05))),                      -webkit-gradient(linear, 0 100%, 100% 0, color-stop(.75,transparent), color-stop(.75, rgba(0, 0, 0, 0.05)));    background-image: -moz-linear-gradient(45deg, rgba(0, 0, 0, 0.05) 25%, transparent 25%, transparent),                      -moz-linear-gradient(-45deg, rgba(0, 0, 0,0.05) 25%, transparent 25%, transparent),                      -moz-linear-gradient(45deg, transparent 75%, rgba(0, 0, 0, 0.05) 75%),                      -moz-linear-gradient(-45deg, transparent 75%, rgba(0, 0, 0, 0.05) 75%);    /* Pattern settings */    -moz-background-size: 3px 3px;    -webkit-background-size: 3px 3px;    background-size: 3px 3px;}.button2:hover{    box-shadow: 0 0 .1em rgba(0,0,0,0.4);    -moz-box-shadow: 0 0 .1em rgba(0,0,0,0.4);    -webkit-box-shadow: 0 0 .1em rgba(0,0,0,0.4);}.button2:active{    /* When pressed, move it down 1px */    position: relative;    top: 1px;}.button-blue{    background: #A01A1A;    background: -webkit-gradient(linear, left top, left bottom, from(#A01A1A), to(#A01A1A) );    background: -moz-linear-gradient(-90deg, #A01A1A, #A01A1A);    filter:  progid:DXImageTransform.Microsoft.gradient(GradientType=0,startColorstr='#A01A1A',endColorstr='#A01A1A');}.button-blue:hover{    background: #FDE7CE;    background: -webkit-gradient(linear, left top, left bottom, from(#A01A1A), to(A01A1A) );    background: -moz-linear-gradient(-90deg, #A01A1A, #A01A1A);    filter:  progid:DXImageTransform.Microsoft.gradient(GradientType=0,startColorstr='#A01A1A', endColorstr='#A01A1A');}.button-blue:active{    background: #A01A1A;}";
        var s = document.createElement('style');
        s.setAttribute("type", "text/css");
        if (s.styleSheet) {   // IE
            s.styleSheet.cssText = style;
        } else {                // the world
            s.appendChild(document.createTextNode(style));
        }

        $('head').append(s);
    // }


    function log(args) {
        if (typeof console == 'undefined' || typeof console.log == 'undefined') {
            return;
        }
        console.log(arguments);
    }

    (function () {
        var queue = [];

        function register(fn) {
            log('registering', queue.length, fn);
            return queue.push({
                fn: fn,
                enabled: true
            }) - 1;
        }

        function deregister(i) {
            queue[i].enabled = false;
        }

        function reregister(i) {
            queue[i].enabled = true;
        }

        function run_all() {
            function run_first(lst, i) {
                if (lst.length > i)  {
                    setTimeout(function () {
                        if (lst[i].enabled) {
                            try {
                                lst[i].fn();
                            } catch (x) {
                                log(x);
                            }
                        }
                        run_first(lst, i+1);
                    }, 0);      //just to not overhog the frontend.
                } else {
                    setTimeout(run_all, 1000); //every second
                }
            }
            run_first(queue, 0);
        }

        // api
        vp.background = {
            register: register,
            deregister: deregister,
            reregister: reregister
        };
	vp1.background = {
            register: register,
            deregister: deregister,
            reregister: reregister
        };
	vp2.background = {
            register: register,
            deregister: deregister,
            reregister: reregister
        };
	vp2.background = {
            register: register,
            deregister: deregister,
            reregister: reregister
        };


        $('document').ready(run_all);
    }());


    (function (players) {
        // @see http://flowplayer.org/forum/3/13896#post-14191
        function seekVideo(p, seconds) {
            if (p.getState() == 1) {
                _resumeAndSeek(p, seconds);
            } else if (p.isLoaded()) {
                p.seek(seconds);
                if (!p.isPlaying()) p.play();
            } else {
                _loadAndSeek(p, seconds);
            }
        }

        function _resumeAndSeek(p, seconds) {
            var initialLoad = true;
            p.onStart(function () {
                if (!initialLoad) return; // Make sure we don't do this for future onStarts.
                initialLoad = false;
                p.seek(seconds);
                if (!p.isPlaying()) p.play();
            });
            p.play();
        }

        function _loadAndSeek(p, seconds) {
            var initialLoad = true;
            p.onStart(function () {
                if (!initialLoad) return; // Make sure we don't do this for future onStarts.
                initialLoad = false;
                p.seek(seconds);
                if (!p.isPlaying()) p.play();
            });
            p.load();
        }

        players.flowplayer = {
            init: function () {
                this.player =  (typeof flo_obj == 'undefined') ? null : flo_obj;
                this.progress_fn = progress_param_fn(players.data);
                vp.background.register(players.flowplayer.progress);
                log('flow registered',  typeof flo_obj, this.progress_fn);
            },
            play: function (index) {
                var self = players.flowplayer;
                log('flowplayer: ', self, self.player);
                if (!self.player) {
                    return;
                }
                log("play: ", self.player, index);
                g.gevent('seek', 'flowplayer', index);
                seekVideo(self.player, index);
            },
            progress: function () {
                // if not playing, dont do anythin but keep calling
                var self = players.flowplayer;

                self.player = (get_player() == 'flowplayer') ? flo_obj : null;

                if (!self.player) {
                    return;
                }

                var ct = self.player.getTime();

                if (self.player === ct) {
                    ct = 0; // bug with flowplayer.
                }

                if (ct) {
                    self.progress_fn(ct);
                }
            }
        };
    })(players);

    (function (players) {
        players.none = {
            init: function () {
                return;
            },
            play: function (index) {
                log('play: none player');
                return;
            }
        };
    })(players);

    function get_video_src($ele) {
        var src = false;

        src = $ele.attr('src');
        src = src || $ele.find('embed').attr('src');
        src = src || $ele.attr('data');

        return src;
    }


    (function (players){
        players.yt = {
            pid: 'vp_yt_player',
            load: function (ytlink, domid, w, h) {
                // Lets Flash from another domain call JavaScript
                var params = {
                    allowScriptAccess: "always",
                    allowFullScreen: "true"
                };
                log('loading', ytlink, domid, w, h);
                // The element id of the Flash embed
                var atts = {
                    id: players.yt.pid,
                    name: players.yt.pid
                };

                function embed_player() {
                    swfobject.embedSWF( ytlink + "&enablejsapi=1&playerapiid="+players.yt.pid,
                                        domid, w, h, "8", null, null, params, atts);
                }

                if (typeof swfobject == 'undefined') {
                    $.getScript('https://ajax.googleapis.com/ajax/libs/swfobject/2.2/swfobject.js', embed_player);
                } else {
                    embed_player();
                }
            },
            check_n_load: function () {
                $('object').each(function () {
                    var $obj, src, w, h;

                    $obj = $(this);
                    src = get_video_src($obj);
                    if (/youtube.com/.test(src)) {
                        w = $obj.attr('width');
                        h = $obj.attr('height');

                        $obj.attr('id', players.yt.pid);
                        $obj.attr('name', players.yt.pid);

                        log($obj, src, w, h);

                        setTimeout(function () {
                            players.yt.load(src, $obj.attr('id'), w, h);
                        }, 0);cursor:pointer;
                    }
                });
            },
            onready: function () {
                window.ytplayer = $("#"+players.yt.pid);

                log('player: ', ytplayer);
                players.yt.init();
                vp.background.register(players.yt.progress);
                players.yt.on_player_progress();
            },
            init: function () {
                log('yt player initialized', this);
                this.player = document.getElementById(players.yt.pid) || null;
                this.progress_fn = progress_param_fn(players.data);
            },
            play: function (index) {
                var self = this;
                log('self', self, index);
                if (typeof self == 'undefined' || typeof self.player == 'undefined') {
                    return;
                }
                log("yt play: ", self.player, index);
                g.gevent('seek', 'yt', index);
                if (index) {
                    self.player.seekTo(index, true);
                } else {
                    self.player.playVideo();
                }
            },
            progress: function (fn) {
                // if not playing, dont do anythin but keep calling
                var self = players.yt;
                if (get_player() != 'yt') {
                    self.player = null;
                } else if (typeof self.player == 'undefined' || !self.player){
                    self.player = document.getElementById(players.yt.pid);
                }
                if (!self.player) {
                    return;
                }

                var ct = self.player.getCurrentTime();
                if (ct) {
                    self.progress_fn(ct);
                }
            }
        };
    })(players);


    function is_flow_player() {
        return $('object#player_api').filter(':visible').length != 0;
    }

    function get_player() {
        if (is_flow_player()) {
            if (!players.flowplayer.player) {
                players.flowplayer.player = flo_obj;
                g.gevent('player-load', 'flowplayer');
            }
            return 'flowplayer';
        } else if (typeof ytplayer != 'undefined' && ytplayer != null) {
            // players.yt.player initialized by the global function onYoutube..
            // players.yt.player = ytPlayer;
            return 'yt';
        } else {
            return 'none';
        }
    };

    var fwd_video_fn = function (v) {
        return function () {
            log('fn: ', get_player(), v.time);
            players[get_player()].play(v.time);
        };
    };


    // converts seconds to (min, sec) pair. no hr!
    function sec2norm(sec) {
        var m, str = '';
        sec = parseInt(sec);
        m = parseInt(sec / 60);
        if (m) {
            str += m + ':';
        }

        sec = parseInt(sec % 60) + '';

        if (sec.length == 1) {
            sec = '0'+sec;
        }
        str += sec;
        return str;
    }

    function title_hover($div) {
        $div.hover(function () {
            $(this).find('.vp_time').css('display', 'inline');
        }, function () {
            $(this).find('.vp_time').css('display', 'none');
        });
        $div.find('.vp_time').css('display', 'none');
    }

    function make_title(v) {
        // var plus_link = '<a href="javascript:;" class="sublist expand">&nbsp;&nbsp;&nbsp</a>';
        // var indent = '<span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>';
        // var usual = '<span>&nbsp;&nbsp;&nbsp;&nbsp';
	var value = new Array(v.id, v.time, v.title);
        var div = $('<div>').append($('<a>', {
            id    : ('vp_id_'+v.time).replace('.', '_'),
            href  : 'javascript:Opencast.Watch.seekSegment('+v.time+')'
        }).append($('<span>', {
            html: $('<span> <span class="vp_fns"></span><p style="text-align:justify; width:23em;"><span class="vp_title" >'+v.title+'</span></span>').addClass( v.childrens ? 'expand sublist' : v.top ? 'vp_sub_list' : 'vp_entry')
        }).append($('<span>', {
            'class': 'vp_time',
            html: sec2norm(v.time)
        }))).css({'text-decoration':'none'}).data({
            time: v.time,
            title: v.title
        }));

        div.data({
            time: v.time,
            title: v.title
        });

        div.fixClick(
           fwd_video_fn(v)
        , function () {
            log('double');
        });

        // div.css({
        //     position: 'relative'
        // });

        if (v.top) {
            div.hide();
        } else {
            div.addClass('top');
        }

        title_hover(div);

        return div;
    }
    function make_title1(v) {
        // var plus_link = '<a href="javascript:;" class="sublist expand">&nbsp;&nbsp;&nbsp</a>';
        // var indent = '<span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>';
        // var usual = '<span>&nbsp;&nbsp;&nbsp;&nbsp';
        var div = $('<div>').append($('<a>', {
            id    : ('vp_id_'+v.time).replace('.', '_'),
            href  : 'javascript:Opencast.Watch.seekSegment('+v.time+')'
        }).append($('<span>', {
            html: $('<span> <span class="vp_fns"></span>'+'<span class="vp_title" style="font-weight:bold;">'+v.title+'</span></span>').addClass( v.childrens ? 'expand sublist' : v.top ? 'vp_sub_list' : 'vp_entry')
        }).append($('<span>', {
            'class': 'vp_time',
            html: sec2norm(v.time)
        }))).css({'text-decoration':'none'}).data({
            time: v.time,
            title: v.title
        }));

        div.data({
            time: v.time,
            title: v.title
        });

        div.fixClick(
          fwd_video_fn(v)
        , function () {
            log('double');
        });

        // div.css({
        //     position: 'relative'
        // });

        if (v.top) {
            div.hide();
        } else {
            div.addClass('top');
        }

        title_hover(div);

        return div;
    }
     function make_title2(v) {
        // var plus_link = '<a href="javascript:;" class="sublist expand">&nbsp;&nbsp;&nbsp</a>';
        // var indent = '<span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>';
        // var usual = '<span>&nbsp;&nbsp;&nbsp;&nbsp';
        var div = $('<div>').append($('<a>', {
            id    : ('vp_id_'+v.time).replace('.', '_'),
            href  : 'javascript:seekVideo('+v.id+')'
        }).append($('<span>', {
            html: $('<span> <span class="vp_fns"></span>'+'<span class="vp_title" style="font-weight:bold;">'+v.title+'<a style="float:right;color:#ffffff;" href="javascript:showBookmarkDetails(&#39;'+v.media_id+'&#39;);"><img src="http://evideo.iitj.ac.in:81/play/details-icon.png" title="Details"></a></a></span></span><p>'+v.desc+'</p>').addClass( v.childrens ? 'expand sublist' : v.top ? 'vp_sub_list' : 'vp_entry')
        }).append($('<span>', {
            'class': 'vp_time',
            html: sec2norm(v.start)+"-"+sec2norm(v.stop)
        }))).css({'text-decoration':'none'}).data({
            time: v.start,
            title: v.title
        }));

        div.data({
            time: v.start,
            title: v.title
        });

        div.fixClick(
            fwd_video_fn(v)
        , function () {
            log('double');
        });

        // div.css({
        //     position: 'relative'
        // });

        if (v.top) {
            div.hide();
        } else {
            div.addClass('top');
        }

        title_hover(div);

        return div;
    }

	function getTitle()
	{
		var title1=document.formadvanced.title1.value;
		return title1;
	}

	function getCateg()
	{
	
		var categ1=document.formadvanced.category.value;
		return categ1;

	}
	var i=true;
	var hext="FDE7CE";
	var white="00000"
	function make_title3(v) {
	//var title=document.formadvanced.title1.value;
	//var categ=document.formadvanced.categ.value;
	//console.log(i+title);
	var title1=getTitle();
	var categ1=getCateg();
	console.log(v);
        console.log(v.table_name);
	if(i==true)
	{
		if(v.table_name=="annotations"){
		console.log("In Annotations Representation");
        	var div = $('<div style="background:#'+hext+';">').append($('<a>', {
            	id    : ('vp_id_'+v.time).replace('.', '_'),
            	href  : 'http://evideo.iitj.ac.in/engage/ui/watch.html?id='+v.media_id+'&start='+v.start+'&q='+title1+'&cat='+categ1
        	}).append($('<span>', {
            	html: $('<span> <span class="vp_fns"></span>'+'<span class="vp_title" style="font-weight:bold;">'+v.title+'<span style="font-weight:normal;float:right"><a href=javascript:viewAnnDetails(&#39;'+v.media_id+'&#39;><img src="http://evideo.iitj.ac.in:81/anno.png" title="Annotations"></a><a href=javascript:viewOCRDetails(&#39;'+v.media_id+'&#39;)><img src="http://evideo.iitj.ac.in:81/ocr.png" title="OCR Text"></a></span></span></span><p><div><table><tr><td style="font-weight:bold;">Name:</td><td>'+v.media_name+'</td></tr><tr><td style="font-weight:bold;">Author:</td><td>'+v.media_creator+'</td></tr><tr><td style="font-weight:bold;">Duration</td><td>'+v.duration+'</td></tr><tr><td style="font-weight:bold;">Explanation:</td><td>'+v.description+'</td></tr></table></div></p>').addClass( v.childrens ? 'expand sublist' : v.top ? 'vp_sub_list' : 'vp_entry')
        	}).append($('<span>', {
            	'class': 'vp_time',
            	html: sec2norm(v.start)+"-"+sec2norm(v.stop)
        	}))).css({'text-decoration':'none'}).data({
            	time: v.start,
            	title: v.title
        	}));

        	div.data({
            	time: v.start,
            	title: v.title
        	});

        	div.fixClick(
            	fwd_video_fn(v)
        	, function () {
          	  log('double');
        	});

        // div.css({
        //     position: 'relative'
        // });

        	if (v.top) {
            	div.hide();
        	} else {
            	div.addClass('top');
        	}

        	title_hover(div);
		}
	
		else if(v.table_name=="Link"){
		console.log("In link Representation");
	
        	var div = $('<div style="background:#'+hext+';">').append($('<a>', {
            	id    : ('vp_id_'+v.time).replace('.', '_'),
     	       href  : 'http://evideo.iitj.ac.in/engage/ui/watch.html?id='+v.media_id+'&start='+v.start+'&q='+title1+'&cat='+categ1
        	}).append($('<span>', {
        	    html: $('<span> <span class="vp_fns"></span>'+'<span class="vp_title" style="font-weight:bold;">'+v.title+'<a href='+v.url+' style="color:blue;">[View More]</a><span style="font-weight:normal;float:right"><a href=javascript:viewAnnDetails(&#39;'+v.media_id+'&#39;)><img src="http://evideo.iitj.ac.in:81/anno.png" title="Annotation"></a><a href=javascript:viewOCRDetails(&#39;'+v.media_id+'&#39;)><img src="http://evideo.iitj.ac.in:81/ocr.png" title="OCR Text"></a></span></span></span><p><div><table><tr><td style="font-weight:bold;">Name:</td><td>'+v.media_name+'</td></tr><tr><td style="font-weight:bold;">Author:</td><td>'+v.media_creator+'</td></tr><tr><td style="font-weight:bold;">Duration</td><td>'+v.duration+'</td></tr><tr><td style="font-weight:bold;">Explanation:</td><td>'+v.description+'</td></tr></table></div></p>').addClass( v.childrens ? 'expand sublist' : v.top ? 'vp_sub_list' : 'vp_entry')
        	}).append($('<span>', {
        	    'class': 'vp_time',
        	    html: sec2norm(v.start)+"-"+sec2norm(v.stop)
        	}))).css({'text-decoration':'none'}).data({
        	    time: v.start,
        	    title: v.title
        	}));
	
        	div.data({
        	    time: v.start,
        	    title: v.title
        	});
	
        	div.fixClick(
        	    fwd_video_fn(v)
        	, function () {
        	    log('double');
        	});
	
        	// div.css({
        	//     position: 'relative'
        	// });
	
        	if (v.top) {
        	    div.hide();
        	} else {
        	    div.addClass('top');
        	}
	
        	title_hover(div);
		}

		else if(v.table_name=="bookmarks"){
	
		console.log("In bookmarks Representation");
		vp_bid=('vp_bookmark_'+v.start).replace('.','_');
			
		$('"#'+vp_bid+'"').hide();
        	var div = $('<div style="background:#'+hext+';">').append($('<a>', {
        	    id    : ('vp_id_'+v.time).replace('.', '_'),
        	    href  : 'http://evideo.iitj.ac.in/engage/ui/watch.html?id='+v.media_id+'&start='+v.start+'&q='+title1+'&cat='+categ1
        	}).append($('<span>', {
        	    html: $('<span> <span class="vp_fns"></span>'+'<span class="vp_title" style="font-weight:bold;">'+v.title+'<span style="font-weight:normal;float:right"><a href=javascript:viewAnnDetails(&#39;'+v.media_id+'&#39;)><img src="http://evideo.iitj.ac.in:81/anno.png" title="Annotation"></a><a href=javascript:viewOCRDetails(&#39;'+v.media_id+'&#39;)><img src="http://evideo.iitj.ac.in:81/ocr.png" title="OCR Text"></a></span></span></span><p id='+vp_bid+'><div><table><tr><td style="font-weight:bold;">Name:</td><td>'+v.media_name+'</td></tr><tr><td style="font-weight:bold;">Author:</td><td>'+v.media_creator+'</td></tr><tr><td style="font-weight:bold;">Duration</td><td>'+v.duration+'</td></tr><tr><td style="font-weight:bold;">Explanation:</td><td>'+v.description+'</td></tr></table></div></p>').addClass( v.childrens ? 'expand sublist' : v.top ? 'vp_sub_list' : 'vp_entry')
        	}).append($('<span>', {
        	    'class': 'vp_time',
        	    html: sec2norm(v.start)+"-"+sec2norm(v.stop)
        	}))).css({'text-decoration':'none'}).data({
        	    time: v.start,
        	    title: v.title
        	}));
	
        	div.data({
        	    time: v.start,
        	    title: v.title
        	});
	
        	div.fixClick(
        	    fwd_video_fn(v)
        	, function () {
        	    log('double');
        	});
	
        	// div.css({
        	//     position: 'relative'
        	// });
	
        	if (v.top) {
        	    div.hide();
        	} else {
        	    div.addClass('top');
        	}
		//lert(document.getElementById(d).innerHTML);
		
        	title_hover(div);
		}
		else if(v.table_name==undefined)
		{
		console.log("In Related Terms Representation");
		var div = $('<div style="background:#'+hext+';">').append($('<a>', {
        	    id    : ('vp_id_'+v).replace('.', '_'),
        	    href  : 'javascript:sample("'+v.property_text+'")',
        	}).append($('<span>', {
        	    html: $('<span> <span class="vp_fns"></span><span class="vp_title" style="font-weight:bold;">'+v.property_text+'<a href='+v+'</span>').addClass( v.childrens ? 'expand sublist' : v.top ? 'vp_sub_list' : 'vp_entry')
        	}).append($('<span>', {
        	    'class': 'vp_time',
        	    html: sec2norm(v.start)+"-"+sec2norm(v.stop)
        	}))).css({'text-decoration':'none'}).data({
        	    time: v.start,
        	    title: v.title
        	}));
	
        	div.data({
        	    time: v.start,
        	    title: v.title
        	});
	
        	div.fixClick(
        	    fwd_video_fn(v)
        	, function () {
        	    log('double');
        	});
	
        	// div.css({
        	//     position: 'relative'
        	// });
	
        	if (v.top) {
        	    div.hide();
        	} else {
        	    div.addClass('top');
        	}
	
        	title_hover(div);
		}
		//console.log(id);
		i=false;
	}
	else
	{

		if(v.table_name=="annotations"){
		console.log("In Annotations Representation");
        	var div = $('<div style="background:#'+white+';">').append($('<a>', {
            	id    : ('vp_id_'+v.time).replace('.', '_'),
            	href  : 'http://evideo.iitj.ac.in/engage/ui/watch.html?id='+v.media_id+'&start='+v.start+'&q='+title1+'&cat='+categ1
        	}).append($('<span>', {
            	html: $('<span> <span class="vp_fns"></span>'+'<span class="vp_title" style="font-weight:bold;">'+v.title+'<span style="font-weight:normal;float:right"><a href=javascript:viewAnnDetails(&#39;'+v.media_id+'&#39;)><img src="http://evideo.iitj.ac.in:81/anno.png" title="Annotation"></a><a href=javascript:viewOCRDetails(&#39;'+v.media_id+'&#39;)><img src="http://evideo.iitj.ac.in:81/ocr.png" title="OCR Text"></a></span></span></span><p><div><table><tr><td style="font-weight:bold;">Name:</td><td>'+v.media_name+'</td></tr><tr><td style="font-weight:bold;">Author:</td><td>'+v.media_creator+'</td></tr><tr><td style="font-weight:bold;">Duration</td><td>'+v.duration+'</td></tr><tr><td style="font-weight:bold;">Explanation:</td><td>'+v.description+'</td></tr></table></div></p>').addClass( v.childrens ? 'expand sublist' : v.top ? 'vp_sub_list' : 'vp_entry')
        	}).append($('<span>', {
            	'class': 'vp_time',
            	html: sec2norm(v.start)+"-"+sec2norm(v.stop)
        	}))).css({'text-decoration':'none'}).data({
            	time: v.start,
            	title: v.title
        	}));

        	div.data({
            	time: v.start,
            	title: v.title
        	});

        	div.fixClick(
            	fwd_video_fn(v)
        	, function () {
          	  log('double');
        	});

        // div.css({
        //     position: 'relative'
        // });

        	if (v.top) {
            	div.hide();
        	} else {
            	div.addClass('top');
        	}

        	title_hover(div);
		}
	
		else if(v.table_name=="Link"){
		console.log("In link Representation");
	
        	var div = $('<div style="background:#'+white+';">').append($('<a>', {
            	id    : ('vp_id_'+v.time).replace('.', '_'),
     	       href  : 'http://evideo.iitj.ac.in/engage/ui/watch.html?id='+v.media_id+'&start='+v.start+'&q='+title1+'&cat='+categ1
        	}).append($('<span>', {
        	    html: $('<span> <span class="vp_fns"></span>'+'<span class="vp_title" style="font-weight:bold;">'+v.title+'<a href='+v.url+' style="color:blue;">[View More]</a><span style="font-weight:normal;float:right"><a href=javascript:viewAnnDetails(&#39;'+v.media_id+'&#39;)><img src="http://evideo.iitj.ac.in:81/anno.png" title="Annotation"></a><a href=javascript:viewOCRDetails(&#39;'+v.media_id+'&#39;)><img src="http://evideo.iitj.ac.in:81/ocr.png" title="OCR Text"></a></span></span></span><p><div><table><tr><td style="font-weight:bold;">Name:</td><td>'+v.media_name+'</td></tr><tr><td style="font-weight:bold;">Author:</td><td>'+v.media_creator+'</td></tr><tr><td style="font-weight:bold;">Duration</td><td>'+v.duration+'</td></tr><tr><td style="font-weight:bold;">Explanation:</td><td>'+v.description+'</td></tr></table></div></p>').addClass( v.childrens ? 'expand sublist' : v.top ? 'vp_sub_list' : 'vp_entry')
        	}).append($('<span>', {
        	    'class': 'vp_time',
        	    html: sec2norm(v.start)+"-"+sec2norm(v.stop)
        	}))).css({'text-decoration':'none'}).data({
        	    time: v.start,
        	    title: v.title
        	}));
	
        	div.data({
        	    time: v.start,
        	    title: v.title
        	});
	
        	div.fixClick(
        	    fwd_video_fn(v)
        	, function () {
        	    log('double');
        	});
	
        	// div.css({
        	//     position: 'relative'
        	// });
	
        	if (v.top) {
        	    div.hide();
        	} else {
        	    div.addClass('top');
        	}
	
        	title_hover(div);
		}

		else if(v.table_name=="bookmarks"){
	
		console.log("In bookmarks Representation");
		vp_bid=('vp_bookmark_'+v.start).replace('.','_');
			
		$('"#'+vp_bid+'"').hide();
        	var div = $('<div style="background:#'+white+';">').append($('<a>', {
        	    id    : ('vp_id_'+v.time).replace('.', '_'),
        	    href  : 'http://evideo.iitj.ac.in/engage/ui/watch.html?id='+v.media_id+'&start='+v.start+'&q='+title1+'&cat='+categ1
        	}).append($('<span>', {
        	    html: $('<span> <span class="vp_fns"></span>'+'<span class="vp_title" style="font-weight:bold;">'+v.title+'<span style="font-weight:normal;float:right"><a href=javascript:viewAnnDetails(&#39;'+v.media_id+'&#39;)><img src="http://evideo.iitj.ac.in:81/anno.png" title="Annotation"></a><a href=javascript:viewOCRDetails(&#39;'+v.media_id+'&#39;)><img src="http://evideo.iitj.ac.in:81/ocr.png" title="OCR Text"></a></span></span></span><p id='+vp_bid+'><div><table><tr><td style="font-weight:bold;">Name:</td><td>'+v.media_name+'</td></tr><tr><td style="font-weight:bold;">Author:</td><td>'+v.media_creator+'</td></tr><tr><td style="font-weight:bold;">Duration</td><td>'+v.duration+'</td></tr><tr><td style="font-weight:bold;">Explanation:</td><td>'+v.description+'</td></tr></table></div></p>').addClass( v.childrens ? 'expand sublist' : v.top ? 'vp_sub_list' : 'vp_entry')
        	}).append($('<span>', {
        	    'class': 'vp_time',
        	    html: sec2norm(v.start)+"-"+sec2norm(v.stop)
        	}))).css({'text-decoration':'none'}).data({
        	    time: v.start,
        	    title: v.title
        	}));
	
        	div.data({
        	    time: v.start,
        	    title: v.title
        	});
	
        	div.fixClick(
        	    fwd_video_fn(v)
        	, function () {
        	    log('double');
        	});
	
        	// div.css({
        	//     position: 'relative'
        	// });
	
        	if (v.top) {
        	    div.hide();
        	} else {
        	    div.addClass('top');
        	}
		//lert(document.getElementById(d).innerHTML);
		
        	title_hover(div);
		}
		else if(v.table_name==undefined)
		{
		console.log("In Related Terms Representation");
		var div = $('<div style="background:#'+white+';">').append($('<a>', {
        	    id    : ('vp_id_'+v).replace('.', '_'),
        	    href  : 'javascript:sample("'+v.property_text+'")',
        	}).append($('<span>', {
        	    html: $('<span> <span class="vp_fns"></span><span class="vp_title" style="font-weight:bold;">'+v.property_text+'<a href='+v+'</span>').addClass( v.childrens ? 'expand sublist' : v.top ? 'vp_sub_list' : 'vp_entry')
        	}).append($('<span>', {
        	    'class': 'vp_time',
        	    html: sec2norm(v.start)+"-"+sec2norm(v.stop)
        	}))).css({'text-decoration':'none'}).data({
        	    time: v.start,
        	    title: v.title
        	}));
	
        	div.data({
        	    time: v.start,
        	    title: v.title
        	});
	
        	div.fixClick(
        	    fwd_video_fn(v)
        	, function () {
        	    log('double');
        	});
	
        	// div.css({
        	//     position: 'relative'
        	// });
	
        	if (v.top) {
        	    div.hide();
        	} else {
        	    div.addClass('top');
        	}
	
        	title_hover(div);
		}
		//console.log(id);
		i=true;		

	}
        	return div;
		
    }

    function make_all_titles(videos) {
        return $.map(videos, make_title);
    }
	function make_all_titles1(videos) {
        return $.map(videos, make_title1);
    }
	function make_all_titles2(videos) {
        return $.map(videos, make_title2);
    }
	function make_all_titles3(videos) {
        return $.map(videos, make_title3);
    }
    function block_for_time(vds, sec) {
        var i;

        if (typeof sec === 'undefined' || sec === null) {
            return vds[0];
        }

        for (i=0; i<vds.length; i++) {
            if (vds[i].time > sec) {
                break;
            }
        }
        i--;

        if (i<0) {
            return null;
        }

        return vds[i];
    };

    vp.scroll_to_point = true;
	vp1.scroll_to_point = true;
	vp2.scroll_to_point = true;
	vp3.scroll_to_point=true;
    function progress_param_fn(vds) {
        var last_v = null;
        return function (sec) {
            var v = block_for_time(vds, sec),
                id,
                $vpl = $('.vp_list'),
                scroll = 0;

            if (!vp.scroll_to_point) {
                return;
            }

            if (!v) {
                return;
            }

            if (last_v == v) {
                return;
            }
            last_v = v;

            id = ('#vp_id_'+v.time).replace('.', '_');

            // highlight the relavent block
            $vpl.children().removeClass('vp_lst_active');
            $(id).parent().addClass('vp_lst_active');

            // scroll to the point
            scroll = $vpl.scrollTop() + $(id).offset().top - $vpl.offset().top;
            $vpl.scrollTop(scroll - 100);

            g.gevent('progress', get_player(), sec);
        };
    };

    var app = {};

    app.dom = {
        style: {

        },
	//edited_Devanshu
        create_header: function (str) {
            return $('<div>', {
                id: 'vp_header',
                'class': 'vp_header',
                'html': '<span><a style="color:#A01A1A;float:left;display:inline-block;" href="http://medialab.iitj.ac.in">'+str+'</a></span><a id="close" style="margin-left:125px;color:#ffffff;display:inline-block;" class="button1"><span>Close</span></a>'
		
            });
        },
	create_header1: function (str) {
            return $('<div>', {
                id: 'vp_header',
                'class': 'vp_header',
                'html': '<a style="color:#A01A1A" href="http://medialab.iitj.ac.in">'+str+'</a>&nbsp;&nbsp;<img id="close" style="margin-left:125px;cursor:pointer;" src="close_button.png">'
		
            });
        },
	create_header2: function (str) {
            return $('<div>', {
                id: 'vp_header',
                'class': 'vp_header',
                'html': '<a style="color:#A01A1A" href="http://medialab.iitj.ac.in">'+str+'</a>&nbsp;&nbsp;<a id="close" style="margin-left:100px;cursor:pointer;color:#ffffff;" class="button1">Close</a>'
		
            });
        },
	
	create_header3: function (str) {
            return $('<div>', {
                id: 'vp_header',
                'class': 'vp_header',
                'html': '<a style="color:#A01A1A" href="http://medialab.iitj.ac.in">'+str+'</a>&nbsp;&nbsp;<a id="close" style="margin-left:100px;cursor:pointer;color:#ffffff;" class="button1">Close</a>'
		
            });
        },	

        create_footer: function () {
            return $('<div>', {
                id: 'vp_footer',
                'class': 'vp_footer',
                'html': $('<a>', {
                    'href': "http://iitj.ac.in",
                    'html': 'Powered by IIT Rajasthan'
                })
            });
        },
        create_list: function (w) {
            var res= $('<div>', {
                id: 'vp_list',
                'class': 'vp_list'
            }).css('height', Math.round(w * 10 / 8) + 'px');
            return res;
        }
    };

    function get_widget_div() {
        var c = '_vp_widget',
            cq = '.'+c,
            $vp;

        $vp = $(cq);

//not getting

        if ($vp.length == 0 && /nptel.iitm.ac.in/.test(document.location.host)) {
            if ($('table.tableheader:contains("Browse by concepts")').length) {
                $('table.tableheader:contains("Browse by concepts")').replaceWith('<div class="'+c+'"></div>');
                log('created one:', c, $(cq), $(cq).css('width'));
            } else {
                $('table:contains("Feedback")').filter(":contains('Coordinators')").last().parent().append('<div class="'+c+'"></div>');
                log('created one afresh:', c, $(cq), $(cq).css('width'));
            }
            $vp = $(cq);
        }

        $vp.html('');

        return $vp;
    }
 function get_widget_div1() {
        var c = '_vp_widget1',
            cq = '.'+c,
            $vp1;

        $vp1 = $(cq);

//not getting

        if ($vp1.length == 0 && /nptel.iitm.ac.in/.test(document.location.host)) {
            if ($('table.tableheader:contains("Browse by concepts")').length) {
                $('table.tableheader:contains("Browse by concepts")').replaceWith('<div class="'+c+'"></div>');
                log('created one:', c, $(cq), $(cq).css('width'));
            } else {
                $('table:contains("Feedback")').filter(":contains('Coordinators')").last().parent().append('<div class="'+c+'"></div>');
                log('created one afresh:', c, $(cq), $(cq).css('width'));
            }
            $vp1 = $(cq);
        }

        $vp1.html('');

        return $vp1;
    }
function get_widget_div2() {
        var c = '_vp_widget2',
            cq = '.'+c,
            $vp2;

        $vp2 = $(cq);

//not getting

        if ($vp2.length == 0 && /nptel.iitm.ac.in/.test(document.location.host)) {
            if ($('table.tableheader:contains("Browse by concepts")').length) {
                $('table.tableheader:contains("Browse by concepts")').replaceWith('<div class="'+c+'"></div>');
                log('created one:', c, $(cq), $(cq).css('width'));
            } else {
                $('table:contains("Feedback")').filter(":contains('Coordinators')").last().parent().append('<div class="'+c+'"></div>');
                log('created one afresh:', c, $(cq), $(cq).css('width'));
            }
            $vp2 = $(cq);
        }

        $vp2.html('');

        return $vp2;
    }

function get_widget_div3() {
        var c = '_vp_widget3',
            cq = '.'+c,
            $vp3;

        $vp3 = $(cq);

//not getting

        if ($vp3.length == 0 && /nptel.iitm.ac.in/.test(document.location.host)) {
            if ($('table.tableheader:contains("Browse by concepts")').length) {
                $('table.tableheader:contains("Browse by concepts")').replaceWith('<div class="'+c+'"></div>');
                log('created one:', c, $(cq), $(cq).css('width'));
            } else {
                $('table:contains("Feedback")').filter(":contains('Coordinators')").last().parent().append('<div class="'+c+'"></div>');
                log('created one afresh:', c, $(cq), $(cq).css('width'));
            }
            $vp3 = $(cq);
        }

        $vp3.html('');

        return $vp3;
    }

//not getting
    function collapse_titles(data) {
        return $.map(data, function (e, i) {
            var prev = data[i-1];
            if (i>0 && $.trim(e.title) == $.trim(prev.title)) {
               log("data e", e.title);
                if (!prev.top) {
                    prev.childrens = [];
                } else {
                    prev = prev.top;
                }
                prev.childrens.push(e);
                e.top = prev;
	
                return e;
            }
		// log("collapse_titles", e);
            return e;
        });
    }
	
	function create_menu()
	{
		$("<div class='squareSelected'><ul class='list' style='list-style-type:none;'><li class='listSelected'>Annotation<li id='doc'>Document<li>Link<li>Playlist</ul></div>").appendTo("._vp_widget");
		$("div.squareSelected").hide();
		/*var $vp1 = get_widget_div(),
            $vpl1 = $vp.find('.vp_list');
	$vpl1 = app.dom.create_list(120);
       
        $vp1.append($vpl1);*/
        
	}
	var $vp1;
	
    function create_sidebar1(data,str) {
        // data should be clean. Ex: shouldnt break the json structure.
        log("logging data", data);
        console.log("In load App inside create_sidebar");
	console.log("string value:",data);
	$vp1 = get_widget_div1();
         var $vpl = $vp1.find('.vp_list'),
            $footer = $vp1.find('.vp_footer'),
            $header = $vp1.find('.vp_header'),
            owidth,
            w,
            old;

        data = collapse_titles(data);
        log("data", data);

        log('width: ', $vp1.css('width'));
//        w = parseInt($vp1.css('width'));

        w=$(window).height();
	h=$(window).width();
	$vp1.css('width',parseInt(h/4.5));
	$footer.css('width',parseInt(h/4.5));
	$header.css('width',parseInt(h/4.5));

        // header
        $header = app.dom.create_header1(str);
        $vp1.append($header);

        log('creating header: ', $header);

        // index list
        $vpl = app.dom.create_list(w);
        log('creating list: ', w, $vpl);
        $vp1.append($vpl).show();
        log('showing list: ', $vpl);

        // footer
        $footer = app.dom.create_footer();
        log('creating footer: ');
        $vp1.append($footer);

        $.each(make_all_titles1(data), function (i, v) {
            $vpl.append(v);
        });

        $vpl.children('div').addClass('vp_lst_even');
        $vpl.children('div').filter(':visible').filter(':odd').addClass('vp_lst_odd');
        // $vpl.children('div:odd').css({'background-color':'lightgray'}).end()
        //     .children('div').css('padding', '4px').end();
	
        $vp1.show();
	//edited_Devanshu

	//creating menu

	//$("<div class='squareSelected'><ul class='list' style='list-style-type:none;'><li class='listSelected'>Annotation<li id='doc'>Document<li>Link<li>Playlist</ul></div>").appendTo(".menu_place");
	//	$("div.squareSelected").hide();


	//on clicking document menu

		
	/* var $vp1 = get_widget_div(),
            $vpl1 = $vp.find('.vp_list');
	$vpl1 = app.dom.create_list(120);
        log('creating list: ', 120, $vpl1);
        $vp1.append($vpl1).show();
        log('showing list: ', $vpl);*/
        $('#close',$vp1).live('click',function(e){
	$vp1.hide();	
	});
        $('.expand', $vp1).live('click', function (e) {
            var $self = $(this);
            e.preventDefault();
            e.stopPropagation();
            var sublist = $self.closest('div').nextUntil('.top');

            $self.removeClass('expand').addClass('collapse');
            sublist.first().css('border-top', '2px solid gray')
                .end().last().css('border-bottom', '2px solid gray');
            sublist.filter(':odd').addClass('vp_lst_odd').end().show();
		
        });
        $('.collapse', $vp1).live('click', function (e){
            var $self = $(this);
            e.preventDefault();
            e.stopPropagation();
            var sublist = $self.closest('div').nextUntil('.top');

            $self.removeClass('collapse').addClass('expand');
            sublist.filter(':odd').removeClass('vp_lst_odd').end().hide();
        });

        players.data = data;

        // initialize the players
        players.flowplayer.init();
        players.yt.check_n_load();

        g.gevent('widget-load', get_player());
    };


	var $vp2;

function create_sidebar2(data,str) {
        // data should be clean. Ex: shouldnt break the json structure.
        log("logging data", data);
        console.log("In load App inside create_sidebar2");
	console.log("string value:",data);
	$vp2 = get_widget_div2();
         var $vpl = $vp2.find('.vp_list'),
            $footer = $vp2.find('.vp_footer'),
            $header = $vp2.find('.vp_header'),
            owidth,
            w,
            old;

        data = collapse_titles(data);
        log("data", data);

        log('width: ', $vp2.css('width'));
//        w = parseInt($vp2.css('width'));

        w=$(window).height();
	h=$(window).width();
	$vp2.css('width',parseInt(h/4.5));
	$footer.css('width',parseInt(h/4.5));
	$header.css('width',parseInt(h/4.5));


        // header
        $header = app.dom.create_header2(str);
        $vp2.append($header);

        log('creating header: ', $header);

        // index list
        $vpl = app.dom.create_list(w);
        log('creating list: ', w, $vpl);
        $vp2.append($vpl).show();
        log('showing list: ', $vpl);

        // footer
        $footer = app.dom.create_footer();
        log('creating footer: ');
        $vp2.append($footer);

        $.each(make_all_titles2(data), function (i, v) {
            $vpl.append(v);
        });

        $vpl.children('div').addClass('vp_lst_even');
        $vpl.children('div').filter(':visible').filter(':odd').addClass('vp_lst_odd');
        // $vpl.children('div:odd').css({'background-color':'lightgray'}).end()
        //     .children('div').css('padding', '4px').end();
	
        $vp2.show();
	//edited_Devanshu

	$('#close',$vp2).live('click',function(e){
	$vp2.hide();	
	});
        $('.expand', $vp2).live('click', function (e) {
            var $self = $(this);
            e.preventDefault();
            e.stopPropagation();
            var sublist = $self.closest('div').nextUntil('.top');

            $self.removeClass('expand').addClass('collapse');
            sublist.first().css('border-top', '2px solid gray')
                .end().last().css('border-bottom', '2px solid gray');
            sublist.filter(':odd').addClass('vp_lst_odd').end().show();
		
        });
        $('.collapse', $vp2).live('click', function (e){
            var $self = $(this);
            e.preventDefault();
            e.stopPropagation();
            var sublist = $self.closest('div').nextUntil('.top');

            $self.removeClass('collapse').addClass('expand');
            sublist.filter(':odd').removeClass('vp_lst_odd').end().hide();
        });

        players.data = data;

        // initialize the players
        players.flowplayer.init();
        players.yt.check_n_load();

        g.gevent('widget-load', get_player());
    };
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
var $vp3;

function create_sidebar3(data,str,title,categ,user) {
	console.log(title);
        // data should be clean. Ex: shouldnt break the json structure.
        log("logging data", data);
        console.log("In load App inside create_sidebar3");
	console.log("string value:",data);
	$vp3 = get_widget_div3();
         var $vpl = $vp3.find('.vp_list'),
            $footer = $vp3.find('.vp_footer'),
            $header = $vp3.find('.vp_header'),
            owidth,
            w,
            old,
	    flag=0;

        data = collapse_titles(data);
        log("data", data);

        log('width: ', $vp3.css('width'));
//        w = parseInt($vp3.css('width'));

        w=$(window).height();
	h=$(window).width();
	$vp3.css('width',parseInt(h/4.5));
	$footer.css('width',parseInt(h/4.5));
	$header.css('width',parseInt(h/4.5));


        // header
        $header = app.dom.create_header3(str);
        $vp3.append($header);

        log('creating header: ', $header);

        // index list
        $vpl = app.dom.create_list(w);
        log('creating list: ', w, $vpl);
        $vp3.append($vpl).show();
        log('showing list: ', $vpl);

	$vpl.append('<div>You Searched for "'+title+'"</div>');

	if(categ!="all" && categ!="bookmarks")
	{
		
			$.each(make_all_titles3(data), function (i, v) {
	            $vpl.append(v);
	        });
		$vpl.append('<div>Related Terms</div>');
		$.getJSON('http://evideo.iitj.ac.in:81/read-xml-trans.php?title='+title+'&jsoncallback=?', function(r4){
		console.log("Inside getJSONterms related terms");
	       	console.log(JSON.stringify(r4));
		$.each(make_all_titles3(r4), function (i,v) {
			$vpl.append(v);		
		});
						
	});
	}
	
	else if(categ==="all")
	{
		var load = [ { url: 'http://evideo.iitj.ac.in:81/search_data.php?title=' + title + '&run=annotations&jsoncallback=?', before: null },
			     { url: 'http://evideo.iitj.ac.in:81/search_data.php?title=' + title + '&run=Link&jsoncallback=?', before: null },
    			   //{ url: 'http://evideo.iitj.ac.in:81/search_data.php?title=' + title + '&user=' + user + '&run=bookmarks&jsoncallback=?', before: null },
   			     { url: 'http://evideo.iitj.ac.in:81/read-xml-trans.php?title=' + title + '&jsoncallback=?', before: function() { $vpl.append('<div>Related Terms</div>'); } }
			   ];
		async.forEachSeries(load, function(item, next) {
			console.log("inside load");
			if(item.before) {
				item.before();
			}
			$.getJSON(item.url, function(r) {
				console.log("inside getJSON and view");
				var i=true;
				$.each(make_all_titles3(r), function (i, v) {
					console.log("Fixing You!");
					$vpl.append(v);
				});
        			next();
    			});
		});
	}

	else
	{
		$vpl.append("<div>You don't have sufficient privileges to search bookmarks!</div>");
	}

	// footer
	  $footer = app.dom.create_footer();
        log('creating footer: ');
        $vp3.append($footer);

        $vpl.children('div').addClass('vp_lst_even');
        $vpl.children('div').filter(':visible').filter(':odd').addClass('vp_lst_odd');
        // $vpl.children('div:odd').css({'background-color':'lightgray'}).end()
        //     .children('div').css('padding', '4px').end();
	
        $vp3.show();
	//edited_Devanshu

	$('#close',$vp3).live('click',function(e){
	$vp3.hide();	
	});
        $('.expand', $vp3).live('click', function (e) {
            var $self = $(this);
            e.preventDefault();
            e.stopPropagation();
            var sublist = $self.closest('div').nextUntil('.top');

            $self.removeClass('expand').addClass('collapse');
            sublist.first().css('border-top', '2px solid gray')
                .end().last().css('border-bottom', '2px solid gray');
            sublist.filter(':odd').addClass('vp_lst_odd').end().show();
		
        });
        $('.collapse', $vp3).live('click', function (e){
            var $self = $(this);
            e.preventDefault();
            e.stopPropagation();
            var sublist = $self.closest('div').nextUntil('.top');

            $self.removeClass('collapse').addClass('expand');
            sublist.filter(':odd').removeClass('vp_lst_odd').end().hide();
        });

        players.data = data;

        // initialize the players
        players.flowplayer.init();
        players.yt.check_n_load();

        g.gevent('widget-load', get_player());
    };
///////////////////////////////////////////////////////////////////////////
	var vp;
    function create_sidebar(data,str) {
        // data should be clean. Ex: shouldnt break the json structure.
        log("logging data", data);
        console.log("In load App inside create_sidebar");
	console.log("string value:",data);
	$vp = get_widget_div();
         var $vpl = $vp.find('.vp_list'),
            $footer = $vp.find('.vp_footer'),
            $header = $vp.find('.vp_header'),
            owidth,
            w,
            old;

        data = collapse_titles(data);
        log("data", data);

        log('width: ', $vp.css('width'));
        //w = parseInt($vp.css('width'));

        w=$(window).height();
	h=$(window).width();
	$vp.css('width',parseInt(h/4.5));
	$footer.css('width',parseInt(h/4.5));
	$header.css('width',parseInt(h/4.5));


        // header
        $header = app.dom.create_header(str);
        $vp.append($header);

        log('creating header: ', $header);

        // index list
        $vpl = app.dom.create_list(w);
        log('creating list: ', w, $vpl);
        $vp.append($vpl).show();
        log('showing list: ', $vpl);

        // footer
        $footer = app.dom.create_footer();
        log('creating footer: ');
        $vp.append($footer);

        $.each(make_all_titles(data), function (i, v) {
            $vpl.append(v);
        });

        $vpl.children('div').addClass('vp_lst_even');
        $vpl.children('div').filter(':visible').filter(':odd').addClass('vp_lst_odd');
        // $vpl.children('div:odd').css({'background-color':'lightgray'}).end()
        //     .children('div').css('padding', '4px').end();
	
        $vp.show();
	//edited_Devanshu

	
        $('#close',$vp).live('click',function(e){
	$vp.hide();	
	});
        $('.expand', $vp).live('click', function (e) {
            var $self = $(this);
            e.preventDefault();
            e.stopPropagation();
            var sublist = $self.closest('div').nextUntil('.top');

            $self.removeClass('expand').addClass('collapse');
            sublist.first().css('border-top', '2px solid gray')
                .end().last().css('border-bottom', '2px solid gray');
            sublist.filter(':odd').addClass('vp_lst_odd').end().show();
		
        });
        $('.collapse', $vp).live('click', function (e){
            var $self = $(this);
            e.preventDefault();
            e.stopPropagation();
            var sublist = $self.closest('div').nextUntil('.top');
		
            $self.removeClass('collapse').addClass('expand');
            sublist.filter(':odd').removeClass('vp_lst_odd').end().hide();
        });
	//var $l=$(".vp_list").children().length();
	
	
		
		
	

      /*  players.data = data;

        // initialize the players
        players.flowplayer.init();
        players.yt.check_n_load();

        g.gevent('widget-load', get_player());*/
    };


    window.onYouTubePlayerReady = function (playerId) {
        log('youtube ready');
        g.gevent('player-load', 'yt');
        players.yt.onready();
    };

    vp.create_sidebar = create_sidebar;
     vp1.create_sidebar1 = create_sidebar1;
    vp2.create_sidebar2 = create_sidebar2;
    vp3.create_sidebar3=create_sidebar3;
    function get_nptel_vid() {
        var $vp = get_widget_div(),
            vid = $vp.attr('data-vid');

        if (vid) {
            return vid;
        }

        // try the uri, for the vid
        var vsearch = window.location.search.substr(1).split('&')[1];
        if (vsearch) {
            return vsearch.split('=')[1];
        }

        // try the first a in lectures.
        vid = $('#videolinks').find('a').first().attr('id');
        if (vid) {
            return vid;
        }

        return false;
    }
    vp.get_nptel_vid = get_nptel_vid;

    function get_nptel_lecture_string() {
        var href = $('span#player').attr('href'),
            objs,
            l,
            i;

        function get_c_n_f_from_path(path) { //course and file
            var l;
            if (!path) {
                return false;
            }

            l = path.split('/');

            if (l.length < 3) {
                return false;
            }

            return [l[l.length-2], l[l.length-1].split('.')[0]];
        }

        objs = ['span#player', 'a[href$=".mp4"]', 'a[href$=".3gp"]'];
        for (i in objs) {
            l = get_c_n_f_from_path($(objs[i]).attr('href'));
            if ($.isArray(l)) {
                break;
            }
        }

        if (document.location.host == 'www.videopulp.in' || document.location.host == 'evideo.iitj.ac.in:4000')  {
            l = ['1074', 'lec01'];
        }

        if (document.location.host == 'evideo.iitj.ac.in') {
            l = ['webex', 'demo'];
        }

        if ($.isArray(l)) {
            return l.join('-');
        }

        return false;
    }
    vp.get_nptel_lecture_string = get_nptel_lecture_string;

    function mixpannel_init () {
        g._gaq = g._gaq || [];
        g._gaq.push(
            ['vp._setAccount', 'UA-20154189-2'],
            ['vp._trackPageview']
        );
        g._gaq.push();

        if (!local) {
            // $.getScript('http://www.google-analytics.com/ga.js');
            // $.getScript('http://api.mixpanel.com/site_media/js/api/mixpanel.js');
        }

    }

    vp.load_app = function (str) {
            console.log("In load App");
		
		 $.getJSON('http://evideo.iitj.ac.in:81/json-create-Annotation.php?media_id='+videoid+'&jsoncallback=?', function(r) {
 		console.log("Inside getJSON");
                      console.log(JSON.stringify(r));
                      create_sidebar(r,str);
			
 
		});
	
    };
vp1.load_app1 = function (str) {
        console.log("In load App");
		 $.getJSON('http://evideo.iitj.ac.in:81/json-create.php?media_id='+videoid+'&jsoncallback=?', function(r) {
 		console.log("Inside getJSON");
                      console.log(JSON.stringify(r));
                      //create_sidebar(r,str);
			create_sidebar1(r,str);
   		//console.log(JSON.stringify(data));
 
		});
    };
vp2.load_app2 = function (str) {
        console.log("In load App");
		 $.getJSON('http://evideo.iitj.ac.in:81/json-get-bookmark.php?user='+user+'&jsoncallback=?', function(r) {
 		console.log("Inside getJSON");
                      console.log(JSON.stringify(r));
                      //create_sidebar(r,str);
			create_sidebar2(r,str);
   		//console.log(JSON.stringify(data));
 
		});
    };

vp3.load_app3 = function (str) {
        console.log("In load App");
		var title=document.formadvanced.title1.value;
		var categ=document.formadvanced.category.value;
		if(QueryString.q!=undefined && QueryString.cat!=undefined)
		{
			title=QueryString.q;//alert(title);
			categ=QueryString.cat;//alert(categ);
		}
		 $.getJSON('http://evideo.iitj.ac.in:81/search_data.php?user='+user+'&title='+title+'&run='+categ+'&jsoncallback=?', function(r) {
 		console.log("Inside getJSON");
                      console.log(JSON.stringify(r));
			var count=r.length;
			create_sidebar3(r,str,title,categ,user);
 		
		});
    };
	
    sample=function(v){
	
	$.getJSON('http://evideo.iitj.ac.in:81/search_data.php?user='+user+'&title='+v+'&run=annotations&jsoncallback=?', function(r){
		console.log("Inside Related Search Terms");
		create_sidebar3(r,"Search",v,"all",user);
	});
	
	};

    // actual call to api
    $(document).ready(function () {
        vp.load_app("Annotations");
	$("#tb1").click(function(){
		  vp.load_app("Annotations");
			console.log("it is hide");
		 $(this).css('background-color','#FDE7CE');
		 $(this).css('color','#000000');
		
		//$vp.show();	
	
	});
	$("#addedAnnotate").click(function(){
		 
		  console.log("it is hide");
		  vp.load_app("Annotations");
		  $(this).css('background-color','#FDE7CE');
		 $(this).css('color','#000000');
	
	});

	$("#appendedAnnotation").click(function(){
		 
		  console.log("it is hide");
		  vp.load_app("Annotations");
		  $(this).css('background-color','#FDE7CE');
		 $(this).css('color','#000000');
	
	});

	$("#tb3").click(function(){
		
		vp1.load_app1("Links");	
		
		console.log("link tab clicked");
	
	});
	$("#tb4").click(function(){
		
		vp2.load_app2("Bookmarks");	
		
		console.log("Bookmark tab clicked");
		$(this).css('background-color','#FDE7CE');
		 $(this).css('color','#000000');
		$("#tb1").css('background-color','#A62727');
		$("#tb1").css('color','#FDE7CE');
		
	
	});
	$("#tb5").click(function(){
		
		vp3.load_app3("Search");	
		
		console.log("Search tab clicked");
			
	});

	$('#category').change(function() {
		vp3.load_app3("Search");
		$("#tabs1").hide();
		$("#tabs3").hide();
		$("#tabs4").hide();
		$("#tabs5").show();
		  
	});

	$("#tb6").click(function(){
		
		vp3.load_app3("Search");	
		
		console.log("Search tab clicked");
			
	});

	$("#addedLink").click(function(){
		 
		  console.log("it is hide");
		  vp1.load_app1("Links");
	
	});
	$("#deletedLink").click(function(){
		 
		  console.log("it is hide");
		  vp1.load_app1("Links");
	
	});
	$("#deletedAnnotation").click(function(){
		 
		  console.log("it is hide");
		 vp.load_app("Annotations");
	
	});
	$("#editedAnnotation").click(function(){
		 
		  console.log("it is hide");
		 vp.load_app("Annotations");
	
	});
	$("#updatedLink").click(function(){
		 
		  console.log("it is hide");
		  vp1.load_app1("Links");
	
	});
	$("#addBookmark").click(function(){
		 
		  console.log("it is hide");
		  vp2.load_app2("Bookmarks");
	
	});
	$("#updatedBookmark").click(function(){
		 
		  console.log("it is hide");
		  vp2.load_app2("Bookmarks");
	
	});
	$("#deletedBookmark").click(function(){
		 
		  console.log("it is hide");
		  vp2.load_app2("Bookmarks");
	
	});


	$(window).resize(function(){

		console.log("Window is being resized!");
		console.log("Window width: "+$(window).width());
		console.log("Document width: "+$(document).width());
		vp.load_app("Annotations");
		vp2.load_app2("Bookmarks");
		vp1.load_app1("Links");
		vp3.load_app3("Search");
//		var width=$(window).width();
		
	});

	$("#logout").click(function(){
	console.log("writing cookie");
	writeCookie('sessionId', "", 3);
	user=readCookie('sessionId');
	console.log(user);  	
	});
    });

    g._vpulp = vp;
    vp.players = players;

})(jQuery);
