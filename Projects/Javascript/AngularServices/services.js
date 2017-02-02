/**
 * @author:Vandit
 */

var service = angular.module('services', ['ngCookies']);

/**------------------------------------------------------
 * This service enables localization.
 * The init localization function is called from the Home controller to load the 
 * local strings as per the the locale set in the rootscope.
 * The default locale is "en"
 * 
 *------------------------------------------------------------*/
service.factory('localization', [ '$rootScope','$http', function($rootScope,$http) {
	var localize = {
			//-----Get locale from root scope
			language:$rootScope.locale||"en",
			//-----array to hold the localized resource string entries
			dictionary:{},
			//-----flag to indicate if the service has loaded the resource file
			resourceFileLoaded:false,

			successCallback:function (data) 
			{
				//-----store the returned array in the dictionary
				localize.dictionary = data;
				//-----set the flag that the resource are loaded
				localize.resourceFileLoaded = true;
				//-----broadcast that the file has been loaded
				$rootScope.$broadcast('localization.update');
			},

			initLocalizedResources:function () {
				//-----build the url to retrieve the localized resource file
				var url = 'app/i18n/locale_' + localize.language + '.json';

				$http({ method:"GET", url:url, cache:false })
				.success(localize.successCallback)
				.error(function () 
						{
					//-----the request failed set the url to the default resource file
					var url = 'i18n/locale_en.json';
					$http({ method:"GET", url:url, cache:false }).success(localize.successCallback);
						});
			},

			getLocalizedString:function (module,key) 
			{
				//-----default the result to an empty string
				var result = '';
				//-----check to see if the resource file has been loaded
				if (!localize.resourceFileLoaded) 
				{
					//-----call the init method
					localize.initLocalizedResources();
					//-----set the flag to keep from looping in init
					localize.resourceFileLoaded = true;
					//-----return the empty string
					return result;
				}
				//-----make sure the dictionary has valid data
				if ((localize.dictionary !== {}) && (localize.dictionary!=undefined))
				{

					//-----check parameter
					if (typeof module != "string" || typeof key != "string") 
					{
						result = module + " - " + key;
					}

					if (localize.dictionary[module]) 
					{
						result = localize.dictionary[module][key];
					}

					if (result == null) 
					{
						result = key;
					}
				}
				// return the value to the call
				return result;
			},

			//-----Returns all the strings associated with a particular module
			getLocalizedStringForModule:function (module)
			{
				//-----default the result to an empty string
				var result = '';
				//-----check to see if the resource file has been loaded
				if (!localize.resourceFileLoaded)
				{
					//-----call the init method
					localize.initLocalizedResources();
					//-----set the flag to keep from looping in init
					localize.resourceFileLoaded = true;
					//-----return the empty string
					return result;
				}
				//-----make sure the dictionary has valid data
				if ((localize.dictionary !== {}) && (localize.dictionary!=undefined))
				{
					//-----check parameter
					if (typeof module != "string" ) 
					{
						result = module ;
					}

					if (localize.dictionary[module]) 
					{
						result = localize.dictionary[module];
					}

					if (result == null) 
					{
						result = module;
					}
				}
				// return the value to the call
				return result;
			}


	};
	// return the local instance when called
	return localize;
} ]);


/**------------------------------------------------------
 * This service will act as an interface between cookies and the application
 * Use this service to set and fetch the session attributes as set in the Cookies.
 * ===Attributes:
 * ----------loggedIn         : Denotes whether the user is logged in or not
 * ----------currentUserId    : Denotes the unique handle of the current user(Ex: userName)
 * ----------currentUserName  : Denotes the display name for the user(Ex: first name)
 * ----------userRole         : Role of the current user (Ex : admin, guest,etc.)
 * ----------sessionTimeout   : Denotes if the current session is active or not
 *------------------------------------------------------------*/

