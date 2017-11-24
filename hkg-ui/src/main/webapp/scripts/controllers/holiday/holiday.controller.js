define(['hkg', 'manageHolidayService', 'addMasterValue', 'dynamicForm'], function (hkg) {
    hkg.register.controller('ManageHoliday', ["$rootScope", "$scope", "$filter", "ManageHolidayService", "$location", "$route", "$timeout", "$http", "limitToFilter", "DynamicFormService", function ($rootScope, $scope, $filter, ManageHolidayService, $location, $route, $timeout, $http, limitToFilter, DynamicFormService) {
            $rootScope.maskLoading();
            $rootScope.mainMenu = "manageLink";
            $rootScope.childMenu = "manageHoliday";
            $rootScope.activateMenu();
            $scope.holidayOperation;
            $scope.holidayName = "";
            $scope.searchedHolidayRecords = [];
            $scope.allHolidays = [];
            $scope.listOfModelsOfDateType = [];
            //retrive all holiday 
            var templateData = DynamicFormService.retrieveSectionWiseCustomFieldInfo("manageHoliday");
            $scope.dbType = {};
            templateData.then(function (section) {
                $scope.listOfModelsOfDateType = [];
                // Method modified by Shifa Salheen on 17 April for implementing sequence number
                $scope.customTemplateDate = angular.copy(section['genralSection']);
                $scope.genralHolidayTemplate = $rootScope.getCustomDataInSequence($scope.customTemplateDate);
                if ($scope.genralHolidayTemplate !== null && $scope.genralHolidayTemplate !== undefined)
                {
                    angular.forEach($scope.genralHolidayTemplate, function (updateTemplate)
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
            $scope.retriveAllHoliday = function () {
                $rootScope.maskLoading();
                ManageHolidayService.retrieveAllHoliday(function (responseData) {
                    $scope.allHolidays = [];
                    angular.forEach(responseData.data, function (item) {
                        item.title = $rootScope.translateValue("H_NAME." + item.title);
                        $scope.allHolidays.push(item);
                    });
//                    $scope.allHolidays = responseData.data;
                    $rootScope.editHoliday = undefined;
                    if ($scope.allHolidays.length > 0) {
                        $scope.renderTable = true;
                    } else {
                        $scope.renderTable = false;
                    }
                    $scope.backupHoliday = responseData.data;
                    $rootScope.unMaskLoading();
                    //   $scope.renderTable = false;
                }, function () {
                    $rootScope.addMessage("Failed to load all holidays.", $rootScope.failure);
                    $rootScope.unMaskLoading();
                });
//                $scope.searchedList = [];
//                if ($rootScope.fileUploadMsg !== undefined && $rootScope.fileUploadMsg.length > 0) {
//                    var msg = $rootScope.fileUploadMsg;
//                    var type = $rootScope.success;
//                    $rootScope.addMessage(msg, type);
//                    $rootScope.fileUploadMsg = "";
//                }
//                if ($rootScope.crudMsg !== undefined && $rootScope.crudMsg.length > 0) {
//                    var msg = $rootScope.crudMsg;
//                    var type = $rootScope.crudMsgType;
//                    $rootScope.addMessage(msg, type);
//                    // $rootScope.crudMsg = "";
//                    //$rootScope.crudMsgType = "";
//                }
//                $scope.renderTable = false;
//
//                if ($rootScope.matchedHolidays !== undefined && $rootScope.matchedHolidays.length > 0) {
//                    $scope.allHolidays = $rootScope.matchedHolidays;
//                    $scope.filteredValue = $rootScope.matchedHolidays;
//                    $rootScope.editHoliday = undefined;
//                    $scope.renderTable = true;
//                    $rootScope.matchedHolidays = [];
//                } else {
//                    ManageHolidayService.retrieveAllHoliday(function(responseData) {
//                        $scope.allHolidays = responseData.data;
//                        $rootScope.editHoliday = undefined;
//                        $scope.renderTable = true;
//                        $scope.backupHoliday = responseData.data;
//                    }, function() {
//                        $rootScope.addMessage("Failed to load all holidays.", $rootScope.failure);
//                    });
//                }

            };
            $scope.setHolidayForEdit = function (holiday) {
                $scope.setHolidayOperation('addHoliday', function () {
                    $scope.isEdit = true;
                    $scope.holiday = holiday;
                    $scope.holiday.status = "Active";
                    $scope.holiday.startDt = new Date($scope.holiday.startDt);
                    $scope.holiday.endDt = new Date($scope.holiday.endDt);
                    $scope.addHolidayData = holiday.holidayCustom;
                    if (!!$scope.addHolidayData) {
                        angular.forEach($scope.listOfModelsOfDateType, function (listOfModel)
                        {
                            if ($scope.addHolidayData.hasOwnProperty(listOfModel))
                            {
                                if ($scope.addHolidayData[listOfModel] !== null && $scope.addHolidayData[listOfModel] !== undefined)
                                {
                                    $scope.addHolidayData[listOfModel] = new Date($scope.addHolidayData[listOfModel]);
                                } else
                                {
                                    $scope.addHolidayData[listOfModel] = '';
                                }
                            }
                        })
                    }
                    $scope.selectedHolidayTitle = {id: 'selected', text: $scope.holiday.title};
                });

                //$location.path("/addholiday");
            };
            $scope.redirectToEdit = function (holidayId) {
                var flag = true;
                var holidayIdForEdit = {
                    primaryKey: holidayId
                }
                $rootScope.maskLoading();
                ManageHolidayService.retrieveHolidayById(holidayIdForEdit, function (res) {
                    $scope.holiday = res.data;
                    $scope.setHolidayForEdit($scope.holiday);
                }, function () {
                    $rootScope.addMessage("Failed to retrieve  holiday.", $rootScope.failure);
                    $rootScope.unMaskLoading();
                })

                if (flag) {
                    $scope.renderTable = false;
                }
            }
            $scope.getSearchedRecords = function (list) {
                var enteredText = $('#scrollable-dropdown-menu.typeahead').typeahead('val');
                if (enteredText.length > 0) {
                    if (enteredText.length < 3) {
                        $scope.searchedHolidayRecords = [];
                        $scope.setHolidayOperation("searchedHoliday");
                    } else {
                        $scope.searchedHolidayRecords = angular.copy(list);
                        $scope.setHolidayOperation("searchedHoliday");
                    }
                }
            }
            $scope.resetList = function () {
                $scope.allHolidays = $scope.backupHoliday;
                $scope.isRecordsExits = true;
            }
            $scope.setHolidayOperation = function (operation, callback) {
                $scope.holidayOperation = operation;

                if (operation === "manageHoliday") {
                    if ($rootScope.getCurrentServerDate() !== undefined) {
                        $scope.currentYear = $rootScope.getCurrentServerDate().getFullYear();
                    }
//                    } else {
//                        $scope.currentYear = new Date().getFullYear();
//                    }

                    $scope.removeFlage = false;
                    $scope.holidayForRemove = {};

                    $scope.entity = "HOLIDAY.";
                    $scope.searchedHoliday = [];
                    $scope.filteredValue = [];
                    $scope.backupHoliday = [];
                    $scope.isRecordsExits = true;
                    $scope.retriveAllHoliday();
                    $('#scrollable-dropdown-menu.typeahead').typeahead('val', '');
                } else if (operation === "addHoliday") {
                    $('#scrollable-dropdown-menu.typeahead').typeahead('val', '');
                    $scope.currentYear = $rootScope.getCurrentServerDate().getFullYear();
                    $scope.holiday = {};
                    $scope.selectedHolidayTitle = "";
                    $scope.holiday.startDt = "";
                    $scope.isEdit = false;
                    $scope.uploadResponseMsgs = [];
                    $scope.distinctHoliday = [];
                    $scope.uploadStatus = false;
                    $scope.updatedTitle = "";
                    $rootScope.fileUploadMsg = "";
                    $rootScope.crudMsg = "";
                    $rootScope.crudMsgType = "";
                    $scope.holidayStatus = "";
                    $scope.errorMsg = [];
                    $scope.warningMsg = [];
                    $scope.entity = "HOLIDAY.";
                    //Writable combobox
                    $scope.holidaySuggestion;

                    $scope.holidayList = [];
                    $scope.uploadWorksheetFile = "";
                    $scope.holidayForm;
                    $scope.holidayIdForRemove;
                    //for warning pop up
                    $scope.options = [{name: "Active", id: 1}, {name: "Remove", id: 2}];

                    $scope.addHolidayData = DynamicFormService.resetSection($scope.genralHolidayTemplate);

                    $scope.cancelHoliday = function (holidayForm) {
                        $scope.holiday = {};
                        holidayForm.$setPristine();
                        $scope.submitted = false;
                        $scope.setHolidayOperation("manageHoliday");
                    }
                    $scope.retriveDistinctHoliday = function () {
                        if ($rootScope.suggestedHolidays !== undefined) {
                            $scope.holidaySuggestion = $rootScope.suggestedHolidays;
                        }
//                        ManageHolidayService.retrieveAllHoliday(function(responseData) {
//                            $scope.holidayList = responseData.data;
//                        }, function() {
//                            $rootScope.addMessage("Failed to load all holidays.", $rootScope.failure);
//                        });
                        $rootScope.maskLoading();
                        ManageHolidayService.retrievePreviousYearDistinctHoliday(function (response) {
                            var allholiday = response.data;
                            $.each(allholiday, function (key, value) {
                                $scope.distinctHoliday.push({"id": value, "text": value});

                            });
                            $rootScope.unMaskLoading();
                        }, function () {
                            $rootScope.addMessage("Failed to load all holidays.", $rootScope.failure);
                            $rootScope.unMaskLoading();
                        });

                    };
                    $scope.retriveDistinctHoliday();
                    $scope.initAddHolidayForm = function (form) {
                        $scope.datepicker = {from: false, end: false};
                        //            $scope.setMinDate();
                        $scope.open = function ($event, opened) {

                            $event.preventDefault();
                            $event.stopPropagation();
                            $scope.datepicker[opened] = true;
                        };
                        form.$dirty = false;
                    }
                    $scope.updateHoliday = function (holidayForm, holiday) {
                        $scope.holidayForm = holidayForm;
                        var success = function (response) {
//                            $scope.uploadResponseMsgs = response.messages;
                            $scope.edituploadResponseMsgs = response.messages;
                            var isError = false;
                            var isWar = false;
                            $.each($scope.edituploadResponseMsgs, function (key, value) {
                                if (value.responseCode === 1) {
                                    isError = true;
                                }
                                if (value.responseCode === 2) {
                                    isWar = true;
                                }
                            });
                            if (!isError && isWar) {
                                $('#warningShowId').modal('show');

                            }
                            if ($scope.edituploadResponseMsgs !== undefined && $scope.edituploadResponseMsgs[0].responseCode) {
                                $scope.holiday = holiday;
                                $rootScope.unMaskLoading();
                            } else {
                                holidayForm.$setPristine();
                                $scope.submitted = false;

                                $scope.holiday = {};
                                $scope.isEdit = false;
                                $rootScope.editHoliday = undefined;
                                $scope.uploadResponseMsgs = [];
                                $rootScope.addMessage(response.messages[0].message, $rootScope.success);
                                $scope.setHolidayOperation("manageHoliday");
                                $rootScope.unMaskLoading();
                            }
                        };

                        var failure = function () {
                            $rootScope.crudMsg = "Holiday could not be saved. Try again.";
                            $rootScope.crudMsgType = $rootScope.failure;
                            $rootScope.unMaskLoading();
                        };

                        $scope.submitted = true;
                        if (holidayForm.$valid) {
                            holidayForm.$setPristine();
                            $scope.submitted = false;
                            if ($scope.holiday.status === "Remove") {
                                $scope.showRemoveConfirmation(holiday);
                                $scope.holidayIdForRemove = holiday.id;
                                // $scope.setHolidayOperation("manageHoliday");
                            } else {
                                var holidayJson = {
                                    "id": holiday.id,
                                    "startDt": holiday.startDt,
                                    "title": holiday.title,
                                    "endDt": holiday.endDt,
                                    "holidayCustom": $scope.addHolidayData,
                                    "dbType": $scope.dbType

                                }
                                var holidayJson = {};
                                if (holiday.forceEdit === true) {
                                    holidayJson = {
                                        "id": holiday.id,
                                        "startDt": holiday.startDt,
                                        "title": holiday.title,
                                        "endDt": holiday.endDt,
                                        "holidayCustom": $scope.addHolidayData,
                                        "dbType": $scope.dbType,
                                        "forceEdit": "true"
                                    }
                                } else {
                                    holidayJson = {
                                        "id": holiday.id,
                                        "startDt": holiday.startDt,
                                        "title": holiday.title,
                                        "endDt": holiday.endDt,
                                        "holidayCustom": $scope.addHolidayData,
                                        "dbType": $scope.dbType

                                    }
                                }
                                $rootScope.maskLoading();
                                ManageHolidayService.editHoliday(holidayJson, success, failure);
                                $scope.holiday = {};
                            }
                        }

                    }
                    ;
                    $scope.saveHoliday = function (holidayForm, holiday) {
                        $scope.holidayForm = holidayForm;
                        var success = function (response) {
                            holidayForm.$setPristine();
                            $scope.submitted = false;
//                            $scope.uploadResponseMsgs = response.messages;
                            $scope.messages = response.messages;
                            var isError = false;
                            var isWar = false;
                            $.each($scope.messages, function (key, value) {
                                if (value.responseCode === 1) {
                                    isError = true;
                                }
                                if (value.responseCode === 2) {
                                    isWar = true;
                                }
                            });
                            if (!isError && isWar) {
                                $('#warningShowId').modal('show');
                            }
                            if (!($scope.messages !== undefined && $scope.messages[0].responseCode)) {
                                $scope.messages = [];
                                $rootScope.addMessage(response.messages[0].message, $rootScope.success);
                                $scope.setHolidayOperation("manageHoliday");

                            }
                            $rootScope.unMaskLoading();
                        };

                        var failure = function () {
                            $rootScope.crudMsg = "Holidays could not be updated";
                            $rootScope.crudMsgType = $rootScope.failure;
                            $rootScope.unMaskLoading();
                        };
                        $scope.submitted = true;

                        if (holidayForm.$valid) {
                            var holidayJson = {
                                "startDt": holiday.startDt,
                                "title": holiday.title,
                                "endDt": holiday.endDt,
                                "forceEdit": holiday.forceEdit,
                                "holidayCustom": $scope.addHolidayData,
                                "dbType": $scope.dbType


                            }
                            $rootScope.maskLoading();
                            if ($scope.holidayForm.$valid) {
                                ManageHolidayService.saveHoliday(holidayJson, success, failure);
                            }
                        }
                    }

                    $scope.proceedOk = function (operation) {
                        $('#warningShowId').modal('hide');
                        $rootScope.removeModalOpenCssAfterModalHide();
                        $('.modal-backdrop').remove();
                        $scope.holiday.forceEdit = true;
                        if ($scope.isEdit) {
                            $scope.updateHoliday($scope.holidayForm, $scope.holiday);
                        } else {
                            $scope.saveHoliday($scope.holidayForm, $scope.holiday);
                        }
                    }
                    $scope.proceedCancel = function () {
                        $('#warningShowId').modal('hide');
                        $rootScope.removeModalOpenCssAfterModalHide();
                        $('.modal-backdrop').remove();
                    }

                    //for upload holiday worksheet
                    $scope.uploadHolidayConfig = {
                        target: $rootScope.appendAuthToken($rootScope.apipath + 'holiday/uploadholidayworksheet'),
                        singleFile: true,
                        testChunks: false,
                        query: {
                            fileName: "shreya"
                        }};

                    $scope.validateExtension = function (file) {
                        $scope.uploadHolidayConfig.query.fileName = file.name;
                        $scope.uploadWorksheetFile = file.name;
                        if ((file.getExtension() !== "xls") && (file.getExtension() !== "xlsx")) {
                            $rootScope.addMessage("Only xls files are allowed.Please upload valid file.", $rootScope.failure);
                        } else {
                            //Check file size greater than 5 MB
                            $scope.seletedFileType = file.getExtension();
                            if (file.size > 5242880) {
                                var msg = "File size too large. Upload file with size less than 5MB.";
                                var type = $rootScope.failure;
                                $rootScope.addMessage(msg, type);
                                return false;
                            }
                        }
                        return !!{
                            xls: 1,
                            xlsx: 1
                        }
                        [file.getExtension()];
                    };
                    $scope.successUpload = function (response, holidayForm) {
                        $scope.holidayForm = holidayForm;
                        $scope.holidayForm.$setPristine();
                        $scope.submitted = false;
                        response = (JSON.parse(response));
                        $scope.uploadResponseMsgs = response.messages;
                        if (!$scope.uploadResponseMsgs[0].responseCode) {
                            $scope.uploadStatus = true;
                            $rootScope.addMessage(response.messages[0].message, $rootScope.success);
                            $scope.setHolidayOperation("manageHoliday");
                        } else {
                            $.each($scope.uploadResponseMsgs, function (key, value) {
                                if (value.responseCode === 1) {
                                    $scope.errorMsg.push(value);
                                }
                                if (value.responseCode === 2) {
                                    $scope.warningMsg.push(value);
                                }
                            });
                            $scope.uploadStatus = false;
                            $scope.holiday = {};
                            $('#showId').modal('show');
                        }
                    };
                    $scope.showDialog = function () {
                        $('#showId').modal('show');
                    }
                    $scope.hideErrorPopup = function () {
                        $scope.errorMsg = [];
                        $scope.warningMsg = [];
                        $('#showId').modal('hide');
                        $rootScope.removeModalOpenCssAfterModalHide();
                        $('.modal-backdrop').remove();
                    }

                    $scope.forceAddHoliday = function () {

                        var success = function (response) {
                            $('#showId').modal('hide');
                            $rootScope.removeModalOpenCssAfterModalHide();
                            $('.modal-backdrop').remove();
                            if (!response.responseCode) {
                                $scope.uploadStatus = true;
                                $rootScope.fileUploadMsg = response.messages[0].message;
                                $scope.setHolidayOperation('manageHoliday');
                            }
                            $rootScope.unMaskLoading();
                        }
                        var failure = function () {
                            $rootScope.unMaskLoading();
                        }
                        $rootScope.maskLoading();
                        ManageHolidayService.forceHolidayAdd(success, failure);
                    }
                    $scope.removeHolidayById = function () {
                        var holidayId = {
                            primaryKey: $scope.holidayIdForRemove
                        }
                        var success = function (response) {
                            var msg = response.messages[0].message;
                            var type = $rootScope.success;
                            $rootScope.addMessage(msg, type);
                            $scope.setHolidayOperation('manageHoliday');
                            $rootScope.unMaskLoading();
                        }
                        var failure = function () {
                            var msg = " Holiday could not be removed. Try again.";
                            var type = $rootScope.failure;
                            $rootScope.addMessage(msg, type);
                            $rootScope.unMaskLoading();
                        }
                        $rootScope.maskLoading();
                        ManageHolidayService.removeHoliday(holidayId, success, failure);
                        $scope.holidayIdForRemove = "";

                    };

                    $scope.showRemoveConfirmation = function (holiday) {
                        $('#removeShowId').modal('show');
                        $scope.holidayName = holiday.title;
                    }
                    $scope.removeOk = function () {
                        $('#removeShowId').modal('hide');
                        $rootScope.removeModalOpenCssAfterModalHide();
                        $('.modal-backdrop').remove();
                        $scope.removeHolidayById();
                    }
                    $scope.removeCancel = function () {
                        $scope.holiday.status = "Active";
                        $('#removeShowId').modal('hide');
                        $rootScope.removeModalOpenCssAfterModalHide();
                        $('.modal-backdrop').remove();
                    }
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



                    // Disable weekend selection
                    $scope.disabled = function (date, mode) {
                        return (mode === 'day' && (date.getDay() === 0));
                    };

                    $scope.toggleMin = function () {
                        var currentYear = $rootScope.getCurrentServerDate();
                        $scope.minFromDate = $rootScope.getCurrentServerDate();// $rootScope.getCurrentServerDate();
                        $scope.minToDate = new Date(currentYear.getFullYear() - 1, 12, 1);//$rootScope.getCurrentServerDate();

                        $scope.maxFromDate = new Date(currentYear.getFullYear(), 11, 31);
                        $scope.maxToDate = new Date(currentYear.getFullYear(), 11, 31);
                        //     $scope.minToDate = new Date();
                    };
                    $scope.toggleMin();
                    $scope.setMaxDate = function () {
                        $scope.maxDate = $scope.holiday.endDt;
                    }
                    $scope.setToDate = function () {
                        if ($scope.holiday.startDt === "" || $scope.holiday.startDt === undefined) {
                            $scope.minToDate = $rootScope.getCurrentServerDate();
                        } else {
                            $scope.minToDate = $scope.holiday.startDt;
                            if (!$scope.isEdit) {
                                $scope.holiday.endDt = $scope.holiday.startDt;
                            }
                        }
                        if ($scope.holiday.startDt > $scope.holiday.endDt) {
                            $scope.holiday.endDt = $scope.holiday.startDt;
                        }

                    };
                    $scope.setFromDate = function () {
                        $scope.maxFromDate = $scope.holiday.endDt;
                    }
                    $scope.setMaxDate = function () {

                    };


                    $scope.dateOptions = {
                        'year-format': "'yy'",
                        'starting-day': 1
                    };
                    $scope.formats = ['dd-MMMM-yyyy', 'yyyy/MM/dd', 'shortDate'];
                    $scope.format = $rootScope.dateFormat;
                    //For date picker
                    $scope.today();

                    $scope.showWeeks = true;
                    $scope.toggleWeeks = function () {
                        $scope.showWeeks = !$scope.showWeeks;
                    };

                    $scope.clear = function () {
                        $scope.dt = null;
                    };

                }

                if (!!callback) {
                    callback();
                }
            }
            $scope.setHolidayOperation('manageHoliday');




            $scope.today = function () {
                $scope.dt = $rootScope.getCurrentServerDate();
            };




            //For Remove Holiday
            $scope.showConfirmDialog = function (holiday) {
                $scope.holidayForRemove = holiday;
                $scope.holidayName = holiday.title;
                $('#removePopupId').modal('show');
            }
            $scope.removeHoliday = function () {
                var success = function (response) {
                    var msg = response.messages[0].message;
                    var type = $rootScope.success;
                    $rootScope.addMessage(msg, type);
                    $scope.setHolidayOperation('manageHoliday');
                    $rootScope.unMaskLoading();
                };
                var failure = function () {
                    var msg = " Holiday could not be removed. Try again.";
                    var type = $rootScope.failure;
                    $rootScope.addMessage(msg, type);
                    $rootScope.unMaskLoading();
                };

                var holidayId = {
                    primaryKey: $scope.holidayForRemove.id
                }
                if ($scope.removeFlage) {
                    $rootScope.maskLoading();
                    ManageHolidayService.removeHoliday(holidayId, success, failure);
                    $scope.holidayForRemove = {};
                    $scope.removeFlage = false;

                }
            };
            $scope.removeOkPress = function () {
                $('#removePopupId').modal('hide');
                $rootScope.removeModalOpenCssAfterModalHide();
                $('.modal-backdrop').remove();
                $scope.removeFlage = true;
                $scope.removeHoliday();//                 
            }

            $scope.removeCancelPress = function () {
                $('#removePopupId').modal('hide');
                $rootScope.removeModalOpenCssAfterModalHide();
                $('.modal-backdrop').remove();
                $scope.removeFlage = false;
                $scope.holidayForRemove = {};

            }

            //get full day name from date
            $scope.retrievFullDayName = function (timestamp, index, holidayTitle, holidayDate) {
                if (timestamp !== null) {
                    var d = new Date(timestamp);
                    var weekday = new Array(7);
                    weekday[0] = "Sunday";
                    weekday[1] = "Monday";
                    weekday[2] = "Tuesday";
                    weekday[3] = "Wednesday";
                    weekday[4] = "Thursday";
                    weekday[5] = "Friday";
                    weekday[6] = "Saturday";
                    var dayName = weekday[d.getDay()];
                    return dayName;
                } else {
                    return "NA";
                }

            }
            //combine start and to  Todate if end date is not same as start date
            $scope.combineStartAndToDate = function (startDate, endDate) {
                if (startDate !== null && endDate !== null) {
                    var startDt = new Date(startDate);
                    var endDt = new Date(endDate);

                    var start_day = startDt.getDate();
                    start_day = (start_day < 10) ? '0' + start_day : start_day;
                    var start_month = startDt.getMonth() + 1;
                    start_month = (start_month < 10) ? '0' + start_month : start_month;

                    if (startDt < endDt) {
                        var end_day = endDt.getDate();
                        end_day = (end_day < 10) ? '0' + end_day : end_day;
                        var end_month = endDt.getMonth() + 1;
                        end_month = (end_month < 10) ? '0' + end_month : end_month;

                        return start_day + '/' + start_month + '/' + startDt.getFullYear() + " To " + end_day + '/' + end_month + '/' + endDt.getFullYear();


                    } else {
                        return start_day + '/' + start_month + '/' + startDt.getFullYear();
                    }

                } else {
                    return "NA";
                }
            }

            $rootScope.unMaskLoading();
        }]);
});
