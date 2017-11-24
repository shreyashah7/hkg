/**
 * This controller is for manage subentity feature
 * Author : Shifa Salheen
 * Date : 20 January 2015
 */
define(['hkg', 'customFieldService', 'ngload!uiGrid', 'subentity.directive', 'messageService'], function (hkg) {
    hkg.register.controller('SubEntityController', ["$rootScope", "$scope", "$timeout", "$filter", "$location", "$window", "DynamicFormService", "CustomFieldService", "Messaging", function ($rootScope, $scope, $timeout, $filter, $location, $window, DynamicFormService, CustomFieldService, Messaging) {
            $rootScope.mainMenu = "manageLink";
            $rootScope.childMenu = "manageSubEntity";
            $scope.entity = "SUBENTITY.";
            $rootScope.activateMenu();
            $scope.$on('$viewContentLoaded', function () {
                $scope.initializePage();
                $scope.retrieveTreeViewFeatures();
                $scope.subEntity = {};
                $scope.featureFieldList = [];
                $scope.SubEntityValuesList = [];
                $scope.addSubmit = false;
                $scope.subId = 0;
                $scope.subEntityShow = false;
                $scope.enableAddFlag = false;
                $scope.cancelFlag = true;
                $scope.displayCustomPage = 'view';
                $scope.reloadSubEntityField = true;
                $scope.addExceptionPage = false;
                $scope.editExceptionFlag = false;
                $scope.exceptionList = [];
                $scope.finalExceptionList = [];
                $scope.forUserList = [];
                $scope.flag = {};
                $scope.subEntityValueException = {};
                $scope.gridOptions = {};
                $scope.gridOptions.enableFiltering = true;
                $scope.editSubOpen = false;
            });
            $scope.select2allFeature = {
                'allowClear': true,
                'data': [],
                'placeholder': "Select"
            };
            $scope.select2allSubEntity = {
                'allowClear': true,
                'data': [],
                'placeholder': "Select"
            };
//retrieval is for bringing data in tree view
            $scope.initializePage = function () {
                $rootScope.maskLoading();
                $scope.select2allFeature.data = [];
                CustomFieldService.retrievePrerequisite(function (res) {
                    var data = res['customfieldvalues'];
                    $scope.featureDetails = data.entityFeature;
                    setFeatures(data.entityFeature);
                    $scope.featureWithCustomField = data.exitingFeature;
                    if (!angular.isDefined($scope.featureWithCustomField)) {
                        $scope.featureWithCustomField = {};
                    }
                    $rootScope.unMaskLoading();
                }, function () {
                    var msg = "Failed to retrieve Feature";
                    var type = $rootScope.error;
                    $rootScope.addMessage(msg, type);
                    $rootScope.unMaskLoading();
                });
            };
            function setFeatures(data) {
                $scope.allfeatures = data.sort($scope.predicateBy("label"));
                angular.forEach($scope.allfeatures, function (featureData) {
                    $scope.select2allFeature.data.push({id: featureData.value, text: featureData.label});
                });
            }
            $scope.retrieveTreeViewFeatures = function ()
            {
                CustomFieldService.retrieveSubentityTreeViewFeatures(function (res)
                {
                    $scope.featureFieldList = angular.copy(res);
                });
            };
            $scope.selectFeaturedata = function (featureData) {
                if (featureData.currentNode.parentId.toString() !== '0') {
                    $scope.retrieveSubentityFieldFromSearch(featureData.currentNode.id + '-' + featureData.currentNode.parentId);
                } else {
                    //Cancel only if you are in searched result page.
                    $scope.cancelExceptionPage();
                    $scope.searchRecords = [];
                    if (featureData.currentNode.children !== null && featureData.currentNode.children !== undefined) {
                        angular.forEach(featureData.currentNode.children, function (item) {
                            var obj = {};
                            obj.id = item.id + '-' + item.parentId;
                            obj.label = item.displayName;
                            obj.feature = item.parentName;
                            $scope.searchRecords.push(obj);
                        });
                    }
                    $scope.setViewFlag('search');
                    //Clear search input field
                    $('#scrollable-dropdown-menu.typeahead').typeahead('val', '');
                    $scope.customLabel = '';
                }
            };
// retrieve All the custom fields which are of subEntityType by featureId
            $scope.retrieveFieldOfTypeSubEntityForFeature = function (featureId, entityFieldId)
            {
                var feature;
                if (featureId instanceof Object)
                {
                    feature = featureId;
                } else
                {

                    CustomFieldService.retrieveCustomFieldOfTypeSubEntityByFeatureId(featureId, function (result)
                    {

                        $scope.subEntityCustom.subEntityId = '';
                        // If we change entity ,all values should get cleared
                        if ($scope.subEntityCustom.subEntityId === null || $scope.subEntityCustom.subEntityId === undefined || $scope.subEntityCustom.subEntityId.length === 0)
                        {
                            $scope.subEntity = {};
                            $scope.subFieldTemplate = {};
                            $scope.subEntityShow = false;
                            $timeout(function () {
                                $scope.subEntityShow = true;
                                $scope.SubEntityValuesList = {};
                                //Set sub entity field
                                if (entityFieldId !== undefined) {
                                    $scope.subEntityCustom.subEntityId = entityFieldId;
                                    $scope.retrieveListOfSubEntities(entityFieldId);
                                    $scope.enableAddButton();
                                }
                            }, 100);
                        }
                        $scope.select2allSubEntity.data = [];
                        angular.forEach(result, function (entity) {
                            $scope.select2allSubEntity.data.push({id: entity.value, text: entity.label});
                        });
                        //Set feature field
                        if (entityFieldId !== undefined) {
                            $scope.subEntityCustom.featureId = featureId;
                        }
                    });
                }

            };
            // This method retrieves list of subentities associated with a custom field
            $scope.retrieveListOfSubEntities = function (fieldId) {
                if (fieldId !== null && fieldId !== undefined)
                {

                    $scope.subEntityShow = false;
                    if (fieldId instanceof Object)
                    {

                    } else
                    {
                        $scope.subEntity = {};
                        $scope.subFieldTemplate = {};
                        $scope.subEntityShow = false;
                        $timeout(function () {
                            $scope.subEntityShow = true;
                        }, 100);
                        $scope.modelAndHeaderList = [];
                        $scope.columnDefs = [];
                        $scope.removedSubEntityList = [];
                        var templateData = DynamicFormService.retrieveSubEntities(fieldId);
                        templateData.then(function (section) {
                            $scope.subFieldTemplate = section;
                            $scope.dateFields = [];
                            $scope.dateFieldsToEdit = [];
                            $scope.dateTimeFieldsToEdit = [];
                            var map = new Object(); // or var map = {};
                            if ($scope.subFieldTemplate != null && $scope.subFieldTemplate.length > 0) {
                                for (var i = 0; i < $scope.subFieldTemplate.length; i++) {
                                    var item = $scope.subFieldTemplate [i];
                                    if (item.dbType === 'Date' || item.dbType === 'datetime') {
                                        $scope.dateFields.push(item.model);
                                        if (item.dbType === 'Date') {
                                            $scope.dateFieldsToEdit.push(item.model);
                                            $scope.columnDefs.push({name: item.model, displayName: item.label,
                                                cellTemplate: '<div class="ui-grid-cell-contents">{{(COL_FIELD === null || COL_FIELD === "" || COL_FIELD === undefined) ? \'N/A\':COL_FIELD | date:"dd/MM/yyyy"}}</div>', minWidth: 200});
                                        } else if (item.dbType === 'datetime') {
                                            $scope.dateTimeFieldsToEdit.push(item.model);
                                            $scope.columnDefs.push({name: item.model, displayName: item.label,
                                                cellTemplate: '<div class="ui-grid-cell-contents">{{(COL_FIELD === null || COL_FIELD === "" || COL_FIELD === undefined) ? \'N/A\':COL_FIELD | date:"dd/MM/yyyy hh:mm a"}}</div>', minWidth: 200});
                                        }
                                    } else {
                                        $scope.columnDefs.push({name: item.model, displayName: item.label,
                                            cellTemplate: '<div class="ui-grid-cell-contents">{{(COL_FIELD === null || COL_FIELD === "" || COL_FIELD === undefined) ? \'N/A\':COL_FIELD}}</div>', minWidth: 200});
                                    }
                                    $scope.modelAndHeaderList.push({model: item.model, header: item.label});
                                    map[ item.model] = item.dbType;
                                }
                                $scope.dbTypeMap = map;
                            }
                            $scope.subEntityShow = true;
                            $scope.gridOptions.columnDefs = $scope.columnDefs;
                            $scope.gridOptions.columnDefs.push({name: 'Action',
                                cellTemplate: '<div class="ui-grid-cell-contents"> <a ng-show="row.entity.isArchive !== true"  ng-click="grid.appScope.openEdit(row.entity)"><i class="glyphicon glyphicon-edit" title="Edit"></i></a>\n\
                                                <a ng-hide="row.entity.isRemoveHide === true || row.entity.isArchive === true" ng-click="grid.appScope.deleteSubFieldValues(row.entity.tempId, row.entity)"><i class="text-danger glyphicon glyphicon-remove" title="Remove"></i></a>\n\
                                                <span class="text-warning" ng-show="row.entity.isArchive === true">removed  </span><a class="text-info" ng-show="row.entity.isArchive === true" ng-click="grid.appScope.undoDelete(row.entity)"><i class="fa fa-undo" title="Undo"></i></a>\n\
                                                <span  ng-show="row.entity.isRemoveHide === true">&nbsp;&nbsp;&nbsp;&nbsp;</span></div>',
                                enableFiltering: false, minWidth: 200});
                            $scope.gridOptions.data = $scope.SubEntityValuesList;
                            $scope.cancelFlag = true;
                            $scope.retrieveValuesFromMongo(fieldId);
                        }, function (reason) {
                        }, function (update) {
                        });
                    }
                }
            };
            // method for storing each entry of SubEntity For Displaying in table
            $scope.addSubEntityValues = function ()
            {
                $scope.addSubmit = true;
                if ($scope.managesubentity.$valid) {
                    var isSubModelEmpty = false;
                    // Check If whole JSON is empty
                    if (!jQuery.isEmptyObject($scope.subEntity)) {
                        var counter = 0;
                        var totalElements = 0;
                        // This is done to check if whether all keys have null values or not.If all keys have null value then dont add
                        for (var k in $scope.subEntity)
                        {
                            totalElements++;
                            if ($scope.subEntity[k] === '' || $scope.subEntity[k] === null || $scope.subEntity[k] === undefined)
                            {
                                counter++;
                            }
                        }
                        if (parseInt(counter) === parseInt(totalElements))
                        {
                            isSubModelEmpty = true;
                        }
                        if (!isSubModelEmpty) {

                            var j = $scope.SubEntityValuesList.indexOf($scope.subEntity);
                            if ($scope.subEntity.tempId == null) {

                                //if this is new contact, add it in contacts array
                                $scope.subEntity.tempId = angular.copy($scope.subId++);
                                $scope.tempList = angular.copy($scope.subEntity);
                                $scope.SubEntityValuesList.push($scope.tempList);
                            } else {
                                //for existing values, find this value using id
                                //and update it.
                                $scope.tempListForUpdate = angular.copy($scope.SubEntityValuesList);
                                for (var i in $scope.tempListForUpdate) {

                                    if ($scope.tempListForUpdate[i].tempId.toString() === $scope.subEntity.tempId.toString()) {
                                        $scope.tempListForUpdate[i] = angular.copy($scope.subEntity);
                                    }
                                }
                                $scope.SubEntityValuesList = angular.copy($scope.tempListForUpdate);
                            }
                            $scope.gridOptions.data = $scope.SubEntityValuesList;
                            if ($scope.gridOptions.data.length > 0) {
                                angular.forEach($scope.gridOptions.data, function (item) {
                                    item.isRemoveHide = false;
                                });
                            }
                            $scope.resetForAdd();
                        }
                    }
                }
            };
            // clears value on each add 
            $scope.resetForAdd = function ()
            {
                $scope.addSubmit = false;
                $scope.editSubOpen = false;
                $scope.subEntity = {};
                // On every add reload directive so as to fetch default values
                $scope.subEntityShow = false;
                $timeout(function () {

                    $scope.subEntityShow = true;
                }, 100);
            };
            // resetting the form
            $scope.resetPage = function ()
            {
                $scope.subEntityCustom = {};
                $scope.subEntity = {};
                $scope.SubEntityValuesList = [];
                $scope.addSubmit = false;
                $scope.subId = 0;
                // Reload SunentityField
                $scope.reloadSubEntityField = false;
                $timeout(function () {

                    $scope.reloadSubEntityField = true;
                    $scope.select2allSubEntity.data = [];
                }, 100);
//                  $scope.enableAddFlag=false;
            }
// method call when you click on edit subentityValue
            $scope.openEdit = function (customValue)
            {
                if ($scope.gridOptions.data.length > 0) {
                    angular.forEach($scope.gridOptions.data, function (item) {
                        item.isRemoveHide = false;
                    });
                }
                //Hide remove sign
                customValue.isRemoveHide = true;
                $scope.editSubOpen = true;
                $scope.subEntity = angular.copy(customValue);
            };
            // method call when you click on delete subentityValue
            $scope.deleteSubFieldValues = function (tempId, customValue)
            {
                customValue.isArchive = true;
//                if (customValue.id != null)
//                {
//                    customValue.isArchive = true;
//                    $scope.removedSubEntityList.push(customValue);
//                }
//                angular.forEach($scope.SubEntityValuesList, function(val, index) {
//                    if (val.tempId == tempId) {
//                        $scope.SubEntityValuesList.splice(index, 1);
//                    }
//                });
            };
            $scope.undoDelete = function (customValue) {
                //Remove isArchive attribute in case of undo
                angular.forEach($scope.SubEntityValuesList, function (val, index) {
                    if (val.tempId == customValue.tempId) {
                        val.isArchive = undefined;
                    }
                });
            };
            // method for saving teh subEntiies value in mongo
            $scope.saveSubEntityInMongo = function ()
            {
                //Add removed sub entity fields to original list.
//                if (angular.isDefined($scope.removedSubEntityList) && $scope.removedSubEntityList.length > 0) {
//                    angular.forEach($scope.removedSubEntityList, function(item) {
//                        $scope.SubEntityValuesList.push(item);
//                    });
//                }
                if ($scope.SubEntityValuesList.length > 0) {
                    for (var i = $scope.SubEntityValuesList.length - 1; i >= 0; i--) {
                        if ($scope.SubEntityValuesList[i].isArchive === true && $scope.SubEntityValuesList[i].id === undefined) {
                            $scope.SubEntityValuesList.splice(i, 1);
                        }
                    }
                }
                if (angular.isDefined($scope.SubEntityValuesList) && $scope.SubEntityValuesList.length > 0)
                {
                    $scope.subEntityListForMongo = [];
                    angular.forEach($scope.SubEntityValuesList, function (list)
                    {

                        var subEntity = {};
                        if ($scope.dateFieldsToEdit !== null && $scope.dateFieldsToEdit !== undefined && $scope.dateFieldsToEdit.length > 0) {
                            for (var key in list) {
                                if (list.hasOwnProperty(key)) {
                                    if ($scope.dateFieldsToEdit.indexOf(key) !== -1) {
                                        list[key] = new Date(list[key]);
                                    }
                                }
                            }
                        }
                        subEntity.dbMap = angular.copy(list);
                        subEntity.dbType = $scope.dbTypeMap;
                        subEntity.instanceId = angular.copy($scope.subEntityCustom.subEntityId);
                        subEntity.componentType = $scope.subEntity.subFieldType;
                        subEntity.id = list.id;
                        $scope.subEntityListForMongo.push(subEntity);
                    });
                    if (angular.isDefined($scope.subEntityListForMongo) && $scope.subEntityListForMongo.length > 0)
                    {
                        var success = function ()
                        {
                            $scope.resetPage();
                            $scope.cancel();
                        };
                        var failure = function ()
                        {
                            var msg = "Failed to add the subentities values";
                            var type = $rootScope.failure;
                            $rootScope.addMessage(msg, type);
                        };
                        CustomFieldService.saveSubEntitiesValue($scope.subEntityListForMongo, success, failure);
                    }
                }
            };
            // method for retrieving the values from mongo on the basis of instance id which is the id of customField
            $scope.retrieveValuesFromMongo = function (instanceId)
            {
                if (Object.prototype.toString.call(instanceId) !== "[object Object]" && instanceId !== undefined) {
                    CustomFieldService.retrievelistofSubEntitiesValuesByInstanceId(instanceId, function (result)
                    {
                        $scope.tempListForDisplay = [];
                        angular.forEach(result, function (documents)
                        {
                            if (documents.dbMap !== null) {
                                if (Object.keys(documents.dbMap).length) {
                                    var tempList = {};
                                    tempList = angular.copy(documents.dbMap);
                                    tempList.id = angular.copy(documents.id);
                                    tempList.tempId = angular.copy(documents.tempId);
                                    $scope.tempListForDisplay.push(tempList);
                                }
                            }

                        });
                        $scope.SubEntityValuesList = angular.copy($scope.tempListForDisplay);
                        if ($scope.dateFieldsToEdit !== null && $scope.dateFieldsToEdit !== undefined && $scope.dateFieldsToEdit.length > 0) {
                            angular.forEach($scope.SubEntityValuesList, function (item) {
                                angular.forEach($scope.dateFieldsToEdit, function (item1) {
                                    if (item[item1] !== null && item[item1] !== undefined) {
                                        item[item1] = new Date(item[item1]);
                                    }
                                });
                            });
                        }
                        if ($scope.dateTimeFieldsToEdit !== null && $scope.dateTimeFieldsToEdit !== undefined && $scope.dateTimeFieldsToEdit.length > 0) {
                            angular.forEach($scope.SubEntityValuesList, function (item) {
                                angular.forEach($scope.dateTimeFieldsToEdit, function (item1) {
                                    if (item[item1] !== null && item[item1] !== undefined) {
                                        item[item1] = $filter('date')(item[item1], 'dd/MM/yyyy hh:mm a');//getDateFromFormat(, $rootScope.dateFormatWithTime);
                                    }
                                });
                            });
                        }
                        $scope.gridOptions.data = $scope.SubEntityValuesList;
                    });
                }
            };
            //Search related operations
            $scope.getSearchedCustomPages = function (list) {
                $scope.isTreeView = false;
                if ($scope.selectedFeatureData !== undefined && $scope.selectedFeatureData.currentNode !== undefined) {
                    $scope.selectedFeatureData.currentNode.selected = undefined;
                }
                var enteredText = $('#searchSubentityField.typeahead').typeahead('val');
                if (enteredText.length > 0) {
                    if (enteredText.length < 3) {
                        $scope.searchRecords = [];
                    } else {
                        $scope.searchRecords = [];
                        angular.forEach(list, function (item) {
                            var fieldDetails = item.text.split(",");
                            item.label = fieldDetails[0];
                            item.feature = fieldDetails[1];
                            $scope.searchRecords.push(item);
                        });
                    }
                    $scope.setViewFlag('search');
                    if ($scope.selectedFeature.currentNode !== undefined) {
                        $scope.selectedFeature.currentNode.selected = undefined;
                    }
                }
            };
            $scope.setViewFlag = function (flag) {
                if (flag !== "search") {
                    //Erase search field.
                    $('#scrollable-dropdown-menu.typeahead').typeahead('val', '');
                    $scope.customLabel = '';
                }
                $scope.displayCustomPage = flag;
            };

            $scope.retrieveSubentityFieldFromSearch = function (searchKey) {
                if (searchKey.indexOf("-") > -1) {
                    var ids = searchKey.split("-");
                    if (ids.length === 2) {
                        var featureId = parseInt(ids[1]);
                        var fieldId = parseInt(ids[0]);
                        if (!angular.isDefined($scope.subEntityCustom)) {
                            $scope.subEntityCustom = {};
                        }
                        //Change feature and field entity accordingly.
                        $scope.retrieveFieldOfTypeSubEntityForFeature(featureId, fieldId);
                        $scope.setViewFlag('view');
                        //Deselect only parent node.
                        if ($scope.selectedFeature.currentNode !== undefined && $scope.selectedFeature.currentNode.parentId == 0) {
                            $scope.selectedFeature.currentNode.selected = undefined;
                        }
                    }
                }
            };
            //Search related operations end
            $scope.enableAddButton = function ()
            {
                $scope.enableAddFlag = true;
                $scope.editSubOpen = false;
            };
            $scope.cancel = function ()
            {
                $scope.subEntityCustom = {};
                $scope.subEntityCustom.featureId = '';
                $scope.subEntityCustom.subEntityId = '';
//                $scope.subEntityShow=
                $scope.subEntity = {};
                $scope.SubEntityValuesList = [];
                $scope.addSubmit = false;
                $scope.subId = 0;
                $scope.cancelFlag = false;
                $scope.enableAddFlag = false;
                $scope.reloadSubEntityField = false;
                $scope.setViewFlag('view');
                $scope.subEntity = {};
                $scope.subFieldTemplate = {};
                $scope.subEntityShow = false;
                if ($scope.selectedFeature.currentNode !== undefined) {
                    $scope.selectedFeature.currentNode.selected = undefined;
                }
                $timeout(function () {

                    $scope.subEntityShow = true;
                    $scope.reloadSubEntityField = true;
                    $scope.select2allSubEntity.data = [];
                }, 100);
            };

            $scope.initAddExceptionForm = function (addSubEntityExceptionForm) {
                $scope.addSubEntityExceptionForm = addSubEntityExceptionForm;
            };

            $scope.addExceptionPopup = function (field) {
                $rootScope.maskLoading();
                $scope.subEntityName = field.label;
                var id = field.id;
                var subId = id.split("-")[0];
                $scope.instanceId = subId;
                $scope.addExceptionPage = true;
                $scope.subEntityValueException = {};
                $scope.count = 0;
                $scope.initUsers();
                $scope.retrieveValuesForSubEntity(subId);
                $scope.retrieveDependentOnFields();
//                $timeout(function () {
                $scope.retrieveValueExceptions($scope.instanceId);
//                }, 200);
                $rootScope.unMaskLoading();
            };


            $scope.retrievePrerequisiteForException = function (instanceId) {
                $scope.finalFieldList = [];
                CustomFieldService.retrievePrerequisiteForException(instanceId, function (result) {
                    if (result !== undefined && result !== null && result.data !== undefined && result.data !== null) {
                        var data = result.data;
                        if (data['dependentFieldList'] !== undefined && data['dependentFieldList'] !== null && data['dependentFieldList'].length > 0) {
                            $scope.fieldDataBeanList = data['dependentFieldList'];
                            if ($scope.fieldDataBeanList.length > 0) {
                                angular.forEach($scope.fieldDataBeanList, function (item) {
                                    $scope.finalFieldList.push({id: item.value + "|" + item.description, text: item.label});
                                });
                            }
                        }
                        if (data['subEntityValueExceptionDataBeans'] !== undefined && data['subEntityValueExceptionDataBeans'] !== null && data['subEntityValueExceptionDataBeans'].length > 0) {
                            $scope.finalExceptionList = [];
                            var cnter = 0;
                            angular.forEach(data['subEntityValueExceptionDataBeans'], function (item) {
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
                                    }
                                });
                            }
                        }
                    }
                    $scope.initDependentOnValues();
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
                            var userIdSplit = $scope.tempSubEntityValueException.forUsers.split(",");
                            var userToBeDisplaySplit = $scope.tempSubEntityValueException.userToBeDisplay.split(",");
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
                            var valueIdSplit = $scope.tempSubEntityValueException.forValue.split(",");
                            var valueToBeDisplaySplit = $scope.tempSubEntityValueException.valueToBeDisplay.split(",");
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

            $scope.initDependentValueList = function () {
                $scope.multiDependentValueList = {
                    multiple: true,
                    closeOnSelect: false,
                    placeholder: 'Select Values',
                    data: $scope.fieldValueList,
                    initSelection: function (element, callback) {
                        if ($scope.editExceptionFlag) {
                            var data = [];
                            if ($scope.tempSubEntityValueException !== undefined && $scope.tempSubEntityValueException.dependentOnField !== undefined && $scope.tempSubEntityValueException.dependentOnFieldValues !== undefined && $scope.tempSubEntityValueException.dependentOnFieldValues !== null && $scope.tempSubEntityValueException.dependentOnFieldValues !== '' && $scope.tempSubEntityValueException.dependentOnFieldValues instanceof Object === false && !angular.isArray($scope.tempSubEntityValueException.dependentOnFieldValues)) {
                                var dependentValuesIdSplit = $scope.tempSubEntityValueException.dependentOnFieldValues.split(",");
                                var dependentValuesSplit = $scope.tempSubEntityValueException.dependentOnFieldValuesToBeDisplay.split(",");
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

            $scope.$watch("subEntityValueException.dependentOnField", function () {
                if ($scope.subEntityValueException.dependentOnField !== undefined && $scope.subEntityValueException.dependentOnField instanceof Object === true && angular.isArray($scope.subEntityValueException.dependentOnField)) {
                } else {
                    if ($scope.fieldValueList !== undefined) {
                        $scope.fieldValueList.splice(0, $scope.fieldValueList.length);
                    }
                    $scope.retrieveFieldValues();
                }

            });

            $scope.retrieveValuesForSubEntity = function (id) {
                $scope.valueList = [];
                $scope.valueList.push({id: 0, text: "All Values"});
                CustomFieldService.createDropDownListForSubEntity(id, function (response) {
                    if (response !== undefined && response !== null) {
                        angular.forEach(response, function (item) {
                            $scope.valueList.push({id: item.label, text: item.value});
                        });
                        if ($scope.valueList !== undefined && $scope.valueList.length > 0) {
                            $scope.flag.valueRetrieved = true;
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
                    }
                });
            };

            $scope.retrieveDependentOnFields = function () {
                CustomFieldService.retrieveCustomFields(function (result) {
                    $scope.fieldDataBeanList = result.data;
                    $scope.finalFieldList = [];
                    if ($scope.fieldDataBeanList.length > 0) {
                        angular.forEach($scope.fieldDataBeanList, function (item) {
                            $scope.finalFieldList.push({id: item.value + "|" + item.description, text: item.label});
                        });
                        if ($scope.finalFieldList.length > 0) {
                            $scope.initDependentOnValues();
                        }

                    }

                });
            };

            $scope.retrieveFieldValues = function () {
                if ($scope.subEntityValueException.dependentOnField !== undefined && $scope.subEntityValueException.dependentOnField !== null && $scope.subEntityValueException.dependentOnField instanceof Object === false && $scope.subEntityValueException.dependentOnField !== '') {
                    $scope.dependentOn = $scope.subEntityValueException.dependentOnField.split('|');
                    $scope.payload = {
                        fieldId: $scope.dependentOn[0],
                        componentType: $scope.dependentOn[1]
                    };
                    CustomFieldService.retrieveCustomFieldsValueByKey($scope.payload, function (res) {
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

            $scope.exceptionList = [];
            $scope.finalExceptionList = [];
            $scope.addException = function (addSubEntityExceptionForm) {
                $scope.submitted = true;
                if (!addSubEntityExceptionForm.$valid) {
                } else if ((($scope.subEntityValueException.dependentOnFieldValues !== undefined && $scope.subEntityValueException.dependentOnFieldValues !== null && $scope.subEntityValueException.dependentOnFieldValues !== '') || ($scope.subEntityValueException.forUsers !== undefined && $scope.subEntityValueException.forUsers !== null && $scope.subEntityValueException.forUsers !== ''))) {
                    $scope.submitted = false;
                    $scope.count = $scope.finalExceptionList.length;
                    $scope.subEntityValueException.index = $scope.count++;
                    $scope.subEntityValueException.instanceId = $scope.instanceId;
                    $scope.finalExceptionList.push(angular.copy($scope.subEntityValueException));
                    $scope.subEntityValueExceptionToBeDisplayed = angular.copy($scope.subEntityValueException);
                    if ($scope.subEntityValueException.forUsers !== undefined && $scope.forUserList.length > 0) {
                        var userName = $scope.subEntityValueException.forUsers.split(',');
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
                        $scope.subEntityValueExceptionToBeDisplayed.userToBeDisplay = userToBeDisplay;
                    }
                    if ($scope.subEntityValueException.forValue !== undefined && $scope.valueList.length > 0) {
                        var valueName = $scope.subEntityValueException.forValue.split(',');
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
                        $scope.subEntityValueExceptionToBeDisplayed.valueToBeDisplay = valueToBeDisplay;
                    }
                    if ($scope.subEntityValueException.dependentOnField !== undefined && $scope.finalFieldList.length > 0) {
                        var dependentOnToBeDisplay = '';
                        angular.forEach($scope.finalFieldList, function (field) {
                            var fullDependentOnField = field.id;
                            if ($scope.subEntityValueException.dependentOnField == fullDependentOnField) {
                                if (dependentOnToBeDisplay === '') {
                                    dependentOnToBeDisplay = dependentOnToBeDisplay + field.text;
                                } else {
                                    dependentOnToBeDisplay = dependentOnToBeDisplay + " , " + field.text;
                                }

                            }
                        });
                        $scope.subEntityValueExceptionToBeDisplayed.dependentOnToBeDisplay = dependentOnToBeDisplay;
                    }
                    if ($scope.subEntityValueException.dependentOnFieldValues !== undefined && $scope.fieldValueList.length > 0) {
                        var dependentOnFieldValuesName = $scope.subEntityValueException.dependentOnFieldValues.split(',');
                        var dependentOnFieldValuesToBeDisplay = '';
                        for (var i = 0; i < dependentOnFieldValuesName.length; i++) {
                            angular.forEach($scope.fieldValueList, function (fieldValue) {
                                var fulldependentOnFieldValuesName = fieldValue.id;
                                if (dependentOnFieldValuesName[i] == fulldependentOnFieldValuesName) {
                                    if (dependentOnFieldValuesToBeDisplay === '') {
                                        dependentOnFieldValuesToBeDisplay = dependentOnFieldValuesToBeDisplay + $scope.subEntityValueExceptionToBeDisplayed.dependentOnToBeDisplay + "-" + fieldValue.text;
                                    } else {
                                        dependentOnFieldValuesToBeDisplay = dependentOnFieldValuesToBeDisplay + " , " + $scope.subEntityValueExceptionToBeDisplayed.dependentOnToBeDisplay + "-" + fieldValue.text;
                                    }

                                }
                            });
                        }
                        $scope.subEntityValueExceptionToBeDisplayed.dependentOnFieldValuesToBeDisplay = dependentOnFieldValuesToBeDisplay;
                    }
                    $scope.subEntityValueExceptionToBeDisplayed.isArchive = false;
                    $scope.exceptionList.push(angular.copy($scope.subEntityValueExceptionToBeDisplayed));
                    $scope.cancelException(addSubEntityExceptionForm);
                } else {
                    $rootScope.addMessage("Select For User or Dependent On to apply Exception", $rootScope.failure);
                    $scope.submitted = false;
                }
            };

            $scope.cancelException = function () {
                $scope.subEntityValueException = {};
                $scope.submitted = false;
                $scope.addSubEntityExceptionForm.$setPristine();
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

            $scope.removeException = function (exception) {
                exception.isArchive = true;
            };
            $scope.undoException = function (exception) {
                exception.isArchive = false;
            };

            $scope.cancelExceptionPage = function () {
                $scope.subEntityCustom = {};
                $scope.subEntityCustom.featureId = '';
                $scope.subEntityCustom.subEntityId = '';
                $scope.subEntity = {};
                $scope.SubEntityValuesList = [];
                $scope.addSubmit = false;
                $scope.subId = 0;
                $scope.cancelFlag = false;
                $scope.enableAddFlag = false;
                $scope.reloadSubEntityField = false;
                $scope.setViewFlag('view');
                $scope.subEntity = {};
                $scope.subFieldTemplate = {};
                $scope.subEntityShow = false;
                $scope.addExceptionPage = false;
                $scope.editExceptionFlag = false;
                $scope.subEntityValueException = {};
                $scope.forUserList = [];
                $scope.submitted = false;
                $scope.exceptionList = [];
                $scope.count = 0;
                if ($scope.addExceptionForm !== undefined) {
                    $scope.addExceptionForm.$setPristine();
                }
                $timeout(function () {
                    $scope.subEntityShow = true;
                    $scope.reloadSubEntityField = true;
                    $scope.select2allSubEntity.data = [];
                }, 100);
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
                    CustomFieldService.saveException($scope.finalListToSend, function () {
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
                        $scope.subEntityValueException = angular.copy(finalEx);
                        $scope.tempSubEntityValueException = angular.copy(exception);
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
                } else if ((($scope.subEntityValueException.dependentOnFieldValues !== undefined && $scope.subEntityValueException.dependentOnFieldValues !== null && $scope.subEntityValueException.dependentOnFieldValues !== '') || ($scope.subEntityValueException.forUsers !== undefined && $scope.subEntityValueException.forUsers !== null && $scope.subEntityValueException.forUsers !== ''))) {
                    $scope.tempValueExceptionObj = angular.copy($scope.subEntityValueException);
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

            $scope.retrieveValueExceptions = function (id) {
                if (id !== undefined) {
                    $scope.finalExceptionList = [];
                    var cnter = 0;
                    CustomFieldService.retrieveValueExceptions(id, function (res) {
                        if (res.data !== undefined && res.data !== null && res.data.length > 0) {
                            angular.forEach(res.data, function (item) {
                                item.index = cnter++;
                                item.isArchive = false;
                                $scope.finalExceptionList.push(item);
                                $scope.exceptionList.push(item);
                            });
                        }
                    });
                }
            };

        }]);
});