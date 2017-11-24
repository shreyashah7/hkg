/**
 * This controller is for manage asset feature
 * Author : Dhwani Mehta
 * Date : 20 June 2014
 */
define(['hkg', 'assetService', 'fileUploadService', 'addMasterValue', 'printBarcodeValue', 'dynamicForm'], function (hkg, assetService, messageService, fileUploadService) {
    hkg.register.controller('CategoryController', ["$rootScope", "$scope", "$location", "AssetService", "$timeout", "$http", "limitToFilter", "DynamicFormService", "FileUploadService", "$filter", function ($rootScope, $scope, $location, AssetService, $timeout, $http, limitToFilter, DynamicFormService, FileUploadService, $filter) {
            $rootScope.maskLoading();
            $rootScope.mainMenu = "manageLink";
            $rootScope.childMenu = "manageAssets";
            $scope.parentId = 0;
            $rootScope.activateMenu();
            $scope.categoryListDropdown = [];
            $scope.categoryDropdownList = [];
            $scope.selectedCategoryDropdown = {};
            $scope.categoryList = [];
            $scope.category = {};
            $scope.dbType = {};
            $scope.addAssetData = {};
            $scope.issueAssetData = {};
            $scope.dbTypeForIssue = {};
            $scope.categoryData = {};
            $scope.categoryData = DynamicFormService.resetSection($scope.categoryTemplate);
            $scope.isAssetInvalid = true;
            $scope.dbTypeForCategory = {};
            $scope.addAssetForm = {};
            $scope.categoryDropdown = [];
            $scope.purchaserList = [];
            $scope.addAssetCategory = false;
            $scope.reloadFlag = true;
            $scope.reload = true;
            $scope.assetModel = '';
//            $filter('number')()
            $scope.setViewFlag = function (flag) {
                if (flag != "search") {
                    $('#scrollable-dropdown-menu.typeahead').typeahead('val', '');
                }
                $scope.displayFlag = flag;
                $scope.submitted = false;
            };
            $scope.entity = "ASSET.";
            $scope.statusList = ["Active", "Remove"];
            $scope.assetNames = [];
            $scope.fileArray = [];
            $scope.fileNames = [];
            $scope.asset = {};
            $scope.asset.fileList = [];
            $scope.multiselecttree = {"text": "All Assets", "isChecked": false,
                items: []};
            $scope.fileCounter = 0;
            $scope.managedAsset = [];
            $scope.unmanagedAsset = [];
            $scope.selectedAssets = [];
            $scope.searchedAssetRecords = [];
            $scope.selectedCategory = [];
            $scope.manifactAvail = true;
            $scope.removeFileList = [];
            $scope.listOfModelsOfDateType = [];
            $scope.listOfModelsOfDateType2 = [];
            $scope.makeEditFlagFalse = function () {
                $scope.assetModel = '';
                $scope.editFlag = false;
                if ($scope.selectedCategory.currentNode != undefined) {
                    $scope.selectedCategory.currentNode.selected = undefined;
                }
                $scope.initCreateAsset();
            };
            $scope.categoryCustomData = {};
            $scope.categoryCustomData = DynamicFormService.resetSection($scope.categoryTemplate);

            $scope.initIssueAssetForm = function () {
                $scope.issueMaxDate = $rootScope.getCurrentServerDate();
                $scope.issueasset = {};
                $scope.assetList = [];
                $scope.selectedAssets = [];
                $scope.selectedInString = 'Select asset';
                $scope.assetUnitList = [];
                $scope.managedAsset = [];
                $scope.unmanagedAsset = [];
                $scope.combinedAssetList = [];
                $scope.issueAssetData = DynamicFormService.resetSection($scope.issueAssetTemplate);
                $scope.dbTypeForIssue = {};
                $scope.issueasset.parentName = "Select asset category";
                $scope.issueasset.issueTo = '';
                $scope.issueSubmitted = false;
                $scope.isAssetInvalid = true;
                $scope.issueasset.issuedOn = $rootScope.getCurrentServerDate();
            };

            $scope.printBarcode = function () {
                var popup = window.open($rootScope.appendAuthToken($rootScope.apipath + "asset/getimage/" + $scope.asset.barcode));
                popup.print();
            };

            $scope.allNames = [];
            $scope.select2Names = [];

            $scope.popover = "<NOBR><font color='red;'>Use the shortcuts to search</font></NOBR><br/><table cellpadding='0' cellspacing='0'><tr><td>\ " +
                    "\n\ " +
                    "'@D'  </td> <td> &nbsp;  &nbsp;</td><td align='left'>Departments</td></tr>\ " +
                    "<tr><td>'@E'  </td> <td> &nbsp; &nbsp;</td><td align='left'>Employees</td></tr>\ " +
                    "</table>\ ";
            $scope.searchpopover =
                    "<NOBR><font color='red;'>Use the shortcut to search</font></NOBR><br/><table cellpadding='0' cellspacing='0'><tr><td>\ " +
                    "\n\ " +
                    "'@C'  </td> <td> &nbsp;  &nbsp;</td><td align='left'>Categories</td></tr>\ " +
                    "</table>\ ";

            $scope.assetpopover = "<NOBR><font color='red;'>Use the shortcuts to search</font></NOBR><br/><table cellpadding='0' cellspacing='0'><tr><td>\ " +
                    "\n\ " +
                    "'@E'  </td> <td> &nbsp;  &nbsp;</td><td align='left'>Employees</td></tr>\ " +
                    "</table>\ ";

            $scope.$on('$viewContentLoaded', function () {
                $scope.setViewFlag('issue');
                $scope.retrieveAssetCategories();
                $scope.initIssueAssetForm();
                AssetService.cancelform();
//                $scope.selectedCategory.currentNode.selected = undefined;
                var templateData = DynamicFormService.retrieveSectionWiseCustomFieldInfo("manageAssets");
                templateData.then(function (section) {

                    // Method modified by Shifa Salheen on 17 April for implementing sequence number
                    $scope.customgeneralTemplateData = angular.copy(section['genralSection']);
                    $scope.genralDepartmentTemplate = $rootScope.getCustomDataInSequence($scope.customgeneralTemplateData);
                    if ($scope.genralDepartmentTemplate !== null && $scope.genralDepartmentTemplate !== undefined)
                    {
                        angular.forEach($scope.genralDepartmentTemplate, function (updateTemplate)
                        {
                            if (updateTemplate.type !== null && updateTemplate.type !== undefined && updateTemplate.type === 'date')
                            {
                                $scope.listOfModelsOfDateType.push(updateTemplate.model);
                            }
                        })
                    }
                    $scope.customIssueTemplateData = angular.copy(section['issueAsset']);
                    $scope.issueAssetTemplate = $rootScope.getCustomDataInSequence($scope.customIssueTemplateData);
                    $scope.customcategoryTemplateData = angular.copy(section['category']);
                    $scope.categoryTemplate = $rootScope.getCustomDataInSequence($scope.customcategoryTemplateData);
                    if ($scope.categoryTemplate !== null && $scope.categoryTemplate !== undefined)
                    {
                        angular.forEach($scope.categoryTemplate, function (updateTemplate)
                        {
                            if (updateTemplate.type !== null && updateTemplate.type !== undefined && updateTemplate.type === 'date')
                            {
                                $scope.listOfModelsOfDateType2.push(updateTemplate.model);
                            }
                        })
                    }
//                    $scope.genralDepartmentTemplate = section['genralSection'];
//                    $scope.issueAssetTemplate = section['issueAsset'];
//                    $scope.categoryTemplate = section['category'];
                }, function (reason) {
//                    console.log('Failed: ' + reason);
                }, function (update) {
//                    console.log('Got notification: ' + update);
                });

            });
            /* Method added by Shifa Salheen on 2 April 2015 for clearing custom fields like multiselect and usermultiselect
             which were not getting cleared after add operation.Also On reset or cancel custom fields need to be cleared
             And On edit from tree u need to reload the directive*/
            $scope.resetCustomFields = function ()
            {
                var templateData = DynamicFormService.retrieveSectionWiseCustomFieldInfo("manageAssets");
                templateData.then(function (section) {

                    // Method modified by Shifa Salheen on 20 April for implementing sequence number
                    $scope.customgeneralTemplateData = angular.copy(section['genralSection']);
                    $scope.genralDepartmentTemplate = $rootScope.getCustomDataInSequence($scope.customgeneralTemplateData);
                    $scope.customIssueTemplateData = angular.copy(section['issueAsset']);
                    $scope.issueAssetTemplate = $rootScope.getCustomDataInSequence($scope.customIssueTemplateData);
                    $scope.customcategoryTemplateData = angular.copy(section['category']);
                    $scope.categoryTemplate = $rootScope.getCustomDataInSequence($scope.customcategoryTemplateData);
                }, function (reason) {
                }, function (update) {
                });
            };
            $scope.setAssetForEdit = function (id) {
                AssetService.retriveAssetById(id, function (res) {
                    $scope.retrieveAsset(res);
                }, function () {
                    $rootScope.addMessage("Could not retrive asset , please try again.", 1);

                })
            }
            $scope.getSearchedAsset = function (list) {
                var enteredText = $('#scrollable-dropdown-menu.typeahead').typeahead('val');
                if (enteredText.length > 0) {
                    if (enteredText.length < 3) {
                        $scope.searchedAssetRecords = [];
                        $scope.setViewFlag('search');
                    } else {
                        $scope.searchedAssetRecords = angular.copy(list);
                        $scope.setViewFlag('search');
                    }
                }

            }

            $scope.retrieveManifacturer = function () {
                if ($scope.editFlag) {
                    AssetService.fillstatuslist($scope.asset.status, function (res) {
                        $scope.statuslist = res;
                    }, function () {
                    });
                }
                AssetService.retrievemanifacturer(function (res) {
                    $scope.manifacturerList = [];
                    if (res != null && angular.isDefined(res) && res.length > 0) {
                        angular.forEach(res, function (item) {
                            item.label = $rootScope.translateValue("MF." + item.label);
                            $scope.manifacturerList.push(item);
                        });
                    }

                    if ($scope.manifacturerList.length === 0) {
                        $scope.manifactAvail = false;
                        $rootScope.addMessage("First add manufacturer from masters then try to add/edit masters.", $rootScope.failure);
                    } else {
                        $scope.manifactAvail = true;
                    }
                    $scope.setViewFlag('create');
                }, function () {
                });
            };

            $scope.initCreateAsset = function () {
                $scope.addAssetCategory = false;
                if (!$scope.editFlag) {
                    $scope.asset = {};
                    $scope.asset.parentCategory = "Select asset category";
                    $scope.today();
                    $scope.editFlag = false;
                    $scope.asset.fileList = [];
                    $scope.dbType = {};
                    $scope.addAssetData = DynamicFormService.resetSection($scope.genralDepartmentTemplate);
                    $scope.categoryCustomData = DynamicFormService.resetSection($scope.categoryTemplate);
                    $scope.dbTypeForCategory = {};
                    $scope.asset.canGenerateBarcode = false;
                    $("#recipients123").select2("data", []);
                }
                $scope.retrieveManifacturer();
            };

            $scope.initForm = function (formname) {
                $scope.addAssetForm = formname;
                $scope.addAssetForm.$pristine;
                $scope.submitted = false;
                if ($scope.editFlag) {
                    $scope.initCreateAsset();
                }
            };

            $scope.retrieveAssetCategories = function (defaultSelected) {
                $rootScope.maskLoading();
                AssetService.retrieveCategories(function (res) {
//                    $scope.categoryList = res;
                    $scope.categoryList = [];
//                    $scope.categoryListDropdown = [];
                    if (res.length > 0) {
                        if (res !== null && angular.isDefined(res) && res.length > 0) {
                            angular.forEach(res, function (item) {
//                                item.displayName = $rootScope.translateValue("ASTCTG_NM." + item.displayName);
                                $scope.categoryList.push(item);
//                                $scope.categoryListDropdown.push(item)
                            });
//                            $.merge($scope.categoryListDropdown, angular.copy($scope.categoryList));
                        }
                        $scope.setDefaultCategory(defaultSelected);
                    } else {
                    }
                    $rootScope.unMaskLoading();
                }, function () {
                    $rootScope.unMaskLoading();
                });
            };

            $scope.unmanagedLabel = [];
            $scope.combinedAssetList = [];
            $scope.retrieveAssetsByCategory = function () {
                $scope.managedAsset = [];
                $scope.unmanagedAsset = [];
                $scope.combinedAssetList = [];
                AssetService.retrieveAssets($scope.category, function (res) {
                    $scope.assetList = res;
                    if ($scope.assetList.length > 0) {
                        angular.forEach($scope.assetList, function (asset) {
                            asset.modelName = $rootScope.translateValue("AST_NM." + asset.modelName);
                            if (asset.assetType) {
                                $scope.managedAsset.push(asset);
                            } else {
                                $scope.unmanagedAsset.push(asset);
                            }
                        });
                    }
                    $scope.combinedAssetList.push(
                            {
                                modelName: '<strong>Managed assets</strong>',
                                multiSelectGroup: true
                            });
                    if ($scope.managedAsset.length > 0) {
                        angular.forEach($scope.managedAsset, function (asset) {
                            $scope.combinedAssetList.push(asset);
                        });
                    }

                    if ($scope.unmanagedAsset.length > 0) {
                        $scope.combinedAssetList.push({
                            multiSelectGroup: false
                        }, {
                            modelName: '<strong>Non-Managed assets</strong>',
                            multiSelectGroup: true
                        });
                        angular.forEach($scope.unmanagedAsset, function (asset) {
                            $scope.combinedAssetList.push(asset);
                        });
                    }

                    $scope.combinedAssetList.push({
                        multiSelectGroup: false
                    },
                    {
                        multiSelectGroup: false
                    });
                }, function () {

                });
            };

            $scope.setDefaultCategory = function () {
                $scope.categoryListDropdown = [];
                $scope.asset.parentCategory = "Select asset category";
                $scope.issueasset.parentName = "Select asset category";
                $.merge($scope.categoryListDropdown, angular.copy($scope.categoryList));
            };

            $scope.assetCancel = function (addAssetForm) {
                $scope.initIssueAssetForm();
                $scope.setViewFlag('issue');
                AssetService.cancelform();
                $scope.isAssetInvalid = false;
                $scope.asset = {};
                $scope.initIssueAssetForm();
                $scope.editFlag = false;
                $scope.fileNames = [];
                if ($scope.selectedCategory.currentNode !== undefined) {
                    $scope.selectedCategory.currentNode.selected = undefined;
                }
                if ($scope.selectedCategoryDropdown.currentNode !== undefined) {
                    $scope.selectedCategoryDropdown.currentNode.selected = undefined;
                }
                addAssetForm.$setPristine();
            }

            $scope.categoryClick = function (selectedCategory) {
                $scope.category.parentId = selectedCategory.currentNode.id;
                $scope.category.parentName = selectedCategory.currentNode.displayName;
//                $scope.category.startIndex = 1;
            };

            $scope.selectCategory = function () {
                $rootScope.maskLoading();
                $scope.editFlag = false;
                $scope.setViewFlag('view');
                $scope.category = angular.copy($scope.selectedCategory.currentNode);
                $scope.parentId = $scope.category.parentId;
                AssetService.addCustomDataToCategoryDataBean($scope.category, function (res) {
                    $scope.categoryCustomData = res.categoryCustomData;
                    if (!!$scope.categoryCustomData) {
                        angular.forEach($scope.listOfModelsOfDateType2, function (listOfModel)
                        {
                            if ($scope.categoryCustomData.hasOwnProperty(listOfModel))
                            {
                                if ($scope.categoryCustomData[listOfModel] !== null && $scope.categoryCustomData[listOfModel] !== undefined)
                                {
                                    $scope.categoryCustomData[listOfModel] = new Date($scope.categoryCustomData[listOfModel]);
                                } else
                                {
                                    $scope.categoryCustomData[listOfModel] = '';
                                }
                            }
                        })
                    }
                });
                AssetService.retrieveAssets($scope.category, function (res) {
                    $rootScope.unMaskLoading();
                    $scope.assetList = [];
                    $scope.assetList = res;
                    if ($scope.assetList.length > 0) {
                        if ($scope.assetList.length > 0) {
                            angular.forEach($scope.assetList, function (asset) {
//                                console.log('asset name : ' + asset.modelName);
                                asset.modelName = $rootScope.translateValue("AST_NM." + asset.modelName);
                            });
                        }
                    }
                    $scope.categoryDropdown = [];
                    $scope.categoryDropdown.push({id: '0', displayName: 'None'});
                    $.merge($scope.categoryDropdown, angular.copy($scope.categoryList));
                    $scope.newDeptList = angular.copy($scope.categoryDropdown);
                    $scope.deleteSelectedNode($scope.newDeptList);


                }, function () {
                    $rootScope.unMaskLoading();
                    $scope.categoryDropdown = [];
                    $scope.categoryDropdown.push({id: '0', displayName: 'None'});
                    $.merge($scope.categoryDropdown, angular.copy($scope.categoryList));
                    $scope.newDeptList = angular.copy($scope.categoryDropdown);
                    $scope.deleteSelectedNode($scope.newDeptList);

                });
            };

            $scope.updateCategory = function (editCatgoryForm) {
//                if (editCatgoryForm.$valid) {
//                    var success = function() {
//                        $scope.retrieveAssetCategories();
//                    };
//                    AssetService.updatecategory($scope.category, success);
//                    $scope.setViewFlag('issue');
//                }
//                
                $scope.submitted = true;
                if (editCatgoryForm.$valid) {
                    $scope.category.displayName = $scope.category.originalName;
                    if ($scope.category.status === 'Remove') {
                        $scope.conformationForRemovecategory();
                    } else {
                        $scope.category.dbTypeForCategory = $scope.dbTypeForCategory;
                        $scope.category.categoryCustomData = $scope.categoryCustomData;
                        AssetService.updatecategory($scope.category, function (res) {
                            if (res.messages !== undefined) {
                                $scope.categoryExistsMsg = res.messages[0].message;
                                editCatgoryForm.availabelAsset.$setValidity("exists", false);

                            } else {
                                $scope.category = {};
                                editCatgoryForm.$setPristine();
                                $scope.retrieveAssetCategories();
                                $scope.initIssueAssetForm();
                                $scope.setViewFlag('issue');
                                $scope.initIssueAssetForm();
                            }

                        }, function () {
                            $rootScope.addMessage("Could not save details, please try again.", 1);
                        });
                    }
                }
            };

            // ---------------------- bootstrap date picker ----------------------
            $scope.today = function () {
                $scope.asset.purchaseDt = $rootScope.getCurrentServerDate();
                $scope.asset.inwardDt = $rootScope.getCurrentServerDate();
                $scope.asset.assetType = true;
                $scope.asset.canProduceImages = false;
                $scope.asset.canGenerateBarcode = false;
            };


            $scope.clear = function () {
                $scope.dt = null;
            };

            // Disable weekend selection
            $scope.disabled = function (date, mode) {
                return (mode === 'day' && (date.getDay() === 0 || date.getDay() === 6));
            };

//            $scope.toggleMin = function() {
//                $scope.maxDate = $rootScope.getCurrentServerDate();
//            };
//            $scope.toggleMin();
            $scope.toggleMin = function () {
                var currentYear = $rootScope.getCurrentServerDate();
                $scope.minFromDate = $rootScope.getCurrentServerDate();
                $scope.minToDate = $rootScope.getCurrentServerDate();
//
//                $scope.maxFromDate = new Date(currentYear.getFullYear(), 11, 31);
//                $scope.maxToDate = new Date(currentYear.getFullYear(), 11, 31);
                //     $scope.minToDate = new Date();
            };
            $scope.toggleMin();
            $scope.setMaxDate = function () {
                $scope.maxDate = $scope.holiday.endDt;
            }

            $scope.open = function ($event) {
                $event.preventDefault();
                $event.stopPropagation();

            };

            $scope.openInwartDt = function ($event) {
                $event.preventDefault();
                $event.stopPropagation();

            };

            $scope.dateOptions = {
                formatYear: 'yy',
                startingDay: 1
            };

            $scope.format = $rootScope.dateFormat;

            $scope.asset = {};
            $scope.assetClick = function (selectedCategory) {
                //alert("asset click");
                $scope.reloadFlag = false;
                $scope.$on('$locationChangeStart', function (event) {
                    if (selectedCategory !== null)
                        if (!confirm('You have unsaved changes, go back?')) {
                            event.preventDefault();
                        } else {
                            selectedCategory === null;
                        }
                });

                $scope.isAssetInvalid = true;
                if (!angular.equals(selectedCategory, {})) {
                    $scope.isAssetInvalid = false;
                }
                if ($scope.displayFlag === 'create') {
                    $scope.asset.category = selectedCategory.currentNode.id;
                    $scope.asset.parentCategory = selectedCategory.currentNode.displayName;
                    $scope.asset.prefix = selectedCategory.currentNode.categoryPrefix;
                    $scope.asset.serialNumber = selectedCategory.currentNode.startIndex;
                    $scope.asset.pattern = selectedCategory.currentNode.pattern;
                    if ($scope.asset.pattern !== undefined && $scope.asset.pattern !== null) {
                        $scope.min_length = $scope.asset.pattern.length;
                    }
                    $timeout(function () {
                        $scope.reloadFlag = true;
                    }, 100);

                }
                if ($scope.displayFlag === 'issue') {
                    $scope.issueasset.parentName = selectedCategory.currentNode.displayName;
                    $scope.issueasset.categoryId = selectedCategory.currentNode.id;
                    $scope.category = selectedCategory.currentNode;
                    $scope.assetUnitList = [];
                    $scope.retrieveAssetsByCategoryForIssue(success);
                }
                if ($scope.displayFlag === 'view') {
                    $scope.parentId = selectedCategory.currentNode.id;
                    $scope.category.parentId = selectedCategory.currentNode.id;
                    $scope.category.parentName = selectedCategory.currentNode.displayName;
                }
                var success = function () {
                    $scope.selectedInString = 'Select assets';
                    $scope.selectedAssets = [];
                };

//                if ($scope.displayFlag !== 'issue') {
//                    $scope.retrieveAssetsByCategory(success);
//                }
            };

            $scope.issueAssetMethod = function (issueAssetForm) {
                $scope.issueSubmitted = true;
                if (issueAssetForm.$valid && !$scope.isAssetInvalid) {
                    $scope.issueasset.assetDataBeanList = angular.copy($scope.selectedAssets);
                    $scope.issueasset.nonManagedAssetDataBeans = angular.copy($scope.assetUnitList);
                    $scope.issueasset.issueCustomData = $scope.issueAssetData;
                    $scope.issueasset.dbTypeForIssue = $scope.dbTypeForIssue;
                    var success = function () {
                        $scope.issueasset = {};
                        $scope.combinedAssetList = [];
                        $scope.issueasset.issueTo = '';
                        issueAssetForm.$setPristine();
                        $scope.issueSubmitted = false;
                        $scope.initIssueAssetForm();
                        $scope.retrieveAssetCategories();
                    }
                    if ($scope.assetList.length === 0) {
                        $rootScope.addMessage("Select atleast one asset", $rootScope.failure);
                    } else {
                        AssetService.issueAsset(angular.copy($scope.issueasset), success, function () {
                            $scope.issueasset = {};
                            $scope.combinedAssetList = [];
                            $scope.issueasset.issueTo = '';
                            issueAssetForm.$setPristine();
                            $scope.issueSubmitted = false;
                            $scope.initIssueAssetForm();
                        });
                    }


                } else {
                }

            };

            $scope.names = [];

            $scope.autoCompleteRecipient = {
                allowClear: true,
                multiple: true,
                closeOnSelect: false,
                placeholder: 'Select purchased by',
                initSelection: function (element, callback) {
//                    if ($scope.addAssetForm.recipients123.$dirty) {
//                        $scope.addAssetForm.recipients123.$dirty = false
//                    }
                    if ($scope.editFlag) {
                        var data = [];
                        angular.forEach($scope.purchaserList, function (recipient) {
//                            console.log('id : ' + recipient.recipientInstance + ":" + recipient.recipientType);
//                            console.log('text : ' + recipient.recipientValue);

                            data.push({
                                id: recipient.recipientInstance + ":" + recipient.recipientType,
                                text: recipient.recipientValue
                            });
                        });
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
                    if (selected.substring(0, 2) == '@E' || selected.substring(0, 2) == '@e') {
                        var search = query.term.slice(2);
                        AssetService.retrieveUserList(search.trim(), success, failure);
                    } else {
                        query.callback({
                            results: $scope.names
                        });
                    }
                }
            };

            $scope.autoCompleteIssueTo = {
//                allowClear: true,
                multiple: true,
                closeOnSelect: false,
                maximumSelectionSize: 1,
                placeholder: 'Select issue to',
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
                    if (selected.substring(0, 2) == '@E' || selected.substring(0, 2) == '@e') {
                        var search = query.term.slice(2);
                        AssetService.retrieveUserList(search.trim(), success, failure);
                    } else if (selected.substring(0, 2) == '@D' || selected.substring(0, 2) == '@d') {
                        var search = query.term.slice(2);
                        AssetService.retrieveDepartmentList(search.trim(), success, failure);
                    } else {
                        query.callback({
                            results: $scope.names
                        });
                    }
                }
            };

            $scope.retrieveAsset = function (asset) {
                $scope.editFlag = true;
                $scope.asset = angular.copy(asset);
                $scope.assetModel = $scope.asset.modelName;
                $scope.asset.assetType = asset.assetType.toString();
                $scope.asset.canProduceImages = asset.canProduceImages.toString();
                if (asset.pattern !== undefined && asset.pattern !== null) {
                    $scope.min_length = asset.pattern.length;
                }
                AssetService.addCustomDataToAssetDataBean(asset, function (res) {
                    $rootScope.unMaskLoading();
                    $scope.asset = res;
                    if ($scope.asset.inwardDt !== null) {
                        $scope.asset.inwardDt = new Date($scope.asset.inwardDt);
                    }
                    if ($scope.asset.purchaseDt !== null) {
                        $scope.asset.purchaseDt = new Date($scope.asset.purchaseDt);
                    }
                    if ($scope.asset.issuedOn !== null) {
                        $scope.asset.issuedOn = new Date($scope.asset.issuedOn);
                    }
                    $scope.purchaserList = res.purchaserDataBeanList;
                    $(document).find($("#recipients123")).select2("val", []);
                    $scope.addAssetData = $scope.asset.addAssetData;
                    if (!!$scope.addAssetData) {
                        angular.forEach($scope.listOfModelsOfDateType, function (listOfModel)
                        {
                            if ($scope.addAssetData.hasOwnProperty(listOfModel))
                            {
                                if ($scope.addAssetData[listOfModel] !== null && $scope.addAssetData[listOfModel] !== undefined)
                                {
                                    $scope.addAssetData[listOfModel] = new Date($scope.addAssetData[listOfModel]);
                                } else
                                {
                                    $scope.addAssetData[listOfModel] = '';
                                }
                            }
                        });
                    }
                }, function () {
                });
//                $scope.addAssetData = asset.addAssetData;
                $scope.setViewFlag('create');
            };


            $scope.viewAssetCategory = function () {
                $scope.addAssetCategory = true;
                $scope.categorySubmitted = false;
                $scope.categoryDropdownList = [];
                $scope.category = {};
                $scope.categoryData = DynamicFormService.resetSection($scope.categoryTemplate);
                $scope.categoryDropdownList.unshift({id: '0', displayName: 'None', selected: 'selected'});
                $.merge($scope.categoryDropdownList, angular.copy($scope.categoryList));
                $scope.category = angular.copy($scope.categoryDropdownList[0]);
                $scope.category.parentName = $scope.category.displayName;
                $scope.category.displayName = '';
//                $scope.selectedCategoryDropdown.currentNode = $scope.categoryDropdownList[0];
                $('#addcategoryPopUp').modal('show');
            };
            $scope.hideAssetCategory = function (addCategoryForm) {
                $scope.category = {};
                $scope.categoryData = DynamicFormService.resetSection($scope.categoryTemplate);
                addCategoryForm.$setPristine();
                $scope.categoryExistsMsg = '';
                $('#addcategoryPopUp').modal('hide');
                $rootScope.removeModalOpenCssAfterModalHide();
            };


            $scope.productSelection = '';
            $scope.Products = [{"name": "Apple"}, {"name": "Banana"}, {"name": "Carrort"}, {"name": "Dart"}];

            $scope.addCategory = function (addCategoryForm) {
                $scope.categorySubmitted = true;

                if (addCategoryForm.$valid) {
                    $scope.category.categoryCustomData = $scope.categoryData;
                    $scope.category.dbTypeForCategory = $scope.dbTypeForCategory;
                    if ($scope.category.startIndex === null) {
                        $scope.category.startIndex = 1;
                    }
                    AssetService.createcategory(angular.copy($scope.category), function (res) {
                        if (res.data !== undefined) {
                            if (res.data.categoryExsist !== undefined) {
                                $scope.categoryExistsMsg = res.data.categoryExsist;
                                addCategoryForm.categoryName.$setValidity("exists", false);

                            } else if (res.data.prefixExsist !== undefined) {
                                $scope.categoryPrefixMsg = res.data.prefixExsist;
                                addCategoryForm.categoryPrefix.$setValidity("exists1", false);
                            }
//                            console.log(res.data.categoryExsist);
                        } else {
                            $scope.category = {};
                            addCategoryForm.$setPristine();
                            $scope.retrieveAssetCategories();
                            $('#addcategoryPopUp').modal('hide');
                            $rootScope.removeModalOpenCssAfterModalHide();
                        }

                    }, function () {
                        $rootScope.addMessage("Could not save details, please try again.", 1);
                    });
                }
            };

            $scope.addAsset = function (addAssetForm, flow) {
                $scope.reload = false;
                $scope.submitted = true;
                if ($scope.editFlag) {
//                    $scope.asset.purchasedBy = $("#recipients123").select2("data");
                    if (addAssetForm.$valid) {
//                        var success = function() {
//                            $scope.retrieveAssetCategories();
//                            $scope.editFlag = false;
//                            $scope.fileArray = [];
//                            $scope.setViewFlag('issue');
//                        };
                        if ($scope.fileArray.length > 0) {
                            angular.forEach($scope.fileArray, function (file) {
//                                console.log(file.archiveStatus);
                            });
                            $.merge($scope.asset.fileDataBeans, angular.copy($scope.fileArray));
                        }
                        if ($scope.asset.status === 'D') {
                            if (Object.prototype.toString.call($scope.asset.purchasedBy) === '[object Array]') {
                                var purchaser = '';
                                angular.forEach($scope.asset.purchasedBy, function (asset) {
                                    if (purchaser === '') {
                                        purchaser = asset.id;
                                    } else {
                                        purchaser = purchaser + ',' + asset.id;
                                    }
                                });

                                $scope.asset.purchasedBy = purchaser;
                            }
                            $scope.conformationForRemoveAsset();
                        } else {
                            $scope.asset.dbType = $scope.dbType;
                            $scope.asset.addAssetData = $scope.addAssetData;
                            if (Object.prototype.toString.call($scope.asset.purchasedBy) === '[object Array]') {
                                var purchaser = '';
                                angular.forEach($scope.asset.purchasedBy, function (asset) {
                                    if (purchaser === '') {
                                        purchaser = asset.id;
                                    } else {
                                        purchaser = purchaser + ',' + asset.id;
                                    }
                                });

                                $scope.asset.purchasedBy = purchaser;
                            }
                            AssetService.updateasset(angular.copy($scope.asset), function (res) {
                                $scope.resetCustomFields();
                                $scope.reload = true;
                                if (res.messages !== undefined) {
                                    $scope.categoryExistsMsg = res.messages[0].message;
                                    addAssetForm.serialNumber.$setValidity("exists", false);

                                } else {
                                    AssetService.cancelform();
                                    $scope.retrieveAssetCategories();
                                    $scope.editFlag = false;
                                    $scope.fileArray = [];
                                    $scope.fileNames = [];
                                    $scope.initIssueAssetForm();
                                    $scope.setViewFlag('issue');
                                    $scope.initCreateAsset();
                                    $scope.submitted = false;
                                }


                            }, function () {
                                $rootScope.addMessage("Could not save details, please try again.", 1);
                            });
                        }
                    } else {
                    }
                } else {
                    if (addAssetForm.$valid) {
                        $scope.asset.dbType = $scope.dbType;
                        $scope.asset.addAssetData = $scope.addAssetData;

                        if (!$scope.asset.assetType) {
                            $scope.asset.serialNumber = undefined;
                        }
                        if ($scope.fileNames.length > 0) {
                            $.merge($scope.asset.fileList, angular.copy($scope.fileNames));
                        }
                        AssetService.createasset(angular.copy($scope.asset), function (res) {
                            $scope.resetCustomFields();
                            $scope.reload = true;
                            if (res.messages !== undefined) {
                                $scope.categoryExistsMsg = res.messages[0].message;
                                if (addAssetForm.serialNumber !== undefined) {
                                    addAssetForm.serialNumber.$setValidity("exists", false);
                                }

                            } else {
                                AssetService.cancelform();
                                $scope.retrieveAssetCategories();
                                $scope.editFlag = false;
                                $scope.initCreateAsset();
                                $scope.setDefaultCategory('issue');
                                $scope.fileNames = [];
                                flow.cancel();
                                $scope.submitted = false;
                                addAssetForm.$setPristine();
                            }

                        }, function () {
                            $rootScope.addMessage("Could not save details, please try again.", 1);
                        });
                    }
//                    if (addAssetForm.$valid) {
//                        var success = function() {
//                            $scope.retrieveAssetCategories();
//                            $scope.editFlag = false;
//                        };
//
//                        AssetService.createasset(angular.copy($scope.asset), success);
//                    } else {
//                    }
                }

            };

            $scope.uploadFile = {
                target: $rootScope.apipath + 'fileUpload/uploadFile',
                singleFile: false,
                testChunks: true,
                query: {
                    fileType: $scope.seletedFileType,
                    model: 'Franchise'
                }
            };

            $scope.submitFilesAndSaveAsset = function (flow, addAssetForm) {
                if (!$scope.editFlag && $scope.asset.parentCategory === 'Select asset category') {
                    $rootScope.addMessage("Select asset category", $rootScope.failure);
                } else {
                    $scope.addAsset(addAssetForm, flow);
                }
            }

            $scope.countFiles = function (file, flow, addAssetForm, response) {
                var info = [file.name, modelName, $scope.seletedFileType];
                var fileType = "ASSET";
                var modelName = 'Franchise';
                var fileDetail = [file.name, fileType];
                var filenameformat;
                var thumbnail = "true";
                var info;
                AssetService.uploadFile(fileDetail, function (result)
                {
                    filenameformat = result.filename;
                    info = [file.name, modelName, filenameformat, thumbnail];
                    console.log("file in count :" + file.name);
                    FileUploadService.uploadFiles(info, function (response) {

                        console.log('after upload : ' + response.res);
                        file.msg = response.res;
                        $scope.removeFileList.push(response.res);
                        $scope.fileNames.push(response.res);
                    });
                });


            }

            $scope.validFileFlag = false;
            $scope.fileAdded = function (file, flow) {
                var maxsize = 5000000;
                var filesize = file.size;
                if ((file.getExtension() != "jpg") && (file.getExtension() != "jpeg") && (file.getExtension() != "png") && (file.getExtension() != "gif")
                        && (file.getExtension() != "txt") && (file.getExtension() != "doc") && (file.getExtension() != "docx") && (file.getExtension() != "pdf")) {
                    $scope.validFileFlag = true;
                    $scope.fileNames.push(file.name);
                    alert('Only images and text formats are supported');
                    return false;
                } else {
                    if (maxsize < filesize) {
                        $scope.validFileFlag = true;
                        $scope.fileNames.push(file.name);
                        alert('You can upload a file upto 5 MB ');
                        return false;
                    }
                    return true;
                }
                console.log("file name :" + file.name)
                $scope.fileNames.push(file.name);
                console.log("file list :" + $scope.fileNames);
            };

            $scope.chooseCategory = function (selectedCategory) {
                if (selectedCategory.currentNode.id != 0) {
                    $scope.categoryListDropdown[0].selected = undefined;
                }
                $scope.asset.category = selectedCategory.currentNode.id;
                $scope.asset.parentCategory = selectedCategory.currentNode.displayName;
                $scope.asset.prefix = selectedCategory.currentNode.categoryPrefix;

            };

            $scope.removeFromPage = function (file) {
                $scope.fileArray.push(file);
            }

            $scope.setAsset = function (selectedAssets) {
                $scope.assetUnitList = [];
                $scope.selectedAssets = [];
                angular.forEach(selectedAssets, function (asset) {
                    $scope.selectedAssets.push(asset);
                    if (!asset.assetType) {
                        $scope.assetUnitList.push(asset);
                    }
                });
            };

            $scope.updateasset = function () {
                $scope.submitted = true;
                if ($scope.editFlag) {
                    var success = function () {
                        $scope.setViewFlag('issue');
                        $scope.retrieveAssetCategories();
                    };
                    if ($scope.fileArray.length > 0) {
                        $.merge($scope.asset.fileDataBeans, angular.copy($scope.fileArray));
                    }
                    console.log('hello : ' + JSON.stringify(angular.copy($scope.asset)));
                    AssetService.updateasset(angular.copy($scope.asset), success);
                    $scope.hideconformationForRemoveAsset();

                }

            };

            $scope.conformationForRemoveAsset = function () {
                $('#removeAssetModal').modal('show');
            };

            $scope.hideconformationForRemoveAsset = function () {
                $('#removeAssetModal').modal('hide');
                $rootScope.removeModalOpenCssAfterModalHide();
                $('.modal-backdrop').remove();
                $scope.initIssueAssetForm();
                $scope.setViewFlag('issue');
            };

            $scope.conformationForRemovecategory = function () {
                if ($scope.assetList.length > 0) {
                    $('#messageModal').modal('show');
                } else {
                    $('#removecategoryModal').modal('show');
                }
            };

            $scope.hideconformationForRemovecategory = function () {
                $('#removecategoryModal').modal('hide');
                $rootScope.removeModalOpenCssAfterModalHide();
                $('.modal-backdrop').remove();
                $scope.category.status = 'Active';
            };

            $scope.successRemoveCat = function () {
                $('#removecategoryModal').modal('hide');
                $rootScope.removeModalOpenCssAfterModalHide();
                $('.modal-backdrop').remove();
                $scope.retrieveAssetCategories();
                $timeout(function () {
                    $scope.setViewFlag('issue');
                }, 400);
                $scope.submitted = false;
            };

            $scope.removeCategory = function () {
                var success = function () {
                    $scope.successRemoveCat();
                };
                AssetService.removecategory($scope.category, success);
            };

            $scope.removeCanNotRemoveCategory = function () {
                $('#messageModal').modal('hide');
                $rootScope.removeModalOpenCssAfterModalHide();
                $('.modal-backdrop').remove();
                $scope.category.status = 'Active';
            };


            $scope.assetNameList = {
                multiple: true,
                closeOnSelect: false,
                maximumSelectionSize: 1,
                width: 'element',
                maximumInputLength: 100,
                placeholder: 'Select asset name or start typing..',
                data: function () {
                    return {'results': $scope.select2Names};
                }
            };


            $scope.retrievesamenamesuggestion = function () {
                if ($scope.asset.modelName !== undefined) {
                    AssetService.retrievesamenamesuggestion($scope.asset.modelName, function (res) {
                        var allNames = res.data;
                        $.each(allNames, function (key, value) {
                            $scope.select2Names.push({id: value, text: value});
                        });
                        $("#assetName").select2('val', null);
                    });
                }

            };
            $scope.retrievesamenamesuggestion();

//            $scope.openFolder = function(path) {
//
//                var isOpera = !!window.opera || navigator.userAgent.indexOf(' OPR/') >= 0;
//                // Opera 8.0+ (UA detection to detect Blink/v8-powered Opera)
//                var isFirefox = typeof InstallTrigger !== 'undefined';   // Firefox 1.0+
//                var isSafari = Object.prototype.toString.call(window.HTMLElement).indexOf('Constructor') > 0;
//                // At least Safari 3+: "[object HTMLElementConstructor]"
//                var isChrome = !!window.chrome && !isOpera;              // Chrome 1+
//                var isIE = /*@cc_on!@*/false || !!document.documentMode; // At least IE6
//                console.log("browser : " + isFirefox);
//                if (isChrome) {
//                    if (path !== undefined) {
//                        if (path.indexOf("smb://") > -1 || path.indexOf("cifs://") > -1 || path.indexOf("http://") > -1 || path.indexOf("https://") > -1) {
//                            window.open(path, "_self");
//                        } else {
//                            copyToClipboard(path);
//                        }
//                    }
//                }
//                if (isFirefox) {
//                    copyToClipboard(path);
//                }
//                if (isIE) {
//                    copy_clip(path);
//                }
//            };
//            function copyToClipboard(path) {
//                window.prompt("Copy to clipboard: Ctrl+C, Enter", path);
//            }
//
//            function copy_clip(text) {
//                window.clipboardData.setData("Copy to clipboard :", text);
//                return;
//            }


//            $scope.trial = function(units, remainingunits, scope) {
//                if (units > remainingunits) {
//                    scope.issueAssetForm.units.$valid = false;
//                    scope.issueAssetForm.units.$setValidity('wrongInput', false);
////                    scope.$valid = false;
////                    scope.$setValidity('wrongInput', false);
//                } else {
//                }
//            }

            $scope.checkValidity = function (remainingunits, scope, index) {
                setValidityOfUnits(
                        scope.assetUnitList[index].units,
                        remainingunits,
                        scope.serialNumberForm.units);

            };
            function setValidityOfUnits(units, remainingunits, element) {
                if (units > remainingunits) {
                    element.$setValidity('wrongInput', false);
                } else {
                    element.$setValidity('wrongInput', true);
                }
            }

            $scope.checkSerialNumber = function (serialNumber, category, assetId, addAssetForm, assetType) {
                console.log("checking :" + assetType);
                $scope.tempAsset = {};
                $scope.tempAsset.category = category;
                $scope.tempAsset.serialNumber = serialNumber;
                $scope.tempAsset.id = assetId;
                $scope.tempAsset.assetType = assetType;
                if (serialNumber !== undefined && assetType) {
                    AssetService.doesserialnumberexist(angular.copy($scope.tempAsset), function (res) {
                        if (res.messages !== undefined && res.messages !== null) {
                            $scope.categoryExistsMsg = res.messages[0].message;
                            addAssetForm.serialNumber.$setValidity("exists", false);

                        } else {
                            addAssetForm.serialNumber.$setValidity("exists", true);
                        }

                    }, function () {
                        $rootScope.addMessage("Could not save details, please try again.", 1);
                    });
                } else {
                    addAssetForm.serialNumber.$setValidity("exists", false);
                }
            };

            $scope.makeFlagDirty = function (addAssetForm) {
                addAssetForm.$dirty = false;
            };

            $scope.deleteSelectedNode = function (node) {
                if (node === null || node === undefined) {
                    return;
                }
                for (var i = 0; i < node.length; i++) {
                    if (angular.isDefined($scope.selectedCategory.currentNode) && $scope.selectedCategory.currentNode.id === node[i].id) {
                        node.splice(i, 1);
                    }
                    else {
                        for (var j = 0; j < node.length; j++) {
                            if (node[j].children !== null) {
                                $scope.deleteSelectedNode(node[j].children);
                            }
                        }

                    }
                }
                $scope.categoryDropdown = angular.copy(node);
            }

            $scope.unmanagedLabel = [];
            $scope.combinedAssetList = [];
            $scope.retrieveAssetsByCategoryForIssue = function () {
                $scope.managedAsset = [];
                $scope.unmanagedAsset = [];
                $scope.combinedAssetList = [];
                AssetService.retrieveAssetsForIssue($scope.category, function (res) {
                    $scope.assetList = res;
                    if ($scope.assetList.length > 0) {
                        angular.forEach($scope.assetList, function (asset) {
//                            console.log('asset name : ' + asset.modelName);
                            asset.assetName = $rootScope.translateValue("AST_NM." + asset.modelName);
//                            console.log('2.asset name : ' + asset.assetName);
                            if (asset.assetType) {
                                $scope.managedAsset.push(asset);
                            } else {
                                $scope.unmanagedAsset.push(asset);
                            }
                        });
                    }
                    $scope.combinedAssetList.push(
                            {
                                assetName: '<strong>Managed assets</strong>',
                                multiSelectGroup: true
                            });
                    if ($scope.managedAsset.length > 0) {
                        angular.forEach($scope.managedAsset, function (asset) {
                            $scope.combinedAssetList.push(asset);
                            console.log("combined asse" + JSON.stringify($scope.combinedAssetList));
                        });
                    }

                    if ($scope.unmanagedAsset.length > 0) {
                        $scope.combinedAssetList.push({
                            multiSelectGroup: false
                        }, {
                            assetName: '<strong>Non-Managed assets</strong>',
                            multiSelectGroup: true
                        });
                        angular.forEach($scope.unmanagedAsset, function (asset) {
                            $scope.combinedAssetList.push(asset);
                        });
                    }

                    $scope.combinedAssetList.push({
                        multiSelectGroup: false
                    },
                    {
                        multiSelectGroup: false
                    });
                }, function () {

                });
            };

            $scope.removeFromSession = function (file) {
                var index = $scope.fileNames.indexOf(file);
                $scope.fileNames.splice(index, 1);
                AssetService.removeFileFromSession(file);
            };

            $scope.setToDate = function () {
                if ($scope.asset.purchaseDt === "" || $scope.asset.purchaseDt === undefined) {
                    $scope.minToDate = $rootScope.getCurrentServerDate();
                } else {
                    $scope.minToDate = $scope.asset.purchaseDt;
                    if (!$scope.isEdit) {
                        $scope.asset.inwardDt = $scope.asset.purchaseDt;
                    }
                }
                if ($scope.asset.purchaseDt > $scope.asset.inwardDt) {
                    $scope.asset.inwardDt = $scope.asset.purchaseDt;
                }

            };
            $scope.setFromDate = function () {
                $scope.maxFromDate = $scope.asset.inwardDt;
            }

            $scope.barcodeList = [];
            $scope.handleBarcodeGeneration = function () {
                if ($scope.asset.canGenerateBarcode) {
                    if ($scope.asset.serialNumber) {
                        var index = -1;
                        for (var i = 0, len = $scope.barcodeList.length; i < len; i++) {
                            if ($scope.barcodeList[ i ].name === "" + $scope.asset.prefix + $scope.asset.serialNumber) {
                                index = i;
                                break;
                            }
                        }
                        if (index === -1) {
                            $rootScope.maskLoading();
                            AssetService.generateBarcode("" + $scope.asset.prefix + $scope.asset.serialNumber, function (res) {
                                if (res) {
                                    $scope.asset.barcode = res.tempFilePath;
                                    $scope.barcodeList.push({name: "" + $scope.asset.prefix + $scope.asset.serialNumber,
                                        path: res.tempFilePath});
                                }
                                $rootScope.unMaskLoading();
                            }, function () {
                                $rootScope.unMaskLoading();
                                $rootScope.addMessage("Error in generating barcode", 1);
                            });
                        } else {
                            $scope.asset.barcode = $scope.barcodeList[index].path;
                        }
                    } else {
                        $rootScope.addMessage("Could not generate barcode, please enter serial number.", 1);
                    }
                } else {
                    $scope.asset.barcode = "";
                }
            }
            $scope.$watch("asset.parentCategory", function (newValue) {
                if (newValue) {
                    $scope.handleBarcodeGeneration();
                }
            });

            $scope.makeDirtyFalse = function (formname) {
                $rootScope.maskLoading();
                $scope.addAssetForm = formname;
                $scope.addAssetForm.$pristine;
                $scope.submitted = false;
                if (!$scope.editFlag) {
                    $timeout(function () {
                        $scope.addAssetForm.recipients123.$dirty = false;
                    }, 700);
                    $timeout(function () {
                        $scope.flag = true;
                        $rootScope.unMaskLoading();
                    }, 1000);
                }
            };
            $scope.checkDateInProperFormat = function (date)
            {
                var convertedDate = $filter("date")(date, "dd/MM/yyyy");
                $scope.issueasset.issuedOn = angular.copy(convertedDate);

            };

            $rootScope.unMaskLoading();
        }]);
});
