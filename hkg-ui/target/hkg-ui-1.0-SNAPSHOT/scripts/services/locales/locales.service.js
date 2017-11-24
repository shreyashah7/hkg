/* 
 @Author : Satyam Koyani
 This service file will create link of controller to rest webservice.All Webservice URLs and method listed in this javascript file.
 */
define(['hkg'], function(hkg) {
    hkg.register.factory('ManageLocalesService', ['$resource', '$rootScope', function(resource, rootScope) {
            var Locales = resource(rootScope.apipath + 'locale/:action', {}, {
                createAllLocales: {
                    method: 'POST',
                    params: {
                        action: 'createall'
                    }
                },
                updateAllLocales: {
                    method: 'POST',
                    params: {
                        action: 'updateall'
                    }
                },
                retrieveAllLanguages: {
                    method: 'GET',
                    isArray: true,
                    params: {
                        action: "retrieve/languages"
                    }
                },
                retrieveLabelsAsPerScroll: {
                    method: 'POST',
                    isArray: true,
                    params: {
                        action: "retrieveasperscroll"
                    }
                },
                retrieveLocalesBySearchFields: {
                    method: 'POST',
                    isArray: true,
                    params: {
                        action: "retrieve/bysearchfields"
                    }
                },
                retrieveContentAsPerScroll: {
                    method: 'POST',
                    isArray: true,
                    params: {
                        action: "retrieveContentAsPerScroll"
                    }
                }, retrieveContentTypeList: {
                    method: 'POST',
                    isArray: false,
                    params: {
                        action: "retrieveContentTypeList"
                    }
                }

            });
            return Locales;
        }]);
});

