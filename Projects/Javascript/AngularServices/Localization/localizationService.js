/**
 * @author:Vandit
 */

var service = angular.module('localizationService', []);

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
				var url = 'i18n/locale_' + localize.language + '.json';

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