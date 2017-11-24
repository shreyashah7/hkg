define(['hkg', 'leaveService', 'addMasterValue', 'dynamicForm'], function (hkg) {
    hkg.register.controller('ManageLeave', ["$rootScope", "$scope", "Leave", "$filter", "DynamicFormService", function ($rootScope, $scope, Leave, $filter, DynamicFormService) {
            $rootScope.maskLoading();
            $rootScope.mainMenu = "manageLink";
            $rootScope.childMenu = "manageLeave";
            $rootScope.activateMenu();
            $scope.manageLeaveOperation = "";
            $scope.allUserLeaves = [];
            $scope.searchedManageLeaveRecords = [];
            $scope.entity = "MANAGELEAVE."
            $scope.dbType = {};

            var applyedLeaveTemplate = DynamicFormService.retrieveSectionWiseCustomFieldInfo("applyLeave");
            $scope.applyedLeaveData = DynamicFormService.resetSection($scope.genralApplyedLeaveTemplate);
            $scope.listOfModelsOfDateType1 = [];
            applyedLeaveTemplate.then(function(section) {
                // Method modified by Shifa Salheen on 17 April for implementing sequence number
                $scope.customTemplateDate = angular.copy(section['genralSection']);
                $scope.genralApplyedLeaveTemplate = $rootScope.getCustomDataInSequence($scope.customTemplateDate);
                if ($scope.genralApplyedLeaveTemplate !== null && $scope.genralApplyedLeaveTemplate !== undefined)
                {
                    angular.forEach($scope.genralApplyedLeaveTemplate, function(updateTemplate)
                    {
                        if (updateTemplate.type !== null && updateTemplate.type !== undefined && updateTemplate.type === 'date')
                        {
                            $scope.listOfModelsOfDateType1.push(updateTemplate.model);
                        }
                    })
                }
//                console.log("$scope.genralApplyedLeaveTemplate : " + JSON.stringify($scope.genralApplyedLeaveTemplate));
            }, function(reason) {
//                console.log('Faile: ' + reason);
            }, function(update) {
//                console.log('Got notification: ' + update);
            });
            $scope.addRespondLeaveData = DynamicFormService.resetSection($scope.genralRespondLeaveTemplate);
            $scope.listOfModelsOfDateType = [];
            var templateData = DynamicFormService.retrieveSectionWiseCustomFieldInfo("manageLeave");
            templateData.then(function(section) {
                $scope.genralRespondLeaveTemplate = section['genralSection'];
                if ($scope.genralRespondLeaveTemplate !== null && $scope.genralRespondLeaveTemplate !== undefined)
                {
                    angular.forEach($scope.genralRespondLeaveTemplate, function(updateTemplate)
                    {
                        if (updateTemplate.type !== null && updateTemplate.type !== undefined && updateTemplate.type === 'date')
                        {
                            $scope.listOfModelsOfDateType.push(updateTemplate.model);
                        }
                    })
                }
//                $scope.leaveTemplate = section['Apply Leave'];
//                console.log("Leave Template=" + $scope.leaveTemplate);
//                console.log("$scope.genralRespondLeaveTemplate : " + JSON.stringify($scope.genralRespondLeaveTemplate));
            }, function(reason) {
                console.log('Faile: ' + reason);
            }, function(update) {
                console.log('Got notification: ' + update);
            });
            $scope.showEmployeeLeavePopup = function() {
                $('#employeeLeavePopup').modal('show');
            }
            $scope.proceedCancel = function() {
                $('#employeeLeavePopup').modal('hide');
                $rootScope.removeModalOpenCssAfterModalHide();
            }
            $scope.proceedOk = function() {
                $('#employeeLeavePopup').modal('hide');
                $rootScope.removeModalOpenCssAfterModalHide();
            }
            $scope.redirectToRespondLeave = function(id) {
                Leave.searchLeaveForRespondById(id, function(res) {
                    $scope.setManageLeaveOperation('respondLeave', res);
                }, function() {
                    $rootScope.addMessage("could not retrive leave", 1);
                })
            }
            $scope.redirectToSearchePage = function(list) {
                var enteredText = $('#scrollable-dropdown-menu.typeahead').typeahead('val');
                if (enteredText.length > 0) {
                    if (enteredText.length < 3) {
                        $scope.searchedManageLeaveRecords = [];
                        $scope.setManageLeaveOperation('searchedApprover');
                    } else {
                        $scope.searchedManageLeaveRecords = angular.copy(list);
                        $scope.setManageLeaveOperation('searchedApprover');
                    }
                }
            }
            $scope.initRespondLeave = function(leave) {
                console.log("leave::::" + JSON.stringify(leave));
                Leave.retrievePendingStock(leave.userId, function(result) {
                    console.log("Pending stock::" + JSON.stringify(result));
                    $scope.pendingStockList = [];
                    $scope.stockList = [];
                    if (!!result["PendingStock"] && result["PendingStock"].length > 0) {
                        $scope.pendingStockList = result["PendingStock"];
                        angular.forEach($scope.pendingStockList, function(item) {
                            item = $rootScope.translateValue("APPLYLEAVE." + item);
                            $scope.stockList.push({"displayName": item});
                        });
                    }
                });
                $scope.cancelRespondLeave = function() {
                    $scope.setManageLeaveOperation('manageLeave');
                }
                $scope.setLeaveForRespond = function(leave) {
                    $scope.leave = leave;
                    Leave.addCustomDataToLeaveDataBean($scope.leave, function(res) {
                        $scope.leave.approverCustom = res.approverCustom;
                        if (!!$scope.leave.approverCustom) {
                            $scope.addRespondLeaveData = $scope.leave.approverCustom;
                        } else {
                            $scope.addRespondLeaveData = {};
                        }

                        if (!!$scope.addRespondLeaveData) {
                            angular.forEach($scope.listOfModelsOfDateType, function(listOfModel)
                            {
                                if ($scope.addRespondLeaveData.hasOwnProperty(listOfModel))
                                {
                                    if ($scope.addRespondLeaveData[listOfModel] !== null && $scope.addRespondLeaveData[listOfModel] !== undefined)
                                    {
                                        $scope.addRespondLeaveData[listOfModel] = new Date($scope.addRespondLeaveData[listOfModel]);
                                    } else
                                    {
                                        $scope.addRespondLeaveData[listOfModel] = '';
                                    }
                                }
                            })
                        }
                        $scope.leave.leaveCustom = res.leaveCustom;
                        if (!!$scope.leave.leaveCustom) {
                            $scope.applyedLeaveData = $scope.leave.leaveCustom;
                        } else {
                            $scope.applyedLeaveData = {};
                        }

                        if (!!$scope.applyedLeaveData) {
                            angular.forEach($scope.listOfModelsOfDateType1, function(listOfModel)
                            {
                                if ($scope.applyedLeaveData.hasOwnProperty(listOfModel))
                                {
                                    if ($scope.applyedLeaveData[listOfModel] !== null && $scope.applyedLeaveData[listOfModel] !== undefined)
                                    {
                                        $scope.applyedLeaveData[listOfModel] = new Date($scope.applyedLeaveData[listOfModel]);
                                    } else
                                    {
                                        $scope.applyedLeaveData[listOfModel] = '';
                                    }
                                }
                            })
                        }
                        $scope.leave.fromDate = $filter("date")(leave.fromDate, "dd/MM/yyyy HH:mm a");
                        $scope.leave.toDate = $filter("date")(leave.toDate, "dd/MM/yyyy HH:mm a");
                        $scope.leave.applyedOn = $filter("date")(leave.applyedOn, "dd/MM/yyyy");
                    }, function() {
                    });
                }
                $scope.approveLeave = function() {
                    var respondJson = {
                        id: $scope.leave.id,
                        remarks: $scope.leave.remarks,
                        respondCustom: $scope.addRespondLeaveData,
                        dbType: $scope.dbType
                    };
                    Leave.approveLeave(respondJson, function(res) {
                        $scope.setManageLeaveOperation('manageLeave');
                    }, function() {
                        $rootScope.addMessage("Failed to approve leave", 1);
                    })
                }
                $scope.disApproveLeave = function() {
                    var respondJson = {
                        id: $scope.leave.id,
                        remarks: $scope.leave.remarks,
                        respondCustom: $scope.addRespondLeaveData,
                        dbType: $scope.dbType
                    };
                    Leave.disApproveLeave(respondJson, function(res) {
                        $scope.setManageLeaveOperation('manageLeave');
                    }, function() {
                        $rootScope.addMessage("Failed to disapprove leave", 1);
                    })
                }
                $scope.cancelApproveLeave = function(record) {
                    $scope.isCancelApprove = false;
                    Leave.cancelApproveLeave(record.id, function(res) {
                        $scope.retrieveApprovalByCondition("false");
                        $scope.setManageLeaveOperation('manageLeave');
                    }, function() {
                        $rootScope.addMessage("Failed to Archiev leave", 1);
                    })
                };
                $scope.setLeaveForRespond(leave);
            }
            $scope.retrieveAllleaveByUserId = function(record) {
                $scope.leaveResponse = false;
                $scope.empName = record.requestFrom;
                Leave.retriveAllLeaveByUserId(record.userId, function(res) {
                    $scope.leaveResponse = true;
                    $scope.allUserLeaves = res;
                    $scope.showEmployeeLeavePopup();
                }, function() {
                    $rootScope.addMessage("Failed to retrive leave list", 1);
                })

            }
            $scope.retrieveApprovalByCondition = function(flag) {
                if (flag === false) {
                    flag = "false";
                } else {
                    flag = "true";
                }
                $scope.searchedApproverList = [];
                $rootScope.maskLoading();
                Leave.retrieveAllApproval(flag, function(res) {
                    $scope.records = res;
                    $rootScope.unMaskLoading();
                }, function() {
                    $rootScope.addMessage("Failed to retrive approval list", 1);
                })
            }
            $scope.setManageLeaveOperation = function(operation, leave, isCancel) {
                $scope.manageLeaveOperation = operation;
                $scope.isChecked = false;
                if (operation === 'respondLeave') {
                    $scope.proceedOk();
                    Leave.retrieveApprovalByID(leave.id, function(res) {
                        console.log("Result:::::" + JSON.stringify(res));
                        $scope.initRespondLeave(res);
                    });
                    if (isCancel) {
                        $scope.isCancelApprove = true;
                    }

                } else if (operation === 'manageLeave') {
                    $scope.retrieveApprovalByCondition($scope.isChecked);
                    $('#scrollable-dropdown-menu.typeahead').typeahead('val', '');
                }
                if (operation !== 'searchedApprover') {
                    $scope.searchedApproverList = [];
                }
            }


            $scope.sendCommentNotification = function() {
                var arr = [];
                arr[0] = $scope.leave.userId;
                arr[1] = $scope.leave.remarks;
                arr[2] = $scope.leave.id;
                arr[3] = $scope.leave.approvalId;
                Leave.sendCommentNotification(arr, function(res) {
                    $scope.setManageLeaveOperation('manageLeave');
                }, function() {
                    $rootScope.addMessage("Failed to comment on leave", 1);
                })
            }

            $rootScope.unMaskLoading();
        }]);
});
