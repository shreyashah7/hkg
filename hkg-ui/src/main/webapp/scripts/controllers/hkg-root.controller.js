var MainController = ["$scope", "$rootScope", "$route", "CustomField", "$location", "$timeout", "Notification", "MessagingPopup", "WorkAllocation", "$filter", "$translate", "$q", "$compile", "$http", "$modalStack", "LABELMASTER", "ReportMaster", "Common", "EmpTemp", "MasterProxyLogin", "FranchiseConfig", "hotkeys", function (scope, rootScope, route, CustomField, location, $timeout, Notification, MessagingPopup, WorkAllocation, $filter, translate, $q, $compile, $http, $modalStack, LABELMASTER, ReportMaster, Common, EmpTemp, MasterProxyLogin, FranchiseConfig, hotkeys) {
        rootScope.location = location;
        rootScope.mask = {mask: false, imagePath: "images/ajax-loader.gif"};
        rootScope.uploadingFiles = {fileName: []};
        rootScope.maxValueForTruncate = 25;
        rootScope.message = {};
        rootScope.primaryFields = ["carat_type$DRP$Long", "COL$DRP$Long", "CLR$DRP$Long", "SHAPE$DRP$Long"];
        rootScope.fourCMap = ["size$DRP$Long", "COL$DRP$Long", "CLR$DRP$Long", "SHAPE$DRP$Long"];
        //if this variable is true then label key is inster in database.if lable has no any key
        rootScope.isLabelEntryInDatabase = true;
        scope.$on('$viewContentLoaded', function () {
//            rootScope.isLabelEntryInDatabase = true;
            //Focus the first input field in page
            setTimeout(function () {
                if ($('.ui-grid-filter-input:input:enabled:visible:first, .form-control:not(#proxy):not(#designation):input:enabled:visible:first') !== undefined) {
                    $('.ui-grid-filter-input:input:enabled:visible:first, .form-control:not(#proxy):not(#designation):input:enabled:visible:first').focus();
                }
            }, 500);
        });
        rootScope.isPrint = false;


        hotkeys.add({
            combo: 'ctrl+m',
            allowIn: ['INPUT', 'SELECT', 'TEXTAREA'],
            callback: function (event, hotkey) {
                document.body.getElementsByClassName('menu-trigger')[0].click();
            }
        });

        //MM : 17-04-2015, Its affecting ajax calls too, which we are callingat every 5 seconds
//        Global Loading code for menu change. pace.js used.
        Pace.on("start", function () {
            if (angular.isDefined(rootScope.maskLoading)) {
                $timeout(function () {
                    rootScope.maskLoading();
                });
            }
        });
        Pace.on("hide", function () {
            if (angular.isDefined(rootScope.unMaskLoading)) {
                $timeout(function () {
                    rootScope.unMaskLoading();
                });
            }
        });

        rootScope.retrieveMinAge = function () {
            if (rootScope.canAccess("manageEmployees")) {
                EmpTemp.retrievePrerequisite(function (res) {
                    var agelimit = res['agelimit'];
                    if (agelimit['minAge'] !== null) {
                        rootScope.minAge = agelimit['minAge'];
                    } else {
                        rootScope.minAge = 18;
                    }
                });
            }
        };
//        if (rootScope.isLoggedIn === true) {
//            rootScope.retrieveMinAge();
//        }
        if (!!localStorage.getItem("proxyLogin")) {
            scope.userProxy = localStorage.getItem("proxyLogin");
        }
        rootScope.loadLabels = function () {
            if (rootScope.isLabelEntryInDatabase) {
                rootScope.isLabelEntryInDatabase = false;
                LABELMASTER.initLabelMaster();
            } else {
                rootScope.isLabelEntryInDatabase = true;
            }
        };


        rootScope.retrieveFranchiseConfiguration = function () {
            FranchiseConfig.retrieveAllConfiguration(function (res) {
                scope.franchiseConfig = angular.copy(res);
                if (scope.franchiseConfig.CURR_VISIBILITY_STATUS !== undefined) {
                    rootScope.haveValue = scope.franchiseConfig.CURR_VISIBILITY_STATUS;
                }
            }, function () {
            });
        };
        //Not required at login time.
//        rootScope.retrieveFranchiseConfiguration();

//        scope.i18Notification = "NOTIFICATIONS.";
        rootScope.commonentity = "COMMON.";
        rootScope.viewEncryptedData = false;
        rootScope.registerLocationChangeEvent = function (formName) {
            scope.$on('$locationChangeStart', function (event) {
                if (formName.$dirty && rootScope.isLoggedIn) {
                    if (!confirm('You have unsaved changes, go back?')) {
                        event.preventDefault();
                    } else {
                        formName.$dirty = false;
                        formName.$setPristine();
                    }
                }
            });
        };
        rootScope.switchLanguage = function (languageKey, companyId) {
            translate.use(languageKey + companyId);
        };
        /*setting container's height to window's height with correction of other padding and margin*/
        rootScope.containerHeight = window.innerHeight - 140;
        rootScope.isLoggedIn = false;
        rootScope.success = 0;
        rootScope.failure = 1;
        rootScope.warning = 2;
        rootScope.error = 3;
        rootScope.getMessageCssClass = function (statusCode) {
            if (typeof statusCode === 'String' && statusCode.lenght > 1)
                return statusCode;
            if (statusCode === 0)
                return "alert-success";
            if (statusCode === 1 || statusCode === 3)
                return "alert-danger";
            if (statusCode === 2)
                return "alert-warning";
        };
        //Timeout increases to 30 second.
        rootScope.successTimeout = 30000;
        rootScope.failureTimeout = 30000;
        rootScope.warningTimeout = 30000;
        rootScope.createLabelAutomatically = false;
        rootScope.getCreateLabelAutomaticallyFlag = function () {
            return rootScope.createLabelAutomatically;
        };
        rootScope.mainMenu = "dashboard";
        rootScope.childMenu = "dashboardLink";
        rootScope.activateMenu = function () {
            rootScope.reportMenu = [];
            if (rootScope.menu !== undefined) {
                angular.forEach(rootScope.menu, function (item) {
                    if ((item.menuType === "M" || item.menuType === "DM" || item.menuType === "RMI" || item.menuType === "DE") && $('a#' + item.featureName).hasClass("active")) {
                        $('a#' + item.featureName).removeClass('active');
                        if (item.menuType === "RMI") {
                            rootScope.reportMenu.push(item);
                        }
                    }
                });
            }
            if (rootScope.mainMenu.trim() === "") {
                if ($('a#manageLink').hasClass("active")) {
                    $('a#manageLink').click();
                    $('a#manageLink').removeClass('active');
                }
                if ($('a#stockLink').hasClass("active")) {
                    $('a#stockLink').click();
                    $('a#stockLink').removeClass('active');
                }
                if ($('a#dashboardLink').hasClass("active")) {
                    $('a#dashboardLink').removeClass('active');
                }
                if ($('a#reportLink').hasClass("active")) {
                    $('a#reportLink').click();
                    $('a#reportLink').removeClass('active');
                }
            } else {
                if (!$('a#' + rootScope.mainMenu).hasClass("active")) {
                    $('a#' + rootScope.mainMenu).addClass('active');
                    $('a#' + rootScope.mainMenu).click();
                }
            }
            if (rootScope.childMenu.trim() !== "") {
                $('a#' + rootScope.childMenu).addClass('active');
            }
        };
        rootScope.activateMenu();
        rootScope.validations = [];
        rootScope.addMessage = function (msg, type, showLoginLink, linkText, translateValueMap) {
            rootScope.scrollTo('topheader');
            var cssClass = rootScope.getMessageCssClass(type);
            var message = {
                msg: msg,
                type: cssClass,
                translateValueMap: translateValueMap,
                showLoginLink: showLoginLink,
                linkText: linkText
            };
            var translatedMsg = '';
            if (translateValueMap !== undefined && translateValueMap !== null) {
                $.each(message.translateValueMap, function (key, value) {
                    var translatedValue = $filter('translate')(value);
                    translateValueMap[key] = translatedValue;
                });
                translatedMsg = $filter('translate')("MSG." + message.msg, translateValueMap);
            } else {
                translatedMsg = $filter('translate')("MSG." + message.msg);
            }
            message.msg = translatedMsg;
            rootScope.validations.push(message);
            if (type === rootScope.success) {
                rootScope.timeout = rootScope.successTimeout;
            }
            else if (type === rootScope.failure || type === rootScope.error) {
                rootScope.timeout = rootScope.failureTimeout;
            }
            else if (type === rootScope.warning) {
                rootScope.timeout = rootScope.warningTimeout;
            }
            if (rootScope.timeout) {
                $timeout(function () {
                    rootScope.closeAlertMessage(rootScope.validations.indexOf(message));
                }, rootScope.timeout);
            }
        };
        rootScope.closeAlertMessage = function (index) {
            rootScope.validations.splice(index, 1);
        };
        rootScope.maskLoading = function () {
            rootScope.mask.mask = true;
//            console.log('mask loading' + rootScope.mask.mask);
        };
        rootScope.unMaskLoading = function () {
            rootScope.mask.mask = false;
//            console.log('unmask loading' + rootScope.mask.mask);
        };
        rootScope.showFirstLogin = function () {
            rootScope.setMessage("", "", false);
            rootScope.showLogin();
        };
        rootScope.showLogin = function () {
            $('#login').modal('show');
        };
        rootScope.loginCancel = function () {
            $('#login').modal('hide');
            rootScope.removeModalOpenCssAfterModalHide();

        };
        pingServerOnLoad();
//        rootScope.pingServer();
        //Not required at the time of login.
        //Will be called by polling method.
//        rootScope.workAllocationCount();
//        rootScope.pingMasterServer();
        rootScope.fetchVersion();
        rootScope.loginTry = -1;

        rootScope.isFirstReq = true;
        rootScope.login = {};
        //Adding User Menu related Flags   
        rootScope.menuManageAccess = false;
        rootScope.menuManage = "manage";
        rootScope.menuStockAccess = false;
        rootScope.menuStock = "stock";
        rootScope.menuReportAccess = false;
        rootScope.menuReport = "report";
        rootScope.ConvertTimeStamptodate = function (timestamp) {
            if (timestamp !== null) {
                var d = new Date(timestamp);
                var day = d.getDate();
                day = (day < 10) ? '0' + day : day;
                var month = d.getMonth() + 1;
                month = (month < 10) ? '0' + month : month;
                return day + '/' + month + '/' + d.getFullYear();
            } else {
                return "NA";
            }
        };
        rootScope.ConvertTimeStampToDateTodayYesterday = function (timestamp) {
            if (timestamp !== null) {
                var paasedDate = new Date(timestamp);
                var d = new Date(timestamp);
                var today = new Date();
                var isToday = d.setHours(0, 0, 0, 0) === today.setHours(0, 0, 0, 0);
                if (isToday) {
                    return paasedDate.toLocaleTimeString();
                }
                var yesterday = new Date(today);
                yesterday.setDate(today.getDate() - 1);
                var isYesterday = d.setHours(0, 0, 0, 0) === yesterday.setHours(0, 0, 0, 0);
                if (isYesterday) {
                    return "yesterday";
                }
                var day = d.getDate();
                day = (day < 10) ? '0' + day : day;
                var month = d.getMonth() + 1;
                month = (month < 10) ? '0' + month : month;
                return day + '/' + month + '/' + d.getFullYear();
            } else {
                return "NA";
            }
        };

        rootScope.encryptPass = function (password, isEnctrypt) {
            if (password !== null && password !== undefined && password !== "")
            {
                var passLength = password.length;
                var newPass = "";
                for (var i = 0; i < passLength; i++)
                {
//                    console.log("password : " + password);
                    var temp = password.charAt(i);
                    var index = rootScope.printableChars.indexOf(temp);
                    if (index !== -1)
                    {
                        if (isEnctrypt)
                        {
                            index += (i + passLength);
                            if (index >= (rootScope.printableChars.length))
                            {
                                index -= (rootScope.printableChars.length);
                            }
                        } else {
                            index -= (i + passLength);
                            if (index < 0)
                            {
                                index += (rootScope.printableChars.length);
                            }
                        }
//                        console.log("rootScope.printableChars : " + rootScope.printableChars);
                        temp = rootScope.printableChars.charAt(index);
                        newPass += temp;
                    } else {
                        newPass += temp;
                    }
                }
                return newPass;
            } else {
                return password;
            }
        };
        rootScope.logout = function () {

            rootScope.user = null;
            scope.username = scope.password = null;
            delete scope.userProxy;
            scope.$emit('event:logoutRequest');
            //        $scope.$apply(function() {
            //            $location.path('/');
            //        });
        };
        /*
         *  Piyush Sanghani - 01/04/2014
         *  Methods added for Alert Notifications
         */
        rootScope.alertId = undefined;
        scope.setAlert = function (message, $index) {
            rootScope.alertId = message.messageObj;
            rootScope.$broadcast('alert:clicked', rootScope.alertId);
            location.path('/managealert');
            $('#alert').popover('hide');
            scope.closeAlert(angular.copy(message), $index);
        };
        scope.setAlertId = function () {
            rootScope.alertId = undefined;
        };
        scope.closeAlert = function (message, $index) {
            var success = function () {
                scope.alerts.splice($index, 1);
            };
            var failure = function () {
            };
            MessagingPopup.markAsClosed(message, success, failure);
        };
        rootScope.retrieveNotificationCount = function () {
            var success = function (data) {
                rootScope.notificationCount = data;
            };
            var failure = function () {

            };
            Notification.retrieveNotificationCount(success);
        };

        rootScope.translateValue = function (text) {
            return $filter('translate')(text);
        };

        rootScope.retrieveNotificatonPopUp = function () {
            var success = function (data) {
                rootScope.notificationsPopUp = angular.copy(data);
                scope.str = [];
                scope.notificationText = [];
                for (var x = 0; x < data.length; x++) {
                    var description = "{";
                    var translatedValue;
                    var jsonData = angular.copy(data[x].notificationDataBean.description);
                    scope.descriptionInJson = JSON.parse(jsonData);
                    scope.str[x] = JSON.stringify(data[x].notificationDataBean.instanceType);
                    scope.str[x] = scope.str[x].replace(/"/g, "");
                    Object.keys(scope.descriptionInJson).forEach(function (key) {
                        translatedValue = $filter('translate')(key + "." + scope.descriptionInJson[key]);
                        description = description + "'" + key + "':'" + translatedValue + "',";
                    });
                    description = description.substring(0, description.length - 1) + "}";
                    scope.notificationText[x] = $filter('translate')("NTN." + scope.str[x], description);
                }
                var html =
                        '<div class="list-group list-special" ng-if="!notificationsPopUp || notificationsPopUp.length === 0">' +
                        '<div class = "row list-group-item" >' +
                        '<div class="col-xs-2 text-center notification-image-icon">' +
                        '<span class="glyphicon  glyphicon-user"></span>' +
                        '</div>' +
                        '<div class = "col-xs-10 hkg-bold">' +
                        '{{"NOTIFICATIONS." + "No notifications available." | translate}}'
                        +
                        '</div>' +
                        '</div>' +
                        '</div>' +
                        '<div style="overflow-y:auto; overflow-x:hidden; height:500px;" ng-if="notificationsPopUp.length > 0" class="list-group list-special">' +
                        '<a ng-repeat="notification in notificationsPopUp" class="row list-group-item" onclick="$(&quot;#Notifications&quot;).popover(&quot;hide&quot;);"href="#shownotification" " >' +
                        '<div class="col-xs-2 text-center notification-image-icon">' +
                        '<span class="glyphicon  glyphicon-user"></span>' +
                        '</div>' +
                        '<div class="col-xs-10">' +
                        '<div class="wordRap" ng-class="{\'hkg-bold\': notification.isSeen === false }">' +
                        '{{notificationText[$index]}}' +
                        '</div>' +
                        '</div>' +
                        '</a> ' + ' </div>' + '<a class="pull-right" ng-if="notificationsPopUp.length > 0" onclick="$(&quot;#Notifications&quot;).popover(&quot;hide&quot;);"href="#shownotification" " >&nbsp;&nbsp;&nbsp;&nbsp;{{"NOTIFICATIONS." + "Show all" | translate }}</a>';
                $(".popover-content").html($compile(html)(scope));
                rootScope.retrieveNotificationCount();
            };
            Notification.retrieveNotificationsPopUp(success);
        };
        rootScope.getCurrentServerDate = function () {
            if (rootScope.session !== undefined) {
                rootScope.serverOffsetInMin = rootScope.session.serverOffsetInMin;
//                console.log('server : ' + rootScope.serverOffsetInMin);
//                console.log('client : ' + rootScope.clientTimezoneOffset);
                var diff = rootScope.serverOffsetInMin + rootScope.clientTimezoneOffset;
                var serverDate = new Date();
//                console.log('client Date : ' + serverDate);
                serverDate.setMinutes(serverDate.getMinutes() + diff);
//                console.log('server Date : ' + serverDate);
                return serverDate;
            }
        }
        ;// Method made by Shifa Salheen on 17 April 2015
        // Sorting function for non diamond custom fields
        rootScope.getCustomDataInSequence = function (templateData)
        {
            if (templateData !== null)
                var modifiedTemplate = [];
            // Put sequence number =1 if it does not have any sequence
            angular.forEach(templateData, function (template)
            {
                if (!angular.isDefined(template.sequenceNum) || template.sequenceNum === null)
                {
                    template.sequenceNum = 1;
                }
                modifiedTemplate.push(template);
            });
            // Sorting applied on the basis of sequence number
            modifiedTemplate.sort(function (a, b) {
                return parseInt(a.sequenceNum) - parseInt(b.sequenceNum)
            });
            return modifiedTemplate;
        }

//        rootScope.retrieveSectionWiseCustomFieldInfo = function (featureName, sectionName) {
//            var data = null;
//            var myJSON = [];
//            var completeJSON = {};
//
//            var data1 = null;
//            if (featureName) {
//                CustomField.retriveAccesibleCustomFieldForFeature(function (response)
//                {
//                    data = encryptPass(localStorage.getItem(featureName));
//                    console.log('local data' + JSON.parse(JSON.stringify(data)));
//                    data1 = JSON.parse(angular.toJson(response));
////                                data1 = response;
//                    var parsedData = JSON.parse(data);
//
//                    console.log('Value from server....' + JSON.stringify(data1));
//                    angular.forEach(data1[sectionName], function (result)
//                    {
//                        var index = $filter('filter')(parsedData[sectionName], function (rule) {
//                            return rule.label === result.label;
//                        })[0];
//
//                        myJSON.push(index);
//                    });
//                    if (myJSON !== null) {
////                                    completeJSON={sectionName:myJSON};
//                        completeJSON[sectionName] = myJSON;
//                        console.log('Final modified local storage' + JSON.stringify(completeJSON));
//                        return completeJSON;
//                    }
//                    else {
//                        console.log('else>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>');
//                        CustomField.retrieveSectionAndCustomFieldInfoTemplateByFeatureId(featureName, function (response) {
//                            if (response) {
//                                var encryptedValue = encryptPass(JSON.stringify(response));
//                                try {
//                                    localStorage.setItem(featureName, encryptedValue);
//                                } catch (e) {
////                                                console.log("LIMIT REACHED: (" + i + ")");
//                                }
//                            }
//                        });
//
//                    }
//                });
//                return completeJSON;
//            }
//
//
//
//        };

        scope.alerts = [];
        scope.retrieveAlerts = function () {
            var success = function (data) {
                scope.alerts = data['priority'];
                scope.messagecount = data['count'];
                if (angular.isDefined(scope.alerts) && scope.alerts.length > 0) {
                    angular.forEach(scope.alerts, function (item) {
                        item.shortMessage = '';
                    });
                    $('#showalertmodal').modal({
                        backdrop: 'static',
                        keyboard: false,
                        show: true
                    });
                }
            };
            MessagingPopup.retrieveAlerts(success);
        };
//        scope.retrieveWorkAllocationCount = function () {
//            var success = function (res) {
//                rootScope.allocationMap = res.data;
//            };
//            WorkAllocation.retrieveWorkAllocationCount(success);
//        };
        //Method used to reload route.
        scope.reloadRoute = function () {
            route.reload();
        };
        //Code to enable refresh button on notification change.
        rootScope.isRefreshRequired = false;
        rootScope.$watch('allocationMap', function (newValue, oldValue) {
            var currentLocation = location.path().substring(1, location.path().length);
            if (rootScope.isRefreshRequired === false) {
                if (currentLocation.length > 0) {
                    angular.forEach(rootScope.menu, function (menuItem) {
                        if (currentLocation === menuItem.featureURL) {
                            var menuLabel = menuItem.menuLabel;
                            if (oldValue === undefined || oldValue === null) {
                                if (newValue !== undefined && newValue !== null && newValue[menuLabel]) {
                                    rootScope.isRefreshRequired = true;
                                } else {
                                    rootScope.isRefreshRequired = false;
                                }
                            }
                            else if (newValue === undefined || newValue === null) {
                                if (oldValue !== undefined && oldValue !== null && oldValue[menuLabel]) {
                                    rootScope.isRefreshRequired = true;
                                } else {
                                    rootScope.isRefreshRequired = false;
                                }
                            }
                            else if (oldValue[menuLabel] === undefined && newValue[menuLabel] === undefined) {
                                rootScope.isRefreshRequired = false;
                            } else if (oldValue[menuLabel] !== newValue[menuLabel]) {
                                rootScope.isRefreshRequired = true;
                            } else {
                                rootScope.isRefreshRequired = false;
                            }
                        }
                    });
                }
            }
        });
        scope.hideModal = function () {
            $('#showalertmodal').modal('hide');
            $rootScope.removeModalOpenCssAfterModalHide();

        };
        scope.markClosed = function (data, $index) {
            var success = function () {
                scope.messagePopUp.splice($index, 1);
                scope.messagecount = scope.messagecount - 1;
            };
            var failure = function () {
            };
            MessagingPopup.markAsClosed(data, success, failure);
        };
        scope.markClosedPriorityMsg = function (data, $index) {
            var success = function () {
                scope.alerts.splice($index, 1);
                scope.messagecount = scope.messagecount - 1;
                if (scope.alerts.length === 0) {
                    $('#showalertmodal').modal('hide');
                    $rootScope.removeModalOpenCssAfterModalHide();

                }
            };
            var failure = function () {
            };
            MessagingPopup.markAsClosed(data, success, failure);
        };
        scope.retrieveAlertCount = function () {
            scope.retrieveAlerts();
        };
        scope.refreshIntervalId = [];
        scope.$on('evaluateResultValue', function (event, evaluateresult) {
            alert('event' + evaluateresult);
            rootScope.evaluateResultValue = evaluateresult;
        });
        scope.$on('event:pollingStart', function (event) {
//            scope.retrieveAlerts();
//            // Stop polling - Piyush Sanghani
            callAlerts();
        });
        scope.$on('event:pollingStop', function (event) {
            angular.forEach(scope.refreshIntervalId, function (item) {
                clearInterval(item);
            });
            scope.alerts = [];
            scope.refreshIntervalId = [];
        });
        function callAlerts() {
            var refreshIntervalId = setInterval(function () {
                scope.retrieveAlertCount();
                scope.retrieveNotificationCount();
//                if (!rootScope.isMaster) {
//                    scope.retrieveWorkAllocationCount();
//                }
//                rootScope.getServerDate();
            }, 10000);
            if (scope.refreshIntervalId.indexOf(refreshIntervalId) === -1) {
                scope.refreshIntervalId.push(refreshIntervalId);
            }
        }
        ;
        /*
         *  Piyush Sanghani - 22/04/2014
         *  Clean validation notifications
         */
        rootScope.$on('$routeChangeStart', function (event, current, previous) {
            if (location.path() !== '/viewreports') {
                localStorage.setItem("menuReportId", null);
            }
            if (location.path() !== '/managemessage') {
                localStorage.removeItem('rootMessage');
            }
            if (location.path() === '/printsvg' || location.path() === '/trackstockprint' || location.path() === '/printflow') {
                rootScope.isPrint = true;
            } else {
                rootScope.isPrint = false;
            }
            rootScope.validations = [];
            //To stop stream of web cam if enabled by any page.
            if (angular.isDefined(rootScope.stream)) {
                rootScope.stream.stop();
            }
            //Dismiss all active modals//For on-demand modal templates.
            $modalStack.dismissAll();
            //Disable refresh sign on route change.
            rootScope.isRefreshRequired = false;

            //Hide select2 drop down.
            if ($('.select2-drop').length > 0) {
                $('.select2-drop').remove();
                $('.select2-drop-mask').remove();
                $('.select2-hidden-accessible').remove();
                $('.select2-sizer').remove();
            }

        });
        /**
         * @author satyajit - 24/04/2014
         * Method for field level validations.
         * For usage refer wiki page : http://apps.argusoft.com:3000/projects/argusoft-projects/wiki/Client-side_Validation_Message
         */
        rootScope.$on('event:validate', function (event, element) {
            var elName = element.$name;
            element.showValidation = false;
            if (!element.$valid) {
                element.showValidation = true;
                if (element.$error.required) {
                    element.validationMsg = elName + 'RequiredMsg';
                } else if (element.$error.email) {
                    element.validationMsg = elName + 'EmailMsg';
                } else if (element.$error.minlength) {
                    element.validationMsg = elName + "LengthMsg";
                }
            }
        });
        rootScope.predicateBy = function (prop) {
            return function (a, b) {
                if (a[prop] > b[prop]) {
                    return 1;
                } else if (a[prop] < b[prop]) {
                    return -1;
                }
                return 0;
            };
        };

        //Added by kvithlani 5-7-2014
        //This format is added for making dateformat consistent in whole system
        rootScope.dateFormat = "dd/MM/yyyy";
        rootScope.dateFormatWithTime = "dd/MM/yyyy HH:mm a";
        //For scrolling to particular div
        rootScope.scrollTo = function (eID) {
            // This scrolling function 
            // is from http://www.itnewb.com/tutorial/Creating-the-Smooth-Scroll-Effect-with-JavaScript

            var startY = currentYPosition();
            var stopY = elmYPosition(eID);
            var distance = stopY > startY ? stopY - startY : startY - stopY;
            if (distance < 100) {
                scrollTo(0, stopY - 60);
                return;
            }
            var speed = Math.round(distance / 100);
            if (speed >= 20)
                speed = 20;
            var step = Math.round(distance / 25);
            var leapY = stopY > startY ? startY + step : startY - step;
            var timer = 0;
            if (stopY > startY) {
                for (var i = startY; i < stopY; i += step) {
                    setTimeout("window.scrollTo(0, " + (leapY - 60) + ")", timer * speed);
                    leapY += step;
                    if (leapY > stopY)
                        leapY = stopY;
                    timer++;
                }
                return;
            }
            for (var i = startY; i > stopY; i -= step) {
                setTimeout("window.scrollTo(0, " + (leapY - 60) + ")", timer * speed);
                leapY -= step;
                if (leapY < stopY)
                    leapY = stopY;
                timer++;
            }

            function currentYPosition() {
                // Firefox, Chrome, Opera, Safari
                if (self.pageYOffset)
                    return self.pageYOffset;
                // Internet Explorer 6 - standards mode
                if (document.documentElement && document.documentElement.scrollTop)
                    return document.documentElement.scrollTop;
                // Internet Explorer 6, 7 and 8
                if (document.body.scrollTop)
                    return document.body.scrollTop;
                return 0;
            }

            function elmYPosition(eID) {
                var elm = document.getElementById(eID);
                var y = elm.offsetTop;
                var node = elm;
                while (node.offsetParent && node.offsetParent !== document.body) {
                    node = node.offsetParent;
                    y += node.offsetTop;
                }
                return y;
            }
        };
        rootScope.doLogin = function (form) {
            var payload = {username: rootScope.login.username, password: rootScope.encryptPass(rootScope.login.password, true)};
            if (rootScope.session && rootScope.session.isProxyLogin) {
                loginToMaster(payload, true, MasterProxyLogin, scope.userProxy);
            }
            else {
                loginToMaster(payload, true);
            }
        };
        rootScope.retrieveUnreadMessages = function () {
            scope.unreadMessages = [];
            var success = function (data) {
                scope.unreadMessages = data;
                if (angular.isDefined(scope.unreadMessages) && scope.unreadMessages.length > 0) {
                    $('#showmsgmodal').modal({
                        keyboard: false,
                        show: true
                    });
                }
            };
            MessagingPopup.retrieveUnreadMessages(success);
        };

        rootScope.retrieveUnreadMsgPopUp = function () {
            var success = function (data) {
                rootScope.messagePopUp = angular.copy(data);
                var html =
                        '<div class="list-group list-special" ng-if="!messagePopUp || messagePopUp.length === 0">' +
                        '<div class = "row list-group-item" >' +
                        '<div class="col-xs-2 text-center notification-image-icon">' +
                        '<span class="glyphicon  glyphicon-user"></span>' +
                        '</div>' +
                        '<div class = "col-xs-10 hkg-bold">' +
                        '{{"MESSAGING." + "No unread messages available." | translate}}' +
                        '</div>' +
                        '</div>' +
                        '</div>' +
                        '<div style="overflow-y:auto; overflow-x:hidden; height:500px;" ng-if="messagePopUp.length > 0" class="list-group list-special">' +
                        '<a class="row list-group-item" ng-repeat="message in messagePopUp">' +
                        '<div class="col-xs-2 text-center notification-image-icon">' +
                        '<span class="glyphicon glyphicon-user"></span>' +
                        '</div>' +
                        '<div class="col-xs-10">' +
                        '<div class="col-xs-10"><label class="control-label">{{message.createdBy}}</label></div>' +
                        '<div class="col-xs-2 text-right" ng-show="{{message.status === \'P\'}}"><span title="Mark as read" class="glyphicon glyphicon-remove pointer pull-right" ng-click="markClosed(message, $index)"/></div>' +
                        '<div class="col-xs-12"><label class="control-label">{{message.createdOn| date:\'mediumDate\'}} {{message.createdOn| date:\'shortTime\'}}</label></div>' +
                        '<div class="col-xs-12 wordRap"  ng-show="{{message.messageBody !== null}}">' +
                        '{{message.messageBody}}' +
                        '</div>' +
                        '<div class="col-xs-12"><span style="padding-left:0px" class="btn btn-link" ng-click="displayMessageDetails(message,$index)">' +
                        '{{ entity + \'View More Details\' | translate}}' +
                        '</span>' + '</div>' +
                        '</div>' +
                        '</a>' +
                        '</div>';
                $(".popover-content").html($compile(html)(scope));
            };
            MessagingPopup.retrieveUnreadMessages(success);
        };

        scope.displayMessageDetails = function (message, index) {
            if (message !== undefined) {
                console.log("msg :" + JSON.stringify(message));
                if (message.hasPriority === true) {
                    scope.markClosedPriorityMsg(message, index);
                } else {
                    scope.markClosed(message, index);
                }
                if (location.path() === '/managemessage') {
                    $('#messages').popover('hide');
                    $('#showalertmodal').modal('hide');
                    rootScope.removeModalOpenCssAfterModalHide();
                    rootScope.$broadcast('rootMessage', {rootMessage: message});
                } else {
                    localStorage.setItem('rootMessage', JSON.stringify(message));
                    $('#messages').popover('hide');
                    $('#showalertmodal').modal('hide');
                    rootScope.removeModalOpenCssAfterModalHide();
                    location.path('/managemessage');
                }
            }
        };
        scope.doProxyLogin = function () {
            var useProxy = scope.userProxy;
            if (!!scope.userProxy) {
                Common.authenticateProxy({id: scope.userProxy}, function (data) {
                    rootScope.authToken = data.token;
                    localStorage.setItem('user', data.token);
                    if (scope.rememberme) {
                        data.password = rootScope.encryptPass(scope.password, true);
                        localStorage.setItem('userDetail', JSON.stringify(data));
                    } else {
                        localStorage.removeItem('userDetail');
                    }
                    var promise = rootScope.pingServer();
                    promise.then(function () {
                        delete rootScope.masterAuthToken;
                        localStorage.removeItem('masterAuthToken');
                        proxyAuthentication(MasterProxyLogin, useProxy);
                        localStorage.setItem("proxyLogin", useProxy);
                        rootScope.retrieveNotificationCount();
//                        scope.retrieveWorkAllocationCount();
                    });
                }, function () {
                    console.log("failure..");
                });
            }
        };
        scope.changeDesignation = function () {
            $http.post(rootScope.appendAuthToken(rootScope.centerapipath + "common/changepreferreddesignation"), rootScope.session.currentDesignation).success(function (data)
            {
                scope.reloadRoute();
//                console.log('success..........');
//                rootScope.pingServer();
            }).error(function () {
//                console.log('failed..........');
            });
        };
        rootScope.removeModalOpenCssAfterModalHide = function () {
            $("body").removeClass("modal-open");
        };
        rootScope.retrieveMsgPopUp = function () {
            $("#recipients").select2("data", undefined);
            $("#recipients").select2("val", undefined);
            rootScope.i18EntityMessagingPopup = "MESSAGING.";
            rootScope.message = {};
            rootScope.addMessageData = {};
            rootScope.dbType = {};
            var templateData = CustomField.retrieveSectionWiseCustomFieldInfos("manageMessages");
            templateData.then(function (section) {
                rootScope.customGeneralMessageTemplateData = angular.copy(section['genralSection']);
                rootScope.generalMessageTemplate = rootScope.getCustomDataInSequence(rootScope.customGeneralMessageTemplateData);
            }, function (reason) {
                console.log('Failed: ' + reason);
            }, function (update) {
                console.log('Got notification: ' + update);
            });
            rootScope.message.hasPriority = false;
            rootScope.clearMsgForm = function () {
                rootScope.message.messageBody = null;
                rootScope.message.nameRecipient = null;
            };
            rootScope.prioritytooltip = "Set as priority";
            rootScope.setpriority = function () {
                if (rootScope.message.hasPriority === false) {
                    rootScope.message.hasPriority = true;
                } else {
                    rootScope.message.hasPriority = false;
                }
            };
            rootScope.recipientValid = false;
            rootScope.$watch('message.nameRecipient', function () {
                rootScope.recipientValid = false;
                if (rootScope.message.nameRecipient != undefined) {
                    if (typeof (rootScope.message.nameRecipient) == "string") {
                        rootScope.recipientValid = true;
                    }
                }
            });
            rootScope.submitMsg = function (form) {
                rootScope.message.submitted = true;
                if (form.$valid) {
                    if (rootScope.message.nameRecipient !== null && rootScope.message.nameRecipient.length > 0) {
                        rootScope.message.submitted = false;
                        var success = function () {
                            $('#messages').popover('hide');
                        };
                        rootScope.message.messageCustom = rootScope.addMessageData;
                        rootScope.message.messageDbType = rootScope.dbType;
                        MessagingPopup.saveMessage(rootScope.message, success);
                    } else {
                        rootScope.message.nameRecipient = "";
                    }
                }
            };
            rootScope.autoCompleteRecipient = {
                multiple: true,
                closeOnSelect: false,
                placeholder: 'Select recipients',
                initSelection: function (element, callback) {
                    var data = [];
                    callback(data);
                },
                formatResult: function (item) {
                    return item.text;
                },
                formatSelection: function (item) {
                    return item.text;
                },
                query: function (query) {
                    var selected = query.term;
                    rootScope.tempNames = [];
                    var success = function (data) {
                        if (data.length == 0) {
                            query.callback({
                                results: rootScope.tempNames
                            });
                        } else {
                            rootScope.tempNames = data;
                            angular.forEach(data, function (item) {
                                rootScope.tempNames.push({
                                    id: item.value + ":" + item.description,
                                    text: item.label
                                });
                            });
                            query.callback({
                                results: rootScope.tempNames
                            });
                        }
                    };
                    var failure = function () {
                    };
                    if (selected.substring(0, 2) == '@E' || selected.substring(0, 2) == '@e') {
                        var search = query.term.slice(2);
                        MessagingPopup.retrieveUserList(search.trim(), success, failure);
                    } else if (selected.substring(0, 2) == '@R' || selected.substring(0, 2) == '@r') {
                        var search = query.term.slice(2);
                        MessagingPopup.retrieveRoleList(search.trim(), success, failure);
                    } else if (selected.substring(0, 2) == '@D' || selected.substring(0, 2) == '@d') {
                        var search = query.term.slice(2);
                        MessagingPopup.retrieveDepartmentList(search.trim(), success, failure);
                    } else if (selected.substring(0, 2) == '@G' || selected.substring(0, 2) == '@g') {
                        var search = query.term.slice(2);
                        MessagingPopup.retrieveGroupList(search.trim(), success, failure);
                    } else if (selected.substring(0, 2) == '@A' || selected.substring(0, 2) == '@a') {
                        var search = query.term.slice(2);
                        MessagingPopup.retrieveActivityList(search.trim(), success, failure);
                    } else if (selected.length > 0) {
                        var search = selected;
                        MessagingPopup.retrieveUserList(search.trim(), success, failure);
                    } else {
                        query.callback({
                            results: rootScope.tempNames
                        });
                    }
                }
            };
            var msgHtml = '<div class="row">&nbsp;</div>' +
                    '<form role="form" name="messaging" ng-init="clearMsgForm()"  class="form-horizontal" novalidate> ' +
                    '<div class="row" >' +
                    '<div class="col-md-12" >' +
                    '<div class="form-group" >' +
                    '<span class="col-xs-12" >' +
                    '<label for=\"messagearea\">' +
                    '{{i18EntityMessagingPopup + \'Message\'| translate }}' +
                    '</label>' +
                    '</span>' +
                    '<div class="col-xs-12" >' +
                    '<div ng-class=\"{\'has-error\': (messaging.message.$dirty || message.submitted) && messaging.message.$invalid}\">' +
                    '<textarea name="message" id="messagearea" rows="3" class="form-control" ng-trim="false" maxlength="500" required ng-model="message.messageBody" placeholder="Message here..."></textarea>' +
                    '<div class="error,help-block" ng-show="(messaging.message.$dirty || message.submitted) && messaging.message.$invalid">' +
                    '<span class="help-block" ng-show="messaging.message.$error.required">{{ i18EntityMessagingPopup + \'Enter message\' | translate }}</span>' +
                    '</div>' +
                    '</div>' +
                    '<div id="messagecounter" class="pull-right center">{{500 - message.messageBody.length}}&nbsp;  {{i18EntityMessagingPopup + \'characters left\'| translate }}</div>' +
                    '</div>' +
                    '</div>' +
                    '<div class="form-group" >' +
                    '<span class="col-xs-12" >' +
                    '<label for="recipients">  {{i18EntityMessagingPopup + \'Send to\'| translate }}' +
                    '</label>' +
                    '</span>' +
                    '<div class="col-xs-12">' +
                    '<div ng-class="{\'has-error\': !recipientValid && message.submitted}">' +
                    '<div class="input-group">' +
                    '<input type="text" class="col-xs-12 hkg-nopadding form-control" id="recipients" value="blank" required ui-select2="autoCompleteRecipient" ng-model="message.nameRecipient"/>' +
                    '<span class="input-group-addon"><span class="glyphicon glyphicon-info-sign" tooltip-html-unsafe="{{popover}}"  tooltip-trigger="mouseenter" tooltip-placement="right"></span></span>' +
                    '<\/div><div class="error,help-block" ng-show="(!recipientValid && message.submitted)">' +
                    '<span class="help-block" ng-show="!recipientValid">{{i18EntityMessagingPopup + \'Select recipients\'| translate }}</span>' +
                    '</div>' +
                    '</div>' +
                    '</div>' +
                    '</div>' +
                    '<div>' +
                    '<dynamic-form input-css ="col-xs-12" label-css="col-xs-12" db-map="dbType" internationalization-label="{{i18DynamicForm}}" form-name="form2" template="generalMessageTemplate" ng-model="addMessageData" ng-if="generalMessageTemplate" edit-flag="false" is-diamond="false" no-of-field-per-row = "1">' +
                    '</dynamic-form>' +
                    '</div>' +
                    '<div class="clearfix"></div>'+
                    '<div class="form-group" >' +
                    '<div class="col-xs-12" >' +
                    '<div class="row">' +
                    '<hr/>'+
                    '<div class="col-xs-12">' +
                    '<div class="col-xs-10">' +
                    '<span class=\"hkg-input-img glyphicon glyphicon-star-empty\" tooltip-html-unsafe=\"{{prioritytooltip}}\"  tooltip-trigger=\"mouseenter\" tooltip-placement=\"bottom\" ng-show=\"!message.hasPriority\" ng-click=\"setpriority();\"><\/span>' +
                    '<span class=\"hkg-input-img glyphicon glyphicon-star\" tooltip-html-unsafe=\"{{prioritytooltip}}\"  tooltip-trigger=\"mouseenter\" tooltip-placement=\"bottom\" ng-show=\"message.hasPriority\" ng-click=\"setpriority();\"><\/span>' +
                    '</div>' +
                    '<button class="btn btn-hkg pull-right" ng-click="submitMsg(messaging)">{{i18EntityMessagingPopup + \'Send\'| translate }}</button>' +
                    '</div>' +
                    '</div>' +
                    '</div>' +
                    '</div>' +
                    '</div>' +
                    '</div>' +
                    '</form>';
            $(".popover-content").html($compile(msgHtml)(scope));
        };

        scope.popover =
                "<NOBR><font color='red;'>Use the shortcuts to search</font></NOBR><br/><table cellpadding='0' cellspacing='0'><tr><td>\ " +
                "\n\ " +
                "'@D'  </td> <td> &nbsp;  &nbsp;</td><td align='left'>Departments</td></tr>\ " +
                "<tr><td>'@E'  </td> <td> &nbsp; &nbsp;</td><td align='left'>Employees</td></tr>\ " +
                "<tr><td>'@R'  </td> <td> &nbsp; &nbsp;</td><td align='left'>Designation</td></tr>\ " +
                "</table>\ ";

        //Set analytics server url.
        rootScope.analyticsLoginUrl = 'http://192.1.200.51:8080/pentaho/j_spring_security_check';
        rootScope.analyticsLogoutUrl = 'http://192.1.200.51:8080/pentaho/Logout';
        rootScope.analyticsPingUrl = 'http://192.1.200.51:8080/pentaho/Home';

    }];
