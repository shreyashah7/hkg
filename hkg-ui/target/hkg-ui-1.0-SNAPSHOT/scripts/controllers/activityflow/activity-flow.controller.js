/**
 * This controller is for manage activity flow feature
 * Author : Shruti Adhvaryu
 * Date : 10 Nov 2014
 */
define(['hkg', 'activityFlowService', 'ruleService', 'messageService', 'customFieldService', 'checklist.directive', 'ruleField', 'ngload!flowChart', 'assetService'], function (hkg, activityFlowService) {
    hkg.register.controller('ActivityFlowController', ["$rootScope", "$scope", "ActivityFlowService", "$timeout", "$filter", "$location", "$window", "RuleService", "$log", "Messaging", "CustomFieldService", "AssetService", function ($rootScope, $scope, ActivityFlowService, $timeout, $filter, $location, $window, RuleService, $log, Messaging, CustomFieldService, AssetService) {

            $rootScope.mainMenu = "manageLink";
            $rootScope.childMenu = "manageActivityFlow";
            $scope.parentId = 0;
            $scope.addFlag = false;
            $rootScope.activateMenu();
            $scope.entity = "ACTIVITYFLOW.";
            $scope.activityflow = {};
            $scope.activityflow.franchise = "";
            $scope.activityflow.version = "";
            $scope.activityFlowVersion = {};
            $scope.activityGroup = {};
            $scope.activityNode = {};
            $scope.activityNodeRoute = {};
            $scope.isVersionPending = false;
            $scope.isVersionActive = false;
            $scope.versionList = {};
            $scope.franchiseList = {};
            $scope.subbmited = false;
            $scope.submitted = false;
            $scope.ruleSubmitted = false;
            $scope.activityFlowDiagram = {};
            $scope.activitylist = [];
            $scope.actName = '';
            $scope.rules = [{id: 5, rule: "equals"}];
            $scope.serviceId = undefined;
            $scope.selectedActivity = '';
            $scope.finalActivityFlow = {};
            $scope.newServiceList = [];
            $scope.versionStatusPending = 'P';
            $scope.versionStatusActive = 'A';
            //-------------rule variables---------------//
            $scope.rulesubmitted = false;
//            $scope.priorityList = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10];
            $scope.applyList = ["All", "Any"];
            $scope.ruleObject = {};
            $scope.ruleList = [];
            $scope.allRules = [];
            $scope.isEditRule = false;
            $scope.rulesetid = null;
            $scope.editRule = false;
            $scope.popover =
                    "<NOBR><font color='red;'>Use the shortcuts to search</font></NOBR><br/><table cellpadding='0' cellspacing='0'><tr><td>\ " +
                    +"\n\ " +
                    +"'@D'  </td> <td> &nbsp;  &nbsp;</td><td align='left'>Departments</td></tr>\ " +
                    +"<tr><td>'@E'  </td> <td> &nbsp; &nbsp;</td><td align='left'>Employees</td></tr>\ " +
                    +"<tr><td>'@R'  </td> <td> &nbsp; &nbsp;</td><td align='left'>Roles</td></tr>\ " +
                    +"<tr><td>'@G'  </td> <td> &nbsp; &nbsp;</td><td align='left'>Groups</td></tr>\ " +
                    +"<tr><td>'@A'  </td> <td> &nbsp; &nbsp;</td><td align='left'>Activities</td></tr>\ " +
                    +"</table>";
            $scope.status = [{"id": true, "name": "Active"}, {"id": false, "name": "InActive"}];
            $scope.fieldListForSer = [{"id": -1,
                    "label": "Status",
                    "oldLabelName": "Status",
                    "type": "Dropdown",
                    "validationPattern": "{}",
                    "isNewField": false,
                    "fieldType": "Long"},
                {"id": -2,
                    "label": "No. of occurance",
                    "oldLabelName": "No. of occurance",
                    "type": "Number",
                    "validationPattern": "{}",
                    "isNewField": false,
                    "fieldType": "Number"},
                {"id": -3,
                    "label": "Time",
                    "oldLabelName": "Time",
                    "type": "Decimal",
                    "validationPattern": "{}",
                    "isNewField": false,
                    "fieldType": "String"},
                {"id": -4,
                    "label": "Name",
                    "oldLabelName": "Name",
                    "type": "Dropdown",
                    "validationPattern": "{}",
                    "isNewField": false,
                    "fieldType": "String"}];
            $scope.fieldListForAct = [{"id": -2,
                    "label": "No. of occurance",
                    "oldLabelName": "No. of occurance",
                    "type": "Number",
                    "validationPattern": "{}",
                    "isNewField": false,
                    "fieldType": "Number"},
                {"id": -3,
                    "label": "Time",
                    "oldLabelName": "Time",
                    "type": "Decimal",
                    "validationPattern": "{}",
                    "isNewField": false,
                    "fieldType": "String"},
                {"id": -4,
                    "label": "Name",
                    "oldLabelName": "Name",
                    "type": "Dropdown",
                    "validationPattern": "{}",
                    "isNewField": false,
                    "fieldType": "String"}];
            $scope.entityList = {};
            $scope.selectedRule = null;
            $scope.lastRuleId = 0;
            $scope.operators = {};
            var operatorMap = {"Number": "Numeric",
                "Text field": "String",
                "Text area": "String",
                "Dropdown": "String",
                "Radio button": "String",
                "Date": "Date",
                "Datepicker": "Date",
                "Date range": "Date range",
                "Time": "Date",
                "Time range": "Date range",
                "Password": "String",
                "Currency": "Numeric",
                "Decimal": "Numeric",
                "Email": "String",
                "Phone": "String",
                "Checkbox": "Boolean",
                "Percent": "Numeric",
                "Formula": "Numeric",
                "Pointer": "Numeric",
                "AutoGenerated": "Boolean",
                "MultiSelect": "String",
                "UserMultiSelect": "String",
                "Angle": "Numeric",
                "Image": "Image",
                "Upload": "Image"
            };
            //---------------Rule variables end--------//


            $scope.$on('$viewContentLoaded', function () {
                $scope.initializeActivityFlow();
            });
            $scope.initializeActivityFlow = function () {
                retrievePrerequisite();
                $scope.retrieveAllServices();
                $scope.retrieveDesignations();
                $scope.retrieveModifiers();
                $scope.retrievePlanStatus();
            };
            $scope.retrieveAllServices = function () {
                $rootScope.maskLoading();
                ActivityFlowService.retrieveServices(function (res) {
                    $scope.serviceList = res;
                    $rootScope.unMaskLoading();
                });
            };
            $scope.retrieveModifiers = function () {
                ActivityFlowService.retrieveModifiers(function (res) {
                    $scope.modifierList = res['Mandatory'];
                    $scope.nonModifierList = res['NonMandatory'];
                });
            };
            $scope.retrieveDesignations = function () {
                $rootScope.maskLoading();
                ActivityFlowService.retrieveDesignations(function (res) {
                    $scope.designationList = res.data;
                    $rootScope.unMaskLoading();
                });
            };
            $scope.selectMe = function (key) {

            };
            function retrievePrerequisite() {
                $rootScope.maskLoading();
                ActivityFlowService.retrievePrerequisite(function (res) {
                    if (angular.isDefined(res) && res != null) {
                        var versions = res['activityflowbycompany'];
                        if ($rootScope.session !== undefined && $rootScope.session.isHKAdmin) {
                            $scope.franchiseList = res['activefranchises'];
                            if ($rootScope.session !== undefined && $rootScope.session.isHKAdmin && $location.path() !== '/printsvg') {
                                var franchise = $rootScope.session.companyId;
                                if ($scope.franchiseList !== undefined && $scope.franchiseList.length > 0) {
                                    for (var index = 0; index < $scope.franchiseList.length; index++) {
                                        if ($scope.franchiseList[index].value === franchise) {
                                            $scope.activityflow.franchise = $scope.franchiseList[index];
                                        }
                                    }
                                    $scope.onFranchiseChange($scope.activityflow.franchise);
                                }
                            }
                            if ($location.path() === '/printsvg') {
                                $scope.activityflow.version = JSON.parse(localStorage.getItem("activityFlowVersion"));
                                $scope.retrieveActivityFlowVersion(true);
                            }
                        } else {
                            if (angular.isDefined(versions[0]) && versions[0] !== null) {
                                $scope.activityflow.franchise = versions[0];
                                localStorage.setItem("activityFlowFranchise", JSON.stringify($scope.activityflow.franchise));
                                $scope.activityflow.activityId = versions[0].value;
                                $scope.versionList = versions[0]["custom2"];
                                if ($location.path() === '/manageactivityflow') {
                                    $scope.activityflow.version = $scope.versionList[0];
                                } else {
                                    $scope.activityflow.version = JSON.parse(localStorage.getItem("activityFlowVersion"));
                                }
                                $scope.onVersionChange($scope.activityflow.version);
                            }

                        }
                    }

                });
                $rootScope.unMaskLoading();
            }

            $scope.positive = function () {
                $rootScope.deselectFlag = true;
                $rootScope.sample();
            };

            $scope.onFranchiseChange = function (franchise) {
                $rootScope.maskLoading();
                if (franchise !== null) {
                    localStorage.setItem("activityFlowFranchise", JSON.stringify(franchise));
                    ActivityFlowService.retrieveActivityFlowByCompany(franchise.value, function (res) {
                        $rootScope.unMaskLoading();
                        if (angular.isDefined(res.data[0]) && res.data[0] != null) {
                            $scope.versionList = res.data[0].custom2;
                            $scope.activityflow.activityId = franchise.value;
                            $scope.activityflow.version = $scope.versionList[0];
                            $scope.onVersionChange($scope.activityflow.version);
                        } else {
                            $scope.versionList = {};
                        }

                    });
                } else {
                    $rootScope.unMaskLoading();
                    $scope.versionList = {};
                }
            };
            $scope.onVersionChange = function (version) {
                $scope.isVersionPending = false;
                $scope.isVersionActive = false;
                var readOnly = false;
                if ($location.path() !== '/manageactivityflow') {
                    readOnly = true;
                }
                if (version !== null) {
                    localStorage.setItem("activityFlowVersion", JSON.stringify(version));
                    if (version.description === $scope.versionStatusPending) {
                        $scope.isVersionPending = true;
                    } else if (version.description === $scope.versionStatusActive) {
                        $scope.isVersionActive = true;
                    } else {
                        readOnly = true;
                    }
                    $scope.retrieveActivityFlowVersion(readOnly);
                } else {
                    $scope.chartViewModel = null;
                }
            };
            $scope.closeAddActivityVersionPopup = function () {
                $scope.activityflow.versionId = "";
                $("#addNewActivityVersionPopup").modal("hide");
                $rootScope.removeModalOpenCssAfterModalHide();
            };
            $scope.showAddActivityVersionPopup = function () {
                $("#addNewActivityVersionPopup").modal("show");
            };
            $scope.createNewVersion = function () {
                $scope.activityFlowVersion = {};
                $scope.activityFlowVersion.activityId = $scope.activityflow.activityId;
                if ($scope.activityflow.versionId !== null && $scope.activityflow.versionId !== undefined) {
                    $scope.activityFlowVersion.flowVersionId = $scope.activityflow.versionId.value;
                    $rootScope.maskLoading();
                    ActivityFlowService.addActivityFlowVersion($scope.activityFlowVersion, function (res) {
                        $rootScope.unMaskLoading();
                        if (res.messages) {
                            $rootScope.addMessage(res.messages[0].message, $rootScope.failure);
                            $scope.initializeActivityFlow();
                            $scope.closeAddActivityVersionPopup();
                            // $scope.retrieveActivityFlowVersion(res.flowVersionId);
                        } else {
                            $scope.initializeActivityFlow();
                            $scope.closeAddActivityVersionPopup();
                            $timeout(function () {
                                angular.forEach($scope.versionList, function (item) {
                                    if (item.value === res.flowVersionId) {
                                        $scope.activityflow.version = item;
                                        $scope.onVersionChange(item);
                                        //$scope.retrieveActivityFlowVersion(res.flowVersionId);
                                    }
                                });
                            }, 300);
                        }
                    }, function () {
                    });
                } else {
                    $scope.activityFlowVersion.flowVersionId = null;
                    $rootScope.maskLoading();
                    ActivityFlowService.addActivityFlowVersion($scope.activityFlowVersion, function (res) {
                        $rootScope.unMaskLoading();
                        if (res.messages) {
                            $rootScope.addMessage(res.messages[0].message, $rootScope.failure);
                        }
                        $scope.initializeActivityFlow();
                        $scope.closeAddActivityVersionPopup();
                    }, function () {
                    });
                }

            };
            $scope.publishVersion = function () {
                if ($scope.activityflow && $scope.activityflow.version.value && $scope.activityflow.version.value !== null) {
                    $rootScope.maskLoading();
                    ActivityFlowService.publishVersion($scope.activityflow.version.value, function (res) {
                        $rootScope.unMaskLoading();
                        $scope.initializeActivityFlow();
//                        $scope.onVersionChange($scope.activityflow.version.value);
                    });
                }

            };
            $scope.openActivityFlow = function () {
                $scope.activityGroup = {};
                $scope.subbmited = false;
                $('#activityPopUp').modal('show');
            };
            $scope.hideActivityPanel = function (activityForm) {
                $scope.activityGroup = {};
                activityForm.$setPristine();
                $scope.subbmited = false;
                $('#activityPopUp').modal('hide');
                $rootScope.removeModalOpenCssAfterModalHide();
            };
            $scope.hideEditActivityPanel = function (activityForm) {
                $scope.activityGroup = {};
                activityForm.$setPristine();
                $scope.subbmited = false;
                $('#activityEditPopUp').modal('hide');
                $rootScope.removeModalOpenCssAfterModalHide();
            };
            $scope.addActivity = function (activityForm) {
                $scope.subbmited = true;
                $scope.activityGroup.versionId = $scope.activityflow.version.value;
                $scope.activityGroup.x = $scope.newPosX;
                $scope.activityGroup.y = $scope.newPosY;
                if (activityForm.$valid) {
                    $rootScope.maskLoading();
                    ActivityFlowService.addActivityFlowGroup($scope.activityGroup, function (res) {
                        $rootScope.unMaskLoading();
                        if (!res.messages) {
                            $scope.retrieveActivityFlowVersion(false);
                            if ($scope.chartViewModel === null) {
                                $scope.chartViewModel = new flowchart.ChartViewModel(chartDataModel);
                            }
                        } else {
                            $rootScope.addMessage(res.messages[0].message, $rootScope.failure);
                        }
                    }, function () {
                    });
                    $scope.activityGroup = {};
                    activityForm.$setPristine();
                    $scope.hideActivityPanel(activityForm);
                }
            };
            $scope.doesActivityNameExists = function (activityName, addActivityForm) {
                angular.forEach($scope.activitylist, function (item) {
                    if (item.name === activityName) {
                        addActivityForm.activityName.$setValidity("exists", false);
                    }
                });
            };
            $scope.editActivity = function (activityForm) {
                $scope.subbmited = true;
                $scope.activityGroup.versionId = $scope.activityflow.version.value;
                $scope.activityGroup.x = $scope.newPosX;
                $scope.activityGroup.y = $scope.newPosY;
                if (activityForm.$valid) {
                    if ($rootScope.flowGroupName !== null) {
                        $scope.activityGroup.flowGroupName = $rootScope.flowGroupName;
                    }
                    if ($rootScope.groupId !== null) {
                        $scope.activityGroup.groupId = $rootScope.groupId;
                    }
                    $rootScope.maskLoading();
                    ActivityFlowService.updateGroup($scope.activityGroup, function (res) {
                        $rootScope.unMaskLoading();
                        if (!res.messages) {
                            $scope.flowGroupName = null;
                            $rootScope.flowGroupName = null;
                            $scope.retrieveActivityFlowVersion(false);
                        } else {
                            $rootScope.addMessage(res.messages[0].message, $rootScope.failure);
                        }
                    }, function () {
                    });
                    $scope.activityGroup = {};
                    activityForm.$setPristine();
                    $scope.hideEditActivityPanel(activityForm);
                }
            };
            $scope.openService = function () {
                $scope.activityNode = {};
                $scope.associatedService = '';
                $scope.submitted = false;
                $scope.retrieveAllServices();
                $('#servicePopUp').modal('show');
            };
            $scope.hideService = function (serviceForm) {
                $scope.activityNode = {};
                if (!!$scope.activityFlow && !!$scope.activityFlow.plans) {
                    delete $scope.activityFlow.plans;
                }
                $scope.activityflow.modifier = "";
                $scope.activityflow.noOfPlans = "";
                $scope.activityflow.nonModifierCodes = [];
                serviceForm.$setPristine();
                $scope.designation = "";
                $scope.submitted = false;
                $('#servicePopUp').modal('hide');
                $rootScope.removeModalOpenCssAfterModalHide();
            };
            $scope.addService = function (serviceForm) {
                $scope.submitted = true;
                $scope.flagmembers = false;
                if ($scope.activityFlow !== undefined && $scope.activityFlow.plans !== undefined && $scope.activityFlow.plans !== null && $scope.activityflow.nonModifierCodes !== undefined && $scope.activityflow.nonModifierCodes !== null && $scope.activityflow.nonModifierCodes.indexOf('SP') > -1) {
                    angular.forEach($scope.activityFlow.plans, function (item) {
                        item.rowsubmitted = true;
                        if (item.taskRecipients.length === 0) {
                            $scope.flagmembers = true;
                        }
                    });
                }
                if (serviceForm.$valid && !$scope.flagmembers) {
                    $scope.activityNode.versionId = $scope.activityflow.version.value;
                    $scope.activityNode.associatedService = $scope.associatedService.id;
                    $scope.activityNode.designationId = $scope.designation;
                    $scope.activityNode.x = $scope.newPosX - $rootScope.mouseOverGroup.x();
                    $scope.activityNode.y = $scope.newPosY - $rootScope.mouseOverGroup.y();
                    $scope.activityNode.groupId = $rootScope.mouseOverGroup.data.id;
                    $scope.activityNode.isLastService = $scope.finalServiceModel;
                    if ($scope.finalServiceModel === true) {
                        $scope.activityNode.previousFinalNode = $scope.finalServiceId;
                    }
                    $scope.activityNode.modifier = $scope.activityflow.modifier;
                    $scope.activityNode.nonMandatoryModifier = $scope.activityflow.nonModifierCodes;
                    $scope.activityNode.noOfPlans = $scope.activityflow.noOfPlans;
                    if (!!$scope.activityflow.nonModifierCodes && $scope.activityflow.nonModifierCodes.indexOf('SP') === -1) {
                        if (!!$scope.activityFlow && !!$scope.activityFlow.plans)
                            delete $scope.activityFlow.plans;
                    }
                    if (!!$scope.activityFlow && !!$scope.activityFlow.plans) {
                        var plans = [];
                        angular.forEach($scope.activityFlow.plans, function (item) {
                            plans.push({"accessToUsers": item.taskRecipients, "planStatuses": item.status});
                        });
                        $scope.activityNode.plans = plans;
                    }
                    if (!!$scope.activityNode.plans) {
                        var listOfCodes = [];
                        angular.forEach($scope.activityNode.plans, function (planObj) {
                            var temp = planObj.accessToUsers.split(",");
                            if (!!temp) {
                                angular.forEach(temp, function (temp1) {
                                    listOfCodes.push(temp1);
                                });
                            }

                        });
                        ActivityFlowService.retrieveUsersByCode(listOfCodes, function (data) {
                            $scope.listOfUserForMultiselect = [];
                            if (!!data) {
                                $scope.listOfUserForMultiselect = data;
                            }
                        });
                    }
                    $rootScope.maskLoading();
                    ActivityFlowService.saveActivityNode($scope.activityNode, function (res) {
                        $rootScope.unMaskLoading();
                        if (res.data !== null) {
//                            var activity = $filter('filter')($scope.activitylist, function(activity) {
//                                return activity.id === $rootScope.mouseOverGroup.data.id;
//                            })[0];
                            var serviceEnt = $filter('filter')($scope.serviceList, function (service) {
                                return service.id === $scope.associatedService.id;
                            })[0];
//                            $scope.nodes = {id: res.data, service: $scope.associatedService, routes: []};
//                            activity.nodes.push($scope.nodes);
                            if (!!$scope.activityNode.plans) {

                                angular.forEach($scope.activityNode.plans, function (item) {
                                    item.multiselectConfig = {multiple: true,
                                        closeOnSelect: false,
                                        placeholder: 'Select members',
                                        initSelection: function (element, callback) {
                                            var data = [];
                                            var codeList = item.accessToUsers.split(",");
                                            angular.forEach(codeList, function (codeId) {
                                                angular.forEach($scope.listOfUserForMultiselect, function (recipient) {
                                                    if (codeId == (recipient.recipientInstance + ":" + recipient.recipientType)) {
                                                        data.push({
                                                            id: recipient.recipientInstance + ":" + recipient.recipientType,
                                                            text: recipient.recipientValue
                                                        });
                                                    }
                                                });
                                            });
                                            callback(data);
                                            item.taskRecipients = item.accessToUsers;
                                        },
                                        formatResult: function (item) {
                                            return item.text;
                                        },
                                        formatSelection: function (item) {
                                            return item.text;
                                        },
                                        query: function (query) {
                                            var selected = query.term;
                                            $scope.names = [];
                                            var success = function (data) {
                                                if (data.length !== 0) {
                                                    $scope.names = data;
                                                    angular.forEach(data, function (item) {
                                                        $scope.names.push({
                                                            id: item.value + ":" + item.description,
                                                            text: item.label

                                                        });
                                                    });
                                                }
                                                query.callback({
                                                    results: $scope.names
                                                });
                                            };
                                            var failure = function () {
                                            };
                                            if (selected.substring(0, 2) === '@E' || selected.substring(0, 2) === '@e') {
                                                var search = query.term.slice(2);
                                                if ($rootScope.session.isHKAdmin) {
                                                    var payload = {"user": search.trim(), franchise: $scope.activityflow.franchise.value};
                                                    Messaging.retrieveUserListFranchise(payload, success, failure);
                                                } else {
                                                    AssetService.retrieveUserList(search.trim(), success, failure);
                                                }
                                            } else if (selected.substring(0, 2) === '@D' || selected.substring(0, 2) === '@d') {
                                                var search = query.term.slice(2);
                                                if ($rootScope.session.isHKAdmin) {
                                                    var payload = {"department": search.trim(), franchise: $scope.activityflow.franchise.value};
                                                    Messaging.retrieveDepartmentListFranchise(payload, success, failure);
                                                } else {
                                                    Messaging.retrieveDepartmentList(search.trim(), success, failure);
                                                }
                                            } else if (selected.substring(0, 2) == '@R' || selected.substring(0, 2) == '@r') {
                                                var search = query.term.slice(2);
                                                if ($rootScope.session.isHKAdmin) {
                                                    var payload = {"role": search.trim(), franchise: $scope.activityflow.franchise.value};
                                                    Messaging.retrieveRoleListFranchise(payload, success, failure);
                                                } else {
                                                    Messaging.retrieveRoleList(search.trim(), success, failure);
                                                }
                                            } else if (selected.substring(0, 2) === '@A' || selected.substring(0, 2) === '@a') {
                                                var search = query.term.slice(2);
                                                Messaging.retrieveActivityList(search.trim(), success, failure);
                                            } else if (selected.substring(0, 2) == '@G' || selected.substring(0, 2) == '@g') {
                                                var search = query.term.slice(2);
                                                Messaging.retrieveGroupList(search.trim(), success, failure);
                                            } else if (selected.length > 0) {
                                                var search = selected;
                                                if ($rootScope.session.isHKAdmin) {
                                                    var payload = {"user": search.trim(), franchise: $scope.activityflow.franchise.value};
                                                    Messaging.retrieveUserListFranchise(payload, success, failure);
                                                } else {
                                                    AssetService.retrieveUserList(search.trim(), success, failure);
                                                }
                                            } else {
                                                query.callback({
                                                    results: $scope.names
                                                });
                                            }
                                        }};
                                    item.statusMultiselectconfig = {
                                        'allowClear': true,
                                        'data': $scope.planStatusList,
                                        'placeholder': "Select",
                                        'multiple': true,
                                        initSelection: function (element, callback) {
                                            var tempdata = item.planStatuses.split(",");
                                            var data = [];
                                            angular.forEach(tempdata, function (code) {
                                                angular.forEach($scope.planStatusList, function (item) {
                                                    if (item.id == code) {
                                                        data.push({id: code, text: item.text});
                                                    }
                                                });
                                            });
                                            callback(data);
                                            item.status = item.planStatuses;
                                        },
                                        formatResult: function (item) {
                                            return item.text;
                                        },
                                        formatSelection: function (item) {
                                            return item.text;
                                        },
                                        query: function (query) {
                                            query.callback({
                                                results: $scope.planStatusList
                                            });
                                        }
                                    };
                                });
                            }

                            var newNodeDataModel = {
                                name: serviceEnt.serviceName,
                                serviceCode: serviceEnt.serviceCode,
                                id: res.data,
                                x: $scope.newPosX - $rootScope.mouseOverGroup.x(),
                                y: $scope.newPosY - $rootScope.mouseOverGroup.y(),
                                "designation": $scope.designation,
                                "modifier": $scope.activityflow.modifier,
                                "nonModifierCodes": $scope.activityNode.nonMandatoryModifier,
                                "plans": $scope.activityNode.plans,
                                "noOfPlans": $scope.activityNode.noOfPlans,
                                inputConnectors: [{
                                        "name": "X"
                                    }],
                                outputConnectors: [{
                                        "name": "1"
                                    }]
                            };
                            var checked = $('#finalServiceAdd').is(':checked');
                            if (checked) {
                                $scope.finalServiceId = newNodeDataModel.id;
                            }
                            $scope.finalServiceModel = false;
                            $scope.designation = "";
                            $rootScope.mouseOverGroup.addNode(newNodeDataModel);
                            $scope.hideService(serviceForm);
                        } else {
                            $rootScope.addMessage(res.messages[0].message, $rootScope.failure);
                            $scope.hideService(serviceForm);
                        }
                        $scope.activityflow.modifier = "";
                        $scope.activityflow.nonModifierCodes = [];
                        delete $scope.activityflow.plans;
                        $scope.activityNode = {};
//                        $scope.activityflow = {};
                        serviceForm.$setPristine();
                    });
                }
            };
            $scope.openRule = function () {
                $scope.activityNodeRoute = {};
                $scope.currentGroupId = '';
                $scope.nextGroupId = '';
                $scope.selectedRule = '';
                $scope.ruleSubmitted = false;
                $('#rulesPopUp').modal('show');
            };
            $scope.hideRule = function (ruleForm, isCancelled) {
                $scope.addFlag = false;
                $scope.currentGroupId = '';
                $scope.nextGroupId = '';
                $scope.selectedRule = '';
                $scope.rulesetid = null;
                $scope.allRules = [];
                $scope.activityNodeRoute = {};
                if (isCancelled && !$scope.isEditRule) {
                    $scope.chartViewModel.cancelled($rootScope.sourceNodeId, $rootScope.destinationNodeId);
                }
                $scope.isEditRule = false;
                ruleForm.$setPristine();
                $scope.ruleSubmitted = false;
                $('#rulesPopUp').modal('hide');
                $rootScope.removeModalOpenCssAfterModalHide();
            };
            $scope.addRuleAndRuleSet = function (ruleForm) {
                $scope.save(ruleForm);
                $scope.ruleSubmitted = true;
                var flag = true;
                for (var index = 0; index < $scope.ruleList.length; index++) {
                    var length2 = $scope.ruleList[index].operator.shortcutCode;
                    for (var i = 0; i < length2; i++) {
                        var val = $scope.ruleList[index].values[i];

                        if ($scope.ruleList[index].operator.label !== 'has no value' && $scope.ruleList[index].operator.label !== 'has any value')
                        {
                            if ($scope.ruleList[index].field.type === "MultiSelect")
                            {
                                flag = (flag && true);
                            } else {
                                if (val !== null && val !== undefined) {
                                    flag = (flag && true);
                                } else {
                                    flag = false;
                                    break;
                                }
                            }
                        } else
                        {
                            flag = (flag && true);
                        }
                    }
                    if (flag === false)
                        break;
                }
                if (!!$scope.allRules) {
                    if (flag && ruleForm.$valid || ($scope.allRules.length <= 0 || ($scope.selectedRule === null || $scope.selectedRule === undefined))) {
                        $scope.addFlag = false;
                        $scope.save(ruleForm);
                        $log.info("in save and submit");
                        // submit logic
                        var ruleset = {};
                        ruleset.rules = angular.copy($scope.allRules);
                        ruleset.id = $scope.rulesetid;
                        ruleset.franchise = $scope.activityflow.franchise.otherId;
                        $scope.activityNodeRoute.ruleSet = ruleset;
                        if ($scope.isEditRule) {
                            $log.info("UPDATE rule::      ");
                            RuleService.updateRule(ruleset, function (res) {
//                        $log.info(JSON.stringify(res));

                                $scope.hideRule(ruleForm, false);
                            });
                            $scope.allRules = [];
                            $scope.rulesetid = null;
                            return;
                        }
                        $scope.activityNodeRoute.curentNode = $rootScope.sourceNodeId;
                        $scope.activityNodeRoute.nextNode = $rootScope.destinationNodeId;
                        $scope.activityNodeRoute.activityVersion = $scope.activityflow.version.value;
//                    if ($scope.allRules.length > 0) {
                        $rootScope.maskLoading();
                        if (!!!$scope.allRules) {
                            delete $scope.activityNodeRoute.ruleSet.rules;
                        }
                        ActivityFlowService.saveActivityNodeRoute($scope.activityNodeRoute, function (res) {
                            $log.info("Activity route..." + JSON.stringify($scope.activityNodeRoute));
                            $rootScope.unMaskLoading();
                            if (res.data !== null) {
                                $scope.chartViewModel.addRouteId($rootScope.sourceNodeId, $rootScope.destinationNodeId, res.data["routeId"], res.data["ruleset"]);
//                            var activity = $filter('filter')($scope.activitylist, function(activity) {
//                                return activity.id === $rootScope.mouseOverGroup.data.id;
//                                ;
//                            })[0];
//                            $log.info('out '+$scope.activityNodeRoute.curentNode);
//                            var route = $filter('filter')(activity.nodes, function(route) {
//                                //alert(route.id);
//                                return route.id === $scope.activityNodeRoute.curentNode;
//                            })[0];
//                            $scope.nodeRoutes = {id: res.data, next: $scope.activityNodeRoute.nextNode, status: 'Y'};
//                            route.routes.push($scope.nodeRoutes);
                            } else {
                                $rootScope.addMessage(res.messages[0].message, $rootScope.failure);
                            }
                            $scope.activityNodeRoute = {};
                            ruleForm.$setPristine();
                            $scope.rulesetid = null;
                            $scope.hideRule(ruleForm, false);
                        });
//                    }
                    } else {
                        return;
                    }
                }
            };
            $scope.listOutCurrentService = function () {
                var activity = $filter('filter')($scope.activitylist, function (activity) {
                    return activity.id === $scope.currentGroupId;
                })[0];
                if (activity) {
                    $scope.currentServiceList = activity.nodes;
                } else {
                    $scope.currentServiceList = [];
                }
            };
            $scope.listOutNextService = function () {
                var activity = $filter('filter')($scope.activitylist, function (activity) {
                    return activity.id === $scope.nextGroupId;
                })[0];
                if (activity) {
                    $scope.nextServiceList = activity.nodes;
                } else {
                    $scope.nextServiceList = [];
                }
            };
            $scope.checkCurrentNode = function (currentId, nextId, currentNode, nextNode, ruleForm) {
                if (currentId === nextId) {
                    if (currentNode === nextNode) {
                        ruleForm.nextService.$setValidity("nodeCheck", false);
                    }
                } else {
                    ruleForm.nextService.$setValidity("nodeCheck", true);
                }
            };
            // ------------------------------------------ flow chart diagram ------------------------------------------------//
            // Added By dmehta
            // 24-11-2014


            // Code for the delete key.
            //
            var deleteKeyCode = 46;
            //
            // Code for control key.
            //
            var ctrlKeyCode = 17;
            //
            // Set to true when the ctrl key is down.
            //
            var ctrlDown = false;
            //
            // Code for A key.
            //
            var aKeyCode = 65;
            //
            // Code for esc key.
            //
            var escKeyCode = 27;
            //
            // Selects the next node id.
            //
            var nextNodeID = 10;
            //
            // Setup the data-model for the chart.
            //
            var chartDataModel = {
                isreadonly: false,
                groups: [
                ],
                "connections": [
                ]
            };
            //
            // Event handler for key-down on the flowchart.
            //
            $rootScope.keyDown = function (evt) {
                if (evt.keyCode === ctrlKeyCode) {
                    ctrlDown = true;
                    evt.stopPropagation();
                    evt.preventDefault();
                }
            };
            //
            // Event handler for key-up on the flowchart.
            //

            $scope.deleteComponent = function () {
                var res = $scope.chartViewModel.deleteSelected();
                var nodeMap = new Object();
                nodeMap['groupIds'] = res.groupIds;
                nodeMap['nodeIds'] = res.nodeIds;
                nodeMap['connectorIds'] = res.connectorIds;
                nodeMap['rulesetIds'] = res.rulesetIds;
                var success = function () {
                    $scope.hideDelete();
                }
                ActivityFlowService.deleteComponent(nodeMap, success);
            };
            $scope.hideDelete = function () {
                $("#deletePopUp").modal("hide");
                $rootScope.removeModalOpenCssAfterModalHide();
            };
            $rootScope.keyUp = function (evt) {
                if (evt.keyCode === deleteKeyCode) {
                    //
                    // Delete key.
                    //                    
                    //   
                    if ($scope.activityflow.version.description !== 'A') {
                        $("#deletePopUp").modal("show");
                    }
//                    $scope.chartViewModel.deleteSelected();
                }

                if (evt.keyCode === aKeyCode && ctrlDown) {
                    // 
                    // Ctrl + A
                    //                    
                    $scope.chartViewModel.selectAll();
                }

                if (evt.keyCode === escKeyCode) {
                    // Escape.
                    $scope.chartViewModel.deselectAll();
                }

                if (evt.keyCode === ctrlKeyCode) {
                    ctrlDown = false;
                    evt.stopPropagation();
                    evt.preventDefault();
                }
            };
            //
            // Add a new node to the chart.
            //
            $scope.addNewNode = function () {

                var nodeName = prompt("Enter a node name:", "New node");
                if (!nodeName) {
                    return;
                }

                //
                // Template for a new node.
                //
                var newNodeDataModel = {
                    name: nodeName,
                    id: nextNodeID++,
                    x: 0,
                    y: 0,
                    inputConnectors: [
                        {
                            name: "X"
                        }
                    ],
                    outputConnectors: [
                        {
                            name: "1"
                        }
                    ]
                };
                $scope.chartViewModel.addNode(newNodeDataModel);
            };
            //
            // Add an input connector to selected groups.
            //
            $scope.addNewInputConnector = function () {
                var connectorName = prompt("Enter a connector name:", "New connector");
                if (!connectorName) {
                    return;
                }

                var selectedNodes = $scope.chartViewModel.getSelectedNodes();
                for (var i = 0; i < selectedNodes.length; ++i) {
                    var node = selectedNodes[i];
                    node.addInputConnector({
                        name: connectorName
                    });
                }
            };
            //
            // Add an output connector to selected groups.
            //
            $scope.addNewOutputConnector = function () {
                var connectorName = prompt("Enter a connector name:", "New connector");
                if (!connectorName) {
                    return;
                }

                var selectedNodes = $scope.chartViewModel.getSelectedNodes();
                for (var i = 0; i < selectedNodes.length; ++i) {
                    var node = selectedNodes[i];
                    node.addOutputConnector({
                        name: connectorName
                    });
                }
            };
            $scope.retrieveActivityFlowVersion = function (readOnlyFlag) {
                chartDataModel = {
                    "isreadonly": readOnlyFlag,
                    "groups": [
                    ],
                    "connections": [
                    ]
                };
                if ($scope.activityflow.version !== null) {
                    $rootScope.maskLoading();
                    ActivityFlowService.retrieveActivityFlowVersion($scope.activityflow.version.value, function (res) {
                        $rootScope.unMaskLoading();
                        if (!!$scope.activitylist) {
                            $scope.activitylist = [];
                        }
                        if (res.data.activityFlowGroups !== null && res.data.activityFlowGroups.length > 0) {
                            angular.forEach(res.data.activityFlowGroups, function (item) {
                                $scope.activitylist.push(item);
                                if (item.nodeDataBeanList !== null) {
                                    $scope.nodes = [];
                                    angular.forEach(item.nodeDataBeanList, function (nodeItem) {
                                        var serviceEnt = $filter('filter')($scope.serviceList, function (service) {
                                            return service.id === nodeItem.associatedService;
                                        })[0];
                                        var designation = $filter('filter')($scope.designationList, function (desg) {
                                            return desg.value === nodeItem.designationId;
                                        })[0];
                                        if (!!nodeItem.plans) {
//                                            var index = 1;
                                            $scope.activityFlow = {};
                                            $scope.activityFlow["plans"] = [];
                                            angular.forEach(nodeItem.plans, function (item) {
                                                item.multiselectConfig = {multiple: true,
                                                    closeOnSelect: false,
                                                    placeholder: 'Select members',
                                                    initSelection: function (element, callback) {
                                                        var data = [];
                                                        angular.forEach(item.userData, function (recipient) {
                                                            data.push({
                                                                id: recipient.recipientInstance + ":" + recipient.recipientType,
                                                                text: recipient.recipientValue
                                                            });
                                                        });
                                                        callback(data);
                                                        item.taskRecipients = item.accessToUsers;
                                                    },
                                                    formatResult: function (item) {
                                                        return item.text;
                                                    },
                                                    formatSelection: function (item) {
                                                        return item.text;
                                                    },
                                                    query: function (query) {
                                                        var selected = query.term;
                                                        $scope.names = [];
                                                        var success = function (data) {
                                                            if (data.length !== 0) {
                                                                $scope.names = data;
                                                                angular.forEach(data, function (item) {
                                                                    $scope.names.push({
                                                                        id: item.value + ":" + item.description,
                                                                        text: item.label

                                                                    });
                                                                });
                                                            }
                                                            query.callback({
                                                                results: $scope.names
                                                            });
                                                        };
                                                        var failure = function () {
                                                        };
                                                        if (selected.substring(0, 2) === '@E' || selected.substring(0, 2) === '@e') {
                                                            var search = query.term.slice(2);
                                                            if ($rootScope.session.isHKAdmin) {
                                                                var payload = {"user": search.trim(), franchise: $scope.activityflow.franchise.value};
                                                                Messaging.retrieveUserListFranchise(payload, success, failure);
                                                            } else {
                                                                AssetService.retrieveUserList(search.trim(), success, failure);
                                                            }
                                                        } else if (selected.substring(0, 2) === '@D' || selected.substring(0, 2) === '@d') {
                                                            var search = query.term.slice(2);
                                                            if ($rootScope.session.isHKAdmin) {
                                                                var payload = {"department": search.trim(), franchise: $scope.activityflow.franchise.value};
                                                                Messaging.retrieveDepartmentListFranchise(payload, success, failure);
                                                            } else {
                                                                Messaging.retrieveDepartmentList(search.trim(), success, failure);
                                                            }
                                                        } else if (selected.substring(0, 2) == '@R' || selected.substring(0, 2) == '@r') {
                                                            var search = query.term.slice(2);
                                                            if ($rootScope.session.isHKAdmin) {
                                                                var payload = {"role": search.trim(), franchise: $scope.activityflow.franchise.value};
                                                                Messaging.retrieveRoleListFranchise(payload, success, failure);
                                                            } else {
                                                                Messaging.retrieveRoleList(search.trim(), success, failure);
                                                            }
                                                        } else if (selected.substring(0, 2) === '@A' || selected.substring(0, 2) === '@a') {
                                                            var search = query.term.slice(2);
                                                            Messaging.retrieveActivityList(search.trim(), success, failure);
                                                        } else if (selected.substring(0, 2) == '@G' || selected.substring(0, 2) == '@g') {
                                                            var search = query.term.slice(2);
                                                            Messaging.retrieveGroupList(search.trim(), success, failure);
                                                        } else if (selected.length > 0) {
                                                            var search = selected;
                                                            if ($rootScope.session.isHKAdmin) {
                                                                var payload = {"user": search.trim(), franchise: $scope.activityflow.franchise.value};
                                                                Messaging.retrieveUserListFranchise(payload, success, failure);
                                                            } else {
                                                                AssetService.retrieveUserList(search.trim(), success, failure);
                                                            }
                                                        } else {
                                                            query.callback({
                                                                results: $scope.names
                                                            });
                                                        }
                                                    }};
                                                item.statusMultiselectconfig = {
                                                    'allowClear': true,
                                                    'data': $scope.planStatusList,
                                                    'placeholder': "Select",
                                                    'multiple': true,
                                                    initSelection: function (element, callback) {
                                                        var tempdata = item.planStatuses.split(",");
                                                        var data = [];
                                                        angular.forEach(tempdata, function (code) {
                                                            angular.forEach($scope.planStatusList, function (item) {
                                                                if (item.id == code) {
                                                                    data.push({id: code, text: item.text});
                                                                }
                                                                ;
                                                            });
                                                        });
                                                        callback(data);
                                                        item.status = item.planStatuses;
                                                    },
                                                    formatResult: function (item) {
                                                        return item.text;
                                                    },
                                                    formatSelection: function (item) {
                                                        return item.text;
                                                    },
                                                    query: function (query) {
                                                        query.callback({
                                                            results: $scope.planStatusList
                                                        });
                                                    }
                                                };
                                            });
                                        }
                                        if (designation) {
                                            $scope.nodeDataModel = {
                                                "name": serviceEnt.serviceName,
                                                "id": nodeItem.nodeId,
                                                "serviceCode": serviceEnt.serviceCode,
                                                "x": nodeItem.x,
                                                "y": nodeItem.y,
                                                "designation": nodeItem.designationId,
                                                "desigName": designation.label,
                                                "modifier": nodeItem.modifier,
                                                "nonModifierCodes": nodeItem.nonMandatoryModifier,
                                                "plans": nodeItem.plans,
                                                "noOfPlans": nodeItem.noOfPlans,
                                                "inputConnectors": [
                                                    {
                                                        "name": "X"
                                                    }
                                                ],
                                                "outputConnectors": [
                                                    {
                                                        "name": "1"
                                                    }
                                                ],
                                                "active": nodeItem.selected
                                            };
                                        } else {
                                            $scope.nodeDataModel = {
                                                "name": serviceEnt.serviceName,
                                                "id": nodeItem.nodeId,
                                                "serviceCode": serviceEnt.serviceCode,
                                                "x": nodeItem.x,
                                                "y": nodeItem.y,
                                                "designation": nodeItem.designationId,
                                                "modifier": nodeItem.modifier,
                                                "nonModifierCodes": nodeItem.nonMandatoryModifier,
                                                "plans": nodeItem.plans,
                                                "noOfPlans": nodeItem.noOfPlans,
                                                "inputConnectors": [
                                                    {
                                                        "name": "X"
                                                    }
                                                ],
                                                "outputConnectors": [
                                                    {
                                                        "name": "1"
                                                    }
                                                ],
                                                "active": nodeItem.selected
                                            };
                                        }
                                        if (nodeItem.isLastService === true) {
                                            $scope.finalServiceId = nodeItem.nodeId;
                                        }
                                        $scope.nodes.push($scope.nodeDataModel);
                                    });
                                    var groupDataModel = {
                                        "name": item.flowGroupName,
                                        "description": item.description,
                                        "id": item.groupId,
                                        "x": item.x,
                                        "y": item.y,
                                        "nodes": [],
                                        "active": item.selected
                                    };
                                    angular.forEach($scope.nodes, function (item) {
                                        groupDataModel.nodes.push(item);
                                    })
                                    chartDataModel.groups.push(groupDataModel);
                                } else {
                                    var groupDataModel = {
                                        "name": item.flowGroupName,
                                        "description": item.description,
                                        "id": item.groupId,
                                        "x": item.x,
                                        "y": item.y,
                                        "nodes": [],
                                        "active": item.selected
                                    };
                                    chartDataModel.groups.push(groupDataModel);
                                }
                            });
                            if (res.data.activityFlowNodeRoutes !== null) {
                                angular.forEach(res.data.activityFlowNodeRoutes, function (item) {
                                    var connectionDataModel = {
                                        "id": item.nodeRouteId,
                                        "ruleset": item.nodeRouteStatus,
                                        "source": {
                                            "nodeID": item.curentNode,
                                            "connectorIndex": 0
                                        },
                                        "dest": {
                                            "nodeID": item.nextNode,
                                            "connectorIndex": 0
                                        },
                                        "active": item.selected
                                    };
                                    chartDataModel.connections.push(connectionDataModel);
                                });
                            }
                            if ($location.path() === '/manageactivityflow') {
                                $scope.chartViewModel = new flowchart.ChartViewModel(chartDataModel);
                            } else {
                                $scope.onlyViewModel = new flowchart.ChartViewModel(chartDataModel);
                            }
                        } else {
                            $scope.chartViewModel = new flowchart.ChartViewModel(chartDataModel);
                        }
                    });
                }
            };
            //
            // Create the view-model for the chart and attach to the scope.
            //                        
            $("#dragActivity").draggable({helper: "clone", cursor: 'move', opacity: 0.25});
            $("#dragService").draggable({helper: "clone", cursor: 'move', opacity: 0.25});
            $("#activityFlowFrame").droppable({
                drop: function (event, ui) {
// position of the draggable minus position of the droppable
// relative to the document
                    $scope.newPosX = ui.offset.left - $(this).offset().left;
                    $scope.newPosY = ui.offset.top - $(this).offset().top;
                    var draggableId = ui.draggable.attr("id");
                    if (draggableId === 'dragActivity') {
                        $scope.openActivityFlow();
                    } else if (draggableId === 'dragService') {
                        if ($rootScope.mouseOverGroup !== null) {
                            $scope.openService();
                        } else {
                            $rootScope.addMessage("Invalid drop , please try again to put it in activity.", 1);
                        }
                    }
                }
            });
            $scope.hideEditService = function (serviceForm) {
                $scope.activityNode = {};
                delete $rootScope.plans;
                $rootScope.noOfPlans = '';
                $rootScope.nonModifierCodes = "";
                $rootScope.modifierCode = '';
                $rootScope.designationId = "";
                serviceForm.$setPristine();
                $scope.submitted = false;
                $('#serviceEditPopUp').modal('hide');
                $rootScope.removeModalOpenCssAfterModalHide();
            };
            $scope.editService = function (serviceForm) {
                $scope.submitted = true;
                $scope.flagmembers = false;
                if ($scope.activityFlow !== undefined && $scope.activityFlow.plans !== undefined && $scope.activityFlow.plans !== null && $scope.activityflow.nonModifierCodes !== undefined && $scope.activityflow.nonModifierCodes !== null && $scope.activityflow.nonModifierCodes.indexOf('SP') > -1) {
                    angular.forEach($scope.activityFlow.plans, function (item) {
                        item.rowsubmitted = true;
                        if (item.taskRecipients.length === 0) {
                            $scope.flagmembers = true;
                        }
                    });
                }
                if (serviceForm.$valid && $rootScope.serviceId !== null && !$scope.flagmembers) {
                    $scope.activityNode.nodeId = $rootScope.id;
                    $scope.activityNode.designationId = $rootScope.designationId;
                    $scope.activityNode.isLastService = $('#finalService').is(':checked');
                    $scope.activityNode.modifier = $rootScope.modifierCode;
                    $scope.activityNode.nonMandatoryModifier = $rootScope.nonModifierCodes;
                    $scope.activityNode.noOfPlans = $rootScope.noOfPlans;
//                    $scope.activityNode.plans = $rootScope.plans;
                    if (!!$rootScope.nonModifierCodes && $rootScope.nonModifierCodes.indexOf('SP') === -1) {
                        if (!!$rootScope.plans)
                            delete $rootScope.plans;
                    }
                    if (!!$rootScope.plans) {
                        var plans = [];
                        angular.forEach($rootScope.plans, function (item) {
                            var users = "";
                            if (angular.isArray(item.taskRecipients)) {
                                angular.forEach(item.taskRecipients, function (data) {
                                    users += data.id + ",";
                                });
                                users = users.replace(/,\s*$/, "");
                            } else {
                                users = item.taskRecipients;
                            }

                            var statusStr = "";
                            if (angular.isArray(item.status)) {
                                angular.forEach(item.status, function (data1) {
                                    statusStr += data1.id + ",";
                                });
                                statusStr = statusStr.replace(/,\s*$/, "");
                            } else {
                                statusStr = item.status;
                            }
                            plans.push({"accessToUsers": users, "planStatuses": statusStr});
                        });

                        $scope.activityNode.plans = plans;
                    }
                    if ($('#finalService').is(':checked') === true) {
                        $scope.activityNode.previousFinalNode = $scope.finalServiceId;
                    }
                    $rootScope.maskLoading();
                    ActivityFlowService.updateNode($scope.activityNode, function () {
                        $rootScope.unMaskLoading();
                        var checked = $('#finalService').is(':checked');
                        if (checked) {
                            $scope.finalServiceId = $rootScope.id;
                        } else {
                            $scope.finalServiceId = "";
                        }
                        $scope.retrieveActivityFlowVersion(false);
                        $scope.hideEditService(serviceForm);
                        $scope.activityNode = {};
                        serviceForm.$setPristine();
                    });
                }
            };
            //------------------------------------------ diagram functions end ----------------------------------//

            //------------------------------------------ Rule Functions -----------------------------------------//

            $('#rulesPopUp').on('show.bs.modal', function (e) {
                $log.info("rules popup show event");
                initAddRule();
                $scope.rulesetid = $rootScope.ruleSetId;
                if ($scope.rulesetid !== undefined && $scope.rulesetid !== null) {
                    $rootScope.ruleSetId = null;
                    $scope.isEditRule = true;
                    RuleService.retrieveRuleById("{\"primaryKey\": \"" + $scope.rulesetid + "\"}", function (res) {


                        $scope.allRules = angular.copy(res.rules);
                        angular.forEach($scope.allRules, function (allRule)
                        {
                            for (var j = 0; j < allRule.criterias.length; j++) {

                                if (allRule.criterias[j].fieldType === "UserMultiSelect" || allRule.criterias[j].fieldType === "MultiSelect" || allRule.criterias[j].fieldType === "Dropdown" || allRule.criterias[j].fieldType === "Pointer" || allRule.criterias[j].fieldType === "SubEntity")
                                {
//                                    if (allRule.criterias[j].value.toString().indexOf("[") > -1 && allRule.criterias[j].value.toString().indexOf("]") > -1)
//                                    {
                                    allRule.criterias[j].value = angular.copy(allRule.criterias[j].value.toString().replace("[", "").replace("]", ""));
//                                    }
                                }
                            }
                        });

                    });
                }
//                $scope.$apply();

            });
            var initializeRule = function () {
                var rule = {};
                rule.entity = "";
                rule.field = "";
                rule.operator = "";
                rule.options = null;
                rule.operatorList = [];
                rule.fieldList = [];
                rule.rowsubmitted = false;
                rule.hint = "Enter value";
//                rule.priority = null;
                rule.values = [];
                return rule;
            }
            var initializeRuleObject = function () {
                $scope.ruleObject = {};
                $scope.ruleList = [];
                var rule = initializeRule();
                $scope.ruleList.push(rule);
                $scope.ruleObject.remarks = "";
                $scope.ruleObject.id = null;
                rule.rowsubmitted = false;
                $scope.ruleObject.isActive = true;
                $scope.ruleObject.ruleName = "";
                $scope.ruleObject.apply = "All";
            }
            var initAddRule = function () {
                $scope.allRules = [];
                RuleService.retrievePrerequisite(function (res) {
                    $scope.entityList = res.entity;
                    if (!!!$scope.entityList) {
                        $scope.entityList = {};
                    }
                    $scope.entityList["-1"] = "Current Activity";
                    $scope.entityList["-2"] = "Next Activity";
                    $scope.entityList["-3"] = "Previous Activity";
                    $scope.entityList["-4"] = "Current Service";
                    $scope.entityList["-5"] = "Next Service";
                    $scope.entityList["-6"] = "Previous Service";
                    $scope.operators = res.operator;
                });
                initializeRuleObject();
                $scope.displayFlag = 'create';
            };
            function setRuleObject(src) {
                $scope.ruleObject = {};
                $scope.ruleList = [];
                if (src !== null && src !== undefined) {
                    $scope.ruleObject.id = src.id;
                    $scope.ruleObject.isActive = src.isActive;
                    $scope.ruleObject.isArchive = src.isArchive;
                    $scope.ruleObject.remarks = src.remarks;
                    $scope.ruleObject.ruleName = src.ruleName;
                    $scope.ruleObject.apply = src.apply;
                    var max = 0;
                    for (var i = 0; i < src.criterias.length; i++) {
                        if (src.criterias[i].id > max)
                            max = src.criterias[i].id;
                    }
//                    $log.info("max=        "+max);
                    $scope.lastRuleId = max;
                    setRuleList(src.criterias, 0);
                }


            }
            function setRuleList(rules, index) {
                if (index < rules.length) {
                    var rule = {}
                    rule.values = [];
                    rule.options = null;
                    rule.hint = "Enter value";
                    if (rules[index].fieldType === "Date range") {
                        rule.values[0] = rules[index].value.split(',')[0];
                        rule.values[1] = rules[index].value.split(',')[1];
                        if (rules[index].value1 !== null) {
                            rule.values[2] = rules[index].value1.split(',')[0];
                            rule.values[3] = rules[index].value1.split(',')[1];
                        }
                    } else {
                        if (rules[index].fieldType !== "Pointer" && rules[index].fieldType !== "SubEntity") {
                            if (rules[index].fieldType === "MultiSelect" || rules[index].fieldType === "Dropdown")
                            {
                                rule.values = rules[index].value;

//                            if (rules[index].value1 !== null) {
//                                rule.values[1] = rules[index].value1;
//                            }
                            }
                            else
                            {
                                rule.values[0] = rules[index].value;

                                if (rules[index].value1 !== null) {
                                    rule.values[1] = rules[index].value1;
                                }
                            }
                        }
                        else if (rules[index].fieldType === "SubEntity") {
                            if (rules[index].subentityComponentType !== "Date")
                            {
                                rule.values = rules[index].value;

//                            if (rules[index].value1 !== null) {
//                                rule.values[1] = rules[index].value1;
//                            }
                            }
                            else
                            {
                                rule.values[0] = rules[index].value;

                                if (rules[index].value1 !== null) {
                                    rule.values[1] = rules[index].value1;
                                }
                            }
                        }

                        else
                        {
                            if (rules[index].pointerComponentType === "MultiSelect" || rules[index].pointerComponentType === "Dropdown")
                            {
                                rule.values = rules[index].value;
                            }
                            else
                            {
                                rule.values[0] = rules[index].value;

                                if (rules[index].value1 !== null) {
                                    rule.values[1] = rules[index].value1;
                                }
                            }
                        }
                    }
                    rule.id = rules[index].id;
//                    rule.priority = rules[index].priority;
                    rule.entity = rules[index].entity.toString();
                    var id = rules[index].field;
                    var operator = rules[index].operator;
                    if (parseInt(rule.entity) < 0) {
                        if (parseInt(rule.entity) >= -3 && parseInt(rule.entity) <= -1) {
                            rule.fieldList = $scope.fieldListForAct;
                        } else {
                            rule.fieldList = $scope.fieldListForSer;
                        }
                        $.grep(rule.fieldList, function (e, i) {
                            if (e.id == id) {
                                rule.field = rule.fieldList[i];
                                return true;
                            }
                        });
                        tmp(rule, operator, rules, index);
                    } else {
                        RuleService.retrieveFieldsByEntity("{\"primaryKey\": " + rule.entity + "}", function (res) {
//                            $log.info("field list: " + JSON.stringify(res));
                            rule.fieldList = res;
                            $.grep(rule.fieldList, function (e, i) {
                                if (e.id == id) {
                                    rule.field = rule.fieldList[i];
                                    return true;
                                }
                            });
                            tmp(rule, operator, rules, index);
                        });
                    }
                }
            }
            function tmp(rule, operator, rules, index) {
                rule.operatorList = [];
                if (rule.field.type !== "SubEntity") {
                    rule.operatorList = $scope.operators[operatorMap[rule.field.type]];

                }
                else
                {
                    rule.operatorList = $scope.operators[operatorMap['Dropdown']];
                }
                $.grep(rule.operatorList, function (e, i) {
                    if (e.label === operator) {
                        rule.operator = rule.operatorList[i];
                        return true;
                    }
                });

                if (rule.field.type === "Dropdown" || rule.field.type === "Radio button" || rule.field.type === "MultiSelect") {
                    if (parseInt(rule.entity) <= -1 && parseInt(rule.entity) >= -3 && parseInt(rule.field.id) !== -1) {
                        RuleService.retrieveActivityMasterByFieldById("{\"primaryKey\": " + $scope.activityflow.version.value + "}", function (res) {
                            rule.options = res;
                            $.grep(rule.options, function (e, i) {
                                if (e.value === rule.value) {
                                    rule.value[0] = rule.options[i];
                                    return true;
                                }
                            });
                            if (rule.value1 !== null) {
                                $.grep(rule.options, function (e, i) {
                                    if (e.value === rule.value1) {
                                        rule.value[1] = rule.options[i];
                                        return true;
                                    }
                                });
                            }
                        });
                    } else if (parseInt(rule.entity) <= -4 && parseInt(rule.entity) >= -6 && parseInt(rule.field.id) !== -1) {
                        RuleService.retrieveServiceMasterByFieldById("{\"primaryKey\": " + $scope.activityflow.version.value + "}", function (res) {
                            rule.options = res;
                            $.grep(rule.options, function (e, i) {
                                if (e.value === rule.value) {
                                    rule.value[0] = rule.options[i];
                                    return true;
                                }
                            });
                            if (rule.value1 !== null) {
                                $.grep(rule.options, function (e, i) {
                                    if (e.value === rule.value1) {
                                        rule.value[1] = rule.options[i];
                                        return true;
                                    }
                                });
                            }
                        });
                    } else {

                        if (rule.field.dbFieldName === 'lot_status' || rule.field.dbFieldName === 'packet_status' || rule.field.dbFieldName === 'status_of_plan'
                                || rule.field.dbFieldName === 'parcel_status' || rule.field.dbFieldName === 'invoice_status'
                                || rule.field.dbFieldName === 'issue_status' || rule.field.dbFieldName === 'type_of_plan')
                        {
                            var dbFieldName = rule.field.dbFieldName;
                            RuleService.retrieveLotStatus(dbFieldName, function (res) {
                                rule.options = res;
                                $.grep(rule.options, function (e, i) {
                                    if (e.value === rule.value) {
                                        rule.value[0] = rule.options[i];
                                        return true;
                                    }
                                });
                                if (rule.value1 !== null) {
                                    $.grep(rule.options, function (e, i) {
                                        if (e.value === rule.value1) {
                                            rule.value[1] = rule.options[i];
                                            return true;
                                        }
                                    });
                                }
                            });


                        }


                        else {
                            RuleService.retrieveMasterByFieldById("{\"primaryKey\": " + rule.field.id + "}", function (res) {
                                rule.options = res;
                                if (rule.field.type !== "MultiSelect") {
                                    $.grep(rule.options, function (e, i) {
                                        if (e.value === rule.value) {
                                            rule.value[0] = rule.options[i];
                                            return true;
                                        }
                                    });
                                    if (rule.value1 !== null) {
                                        $.grep(rule.options, function (e, i) {
                                            if (e.value === rule.value1) {
                                                rule.value[1] = rule.options[i];
                                                return true;
                                            }
                                        });
                                    }
                                }
                            });
                        }
                    }
                } else if (rule.field.type === "Checkbox") {
                    rule.options = [{label: 'True', value: true}, {label: 'False', value: false}];
                }
                else if (rule.field.type === "Pointer")
                {
                    var pointerComponentType;
                    var validationPattern = rule.field.validationPattern.replace("{", "")
                            .replace("}", "");
                    var validationsArr = [];
                    var pointerArray = [];
                    var pointerId;
                    validationsArr = validationPattern.split(",");
                    angular.forEach(validationsArr, function (validate)
                    {
                        if (validate.indexOf("\"pointer\":") > -1) {
                            pointerArray = validate.split(":");
                            pointerId = pointerArray[1].replace(/["']/g, "");
                            rule.field.pointerId = pointerId;
                        }
                    });
                    angular.forEach(rule.fieldList, function (fieldList)
                    {
                        if (fieldList.id.toString() === pointerId.toString())
                        {
                            pointerComponentType = fieldList.type;
                            rule.field.pointertype = pointerComponentType;
                        }
                    });
                    RuleService.retrieveMasterByFieldById("{\"primaryKey\": " + pointerId + "}", function (res) {
                        rule.options = res;
                    });
                }
                else
                if (rule.field.type === 'SubEntity')
                {
                    rule.field.subEntityType = "Dropdown";

                    RuleService.retrieveDropListForSubEntity(rule.field.id, function (res) {
                        rule.options = res;
                    });
                }
                rule.showtemplate = false;
                rule.showtemplate = true;
                function ruleOptions()
                {
                    if (rule.field.type === "MultiSelect") {
                        RuleService.retrieveMasterByFieldById("{\"primaryKey\": " + rule.field.id + "}", function (res) {
                            rule.options = res;
                        });
                    }
                }
                $scope.ruleList[index] = rule;
                index++;


                setRuleList(rules, index);


                ruleOptions();
                if (rule.field.type === "MultiSelect")
                {
                    angular.forEach($scope.ruleList, function (item) {
                        var valueList = angular.copy(item.values)
                        item.multiselectComponent = {
                            multiple: true,
                            closeOnSelect: false,
                            placeholder: 'Select ',
                            initSelection: function (element, callback) {
                                var data = [];
                                RuleService.retrieveMasterByFieldById("{\"primaryKey\": " + item.field.id + "}", function (res) {
                                    $scope.optionsMultiSelect = res;
                                    var codeList = [];
                                    codeList = angular.copy(valueList.toString().replace("[", "").replace("]", "").split(","));
                                    var tempdatamultiple = angular.copy(item.values);
                                    angular.forEach(codeList, function (codeId) {
                                        angular.forEach($scope.optionsMultiSelect, function (recipient) {
                                            if (codeId.toString() === recipient.value.toString()) {
                                                data.push({
                                                    id: recipient.value,
                                                    text: recipient.label
                                                });
                                            }
                                        });
                                    });
                                    callback(data);
                                    item.values = angular.copy(tempdatamultiple);
                                });
                            },
                            formatResult: function (item) {
                                return item.text;
                            },
                            formatSelection: function (item) {
                                return item.text;
                            },
                            query: function (query) {
                                var selected = query.term;
                                $scope.names = [];
//                                           
                                if (item.options.length !== 0) {

                                    $scope.names = angular.copy(item.options);
                                    angular.forEach(item.options, function (item) {
                                        $scope.names.push({
                                            id: item.value,
                                            text: item.label

                                        });
                                    });
                                }
                                query.callback({
                                    results: $scope.names
                                });
//                                            


                            }};

                        item.multiselectSingleComponent = {
                            multiple: true,
                            closeOnSelect: false,
                            placeholder: 'Select',
                            maximumSelectionSize: 1,
                            initSelection: function (element, callback) {
                                var data = [];
                                RuleService.retrieveMasterByFieldById("{\"primaryKey\": " + item.field.id + "}", function (res) {
                                    $scope.optionsMultiSelect = res;
                                    var codeList = [];
                                    codeList = angular.copy(valueList.toString().replace("[", "").replace("]", "").split(","));
                                    var tempdatamultiple = angular.copy(item.values);
                                    angular.forEach(codeList, function (codeId) {
                                        angular.forEach($scope.optionsMultiSelect, function (recipient) {
                                            if (codeId.toString() === recipient.value.toString()) {
                                                data.push({
                                                    id: recipient.value,
                                                    text: recipient.label
                                                });
                                            }
                                        });
                                    });
                                    callback(data);
                                    item.values = angular.copy(tempdatamultiple);
                                });
                            },
                            formatResult: function (item) {
                                return item.text;
                            },
                            formatSelection: function (item) {
                                return item.text;
                            },
                            query: function (query) {
                                var selected = query.term;
                                $scope.names = [];
//                                           
                                if (item.options.length !== 0) {

                                    $scope.names = angular.copy(item.options);
                                    angular.forEach(item.options, function (item) {
                                        $scope.names.push({
                                            id: item.value,
                                            text: item.label

                                        });
                                    });
                                }
                                query.callback({
                                    results: $scope.names
                                });
//                                            


                            }};

                    });
                }

                if (rule.field.type === "Pointer")
                {

                    angular.forEach($scope.ruleList, function (item) {

                        var pointerId;
                        if (rule.field.pointerId !== null && rule.field.pointerId !== undefined) {
                            pointerId = rule.field.pointerId.replace("\"", "");
                        }
                        var valueList = angular.copy(item.values)
                        item.pointerMultiSelectComponent = {
                            multiple: true,
                            closeOnSelect: false,
                            placeholder: 'Select ',
                            initSelection: function (element, callback) {
                                var data = [];
                                RuleService.retrieveMasterByFieldById("{\"primaryKey\": " + pointerId + "}", function (res) {
                                    $scope.optionspointerMultiSelect = res;
                                    var codeList = [];
                                    codeList = angular.copy(valueList.toString().replace("[", "").replace("]", "").split(","));
                                    var tempdatamultiplepointer = angular.copy(item.values);
                                    angular.forEach(codeList, function (codeId) {
                                        angular.forEach($scope.optionspointerMultiSelect, function (recipient) {
                                            if (codeId.toString() === recipient.value.toString()) {
                                                data.push({
                                                    id: recipient.value,
                                                    text: recipient.label
                                                });
                                            }
                                        });
                                    });
                                    callback(data);
                                    item.values = angular.copy(tempdatamultiplepointer);
                                });
                            },
                            formatResult: function (item) {
                                return item.text;
                            },
                            formatSelection: function (item) {
                                return item.text;
                            },
                            query: function (query) {
                                var selected = query.term;
                                $scope.names = [];
//                                           
                                if (item.options.length !== 0) {

                                    $scope.names = angular.copy(item.options);
                                    angular.forEach(item.options, function (item) {
                                        $scope.names.push({
                                            id: item.value,
                                            text: item.label

                                        });
                                    });
                                }
                                query.callback({
                                    results: $scope.names
                                });
//                                            


                            }};

                        item.pointerSingleSelectComponent = {
                            multiple: true,
                            closeOnSelect: false,
                            placeholder: 'Select',
                            maximumSelectionSize: 1,
                            initSelection: function (element, callback) {
                                var data = [];
                                var pointerId = item.field.pointerId.replace("\"", "");
                                RuleService.retrieveMasterByFieldById("{\"primaryKey\": " + pointerId + "}", function (res) {
                                    $scope.optionsSingleSelectPointer = res;
                                    var codeList = [];
                                    codeList = angular.copy(valueList.toString().replace("[", "").replace("]", "").split(","));
                                    var tempdatasinglepointer = angular.copy(item.values);
                                    angular.forEach(codeList, function (codeId) {
                                        angular.forEach($scope.optionsSingleSelectPointer, function (recipient) {
                                            if (codeId.toString() === recipient.value.toString()) {
                                                data.push({
                                                    id: recipient.value,
                                                    text: recipient.label
                                                });
                                            }
                                        });
                                    });
                                    callback(data);
                                    item.values = angular.copy(tempdatasinglepointer);
                                });
                            },
                            formatResult: function (item) {
                                return item.text;
                            },
                            formatSelection: function (item) {
                                return item.text;
                            },
                            query: function (query) {
                                var selected = query.term;
                                $scope.names = [];
//                                           
                                if (item.options.length !== 0) {

                                    $scope.names = angular.copy(item.options);
                                    angular.forEach(item.options, function (item) {
                                        $scope.names.push({
                                            id: item.value,
                                            text: item.label

                                        });
                                    });
                                }
                                query.callback({
                                    results: $scope.names
                                });
//                                            


                            }};

                    });
                }

                if (rule.field.type === "SubEntity")
                {

                    angular.forEach($scope.ruleList, function (item) {

                        var pointerId;
                        if (rule.field.pointerId !== null && rule.field.pointerId !== undefined) {
                            pointerId = rule.field.pointerId.replace("\"", "");
                        }
                        var valueList = angular.copy(item.values)
                        item.subEntityMultiSelectComponent = {
                            multiple: true,
                            closeOnSelect: false,
                            placeholder: 'Select ',
                            initSelection: function (element, callback) {
                                var data = [];

                                RuleService.retrieveDropListForSubEntity(item.field.id, function (res) {
                                    $scope.optionsSubentityMultiSelect = res;
                                    var codeList = [];
                                    codeList = angular.copy(valueList.toString().replace("[", "").replace("]", "").split(","));
                                    var tempdatamultipleSubEntity = angular.copy(item.values);
                                    angular.forEach(codeList, function (codeId) {
                                        angular.forEach($scope.optionsSubentityMultiSelect, function (recipient) {
                                            if (codeId.toString() === recipient.value.toString()) {
                                                data.push({
                                                    id: recipient.value,
                                                    text: recipient.label
                                                });
                                            }
                                        });
                                    });
                                    callback(data);
                                    item.values = angular.copy(tempdatamultipleSubEntity);
                                });
                            },
                            formatResult: function (item) {
                                return item.text;
                            },
                            formatSelection: function (item) {
                                return item.text;
                            },
                            query: function (query) {
                                var selected = query.term;
                                $scope.names = [];
//                                           
                                if (item.options.length !== 0) {

                                    $scope.names = angular.copy(item.options);
                                    angular.forEach(item.options, function (item) {
                                        $scope.names.push({
                                            id: item.value,
                                            text: item.label

                                        });
                                    });
                                }
                                query.callback({
                                    results: $scope.names
                                });
//                                            


                            }};

                        item.subEntitySingleSelectComponent = {
                            multiple: true,
                            closeOnSelect: false,
                            placeholder: 'Select',
                            maximumSelectionSize: 1,
                            initSelection: function (element, callback) {
                                var data = [];

                                RuleService.retrieveDropListForSubEntity(item.field.id, function (res) {
                                    $scope.optionsSubentitySingleSelect = res;
                                    var codeList = [];
                                    codeList = angular.copy(valueList.toString().replace("[", "").replace("]", "").split(","));
                                    var tempdatamultipleSubEntity = angular.copy(item.values);
                                    angular.forEach(codeList, function (codeId) {
                                        angular.forEach($scope.optionsSubentitySingleSelect, function (recipient) {
                                            if (codeId.toString() === recipient.value.toString()) {
                                                data.push({
                                                    id: recipient.value,
                                                    text: recipient.label
                                                });
                                            }
                                        });
                                    });
                                    callback(data);
                                    item.values = angular.copy(tempdatamultipleSubEntity);
                                });
                            },
                            formatResult: function (item) {
                                return item.text;
                            },
                            formatSelection: function (item) {
                                return item.text;
                            },
                            query: function (query) {
                                var selected = query.term;
                                $scope.names = [];
//                                           
                                if (item.options.length !== 0) {

                                    $scope.names = angular.copy(item.options);
                                    angular.forEach(item.options, function (item) {
                                        $scope.names.push({
                                            id: item.value,
                                            text: item.label

                                        });
                                    });
                                }
                                query.callback({
                                    results: $scope.names
                                });
//                                            


                            }};

                    });
                }


                // This method is for edit dropdown component
                if (rule.field.type === "Dropdown")
                {
                    angular.forEach($scope.ruleList, function (item) {
                        if (rule.id === item.id) {
                            var valueList = angular.copy(item.values)
                            item.dropdownMultiSelectComponent = {
                                multiple: true,
                                closeOnSelect: false,
                                placeholder: 'Select ',
                                initSelection: function (element, callback) {
                                    var data = [];
                                    var success = function (res) {
                                        $scope.optionsDropdown = res;
                                        var codeList = [];
                                        codeList = angular.copy(valueList.toString().replace("[", "").replace("]", "").split(","));
                                        var tempdatadropdown = angular.copy(item.values);
                                        angular.forEach(codeList, function (codeId) {
                                            angular.forEach($scope.optionsDropdown, function (recipient) {
                                                if (codeId.toString() === recipient.value.toString()) {
                                                    data.push({
                                                        id: recipient.value,
                                                        text: recipient.label
                                                    });
                                                }
                                            });
                                        });
                                        callback(data);
                                        item.values = angular.copy(tempdatadropdown);
                                    }
                                    var failure = function ()
                                    {
                                    }
                                    if (parseInt(rule.entity) <= -1 && parseInt(rule.entity) >= -3 && parseInt(rule.field.id) !== -1) {
                                        RuleService.retrieveActivityMasterByFieldById("{\"primaryKey\": " + $scope.activityflow.version.value + "}", success, failure);
                                    }
                                    else if (parseInt(rule.entity) <= -4 && parseInt(rule.entity) >= -6 && parseInt(rule.field.id) !== -1) {
                                        RuleService.retrieveServiceMasterByFieldById("{\"primaryKey\": " + $scope.activityflow.version.value + "}", success, failure)
                                    }
                                    else {
                                        if (rule.field.dbFieldName === 'lot_status' || rule.field.dbFieldName === 'packet_status' || rule.field.dbFieldName === 'status_of_plan'
                                                || rule.field.dbFieldName === 'parcel_status' || rule.field.dbFieldName === 'invoice_status'
                                                || rule.field.dbFieldName === 'issue_status' || rule.field.dbFieldName === 'type_of_plan')
                                        {
                                            var dbFieldName = rule.field.dbFieldName;
                                            RuleService.retrieveLotStatus(dbFieldName, success, failure);
                                        } else {
                                            RuleService.retrieveMasterByFieldById("{\"primaryKey\": " + item.field.id + "}", success, failure);
                                        }
                                    }
//                             

                                }
                                ,
                                formatResult: function (item) {
                                    return item.text;
                                },
                                formatSelection: function (item) {
                                    return item.text;
                                },
                                query: function (query) {
                                    var selected = query.term;
                                    $scope.names = [];
//                                           
                                    if (item.options.length !== 0) {

                                        $scope.names = angular.copy(item.options);
                                        angular.forEach(item.options, function (item) {
                                            $scope.names.push({
                                                id: item.value,
                                                text: item.label

                                            });
                                        });
                                    }
                                    query.callback({
                                        results: $scope.names
                                    });
//                                            


                                }};
                            // For multiselect Single Select
                            item.dropdownSingleSelectComponent = {
                                multiple: true,
                                closeOnSelect: false,
                                placeholder: 'Select',
                                maximumSelectionSize: 1,
                                initSelection: function (element, callback) {
                                    var data = [];
                                    var success = function (res) {
                                        $scope.optionsDropdown = res;
                                        var codeList = [];
                                        codeList = angular.copy(valueList.toString().replace("[", "").replace("]", "").split(","));
                                        var tempdatadropdown = angular.copy(item.values);
                                        angular.forEach(codeList, function (codeId) {
                                            angular.forEach($scope.optionsDropdown, function (recipient) {
                                                if (codeId.toString() === recipient.value.toString()) {
                                                    data.push({
                                                        id: recipient.value,
                                                        text: recipient.label
                                                    });
                                                }
                                            });
                                        });
                                        callback(data);
                                        item.values = angular.copy(tempdatadropdown);
                                    }
                                    var failure = function ()
                                    {
                                    }
                                    if (parseInt(rule.entity) <= -1 && parseInt(rule.entity) >= -3 && parseInt(rule.field.id) !== -1) {
                                        RuleService.retrieveActivityMasterByFieldById("{\"primaryKey\": " + $scope.activityflow.version.value + "}", success, failure);
                                    }
                                    else if (parseInt(rule.entity) <= -4 && parseInt(rule.entity) >= -6 && parseInt(rule.field.id) !== -1) {
                                        RuleService.retrieveServiceMasterByFieldById("{\"primaryKey\": " + $scope.activityflow.version.value + "}", success, failure)
                                    }
                                    else {
                                        if (rule.field.dbFieldName === 'lot_status' || rule.field.dbFieldName === 'packet_status' || rule.field.dbFieldName === 'status_of_plan'
                                                || rule.field.dbFieldName === 'parcel_status' || rule.field.dbFieldName === 'invoice_status'
                                                || rule.field.dbFieldName === 'issue_status' || rule.field.dbFieldName === 'type_of_plan')
                                        {
                                            var dbFieldName = rule.field.dbFieldName;
                                            RuleService.retrieveLotStatus(dbFieldName, success, failure);
                                        } else {
                                            RuleService.retrieveMasterByFieldById("{\"primaryKey\": " + item.field.id + "}", success, failure);
                                        }
                                    }
//                             

                                }
                                ,
                                formatResult: function (item) {
                                    return item.text;
                                },
                                formatSelection: function (item) {
                                    return item.text;
                                },
                                query: function (query) {
                                    var selected = query.term;
                                    $scope.names = [];
//                                           
                                    if (item.options.length !== 0) {

                                        $scope.names = angular.copy(item.options);
                                        angular.forEach(item.options, function (item) {
                                            $scope.names.push({
                                                id: item.value,
                                                text: item.label

                                            });
                                        });
                                    }
                                    query.callback({
                                        results: $scope.names
                                    });
//                                            


                                }};
                        }
                    });
                }
                if (rule.field.type === 'UserMultiSelect')
                {
                    angular.forEach($scope.ruleList, function (item) {
                        var validationPattern = item.field.validationPattern;
                        var isEmployee = false;
                        var isDepartment = false;
                        var isDesignation = false;
                        var validationsArr = validationPattern.replace("{", "")
                                .replace("}", "")
                                .split(",");
                        angular.forEach(validationsArr, function (valArr)
                        {
                            if (valArr.indexOf("\"isEmployee\":") > -1)
                            {
                                isEmployee = true;
                            }
                            if (valArr.indexOf("\"isDepartment\":") > -1)
                            {
                                isDepartment = true;
                            }
                            if (valArr.indexOf("\"isDesignation\":") > -1)
                            {
                                isDesignation = true;
                            }
                        });
                        // This method is for edit and single select i.e when the operator label is  "has any value from"
                        item.userMultiselectComponent = {
                            multiple: true,
                            closeOnSelect: false,
                            placeholder: 'Select',
                            initSelection: function (element, callback) {
                                var data = [];
                                var tempdatamultiple = angular.copy(item.values);
                                var array = item.values.toString().split(',');
                                var newArray = [];
                                if (array !== undefined) {
                                    angular.forEach(array, function (arr)
                                    {
                                        arr = arr.replace("\"", "").trim();
                                        newArray.push(arr);
                                    });
                                    CustomFieldService.defaultSelection(newArray, function (res) {
                                        var data = [];
                                        $scope.result = JSON.parse(angular.toJson(res));
                                        for (key in  $scope.result)
                                        {
                                            data.push({id: key, text: res[key]});
                                        }
                                        callback(data);
                                        item.values = angular.copy(tempdatamultiple);
                                    }, function () {
                                    });
                                }

                            },
                            formatResult: function (item) {
                                return item.text;
                            },
                            formatSelection: function (item) {
                                return item.text;
                            },
                            query: function (query) {
                                var selected = query.term;
                                $scope.names = [];
                                var success = function (data) {
                                    if (data.length !== 0) {
                                        $scope.names = data;
                                        angular.forEach(data, function (item) {
                                            $scope.names.push({
                                                id: item.value + ":" + item.description,
                                                text: item.label

                                            });
                                        });
                                    }
                                    query.callback({
                                        results: $scope.names
                                    });
                                };
                                var failure = function () {
                                };
                                if (isEmployee) {
                                    var search;
                                    var searchEmp = false;
                                    if (isDepartment === false && isDesignation === false)
                                    {
                                        if (selected.substring(0, 2) === '@E' || selected.substring(0, 2) === '@e')
                                        {
                                            searchEmp = true;
                                            search = query.term.slice(2);
                                        } else {
                                            searchEmp = true;
                                            search = query.term;
                                        }
                                    } else
                                    {
                                        if (selected.substring(0, 2) === '@E' || selected.substring(0, 2) === '@e')
                                        {
                                            searchEmp = true;
                                            search = query.term.slice(2);
                                        }
                                    }
                                    if (searchEmp === true) {

                                        if ($rootScope.session.isHKAdmin) {
                                            var payload = {"user": search.trim(), franchise: $scope.activityflow.franchise.value};
                                            Messaging.retrieveUserListFranchise(payload, success, failure);
                                        } else {
                                            AssetService.retrieveUserList(search.trim(), success, failure);
                                        }
                                    }
                                }
                                if (isDepartment) {
                                    var search;
                                    var searchDep = false;
                                    if (isEmployee === false && isDesignation === false) {
                                        if (selected.substring(0, 2) === '@D' || selected.substring(0, 2) === '@d') {
                                            searchDep = true;
                                            search = query.term.slice(2);
                                        } else {
                                            searchDep = true;
                                            search = query.term;
                                        }
                                    } else {
                                        if (selected.substring(0, 2) === '@D' || selected.substring(0, 2) === '@d') {
                                            searchDep = true;
                                            search = query.term.slice(2);
                                        }
                                    }
                                    if (searchDep === true) {

                                        if ($rootScope.session.isHKAdmin) {
                                            var payload = {"department": search.trim(), franchise: $scope.activityflow.franchise.value};
                                            Messaging.retrieveDepartmentListFranchise(payload, success, failure);
                                        } else {
                                            Messaging.retrieveDepartmentList(search.trim(), success, failure);
                                        }
                                    }
                                }
                                if (isDesignation) {
                                    var search;
                                    var searchRole = false;
                                    if (isEmployee === false && isDepartment === false)
                                    {
                                        if (selected.substring(0, 2) == '@R' || selected.substring(0, 2) == '@r') {
                                            searchRole = true;
                                            search = query.term.slice(2);
                                        } else {
                                            searchRole = true;
                                            search = query.term;
                                        }
                                    } else
                                    {
                                        if (selected.substring(0, 2) == '@R' || selected.substring(0, 2) == '@r') {
                                            searchRole = true;
                                            search = query.term.slice(2);
                                        }
                                    }
                                    if (searchRole === true) {

                                        if ($rootScope.session.isHKAdmin) {
                                            var payload = {"role": search.trim(), franchise: $scope.activityflow.franchise.value};
                                            Messaging.retrieveRoleListFranchise(payload, success, failure);
                                        } else {
                                            Messaging.retrieveRoleList(search.trim(), success, failure);
                                        }
                                    }
                                }
//                                else {
//                                    query.callback({
//                                        results: $scope.names
//                                    });
//                                }
                            }
                        };
                        item.values = item.values.toString();

                        // This method is for edit and single select i.e when the operator label is not "has any value from"

                        item.userSingleselectComponent = {
                            multiple: true,
                            closeOnSelect: false,
                            placeholder: 'Select',
                            maximumSelectionSize: 1,
                            initSelection: function (element, callback) {
                                var data = [];
                                var tempdata = angular.copy(item.values);
                                var array = item.values.toString().split(',');
                                var newArray = [];
                                if (array !== undefined) {
                                    angular.forEach(array, function (arr)
                                    {
                                        arr = arr.replace("\"", "").trim();
                                        newArray.push(arr);
                                    });
                                    CustomFieldService.defaultSelection(newArray, function (res) {
                                        var data = [];
                                        $scope.result = JSON.parse(angular.toJson(res));
                                        for (key in  $scope.result)
                                        {
                                            data.push({id: key, text: res[key]});
                                        }
                                        callback(data);
                                        item.values = angular.copy(tempdata);
                                    }, function () {
                                    });
                                }

                            },
                            formatResult: function (item) {
                                return item.text;
                            },
                            formatSelection: function (item) {
                                return item.text;
                            },
                            query: function (query) {
                                var selected = query.term;
                                $scope.names = [];
                                var success = function (data) {
                                    if (data.length !== 0) {
                                        $scope.names = data;
                                        angular.forEach(data, function (item) {
                                            $scope.names.push({
                                                id: item.value + ":" + item.description,
                                                text: item.label

                                            });
                                        });
                                    }
                                    query.callback({
                                        results: $scope.names
                                    });
                                };
                                var failure = function () {
                                };
                                if (isEmployee) {
                                    var search;

                                    var searchEmp = false;
                                    if (isDepartment === false && isDesignation === false)
                                    {

                                        if (selected.substring(0, 2) === '@E' || selected.substring(0, 2) === '@e')
                                        {
                                            searchEmp = true;
                                            search = query.term.slice(2);
                                        } else {
                                            searchEmp = true;
                                            search = query.term;
                                        }
                                    } else
                                    {
                                        if (selected.substring(0, 2) === '@E' || selected.substring(0, 2) === '@e')
                                        {
                                            searchEmp = true;
                                            search = query.term.slice(2);
                                        }
                                    }
                                    if (searchEmp === true) {
                                        if ($rootScope.session.isHKAdmin) {
                                            var payload = {"user": search.trim(), franchise: $scope.activityflow.franchise.value};
                                            Messaging.retrieveUserListFranchise(payload, success, failure);
                                        } else {
                                            AssetService.retrieveUserList(search.trim(), success, failure);
                                        }
                                    }
                                }

                                if (isDepartment) {
                                    var searchDep = false;
                                    var search;

                                    if (isEmployee === false && isDesignation === false) {
                                        if (selected.substring(0, 2) === '@D' || selected.substring(0, 2) === '@d') {
                                            searchDep = true;
                                            search = query.term.slice(2);
                                        } else {
                                            searchDep = true;
                                            search = query.term;
                                        }
                                    } else {
                                        if (selected.substring(0, 2) === '@D' || selected.substring(0, 2) === '@d') {
                                            searchDep = true;
                                            search = query.term.slice(2);
                                        }
                                    }
                                    if (searchDep === true) {
                                        if ($rootScope.session.isHKAdmin) {
                                            var payload = {"department": search.trim(), franchise: $scope.activityflow.franchise.value};
                                            Messaging.retrieveDepartmentListFranchise(payload, success, failure);
                                        } else {
                                            Messaging.retrieveDepartmentList(search.trim(), success, failure);
                                        }
                                    }
                                }

                                if (isDesignation) {
                                    var searchRole = false;
                                    var search;
                                    if (isEmployee === false && isDepartment === false)
                                    {
                                        if (selected.substring(0, 2) == '@R' || selected.substring(0, 2) == '@r') {
                                            searchRole = true;
                                            search = query.term.slice(2);
                                        } else {
                                            searchRole = true;
                                            search = query.term;
                                        }
                                    } else
                                    {
                                        if (selected.substring(0, 2) == '@R' || selected.substring(0, 2) == '@r') {
                                            searchRole = true;
                                            search = query.term.slice(2);
                                        }
                                    }
                                    if (searchRole === true) {
                                        if ($rootScope.session.isHKAdmin) {
                                            var payload = {"role": search.trim(), franchise: $scope.activityflow.franchise.value};
                                            Messaging.retrieveRoleListFranchise(payload, success, failure);
                                        } else {
                                            Messaging.retrieveRoleList(search.trim(), success, failure);
                                        }
                                    }
                                } else if (selected.substring(0, 2) === '@A' || selected.substring(0, 2) === '@a') {
                                    var search = query.term.slice(2);
                                    Messaging.retrieveActivityList(search.trim(), success, failure);
                                } else {
                                    query.callback({
                                        results: $scope.names
                                    });
                                }
                            }
                        };


                    });
                }



//                $log.info("end");

            }

            $scope.onEntityChange = function (index) {
                if ($scope.ruleList[index].entity !== null) {
                    $scope.ruleList[index].field = {};
                    $scope.ruleList[index].operatorList = [];
                    var id = $scope.ruleList[index].entity;
                    $scope.ruleList[index].showtemplate = false;
                    $scope.ruleList[index].values = [];
                    $scope.ruleList[index].rowsubmitted = false;
                    if (parseInt(id) > 0) {
                        RuleService.retrieveFieldsByEntity("{\"primaryKey\": " + id + "}", function (res) {
                            $scope.ruleList[index].fieldList = res;
                        });
                    } else {
                        if (parseInt(id) >= -3 && parseInt(id) <= -1) {
                            $scope.ruleList[index].fieldList = $scope.fieldListForAct;
                        } else {
                            $scope.ruleList[index].fieldList = $scope.fieldListForSer;
                        }
//                        $scope.ruleList[index].fieldList = $scope.fieldListForSer;
                    }
                }
            }

            $scope.onFieldChange = function (index) {
                if ($scope.ruleList[index].field !== null) {
//                    $scope.ruleList[index].operator = {};
                    var componentType = $scope.ruleList[index].field.type;

                    $scope.ruleList[index].operatorList = [];
                    var pointerId;
                    if (componentType === 'Pointer') {
                        var pointerComponentType;
                        var validationPattern = $scope.ruleList[index].field.validationPattern.replace("{", "")
                                .replace("}", "");
                        var validationsArr = [];
                        var pointerArray = [];

                        validationsArr = validationPattern.split(",");
                        angular.forEach(validationsArr, function (validate)
                        {
                            if (validate.indexOf("\"pointer\":") > -1) {
                                pointerArray = validate.split(":");
                                pointerId = pointerArray[1].replace(/["']/g, "");
                                $scope.ruleList[index].field.pointerId = pointerId;
                            }
                        });
                        angular.forEach($scope.ruleList[index].fieldList, function (fieldList)
                        {
                            if (fieldList.id.toString() === pointerId.toString())
                            {
                                pointerComponentType = fieldList.type;
                                $scope.ruleList[index].field.pointertype = pointerComponentType;

                            }
                        });
                        $scope.ruleList[index].operatorList = $scope.operators[operatorMap[pointerComponentType]];

                    }
                    else
                    if (componentType === 'SubEntity')
                    {

                        $scope.ruleList[index].field.subEntityType = "Dropdown";

                        $scope.ruleList[index].operatorList = $scope.operators[operatorMap["Dropdown"]];
                        RuleService.retrieveDropListForSubEntity($scope.ruleList[index].field.id, function (res) {
                            $scope.ruleList[index].options = res;
                            // I have taken index 0 because everyindex will have same component type
                            $scope.ruleList[index].field.subentityDropListType = res[0].description;
                        });


                    }

                    else {
                        $scope.ruleList[index].operatorList = $scope.operators[operatorMap[$scope.ruleList[index].field.type]];
                    }
                    if (componentType === "Dropdown" || componentType === "Radio button" || componentType === "MultiSelect") {

                        if (parseInt($scope.ruleList[index].entity) <= -1 && parseInt($scope.ruleList[index].entity) >= -3 && parseInt($scope.ruleList[index].field.id) !== -1) {
                            RuleService.retrieveActivityMasterByFieldById("{\"primaryKey\": " + $scope.activityflow.version.value + "}", function (res) {
                                $scope.ruleList[index].options = res;
                            });
                        } else if (parseInt($scope.ruleList[index].entity) <= -4 && parseInt($scope.ruleList[index].entity) >= -6 && parseInt($scope.ruleList[index].field.id) !== -1) {
                            RuleService.retrieveServiceMasterByFieldById("{\"primaryKey\": " + $scope.activityflow.version.value + "}", function (res) {
                                $scope.ruleList[index].options = res;
                            });
                        } else {
                            // For Lot status the dropdown should not be populated from here
                            if ($scope.ruleList[index].field.dbFieldName === 'lot_status' || $scope.ruleList[index].field.dbFieldName === 'packet_status'
                                    || $scope.ruleList[index].field.dbFieldName === 'status_of_plan' || $scope.ruleList[index].field.dbFieldName === 'parcel_status' || $scope.ruleList[index].field.dbFieldName === 'invoice_status'
                                    || $scope.ruleList[index].field.dbFieldName === 'issue_status' || $scope.ruleList[index].field.dbFieldName === 'type_of_plan')
                            {
                                var dbFieldName = $scope.ruleList[index].field.dbFieldName;

                                RuleService.retrieveLotStatus(dbFieldName, function (res) {
                                    $scope.ruleList[index].options = res;
                                });

                            }
                            else
                            {
                                RuleService.retrieveMasterByFieldById("{\"primaryKey\": " + $scope.ruleList[index].field.id + "}", function (res) {
                                    $scope.ruleList[index].options = res;

                                });
                            }
                        }
                    } else if (componentType === "Checkbox") {
                        $scope.ruleList[index].options = [{label: 'True', value: true}, {label: 'False', value: false}];
                    }
                    else if (componentType === "Pointer")
                    {
                        RuleService.retrieveMasterByFieldById("{\"primaryKey\": " + pointerId + "}", function (res) {
                            $scope.ruleList[index].options = res;
                        });
                    }

                    $scope.ruleList[index].showtemplate = false;
                    $scope.ruleList[index].values = [];
                    $scope.ruleList[index].rowsubmitted = false;
                }
            }
            $scope.onOperatorChange = function (index) {
                $scope.ruleList[index].showtemplate = false;
                $scope.ruleList[index].rowsubmitted = false;
                $scope.ruleList[index].values = [];
                var operator = $scope.ruleList[index].operator;
                if (!!operator) {
                    if (operator.label === "contains" || operator.label === "has no value" || operator.label === "has any value") {
                        $scope.ruleList[index].hint = "Enter comma separated list";
                    }

                    $scope.ruleList[index].showtemplate = true;
                    angular.forEach($scope.ruleList, function (item) {
                        var validationPattern = item.field.validationPattern;
                        var isEmployee = false;
                        var isDepartment = false;
                        var isDesignation = false;
                        var validationsArr = validationPattern.replace("{", "")
                                .replace("}", "")
                                .split(",");
                        angular.forEach(validationsArr, function (valArr)
                        {
                            if (valArr.indexOf("\"isEmployee\":") > -1)
                            {
                                isEmployee = true;
                            }
                            if (valArr.indexOf("\"isDepartment\":") > -1)
                            {
                                isDepartment = true;
                            }
                            if (valArr.indexOf("\"isDesignation\":") > -1)
                            {
                                isDesignation = true;
                            }
                        })
// For multiselect component Multiple i.e. IN query
                        item.multiselectComponent = {
                            multiple: true,
                            closeOnSelect: false,
                            placeholder: 'Select',
                            initSelection: function (element, callback) {

                            },
                            formatResult: function (item) {
                                return item.text;
                            },
                            formatSelection: function (item) {
                                return item.text;
                            },
                            query: function (query) {
                                var selected = query.term;
                                $scope.names = [];
//                                           
                                if (item.options.length !== 0) {

                                    $scope.names = angular.copy(item.options);
                                    angular.forEach(item.options, function (item) {
                                        $scope.names.push({
                                            id: item.value,
                                            text: item.label

                                        });
                                    });
                                }
                                query.callback({
                                    results: $scope.names
                                });
//                                            


                            }
                        };
                        // For multiselect component Single i.e. EQUALS OR NOT EQUAL query
                        item.multiselectSingleComponent = {
                            multiple: true,
                            closeOnSelect: false,
                            placeholder: 'Select',
                            maximumSelectionSize: 1,
                            initSelection: function (element, callback) {

                            },
                            formatResult: function (item) {
                                return item.text;
                            },
                            formatSelection: function (item) {
                                return item.text;
                            },
                            query: function (query) {
                                var selected = query.term;
                                $scope.names = [];
//                                           
                                if (item.options.length !== 0) {

                                    $scope.names = angular.copy(item.options);
                                    angular.forEach(item.options, function (item) {
                                        $scope.names.push({
                                            id: item.value,
                                            text: item.label

                                        });
                                    });
                                }
                                query.callback({
                                    results: $scope.names
                                });
//                                            


                            }
                        };
                        // For dropdown component Multiple i.e. IN query
                        item.dropdownMultiSelectComponent = {
                            multiple: true,
                            closeOnSelect: false,
                            placeholder: 'Select',
                            initSelection: function (element, callback) {

                            },
                            formatResult: function (item) {
                                return item.text;
                            },
                            formatSelection: function (item) {
                                return item.text;
                            },
                            query: function (query) {
                                var selected = query.term;
                                $scope.names = [];
//                                           
                                if (item.options.length !== 0) {

                                    $scope.names = angular.copy(item.options);
                                    angular.forEach(item.options, function (item) {
                                        $scope.names.push({
                                            id: item.value,
                                            text: item.label

                                        });
                                    });
                                }
                                query.callback({
                                    results: $scope.names
                                });
                            }
                        };

                        // For dropdown component SINGLE i.e. EQUAL query
                        item.dropdownSingleSelectComponent = {
                            multiple: true,
                            closeOnSelect: false,
                            placeholder: 'Select',
                            maximumSelectionSize: 1,
                            initSelection: function (element, callback) {

                            },
                            formatResult: function (item) {
                                return item.text;
                            },
                            formatSelection: function (item) {
                                return item.text;
                            },
                            query: function (query) {
                                var selected = query.term;
                                $scope.names = [];
//                                           
                                if (item.options.length !== 0) {

                                    $scope.names = angular.copy(item.options);
                                    angular.forEach(item.options, function (item) {
                                        $scope.names.push({
                                            id: item.value,
                                            text: item.label

                                        });
                                    });
                                }
                                query.callback({
                                    results: $scope.names
                                });
                            }
                        };
                        // For SubEntity component Multiple i.e. IN query
                        item.subEntityMultiSelectComponent = {
                            multiple: true,
                            closeOnSelect: false,
                            placeholder: 'Select',
                            initSelection: function (element, callback) {

                            },
                            formatResult: function (item) {
                                return item.text;
                            },
                            formatSelection: function (item) {
                                return item.text;
                            },
                            query: function (query) {
                                var selected = query.term;
                                $scope.names = [];
//                                           
                                if (item.options.length !== 0) {

                                    $scope.names = angular.copy(item.options);
                                    angular.forEach(item.options, function (item) {
                                        $scope.names.push({
                                            id: item.value,
                                            text: item.label

                                        });
                                    });
                                }
                                query.callback({
                                    results: $scope.names
                                });
                            }
                        };

                        // For SubENtity component SINGLE i.e. EQUAL query
                        item.subEntitySingleSelectComponent = {
                            multiple: true,
                            closeOnSelect: false,
                            placeholder: 'Select',
                            maximumSelectionSize: 1,
                            initSelection: function (element, callback) {

                            },
                            formatResult: function (item) {
                                return item.text;
                            },
                            formatSelection: function (item) {
                                return item.text;
                            },
                            query: function (query) {
                                var selected = query.term;
                                $scope.names = [];
//                                           
                                if (item.options.length !== 0) {

                                    $scope.names = angular.copy(item.options);
                                    angular.forEach(item.options, function (item) {
                                        $scope.names.push({
                                            id: item.value,
                                            text: item.label

                                        });
                                    });
                                }
                                query.callback({
                                    results: $scope.names
                                });
                            }
                        };

                        // For dropdown pointer component Multiple i.e. IN query
                        item.pointerMultiSelectComponent = {
                            multiple: true,
                            closeOnSelect: false,
                            placeholder: 'Select',
                            initSelection: function (element, callback) {

                            },
                            formatResult: function (item) {
                                return item.text;
                            },
                            formatSelection: function (item) {
                                return item.text;
                            },
                            query: function (query) {
                                var selected = query.term;
                                $scope.names = [];
//                                           
                                if (item.options.length !== 0) {

                                    $scope.names = angular.copy(item.options);
                                    angular.forEach(item.options, function (item) {
                                        $scope.names.push({
                                            id: item.value,
                                            text: item.label

                                        });
                                    });
                                }
                                query.callback({
                                    results: $scope.names
                                });
                            }
                        };

                        // For pointer dropdown component SINGLE i.e. EQUAL query
                        item.pointerSingleSelectComponent = {
                            multiple: true,
                            closeOnSelect: false,
                            placeholder: 'Select',
                            maximumSelectionSize: 1,
                            initSelection: function (element, callback) {

                            },
                            formatResult: function (item) {
                                return item.text;
                            },
                            formatSelection: function (item) {
                                return item.text;
                            },
                            query: function (query) {
                                var selected = query.term;
                                $scope.names = [];
//                                           
                                if (item.options.length !== 0) {

                                    $scope.names = angular.copy(item.options);
                                    angular.forEach(item.options, function (item) {
                                        $scope.names.push({
                                            id: item.value,
                                            text: item.label

                                        });
                                    });
                                }
                                query.callback({
                                    results: $scope.names
                                });
                            }
                        };

                        // For USerMultiSelect on operator change for multiple select 
                        item.userMultiselectComponent = {
                            multiple: true,
                            closeOnSelect: false,
                            placeholder: 'Select',
                            initSelection: function (element, callback) {

                            },
                            formatResult: function (item) {
                                return item.text;
                            },
                            formatSelection: function (item) {
                                return item.text;
                            },
                            query: function (query) {
                                var selected = query.term;
                                $scope.names = [];
                                var success = function (data) {
                                    if (data.length !== 0) {
                                        $scope.names = data;
                                        angular.forEach(data, function (item) {
                                            $scope.names.push({
                                                id: item.value + ":" + item.description,
                                                text: item.label

                                            });
                                        });
                                    }
                                    query.callback({
                                        results: $scope.names
                                    });
                                };
                                var failure = function () {
                                };
                                if (isEmployee) {
                                    var searchEmp = false;
                                    var search;
                                    if (isDepartment === false && isDesignation === false) {
                                        if (selected.substring(0, 2) === '@E' || selected.substring(0, 2) === '@e')
                                        {
                                            searchEmp = true;
                                            search = query.term.slice(2);

                                        } else {
                                            searchEmp = true;
                                            search = query.term;
                                        }
                                    } else
                                    {
                                        if (selected.substring(0, 2) === '@E' || selected.substring(0, 2) === '@e')
                                        {
                                            searchEmp = true;
                                            search = query.term.slice(2);

                                        }
                                    }
                                    if (searchEmp === true) {

                                        if ($rootScope.session.isHKAdmin) {
                                            var payload = {"user": search.trim(), franchise: $scope.activityflow.franchise.value};
                                            Messaging.retrieveUserListFranchise(payload, success, failure);
                                        } else {
                                            AssetService.retrieveUserList(search.trim(), success, failure);
                                        }
                                    }
                                }

                                if (isDepartment) {
                                    var searchDep = false;
                                    var search;
                                    if (isEmployee === false && isDesignation === false)
                                    {
                                        if (selected.substring(0, 2) === '@D' || selected.substring(0, 2) === '@d')
                                        {
                                            searchDep = true;
                                            search = query.term.slice(2);
                                        } else {
                                            searchDep = true;
                                            search = query.term;
                                        }
                                    } else
                                    {
                                        if (selected.substring(0, 2) === '@D' || selected.substring(0, 2) === '@d')
                                        {
                                            searchDep = true;
                                            search = query.term.slice(2);
                                        }

                                    }
                                    if (searchDep === true) {

                                        if ($rootScope.session.isHKAdmin) {
                                            var payload = {"department": search.trim(), franchise: $scope.activityflow.franchise.value};
                                            Messaging.retrieveDepartmentListFranchise(payload, success, failure);
                                        } else {
                                            Messaging.retrieveDepartmentList(search.trim(), success, failure);
                                        }
                                    }
                                }
                                if (isDesignation) {
                                    var searchRole = false;
                                    var search;
                                    if (isEmployee === false && isDepartment === false) {

                                        if (selected.substring(0, 2) == '@R' || selected.substring(0, 2) == '@r')
                                        {
                                            searchRole = true;
                                            search = query.term.slice(2);

                                        } else {
                                            searchRole = true;
                                            search = query.term;
                                        }
                                    } else
                                    {
                                        if (selected.substring(0, 2) == '@R' || selected.substring(0, 2) == '@r')
                                        {
                                            searchRole = true;
                                            search = query.term.slice(2);

                                        }
                                    }
                                    if (searchRole === true) {
                                        if ($rootScope.session.isHKAdmin) {
                                            var payload = {"role": search.trim(), franchise: $scope.activityflow.franchise.value};
                                            Messaging.retrieveRoleListFranchise(payload, success, failure);
                                        } else {
                                            Messaging.retrieveRoleList(search.trim(), success, failure);
                                        }
                                    }

                                } else {
                                    query.callback({
                                        results: $scope.names
                                    });
                                }
                            }};
                        // For USerMultiSelect on operator change for Single select 

                        item.userSingleselectComponent = {
                            multiple: true,
                            closeOnSelect: false,
                            placeholder: 'Select',
                            maximumSelectionSize: 1,
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
                                $scope.names = [];
                                var success = function (data) {
                                    if (data.length !== 0) {
                                        $scope.names = data;
                                        angular.forEach(data, function (item) {
                                            $scope.names.push({
                                                id: item.value + ":" + item.description,
                                                text: item.label

                                            });
                                        });
                                    }
                                    query.callback({
                                        results: $scope.names
                                    });
                                };
                                var failure = function () {
                                };

                                if (isEmployee) {
                                    var search;
                                    var searchEmp = false;
                                    if (isDepartment === false && isDesignation === false) {
                                        if (selected.substring(0, 2) === '@E' || selected.substring(0, 2) === '@e')
                                        {
                                            searchEmp = true;
                                            search = query.term.slice(2);
                                        } else {
                                            searchEmp = true;
                                            search = query.term;
                                        }
                                    } else
                                    {
                                        if (selected.substring(0, 2) === '@E' || selected.substring(0, 2) === '@e')
                                        {
                                            searchEmp = true;
                                            search = query.term.slice(2);
                                        }
                                    }
                                    if (searchEmp === true) {

                                        if ($rootScope.session.isHKAdmin) {
                                            var payload = {"user": search.trim(), franchise: $scope.activityflow.franchise.value};
                                            Messaging.retrieveUserListFranchise(payload, success, failure);
                                        } else {
                                            AssetService.retrieveUserList(search.trim(), success, failure);
                                        }
                                    }
                                }

                                if (isDepartment) {
                                    var searchDep = false;
                                    var search;
                                    if (isEmployee === false && isDesignation === false)
                                    {
                                        if (selected.substring(0, 2) === '@D' || selected.substring(0, 2) === '@d')
                                        {
                                            searchDep = true;
                                            search = query.term.slice(2);

                                        } else {
                                            searchDep = true;
                                            search = query.term;
                                        }
                                    } else
                                    {
                                        if (selected.substring(0, 2) === '@D' || selected.substring(0, 2) === '@d')
                                        {
                                            searchDep = true;
                                            search = query.term.slice(2);

                                        }

                                    }
                                    if (searchDep === true) {
                                        if ($rootScope.session.isHKAdmin) {
                                            var payload = {"department": search.trim(), franchise: $scope.activityflow.franchise.value};
                                            Messaging.retrieveDepartmentListFranchise(payload, success, failure);
                                        } else {
                                            Messaging.retrieveDepartmentList(search.trim(), success, failure);
                                        }
                                    }
                                }
                                if (isDesignation) {
                                    var searchRole = false;
                                    var search;
                                    if (isEmployee === false && isDepartment === false) {
                                        if (selected.substring(0, 2) == '@R' || selected.substring(0, 2) == '@r')
                                        {
                                            searchRole = true;
                                            search = query.term.slice(2);
                                        } else {
                                            searchRole = true;
                                            search = query.term;
                                        }
                                    } else
                                    {
                                        if (selected.substring(0, 2) == '@R' || selected.substring(0, 2) == '@r')
                                        {
                                            searchRole = true;
                                            search = query.term.slice(2);
                                        }
                                    }
                                    if (searchRole === true) {

                                        if ($rootScope.session.isHKAdmin) {
                                            var payload = {"role": search.trim(), franchise: $scope.activityflow.franchise.value};
                                            Messaging.retrieveRoleListFranchise(payload, success, failure);
                                        } else {
                                            Messaging.retrieveRoleList(search.trim(), success, failure);
                                        }
                                    }
                                }
                                else {
//                                    query.callback({
//                                        results: $scope.names
//                                    });
                                }
                            }};

                    });
                    $scope.ruleList[index].showtemplate = true;
                    //
                } else {
                    $scope.ruleList[index].showtemplate = false;
                }

//                 $scope.rules.showtemplate=true;

            }
            $scope.isRulesEmpty = function () {
                if (!!$scope.allRules) {
                    return $scope.allRules.length <= 0;
                } else {
                    return true;
                }
            }
            $scope.onOperatorClick = function (index) {
                $scope.ruleList[index].showtemplate = false;
                $scope.ruleList[index].rowsubmitted = false;
            }
            $scope.addRule = function (form, index) {
                console.log(form.$valid);
                $scope.ruleList[index].rowsubmitted = true;
                console.log(form.$valid);
                if (form.$valid) {
                    var flag = true;
                    var length = $scope.ruleList[index].operator.shortcutCode;
                    for (var i = 0; i < length; i++) {
                        var val = $scope.ruleList[index].values[i];

                        if ($scope.ruleList[index].operator.label !== 'has no value' && $scope.ruleList[index].operator.label !== 'has any value')
                        {
                            if ($scope.ruleList[index].field.type === "MultiSelect")
                            {
                                var valueOfMultiSelect = $scope.ruleList[index].values;
                                flag = (flag && true);
                            } else {
                                if (val !== null && val !== undefined) {
                                    flag = (flag && true);
                                } else {
                                    flag = false;
                                }
                            }
                        } else
                        {
                            flag = (flag && true);
                        }
                    }
                    if (flag) {
                        var rule = initializeRule();
                        $scope.ruleList.push(rule);
                    }
                }
            }
            $scope.remove = function (index) {
                $scope.ruleList.splice(index, 1);
                if ($scope.ruleList.length === 0) {
                    var rule = initializeRule();
                    $scope.ruleList.push(rule);
                    $scope.ruleList[index].rowsubmitted = false;
                }

            }

            $scope.save = function (form) {

                if (!!$scope.ruleList) {
                    for (var i = 0; i < $scope.ruleList.length; i++) {
                        $scope.ruleList[i].rowsubmitted = true;
                    }
                }
                $scope.ruleSubmitted = true;
                if (form.$valid) {
                    var flag = true;
                    for (var index = 0; index < $scope.ruleList.length; index++) {
                        var length2 = $scope.ruleList[index].operator.shortcutCode;
                        for (var i = 0; i < length2; i++) {
                            var val = $scope.ruleList[index].values[i];

                            if ($scope.ruleList[index].operator.label !== 'has no value' && $scope.ruleList[index].operator.label !== 'has any value')
                            {
                                if ($scope.ruleList[index].field.type === "MultiSelect")
                                {
                                    flag = (flag && true);
                                } else {
                                    if (val !== null && val !== undefined) {
                                        flag = (flag && true);
                                    } else {
                                        flag = false;
                                        break;
                                    }
                                }
                            } else
                            {
                                flag = (flag && true);
                            }
                        }
                        if (flag === false)
                            break;
                    }
                    if (flag) {
                        var rules = [];
                        var length = $scope.ruleList.length;
                        for (var i = 0; i < length; i++) {
                            $scope.ruleList[i].rowsubmitted = true;
                            var tmprule = {};
                            tmprule = angular.copy($scope.ruleList[i]);
                            var rule = {};
                            rule.id = i;
                            //                    rule.priority = tmprule.priority;
                            rule.entity = tmprule.entity;
                            rule.field = tmprule.field.id;
                            rule.operator = tmprule.operator.label;
                            rule.fieldType = tmprule.field.type;

                            // For non pointer components
                            if (rule.fieldType !== "Pointer" && rule.fieldType !== "SubEntity") {
                                if (rule.fieldType !== "Date range" && rule.fieldType !== "UserMultiSelect" && rule.fieldType !== "MultiSelect" && rule.fieldType !== "Dropdown") {
                                    rule.value = tmprule.values[0];
                                    if (tmprule.values.length > 1) {
                                        rule.value1 = tmprule.values[1];
                                    }
                                } else {
                                    if (rule.fieldType !== "UserMultiSelect" && rule.fieldType !== "MultiSelect" && rule.fieldType !== "Dropdown") {
                                        rule.value = tmprule.values[0] + "," + tmprule.values[1];
                                    }
                                    else
                                    {
                                        if (tmprule.operator.label !== "has any value from")
                                        {
                                            if (tmprule.values.toString().indexOf(',') > -1)
                                            {
                                                tmprule.values.toString().replace(/,/g, '');
                                            }

                                        }
                                        rule.value = tmprule.values;
                                    }


                                    if (tmprule.values.length > 1 && rule.fieldType !== "UserMultiSelect" && rule.fieldType !== "MultiSelect" && rule.fieldType !== "Dropdown") {
                                        rule.value1 = tmprule.values[2] + "," + tmprule.values[3];
                                    }
                                }
                            }
                            else if (rule.fieldType === "SubEntity")
                            {
                                if (tmprule.field.subentityDropListType === "Date ") {
                                    rule.value = tmprule.values[0];
                                    if (tmprule.values.length > 1) {
                                        rule.value1 = tmprule.values[1];
                                    }
                                } else {


                                    if (tmprule.operator.label !== "has any value from")
                                    {
                                        if (tmprule.values.toString().indexOf(',') > -1)
                                        {
                                            tmprule.values.toString().replace(/,/g, '');
                                        }

                                    }
                                    rule.value = tmprule.values;
                                }
                                if (tmprule.values.length > 1 && tmprule.field.subentityDropListType === "Date ") {
                                    rule.value1 = tmprule.values[2] + "," + tmprule.values[3];
                                }

                            }
                            else
                            {
                                if (tmprule.field.pointertype !== "MultiSelect" && tmprule.field.pointertype !== "Dropdown") {
                                    rule.value = tmprule.values[0];
                                    if (tmprule.values.length > 1) {
                                        rule.value1 = tmprule.values[1];
                                    }
                                } else {
                                    if (tmprule.field.pointertype !== "MultiSelect" && tmprule.field.pointertype !== "Dropdown") {
                                        rule.value = tmprule.values[0] + "," + tmprule.values[1];
                                    }
                                    else
                                    {
                                        if (tmprule.operator.label !== "has any value from")
                                        {
                                            if (tmprule.values.toString().indexOf(',') > -1)
                                            {
                                                tmprule.values.toString().replace(/,/g, '');
                                            }

                                        }
                                        rule.value = tmprule.values;
                                    }


                                    if (tmprule.values.length > 1 && tmprule.field.pointertype !== "MultiSelect" && tmprule.field.pointertype !== "Dropdown") {
                                        rule.value1 = tmprule.values[2] + "," + tmprule.values[3];
                                    }

                                }
                            }

                            rules.push(rule);
                        }

                        if (form.$valid) {
                            $scope.ruleObject.criterias = angular.copy(rules);
                            $scope.ruleObject.isArchive = !$scope.ruleObject.isActive;
                            if (!!$scope.selectedRule) {
                                var index = parseInt($scope.selectedRule.id);
//                        $.grep($scope.allRules, function(e, i) {
//                            if (e.id === $scope.ruleObject.id) {
//                                index = i;
//                                return true;
//                            }
//                        });
                                $scope.allRules[index].ruleName = angular.copy($scope.ruleObject.ruleName);
                                $scope.allRules[index].remarks = angular.copy($scope.ruleObject.remarks);
                                $scope.allRules[index].apply = angular.copy($scope.ruleObject.apply);
                                $scope.allRules[index].criterias = angular.copy($scope.ruleObject.criterias);
                                $scope.allRules[index].isActive = angular.copy($scope.ruleObject.isActive);
                                $scope.allRules[index].isArchive = angular.copy($scope.ruleObject.isArchive);
                                if ($scope.isEditRule) {
                                    $scope.reset(form);
                                }
                            } else {
                                if ($scope.addFlag) {
                                    if ($scope.ruleObject.id === null) {

                                        $scope.lastRuleId++;
//                                console.log("$scope.lastRuleId::"+$scope.lastRuleId);
                                        if (!!$scope.allRules) {
                                            $scope.ruleObject.id = $scope.allRules.length;
                                        } else {
                                            $scope.ruleObject.id = 0;
                                        }
                                    }
                                    if (!!!$scope.allRules) {
                                        $scope.allRules = [];
                                    }
                                    $scope.allRules.push($scope.ruleObject);
                                    $scope.selectedRule = $scope.ruleObject;
                                }
//                        $scope.reset(form);
                            }

                            $scope.ruleSubmitted = false;
                            $scope.addFlag = false;
                            $scope.selectedRule = "";
                        }
                    }
                }
            }
            $scope.saveAndSubmit = function (form) {
                $scope.save(form);
//                }

                if (form.$valid || $scope.allRules.length <= 0) {
                    //                    $scope.save(form);
                    $log.info("in save and submit");                     // submit logic
                    var ruleset = {};
                    ruleset.rules = angular.copy($scope.allRules);

                    ruleset.franchise = $scope.activityflow.franchise.otherId;
                    if ($scope.allRules.length > 0) {
                        RuleService.createRuleSet(ruleset, function (res) {
                            $scope.rulesetid = null;
                            $scope.hideRule(form, false);
                        });
                        $scope.allRules = [];
                    }
                } else {
                    return;
                }
            }

            $scope.onRuleChange = function (form) {
                $rootScope.maskLoading();
                if (!!!$scope.selectedRule) {
                } else {
                    setRuleObject($scope.allRules[$scope.allRules.indexOf($scope.selectedRule)]);
                    $scope.editRule = true;
                    $scope.addFlag = false;
                }
                $rootScope.unMaskLoading();
            }
            $scope.deleteRule = function () {
                var index;
                if ($scope.selectedRule !== null) {
                    $.grep($scope.allRules, function (e, i) {
                        if (e.ruleName === $scope.selectedRule.ruleName) {
                            index = i;
                            return true;
                        }
                    });
                    $scope.allRules.splice(index, 1);
                    $scope.selectedRule = null;
                }
            }
            $scope.reset = function (form) {
                $scope.selectedRule = "";
                $scope.addFlag = true;
                initializeRuleObject();
                form.$setPristine();
                $scope.selectedRule = null;
                $scope.ruleSubmitted = false;
            }
            $scope.resetCancel = function (form) {
                $scope.selectedRule = "";
                $scope.addFlag = false;
                initializeRuleObject();
                form.$setPristine();
                $scope.ruleSubmitted = false;
                $scope.selectedRule = null;
            }
            $scope.loadPlans = function () {
                if (!!$scope.activityflow.nonModifierCodes) {
                    if ($scope.activityflow.nonModifierCodes.indexOf('SP') > -1) {
                        if (!!!$scope.activityFlow) {
                            $scope.activityFlow = {};
                        }
                        if (!!!$scope.activityFlow.plans || $scope.activityFlow.plans.length === 0) {
                            $scope.activityFlow.plans = [];
                            $scope.activityFlow.plans.push({});
                        }
                    } else {
                        if (!!!$scope.activityFlow) {
                            $scope.activityFlow = {};
                        }
                        $scope.activityFlow.plans = [];
                        $scope.activityFlow.plans.push({});
                    }
                }
            };
            $scope.loadPlansForEdit = function () {
                if (!!$rootScope.nonModifierCodes) {
                    if ($rootScope.nonModifierCodes.indexOf('SP') > -1) {
                        if (!!!$rootScope.plans) {
                            $rootScope.plans = [];
                            var item = {};
                            item.multiselectConfig = {multiple: true,
                                closeOnSelect: false,
                                placeholder: 'Select members',
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
                                    $scope.names = [];
                                    var success = function (data) {
                                        if (data.length !== 0) {
                                            $scope.names = data;
                                            angular.forEach(data, function (item) {
                                                $scope.names.push({
                                                    id: item.value + ":" + item.description,
                                                    text: item.label

                                                });
                                            });
                                        }
                                        query.callback({
                                            results: $scope.names
                                        });
                                    };
                                    var failure = function () {
                                    };
                                    if (selected.substring(0, 2) === '@E' || selected.substring(0, 2) === '@e') {
                                        var search = query.term.slice(2);
                                        if ($rootScope.session.isHKAdmin) {
                                            var payload = {"user": search.trim(), franchise: $scope.activityflow.franchise.value};
                                            Messaging.retrieveUserListFranchise(payload, success, failure);
                                        } else {
                                            AssetService.retrieveUserList(search.trim(), success, failure);
                                        }
                                    } else if (selected.substring(0, 2) === '@D' || selected.substring(0, 2) === '@d') {
                                        var search = query.term.slice(2);
                                        if ($rootScope.session.isHKAdmin) {
                                            var payload = {"department": search.trim(), franchise: $scope.activityflow.franchise.value};
                                            Messaging.retrieveDepartmentListFranchise(payload, success, failure);
                                        } else {
                                            Messaging.retrieveDepartmentList(search.trim(), success, failure);
                                        }
                                    } else if (selected.substring(0, 2) == '@R' || selected.substring(0, 2) == '@r') {
                                        var search = query.term.slice(2);
                                        if ($rootScope.session.isHKAdmin) {
                                            var payload = {"role": search.trim(), franchise: $scope.activityflow.franchise.value};
                                            Messaging.retrieveRoleListFranchise(payload, success, failure);
                                        } else {
                                            Messaging.retrieveRoleList(search.trim(), success, failure);
                                        }
                                    } else if (selected.substring(0, 2) === '@A' || selected.substring(0, 2) === '@a') {
                                        var search = query.term.slice(2);
                                        Messaging.retrieveActivityList(search.trim(), success, failure);
                                    } else if (selected.substring(0, 2) == '@G' || selected.substring(0, 2) == '@g') {
                                        var search = query.term.slice(2);
                                        Messaging.retrieveGroupList(search.trim(), success, failure);
                                    } else if (selected.length > 0) {
                                        var search = selected;
                                        if ($rootScope.session.isHKAdmin) {
                                            var payload = {"user": search.trim(), franchise: $scope.activityflow.franchise.value};
                                            Messaging.retrieveUserListFranchise(payload, success, failure);
                                        } else {
                                            AssetService.retrieveUserList(search.trim(), success, failure);
                                        }
                                    } else {
                                        query.callback({
                                            results: $scope.names
                                        });
                                    }
                                }};
                            item.statusMultiselectconfig = {
                                'allowClear': true,
                                'data': $scope.planStatusList,
                                'placeholder': "Select",
                                'multiple': true,
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
                                    query.callback({
                                        results: $scope.planStatusList
                                    });
                                }
                            };
                            $rootScope.plans.push(item);
                        }
                    } else {
                        $rootScope.plans = [];
                        var item = {};
                        item.multiselectConfig = {multiple: true,
                            closeOnSelect: false,
                            placeholder: 'Select members',
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
                                $scope.names = [];
                                var success = function (data) {
                                    if (data.length !== 0) {
                                        $scope.names = data;
                                        angular.forEach(data, function (item) {
                                            $scope.names.push({
                                                id: item.value + ":" + item.description,
                                                text: item.label

                                            });
                                        });
                                    }
                                    query.callback({
                                        results: $scope.names
                                    });
                                };
                                var failure = function () {
                                };
                                if (selected.substring(0, 2) === '@E' || selected.substring(0, 2) === '@e') {
                                    var search = query.term.slice(2);
                                    if ($rootScope.session.isHKAdmin) {
                                        var payload = {"user": search.trim(), franchise: $scope.activityflow.franchise.value};
                                        Messaging.retrieveUserListFranchise(payload, success, failure);
                                    } else {
                                        AssetService.retrieveUserList(search.trim(), success, failure);
                                    }
                                } else if (selected.substring(0, 2) === '@D' || selected.substring(0, 2) === '@d') {
                                    var search = query.term.slice(2);
                                    if ($rootScope.session.isHKAdmin) {
                                        var payload = {"department": search.trim(), franchise: $scope.activityflow.franchise.value};
                                        Messaging.retrieveDepartmentListFranchise(payload, success, failure);
                                    } else {
                                        Messaging.retrieveDepartmentList(search.trim(), success, failure);
                                    }
                                } else if (selected.substring(0, 2) == '@R' || selected.substring(0, 2) == '@r') {
                                    var search = query.term.slice(2);
                                    if ($rootScope.session.isHKAdmin) {
                                        var payload = {"role": search.trim(), franchise: $scope.activityflow.franchise.value};
                                        Messaging.retrieveRoleListFranchise(payload, success, failure);
                                    } else {
                                        Messaging.retrieveRoleList(search.trim(), success, failure);
                                    }
                                } else if (selected.substring(0, 2) === '@A' || selected.substring(0, 2) === '@a') {
                                    var search = query.term.slice(2);
                                    Messaging.retrieveActivityList(search.trim(), success, failure);
                                } else if (selected.substring(0, 2) == '@G' || selected.substring(0, 2) == '@g') {
                                    var search = query.term.slice(2);
                                    Messaging.retrieveGroupList(search.trim(), success, failure);
                                } else if (selected.length > 0) {
                                    var search = selected;
                                    if ($rootScope.session.isHKAdmin) {
                                        var payload = {"user": search.trim(), franchise: $scope.activityflow.franchise.value};
                                        Messaging.retrieveUserListFranchise(payload, success, failure);
                                    } else {
                                        AssetService.retrieveUserList(search.trim(), success, failure);
                                    }
                                } else {
                                    query.callback({
                                        results: $scope.names
                                    });
                                }
                            }};
                        item.statusMultiselectconfig = {
                            'allowClear': true,
                            'data': $scope.planStatusList,
                            'placeholder': "Select",
                            'multiple': true,
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
                                query.callback({
                                    results: $scope.planStatusList
                                });
                            }
                        };
                        $rootScope.plans.push(item);
                    }
                }
            };
            $scope.autoCompleteInvitees = {
                multiple: true,
                closeOnSelect: false,
                placeholder: 'Select members',
                initSelection: function (element, callback) {
                    var data = [];
                    angular.forEach($scope.userData, function (recipient) {
                        data.push({
                            id: recipient.recipientInstance + ":" + recipient.recipientType,
                            text: recipient.recipientValue
                        });
                    });
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
                    $scope.names = [];
                    var success = function (data) {
                        if (data.length !== 0) {
                            $scope.names = data;
                            angular.forEach(data, function (item) {
                                $scope.names.push({
                                    id: item.value + ":" + item.description,
                                    text: item.label

                                });
                            });
                        }
                        query.callback({
                            results: $scope.names
                        });
                    };
                    var failure = function () {
                    };
                    if (selected.substring(0, 2) === '@E' || selected.substring(0, 2) === '@e') {
                        var search = query.term.slice(2);
                        if ($rootScope.session.isHKAdmin) {
                            var payload = {"user": search.trim(), franchise: $scope.activityflow.franchise.value};
                            Messaging.retrieveUserListFranchise(payload, success, failure);
                        } else {
                            AssetService.retrieveUserList(search.trim(), success, failure);
                        }
                    } else if (selected.substring(0, 2) === '@D' || selected.substring(0, 2) === '@d') {
                        var search = query.term.slice(2);
                        if ($rootScope.session.isHKAdmin) {
                            var payload = {"department": search.trim(), franchise: $scope.activityflow.franchise.value};
                            Messaging.retrieveDepartmentListFranchise(payload, success, failure);
                        } else {
                            Messaging.retrieveDepartmentList(search.trim(), success, failure);
                        }
                    } else if (selected.substring(0, 2) == '@R' || selected.substring(0, 2) == '@r') {
                        var search = query.term.slice(2);
                        if ($rootScope.session.isHKAdmin) {
                            var payload = {"role": search.trim(), franchise: $scope.activityflow.franchise.value};
                            Messaging.retrieveRoleListFranchise(payload, success, failure);
                        } else {
                            Messaging.retrieveRoleList(search.trim(), success, failure);
                        }
                    } else if (selected.substring(0, 2) === '@A' || selected.substring(0, 2) === '@a') {
                        var search = query.term.slice(2);
                        Messaging.retrieveActivityList(search.trim(), success, failure);
                    } else if (selected.substring(0, 2) == '@G' || selected.substring(0, 2) == '@g') {
                        var search = query.term.slice(2);
                        Messaging.retrieveGroupList(search.trim(), success, failure);
                    } else if (selected.length > 0) {
                        var search = selected;
                        if ($rootScope.session.isHKAdmin) {
                            var payload = {"user": search.trim(), franchise: $scope.activityflow.franchise.value};
                            Messaging.retrieveUserListFranchise(payload, success, failure);
                        } else {
                            AssetService.retrieveUserList(search.trim(), success, failure);
                        }
                    } else {
                        query.callback({
                            results: $scope.names
                        });
                    }
                }
            };
            $scope.allowedType = {
                'allowClear': true,
                'data': $scope.planStatusList,
                'placeholder': "Select",
                'multiple': true,
                initSelection: function (element, callback) {
                    if ($scope.editCustomField) {
                        var tempdata = $scope.customFieldValidation.allowedTypes;
                        var data = [];
                        if ($scope.allowedTypeChars !== undefined)
                        {
                            var allowedTypeArray = $scope.allowedTypeChars.split(",");
                            angular.forEach(allowedTypeArray, function (allowedType)
                            {
                                data.push({id: allowedType, text: allowedType});
                            })
                        }
                        callback(data);
                        $scope.customFieldValidation.allowedTypes = tempdata;
                    }
                },
                formatResult: function (item) {
                    return item.text;
                },
                formatSelection: function (item) {
                    return item.text;
                },
                query: function (query) {
                    query.callback({
                        results: $scope.planStatusList
                    });
                }
            };
            $scope.retrievePlanStatus = function () {
                ActivityFlowService.retrievePlanStatus(function (res) {
                    $scope.planStatusList = [];
                    angular.forEach(JSON.parse(angular.toJson(res)), function (k, v) {
                        $scope.planStatusList.push({id: v, text: k});
                    });
                }, function () {
                });
            };
            $scope.addNewPlan = function (form, index) {
                $scope.activityFlow.plans[index].rowsubmitted = true;
                if (form.$valid) {
                    $scope.activityFlow.plans.push({});
                }
            }
            $scope.deletePlan = function (index) {
                if ($scope.activityFlow.plans.length === 1) {
                    $scope.activityFlow.plans = [];
                    $scope.activityFlow.plans.push({});
                } else {
                    $scope.activityFlow.plans.splice(index, 1);
                }
            };
            $scope.addInEditNewPlan = function (form, index) {
                $rootScope.plans[index].rowsubmitted = true;
                if (form.$valid) {
                    var obj = {};
                    obj["multiselectConfig"] = {
                        multiple: true,
                        closeOnSelect: false,
                        placeholder: 'Select members',
                        initSelection: function (element, callback) {
                            var data = [];
                        },
                        formatResult: function (item) {
                            return item.text;
                        },
                        formatSelection: function (item) {
                            return item.text;
                        },
                        query: function (query) {
                            var selected = query.term;
                            $scope.names = [];
                            var success = function (data) {
                                if (data.length !== 0) {
                                    $scope.names = data;
                                    angular.forEach(data, function (item) {
                                        $scope.names.push({
                                            id: item.value + ":" + item.description,
                                            text: item.label

                                        });
                                    });
                                }
                                query.callback({
                                    results: $scope.names
                                });
                            };
                            var failure = function () {
                            };
                            if (selected.substring(0, 2) === '@E' || selected.substring(0, 2) === '@e') {
                                var search = query.term.slice(2);
                                if ($rootScope.session.isHKAdmin) {
                                    var payload = {"user": search.trim(), franchise: $scope.activityflow.franchise.value};
                                    Messaging.retrieveUserListFranchise(payload, success, failure);
                                } else {
                                    AssetService.retrieveUserList(search.trim(), success, failure);
                                }
                            } else if (selected.substring(0, 2) === '@D' || selected.substring(0, 2) === '@d') {
                                var search = query.term.slice(2);
                                if ($rootScope.session.isHKAdmin) {
                                    var payload = {"department": search.trim(), franchise: $scope.activityflow.franchise.value};
                                    Messaging.retrieveDepartmentListFranchise(payload, success, failure);
                                } else {
                                    Messaging.retrieveDepartmentList(search.trim(), success, failure);
                                }
                            } else if (selected.substring(0, 2) == '@R' || selected.substring(0, 2) == '@r') {
                                var search = query.term.slice(2);
                                if ($rootScope.session.isHKAdmin) {
                                    var payload = {"role": search.trim(), franchise: $scope.activityflow.franchise.value};
                                    Messaging.retrieveRoleListFranchise(payload, success, failure);
                                } else {
                                    Messaging.retrieveRoleList(search.trim(), success, failure);
                                }
                            } else if (selected.substring(0, 2) === '@A' || selected.substring(0, 2) === '@a') {
                                var search = query.term.slice(2);
                                Messaging.retrieveActivityList(search.trim(), success, failure);
                            } else if (selected.substring(0, 2) == '@G' || selected.substring(0, 2) == '@g') {
                                var search = query.term.slice(2);
                                Messaging.retrieveGroupList(search.trim(), success, failure);
                            } else if (selected.length > 0) {
                                var search = selected;
                                if ($rootScope.session.isHKAdmin) {
                                    var payload = {"user": search.trim(), franchise: $scope.activityflow.franchise.value};
                                    Messaging.retrieveUserListFranchise(payload, success, failure);
                                } else {
                                    AssetService.retrieveUserList(search.trim(), success, failure);
                                }
                            } else {
                                query.callback({
                                    results: $scope.names
                                });
                            }
                        }
                    }
                    obj["statusMultiselectconfig"] = {
                        'allowClear': true,
                        'data': $scope.planStatusList,
                        'placeholder': "Select",
                        'multiple': true,
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
                            query.callback({
                                results: $scope.planStatusList
                            });
                        }
                    }
                    $rootScope.plans.push(obj);
                }
            }
            $scope.deleteInEditPlan = function (index) {
                if ($rootScope.plans.length === 1) {
                    $rootScope.plans = [];
                    var item = {};
                    item.multiselectConfig = {multiple: true,
                        closeOnSelect: false,
                        placeholder: 'Select members',
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
                            $scope.names = [];
                            var success = function (data) {
                                if (data.length !== 0) {
                                    $scope.names = data;
                                    angular.forEach(data, function (item) {
                                        $scope.names.push({
                                            id: item.value + ":" + item.description,
                                            text: item.label

                                        });
                                    });
                                }
                                query.callback({
                                    results: $scope.names
                                });
                            };
                            var failure = function () {
                            };
                            if (selected.substring(0, 2) === '@E' || selected.substring(0, 2) === '@e') {
                                var search = query.term.slice(2);
                                if ($rootScope.session.isHKAdmin) {
                                    var payload = {"user": search.trim(), franchise: $scope.activityflow.franchise.value};
                                    Messaging.retrieveUserListFranchise(payload, success, failure);
                                } else {
                                    AssetService.retrieveUserList(search.trim(), success, failure);
                                }
                            } else if (selected.substring(0, 2) === '@D' || selected.substring(0, 2) === '@d') {
                                var search = query.term.slice(2);
                                if ($rootScope.session.isHKAdmin) {
                                    var payload = {"department": search.trim(), franchise: $scope.activityflow.franchise.value};
                                    Messaging.retrieveDepartmentListFranchise(payload, success, failure);
                                } else {
                                    Messaging.retrieveDepartmentList(search.trim(), success, failure);
                                }
                            } else if (selected.substring(0, 2) == '@R' || selected.substring(0, 2) == '@r') {
                                var search = query.term.slice(2);
                                if ($rootScope.session.isHKAdmin) {
                                    var payload = {"role": search.trim(), franchise: $scope.activityflow.franchise.value};
                                    Messaging.retrieveRoleListFranchise(payload, success, failure);
                                } else {
                                    Messaging.retrieveRoleList(search.trim(), success, failure);
                                }
                            } else if (selected.substring(0, 2) === '@A' || selected.substring(0, 2) === '@a') {
                                var search = query.term.slice(2);
                                Messaging.retrieveActivityList(search.trim(), success, failure);
                            } else if (selected.substring(0, 2) == '@G' || selected.substring(0, 2) == '@g') {
                                var search = query.term.slice(2);
                                Messaging.retrieveGroupList(search.trim(), success, failure);
                            } else if (selected.length > 0) {
                                var search = selected;
                                if ($rootScope.session.isHKAdmin) {
                                    var payload = {"user": search.trim(), franchise: $scope.activityflow.franchise.value};
                                    Messaging.retrieveUserListFranchise(payload, success, failure);
                                } else {
                                    AssetService.retrieveUserList(search.trim(), success, failure);
                                }
                            } else {
                                query.callback({
                                    results: $scope.names
                                });
                            }
                        }};
                    item.statusMultiselectconfig = {
                        'allowClear': true,
                        'data': $scope.planStatusList,
                        'placeholder': "Select",
                        'multiple': true,
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
                            query.callback({
                                results: $scope.planStatusList
                            });
                        }
                    };
                    $rootScope.plans.push(item);
                } else {
                    $rootScope.plans.splice(index, 1);
                }
            };
//            ----------------------------------------Rule functions end----------------------------//
        }]);
});