/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


define(['hkg'], function(hkg) {
    hkg.register.factory('RuleConfigService', ['$resource', '$rootScope', function(resource, rootScope) {
            var RuleConfigManagement = resource(
                    rootScope.apipath + 'ruleconfig/:action', //url being hit
                    {
                        action: '@actionName'
                    }, // url perameters

            {
                retrieveruletypes: {
                    method: 'GET',
                    isArray: true,
                    params: {
                        action: 'retrieveruletypes'
                    }
                },
                saveRule: {
                    method: 'POST',
                    isArray: false,
                    params: {
                        action: "update"
                    }
                },
                retrievePrerequisite: {
                    method: 'GET',
                    params: {
                        action: 'retrieveprerequisite'
                    }
//                    url: rootScope.centerapipath + "customfield/:action"

                },
                retrieveRulesForTree: {
                    method: 'GET',
                    params: {
                        action: 'retrieve'
                    }
                },
                retrieveRulebyRulenumber: {
                    method: 'POST',
                    params: {
                        action: 'retrieverulebyrulenumber'
                    }
                },
                searchrules:
                        {
                            method: 'GET',
                            isArray: true,
                            params: {
                                action: 'searchrules'
                            }
                        },
                         retrieveAllRuleList:
                        {
                            method: 'GET',
                            isArray: false,
                            params:
                                    {
                                        action: 'retrieveAllRules'
                                    }
                        },
                removeRule:
                        {
                            method: 'POST',
                            isArray: true,
                            params: {
                                action: 'removeRule'
                            }
                        }
               
//                
            });
            return RuleConfigManagement;
        }]);
});