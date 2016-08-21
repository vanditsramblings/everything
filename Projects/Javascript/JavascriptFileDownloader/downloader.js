/**
*
*Use the following snippet , if there are multiple downloadable links on a single page.
*It is driven by the link's query selector.
*
*Dependency : None.Uses native Javascript
*/
(function(){
	
	//Update this variable with the link query selector.
	//It will select get all valid links and init download for those links.
	var dowload_link_selector="li[class='verbslink'] a";
	
	var urls=document.querySelectorAll(dowload_link_selector);
	console.log(urls);
	for(var i=0;i<urls.length;i++)
	{		var url=urls[i].href;
	
	var filename = url.substring(url.lastIndexOf("/") + 1).split("?")[0];
		var save = document.createElement('a');
        save.href = url;
        save.target = '_blank';
        save.download = filename || 'unknown';

        var event = document.createEvent('Event');
        event.initEvent('click', true, true);
        save.dispatchEvent(event);
        (window.URL || window.webkitURL).revokeObjectURL(save.href);
  }
})();