/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


define(['hkg', 'notificationService'], function(hkg, notificationService) {
    hkg.register.controller('showNotification', ["$rootScope", "$translate", "$scope", "$filter", "NotificationService","$location", function($rootScope, translate, $scope, $filter, notificationService,$location) {
            $rootScope.mainMenu = "manageLink";
            $rootScope.childMenu = "manageNotifications";
            $rootScope.activateMenu();
            $scope.initializePage = function() {
                $scope.retrieveNotifications();
            }

            $scope.i18Notification = "NOTIFICATIONS."

            $scope.removeNotifications = function(notificationId) {
                notificationService.removeNotification(notificationId, function(data) {

                    $scope.retrieveNotifications();
                }, function() {

                    var msg = "Failed to remove notification";
                    var type = $rootScope.error;
                    $rootScope.addMessage(msg, type);
                });
            }
            $scope.retrieveNotifications = function() {
                notificationService.retrieveNotifications(function(data) {
                    $rootScope.notifications = angular.copy(data);
                    $scope.str = [];
                    $scope.notificationText = [];
                    $scope.shortNotificationText = [];
                    for (var x = 0; x < data.length; x++) {
                        var description = "{";
                        var translatedValue;
                        var jsonData = angular.copy(data[x].notificationDataBean.description);
                        $scope.descriptionInJson = JSON.parse(jsonData);
                        $scope.str[x] = JSON.stringify(data[x].notificationDataBean.instanceType);
                        $scope.str[x] = $scope.str[x].replace(/"/g, "");
                        Object.keys($scope.descriptionInJson).forEach(function(key) {
                            translatedValue = $filter('translate')(key + "." + $scope.descriptionInJson[key]);
                            description = description + "'" + key + "':'" + translatedValue + "',";
                        });
                        description = description.substring(0, description.length - 1) + "}";

                        $scope.notificationText[x] = $filter('translate')("NTN." + $scope.str[x], description);

                        $scope.shortNotificationText[x] = angular.copy($scope.notificationText[x]).slice(0,135);
                        console.log($scope.notificationText[x].length);
                    }
//                    
                }
                )
            }
            $scope.goToNotificationConfig=function(){
                $location.path("/managenotificationconfig");
            }

        }]);
});

