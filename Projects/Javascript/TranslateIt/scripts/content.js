/*
*Translate It Plugin.
*A popover based plugin , which displays the translated text in the popover 
*as per the user preference.
*
*Powered by Yandex Translation API
*Developed By Vandit Sharma(vanditsharma001@gmail.com)
*/


(function($) {
	var key="trnsl.1.1.20160909T103949Z.cd2d55f0764e78cb.f598c8b4282d6d69834ac3273b72fa7de737ecbf";
	var targetLang="ru";
	//Append popover to view
	var popover = document.createElement('div');
	popover.setAttribute('class', 'translateit-popover');
	document.body.appendChild(popover);


	//Shows popover
	function showPopover(mouseX, mouseY, selection) {

		if(mouseY<100 && mouseY<180 && mouseX >90){
			popover.style.top  = (mouseY + window.scrollY) +'px';
			popover.style.left = (mouseX + window.scrollX-90) + 'px';
		}
		else if(mouseY>100 && mouseY<180 && mouseX >90){
			popover.style.top  = (mouseY + window.scrollY) +'px';
			popover.style.left = (mouseX + window.scrollX-90) + 'px';
		}
		else if(mouseY<180 && mouseX <90){	
			popover.style.top  = (mouseY + window.scrollY) +'px';
			popover.style.left = (mouseX + window.scrollX-10) + 'px';
		}else{
			popover.style.top  = (mouseY + window.scrollY-180) +'px';
			popover.style.left = (mouseX + window.scrollX-90) + 'px';
		}
		var translation=getTranslation(targetLang,selection.toString());
		
	}
	
	getTranslation=function(targetLang,text){
		$.ajax({
		  method: "POST",
		  url: "https://translate.yandex.net/api/v1.5/tr.json/detect",
		  data: { key: key, text: text }
		}).done(function(detectData) {
			  $.ajax({
			  method: "POST",
			  url: "https://translate.yandex.net/api/v1.5/tr.json/translate",
			  data: { key: key,"text to translate": text ,"translation direction":detectData.lang+"-"+targetLang}
			}).done(function(data) {
			  console.log(data);
			  return data.text[0];
			  popover.innerHTML=translation;
			  popover.style.visibility = 'visible';
		      popover.style.zIndex	 = 999;
			});
	  	});
	}


	//Hides popover
	hidePopover=function() {
		popover.style.zIndex 	   = -1;
		popover.style.visibility = 'hidden';	
	}


	//Single Click callback : Hide popover
	clickCallback=function(){
		hidePopover();
	}

	//Double click callback : Show popover
	doubleClickCallback=function(){
		//Proceed only when it is a valid selection
		if(window.getSelection()!=null && 
		   window.getSelection()!= undefined && 
		   window.getSelection().toString().trim()!="" ){
			var selection = window.getSelection(),
				range     = selection.getRangeAt(0),
				rect      = range.getBoundingClientRect();
			showPopover(rect.left,rect.top,selection);
		}
	}

	//Register Event Listener for Double click
	document.body.addEventListener('dblclick',doubleClickCallback);

	document.body.addEventListener('click',clickCallback);

})(jQuery);