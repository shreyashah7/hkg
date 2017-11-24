

define(['angular'], function () {
    globalProvider.compileProvider.directive('addMasterValue',
            ['$parse', '$compile', '$rootScope', '$filter', 'MasterService', 'CenterMasterService', 'MessagingService', '$modal', function ($parse, $compile, $rootScope, $filter, MasterService, CenterMasterService, MessagingService, $modal) {
                    var scope = {
                        modelValue: '=',
                        records: '=',
                        masterCode: '@'
                    };
                    var link = function (scope, element, attrs) {
                    };
                    var controller = ["$scope", "$element", "$attrs", function ($scope, $element, $attrs) {

                            $scope.allMasters = [];
                            $scope.flags = {};
                            $scope.forms = {};
                            $scope.langNotSelected = true;
                            $scope.currentMaster = {};
                            $scope.shortCutInDB = false;
                            $scope.masterValueInDB = false;
                            $scope.separator = '_A_';
                            $scope.count = 0;
                            $scope.forUserList = [];
                            $scope.exceptionList = [];
                            $scope.finalExceptionList = [];
                            $scope.valueException = {};
                            //Function to escape meta-characters from id in jquery.
                            function escapeMetaChars(selector) {
                                return selector.replace(/(!|"|#|\$|%|\'|\(|\)|\*|\+|\,|\.|\/|\:|\;|\?|@)/g, function ($1, $2) {
                                    return "\\" + $2;
                                });
                            }

                            if (angular.isDefined($attrs.elementId)) {
                                $scope.elementId = $attrs.elementId;
                                $scope.elementId = escapeMetaChars($scope.elementId);
                            } else {
                                $scope.elementId = null;
                            }
                            if (angular.isDefined($attrs.isShortcutRequired)) {
                                try {
                                    $scope.isShortcutRequired = JSON.parse($attrs.isShortcutRequired.toLowerCase());
                                } catch (exception) {
                                    $scope.isShortcutRequired = false;
                                    console.log('Can not parse isShortcutRequired' + exception);
                                }
                            } else {
                                $scope.isShortcutRequired = false;
                            }
                            if (angular.isDefined($attrs.isCheckBox)) {
                                try {
                                    $scope.isCheckBox = JSON.parse($attrs.isCheckBox.toLowerCase());
                                } catch (exception) {
                                    $scope.isCheckBox = false;
                                    console.log('Can not parse isCheckBox' + exception);
                                }
                            } else {
                                $scope.isCheckBox = false;
                            }
                            if (angular.isDefined($attrs.modalName)) {
                                $scope.modalName = $attrs.modalName;
                                if ($attrs.modalName === '') {
                                    $scope.modalName = null;
                                }
                            } else {
                                $scope.modalName = null;
                            }
                            if (angular.isDefined($attrs.isCustom)) {
                                try {
                                    $scope.isCustom = JSON.parse($attrs.isCustom.toLowerCase());
                                } catch (exception) {
                                    $scope.isCustom = false;
                                    console.log('Can not parse isCustom' + exception);
                                }
                            } else {
                                $scope.isCustom = false;
                            }
                            if (angular.isDefined($attrs.isObject)) {
                                try {
                                    $scope.isObject = JSON.parse($attrs.isObject.toLowerCase());
                                } catch (exception) {
                                    $scope.isObject = false;
                                    console.log('Can not parse isObject' + exception);
                                }
                            } else {
                                $scope.isObject = false;
                            }

                            if (angular.isDefined($attrs.isMultiselect)) {
                                try {
                                    $scope.isMultiselect = JSON.parse($attrs.isMultiselect.toLowerCase());
                                } catch (exception) {
                                    $scope.isMultiselect = false;
                                    console.log('Can not parse isMultiselect' + exception);
                                }
                            } else {
                                $scope.isMultiselect = false;
                            }
                            if (angular.isDefined($attrs.isDropdown)) {
                                try {
                                    $scope.isDropdown = JSON.parse($attrs.isDropdown.toLowerCase());
                                } catch (exception) {
                                    $scope.isDropdown = false;
                                    console.log('Can not parse isDropdown' + exception);
                                }
                            } else {
                                $scope.isDropdown = false;
                            }

                            if (angular.isDefined($attrs.isDiamond)) {
                                $scope.isDiamond = $attrs.isDiamond;
                            } else {
                                $scope.isDiamond = false;
                            }
                            var Service;
//                            if ($scope.isDiamond === true)
//                            {
                            // Center Call
//                                Service = CenterMasterService;
//                            }
//                            else
//                            {
                            // Master Call
                            Service = MasterService;
//                            }

//                            $attrs.$observe('fieldLabel', function (value) {
//                                if (value !== null && value !== undefined && value !== '') {
//                                    $scope.fieldLabel = value;
//                                }
//                                if ($scope.fieldLabel !== undefined && $scope.fieldLabel !== null && $scope.isCustom) {
//                                    $rootScope.maskLoading();
//
//                                    Service.retrieveCustomOfMaster($scope.fieldLabel, function (response) {
//                                        $scope.detailOfMaster = response;
//                                        $scope.shortCutCodes = [];
//                                        if ($scope.detailOfMaster.masterDataBeans === null) {
//                                            $scope.detailOfMaster.masterDataBeans = [];
//                                        }
//                                        $scope.masterCode = $scope.detailOfMaster.code;
//                                        $rootScope.unMaskLoading();
//                                    }, function (res) {
//                                        $rootScope.unMaskLoading();
//                                    });
//                                }
//                            });
                            var orderBy = $filter('orderBy');
                            $scope.initMasterChange = function () {
                                $scope.submitted = false;
                                $scope.valueSubmitted = false;
                                $scope.flags = {
                                    passwordEmpty: false,
                                    invalidPassword: false,
                                    shortcutExists: false,
                                    valueEmpty: false,
                                    valueExists: false
                                };
//                            console.log('masterCode11------' + $scope.masterCode);
//                            console.log('-----records' + JSON.stringify($scope.records));
//                            console.log('-----model value' + JSON.stringify($scope.modelValue));
                                if ($scope.records === undefined || $scope.records === null) {
                                    $scope.records = [];
                                }
                                if (($scope.masterCode !== undefined && $scope.masterCode !== null && $scope.masterCode !== '')) {
                                    $rootScope.maskLoading();
                                    Service.retrieveListOfMaster(function (data) {
                                        $scope.allMasters = data;
                                        Service.retrieveDetailsOfMaster({primaryKey: $scope.masterCode}, function (response) {
                                            $rootScope.unMaskLoading();
                                            $scope.detailOfMaster = response;
                                            $scope.shortCutCodes = [];
                                            if ($scope.detailOfMaster.masterDataBeans === null) {
                                                $scope.detailOfMaster.masterDataBeans = [];
                                            }
                                            angular.forEach($scope.detailOfMaster.masterDataBeans, function (masterDetail) {
                                                $scope.shortCutCodes.push(masterDetail.shortcutCode);
                                            });
                                            angular.forEach($scope.allMasters, function (master) {
                                                if (master.code == $scope.masterCode) {
                                                    $scope.masterName = master.masterName;
                                                    $scope.showConfirmationPopup(master);
                                                }
                                            });
                                            $scope.addExceptionPopup($scope.detailOfMaster);
                                        }, function (res) {
                                            $rootScope.unMaskLoading();
                                        });
                                    }, function (res) {
                                        var msg = "Could not retrieve master, please try again";
                                        var type = $rootScope.error;
                                        $rootScope.addMessage(msg, type);
                                        $rootScope.unMaskLoading();
                                    });
                                }

                            };
                            $scope.passwordChange = function () {
                                $scope.submitted = false;
                                $scope.flags.invalidPassword = false;
                                $scope.flags.passwordEmpty = false;
                                if ($scope.password === undefined || $scope.password === null || $scope.password === '') {
                                    $scope.flags.passwordEmpty = true;
                                }
                            };
                            $scope.proceed = function () {
                                $scope.submitted = false;
                                var password = $scope.password;
                                $scope.flags.passwordEmpty = false;
                                if (password !== undefined && password !== null && password.length > 0) {
                                    $rootScope.maskLoading();
                                    Service.authenticateForEditMaster(password, function (res) {
                                        $rootScope.unMaskLoading();
                                        if (res.data) {
                                            $scope.flags.invalidPassword = false;
                                            $scope.editMasterDetail();
                                            $scope.showPopup = false;
                                        } else {
                                            $scope.flags.invalidPassword = true;
                                            $scope.showPopup = true;
                                        }
                                    }, function (res) {
                                        $rootScope.unMaskLoading();
                                    });
                                } else {
                                    $scope.flags.passwordEmpty = true;
                                }
                            };
                            //Scoll and focus input field.
                            function scollAndFocus() {
                                setTimeout(function () {
                                    $($element).siblings().find('select:first, input:first').focus();
                                    $(window).scrollTop($($element).siblings().find('select:first, input:first').offset());
                                });
                            }

                            $scope.showConfirmationPopup = function (master) {
                                $scope.doNotScroll = false;
                                $scope.serchedList;
                                $scope.searchPage = false;
                                $scope.code = master.code;
                                $scope.masterName = master.masterName;
                                $scope.isSensitiveMaster = master.isSensitiveMaster;
                                if (master.isSensitiveMaster === true) {
                                    $scope.showPopup = true;
//                                console.log('$scope.showPopup----' + $scope.showPopup);
//                                    $("#passwordPopUp" + $scope.separator + $scope.masterCode + $scope.separator + ($scope.modalName !== null ? $scope.modalName : '')).modal('show');
                                    var t = "passwordPopUp.html";
                                    $scope.passwordModal = $modal.open({
                                        templateUrl: t,
                                        scope: $scope,
                                        size: 'lg'
                                    });
                                    $scope.passwordModal.result.then(function () {
                                        //success callback
                                    }, function () {
                                        //failure callback
                                        if ($scope.doNotScroll !== true) {
                                            scollAndFocus();
                                        }
                                    });
                                } else {
                                    $scope.showPopup = false;
//                                    $("#passwordPopUp" + $scope.separator + $scope.masterCode + $scope.separator + ($scope.modalName !== null ? $scope.modalName : '')).modal('show');
                                    var t = "passwordPopUp.html";
                                    $scope.passwordModal = $modal.open({
                                        templateUrl: t,
                                        scope: $scope,
                                        size: 'lg'
                                    });
                                    $scope.passwordModal.result.then(function () {
                                        //success callback
                                    }, function () {
                                        //failure callback
                                        if ($scope.doNotScroll !== true) {
                                            scollAndFocus();
                                        }
                                    });
                                    $scope.editMasterDetail();
                                }

                            };
                            $scope.hideConfirmationPopup = function () {
                                $scope.password = '';
                                $scope.submitted = false;
                                $scope.inValidPassword = false;
                                $scope.flags = {};
                                //$("#passwordPopUp" + $scope.separator + $scope.masterCode + $scope.separator + ($scope.modalName !== null ? $scope.modalName : '')).modal('hide');
                                $scope.passwordModal.dismiss();
                                $scope.cancelExceptionPage();
                                $rootScope.removeModalOpenCssAfterModalHide();
                            };
                            $scope.languageChange = function (id) {
                                $scope.langNotSelected = true;
                                if (id) {
                                    $scope.selectedId = id;
                                    $scope.langNotSelected = false;
                                }
                            };
                            $scope.editMasterDetail = function () {
                                $scope.submitted = false;
                                $scope.currentMaster = {};
                                $scope.currentMaster.langaugeIdNameMap = {};
                                if ($scope.shortCutCodes.length > 0) {
                                    var maximum = Math.max.apply(Math, $scope.shortCutCodes);
                                    $scope.currentMaster.shortcutCode = maximum + 1;
                                } else {
                                    $scope.currentMaster.shortcutCode = 1;
                                }
                            };
//                            $scope.alphabat = function (row) {
//                                if (row.value && row.value !== "") {
//                                    var letters = "[0-9a-zA-Z]+";
//                                    if (row.value.match(letters)) {
//                                        var val = parseInt(row.value.toString().charAt(0));
//                                        if (row.value.length > 1)
//                                            var last = row.value.toString().substring(1);
//                                        if (val <= 9 || val >= 0) {
//                                            if (last) {
//                                                row.value = "";
//                                            } else {
//                                                row.value = "";
//                                            }
//                                        }
//                                    }
//                                }
//                            };

                            $scope.masterValueExists = function (master, index) {
                                $scope.masterValueInDB = false;
                                $scope.submitted = false;
                                $scope.valueSubmitted = false;
//                                $scope.flags.valueEmpty = false;
                                if ($scope.forms.editMasterForm.editMaster1) {
                                    $scope.forms.editMasterForm.editMaster1.masterValue.$setValidity('exists', true);
                                }
                                if (master.value !== undefined && master.value !== null && master.value.length > 0) {
                                    $scope.masterValueInUI = master.value.toUpperCase().trim();
                                    var count = 0;
                                    for (var i = 0; i < $scope.detailOfMaster.masterDataBeans.length; i++) {
                                        if ($scope.detailOfMaster.masterDataBeans[i].value) {
                                            if ($scope.detailOfMaster.masterDataBeans[i].value.toUpperCase().trim() === $scope.masterValueInUI) {
                                                count++;
                                                if (count === 1) {
                                                    $scope.masterValueInDB = true;
//                                                    $scope.flags.valueExists = true;
                                                    $scope.forms.editMasterForm.editMaster1.masterValue.$setValidity('exists', false);
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                }
                                if ($scope.forms.editMasterForm.editMaster1.$valid) {
                                    var count = 0;
                                    angular.forEach($scope.valueList, function (item) {
                                        if (item.id == -1) {
                                            item.text = master.value;
                                            count++;
                                        }
                                    });
                                    if (count == 0) {
                                        $scope.valueList.push({id: -1, text: master.value});
                                    }
                                }
                            };
                            $scope.shortCutExists = function (master) {
                                $scope.shortCutInDB = false;
                                $scope.shortCutInUI = master.shortcutCode;
                                var count = 0;
                                $scope.flags.shortcutExists = false;
                                if ($scope.shortCutInUI && $scope.shortCutInUI !== '') {
                                    for (var i = 0; i < $scope.detailOfMaster.masterDataBeans.length; i++) {
                                        if ($scope.detailOfMaster.masterDataBeans[i].shortcutCode) {
                                            if ($scope.detailOfMaster.masterDataBeans[i].shortcutCode.toString() === $scope.shortCutInUI) {
                                                count++;
                                                if (count === 1) {
                                                    $scope.shortCutInDB = true;
                                                    $scope.flags.shortcutExists = true;
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                }
                            };
                            $scope.saveMaster = function () {
                                console.log($scope.forms.editMasterForm.editMaster1);
                                $scope.submitted = true;
                                $scope.valueSubmitted = true;
                                if ($scope.forms.editMasterForm.editMaster1) {
                                    if ($scope.forms.editMasterForm.editMaster1.masterValue.$error.exists)
                                    {
                                        $scope.valueSubmitted = false;
                                    }
                                }
//                                if ($scope.currentMaster.value !== undefined && $scope.currentMaster.value !== null && $scope.currentMaster.value.length > 0) {
//
//                                } else {
//                                    $scope.flags.valueEmpty = true;
//                                }

                                if ($scope.forms.editMasterForm.$valid) {
                                    $scope.valueSubmitted = false;
                                    $scope.submitted = false;
                                    if ($scope.shortCutInDB === false && $scope.masterValueInDB === false)
                                    {
                                        $rootScope.maskLoading();
                                        var shortCutCodeList = [];
                                        angular.forEach($scope.detailOfMaster.masterDataBeans, function (itr) {
                                            if (itr.shortcutCode !== undefined && itr.shortcutCode !== null && itr.shortcutCode !== '') {
                                                shortCutCodeList.push(parseInt(itr.shortcutCode));
                                            }
                                        });
                                        var item = $scope.currentMaster;
                                        item.isArchieve = false;
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
                                        $scope.detailOfMaster.masterDataBeans.push(item);
                                        var master = {
                                            code: $scope.code,
                                            isSensitiveMaster: $scope.isSensitiveMaster,
                                            masterDataBeans: $scope.detailOfMaster.masterDataBeans
                                        };
                                        Service.update(master, function (res) {
                                            Service.retrieveDetailsOfMaster({primaryKey: $scope.masterCode}, function (response) {
                                                $rootScope.unMaskLoading();
                                                $scope.detailOfMaster = response;
                                                $scope.shortCutCodes = [];
                                                angular.forEach($scope.detailOfMaster.masterDataBeans, function (masterDetail) {
                                                    $scope.shortCutCodes.push(masterDetail.shortcutCode);
                                                });
                                                $scope.detailOfMaster.masterDataBeans = $filter('orderBy')($scope.detailOfMaster.masterDataBeans, ['-isOftenUsed', 'shortcutCode', 'value']);
                                                angular.forEach($scope.detailOfMaster.masterDataBeans, function (item) {
                                                    if (item.shortcutCode == $scope.currentMaster.shortcutCode) {
                                                        $scope.valueEntityId = item.valueEntityId;
                                                    }
                                                });
                                                var currentValue = null;
                                                if ($scope.isObject) {
                                                    angular.forEach($scope.detailOfMaster.masterDataBeans, function (item) {
                                                        if (item.shortcutCode == $scope.currentMaster.shortcutCode) {
                                                            var selectItem = {};
                                                            selectItem.id = item.valueEntityId;
                                                            selectItem.text = item.value;
                                                            if (selectItem.text.length > $rootScope.maxValueForTruncate) {
                                                                selectItem.text = selectItem.text.substring(0, $rootScope.maxValueForTruncate) + '...';
                                                            }
                                                            selectItem.displayName = item.value;
                                                            currentValue = selectItem;
                                                            $scope.records.push(selectItem);
                                                        }
                                                    });
                                                } else if (!$scope.isCustom) {
                                                    if ($scope.isShortcutRequired) {
                                                        if (!$scope.isCheckBox) {
                                                            angular.forEach($scope.detailOfMaster.masterDataBeans, function (item) {
                                                                if (item.shortcutCode == $scope.currentMaster.shortcutCode) {
                                                                    var selectItem = {};
                                                                    selectItem.value = item.valueEntityId;
                                                                    selectItem.label = $rootScope.translateValue($scope.masterCode + "." + item.value);
                                                                    if (selectItem.label.length > $rootScope.maxValueForTruncate) {
                                                                        selectItem.label = selectItem.label.substring(0, $rootScope.maxValueForTruncate) + '...';
                                                                    }
                                                                    selectItem.shortcutCode = item.shortcutCode;
                                                                    currentValue = item.valueEntityId;
                                                                    $scope.records.push(selectItem);
                                                                }
                                                            });
                                                        } else {
                                                            angular.forEach($scope.detailOfMaster.masterDataBeans, function (item) {
                                                                if (item.shortcutCode == $scope.currentMaster.shortcutCode) {
                                                                    var selectItem = {};
                                                                    selectItem.value = item.valueEntityId;
                                                                    selectItem.label = $rootScope.translateValue($scope.masterCode + "." + item.value);
                                                                    if (selectItem.label.length > $rootScope.maxValueForTruncate) {
                                                                        selectItem.label = selectItem.label.substring(0, $rootScope.maxValueForTruncate) + '...';
                                                                    }
                                                                    selectItem.shortcutCode = item.shortcutCode;
                                                                    selectItem.isActive = true;
                                                                    $scope.records.push(selectItem);
                                                                }
                                                            });
                                                        }
                                                    } else {
                                                        angular.forEach($scope.detailOfMaster.masterDataBeans, function (item) {
                                                            if (item.shortcutCode == $scope.currentMaster.shortcutCode) {
                                                                var selectItem = {};
                                                                selectItem.id = item.valueEntityId;
                                                                selectItem.text = item.value;
                                                                if (selectItem.text.length > $rootScope.maxValueForTruncate) {
                                                                    selectItem.text = selectItem.text.substring(0, $rootScope.maxValueForTruncate) + '...';
                                                                }
                                                                selectItem.shortcutCode = item.shortcutCode;
                                                                currentValue = item.valueEntityId;
                                                                $scope.records.push(item);
                                                            }
                                                        });
                                                    }
                                                } else {
                                                    if ($scope.isMultiselect) {
                                                        var selectedValues = [];
                                                        var records = [];
                                                        angular.forEach($scope.records, function (record) {
                                                            var isExists = false;
                                                            angular.forEach(records, function (temp) {
                                                                if (record.id === temp.id) {
                                                                    isExists = true;
                                                                }
                                                            });
                                                            if (!isExists) {
                                                                records.push(record);
                                                            }
                                                        });
                                                        if ($scope.modelValue !== null && $scope.modelValue !== undefined && $scope.modelValue !== '' && !($scope.modelValue instanceof Object)) {
                                                            var selectedIds = $scope.modelValue.toString().split(",");
                                                            angular.forEach(selectedIds, function (id) {
                                                                angular.forEach(records, function (item) {
                                                                    if (parseInt(item.id) === parseInt(id.trim())) {
                                                                        selectedValues.push(item);
                                                                    }
                                                                });
                                                            });
                                                        }

                                                        angular.forEach($scope.detailOfMaster.masterDataBeans, function (item) {
                                                            if (item.shortcutCode == $scope.currentMaster.shortcutCode) {
                                                                var selectItem = {};
                                                                selectItem.id = item.valueEntityId;
                                                                selectItem.text = item.shortcutCode + '-' + item.value;
                                                                if (selectItem.text.length > $rootScope.maxValueForTruncate) {
                                                                    selectItem.text = selectItem.text.substring(0, $rootScope.maxValueForTruncate) + '...';
                                                                }
                                                                selectedValues.push(selectItem);
                                                                $scope.records.push(selectItem);
                                                            }
                                                        });
                                                        $("#" + $scope.elementId).select2('data', selectedValues);
                                                        if (selectedValues.length > 0) {
                                                            var res = null;
                                                            angular.forEach(selectedValues, function (item) {
                                                                if (res === null) {
                                                                    res = item.id;
                                                                } else {
                                                                    res += "," + item.id;
                                                                }
                                                            });
                                                            currentValue = res;
                                                        }
                                                    } else {
                                                        angular.forEach($scope.detailOfMaster.masterDataBeans, function (item) {
                                                            if (item.shortcutCode == $scope.currentMaster.shortcutCode) {
                                                                var selectItem = {};
                                                                selectItem.id = item.valueEntityId;
                                                                selectItem.text = item.shortcutCode + '-' + item.value;
                                                                if (selectItem.text.length > $rootScope.maxValueForTruncate) {
                                                                    selectItem.text = selectItem.text.substring(0, $rootScope.maxValueForTruncate) + '...';
                                                                }
                                                                currentValue = item.valueEntityId;
                                                                $scope.records.push(selectItem);
                                                            }
                                                        });
                                                        if ($scope.isDropdown) {
                                                            var codeWithSeparator = $scope.separator + $scope.masterCode + $scope.separator;
                                                            var currentId = 'passwordPopUp' + $scope.separator + $scope.masterCode + $scope.separator + ($scope.modalName !== null ? $scope.modalName : '');
                                                            var domElements = document.getElementsByTagName("*");
                                                            for (var i = domElements.length; i--; ) {
                                                                if ($(domElements[i])[0] !== $($element)[0]) {
                                                                    var elementId = $(domElements[i]).attr('id');
                                                                    if (elementId !== undefined) {
                                                                        if (elementId.toString().indexOf(codeWithSeparator) > -1 && elementId.toString() !== currentId) {
                                                                            var ctrlElement = $(domElements[i]).closest("[ng-controller]");
                                                                            //Call the method of external controller to update the list.
                                                                            angular.element(ctrlElement).scope().updateDropdownList($scope.records);
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                                $scope.saveException();
//                                            $scope.records = angular.copy($scope.detailOfMaster.masterDataBeans);
                                                if (!$scope.isCheckBox) {
                                                    $scope.modelValue = currentValue;
                                                }
//                                            console.log('-----' + JSON.stringify($scope.records));
//                                            console.log('-----#######' + JSON.stringify($scope.modelValue));
                                                $scope.submitted = false;
                                                $scope.hideConfirmationPopup();
                                            }, function (res) {
                                                $rootScope.unMaskLoading();
                                            });
                                        }, function () {
                                            $scope.doNotScroll = true;
                                            $scope.hideConfirmationPopup();
                                            var msg = "Could not update master, please try again.";
                                            var type = $rootScope.warning;
                                            $rootScope.addMessage(msg, type);
                                            $rootScope.unMaskLoading();
                                        });
                                    }
                                }
                            };
                            $scope.canAccess = function (feature) {
                                return $rootScope.canAccess(feature);
                            };

                            //-----**************************Exception Code starts from here********************************-----

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
                                $rootScope.unMaskLoading();
                            };

                            $scope.retrievePrerequisite = function (instanceId) {
                                $scope.finalFieldList = [];
                                Service.retrievePrerequisite(instanceId, function (result) {
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
                                Service.retrieveDetailsOfMaster({primaryKey: $scope.code}, function (response) {
                                    $scope.detailOfMasters = response;
                                    $scope.valueList = [];
                                    $scope.valueList.push({id: 0, text: "All Values"});
                                    $scope.detailOfMasters.masterDataBeans = orderBy($scope.detailOfMasters.masterDataBeans, ['-isOftenUsed', 'shortcutCode', 'value']);
                                    if ($scope.detailOfMasters.masterDataBeans) {
                                        for (var i = 0; i < $scope.detailOfMasters.masterDataBeans.length; i++)
                                        {
                                            var item = $scope.detailOfMasters.masterDataBeans[i];
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
                                            MessagingService.retrieveUserList(search.trim(), success, failure);
                                        } else if (selected.substring(0, 2) === '@R' || selected.substring(0, 2) === '@r') {
                                            var search = query.term.slice(2);
                                            MessagingService.retrieveRoleList(search.trim(), success, failure);
                                        } else if (selected.substring(0, 2) === '@D' || selected.substring(0, 2) === '@d') {
                                            var search = query.term.slice(2);
                                            MessagingService.retrieveDepartmentList(search.trim(), success, failure);
                                        } else if (selected.length > 0) {
                                            var search = selected;
                                            MessagingService.retrieveUserList(search.trim(), success, failure);
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
//                Service.retrieveCustomFields(instanceId, function (result) {
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
                                    Service.retrieveCustomFieldsValueByKey($scope.payload, function (res) {
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
                                Service.retrieveCustomFieldsValueByKey($scope.payload, function (res) {
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
                                    $scope.cancelException();
                                } else {
                                    $rootScope.addMessage("Select For User or Dependent On to apply Exception", $rootScope.failure);
                                    $scope.submitted = false;
                                    $scope.hideConfirmationPopup();
                                }
                            };
                            $scope.removeException = function (exception) {
                                exception.isArchive = true;
                            };
                            $scope.undoException = function (exception) {
                                exception.isArchive = false;
                            };
                            $scope.cancelExceptionPage = function () {
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
                                if ($scope.detailOfMasters && $scope.detailOfMasters.lenth > 0)
                                {
                                    $scope.detailOfMasters.masterDataBeans = [];
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
                                                if (finalEx.forValue == -1 && $scope.valueEntityId !== undefined) {
                                                    finalEx.forValue = $scope.valueEntityId;
                                                }
                                                $scope.finalListToSend.push(finalEx);
                                            }
                                        });
                                    });
                                    Service.saveException($scope.finalListToSend, function () {
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
                                            if ($scope.tempValueExceptionObj.dependentOnFieldValues !== undefined && $scope.fieldValueList !== undefined && $scope.fieldValueList.length > 0) {
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






                        }];
                    //ng-show="canAccess('mastersAddEdit')"
                    return {
                        restrict: 'E',
                        scope: scope,
                        link: link,
                        templateUrl: 'scripts/directives/master/master.tmpl.html',
                        controller: controller
                    };
                }]);
    globalProvider.provide.factory('MasterService', ['$resource', '$rootScope', function (resource, rootScope) {
            var Master = resource(rootScope.apipath + 'master/:action', {}, {
                retrieveListOfMaster: {
                    method: 'GET',
                    isArray: true,
                    params: {
                        action: 'retrieve'
                    }
                },
                retrieveDetailsOfMaster: {
                    method: 'POST',
                    params: {
                        action: 'retrieve'
                    }
                },
                update: {
                    method: 'POST',
                    params: {
                        action: 'update'
                    }
                },
                create: {
                    method: 'PUT',
                    params: {
                        action: 'create'
                    }
                },
                deleteById: {
                    method: 'POST',
                    params: {
                        action: 'delete'
                    }
                },
                retrieveSystemFeatures: {
                    method: 'GET',
                    isArray: true,
                    params: {
                        action: 'retrieve/systemfeatures'
                    }
                },
                authenticateForEditMaster: {
                    method: 'PUT',
                    params: {
                        action: 'checkpassword'
                    }
                },
                retrieveLanguage: {
                    method: 'GET',
                    params: {
                        action: 'retrievelanguages'
                    }
                },
                retrieveCustomOfMaster: {
                    method: 'POST',
                    isArray: false,
                    params: {
                        action: 'retrieveMasterValues'
                    }
                },
                retrieveCustomFieldsValueByKey: {
                    method: 'POST',
                    params: {
                        action: 'retrievecustomfieldsvaluebykey'
                    }
                },
                saveException: {
                    method: 'POST',
                    params: {
                        action: 'saveexception'
                    }
                },
                retrieveValueExceptions: {
                    method: 'POST',
                    params: {
                        action: 'retrievevalueexceptions'
                    }
                },
                retrievePrerequisite: {
                    method: 'POST',
                    params: {
                        action: 'retrieveprerequisites'
                    }
                }
            });
            return Master;
        }]);
    globalProvider.provide.factory('CenterMasterService', ['$resource', '$rootScope', function (resource, rootScope) {
            var Master = resource(rootScope.centerapipath + 'master/:action', {}, {
                retrieveListOfMaster: {
                    method: 'GET',
                    isArray: true,
                    params: {
                        action: 'retrieve'
                    }
                },
                retrieveDetailsOfMaster: {
                    method: 'POST',
                    params: {
                        action: 'retrieve'
                    }
                },
                update: {
                    method: 'POST',
                    params: {
                        action: 'update'
                    }
                },
                create: {
                    method: 'PUT',
                    params: {
                        action: 'create'
                    }
                },
                deleteById: {
                    method: 'POST',
                    params: {
                        action: 'delete'
                    }
                },
                retrieveSystemFeatures: {
                    method: 'GET',
                    isArray: true,
                    params: {
                        action: 'retrieve/systemfeatures'
                    }
                },
                authenticateForEditMaster: {
                    method: 'PUT',
                    params: {
                        action: 'checkpassword'
                    }
                },
                retrieveLanguage: {
                    method: 'GET',
                    params: {
                        action: 'retrievelanguages'
                    }
                },
                retrieveCustomOfMaster: {
                    method: 'POST',
                    isArray: false,
                    params: {
                        action: 'retrieveMasterValues'
                    }
                }
            });
            return Master;
        }]);

    globalProvider.provide.factory('MessagingService', ['$resource', '$rootScope', function (resource, rootScope) {
            var message = resource(rootScope.apipath + 'messaging/:action', {}, {
                retrieveUserList: {
                    method: 'POST',
                    isArray: true,
                    params: {
                        action: 'retrieve/users'
                    }
                },
                retrieveUserListFranchise: {
                    method: 'POST',
                    isArray: true,
                    params: {
                        action: 'retrieve/usersbyfranchise'
                    }
                },
                retrieveRoleList: {
                    method: 'POST',
                    isArray: true,
                    params: {
                        action: 'retrieve/roles'
                    }
                },
                retrieveRoleListFranchise: {
                    method: 'POST',
                    isArray: true,
                    params: {
                        action: 'retrieve/rolesbyfranchise'
                    }
                },
                retrieveDepartmentList: {
                    method: 'POST',
                    isArray: true,
                    params: {
                        action: 'retrieve/departments'
                    }
                },
                retrieveDepartmentListFranchise: {
                    method: 'POST',
                    isArray: true,
                    params: {
                        action: 'retrieve/departmentsbyfranchise'
                    }
                },
            });
            return message;
        }]);
});

/**
 * 
 * Author: Gautam
 * 
 * Objective : Add a master value on the same page only.
 * ** Required a permission "addMasterValueShortcut".
 * The dirctive may hide if the above permission is not granted.
 * 
 * Implementation:
 * 
 * <add-master-value ng-show="canAccess('addMasterValueShortcut')" class="col-md-2" modal-name="select_member_bld_grp" records="bloodGroups" model-value="employee.family[familyIndex].bloodGroup" master-code="BG" is-employee-feature="true"></add-master-value>
 * 
 * modal-name : (Optional) To change modal id in case we have more tha one master field with same code.
 * model-value : (Optional if is-check-box = 'true') Model name to be updated with new added value.
 * records : Existing records of values of selected master type. Will be updated with newly added value.
 * master-code : Code of master.
 * is-shortcut-required : (Optional)(Boolean) If value needs to be displayed with shortcut like in employee feature.
 * is-check-box : (Optional)(Boolean) If option values are in form of check box and multiple selection. (model-value not required here.)
 * is-custom : (Optional)(Boolean) If the specified field is a custom field.
 * is-object : (Optional)(Boolean) If model is a object like id, text, displayName. Illustration on leave reason.
 * is-multiselect : (Optional)(Boolean) In case model is a multi select input box.
 * element-id : (Required if is-multiselect is true). To set multiselect values.
 * is-dropdown : (Required if field is of pointer type pointing to dropdown or a dropdown type)(Boolean)
 * 
 */


