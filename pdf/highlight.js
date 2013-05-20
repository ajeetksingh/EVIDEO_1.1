
//function for loading the stored highlights from database
function loadHighlight(currentPDF, page)
{
	var currentpdf = currentPDF;	
	var pdfHighlightObj, objLength;
	$.get('queryHighlight.php', 
		{pname: "\'"+currentpdf+"\'", container: "\'"+page+"\'", cCode: "highlight"}, 
		function(replyData) 
			{ 
			pdfHighlightObj = $.parseJSON(replyData);
			objLength = pdfHighlightObj.length; 
			for(var j = 0; j<objLength; ++j)
			{
			var pdfPageContainer = pdfHighlightObj[j]['pagecontainer'];
			var pdfInitialIndex = pdfHighlightObj[j]['index_initial'];
			var pdfFinalIndex = pdfHighlightObj[j]['index_final'];
			var containStr = String(pdfPageContainer);
			var txtDivision = $(containStr + " .textLayer div");
				for(var i = pdfInitialIndex; i<=pdfFinalIndex; ++i)
				{ 
				txtDivision[i].style.opacity = 0.35;
				txtDivision[i].style.backgroundColor = "lightblue";					
				}
		}
	} );
};


// function for checking the click of highlighted text of pdf
function clickHighlight(divNo, divContainer, currentPDF, callback){
	var pdfHighlightObj, objLength, pdfVideoTime;
	$.get('queryHighlight.php', 
		{pname: "\'"+currentPDF+"\'", cCode: "click", page: "\'"+divContainer+"\'"}, 
		function(replyData) { 
		pdfHighlightObj = jQuery.parseJSON(replyData);
		objLength = pdfHighlightObj.length;
		for(var i = 0; i<objLength; ++i ){

			var pdfInitialIndex = pdfHighlightObj[i]['index_initial'];
			var pdfFinalIndex = pdfHighlightObj[i]['index_final'];
			var tempNum = parseInt(divNo);
			if(tempNum >= pdfInitialIndex && tempNum <= pdfFinalIndex){
				pdfVideoTime = parseFloat(pdfHighlightObj[i]['startTime']);
				callback(pdfVideoTime);
			}
		}
	
	} );

};

// function for scrolling the pdf to syncronised text with video time
function scrollpdf(pdfname,time)
{
	$.get('queryHighlight.php',
		{pname: "\'"+pdfname+"\'", cCode: "scroll"},
		function(replyData)
		{ 
			var pdfObj = jQuery.parseJSON(replyData);
			var len = pdfObj.length;
			
			for(var i = 0; i<len; ++i)
			{
				var sTime = parseFloat(pdfObj[i]['startTime']);
				var eTime = parseFloat(pdfObj[i]['endTime']);
				var pageToJump = parseInt(pdfObj[i]['pageNo']);
				
				if(time>=sTime && time<=eTime)
				{
					PDFView.pages[--pageToJump].scrollIntoView();
					break;
				}
			}
		});

};

//Function for alteration of highlight
function alterHighlight(oldStartIndex, oldFinalIndex, newStartIndex, newFinalIndex) {
	$.get('alterHighlight.php',{ oldInitial: oldStartIndex, oldFinal: oldFinalIndex, newInitial: newStartIndex, newFinal: newFinalIndex, rCode: "alterH"},
		function(replyData) {
		});	
}

//Function for alteration of time
function alterTime(startIndex, finalIndex, startTime, endTime) {
	$.get('alterHighlight.php',
		{sIndex: startIndex, fIndex: finalIndex, sTime: startTime, eTime: endTime, rCode: "alterT"},
		function(replyData) {});
}

//Function for removal of highlight completely
function removeHighlight(startIndex, finalIndex) {
	$.get('alterHighlight.php',
		{sIndex: startIndex, fIndex: finalIndex, rCode: "remove"},
		function(replyData) {});
}

//Function for removal color of highlighted text
function colorRemove(index_i, index_f, pagecontainer) {
	
	var txtDivision = $(pagecontainer + " .textLayer div");
	for(var i = index_i; i<=index_f; ++i)
	{ 
		txtDivision[i].style.backgroundColor = "transparent";					
	}
}
