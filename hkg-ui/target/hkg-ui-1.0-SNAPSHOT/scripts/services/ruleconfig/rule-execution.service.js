/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


define(['hkg'], function(hkg) {
    hkg.register.factory('RuleExecutionService', ['$resource', '$rootScope', function(resource, rootScope) {
            var RuleExecution = resource(
                    rootScope.centerapipath + 'executerule/:action', //url being hit
                    {
                        action: '@actionName'
                    }, // url perameters

            {
                executePreRule: {
                    method: 'POST',
                    params: {
                        action: 'prerule'
                    }
                },
                executePostRule: {
                    method: 'POST',
                    params: {
                        action: "postrule"
                    }
                }
               
//                
            });
            return RuleExecution;
        }]);
});