service.factory('session', ['$rootScope','$http','$cookies', function($rootScope,$http,$cookies) {

	var sessionState=
	{
			/*
			 * Session attributes as initialized in Cookies
			 * initialization is done in "mainController"
			 * 
			 *-loggedIn:false, 
			 *-currentUser:"guest",    --Use this if you want to have a unique id for a user and not display it on the app
			 *	
			 *-currentUserName:"guest",-- Remove this if you does not need a user name separate from the unique user id 
			 *-sessionActive:false,
			 *-userRole:"guest",
			 *
			 */

			setLoginState : function(currentState)
			{
				$cookies.loggedIn=currentState;
				$rootScope.$broadcast('sessionState.loginStateChanged');
			},
			getLoginState : function()
			{
				return $cookies.loggedIn;
			},
			setCurrentUser : function(user)
			{
				$cookies.currentUser=user;
				$rootScope.$broadcast('sessionState.currentUserChanged');
			},
			getCurrentUser : function()
			{
				return $cookies.currentUser
			},
			setCurrentUserName : function(userName)
			{
				$cookies.currentUserName=userName;
				//No broadcast is done in this case
			},
			getCurrentUserName : function()
			{
				return $cookies.currentUserName;
			},
			setUserRole : function(userRole)
			{
				$cookies.currentUserRole=userRole;
				$rootScope.$broadcast('sessionState.currentUserRoleChanged');
			},
			getUserRole : function()
			{
				return $cookies.currentUserRole;
			},
			setSessionActive : function(sessionActive)
			{
				$cookies.sessionActive=sessionActive;
				$rootScope.$broadcast('sessionState.sessionTimedOut');
			},
			getSessionActive : function()
			{
				return $cookies.sessionActive
			},
			invalidateSession:function()
			{
				//NOTE : Use the following is you need to delete the cookies.
				/*
				$cookies.remove("currentUser");
				$cookies.remove("sessionActive");
				$cookies.remove("loginState");
				$cookies.remove("userRole");
				$cookies.remove("currentUserName");
				 */

				sessionState.setCurrentUser('guest');
				sessionState.setSessionActive('false');
				sessionState.setLoginState('false');
				sessionState.setUserRole('guest');
				sessionState.setCurrentUserName('guest');
			}
	};

	return sessionState;

}]); 


/**------------------------------------------------------
 * This service enables loading of the google api script dynamically.
 * It returns a promise once all the google's apis are loaded.
 * 
 * Use it as : --initializeMap.mapsInitialized.then(function() {});--
 * 
 *------------------------------------------------------------*/

service.factory('initializeMap',['$window','$q', function($window, $q){

	//Google's url for async maps initialization accepting callback function
	var asyncUrl = 'https://maps.googleapis.com/maps/api/js?callback=',
	mapsDefer = $q.defer();

	//Callback function - resolving promise after maps successfully loaded
	$window.googleMapsInitialized = mapsDefer.resolve; // removed ()

	//Async loader
	var asyncLoad = function(asyncUrl, callbackName) {
		var script = document.createElement('script');

		script.src = asyncUrl + callbackName;
		document.body.appendChild(script);
	};
	//Start loading google maps
	asyncLoad(asyncUrl, 'googleMapsInitialized');

	//Usage: initializeMap.mapsInitialized.then(callback)
	return {
		mapsInitialized : mapsDefer.promise
	};
}]);

/**------------------------------------------------------
 * This service provides logging functionality for tha app
 * 
 * IMP: To disable logging set the loggingEnabled flab to false.
 * This will disable all the app's logging.
 * 
 * To disable specific logging use the following flags:
 * 
 * ::infoEnabled
 * ::errorEnabled
 * ::debugEnabled
 * 
 * Currently three methods are defined : 
 * ::log.info -
 * ::log.error -
 * ::log.debug -
 *------------------------------------------------------------*/

service.factory('log',['$log', function($log){

	var log=
	{	
			loggingEnabled  : true,
			infoEnabled     : true,
			errorEnabled    : true,
			debugEnabled    : true,

			info :function(message)
			{	
				if(log.loggingEnabled && log.infoEnabled)
				{	
					message = "::::[" + (new Date()) + "]::::[INFO]::::[MESSAGE]~~> " + message;
					$log.log(message);
				}
				return true;
			},
			error :function(message)
			{	
				if(log.loggingEnabled && log.errorEnabled)
				{
					message = "::::[" + (new Date()) + "]::::[ERROR]::::[ERROR MESSAGE]~~> " + message;
					$log.log(message);
				}
				return true;
			},
			debug :function(message)
			{	
				if(log.loggingEnabled && log.debugEnabled)
				{
					message = "::::[" + (new Date()) + "]::::[DEBUG]::::[DEBUG MESSAGE]~~> " + message;
					$log.log(message);
				}
				return true;
			}	 
	};

	return log;
}]);

/**------------------------------------------------------
 * This service provides various utility functions.
 * You can create a seperate module if there are many utility functions required
 * 
 * -----Functions-----
 * 
 * ::isStringEmpty(value)-
 * 			-Returns true if value 'undefined'
 * 			-Returns true if value 'null'
 * 			-Returns true if value is not a String *be careful about this case
 * 			-returns true if value empty
 * 			-returns true if value consists only blank space
 * 
 *------------------------------------------------------------*/
service.factory('utilities',['$rootScope',function($rootScope){

	var utilities =
	{
			isStringEmpty : function(value)
			{	
				if(value==undefined)
					return true;
				else if(value==null)
					return true;
				if(typeof value!="string")
					return true;
				else if(value.trim().length==0)
					return true;

				return false;
			}


	}
	return utilities;

}]);