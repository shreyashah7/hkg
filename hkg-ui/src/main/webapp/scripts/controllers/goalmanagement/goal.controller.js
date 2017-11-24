/* 
 * Author: Raj Kantaria
 * Controller file for manage goal feature
 */
define(['hkg', 'goalService', 'activityFlowService', 'leaveWorkflowService', 'designationService', 'departmentService', 'addMasterValue', 'ruleField', 'dynamicForm'], function(hkg) {
    hkg.register.controller('GoalController', ["$rootScope", "$scope", "GoalService", "ActivityFlowService", "LeaveWorkflow", "Designation", "DepartmentService", '$location', function($rootScope, $scope, GoalService, ActivityFlowService, LeaveWorkflow, Designation, DepartmentService, $location) {
            $rootScope.maskLoading();
            $rootScope.mainMenu = "manageLink";
            $rootScope.childMenu = "manageGoals";
            $rootScope.activateMenu();
            $scope.goal = {};
            $scope.entity = "GOAL.";
            $scope.flag = false;
            $scope.submitted = false;
            $scope.initData = function() {
                $scope.displaySearchedGoalTemplate = 'view';
                if ($rootScope.clearGoalData === undefined || $rootScope.clearGoalData === true) {
                    delete $rootScope.goalTemplatePayload;
                }
                if (!!$rootScope.goalTemplatePayload) {
                    $scope.goal.service = $rootScope.goalTemplatePayload.service;
                    $scope.goal.designation = $rootScope.goalTemplatePayload.designation;
                    $scope.selectedParent = $rootScope.goalTemplatePayload.selectedDepartment;
                    $scope.goal.type = $rootScope.goalTemplatePayload.type;
                }
                $rootScope.maskLoading();
                GoalService.retrieveGoalPermissionByDesignations({designations: $rootScope.session.roleIds}, function(data) {
                    $scope.mapOfGoalPermission = data;

                    if ($scope.mapOfGoalPermission["S"] != null) {
                        var tempValues = [];
                        ActivityFlowService.retrieveServicesWithActivity({"q": ""}, function(serviceData) {
                            angular.forEach(serviceData.data, function(designation) {
                                angular.forEach($scope.mapOfGoalPermission["S"], function(item) {
                                    if (item == designation.nodeId) {
                                        tempValues.push({
                                            id: designation.nodeId,
                                            text: designation.associatedServiceName + "(" + designation.activityName + ")",
                                            designationName: designation.designationName
                                        });
                                    }
                                });
                            });
                            $scope.serviceList = tempValues;
                            angular.forEach($scope.serviceList, function(item) {
                                if (item.id == $scope.goal.service) {
                                    $scope.designationName = item.designationName;
                                }
                            });
                        });
                    }
                    if ($scope.mapOfGoalPermission["R"] != null) {
                        var tempValues = [];
                        Designation.retrieveDesignations(function(data) {
                            var tempDesignations = [];
                            if (data !== null) {
                                angular.forEach($scope.mapOfGoalPermission["R"], function(item1) {
                                    angular.forEach(data, function(item) {
                                        if (item1 == item.id) {
                                            tempDesignations.push(item);
                                        }
                                    });
                                });
                            }
                            $scope.designationList = tempDesignations;
                        });
                    }
                    if ($scope.mapOfGoalPermission["D"] != null) {

                        DepartmentService.retrieveDepartmentSimpleTreeView(function(data) {
                            var tempDepartments = [];
                            if (data !== null) {
                                angular.forEach($scope.mapOfGoalPermission["D"], function(item1) {
                                    angular.forEach(data, function(item) {
                                        if (item1 == item.id) {
                                            tempDepartments.push(item);
                                        }
                                    });
                                });
                            }
                            $scope.departmentList = tempDepartments;
                        });
                    }
                    if ($scope.mapOfGoalPermission["S"] === null && $scope.mapOfGoalPermission["R"] === null && $scope.mapOfGoalPermission["D"] === null) {
                        $rootScope.addMessage("Configure goal add edit permission from designation page first  ", $rootScope.warning);
                    }
                    $rootScope.unMaskLoading();
                }, function() {
                    console.log("Failed to retrieve...");
                    $rootScope.unMaskLoading();
                });


                $rootScope.clearGoalData = true;
            };

            $scope.setSelectedParent = function(selected) {
                if (!angular.equals(selected, {})) {
                    $scope.selectedParent = selected.currentNode;
                    selected.currentNode.displayName = $rootScope.translateValue('DPT_NM.' + selected.currentNode.displayName);
                    $scope.retrieveGoalTemplatesByDepartment();
                }
            };

            $scope.clearSelection = function() {
                $scope.goal.service = "";
                $scope.goal.designation = "";
                $scope.selectedDepartmentDropdown["currentNode"] = "";
                delete $scope.selectedParent;
                $scope.clearTreeSelect($scope.departmentList);
            };
            $scope.clearTreeSelect = function(datas) {
                angular.forEach(datas, function(item) {
                    item.selected = undefined;
                    if (!!item.children) {
                        $scope.clearTreeSelect(item.children);
                    }
                });
            };

            $scope.addNewTemplate = function() {
                var payload = $scope.goal;
                payload.selectedDepartment = $scope.selectedParent;
                payload.goalTemplatesSelected = $scope.goal.goalTemplates;
                $rootScope.goalTemplatePayload = payload;
                $scope.flag = false;
                $location.path("/managegoaltemplate");
            };

            $scope.$watch("goal.goalTemplates", function(newVal) {
                console.log("newVal:::::"+JSON.stringify(newVal));
                if (typeof newVal === "string") {
                    $scope.goalTemplatesList = [];
                    if (newVal !== undefined && newVal !== null && newVal.length > 0) {
                        angular.forEach(newVal.split(","), function(item) {
                            angular.forEach($scope.allGoalTemplates, function(item1) {
                                if (item == item1.id) {
                                    $scope.goalTemplatesList.push(item1);
                                }
                            });
                        });
                    }
                }else{
                    delete $scope.goalTemplatesList;
                }
            }, true);
            $scope.autoCompleteApprover = {
                allowClear: true,
                multiple: true,
                closeOnSelect: true,
                placeholder: 'Select goal templates',
                initSelection: function(element, callback) {
                    if (!$scope.flag) {
                        $rootScope.maskLoading();
                        GoalService.retrieveAllGoalTemplates(function(data) {
                            if (!!data) {
                                $scope.allGoalTemplates = data;
                            }
                            var tempValues = [];
                            var ids = [];
                            $scope.goalTemplatesList = [];
                            if (!!$rootScope.goalTemplatePayload) {
                                angular.forEach($rootScope.goalTemplatePayload.goalTemplatesSelected.split(","), function(item) {
                                    angular.forEach($scope.allGoalTemplates, function(item1) {
                                        if (item == item1.id) {
                                            tempValues.push({
                                                id: item1.id,
                                                text: item1.name + ", " + item1.period + " days, " + item1.nameOfAssociation
                                            });
                                            ids.push(item1.id);
                                            $scope.goalTemplatesList.push(item1);
                                        }
                                    });
                                });
                            }
                            callback(tempValues);
                            $scope.goal.goalTemplates = ids.toString();
                            $rootScope.unMaskLoading();
                        }, function() {
                            console.log("Failed");
                            $rootScope.unMaskLoading();
                        });
                    } else {
                        callback($scope.goalTemplatesValue);
                    }
                },
                formatSelection: function(item) {
                    return item.text.split(",")[0];
                },
                query: function(query) {
                    var selected = query.term;
                    $scope.names = [];
                    var success = function(data) {
                        if (data.length === 0) {
                            query.callback({
                                results: $scope.names
                            });
                        } else {
                            angular.forEach(data, function(item) {
                                $scope.names.push({
                                    id: item.id,
                                    text: item.name + ", " + item.period + " days, " + item.nameOfAssociation
                                });
                            });
                            query.callback({
                                results: $scope.names
                            });
                        }
                    };
                    var failure = function() {
                    };
                    if (query.term != null && query.term != "") {
                        GoalService.retrieveGoalTemplatesBySearch({"q": query.term.trim()}, success, failure);
                    }
                }
            };
//            $('#goalTemplates').select2()
//                    .on("select2-selecting", function(e) {
////                        log("selecting val=" + e.val + " choice=" + e.object.text);
//                        if (!!$scope.goal.goalTemplates) {
//                            $scope.goalTemplatesList = [];
//                            angular.forEach($scope.goal.goalTemplates.split(","), function(item) {
//                                angular.forEach($scope.allGoalTemplates, function(item1) {
//                                    if (item == item1.id) {
//                                        $scope.goalTemplatesList.push(item1);
//                                    }
//                                });
//                            });
//                        }
//                    });
            $scope.saveGoals = function() {
                var tempValues = [];
                $scope.submitted = true;
                if ($scope.goal !== undefined && $scope.goal !== null && $scope.goal.goalTemplates !== undefined && $scope.goal.goalTemplates !== null && $scope.goal.goalTemplates.length >= 0) {
                    angular.forEach($scope.goal.goalTemplates.split(","), function(item) {
                        angular.forEach($scope.allGoalTemplates, function(item1) {
                            if (item == item1.id) {
                                if ($scope.goal.type == '1')
                                    item1.for_service = $scope.goal.service;
                                else
                                    item1.for_service = "";

                                if ($scope.goal.type == '2')
                                    item1.for_designation = $scope.goal.designation;
                                else
                                    item1.for_designation = "";

                                if ($scope.goal.type == '3')
                                    item1.for_department = $scope.selectedParent.id;
                                else
                                    item1.for_department = "";

                                tempValues.push(item1);
                            }
                        });
                    });
                    $rootScope.maskLoading();
                    GoalService.saveGoalTemplates(tempValues, function() {
                        $rootScope.unMaskLoading();
                        $scope.goal = {};
//                        $rootScope.addMessage("Goal template configured successfully", $rootScope.success);
                        var msg = "Goal template configured successfully";
                        var type = $rootScope.success;
                        $rootScope.addMessage(msg, type);
                        $rootScope.goalTemplatePayload = {};
                        delete $scope.selectedParent;
                        $scope.selectedDepartmentDropdown["currentNode"] = "";
                        $('#scrollable-dropdown-menu.typeahead').typeahead('val', '');
                        $scope.submitted = false;
                        $scope.goalForm.$setPristine();

                    }, function() {
                        $rootScope.unMaskLoading();
                        $rootScope.addMessage("Problem in configuring goal template", $rootScope.failure);
                    });
                }
//                } else {
//                    var payload = {};
//                    if ($scope.goal.type == '1') {
//                        payload.type = 'S';
//                        payload.val = $scope.goal.service;
//                    }
//                    if ($scope.goal.type == '2') {
//                        payload.type = 'R';
//                        payload.val = $scope.goal.designation;
//                    }
//                    if ($scope.goal.type == '3') {
//                        payload.type = 'D';
//                        payload.val = $scope.selectedParent.id;
//                    }
//                    $rootScope.maskLoading();
//                    GoalService.deleteActiveGoalTemplates(payload, function() {
//                        $rootScope.unMaskLoading();
//                        $scope.goal = {};
//                        var msg = "Goal template configured successfully";
//                        var type = $rootScope.success;
//                        $rootScope.addMessage(msg, type);
//                        $rootScope.goalTemplatePayload = {};
//                        delete $scope.selectedParent;
//                        $scope.selectedDepartmentDropdown["currentNode"] = "";
//                        $scope.submitted = false;
//                        $scope.goalForm.$setPristine();
//
//                    }, function() {
//                        $rootScope.unMaskLoading();
//                        console.log("Failure...");
//                    });
//                }
            };
            $scope.retrieveGoalTemplatesByService = function(index) {
                angular.forEach($scope.serviceList, function(item) {
                    if (item.id == $scope.goal.service) {
                        $scope.designationName = item.designationName;
                    }
                });
                $rootScope.maskLoading();
                GoalService.retrieveActiveGoalTemplatesByService($scope.goal.service, function(data) {
                    $rootScope.unMaskLoading();
                    if (!!data) {
                        var tempValues = [];
                        var ids = [];
                        $scope.goalTemplatesList = [];
                        angular.forEach(data, function(item1) {
                            tempValues.push({
                                id: item1.id,
                                text: item1.name + ", " + item1.period + " days, " + item1.nameOfAssociation
                            });
                            ids.push(item1.id);
                            $scope.goalTemplatesList.push(item1);
                        });
                        $scope.flag = true;
                        $scope.goalTemplatesValue = tempValues;
                        $('#goalTemplates').select2('val', tempValues);
                        $scope.goal.goalTemplates = ids.toString();
                    }
                }, function() {
                    $rootScope.unMaskLoading();
                    console.log("Fialure");
                });
            };
            $scope.retrieveGoalTemplatesByDesignation = function() {
                $rootScope.maskLoading();
                GoalService.retrieveActiveGoalTemplatesByDesignation($scope.goal.designation, function(data) {
                    $rootScope.unMaskLoading();
                    if (!!data) {
                        var tempValues = [];
                        var ids = [];
                        $scope.goalTemplatesList = [];
                        angular.forEach(data, function(item1) {
                            tempValues.push({
                                id: item1.id,
                                text: item1.name + ", " + item1.period + " days, " + item1.nameOfAssociation
                            });
                            ids.push(item1.id);
                            $scope.goalTemplatesList.push(item1);
                        });
                        $scope.flag = true;
                        $scope.goalTemplatesValue = tempValues;
                        $('#goalTemplates').select2('val', tempValues);
                        $scope.goal.goalTemplates = ids.toString();
                    }
                }, function() {
                    $rootScope.unMaskLoading();
                    console.log("Fialure");
                });
            };
            $scope.retrieveGoalTemplatesByDepartment = function() {
                $rootScope.maskLoading();
                GoalService.retrieveActiveGoalTemplatesByDepartment($scope.selectedParent.id, function(data) {
                    $rootScope.unMaskLoading();
                    if (!!data) {
                        var tempValues = [];
                        var ids = [];
                        $scope.goalTemplatesList = [];
                        angular.forEach(data, function(item1) {
                            tempValues.push({
                                id: item1.id,
                                text: item1.name + ", " + item1.period + " days, " + item1.nameOfAssociation
                            });
                            ids.push(item1.id);
                            $scope.goalTemplatesList.push(item1);
                        });
                        $scope.flag = true;
                        $scope.goalTemplatesValue = tempValues;
                        $('#goalTemplates').select2('val', tempValues);
                        $scope.goal.goalTemplates = ids.toString();
                    }
                }, function() {
                    $rootScope.unMaskLoading();
                    console.log("Fialure");
                });
            };

            $scope.editGoalTemplate = function(id) {
                var payload = $scope.goal;
                payload.selectedDepartment = $scope.selectedParent;
                payload.goalTemplatesSelected = $scope.goal.goalTemplates;
                payload.editGoalTemplateId = id;
                $rootScope.goalTemplatePayload = payload;
                $scope.flag = false;
                $location.path("/managegoaltemplate");
            };

            $scope.editFromSearch = function(searchVal) {
                $rootScope.maskLoading();
                GoalService.retrieveGoalTemplateById(searchVal, function(data) {
                    $rootScope.unMaskLoading();
                    $scope.goal = {};
                    if (!!data) {
                        if (!!data.for_service) {
                            $scope.goal.type = 1;
                            $scope.goal.service = data.for_service;
                            $scope.retrieveGoalTemplatesByService();
                        }
                        if (!!data.for_designation) {
                            $scope.goal.type = 2;
                            $scope.goal.designation = data.for_designation;
                            $scope.retrieveGoalTemplatesByDesignation();
                        }
                        if (!!data.for_department) {
                            $scope.goal.type = 3;
                            var department = {};
                            for (var index = 0; index < $scope.departmentList.length; index++) {
                                department = $scope.searchInDepartment($scope.departmentList[index], data.for_department);
                                if (!!data) {
                                    break;
                                }
                            }
                            var obj = {"currentNode": department};
                            $scope.selectedDepartmentDropdown = obj;
                            $scope.setSelectedParent($scope.selectedDepartmentDropdown);
                            $scope.retrieveGoalTemplatesByDepartment();
                        }
                    }
                }, function() {
                    $rootScope.unMaskLoading();
                    console.log("Failed..");
                });
            };
            $scope.searchInDepartment = function(data, id) {
                if (data.id == id) {
                    data.selected = true;
                    return data;
                } else {
                    if (!!data.children) {
                        return $scope.searchInDepartment(data.children, id);
                    }
                }
            };
            $scope.getSearchedDesignationRecords = function(list) {
                $scope.searchRecords = [];
                var enteredText = $('#scrollable-dropdown-menu.typeahead').typeahead('val');
                if (enteredText.length > 0) {
                    if (enteredText.length < 3) {
                        $scope.searchRecords = [];
                    } else {
                        if (list !== null && angular.isDefined(list) && list.length > 0) {
                            angular.forEach(list, function(item) {
                                $scope.searchRecords.push(item);
                            });
                        }
                    }
//                    $scope.resetSelection();
                    $scope.displaySearchedGoalTemplate = 'search';
                }
            };
            $scope.editGoalTemplateFromSearch = function(id) {
                $scope.displaySearchedGoalTemplate = 'view';
                $scope.editFromSearch(id);
            };
            $scope.cancelGoal = function() {
                $scope.displaySearchedGoalTemplate = 'view';
                $scope.goal = {};
                $scope.selectedDepartmentDropdown["currentNode"] = "";
                delete $scope.selectedParent;
                $scope.clearTreeSelect($scope.departmentList);
                $('#scrollable-dropdown-menu.typeahead').typeahead('val', '');
                $scope.goalForm.$setPristine();
            };
            $rootScope.unMaskLoading();
        }]);
});

