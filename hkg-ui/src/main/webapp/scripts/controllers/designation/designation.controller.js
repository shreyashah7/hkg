/* 
 * Author: Akta Kalariya
 * Controller file for manage designation feature
 */
define(['hkg', 'designationService', 'departmentService', 'leaveWorkflowService', 'activityFlowService', 'treeviewMultiselect.directive'], function(hkg) {
    hkg.register.controller('DesignationCtrl', ["$rootScope", "$scope", "Designation", "DepartmentService", "LeaveWorkflow", 'ActivityFlowService', "$filter", function($rootScope, $scope, Designation, DepartmentService, LeaveWorkflow, ActivityFlowService, $filter) {
            $rootScope.maskLoading();
            $scope.searchRecords = [];
            $rootScope.mainMenu = "manageLink";
            $rootScope.childMenu = "manageDesignation";
            $rootScope.activateMenu();
            $scope.validations = [];
            $scope.combinedFieldListForSearch = [];
            $scope.combinedFieldListForParent = [];
            $scope.invoiceList = [];
            $scope.lotList = [];
            $scope.parcelList = [];
            $scope.packetList = [];
            $scope.coatedRoughList = [];
            $scope.issueList = [];
            $scope.planList = [];
            $scope.diamondList = [];
            $scope.fieldList = [];
            $scope.clearSelectedSearchFields = function() {
                if ($scope.combinedFieldListForSearch !== null && $scope.combinedFieldListForSearch !== undefined && $scope.combinedFieldListForSearch.length > 0) {
                    angular.forEach($scope.combinedFieldListForSearch, function(item) {
                        item.ticked = false;
                    });
                }
            };
            $scope.clearSelectedParentFields = function() {
                if ($scope.combinedFieldListForParent !== null && $scope.combinedFieldListForParent !== undefined && $scope.combinedFieldListForParent.length > 0) {
                    angular.forEach($scope.combinedFieldListForParent, function(item) {
                        item.ticked = false;
                    });
                }
            };
            $scope.selectedFieldsForSearch = [];
            $scope.clearSelectedSearchFields();
            $scope.clearSelectedParentFields();
            $scope.selectedFieldsForParent = [];
            $scope.defaultDepartmentIds = [];
            $scope.featureFieldMap = {};
            $scope.configureGoalSheet = {};
            $scope.multiselecttreeGoalSheet = {};
            $scope.selectedDepValuesGoalSheet = [];
            $scope.entity = "DESIGNATION.";
            $scope.isEditing = false;
            $scope.visibleFields = {'invoice_add': [{id: 'Invoice', name: 'Invoice'}],
                'invoice_edit': [{id: 'Invoice', name: 'Invoice'}],
                'invoice_add_edit': [{id: 'Invoice', name: 'Invoice'}],
                'parcel_add': [{id: 'Parcel', name: 'Parcel'}],
                'parcel_edit': [{id: 'Parcel', name: 'Parcel'}],
                'lot_add': [{id: 'Lot', name: 'Lot'}],
                'lot_edit': [{id: 'Lot', name: 'Lot'}],
                'stock_sell': [{id: 'Sell', name: 'Sell'}],
                'stock_transfer': [{id: 'Transfer', name: 'Transfer'}],
                'stock_merge': [{id: 'Lot', name: 'Lot'}, {id: 'Packet', name: 'Packet'}],
                'stock_split': [{id: 'Lot', name: 'Lot'}, {id: 'Packet', name: 'Packet'}],
                'packet_add': [{id: 'Packet', name: 'Packet'}],
                'packet_edit': [{id: 'Packet', name: 'Packet'}],
                'allot': [{id: 'Lot', name: 'Lot'}, {id: 'Packet', name: 'Packet'}],
                'finalize': [{id: 'Plan', name: 'Plan'}],
                'generate_barcode': [{id: 'Lot', name: 'Lot'}, {id: 'Packet', name: 'Packet'}, {id: 'Plan', name: 'Plan'}, {id: 'Issue', name: 'Issue'}],
                'generate_slip': [{id: 'Lot', name: 'Lot'}, {id: 'Packet', name: 'Packet'}],
                'issue_receive': [{id: 'Lot', name: 'Lot'}, {id: 'Packet', name: 'Packet'}, {id: 'Issue', name: 'Issue'}],
                'write_service': [{id: 'Plan', name: 'Plan'}],
                'print_static': [{id: 'Lot', name: 'Lot'}, {id: 'Packet', name: 'Packet'}],
                'print_dynamic': [{id: 'Lot', name: 'Lot'}, {id: 'Packet', name: 'Packet'}],
                'status_change': [{id: 'Lot', name: 'Lot'}, {id: 'Packet', name: 'Packet'}],
                'parcel_merge': [{id: 'Parcel', name: 'Parcel'}]};
            $scope.$on('$viewContentLoaded', function() {
                $scope.retrieveAccessRightsForDesignation();
            });
            $scope.initAddDesignationForm = function(designationForm) {
                $scope.displaySearchedDesignation = 'view';
                $scope.designationNameForRemove = '';
                $scope.designationForm = designationForm;
            };
            $scope.retrieveAccessRightsForDesignation = function()
            {
                $scope.isAccessCreate = true;
                if (!$rootScope.canAccess('designationAdd'))
                {
                    $scope.isAccessCreate = false;
                }
                $scope.isAccessEdit = true;
                if (!$rootScope.canAccess('designationEdit'))
                {
                    $scope.isAccessEdit = false;
                }
            }
            $scope.retrieveAllDesignations = function() {
                $('#scrollable-dropdown-menu.typeahead').typeahead('val', '');
                $rootScope.maskLoading();

                DepartmentService.retrieveDepartment(function(data) {
                    $scope.departmentList = [];
                    $scope.departmentIdNameMap = {};
                    $scope.designationWithDepartmentList = [];
                    if (data != null && angular.isDefined(data) && data.length > 0) {
                        $scope.convertTreeviewToNormalList(data);
                    }
                    Designation.retrieveDesignations(function(data) {
                        $scope.designationList = [];
                        $scope.displaySearchedDesignation = 'view';
                        if (data != null && angular.isDefined(data) && data.length > 0) {
                            angular.forEach(data, function(item) {
                                item.displayName = $rootScope.translateValue("DESIG_NM." + item.displayName);
                                $scope.designationList.push(item);
                                $scope.designationWithDepartmentList.push({id: item.id, text: item.displayName + ($scope.departmentIdNameMap[item.associatedDepartment] == undefined ? '' : ' (' + $scope.departmentIdNameMap[item.associatedDepartment] + ')')});
                            });
                        }
//                    $scope.designationList = data;
                        if (!$scope.designationList || $scope.designationList == null) {
                            $scope.designationList = [];
                        }
                        $scope.designationForm.$dirty = false;
                        $rootScope.unMaskLoading();
                        $scope.retrieveAllSysFeatures();
                    }, function() {
                        $rootScope.unMaskLoading();
                        $scope.retrieveAllSysFeatures();
                    });

                }, function() {
                    $rootScope.unMaskLoading();
                    var msg = "Failed to retrieve department";
                    var type = $rootScope.error;
                    $rootScope.addMessage(msg, type);

                });
            };
            $scope.cancelLink = function() {
                $scope.clearDesignationForm();
                $('#designation').select2('val', []);
                $('#designation1').select2('val', []);
                $('#service').select2('val', []);
                $('#service1').select2('val', []);
                $scope.resetSelection();
            };
            $scope.resetSelection = function() {
                if ($scope.selectedDesignation != undefined) {
                    $scope.selectedDesignation.selected = undefined;
                }
                if ($scope.selectedDesignation != undefined && $scope.selectedDesignation.currentNode != undefined) {
                    $scope.selectedDesignation.currentNode.selected = undefined;
                    $scope.selectedDesignation.currentNode = undefined;
                }
                $scope.configureGoal = {};
                $scope.configureGoalSheet = {};
                $scope.selectedInStringIds = [];
                $scope.selectedInStringIdsGoalSheet = [];
                $scope.selectedInStringGoalSheet = [];
                $scope.defaultDepartmentIds = [];
                $scope.defaultDepartmentIdsGoalSheet = [];
                $scope.listForTable = [];
                $scope.listOfPermissionGoalSheet = [];
//                $scope.roleForm.$setPristine();
//                $scope.goalSheetForm.$setPristine();
                $('#designation').select2('val', []);
                $('#designation1').select2('val', []);
                $('#service').select2('val', []);
                $('#service1').select2('val', []);
            };
            $scope.getSearchedDesignationRecords = function(list) {
                $scope.searchRecords = [];
                var enteredText = $('#scrollable-dropdown-menu.typeahead').typeahead('val');
                if (enteredText.length > 0) {
                    if (enteredText.length < 3) {
                        $scope.searchRecords = [];
                    } else {
                        if (list != null && angular.isDefined(list) && list.length > 0) {
                            angular.forEach(list, function(item) {
                                item.displayName = $rootScope.translateValue("DESIG_NM." + item.displayName);
                                $scope.searchRecords.push(item);
                            });
                        }
//                        $scope.searchRecords = angular.copy(list);
                    }
                    $scope.resetSelection();
                    $scope.displaySearchedDesignation = 'search';
                }
            };

            //Convert tree list to normal list
            $scope.convertTreeviewToNormalList = function convertTreeviewToNormalList(treeData) {
                angular.forEach(treeData, function(department) {
                    if (department.children === null || department.children === undefined) {
                        department.displayName = $rootScope.translateValue("DPT_NM." + angular.copy(department.displayName));
                        $scope.departmentList.push(department);
                        $scope.departmentIdNameMap[department.id] = department.displayName;
                    } else {
                        var temp = angular.copy(department);
                        temp.children = null;
                        temp.displayName = $rootScope.translateValue("DPT_NM." + angular.copy(temp.displayName));
                        $scope.departmentList.push(temp);
                        $scope.departmentIdNameMap[temp.id] = temp.displayName;
                        convertTreeviewToNormalList(department.children);
                    }
                });
            };
            
            $scope.checkIfParentAvailable = function(parentId, field){
                var dataToSend = {
                    designationId : $scope.editingDesigId,
                    parentId : parentId
                };
                Designation.checkCircularDependency(dataToSend, function(res){
                    if(res.data === true){
                        field.$setValidity('circular', false);
                    }else{
                        field.$setValidity('circular', true);
                    }
                }, function(err){
                });
            };
            $scope.retrieveAllSysFeatures = function() {
                $rootScope.maskLoading();
                Designation.retrieveSystemFeatures(function(data) {
                    $rootScope.unMaskLoading();
                    $scope.systemFeaturesList = data;
                    if (!$scope.systemFeaturesList || $scope.systemFeaturesList == null) {
                        $scope.systemFeaturesList = [];
                    } else {
                        $scope.tempSystemFeatureList = [];
                        $scope.diamondSystemFeatureList = [];
                        $scope.reportSystemFeatureList = [];
                        angular.forEach($scope.systemFeaturesList, function(item) {
                            if (item.type === "DMI" || item.type === "DEI") {
                                //Hide dynamic services.
                                if (item.type !== "DEI") {
                                    $scope.diamondSystemFeatureList.push(item);
                                }
                            } else if (item.type === "RMI") {
                                $scope.reportSystemFeatureList.push(item);
                            } else {
                                $scope.tempSystemFeatureList.push(item);
                            }
                        });
                        $scope.systemFeaturesList = [];
                        $scope.systemFeaturesList = angular.copy($scope.tempSystemFeatureList);
                        $scope.retrieveAssociatedGoalPermission();
                        $scope.retrieveAssociatedGoalSheetPermission();
                        $scope.retrieveReportGroupNames();
                    }
                }, function() {
                    $rootScope.unMaskLoading();
                });
            };

            $scope.retrieveReportGroupNames = function() {
                $scope.groupIds = [];
                if ($scope.reportSystemFeatureList !== undefined) {
                    for (var i = 0; i < $scope.reportSystemFeatureList.length; i++) {
                        if ($scope.reportSystemFeatureList[i].description !== null && $scope.reportSystemFeatureList[i].description !== '' && $scope.reportSystemFeatureList[i].type === "RMI") {
                            var rowInvalid = false;
                            angular.forEach($scope.groupIds, function(item) {
                                if ($scope.reportSystemFeatureList[i].description === item) {
                                    rowInvalid = true;
                                }
                            });
                            if (!rowInvalid) {
                                $scope.groupIds.push($scope.reportSystemFeatureList[i].description);
                            }
                        }
                    }
                }
                Designation.retrieveReportGroupNames($scope.groupIds, function(res) {
                    $scope.groupFeatureMap = [];
                    $scope.defaultFeatures = [];
                    $scope.reportGroupIdNameMap = res.data;
                    if (!!res.data) {
                        for (var key in $scope.reportGroupIdNameMap) {
                            $scope.groupFeatures = [];
                            for (var i = 0; i < $scope.reportSystemFeatureList.length; i++) {
                                var rowInvalidStatus = false;
                                if ($scope.reportSystemFeatureList[i].description !== null && $scope.reportSystemFeatureList[i].type === "RMI") {
                                    if ($scope.reportSystemFeatureList[i].description === key) {
                                        $scope.groupFeatures.push($scope.reportSystemFeatureList[i]);
                                    }
                                } else {
                                    angular.forEach($scope.defaultFeatures, function(item) {
                                        if (item.id === $scope.reportSystemFeatureList[i].id) {
                                            rowInvalidStatus = true;
                                        }
                                    });
                                    if (!rowInvalidStatus) {
                                        $scope.defaultFeatures.push($scope.reportSystemFeatureList[i]);
                                    }
                                }
                            }
                            $scope.groupFeatureMap.push({groupName: $scope.reportGroupIdNameMap[key], features: $scope.groupFeatures});
                        }
                    } else {
                        for (var i = 0; i < $scope.reportSystemFeatureList.length; i++) {
                            var rowInvalidStatus = false;
                            if ($scope.reportSystemFeatureList[i].description !== null && $scope.reportSystemFeatureList[i].type === "RMI") {

                            } else {
                                angular.forEach($scope.defaultFeatures, function(item) {
                                    if (item.id === $scope.reportSystemFeatureList[i].id) {
                                        rowInvalidStatus = true;
                                    }
                                });
                                if (!rowInvalidStatus) {
                                    $scope.defaultFeatures.push($scope.reportSystemFeatureList[i]);
                                }
                            }
                        }
                    }

                    if ($scope.defaultFeatures !== undefined && $scope.defaultFeatures !== null && $scope.defaultFeatures.length > 0) {
                        $scope.groupFeatureMap.push({groupName: "Default", features: $scope.defaultFeatures});
                    }
//                    console.log("groupFeatureMap ::: " + JSON.stringify($scope.groupFeatureMap));
                }, function() {
                });
            };
            $scope.createDesignation = function() {
                $scope.submitted = true;
                if (!$scope.designationForm.$invalid) {
                    //No need to show missing configuration warning
//                    var count = 0;
//                    var diamondLength = $scope.diamondSystemFeatureList.length;
//                    for (var i = 0; i < diamondLength; i++) {
//                        if ($scope.diamondSystemFeatureList[i].checked && !$scope.diamondSystemFeatureList[i].configure && $scope.diamondSystemFeatureList[i].type !== "DEI") {
//                            count++;
//                        }
//                    }
//                    if (count > 0) {
//                        $("#notConfiguredPopup").modal('show');
//                    } else {
                    $scope.createBothDesignation();
//                    }
                }
            };
            $scope.createBothDesignation = function() {
                var diamondLength = $scope.diamondSystemFeatureList.length;
                var sysFeatureListtoSend = [];
                var length = $scope.systemFeaturesList.length;
                for (var i = 0; i < length; i++) {
                    if (($scope.systemFeaturesList[i].iteamAttributesList == null || $scope.systemFeaturesList[i].iteamAttributesList.length == 0) && $scope.systemFeaturesList[i].checked) {
                        sysFeatureListtoSend.push($scope.systemFeaturesList[i].id);
                    }
                    var iaLength = $scope.systemFeaturesList[i].iteamAttributesList.length;
                    for (var j = 0; j < iaLength; j++) {
                        if ($scope.systemFeaturesList[i].iteamAttributesList[j].checked) {
                            sysFeatureListtoSend.push($scope.systemFeaturesList[i].iteamAttributesList[j].id);
                        }
                    }
                }
                for (var i = 0; i < diamondLength; i++) {
                    if ($scope.diamondSystemFeatureList[i].checked) {
                        sysFeatureListtoSend.push($scope.diamondSystemFeatureList[i].id);
                    }
                }
                var GoalPermissions = [];
                var GoalPermission = {
                };
                var GoalPermissionForDesignation = {
                };
                var GoalPermissionForDepartment = {
                };
                if (!!$scope.selectedDesignation.currentNode) {
                    GoalPermission.designation = $scope.selectedDesignation.currentNode.id;
                    GoalPermissionForDesignation.designation = $scope.selectedDesignation.currentNode.id;
                    GoalPermissionForDepartment.designation = $scope.selectedDesignation.currentNode.id;
                }
                GoalPermission.referenceType = "S";
                if (!!$scope.configureGoal.service)
                    GoalPermission.referenceInstance = $scope.configureGoal.service.toString();

                GoalPermissionForDesignation.referenceType = "R";
                if (!!$scope.configureGoal.designation)
                    GoalPermissionForDesignation.referenceInstance = $scope.configureGoal.designation.toString();

                GoalPermissionForDepartment.referenceType = "D";
                if (!!$scope.selectedInStringIds)
                    GoalPermissionForDepartment.referenceInstance = $scope.selectedInStringIds.toString();

                GoalPermissions.push(GoalPermission);
                GoalPermissions.push(GoalPermissionForDesignation);
                GoalPermissions.push(GoalPermissionForDepartment);
                var GoalSheetPermissions = [];
                var GoalSheetPermission = {
                };
                var GoalSheetPermissionForDesignation = {
                };
                var GoalSheetPermissionForDepartment = {
                };
                if (!!$scope.selectedDesignation.currentNode) {
                    GoalSheetPermission.designation = $scope.selectedDesignation.currentNode.id;
                    GoalSheetPermissionForDesignation.designation = $scope.selectedDesignation.currentNode.id;
                    GoalSheetPermissionForDepartment.designation = $scope.selectedDesignation.currentNode.id;
                }
                GoalSheetPermission.referenceType = "S";
                if (!!$scope.configureGoalSheet.service)
                    GoalSheetPermission.referenceInstance = $scope.configureGoalSheet.service.toString();

                GoalSheetPermissionForDesignation.referenceType = "R";
                if (!!$scope.configureGoalSheet.designation)
                    GoalSheetPermissionForDesignation.referenceInstance = $scope.configureGoalSheet.designation.toString();

                GoalSheetPermissionForDepartment.referenceType = "D";
                if (!!$scope.selectedInStringIdsGoalSheet)
                    GoalSheetPermissionForDepartment.referenceInstance = $scope.selectedInStringIdsGoalSheet.toString();

                GoalSheetPermissions.push(GoalSheetPermission);
                GoalSheetPermissions.push(GoalSheetPermissionForDesignation);
                GoalSheetPermissions.push(GoalSheetPermissionForDepartment);
                var designation = {displayName: $scope.designationTitle, associatedDepartment: $scope.associatedDepartment, parentDesignation: $scope.parentDesignation, sysFeatureIdList: sysFeatureListtoSend, staticServicesMap: $scope.featureFieldMap, goalPermissions: GoalPermissions, goalSheetPermissions: GoalSheetPermissions};

                $rootScope.maskLoading();
                Designation.create(designation, function(res) {
                    $rootScope.unMaskLoading();
                    if (res.messages) {
                        if (res.messages.length > 0) {
                            if (res.messages[0].responseCode == 1) {
                                $scope.showIsDesigExist = true;
                            }
                        }
                    }
                    else {
                        $scope.clearDesignationForm();
                        $scope.searchDesignation = null;
                    }
                    $scope.retrieveAllDesignations();
                    $scope.submitted = false;
                    $scope.configureGoal = {};
                    $scope.configureGoalSheet = {};
                    $scope.selectedInStringIds = [];
                    $scope.selectedInStringIdsGoalSheet = [];
                    $scope.selectedInString = [];
                    $scope.selectedInStringGoalSheet = [];

//                    uncheckAll($scope.multiselecttree.items);
//                    uncheckAll($scope.multiselecttreeGoalSheet.items);
                    $scope.defaultDepartmentIds = undefined;
                    $scope.defaultDepartmentIdsGoalSheet = undefined;
                    $scope.listForTable = [];
                    $scope.listOfPermissionGoalSheet = [];

                    $('#designation').select2('val', []);
                    $('#designation1').select2('val', []);
                    $('#service').select2('val', []);
                    $('#service1').select2('val', []);
//                    $scope.roleForm.$setPristine();
//                    $scope.goalSheetForm.$setPristine();
                    $('#scrollable-dropdown-menu.typeahead').typeahead('val', '');
                    $scope.cancelConfigurationPopup();
                    $scope.cancelConfirmPopup();

                }, function() {
                    $rootScope.unMaskLoading();
                    $rootScope.addMessage("Designation could not be saved. Try again", 1);
                });
            };
            $scope.retrieveDesignationById = function(designationId, fromSearch) {
                if (!$scope.isAccessEdit) {
                    $rootScope.addMessage("You don't have the right to edit designation", 1);
                } else {
                    Designation.retrieveDesignation({primaryKey: designationId}, function(response) {
                        $rootScope.maskLoading();
                        $scope.designationNameForRemove = response.displayName;
                        $scope.fillDesignationForm(response.displayName, response.associatedDepartment, response.parentDesignation, response.sysFeatureIdList, designationId, fromSearch, response.staticServicesMap);
                        if (response.displayName === 'HK Admin' || response.displayName === 'Franchise Admin') {
                            $scope.isDisabled = true;
                        }
                        else {
                            $scope.isDisabled = false;
                        }

                        $('#scrollable-dropdown-menu.typeahead').typeahead('val', '');
                        $scope.retrieveAssociatedGoalPermission();
                        $scope.retrieveAssociatedGoalSheetPermission();
                        $rootScope.unMaskLoading();
                    });
                }
            };
            $scope.deleteDesignationById = function(designationId) {

                Designation.retrieveUsersCount({primaryKey: designationId}, function(response) {

                    if (response.data > 0) {
                        $rootScope.unMaskLoading();
                        $('#designationActiveUserModal').modal('show');
                        $("#confirmationPopUp").modal('hide');
                        $rootScope.removeModalOpenCssAfterModalHide();

                    }
                    else {
                        $rootScope.maskLoading();
                        Designation.deleteById({primaryKey: designationId}, function() {
                            $scope.retrieveAllDesignations();
                            $scope.hideConfirmationPopup();
                            $scope.clearDesignationForm();
                            $('#designation').select2('val', []);
                            $('#service').select2('val', []);
                            $scope.isEditing = false;
                            $('#scrollable-dropdown-menu.typeahead').typeahead('val', '');
                            $rootScope.unMaskLoading();
                        }, function() {
                            $rootScope.addMessage("Designation could not be removed. Try again.", 1);
                            $rootScope.unMaskLoading();
                        });
                    }
                })
            };
            $scope.fillDesignationForm = function(title, associatedDepartment, parentDesignation, sysFeatureIds, designationId, fromSearch, featureFieldMap) {
                var copydesig = $scope.copyDesignation;
                if ($scope.isEditing) {
                    $scope.clearDesignationForm();
                }
                if (fromSearch) {
                    $scope.searchDesignation = title;
                } else {
                    $scope.searchDesignation = null;
                }
                $scope.designationStatus = "Active";
                if ($scope.isEditing) {
                    $scope.designationTitle = title;
                }
                $scope.associatedDepartment = associatedDepartment;
                $scope.parentDesignation = parentDesignation;
                if ($scope.isCopy) {
//                    $scope.designationTitle = "Copy of " + $scope.designationTitle;
                    $scope.copyDesignation = copydesig;
//                        $scope.designationTitle="";
                }
                if ($scope.isEditing) {
                    if (!$scope.isCopy) {
                        $scope.editingDesigId = designationId;
                    }
                    $scope.editingDesigTtile = title;
                }
                $scope.isDesigNameExist();
                if ($scope.isEditing || $scope.isCopy) {
                    $scope.featureFieldMap = featureFieldMap;
                }

                var length1 = sysFeatureIds.length;
                var length2 = $scope.systemFeaturesList.length;
                var length3 = $scope.diamondSystemFeatureList.length;
                var length4 = $scope.reportSystemFeatureList.length;
                var iaLength;
                for (var i = 0; i < length1; i++) {
                    for (var k = 0; k < length2; k++) {
                        if ($scope.systemFeaturesList[k].id == sysFeatureIds[i]) {
                            $scope.systemFeaturesList[k].checked = true;
                            $scope.systemFeaturesList[k].configure = true;
                        }
                        iaLength = $scope.systemFeaturesList[k].iteamAttributesList.length;
                        for (var j = 0; j < iaLength; j++) {
                            if ($scope.systemFeaturesList[k].iteamAttributesList[j].id == sysFeatureIds[i]) {
                                $scope.systemFeaturesList[k].iteamAttributesList[j].checked = true;
                                $scope.systemFeaturesList[k].iteamAttributesList[j].configure = true;
                            }
                        }
                        $scope.checkUncheckMI($scope.systemFeaturesList[k]);
                    }
                    for (var k = 0; k < length3; k++) {
                        if ($scope.diamondSystemFeatureList[k].id == sysFeatureIds[i]) {
                            $scope.diamondSystemFeatureList[k].checked = true;
                            $scope.diamondSystemFeatureList[k].configure = true;
                        }
                        iaLength = $scope.diamondSystemFeatureList[k].iteamAttributesList.length;
                        for (var j = 0; j < iaLength; j++) {
                            if ($scope.diamondSystemFeatureList[k].iteamAttributesList[j].id == sysFeatureIds[i]) {
                                $scope.diamondSystemFeatureList[k].iteamAttributesList[j].checked = true;
                            }
                        }
                        $scope.checkUncheckMI($scope.systemFeaturesList[k]);
                    }
                    for (var k = 0; k < length4; k++) {
                        if ($scope.reportSystemFeatureList[k].id == sysFeatureIds[i]) {
                            $scope.reportSystemFeatureList[k].checked = true;
                        }
                        iaLength = $scope.reportSystemFeatureList[k].iteamAttributesList.length;
                        for (var j = 0; j < iaLength; j++) {
                            if ($scope.reportSystemFeatureList[k].iteamAttributesList[j].id == sysFeatureIds[i]) {
                                $scope.reportSystemFeatureList[k].iteamAttributesList[j].checked = true;
                            }
                        }
                    }

                }
            };
            $scope.isDesigNameExist = function() {
                if ($scope.designationTitle !== null && $scope.designationTitle !== undefined && $scope.designationTitle !== "") {
                    var desiglistlength = $scope.designationList.length;
                    for (var i = 0; i < desiglistlength; i++) {
                        if (($scope.designationList[i].displayName).toLowerCase() == ($scope.designationTitle).toLowerCase()) {
                            if ($scope.isEditing) {
                                if ($scope.editingDesigTtile.toLowerCase() == $scope.designationTitle.toLowerCase()) {
                                    $scope.showIsDesigExist = false;
                                }
                                else {
                                    $scope.showIsDesigExist = true;
                                }
                            }
                            else {
                                $scope.showIsDesigExist = true;
                            }
                            break;
                        }
                        else {
                            $scope.showIsDesigExist = false;
                        }
                    }
                }

            };
            $scope.editFromSearch = function(desigval) {
                $scope.isShowEditForm(desigval, true, false, false);
            }
            $scope.isShowEditForm = function(desigval, showEdit, iscopy, fromSearch) {
                $scope.displaySearchedDesignation = 'view';
                if (desigval !== null && desigval !== "" && desigval !== undefined) {
                    $scope.isEditing = showEdit;
                    $scope.isCopy = iscopy;
                    $scope.retrieveDesignationById(desigval, fromSearch);
                }
                else if (desigval == null || desigval == "" || desigval == undefined && iscopy === true && $scope.editingDesigId !== null) {
                    $scope.retrieveAllSysFeatures();
//                    $scope.retrieveDesignationById($scope.editingDesigId, fromSearch);
                }
//                $scope.retrieveAssociatedGoalPermission();
            };
            $scope.retrieveAssociatedGoalPermission = function() {
                $scope.configureGoal = {};
                $scope.listForTable = [];
                if (!!$scope.selectedDesignation.currentNode) {
                    Designation.retrieveActiveGoalPermission({"designation": $scope.selectedDesignation.currentNode.id}, function(data) {
                        var tempGoalPermissions = data.data;
                        if (tempGoalPermissions !== null) {

                            var vals = [];
                            angular.forEach(tempGoalPermissions, function(item) {
                                if (item.referenceType == "S") {
                                    vals.push(item.referenceInstance);
                                }
                            });
                            $scope.configureGoal.service = [];
                            var a = vals.toString();
                            var b = a.split(',').map(function(item) {
                                return parseInt(item, 10);
                            });
                            $scope.configureGoal.service = b;
                            $('#service').select2('val', b);
                            $scope.configureGoal.designationNames = [];
                            angular.forEach($scope.configureGoal.service, function(item) {
                                angular.forEach($scope.allServices, function(item1) {
                                    if (item == item1.nodeId) {
                                        if ($scope.configureGoal.designationNames.indexOf(item1.designationName) == -1)
                                            $scope.configureGoal.designationNames.push(item1.designationName);
                                        $scope.listForTable.push({id: item1.nodeId, name: item1.associatedServiceName + "(" + item1.activityName + ")", type: "Activity-Service"});
                                    }
                                });
                            });
                            $scope.configureGoal.designationNamesInString = $scope.configureGoal.designationNames.toString();

                            var vals = [];
                            angular.forEach(tempGoalPermissions, function(item) {
                                if (item.referenceType == "R") {
                                    vals.push(item.referenceInstance);
                                }
                            });
                            angular.forEach($scope.designationList, function(designation) {
                                angular.forEach(vals, function(item) {
                                    if (parseInt(item) === designation.id) {
                                        $scope.listForTable.push({id: designation.id, name: designation.displayName, type: "Designation"});
                                    }
                                });
                            });
                            $scope.configureGoal.designation = [];
                            var a = vals.toString();
                            var c = a.split(',').map(function(item) {
                                return parseInt(item, 10);
                            });
                            $scope.configureGoal.designation = c;
                            $('#designation').select2('val', c);

                            var vals = [];
                            $scope.selectedInString = [];
                            angular.forEach(tempGoalPermissions, function(item) {
                                if (item.referenceType == "D") {
                                    vals.push(item.referenceInstance);
                                    $scope.depName = "";
                                    var depName = $scope.getDepName(item.referenceInstance, $scope.multiselecttree.items);
                                    $scope.selectedInString.push(depName);
                                    $scope.listForTable.push({id: item.referenceInstance, name: depName, type: "Department"});
                                }
                            });
                            $scope.defaultDepartmentIds = vals;
                            $scope.selectedInStringIds = vals;
                        }
                    }, function() {
                        console.log("Fail to retrieve...");
                    });
                }
            };
            $scope.retrieveAssociatedGoalSheetPermission = function() {
                $scope.configureGoalSheet = {};
                $scope.listOfPermissionGoalSheet = [];
                if (!!$scope.selectedDesignation.currentNode) {
                    Designation.retrieveActiveGoalSheetPermission({"designation": $scope.selectedDesignation.currentNode.id}, function(data) {
                        var tempGoalPermissions = data.data;
                        if (tempGoalPermissions !== null) {

                            var vals = [];
                            angular.forEach(tempGoalPermissions, function(item) {
                                if (item.referenceType == "S") {
                                    vals.push(item.referenceInstance);
                                }
                            });
                            $scope.configureGoalSheet.service = [];
                            var a = vals.toString();
                            var b = a.split(',').map(function(item) {
                                return parseInt(item, 10);
                            });
                            $scope.configureGoalSheet.service = b;
                            $('#service1').select2('val', b);
                            $scope.configureGoalSheet.designationNames = [];
                            angular.forEach($scope.configureGoalSheet.service, function(item) {
                                angular.forEach($scope.allServices, function(item1) {
                                    if (item == item1.nodeId) {
                                        if ($scope.configureGoalSheet.designationNames.indexOf(item1.designationName) == -1)
                                            $scope.configureGoalSheet.designationNames.push(item1.designationName);
                                        $scope.listOfPermissionGoalSheet.push({id: item1.nodeId, name: item1.associatedServiceName + "(" + item1.activityName + ")", type: "Activity-Service"});
                                    }
                                });
                            });
                            $scope.configureGoalSheet.designationNamesInStringGoalSheet = $scope.configureGoalSheet.designationNames.toString();

                            var vals = [];
                            angular.forEach(tempGoalPermissions, function(item) {
                                if (item.referenceType == "R") {
                                    vals.push(item.referenceInstance);
                                }
                            });
                            angular.forEach($scope.designationList, function(designation) {
                                angular.forEach(vals, function(item) {
                                    if (parseInt(item) === designation.id) {
                                        $scope.listOfPermissionGoalSheet.push({id: designation.id, name: designation.displayName, type: "Designation"});
                                    }
                                });
                            });
                            $scope.configureGoalSheet.designation = [];
                            var a = vals.toString();
                            var c = a.split(',').map(function(item) {
                                return parseInt(item, 10);
                            });
                            $scope.configureGoalSheet.designation = c;
                            $('#designation1').select2('val', c);

                            var vals = [];
                            $scope.selectedInStringGoalSheet = [];
                            angular.forEach(tempGoalPermissions, function(item) {
                                if (item.referenceType == "D") {
                                    vals.push(item.referenceInstance);
                                    $scope.depName = "";
                                    var depName = $scope.getDepName(item.referenceInstance, $scope.multiselecttree.items);
                                    $scope.selectedInStringGoalSheet.push(depName);
                                    $scope.listOfPermissionGoalSheet.push({id: item.referenceInstance, name: depName, type: "Department"});
                                }
                            });
                            $scope.defaultDepartmentIdsGoalSheet = vals;
                            $scope.selectedInStringIdsGoalSheet = vals;
                        }
                    }, function() {
                        console.log("Fail to retrieve...");
                    });
                }
            };
            $scope.addClick = function() {
                $scope.displaySearchedDesignation = 'view';
                $scope.isDisabled = false;
                $scope.submitted = false;
                $scope.isEditing = false;
                $scope.searchDesignation = null;
                $scope.editingDesigId = null
                $scope.clearDesignationForm();
                $('#designation').select2('val', []);
                $('#designation1').select2('val', []);
                $('#service').select2('val', []);
                $('#service1').select2('val', []);
            };
            $scope.clearDesignationForm = function() {
                $scope.isDisabled = false;
                $scope.submitted = false;
                $scope.designationTitle = null;
                $scope.associatedDepartment = undefined;
                $scope.parentDesignation = undefined;
                $scope.copyDesignation = "";
                $scope.selectedFieldsForSearch = [];
                $scope.clearSelectedSearchFields();
                $scope.clearSelectedParentFields();
                $scope.selectedFieldsForParent = [];
                $scope.commonList = [];
                $scope.entitys = [];
                $scope.designationStatus = "";
                $scope.showIsDesigExist = false;
                $('#scrollable-dropdown-menu.typeahead').typeahead('val', '');
                var length = $scope.systemFeaturesList.length;
                for (var i = 0; i < length; i++) {
                    var iaLength = $scope.systemFeaturesList[i].iteamAttributesList.length;
                    $scope.systemFeaturesList[i].checked = false;
                    $scope.systemFeaturesList[i].configure = false;
                    for (var j = 0; j < iaLength; j++) {
                        $scope.systemFeaturesList[i].iteamAttributesList[j].checked = false;
                    }
                }
                var length = $scope.diamondSystemFeatureList.length;
                for (var i = 0; i < length; i++) {
                    var iaLength = $scope.diamondSystemFeatureList[i].iteamAttributesList.length;
                    $scope.diamondSystemFeatureList[i].checked = false;
                    $scope.diamondSystemFeatureList[i].configure = false;
                    for (var j = 0; j < iaLength; j++) {
                        $scope.diamondSystemFeatureList[i].iteamAttributesList[j].checked = false;
                    }
                }
                var length = $scope.reportSystemFeatureList.length;
                for (var i = 0; i < length; i++) {
                    var iaLength = $scope.reportSystemFeatureList[i].iteamAttributesList.length;
                    $scope.reportSystemFeatureList[i].checked = false;
                    for (var j = 0; j < iaLength; j++) {
                        $scope.reportSystemFeatureList[i].iteamAttributesList[j].checked = false;
                    }
                }
                if (!$scope.isEditing) {
                    $scope.searchDesignation = null;
                }
                $scope.designationForm.$setPristine();
                $scope.configureGoal = {};
                $scope.configureGoalSheet = {};
                $scope.selectedInStringIds = [];
                $scope.selectedInStringIdsGoalSheet = [];
                $scope.selectedInStringGoalSheet = [];
                $scope.defaultDepartmentIdsGoalSheet = [];
                $scope.listForTable = [];
                $scope.listOfPermissionGoalSheet = [];
//                $scope.roleForm.$setPristine();
//                $scope.goalSheetForm.$setPristine();
            };
            $scope.updateDesignation = function() {
                var sysFeatureListtoSend = [];
                var length = $scope.systemFeaturesList.length;
                for (var i = 0; i < length; i++) {
                    if ($scope.systemFeaturesList[i].checked) {
                        sysFeatureListtoSend.push($scope.systemFeaturesList[i].id);
                    }
                    var iaLength = $scope.systemFeaturesList[i].iteamAttributesList.length;
                    for (var j = 0; j < iaLength; j++) {
                        if ($scope.systemFeaturesList[i].iteamAttributesList[j].checked) {
                            sysFeatureListtoSend.push($scope.systemFeaturesList[i].iteamAttributesList[j].id);
                        }
                    }
                }

                var length = $scope.diamondSystemFeatureList.length;
                for (var i = 0; i < length; i++) {
                    if ($scope.diamondSystemFeatureList[i].checked) {
                        sysFeatureListtoSend.push($scope.diamondSystemFeatureList[i].id);
                    }
                    var iaLength = $scope.diamondSystemFeatureList[i].iteamAttributesList.length;
                    for (var j = 0; j < iaLength; j++) {
                        if ($scope.diamondSystemFeatureList[i].iteamAttributesList[j].checked) {
                            sysFeatureListtoSend.push($scope.diamondSystemFeatureList[i].iteamAttributesList[j].id);
                        }
                    }
                }

                var length = $scope.reportSystemFeatureList.length;
                for (var i = 0; i < length; i++) {
                    if ($scope.reportSystemFeatureList[i].checked) {
                        sysFeatureListtoSend.push($scope.reportSystemFeatureList[i].id);
                    }
                    var iaLength = $scope.reportSystemFeatureList[i].iteamAttributesList.length;
                    for (var j = 0; j < iaLength; j++) {
                        if ($scope.reportSystemFeatureList[i].iteamAttributesList[j].checked) {
                            sysFeatureListtoSend.push($scope.reportSystemFeatureList[i].iteamAttributesList[j].id);
                        }
                    }
                }
                if (!$scope.designationForm.$invalid) {
                    var designation = {displayName: $scope.designationTitle, associatedDepartment: $scope.associatedDepartment, parentDesignation: $scope.parentDesignation, sysFeatureIdList: sysFeatureListtoSend, staticServicesMap: $scope.featureFieldMap, id: $scope.editingDesigId, goalPermissions: $scope.goalPermissionToConfigure, goalSheetPermissions: $scope.goalSheetPermissionToConfigure};
                    console.log("sending deisgnation///" + JSON.stringify(designation))
                    $rootScope.maskLoading();
                    Designation.update(designation, function(res) {
                        $rootScope.unMaskLoading();
                        if (res.messages) {
                            if (res.messages.length > 0) {
                                if (res.messages[0].responseCode == 1) {
                                    $scope.showIsDesigExist = true;
                                }
                            }
                        }
                        else {
                            $scope.clearDesignationForm();
                            $scope.isEditing = false;
                            $scope.searchDesignation = null;
                            $scope.editingDesigId = null
                        }
                        $scope.submitted = false;
                        $scope.configureGoal = {};
                        $scope.configureGoalSheet = {};

//                        uncheckAll($scope.multiselecttreeGoalSheet.items);
//                        uncheckAll($scope.multiselecttree.items);
                        $scope.selectedInStringIds = [];
                        $scope.selectedInStringIdsGoalSheet = [];
                        $scope.selectedInString = [];
                        $scope.selectedInStringGoalSheet = [];
                        $scope.defaultDepartmentIds = [];
                        $scope.defaultDepartmentIdsGoalSheet = [];

                        $scope.listForTable = [];
                        $scope.listOfPermissionGoalSheet = [];
                        $('#designation').select2('val', []);
                        $('#designation1').select2('val', []);
                        $('#service').select2('val', []);
                        $('#service1').select2('val', []);
//                        $scope.roleForm.$setPristine();
//                        $scope.goalSheetForm.$setPristine();
                        $scope.cancelLink();
                        $('#scrollable-dropdown-menu.typeahead').typeahead('val', '');
                        $("#notConfiguredPopup").modal('hide');
                        $rootScope.removeModalOpenCssAfterModalHide();

                    }, function() {
                        $rootScope.unMaskLoading();
                        $rootScope.addMessage("Designation could not be updated. Try again.", 1);
                    });
                }
            };
            $scope.saveDesignation = function() {
                $scope.submitted = true;
                if ($scope.designationStatus === "Active") {
                    //No need to show missing configuration warning
//                    var count = 0;
//                    var diamondLength = $scope.diamondSystemFeatureList.length;
//                    for (var i = 0; i < diamondLength; i++) {
//                        if ($scope.diamondSystemFeatureList[i].checked && !$scope.diamondSystemFeatureList[i].configure) {
//                            count++;
//                        }
//                    }
//                    if (count > 0) {
//                        $("#notConfiguredPopup").modal('show');
//                    } else {
                    $scope.updateDesignation();
//                    }

                } else {
                    $scope.showConfirmationPopup();
                }
            };
            $scope.showConfirmationPopup = function() {
                $("#confirmationPopUp").modal('show');
            };
            $scope.hideConfirmationPopup = function() {
                $("#confirmationPopUp").modal('hide');
                $rootScope.removeModalOpenCssAfterModalHide();
                $('#designationActiveUserModal').modal('hide');
                $rootScope.removeModalOpenCssAfterModalHide();
            };
            $scope.isTextContains = function(featurename) {
                if ($scope.searchSystemFeature !== null && $scope.searchSystemFeature !== undefined && $scope.searchSystemFeature !== "") {
                    if (featurename.toLowerCase().indexOf($scope.searchSystemFeature.toLowerCase()) >= 0)
                    {
                        return true;
                    }
                    else {
                        return false;
                    }
                }
            };
            $scope.filterTypeahead = function() {
                var searchDesigFilterData = $filter('filter')($scope.designationList, {name: $scope.searchDesignation});
                if (searchDesigFilterData.length > 0) {
                    $scope.isInValidSearchDesig = false;
                }
                else {
                    $scope.isInValidSearchDesig = true;
                }
            };
            //new Added methods after UI change
            $scope.checkUncheckIA = function(feature) {
                var iaLen = feature.iteamAttributesList.length;
                for (var i = 0; i < iaLen; i++) {
                    feature.iteamAttributesList[i].checked = feature.checked;
                }
                if (!feature.checked && feature.name == "goals_add_edit") {
                    feature.configure = false;
                    $scope.configureGoal = {};
                    $scope.goalPermissionToConfigure = [];
                    $scope.selectedInStringIds = [];
                    $scope.selectedInString = [];
                    $scope.selectedDepValues = [];
                    $scope.defaultDepartmentIds = undefined;
                    $scope.listForTable = [];
                    uncheckAll($scope.multiselecttree.items);
                    $('#designation').select2('val', []);
                    $('#service').select2('val', []);
//                    $scope.roleForm.$setPristine();
                }
                if (!feature.checked && feature.name == "goalsheet_access") {
                    feature.configure = false;
                }
            };
            var uncheckAll = function(list, ary) {
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
            $scope.checkUncheckMI = function(feature) {
                var totalChecked = 0;
                var iaLen = feature.iteamAttributesList.length;
                if (iaLen > 0) {
                    for (var i = 0; i < iaLen; i++) {
                        if (feature.iteamAttributesList[i].checked) {
                            totalChecked++;
                        }
                    }
                    if (totalChecked === iaLen) {
                        feature.checked = feature.iteamAttributesList[0].checked;
                    }
                    else {
                        feature.checked = false;
                    }
                }
            };
            $scope.checkUncheckDiamondIA = function(feature) {
                var iaLen = feature.iteamAttributesList.length;
                for (var i = 0; i < iaLen; i++) {
                    feature.iteamAttributesList[i].checked = feature.checked;
                }
                if (feature.configure && !feature.checked) {
                    feature.configure = false;
                    for (var key in $scope.featureFieldMap) {
                        if (key == feature.id) {
                            delete $scope.featureFieldMap[key];
                        }
                    }
                }

            };

            $scope.retrieveFieldsByFeature = function() {
                Designation.retrieveFieldsByFeature(function(res) {
                    angular.forEach(res.data, function(item) {
                        if (item.length > 0) {
                            angular.forEach(item, function(itemValue) {
                                $scope.fieldList.push(itemValue);
                            });
                        }

                    });
                });
            };
            $scope.retrieveFieldsByFeature();
            $scope.combinedFieldListForSearch = [];
            $scope.configureFeature = function(diamondSystemFeature) {
                console.log("diamond System featrure..." + JSON.stringify(diamondSystemFeature))
                if (diamondSystemFeature.checked) {
                    $scope.diamondFeature = angular.copy(diamondSystemFeature);
                    var label = $scope.diamondFeature.menuLabel.split('_');
                    if (label[1]) {
                        var menuLabel = label[1] + " " + label[0];
                        $scope.labels = menuLabel;
                    } else {
                        var menuLabel = label[0];
                        $scope.labels = menuLabel;
                    }

                    $scope.type = $scope.diamondFeature.type;
                    $scope.featureEntity = label[0];
//                    if ($scope.type === "DEI" || $scope.labels === "static print" || $scope.labels === "merge stock") {
//                        $scope.entityList = [{id: 'Lot', name: 'Lot'}, {id: 'Packet', name: 'Packet'}];
                    $scope.entityList = $scope.visibleFields[$scope.diamondFeature.menuLabel];
                    if (!!$scope.entityList) {
                        if (!!!$scope.entitys) {
                            $scope.entitys = {};
                        }
                        $scope.entitys.name = $scope.entityList[0].id;
                    }
//                    }
                    var temp = '$scope.invoiceList';
                    $scope.invoiceList = [];
                    $scope.lotList = [];
                    $scope.parcelList = [];
                    $scope.packetList = [];
                    $scope.coatedRoughList = [];
                    $scope.issueList = [];
                    $scope.planList = [];
                    $scope.diamondList = [];
                    $scope.allotmentList = [];
                    $scope.transferList = [];
                    $scope.sellList = [];
                    $scope.selectedFieldsForSearch = [];
                    $scope.clearSelectedSearchFields();
                    $scope.clearSelectedParentFields();
                    $scope.selectedFieldsForParent = [];
                    $scope.combinedFieldListForSearch = [];
                    $scope.combinedFieldListForParent = [];
//                    $scope.fieldList = [];
                    $scope.fieldListToSend = [];
//                    Designation.retrieveFieldsByFeature(function (res) {
//                        angular.forEach(res.data, function (item) {
//                            if (item.length > 0) {
//                                angular.forEach(item, function (itemValue) {
//                                    $scope.fieldList.push(itemValue);
//                                });
//                            }
//
//                        });
                    $scope.fieldListToSend = angular.copy($scope.fieldList);
                    angular.forEach($scope.fieldList, function(item) {
                        if (item.entity === 'Invoice') {
                            $scope.invoiceList.push(item);
                        } else if (item.entity === 'Parcel') {
                            $scope.parcelList.push(item);
                        } else if (item.entity === 'Packet') {
                            $scope.packetList.push(item);
                        } else if (item.entity === 'Lot') {
                            $scope.lotList.push(item);
                        } else if (item.entity === 'Coated Rough') {
                            $scope.coatedRoughList.push(item);
                        } else if (item.entity === 'Issue') {
                            $scope.issueList.push(item);
                        } else if (item.entity === 'Plan') {
                            $scope.planList.push(item);
                        } else if (item.entity === 'Diamond') {
                            $scope.diamondList.push(item);
                        } else if (item.entity === 'Allotment') {
                            $scope.allotmentList.push(item);
                        } else if (item.entity === 'Transfer') {
                            $scope.transferList.push(item);
                        } else if (item.entity === 'Sell') {
                            $scope.sellList.push(item);
                        }
                    });
                    //----Combining the list to prepare data for multiselect checkbox--------------
                    //If fields added if invoice etity contains any field.
                    if ($scope.invoiceList.length > 0) {
                        $scope.combinedFieldListForSearch.push(
                                {
                                    modelName: '<strong>Invoice</strong>',
                                    multiSelectGroup: true
                                });
                        if ($scope.invoiceList.length > 0) {
                            angular.forEach($scope.invoiceList, function(item) {
                                if (item.dbFieldName !== undefined) {
                                    var split = item.dbFieldName.split('$');
                                    if (split.length > 0) {
                                        var componentType = split[1];
                                        if (componentType !== 'IMG' && componentType !== 'UPD') {
                                            $scope.combinedFieldListForSearch.push(item);
                                        }
                                    }
                                }
                            });
                        }
                    }
                    if ($scope.invoiceList.length !== 0) {
                        $scope.combinedFieldListForSearch.push({
                            multiSelectGroup: false
                        });
                    }
                    //parcel fields are added to search list if parcel entity contains any.
                    if ($scope.parcelList.length > 0) {
                        $scope.combinedFieldListForSearch.push(
                                {
                                    modelName: '<strong>Parcel</strong>',
                                    multiSelectGroup: true
                                });
                        if ($scope.parcelList.length > 0) {
                            angular.forEach($scope.parcelList, function(item) {
                                if (item.dbFieldName !== undefined) {
                                    var split = item.dbFieldName.split('$');
                                    if (split.length > 0) {
                                        var componentType = split[1];
                                        if (componentType !== 'IMG' && componentType !== 'UPD') {
                                            $scope.combinedFieldListForSearch.push(item);
                                        }
                                    }
                                }
                            });
                        }
                    }

                    if ($scope.parcelList.length !== 0) {
                        $scope.combinedFieldListForSearch.push({
                            multiSelectGroup: false
                        });
                    }
                    //lot fields are added to search list if lot entity contains any.
                    if ($scope.lotList.length > 0) {
                        $scope.combinedFieldListForSearch.push(
                                {
                                    modelName: '<strong>Lot</strong>',
                                    multiSelectGroup: true
                                });
                        if ($scope.lotList.length > 0) {
                            angular.forEach($scope.lotList, function(item) {
                                if (item.dbFieldName !== undefined) {
                                    var split = item.dbFieldName.split('$');
                                    if (split.length > 0) {
                                        var componentType = split[1];
                                        if (componentType !== 'IMG' && componentType !== 'UPD') {
                                            $scope.combinedFieldListForSearch.push(item);
                                        }
                                    }
                                }
                            });
                        }
                    }
                    if ($scope.lotList.length !== 0) {
                        $scope.combinedFieldListForSearch.push({
                            multiSelectGroup: false
                        });
                    }
                    //packet fields are added to search list if packet entity contains any.
                    if ($scope.packetList.length > 0) {
                        $scope.combinedFieldListForSearch.push({
                            modelName: '<strong>Packet</strong>',
                            multiSelectGroup: true
                        });
                        if ($scope.packetList.length > 0) {
                            angular.forEach($scope.packetList, function(item) {
                                if (item.dbFieldName !== undefined) {
                                    var split = item.dbFieldName.split('$');
                                    if (split.length > 0) {
                                        var componentType = split[1];
                                        if (componentType !== 'IMG' && componentType !== 'UPD') {
                                            $scope.combinedFieldListForSearch.push(item);
                                        }
                                    }
                                }
                            });
                        }
                    }
                    if ($scope.packetList.length !== 0) {
                        $scope.combinedFieldListForSearch.push({
                            multiSelectGroup: false
                        });
                    }
                    //issue fields are added to search list if issue entity contains any.
                    if ($scope.issueList.length > 0) {
                        $scope.combinedFieldListForSearch.push({
                            modelName: '<strong>Issue</strong>',
                            multiSelectGroup: true
                        });
                        if ($scope.issueList.length > 0) {
                            angular.forEach($scope.issueList, function(item) {
                                if (item.dbFieldName !== undefined) {
                                    var split = item.dbFieldName.split('$');
                                    if (split.length > 0) {
                                        var componentType = split[1];
                                        if (componentType !== 'IMG' && componentType !== 'UPD') {
                                            $scope.combinedFieldListForSearch.push(item);
                                        }
                                    }
                                }
                            });
                        }
                    }
                    if ($scope.issueList.length !== 0) {
                        $scope.combinedFieldListForSearch.push({
                            multiSelectGroup: false
                        });
                    }
                    //transfer fields are added to search list if transfer entity contains any.
                    if ($scope.transferList.length > 0) {
                        $scope.combinedFieldListForSearch.push({
                            modelName: '<strong>Transfer</strong>',
                            multiSelectGroup: true
                        });
                        if ($scope.transferList.length > 0) {
                            angular.forEach($scope.transferList, function(item) {
                                if (item.dbFieldName !== undefined) {
                                    var split = item.dbFieldName.split('$');
                                    if (split.length > 0) {
                                        var componentType = split[1];
                                        if (componentType !== 'IMG' && componentType !== 'UPD') {
                                            $scope.combinedFieldListForSearch.push(item);
                                        }
                                    }
                                }
                            });
                        }
                    }
                    if ($scope.transferList.length !== 0) {
                        $scope.combinedFieldListForSearch.push({
                            multiSelectGroup: false
                        });
                    }
                    //sell fields are added to search list if sell entity contains any.
                    if ($scope.sellList.length > 0) {
                        $scope.combinedFieldListForSearch.push({
                            modelName: '<strong>Sell</strong>',
                            multiSelectGroup: true
                        });
                        if ($scope.sellList.length > 0) {
                            angular.forEach($scope.sellList, function(item) {
                                if (item.dbFieldName !== undefined) {
                                    var split = item.dbFieldName.split('$');
                                    if (split.length > 0) {
                                        var componentType = split[1];
                                        if (componentType !== 'IMG' && componentType !== 'UPD') {
                                            $scope.combinedFieldListForSearch.push(item);
                                        }
                                    }
                                }
                            });
                        }
                    }
                    if ($scope.sellList.length !== 0) {
                        $scope.combinedFieldListForSearch.push({
                            multiSelectGroup: false
                        });
                    }
                    $scope.valList = {"Invoice": $scope.invoiceList,
                        "Parcel": $scope.parcelList,
                        "Lot": $scope.lotList,
                        "Packet": $scope.packetList,
                        "Issue": $scope.issueList,
                        "Plan": $scope.planList,
                        "Allotment": $scope.allotmentList,
                        "Transfer": $scope.transferList,
                        "Sell": $scope.sellList};

                    //----Combining the list to prepare data for multiselect checkbox for parent entities--------------
                    if ($scope.invoiceList.length > 0) {
                        $scope.combinedFieldListForParent.push(
                                {
                                    modelName: '<strong>Invoice</strong>',
                                    multiSelectGroup1: true
                                });
                        if ($scope.invoiceList.length > 0) {
                            angular.forEach($scope.invoiceList, function(item) {
                                $scope.combinedFieldListForParent.push(angular.copy(item));
                            });
                        }
                    }
                    if ($scope.invoiceList.length !== 0) {
                        $scope.combinedFieldListForParent.push({
                            multiSelectGroup1: false
                        });
                    }
                    //parcel fields are added to parent list if parcel entity contains any.
                    if ($scope.parcelList.length > 0) {
                        $scope.combinedFieldListForParent.push(
                                {
                                    modelName: '<strong>Parcel</strong>',
                                    multiSelectGroup1: true
                                });
                        if ($scope.parcelList.length > 0) {
                            angular.forEach($scope.parcelList, function(item) {
                                $scope.combinedFieldListForParent.push(angular.copy(item));
                            });
                        }
                    }

                    if ($scope.parcelList.length !== 0) {
                        $scope.combinedFieldListForParent.push({
                            multiSelectGroup1: false
                        });
                    }
                    //lot fields are added to parent list if lot entity contains any.
                    if ($scope.lotList.length > 0) {
                        $scope.combinedFieldListForParent.push(
                                {
                                    modelName: '<strong>Lot</strong>',
                                    multiSelectGroup1: true
                                });
                        if ($scope.lotList.length > 0) {
                            angular.forEach($scope.lotList, function(item) {
                                $scope.combinedFieldListForParent.push(angular.copy(item));
                            });
                        }
                    }
                    if ($scope.lotList.length !== 0) {
                        $scope.combinedFieldListForParent.push({
                            multiSelectGroup1: false
                        });
                    }
                    //packet fields are added to parent list if packet entity contains any.
                    if ($scope.packetList.length > 0) {
                        $scope.combinedFieldListForParent.push({
                            modelName: '<strong>Packet</strong>',
                            multiSelectGroup1: true
                        });
                        if ($scope.packetList.length > 0) {
                            angular.forEach($scope.packetList, function(item) {
                                $scope.combinedFieldListForParent.push(angular.copy(item));
                            });
                        }
                    }
                    if ($scope.packetList.length !== 0) {
                        $scope.combinedFieldListForParent.push({
                            multiSelectGroup1: false
                        });
                    }
                    //issue fields are added to parent list if issue entity contains any.
                    if ($scope.issueList.length > 0) {
                        $scope.combinedFieldListForParent.push({
                            modelName: '<strong>Issue</strong>',
                            multiSelectGroup1: true
                        });
                        if ($scope.issueList.length > 0) {
                            angular.forEach($scope.issueList, function(item) {
                                $scope.combinedFieldListForParent.push(angular.copy(item));
                            });
                        }
                    }
                    if ($scope.issueList.length !== 0) {
                        $scope.combinedFieldListForParent.push({
                            multiSelectGroup1: false
                        });
                    }
//                        $scope.combinedFieldListForParent = angular.copy($scope.combinedFieldListForSearch);
                    var label = $scope.diamondFeature.menuLabel.split('_');
                    var entity = label[0];
                    angular.forEach($scope.visibleFields[$scope.diamondFeature.menuLabel], function(item) {
                        if (!!item) {
                            if (!!!$scope.commonList || $scope.commonList.length < 1) {
                                $scope.commonList = [];
                                $scope.commonList = angular.copy($scope.valList[item.id]);
                            } else {
                                if (!!$scope.valList[item.id]) {
                                    $.merge($scope.commonList, $scope.valList[item.id]);
                                }
                            }
                        }
                    });

                    angular.forEach($scope.commonList, function(item) {
                        item.selected = "hide";
//                        item.isRequired = false;
                    });
                    $scope.commonList.sort(SortByFieldSequence);
                    if ($scope.isEditing || $scope.isCopy || diamondSystemFeature.configure === true) {
                        $scope.editFieldList = [];
                        $scope.editFieldList = $scope.featureFieldMap[diamondSystemFeature.id];
                        angular.forEach($scope.editFieldList, function(editItem) {
                            if (editItem.searchFlag) {
                                editItem.ticked = true;
                                $scope.selectedFieldsForSearch.push(editItem);
                                editItem.ticked = false;
                            }
                            angular.forEach($scope.combinedFieldListForSearch, function(item) {
                                if (editItem.field === item.field && editItem.searchFlag) {
                                    item.ticked = true;
                                }
                            });
                        });
                        angular.forEach($scope.editFieldList, function(editItem) {
                            if (editItem.parentViewFlag) {
                                editItem.ticked = true;
                                $scope.selectedFieldsForParent.push(editItem);
                                editItem.ticked = false;
                            }
                            angular.forEach($scope.combinedFieldListForParent, function(item) {
                                if (editItem.field === item.field && editItem.parentViewFlag) {
                                    item.ticked = true;
                                }
                            });
                        });
                    }
//                    console.log("diamondSystemFeature :" + JSON.stringify(diamondSystemFeature));
                    if (!$scope.isEditing && !$scope.isCopy && diamondSystemFeature.configure === false) {
                        var length = $scope.diamondSystemFeatureList.length;
                        for (var key in $scope.featureFieldMap) {
                            for (var i = 0; i < length; i++) {
                                if (key === $scope.diamondSystemFeatureList[i].id && diamondSystemFeature.configure === false) {
                                    var name = $scope.diamondSystemFeatureList[i].menuLabel.split('_');
                                    if (name[0] === label[0]) {
                                        $scope.editFieldList = [];
                                        $scope.editFieldList = $scope.featureFieldMap[key];
                                        angular.forEach($scope.editFieldList, function(editItem) {
                                            if (editItem.searchFlag) {
                                                editItem.ticked = true;
                                                $scope.selectedFieldsForSearch.push(editItem);
                                                editItem.ticked = false;
                                            }
                                            angular.forEach($scope.combinedFieldListForSearch, function(item) {
                                                if (editItem.field === item.field && editItem.searchFlag) {
                                                    item.ticked = true;
                                                }
                                            });
                                        });
                                        angular.forEach($scope.editFieldList, function(editItem) {
                                            if (editItem.parentViewFlag) {
                                                editItem.ticked = true;
                                                $scope.selectedFieldsForParent.push(editItem);
                                                editItem.ticked = false;
                                            }
                                            angular.forEach($scope.combinedFieldListForParent, function(item) {
                                                if (editItem.field === item.field && editItem.parentViewFlag) {
                                                    item.ticked = true;
                                                }
                                            });
                                        });
                                    }
                                }

                            }
                        }
                    }

//                    }, function () {
//                        alert("failure");
//                    });
                    $("#configurePopUp").modal('show');
                }

            };
            $scope.setSearchField = function(selectedFieldsForSearch) {
                $scope.selectedFieldsForSearch = angular.copy(selectedFieldsForSearch);
            };
            $scope.setParentField = function(selectedFieldsForParent) {
                $scope.selectedFieldsForParent = angular.copy(selectedFieldsForParent);
            };

//            $scope.selectAllForParent = function () {
//                angular.forEach($scope.combinedFieldListForParent, function (item) {
//                    $scope.selectedFieldsForParent.push(item);
//                });
//            };
//            $scope.selectAllForSearch = function () {
//                angular.forEach($scope.combinedFieldListForSearch, function (item) {
//                    $scope.selectedFieldsForSearch.push(item);
//                });
//            };
//            
//            $scope.selectNoneForSearch = function () {
//                $scope.selectedFieldsForSearch = [];
//            };
//             $scope.selectNoneForParent = function () {
//                $scope.selectedFieldsForParent = [];
//            };
            $scope.cancelConfirmPopup = function() {
                $("#notConfiguredPopup").modal('hide');
                $rootScope.removeModalOpenCssAfterModalHide();

            };
            $scope.cancelConfigurationPopup = function() {
                countContent = 0;
                $scope.nextPage = false;
//                $scope.diamondFeature = '';
                $scope.selectedFieldsForSearch = [];
                $scope.clearSelectedSearchFields();
                $scope.clearSelectedParentFields();
                $scope.selectedFieldsForParent = [];
                $scope.commonList = [];
                $scope.entitys = [];
                for (var i = 0; i < $scope.systemFeaturesList.length; i++) {
                    $scope.systemFeaturesList[i].configure = false;
                }
                $("#configurePopUp").modal('hide');
                $rootScope.removeModalOpenCssAfterModalHide();

                if ($scope.diamondFeature && ($scope.featureFieldMap[$scope.diamondFeature.id] !== undefined && $scope.featureFieldMap[$scope.diamondFeature.id] !== null) && $scope.featureFieldMap[$scope.diamondFeature.id].length > 0) {
                    for (var index = 0; index < $scope.featureFieldMap[$scope.diamondFeature.id].length; index++) {
                        delete $scope.featureFieldMap[$scope.diamondFeature.id][index].ticked;
                    }
                }
            };
            $scope.setNextPage = function() {
                $scope.nextPage = true;
                if ($scope.isEditing) {
                    if ($scope.editFieldList) {
                        for (var i = 0; i < $scope.editFieldList.length; i++) {
                            for (var j = 0; j < $scope.commonList.length; j++) {
                                if ($scope.editFieldList[i].field === $scope.commonList[j].field) {
                                    if ($scope.editFieldList[i].readonlyFlag) {
                                        $scope.commonList[j].selected = 'view only';
                                    } else if ($scope.editFieldList[i].editableFlag) {
                                        $scope.commonList[j].selected = 'view and edit';
                                    } else {
                                        $scope.commonList[j].selected = 'hide';
                                    }
                                    $scope.commonList[j].sequenceNo = $scope.editFieldList[i].sequenceNo;
                                    $scope.commonList[j].isRequired = $scope.editFieldList[i].isRequired;
                                }
                            }
                        }
                        $scope.commonList.sort(SortByFieldSequence);
                    }
                }
            };
            $scope.setPreviousPage = function() {
                $scope.nextPage = false;
            };
            function SortByFieldSequence(x, y) {
                if (x.sequenceNo === '' || x.sequenceNo === null) {
                    return 1;
                }
                if (y.sequenceNo === '' || y.sequenceNo === null) {
                    return -1;
                }
                return x.sequenceNo - y.sequenceNo;
            }

            $scope.sortSequence = function() {
                $scope.commonList.sort(SortByFieldSequence);
            };
            $scope.savePermission = function() {
                for (var i = 0; i < $scope.commonList.length; i++) {
                    for (var j = 0; j < $scope.fieldListToSend.length; j++) {
                        if ($scope.commonList[i].field === $scope.fieldListToSend[j].field) {
                            if ($scope.commonList[i].selected === 'hide') {
                            } else if ($scope.commonList[i].selected === 'view only') {
                                $scope.fieldListToSend[j].readonlyFlag = true;
                            } else if ($scope.commonList[i].selected === 'view and edit') {
                                $scope.fieldListToSend[j].editableFlag = true;
                            }
                            $scope.fieldListToSend[j].sequenceNo = $scope.commonList[i].sequenceNo;
                            $scope.fieldListToSend[j].isRequired = $scope.commonList[i].isRequired;
                        }
                    }
                }
                for (var i = 0; i < $scope.selectedFieldsForSearch.length; i++) {
                    for (var j = 0; j < $scope.fieldListToSend.length; j++) {
                        if ($scope.selectedFieldsForSearch[i].field === $scope.fieldListToSend[j].field) {
                            $scope.fieldListToSend[j].searchFlag = true;
                        }
                    }
                }
                for (var i = 0; i < $scope.selectedFieldsForParent.length; i++) {
                    for (var j = 0; j < $scope.fieldListToSend.length; j++) {
                        if ($scope.selectedFieldsForParent[i].field === $scope.fieldListToSend[j].field) {
                            $scope.fieldListToSend[j].parentViewFlag = true;
                        }
                    }
                }
                $scope.featureFieldMap[$scope.diamondFeature.id] = $scope.fieldListToSend;
                var length = $scope.diamondSystemFeatureList.length;
                for (var i = 0; i < length; i++) {
                    if ($scope.diamondSystemFeatureList[i].id === $scope.diamondFeature.id && $scope.diamondSystemFeatureList[i].checked) {
                        $scope.diamondSystemFeatureList[i].configure = true;
                    }

                }
//                $scope.nextPage = false;
                $scope.cancelConfigurationPopup();
                $("#configurePopUp").modal('hide');
                $rootScope.removeModalOpenCssAfterModalHide();
                countContent = 0;
            };
            $scope.commonListDtOptions = {
                aoColumnDefs: [
                    {
                        bSortable: false,
                        aTargets: [-2, -3]
                    }
                ]
            };
            var countContent = 0;
            $scope.fieldTableContent = function() {
                countContent++;
                //Method is commented. Below code will never be executed.
                //Only last line used to get new filtered records.
                if (countContent <= 0) {
                    $scope.commonList = [];
//                    angular.forEach($scope.lotList, function(item) {
//                        $scope.commonList.push(item);
//                    });
//                    angular.forEach($scope.packetList, function(item) {
//                        $scope.commonList.push(item);
//                    });
                    angular.forEach($scope.visibleFields[$scope.diamondFeature.menuLabel], function(item) {

                        if (!!item) {
                            if (!!!$scope.commonList || $scope.commonList.length < 1) {
                                $scope.commonList = [];
                                $scope.commonList = angular.copy($scope.valList[item.id]);
                            } else {
                                $.merge($scope.commonList, $scope.valList[item.id]);
                            }
                        }
                    });
                    angular.forEach($scope.commonList, function(item) {
                        item.selected = "hide";
                        item.isRequired = false;
                    });
                    if ($scope.editFieldList) {
                        for (var i = 0; i < $scope.editFieldList.length; i++) {
                            for (var j = 0; j < $scope.commonList.length; j++) {
                                if ($scope.editFieldList[i].field === $scope.commonList[j].field) {
                                    if ($scope.editFieldList[i].readonlyFlag) {
                                        $scope.commonList[j].selected = 'view only';
                                    } else if ($scope.editFieldList[i].editableFlag) {
                                        $scope.commonList[j].selected = 'view and edit';
                                    } else {
                                        $scope.commonList[j].selected = 'hide';
                                    }
                                    $scope.commonList[j].sequenceNo = $scope.editFieldList[i].sequenceNo;
                                    $scope.commonList[j].isRequired = $scope.editFieldList[i].isRequired;
                                }
                            }
                        }
                    }

                }
                //Used as a parameter to data table to determine length of filtered data.
                $scope.entitys.filteredList = $filter('commonListFilter')($scope.commonList, $scope.entitys.name);

            };
            hkg.register.filter('commonListFilter', function() {
                return  function(items, searchText) {
                    if (angular.isDefined(items) && angular.isDefined(searchText)) {
                        if (angular.isDefined(searchText) && !!searchText) {
                            var filtered = [];
                            for (var i = 0; i < items.length; i++) {
                                var item = items[i];
                                if ((item.entity !== null && item.entity.toLowerCase().indexOf(searchText.toLowerCase()) >= 0))
                                {
                                    filtered.push(item);
                                }
                            }
                            return filtered;
                        }
                    } else {
                        if ($scope.type === "DMI") {
                            return items;
                        } else {
                            return null;
                        }
                    }


                };
            });
            //Remove current designation from parent selection.
            hkg.register.filter('removeCurrent', function() {
                return  function(items, designationId) {
                    var filtered = [];
                    if (items !== undefined && items !== null) {
                        for (var i = 0; i < items.length; i++) {
                            var item = items[i];
                            if (item.id != designationId) {
                                filtered.push(item);
                            }
                        }
                        return filtered;
                    } else {
                        return null;
                    }
                };
            });
            $scope.configureGoal = {};
            $scope.configureNonDiamondFeature = function(nonDiamondSystemFeature) {
                $scope.diamondGoalFeature = nonDiamondSystemFeature;
                if (nonDiamondSystemFeature.checked) {
                    $scope.labels = nonDiamondSystemFeature.menuLabel;
                    if (nonDiamondSystemFeature.name === 'goals_add_edit') {
                        $("#configureNonDiamondPopUp").modal('show');
                    }
                    else if (nonDiamondSystemFeature.name === 'goalsheet_access') {
                        $("#configureGoalSheetSheetPopUp").modal('show');
                    }
                }
            };
            $scope.cancelNonDiamondConfigurationPopup = function() {
                $("#configureNonDiamondPopUp").modal('hide');
                $rootScope.removeModalOpenCssAfterModalHide();

            };
            $scope.cancelGoalSheetPopup = function() {
                $("#configureGoalSheetSheetPopUp").modal('hide');
                $rootScope.removeModalOpenCssAfterModalHide();
            }
            $scope.autoCompleteApprover = {
                allowClear: true,
                multiple: true,
                closeOnSelect: false,
                placeholder: 'Select designation',
                initSelection: function(element, callback) {
                    var tempValues = [];
                    if (!!$scope.configureGoal.designation) {
                        angular.forEach($scope.designationList, function(designation) {
                            angular.forEach($scope.configureGoal.designation, function(item) {
                                if (item === designation.id) {
                                    tempValues.push({
                                        id: designation.id,
                                        text: designation.displayName
                                    });
                                }
                            });
                        });
                    }

                    callback(tempValues);
                },
                query: function(query) {
                    var selected = query.term;
                    $scope.names = [];
                    var success = function(data) {
                        if (data.length == 0) {
                            query.callback({
                                results: $scope.names
                            });
                        } else {
                            angular.forEach(data, function(item) {
                                $scope.names.push({
                                    id: item.id,
                                    text: item.displayName
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
                        Designation.retrieveDesignationBySearch({"q": query.term.trim()}, success, failure);
                    }

                }

            };
            $scope.autoCompleteApproverGoalSheet = {
                allowClear: true,
                multiple: true,
                closeOnSelect: false,
                placeholder: 'Select designation',
                initSelection: function(element, callback) {
                    var tempValues = [];
                    if (!!$scope.configureGoalSheet.designation) {
                        angular.forEach($scope.designationList, function(designation) {
                            angular.forEach($scope.configureGoalSheet.designation, function(item) {
                                if (item === designation.id) {
                                    tempValues.push({
                                        id: designation.id,
                                        text: designation.displayName
                                    });
                                }
                            });
                        });
                    }

                    callback(tempValues);
                },
                query: function(query) {
                    var selected = query.term;
                    $scope.names = [];
                    var success = function(data) {
                        if (data.length == 0) {
                            query.callback({
                                results: $scope.names
                            });
                        } else {
                            angular.forEach(data, function(item) {
                                $scope.names.push({
                                    id: item.id,
                                    text: item.displayName
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
                        Designation.retrieveDesignationBySearch({"q": query.term.trim()}, success, failure);
                    }

                }

            };
            //Called when dropdown is clicked
            $scope.setSelectedParent = function(selected) {
                $scope.$on('$locationChangeStart', function(event) {
                    if (selected !== null)
                        if (!confirm('You have unsaved changes, go back?')) {
                            event.preventDefault();
                        } else {
                            selected === null;
                        }
                });
                if (!angular.equals(selected, {})) {
                    $scope.invalidParent = false;
                    $scope.selectedParent = selected.currentNode;
                    if ($scope.selectedParent.displayName.length > 50) {
                        $scope.selectedParent.displayName = $scope.selectedParent.displayName.substring(0, 32).concat("---");
                    }
                }
            };
            $scope.initDept = function() {
                $scope.multiselecttree = {"text": "All Department", "isChecked": false,
                    items: []};
                $scope.departments = $scope.retrieveAllDepartments();
                $scope.createWatchOnSelectedItemList();
            };
            $scope.initDeptGoalSheet = function() {
                $scope.multiselecttreeGoalSheet = {"text": "All Department", "isChecked": false,
                    items: []};
                $scope.departments = $scope.retrieveAllDepartments();
                $scope.createWatchOnSelectedItemListGoalSheet();
            };
            $scope.retrieveAllDepartments = function() {

                var failure = function() {
                    var msg = "Failed to retrieve departments. Try again.";
                    var type = $rootScope.failure;
                    $rootScope.addMessage(msg, type);
                    $rootScope.unMaskLoading();
                };
                LeaveWorkflow.retrieveDepartmentsCombine(function(res) {
                    $scope.multiselect = []; // your list declaration
                    if (res['tree3'].items != null && angular.isDefined(res['tree3'].items) && res['tree3'].items.length > 0) {
                        angular.forEach(res['tree3'].items, function(item) {
                            $scope.multiselect.push(item);
                        });
                    }
                    $scope.multiselecttree.items = $scope.multiselect;
                    $scope.multiselecttreeGoalSheet.items = $scope.multiselect;
                }, failure);
            };
            $scope.createWatchOnSelectedItemList = function() {
                $scope.$watch('selectedItemList', function(value) {
                    if ($scope.selectedItemList != undefined) {
                        $scope.selectedInString = [];
                        $scope.selectedInStringIds = [];
                        if ($scope.selectedItemList.items.length > 0) {
                            $scope.selectedDepValues = [];
                            $scope.iterateChildOfTree($scope.selectedItemList.items);
                        } else {
                            $scope.selectedDepValues = [];
                            $scope.selectedInString = [];
                            $scope.selectedInStringIds = [];
                            $scope.configureGoal.isDepInvalid = true;
                        }
                    }
                    if (!!$scope.selectedDepValues) {
                        if (!!$scope.listForTable) {
                            var indexes = $.map($scope.listForTable, function(obj, index) {
                                if (obj.type == 'Department') {
                                    return index;
                                }
                            });
                        }
                        if (!!indexes) {
                            indexes.sort();
                            for (var i = 0; i < indexes.length; i++) {
                                $scope.listForTable.splice(indexes[i] - i, 1);
                            }
                        }
                        if ($scope.selectedDepValues.length > 0) {
                            angular.forEach($scope.selectedDepValues, function(item) {
                                $scope.selectedInString.push(item.text);
                                $scope.selectedInStringIds.push(item.id);
                                if (!!!$scope.listForTable) {
                                    $scope.listForTable = [];
                                }
                                $scope.listForTable.push({id: item.id, name: item.text, type: 'Department'});
                            });
                        }
                    }
                }, true);

            };
            $scope.createWatchOnSelectedItemListGoalSheet = function() {
                $scope.$watch('selectedItemListGoalSheet', function(value) {
                    if ($scope.selectedItemListGoalSheet != undefined) {
                        $scope.selectedInStringGoalSheet = [];
                        $scope.selectedInStringIdsGoalSheet = [];
                        if ($scope.selectedItemListGoalSheet.items.length > 0) {
                            $scope.selectedDepValuesGoalSheet = [];
                            $scope.iterateChildOfTreeGoalSheet($scope.selectedItemListGoalSheet.items);
                        } else {
                            $scope.selectedDepValuesGoalSheet = [];
                            $scope.selectedInStringGoalSheet = [];
                            $scope.selectedInStringIdsGoalSheet = [];
                            $scope.configureGoalSheet.isDepInvalid = true;
                        }
                    }
                    if (!!$scope.selectedDepValuesGoalSheet) {
                        if (!!$scope.listOfPermissionGoalSheet) {
                            var indexes = $.map($scope.listOfPermissionGoalSheet, function(obj, index) {
                                if (obj.type == 'Department') {
                                    return index;
                                }
                            });
                        }
                        if (!!indexes) {
                            indexes.sort();
                            for (var i = 0; i < indexes.length; i++) {
                                $scope.listOfPermissionGoalSheet.splice(indexes[i] - i, 1);
                            }
                        }
                        if ($scope.selectedDepValuesGoalSheet.length > 0) {
                            angular.forEach($scope.selectedDepValuesGoalSheet, function(item) {
                                $scope.selectedInStringGoalSheet.push(item.text);
                                $scope.selectedInStringIdsGoalSheet.push(item.id);
                                if (!!!$scope.listOfPermissionGoalSheet) {
                                    $scope.listOfPermissionGoalSheet = [];
                                }
                                $scope.listOfPermissionGoalSheet.push({id: item.id, name: item.text, type: 'Department'});
                            });
                        }
                    }
                }, true);
            }
            $scope.iterateChildOfTree = function(childItems) {
                var currentitems = childItems;
                for (var i = 0; i < currentitems.length; i++) {
                    if (currentitems[i].isChecked) {
                        $scope.selectedDepValues.push({id: currentitems[i].id, text: currentitems[i].text});
                    }
                    if (currentitems[i].items) {
                        $scope.iterateChildOfTree(currentitems[i].items);
                    }
                }
            };
            $scope.iterateChildOfTreeGoalSheet = function(childItems) {
                var currentitems = childItems;
                for (var i = 0; i < currentitems.length; i++) {
                    if (currentitems[i].isChecked) {
                        $scope.selectedDepValuesGoalSheet.push({id: currentitems[i].id, text: currentitems[i].text});
                    }
                    if (currentitems[i].items) {
                        $scope.iterateChildOfTreeGoalSheet(currentitems[i].items);
                    }
                }
            };
            $scope.clearTreeSelection = function(childItems) {
                var currentitems = childItems;
                currentitems.isChecked = false;
                if (!!currentitems.items && currentitems.items.length > 0) {
                    $scope.clearTreeSelection(currentitems.items);
                }
                $scope.clearData = currentitems;
            };
            $scope.getDepName = function(id, childItems) {
                var currentitems = childItems;
                for (var i = 0; i < currentitems.length; i++) {
                    if (currentitems[i].id == id) {
                        $scope.depName = currentitems[i].text;
                    } else if (currentitems[i].items) {
                        $scope.getDepName(id, currentitems[i].items);
                    }
                }
                return $scope.depName;
            }
            $scope.initSelectedChildsOfTree = function(childItems, arrayOfIds) {
                var currentitems = childItems;
                for (var i = 0; i < currentitems.length; i++) {
                    if (arrayOfIds.indexOf(currentitems[i].id)) {
                        $scope.selectedvals.push({id: currentitems[i].id, text: currentitems[i].text, isChecked: true});
                    }
                    if (currentitems[i].items) {
                        $scope.initSelectedChildsOfTree(currentitems[i].items, arrayOfIds);
                    }
                }
            };
            //MM - 21-9-2015 Commented Service related Code
//            ActivityFlowService.retrieveServicesWithActivity({"q": ""}, function(data) {
//                $scope.allServices = data.data;
//            }, function() {
//                console.log("Failed In..");
//            });

            $("#service").on("select2-selecting", function(e) {
                if ($scope.configureGoal.designationNames == null)
                    $scope.configureGoal.designationNames = [];
                var temp = "";
                var services = [];
                if (Object.prototype.toString.call($scope.configureGoal.service) === '[object Array]') {
                    angular.forEach($scope.configureGoal.service, function(item) {
                        services.push(item.id);
                    });
                    temp = services.toString();
                } else {
                    temp = $scope.configureGoal.service;
                }
                if (!!temp) {
                    angular.forEach($scope.allServices, function(item) {
                        angular.forEach(temp.split(","), function(item1) {
                            if (parseInt(item1) == parseInt(item.nodeId) && $scope.configureGoal.designationNames.indexOf(item.designationName) == -1) {
                                $scope.configureGoal.designationNames.push(item.designationName);
                            }
                        });
                    });
                }
                angular.forEach($scope.allServices, function(item1) {
                    if (item1.nodeId == e.val) {
                        if (!!!temp)
                            $scope.configureGoal.designationNames.push(item1.designationName);
                        if (!!!$scope.listForTable) {
                            $scope.listForTable = [];
                        }
                        $scope.listForTable.push({id: e.val, name: e.object.text, type: 'Activity-Service'});
                    }
                });
                $scope.configureGoal.designationNamesInString = $scope.configureGoal.designationNames.toString();
            });
            $("#service1").on("select2-selecting", function(e) {
                if ($scope.configureGoalSheet.designationNames == null)
                    $scope.configureGoalSheet.designationNames = [];
                var temp = "";
                var services = [];
                if (Object.prototype.toString.call($scope.configureGoalSheet.service) === '[object Array]') {
                    angular.forEach($scope.configureGoalSheet.service, function(item) {
                        services.push(item.id);
                    });
                    temp = services.toString();
                } else {
                    temp = $scope.configureGoalSheet.service;
                }
                if (!!temp) {
                    angular.forEach($scope.allServices, function(item) {
                        angular.forEach(temp.split(","), function(item1) {
                            if (parseInt(item1) == parseInt(item.nodeId) && $scope.configureGoalSheet.designationNames.indexOf(item.designationName) == -1) {
                                $scope.configureGoalSheet.designationNames.push(item.designationName);
                            }
                        });
                    });
                }
                angular.forEach($scope.allServices, function(item1) {
                    if (item1.nodeId == e.val) {
                        if (!!!temp)
                            $scope.configureGoalSheet.designationNames.push(item1.designationName);
                        if (!!!$scope.listOfPermissionGoalSheet) {
                            $scope.listOfPermissionGoalSheet = [];
                        }
                        $scope.listOfPermissionGoalSheet.push({id: e.val, name: e.object.text, type: 'Activity-Service'});
                    }
                });
                $scope.configureGoalSheet.designationNamesInStringGoalSheet = $scope.configureGoalSheet.designationNames.toString();
            });
            $("#service").on("select2-removing", function(e) {
                angular.forEach($scope.allServices, function(item1) {
                    if (item1.nodeId == e.val) {
//                        var indexOfDes = $scope.configureGoal.designationNames.indexOf(item1.designationName);
//                        if (indexOfDes > -1) {
//                            $scope.configureGoal.designationNames.splice(indexOfDes, 1);
//                        }
                        var indexes = $.map($scope.listForTable, function(obj, index) {
                            if (obj.id == e.val && obj.name == e.choice.text && obj.type == 'Activity-Service') {
                                return index;
                            }
                        })
                        var index = indexes[0];
                        if (index > -1) {
                            $scope.listForTable.splice(index, 1);
                        }
                    }
                });
                var temp = "";
                var services = [];
                $scope.configureGoal.designationNames = [];
                if (Object.prototype.toString.call($scope.configureGoal.service) === '[object Array]') {
                    angular.forEach($scope.configureGoal.service, function(item) {
                        services.push(item.id);
                    });
                    temp = services.toString();
                } else {
                    temp = $scope.configureGoal.service;
                }
                if (!!temp) {
                    temp = temp.split(",");
                    temp.pop(e.val);
                    temp = temp.toString();
                    angular.forEach($scope.allServices, function(item) {
                        angular.forEach(temp.split(","), function(item1) {
                            if (parseInt(item1) == parseInt(item.nodeId) && $scope.configureGoal.designationNames.indexOf(item.designationName) == -1) {
                                $scope.configureGoal.designationNames.push(item.designationName);
                            }
                        });
                    });
                }
                $scope.configureGoal.designationNamesInString = $scope.configureGoal.designationNames.toString();
            });
            $("#service1").on("select2-removing", function(e) {
                angular.forEach($scope.allServices, function(item1) {
                    if (item1.nodeId == e.val) {
                        var indexes = $.map($scope.listOfPermissionGoalSheet, function(obj, index) {
                            if (obj.id == e.val && obj.name == e.choice.text && obj.type == 'Activity-Service') {
                                return index;
                            }
                        })
                        var index = indexes[0];
                        if (index > -1) {
                            $scope.listOfPermissionGoalSheet.splice(index, 1);
                        }
                    }
                });
                var temp = "";
                var services = [];
                $scope.configureGoalSheet.designationNames = [];
                if (Object.prototype.toString.call($scope.configureGoalSheet.service) === '[object Array]') {
                    angular.forEach($scope.configureGoalSheet.service, function(item) {
                        services.push(item.id);
                    });
                    temp = services.toString();
                } else {
                    temp = $scope.configureGoalSheet.service;
                }
                if (!!temp) {
                    temp = temp.split(",");
                    temp.pop(e.val);
                    temp = temp.toString();
                    angular.forEach($scope.allServices, function(item) {
                        angular.forEach(temp.split(","), function(item1) {
                            if (parseInt(item1) == parseInt(item.nodeId) && $scope.configureGoalSheet.designationNames.indexOf(item.designationName) == -1) {
                                $scope.configureGoalSheet.designationNames.push(item.designationName);
                            }
                        });
                    });
                }
                $scope.configureGoalSheet.designationNamesInStringGoalSheet = $scope.configureGoalSheet.designationNames.toString();
            });
            $("#designation").on("select2-selecting", function(e) {
                if (!!!$scope.listForTable) {
                    $scope.listForTable = [];
                }
                $scope.listForTable.push({id: e.val, name: e.object.text, type: 'Designation'});
            });
            $("#designation").on("select2-removing", function(e) {
                var indexes = $.map($scope.listForTable, function(obj, index) {
                    if (obj.id == e.val && obj.name == e.choice.text && obj.type == 'Designation') {
                        return index;
                    }
                })
                var index = indexes[0];
                if (index > -1) {
                    $scope.listForTable.splice(index, 1);
                }
            });
            $("#designation1").on("select2-selecting", function(e) {
                if (!!!$scope.listOfPermissionGoalSheet) {
                    $scope.listOfPermissionGoalSheet = [];
                }
                $scope.listOfPermissionGoalSheet.push({id: e.val, name: e.object.text, type: 'Designation'});
            });
            $("#designation1").on("select2-removing", function(e) {
                var indexes = $.map($scope.listOfPermissionGoalSheet, function(obj, index) {
                    if (obj.id == e.val && obj.name == e.choice.text && obj.type == 'Designation') {
                        return index;
                    }
                })
                var index = indexes[0];
                if (index > -1) {
                    $scope.listOfPermissionGoalSheet.splice(index, 1);
                }
            });
            $scope.autoCompleteApproverForService = {
                allowClear: true,
                multiple: true,
                closeOnSelect: true,
                placeholder: 'Select service',
                initSelection: function(element, callback) {
                    var tempValues = [];
                    ActivityFlowService.retrieveServicesWithActivity({"q": ""}, function(data) {
                        delete $scope.configureGoal.designationNamesInString;
                        if (!!$scope.configureGoal.service) {
                            var names = [];
                            angular.forEach(data.data, function(designation) {
                                angular.forEach($scope.configureGoal.service, function(item) {
                                    if (parseInt(item) === parseInt(designation.nodeId)) {
                                        tempValues.push({
                                            id: designation.nodeId,
                                            text: designation.associatedServiceName + "(" + designation.activityName + ")"
                                        });
                                        if (names.indexOf(designation.designationName) == -1) {
                                            names.push(designation.designationName);
                                        }

                                    }
                                });
                            });
                            $scope.configureGoal.designationNamesInString = names.toString();
                        }
                        callback(tempValues);
                    }, function() {
                        console.log("Failed In..");
                    });
                },
                query: function(query) {
                    var selected = query.term;
                    var success = function(data) {
                        $scope.names = [];
                        if (data.data.length == 0) {
                            query.callback({
                                results: $scope.names
                            });
                        } else {
                            angular.forEach(data.data, function(item) {
                                $scope.names.push({
                                    id: item.nodeId,
                                    text: item.associatedServiceName + "(" + item.activityName + ")"
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
                        ActivityFlowService.retrieveServicesWithActivity({"q": query.term.trim()}, success, failure);
                    } else {
                        query.callback({
                            results: []
                        });
                    }

                }

            };
            $scope.autoCompleteApproverForServiceGoalSheet = {
                allowClear: true,
                multiple: true,
                closeOnSelect: true,
                placeholder: 'Select service',
                initSelection: function(element, callback) {
                    var tempValues = [];
                    ActivityFlowService.retrieveServicesWithActivity({"q": ""}, function(data) {
                        delete $scope.configureGoalSheet.designationNamesInString;
                        if (!!$scope.configureGoalSheet.service) {
                            var names = [];
                            angular.forEach(data.data, function(designation) {
                                angular.forEach($scope.configureGoalSheet.service, function(item) {
                                    if (parseInt(item) === parseInt(designation.nodeId)) {
                                        tempValues.push({
                                            id: designation.nodeId,
                                            text: designation.associatedServiceName + "(" + designation.activityName + ")"
                                        });
                                        if (names.indexOf(designation.designationName) == -1) {
                                            names.push(designation.designationName);
                                        }

                                    }
                                });
                            });
                            $scope.configureGoalSheet.designationNamesInStringGoalSheet = names.toString();
                        }
                        callback(tempValues);
                    }, function() {
                        console.log("Failed In..");
                    });
                },
                query: function(query) {
                    var selected = query.term;
                    var success = function(data) {
                        $scope.names = [];
                        if (data.data.length == 0) {
                            query.callback({
                                results: $scope.names
                            });
                        } else {
                            angular.forEach(data.data, function(item) {
                                $scope.names.push({
                                    id: item.nodeId,
                                    text: item.associatedServiceName + "(" + item.activityName + ")"
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
                        ActivityFlowService.retrieveServicesWithActivity({"q": query.term.trim()}, success, failure);
                    } else {
                        query.callback({
                            results: []
                        });
                    }

                }

            };
            $scope.saveGoalPermission = function() {
                $scope.configureGoal.submitted = true;
                if ($scope.roleForm.$valid) {
                    var flag = true;
                    if ($scope.configureGoal.type === 3 && $scope.selectedInStringIds == null) {
                        flag = false;
                    }
                    if (flag) {
                        var GoalPermissions = [];
                        var GoalPermission = {
                        };
                        var GoalPermissionForDesignation = {
                        };
                        var GoalPermissionForDepartment = {
                        };
                        if (!!$scope.selectedDesignation.currentNode) {
                            GoalPermission.designation = $scope.selectedDesignation.currentNode.id;
                            GoalPermissionForDesignation.designation = $scope.selectedDesignation.currentNode.id;
                            GoalPermissionForDepartment.designation = $scope.selectedDesignation.currentNode.id;
                        }
                        GoalPermission.referenceType = "S";
                        var services = [];
                        if (Object.prototype.toString.call($scope.configureGoal.service) === '[object Array]') {
                            angular.forEach($scope.configureGoal.service, function(item) {
                                services.push(item.id);
                            });
                            GoalPermission.referenceInstance = services.toString();
                        } else {
                            GoalPermission.referenceInstance = $scope.configureGoal.service;
                        }
                        GoalPermissionForDesignation.referenceType = "R";
                        var designation = [];
                        if (Object.prototype.toString.call($scope.configureGoal.designation) === '[object Array]') {
                            angular.forEach($scope.configureGoal.designation, function(item) {
                                designation.push(item.id);
                            });
                            GoalPermissionForDesignation.referenceInstance = designation.toString();
                        } else {
                            GoalPermissionForDesignation.referenceInstance = $scope.configureGoal.designation;
                        }

                        GoalPermissionForDepartment.referenceType = "D";
                        if (!!$scope.selectedInStringIds)
                            GoalPermissionForDepartment.referenceInstance = $scope.selectedInStringIds.toString();
                        GoalPermissions.push(GoalPermission);
                        GoalPermissions.push(GoalPermissionForDesignation);
                        GoalPermissions.push(GoalPermissionForDepartment);

                        $scope.goalPermissionToConfigure = GoalPermissions;
                        $("#configureNonDiamondPopUp").modal('hide');
                        $rootScope.removeModalOpenCssAfterModalHide();

//                        }
                        for (var i = 0; i < $scope.systemFeaturesList.length; i++) {
                            if ($scope.systemFeaturesList[i].id === $scope.diamondGoalFeature.id && $scope.systemFeaturesList[i].checked) {
                                $scope.systemFeaturesList[i].configure = true;
                            }
                        }
                    }
                }
            };
            $scope.saveGoalSheetPermission = function() {
                $scope.configureGoalSheet.submitted = true;
                if ($scope.goalSheetForm.$valid) {
                    var flag = true;
                    if ($scope.configureGoalSheet.type === 3 && $scope.selectedInStringIdsGoalSheet == null) {
                        flag = false;
                    }
                    if (flag) {
                        var GoalPermissions = [];
                        var GoalPermission = {
                        };
                        var GoalPermissionForDesignation = {
                        };
                        var GoalPermissionForDepartment = {
                        };
                        if (!!$scope.selectedDesignation.currentNode) {
                            GoalPermission.designation = $scope.selectedDesignation.currentNode.id;
                            GoalPermissionForDesignation.designation = $scope.selectedDesignation.currentNode.id;
                            GoalPermissionForDepartment.designation = $scope.selectedDesignation.currentNode.id;
                        }
                        GoalPermission.referenceType = "S";
                        var services = [];
                        if (Object.prototype.toString.call($scope.configureGoalSheet.service) === '[object Array]') {
                            angular.forEach($scope.configureGoalSheet.service, function(item) {
                                services.push(item.id);
                            });
                            GoalPermission.referenceInstance = services.toString();
                        } else {
                            GoalPermission.referenceInstance = $scope.configureGoalSheet.service;
                        }
                        GoalPermissionForDesignation.referenceType = "R";
                        var designation = [];
                        if (Object.prototype.toString.call($scope.configureGoalSheet.designation) === '[object Array]') {
                            angular.forEach($scope.configureGoalSheet.designation, function(item) {
                                designation.push(item.id);
                            });
                            GoalPermissionForDesignation.referenceInstance = designation.toString();
                        } else {
                            GoalPermissionForDesignation.referenceInstance = $scope.configureGoalSheet.designation;
                        }

                        GoalPermissionForDepartment.referenceType = "D";
                        if (!!$scope.selectedInStringIdsGoalSheet)
                            GoalPermissionForDepartment.referenceInstance = $scope.selectedInStringIdsGoalSheet.toString();
                        GoalPermissions.push(GoalPermission);
                        GoalPermissions.push(GoalPermissionForDesignation);
                        GoalPermissions.push(GoalPermissionForDepartment);

                        $scope.goalSheetPermissionToConfigure = GoalPermissions;
                        $("#configureGoalSheetSheetPopUp").modal('hide');
                        $rootScope.removeModalOpenCssAfterModalHide();

                        for (var i = 0; i < $scope.systemFeaturesList.length; i++) {
                            if ($scope.systemFeaturesList[i].id === $scope.diamondGoalFeature.id && $scope.systemFeaturesList[i].checked) {
                                $scope.systemFeaturesList[i].configure = true;
                            }
                        }
                    }
                }
            };
            $scope.cancelGoalConfigurationPopup = function() {
                $("#configureNonDiamondPopUp").modal('hide');
                $rootScope.removeModalOpenCssAfterModalHide();

            };
            $scope.cancelGoalSheetPopup = function() {
                $("#configureGoalSheetSheetPopUp").modal('hide');
                $rootScope.removeModalOpenCssAfterModalHide();

            };
            $scope.deletePermission = function(item) {
                var index = $scope.listForTable.indexOf(item);
                $scope.listForTable.splice(index, 1);
                if (item.type == 'Activity-Service') {
                    var temp;
                    var services = [];
                    if (Object.prototype.toString.call($scope.configureGoal.service) === '[object Array]') {
                        angular.forEach($scope.configureGoal.service, function(item) {
                            services.push(item.id);
                        });
                        temp = services.toString();
                    } else {
                        temp = $scope.configureGoal.service;
                    }
                    var arr = temp.split(",");
                    var index = arr.indexOf(item.id.toString());
                    if (index > -1) {
                        arr.splice(index, 1);
                    }
                    var b = arr.map(function(item) {
                        return parseInt(item, 10);
                    });
                    $scope.configureGoal.service = b;
                    $('#service').select2('val', b);
                } else if (item.type == 'Designation') {
                    var desg = [];
                    var temp;
                    if (Object.prototype.toString.call($scope.configureGoal.designation) === '[object Array]') {
                        angular.forEach($scope.configureGoal.designation, function(item) {
                            desg.push(item.id);
                        });
                        temp = desg.toString();
                    } else {
                        temp = $scope.configureGoal.designation;
                    }
                    var arr = temp.split(",");
                    var index = arr.indexOf(item.id.toString());
                    if (index > -1) {
                        arr.splice(index, 1);
                    }
                    var b = arr.map(function(item) {
                        return parseInt(item, 10);
                    });
                    $scope.configureGoal.designation = b;
                    $('#designation').select2('val', b);
                } else if (item.type == 'Department') {
                    var vals = [];
                    if (!!$scope.selectedInStringIds) {
                        var arr = $scope.selectedInStringIds;
                        var index = arr.indexOf(item.id);
                        if (index > -1) {
                            arr.splice(index, 1);
                        }

                        $scope.selectedInString = [];
                        angular.forEach(arr, function(item) {
                            vals.push(item);
                            $scope.depName = "";
                            $scope.selectedInString.push($scope.getDepName(item, $scope.multiselecttree.items));
                        });
                        $scope.defaultDepartmentIds = vals;
                        $scope.selectedInStringIds = vals;
                    }

                }
            };
            $scope.deletePermissionGoalSheet = function(item) {
                var index = $scope.listOfPermissionGoalSheet.indexOf(item);
                $scope.listOfPermissionGoalSheet.splice(index, 1);
                if (item.type == 'Activity-Service') {
                    var temp;
                    var services = [];
                    if (Object.prototype.toString.call($scope.configureGoalSheet.service) === '[object Array]') {
                        angular.forEach($scope.configureGoalSheet.service, function(item) {
                            services.push(item.id);
                        });
                        temp = services.toString();
                    } else {
                        temp = $scope.configureGoalSheet.service;
                    }
                    var arr = temp.split(",");
                    var index = arr.indexOf(item.id.toString());
                    if (index > -1) {
                        arr.splice(index, 1);
                    }
                    var b = arr.map(function(item) {
                        return parseInt(item, 10);
                    });
                    $scope.configureGoalSheet.service = b;
                    $('#service1').select2('val', b);
                } else if (item.type == 'Designation') {
                    var desg = [];
                    var temp;
                    if (Object.prototype.toString.call($scope.configureGoalSheet.designation) === '[object Array]') {
                        angular.forEach($scope.configureGoalSheet.designation, function(item) {
                            desg.push(item.id);
                        });
                        temp = desg.toString();
                    } else {
                        temp = $scope.configureGoalSheet.designation;
                    }
                    var arr = temp.split(",");
                    var index = arr.indexOf(item.id.toString());
                    if (index > -1) {
                        arr.splice(index, 1);
                    }
                    var b = arr.map(function(item) {
                        return parseInt(item, 10);
                    });
                    $scope.configureGoalSheet.designation = b;
                    $('#designation1').select2('val', b);
                } else if (item.type == 'Department') {
                    var vals = [];
                    if (!!$scope.selectedInStringIdsGoalSheet) {
                        var arr = $scope.selectedInStringIdsGoalSheet;
                        var index = arr.indexOf(item.id);
                        if (index > -1) {
                            arr.splice(index, 1);
                        }

                        $scope.selectedInStringGoalSheet = [];
                        angular.forEach(arr, function(item) {
                            vals.push(item);
                            $scope.depName = "";
                            $scope.selectedInStringGoalSheet.push($scope.getDepName(item, $scope.multiselecttree.items));
                        });
                        $scope.defaultDepartmentIdsGoalSheet = vals;
                        $scope.selectedInStringIdsGoalSheet = vals;
                    }

                }
            };

            //For custom order type to manipulate DOM sorting.
            //Mapped to respective columns. null, if default required.
            $scope.dataTableOptions = {
                "columns": [
                    null,
                    null,
                    {"orderDataType": "dom-text-numeric"}
                ],
                "autoWidth": false
            };
            $scope.retrieveAllDesignations();
//            $scope.retrieveAllSysFeatures();
//            $rootScope.unMaskLoading();
        }]);
});

