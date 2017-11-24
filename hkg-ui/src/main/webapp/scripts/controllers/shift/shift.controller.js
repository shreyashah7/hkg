define(['hkg', 'shiftService', 'treeviewMultiselect.directive', 'addMasterValue', 'dynamicForm'], function (hkg, shiftService) {
    hkg.register.controller('ShiftController', ["$rootScope", "$scope", "ShiftService", "$location", "DynamicFormService", "$timeout", function ($rootScope, $scope, ShiftService, $location, DynamicFormService, $timeout) {
            $rootScope.maskLoading();
            $rootScope.mainMenu = "manageLink";
            $rootScope.childMenu = "manageShift";
            $rootScope.activateMenu();
            $scope.defaultSelectedids = [];
            if ($location.path() === '/manageshift') {
                $rootScope.configureDefaultShift = false;
            }
            $scope.initShiftManagementForm = function (shiftManagementForm) {
                $scope.shiftManagementForm = shiftManagementForm;
            };
            $scope.initTempShiftManagementForm = function (tempShiftForm) {
                $scope.tempShiftForm = tempShiftForm;
            };
            $scope.ovrDeptNames = [];
            $scope.ovrDeptNamesString = '';
            $scope.entity = "SHIFT.";
            $scope.addDefaultShiftPanel = true;
            $scope.temporaryShift = false;
            $scope.searchPage = false;
            $scope.tempShiftNameInPopUp = '';
            $scope.begins = {};
            $scope.shift = {};
//            $scope.shift.shiftStartTime = new Date();
//            $scope.shift.shiftEndTime = new Date();
//            $scope.shift.tempShiftStartTime = new Date();
//            $scope.shift.tempShiftEndTime = new Date();
            $scope.shift.selectedInString = [];
            $scope.isEditing = false;
            $scope.updateButton = false;
            $scope.endsHolidayOrEventFlag1 = false;
            $scope.beginsHoliday = false;
            $scope.beginsEvent = false;
            $scope.endsHoliday = false;
            $scope.endsEvent = false;
            $scope.allDaysAreSelectedAsWorking = false;
            $scope.breakCountZero = false;
            $scope.breakCountZeroForTempShift = false;
            $scope.updateShiftPanel = false;
            $scope.hideDatePicker = true;
            $scope.departmentRequired = true;
            $scope.shift.breakList = [];
            $scope.holidayList = [];
            $scope.eventList = [];
            $scope.list = [];
            $scope.listToShow = [];
            $scope.shift.tempShiftBreakList = [];
            $scope.temp = true;
            $scope.beginitemFlag = false;
            $scope.deptNameInPopUp = '';
            $scope.shiftNameInPopUp = '';
            $scope.mainShiftName = '';
            $scope.reload = true;
            var ovelap = {};
            $scope.addShiftData = {};
            $scope.listOfModelsOfDateType = [];
            var templateData = DynamicFormService.retrieveSectionWiseCustomFieldInfo("manageShift");
            $scope.dbType = {};
            templateData.then(function (section) {
                // Method modified by Shifa Salheen on 17 April for implementing sequence number
                $scope.customTemplateDate = angular.copy(section['genralSection']);
                $scope.generalShiftTemplate = $rootScope.getCustomDataInSequence($scope.customTemplateDate);
                if ($scope.generalShiftTemplate !== null && $scope.generalShiftTemplate !== undefined)
                {
                    angular.forEach($scope.generalShiftTemplate, function (updateTemplate)
                    {
                        if (updateTemplate.type !== null && updateTemplate.type !== undefined && updateTemplate.type === 'date')
                        {
                            $scope.listOfModelsOfDateType.push(updateTemplate.model);
                        }
                    })
                }
            }, function (reason) {

            }, function (update) {

            });
            /* Method added by Shifa Salheen on 2 April 2015 for clearing custom fields like multiselect and usermultiselect
             which were not getting cleared after add operation.Also On reset or cancel custom fields need to be cleared
             And On edit from tree u need to reload the directive*/
//            $scope.resetCustomFields = function ()
//            {
//                $scope.addShiftData = {};
//                var templateData = DynamicFormService.retrieveSectionWiseCustomFieldInfo("manageShift");
//                $scope.dbType = {};
//                templateData.then(function (section) {
//                  $scope.customTemplateDate = angular.copy(section['genralSection']);
//                $scope.generalShiftTemplate = $rootScope.getCustomDataInSequence($scope.customTemplateDate);
//
//                }, function (reason) {
//
//                }, function (update) {
//
//                });
//            };


            $scope.multiselecttree = {"text": "All Dep", "isChecked": false,
                items: []};
            $scope.$on('$viewContentLoaded', function () {
                $scope.departments = $scope.retrieveAllDepartments();
                $scope.initializWeekday();
                $scope.initializWeekdayFortemp();
            });

            $scope.initializWeekday = function () {
                $scope.weekDay = {};
                $scope.weekDay.WeekOffList = [{key: '2', code: 'M', value: 'Monday', isChecked: true}, {key: '3', code: 'Tu', value: 'Tuesday', isChecked: true},
                    {key: '4', code: 'W', value: 'Wednesday', isChecked: true}, {key: '5', code: 'Th', value: 'Thursday', isChecked: true}, {key: '6', code: 'F', value: 'Friday', isChecked: true},
                    {key: '7', code: 'Sa', value: 'Saturday', isChecked: true}, {key: '1', code: 'S', value: 'Sunday', isChecked: false}
                ];
            };
            $scope.weekDay = {};
            $scope.weekDay.WeekOffList = [{key: '2', code: 'M', value: 'Monday', isChecked: true}, {key: '3', code: 'Tu', value: 'Tuesday', isChecked: true},
                {key: '4', code: 'W', value: 'Wednesday', isChecked: true}, {key: '5', code: 'Th', value: 'Thursday', isChecked: true}, {key: '6', code: 'F', value: 'Friday', isChecked: true},
                {key: '7', code: 'Sa', value: 'Saturday', isChecked: true}, {key: '1', code: 'S', value: 'Sunday', isChecked: false}
            ];
            $scope.initializWeekdayFortemp = function () {
                $scope.shift.WeekOffListForTempShift = [{key: '2', code: 'M', value: 'Monday', isChecked: true}, {key: '3', code: 'Tu', value: 'Tuesday', isChecked: true},
                    {key: '4', code: 'W', value: 'Wednesday', isChecked: true}, {key: '5', code: 'Th', value: 'Thursday', isChecked: true}, {key: '6', code: 'F', value: 'Friday', isChecked: true},
                    {key: '7', code: 'Sa', value: 'Saturday', isChecked: true}, {key: '1', code: 'S', value: 'Sunday', isChecked: false}
                ];
            };

            $scope.beforeOrAfterList = [{id: 1, title: 'before'}, {id: 2, title: 'after'}];
            if ($scope.beforeOrAfterList !== undefined && $scope.beforeOrAfterList !== null && $scope.beforeOrAfterList.length > 0) {
                for (var i = 0; i < $scope.beforeOrAfterList.length; i++) {
                    $scope.beforeOrAfterList[i].title = $rootScope.translateValue("SHIFTS." + $scope.beforeOrAfterList[i].title);
                }
            }
            $scope.holidayOrEventList = [{id: 1, title: 'Holiday'}, {id: 2, title: 'Event'}];
            if ($scope.holidayOrEventList !== undefined && $scope.holidayOrEventList !== null && $scope.holidayOrEventList.length > 0) {
                for (var i = 0; i < $scope.holidayOrEventList.length; i++) {
                    $scope.holidayOrEventList[i].title = $rootScope.translateValue("SHIFTS." + $scope.holidayOrEventList[i].title);
                }
            }

            $scope.setSearchflag = function (searchedShift) {
                var enteredText = $('#scrollable-dropdown-menu.typeahead').typeahead('val');
                if (enteredText.length > 0) {
                    if ($scope.selectedShift != undefined) {
                        $scope.selectedShift.selected = undefined;
                    }
                    if ($scope.selectedShift != undefined && $scope.selectedShift.currentNode) {
                        $scope.selectedShift.currentNode.selected = undefined;
                        $scope.selectedShift.currentNode = undefined;
                    }
                    if ($scope.searchedShiftTree !== undefined) {
                        $scope.searchedShiftTree = undefined;
                    }
                    if (enteredText.length < 3) {
                        $scope.searchedShiftRecord = [];
                        $scope.searchPage = true;
                    } else {
                        if (searchedShift !== null && angular.isDefined(searchedShift) && searchedShift.length > 0) {
                            for (var i = 0; i < searchedShift.length; i++) {
                                searchedShift[i].text = $rootScope.translateValue("SHIFT." + searchedShift[i].text);
                            }
                        }
                        $scope.searchedShiftRecord = angular.copy(searchedShift);
                        $scope.searchPage = true;
                        $scope.isEditing = false;
                    }
                }
            };
            $scope.retrieveAllDepartments = function () {
                var failure = function () {
                    var msg = "Failed to retrieve departments. Try again.";
                    var type = $rootScope.failure;
                    $rootScope.addMessage(msg, type);
                    $rootScope.unMaskLoading();
                };
                ShiftService.retrieveDepartmentList(function (data) {
                    $scope.multiselecttree.items = data.items;
                    if (data !== null && angular.isDefined(data) && data.length > 0) {
                        for (var i = 0; i < $scope.multiselecttree.items.length; i++) {
                            $scope.multiselecttree.items[i].text = $rootScope.translateValue("DPT_NM." + $scope.multiselecttree.items.text);
                        }
                    }
                    $scope.dep = data;
                    $scope.setDefaultWorkflowRootOption();
                    for (var i = 0; i < $scope.dep.length; i++) {
                        convertTreeToList($scope.dep[i]);
                    }
//                    $rootScope.unMaskLoading();
                }, failure);

                ShiftService.retreiveShiftTree(function (data) {
                    $scope.allShifts = data;
                    $scope.shiftManagementForm.$setPristine();

                }, function () {
                    var msg = "Failed to retrieve shifts. Try again.";
                    var type = $rootScope.failure;
                    $rootScope.addMessage(msg, type);
                    $rootScope.unMaskLoading();
                });

                ShiftService.retrieveShiftsWithDetails(function (data) {
                    $scope.shiftsDetail = data;
//                    ////console.log(data[data.length - 1].overRideShifts);
                    $scope.shiftsDetailFromDB = angular.copy(data);
                    if ($scope.pId) {
                        $scope.editShift($scope.pId);
                    }
                    $scope.allShiftNames = [];
                    if ($scope.shiftsDetailFromDB !== undefined && $scope.shiftsDetailFromDB.length !== 0) {
                        for (var i = 0; i < $scope.shiftsDetailFromDB.length; i++) {
                            $scope.allShiftNames.push({label: $scope.shiftsDetailFromDB[i].shiftName});
                        }
                        for (var iterator = 0; iterator < $scope.shiftsDetailFromDB.length; iterator++) {
                            if (!!($scope.shiftsDetailFromDB[iterator].temporaryShifts) && $scope.shiftsDetailFromDB[iterator].temporaryShifts.length > 0) {
                                for (var j = 0; j < $scope.shiftsDetailFromDB[iterator].temporaryShifts.length; j++) {
                                    $scope.allShiftNames.push({label: $scope.shiftsDetailFromDB[iterator].temporaryShifts[j].tempShiftName});
                                }
                            }
                        }
                        for (var iterator = 0; iterator < $scope.shiftsDetailFromDB.length; iterator++) {
                            if (!!($scope.shiftsDetailFromDB[iterator].overRideShifts) && $scope.shiftsDetailFromDB[iterator].overRideShifts.length > 0) {
                                for (var j = 0; j < $scope.shiftsDetailFromDB[iterator].overRideShifts.length; j++) {
                                    $scope.allShiftNames.push({label: $scope.shiftsDetailFromDB[iterator].overRideShifts[j].shiftName});
                                }
                            }
                        }
                    }
                    $scope.shiftManagementForm.$setPristine();

                }, function () {
                    var msg = "Failed to retrieve shifts. Try again.";
                    var type = $rootScope.failure;
                    $rootScope.addMessage(msg, type);
                    $rootScope.unMaskLoading();
                });
                var failureForHoliday = function () {
                    var msg = "Failed to retrieve Holiday. Try again.";
                    var type = $rootScope.failure;
                    $rootScope.addMessage(msg, type);
                    $rootScope.unMaskLoading();
                };
                ShiftService.retrieveHolidayList(function (data) {
//                    $scope.list = [];
                    $scope.holidayList = data;
                    if (data !== null && angular.isDefined(data) && data.length > 0) {
                        for (var i = 0; i < $scope.holidayList.length; i++) {
                            $scope.holidayList[i].title = $rootScope.translateValue("H_NAME." + $scope.holidayList[i].title);
                        }
                    }
                    $scope.holidayListFromDb = angular.copy(data);
                    for (var i = 0; i < $scope.holidayList.length; i++) {
                        var item = $scope.holidayList[i];

                        item.type = "Holiday";
                        $scope.list.push(item);
                    }
                }, failureForHoliday);
                var failure = function () {
                    var msg = "Failed to retrieve List. Try again.";
                    var type = $rootScope.failure;
                    $rootScope.addMessage(msg, type);
                    $rootScope.unMaskLoading();
                };
                ShiftService.retrieveEventList(function (res) {
                    //                    $scope.list = [];
                    $scope.eventList = res;
                    $scope.eventListFromDb = angular.copy(res);
                    if (res !== null && angular.isDefined(res) && res.length > 0) {
                        for (var i = 0; i < $scope.eventList.length; i++) {
                            $scope.eventList[i].eventTitle = $rootScope.translateValue("EVNT_TITLE." + $scope.eventList[i].eventTitle);
                        }
                    }
                    if (res !== null && angular.isDefined(res) && res.length > 0) {
                        for (var i = 0; i < $scope.eventListFromDb.length; i++) {
                            $scope.eventListFromDb[i].startDt = $scope.eventListFromDb[i].fromDate;
                            $scope.eventListFromDb[i].endDt = $scope.eventListFromDb[i].toDate;
                            $scope.eventListFromDb[i].title = $scope.eventListFromDb[i].eventTitle;
                        }
                    }

//                    $scope.list.push({title: "-- select event --", type: "Event"});
                    for (var i = 0; i < $scope.eventList.length; i++) {
                        var item = $scope.eventList[i];
                        item.type = "Event";
                        item.title = item.eventTitle;
                        $scope.list.push(item);
                    }
                }, failure);

                $scope.departmentListOption = [];
                function convertTreeToList(node) {

                    if (node === null || node === undefined) {
                        return;
                    }
                    $scope.departmentListOption.push({"id": node.id, "displayName": node.displayName, "description": node.description, "children": node.children, "parentId": node.parentId, "parentName": node.parentName, "companyId": node.companyId, "isActive": node.isActive, "showAllDepartment": node.showAllDepartment, "addDepartmentData": node.departmentCustom});
                    if (node.children === null || node.children.length === 0) {
                        return;
                    } else {
                        for (var i = 0; i < node.children.length > 0; i++) {
                            convertTreeToList(node.children[i]);
                        }

                    }
                }
                $rootScope.unMaskLoading();
                $scope.shiftManagementForm.$dirty = false;
                if ($scope.tempShiftForm !== undefined) {
                    $scope.tempShiftForm.$dirty = false;
                }
            };
            $scope.shiftTitleExists = function (flag) {
                $scope.shiftNameInDb = false;
                $scope.shiftTitleIsNull = false;
                var count = 0;
                if (flag) {
                    $scope.shift.shiftNameFromUI = $scope.shift.shiftName;
                } else {
                    $scope.shift.shiftNameFromUI = $scope.shift.tempShiftName;
                }
                if ($scope.shift.shiftNameFromUI !== undefined && $scope.shift.shiftNameFromUI !== '' && $scope.shift.shiftNameFromUI !== null) {
                    $scope.shiftTitleIsNull = false;
                    if ($scope.shiftsDetailFromDB !== undefined && $scope.shiftsDetailFromDB.length !== 0) {
                        for (var i = 0; i < $scope.shiftsDetailFromDB.length; i++) {
                            if ($scope.shiftsDetailFromDB[i].shiftName.toUpperCase() === $scope.shift.shiftNameFromUI.toUpperCase()) {
                                if (!$scope.isEditing || $scope.temporaryShift) {
                                    $scope.shiftNameInDb = true;
                                    break;
                                }
                                if ($scope.isEditing && $scope.shift.shiftNameFromUI.toUpperCase() !== $scope.oldShiftName.toUpperCase()) {
                                    $scope.shiftNameInDb = true;
                                    break;
                                }
                            }
                            if (!$scope.shiftNameInDb) {
                                for (var iterator = 0; iterator < $scope.shiftsDetailFromDB.length; iterator++) {
                                    if (!!($scope.shiftsDetailFromDB[iterator].temporaryShifts) && $scope.shiftsDetailFromDB[iterator].temporaryShifts.length > 0) {
                                        for (var k = 0; k < $scope.shiftsDetailFromDB[iterator].temporaryShifts.length; k++) {
                                            if ($scope.shiftsDetailFromDB[iterator].temporaryShifts[k].tempShiftName.toUpperCase() === $scope.shift.shiftNameFromUI.toUpperCase()) {

                                                if (!$scope.updateButton) {
                                                    $scope.shiftNameInDb = true;
                                                    break;
                                                }
                                                if ($scope.updateButton && $scope.shift.shiftNameFromUI.toUpperCase() !== $scope.oldShiftName.toUpperCase()) {
                                                    $scope.shiftNameInDb = true;
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            if (!$scope.shiftNameInDb) {
                                for (var iterator = 0; iterator < $scope.shiftsDetailFromDB.length; iterator++) {
                                    if (!!($scope.shiftsDetailFromDB[iterator].overRideShifts) && $scope.shiftsDetailFromDB[iterator].overRideShifts.length > 0) {
                                        for (var k = 0; k < $scope.shiftsDetailFromDB[iterator].overRideShifts.length; k++) {
                                            if ($scope.shiftsDetailFromDB[iterator].overRideShifts[k].shiftName.toUpperCase() === $scope.shift.shiftNameFromUI.toUpperCase()) {

                                                if (!$scope.updateButton) {
                                                    $scope.shiftNameInDb = true;
                                                    break;
                                                }
                                                if ($scope.updateButton && $scope.shift.shiftNameFromUI.toUpperCase() !== $scope.oldShiftName.toUpperCase()) {
                                                    $scope.shiftNameInDb = true;
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                } else {
                    $scope.shiftTitleIsNull = true;
                }
            };
            if ($rootScope.configureDefaultShift) {
                ShiftService.retrieveShiftsWithDetails(function (data) {
                    $scope.list1 = [];
                    $scope.shiftsDetail = data;
                    $scope.shiftsDetailFromDB = angular.copy(data);
                    $scope.allShiftNames = [];
                    if ($scope.shiftsDetailFromDB !== undefined && $scope.shiftsDetailFromDB.length !== 0) {
                        for (var i = 0; i < $scope.shiftsDetailFromDB.length; i++) {
                            $scope.allShiftNames.push({label: $scope.shiftsDetailFromDB[i].shiftName});
                            if ($scope.shiftsDetailFromDB[i].defaultShift) {
                                $scope.defaultShift = angular.copy($scope.shiftsDetailFromDB[i]);
                                $scope.mainShiftName = $scope.defaultShift.shiftName;
                            }
                        }
                        for (var iterator = 0; iterator < $scope.shiftsDetailFromDB.length; iterator++) {
                            if (!!($scope.shiftsDetailFromDB[iterator].temporaryShifts) && $scope.shiftsDetailFromDB[iterator].temporaryShifts.length > 0) {
                                for (var j = 0; j < $scope.shiftsDetailFromDB[iterator].temporaryShifts.length; j++) {
                                    $scope.allShiftNames.push({label: $scope.shiftsDetailFromDB[iterator].temporaryShifts[j].tempShiftName});
                                }
                            }
                        }
                    }
                    if ($scope.defaultShift !== undefined && $scope.defaultShift.id !== undefined) {
                        $scope.isEditing = true;
                        $scope.shift.id = $scope.defaultShift.id;
                        $scope.oldShiftName = angular.copy($scope.defaultShift.shiftName);
                        $scope.shift = $scope.defaultShift;
                    }

                    var failureForHoliday = function () {
                        var msg = "Failed to retrieve Holiday. Try again.";
                        var type = $rootScope.failure;
                        $rootScope.addMessage(msg, type);
                        $rootScope.unMaskLoading();
                    };
                    ShiftService.retrieveHolidayList(function (data) {
                        $scope.holidayList = data;
                        if (data !== null && angular.isDefined(data) && data.length > 0) {
                            for (var i = 0; i < $scope.holidayList.length; i++) {
                                $scope.holidayList[i].title = $rootScope.translateValue("H_NAME." + $scope.holidayList[i].title);
                            }
                        }
                        $scope.holidayListFromDb = angular.copy(data);
                        for (var i = 0; i < $scope.holidayList.length; i++) {
                            var item = $scope.holidayList[i];

                            item.type = "Holiday";
                            $scope.list.push(item);
                        }
                    }, failureForHoliday);

                    var failure = function () {
                        var msg = "Failed to retrieve List. Try again.";
                        var type = $rootScope.failure;
                        $rootScope.addMessage(msg, type);
                        $rootScope.unMaskLoading();
                    };

                    ShiftService.retrieveEventList(function (res) {
                        $scope.eventList = res;
                        $scope.eventListFromDb = angular.copy(res);
                        if (res !== null && angular.isDefined(res) && res.length > 0) {
                            for (var i = 0; i < $scope.eventList.length; i++) {
                                $scope.eventList[i].eventTitle = $rootScope.translateValue("EVNT_TITLE." + $scope.eventList[i].eventTitle);
                            }
                        }
                        if (res !== null && angular.isDefined(res) && res.length > 0) {
                            for (var i = 0; i < $scope.eventListFromDb.length; i++) {
                                $scope.eventListFromDb[i].startDt = $scope.eventListFromDb[i].fromDate;
                                $scope.eventListFromDb[i].endDt = $scope.eventListFromDb[i].toDate;
                                $scope.eventListFromDb[i].title = $scope.eventListFromDb[i].eventTitle;
                            }
                        }

                        for (var i = 0; i < $scope.eventList.length; i++) {
                            var item = $scope.eventList[i];
                            item.type = "Event";
                            item.title = item.eventTitle;
                            $scope.list.push(item);
                        }
                    }, failure);
                    if ($scope.defaultShift != null) {
                        if (angular.isDefined($scope.defaultShift.shiftCustom) && $scope.defaultShift.shiftCustom != null) {
                            $scope.addShiftData = angular.copy($scope.defaultShift.shiftCustom);
                            if (!!$scope.addShiftData) {
                                angular.forEach($scope.listOfModelsOfDateType, function (listOfModel)
                                {
                                    if ($scope.addShiftData.hasOwnProperty(listOfModel))
                                    {
                                        if ($scope.addShiftData[listOfModel] !== null && $scope.addShiftData[listOfModel] !== undefined)
                                        {
                                            $scope.addShiftData[listOfModel] = new Date($scope.addShiftData[listOfModel]);
                                        } else
                                        {
                                            $scope.addShiftData[listOfModel] = '';
                                        }
                                    }
                                })
                            }

                        }
                        $scope.weekOffId = $scope.defaultShift.workingDayIds;
                        if ($scope.weekOffId) {
                            $scope.weekOffIdCode = $scope.weekOffId.split(",");
                        }
                        if ($scope.weekOffId) {
                            $.each($scope.weekDay.WeekOffList, function (index, weekDay) {
                                var flag = false;
                                if ($scope.weekOffIdCode) {
                                    $.each($scope.weekOffIdCode, function (i, selectedDay) {
                                        if (selectedDay === weekDay.key) {
                                            flag = true;
                                        }
                                    });
                                }
                                if (flag) {
                                    weekDay.isChecked = true;
                                } else {
                                    weekDay.isChecked = false;
                                }
                            });
                        }
                    }
                    if ($scope.shiftManagementForm != null) {
                        $scope.shiftManagementForm.$setPristine();
                    }
                }, function () {
                    var msg = "Failed to retrieve shifts. Try again.";
                    var type = $rootScope.failure;
                    $rootScope.addMessage(msg, type);
                    $rootScope.unMaskLoading();
                });
                ShiftService.retreiveShiftTree(function (data) {
                    $scope.ShiftDetailList = [];
                    $scope.ShiftDetailList = data;
//                    if (data !== null && angular.isDefined(data) && data.length > 0) {
//                        for (var i = 0; i < $scope.ShiftDetailList.length; i++) {
//                            $scope.ShiftDetailList[i].displayName = $rootScope.translateValue("SHIFT." + $scope.ShiftDetailList[i].displayName);
//                        }
//                    }
                    if ($scope.ShiftDetailList !== undefined && $scope.ShiftDetailList.length !== 0) {
                        $scope.updateButton = true;
                    }

                }, function () {
                    var msg = "Failed to retrieve shifts. Try again.";
                    var type = $rootScope.failure;
                    $rootScope.addMessage(msg, type);
                });
                $scope.breakNameExist = function (slot) {
                    $scope.breakNameInDb = false;
                    if (slot === undefined || slot === '' || slot === null)
                    {
                        $scope.breakNameInDb = true;
                    }
                };
            }
            $scope.addNewBreak = function () {
                if ($scope.shift.breakList === undefined) {
                    $scope.shift.breakList = [];
                }
                $scope.submitted = true;
                if ((!$scope.shiftManagementForm.breakForm) || ($scope.shiftManagementForm.breakForm !== undefined && $scope.shiftManagementForm.breakForm.$valid)) {
                    $scope.submitted = false;
                    $scope.shift.breakList.push({
                        breakSlotTitle: '',
                        breakStartTime: new Date(),
                        breakEndTime: new Date()
                    });
                }
                ////console.log("----break added---");
            };
            $scope.removeBreak = function (breakObj) {
                var i = undefined;
                $.each($scope.shift.breakList, function (index, b) {
                    if (b === breakObj) {
                        i = index;
                    }
                });
                if (i !== undefined) {
                    if ($scope.shift.breakList.length === 1) {
                        $scope.localBreak = angular.copy($scope.shift.breakList);
                    }
                    $scope.shift.breakList.splice(i, 1);
                    if ($scope.shift.breakList !== undefined && $scope.shift.breakList.length === 0) {
                        $scope.breakCountZero = true;
                        $("#passwordPopUp").modal('show');
                    }
                }
            };
            $scope.cancelBreak = function (parentShift) {
                $("#passwordPopUp").modal('hide');
                $rootScope.removeModalOpenCssAfterModalHide();
                ////console.log(parentShift);
                if (parentShift === true)
                {
                    if ($scope.localBreak != null) {
                        $scope.shift.breakList = $scope.localBreak;
                    }
                    $scope.addNewBreak();

                    ////console.log("---done--")
                } else {
                    $scope.addNewBreakForTempShift();
                    if ($scope.localBreak != null) {
                        $scope.shift.tempShiftBreakList = $scope.localBreak;
                    }
                }
            };
            $scope.noBreaks = function () {
                if (!$scope.breakCountZero && $scope.overRideShiftFlag && !$scope.isEditing) {
                    $scope.breakCountZero = true;
                    $scope.addNewOverRideShift();
                } else if (!$scope.breakCountZero && $scope.overRideShiftFlag && $scope.isEditing) {
                    $scope.breakCountZero = true;
                    $scope.saveOverRideShift();
                }
                if (!$scope.breakCountZero && !$scope.temporaryShift) {
                    $scope.breakCountZero = true;
                    $scope.addNewShift();
                }
                if (!$scope.breakCountZeroForTempShift && $scope.temporaryShift) {
                    $scope.breakCountZeroForTempShift = true;
                    $scope.addTemporaryShift();
                }
                $scope.breakCountZero = true;
                $scope.invalidBreakTime = false;
                $("#passwordPopUp").modal('hide');
                $rootScope.removeModalOpenCssAfterModalHide();
                if ($rootScope.configureDefaultShift) {
                    $('.modal-backdrop').remove();
                }

            };
            $scope.addNewBreakForTempShift = function () {
                $scope.submitted = true;
                if ((!$scope.tempShiftForm.tempBreakForm) || ($scope.tempShiftForm.tempBreakForm !== undefined && $scope.tempShiftForm.tempBreakForm.$valid)) {
                    $scope.submitted = false;
                    $scope.shift.tempShiftBreakList.push({
                        breakSlotTitle: '',
                        breakStartTime: new Date(),
                        breakEndTime: new Date()
                    });
                }
            };

            $scope.removeBreakForTempShift = function (breakObj) {
                var i = undefined;
                $.each($scope.shift.tempShiftBreakList, function (index, b) {
                    if (b === breakObj) {
                        i = index;
                    }
                });
                if (i !== undefined) {
                    if ($scope.shift.tempShiftBreakList.length === 1) {
                        $scope.localBreak = angular.copy($scope.shift.tempShiftBreakList);
                    }
                    $scope.shift.tempShiftBreakList.splice(i, 1);
                }
                if ($scope.shift.tempShiftBreakList !== undefined && $scope.shift.tempShiftBreakList.length === 0)
                {
                    $scope.breakCountZeroForTempShift = true;
                    $("#passwordPopUp").modal('show');
                }
            };

            $scope.setDefaultWorkflowRootOption = function () {
                $scope.allShiftDropdown1 = [];
                $scope.allShiftDropdown1.push({id: '0', displayName: 'None', "children": null});
                $.merge($scope.allShiftDropdown1, angular.copy($scope.dep));
            };

            $scope.iterateChild = function (childItems, addOnlyIfTrue, matchWithThis, matchItem, idRequired, both) {
                var currentitems = childItems;
                for (var i = 0; i < currentitems.length; i++) {
                    if (addOnlyIfTrue && currentitems[i].isChecked) {
                        if (matchWithThis) {
                            if (currentitems[i].id == matchItem) {
                                if (both) {
                                    $scope.selectedDepValues.push({id: currentitems[i].id, text: currentitems[i].text});
                                } else {
                                    if (idRequired) {
                                        $scope.selectedDepValues.push({id: currentitems[i].id});
                                    } else {
                                        $scope.selectedDepValues.push({text: currentitems[i].text});
                                    }
                                }
                            }
                        } else {
                            if (both) {
                                $scope.selectedDepValues.push({id: currentitems[i].id, text: currentitems[i].text});
                            } else {
                                if (idRequired) {
                                    $scope.selectedDepValues.push({id: currentitems[i].id});
                                } else {
                                    $scope.selectedDepValues.push({text: currentitems[i].text});
                                }
                            }
                        }
                    }
                    if (currentitems[i].items) {
                        $scope.iterateChild(currentitems[i].items, addOnlyIfTrue, matchWithThis, matchItem, idRequired, both);
                    }
                }
            };
            function getIdsOfExistingDepartment(node) {
                if (node === null || node === undefined) {
                    return;
                }
                $scope.existingIds.push({id: node.id});
                if (node.children === null || node.children.length === 0) {
                    return;
                } else {
                    for (var i = 0; i < node.children.length > 0; i++) {
                        getIdsOfExistingDepartment(node.children[i]);
                    }
                }
            }
            $scope.$watch('selectedItemList', function () {
                if ($scope.selectedItemList !== undefined) {
                    var finalname = [];
                    var finalSelectedArray = [];
                    $scope.shift.selectedInString = [];
                    $scope.shift.selectedInStringIds = [];
                    if ($scope.selectedItemList.items !== undefined && $scope.selectedItemList.items !== null) {
                        if ($scope.selectedItemList.items.length > 0) {
                            $scope.existingIds = [];

                            for (var i = 0; i < $scope.allShiftDropdown1.length; i++) {
                                getIdsOfExistingDepartment($scope.allShiftDropdown1[i]);
                            }
                            for (var i = 0; i < $scope.existingIds.length; i++) {
                                var depnid = $scope.existingIds[i].id;
                                $scope.selectedDepValues = [];
                                if (i === 0) {
                                    $scope.iterateChild($scope.selectedItemList.items, true, false, depnid, false, true);
                                    finalSelectedArray = angular.copy($scope.selectedDepValues);
                                }
                                $scope.selectedDepValues = [];
                                $scope.iterateChild($scope.selectedItemList.items, true, true, depnid, false, true);
                                $.merge(finalname, angular.copy($scope.selectedDepValues));
                            }
                        }
                    }
                    if (finalSelectedArray !== undefined && finalSelectedArray !== null) {
                        if (finalSelectedArray.length > 0) {
                            angular.forEach(finalSelectedArray, function (item) {
                                $scope.shift.selectedInString.push(item.text);
                                $scope.shift.selectedInStringIds.push(item.id);
                            });
                        }
                    }
                    $scope.selectedDepValues = [];
                    if (finalname.length > 0) {
                        $scope.overrideDep = finalname;
                        $('#overrideModal').modal('show');
                    }
                    if (!$scope.shift.selectedInStringIds.length > 0)
                    {
                        $scope.departmentRequired = false;
                    }
                    else {
                        $scope.departmentRequired = true;
                    }
                }
                if ($scope.shift.selectedInString !== undefined && $scope.shift.selectedInString !== null && $scope.shift.selectedInString.toString().length > 0) {
                    if ($scope.shift.selectedInString.toString().length > 25) {
                        $scope.shift.selectedInString = $scope.shift.selectedInString.toString().substr(0, 25);
                    }
                }
            });
            var uncheckAll = function (list, ary) {
                if (list !== undefined && list !== null) {
                    for (var i = 0; i < list.length; i++) {
                        var elem = list[i];
                        elem.isChecked = false;
                        if (elem.items && elem.items !== null) {
                            uncheckAll(elem.items, ary);
                        }
                    }
                }
            };
            $scope.clearData = function (flag) {
                $scope.overRideShiftFlag = false;
                $scope.reload = false;
//                $scope.addShiftData = DynamicFormService.resetSection($scope.generalShiftTemplate);
                var isTemporaryShift = angular.copy($scope.temporaryShift);
                var parentShiftId = null;
                if (isTemporaryShift) {
                    parentShiftId = angular.copy($scope.mainShiftId);
                }
                $('#scrollable-dropdown-menu.typeahead').typeahead('val', '');
                $scope.submitted = false;
                $scope.shiftManagementForm.$setPristine();
                if ($scope.tempShiftForm !== undefined) {
                    $scope.tempShiftForm.$setPristine();
                }
                $scope.searchedShift = [];
                if ($scope.selectedShift != undefined) {
                    $scope.selectedShift.selected = undefined;
                }
                if ($scope.selectedShift != undefined && $scope.selectedShift.currentNode) {
                    $scope.selectedShift.currentNode.selected = undefined;
                    $scope.selectedShift.currentNode = undefined;
                }
                if ($scope.shift.tempShiftStartTime !== undefined && $scope.shift.tempShiftStartTime !== null) {
                    $scope.shift.tempShiftStartTime = undefined;
                }
                $scope.ovrDeptNames = '';
                $scope.searchedShiftTree = undefined;
                $scope.searchPage = false;
                $scope.breakCountZero = false;
                $scope.temporaryShift = false;
                $scope.addDefaultShiftPanel = true;
                $scope.breakCountZeroForTempShift = false;
                $scope.isEditing = false;
                $scope.updateButton = false;
                $scope.shiftTitleIsNull = false;
                $scope.departmentRequired = true;
                $scope.beginitemFlag = false;
                $scope.beginsDurationFlag = false;
                $scope.beginsHolidayOrEventFlag = false;
                $scope.startDayCount = false;
                $scope.enditemFlag = false;
                $scope.endsDayCount = false;
                $scope.endsDurationFlag = false;
                $scope.shiftNameInDb = false;
                $scope.endsHolidayOrEventFlag1 = false;
                $scope.dateRangeNotValid = false;
                $scope.invalidDate = false;
                $scope.shiftTitleIsNull = false;
                $scope.invalidTime = false;
                $scope.invalidBreakTime = false;
                $scope.alreadyCreated = false;
                $scope.tempShiftNameInPopUp = '';
                $scope.ovrDeptNames = [];
                $scope.shift = {};
                $scope.shift.status = true;
                $scope.shift.breakList = [];
                $scope.listToShowBegin = [];
                $scope.listToShowEnd = [];
//                $scope.list = [];
                $scope.initializWeekday();
                $scope.initializWeekdayFortemp();
                $scope.allDaysAreSelectedAsWorking = false;
                $scope.localBreak = undefined;
                $timeout(function () {
//                    $scope.resetCustomFields();
                    $scope.reload = true;
                }, 50);
                if ($rootScope.configureDefaultShift) {
                    $rootScope.configureDefaultShift = false;
                }
                else {
                    $scope.shift.selectedInString = '';
                    $scope.shift.selectedInStringIds = undefined;

                    uncheckAll($scope.multiselecttree.items);
                    $scope.defaultSelectedids = undefined;
                }
                if (!flag && isTemporaryShift) {
                    $scope.editShift(parentShiftId);
                }
            };
            $scope.checkConditionForDays = function (code)
            {
                $scope.selectedDays = [];
                $.each($scope.weekDay.WeekOffList, function (index, day) {
                    if (day.isChecked === 'true' || day.isChecked === true) {
                        $scope.selectedDays.push(day);
                    }
                });
                if ($scope.selectedDays.length === 7) {
                    $scope.allDaysAreSelectedAsWorking = true;
                }
                else {
                    $scope.allDaysAreSelectedAsWorking = false;
                }
                if ($scope.selectedDays.length === 0) {
                    $scope.weekDay = {};
                    $scope.weekDay.WeekOffList = [{key: '2', code: 'M', value: 'Monday', isChecked: false}, {key: '3', code: 'Tu', value: 'Tuesday', isChecked: false},
                        {key: '4', code: 'W', value: 'Wednesday', isChecked: false}, {key: '5', code: 'Th', value: 'Thursday', isChecked: false}, {key: '6', code: 'F', value: 'Friday', isChecked: false},
                        {key: '7', code: 'Sa', value: 'Saturday', isChecked: false}, {key: '1', code: 'S', value: 'Sunday', isChecked: false}
                    ];
                    for (var i = 0; i < $scope.weekDay.WeekOffList.length; i++) {
                        if ($scope.weekDay.WeekOffList[i].code === code) {
                            $scope.weekDay.WeekOffList[i].isChecked = true;
                            break;
                        }
                    }
                }
            };
            $scope.checkOverlapOfTime = function (departmentIds) {
                var shiftExistForDept = false;
                var exisitingdeptIds = [];
                var ovrtempShift = [];
                var weekdays = [];
                $scope.timeClash = false;
                if ($scope.shiftsDetailFromDB !== undefined && $scope.shiftsDetailFromDB !== null && $scope.shiftsDetailFromDB.length > 0) {
                    var localShiftDetail = angular.copy($scope.shiftsDetailFromDB);
                    for (var i = 0; i < localShiftDetail.length; i++) {
                        if ($scope.shift.id && $scope.shift.id !== undefined) {
                            if ($scope.shift.id === localShiftDetail[i].id) {
                                localShiftDetail.splice(i, 1);
                            }
                        }
                    }
                    for (var day = 0; day < $scope.weekDay.WeekOffList.length; day++) {
                        if ($scope.weekDay.WeekOffList[day].isChecked === true || $scope.weekDay.WeekOffList[day].isChecked === 'true') {
                            weekdays.push(parseInt($scope.weekDay.WeekOffList[day].key));
                        }
                    }
                    var commonWorkingDay = false;
                    for (var workingDay = 0; workingDay < localShiftDetail.length; workingDay++) {
                        var workingIdArray = localShiftDetail[workingDay].workingDayIds.split(",");
                        var commonDays = intersect(weekdays, workingIdArray);
                        if (commonDays && commonDays.length > 0) {
                            commonWorkingDay = true;
                            break;
                        }
                    }
                    if (localShiftDetail && localShiftDetail.length > 0 && commonWorkingDay) {
                        for (var i = 0; i < localShiftDetail.length; i++) {
                            var arrayofId = localShiftDetail[i].departmentIds.split(",");
                            for (var j = 0; j < departmentIds.length; j++) {
                                var flag = ($.inArray(departmentIds[j] + "", arrayofId) !== -1);
                                if (flag) {
                                    shiftExistForDept = true;
                                    exisitingdeptIds.push(departmentIds[j]);
                                    ovrtempShift.push(localShiftDetail[i]);
                                }
                            }
                        }
                    }

                }
                if (shiftExistForDept) {
                    var startTime = new Date($scope.shift.shiftStartTime);
                    var endTime = new Date($scope.shift.shiftEndTime);
                    var startHour = parseInt(startTime.getHours());
                    var startMins = parseInt(startTime.getMinutes());
                    var endHour = parseInt(endTime.getHours());
                    var endMins = parseInt(endTime.getMinutes());
                    $scope.givenHourSet = [];
                    var strtHourVar = startHour;
                    var checkVar = strtHourVar;
//                    $scope.givenHourSet.push(strtHourVar);                    
                    while ((strtHourVar !== endHour)) {
                        $scope.givenHourSet.push(strtHourVar);
                        strtHourVar++;
                        if (strtHourVar > 23) {
                            strtHourVar -= 24;
                        }
                    }
                    if (strtHourVar === endHour) {
                        if (endHour > 23) {
                            endHour -= 24;
                        }
                        $scope.givenHourSet.push(endHour);
                    }
                    var currentStartTime = startHour + (startMins / 60);
                    var currentEndTime = endHour + (endMins / 60);
                    var finalovrlapShift = [];
                    for (var time = 0; time < ovrtempShift.length; time++) {
                        var startTimeInLoop = new Date(ovrtempShift[time].shiftStartTime);
                        var endTimeInLoop = new Date(ovrtempShift[time].shiftEndTime);
                        var startHourInLoop = parseInt(startTimeInLoop.getHours());
                        var startMinsInLoop = parseInt(startTimeInLoop.getMinutes());
                        var endHourInLoop = parseInt(endTimeInLoop.getHours());
                        var endMinsInLoop = parseInt(endTimeInLoop.getMinutes());
                        var currentStartTimeInLoop = startHourInLoop + (startMinsInLoop / 60);
                        var currentEndTimeInLoop = endHourInLoop + (endMinsInLoop / 60);
                        var a = currentStartTimeInLoop;
                        var b = currentEndTimeInLoop;
                        var c = currentStartTime;
                        var d = currentEndTime;
                        $scope.dbHourSet = [];
                        $scope.dbMinSet = [];
                        var strtHourVarInLoop = startHourInLoop;
                        var checkVarInLoop = strtHourVarInLoop;
//                        $scope.dbHourSet.push(strtHourVarInLoop);
                        while ((strtHourVarInLoop !== endHourInLoop)) {
                            $scope.dbHourSet.push(strtHourVarInLoop);
                            strtHourVarInLoop++;
                            if (strtHourVarInLoop > 23) {
                                strtHourVarInLoop -= 24;
                            }
                        }
                        if (strtHourVarInLoop === endHourInLoop) {
                            if (strtHourVarInLoop > 23) {
                                strtHourVarInLoop -= 24;
                            }
                            $scope.dbHourSet.push(strtHourVarInLoop);
                        }
                        var result = intersect($scope.givenHourSet, $scope.dbHourSet);
                        $scope.timeClash = false;
                        $scope.givenMinSet = [];
                        if (result.length > 2) {
                            $scope.timeClash = true;
                            finalovrlapShift.push(ovrtempShift[time]);
                            break;
                        }
                        if (result.length === 2 && !$scope.timeClash) {
                            if (startHour === endHourInLoop && endHour === startHourInLoop) {

                                if (startMins >= endMinsInLoop && endMins <= startMinsInLoop) {

                                } else {// clash
                                    $scope.timeClash = true;
                                    finalovrlapShift.push(ovrtempShift[time]);
                                    break;
                                }
                            } else {
                                $scope.timeClash = true;
                                finalovrlapShift.push(ovrtempShift[time]);
                                break;
                            }
                        }
                        if (result.length === 1 && !$scope.timeClash) {
                            console.log("startHour" + startHour);
                            console.log("endHourInLoop" + endHourInLoop)
                            console.log("-----------------------------");
                            console.log("startHourInLoop" + startHourInLoop);
                            console.log("endHour" + endHour);
                            if (startHour === endHourInLoop) {
                                if (startMins < endMinsInLoop) {
                                    $scope.timeClash = true;
                                    finalovrlapShift.push(ovrtempShift[time]);
                                    break;
                                }
                            }

                            else if (startHourInLoop === endHour) {
                                if (endMins > startMinsInLoop && !$scope.timeClash) {
                                    $scope.timeClash = true;
                                    finalovrlapShift.push(ovrtempShift[time]);
                                    break;
                                }
                            }
                        }
                    }
                    if ($scope.timeClash) {
                        $scope.finalIds = [];
                        for (var i = 0; i < finalovrlapShift.length; i++) {
                            var arrayofId = finalovrlapShift[i].departmentIds.split(",");
                            for (var j = 0; j < exisitingdeptIds.length; j++) {
                                var flag = $.inArray(exisitingdeptIds[j].toString(), arrayofId) !== -1;
                                if (flag) {
                                    var idFound = $.inArray(exisitingdeptIds[j], $scope.finalIds) !== -1;
                                    if (!idFound) {
                                        $scope.finalIds.push(exisitingdeptIds[j]);
                                    }
                                }
                            }
                        }
                        $scope.listOfDeptIds = $scope.finalIds;
                        var tempList = angular.copy($scope.multiselecttree);
                        $scope.ovrDeptNames = [];
                        getDepartmentName(tempList.items, $scope.listOfDeptIds);
                        $scope.ovrDeptNamesString = '';
                        $scope.ovrDeptNamesString = $scope.ovrDeptNames.toString();
                    }

                }
//                console.log($scope.timeClash);
            };

            function intersect(a, b) {
                var d = {};
                var results = [];
                for (var i = 0; i < b.length; i++) {
                    d[parseInt(b[i])] = true;
                }
                for (var j = 0; j < a.length; j++) {
                    if (d[a[j]])
                        results.push(a[j]);
                }
                return results;
            }
            ;

            $scope.checkOvrlapOfDate = function () {
                $scope.alreadyCreated = false;
                var parentShiftId = '';
                if ($scope.shift.id) {
                    parentShiftId = $scope.shift.id;
                } else {
                    parentShiftId = $scope.mainShiftId;
                }
                if (parentShiftId) {
                    var paretShift = [];
                    for (var i = 0; i < $scope.shiftsDetailFromDB.length; i++) {
                        if (parentShiftId === $scope.shiftsDetailFromDB[i].id) {
                            paretShift.push($scope.shiftsDetailFromDB[i])
                            break;
                        }
                    }
                    var listOfTempShift = [];
                    for (var i = 0; i < paretShift.length; i++) {
                        if (paretShift[i].temporaryShifts) {
                            for (var tempShift = 0; tempShift < paretShift[i].temporaryShifts.length; tempShift++) {
                                listOfTempShift.push(paretShift[i].temporaryShifts);
                            }
                            for (tempShift = 0; tempShift < paretShift[i].temporaryShifts.length; tempShift++) {
                                var tempo = paretShift[i].temporaryShifts[tempShift];
                                if ($scope.shift.tempShiftId && $scope.shift.tempShiftId !== undefined) {
                                    if ($scope.shift.tempShiftId === paretShift[i].temporaryShifts[tempShift].tempShiftId) {
                                        paretShift[i].temporaryShifts.splice(tempShift, 1);
                                        var tempo = paretShift[i].temporaryShifts[tempShift];
                                    }
                                }

                                if (tempo !== undefined) {
                                    var c = new Date(tempo.tempShiftFromDate);
                                    var d = new Date(tempo.tempShiftEndDate);
                                    var a = new Date($scope.fromDateForTempShit);
                                    var b = new Date($scope.toDateForTempShit);
                                    var flag = ((a < c && c < b) || (a < d && d < b));
                                    if ((a < c || a > d) && (b < c || b > d) && (b > a) && (!flag)) {
                                    } else {
                                        $scope.alreadyCreated = true;
                                    }
                                }
                            }
                        }
                    }
                }
            };
            var getDepartmentName = function (list, ary) {
                if (list !== undefined && list !== null) {
                    for (var i = 0; i < list.length; i++) {
                        var elem = list[i];
                        if ($.inArray(elem.id + "", ary) !== -1 || $.inArray(elem.id, ary) !== -1) {
                            $scope.ovrDeptNames.push(elem.text);
                        }
                        if (elem.items && elem.items !== null) {
                            getDepartmentName(elem.items, ary);
                        }
                    }
                }
            };
            $scope.addNewShift = function (flag, shiftManagementForm) {
//                shiftManagementForm = $scope.shiftManagementForm;
                ////console.log("--->>>>>>>>>here---!!!!!!!!!!!!!!");
                $scope.reload = false;
                $scope.submitted = true;
                if ($rootScope.configureDefaultShift) {
                    if (!$scope.shift.shiftStartTime) {
                        $scope.shift.shiftStartTime = new Date();
                    }
                    if (!$scope.shift.shiftEndTime) {
                        $scope.shift.shiftEndTime = new Date();
                    }
                }
                var tempStartTime = new Date($scope.shift.shiftStartTime);
                var tempEndTime = new Date($scope.shift.shiftEndTime);
                var startHours = tempStartTime.getHours();
                //console.log(tempStartTime.getHours());
                var startHoursTocompare = angular.copy(startHours);
                var endHours = tempEndTime.getHours();
                var startMins = tempStartTime.getMinutes();
                var endMins = tempEndTime.getMinutes();
                $scope.validBreakTimeSlot = true;
                $scope.invalidTime = false;
                if (startHours === endHours && startMins === endMins) {
                    $scope.invalidTime = true;
                }
                $scope.invalidBreakTime = false;
                var mainTimeSet = [];
                var mainTimeMinSet = [];
                if ($scope.shift.breakList !== undefined && $scope.shift.breakList.length > 0 && !$scope.invalidTime) {
                    ////console.log("11111111111"+$scope.shift.breakList.length);
//                    mainTimeSet.push(startHours);
                    while ((startHoursTocompare !== endHours)) {
                        mainTimeSet.push(startHoursTocompare);
                        startHoursTocompare++;
                        if (startHoursTocompare > 23) {
                            startHoursTocompare -= 24;
                        }
                    }
                    if (startHoursTocompare === endHours) {
                        if (startHoursTocompare > 23) {
                            startHoursTocompare -= 24;
                        }
                        mainTimeSet.push(startHoursTocompare);
                    }
                    for (var i = 0; i < $scope.shift.breakList.length; i++) {
                        var tempBreakStartTime = new Date($scope.shift.breakList[i].breakStartTime);
                        var tempBreakEndTime = new Date($scope.shift.breakList[i].breakEndTime);
                        var breakStartHours = tempBreakStartTime.getHours();
                        //console.log(tempBreakStartTime.getHours());

                        var breakStartHoursTocompare = angular.copy(breakStartHours);
                        var breakEndHours = tempBreakEndTime.getHours();
                        var breakStartMins = tempBreakStartTime.getMinutes();
                        var breakEndMins = tempBreakEndTime.getMinutes();
                        var breakSet = [];
//                        breakSet.push(breakStartHoursTocompare);
                        while ((breakStartHoursTocompare !== breakEndHours)) {
                            breakSet.push(breakStartHoursTocompare);
                            breakStartHoursTocompare++;
                            if (breakStartHoursTocompare > 23) {
                                breakStartHoursTocompare -= 24;
                            }
                        }
                        if (breakStartHoursTocompare === breakEndHours) {
                            if (breakStartHoursTocompare > 23) {
                                breakStartHoursTocompare -= 24;
                            }
                            breakSet.push(breakStartHoursTocompare);
                        }
                        console.log(mainTimeSet);
                        console.log(breakSet);
                        for (var breakTime = 0; breakTime < breakSet.length; breakTime++) {
//                            //console.log("break invalid 11111" + JSON.stringify(mainTimeSet) + "break setttt" + JSON.stringify(breakSet));
                            if (mainTimeSet.indexOf(breakSet[breakTime]) === -1) {
//                                console.log("break invalid 11111" + JSON.stringify(mainTimeSet) + "break setttt" + JSON.stringify(breakSet));
                                $scope.invalidBreakTime = true;
                                break;
                            } else {
                                //console.log("break invalid 11111" + JSON.stringify(mainTimeSet) + "break setttt" + JSON.stringify(breakSet));
                            }
                        }
                        if ($scope.invalidBreakTime) {
                            break;
                        } else {
                            if (breakStartHours === startHours) {
                                if (breakStartMins <= startMins) {
                                    //console.log("break invalid 2222");
                                    $scope.invalidBreakTime = true;
                                    break;
                                }
                            }
                            if (breakEndHours === endHours) {
                                if (breakEndMins >= endMins) {
                                    //console.log("break invalid 333");
                                    $scope.invalidBreakTime = true;
                                    break;
                                }
                            }
                            if (breakEndHours === breakStartHours) {
                                if (breakStartMins >= breakEndMins) {
                                    //console.log("break invalid 4444");
                                    $scope.invalidBreakTime = true;
                                    break;
                                }
                            }

                            if (breakStartHours === startHours && (breakEndHours === endHours) && (startHours === endHours)) {
                                if (breakStartMins <= startMins || breakEndMins >= endMins || breakStartMins >= endMins || breakEndMins <= startMins) {
                                    //console.log("break invalid 5555");
                                    $scope.invalidBreakTime = true;
                                    break;
                                }
                            }
                        }
                        if (!$scope.invalidBreakTime) {
                            for (var innerLoop = 0; innerLoop < i; innerLoop++) {
                                var tempBreakStartTimeinnerLoop = new Date($scope.shift.breakList[innerLoop].breakStartTime);
                                var tempBreakEndTimeinnerLoop = new Date($scope.shift.breakList[innerLoop].breakEndTime);
                                var breakStartHoursinnerLoop = tempBreakStartTimeinnerLoop.getHours();
                                var breakEndHoursinnerLoop = tempBreakEndTimeinnerLoop.getHours();
                                var breakStartMinsinnerLoop = tempBreakStartTimeinnerLoop.getMinutes();
                                var breakEndMinsinnerLoop = tempBreakEndTimeinnerLoop.getMinutes();
                                var endM = parseInt(breakEndMinsinnerLoop);
                                var endH = parseInt(breakEndHoursinnerLoop);
                                var b = endH + (endM / 60);
                                var StartM = parseInt(breakStartMinsinnerLoop);
                                var StartH = parseInt(breakStartHoursinnerLoop);
                                var a = StartH + (StartM / 60);
                                var outerStartH = parseInt(breakStartHours);
                                var outerStartM = parseInt(breakStartMins);
                                var c = outerStartH + (outerStartM / 60);
                                var outerEndH = parseInt(breakEndHours);
                                var outerEndM = parseInt(breakEndMins);
                                var d = outerEndH + (outerEndM / 60);
                                if (a !== undefined && b !== undefined && c !== undefined && d !== undefined) {
                                    var flag = ((a < c && c < b) || (a < d && d < b));
                                    if ((a < c || a > d) && (b < c || b > d) && (!flag)) {
                                    } else {
                                        //console.log("break invalid 66666");
                                        $scope.invalidBreakTime = true;
                                    }
                                }
                            }
                        }
                    }
                }
                $scope.selectedDays = [];
                $.each($scope.weekDay.WeekOffList, function (index, day) {
                    if (day.isChecked === 'true' || day.isChecked === true) {
                        $scope.selectedDays.push(day);
                    }
                });
                $scope.weekOffId = '';
                for (var i in $scope.selectedDays) {
                    var id = $scope.selectedDays[i].key;
                    if (id !== 'undefined' || id !== null || id !== '') {
                        if ($scope.weekOffId === '') {
                            $scope.weekOffId = id;
                        }
                        else {
                            $scope.weekOffId += ',' + id;
                        }
                    }
                }
                ////console.log("=======for completed")
                var createShiftJson = {};
                $scope.departmentRequired = false;
                if ($scope.shift.shiftName === undefined || $scope.shift.shiftName === null || $scope.shift.shiftName === '') {
                    $scope.shiftTitleIsNull = true;
                }
                if ($rootScope.configureDefaultShift || $scope.shift.defaultShift) {
                    $scope.departmentRequired = true;
                }
                else {
                    if ($scope.shift.selectedInStringIds !== undefined && $scope.shift.selectedInStringIds.length > 0) {
                        createShiftJson.departmentIds = $scope.shift.selectedInStringIds.toString();
                        createShiftJson.defaultShift = false;
                        $scope.departmentRequired = true;
                    }
                    else {
                        $scope.departmentRequired = false;
                    }
                }
                $scope.breakNameInDb = false;
                ////console.log("2222222222");
                if ($scope.shiftManagementForm.$valid && $scope.shift.shiftStartTime !== undefined && $scope.shift.shiftEndTime !== undefined) {
                    ////console.log("33333");
                    createShiftJson = {
                        status: $scope.shift.status
                    };
                    if ((createShiftJson.status || !$scope.isEditing) && !$scope.breakCountZero && $scope.shift.breakList !== undefined && $scope.shift.breakList.length === 0) {
                        $("#passwordPopUp").modal('show');
                        return;
                    }
                    if ($rootScope.configureDefaultShift && $scope.shift.breakList === undefined) {
                        $("#passwordPopUp").modal('show');
                        $scope.shift.breakList = [];
                        return;
                    }
                    if (!$scope.shiftNameInDb && $scope.selectedDays.length !== 0 && !$scope.breakNameInDb && $scope.departmentRequired && !$scope.invalidTime && !$scope.invalidBreakTime
                            && ((!createShiftJson.status && $scope.shift.id !== undefined) || (($scope.breakCountZero && $scope.shift.breakList !== undefined && $scope.shift.breakList.length === 0) || ($scope.shift.breakList !== undefined && $scope.shift.breakList.length > 0)))) {
                        if (!$rootScope.configureDefaultShift) {
                            ////console.log("4444");
                            $scope.checkOverlapOfTime($scope.shift.selectedInStringIds);
                        }
                        ////console.log("55555");
                        var res = $scope.timeClash;
                        createShiftJson = {
                            shiftName: $scope.shift.shiftName,
                            shiftStartTime: $scope.shift.shiftStartTime,
                            shiftEndTime: $scope.shift.shiftEndTime,
                            workingDayIds: $scope.weekOffId,
                            breakList: $scope.shift.breakList,
                            status: $scope.shift.status
                        };
                        if (!$rootScope.configureDefaultShift) {
                            createShiftJson.departmentIds = $scope.shift.selectedInStringIds.toString();
                        } else {
                            createShiftJson.departmentIds = '0';
                            createShiftJson.defaultShift = true;
                        }
                        createShiftJson.shiftCustom = $scope.addShiftData;
                        createShiftJson.shiftDbType = $scope.dbType;
                        if (!$scope.isEditing && res) {
                            ovelap = createShiftJson;
                            $scope.overridePopUpp();
                        }
                        else if ((!$scope.isEditing && !res) || ($rootScope.configureDefaultShift && !$scope.isEditing)) {
                            ShiftService.create(createShiftJson, function () {
//                                $scope.resetCustomFields();
                                $scope.reload = true;
                                $scope.clearData();
                                if (!$rootScope.configureDefaultShift) {
                                    $scope.retrieveAllDepartments();
                                }
                                $scope.addShiftData = {};
                                $rootScope.unMaskLoading();
                            }, function () {
//                                $scope.resetCustomFields();
                                $scope.reload = true;
                                $scope.addShiftData = {};
                                var msg = "Could not create shift, please try again.";
                                var type = $rootScope.error;
                                $rootScope.addMessage(msg, type);
                                $rootScope.unMaskLoading();
                            });
                        } else if ($scope.isEditing) {
                            createShiftJson.id = $scope.shift.id;
                            if (!createShiftJson.status && !$rootScope.configureDefaultShift) {
                                ShiftService.userExisrForShift($scope.shift.id, function (res) {
                                    $scope.useresExist = false;
                                    if (res.data) {
                                        $scope.useresExist = true;
                                    } else {
                                        var ids = $scope.shift.departmentIds.split(",");
                                        $scope.ovrDeptNames = [];
                                        getDepartmentName($scope.multiselecttree.items, ids);
                                        $scope.deptNameInPopUp = $scope.ovrDeptNames.toString();
                                        $scope.tempShiftNotAvailable = false;
                                        if ($scope.shift.temporaryShifts !== undefined && $scope.shift.temporaryShifts !== null) {
                                            var shiftName = [];
                                            for (var tempShiftName = 0; tempShiftName < $scope.shift.temporaryShifts.length; tempShiftName++) {
                                                var name = $scope.shift.temporaryShifts[tempShiftName];
                                                shiftName.push(name.tempShiftName);
                                            }
                                            $scope.shiftNameInPopUp = '';
                                            $scope.shiftNameInPopUp = shiftName.toString();
                                        }
                                        else {
                                            $scope.shiftNameInPopUp = '';
                                            $scope.shiftNameInPopUp = $scope.shift.shiftName;
                                            $scope.tempShiftNotAvailable = true;
                                        }
                                    }
                                });
                                $scope.update = createShiftJson;
                                $scope.showConfirmationPopup();
                            } else if ((!res && $scope.isEditing) || $rootScope.configureDefaultShift) {
                                $rootScope.maskLoading();
                                ShiftService.update(createShiftJson, function () {
                                    $scope.clearData();
                                    $scope.retrieveAllDepartments();
                                    $scope.addShiftData = {};
                                    $rootScope.unMaskLoading();

                                }, function () {
                                    //Failure Call back
                                    var msg = "Could not update shift, please try again.";
                                    var type = $rootScope.error;
                                    $rootScope.addMessage(msg, type);
                                    $rootScope.unMaskLoading();
                                });
                            } else if (res && $scope.isEditing && createShiftJson.status) {
                                ovelap = createShiftJson;
                                $scope.overridePopUpp();
                            }
                        }

                    }
                }
//                $scope.resetCustomFields();
                $scope.reload = true;
            };
            $scope.removeMainShift = function (Flag) {
                if (Flag) {
                    ShiftService.update($scope.update, function () {
                        $scope.hideConfirmationPopup();
                        $scope.clearData();
                        $scope.retrieveAllDepartments();
                    }, function () {
                        var msg = "Could not update shift, please try again.";
                        var type = $rootScope.error;
                        $rootScope.addMessage(msg, type);
                    });
                }
            };
            $scope.removeTempShift = function (Flag) {
                if (Flag) {
                    $rootScope.maskLoading();
                    $scope.pId = $scope.mainShiftId;
                    ShiftService.updateTemporaryShift($scope.updateTempShift, function () {
                        $scope.hideConfirmationPopup();
                        $scope.clearData();
                        $scope.retrieveAllDepartments();
//                        $scope.editShift(pId);
                        $rootScope.unMaskLoading();
                    }, function () {
                        var msg = "Could not update shift, please try again.";
                        var type = $rootScope.error;
                        $rootScope.addMessage(msg, type);
                        $rootScope.unMaskLoading();
                    });
                }
            };
            $scope.overridePopUpp = function () {
                $("#overridePopUp").modal('show');
            };
            $scope.hideOverridePopUpp = function () {
                $("#overridePopUp").modal('hide');
                $rootScope.removeModalOpenCssAfterModalHide();
            };
            $scope.showConfirmationPopup = function () {
                $("#confirmationPopUp").modal('show');
            };
            $scope.hideConfirmationPopup = function () {
                $scope.shiftManagementForm.$dirty = false;
                $scope.update = {};
                $scope.tempShiftNameInPopUp = undefined;
                if ($scope.shift.tempShiftStatus !== undefined) {
                    $scope.shift.tempShiftStatus = true;
                }
                if ($scope.shift.status !== undefined) {
                    $scope.shift.status = true;
                }
                $("#confirmationPopUp").modal('hide');
                $rootScope.removeModalOpenCssAfterModalHide();
                $('.modal-backdrop').remove();

            };
            $scope.showFieldsForTempShift = function (flag) {
                $scope.submitted = false;
                $scope.shift.tempShiftBreakList = [];
                $scope.addTempShift = false;
                $scope.temporaryShift = true;
                $scope.addDefaultShiftPanel = false;
                $scope.alreadyCreated = false;
                $scope.dateRangeNotValid = false;
                $scope.invalidDate = false;
                $scope.alreadyCreated = false;
                $scope.beginitemFlag = false;
                $scope.beginsDurationFlag = false;
                $scope.beginsHolidayOrEventFlag = false;
                $scope.startDayCount = false;
                $scope.enditemFlag = false;
                $scope.endsDayCount = false;
                $scope.endsDurationFlag = false;
                $scope.endsHolidayOrEventFlag1 = false;
                $scope.invalidDate = false;
                $scope.alreadyCreated = false;
                if ($scope.tempShiftForm !== undefined) {
                    $scope.tempShiftForm.$setPristine();
                }
                if (flag) {
                    $scope.shift.setRule = true;
//                    $scope.shift.tempShiftStartTime = new Date();
//                    $scope.shift.tempShiftEndTime = new Date();
                    $scope.begins.beginsDuration = '';
                    $scope.begins.beginsHolidayOrEvent = '';
                    $scope.begins.beginitem = '';
                    $scope.begins.endsDuration = '';
                    $scope.begins.endsHolidayOrEvent = '';
                    $scope.begins.enditem = '';
                    $scope.listToShowEnd = [];
                    $scope.listToShowBegin = [];
                    $scope.shift.tempShiftFromDate = null;
                    $scope.shift.tempShiftEndDate = null;
                }
                //                $scope.list.push({title: "--pick holiday--", type: "Holiday"});
                $scope.initializWeekdayFortemp();
            };

            $scope.showFieldsForOverrideShift = function (flag) {
                if (flag) {
                    $scope.departmentIds = undefined;
                    if ($scope.shift.selectedInStringIds !== undefined && $scope.shift.selectedInStringIds !== null && $scope.shift.selectedInStringIds.length > 0) {
                        $scope.departmentIds = angular.copy($scope.shift.selectedInStringIds.toString());
                    }
                    $scope.clearData();
                    if ($rootScope.configureDefaultShift) {
                        $rootScope.configureDefaultShift = true;
                    }
                    $scope.overRideShiftFlag = true;
//                    $scope.initializWeekdayFortemp();
                }
            };
            $scope.onTypeChangeBegin = function () {
                var selection = $scope.begins.beginsHolidayOrEvent;
                $scope.begins.beginitem = undefined;
                $scope.listToShowBegin = [];
                if (selection === undefined || selection === null) {
                    $scope.beginsHolidayOrEventFlag = true;
                } else {
                    $scope.beginsHolidayOrEventFlag = false;
                    //                    $scope.listToShowBegin.push({title: "--pick holiday--", type: "Holiday"});
                    for (var i = 0; i < $scope.list.length; i++) {
                        var item = $scope.list[i];
                        if (item.type === selection) {
                            $scope.listToShowBegin.push(item);
                        }
                    }
                }
                if ($scope.listToShowBegin && $scope.listToShowBegin.length === 0) {
                    $scope.listToShowBegin.push({title: "Not available", type: selection});
                }
            };
            $scope.onTypeChangeEnd = function () {
                $scope.begins.enditem = undefined;
                var selection = $scope.begins.endsHolidayOrEvent;
                $scope.listToShowEnd = [];
                if (selection === undefined || selection === null) {
                    $scope.endsHolidayOrEventFlag1 = true;
//                    $scope.listToShowEnd.push({title: "Select"});
                } else {
                    $scope.listToShowEnd = [];
                    $scope.endsHolidayOrEventFlag1 = false;
                    for (var i = 0; i < $scope.list.length; i++) {
                        var item = $scope.list[i];
                        if (item.type === selection) {
                            $scope.listToShowEnd.push(item);
                        }
                    }
                }
                if ($scope.listToShowEnd && $scope.listToShowEnd.length === 0) {
                    $scope.listToShowEnd.push({title: "Not available", type: selection});
                }
            };
            $scope.checkCountForBegins = function (dayCount) {
                if ($scope.shift.setRule) {
                    $scope.startDayCount = true;
                    if ($scope.shift.beginsDayCount) {
                        $scope.startDayCount = false;
                    }
                }
            };
            $scope.checkCountForEnds = function () {
                if ($scope.shift.setRule) {
                    $scope.endsDayCount = true;
                    if ($scope.shift.endsDayCount) {
                        $scope.endsDayCount = false;
                    }
                }
            };
            $scope.checkDurationForEnd = function (Flag) {
                if ($scope.shift.setRule) {
                    $scope.endsDurationFlag = true;
                    if ($scope.begins.endsDuration) {
                        $scope.endsDurationFlag = false;
                    }
                }
            };
            $scope.checkDurationForStart = function () {
                $scope.beginsDurationFlag = true;
                if ($scope.begins.beginsDuration !== undefined && $scope.begins.beginsDuration !== null) {
                    $scope.beginsDurationFlag = false;
                }
            };
            $scope.endItemChanged = function () {
                $scope.enditemFlag = true;
                if ($scope.begins.enditem !== '--pick holiday--' && $scope.begins.enditem !== '--pick event--') {
                    $scope.enditemFlag = false;
                }
            };
            $scope.startItemChanged = function () {
                $scope.beginitemFlag = true;
                if ($scope.begins.beginitem !== '--pick holiday--' && $scope.begins.beginitem !== '--pick event--') {
                    $scope.beginitemFlag = false;
                }
            };
            $scope.checkConditionForDaysForTempShift = function (code) {
                $scope.shift.selectedDaysForTempShift = [];
                $.each($scope.shift.WeekOffListForTempShift, function (index, day) {
                    if (day.isChecked === 'true' || day.isChecked === true) {
                        $scope.shift.selectedDaysForTempShift.push(day);
                    }
                });
                if ($scope.shift.selectedDaysForTempShift.length === 7) {
                    $scope.allDaysAreSelectedAsWorkingTempShift = true;
                }
                else {
                    $scope.allDaysAreSelectedAsWorkingTempShift = false;
                }
                if ($scope.shift.selectedDaysForTempShift.length === 0) {
                    $scope.shift.WeekOffListForTempShift = [{key: '2', code: 'M', value: 'Monday', isChecked: false}, {key: '3', code: 'Tu', value: 'Tuesday', isChecked: false},
                        {key: '4', code: 'W', value: 'Wednesday', isChecked: false}, {key: '5', code: 'Th', value: 'Thursday', isChecked: false}, {key: '6', code: 'F', value: 'Friday', isChecked: false},
                        {key: '7', code: 'Sa', value: 'Saturday', isChecked: false}, {key: '1', code: 'S', value: 'Sunday', isChecked: false}
                    ];
                    for (var i = 0; i < $scope.shift.WeekOffListForTempShift.length; i++) {
                        if ($scope.shift.WeekOffListForTempShift[i].code === code) {
                            $scope.shift.WeekOffListForTempShift[i].isChecked = true;
                            break;
                        }
                    }
                }
            };
            $scope.addTemporaryShift = function () {
                ////console.log("here");
                $scope.reload = false;
                $scope.shiftTitleIsNull = true;
                if ($scope.shift.tempShiftName) {
                    $scope.shiftTitleIsNull = false;
                }

                $scope.shift.beginsEventOrHolidayId = '';
                $scope.shift.endsHolidayOrEventId = '';
                $scope.shift.selectedDaysForTempShift = [];
                $.each($scope.shift.WeekOffListForTempShift, function (index, day) {
                    if (day.isChecked === 'true' || day.isChecked === true) {
                        $scope.shift.selectedDaysForTempShift.push(day);
                    }
                });
                $scope.shift.weekOffIdForTempShift = '';
                for (var i in $scope.shift.selectedDaysForTempShift) {
                    var id = $scope.shift.selectedDaysForTempShift[i].key;
                    if (id !== undefined && id !== null && id !== '') {
                        if ($scope.shift.weekOffIdForTempShift === '') {
                            $scope.shift.weekOffIdForTempShift = id;
                        }
                        else {
                            $scope.shift.weekOffIdForTempShift += ',' + id;
                        }
                    }
                }
                $scope.invalidDate = false;
                if ($scope.shift.setRule === true) {
                    $scope.dateRangeNotValid = false;
                    if (!($scope.begins.beginsDuration !== undefined && $scope.begins.beginsDuration.length > 0)) {
                        $scope.beginsDurationFlag = true;
                    }
                    if (!($scope.begins.beginsHolidayOrEvent !== undefined && $scope.begins.beginsHolidayOrEvent.length > 0)) {
                        $scope.beginsHolidayOrEventFlag = true;
                    }
                    if (!($scope.begins.beginitem !== undefined && $scope.begins.beginitem.length > 0 && $scope.begins.beginitem !== 'Not available')) {
                        $scope.beginitemFlag = true;
                    }

                    if (!($scope.begins.endsDuration !== undefined && $scope.begins.endsDuration.length > 0)) {
                        $scope.endsDurationFlag = true;
                    }
                    if (!($scope.begins.endsHolidayOrEvent !== undefined && $scope.begins.endsHolidayOrEvent.length > 0)) {
                        $scope.endsHolidayOrEventFlag1 = true;
                    }
                    if (!($scope.begins.enditem !== undefined && $scope.begins.enditem.length > 0 && $scope.begins.enditem !== 'Not available')) {
                        $scope.enditemFlag = true;
                    }

                    if (($scope.shift.beginsDayCount !== undefined && $scope.shift.beginsDayCount !== null)) {
                        $scope.startDayCount = false;
                        var countbegin = parseInt($scope.shift.beginsDayCount);
                    }
                    else {
                        $scope.startDayCount = true;
                    }
                    if ($scope.shift.endsDayCount !== undefined && $scope.shift.endsDayCount !== null) {
                        $scope.endsDayCount = false;
                        var countend = parseInt($scope.shift.endsDayCount);
                    } else {
                        $scope.endsDayCount = true;
                    }
                    if ($scope.begins.endsHolidayOrEvent !== undefined && $scope.begins.endsHolidayOrEvent !== null) {
                        if ($scope.begins.endsHolidayOrEvent.charAt(0) === 'h' || $scope.begins.endsHolidayOrEvent.charAt(0) === 'H') {
                            for (var i = 0; i < $scope.holidayListFromDb.length; i++) {
                                if ($scope.holidayListFromDb[i].title === $scope.begins.enditem) {
                                    $scope.enditemToSent = $scope.holidayListFromDb[i];
                                }
                            }
                        }
                        else {
                            for (var i = 0; i < $scope.eventListFromDb.length; i++) {
                                if ($scope.eventListFromDb[i].title === $scope.begins.enditem) {
                                    $scope.enditemToSent = $scope.eventListFromDb[i];
                                }
                            }
                        }
                    }
                    if ($scope.begins.beginsHolidayOrEvent !== undefined && $scope.begins.beginsHolidayOrEvent !== null) {
                        if ($scope.begins.beginsHolidayOrEvent.charAt(0) === 'h' || $scope.begins.beginsHolidayOrEvent.charAt(0) === 'H') {
                            for (var i = 0; i < $scope.holidayListFromDb.length; i++) {
                                if ($scope.holidayListFromDb[i].title === $scope.begins.beginitem) {
                                    $scope.beginitemToSent = $scope.holidayListFromDb[i];
                                }
                            }
                        }
                        else {
                            for (var i = 0; i < $scope.eventListFromDb.length; i++) {
                                if ($scope.eventListFromDb[i].title === $scope.begins.beginitem) {
                                    $scope.beginitemToSent = $scope.eventListFromDb[i];
                                }
                            }
                        }
                    }
                    if ($scope.begins.beginsDuration !== undefined && $scope.begins.beginsDuration.length > 0) {
                        if ($scope.listToShowBegin !== undefined && $scope.listToShowBegin.length > 0) {
                            if ($scope.begins.beginsDuration.charAt(0) === 'A' || $scope.begins.beginsDuration.charAt(0) === 'a') {
                                $scope.fromDateForTempShit = $scope.beginitemToSent.endDt + (countbegin * 24 * 3600 * 1000);
                            }
                            else {
                                $scope.fromDateForTempShit = $scope.beginitemToSent.startDt - (countbegin * 24 * 3600 * 1000);
                            }
                        }
                    }
                    if ($scope.begins.endsDuration !== undefined && $scope.begins.endsDuration.length > 0) {
                        if ($scope.listToShowEnd !== undefined && $scope.listToShowEnd.length > 0) {
                            if ($scope.begins.endsDuration.charAt(0) === 'A' || $scope.begins.endsDuration.charAt(0) === 'a') {
                                $scope.toDateForTempShit = $scope.enditemToSent.endDt + (countend * 24 * 3600 * 1000);
                            }
                            else {
                                $scope.toDateForTempShit = $scope.enditemToSent.startDt - (countend * 24 * 3600 * 1000);
                            }
                        }
                    }
                }
                else {
                    $scope.beginitemFlag = false;
                    $scope.beginsDurationFlag = false;
                    $scope.beginsHolidayOrEventFlag = false;
                    $scope.startDayCount = false;
                    $scope.enditemFlag = false;
                    $scope.endsDayCount = false;
                    $scope.endsDurationFlag = false;
                    $scope.endsHolidayOrEventFlag1 = false;
                    $scope.invalidDate = false;
                    $scope.dateRangeNotValid = false;
                    if ($scope.shift.tempShiftFromDate !== undefined && $scope.shift.tempShiftFromDate !== null) {
                        $scope.fromDateForTempShit = new Date($scope.shift.tempShiftFromDate);
                    } else {
                        $scope.dateRangeNotValid = true;
                    }
                    if ($scope.shift.tempShiftEndDate !== undefined && $scope.shift.tempShiftEndDate !== null) {
                        $scope.toDateForTempShit = new Date($scope.shift.tempShiftEndDate);
                    } else {
                        $scope.dateRangeNotValid = true;
                    }
                }
                if ($scope.shift.setRule === false) {
                    $scope.fromDateForTempShit = $scope.fromDateForTempShit.getTime();
                    $scope.toDateForTempShit = $scope.toDateForTempShit.getTime();
                }
                if ($scope.fromDateForTempShit > $scope.toDateForTempShit || new Date($scope.fromDateForTempShit) < new Date().setHours(0, 0, 0, 0)) {
                    $scope.invalidDate = true;
                }
                var tempStartTime = new Date($scope.shift.tempShiftStartTime);
                var tempEndTime = new Date($scope.shift.tempShiftEndTime);
                var startHours = tempStartTime.getHours();
                var endHours = tempEndTime.getHours();
                var startMins = tempStartTime.getMinutes();
                var endMins = tempEndTime.getMinutes();
                $scope.validBreakTimeSlot = true;
                $scope.invalidTime = false;
                if (startHours === endHours && startMins === endMins) {
                    $scope.invalidTime = true;
                }
                $scope.invalidBreakTime = false;
                if ($scope.shift.tempShiftBreakList !== undefined && $scope.shift.tempShiftBreakList.length > 0 && !$scope.invalidTime) {
                    for (var i = 0; i < $scope.shift.tempShiftBreakList.length; i++) {
                        var tempBreakStartTime = new Date($scope.shift.tempShiftBreakList[i].breakStartTime);
                        var tempBreakEndTime = new Date($scope.shift.tempShiftBreakList[i].breakEndTime);
                        var breakStartHours = tempBreakStartTime.getHours();
                        var breakEndHours = tempBreakEndTime.getHours();
                        var breakStartMins = tempBreakStartTime.getMinutes();
                        var breakEndMins = tempBreakEndTime.getMinutes();
                        if (breakStartHours === breakEndHours && breakStartMins === breakEndMins) {
                            $scope.invalidBreakTime = true;
                        }
                        else if (breakStartHours === startHours && breakStartMins === startMins) {
                            $scope.invalidBreakTime = true;
                        } else if (breakEndHours === endHours && breakEndMins === endMins) {
                            $scope.invalidBreakTime = true;
                        }
                        else {
                            if (startHours < endHours) {
                                if (breakStartHours < startHours || breakStartHours > endHours || breakEndHours < startHours || breakEndHours > endHours) {
                                    $scope.invalidBreakTime = true;
                                }
                                else if ((breakEndHours < breakStartHours) || (breakStartHours === breakEndHours && breakEndMins < breakStartMins) || (breakStartHours === breakEndHours && breakEndMins < breakStartMins)) {
                                    $scope.invalidBreakTime = true;
                                }
                                else if ((breakStartHours === startHours && breakStartMins < startMins) || (breakEndHours === endHours && breakEndMins > endMins) || (breakStartHours === endHours && breakEndMins > endMins)) {
                                    $scope.invalidBreakTime = true;
                                }

                            }
                            else {
                                if ((breakStartHours < startHours && breakStartHours > endHours) || (breakEndHours < startHours && breakEndHours > endHours)) {
                                    $scope.invalidBreakTime = true;
                                } else if ((breakStartHours === breakEndHours && breakEndMins < breakStartMins)) {
                                    $scope.invalidBreakTime = true;
                                }
                                else if ((breakStartHours === startHours && breakStartMins < startMins) || (breakEndHours === endHours && breakEndMins > endMins) || (breakStartHours === endHours && breakEndMins > endMins)) {
                                    $scope.invalidBreakTime = true;
                                }
                                else if ((breakStartHours > breakEndHours && breakEndHours > endHours)) {
                                    $scope.invalidBreakTime = true;
                                }
                            }
                        }
                        if (!$scope.invalidBreakTime) {
                            for (var innerLoop = 0; innerLoop < i; innerLoop++) {
                                var tempBreakStartTimeinnerLoop = new Date($scope.shift.tempShiftBreakList[innerLoop].breakStartTime);
                                var tempBreakEndTimeinnerLoop = new Date($scope.shift.tempShiftBreakList[innerLoop].breakEndTime);
                                var breakStartHoursinnerLoop = tempBreakStartTimeinnerLoop.getHours();
                                var breakEndHoursinnerLoop = tempBreakEndTimeinnerLoop.getHours();
                                var breakStartMinsinnerLoop = tempBreakStartTimeinnerLoop.getMinutes();
                                var breakEndMinsinnerLoop = tempBreakEndTimeinnerLoop.getMinutes();
                                var endM = parseInt(breakEndMinsinnerLoop);
                                var endH = parseInt(breakEndHoursinnerLoop);
                                var b = endH + (endM / 60);
                                var StartM = parseInt(breakStartMinsinnerLoop);
                                var StartH = parseInt(breakStartHoursinnerLoop);
                                var a = StartH + (StartM / 60);
                                var outerStartH = parseInt(breakStartHours);
                                var outerStartM = parseInt(breakStartMins);
                                var c = outerStartH + (outerStartM / 60);
                                var outerEndH = parseInt(breakEndHours);
                                var outerEndM = parseInt(breakEndMins);
                                var d = outerEndH + (outerEndM / 60);
                                var flag = ((a < c && c < b) || (a < d && d < b));
                                if (!((a < c || a > d) && (b < c || b > d) && (b > a) && (!flag))) {
                                    $scope.invalidBreakTime = true;
                                    break;
                                }
                            }
                        }
                    }
                }
                var createTemporaryShiftJson = {};
                createTemporaryShiftJson = {
                    tempShiftStatus: $scope.shift.tempShiftStatus
                };

                $scope.submitted = true;
                if (($scope.fromDateForTempShit <= $scope.toDateForTempShit)) {
                    //                    $scope.checkOvrlapOfDate();
                    if (!$scope.alreadyCreated && !$scope.invalidTime && !$scope.invalidBreakTime && !$scope.shiftNameInDb && !$scope.invalidDate && !$scope.beginitemFlag && !$scope.enditemFlag && $scope.tempShiftForm.$valid) {
                        if ((createTemporaryShiftJson.tempShiftStatus || $scope.shift.tempShiftId === undefined) && !$scope.breakCountZeroForTempShift && $scope.shift.tempShiftBreakList !== undefined && $scope.shift.tempShiftBreakList.length === 0) {
                            $("#passwordPopUp").modal('show');
                        }
                        if (((!createTemporaryShiftJson.tempShiftStatus && $scope.shift.tempShiftId !== undefined) || (($scope.breakCountZeroForTempShift && $scope.shift.tempShiftBreakList !== undefined && $scope.shift.tempShiftBreakList.length === 0) || ($scope.shift.tempShiftBreakList !== undefined && $scope.shift.tempShiftBreakList.length > 0)))) {
                            createTemporaryShiftJson = {
                                //                    departmentIdsForTempShift: $scope.shift.selectedInStringIds.toString(),
                                parentShiftId: $scope.shift.id,
                                tempShiftName: $scope.shift.tempShiftName,
                                tempShiftStartTime: $scope.shift.tempShiftStartTime,
                                tempShiftEndTime: $scope.shift.tempShiftEndTime,
                                tempShiftBreakList: $scope.shift.tempShiftBreakList,
                                tempShiftWorkingDayIds: $scope.shift.weekOffIdForTempShift,
                                tempShiftFromDate: $scope.shift.tempShiftFromDate,
                                tempShiftEndDate: $scope.shift.tempShiftEndDate,
                                setRule: $scope.shift.setRule,
                                beginRuleId: $scope.shift.beginRuleId,
                                endRuleId: $scope.shift.endRuleId,
                                dateRangeRuleId: $scope.shift.dateRangeRuleId,
                                tempShiftStatus: $scope.shift.tempShiftStatus
                            };
                            if ($scope.shift.setRule === true) {
                                createTemporaryShiftJson.beginsDayCount = $scope.shift.beginsDayCount;
                                createTemporaryShiftJson.beginsAction = $scope.begins.beginsDuration.charAt(0);
                                createTemporaryShiftJson.beginsEventType = $scope.begins.beginsHolidayOrEvent;
                                createTemporaryShiftJson.beginsEventOrHolidayId = $scope.beginitemToSent.id;
                                createTemporaryShiftJson.endsDayCount = $scope.shift.endsDayCount;
                                createTemporaryShiftJson.endsAction = $scope.begins.endsDuration.charAt(0);
                                createTemporaryShiftJson.endsEventType = $scope.begins.endsHolidayOrEvent;
                                createTemporaryShiftJson.endsEventOrHolidayId = $scope.enditemToSent.id;
                                createTemporaryShiftJson.tempShiftFromDate = new Date($scope.fromDateForTempShit);
                                createTemporaryShiftJson.tempShiftEndDate = new Date($scope.toDateForTempShit);
                            }
                            ;
                            if ($scope.shift.tempShiftId !== undefined && $scope.shift.tempShiftId !== null) {
                                createTemporaryShiftJson.tempShiftId = $scope.shift.tempShiftId;
                                if (!$scope.shift.tempShiftStatus) {
                                    ShiftService.userExisrForShift($scope.shift.tempShiftId, function (res) {
//                                        $scope.resetCustomFields();
                                        $scope.reload = true;
                                        $scope.useresExist = false;
                                        if (res.data) {
                                            $scope.useresExist = true;
                                        } else {
                                            $scope.tempShiftNotAvailable = true;
                                            $scope.shift.id = $scope.shift.tempShiftId.parentShiftId;
                                            $scope.ovrDeptNames = [];
                                            $scope.tempShiftNameInPopUp = undefined;
                                            $scope.tempShiftNameInPopUp = createTemporaryShiftJson.tempShiftName;
                                            for (var i = 0; i < $scope.shiftsDetailFromDB.length; i++) {
                                                if ($scope.mainShiftId === $scope.shiftsDetailFromDB[i].id) {
                                                    var deptList = $scope.shiftsDetailFromDB[i].departmentIds.split(",");
                                                    var tempList = angular.copy($scope.multiselecttree);
                                                    getDepartmentName(tempList.items, deptList);
                                                }
                                            }
                                            $scope.deptNameInPopUp = '';
                                            $scope.deptNameInPopUp = $scope.ovrDeptNames.toString();
                                        }
                                    });

                                    createTemporaryShiftJson.tempShiftId = $scope.shift.tempShiftId;
                                    $scope.updateTempShift = createTemporaryShiftJson;
                                    $scope.showConfirmationPopup();
                                } else {
//                                    $scope.checkOvrlapOfDate();
                                    $rootScope.maskLoading();
                                    $scope.pId = $scope.mainShiftId;
                                    ShiftService.updateTemporaryShift(createTemporaryShiftJson, function () {
//                                        $scope.resetCustomFields();
                                        $scope.reload = true;
                                        $scope.hideConfirmationPopup();
                                        $scope.clearData();
                                        $scope.retrieveAllDepartments();
//                                        $scope.editShift(pId);
                                        $rootScope.unMaskLoading();
                                    }, function () {
                                        var msg = "Could not update shift, please try again.";
                                        var type = $rootScope.error;
                                        $rootScope.addMessage(msg, type);
                                        $rootScope.unMaskLoading();
                                    });
                                }
                            } else {
                                $rootScope.maskLoading();
                                $scope.pId = $scope.mainShiftId;
                                ShiftService.createTemporaryShift(createTemporaryShiftJson, function () {
//                                    $scope.resetCustomFields();
                                    $scope.reload = true;
                                    $scope.addShiftData = {};
                                    $scope.shiftManagementForm.$setPristine();
//                                    $scope.resetCustomFields();
                                    $scope.reload = true;
                                    $scope.clearData();
                                    $scope.retrieveAllDepartments();
                                    $scope.temporaryShift = false;
                                    $scope.addDefaultShiftPanel = true;
//                                    $scope.editShift(pId);
                                    $rootScope.unMaskLoading();
                                    //Success Call Back

                                }, function () {
                                    //Failure Call back
                                    var msg = "Could not create temporary shift, please try again.";
                                    var type = $rootScope.error;
                                    $rootScope.addMessage(msg, type);
                                    $rootScope.unMaskLoading();
                                });
                            }
//                            $scope.resetCustomFields();
                            $scope.reload = true;
                        }
                    }
                }

            };

            var checkElementsTrue = function (list, ary) {
                if (list !== undefined && list !== null) {
                    for (var i = 0; i < list.length; i++) {
                        var elem = list[i];
                        if ($.inArray(elem.id + "", ary) !== -1 || $.inArray(elem.id, ary) !== -1) {
                            elem.isChecked = true;
                        } else {
                            elem.isChecked = false;
                        }
                        if (angular.isDefined(elem.items) && elem.items !== null) {
                            checkElementsTrue(elem.items, ary);
                        }
                    }
                }
            };

            $scope.editShift = function (selectedShiftfromUI) {
                if (angular.isDefined($scope.shiftManagementForm)) {
                    $scope.shiftManagementForm.$setPristine();
                }
                $scope.reload = false;
//                $scope.addShiftData = DynamicFormService.resetSection($scope.generalShiftTemplate);
                var selectedShift = {currentNode: {id: ''}};
                $scope.searchedShiftTree = undefined;
                if (selectedShiftfromUI.id) {
                    selectedShift.currentNode.id = selectedShiftfromUI.id;
                    $scope.searchedShiftTree = selectedShiftfromUI.id;
                } else if (selectedShiftfromUI.currentNode) {
                    selectedShift.currentNode.id = selectedShiftfromUI.currentNode.id;
                    $scope.searchedShiftTree = selectedShiftfromUI.currentNode.id;
                    $('#scrollable-dropdown-menu.typeahead').typeahead('val', '');
                } else if (selectedShiftfromUI.parentShiftId) {
                    selectedShift.currentNode.id = selectedShiftfromUI.parentShiftId;
                    $scope.searchedShiftTree = selectedShiftfromUI.parentShiftId;
                }
                else if (selectedShiftfromUI) {
                    selectedShift.currentNode.id = selectedShiftfromUI;
                    $scope.searchedShiftTree = selectedShiftfromUI;
                }
                $scope.allDaysAreSelectedAsWorking = false;
                $scope.invalidTime = false;
                if (selectedShift.currentNode.id !== undefined && selectedShift.currentNode.id !== null) {
                    $scope.shiftsDetail = angular.copy($scope.shiftsDetailFromDB);
                    $scope.shiftNameInDb = false;
                    $scope.shiftTitleIsNull = false;
                    $scope.searchPage = false;
                    if ($scope.shiftsDetail !== undefined && $scope.shiftsDetail.length !== 0) {
                        var found = false;
                        $scope.isEditing = true;
                        for (var i = 0; i < $scope.shiftsDetail.length; i++) {
                            if ($scope.shiftsDetail[i].id === selectedShift.currentNode.id) {
                                $scope.overRideShiftFlag = false;
//                                $scope.addShiftData = DynamicFormService.resetSection($scope.generalShiftTemplate);
                                if (!angular.isDefined($scope.shiftsDetail[i].shiftCustom) || $scope.shiftsDetail[i].shiftCustom == null || $scope.shiftsDetail[i].shiftCustom == undefined) {
                                    var primaryKey = {primaryKey: parseInt(selectedShift.currentNode.id)};
                                    ShiftService.retrieveCustomFieldDataById(primaryKey, function (customData) {
                                        if (customData.shiftCustom != null) {
                                            $scope.shiftsDetail[i].shiftCustom = angular.copy(customData.shiftCustom);

//                                            $scope.resetCustomFields();
                                            $scope.addShiftData = angular.copy(customData.shiftCustom);
                                            if (!!$scope.addShiftData) {
                                                angular.forEach($scope.listOfModelsOfDateType, function (listOfModel)
                                                {
                                                    if ($scope.addShiftData.hasOwnProperty(listOfModel))
                                                    {
                                                        if ($scope.addShiftData[listOfModel] !== null && $scope.addShiftData[listOfModel] !== undefined)
                                                        {
                                                            $scope.addShiftData[listOfModel] = new Date($scope.addShiftData[listOfModel]);
                                                        } else
                                                        {
                                                            $scope.addShiftData[listOfModel] = '';
                                                        }
                                                    }
                                                })
                                            }
                                            $scope.reload = true;
                                        }
                                    }, function () {
                                        var msg = "Failed to retrieve custom field. Try again.";
                                        var type = $rootScope.failure;
                                        $rootScope.addMessage(msg, type);
                                        $rootScope.unMaskLoading();
                                    });
                                }
                                $scope.mainShiftName = $scope.shiftsDetailFromDB[i].shiftName;
                                found = true;
                                $scope.mainShiftId = selectedShift.currentNode.id;
                                $scope.shift = {};
                                $scope.shift = $scope.shiftsDetail[i];
                                $scope.oldShiftName = $scope.shift.shiftName;
                                $scope.shift.selectedInStringIds = $scope.shiftsDetail[i].departmentIds.split(",");
                                //                                $scope.defaultSelectedids = undefined;
                                $scope.defaultSelectedids = $scope.shift.selectedInStringIds;
                                //                                ////console.log("default ids" + $scope.defaultSelectedids.toString());
                                var tempList = angular.copy($scope.multiselecttree);
                                checkElementsTrue(tempList.items, $scope.shift.selectedInStringIds);
                                $scope.selectedItemList = tempList;
                                if ($scope.shiftsDetail[i].parentShiftId === undefined) {
                                    $scope.weekOffIdCode = $scope.shiftsDetail[i].workingDayIds.split(",");
                                    $.each($scope.weekDay.WeekOffList, function (index, weekDay) {
                                        var flag = false;
                                        $.each($scope.weekOffIdCode, function (i, selectedDay) {
                                            if (selectedDay === weekDay.key) {
                                                flag = true;
                                            }
                                        });
                                        if (flag) {
                                            weekDay.isChecked = true;
                                        } else {
                                            weekDay.isChecked = false;
                                        }
                                    });
                                    $scope.updateButton = true;
                                    $scope.temporaryShift = false;
                                    $scope.addDefaultShiftPanel = true;
                                    break;
                                }
                                break;
                            }
                        }
                        if (found === false) {
                            for (var iterator = 0; iterator < $scope.shiftsDetail.length; iterator++) {
                                if ($scope.shiftsDetail[iterator].temporaryShifts !== undefined && $scope.shiftsDetail[iterator].temporaryShifts !== null && $scope.shiftsDetail[iterator].temporaryShifts.length > 0) {
                                    for (var itr = 0; itr < $scope.shiftsDetail[iterator].temporaryShifts.length; itr++) {
                                        if ($scope.shiftsDetail[iterator].temporaryShifts[itr].tempShiftId === selectedShift.currentNode.id) {
                                            $scope.showFieldsForTempShift(false);
                                            $scope.overRideShiftFlag = false;
                                            $scope.addTempShift = true;
                                            $scope.shift = {};
                                            $scope.mainShiftId = $scope.shiftsDetail[iterator].temporaryShifts[itr].parentShiftId;
                                            for (var mainshift = 0; mainshift < $scope.shiftsDetailFromDB.length; mainshift++) {
                                                if ($scope.mainShiftId === $scope.shiftsDetailFromDB[mainshift].id)
                                                    $scope.mainShiftName = $scope.shiftsDetailFromDB[mainshift].shiftName;
                                            }

                                            $scope.shift = $scope.shiftsDetail[iterator].temporaryShifts[itr];
                                            $scope.oldShiftName = $scope.shift.tempShiftName;
                                            if (!$scope.shift.setRule) {
                                                $scope.begins.beginsDuration = '';
                                                $scope.begins.beginsHolidayOrEvent = '';
                                                $scope.begins.beginitem = '';
                                                $scope.begins.endsDuration = '';
                                                $scope.begins.endsHolidayOrEvent = '';
                                                $scope.begins.enditem = '';
                                                $scope.listToShowEnd = [];
                                                $scope.listToShowBegin = [];
                                                $scope.shift.tempShiftEndDate = new Date($scope.shift.tempShiftEndDate);
                                                $scope.shift.tempShiftFromDate = new Date($scope.shift.tempShiftFromDate);
                                                $scope.minToDate = new Date($scope.shift.tempShiftFromDate);
                                            }
                                            else {
                                                $scope.shift.tempShiftFromDate = null;
                                                $scope.shift.tempShiftEndDate = null;
                                            }
                                            if ($scope.shift.setRule) {
                                                if ($scope.shift.beginsAction === 'a' || $scope.shift.beginsAction === 'A') {
                                                    $scope.begins.beginsDuration = 'after';
                                                } else {
                                                    $scope.begins.beginsDuration = 'before';
                                                }
                                                if ($scope.shift.endsAction === 'a' || $scope.shift.endsAction === 'A') {
                                                    $scope.begins.endsDuration = 'after'
                                                } else {
                                                    $scope.begins.endsDuration = 'before'
                                                }
                                                if ($scope.shift.beginsEventType === 'h' || $scope.shift.beginsEventType === 'H') {
                                                    $scope.begins.beginsHolidayOrEvent = 'Holiday';
                                                    for (var i = 0; i < $scope.holidayListFromDb.length; i++) {
                                                        if ($scope.holidayListFromDb[i].id === $scope.shift.beginsEventOrHolidayId) {
                                                            $scope.listToShowBegin = [];
                                                            $scope.begins.beginitem = $scope.holidayListFromDb[i].title;
                                                            for (var i = 0; i < $scope.list.length; i++) {
                                                                var item = $scope.list[i];
                                                                if (item.type === 'Holiday') {
                                                                    $scope.listToShowBegin.push(item);
                                                                }
                                                            }
                                                        }
                                                    }
                                                } else {
                                                    $scope.begins.beginsHolidayOrEvent = 'Event';
                                                    for (var i = 0; i < $scope.eventListFromDb.length; i++) {
                                                        if ($scope.eventListFromDb[i].id === $scope.shift.beginsEventOrHolidayId)
                                                        {
                                                            $scope.listToShowBegin = [];
                                                            $scope.begins.beginitem = $scope.eventListFromDb[i].title;
                                                            for (var i = 0; i < $scope.list.length; i++) {
                                                                var item = $scope.list[i];
                                                                if (item.type === 'Event') {
                                                                    $scope.listToShowBegin.push(item);
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                                if ($scope.shift.endsEventType === 'h' || $scope.shift.endsEventType === 'H') {
                                                    $scope.begins.endsHolidayOrEvent = 'Holiday'
                                                    for (var i = 0; i < $scope.holidayListFromDb.length; i++) {
                                                        if ($scope.holidayListFromDb[i].id === $scope.shift.endsEventOrHolidayId)
                                                        {
                                                            $scope.begins.enditem = $scope.holidayListFromDb[i];
                                                            $scope.listToShowEnd = [];
                                                            $scope.begins.enditem = $scope.holidayListFromDb[i].title;
                                                            for (var i = 0; i < $scope.list.length; i++) {
                                                                var item = $scope.list[i];
                                                                if (item.type === 'Holiday') {
                                                                    $scope.listToShowEnd.push(item);

                                                                }
                                                            }
                                                        }
                                                    }
                                                } else {
                                                    $scope.begins.endsHolidayOrEvent = 'Event'
                                                    for (var i = 0; i < $scope.eventListFromDb.length; i++) {
                                                        if ($scope.eventListFromDb[i].id === $scope.shift.endsEventOrHolidayId)
                                                        {

                                                            $scope.listToShowEnd = [];
                                                            $scope.begins.enditem = $scope.eventListFromDb[i].title;
                                                            for (var i = 0; i < $scope.list.length; i++) {
                                                                var item = $scope.list[i];
                                                                if (item.type === 'Event') {
                                                                    $scope.listToShowEnd.push(item);
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                            $scope.shift.selectedInString = [];
                                            $scope.shift.selectedInString = $scope.shiftsDetail[iterator].temporaryShifts[itr].departmentIds;
                                            $scope.temporaryShift = true;
                                            $scope.updateButton = true;
                                            $scope.addDefaultShiftPanel = false;
                                            $scope.shift.weekOffIdForTempShift = $scope.shiftsDetail[iterator].temporaryShifts[itr].tempShiftWorkingDayIds.split(",");
                                            $scope.initializWeekdayFortemp();
                                            $.each($scope.shift.WeekOffListForTempShift, function (index, weekDay) {
                                                var flag = false;
                                                $.each($scope.shift.weekOffIdForTempShift, function (i, selectedDay) {
                                                    if (selectedDay === weekDay.key) {
                                                        flag = true;
                                                    }
                                                });
                                                if (flag) {
                                                    weekDay.isChecked = true;
                                                } else {
                                                    weekDay.isChecked = false;
                                                }
                                            });
                                            found = true;
                                            break;
                                        }
                                    }
//                                    }
                                }
                            }


                        }
                        if (found === false) {
                            for (var iterator = 0; iterator < $scope.shiftsDetail.length; iterator++) {
                                if ($scope.shiftsDetail[iterator].overRideShifts !== undefined && $scope.shiftsDetail[iterator].overRideShifts !== null && $scope.shiftsDetail[iterator].overRideShifts.length > 0) {
                                    for (var itr = 0; itr < $scope.shiftsDetail[iterator].overRideShifts.length; itr++) {
                                        if ($scope.shiftsDetail[iterator] !== undefined && $scope.shiftsDetail[iterator].overRideShifts[itr].id === selectedShift.currentNode.id) {
//                                            ////console.log($scope.shiftsDetail[iterator].overRideShifts[itr]);
                                            $scope.overRideShiftFlag = true;
                                            $scope.ovrShiftId = selectedShift.currentNode.id;
//                                            ////console.log($scope.shiftsDetail[iterator]);
                                            if (!angular.isDefined($scope.shiftsDetail[iterator].overRideShifts[itr].shiftCustom) || $scope.shiftsDetail[iterator].overRideShifts[itr].shiftCustom == null || $scope.shiftsDetail[iterator].overRideShifts[itr].shiftCustom == undefined) {
//                                                ////console.log($scope.shiftsDetail[iterator]);
                                                var primaryKey = {primaryKey: parseInt(selectedShift.currentNode.id)};
                                                ShiftService.retrieveCustomFieldDataById(primaryKey, function (customData) {
//                                                    ////console.log(customData.shiftCustom);
                                                    if (Object.getOwnPropertyNames(customData.shiftCustom).length > 0) {
//                                                        ////console.log($scope.shiftsDetail[iterator].overRideShifts[itr]);
                                                        $scope.shiftsDetail[iterator].overRideShifts[itr].shiftCustom = angular.copy(customData.shiftCustom);

//                                            $scope.resetCustomFields();
                                                        $scope.addShiftData = angular.copy(customData.shiftCustom);
                                                        if (!!$scope.addShiftData) {
                                                            angular.forEach($scope.listOfModelsOfDateType, function (listOfModel)
                                                            {
                                                                if ($scope.addShiftData.hasOwnProperty(listOfModel))
                                                                {
                                                                    if ($scope.addShiftData[listOfModel] !== null && $scope.addShiftData[listOfModel] !== undefined)
                                                                    {
                                                                        $scope.addShiftData[listOfModel] = new Date($scope.addShiftData[listOfModel]);
                                                                    } else
                                                                    {
                                                                        $scope.addShiftData[listOfModel] = '';
                                                                    }
                                                                }
                                                            })
                                                        }
                                                        $scope.reload = true;
                                                    }
                                                }, function () {
                                                    var msg = "Failed to retrieve custom field. Try again.";
                                                    var type = $rootScope.failure;
                                                    $rootScope.addMessage(msg, type);
                                                    $rootScope.unMaskLoading();
                                                });
                                            }
//                                            $scope.mainShiftName = $scope.shiftsDetailFromDB[i].shiftName;
                                            found = true;
                                            $scope.mainShiftId = selectedShift.currentNode.id;
                                            $scope.shift = {};
                                            $scope.shift = $scope.shiftsDetail[iterator].overRideShifts[itr];
                                            $scope.shift.status = true;
                                            $scope.oldShiftName = $scope.shift.shiftName;
                                            $scope.shift.selectedInStringIds = $scope.shiftsDetail[iterator].overRideShifts[itr].departmentIds.split(",");
                                            $scope.departmentIds = angular.copy($scope.shift.selectedInStringIds.toString());
                                            //                                $scope.defaultSelectedids = undefined;
                                            $scope.defaultSelectedids = $scope.shift.selectedInStringIds;
                                            //                                ////console.log("default ids" + $scope.defaultSelectedids.toString());
                                            var tempList = angular.copy($scope.multiselecttree);
                                            checkElementsTrue(tempList.items, $scope.shift.selectedInStringIds);
                                            $scope.selectedItemList = tempList;
                                            if ($scope.shiftsDetail[iterator].overRideShifts[itr].parentShiftId === undefined) {
                                                $scope.weekOffIdCode = $scope.shiftsDetail[iterator].overRideShifts[itr].workingDayIds.split(",");
                                                $.each($scope.weekDay.WeekOffList, function (index, weekDay) {
                                                    var flag = false;
                                                    $.each($scope.weekOffIdCode, function (i, selectedDay) {
                                                        if (selectedDay === weekDay.key) {
                                                            flag = true;
                                                        }
                                                    });
                                                    if (flag) {
                                                        weekDay.isChecked = true;
                                                    } else {
                                                        weekDay.isChecked = false;
                                                    }
                                                });
                                                $scope.updateButton = true;
                                                $scope.temporaryShift = false;
                                                $scope.addDefaultShiftPanel = true;
                                                break;
                                            }
                                            break;
                                        }
                                    }
//                                    }
                                }
                            }
                        }

//                        }
                    }
                }
            };
            $scope.hstep = 1;
            $scope.mstep = 1;
            //remove options
            $scope.options = {
                hstep: [1, 2, 3],
                mstep: [1, 5, 10, 15, 25, 30]
            };
            $scope.ismeridian = true;
            $scope.today = function () {
                $scope.dt = new Date();
            };
            $scope.today();
            $scope.showWeeks = true;
            $scope.toggleWeeks = function () {
                $scope.showWeeks = !$scope.showWeeks;
            };
            $scope.clear = function () {
                $scope.dt = null;
            };
            $scope.disabled = function (date, mode) {
                return (mode === 'day' && (date.getDay() === 0));
            };
            $scope.toggleMin = function () {
                $scope.minDate = ($scope.minDate) ? null : new Date();
                var currentYear = new Date();
                $scope.maxDate = ($scope.maxDate) ? null : new Date(currentYear.getFullYear(), 11, 31);
            };
            $scope.toggleMin();
            $scope.setMinDate = function () {
                if ($scope.shift.tempShiftFromDate === "" || $scope.shift.tempShiftFromDate === undefined) {
                    $scope.dateRangeNotValid = false;
                    $scope.minToDate = new Date();
                } else {
                    $scope.dateRangeNotValid = false;
                    $scope.minToDate = $scope.shift.tempShiftFromDate;
                    if (!$scope.isEdit) {
                        //                        $scope.shift.tempShiftEndDate = $scope.shift.tempShiftFromDate;
                    }
                }
                if ($scope.shift.tempShiftFromDate > $scope.shift.tempShiftEndDate) {
                    $scope.shift.tempShiftEndDate = $scope.shift.tempShiftFromDate;
                }

            };
            $scope.setMinDate();
            $scope.datePicker = {};
            $scope.open = function ($event, opened) {
                $event.preventDefault();
                $event.stopPropagation();
                $scope.datePicker[opened] = true;
            };
            $scope.dateOptions = {
                'year-format': "'yy'",
                'starting-day': 1
            };
            $scope.formats = ['dd-MMMM-yyyy', 'yyyy/MM/dd', 'shortDate'];
            $scope.format = $rootScope.dateFormat;

            $scope.setRuleChanged = function ()
            {
                if ($scope.shift.setRule) {
                    $scope.dateRangeNotValid = false;
                    $scope.invalidDate = false;
                    $scope.alreadyCreated = false;
                }
                else {
                    $scope.beginitemFlag = false;
                    $scope.beginsDurationFlag = false;
                    $scope.beginsHolidayOrEventFlag = false;
                    $scope.startDayCount = false;
                    $scope.enditemFlag = false;
                    $scope.endsDayCount = false;
                    $scope.endsDurationFlag = false;
                    $scope.endsHolidayOrEventFlag1 = false;
                    $scope.invalidDate = false;
                    $scope.alreadyCreated = false;
                }
            };
            $scope.createOsverrideShift = function () {
                $rootScope.maskLoading();
                ShiftService.create(ovelap, function () {
                    $scope.reload = true;
                    $scope.addShiftData = {};
                    $scope.clearData();
                    $scope.retrieveAllDepartments();
                    $scope.hideOverridePopUpp();
                    $rootScope.unMaskLoading();

                }, function () {
                    var msg = "Could not create shift, please try again.";
                    var type = $rootScope.error;
                    $rootScope.addMessage(msg, type);
                    $rootScope.unMaskLoading();
                });
            };
            $scope.updateOsverrideShift = function () {
                $rootScope.maskLoading();
                ShiftService.update(ovelap, function () {
                    $scope.reload = true;
                    $scope.addShiftData = {};
                    $scope.clearData();
                    $scope.retrieveAllDepartments();
                    $scope.hideOverridePopUpp();
                    $rootScope.unMaskLoading();

                }, function () {
                    var msg = "Could not update shift, please try again.";
                    var type = $rootScope.error;
                    $rootScope.addMessage(msg, type);
                    $rootScope.unMaskLoading();
                });
            };

            $scope.addNewOverRideShift = function (flag, shiftManagementForm) {
//                ////console.log($scope.shift.selectedInString);
                $scope.reload = false;
                $scope.submitted = true;
                if ($rootScope.configureDefaultShift) {
                    if (!$scope.shift.shiftStartTime) {
                        $scope.shift.shiftStartTime = new Date();
                    }
                    if (!$scope.shift.shiftEndTime) {
                        $scope.shift.shiftEndTime = new Date();
                    }
                }
                var tempStartTime = new Date($scope.shift.shiftStartTime);
                var tempEndTime = new Date($scope.shift.shiftEndTime);
                var startHours = tempStartTime.getHours();
                var startHoursTocompare = angular.copy(startHours);
                var endHours = tempEndTime.getHours();
                var startMins = tempStartTime.getMinutes();
                var endMins = tempEndTime.getMinutes();
                $scope.validBreakTimeSlot = true;
                $scope.invalidTime = false;
                if (startHours === endHours && startMins === endMins) {
                    $scope.invalidTime = true;
                }
                $scope.invalidBreakTime = false;
                var mainTimeSet = [];
                var mainTimeMinSet = [];
                if ($scope.shift.breakList !== undefined && $scope.shift.breakList.length > 0 && !$scope.invalidTime) {
//                    mainTimeSet.push(startHours);
                    while ((startHoursTocompare !== endHours)) {
                        mainTimeSet.push(startHoursTocompare);
                        startHoursTocompare++;
                        if (startHoursTocompare > 23) {
                            startHoursTocompare -= 24;
                        }
                    }
                    for (var i = 0; i < $scope.shift.breakList.length; i++) {
                        var tempBreakStartTime = new Date($scope.shift.breakList[i].breakStartTime);
                        var tempBreakEndTime = new Date($scope.shift.breakList[i].breakEndTime);
                        var breakStartHours = tempBreakStartTime.getHours();
                        var breakStartHoursTocompare = angular.copy(breakStartHours);
                        var breakEndHours = tempBreakEndTime.getHours();
                        var breakStartMins = tempBreakStartTime.getMinutes();
                        var breakEndMins = tempBreakEndTime.getMinutes();
                        var breakSet = [];
//                        breakSet.push(breakStartHoursTocompare);
                        while ((breakStartHoursTocompare !== breakEndHours)) {
                            breakSet.push(breakStartHoursTocompare);
                            breakStartHoursTocompare++;
                            if (breakStartHoursTocompare > 23) {
                                breakStartHoursTocompare -= 24;
                            }
                        }
                        for (var breakTime = 0; breakTime < breakSet.length; breakTime++) {
                            if (mainTimeSet.indexOf(breakSet[breakTime]) === -1) {
                                $scope.invalidBreakTime = true;
                                break;
                            }
                        }
                        if ($scope.invalidBreakTime) {
                            break;
                        } else {
                            if (breakStartHours === startHours) {
                                if (breakStartMins <= startMins) {
                                    $scope.invalidBreakTime = true;
                                    break;
                                }
                            }
                            if (breakEndHours === endHours) {
                                if (breakEndMins >= endMins) {
                                    $scope.invalidBreakTime = true;
                                    break;
                                }
                            }
                            if (breakEndHours === breakStartHours) {
                                if (breakStartMins >= breakEndMins) {
                                    $scope.invalidBreakTime = true;
                                    break;
                                }
                            }

                            if (breakStartHours === startHours && (breakEndHours === endHours) && (startHours === endHours)) {
                                if (breakStartMins <= startMins || breakEndMins >= endMins || breakStartMins >= endMins || breakEndMins <= startMins) {
                                    $scope.invalidBreakTime = true;
                                    break;
                                }
                            }
                        }
                        if (!$scope.invalidBreakTime) {
                            for (var innerLoop = 0; innerLoop < i; innerLoop++) {
                                var tempBreakStartTimeinnerLoop = new Date($scope.shift.breakList[innerLoop].breakStartTime);
                                var tempBreakEndTimeinnerLoop = new Date($scope.shift.breakList[innerLoop].breakEndTime);
                                var breakStartHoursinnerLoop = tempBreakStartTimeinnerLoop.getHours();
                                var breakEndHoursinnerLoop = tempBreakEndTimeinnerLoop.getHours();
                                var breakStartMinsinnerLoop = tempBreakStartTimeinnerLoop.getMinutes();
                                var breakEndMinsinnerLoop = tempBreakEndTimeinnerLoop.getMinutes();
                                var endM = parseInt(breakEndMinsinnerLoop);
                                var endH = parseInt(breakEndHoursinnerLoop);
                                var b = endH + (endM / 60);
                                var StartM = parseInt(breakStartMinsinnerLoop);
                                var StartH = parseInt(breakStartHoursinnerLoop);
                                var a = StartH + (StartM / 60);
                                var outerStartH = parseInt(breakStartHours);
                                var outerStartM = parseInt(breakStartMins);
                                var c = outerStartH + (outerStartM / 60);
                                var outerEndH = parseInt(breakEndHours);
                                var outerEndM = parseInt(breakEndMins);
                                var d = outerEndH + (outerEndM / 60);
                                if (a !== undefined && b !== undefined && c !== undefined && d !== undefined) {
                                    var flag = ((a < c && c < b) || (a < d && d < b));
                                    if ((a < c || a > d) && (b < c || b > d) && (!flag)) {
                                    } else {
                                        $scope.invalidBreakTime = true;
                                    }
                                }
                            }
                        }
                    }
                }
                $scope.selectedDays = [];
                $.each($scope.weekDay.WeekOffList, function (index, day) {
                    if (day.isChecked === 'true' || day.isChecked === true) {
                        $scope.selectedDays.push(day);
                    }
                });
                $scope.weekOffId = '';
                for (var i in $scope.selectedDays) {
                    var id = $scope.selectedDays[i].key;
                    if (id !== 'undefined' || id !== null || id !== '') {
                        if ($scope.weekOffId === '') {
                            $scope.weekOffId = id;
                        }
                        else {
                            $scope.weekOffId += ',' + id;
                        }
                    }
                }
                var createShiftJson = {};
                $scope.departmentRequired = false;
                if ($scope.shift.shiftName === undefined || $scope.shift.shiftName === null || $scope.shift.shiftName === '') {
                    $scope.shiftTitleIsNull = true;
                }
                if ($rootScope.configureDefaultShift || $scope.shift.defaultShift || $scope.overRideShiftFlag) {
                    $scope.departmentRequired = true;
                }
                else {
                    if ($scope.shift.selectedInStringIds !== undefined && $scope.shift.selectedInStringIds.length > 0) {
                        createShiftJson.departmentIds = $scope.shift.selectedInStringIds.toString();
                        createShiftJson.defaultShift = false;
                        $scope.departmentRequired = true;
                    }
                    else {
                        $scope.departmentRequired = false;
                    }
                }
                $scope.breakNameInDb = false;
                if ($scope.shiftManagementForm.$valid && $scope.shift.shiftStartTime !== undefined && $scope.shift.shiftEndTime !== undefined) {
                    createShiftJson = {
                        status: $scope.shift.status
                    };
                    if ((createShiftJson.status || !$scope.isEditing) && !$scope.breakCountZero && $scope.shift.breakList !== undefined && $scope.shift.breakList.length === 0) {
                        $("#passwordPopUp").modal('show');
                    }
                    if ($rootScope.configureDefaultShift && $scope.shift.breakList === undefined) {
                        $("#passwordPopUp").modal('show');
                        $scope.shift.breakList = [];
                    }
                    if (!$scope.shiftNameInDb && $scope.selectedDays.length !== 0 && !$scope.breakNameInDb && $scope.departmentRequired && !$scope.invalidTime && !$scope.invalidBreakTime
                            && ((!createShiftJson.status && $scope.shift.id !== undefined) || (($scope.breakCountZero && $scope.shift.breakList !== undefined && $scope.shift.breakList.length === 0) || ($scope.shift.breakList !== undefined && $scope.shift.breakList.length > 0)))) {
                        var id;
                        if ($rootScope.configureDefaultShift) {
                            id = $scope.defaultShift.id;
                        } else {
                            id = $scope.mainShiftId;
                        }
                        createShiftJson = {
                            id: id,
                            shiftName: $scope.shift.shiftName,
                            shiftStartTime: $scope.shift.shiftStartTime,
                            shiftEndTime: $scope.shift.shiftEndTime,
                            workingDayIds: $scope.weekOffId,
                            breakList: $scope.shift.breakList,
                            status: $scope.shift.status,
                            overrideShift: true,
                            departmentIds: $scope.departmentIds
                        };
                        if ($rootScope.configureDefaultShift) {
                            createShiftJson.departmentIds = '0';
                            createShiftJson.defaultShift = true;
                        }
                        createShiftJson.shiftCustom = $scope.addShiftData;
                        createShiftJson.shiftDbType = $scope.dbType;
                        if ($scope.overRideShiftFlag) {
                            ////console.log(JSON.stringify(createShiftJson));
                            ShiftService.create(createShiftJson, function () {
//                                $scope.resetCustomFields();
                                $scope.reload = true;
                                $scope.clearData();
                                if (!$rootScope.configureDefaultShift) {
                                    $scope.retrieveAllDepartments();
                                }
                                $scope.addShiftData = {};
                                $rootScope.unMaskLoading();
                            }, function () {
//                                $scope.resetCustomFields();
                                $scope.reload = true;
                                $scope.addShiftData = {};
                                var msg = "Could not create shift, please try again.";
                                var type = $rootScope.error;
                                $rootScope.addMessage(msg, type);
                                $rootScope.unMaskLoading();
                            });
                        } else if ($scope.isEditing) {
                            createShiftJson.id = $scope.shift.id;
                            if (!createShiftJson.status && !$rootScope.configureDefaultShift) {
                                ShiftService.userExisrForShift($scope.shift.id, function (res) {
                                    $scope.useresExist = false;
                                    if (res.data) {
                                        $scope.useresExist = true;
                                    } else {
                                        var ids = $scope.shift.departmentIds.split(",");
                                        $scope.ovrDeptNames = [];
                                        getDepartmentName($scope.multiselecttree.items, ids);
                                        $scope.deptNameInPopUp = $scope.ovrDeptNames.toString();
                                        $scope.tempShiftNotAvailable = false;
                                        if ($scope.shift.temporaryShifts !== undefined && $scope.shift.temporaryShifts !== null) {
                                            var shiftName = [];
                                            for (var tempShiftName = 0; tempShiftName < $scope.shift.temporaryShifts.length; tempShiftName++) {
                                                var name = $scope.shift.temporaryShifts[tempShiftName];
                                                shiftName.push(name.tempShiftName);
                                            }
                                            $scope.shiftNameInPopUp = '';
                                            $scope.shiftNameInPopUp = shiftName.toString();
                                        }
                                        else {
                                            $scope.shiftNameInPopUp = '';
                                            $scope.shiftNameInPopUp = $scope.shift.shiftName;
                                            $scope.tempShiftNotAvailable = true;
                                        }
                                    }
                                });
                                $scope.update = createShiftJson;
                                $scope.showConfirmationPopup();
                            } else if ((!res && $scope.isEditing) || $rootScope.configureDefaultShift) {
                                $rootScope.maskLoading();
                                ShiftService.update(createShiftJson, function () {
                                    $scope.clearData();
                                    $scope.retrieveAllDepartments();
                                    $scope.addShiftData = {};
                                    $rootScope.unMaskLoading();

                                }, function () {
                                    //Failure Call back
                                    var msg = "Could not update shift, please try again.";
                                    var type = $rootScope.error;
                                    $rootScope.addMessage(msg, type);
                                    $rootScope.unMaskLoading();
                                });
                            } else if (res && $scope.isEditing && createShiftJson.status) {
                                ovelap = createShiftJson;
                                $scope.overridePopUpp();
                            }
                        }

                    }
                }
//                $scope.resetCustomFields();
                $scope.reload = true;
            };

            $scope.saveOverRideShift = function (flag, shiftManagementForm) {
//                ////console.log($scope.shift.selectedInString);
                $scope.reload = false;
                $scope.submitted = true;
                if ($rootScope.configureDefaultShift) {
                    if (!$scope.shift.shiftStartTime) {
                        $scope.shift.shiftStartTime = new Date();
                    }
                    if (!$scope.shift.shiftEndTime) {
                        $scope.shift.shiftEndTime = new Date();
                    }
                }
                var tempStartTime = new Date($scope.shift.shiftStartTime);
                var tempEndTime = new Date($scope.shift.shiftEndTime);
                var startHours = tempStartTime.getHours();
                var startHoursTocompare = angular.copy(startHours);
                var endHours = tempEndTime.getHours();
                var startMins = tempStartTime.getMinutes();
                var endMins = tempEndTime.getMinutes();
                $scope.validBreakTimeSlot = true;
                $scope.invalidTime = false;
                if (startHours === endHours && startMins === endMins) {
                    $scope.invalidTime = true;
                }
                $scope.invalidBreakTime = false;
                var mainTimeSet = [];
                var mainTimeMinSet = [];
                if ($scope.shift.breakList !== undefined && $scope.shift.breakList.length > 0 && !$scope.invalidTime) {
//                    mainTimeSet.push(startHours);
                    while ((startHoursTocompare !== endHours)) {
                        mainTimeSet.push(startHoursTocompare);
                        startHoursTocompare++;
                        if (startHoursTocompare > 23) {
                            startHoursTocompare -= 24;
                        }
                    }
                    for (var i = 0; i < $scope.shift.breakList.length; i++) {
                        var tempBreakStartTime = new Date($scope.shift.breakList[i].breakStartTime);
                        var tempBreakEndTime = new Date($scope.shift.breakList[i].breakEndTime);
                        var breakStartHours = tempBreakStartTime.getHours();
                        var breakStartHoursTocompare = angular.copy(breakStartHours);
                        var breakEndHours = tempBreakEndTime.getHours();
                        var breakStartMins = tempBreakStartTime.getMinutes();
                        var breakEndMins = tempBreakEndTime.getMinutes();
                        var breakSet = [];
//                        breakSet.push(breakStartHoursTocompare);
                        while ((breakStartHoursTocompare !== breakEndHours)) {
                            breakSet.push(breakStartHoursTocompare);
                            breakStartHoursTocompare++;
                            if (breakStartHoursTocompare > 23) {
                                breakStartHoursTocompare -= 24;
                            }
                        }
                        for (var breakTime = 0; breakTime < breakSet.length; breakTime++) {
                            if (mainTimeSet.indexOf(breakSet[breakTime]) === -1) {
                                $scope.invalidBreakTime = true;
                                break;
                            }
                        }
                        if ($scope.invalidBreakTime) {
                            break;
                        } else {
                            if (breakStartHours === startHours) {
                                if (breakStartMins <= startMins) {
                                    $scope.invalidBreakTime = true;
                                    break;
                                }
                            }
                            if (breakEndHours === endHours) {
                                if (breakEndMins >= endMins) {
                                    $scope.invalidBreakTime = true;
                                    break;
                                }
                            }
                            if (breakEndHours === breakStartHours) {
                                if (breakStartMins >= breakEndMins) {
                                    $scope.invalidBreakTime = true;
                                    break;
                                }
                            }

                            if (breakStartHours === startHours && (breakEndHours === endHours) && (startHours === endHours)) {
                                if (breakStartMins <= startMins || breakEndMins >= endMins || breakStartMins >= endMins || breakEndMins <= startMins) {
                                    $scope.invalidBreakTime = true;
                                    break;
                                }
                            }
                        }
                        if (!$scope.invalidBreakTime) {
                            for (var innerLoop = 0; innerLoop < i; innerLoop++) {
                                var tempBreakStartTimeinnerLoop = new Date($scope.shift.breakList[innerLoop].breakStartTime);
                                var tempBreakEndTimeinnerLoop = new Date($scope.shift.breakList[innerLoop].breakEndTime);
                                var breakStartHoursinnerLoop = tempBreakStartTimeinnerLoop.getHours();
                                var breakEndHoursinnerLoop = tempBreakEndTimeinnerLoop.getHours();
                                var breakStartMinsinnerLoop = tempBreakStartTimeinnerLoop.getMinutes();
                                var breakEndMinsinnerLoop = tempBreakEndTimeinnerLoop.getMinutes();
                                var endM = parseInt(breakEndMinsinnerLoop);
                                var endH = parseInt(breakEndHoursinnerLoop);
                                var b = endH + (endM / 60);
                                var StartM = parseInt(breakStartMinsinnerLoop);
                                var StartH = parseInt(breakStartHoursinnerLoop);
                                var a = StartH + (StartM / 60);
                                var outerStartH = parseInt(breakStartHours);
                                var outerStartM = parseInt(breakStartMins);
                                var c = outerStartH + (outerStartM / 60);
                                var outerEndH = parseInt(breakEndHours);
                                var outerEndM = parseInt(breakEndMins);
                                var d = outerEndH + (outerEndM / 60);
                                if (a !== undefined && b !== undefined && c !== undefined && d !== undefined) {
                                    var flag = ((a < c && c < b) || (a < d && d < b));
                                    if ((a < c || a > d) && (b < c || b > d) && (!flag)) {
                                    } else {
                                        $scope.invalidBreakTime = true;
                                    }
                                }
                            }
                        }
                    }
                }
                $scope.selectedDays = [];
                $.each($scope.weekDay.WeekOffList, function (index, day) {
                    if (day.isChecked === 'true' || day.isChecked === true) {
                        $scope.selectedDays.push(day);
                    }
                });
                $scope.weekOffId = '';
                for (var i in $scope.selectedDays) {
                    var id = $scope.selectedDays[i].key;
                    if (id !== 'undefined' || id !== null || id !== '') {
                        if ($scope.weekOffId === '') {
                            $scope.weekOffId = id;
                        }
                        else {
                            $scope.weekOffId += ',' + id;
                        }
                    }
                }
                var createShiftJson = {};
                $scope.departmentRequired = false;
                if ($scope.shift.shiftName === undefined || $scope.shift.shiftName === null || $scope.shift.shiftName === '') {
                    $scope.shiftTitleIsNull = true;
                }
                if ($rootScope.configureDefaultShift || $scope.shift.defaultShift || $scope.overRideShiftFlag) {
                    $scope.departmentRequired = true;
                }
                else {
                    if ($scope.shift.selectedInStringIds !== undefined && $scope.shift.selectedInStringIds.length > 0) {
                        createShiftJson.departmentIds = $scope.shift.selectedInStringIds.toString();
                        createShiftJson.defaultShift = false;
                        $scope.departmentRequired = true;
                    }
                    else {
                        $scope.departmentRequired = false;
                    }
                }
                $scope.breakNameInDb = false;
                if ($scope.shiftManagementForm.$valid && $scope.shift.shiftStartTime !== undefined && $scope.shift.shiftEndTime !== undefined) {
                    createShiftJson = {
                        status: $scope.shift.status
                    };
                    if ((createShiftJson.status || !$scope.isEditing) && !$scope.breakCountZero && $scope.shift.breakList !== undefined && $scope.shift.breakList.length === 0) {
                        $("#passwordPopUp").modal('show');
                    }
                    if ($rootScope.configureDefaultShift && $scope.shift.breakList === undefined) {
                        $("#passwordPopUp").modal('show');
                        $scope.shift.breakList = [];
                    }
                    if (!$scope.shiftNameInDb && $scope.selectedDays.length !== 0 && !$scope.breakNameInDb && $scope.departmentRequired && !$scope.invalidTime && !$scope.invalidBreakTime
                            && ((!createShiftJson.status && $scope.shift.id !== undefined) || (($scope.breakCountZero && $scope.shift.breakList !== undefined && $scope.shift.breakList.length === 0) || ($scope.shift.breakList !== undefined && $scope.shift.breakList.length > 0)))) {

//                        var res = $scope.timeClash;
                        createShiftJson = {
                            id: $scope.ovrShiftId,
                            shiftName: $scope.shift.shiftName,
                            shiftStartTime: $scope.shift.shiftStartTime,
                            shiftEndTime: $scope.shift.shiftEndTime,
                            workingDayIds: $scope.weekOffId,
                            breakList: $scope.shift.breakList,
                            status: $scope.shift.status,
                            overrideShift: true,
                            departmentIds: $scope.departmentIds
                        };
                        if ($rootScope.configureDefaultShift) {
                            createShiftJson.departmentIds = '0';
                            createShiftJson.defaultShift = true;
                        }
                        createShiftJson.shiftCustom = $scope.addShiftData;
                        createShiftJson.shiftDbType = $scope.dbType;
                        if ($scope.overRideShiftFlag) {
//                            ////console.log(createShiftJson);
                            ShiftService.update(createShiftJson, function () {
                                $scope.clearData();
                                $scope.retrieveAllDepartments();
                                $scope.addShiftData = {};
                                $rootScope.unMaskLoading();
                            }, function () {
//                                $scope.resetCustomFields();
                                $scope.reload = true;
                                $scope.addShiftData = {};
                                var msg = "Could not create shift, please try again.";
                                var type = $rootScope.error;
                                $rootScope.addMessage(msg, type);
                                $rootScope.unMaskLoading();
                            });
                        } else if ($scope.isEditing) {
                            createShiftJson.id = $scope.shift.id;
                            if (!createShiftJson.status && !$rootScope.configureDefaultShift) {
                                ShiftService.userExisrForShift($scope.shift.id, function (res) {
                                    $scope.useresExist = false;
                                    if (res.data) {
                                        $scope.useresExist = true;
                                    } else {
                                        var ids = $scope.shift.departmentIds.split(",");
                                        $scope.ovrDeptNames = [];
                                        getDepartmentName($scope.multiselecttree.items, ids);
                                        $scope.deptNameInPopUp = $scope.ovrDeptNames.toString();
                                        $scope.tempShiftNotAvailable = false;
                                        if ($scope.shift.temporaryShifts !== undefined && $scope.shift.temporaryShifts !== null) {
                                            var shiftName = [];
                                            for (var tempShiftName = 0; tempShiftName < $scope.shift.temporaryShifts.length; tempShiftName++) {
                                                var name = $scope.shift.temporaryShifts[tempShiftName];
                                                shiftName.push(name.tempShiftName);
                                            }
                                            $scope.shiftNameInPopUp = '';
                                            $scope.shiftNameInPopUp = shiftName.toString();
                                        }
                                        else {
                                            $scope.shiftNameInPopUp = '';
                                            $scope.shiftNameInPopUp = $scope.shift.shiftName;
                                            $scope.tempShiftNotAvailable = true;
                                        }
                                    }
                                });
                                $scope.update = createShiftJson;
                                $scope.showConfirmationPopup();
                            } else if ((!res && $scope.isEditing) || $rootScope.configureDefaultShift) {
                                $rootScope.maskLoading();
                                ShiftService.update(createShiftJson, function () {
                                    $scope.clearData();
                                    $scope.retrieveAllDepartments();
                                    $scope.addShiftData = {};
                                    $rootScope.unMaskLoading();

                                }, function () {
                                    //Failure Call back
                                    var msg = "Could not update shift, please try again.";
                                    var type = $rootScope.error;
                                    $rootScope.addMessage(msg, type);
                                    $rootScope.unMaskLoading();
                                });
                            } else if (res && $scope.isEditing && createShiftJson.status) {
                                ovelap = createShiftJson;
                                $scope.overridePopUpp();
                            }
                        }

                    }
                }
//                $scope.resetCustomFields();
                $scope.reload = true;
            };
            $rootScope.unMaskLoading();
        }]);
});