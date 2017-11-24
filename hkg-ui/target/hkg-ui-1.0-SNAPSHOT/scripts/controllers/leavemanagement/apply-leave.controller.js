define(['hkg', 'leaveService', 'addMasterValue', 'dynamicForm'], function (hkg) {
    hkg.register.controller('ApplyLeave', ["$rootScope", "$scope", "$location", "Leave", "DynamicFormService", "$filter", function ($rootScope, $scope, $location, Leave, DynamicFormService, $filter) {

            $rootScope.mainMenu = "manageLink";
            $rootScope.childMenu = "applyLeave";
            $rootScope.activateMenu();
            $scope.entity = "APPLYLEAVE.";
            $scope.leaveOperation = "";
            $scope.leaveReasonTypes = [];
            $scope.isEdit = false;
            //For ADD leave
            $scope.dateWarning = "";
            $scope.minDate = $rootScope.getCurrentServerDate();
            $scope.leave = {};
            $scope.leave.fromDate;
            $scope.leaveStartDate = "";
            $scope.leaveToDate = ""
            $scope.searchedApplyLeaveRecords = [];
            $scope.leave.remarks;
            $scope.listOfModelsOfDateType = [];
            Leave.retrievePendingStock($rootScope.session.id, function(result) {
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
            //For Apply and Manage Leave
            $scope.setLeaveOperation = function(operation,callback) {
                var templateData = DynamicFormService.retrieveSectionWiseCustomFieldInfo("applyLeave");
                $scope.dbType = {};
                templateData.then(function(section) {
                    $scope.listOfModelsOfDateType = [];
//                    $scope.addHolidayTemplate = section['Add Holiday'];
//                    $scope.editHolidayTemplate = section['Edit Holiday'];
                    // Method modified by Shifa Salheen on 17 April for implementing sequence number
                    $scope.customTemplateDate = angular.copy(section['genralSection']);
                    $scope.genralLeaveTemplate = $rootScope.getCustomDataInSequence($scope.customTemplateDate);
                    if ($scope.genralLeaveTemplate !== null && $scope.genralLeaveTemplate !== undefined)
                    {
                        angular.forEach($scope.genralLeaveTemplate, function(updateTemplate)
                        {
                            if (updateTemplate.type !== null && updateTemplate.type !== undefined && updateTemplate.type === 'date')
                            {
                                $scope.listOfModelsOfDateType.push(updateTemplate.model);
                            }
                        })
                    }


                }, function(reason) {
                }, function(update) {
                });
                $scope.leaveOperation = operation;
                if ($scope.leaveOperation === 'addLeave' && $scope.isEdit === false) {
                    $scope.leave.remarks = "";
//                    $scope.setTime();
//                    $scope.setDefaultTime()
//
//                    $scope.setToTime();
//                    $scope.setToDefaultTime();
                    $('#scrollable-dropdown-menu.typeahead').typeahead('val', '');
                    $scope.addLeaveData = DynamicFormService.resetSection($scope.genralLeaveTemplate);

                }
                if ($scope.leaveOperation !== 'searchedLeave') {
                    $scope.searchedApplyLeaveRecords = [];
                    $('#scrollable-dropdown-menu.typeahead').typeahead('val', '');
                }
                if(!!callback){
                    callback();
                }
            }


//            $scope.$watch("leave.fromDate", function() {
//                if ($scope.leave.fromDate !== undefined && !$scope.isEdit) {
//                    $scope.setToTime($scope.leave.fromDate, true);
//                    $scope.setToDefaultTime();
//                }
//            });
            $scope.initApplyLeave = function() {
                $scope.setLeaveOperation('applyLeave');
                $scope.retrieveAllLeave();

                Leave.retrieveLeaveReason(function(res) {
                    angular.forEach(res.data, function(item) {
                        $scope.leaveReasonTypes.push({
                            id: item.value,
                            text: item.label,
                            displayName: item.label

                        });
                    });
                }, function() {
                    $rootScope.addMessage("Failed to retrive leave reason", 1);
                });
            }
            $scope.retrieveAllLeave = function() {
                $rootScope.maskLoading();
                $scope.addLeaveData = DynamicFormService.resetSection($scope.genralLeaveTemplate);
                Leave.retrieveLeave(function(res) {
                    $scope.records = res.data;
                    $rootScope.unMaskLoading();
                }, function() {
                    $rootScope.addMessage("Failed to retrive leave reason", 1)
                });
            }
            $scope.initApplyLeave();
            $scope.getReasonById = function(id) {
                var reasonText = {};
                angular.forEach($scope.leaveReasonTypes, function(item) {
                    if (item.id === id) {
                        reasonText = item;
                    }
                });
                return reasonText;
            }
            $scope.cancelLeave = function(form) {
                $scope.addLeaveData = DynamicFormService.resetSection($scope.genralLeaveTemplate);
                $scope.isEdit = false;
                $scope.leave = {};
                $scope.leave.reason = "";
                form.$setPristine();
                $scope.submitted = false;
                $scope.retrieveAllLeave();
                $scope.setLeaveOperation('applyLeave');
            }
            $scope.initAddHolidayForm = function(form) {
                form.$dirty = false;
            }
//Applyed leave add in system
            $scope.addLeave = function(leaveForm, leaveModel) {
                $scope.submitted = true;
                var valid = true;
                for (var key in leaveForm) {
                    if (key !== 'passwordPopUpForm' && key !== 'editMasterForm') {
                        if (leaveForm[key].$invalid) {
                            console.log("foem" + leaveForm[key] + "and key is" + key)
                            valid = false;
                            break;
                        }
                    }
                }

                if (valid) {
                    var leaveJson = {
                        "fromDate": getDateFromFormat(leaveModel.fromDate, $rootScope.dateFormatWithTime),
                        "toDate": getDateFromFormat(leaveModel.toDate, $rootScope.dateFormatWithTime),
                        "description": leaveModel.description,
                        "reasonId": leaveModel.reason.id,
                        "reason": leaveModel.reason.text,
                        "edit": true,
                        "status": "Pending",
                        "requestType": "Leave",
                        "leaveCustom": $scope.addLeaveData,
                        "dbType": $scope.dbType,
                        "remarks": leaveModel.remarks

                    }
                    $rootScope.maskLoading();
                    Leave.addLeave(leaveJson, function(res) {

                        if (res.messages !== undefined && res.messages[0].responseCode) {
                            $rootScope.addMessage(res.messages[0].message, res.messages[0].responseCode);
                            leaveForm.$setPristine();
                            $scope.submitted = false;
                            $scope.leave.reason.id = leaveModel.reason.id;
                            $scope.leave.reason = $scope.getReasonById(leaveModel.reason.id);// {id: leaveModel.reason.id, text: $scope.getReasonById(leaveModel.reason.id)};
                        } else {
                            $scope.retrieveAllLeave();
                            $scope.leave = {};
                            $scope.leave.reason = "";
                            leaveForm.$setPristine();
                            $scope.submitted = false;
                            $scope.setLeaveOperation('applyLeave');
                        }
                        $rootScope.pingurl = $rootScope.centerapipath + "common/getsession";
                        $rootScope.$broadcast('event:loginConfirmed', $rootScope.pingurl);
                        $rootScope.unMaskLoading();
                    }, function() {
                        $rootScope.addMessage("Could not save leave, please try again.", 1);
                        $rootScope.unMaskLoading();
                    });
                }
            }

//for edit and remove Leave
            $scope.updateLeave = function(leave, leaveStatus, leaveForm) {
                $scope.submitted = true;

                var valid = true;
                for (var key in leaveForm) {
                    if (key !== 'passwordPopUpForm' && key !== 'editMasterForm') {
                        if (leaveForm[key].$invalid) {
                            console.log("foem" + leaveForm[key] + "and key is" + key)
                            valid = false;
                            break;
                        }
                    }
                }

                if (valid) {
                    if ($scope.leave.cancel) {
                        $scope.showRemoveConfirmation(leave);
                    } else {
                        $scope.leave.reasonId = $scope.leave.reason.id;
                        $scope.leave.reason = " ";
                        $scope.leave.dbType = $scope.dbType;
                        $scope.leave.fromDate = getDateFromFormat($scope.leave.fromDate, $rootScope.dateFormatWithTime);
                        $scope.leave.toDate = getDateFromFormat($scope.leave.toDate, $rootScope.dateFormatWithTime);
                        $scope.leave.applyedOn = getDateFromFormat($scope.leave.toDate, $rootScope.dateFormat);
                        Leave.updateLeave($scope.leave, function(res) {
                            if (res.messages !== undefined && res.messages[0].responseCode) {
                                $rootScope.addMessage(res.messages[0].message, res.messages[0].responseCode);
                                $scope.leave.reason = {id: $scope.leave.reasonId, text: $scope.getReasonById($scope.leave.reasonId)};
                            } else {
                                $scope.retrieveAllLeave();
                                $scope.leave = {};
                                $scope.isEdit = false;
                                leaveForm.$setPristine();
                                $scope.submitted = false;
                                $scope.setLeaveOperation('applyLeave');
                            }
                        }, function() {
                            $rootScope.addMessage("Could not update leave, please try again.", 1);
                        });
                    }
                }
            }
            $scope.setEditLeave = function(leave) {
                Leave.retrieveLeaveByLeaveId(leave.id, function(res) {
                    
                    $scope.options = [{name: "Active", id: 1}, {name: "Remove", id: 2}];
                    $scope.leaveStatus = $scope.options [0];
                    $scope.isEdit = true;
                    $scope.leave = res;
                    $scope.addLeaveData = res['leaveCustom'];
                    $scope.leave.applyedOn = $filter("date")(leave.applyedOn, "dd/MM/yyyy");
                    $scope.leave.reason = $scope.getReasonById($scope.leave.reasonId);
                    $scope.setLeaveOperation('addLeave', function() {
                        if (!!$scope.addLeaveData) {
                            angular.forEach($scope.listOfModelsOfDateType, function(listOfModel)
                            {
                                if ($scope.addLeaveData.hasOwnProperty(listOfModel))
                                {
                                    if ($scope.addLeaveData[listOfModel] !== null && $scope.addLeaveData[listOfModel] !== undefined)
                                    {
                                        $scope.addLeaveData[listOfModel] = new Date($scope.addLeaveData[listOfModel]);
                                    } else
                                    {
                                        $scope.addLeaveData[listOfModel] = '';
                                    }
                                }
                            })
                        }
                    });
                });
            }
            $scope.redirectToEdit = function(id) {
                var leave = $scope.getLeaveById(id);
                $scope.setEditLeave(leave);

            }
            $scope.redirectToSearchedPage = function(list) {
                var enteredText = $('#scrollable-dropdown-menu.typeahead').typeahead('val');
                if (enteredText.length > 0) {
                    if (enteredText.length < 3) {
                        $scope.searchedApplyLeaveRecords = [];
                        $scope.setLeaveOperation('searchedLeave');
                    } else {
                        $scope.searchedApplyLeaveRecords = angular.copy(list);
                        $scope.setLeaveOperation('searchedLeave');
                    }
                }
            }
            $scope.getLeaveById = function(id) {
                var leave = {};
                angular.forEach($scope.records, function(item) {
                    if (item.id === id) {
                        leave = item;
                    }
                });
                return leave;
            }
            $scope.removeLeave = function() {
                var leaveId = {
                    primaryKey: $scope.leave.id
                }
                Leave.deleteLeave(leaveId, function(res) {
                    $scope.retrieveAllLeave();
                    $scope.isEdit = false;
                    $scope.leave = {};
                    $scope.leave.reason = "";
                    $scope.setLeaveOperation('applyLeave');
                }, function() {
                    $rootScope.addMessage("Could not remove leave, please try again.", 1);
                });
            }

            $scope.archiveLeave = function(leave) {
                var leaveId = {
                    primaryKey: leave.id
                }
                Leave.archiveLeave(leaveId, function(res) {
                    $scope.retrieveAllLeave();
                }, function() {
                    $rootScope.addMessage("Could not archive leave, please try again.", 1);
                })
            }
            $scope.applyLeave = function() {
                $scope.isEdit = false;
                $scope.leave = {};
                $scope.leave.reason = "";
                $scope.setLeaveOperation('addLeave');
            }
            $scope.showRemoveConfirmation = function(leave) {
                $(document).find($('#removeShowId')).modal('show');
                $scope.leaveStartDate = leave.fromDate;
                $scope.leaveToDate = leave.toDate;
            }
            $scope.removeOk = function() {

                $(document).find($('#removeShowId')).modal('hide');
                $rootScope.removeModalOpenCssAfterModalHide();
                $('.modal-backdrop').remove();
                $scope.removeLeave();
            }
            $scope.removeCancel = function() {
                $(document).find($('#removeShowId')).modal('hide');
                $rootScope.removeModalOpenCssAfterModalHide();

            }

//            $(document).on({
//                "shown.bs.dropdown": function() {
//                    $(this).data('closable', false);
//                },
//                "hide.bs.dropdown": function() {
//                    return $(this).data('closable');
//                }
//            }, '.dropdown.keep-open');
//            $scope.setDateAndTime = function() {
//
//                $(document).find($('.dropdown.keep-open')).data('closable', true);
//            }
//            $scope.setTime = function(value) {
//                var d = new Date(value);
//                if ($scope.leave !== undefined && $scope.leave.fromDate !== undefined) {
//                    var hours = (d.getHours() < 10) ? '0' + d.getHours() : d.getHours();
//                    var minutes = (d.getMinutes() < 10) ? '0' + d.getMinutes() : d.getMinutes();
//                    var d = new Date($scope.leave.fromDate);
//                    d.setHours(hours);
//                    d.setMinutes(minutes);
//                    $scope.leave.fromDate = d;
//                } else {
//                    var date = new Date();
//                    if (date.getDate() == $scope.daysInMonth(date.getMonth() + 1, date.getFullYear())) {
//                        date.setDate(1);
//                    } else {
//                        date.setDate(date.getDate() + 1);
//                    }
//                    date.setHours(00);
//                    date.setMinutes(00);
//                    $scope.leave.fromDate = date;
//                    //$scope.dateWarning = "Select Date first......";
//                }
//            }
//
//            $scope.daysInMonth = function(month, year) {
//                return new Date(year, month, 0).getDate();
//            }
//            $scope.setToTime = function(value, flag) {
//                var d = new Date(value);
//                if ($scope.leave !== undefined && $scope.leave.toDate !== undefined && flag === undefined) {
//                    var hours = (d.getHours() < 10) ? '0' + d.getHours() : d.getHours();
//                    var minutes = (d.getMinutes() < 10) ? '0' + d.getMinutes() : d.getMinutes();
//                    var newTodt = new Date($scope.leave.toDate);
//                    newTodt.setHours(hours);
//                    newTodt.setMinutes(minutes);
//                    $scope.leave.toDate = newTodt;
//                } else {
//                    if ($scope.leave !== undefined && $scope.leave.fromDate !== undefined) {
//                        var date = new Date($scope.leave.fromDate);
//                        if (date.getDate() == $scope.daysInMonth(date.getMonth() + 1, date.getFullYear())) {
//                            date.setDate(1);
//                            date.setMonth(date.getMonth() + 1);
//                        } else {
//                            date.setDate(date.getDate());
//                        }
//                        date.setHours(00);
//                        date.setMinutes(00);
//                        $scope.leave.toDate = date;
//                    }
//                }
//            }
//            $scope.setDefaultTime = function() {
//                $scope.dateWarning = "";
//                var defaultTime = new Date($scope.leave.fromDate);
//                defaultTime.setHours(00);
//                defaultTime.setMinutes(00);
//                $scope.leave.fromDate = defaultTime;
//            }
//            $scope.setToDefaultTime = function() {
//                $scope.dateWarning = "";
//                var defaultTime = new Date($scope.leave.toDate);
//                defaultTime.setHours(23);
//                defaultTime.setMinutes(59);
//                $scope.leave.toDate = defaultTime;
//            }
//
//// for date picker
//            $scope.open = function($event, opened) {
//                $event.preventDefault();
//                $event.stopPropagation();
//                $scope.minDate = $rootScope.getCurrentServerDate();
//                $scope[opened] = true;
//            };
//            $scope.setMaxDate = function(maxDate) {
//
//                $scope.maxDate = maxDate;
//            }
//            $scope.dateOptions = {
//                'year-format': "'yy'",
//                'starting-day': 1
//            };
//            $scope.formats = ['dd/MM/yyyy HH:mm a', 'yyyy/MM/dd', 'shortDate'];
//            $scope.format = $scope.formats[0];
//            //for time picker
//
//            $scope.fromTime = new Date(", 2008 00:00:00");
//            $scope.toTime = new Date(", 2008 23:59:00");
//            // $scope.fromTime.setTime(0000000);
//            //$scope.toTime.setTime(0);
//            $scope.hstep = 1;
//            $scope.mstep = 30;
//            $scope.ismeridian = false;
            //$rootScope.unMaskLoading();
        }]);
});
