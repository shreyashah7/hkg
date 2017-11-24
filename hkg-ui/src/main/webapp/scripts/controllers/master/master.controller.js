define(['hkg', 'manageMasterService', 'ngload!ngTable', 'messageService'], function (hkg, manageMasterService) {
    hkg.register.controller('ManageMaster', ["$rootScope", "$scope", "$filter", "ManageMasterService", "ngTableParams", "Messaging", "$timeout", function ($rootScope, $scope, $filter, ManageMasterService, ngTableParams, Messaging, $timeout) {
            $rootScope.maskLoading();
            var orderBy = $filter('orderBy');
            $rootScope.mainMenu = "manageLink";
            $rootScope.childMenu = "manageMasters";
            $rootScope.activateMenu();
            $scope.listFilled = false;
            $scope.masterList = true;
            $scope.shortCutInDB = false;
            $scope.masterValueInDB = false;
            $scope.shortCutCodeLength = false;
            $scope.editMaster = false;
            $scope.addExceptionPage = false;
            $scope.editablle = false;
            $scope.editExceptionFlag = false;
            $scope.isEditing = false;
            $scope.detailsFilled = false;
            $scope.submitted = false;
            $scope.searchPage = false;
            $scope.count = 0;
            $scope.forUserList = [];
            $scope.exceptionList = [];
            $scope.finalExceptionList = [];
            $scope.valueException = {};
            $scope.selected = null;
            $scope.masterType = [{"id": 1, "text": null, "displayName": "Built- in masters", children: null}];
            $scope.hasCustomMasterRigth = function (flag) {
                if (flag)
                    $scope.masterType.push({"id": 2, "text": null, "displayName": "Custom masters", children: null});
            }
            $scope.tableParams = new ngTableParams({
                page: 1, // show first page
                count: 5 // count per page
            });
            $scope.entity = 'MASTER.';
            $scope.initMasterForm = function (editMaster) {
                $scope.editMasterForm = editMaster;
            };
            $scope.sort = function (alphabet) {
                if (alphabet) {
                    $scope.selected = alphabet;
                    if ($scope.listOfMaster) {
                        $scope.listOfMaster = [];
                        if (alphabet !== 'All') {
                            for (var i = 0; i < $scope.masters.length; i++) {
                                if (alphabet === $scope.masters[i].masterName.charAt(0).toUpperCase()) {
//                                    $scope.listOfMaster.push(angular.copy($scope.masters[i]));
                                    $scope.listOfMaster.push($scope.masters[i]);
                                    $scope.listOfMasterFilled = true;
//                                    break;
                                }
                            }
                        } else {
                            $scope.listOfMaster = angular.copy($scope.masters);
                            $scope.listOfMasterFilled = true;
                        }
                    }
                }
            };
            $scope.showSelectedMasters = function (selectedMasterId) {
                $scope.editMaster = false;
                $scope.masterList = true;
                $scope.letterList = [];
                $scope.selected = 'All';
                $scope.addExceptionPage = false;
                $scope.editExceptionFlag = false;
                $scope.cancelExceptionPage();
                if (selectedMasterId.currentNode.id.toString() === '1') {
                    $scope.listOfMaster = angular.copy($scope.builtInMaster);
                    $scope.masters = angular.copy($scope.builtInMaster);
                    $scope.selectedType = '1';
                    for (var i = 65; i <= 90; i++) {
                        $scope.letterList.push(String.fromCharCode(i));
//                        for (var itr = 0; itr < $scope.builtInMaster.length; itr++) {
//                            var flag = $.inArray(String.fromCharCode(i), $scope.letterList) > -1;
//                            if (String.fromCharCode(i) === $scope.builtInMaster[itr].masterName.toUpperCase().charAt(0) && !flag) {
//                                
//                            }
//                        }
                        if (i === 90) {
                            $scope.letterList.push('All');
                            $scope.listFilled = true;
                        }
                    }
                } else {
                    $scope.listOfMaster = angular.copy($scope.customMaster);
                    $scope.selectedType = '2';
                    $scope.masters = angular.copy($scope.customMaster);
                    for (var i = 65; i <= 90; i++) {
                        $scope.letterList.push(String.fromCharCode(i));
//                        for (var itr = 0; itr < $scope.customMaster.length; itr++) {
//                            var flag = $.inArray(String.fromCharCode(i), $scope.letterList) > -1;
//                            if (String.fromCharCode(i) === $scope.customMaster[itr].masterName.toUpperCase().charAt(0) && !flag) {
//                                
//                            }
//                        }
                        if (i === 90) {
                            $scope.letterList.push('All');
                            $scope.listFilled = true;
                        }
                    }
                }
                $scope.sort();
            };
            ManageMasterService.retrieveListOfMaster(function (data) {
                $rootScope.maskLoading();
                data = orderBy(data, ['masterName']);
                $scope.letterList = [];
                var count = 0;
                for (var i = 65; i <= 90; i++) {
                    $scope.letterList.push(String.fromCharCode(i));
//                    for (var itr = 0; itr < data.length; itr++) {
//                        var flag = $.inArray(String.fromCharCode(i), $scope.letterList) > -1;
//                        if (String.fromCharCode(i) === data[itr].masterName.charAt(0) && !flag) {
//                            count++;
//                            
//                        }
//                    }
                    if (i === 90) {
                        $scope.letterList.push('All');
                        $scope.listFilled = true;
                        $scope.selected = 'All';
                    }
                }
                $scope.listOfMaster = data;
                $scope.listOfMasterFilled = true;
                $scope.langNotSelected = true;
                $rootScope.unMaskLoading();
                $scope.masters = angular.copy($scope.listOfMaster);
                $scope.builtInMaster = [];
                $scope.customMaster = [];
                for (var i = 0; i < $scope.masters.length; i++) {
                    var item = $scope.masters[i];
                    if (item.masterType === 'B' || item.masterType === 'b') {
                        $scope.builtInMaster.push(item);
                    } else {
                        $scope.customMaster.push(item);
                    }
                }
            }, function () {
                var msg = "Failed to retrieve master";
                var type = $rootScope.error;
                $rootScope.addMessage(msg, type);
            });
            $scope.showConfirmationPopup = function (master) {
                $rootScope.maskLoading();
                $scope.serchedList;
                $scope.searchPage = false;
                $scope.addExceptionPage = false;
                $scope.editExceptionFlag = false;
                $scope.code = master.code;
                $scope.masterName = master.masterName;
                $scope.isSensitiveMaster = master.isSensitiveMaster;
                if (master.isSensitiveMaster === true) {
                    $("#passwordPopUp").modal('show');
                    $scope.showPopup = true;
                    $rootScope.unMaskLoading();
                } else {
                    $scope.editMasterDetail();
                    $scope.showPopup = false;
                    $rootScope.unMaskLoading();
                }
            };
            $scope.proceed = function (passwordPopUpForm) {
                $scope.submitted = true;
                if (passwordPopUpForm.$valid) {
                    $scope.submitted = false;
                    var password = $scope.password;
                    ManageMasterService.authenticateForEditMaster(password, function (res) {
                        if (res.data) {
                            $scope.inValidPassword = false;
                            $scope.editMasterDetail();
                            $scope.showPopup = false;
                            $rootScope.unMaskLoading();
                        } else {
                            $scope.inValidPassword = true;
                            $rootScope.unMaskLoading();
                        }
                    });
                }
            };
            $scope.hideConfirmationPopup = function () {
                $scope.password = '';
                $scope.submitted = false;
                $scope.inValidPassword = false;
                $scope.passwordPopUpForm.$setPristine();
                $("#passwordPopUp").modal('hide');
                $rootScope.removeModalOpenCssAfterModalHide();
            };
            $scope.editMasterDetail = function () {
                $rootScope.maskLoading();
                ManageMasterService.retrieveDetailsOfMaster({primaryKey: $scope.code}, function (response) {
                    $scope.detailOfMaster = response;
                    if ($scope.detailOfMaster.masterDataBeans) {
                        for (var i = 0; i < $scope.detailOfMaster.masterDataBeans.length; i++)
                        {
                            var item = $scope.detailOfMaster.masterDataBeans[i];
                            item.newArchieve = item.isArchieve;
                        }
                    }
                    $scope.detailOfMaster.masterDataBeans = orderBy($scope.detailOfMaster.masterDataBeans, ['-isOftenUsed', 'shortcutCode', 'value']);
                    $scope.detailOfMasterInDB = angular.copy(response);
                    $scope.detailsFilled = true;
                    $rootScope.unMaskLoading();
                });
                $scope.editMasterForm.$dirty = false;
                $scope.masterList = false;
                $scope.editMaster = true;
                if ($scope.showPopup === true || $scope.showPopup === 'true') {
                    $("#passwordPopUp").modal('hide');
                    $rootScope.removeModalOpenCssAfterModalHide();
                }
                ManageMasterService.retrieveLanguage(function (res) {
                    $scope.languageList = [];
                    var check = [];
                    for (var prop in res["data"]) {
                        var obj = {id: prop, title: res["data"][prop]};
                        $scope.languageList.push(obj);
                    }
                    $scope.languageList = $scope.languageList.sort($rootScope.predicateBy("title"));
                });
            };
            $scope.shortCutExists = function (master, editMaster1) {
                $scope.shortCutInDB = false;
                if (editMaster1) {
                    editMaster1.shortcutCode.$setValidity('exists', true);
                }
                $scope.shortCutInUI = master.shortcutCode;
                var count = 0;
                var exist = false;
                for (var itr = 0; itr < $scope.detailOfMaster.masterDataBeans.length; itr++) {
                    if ($scope.detailOfMaster.masterDataBeans[itr].shortcutCode !== undefined && $scope.detailOfMaster.masterDataBeans[itr].shortcutCode !== null) {
                        for (var innerLoop = 0; innerLoop < itr; innerLoop++) {
                            if (parseInt($scope.detailOfMaster.masterDataBeans[itr].shortcutCode) === parseInt($scope.detailOfMaster.masterDataBeans[innerLoop].shortcutCode)) {
                                exist = true;
                                break;
                            }
                        }
                    }
                }
                if (!exist) {
                    for (var i = 0; i < $scope.refArr.length; i++) {
                        if ($scope.refArr[i] !== undefined && $scope.refArr[i] !== null) {
                            $scope.refArr[i].shortcutCode.$setValidity('exists', true);
                        }
                    }
                }
                if ($scope.shortCutInUI && $scope.shortCutInUI !== '') {
                    for (var i = 0; i < $scope.detailOfMaster.masterDataBeans.length; i++) {
                        if ($scope.detailOfMaster.masterDataBeans[i].shortcutCode) {
                            if ($scope.detailOfMaster.masterDataBeans[i].shortcutCode.toString() === $scope.shortCutInUI) {
                                count++;
                                if (count === 2) {
                                    $scope.shortCutInDB = true;
                                    editMaster1.shortcutCode.$setValidity('exists', false);
                                    break;
                                }
                            }
                        }
                    }
                }
            };
//            $scope.alphabat = function (row) {
//                if (row.value && row.value !== "") {
//                    var letters = "[0-9a-zA-Z]+";
//                    if (row.value.match(letters)) {
//                        var val = parseInt(row.value.toString().charAt(0));
//                        if (row.value.length > 1)
//                            var last = row.value.toString().substring(1);
//                        if (val <= 9 || val >= 0) {
//                            if (last) {
//                                row.value = "";
//                            } else {
//                                row.value = "";
//                            }
//                        }
//                    }
//                }
//            };
            $scope.masterValueExists = function (master, editMaster1, index) {
                $scope.masterValueInDB = false;
                $scope.submitted = false;
                if (editMaster1) {
                    editMaster1.masterValue.$setValidity('exists', true);
                }
                var exist = false;
                for (var itr = 0; itr < $scope.detailOfMaster.masterDataBeans.length; itr++) {
                    if ($scope.detailOfMaster.masterDataBeans[itr].value !== undefined && $scope.detailOfMaster.masterDataBeans[itr].value !== null) {
                        for (var innerLoop = 0; innerLoop < itr; innerLoop++) {
                            if ($scope.detailOfMaster.masterDataBeans[innerLoop].value !== undefined && $scope.detailOfMaster.masterDataBeans[innerLoop].value !== null) {
                                if (($scope.detailOfMaster.masterDataBeans[itr].value.toUpperCase()) === ($scope.detailOfMaster.masterDataBeans[innerLoop].value.toUpperCase())) {
                                    exist = true;
                                    break;
                                }
                            }
                        }
                    }
                }
                if (!exist) {

                    for (var i = 0; i < $scope.refArr.length; i++) {
                        if ($scope.refArr[i] !== undefined && $scope.refArr[i] !== null) {
                            $scope.refArr[i].masterValue.$setValidity('exists', true);
                        }
                    }
                }
                if (master.value !== undefined && master.value !== null && master.value.length > 0) {
                    $scope.masterValueInUI = master.value.toUpperCase();
                    var count = 0;
                    for (var i = 0; i < $scope.detailOfMaster.masterDataBeans.length; i++) {
                        if ($scope.detailOfMaster.masterDataBeans[i].value) {
                            if ($scope.detailOfMaster.masterDataBeans[i].value.toUpperCase() === $scope.masterValueInUI) {
                                count++;
                                if (count === 2) {
                                    $scope.masterValueInDB = true;
                                    editMaster1.masterValue.$setValidity('exists', false);
                                    break;
                                }
                            }
                        }
                    }
                }

            };
            $scope.refArr = [];
            $scope.getReference = function (form, index) {
                $scope.refArr.push(form);
            };
            $scope.removeReference = function (obj) {
                var i;
                $.each($scope.refArr, function (index, b) {
                    if (b === obj) {
                        i = index;
                    }
                });
                if (i) {
                    $scope.refArr.splice(i, 1);
                }
            };
            $scope.saveMaster = function (editMasterForm) {
                $scope.submitted = true;
                if ($scope.editMasterForm.editMaster1) {
                    if ($scope.editMasterForm.editMaster1.masterValue.$error.exists)
                    {
                        $scope.submitted = false;
                    }
                }

                if (editMasterForm.$valid) {
                    $scope.submitted = false;
                    if ($scope.shortCutInDB === false && $scope.masterValueInDB === false && $scope.shortCutCodeLength === false)
                    {
                        $rootScope.maskLoading();
                        if ($scope.detailOfMaster.masterDataBeans) {
                            var shortCutCodeList = [];
                            angular.forEach($scope.detailOfMaster.masterDataBeans, function (itr) {
                                if (!!(itr.shortcutCode && itr.shortcutCode.length > 0)) {
                                    shortCutCodeList.push(parseInt(itr.shortcutCode));
                                }
                            });
                            for (var i = 0; i < $scope.detailOfMaster.masterDataBeans.length; i++)
                            {
                                var item = $scope.detailOfMaster.masterDataBeans[i];
                                item.isArchieve = item.newArchieve;
                                delete item["newArchieve"];
                                if (!(!!item.shortcutCode)) {
                                    if (!!(shortCutCodeList && shortCutCodeList.length > 0)) {
                                        var maximum = Math.max.apply(Math, shortCutCodeList)
                                        item.shortcutCode = maximum + 1;
                                        shortCutCodeList.push(item.shortcutCode);
                                    } else {
                                        item.shortcutCode = 1;
                                        shortCutCodeList.push(parseInt(1));
                                    }
                                }

                            }
                        } else {
                            $scope.detailOfMaster.masterDataBeans = [];
                        }
                        var master = {
                            code: $scope.code,
                            isSensitiveMaster: $scope.isSensitiveMaster,
                            masterDataBeans: $scope.detailOfMaster.masterDataBeans
                        };
                        ManageMasterService.update(master, function (res) {
                            $scope.editMasterForm.$dirty = false;
                            $scope.cancel();
                            var msg = "Master updated successfully";
                            var type = $rootScope.success;
                            $rootScope.addMessage(msg, type);
                            ManageMasterService.retrieveListOfMaster(function (data) {
                                data = orderBy(data, ['masterName']);
                                if ($scope.selectedType)
                                {
                                    if ($scope.selectedType === '1') {
                                        $scope.listOfMaster = angular.copy($scope.builtInMaster);
                                    } else {
                                        $scope.listOfMaster = angular.copy($scope.customMaster);
                                    }
                                } else {
                                    $scope.listOfMaster = data;
                                }

                                $scope.masters = angular.copy($scope.listOfMaster);
                                if ($scope.selected) {
                                    $scope.sort($scope.selected);
                                } else {
                                    $scope.selected = 'All';
                                    $scope.sort($scope.selected);
                                }
                                $scope.refArr = [];
                                $rootScope.unMaskLoading();
                            }, function () {
                                var msg = "Failed to retrieve master";
                                var type = $rootScope.error;
                                $rootScope.addMessage(msg, type);
                                $rootScope.unMaskLoading();
                            });
                        }, function () {
                            var msg = "Could not update master, please try again.";
                            var type = $rootScope.error;
                            $rootScope.addMessage(msg, type);
                            $rootScope.unMaskLoading();
                        });
                    }
                }
            };
            $scope.cancel = function () {
                $scope.passwordPopUpForm.$setPristine();
                $scope.refArr = [];
                $scope.code = '';
                $scope.submitted = false;
                $scope.password = '';
                $scope.masterValueInDB = false;
                $scope.searchPage = false;
                $scope.isSensitiveMaster = false;
                $scope.inValidPassword = false;
                $('#scrollable-dropdown-menu.typeahead').typeahead('val', '');
                if ($scope.detailOfMaster && $scope.detailOfMaster.lenth > 0)
                {
                    $scope.detailOfMaster.masterDataBeans = [];
                }
                $scope.masterList = true;
                $scope.editMaster = false;
                $scope.languageList = [];
            };
            $scope.retrieve = function (code) {
                ManageMasterService.retrieveDetailsOfMaster({primaryKey: code}, function (response) {
                    $scope.detailOfMaster = response;
                    $scope.detailOfMasterInDB = angular.copy(response);
                });
            };
            $scope.isShowEditForm = function (desigval, showEdit, iscopy) {
                if (desigval && desigval !== "") {
                    $scope.isEditing = showEdit;
                    $scope.isCopy = iscopy;
                    for (var i = 0; i < $scope.listOfMaster.length; i++)
                    {
                        if ($scope.listOfMaster[i].code.toUpperCase() === desigval.toString().toUpperCase()) {
                            $scope.listOfMaster = $scope.listOfMaster[i];
                        }

                    }
                }
            };
            $scope.filterTypeahead = function () {
                var searchDesigFilterData = $filter('filter')($scope.listOfMaster, {masterName: $scope.searchMaster});
                if (searchDesigFilterData.length > 0) {
                    $scope.isInValidSearchDesig = false;
                }
                else {
                    $scope.isInValidSearchDesig = true;
                }
            };
            $scope.setSearchflag = function (searchedMaster) {
                var enteredText = $('#scrollable-dropdown-menu.typeahead').typeahead('val');
                if (enteredText.length > 0) {
                    if (enteredText.length < 3) {
                        $scope.searchedMasterRecord = [];
                        $scope.searchPage = true;
                    } else {
                        $scope.searchedMasterRecord = angular.copy(searchedMaster);
                        $scope.searchPage = true;
                        $scope.masterList = true;
                    }
                }
            };
            $scope.setMasterForEdit = function (master) {
                $scope.serchedList;
                var editData = {};
                if (master.id)
                {
                    editData.code = master.id;
                }
                else {
                    editData.code = master;
                }
                for (var i = 0; i < $scope.masters.length; i++)
                {
                    if (editData.code === $scope.masters[i].code)
                    {
                        editData.isSensitiveMaster = $scope.masters[i].isSensitiveMaster;
                        editData.masterName = $scope.masters[i].masterName;
                    }
                }
                $scope.showConfirmationPopup(editData);
            };
            $scope.addRow = function () {
                $scope.submitted = false;
                if ($scope.detailOfMaster.masterDataBeans === undefined || $scope.detailOfMaster.masterDataBeans === null) {
                    $scope.detailOfMaster = {};
                    $scope.detailOfMaster.masterDataBeans = [];
                    var max = 0;
                }
                else {
                    var maxShortCutCode = parseInt($scope.detailOfMaster.masterDataBeans[0].shortcutCode);
                    if (!(!!(maxShortCutCode))) {
                        maxShortCutCode = 0;
                    }
                    for (var itr = 1; itr < $scope.detailOfMaster.masterDataBeans.length; itr++) {
                        if ($scope.detailOfMaster.masterDataBeans[itr].shortcutCode !== undefined && $scope.detailOfMaster.masterDataBeans[itr].shortcutCode !== null) {
                            if (maxShortCutCode < parseInt($scope.detailOfMaster.masterDataBeans[itr].shortcutCode)) {
                                maxShortCutCode = $scope.detailOfMaster.masterDataBeans[itr].shortcutCode;
                            }
                        }
                    }
                    max = parseInt(maxShortCutCode);
                }
                $scope.detailsFilled = true;
                $scope.detailOfMaster.masterDataBeans.push({
                    id: null, code: null, masterName: null, usedInFeature: null, "password": null, "masterType": null, "isSensitiveMaster": false, "shortcutCode": max + 1, "value": null, "isOftenUsed": false, "valueEntityId": null, "masterDataBeans": null, "isArchieve": false, "newArchieve": false,
                    langaugeIdNameMap: {}
                });
            };
            $scope.removeRow = function (obj, form) {
                $scope.removeReference(form);
                var i;
                $.each($scope.detailOfMaster.masterDataBeans, function (index, b) {
                    if (b === obj) {
                        i = index;
                    }
                });
                if (i) {
                    $scope.detailOfMaster.masterDataBeans.splice(i, 1);
                }
            };
            $scope.languageChange = function (id) {
                $scope.langNotSelected = true;
                if (id) {
                    $scope.selectedId = id;
                    $scope.langNotSelected = false;
                }
            };
            $scope.passwordChange = function () {
                $scope.submitted = false;
            };
            $scope.initAddExceptionForm = function (addExceptionForm) {
                $scope.addExceptionForm = addExceptionForm;
            };
            $scope.addExceptionPopup = function (master) {
                $rootScope.maskLoading();
                $scope.code = master.code;
                $scope.instanceId = master.id;
                $scope.masterName = master.masterName;
                $scope.editMaster = false;
                $scope.addExceptionPage = true;
                $scope.searchPage = false;
                $scope.masterList = false;
                $scope.valueException = {};
                $scope.count = 0;
                $scope.initUsers();
                $scope.retrieveValuesFromMasterCode();
                if ($scope.instanceId !== undefined) {
                    $scope.retrievePrerequisite($scope.instanceId);
                }
//                $scope.retrieveDependentOnFields($scope.instanceId);
//                $scope.retrieveValueExceptions($scope.instanceId);
                $rootScope.unMaskLoading();
            };

            $scope.retrievePrerequisite = function (instanceId) {
                $scope.finalFieldList = [];
                ManageMasterService.retrievePrerequisite(instanceId, function (result) {
                    if (result !== undefined && result !== null && result.data !== undefined && result.data !== null) {
                        var data = result.data;
                        if (data['dependentFieldList'] !== undefined && data['dependentFieldList'] !== null && data['dependentFieldList'].length > 0) {
                            $scope.fieldDataBeanList = data['dependentFieldList'];
                            if ($scope.fieldDataBeanList.length > 0) {
                                angular.forEach($scope.fieldDataBeanList, function (item) {
                                    $scope.finalFieldList.push({id: item.value + "|" + item.description, text: item.label});
                                });
                                if ($scope.finalFieldList.length > 0) {
                                    $scope.initDependentOnValues();
                                }
                            }
                        }
                        if (data['valueExceptionDataBeans'] !== undefined && data['valueExceptionDataBeans'] !== null && data['valueExceptionDataBeans'].length > 0) {
                            $scope.finalExceptionList = [];
                            var cnter = 0;
                            angular.forEach(data['valueExceptionDataBeans'], function (item) {
                                item.index = cnter++;
                                item.isArchive = false;
                                $scope.finalExceptionList.push(item);
                                $scope.exceptionList.push(item);
                            });
                            if ($scope.exceptionList !== undefined && $scope.exceptionList.length > 0) {
                                $scope.tempfinalFieldList = [];
                                angular.forEach($scope.exceptionList, function (list) {
                                    list.isUpdated = false;
                                    angular.forEach($scope.finalFieldList, function (item) {
                                        if (list.dependentOnField == item.id) {
                                            var count = 0;
                                            angular.forEach($scope.tempfinalFieldList, function (tempItem) {
                                                if (item.id === tempItem.id) {
                                                    count++;
                                                }
                                            });
                                            if (count === 0) {
                                                $scope.tempfinalFieldList.push({id: item.id, text: item.text});
                                            }
                                        }
                                    });
                                    if ($scope.tempfinalFieldList.length > 0) {
                                        $scope.finalFieldList = angular.copy($scope.tempfinalFieldList);
                                        $scope.initDependentOnValues();
                                    }
                                });
                            }
                        }
                    } else {
                        $scope.initDependentOnValues();
                    }
                });
            };
            $scope.retrieveValuesFromMasterCode = function () {
                ManageMasterService.retrieveDetailsOfMaster({primaryKey: $scope.code}, function (response) {
                    $scope.detailOfMaster = response;
                    $scope.valueList = [];
                    $scope.valueList.push({id: 0, text: "All Values"});
                    $scope.detailOfMaster.masterDataBeans = orderBy($scope.detailOfMaster.masterDataBeans, ['-isOftenUsed', 'shortcutCode', 'value']);
                    if ($scope.detailOfMaster.masterDataBeans) {
                        for (var i = 0; i < $scope.detailOfMaster.masterDataBeans.length; i++)
                        {
                            var item = $scope.detailOfMaster.masterDataBeans[i];
                            item.newArchieve = item.isArchieve;
                            $scope.valueList.push({id: item.valueEntityId, text: item.value});
                        }
                    }
                    if ($scope.multiValues === undefined) {
                        $scope.initValues();
                    } else {
                        $scope.multiValues.data.splice(0, $scope.multiValues.data.length);
                        if ($scope.valueList.length > 0) {
                            angular.forEach($scope.valueList, function (item) {
                                $scope.multiValues.data.push(item);
                            });
                        }
                    }
                    $rootScope.unMaskLoading();
                });
            };
            //For tooltip
            $scope.popover =
                    "<NOBR><font color='red;'>Use the shortcuts to search</font></NOBR><br/><table cellpadding='0' cellspacing='0'><tr><td>\ " +
                    "\n\ " +
                    "'@D'  </td> <td> &nbsp;  &nbsp;</td><td align='left'>Departments</td></tr>\ " +
                    "<tr><td>'@E'  </td> <td> &nbsp; &nbsp;</td><td align='left'>Employees</td></tr>\ " +
                    "<tr><td>'@R'  </td> <td> &nbsp; &nbsp;</td><td align='left'>Designations</td></tr>\ " +
                    "</table>\ ";
            $scope.initUsers = function () {
                //For recipients,
                $scope.autoCompleteUsers = {
                    multiple: true,
                    closeOnSelect: false,
                    placeholder: 'Select Users',
                    initSelection: function (element, callback) {
                        if ($scope.editExceptionFlag) {
                            var data = [];
                            var userIdSplit = $scope.tempValueException.forUsers.split(",");
                            var userToBeDisplaySplit = $scope.tempValueException.userToBeDisplay.split(",");
                            for (var i = 0; i < userIdSplit.length; i++) {
                                data.push({
                                    id: userIdSplit[i],
                                    text: userToBeDisplaySplit[i]
                                });
                            }
                            callback(data);
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
                                if (selected.substring(0, 2) === '@E' || selected.substring(0, 2) === '@e') {
                                    data.push({value: "All", description: "All", label: "All Users"});
                                }
                                $scope.names = data;
                                angular.forEach(data, function (item) {
                                    var flag = 0;
                                    angular.forEach($scope.forUserList, function (user) {
                                        if ((item.value + ":" + item.description) == (user.value + ":" + user.description)) {
                                            flag++;
                                        }
                                    });
                                    if (flag === 0) {
                                        $scope.forUserList.push(item);
                                    }

                                });
                                angular.forEach(data, function (item) {
                                    if (item.value === "All" && item.description === "All") {
                                        $scope.names.push({
                                            id: "0" + ":" + item.description,
                                            text: item.label
                                        });
                                    } else {
                                        $scope.names.push({
                                            id: item.value + ":" + item.description,
                                            text: item.label
                                        });
                                    }
//                                    
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
                            Messaging.retrieveUserList(search.trim(), success, failure);
                        } else if (selected.substring(0, 2) === '@R' || selected.substring(0, 2) === '@r') {
                            var search = query.term.slice(2);
                            Messaging.retrieveRoleList(search.trim(), success, failure);
                        } else if (selected.substring(0, 2) === '@D' || selected.substring(0, 2) === '@d') {
                            var search = query.term.slice(2);
                            Messaging.retrieveDepartmentList(search.trim(), success, failure);
                        } else if (selected.length > 0) {
                            var search = selected;
                            Messaging.retrieveUserList(search.trim(), success, failure);
                        } else {
                            query.callback({
                                results: $scope.names
                            });
                        }
                    }
                };
            };
            $scope.initValues = function () {
                $scope.multiValues = {
                    multiple: true,
                    closeOnSelect: false,
                    placeholder: 'Select Values',
                    data: $scope.valueList,
                    initSelection: function (element, callback) {
                        if ($scope.editExceptionFlag) {
                            var data = [];
                            var valueIdSplit = $scope.tempValueException.forValue.split(",");
                            var valueToBeDisplaySplit = $scope.tempValueException.valueToBeDisplay.split(",");
                            for (var j = 0; j < valueIdSplit.length; j++) {
                                data.push({
                                    id: valueIdSplit[j],
                                    text: valueToBeDisplaySplit[j]
                                });
                            }
                            callback(data);
                        }
                    },
                    formatResult: function (item) {
                        return item.text;
                    }

                };
            };
            $scope.initDependentValueList = function () {
                $scope.multiDependentValueList = {
                    multiple: true,
                    closeOnSelect: false,
                    placeholder: 'Select Values',
                    data: $scope.fieldValueList,
                    initSelection: function (element, callback) {
                        if ($scope.editExceptionFlag) {
                            var data = [];
                            if ($scope.tempValueException !== undefined && $scope.tempValueException.dependentOnField !== undefined && $scope.tempValueException.dependentOnFieldValues !== undefined && $scope.tempValueException.dependentOnFieldValues !== null && $scope.tempValueException.dependentOnFieldValues !== '' && $scope.tempValueException.dependentOnFieldValues instanceof Object === false && !angular.isArray($scope.tempValueException.dependentOnFieldValues)) {
                                var dependentValuesIdSplit = $scope.tempValueException.dependentOnFieldValues.split(",");
                                var dependentValuesSplit = $scope.tempValueException.dependentOnFieldValuesToBeDisplay.split(",");
                                for (var j = 0; j < dependentValuesIdSplit.length; j++) {
                                    var cnt = 0;
                                    angular.forEach($scope.fieldValueList, function (val) {
                                        if (val.id == dependentValuesIdSplit[j]) {
                                            cnt++;
                                        }
                                    });
                                    if (cnt !== 0) {
                                        data.push({
                                            id: dependentValuesIdSplit[j],
                                            text: dependentValuesSplit[j].split("-")[1]
                                        });
                                    }

                                }
                                callback(data);
                            }

                        }
                    },
                    formatResult: function (item) {
                        return item.text;
                    }

                };
            };
            $scope.initDependentOnValues = function () {
                $scope.autoCompleteDependentOnFields = {
                    multiple: false,
                    closeOnSelect: false,
                    placeholder: "Select dependent On",
                    allowClear: true,
                    data: function () {
                        if ($scope.finalFieldList.length > 0) {
                            return {'results': $scope.finalFieldList};
                        } else {
                            return {'results': []};
                        }

                    },
                    formatResult: function (item) {
                        return item.text;
                    }
                };
            };
            $scope.$watch("valueException.dependentOnField", function () {
                if ($scope.valueException.dependentOnField !== undefined && $scope.valueException.dependentOnField instanceof Object === true && angular.isArray($scope.valueException.dependentOnField)) {
                } else {
                    if ($scope.fieldValueList !== undefined) {
                        $scope.fieldValueList.splice(0, $scope.fieldValueList.length);
                    }
                    $scope.retrieveFieldValues();
                }

            });
//            $scope.retrieveDependentOnFields = function (instanceId) {
//                ManageMasterService.retrieveCustomFields(instanceId, function (result) {
//                    $scope.fieldDataBeanList = result.data;
//                    $scope.finalFieldList = [];
//                    if ($scope.fieldDataBeanList.length > 0) {
//                        angular.forEach($scope.fieldDataBeanList, function (item) {
//                            $scope.finalFieldList.push({id: item.value + "|" + item.description, text: item.label});
//                        });
//                        if ($scope.finalFieldList.length > 0) {
//                            $scope.initDependentOnValues();
//                        }
//
//                    }
//
//                });
//            };
            $scope.retrieveFieldValues = function () {
                if ($scope.valueException.dependentOnField !== undefined && $scope.valueException.dependentOnField !== null && $scope.valueException.dependentOnField instanceof Object === false && $scope.valueException.dependentOnField !== '') {
                    $scope.dependentOn = $scope.valueException.dependentOnField.split('|');
                    $scope.payload = {
                        fieldId: $scope.dependentOn[0],
                        componentType: $scope.dependentOn[1]
                    };
                    ManageMasterService.retrieveCustomFieldsValueByKey($scope.payload, function (res) {
                        $scope.fieldValueList = [];
                        if (res !== undefined && res.data !== undefined && res.data !== null && res.data.length > 0) {
                            angular.forEach(res.data, function (item) {
                                $scope.fieldValueList.push({id: item.value, text: item.label})
                            });
                            $scope.initDependentValueList();
                        }

                    });
                }
            };
            $scope.retrieveFieldValuesForDisplay = function (dependentOn) {
                $scope.payload = {
                    fieldId: dependentOn[0],
                    componentType: dependentOn[1]
                };
                ManageMasterService.retrieveCustomFieldsValueByKey($scope.payload, function (res) {
                    $scope.fieldVals = [];
                    if (res !== undefined && res.data !== undefined && res.data !== null && res.data.length > 0) {
                        angular.forEach(res.data, function (item) {
                            $scope.fieldVals.push({id: item.value, text: item.label})
                        });
                    }
                });
            };
            $scope.addException = function (addExceptionForm) {
                $scope.submitted = true;
                if (!addExceptionForm.$valid) {
                } else if ((($scope.valueException.dependentOnFieldValues !== undefined && $scope.valueException.dependentOnFieldValues !== null && $scope.valueException.dependentOnFieldValues !== '') || ($scope.valueException.forUsers !== undefined && $scope.valueException.forUsers !== null && $scope.valueException.forUsers !== ''))) {
                    $scope.submitted = false;
                    $scope.count = $scope.finalExceptionList.length;
                    $scope.valueException.index = $scope.count++;
                    $scope.valueException.instanceId = $scope.instanceId;
                    $scope.finalExceptionList.push(angular.copy($scope.valueException));
                    $scope.valueExceptionToBeDisplayed = angular.copy($scope.valueException);
                    if ($scope.valueException.forUsers !== undefined && $scope.forUserList.length > 0) {
                        var userName = $scope.valueException.forUsers.split(',');
                        var userToBeDisplay = '';
                        for (var i = 0; i < userName.length; i++) {
                            angular.forEach($scope.forUserList, function (user) {
                                var userFullName;
                                if (user.value === "All") {
                                    userFullName = "0:" + user.description;
                                } else {
                                    userFullName = user.value + ":" + user.description;
                                }
                                if (userName[i] === userFullName) {
                                    if (userToBeDisplay === '') {
                                        userToBeDisplay = userToBeDisplay + user.label;
                                    } else {
                                        userToBeDisplay = userToBeDisplay + " , " + user.label;
                                    }

                                }
                            });
                        }
                        $scope.valueExceptionToBeDisplayed.userToBeDisplay = userToBeDisplay;
                    }
                    if ($scope.valueException.forValue !== undefined && $scope.valueList.length > 0) {
                        var valueName = $scope.valueException.forValue.split(',');
                        var valueToBeDisplay = '';
                        for (var i = 0; i < valueName.length; i++) {
                            angular.forEach($scope.valueList, function (value) {
                                var fullValueName = value.id;
                                if (valueName[i] == fullValueName) {
                                    if (valueToBeDisplay === '') {
                                        valueToBeDisplay = valueToBeDisplay + value.text;
                                    } else {
                                        valueToBeDisplay = valueToBeDisplay + " , " + value.text;
                                    }

                                }
                            });
                        }
                        $scope.valueExceptionToBeDisplayed.valueToBeDisplay = valueToBeDisplay;
                    }
                    if ($scope.valueException.dependentOnField !== undefined && $scope.finalFieldList.length > 0) {
                        var dependentOnToBeDisplay = '';
                        angular.forEach($scope.finalFieldList, function (field) {
                            var fullDependentOnField = field.id;
                            if ($scope.valueException.dependentOnField == fullDependentOnField) {
                                if (dependentOnToBeDisplay === '') {
                                    dependentOnToBeDisplay = dependentOnToBeDisplay + field.text;
                                } else {
                                    dependentOnToBeDisplay = dependentOnToBeDisplay + " , " + field.text;
                                }

                            }
                        });
                        $scope.valueExceptionToBeDisplayed.dependentOnToBeDisplay = dependentOnToBeDisplay;
                    }
                    if ($scope.valueException.dependentOnFieldValues !== undefined && $scope.fieldValueList.length > 0) {
                        var dependentOnFieldValuesName = $scope.valueException.dependentOnFieldValues.split(',');
                        var dependentOnFieldValuesToBeDisplay = '';
                        for (var i = 0; i < dependentOnFieldValuesName.length; i++) {
                            angular.forEach($scope.fieldValueList, function (fieldValue) {
                                var fulldependentOnFieldValuesName = fieldValue.id;
                                if (dependentOnFieldValuesName[i] == fulldependentOnFieldValuesName) {
                                    if (dependentOnFieldValuesToBeDisplay === '') {
                                        dependentOnFieldValuesToBeDisplay = dependentOnFieldValuesToBeDisplay + $scope.valueExceptionToBeDisplayed.dependentOnToBeDisplay + "-" + fieldValue.text;
                                    } else {
                                        dependentOnFieldValuesToBeDisplay = dependentOnFieldValuesToBeDisplay + " , " + $scope.valueExceptionToBeDisplayed.dependentOnToBeDisplay + "-" + fieldValue.text;
                                    }

                                }
                            });
                        }
                        $scope.valueExceptionToBeDisplayed.dependentOnFieldValuesToBeDisplay = dependentOnFieldValuesToBeDisplay;
                    }
                    $scope.valueExceptionToBeDisplayed.isArchive = false;
                    $scope.exceptionList.push(angular.copy($scope.valueExceptionToBeDisplayed));
                    $scope.cancelException(addExceptionForm);
                } else {
                    $rootScope.addMessage("Select For User or Dependent On to apply Exception", $rootScope.failure);
                    $scope.submitted = false;
                }
            };
            $scope.removeException = function (exception) {
                exception.isArchive = true;
            };
            $scope.undoException = function (exception) {
                exception.isArchive = false;
            };
            $scope.cancelExceptionPage = function () {
                $scope.passwordPopUpForm.$setPristine();
                $scope.masterName = '';
                $scope.editMaster = false;
                $scope.addExceptionPage = false;
                $scope.editExceptionFlag = false;
                $scope.valueException = {};
                $scope.exceptionList = [];
                $scope.count = 0;
                $scope.code = '';
                $scope.forUserList = [];
                $scope.submitted = false;
                $scope.masterValueInDB = false;
                $scope.searchPage = false;
                $scope.isSensitiveMaster = false;
                if ($scope.addExceptionForm !== undefined) {
                    $scope.addExceptionForm.$setPristine();
                }
                $('#scrollable-dropdown-menu.typeahead').typeahead('val', '');
                if ($scope.detailOfMaster && $scope.detailOfMaster.lenth > 0)
                {
                    $scope.detailOfMaster.masterDataBeans = [];
                }
                $scope.masterList = true;
                $scope.editMaster = false;
                $scope.languageList = [];
            };
            $scope.cancelException = function () {
                $scope.valueException = {};
                $scope.submitted = false;
                $scope.addExceptionForm.$setPristine();
                $("#forUser").select2("data", "");
                $("#forValue").select2("data", "");
                $("#dependentOnField").select2("data", "");
                $scope.editExceptionFlag = false;
                if ($scope.exceptionList !== undefined && $scope.exceptionList.length > 0) {
                    $scope.tempfinalFieldList = [];
                    angular.forEach($scope.exceptionList, function (list) {
                        list.isUpdated = false;
                        angular.forEach($scope.finalFieldList, function (item) {
                            if (list.dependentOnField == item.id) {
                                var count = 0;
                                angular.forEach($scope.tempfinalFieldList, function (tempItem) {
                                    if (item.id === tempItem.id) {
                                        count++;
                                    }
                                });
                                if (count === 0) {
                                    $scope.tempfinalFieldList.push({id: item.id, text: item.text});
                                }
                            }
                        });
                        if ($scope.tempfinalFieldList.length > 0) {
                            $scope.finalFieldList = angular.copy($scope.tempfinalFieldList);
                            $scope.initDependentOnValues();
                        }
                    });
                } else {
                    $scope.cancelExceptionPage();
                }
            };
            function removeCompleted(exceptionList) {
                return $.grep(exceptionList, function (ex) {
                    return ex.isArchive == false || (ex.isArchive == true && ex.id !== undefined && ex.id !== null);
                });
            }

            $scope.saveException = function () {
                if ($scope.exceptionList !== undefined && $scope.exceptionList.length > 0) {
                    $scope.listToSend = [];
                    $scope.finalListToSend = [];
                    $scope.listToSend = removeCompleted($scope.exceptionList);
                    angular.forEach($scope.listToSend, function (listItems) {
                        angular.forEach($scope.finalExceptionList, function (finalEx) {
                            if (listItems.index == finalEx.index) {
                                $scope.finalListToSend.push(finalEx);
                            }
                        });
                    });
                    ManageMasterService.saveException($scope.finalListToSend, function () {
                        console.log("sucess");
                        $scope.cancelExceptionPage();
                    });
                }
            };
            $scope.retrieveExceptionForUpdate = function (exception) {
                $scope.editExceptionFlag = true;
                angular.forEach($scope.finalExceptionList, function (finalEx) {
                    if (finalEx.index == exception.index) {
                        $scope.send = {};
                        $scope.uiSelectRecipients = [];
                        exception.isUpdated = true;
                        $scope.valueException = angular.copy(finalEx);
                        $scope.tempValueException = angular.copy(exception);
                        if (exception.forUsers !== undefined && exception.forUsers !== null && exception.forUsers !== '') {
                            var userIdSplit = exception.forUsers.split(",");
                            var userToBeDisplaySplit = exception.userToBeDisplay.split(",");
                            var userData = [];
                            for (var i = 0; i < userIdSplit.length; i++) {
                                userData.push({
                                    id: userIdSplit[i],
                                    text: userToBeDisplaySplit[i]
                                });
                            }
                            $("#forUser").select2("val", userData);
                        }
                        var valueIdSplit = exception.forValue.split(",");
                        var valueToBeDisplaySplit = exception.valueToBeDisplay.split(",");
                        var valData = [];
                        for (var j = 0; j < valueIdSplit.length; j++) {
                            valData.push({
                                id: valueIdSplit[j],
                                text: valueToBeDisplaySplit[j]
                            });
                        }
                        if (exception.dependentOnFieldValues !== undefined && exception.dependentOnFieldValues !== null && exception.dependentOnFieldValues !== '') {
                            var dependentValuesIdSplit = exception.dependentOnFieldValues.split(",");
                            var dependentValuesSplit = exception.dependentOnFieldValuesToBeDisplay.split(",");
                            var depValData = [];
                            for (var j = 0; j < dependentValuesIdSplit.length; j++) {
                                depValData.push({
                                    id: dependentValuesIdSplit[j],
                                    text: dependentValuesSplit[j]
                                });
                            }
                            $("#dependentOnFieldValues").select2("val", depValData);
                        }

                        $("#forValue").select2("val", valData);
                        $("#dependentOnField").select2("val", exception.dependentOnField);
                    }
                });
            };
            $scope.updateException = function (addExceptionForm) {
                $scope.submitted = true;
                if (!addExceptionForm.$valid) {
                    $scope.submitted = false;
                } else if ((($scope.valueException.dependentOnFieldValues !== undefined && $scope.valueException.dependentOnFieldValues !== null && $scope.valueException.dependentOnFieldValues !== '') || ($scope.valueException.forUsers !== undefined && $scope.valueException.forUsers !== null && $scope.valueException.forUsers !== ''))) {
                    $scope.tempValueExceptionObj = angular.copy($scope.valueException);
                    if ($scope.tempValueExceptionObj.forUsers !== undefined) {
                        var userName = '';
                        if (angular.isArray($scope.tempValueExceptionObj.forUsers)) {
                            var uid = '';
                            angular.forEach($scope.tempValueExceptionObj.forUsers, function (uItem) {
                                if (uid !== '') {
                                    uid = uid + "," + uItem.id;
                                } else {
                                    uid = uid + uItem.id;
                                }
                            });
                            userName = uid;
                        } else {
                            userName = $scope.tempValueExceptionObj.forUsers;
                        }
                        $scope.tempValueExceptionObj.forUsers = userName;
                    }
                    if ($scope.tempValueExceptionObj.forValue !== undefined) {
                        var valueName = '';
                        if (angular.isArray($scope.tempValueExceptionObj.forValue)) {
                            var vid = '';
                            angular.forEach($scope.tempValueExceptionObj.forValue, function (vItem) {
                                if (vid !== '') {
                                    vid = vid + "," + vItem.id;
                                } else {
                                    vid = vid + vItem.id;
                                }
                            });
                            valueName = vid;
                        } else {
                            valueName = $scope.tempValueExceptionObj.forValue;
                        }
                        $scope.tempValueExceptionObj.forValue = valueName;
                    }
                    if ($scope.tempValueExceptionObj.dependentOnFieldValues !== undefined) {
                        var dependentOnFieldValuesName = '';
                        if (angular.isArray($scope.tempValueExceptionObj.dependentOnFieldValues)) {
                            var did = '';
                            angular.forEach($scope.tempValueExceptionObj.dependentOnFieldValues, function (dItem) {
                                if (did !== '') {
                                    did = did + "," + dItem.id;
                                } else {
                                    did = did + dItem.id;
                                }
                            });
                            dependentOnFieldValuesName = did;
                        } else {
                            dependentOnFieldValuesName = $scope.tempValueExceptionObj.dependentOnFieldValues;
                        }
                        $scope.tempValueExceptionObj.dependentOnFieldValues = dependentOnFieldValuesName;
                    }
                    angular.forEach($scope.finalExceptionList, function (finalItem) {
                        if ($scope.tempValueExceptionObj.index == finalItem.index) {
                            finalItem.forUsers = $scope.tempValueExceptionObj.forUsers;
                            finalItem.forValue = $scope.tempValueExceptionObj.forValue;
                            finalItem.dependentOnField = $scope.tempValueExceptionObj.dependentOnField;
                            finalItem.dependentOnFieldValues = $scope.tempValueExceptionObj.dependentOnFieldValues;
                        }
                    });
                    angular.forEach($scope.exceptionList, function (exceptObj) {
                        if (exceptObj.index == $scope.tempValueExceptionObj.index) {
                            exceptObj.isUpdated = false;
                            $scope.tempValueExceptionObj.isUpdated = false;
                            exceptObj.forUsers = $scope.tempValueExceptionObj.forUsers;
                            exceptObj.forValue = $scope.tempValueExceptionObj.forValue;
                            exceptObj.dependentOnField = $scope.tempValueExceptionObj.dependentOnField;
                            exceptObj.dependentOnFieldValues = $scope.tempValueExceptionObj.dependentOnFieldValues;
//                            exceptObj = angular.copy($scope.tempValueExceptionObj);
                            if ($scope.tempValueExceptionObj.forUsers !== undefined && $scope.forUserList.length > 0) {
                                var userName = '';
                                if (angular.isArray($scope.tempValueExceptionObj.forUsers)) {
                                    var uid = '';
                                    angular.forEach($scope.tempValueExceptionObj.forUsers, function (uItem) {
                                        if (uid !== '') {
                                            uid = uid + "," + uItem.id;
                                        } else {
                                            uid = uid + uItem.id;
                                        }
                                    });
                                    userName = uid.split(',');
                                } else {
                                    userName = $scope.tempValueExceptionObj.forUsers.split(',');
                                }
                                var userToBeDisplay = '';
                                for (var i = 0; i < userName.length; i++) {
                                    angular.forEach($scope.forUserList, function (user) {
                                        var userFullName = user.value + ":" + user.description;
                                        if (userName[i] === userFullName) {
                                            if (userToBeDisplay === '') {
                                                userToBeDisplay = userToBeDisplay + user.label;
                                            } else {
                                                userToBeDisplay = userToBeDisplay + " , " + user.label;
                                            }

                                        }
                                    });
                                }
                                exceptObj.userToBeDisplay = userToBeDisplay;
                            }
                            if ($scope.tempValueExceptionObj.forValue !== undefined && $scope.valueList.length > 0) {
                                var valueName = '';
                                if (angular.isArray($scope.tempValueExceptionObj.forValue)) {
                                    var vid = '';
                                    angular.forEach($scope.tempValueExceptionObj.forValue, function (vItem) {
                                        if (vid !== '') {
                                            vid = vid + "," + vItem.id;
                                        } else {
                                            vid = vid + vItem.id;
                                        }
                                    });
                                    valueName = vid.split(',');
                                } else {
                                    valueName = $scope.tempValueExceptionObj.forValue.split(',');
                                }
                                var valueToBeDisplay = '';
                                for (var i = 0; i < valueName.length; i++) {
                                    angular.forEach($scope.valueList, function (value) {
                                        var fullValueName = value.id;
                                        if (valueName[i] == fullValueName) {
                                            if (valueToBeDisplay === '') {
                                                valueToBeDisplay = valueToBeDisplay + value.text;
                                            } else {
                                                valueToBeDisplay = valueToBeDisplay + " , " + value.text;
                                            }

                                        }
                                    });
                                }
                                exceptObj.valueToBeDisplay = valueToBeDisplay;
                            }
                            if ($scope.tempValueExceptionObj.dependentOnField !== undefined && $scope.finalFieldList.length > 0) {
                                var dependentOnToBeDisplay = '';
                                angular.forEach($scope.finalFieldList, function (field) {
                                    var fullDependentOnField = field.id;
                                    if ($scope.tempValueExceptionObj.dependentOnField == fullDependentOnField) {
                                        if (dependentOnToBeDisplay === '') {
                                            dependentOnToBeDisplay = dependentOnToBeDisplay + field.text;
                                        } else {
                                            dependentOnToBeDisplay = dependentOnToBeDisplay + " , " + field.text;
                                        }

                                    }
                                });
                                exceptObj.dependentOnToBeDisplay = dependentOnToBeDisplay;
                            }
                            if ($scope.tempValueExceptionObj.dependentOnFieldValues !== undefined && $scope.fieldValueList.length > 0) {
                                var dependentOnFieldValuesName = '';
                                if (angular.isArray($scope.tempValueExceptionObj.dependentOnFieldValues)) {
                                    var did = '';
                                    angular.forEach($scope.tempValueExceptionObj.dependentOnFieldValues, function (dItem) {
                                        if (did !== '') {
                                            did = did + "," + dItem.id;
                                        } else {
                                            did = did + dItem.id;
                                        }
                                    });
                                    dependentOnFieldValuesName = did.split(',');
                                } else {
                                    dependentOnFieldValuesName = $scope.tempValueExceptionObj.dependentOnFieldValues.split(',');
                                }
//                                var dependentOnFieldValuesName = $scope.tempValueExceptionObj.dependentOnFieldValues.split(',');
                                var dependentOnFieldValuesToBeDisplay = '';
                                for (var i = 0; i < dependentOnFieldValuesName.length; i++) {
                                    angular.forEach($scope.fieldValueList, function (fieldValue) {
                                        var fulldependentOnFieldValuesName = fieldValue.id;
                                        if (dependentOnFieldValuesName[i] == fulldependentOnFieldValuesName) {
                                            if (dependentOnFieldValuesToBeDisplay === '') {
                                                dependentOnFieldValuesToBeDisplay = dependentOnFieldValuesToBeDisplay + exceptObj.dependentOnToBeDisplay + "-" + fieldValue.text;
                                            } else {
                                                dependentOnFieldValuesToBeDisplay = dependentOnFieldValuesToBeDisplay + " , " + exceptObj.dependentOnToBeDisplay + "-" + fieldValue.text;
                                            }

                                        }
                                    });
                                }
                                exceptObj.dependentOnFieldValuesToBeDisplay = dependentOnFieldValuesToBeDisplay;
                            }
                        }
                    });
                    $scope.cancelException(addExceptionForm);
                    $scope.editExceptionFlag = false;
                } else {
                    $rootScope.addMessage("Select For User or Dependent On to Apply Exception", $rootScope.failure);
                    $scope.submitted = false;
                }
            };
//            $scope.retrieveValueExceptions = function (id) {
//                if (id !== undefined) {
//                    $scope.finalExceptionList = [];
//                    var cnter = 0;
//                    ManageMasterService.retrieveValueExceptions(id, function (res) {
//                        if (res.data !== undefined && res.data !== null && res.data.length > 0) {
//                            angular.forEach(res.data, function (item) {
//                                item.index = cnter++;
//                                item.isArchive = false;
//                                $scope.finalExceptionList.push(item);
//                                $scope.exceptionList.push(item);
//                            });
//                        }
//                    });
//                }
//            };
        }]);
});
