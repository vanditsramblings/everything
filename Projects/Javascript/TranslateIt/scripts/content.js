
//Append popover to view
var popover = document.createElement('div');
popover.setAttribute('class', 'translateit-popover');
document.body.appendChild(popover);


//Shows popover
function showPopover(mouseX, mouseY, selection) {
  popover.innerHTML = selection;
  popover.style.top = mouseY + 'px';
  popover.style.left = mouseX + 'px';
  popover.style.visibility = 'visible';
}

//Hides popover
function hidePopover() {
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