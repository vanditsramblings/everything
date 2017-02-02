
var app= angular.module('app',['localizationService']);

app.controller('mainController', ['$rootScope','$scope','log','localization', function($rootScope,$scope,localization){

	//-----INITIALIZE THIS VARIABLE WITH THE CURRENT LOCALE------
	$rootScope.locale  ="en";
	var localeModule   ="main";
	$scope.mainLocale  ={};
	//Initializing all the localization strings
	localization.initLocalizedResources();
	
	//This will ensure to catch the locale changes
	$rootScope.$on("localization.update", function(event, ele){
		var moduleStrings=localization.getLocalizedStringForModule(localeModule);
		
		$scope.mainLocale=
		{
				header					:moduleStrings["header"],
				title					:moduleStrings["title"]
		};
		//--------The following stops the event to be processed in the child scopes
		// handle event only if it was not defaultPrevented
		if(event.defaultPrevented) 
		{
			return;
		}
		// mark event as "not handle in children scopes"
		event.preventDefault();	
	});
	/*-----------------------------------------------------*/	

}); 