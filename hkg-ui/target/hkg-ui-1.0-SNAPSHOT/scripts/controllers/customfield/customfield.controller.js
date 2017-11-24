
/**
 * This controler is for manage custom field
 * Author : Harshit shah
 * Date : 18 June 2014
 */
define(['hkg', 'jqueryUi', 'customFieldService', 'selectize', 'selectize.directive', 'selectizeforpointer.directive', 'selectizeforconstraint.directive', 'selectizefordate.directive', 'whitespace-remove.directive', 'colorpicker.directive'], function (hkg) {
    hkg.register.controller('CustomField', ['$rootScope', '$scope', 'CustomFieldService', 'DynamicFormService', '$timeout', '$location', '$filter', function ($rootScope, $scope, CustomFieldService, DynamicFormService, $timeout, location, $filter) {
            $rootScope.mainMenu = "manageLink";
            $rootScope.childMenu = "manageCustomField";
            $scope.entity = "CSTMFLD.";
            $rootScope.activateMenu();
            var self = this;
           $scope.noshow ="noshow";
           $scope.smallCurrency="small";
           $scope.largeCurrency ="large";
            $scope.addedCustomFields = [];
            $scope.tempCustomFieldList = [];
            $scope.statusList = ["Active", "Remove"];
            $scope.listToCompare = [];
            $scope.fileTypes = [];
            $scope.type = {};
            $scope.selectedFeatureEntityType = undefined;
            $scope.customFieldInfo = {};
            $scope.customField = {};
            $scope.isEditPage = false;
            $scope.removeCustomFieldIndexNo = {};
            $scope.i = 0;
            $scope.isSubEntity = false;
            $scope.searchFlag = false;
            $scope.submittedForm = false;
            $scope.isEditable = true;
            $scope.items = "invoice.number1|+|SUM|(|lot.num|)|+|invoice.number21";
            var labelNames = [];
            $scope.multiSelect = false;
            $scope.featureSubmited = false;
            $scope.displayCustomPage = 'view';
            $scope.sectionSubmited = false;
            $scope.featureWithCustomField = {};
            $scope.isShowAddCustomFieldDiv = {};
            $scope.showConstraints = false;
            $scope.isUserMultiSelect = false;
            $scope.isTextField = false;
            $scope.conditionalOperators = [];
            $scope.numericValueRequired = false;
            $scope.arithmeticOperatorRequired = false;
            self.constraintsEntityRequired = false;
            $scope.subEntityList = [];
            $scope.showTable = false;
            $scope.subId = 0;
            var i = 0;
            $scope.dropListRequired = false;
            $scope.loadMaster = false;
            $scope.loadValues = false;
            // flag to determine whether it is edit page
            $scope.editCustomField = false;
            $scope.disableSubEntityAdd = true;
            $scope.allowedTypeSubEntity = false;
//            $scope.ListOfFields = [{id: "Number", type: "Number", img: "images/custome-field/Number.jpg"}, {id: "Dropdown", type: "Dropdown", img: "images/custome-field/Select.jpg"}, {id: "Text field", type: "Text field", img: "images/custome-field/Text.jpg"}, {id: "Text area", type: "Text area", img: "images/custome-field/TextArea.jpg"}, {id: "Radio button", type: "Radio button", img: "images/custome-field/Radio.jpg"}, {id: "Date", type: "Date", img: "images/custome-field/Date.jpg"}, {id: "Date range", type: "Date range", img: "images/custome-field/DateRange.jpg"}, {id: "Time", type: "Time", img: "images/custome-field/Time.jpg"}, {id: "Time range", type: "Time range", img: "images/custome-field/TimeRange.jpg"},{id: "Currency", type: "Currency", img: "images/custome-field/Currency.jpg"}, {id: "Upload", type: "Upload", img: "images/custome-field/Upload.jpg"}, {id: "Image", type: "Image", img: "images/custome-field/Image.jpg"}, {id: "Email", type: "email", img: "images/custome-field/Email.jpg"}, {id: "Phone", type: "Text field", img: "images/custome-field/phone.jpg"}, {id: "Checkbox", type: "Checkbox", img: "images/custome-field/CheckBox.jpg"}, {id: "MultiSelect", type: "MultiSelect", img: "images/custome-field/multiselect.jpg"}, {id: "Percent", type: "Percent", img: "images/custome-field/percent.jpg"}, {id: "Formula", type: "Formula", img: "images/custome-field/formula.jpg"}];
            $scope.ListOfFields = [{id: "Text field", type: "Text field", img: "images/custome-field/Text.jpg"}, {id: "Text area", type: "Text area", img: "images/custome-field/TextArea.jpg"}, {id: "Email", type: "email", img: "images/custome-field/Email.jpg"}, {id: "Phone", type: "Text field", img: "images/custome-field/phone.jpg"}, {id: "Checkbox", type: "Checkbox", img: "images/custome-field/CheckBox.jpg"}, {id: "Dropdown", type: "Dropdown", img: "images/custome-field/Select.jpg"}, {id: "MultiSelect", type: "MultiSelect", img: "images/custome-field/multiselect.jpg"}, {id: "Date", type: "Date", img: "images/custome-field/Date.jpg"}, {id: "Number", type: "Number", img: "images/custome-field/Number.jpg"}, {id: "Percent", type: "Percent", img: "images/custome-field/percent.jpg"}, {id: "Currency", type: "Currency", img: "images/custome-field/Currency.jpg"}, {id: "Exchange rate", type: "Exchange rate", img: "images/custome-field/exchangerate.jpg"}, {id: "Formula", type: "Formula", img: "images/custome-field/formula.jpg"}, {id: "Pointer", type: "Pointer", img: "images/custome-field/pointer.jpg"}, {id: "Image", type: "Image", img: "images/custome-field/Image.jpg"}, {id: "Upload", type: "Upload", img: "images/custome-field/Upload.jpg"}, {id: "UserMultiSelect", type: "UserMultiSelect", img: "images/custome-field/user.jpg"}, {id: "Angle", type: "Angle", img: "images/custome-field/angle.jpg"}, {id: "SubEntity", type: "SubEntity", img: "images/custome-field/subentity.jpg"}];
            $scope.ListOfSubEntityFields = [{id: "Text field", type: "Text field", img: "images/custome-field/Text.jpg"}, {id: "Number", type: "Number", img: "images/custome-field/Number.jpg"}, {id: "Text area", type: "Text area", img: "images/custome-field/TextArea.jpg"}, {id: "Date", type: "Date", img: "images/custome-field/Date.jpg"}, {id: "Email", type: "email", img: "images/custome-field/Email.jpg"}, {id: "Phone", type: "Text field", img: "images/custome-field/phone.jpg"}, {id: "Percent", type: "Percent", img: "images/custome-field/percent.jpg"}];
            $scope.customFieldValidation = {};
            $scope.subEntity = {};
            $scope.subEntityCustomFieldValidation = {};
            $scope.localEditFlag = false;
            $scope.isTreeView = false;
            $scope.editDisableSubEntity = false;
            $scope.allowDefaultDate = false;
            $scope.reloadDefaultMultiSelect = false;
            $scope.hasConstraintValue = false;
            $scope.reloadAllowedTypeForSubEntity = true;
            $scope.showSubEntitySampleInput = false;
            $scope.disableOkForRemove = false;
            $scope.changeDependantmasterFlag = false;
            $scope.editCount = 0;
            $scope.negativeAllowed = false;
            $scope.negativeAllowedSubEntity = false;
            $scope.showIsPrivate = false;
            $scope.showComboForDepOrDesg = false;
            $scope.listForUserMultiSelect = [{id: 1, text: "Department"}, {id: 2, text: "Designation"}, {id: 3, text: "All"}];
            $scope.showDesgList = false;
            $scope.showDeptList = false;
            $scope.noShow="noShow";
            $scope.customsearchpopover =
                    "<NOBR><font color='red;'>Use the shortcut to search</font></NOBR><br/><table cellpadding='0' cellspacing='0'><tr><td>\ " +
                    "\n\ " +
                    "'@F'  </td> <td> &nbsp;  &nbsp;</td><td align='left'>Feature</td></tr>\ " +
                    "</table>\ ";
            $scope.displayFormat =
                    "<font color='red;'>Display format</font><br/>\ " +
                    "<table cellpadding='0' cellspacing='0' width='100%'>\ " +
                    "<tr><td>'0'  </td> <td> &nbsp;</td><td align='left'>(Digit)</td></tr>\ " +
                    "<tr><td>'#'  </td> <td> &nbsp;</td><td align='left'> (Digit,zero shows as absent )</td></tr>\ " +
                    "<tr><td>'.'  </td> <td> &nbsp; </td><td align='left'>(Example: ###.## )</td></tr>\ " +
                    "<tr><td>','  </td> <td> &nbsp; </td><td align='left'>(Example: ###,###.## )</td></tr>\\n\ " +
                    "</table>\ ";
            var sectionAndCustomFieldDetail = {};
            var editCustomField;
            $scope.$on('$viewContentLoaded', function () {
                var self = this;
                $scope.initializePage();
                $scope.retrieveTreeViewFeatures();
                $scope.dataTableOptions = {
                    "columns": [
                        "Tab Index",
                        null,
                        {"orderDataType": "dom-text-numeric"}
                    ],
                    "autoWidth": false
                };
            });
            $scope.dataTableOptions = {
                "columns": [
                    null,
                    null,
                    null,
                    null,
                    {"orderDataType": "dom-text-numeric"},
                    null
                ],
                "autoWidth": false
            };
            $scope.select2allFeature = {
                'allowClear': true,
                'data': [],
                'placeholder': "Select"
            };
            // Code added For handling dependant field by Shifa Salheen on 30 December 2014
            $scope.select2allMasters = {
                'allowClear': true,
//                'data': [],
                'placeholder': "Select",
                initSelection: function (element, callback) {
                    if ($scope.editCustomField) {
                        var data = {};
                        data = {
                            id: $scope.editMasterId,
                            text: $scope.editMasterValue
                        };
                        callback(data);
                    }
                }
//                data: function () {
//                    return {'results': $scope.masters};
//
//                }

            };
            // Method added by Shifa Salheen on 24 April 2015 for tree view
            $scope.retrieveTreeViewFeatures = function ()
            {
                CustomFieldService.retrieveTreeViewFeatures(function (res)
                {
                    $scope.featureSectionList = angular.copy(res);
                });
            };
            $scope.selectFeaturedata = function (selectedFeature)
            {
                // For clearing search input 
                $scope.customLabel = '';
                $scope.isTreeView = true;
                $scope.selectedFeatureData = selectedFeature;
                var infoMap = new Object();
                if (selectedFeature.currentNode.parentId.toString() !== '0')
                {

                    infoMap['isSection'] = true;
                    infoMap['sectionId'] = selectedFeature.currentNode.id;
                    infoMap['featureId'] = selectedFeature.currentNode.parentId;
                } else
                {
                    infoMap['isSection'] = false;
                    infoMap['featureId'] = selectedFeature.currentNode.id;
                }
                CustomFieldService.retrieveFeatureOrSectionCustomFields(infoMap, function (response)
                {

                    $scope.searchRecords = [];
                    angular.forEach(response, function (item) {

//                        item.label = item.label;
                        item.type = angular.copy(item.fieldType);
                        item.section = angular.copy(item.sectionName);
                        item.feature = angular.copy(item.featureName);
                        item.sequenceNum = angular.copy(item.seqNo);
                        $scope.searchRecords.push(item);
                    });
                    $scope.setViewFlag('search');
                });
            };
            $scope.saveSequenceNum = function (searchRecords)
            {
                var sequenceNumMap = {};
                $scope.sequenceNum = [];
                angular.forEach(searchRecords, function (search)
                {
                    sequenceNumMap[search.id] = search.sequenceNum;
                });
                var success = function ()
                {
                    DynamicFormService.storeAllCustomFieldData($rootScope.session.companyId);
                    var msg = "Custom fields updated successfully";
                    var type = $rootScope.success;
                    $rootScope.addMessage(msg, type);
                    $scope.reset();
                };
                var failure = function ()
                {
                    var msg = "The fields could not be updated, please try again.";
                    var type = $rootScope.failure;
                    $rootScope.addMessage(msg, type);
                };
                CustomFieldService.updateSequenceNum(sequenceNumMap, success, failure);
            };
            $scope.select2allMasterValues = {
                'allowClear': true,
//                'data': [],
                'placeholder': "Select",
                'multiple': true,
                initSelection: function (element, callback) {
                    if ($scope.editCustomField) {

                        if ($scope.customFieldValidation.dependantMasterValue !== null && $scope.customFieldValidation.dependantMasterValue !== undefined) {
                            if ($scope.customFieldValidation.dependantMasterValue instanceof Array)
                            {

                            } else
                            {

                                // Edit Count is to check scenarios ..on edit i.e. on retrieval this method already gets called..so we need to call init selection only once and not on every change
                                if (($scope.editCount === 0 || $scope.editCount === 1) && ($scope.customFieldValidation.dependantMasterValue !== null && $scope.customFieldValidation.dependantMasterValue !== undefined))
                                {
                                    var tempdata = $scope.customFieldValidation.dependantMasterValue.toString().replace('~M', ",");
                                    var tempArray = tempdata.split(',');
                                    var data = [];
                                    angular.forEach(tempArray, function (temp)
                                    {
//                                                                
                                        var index = $filter('filter')($scope.values, function (result) {

                                            return temp.toString() === result.id.toString();
                                        })[0];
                                        data.push(index);
                                    });
//                            $scope.customFieldValidation.dependantMasterValue = tempdata;
//                            if(data !== null && data.length >0){  
                                    callback(data);
                                    $scope.customFieldValidation.dependantMasterValue = tempdata;
                                }
                            }
                        }
                    }
//                  
                }
            };
            $scope.setMasters = function ()
            {
                if ($scope.customFieldValidation.dependantOnMasterValue)
                {
                    CustomFieldService.retrieveCustomFieldMastersForDependantField($scope.customFieldInfo.featureId, function (result)
                    {
                        $scope.purchaserList = result;
//                        $scope.select2allMasters.data = [];
                        $scope.masters = [];
                        angular.forEach(result, function (featureData) {
                            if ($scope.customFieldValidation.dependantMasterId !== null && $scope.customFieldValidation.dependantMasterId !== undefined)
                            {
                                if (featureData.value.toString() === $scope.customFieldValidation.dependantMasterId.toString())
                                {

                                    $scope.editMasterId = featureData.value;
                                    $scope.editMasterValue = featureData.label;
                                }
                            }
                            $scope.masters.push({id: featureData.value, text: featureData.label});
                            $scope.select2allMasters.data.push({id: featureData.value, text: featureData.label});
                            $scope.loadMaster = true;
                        });
                    });
                }
            };
            $scope.changeMasters = function ()
            {
                if ($scope.editCustomField)
                {
                    angular.forEach($scope.purchaserList, function (featureData) {
                        if ($scope.customFieldValidation.dependantMasterId !== null && $scope.customFieldValidation.dependantMasterId !== undefined)
                        {
                            if (featureData.value.toString() === $scope.customFieldValidation.dependantMasterId.toString())
                            {

                                $scope.editMasterId = featureData.value;
                                $scope.editMasterValue = featureData.label;
                            }
                        }

                    });
                }
                $scope.values = [];
            };
            $scope.setMasterValuesForDependantField = function (masterId)
            {
                $scope.editCount++;
                $scope.changeDependantmasterFlag = true;
                $scope.loadValues = false;
                if ($scope.editCustomField === true && parseInt($scope.editCount) !== 0 && parseInt($scope.editCount) !== 1)
                {
                    $scope.customFieldValidation.dependantMasterValue = '';
                } else if ($scope.editCustomField === false)
                {
                    // Add
                    $scope.customFieldValidation.dependantMasterValue = '';
                }


                if (masterId instanceof Object || masterId === undefined)
                {
                } else
                {

                    CustomFieldService.retrieveCustomFieldValuesForDependantField(masterId.toString(), function (result)
                    {
                        $scope.setValueMasters = result;
                        angular.forEach($scope.setValueMasters, function (valueMaster)
                        {
                            if ($scope.customFieldValidation.dependantMasterValue !== null && $scope.customFieldValidation.dependantMasterValue !== undefined)
                            {
                                if (valueMaster.value.toString() === $scope.customFieldValidation.dependantMasterValue.toString())
                                {
                                    $scope.editValueId = valueMaster.value;
                                    $scope.editValue = valueMaster.label;
                                }
                            }
                        });
                        $scope.select2allMasterValues.data = [];
                        $scope.values = [];
                        angular.forEach(result, function (featureData) {
                            $scope.values.push({id: featureData.value, text: featureData.label});
//                            $scope.select2allMasterValues.data.push({id: featureData.value, text: featureData.label});

                        });
                        $timeout(function () {
                            $scope.loadValues = true;
                            $scope.select2allMasterValues.data = angular.copy($scope.values);
                        }, 100);
                    });
                }

            };
            $scope.changeValueMaster = function ()
            {
                if ($scope.editCustomField)
                {
                    angular.forEach($scope.setValueMasters, function (valueMaster)
                    {
                        if (valueMaster.value.toString() === $scope.customFieldValidation.dependantMasterValue.toString())
                        {
                            $scope.editValueId = valueMaster.value;
                            $scope.editValue = valueMaster.label;
                        }
                    });
                }
            };
            $scope.checkAngleStartRange = function (startRange)
            {
                if (startRange !== undefined)
                {
                    if (startRange > 360)
                    {
                        $scope.validation.startvalue.$setValidity("startvalue", false);
                    }
                    else
                    {
                        $scope.validation.startvalue.$setValidity("startvalue", true);
                    }
                }
            };
            $scope.checkAngleEndRange = function (endRange)
            {
                if (endRange !== undefined)
                {
                    if (endRange > 360)
                    {
                        $scope.validation.endvalue.$setValidity("endvalue", false);
                    }
                    else
                    {
                        $scope.validation.endvalue.$setValidity("endvalue", true);
                    }
                }
            };
            $scope.checkEntityIsDiamond = function (featureid)
            {
                $scope.selectedFeatureEntityType = false;
                if (featureid instanceof Object) {
                }
                else
                {
                    angular.forEach($scope.allfeatures, function (feature)
                    {
                        if (feature.value == featureid && feature.description == 'E')
                        {
                            $scope.selectedFeatureEntityType = true;
                        }

                    });
                }
            };
            $scope.checkEntityIsDiamondAndClearField = function (featureId) {
                //Replicate of above method to check if feature is isDiamond.
                $scope.selectedFeatureEntityType = false;
                if (featureId instanceof Object) {
                    featureId = featureId.id;
                }
                angular.forEach($scope.allfeatures, function (feature)
                {
                    if (feature.value == featureId && feature.description == 'E')
                    {
                        $scope.selectedFeatureEntityType = true;
                    }

                });
                if ($scope.selectedFeatureEntityType === false) {
                    if ($scope.customField.type === 'SubEntity' || $scope.customField.type === 'Pointer' || $scope.customField.type === 'Formula')
                    {
                        $scope.customField.type = undefined;
                    }
                }
            };
            // code ends here for dependant field
            $scope.checkSubEntityAddFlag = function ()
            {
                if (angular.isDefined($scope.subEntity.subFieldLabel) && angular.isDefined($scope.subEntity.componentType) && $scope.subEntity.subFieldLabel !== null && $scope.subEntity.componentType !== null && $scope.subEntity.subFieldLabel.length > 0 && $scope.subEntity.componentType.length > 0)
                {
                    $scope.disableSubEntityAdd = false;
                } else
                {
                    $scope.disableSubEntityAdd = true;
                }
            };
            $scope.storeDbTypeForSubEntity = function (subEntityType)
            {
                var dbType;
                if (subEntityType === 'Number')
                {
                    dbType = 'Double';
                }
                if (subEntityType === 'Text field')
                {
                    dbType = 'String';
                }
                if (subEntityType === 'Text area')
                {
                    dbType = 'String';
                }
                if (subEntityType === 'Percent')
                {
                    dbType = 'Double';
                }
                if (subEntityType === 'Date')
                {
                    dbType = 'Date';
                }
                if (subEntityType === 'Email')
                {
                    dbType = 'String';
                }
                if (subEntityType === 'Phone')
                {
                    dbType = 'String';
                }
                return dbType;
            };
            $scope.saveSubEntity = function ()
            {
//                console.log("error" + JSON.stringify($scope.subentityform.$error))
                if ($scope.subentityform.$valid === true) {
                    $scope.editDisableSubEntity = false;
                    $scope.saveSubEntityFlag = true;
                    if ($scope.subEntity.componentType === 'Date')
                    {
                        var dateDefault = 'Today';
                        var delimiterForDateDefault = '#@';
                        if ($scope.arithmeticOperatorForDateSub !== null && $scope.arithmeticOperatorForDateSub)
                        {
                            dateDefault += delimiterForDateDefault + $scope.arithmeticOperatorForDateSub;
                        }
                        var self = this;
                        if (angular.isDefined($scope.defaultNumberForDateSub) && $scope.defaultNumberForDateSub !== null && $scope.defaultNumberForDateSub)
                        {
                            dateDefault += delimiterForDateDefault + $scope.defaultNumberForDateSub;
                        }

                        if (dateDefault !== null && dateDefault !== undefined)
                        {
                            $scope.subEntityCustomFieldValidation.defaultDate = dateDefault;
                        }



                    }

                    if ($scope.subEntity.componentType === 'Text field')
                    {
                        if ($scope.subEntityCustomFieldValidation.allowedTypes !== null && $scope.subEntityCustomFieldValidation.allowedTypes !== undefined && $scope.subEntityCustomFieldValidation.allowedTypes.length > 0)
                        {
                            $scope.allowedTypeFlagForTextSubentity = true;
                        }
                        else
                        {
                            $scope.allowedTypeFlagForTextSubentity = false;
                        }
                    }
                    else
                    {
                        $scope.allowedTypeFlagForTextSubentity = true;
                    }

                    if ($scope.allowedTypeFlagForTextSubentity === true) {
//                if ($scope.subEntity.componentType === "Text Field")
//                {
//                    if (!angular.isDefined($scope.subEntityCustomFieldValidation.allowedTypes) && !$scope.subEntityCustomFieldValidation.allowedTypes.length > 0)
//                    {
//                        $scope.allowedTypeSubEntity = true;
//                    }
//                    else
//                    {
//                        $scope.allowedTypeSubEntity = false;
//                    }
//                }
//                if ($scope.allowedTypeSubEntity) {
                        $scope.showTable = true;
                        var subEntity = angular.copy($scope.subEntity);
                        subEntity.disableArchiveButton = false;
                        subEntity.componentType = $scope.subEntity.componentType;
                        subEntity.status = 'A';
                        subEntity.validationPattern = JSON.stringify($scope.subEntityCustomFieldValidation);
                        var dbtype = $scope.storeDbTypeForSubEntity(subEntity.componentType);
                        subEntity.subFieldType = dbtype;
                        if (subEntity.id == null) {
//if this is new contact, add it in contacts array
                            subEntity.id = $scope.subId++;
                            subEntity.isCreate = true;
                            subEntity.isUpdate = false;
                            subEntity.isArchive = false;
                            $scope.subEntityList.push(subEntity);
                            $scope.tempList = angular.copy($scope.subEntityList);
                            // Code added for handling the droplist condition for subentity
                            if ($scope.dropListSubEntity !== null && $scope.dropListSubEntity !== undefined)
                            {
                                for (var j in $scope.tempList)
                                {
                                    if ($scope.tempList[j].id.toString() === $scope.dropListSubEntity.id.toString())
                                    {
                                        $scope.dropListSubEntity.isUpdate = true;
                                        $scope.dropListSubEntity.isCreate = false;
                                        $scope.subEntityList[j] = angular.copy($scope.dropListSubEntity);
                                    }
                                }
                            }

                        } else {
//for existing contact, find this contact using id
//and update it.
                            for (i in $scope.subEntityList) {
                                subEntity.isCreate = false;
                                subEntity.isArchive = false;
                                if ($scope.dropListSubEntity !== null && $scope.dropListSubEntity !== undefined)
                                {
                                    if ($scope.subEntityList[i].id.toString() === subEntity.id.toString()) {

                                        $scope.subEntityList[i] = angular.copy(subEntity);
                                    }

                                    else if ($scope.subEntityList[i].id.toString() === $scope.dropListSubEntity.id.toString())
                                    {

                                        $scope.dropListSubEntity.isUpdate = true;
                                        $scope.dropListSubEntity.isCreate = false;
                                        $scope.subEntityList[i] = angular.copy($scope.dropListSubEntity);
                                    }
                                } else
                                {

                                    if ($scope.subEntityList[i].id.toString() === subEntity.id.toString()) {
                                        $scope.subEntityList[i] = angular.copy(subEntity);
                                    }
                                }

                            }
                        }
                        $scope.customField.subEntityDataBean = angular.copy($scope.subEntityList);
                        $scope.disableSubEntityAdd = true;
                        $scope.negativeAllowedSubEntity = false;
                        $scope.clearSubEntity();
                    }
                    $scope.localEditFlag = false;
                }
            };
            $scope.clearSubEntity = function ()
            {
                $scope.subEntity = {};
                $scope.subEntityCustomFieldValidation = {};
                $scope.subentityform.$setPristine();
                $scope.submittedForm = false;
                $scope.subEntityForm = false;
                $scope.subEntityCustomFieldValidation.allowedTypes = '';
                $scope.saveSubEntityFlag = false;
                $scope.arithmeticOperatorForDateSub = '';
                $scope.defaultNumberForDateSub = '';
                $scope.requiredDefaultNumberFlagSub = false;
                $scope.allowDefaultDateForSubEntity = false;
//                $scope.disableSubEntityAdd = true;
            };
            $scope.openEditSubentity = function (entity)
            {
                $scope.reloadAllowedTypeForSubEntity = false;
                $timeout(function () {

                    $scope.reloadAllowedTypeForSubEntity = true;
                }, 100);
                if (angular.isDefined(entity.isServerData) && entity.isServerData === true)
                {
                    $scope.editDisableSubEntity = true;
                }
                else
                {
                    $scope.editDisableSubEntity = false;
                }
                $scope.localEditFlag = true;
                $scope.subEntity = {};
                $scope.subEntityCustomFieldValidation = {};
                $scope.subEntity.subFieldLabel = entity.subFieldLabel;
                $scope.subEntity.id = entity.id;
                // To disable remove button when edit 
                angular.forEach($scope.subEntityList, function (subenty)
                {
                    if (subenty.id.toString() === entity.id.toString())
                    {
                        subenty.disableArchiveButton = true;
                    } else
                    {
                        // Else condition is because if I edit one field,now without update ,if I click edit on another field,the first field cross should appear
                        subenty.disableArchiveButton = false;
                    }
                })

                $scope.subEntity.subFieldId = entity.subFieldId;
                $scope.subEntity.componentType = entity.componentType;
//                $scope.subEntity.subFieldType = entity.componentType;

                var editdbtype = $scope.storeDbTypeForSubEntity(entity.subFieldType);
                $scope.subEntity.subFieldType = editdbtype;
                $scope.subEntity.isDroplistField = entity.isDroplistField;
                $scope.subEntity.subFieldName = entity.subFieldName;
                if (entity.subFieldId !== null)
                {
                    $scope.subEntity.isUpdate = true;
                }

                if (angular.isDefined(entity.validationPattern)) {
                    $scope.subEntityCustomFieldValidation = JSON.parse(entity.validationPattern);
                }
                $scope.allowDefaultdateForSubEntityFunc($scope.subEntityCustomFieldValidation.allowDefaultDate);
                if (angular.isDefined($scope.subEntityCustomFieldValidation.defaultDate) && $scope.subEntityCustomFieldValidation.defaultDate !== null)
                {


                    var defaultDateOption = $scope.subEntityCustomFieldValidation.defaultDate.toString().split("#@")
                    if (defaultDateOption.length >= 2) {
                        $scope.arithmeticOperatorForDateSub = defaultDateOption[1];
                        $scope.defaultNumberForDateSub = defaultDateOption[2];
                        $scope.requiredDefaultNumberFlagSub = true;
                    }
                    else
                    {
                        $scope.requiredDefaultNumberFlagSub = false;
                    }

                } else
                {
                    $scope.requiredDefaultNumberFlagSub = false;
                }
                $scope.checkValidRangeSubEntity($scope.subEntityCustomFieldValidation.negativeAllowed, $scope.subEntityCustomFieldValidation.startRange, $scope.subEntityCustomFieldValidation.endRange)

                $scope.checkSubEntityAddFlag();
            };
            $scope.removeSubEntityFromLocalList = function (index, subEntity)
            {
                if (subEntity.isDroplistField !== null && subEntity.isDroplistField !== undefined && subEntity.isDroplistField === true)
                {
                    // Don't delete droplist field
                    var msg = "You cannot delete droplist field.First make another field as droplist and then try to delete it.";
                    var type = $rootScope.error;
                    $rootScope.addMessage(msg, type);
                } else
                {
                    // delete
                    if (subEntity.subFieldId != null)
                    {
                        subEntity.isArchive = true;
                        for (i in $scope.subEntityList) {
                            subEntity.isCreate = false;
                            subEntity.isUpdate = true;
                            if ($scope.subEntityList[i].id.toString() === subEntity.id.toString()) {
                                $scope.subEntityList[i] = angular.copy(subEntity);
                            }
                        }
                    }
                    else {
                        $scope.subEntityList.splice(index, 1);
                    }
                    $scope.customField.subEntityDataBean = angular.copy($scope.subEntityList);
                }
            };
            $scope.checkForDropList = function (checkBoxValue)
            {
                if (checkBoxValue === true)
                {
                    if (angular.isDefined($scope.subEntityList) && $scope.subEntityList.length > 0)
                    {
                        angular.forEach($scope.subEntityList, function (list)
                        {
                            if (list.isDroplistField === true)
                            {
                                list.isDroplistField = false;
                            }
                        });
                    }
                }
            };
            $scope.onFieldTypeSelect = function (fieldType) {
                $scope.customField.type = fieldType;
            };
            $scope.select2Section = {
                'allowClear': true,
                'data': [],
                'placeholder': "Select"

            };
            $scope.autoCompleteDefaultValue = {
                'allowClear': true,
                'data': [],
                'placeholder': "Select",
                'multiple': true,
                initSelection: function (element, callback) {
                    if ($scope.editCustomField) {
                        var tempData = [];
                        var data = [];
                        for (var key in $scope.defaultMultiSelectedValueMap)
                        {


                            if ($scope.defaultMultiSelectedValueMap.hasOwnProperty(key))
                            {
                                tempData.push(key);
                            }

                            data.push({id: key, text: $scope.defaultMultiSelectedValueMap[key]});
                        }


                        callback(data);
                        $scope.customFieldValidation.defaultValue = angular.copy(tempData.toString());
                    }
                },
                formatResult: function (item) {
                    return item.text;
                },
                formatSelection: function (item) {
                    return item.text;
                }
            };
            // method added by Shifa on 24 December 2014 for the multiselect component for filling the options for  customfield component UserMultiSelect
            var self = this;
            $scope.changeFieldType = function ()
            {
                $scope.negativeAllowed = false;
                if ($scope.editCustomField === false) {
                    $scope.customFieldValidation = {};
                }
                if ($scope.customField.type === 'SubEntity' || $scope.customField.type === 'Dropdown' || $scope.customField.type === 'MultiSelect')
                {
                    $scope.showIsPrivate = true;
                } else
                {
                    $scope.showIsPrivate = false;
                }
                if ($scope.customField.type === 'UserMultiSelect')
                {
                    $scope.autoCompleteValues = [];
                    $scope.autoCompleteValues.push({id: '1', text: 'Employee'}, {id: '2', text: 'Designation'}, {id: '3', text: 'Department'}, {id: '4', text: 'Franchise'});
                    $scope.autoCompleteUserMultiSelect.data = angular.copy($scope.autoCompleteValues);
                    $scope.isUserMultiSelect = true;
                }
                if ($scope.customField.type === 'Text field')
                {

//                    $scope.allowedTypesForText = [];
//                    $scope.allowedTypesForText.push({id: "Numeric", text: 'Numeric'}, {id: 'Alphabet', text: 'Alphabet'}, {id: 'Special character', text: 'Special character'});
//                    $scope.allowedType.data = angular.copy($scope.allowedTypesForText);
                    $scope.isTextField = true;
                }
                if ($scope.customField.type === 'Currency' || $scope.customField.type === 'Exchange rate')
                {
                    $scope.retrieveCurrenciesForCombo();
                }
                if ($scope.customField.type === 'Formula')
                {
                    $scope.items = [];
                    $scope.reloadFormula = false;
                    $timeout(function () {

                        $scope.reloadFormula = true;
                    }, 100);
                }
                if ($scope.customField.type === 'Number' || $scope.customField.type === 'Percent' || $scope.customField.type === 'Currency' || $scope.customField.type === 'Formula')

                {
                    $scope.items = [];
                    $scope.reloadConstraint = false;
                    $timeout(function () {
                        self.constraintEntity = '';
                        self.conditionalOperator = '';
                        self.numericValue = '';
                        self.arithmeticOperator = '';
                        $scope.reloadConstraint = true;
                    }, 100);
                }
                if ($scope.customField.type === 'Pointer')
                {
                    $scope.items = [];
                    $scope.reloadPointer = false;
                    $timeout(function () {
                        $scope.reloadPointer = true;
                    }, 100);
                }
                if ($scope.customField.type === 'SubEntity')
                {
                    $scope.isSubEntity = true;
                    if ($scope.subEntity.componentType === 'Date')
                    {
                        $scope.dateList = [];
                        angular.forEach(this.arithmeticOperatorList, function (operator)
                        {
                            if (operator.label === '+' || operator.label === '-')
                            {
                                $scope.dateList.push(operator);
                            }
                        });
                        $scope.operatorList = angular.copy($scope.dateList);
                    }

                }
                else
                {
                    $scope.isSubEntity = false;
                }
            };
            // method added by Shifa on 24 December 2014 for the multiselect component for customfield component UserMultiSelect
            $scope.autoCompleteUserMultiSelect = {
                'allowClear': true,
                'data': [],
                'placeholder': "Select",
                'multiple': true,
                initSelection: function (element, callback) {
                    if ($scope.searchFlag) {
                        var data = [];
                        angular.forEach($scope.autoCompleteValues, function (recipient) {
                            var temp1 = new Array();
                            var defaultString = angular.copy($scope.customFieldValidation.defaultValue);
                            //default is a keyword. don't use default, delete as variable names in script.
                            $scope.defaultValues = angular.copy($scope.customFieldValidation.defaultValue);
                            if ($scope.defaultValues !== 'undefined') {
                                temp1 = $scope.defaultValues.split(",");
                                for (var a in temp1) {
                                    if (temp1[a] === recipient.id) {
                                        data.push({
                                            id: recipient.id,
                                            text: recipient.text
                                        });
                                    }
                                }
                            }
                            callback(data);
                            $scope.customFieldValidation.defaultValue = angular.copy(defaultString);
                        });
                    }
                    $scope.showComboForEmployee($scope.customFieldValidation.defaultValue);
                },
                formatResult: function (item) {
                    return item.text;
                },
                formatSelection: function (item) {
                    return item.text;
                }
            };
            $scope.keyCallbackMin = function () {
                $scope.customFieldValidation.minLength = '';
            };
            $scope.keyCallbackMax = function () {
                $scope.customFieldValidation.maxLength = '';
            };
            // method added by Shifa on 25 December 2014 so as to check the validation for min and max for custom field component validations 
            $scope.minLengthCheck = function ()
            {
                if (angular.isDefined($scope.customFieldValidation.maxLength) && angular.isDefined($scope.customFieldValidation.minLength))
                {
                    if (parseInt($scope.customFieldValidation.minLength) > parseInt($scope.customFieldValidation.maxLength))
                    {
                        $scope.validation.maxLength.$setValidity("maxLength", false);
                        $scope.validation.minLength.$setValidity("minLength", false);
                    }
                    else
                    {
                        $scope.validation.maxLength.$setValidity("maxLength", true);
                        $scope.validation.minLength.$setValidity("minLength", true);
                    }
                }
            };
            $scope.minLengthCheckForSubEntity = function ()
            {
                if (angular.isDefined($scope.subEntityCustomFieldValidation.maxLength) && angular.isDefined($scope.subEntityCustomFieldValidation.minLength))
                {
                    if (parseInt($scope.subEntityCustomFieldValidation.minLength) > parseInt($scope.subEntityCustomFieldValidation.maxLength))
                    {
                        $scope.subentityform.maxLength.$setValidity("maxLength", false);
                        $scope.subentityform.minLength.$setValidity("minLength", false);
                    }
                    else
                    {
                        $scope.subentityform.maxLength.$setValidity("maxLength", true);
                        $scope.subentityform.minLength.$setValidity("minLength", true);
                    }
                }
            };
            // method added by Shifa on 25 December 2014 for allowed types for the validations for textfield component
            $scope.allowedType = {
                'allowClear': true,
                'data': [{id: "Numeric", text: 'Numeric'}, {id: 'Alphabet', text: 'Alphabet'}, {id: 'Special character', text: 'Special character'}],
                'placeholder': "Select",
                'multiple': true,
                initSelection: function (element, callback) {
                    if ($scope.localEditFlag)
                    {
//                        if (Object.prototype.toString.call($scope.subEntityCustomFieldValidation.allowedTypes) !==  '[object Object]' )
                        if ($scope.subEntityCustomFieldValidation.allowedTypes instanceof Object)
                        {
                        } else
                        {
                            var tempdata = angular.copy($scope.subEntityCustomFieldValidation.allowedTypes);
                            var listOfAllowedTypes = angular.copy($scope.subEntityCustomFieldValidation.allowedTypes);
                            var data = [];
                            if (listOfAllowedTypes !== undefined)
                            {
                                var allowedTypeArray = listOfAllowedTypes.split(",");
                                angular.forEach(allowedTypeArray, function (allowedType)
                                {
                                    data.push({id: allowedType, text: allowedType});
                                })
                            }

                            callback(data);
                            $scope.subEntityCustomFieldValidation.allowedTypes = angular.copy(tempdata);
                        }
                    } else {
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
                    }

                },
                formatResult: function (item) {
                    return item.text;
                },
                formatSelection: function (item) {
                    return item.text;
                }
            };
            function setFeatures(data) {
                $scope.allfeatures = data.sort($scope.predicateBy("label"));
                angular.forEach($scope.allfeatures, function (featureData) {
                    $scope.select2allFeature.data.push({id: featureData.value, text: featureData.label});
                });
            }

//retrieval is for bringing data in tree view
            $scope.initializePage = function () {
                $rootScope.maskLoading();
                $scope.conditionalOperators.push({'greta': '>(Greater than)'});
                $scope.fileTypes.push({allowFileType: 'All'});
                for (var i = 0; i < 3; i++) {
                    if (i === 0) {
                        $scope.fileTypes.push({allowFileType: 'Image'});
                    }
                    if (i === 1) {
                        $scope.fileTypes.push({allowFileType: 'Text'});
                    }
                    if (i === 2) {
                        $scope.fileTypes.push({allowFileType: 'Media'});
                    }
                }
                $scope.fileTypes.push({allowFileType: 'Other'});
                $scope.select2allFeature.data = [];
                $scope.select2allMasters.data = [];
                $scope.select2allMasterValues.data = [];
                CustomFieldService.retrievePrerequisite(function (res) {
                    var data = res['customfieldvalues'];
                    $scope.conditionalOperatorList = data.conditionalOperatorList;
                    $scope.arithmeticOperatorList = data.arithemeticOperatorsList;
                    $scope.featureDetails = data.allFeature;
                    setFeatures(data.allFeature);
                    $scope.currencyList = data.currency;
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
// method added by Shifa on 23 December as per the new changes
            var self = this;
            // Function to be called to clear persistent values like that on search or if i change fielde type
            $scope.clearSubEntityOnSearchAndChangeFieldType = function ()
            {

                // Scenario:- If i wrote invalid regex,then form will get invalidated,then if i search,its validity has to be changed,otherwise it will show same error
                if ($scope.validation !== undefined && $scope.validation.regex !== undefined) {
                    $scope.validation.regex.$setValidity("regexPattern", true);
                }
                $scope.subEntityCustomFieldValidation = {};
                $scope.subEntitySampleInput = '';
                $scope.showSubEntitySampleInput = false;
                $scope.arithmeticOperatorForDateSub = '';
                $scope.defaultNumberForDateSub = '';
                $scope.requiredDefaultNumberFlagSub = false;
                $scope.allowDefaultDateForSubEntity = false;
                // Constraints clear
                // Resetting the parameters For constraints
                self.conditionalOperator = '';
                self.numericValue = '';
                self.arithmeticOperator = '';
                self.constraintEntity = '';
                $scope.items = [];
                $scope.reloadConstraint = false;
                $timeout(function () {

                    $scope.reloadConstraint = true;
                }, 100);
            }
            $scope.retrieveCustomFieldById = function (id)
            {
                $scope.showIsPrivate = false;
                $scope.changeDependantmasterFlag = false;
                $scope.editCount = 0;
                // For clearing search
                $scope.customLabel = '';
                if ($scope.selectedFeatureData !== undefined && $scope.selectedFeatureData.currentNode !== undefined) {
                    $scope.selectedFeatureData.currentNode.selected = undefined;
                }
// For clearing sample input for regex
                $scope.showSampleInput = false;
                $scope.sampleInput = '';
                // Clear Inputs of subentity
                $scope.subEntity = {};
                $scope.clearSubEntityOnSearchAndChangeFieldType();
//
                $scope.showConstraints = false;
                var i = 0;
                var pointerIdIncrement = 0;
                var constraintIncrement = 0;
                $scope.editCustomField = true;
                $scope.searchFlag = true;
                CustomFieldService.retriveCustomFieldById(id, function (res) {
                    $scope.customFieldInfo.featureId = res.featureId;
                    if (res.sectionId === null) {

                        $scope.retrieveSection(res.featureId, -1, true);
                    } else {
                        $scope.retrieveSection(res.featureId, res.sectionId, true);
                    }
                    $scope.checkEntityIsDiamond(res.featureId);
                    $scope.retrievedCustomDataBean = res.customFieldDataBean;
                    $scope.customField.label = $scope.retrievedCustomDataBean.label;
                    $scope.customField.seqNo = $scope.retrievedCustomDataBean.seqNo;
                    $scope.customField.isPrivate = $scope.retrievedCustomDataBean.isPrivate;
                    // To store the value of isPrivate coming from server
                    $scope.privatevalue = angular.copy($scope.retrievedCustomDataBean.isPrivate)
                    $scope.customField.status = 'Active';
                    $scope.isEditable = $scope.retrievedCustomDataBean.isEditable;
                    $scope.customFieldValidation = JSON.parse($scope.retrievedCustomDataBean.validationPattern);
                    if ($scope.retrievedCustomDataBean.defaultSelectedValue !== null)
                    {
                        $scope.customFieldValidation.defaultValue = $scope.retrievedCustomDataBean.defaultSelectedValue;
                    }
                    if ($scope.retrievedCustomDataBean.defaultMultiSelectedValueMap !== null)
                    {
                        $scope.defaultMultiSelectedValueMap = $scope.retrievedCustomDataBean.defaultMultiSelectedValueMap;
                    }
                    $scope.customField.type = $scope.retrievedCustomDataBean.type;
                    if ($scope.retrievedCustomDataBean.type === 'Number' || $scope.retrievedCustomDataBean.type === 'Percent' || $scope.retrievedCustomDataBean.type === 'Currency' || $scope.retrievedCustomDataBean.type === 'Angle')
                    {
                        $scope.checkValidRange($scope.customFieldValidation.negativeAllowed, $scope.customFieldValidation.startRange, $scope.customFieldValidation.endRange);
                    }
                    if ($scope.retrievedCustomDataBean.type === 'Upload') {

                        if ($scope.customFieldValidation.allowFileType !== null && $scope.customFieldValidation.allowFileType !== undefined && $scope.customFieldValidation.allowFileType === 'Other')
                        {
                            $scope.fileTypeList = true;
                            $scope.type.allowFileTypeList = $scope.customFieldValidation.supportedTypesForOther;
                        } else
                        {
                            $scope.fileTypeList = false;
                        }
                    }

                    if ($scope.retrievedCustomDataBean.type === 'UserMultiSelect') {
                        $scope.changeFieldType();
                        if ($scope.customFieldValidation.selectedParameter !== null && $scope.customFieldValidation.selectedParameter !== undefined)
                        {
                            $scope.setDeptOrDesgList($scope.customFieldValidation.selectedParameter);
                        }
                    }

                    if ($scope.retrievedCustomDataBean.type === 'Formula') {
//                        $scope.items = [];
//                        $scope.options = [];
                        $scope.options = angular.copy($scope.customFieldValidation.formulaOption);
                        $scope.items = angular.copy($scope.customFieldValidation.formula);
                        $scope.i = i++;
//                        $scope.shifa = $scope.options;
                        $scope.reloadFormula = false;
                        $timeout(function () {

                            $scope.reloadFormula = true;
                        }, 100);
                    }
                    if ($scope.retrievedCustomDataBean.type === 'Pointer')
                    {
                        $scope.options = angular.copy($scope.customFieldValidation.pointerOption);
                        $scope.items = angular.copy($scope.customFieldValidation.pointer);
                        $scope.pointerIdIncrement = pointerIdIncrement++;
                        $scope.reloadPointer = false;
                        $timeout(function () {

                            $scope.reloadPointer = true;
                        }, 100);
//                        $scope.items=angular.copy($scope.customFieldValidation.pointer);
//                         $scope.options=angular.copy($scope.customFieldValidation.pointer);
                    }
                    if ($scope.retrievedCustomDataBean.type === 'SubEntity') {
                        $scope.showIsPrivate = true;
                        $scope.isSubEntity = true;
                        $scope.subEntityList = angular.copy($scope.retrievedCustomDataBean.subEntityDataBean);
//                       $scope.customField.subEntityDataBean=angular.copy($scope.retrievedCustomDataBean.subEntityDataBean)
                        if (angular.isDefined($scope.subEntityList) && $scope.subEntityList.length > 0)
                        {
                            $scope.showTable = true;
                            angular.forEach($scope.subEntityList, function (subEntityList)
                            {
                                subEntityList.disableArchiveButton = false;
                                if (subEntityList.isDroplistField === true)
                                {
                                    $scope.dropListSubEntity = subEntityList;
                                }
                            });
                        }



                    }
                    else
                    {
                        $scope.isSubEntity = false;
                        $scope.checkFieldType();
                    }
                    $scope.customField.values = $scope.retrievedCustomDataBean.values;
                    $scope.customField.id = id;
                    //
                    if (($scope.customField.type === 'Dropdown'
                            || $scope.customField.type === 'Radio button') || $scope.customField.type === 'MultiSelect') {
                        $scope.showIsPrivate = true;
                        if ($scope.customField.values) {
                            $scope.createDefaultValueOption($scope.customField.values);
                            for (var i = 0; i < $scope.enteredValuesInDropdown.length; i++) {
                                if ($scope.customField.type === 'MultiSelect')
                                {
                                    $scope.enteredValuesInDropdown[i] = {id: $scope.enteredValuesInDropdown[i], text: $scope.enteredValuesInDropdown[i]};
                                }
                                else
                                {
                                    $scope.enteredValuesInDropdown[i] = {id: $scope.enteredValuesInDropdown[i], label: $scope.enteredValuesInDropdown[i]};
                                }
                            }
                            $scope.autoCompleteDefaultValue.data = angular.copy($scope.enteredValuesInDropdown);
                            $scope.multiSelect = true;
                            $scope.reloadDefaultMultiSelect = true;
                        }

                    }
                    //
                    if ($scope.retrievedCustomDataBean.type === 'Currency' || $scope.customField.type === 'Exchange rate') {
                        $scope.retrieveCurrenciesForCombo();
//                        $scope.customField.currencyMasterId = $scope.retrievedCustomDataBean.currencyMasterId;
                        $scope.isCurrencyFieldReq = true;
                    }
                    if ($scope.retrievedCustomDataBean.type === 'MultiSelect')
                    {
                        $scope.isValueFieldReq = true;
                        $scope.multiSelect = true;
                    }
                    if ($scope.retrievedCustomDataBean.type === 'Text field')
                    {
                        $scope.isTextField = true;
                        $scope.allowedTypeChars = $scope.customFieldValidation.allowedTypes;
                    }
                    if ($scope.retrievedCustomDataBean.type === 'Dropdown' || $scope.retrievedCustomDataBean.type === 'Radio button' || $scope.retrievedCustomDataBean.type === 'MultiSelect')
                    {
                        $scope.isValueFieldReq = true;
                        $scope.isCurrencyFieldReq = false;
                    }
                    else {
                        $scope.isValueFieldReq = false;
                    }
                    if ($scope.customFieldValidation.dependantMasterId !== null && $scope.customFieldValidation.dependantMasterId !== undefined)
                    {
                        $scope.setMasters();
                        $scope.setMasterValuesForDependantField($scope.customFieldValidation.dependantMasterId);
                    }
                    $scope.displayCustomPage = 'view';
                    if (angular.isDefined($scope.customFieldValidation.constraint) && $scope.customFieldValidation.constraint !== null)
                    {
                        var constraintValArray = [];
                        $scope.constraintVal = angular.copy($scope.customFieldValidation.constraint);
                        constraintValArray = $scope.constraintVal.toString().split("@");
                        // Static code when the constraint condition involves arithmetic operators
                        if (constraintValArray.length === 3)
                        {
                            self.conditionalOperator = constraintValArray[1];
                            self.constraintEntity = constraintValArray[2];
                        }
                        // Static code when the constraint condition does not involves arithmetic operators
                        else
                        {
                            self.conditionalOperator = constraintValArray[1];
                            self.numericValue = constraintValArray[2];
                            self.arithmeticOperator = constraintValArray[3];
                            self.constraintEntity = constraintValArray[4];
                        }
                        $scope.options = angular.copy($scope.customFieldValidation.constraintOption);
                        $scope.items = angular.copy(self.constraintEntity);
                        $scope.constraintIncrement = constraintIncrement++;
                        $scope.checkFieldType();
                        $scope.hasConstraintValue = true;
                    }
                    if (angular.isDefined($scope.customFieldValidation.defaultDate) && $scope.customFieldValidation.defaultDate !== null)
                    {
                        var defaultDateOption = $scope.customFieldValidation.defaultDate.toString().split("#@")
                        if (defaultDateOption.length > 2) {
                            $scope.arithmeticOperatorForDate = defaultDateOption[1];
                            $scope.defaultNumberForDate = defaultDateOption[2];
                            $scope.requiredDefaultNumberFlag = true;
                        }
                        else
                        {
                            $scope.requiredDefaultNumberFlag = false;
                        }

                    } else
                    {
                        $scope.requiredDefaultNumberFlag = false;
                    }
                    if ($scope.retrievedCustomDataBean.type === 'Date')
                    {
                        $scope.allowDefaultdate($scope.customFieldValidation.allowDefaultDate);
                        $scope.checkFieldType();
                        $scope.dateoptions = angular.copy($scope.customFieldValidation.dateOption);
                        $scope.dateitems = angular.copy($scope.defaultNumberForDate);

                        $scope.allowDefaultDate = false;
                        $timeout(function () {

                            $scope.allowDefaultDate = true;
                        }, 100);
                    }

                }, function () {
                    $rootScope.addMessage("Could not retrive custom field , please try again.", 1);
                });
            };
            $scope.retrieveSection = function (featureId, sectionId, isDirectCall) {

                if (featureId !== 'undefined' && featureId !== null && featureId !== undefined) {
                    angular.forEach($scope.featureDetails, function (feature)
                    {

                        if (feature.value == featureId) {
                            if (feature.isActive) {
                                $scope.isSectionRequired = true;
                            } else {
                                $scope.isSectionRequired = false;
                            }
                        }

                    });
                    $rootScope.maskLoading();
                    $scope.customFieldValidation = {};
                    // If you change the feature reset pointer,formula constraint directive
                    if ($scope.customField.type === 'Formula')
                    {
                        $scope.items = [];
                        $scope.reloadFormula = false;
                        $timeout(function () {

                            $scope.reloadFormula = true;
                        }, 100);
                    }
                    if ($scope.showConstraints === true)
                    {
                        $scope.items = [];
                        $scope.reloadConstraint = false;
                        $timeout(function () {

                            $scope.reloadConstraint = true;
                        }, 100);
                    }
                    if ($scope.customField.type === 'Pointer')
                    {
                        $scope.items = [];
                        $scope.reloadPointer = false;
                        $timeout(function () {
                            $scope.reloadPointer = true;
                        }, 100);
                    }
                    if (isDirectCall === true) {
                        $scope.exitingFeatureId = {};
                    }
                    sectionAndCustomFieldDetail = {};
                    if (angular.isDefined(featureId)) {
                        CustomFieldService.retrieveSectionAndCustomFieldInfoByFeatureId(featureId, function (data) {
                            sectionAndCustomFieldDetail = data;
                            $scope.sections = [];
                            $scope.sections.push({id: -1, text: "General Section"});
                            if (data.sectionList) {
                                $scope.sections = $.merge($scope.sections, data.sectionList);
                            }
                            $scope.select2Section.data = $scope.sections;
                            $timeout(function () {
                                $scope.customFieldInfo.sectionId = sectionId;
                            }, 20);
                            labelNames = [];
                            createLabelList();
                            $rootScope.unMaskLoading();
                        }, function () {
                            var msg = "Failed to retrieve Section";
                            var type = $rootScope.error;
                            $rootScope.addMessage(msg, type);
                            $rootScope.unMaskLoading();
                        });
                    } else {
                        $rootScope.unMaskLoading();
                        $scope.customFieldInfo.sectionId = null;
                        $scope.select2Section.data = [];
                    }
                }
                else
                {

                    $rootScope.unMaskLoading();
                    $scope.customFieldInfo.sectionId = null;
                    $scope.select2Section.data = [];
//                        $scope.isSectionRequired = false;

                }
            };
            function createLabelList() {
                for (var key in sectionAndCustomFieldDetail.customFieldsMap) {
                    if (sectionAndCustomFieldDetail.customFieldsMap.hasOwnProperty(key)) {
                        for (var i in sectionAndCustomFieldDetail.customFieldsMap[key]) {
                            labelNames.push(sectionAndCustomFieldDetail.customFieldsMap[key][i].label);
                        }
                    }
                }
            }
            $scope.getSearchedCustomPages = function (list) {
                $scope.isTreeView = false;
                if ($scope.selectedFeatureData !== undefined && $scope.selectedFeatureData.currentNode !== undefined) {
                    $scope.selectedFeatureData.currentNode.selected = undefined;
                }
                var enteredText = $('#searchCustomField.typeahead').typeahead('val');
                if (enteredText.length > 0) {
                    if (enteredText.length < 3) {
                        $scope.searchRecords = [];
                    } else {
                        $scope.searchRecords = [];
                        angular.forEach(list, function (item) {
                            var fieldDetails = item.text.split(",");
                            item.label = fieldDetails[0];
                            item.type = fieldDetails[1];
                            item.section = fieldDetails[2];
                            item.feature = fieldDetails[3];
                            $scope.searchRecords.push(item);
                        });
                    }
                    $scope.setViewFlag('search');
                }
            };
            $scope.setViewFlag = function (flag) {
                if (flag !== "search") {
                    $('#scrollable-dropdown-menu.typeahead').typeahead('val', '');
                }
                $scope.displayCustomPage = flag;
            };
            $scope.isCustomFieldLabelUnique = function (labelDetail, element, isFromList) {
                var labelName = labelDetail.label;
                var oldLabelName = labelDetail.oldLabelName;
                labelDetail.oldLabelName = labelDetail.label;
                if (labelName && (oldLabelName || labelName !== oldLabelName)) {
                    if (oldLabelName && isFromList) {
                        removeLabelFromArray(oldLabelName);
                    }
                    if (!labelIsInArray(labelName)) {
                        element.$setValidity('sameValue', true);
                    } else {
                        element.$setValidity('sameValue', false);
                    }
                } else {
                    $scope.isSameValue = false;
                    element.$setValidity('sameValue', true);
                }
                if (labelName && isFromList) {
                    labelNames.push(labelName);
                }
            };
            function removeLabelFromArray(label) {
                for (var i = labelNames.length - 1; i >= 0; i--) {
                    if (labelNames[i].toUpperCase() === label.toUpperCase()) {
                        labelNames.splice(i, 1);
                        break; //<-- Uncomment  if only the first term has to be removed
                    }
                }
            }

            function labelIsInArray(label) {
                if (label != null) {
                    for (var i = labelNames.length - 1; i >= 0; i--) {
                        if (labelNames[i].toUpperCase() === label.toUpperCase()) {
                            return true;
                        }
                    }
                    return false;
                }
            }
            ;
            $scope.isLabelInLocalArray = function (label)
            {
                if (label != null) {
                    for (var i = $scope.addedCustomFields.length - 1; i >= 0; i--) {
                        if ($scope.addedCustomFields[i].label.toUpperCase() == label.toUpperCase()) {
                            return true;
                        }
                    }
                    return false;
                }
            };
            $scope.retrieveSectionForExistingFeature = function (feature) {
                if (angular.isDefined(feature.currentNode) && angular.isDefined(feature.currentNode.id)) {
                    $scope.customFieldInfo.featureId = feature.currentNode.id;
                    $scope.retrieveSection($scope.customFieldInfo.featureId, -1, false);
                } else {
                    $scope.customFieldInfo.sectionId = null;
                    $scope.sections = {};
                }
            };
            $scope.showValueField = function (fieldType) {
                $scope.customField.type = fieldType;
                $scope.customFieldValidation = {};
                $scope.showSampleInput = false;
                $scope.sampleInput = '';
                if ($scope.customField.type === 'Dropdown' || $scope.customField.type === 'Radio button' || $scope.customField.type === 'MultiSelect') {
                    $scope.isValueFieldReq = true;
                } else {
                    $scope.isValueFieldReq = false;
                }
                if ($scope.customField.type === 'Currency') {
                    $scope.isCurrencyFieldReq = true;
                } else {
                    $scope.isCurrencyFieldReq = false;
                }
            };
            $scope.showValueFieldForSubEntity = function (fieldType) {

                $scope.subEntity.componentType = fieldType;
                $scope.clearSubEntityOnSearchAndChangeFieldType();
            };
            $scope.showValidationModel = function (element, form) {
                $scope.customFieldValidation.formula = '';
//                if (labelIsInArray($scope.customField.label)) {
//                    element.$setValidity('sameValue', false);
//                } 

//                else {
//                    element.$setValidity('sameValue', true);
                if (angular.isDefined($scope.customField.type))
                {
                    if (!element.select_CurrencyFieldType || element.select_CurrencyFieldType.$valid) {
                        $scope.mode = 'add';
                        $scope.fieldType = angular.copy($scope.customField.type);
                        if (($scope.customField.type === 'Dropdown'
                                || $scope.customField.type === 'Radio button') || $scope.customField.type === 'MultiSelect') {
                            if ($scope.customField.values) {
                                $scope.createDefaultValueOption($scope.customField.values);
                                for (var i = 0; i < $scope.enteredValuesInDropdown.length; i++) {
                                    if ($scope.customField.type === 'MultiSelect')
                                    {
                                        $scope.enteredValuesInDropdown[i] = {id: $scope.enteredValuesInDropdown[i], text: $scope.enteredValuesInDropdown[i]};
                                    }
                                    else
                                    {
                                        $scope.enteredValuesInDropdown[i] = {id: $scope.enteredValuesInDropdown[i], label: $scope.enteredValuesInDropdown[i]};
                                    }
                                }
                                $scope.autoCompleteDefaultValue.data = [];
                                $scope.autoCompleteDefaultValue.data = angular.copy($scope.enteredValuesInDropdown);
                                $scope.multiSelect = true;
                                // Reload Default values of multiselect when we change values
                                $scope.reloadDefaultMultiSelect = false;
                                $timeout(function () {

                                    $scope.reloadDefaultMultiSelect = true;
                                    $scope.customFieldValidation.defaultValue = '';
                                }, 100);
                            }
                            // This condition is when we clear dropdown values and default should be reloaded
                            else
                            {

                                $scope.reloadDefaultMultiSelect = false;
                                $scope.customFieldValidation.defaultValue = '';
                            }
                        }
                    }
                }

//                }

            };
            $scope.commaSepratedListIsSame = function (element) {
                var samevalue = false;
                for (var i = 0; i < $scope.enteredValuesInDropdown.length; i++) {
                    var cnt = 0;
                    for (var j = 0; j < $scope.enteredValuesInDropdown.length; j++) {
                        if ($scope.enteredValuesInDropdown[j] === $scope.enteredValuesInDropdown[i]) {
                            cnt++;
                        }
                    }
                    if (cnt > 1) {
                        samevalue = true;
                    }
                    if (samevalue === true) {
                        element['valuesList'].$setValidity('sameValue', false);
                    } else {
                        element['valuesList'].$setValidity('sameValue', true);
                    }
                }
            };
            $scope.createDefaultValueOption = function (values) {
                if (angular.isDefined(values) && values !== null) {
                    $scope.enteredValuesInDropdown = [];
                    $scope.dropDownValues = angular.copy(values);
                    var dropArray = [];
                    dropArray = $scope.dropDownValues.split(',');
                    // to avoid adding blank values in dropdown
                    angular.forEach(dropArray, function (drop)
                    {
                        if (drop != 'undefined' && drop != null && drop.length > 0)
                        {
                            $scope.enteredValuesInDropdown.push(drop);
                        }
                    });
                } else {
                    $scope.enteredValuesInDropdown = {};
                }
            };
            $scope.checkNumericRequiredFlag = function ()
            {
                if (angular.isDefined(self.arithmeticOperator) && (self.arithmeticOperator))
                {
                    $scope.arithmeticOperatorRequired = false;
                    if (angular.isDefined(self.numericValue) && self.numericValue)
                    {
                        $scope.numericValueRequired = false;
                    } else
                    {
                        $scope.numericValueRequired = true;
                    }
                }
                else
                {
                    $scope.numericValueRequired = false;
                }
            };
//            var self = this;
            $scope.checkNumericArithmeticOperatorFlag = function ()
            {
                if (angular.isDefined(self.numericValue) && (self.numericValue))
                {
                    $scope.numericValueRequired = false;
                    if (angular.isDefined(self.arithmeticOperator) && self.arithmeticOperator)
                    {
                        $scope.arithmeticOperatorRequired = false;
                    } else
                    {
                        $scope.arithmeticOperatorRequired = true;
                    }
                }
                else
                {
                    $scope.arithmeticOperatorRequired = false;
                }
            };
            $scope.setRequiredForDefaultNumber = function (arithmeticOperatorForDate)
            {

                $scope.arithmeticOperatorForDate = arithmeticOperatorForDate;
                $scope.requiredDefaultNumberFlag = false;
                if (angular.isDefined($scope.arithmeticOperatorForDate) && ($scope.arithmeticOperatorForDate))
                {
                    $scope.requiredDefaultNumberFlag = true;
                }
                else
                {
                    $scope.requiredDefaultNumberFlag = false;
                    $scope.defaultNumberForDate = '';
                    $scope.requireddefaultNumberForDateFlag = false;
                }

            };
            $scope.setRequiredForDateOperator = function (defaultNumberForDate)
            {
                $scope.defaultNumberForDate = defaultNumberForDate;
                $scope.requireddefaultNumberForDateFlag = false;
                if (angular.isDefined($scope.defaultNumberForDate) && ($scope.defaultNumberForDate))
                {
                    $scope.requireddefaultNumberForDateFlag = true;
                }
            };
            $scope.setRequiredForDefaultNumberSub = function (arithmeticOperatorForDate)
            {

                $scope.arithmeticOperatorForDateSub = arithmeticOperatorForDate;
                $scope.requiredDefaultNumberFlagSub = false;
                if (angular.isDefined($scope.arithmeticOperatorForDateSub) && ($scope.arithmeticOperatorForDateSub))
                {
                    $scope.requiredDefaultNumberFlagSub = true;
                }
                else
                {
                    $scope.requiredDefaultNumberFlagSub = false;
                    $scope.defaultNumberForDateSub = '';
                    $scope.requireddefaultNumberForDateFlagSub = false;
                }

            };
            $scope.setRequiredForDateOperatorSub = function (defaultNumberForDate)
            {
                $scope.defaultNumberForDateSub = defaultNumberForDate;
                $scope.requireddefaultNumberForDateFlagSub = false;
                if (angular.isDefined($scope.defaultNumberForDateSub) && ($scope.defaultNumberForDateSub))
                {
                    $scope.requireddefaultNumberForDateFlagSub = true;
                }
                else
                {
                    $scope.requireddefaultNumberForDateFlagSub = false;
                }
            };
            var self = this;
            $scope.addField = function (form) {
                $scope.items = [];
                $scope.submitted = true;
                if ($scope.validation.$valid) {
                    $scope.submitted = false;
                    if ($scope.customFieldValidation.allowFileType === 'Other') {
                        $scope.customFieldValidation.supportedTypesForOther = $scope.type.allowFileTypeList;
//                        $scope.customFieldValidation.allowFileType = $scope.type.allowFileTypeList;
                    }
                    if ($scope.customField.type === 'Checkbox') {
                        if ($scope.customFieldValidation.formatValue === '' || $scope.customFieldValidation.formatValue === undefined) {
                            $scope.customFieldValidation.formatValue = "after";
                        }

                        if (!angular.isDefined($scope.customFieldValidation.defaultValue) || $scope.customFieldValidation.defaultValue === null || $scope.customFieldValidation.defaultValue === undefined)
                        {
                            $scope.customFieldValidation.defaultValue = false;
                        }
                    }
// For handling required conditions for constraints
                    $scope.checkNumericArithmeticOperatorFlag();
                    $scope.checkNumericRequiredFlag();
//                    if ($scope.selectedFeatureEntityType && $scope.showConstraints)
//                    {
//
//                        if (!$scope.conditionalOperator)
//                        {
//                            $scope.constraintsValid = false;
//                        }
//                        else
//                        {
//                            $scope.constraintsValid = true;
//                        }
//                    }
//                    else
//                    {
                    $scope.constraintsValid = true;
//                    }
                    $scope.dateValidation = true;
                    if ((angular.isDefined(this.requireddefaultNumberForDateFlag) && this.requireddefaultNumberForDateFlag === true) || (angular.isDefined($scope.requiredDefaultNumberFlag) && $scope.requiredDefaultNumberFlag === true))
                    {
                        $scope.dateValidation = false;
                    }
// code ends here
// For handling constrainsts  code starts here
//                    var self = this;
//                    self.conditionalOperator = "1";
                    var constraints = '';
                    var delimiterForConstraint = '@';
                    if (angular.isDefined(self.conditionalOperator) && self.conditionalOperator !== null && self.conditionalOperator)
                    {
                        constraints += delimiterForConstraint + self.conditionalOperator;
                    }
                    if (angular.isDefined(self.numericValue) && self.numericValue !== null && self.numericValue)
                    {
                        constraints += delimiterForConstraint + self.numericValue;
                    }
                    if (angular.isDefined(self.arithmeticOperator) && self.arithmeticOperator !== null && self.arithmeticOperator)
                    {
                        constraints += delimiterForConstraint + self.arithmeticOperator;
                    }
                    if (angular.isDefined(self.constraintEntity) && self.constraintEntity !== null && self.constraintEntity)
                    {
                        constraints += delimiterForConstraint + self.constraintEntity;
                        var constraintArray = self.constraintEntity.toString().split("|");
                        $scope.constraintIds = [];
                        for (var i = 0; i < constraintArray.length; i++)
                        {
                            $scope.constraintIds.push(constraintArray[i]);
                        }
                        $scope.selectedOptionConstraint = [];
                        angular.forEach($scope.options, function (result)
                        {
                            var index = $filter('filter')($scope.constraintIds, function (rule) {
                                return rule === result.id;
                            })[0];
                            if (index !== undefined) {
                                $scope.selectedOptionConstraint.push(result);
                            }
                        });
                        $scope.customFieldValidation.constraintOption = angular.copy($scope.selectedOptionConstraint);
                    }
                    // For removing constraints once entered
                    if ($scope.hasConstraintValue === true && !self.conditionalOperator && !self.numericValue && !self.arithmeticOperator && !self.constraintEntity)
                    {
                        delete $scope.customFieldValidation.constraint;
                        delete $scope.customFieldValidation.constraintOption;
//                        $scope.customFieldValidation.constraint = '';
//                        $scope.customFieldValidation.constraintOption = '';
                    }


                    if (constraints !== null && constraints !== undefined && constraints !== '')
                    {
                        $scope.customFieldValidation.constraint = constraints;
                    }


// code ends here

                    if ($scope.customField.type === 'Pointer')
                    {
                        if ($scope.customFieldValidation.pointer !== null && $scope.customFieldValidation.pointer !== undefined) {
                            var pointerArray = $scope.customFieldValidation.pointer.split("|");
                            $scope.pointerIds = [];
                            for (var i = 0; i < pointerArray.length; i++)
                            {
                                $scope.pointerIds.push(pointerArray[i]);
                            }
                            $scope.selectedOptionPointer = [];
                            angular.forEach($scope.options, function (result)
                            {
                                var index = $filter('filter')($scope.pointerIds, function (rule) {
                                    return rule === result.id;
                                })[0];
                                if (index !== undefined) {
                                    $scope.selectedOptionPointer.push(result);
                                }
                            });
                            $scope.customFieldValidation.pointerOption = angular.copy($scope.selectedOptionPointer);
                        }
                    }

                    if ($scope.customField.type === 'Date')
                    {
                        if ($scope.defaultNumberForDate !== null && $scope.defaultNumberForDate !== undefined) {
                            var dateArray = $scope.defaultNumberForDate.split("|");
                            $scope.dateIds = [];
                            for (var i = 0; i < dateArray.length; i++)
                            {
                                $scope.dateIds.push(dateArray[i]);
                            }
                            $scope.selectedOptionDate = [];
                            angular.forEach($scope.dateoptions, function (result)
                            {
                                var index = $filter('filter')($scope.dateIds, function (rule) {
                                    return rule === result.id;
                                })[0];
                                if (index !== undefined) {
                                    $scope.selectedOptionDate.push(result);
                                }
                            });
                            $scope.customFieldValidation.dateOption = angular.copy($scope.selectedOptionDate);
                        }
                    }





                    if ($scope.customField.type === 'Formula') {
                        if ($scope.customFieldValidation.formula !== undefined) {
                            var formulaArray = $scope.customFieldValidation.formula.split("|");
                            $scope.formulaIds = [];
                            for (var i = 0; i < formulaArray.length; i++)
                            {
                                $scope.formulaIds.push(formulaArray[i]);
                            }
//                        $scope.customFieldValidation.formula = angular.copy(formulaArray.toString());
                            $scope.customFieldValidation.formulaArray = angular.copy($scope.formulaIds);
                            $scope.selectedOption = [];
                            angular.forEach($scope.options, function (result)
                            {
                                var index = $filter('filter')($scope.formulaIds, function (rule) {
                                    return rule === result.id;
                                })[0];
                                if (index !== undefined) {
                                    $scope.selectedOption.push(result);
                                }
                            });
                            angular.forEach($scope.formulaIds, function (result)
                            {
                                var index = $filter('filter')($scope.selectedOption, function (rule) {

                                    return rule.id === result;
                                })[0];
                                if (index === undefined) {
                                    $scope.selectedOption.push({id: result, text: result});
                                }

                            });
                            $scope.customFieldValidation.formulaOption = angular.copy($scope.selectedOption);
                        }
                    }
                    if ($scope.customField.type === 'UserMultiSelect')
                    {
                        var temp = new Array();
                        temp = $scope.customFieldValidation.defaultValue.split(",");
                        $scope.customFieldValidation.isEmployee = false;
                        $scope.customFieldValidation.isDesignation = false;
                        $scope.customFieldValidation.isDepartment = false;
                        $scope.customFieldValidation.isFranchise = false;
                        for (var a in temp) {
                            if (temp[a] === '1')
                            {
                                $scope.customFieldValidation.isEmployee = true;
                            }
                            if (temp[a] === '2')
                            {
                                $scope.customFieldValidation.isDesignation = true;
                            }

                            if (temp[a] === '3')
                            {
                                $scope.customFieldValidation.isDepartment = true;
                            }
                            if (temp[a] === '4')
                            {
                                $scope.customFieldValidation.isFranchise = true;
                            }

                        }
                    }
                    if ($scope.customField.type === 'MultiSelect')
                    {
//                        str.replace(/,/g, '')
                        if ($scope.customFieldValidation.defaultValue !== null && $scope.customFieldValidation.defaultValue !== undefined) {
                            $scope.customFieldValidation.defaultValue = $scope.customFieldValidation.defaultValue.replace(/,/g, '~!');
                        }
                    }
                    else if ($scope.customField.type === 'Date')
                    {
                        var dateDefault = $scope.customFieldValidation.todayDate;
                        var delimiterForDateDefault = '#@';
                        if ($scope.arithmeticOperatorForDate !== null && $scope.arithmeticOperatorForDate)
                        {
                            dateDefault += delimiterForDateDefault + $scope.arithmeticOperatorForDate;
                        }
                        if (angular.isDefined($scope.defaultNumberForDate) && $scope.defaultNumberForDate !== null && $scope.defaultNumberForDate)
                        {
                            dateDefault += delimiterForDateDefault + $scope.defaultNumberForDate;
                        }

                        if (dateDefault !== null && dateDefault !== undefined)
                        {
                            $scope.customFieldValidation.defaultDate = dateDefault;
                        }




                    }
                    $scope.customField.validationPattern = JSON.stringify($scope.customFieldValidation);
                    $scope.customField.formulaValue = angular.copy($scope.customFieldValidation.formula);
                    if ($scope.customFieldValidation.dependantMasterValue !== null && $scope.customFieldValidation.dependantMasterValue !== undefined && !$scope.customFieldValidation.dependantMasterId !== undefined && $scope.customFieldValidation.dependantMasterId !== null)
                    {
                        $scope.customFieldValidation.dependantMasterValue = $scope.customFieldValidation.dependantMasterValue.replace(/,/g, '~M');
                        $scope.customField.isDependable = true;
                        var map = {"dependantMasterValue": $scope.customFieldValidation.dependantMasterValue, "dependantMasterId": $scope.customFieldValidation.dependantMasterId};
                        $scope.customField.dependantValuesMap = map;
                    }
                    $scope.customField.validationPattern = JSON.stringify($scope.customFieldValidation);
                    if ($scope.customField.type === 'Currency' || $scope.customField.type === 'Exchange rate') {
                        $scope.customField.valueString = $scope.customFieldValidation.currency;
                    }

                    else {
//                        $scope.customField.valueString = $scope.customField.values;
                    }
                    $scope.isValueFieldReq = false;
                    $scope.isCurrencyFieldReq = false;
//                    $scope.customFieldValidation = {};
                    $scope.add_field_form.$setPristine();
                    $scope.subentityform.$setPristine();
                    $(document).find("#othertype").val("");
                }
            };
            $scope.editValidation = function (customField) {
                $scope.isEdit = $scope.i++;
                $scope.isEditPage = true;
                $scope.items = [];
                $scope.options = [];
                editCustomField = customField;
                $scope.fieldType = customField.type;
                $scope.mode = 'edit';
                if (angular.isDefined(customField.valueList)) {
                    $scope.createDefaultValueOptionForEdit(customField.valueList);
                }
                $scope.customFieldValidation = JSON.parse(customField.validationPattern);
                $scope.options = angular.copy($scope.customFieldValidation.formulaOption);
                $scope.items = angular.copy($scope.customFieldValidation.formula);
                $scope.fileTypeList = false;
                if ($scope.customFieldValidation !== null && $scope.customFieldValidation.allowFileType !== null && $scope.customFieldValidation.allowFileType !== 'All' && $scope.customFieldValidation.allowFileType !== 'Text' && $scope.customFieldValidation.allowFileType !== 'Media' && $scope.customFieldValidation.allowFileType !== 'Image') {
                    $scope.type.allowFileTypeList = angular.copy($scope.customFieldValidation.allowFileType);
                    $scope.customFieldValidation.allowFileType = 'Other';
                    $scope.fileTypeList = true;
                }
                $('#addValidationModal').modal('show');
            };
            $scope.createDefaultValueOptionForEdit = function (values) {
                if (angular.isDefined(values) && values !== null) {
                    $scope.enteredValuesInDropdown = angular.copy(values);
                } else {
                    $scope.enteredValuesInDropdown = {};
                }
            };
            $scope.onCancel = function () {
                $scope.customFieldValidation = {};
                $(document).find("#othertype").val("");
                $('#addValidationModal').modal('hide');
                $rootScope.removeModalOpenCssAfterModalHide();
            };
            $scope.editFeature = function (selectedFeatureId) {
                if (angular.isDefined(selectedFeatureId.currentNode)) {
                    $scope.customfeature = selectedFeatureId.currentNode.id;
                    $scope.customsection = selectedFeatureId.currentNode.section;
                }
            };
            $scope.confirmationForRemoveCustomField = function () {
                $('#messageModal').modal('show');
            };
            $scope.cannotremoveCustomField = function ()
            {
                $('#messageModalFormRemove').modal('hide');
                $rootScope.removeModalOpenCssAfterModalHide();
                $('.modal-backdrop').remove();
                $scope.customField.status = 'Active';
            };
// method added by Shifa on 24 December 2014 for removing custom field
            $scope.removeCustomField = function ()
            {
                $rootScope.maskLoading();
                $scope.disableOkForRemove = true;
                CustomFieldService.checkFieldIsInvolvedInOtherFields($scope.customField.id, function (result)
                {
                    if (result.fieldInvolved === true)
                    {
                        $('#messageModal').modal('hide');
                        $rootScope.removeModalOpenCssAfterModalHide();
                        $('.modal-backdrop').remove();
                        $('#messageModalFormRemove').modal('show');
                        $rootScope.unMaskLoading();
                        $scope.disableOkForRemove = false;
                    } else
                    {

                        CustomFieldService.remove($scope.customField.id, function (response) {
                            $scope.reset();
                            $scope.add_field_form.$setPristine();
                            $scope.validation.$setPristine();
                            $scope.custom_field.$setPristine();
                            $('#messageModal').modal('hide');
                            $rootScope.removeModalOpenCssAfterModalHide();
                            $('.modal-backdrop').remove();
                            DynamicFormService.storeAllCustomFieldData($rootScope.session.companyId);
                            var msg = "Custom fields removed successfully";
                            var type = $rootScope.success;
                            $rootScope.addMessage(msg, type);
                            $rootScope.unMaskLoading();
                            $scope.disableOkForRemove = false;
                        }, function () {
                            var msg = "The fields could not be saved, please try again.";
                            var type = $rootScope.error;
                            $rootScope.addMessage(msg, type);
                            $rootScope.unMaskLoading();
                            $scope.disableOkForRemove = false;
                        });
                    }
                });
            };
            // method added by Shifa on 24 December 2014 for hiding the confirmation popup on removing customfield
            $scope.hideconformationForRemoveCustomField = function () {
                $('#messageModal').modal('hide');
                $rootScope.removeModalOpenCssAfterModalHide();
                $('.modal-backdrop').remove();
                $scope.customField.status = 'Active';
            };
            // code modified by Shifa on 23 December 2014 as per new requirement
            $scope.save = function () {
// Seperate flag taken as before success reset function will be called which will make the edit flag false
                $scope.editFlagForSuccessMessage = angular.copy($scope.editCustomField);
                $scope.submittedForm = true;
                $scope.featureSubmited = true;
                // Code added by Shifa for checking that the subEntityList contains atleast one field as droplist
                if ($scope.customField.type === 'SubEntity') {
                    if (angular.isDefined($scope.customField.subEntityDataBean) && $scope.customField.subEntityDataBean.length > 0)
                    {
                        var countDropList = 0;
                        angular.forEach($scope.customField.subEntityDataBean, function (subEntityList)
                        {
                            if (subEntityList.isDroplistField === true)
                            {
                                countDropList++;
                            }
                        });
                        if (countDropList > 0)
                        {
                            $scope.dropListRequired = false;
                        } else
                        {
                            $scope.dropListRequired = true;
                            $scope.submittedForm = false;
                            $scope.subentityform.$setPristine();
                            $rootScope.addMessage("You have to select atleast one subfield as droplist", 1);
                        }
                    } else
                    {
// For craete only
                        if ($scope.searchFlag === false) {
                            $scope.dropListRequired = true;
                            $scope.submittedForm = false;
                            $scope.subentityform.$setPristine();
                            $rootScope.addMessage("You have to create and select atleast one subfield as droplist", 1);
                        } else
                        {
                            // It will go in this block when we will retrive and dnt modify..So in this case obviously we will have a droplist
                            $scope.dropListRequired = false;
                        }
                    }
                }
                // code ends here
                // Code added By Shifa on 31 January 2014 for handling the validation of allowed types of characters for textfield
                if ($scope.customField.type === 'Text field')
                {
                    if ($scope.customFieldValidation.allowedTypes !== null && $scope.customFieldValidation.allowedTypes !== undefined && $scope.customFieldValidation.allowedTypes.length > 0)
                    {
                        $scope.allowedTypeFlagForText = true;
                    }
                    else
                    {
                        $scope.allowedTypeFlagForText = false;
                    }
                }
                else
                {
                    $scope.allowedTypeFlagForText = true;
                }
                // code ends here
//                console.log("droplosy" + !$scope.dropListRequired +"asdsa"+$scope.allowedTypeFlagForText);
//             console.log("$scope.add_field_form.$valid"+$scope.add_field_form.$valid);
//             console.log("$scope.validation.$valid "+$scope.validation.$valid );
                //             console.log("$scope.custom_field.$valid"+$scope.custom_field.$valid);
//             console.log("!$scope.arithmeticOperatorRequired "+!$scope.arithmeticOperatorRequired );
//             console.log("!$scope.numericValueRequired"+!$scope.numericValueRequired);
//                console.log("!$scope.constraintsEntityRequired" + !$scope.constraintsEntityRequired);

                if (self.conditionalOperator && !self.constraintEntity)
                {
                    self.constraintsEntityRequired = true;
                }
                else
                {
                    self.constraintsEntityRequired = false;
                }

                if ($scope.add_field_form.$valid && $scope.validation.$valid && $scope.custom_field.$valid && !$scope.arithmeticOperatorRequired && !$scope.numericValueRequired && self.constraintsEntityRequired === false && !$scope.dropListRequired && $scope.allowedTypeFlagForText)
                {
                    if ($scope.customField.status === 'Remove') {
                        $scope.confirmationForRemoveCustomField();
                    } else {
                        $scope.addField();
                        if ($scope.add_field_form.$valid && $scope.validation.$valid && $scope.custom_field.$valid && !$scope.arithmeticOperatorRequired && !$scope.numericValueRequired && $scope.constraintsValid && self.constraintsEntityRequired === false)
                        {
                            $scope.customFieldInfo.customFieldDataBean = angular.copy($scope.customField);
                            if (angular.isDefined($scope.customFieldInfo.customFieldDataBean.valueList)) {
                                delete   $scope.customFieldInfo.customFieldDataBean.valueList;
                            }
                            if (angular.isDefined($scope.customFieldInfo.customFieldDataBean.valueString)) {
                                delete   $scope.customFieldInfo.customFieldDataBean.valueList;
                            }
                            $rootScope.maskLoading();
                            CustomFieldService.createCustomField($scope.customFieldInfo, function (response) {
                                var responseMap = JSON.parse(angular.toJson(response));
                                if (responseMap !== null && responseMap !== undefined && (responseMap['IslabelExist'] === false || responseMap['IslabelExist'] === 'false'))
                                {
                                    $scope.reset();
                                    $scope.add_field_form.$setPristine();
                                    $scope.validation.$setPristine();
                                    $scope.custom_field.$setPristine();
                                    $scope.subentityform.$setPristine();
                                    if (!checkFeatureAlreadyAvailable($scope.customFieldInfo.featureId)) {
                                        CustomFieldService.retrieveFeatures(function (data) {
                                            $scope.featureWithCustomField = data.exitingFeature;
                                            if (!angular.isDefined($scope.featureWithCustomField)) {
                                                $scope.featureWithCustomField = [];
                                            }
                                        }, function () {

                                        });
                                    }
                                    $rootScope.unMaskLoading();
                                    var msg;
                                    if ($scope.editFlagForSuccessMessage) {
                                        msg = "Custom fields updated successfully";
                                    } else
                                    {
                                        msg = "Custom fields created successfully";
                                    }
                                    var type = $rootScope.success;
                                    $rootScope.addMessage(msg, type);
                                    DynamicFormService.storeAllCustomFieldData($rootScope.session.companyId);
                                    $rootScope.unMaskLoading();
                                }
                                else
                                {
                                    // When concurrent users access and save with same label 
                                    $rootScope.unMaskLoading();
                                    var msg = "The fields could not be saved as custom field with same label already exists";
                                    var type = $rootScope.error;
                                    $rootScope.addMessage(msg, type);
                                }
                            }, function () {
                                $rootScope.unMaskLoading();
                                var msg = "The fields could not be saved, please try again.";
                                var type = $rootScope.error;
                                $rootScope.addMessage(msg, type);
                            });
                        }
                    }
                }

            };
            function checkFeatureAlreadyAvailable(id) {
                var result = false;
                if ($scope.featureWithCustomField) {
                    $.each($scope.featureWithCustomField, function (index, val) {
                        if (val.id === id) {
                            result = true;
                            return false;
                        }
                    });
                }
                return result;
            }

            var self = this;
            $scope.reset = function () {
                $scope.showDesgList = false;
                $scope.showDeptList = false;
                $scope.showComboForDepOrDesg = false;
                $scope.showIsPrivate = false;
                $scope.negativeAllowed = false;
                $scope.changeDependantmasterFlag = false;
                $scope.disableOkForRemove = false;
                $scope.showSubEntitySampleInput = false;
                $scope.showSampleInput = false;
                if ($scope.selectedFeatureData !== undefined && $scope.selectedFeatureData.currentNode !== undefined) {
                    $scope.selectedFeatureData.currentNode.selected = undefined;
                }
                // For clearing search input 
                $scope.hasConstraintValue = false;
                $scope.customLabel = '';
                $scope.editDisableSubEntity = false;
                $scope.allowDefaultDate = false;
                $scope.isTreeView = false;
                $scope.reloadFormula = false;
                $scope.reloadPointer = false;
                $scope.reloadConstraint = false;
                $scope.localEditFlag = false;
                $scope.subEntity = {};
                $scope.subEntityCustomFieldValidation = {};
                $scope.editCustomField = false;
                $scope.dropListRequired = false;
                $scope.isSubEntity = false;
                $scope.subId = 0;
                $scope.isValueFieldReq = false;
                $scope.showConstraints = false;
                $scope.selectedFeatureEntityType = undefined;
                $scope.isEditable = true;
                $scope.multiSelect = false;
                $scope.isCurrencyFieldReq = false;
                $scope.searchFlag = false;
                $scope.submittedForm = false;
                $scope.addedCustomFields = [];
                $scope.isSectionRequired = false;
                $scope.customFieldValidation = {};
                $scope.customField = {};
                $scope.customFieldInfo.featureId = null;
                $scope.customFieldInfo.sectionId = null;
                $scope.select2Section.data = [];
                $scope.exitingFeatureId = {};
                $scope.featureSubmited = false;
                $scope.sectionSubmited = false;
                $scope.isUserMultiSelect = false;
                $scope.isTextField = false;
                // Resetting the parameters For constraints
                self.conditionalOperator = '';
                self.numericValue = '';
                self.arithmeticOperator = '';
                self.constraintEntity = '';
                $scope.defaultNumberForDate = '';
                $scope.arithmeticOperatorForDate = '';
                $scope.requireddefaultNumberForDateFlag = false;
                $scope.requiredDefaultNumberFlag = false;
                $scope.numericValueRequired = false;
                $scope.arithmeticOperatorRequired = false;
                self.constraintsEntityRequired = false;
                $scope.subEntityList = [];
                $scope.displayCustomPage = 'view';
                $scope.disableSubEntityAdd = true;
                $scope.allowedTypeSubEntity = false;
                $scope.select2allMasters.data = [];
                $scope.select2allMasterValues.data = [];
                if (angular.isDefined($scope.add_field_form)) {
                    $scope.add_field_form.$setPristine();
                }
                $scope.reloadDefaultMultiSelect = false;
                $scope.reloadAllowedTypeForSubEntity = true;
                $('#scrollable-dropdown-menu.typeahead').typeahead('val', '');
//                $scope.reloadConstraint = false;
//                console.log("constraimt entity inreset" + self.constraintEntity + 'tim')
//                $timeout(function () {
//                    console.log("constraimt entity inreset" + self.constraintEntity)
//                    self.constraintEntity = '';
//                    console.log("constraimt entity inreset" + self.constraintEntity)
//                    $scope.reloadConstraint = true;
//                }, 50);
//
            };
            $scope.allowFileTypeChanged = function (customFieldValidation) {
                if (customFieldValidation != null && (customFieldValidation.toString() === 'Other' || (customFieldValidation.toString() !== 'Image' && customFieldValidation.toString() !== 'Text') && customFieldValidation.toString() !== 'Media' && customFieldValidation.toString() !== 'All')) {
                    $scope.fileTypeList = true;
                } else {
                    $scope.submitted = false;
                    $scope.fileTypeList = false;
                    $scope.validation.othertype.$dirty = false;
                    $(document).find("#othertype").val("");
                }
            };
            // Methods For handling currency component added by Shifa on 19 March 2015

            $scope.retrieveCurrenciesForCombo = function () {
                var currencyFailure = function () {
                    var msg = "Failed to retrieve currency. Try again.";
                    var type = $rootScope.failure;
                    $rootScope.addMessage(msg, type);
                    $rootScope.unMaskLoading();
                };
                CustomFieldService.retrieveCurrencies(function (res) {
                    $scope.allcurrencyList = angular.copy(res);
                }, currencyFailure);
            };
            $scope.checkPattern = function (displayFormat) {
                var format = "^[#.,]*$";
                if (displayFormat != null && !displayFormat.match(format)) {
                    if ($scope.customFieldValidation.format.length > 1) {
                        $scope.customFieldValidation.format = $scope.customFieldValidation.format.substring(0, $scope.customFieldValidation.format.length - 1);
                    }
                    else {
                        $scope.customFieldValidation.format = null;
                    }
                }
            };
// Methods For Currency ends here

//            $(function () {
//                $('select.selectized,input.selectized').each(function () {
//                    var $container = $('<div>').addClass('value').html('Current Value: ');
//                    var $value = $('<span>').appendTo($container);
//                    var $input = $(this);
//                    var update = function (e) {
//                        $value.text(JSON.stringify($input.val()));
//                    }
//
//                    $(this).on('change', update);
//                    update();
//                    $container.insertAfter($input.next());
//                });
//            });
            // Methods for Formula Component added by Shifa
            var math = mathjs();
            $scope.mathHint =
                    "<NOBR><font color='red;'>Use the shortcuts to create formula</font></NOBR><br/>\ " +
                    "<table cellpadding='0' cellspacing='0'>\ " +
                    "<tr><td>'SUM(argument)'  </td> <td> &nbsp;  &nbsp;</td><td align='left'>Sum</td></tr>\ " +
                    "<tr><td>'MIN(argument)'  </td> <td> &nbsp; &nbsp;</td><td align='left'>Minimum</td></tr>\ " +
                    "<tr><td>'MAX(argument)'  </td> <td> &nbsp; &nbsp;</td><td align='left'>Maximum</td></tr>\ " +
                    "<tr><td>'AVG(argument)'  </td> <td> &nbsp; &nbsp;</td><td align='left'>Average</td></tr>\\n\ " +
                    "<tr><td>'COUNT(argument)'  </td> <td> &nbsp; &nbsp;</td><td align='left'>Count</td></tr>\ " +
                    "</table>\ ";
            $scope.i18FormulaConfiguration = "FORMULACONFIGURATION";
            $scope.selectizeOptions = {
                plugins: ['drag_drop'],
                persist: false,
                preload: true,
                enableDuplicate: true,
                hideSelected: false,
                valueField: 'id',
                labelField: 'text',
                searchField: 'text',
                delimiter: '|',
                options: [],
                items: [],
                initData: true,
                render: {
                    option: function (item, escape) {
                        return '<div>' + escape(item.text) + '</div>';
                    }

                },
                create: function (input, callback) {

                    return {"id": input, "text": input};
                },
                load: function (query, callback) {
                    var newUrl = $rootScope.appendAuthToken($rootScope.apipath + 'customfield/searchCustomFields?search=' + (query));
                    if (!query.length)
                        return callback();
                    $.ajax({
                        url: newUrl,
                        type: 'GET',
                        error: function () {
                            callback();
                        },
                        success: function (res) {
                            for (key in res)
                            {
                                $scope.options.push({id: key, text: res[key]});
                            }

                            callback($scope.options.slice(0, 200));
                        }
                    });
                }

            };
            $scope.items = [];
            $scope.options = [];
            $scope.findField = function (fieldId) {

                var array = ['+', '-', '*', '/', '(', ')'];
                $scope.modifiedOptions = angular.copy($scope.options);
                angular.forEach($scope.modifiedOptions, function (modified)
                {
                    if (array.indexOf(modified.id) > -1)
                    {
                        modified.id = '';
                    }
                });
                for (var i = 0; i < $scope.modifiedOptions.length; i++) {
                    if ($scope.modifiedOptions[i].id === fieldId) {
                        return true;
                    }

                }
            };
            $scope.generateFormula = function () {
                var form = angular.copy($scope.customFieldValidation.formula);
                var formula = form.split("|");
                var generatedFormula = "";
                var mulitpliedField = 0;
                for (var i = 0; i < formula.length; i++) {
                    if ($scope.findField(formula[i])) {
                        mulitpliedField++;
                        if (mulitpliedField >= 2) {
                            generatedFormula += "Invalid";
                        }
                    } else {
                        mulitpliedField = 0;
                    }
                    generatedFormula += formula[i];
                }
                console.log("Generated formula" + generatedFormula);
                return generatedFormula;
            };
            $scope.saveFormula = function () {
                alert($scope.generateFormula());
            };
            $scope.validateMathematicalExpressions = function (formulaWithValue, formulaConfigForm, func)
            {
                var sumIndex = formulaWithValue.indexOf(func);
                var sumFullExpression = formulaWithValue.substring(sumIndex, formulaWithValue.length);
                if (sumFullExpression.indexOf(")") >= 0)
                {
                    var indexOfFirstParenthesis = sumFullExpression.indexOf("(");
                    var indexOfLstParenthesis = sumFullExpression.indexOf(")");
                    var sumExp = sumFullExpression.substring(0, indexOfLstParenthesis + 1);
                    if (sumExp === func + "(1)")
                    {
                        var result = formulaWithValue.replace(sumExp, "1");
                        $scope.evalMathExp = true;
                    }
                    else
                    {
                        $scope.evalMathExp = false;
                        formulaConfigForm.formula.$setValidity("invalidFormula", false);
                    }


                }
                else
                {
                    $scope.evalMathExp = false;
                    formulaConfigForm.formula.$setValidity("invalidFormula", false);
                }

                return result;
            };
            $scope.validateFormula = function (formulaConfigForm) {
                var formula = $scope.generateFormula();
                console.log("generate formual..." + formula)
                if (formula.indexOf('Invalid') > -1) {
                    formulaConfigForm.formula.$setValidity("invalidFormula", false);
                } else {

                    var formulaWithValue = formula;
                    var array = ['+', '-', '*', '/'];
                    for (var i = 0; i < $scope.options.length; i++) {
                        if (formula.toString().indexOf($scope.options[i].id.toString()) > -1) {
                            if (array.indexOf($scope.options[i].id.toString()) < 0) {
                                console.log("id..." + $scope.options[i].id)
//                                var re = new RegExp($scope.options[i].id, 'g');
//                                console.log("Re..."+re +"formuka with val"+formulaWithValue.toString())
                                formulaWithValue = formulaWithValue.toString().replace($scope.options[i].id, "1");
//                                console.log("formula with value.."+formulaWithValue);
                            }
                        }
                    }
//                    try {
                    var result1;
                    var mathFunctions = ["SUM", "MIN", "MAX", "AVG", "COUNT"];
                    mathFunctions.forEach(function (func) {
//                            console.log("formulaWithValue.."+formulaWithValue.toString().indexOf(func))
                        if (formulaWithValue.toString().indexOf(func) >= 0) {
//                                console.log("form wid val..."+formulaWithValue.toString() +"fuincccccccccccc"+func)
                            formulaWithValue = $scope.validateMathematicalExpressions(formulaWithValue, formulaConfigForm, func);
                            if (formulaWithValue.indexOf(func) >= 0)
                            {
                                formulaWithValue = $scope.validateMathematicalExpressions(result1, formulaConfigForm, func);
                            }

                            else
                            {
                            }
                        }
                        else
                        {
                            if (result1 === undefined)
                            {
                                result1 = formulaWithValue;
                            }
                        }
                    });
                    try {
                        var matheval = math.eval(formulaWithValue);
                        formulaConfigForm.formula.$setValidity("invalidFormula", true);
                    } catch (e) {
                        console.log(e);
                        formulaConfigForm.formula.$setValidity("invalidFormula", false);
                    }
//                    } 
//                    catch (e) {
//                        console.log(e);
////                         formulaConfigForm.formula.$setValidity("invalidFormula", false);
//                    }
                }
            };
// Methods added By Shifa For Pointer Component
            $scope.selectizeOptionsForPointer = {
                plugins: ['drag_drop'],
                persist: false,
                preload: false,
                enableDuplicate: true,
                hideSelected: false,
                valueField: 'id',
                labelField: 'text',
                searchField: 'text',
                delimiter: '%',
                options: [],
                items: [],
                initData: true,
                maxItems: 1,
                render: {
                    option: function (item, escape) {
                        return '<div>' + escape(item.text) + '</div>';
                    }

                },
                // This has been commented because in pointer we dont want user to add his own values apart from suggestions
//                create: function (input, callback) {
//                    return {"id": input, "text": input};
//                },
                load: function (query, callback) {
                    var newUrl = $rootScope.appendAuthToken($rootScope.apipath + 'customfield/searchCustomFieldsForPointer?search=' + (query));
                    if (!query.length)
                        return callback();
                    $.ajax({
                        url: newUrl,
                        type: 'GET',
                        error: function () {
                            callback();
                        },
                        success: function (res) {

                            for (key in res)
                            {
                                $scope.options.push({id: key, text: res[key]});
                            }

                            callback($scope.options.slice(0, 200));
                        }
                    });
                }

            };
// Methods added By Shifa For Constraints Component
            var self = this;
            $scope.checkFieldType = function ()
            {
                if ($scope.customField.type === 'Number' || $scope.customField.type === 'Percent' || $scope.customField.type === 'Currency' || $scope.customField.type === 'Angle')
                {
                    $scope.showConstraints = true;
                    $scope.selectedCustomFieldType = 'Number';
                    $scope.operatorList = angular.copy(this.arithmeticOperatorList);
                    //Code for reset

//                    $scope.items = [];
//                    var temp = angular.copy(self.constraintEntity);
                    $scope.reloadConstraint = false;
                    $timeout(function () {
//                        $scope.constraintEntity = angular.copy(temp);
                        $scope.reloadConstraint = true;
                    }, 50);
                }
                else
                if ($scope.customField.type === 'Date')
                {
                    $scope.showConstraints = true;
                    $scope.selectedCustomFieldType = 'Date';
                    $scope.dateList = [];
                    angular.forEach(this.arithmeticOperatorList, function (operator)
                    {
                        if (operator.label === '+' || operator.label === '-')
                        {
                            $scope.dateList.push(operator);
                        }
                    });
                    $scope.operatorList = angular.copy($scope.dateList);
                    //Code for reset
//                    $scope.items = [];
                    $scope.reloadConstraint = false;
                    $timeout(function () {

                        $scope.reloadConstraint = true;
                    }, 50);
                }
                else if ($scope.customField.type === 'SubEntity')
                {
                    if ($scope.subEntity.componentType === 'Date')
                    {
                        $scope.dateList = [];
                        angular.forEach(this.arithmeticOperatorList, function (operator)
                        {
                            if (operator.label === '+' || operator.label === '-')
                            {
                                $scope.dateList.push(operator);
                            }
                        });
                        $scope.operatorList = angular.copy($scope.dateList);
                    }
                }
                else
                {
                    $scope.showConstraints = false;
                }
            };
            $scope.selectizeOptionsForConstraints = {
                plugins: ['drag_drop'],
                persist: false,
                preload: false,
                enableDuplicate: true,
                hideSelected: false,
                valueField: 'id',
                labelField: 'text',
                searchField: 'text',
                delimiter: '%',
                options: [],
                items: [],
                initData: true,
                maxItems: 1,
                render: {
                    option: function (item, escape) {
                        return '<div>' + escape(item.text) + '</div>';
                    }

                },
                // This has been commented because in pointer we dont want user to add his own values apart from suggestions
//                create: function (input, callback) {
//
//                    return {"id": input, "text": input};
//                },
                load: function (query, callback) {

                    var q = $scope.selectedCustomFieldType + '_' + query;
                    var newUrl = $rootScope.appendAuthToken($rootScope.apipath + 'customfield/searchCustomFieldsForConstraints?search=' + q);
                    if (!query.length)
                        return callback();
                    $.ajax({
                        url: newUrl,
                        type: 'GET',
                        error: function () {
                            callback();
                        },
                        success: function (res) {

                            for (key in res)
                            {
                                $scope.options.push({id: key, text: res[key]});
                            }

                            callback($scope.options.slice(0, 200));
                        }
                    });
                }

            };
            // Method added by Shifa for handling regex
            $scope.showSubEntitySampleInputTextBox = function (regexSubEntity)
            {
                if (!!regexSubEntity && regexSubEntity.length > 0) {
                    $scope.showSubEntitySampleInput = true;
                }
                else
                {
                    $scope.showSubEntitySampleInput = false;
                    $scope.subentityform.regexSub.$setValidity("regexPattern", true);
                }

            };
            $scope.validateRegExPattern = function (sampleInput, regex)
            {
                $scope.sampleInput = sampleInput;
                $scope.customFieldValidation.regex = angular.copy(regex);
                var modifiedCustomField = angular.copy($scope.customFieldValidation.regex);
                var finalString = angular.copy($scope.customFieldValidation.regex.toString());
                if ($scope.customFieldValidation.regex.toString().charAt(0) === "/")
                {
                    modifiedCustomField = $scope.customFieldValidation.regex.toString().substr(1);
                }
                if ($scope.customFieldValidation.regex.toString().charAt(($scope.customFieldValidation.regex.length) - 1) === "/")
                {
                    finalString = modifiedCustomField.toString().substr(0, modifiedCustomField.toString().length - 1);
                }
                try {
//                    console.log("in try" + finalString);
                    $scope.regex = new RegExp(angular.copy(finalString.toString()));
                    if (!!$scope.customFieldValidation.regex && $scope.customFieldValidation.regex.toString().length > 0 && !!sampleInput && sampleInput.length > 0) {
                        if ($scope.regex.test(sampleInput)) {
                            $scope.validation.regex.$setValidity("regexPattern", true);
                        } else {
                            $scope.validation.regex.$setValidity("regexPattern", false);
                        }
                    } else
                    {
                        $scope.validation.regex.$setValidity("regexPattern", true);
                    }
                    $scope.customFieldValidation.regexModified = angular.copy($scope.regex.toString());
                } catch (e)
                {
//                    console.log("in catch" + finalString)
                    $scope.validation.regex.$setValidity("regexPattern", false);
//                    console.log("e" + e);
                    $scope.showSampleInput = false;
                }
            };
            // Method added by Shifa for handling subentity
            $scope.showSampleInputTextBox = function (regex)
            {
                if (!!regex && regex.length > 0) {
                    $scope.showSampleInput = true;
                }
                else
                {
                    $scope.showSampleInput = false;
                    $scope.validation.regex.$setValidity("regexPattern", true);
                }

            };
            $scope.validateSubEntityRegExPattern = function (sampleInput, regex)
            {
                $scope.sampleInput = sampleInput;
                $scope.subEntityCustomFieldValidation.regex = angular.copy(regex);
                var modifiedCustomField = angular.copy($scope.subEntityCustomFieldValidation.regex);
                var finalString = angular.copy($scope.subEntityCustomFieldValidation.regex.toString());
                if ($scope.subEntityCustomFieldValidation.regex.toString().charAt(0) === "/")
                {
                    modifiedCustomField = $scope.subEntityCustomFieldValidation.regex.toString().substr(1);
                }
                if ($scope.subEntityCustomFieldValidation.regex.toString().charAt(($scope.subEntityCustomFieldValidation.regex.length) - 1) === "/")
                {
                    finalString = modifiedCustomField.toString().substr(0, modifiedCustomField.toString().length - 1);
                }
                try {
                    $scope.subRegex = new RegExp(angular.copy(finalString.toString()));
                    if (!!$scope.subEntityCustomFieldValidation.regex && $scope.subEntityCustomFieldValidation.regex.toString().length > 0 && !!sampleInput && sampleInput.length > 0) {
                        if ($scope.subRegex.test(sampleInput.toString())) {
                            $scope.subentityform.regexSub.$setValidity("regexPattern", true);
                        } else {
                            $scope.subentityform.regexSub.$setValidity("regexPattern", false);
                        }
                    } else
                    {
                        $scope.subentityform.regexSub.$setValidity("regexPattern", true);
                    }
                    $scope.subEntityCustomFieldValidation.regexModified = angular.copy($scope.subRegex.toString());
                } catch (e)
                {
                    $scope.subentityform.regexSub.$setValidity("regexPattern", false);
                    $scope.showSubEntitySampleInput = false;
                }
            };
            $scope.checkSizeOfFile = function (fileSize)
            {
                if (fileSize !== null && fileSize !== undefined)
                {
                    if (parseFloat(fileSize) > 100 || parseFloat(fileSize) === 0)
                    {
                        $scope.validation.maxFileSize.$setValidity("maxSize", false);
                    }
                    else
                    {
                        $scope.validation.maxFileSize.$setValidity("maxSize", true);
                    }
                } else
                {
                    $scope.validation.maxFileSize.$setValidity("maxSize", true);
                }
            };
            $scope.allowDefaultdate = function (allowDate)
            {
                if (allowDate !== null && allowDate !== undefined) {

                    $scope.allowDefaultDate = allowDate;
                    if (!$scope.allowDefaultDate)
                    {
                        $scope.arithmeticOperatorForDate = '';
                        $scope.defaultNumberForDate = '';
                    }

                }
            };
            $scope.allowDefaultdateForSubEntityFunc = function (allowDate)
            {

                if ($scope.subEntity.componentType === 'Date')
                {
                    $scope.dateList = [];
                    angular.forEach(this.arithmeticOperatorList, function (operator)
                    {
                        if (operator.label === '+' || operator.label === '-')
                        {
                            $scope.dateList.push(operator);
                        }
                    });
                    $scope.operatorList = angular.copy($scope.dateList);
                }
                if (allowDate !== null && allowDate !== undefined) {

                    $scope.allowDefaultDateForSubEntity = allowDate;
                    if ($scope.allowDefaultdateForSubEntity === false || $scope.allowDefaultdateForSubEntity === 'false' || $scope.allowDefaultdateForSubEntity === undefined)
                    {
                        $scope.arithmeticOperatorForDateSub = '';
                        $scope.defaultNumberForDateSub = '';
                    }

                }
            };
            $scope.checkBeforePrecision = function ()
            {
                if ($scope.customFieldValidation.digitsBefore !== null && $scope.customFieldValidation.digitsBefore !== undefined && parseInt($scope.customFieldValidation.digitsBefore.toString()) === 0)
                {
                    $scope.customFieldValidation.digitsBefore = '';
                }

            };
            $scope.checkBeforePrecisionForSubEntity = function ()
            {
                if ($scope.subEntityCustomFieldValidation.digitsBefore !== null && $scope.subEntityCustomFieldValidation.digitsBefore !== undefined && parseInt($scope.subEntityCustomFieldValidation.digitsBefore.toString()) === 0)
                {
                    $scope.subEntityCustomFieldValidation.digitsBefore = '';
                }

            };
            // Method to check whether range should accept negative values or not
            $scope.checkValidRange = function (negativeAllowed, startRange, endRange)
            {
                if (negativeAllowed !== null && negativeAllowed !== undefined)
                {

                    $scope.negativeAllowed = negativeAllowed;
                    if (startRange !== null && startRange !== undefined)
                    {


                        if ($scope.negativeAllowed === false && parseFloat(startRange) < 0)
                        {
                            if ($scope.validation.startvalue !== undefined) {
                                $scope.validation.startvalue.$setValidity("negativeAllowedStart", false);
                            }
                        } else
                        {
                            if ($scope.validation.startvalue !== undefined) {
                                $scope.validation.startvalue.$setValidity("negativeAllowedStart", true);
                            }
                        }

                    }
                    if (endRange !== null && endRange !== undefined)
                    {
                        if ($scope.negativeAllowed === false && parseFloat(endRange) < 0)
                        {
                            if ($scope.validation.endvalue !== undefined) {
                                $scope.validation.endvalue.$setValidity("negativeAllowedEnd", false);
                            }
                        } else
                        {
                            if ($scope.validation.endvalue !== undefined) {
                                $scope.validation.endvalue.$setValidity("negativeAllowedEnd", true);
                            }
                        }

                    }

                } else
                {
                    $scope.negativeAllowed = false;
                }

            };
            $scope.checkRangeIsValid = function (startRange, endRange)
            {
                if (startRange !== null && startRange !== undefined)
                {
                    if (startRange === '-0')
                    {
                        $scope.validation.startvalue.$setValidity("negativeAllowedStart", false);
                    }
                    else if ($scope.negativeAllowed === false && parseFloat(startRange) < 0)
                    {
                        $scope.validation.startvalue.$setValidity("negativeAllowedStart", false);
                    }
                    else
                    {
                        $scope.validation.startvalue.$setValidity("negativeAllowedStart", true);
                    }
                }
                if (endRange !== null && endRange !== undefined)
                {
                    if (endRange === '-0')
                    {
                        $scope.validation.endvalue.$setValidity("negativeAllowedEnd", false);
                    } else if ($scope.negativeAllowed === false && parseFloat(endRange) < 0)
                    {
                        $scope.validation.endvalue.$setValidity("negativeAllowedEnd", false);
                    } else

                    {
                        $scope.validation.endvalue.$setValidity("negativeAllowedEnd", true);
                    }
                }
                if (startRange !== null && startRange !== undefined && endRange !== null && endRange !== undefined)
                {
                    if (parseFloat(startRange) > parseFloat(endRange))
                    {
                        $scope.validation.startvalue.$setValidity("startRangeError", false);
                        $scope.validation.endvalue.$setValidity("endRangeError", false);
                    } else
                    {
                        $scope.validation.startvalue.$setValidity("startRangeError", true);
                        $scope.validation.endvalue.$setValidity("endRangeError", true);
                    }
                }

            };
            // For SubEntity

            // Method to check whether range should accept negative values or not
            $scope.checkValidRangeSubEntity = function (negativeAllowed, startRange, endRange)
            {
                if (negativeAllowed !== null && negativeAllowed !== undefined)
                {

                    $scope.negativeAllowedSubEntity = negativeAllowed;
                    if (startRange !== null && startRange !== undefined)
                    {


                        if ($scope.negativeAllowedSubEntity === false && parseFloat(startRange) < 0)
                        {
                            if ($scope.subentityform.startvalue !== undefined) {
                                $scope.subentityform.startvalue.$setValidity("negativeAllowedStartSub", false);
                            }
                        } else
                        {
                            if ($scope.subentityform.startvalue !== undefined) {
                                $scope.subentityform.startvalue.$setValidity("negativeAllowedStartSub", true);
                            }
                        }

                    }
                    if (endRange !== null && endRange !== undefined)
                    {
                        if ($scope.negativeAllowedSubEntity === false && parseFloat(endRange) < 0)
                        {
                            if ($scope.subentityform.endvalue !== undefined) {
                                $scope.subentityform.endvalue.$setValidity("negativeAllowedEndSub", false);
                            }
                        } else
                        {
                            if ($scope.subentityform.endvalue !== undefined) {
                                $scope.subentityform.endvalue.$setValidity("negativeAllowedEndSub", true);
                            }
                        }

                    }

                } else
                {
                    $scope.negativeAllowedSubEntity = false;
                }

            };
            $scope.checkRangeIsValidForSubEntity = function (startRange, endRange)
            {
                if (startRange !== null && startRange !== undefined)
                {
                    if (startRange === '-0')
                    {
                        $scope.subentityform.startvalue.$setValidity("negativeAllowedStartSub", false);
                    }
                    else if ($scope.negativeAllowedSubEntity === false && parseFloat(startRange) < 0)
                    {
                        $scope.subentityform.startvalue.$setValidity("negativeAllowedStartSub", false);
                    } else
                    {
                        $scope.subentityform.startvalue.$setValidity("negativeAllowedStartSub", true);
                    }
                }
                if (endRange !== null && endRange !== undefined)
                {
                    if (endRange === '-0')
                    {
                        $scope.subentityform.endvalue.$setValidity("negativeAllowedEndSub", false);
                    }
                    else if ($scope.negativeAllowedSubEntity === false && parseFloat(endRange) < 0)
                    {
                        $scope.subentityform.endvalue.$setValidity("negativeAllowedEndSub", false);
                    }
                    else
                    {
                        $scope.subentityform.endvalue.$setValidity("negativeAllowedEndSub", true);
                    }
                }
                if (startRange !== null && startRange !== undefined && endRange !== null && endRange !== undefined)
                {
//                    console.log("sr" + parseDouble(startRange) + "er..." + parseDouble(endRange))
                    if (parseFloat(startRange) > parseFloat(endRange))
                    {
                        $scope.subentityform.startvalue.$setValidity("startRangeErrorSub", false);
                        $scope.subentityform.endvalue.$setValidity("endRangeErrorSub", false);
                    } else
                    {
                        $scope.subentityform.startvalue.$setValidity("startRangeErrorSub", true);
                        $scope.subentityform.endvalue.$setValidity("endRangeErrorSub", true);
                    }
                }

            };
            $scope.changeIsPrivate = function (isPrivate)
            {
                if (isPrivate !== null && isPrivate !== undefined && $scope.privatevalue !== null && $scope.privatevalue !== undefined)
                {
                    // If server private value and value coming from page are different,then only open popup
                    if (isPrivate.toString() !== $scope.privatevalue.toString())
                    {
                        $('#messageModalFormPrivate').modal('show');
                    }
                }
            };
            $scope.okPrivate = function ()
            {
                $('#messageModalFormPrivate').modal('hide');
                $rootScope.removeModalOpenCssAfterModalHide();
                $('.modal-backdrop').remove();
            };
            $scope.cancelPrivate = function ()
            {

                $scope.customField.isPrivate = angular.copy($scope.privatevalue);
                $('#messageModalFormPrivate').modal('hide');
                $rootScope.removeModalOpenCssAfterModalHide();
                $('.modal-backdrop').remove();
            };
            $scope.showComboForEmployee = function (defaultValue)
            {
                if (defaultValue instanceof Array)
                {
                } else
                {
                    if (defaultValue.length === 1 && defaultValue.toString() === "1")
                    {
                        $scope.showComboForDepOrDesg = true;
                    }
                    else
                    {
                        $scope.customFieldValidation.selectedParameter = "";
                        $scope.customFieldValidation.deptList = "";
                        $scope.customFieldValidation.desgList = "";
                        $scope.showComboForDepOrDesg = false;
                        $scope.showDeptList = false;
                        $scope.showDesgList = false;
                    }
                }

            };
            $scope.setDeptOrDesgList = function (selectedParam)
            {
                if (selectedParam === 1)
                {

                    CustomFieldService.retrieveDept(function (response)
                    {
                        $scope.departmentListDropdown = angular.copy(response)
                        $scope.showDeptList = true;
                        $scope.showDesgList = false;
                        $scope.customFieldValidation.desgList = "";
                    })
                }
                else
                if (selectedParam === 2)
                {
                    CustomFieldService.retrieveDesg(function (res)
                    {
                        $scope.designationListDropdown = angular.copy(res);
                        $scope.showDesgList = true;
                        $scope.showDeptList = false;
                        $scope.customFieldValidation.deptList = "";
                    }
                    )

                }
                else
                {
                    $scope.showDesgList = false;
                    $scope.showDeptList = false;
                    $scope.customFieldValidation.desgList = "";
                    $scope.customFieldValidation.deptList = "";
                }

            };
            $scope.autoCompleteDept = {
                'allowClear': true,
                'data': [],
                'placeholder': "Select",
                'multiple': true,
                initSelection: function (element, callback) {
                    if ($scope.editCustomField) {
                        if ($scope.editCustomField) {
//                        var tempData = [];
                            var data = [];
                            var selectedArr = [];
                            if ($scope.customFieldValidation.deptList instanceof Array) {
                            } else {
                                if ($scope.customFieldValidation.deptList !== null && $scope.customFieldValidation.deptList !== undefined)
                                {
                                    var tempData = angular.copy($scope.customFieldValidation.deptList);
                                    selectedArr = $scope.customFieldValidation.deptList.toString().split(",");
                                    angular.forEach($scope.departmentListDropdown, function (dept)
                                    {

                                        for (var j in selectedArr)
                                        {
                                            if (dept.id.toString() === selectedArr[j].toString()) {
//                                        tempData.push(desg.id);
                                                data.push({id: dept.id, text: dept.displayName});
                                            }
                                        }


                                    });

                                    callback(data);

                                    $scope.customFieldValidation.deptList = angular.copy(tempData.toString());
                                }
                            }
                        }
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
                    if ($scope.departmentListDropdown.length !== 0) {

                        angular.forEach($scope.departmentListDropdown, function (item) {
                            $scope.names.push({
                                id: item.id,
                                text: item.displayName
                            });
                        });
                    }
                    query.callback({
                        results: $scope.names
                    });


                }
            };
            $scope.autoCompleteDesg = {
                'allowClear': true,
                'data': [],
                'placeholder': "Select",
                'multiple': true,
                initSelection: function (element, callback) {
                    if ($scope.editCustomField) {
//                        var tempData = [];
                        var data = [];
                        var selectedArr = [];
                        if ($scope.customFieldValidation.desgList instanceof Array) {
                        } else {
                            if ($scope.customFieldValidation.desgList !== null && $scope.customFieldValidation.desgList !== undefined) {
                                var tempData = angular.copy($scope.customFieldValidation.desgList);
                                selectedArr = $scope.customFieldValidation.desgList.toString().split(",");
                                angular.forEach($scope.designationListDropdown, function (desg)
                                {

                                    for (var j in selectedArr)
                                    {
                                        if (desg.id.toString() === selectedArr[j].toString()) {
//                                        tempData.push(desg.id);
                                            data.push({id: desg.id, text: desg.displayName});
                                        }
                                    }


                                });

                                callback(data);

                                $scope.customFieldValidation.desgList = angular.copy(tempData.toString());
                            }
                        }
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
                    if ($scope.designationListDropdown.length !== 0) {

                        angular.forEach($scope.designationListDropdown, function (item) {
                            $scope.names.push({
                                id: item.id,
                                text: item.displayName
                            });
                        });
                    }
                    query.callback({
                        results: $scope.names
                    });


                }
            };

            $scope.autoCompleteUniqueFor = {
                'allowClear': true,
                'data': [],
                'placeholder': "Select",
                'multiple': true,
                initSelection: function (element, callback) {
                    if ($scope.editCustomField) {
                        var tempData = angular.copy($scope.customFieldValidation.uniqueForFields);
                        var data = [];
                        var uniArr = [];
                        uniArr = $scope.customFieldValidation.uniqueForFields.split(",");
                        var success = function (result)
                        {
                            var fieldListForUniquenessEdit = JSON.parse(angular.toJson(result));
                            for (var key in fieldListForUniquenessEdit)
                            {

                                for (var k in uniArr)
                                {
                                    if (key === uniArr[k])
                                    {
                                        data.push({id: key, text: fieldListForUniquenessEdit[key]});
                                    }
                                }
                            }
                            callback(data);
                            $scope.customFieldValidation.uniqueForFields = angular.copy(tempData.toString());
                        }
                        var failure = function ()
                        {

                        }
                        CustomFieldService.retrieveFieldsForUniqueness($scope.customFieldInfo.featureId, success, failure);
                    }
                },
                formatResult: function (item) {
                    return item.text;
                },
                formatSelection: function (item) {
                    return item.text;
                },
                query: function (query) {

                    var success = function (res)
                    {
                        var fieldListForUniqueness = JSON.parse(angular.toJson(res));
                        var selected = query.term;
                        console.log("res...." + JSON.stringify(res))
                        $scope.names = [];
//                        if (res.length !== 0) {
                        for (var key in fieldListForUniqueness)
                        {
                            console.log("key..." + key + "val..." + res[fieldListForUniqueness])
                            $scope.names.push({
                                id: key,
                                text: fieldListForUniqueness[key]
                            });
                        }

//                        }
                        query.callback({
                            results: $scope.names
                        });
                    }
                    var failure = function ()
                    {

                    }

                    CustomFieldService.retrieveFieldsForUniqueness($scope.customFieldInfo.featureId, success, failure);



                }
            };
            $scope.autoCompleteDateFor = {
                'allowClear': true,
                'data': [],
                'placeholder': "Select",
                'multiple': true,
                initSelection: function (element, callback) {
                    if ($scope.editCustomField) {
                        var tempData = angular.copy($scope.customFieldValidation.todayDate);
                        console.log("temp data" + tempData)
                        var data = [];
                        var uniArr = [];
                        uniArr = $scope.customFieldValidation.todayDate.split(",");
                        var success = function (result)
                        {
                            console.log("init result" + JSON.stringify(result))
                            $scope.names = [];
//                        if (res.length !== 0) {
                            for (var key in result)
                            {
                                if (key.toString() === $scope.customFieldValidation.todayDate.toString())
                                {
                                    data.push({
                                        id: key,
                                        text: result[key]
                                    });
                                }
                            }
                            callback(data);
                            $scope.customFieldValidation.todayDate = angular.copy(tempData.toString());
                        }
                        var failure = function ()
                        {

                        }
                        var mapForDate = new Object();
                        mapForDate["search"] = "";
                        mapForDate["featureId"] = $scope.customFieldInfo.featureId;
                        console.log("map for date" + JSON.stringify(mapForDate))
                        CustomFieldService.retrieveFieldsForDate(mapForDate, success, failure);
                    }
                },
                formatResult: function (item) {
                    return item.text;
                },
                formatSelection: function (item) {
                    return item.text;
                },
                query: function (query) {
                    var search = query.term;
                    var success = function (res)
                    {
                        console.log("result....." + JSON.stringify(res))
                        var fieldListForDate = JSON.parse(angular.toJson(res));
                        var selected = query.term;
                        console.log("res...." + JSON.stringify(res))
                        $scope.names = [];
//                        if (res.length !== 0) {
                        for (var key in fieldListForDate)
                        {
                            $scope.names.push({
                                id: key,
                                text: fieldListForDate[key]
                            });
                        }

//                        }
                        query.callback({
                            results: $scope.names
                        });
                    };
                    var failure = function ()
                    {

                    };
                    var mapForDate = new Object();
                    mapForDate["search"] = search.trim();
                    mapForDate["featureId"] = $scope.customFieldInfo.featureId;
                    console.log("map for date" + JSON.stringify(mapForDate))
                    CustomFieldService.retrieveFieldsForDate(mapForDate, success, failure);



                }
            };

            // Methods added By Shifa For Date Component
            $scope.dateoptions = [];
            $scope.selectizeOptionsForDateField = {
                plugins: ['drag_drop'],
                persist: false,
                preload: true,
                enableDuplicate: true,
                hideSelected: false,
                valueField: 'id',
                labelField: 'text',
                searchField: 'text',
                delimiter: '|',
                dateoptions: [],
                dateitems: [],
                initData: true,
                maxItems: 1,
                render: {
                    option: function (item, escape) {
                        return '<div>' + escape(item.text) + '</div>';
                    }

                },
                create: function (input, callback) {
                    $scope.dateoptions.push({id: input, text: input});
                    return {"id": input, "text": input};
                },
                load: function (query, callback) {

                    var newUrl = $rootScope.appendAuthToken($rootScope.apipath + 'customfield/searchCustomFieldsForDate?search=' + (query) + '&featureId=' + ($scope.customFieldInfo.featureId));
                    if (!query.length)
                        return callback();
                    $.ajax({
                        url: newUrl,
                        type: 'GET',
                        error: function () {
                            callback();
                        },
                        success: function (res) {
                            $scope.dateoptions = [];
                            for (var keys in res)
                            {
                                $scope.dateoptions.push({id: keys, text: res[keys]});
                            }

                            callback($scope.dateoptions.slice(0, 200));
                        }
                    });
                }

            };


        }]);
    hkg.register.filter('customFieldTypeFilter', function () {
        return  function (items, searchText) {
            if (angular.isDefined(searchText) && searchText === false) {
                var filtered = [];
                for (var i = 0; i < items.length; i++) {
                    var item = items[i];
                    if (item.id !== 'SubEntity' && item.id !== 'Pointer' && item.id !== 'Formula')
                    {
                        filtered.push(item);
                    }
                }
                return filtered;
            } else {
                return items;
            }
        };
    });
});
