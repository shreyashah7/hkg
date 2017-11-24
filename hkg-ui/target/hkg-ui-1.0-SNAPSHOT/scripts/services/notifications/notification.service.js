/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


angular.module("notificationmodule", [])
        .factory('NotificationService', ['$resource', '$q','$rootScope', function(resource, $q,$rootScope) {
                var Notification = resource($rootScope.apipath+'notification/:action', {
                    action: '@actionName'},
                {
                    removeNotification: {
                        method: 'POST',
                        isArray: true,
                        params: {
                            action: 'remove'
                        }
                    },
                    retrieveNotifications: {
                        method: 'GET',
                        isArray: true,
                        params: {
                            action: 'retrievenotification'
                        }
                    },
                    createNotificationConfiguration: {
                        method: 'POST',
                        params: {
                            action: 'createNotificationConfiguration'
                        }
                    },
                    retrieveAllNotificationConfigurations: {
                        method: 'POST',
                        isArray: true,
                        params: {
                            action: 'retrieveAllNotificationConfigurations'
                        }
                    },
                    updateNotificationConfiguration: {
                        method: 'POST',
                        params: {
                            action: 'updateNotificationConfiguration'
                        }
                    },
                    searchNotificationConfigurations: {
                        method: 'POST',
                        isArray: true,
                        params: {
                            action: 'searchnotificationconfigurations'
                        }
                    },
                    retrieveNotificationConfigurationById: {
                        method: 'POST',
                        params: {
                            action: 'retrieveNotificationConfigurationById'
                        }
                    }
                });

                return Notification;
            }]);


