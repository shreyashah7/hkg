/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


define(['hkg'], function (hkg) {
    hkg.register.factory('CustomFieldService', ['$resource', '$rootScope', function (resource, rootScope) {
            var customfieldManagment = resource(
                    rootScope.apipath + 'customfield/:action', //url being hit
                    {
                        action: '@actionName'
                    }, // url perameters

            {
                //methods
                retrieveFeatures: {
                    method: 'GET',
                    isArray: false,
                    params: {
                        action: "retrievefeatures"
                    }

                },
                retrieveSectionAndCustomFieldInfoByFeatureId: {
                    method: 'POST',
                    isArray: false,
                    params: {
                        action: "retrievesectionandcustomfields"
                    }

                },
                retrieveSectionAndCustomFieldInfoTemplateByFeatureId: {
                    method: 'POST',
                    isArray: false,
                    params: {
                        action: "retrievesectionandcustomfieldtemplate"
                    }

                },
                
                create: {
                    method: 'PUT',
                    isArray: false,
                    params: {
                        action: "create"
                    }
                }
            });
            return customfieldManagment;
        }]);
});