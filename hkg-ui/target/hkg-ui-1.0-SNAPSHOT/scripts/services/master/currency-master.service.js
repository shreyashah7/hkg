/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
define(['hkg'], function(hkg) {
    hkg.register.factory('ManageCurrencyMasterService', ['$resource', '$rootScope', function(resource, rootScope) {
            var currencyMaster = resource(rootScope.apipath + 'currencymaster/:action', {}, {
                retrieveCurrencies: {
                    method: 'GET',
                    isArray: true,
                    params: {
                        action: 'retrievecurrencies'
                    }
                },
                retrieveCurrencyDataBean: {
                    method: 'GET',
                    isArray: true,
                    params: {
                        action: 'retrieve'
                    }
                },
                addReferenceRate: {
                    method: 'PUT',
                    params: {
                        action: 'addreferencerate'
                    }
                },
                retrieveCurrency: {
                    method: 'POST',
                    isArray: true,
                    params: {
                        action: 'retrievecurrency'
                    }
                },
                update: {
                    method: 'POST',
                    params: {
                        action: 'update'
                    }
                },
                create: {
                    method: 'PUT',
                    params: {
                        action: 'create'
                    }
                },
                deleteById: {
                    method: 'POST',
                    params: {
                        action: 'delete'
                    }
                },
                retrieveArchivedCurrency: {
                    method: 'GET',
                    params: {
                        action: 'retrievearchivedcurrency'
                    }
                },
                retrieveCurrentCurrency: {
                    method: 'GET',
                    isArray: true,
                    params: {
                        action: 'retrievecurrentcurrency'
                    }
                },
                updateReferenceRate: {
                    method: 'POST',
                    params: {
                        action: 'updatereferencerate'
                    }
                }
            });
            return currencyMaster;
        }]);
});