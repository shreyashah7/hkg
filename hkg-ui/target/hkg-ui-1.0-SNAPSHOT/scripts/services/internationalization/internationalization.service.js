/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

define(['hkg'], function(hkg) {
    hkg.register.factory('Internationalization', ['$resource', '$rootScope', function(resource, rootScope) {
            var Internationalization = resource(rootScope.apipath + 'internationalization/:action',
                    {},
                    {
                        createLabel: {
                            method: 'POST',
                            params: {
                                action: 'create/label'
                            }
                        },
                        getConstants: {
                            method: 'GET',
                            params: {
                                action: 'retrieve/constantmap'
                            }
                        },
                        getAllLanguages: {
                            method: 'GET',
                            isArray: true,
                            params: {
                                action: "retrieve/languages"
                            }
                        },
                        getTranslationPendingLabels: {
                            method: 'POST',
                            isArray: true,
                            params: {
                                action: "retrieve/pendingtranslationlabels"
                            }
                        },
                        updateLabels: {
                            method: 'POST',
                            isArray: true,
                            params: {
                                action: "update/labeldetails"
                            }
                        },
                        deleteLabel: {
                            method: 'POST',
                            isArray: true,
                            params: {
                                action: "delete/labeldetails"
                            }
                        },
                        retrieveCountryLanguages: {
                            method: 'GET',
                            params: {
                                action: 'retrieve/countryandlanguage'
                            }
                        },
                        createCountryLanguage: {
                            method: 'POST',
                            params: {
                                action: 'create/countryandlanguage'
                            }
                        },
                        copyLabelToPropertyFile: {
                            method: 'GET',
                            params: {
                                action: 'copy/label'
                            }
                        }
                    });
            return Internationalization;
        }]);
});
