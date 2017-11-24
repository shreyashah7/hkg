/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
define(['hkg', 'departmentService', 'addMasterValue', 'dynamicForm'], function (hkg, departmentService) {
    hkg.register.controller('DepartmentController', ["$rootScope", "$scope", "$http", "DepartmentService", "$filter", "DynamicFormService", "$timeout", function ($rootScope, $scope, $http, DepartmentService, $filter, DynamicFormService, $timeout) {
            $scope.searchRecords = [];
            $rootScope.maskLoading();
            $rootScope.mainMenu = "manageLink";
            $rootScope.childMenu = "manageDepartment";
            $rootScope.activateMenu();
            $scope.entity = 'DEPARTMENTS.';
            $scope.hasDepartmentAccess = $rootScope.canAccess('departmentAddEdit');
            $scope.msgForDep = '';
            $scope.dbType = {};
            $scope.$watch('departmentTitle', function(value){
                console.log('watching1....'+value);
            });
            $scope.$watch('departmentTitle', function(value){
                console.log('watching2....'+value);
            });

            $scope.initializePage = function () {
                $('#scrollable-dropdown-menu.typeahead').typeahead('val', '');
                $scope.submitted = false;
                $scope.departmentListOption = [];
                $scope.isEditMode = 'false';
                $scope.showSameDeptIdError = false;
                $scope.statusDepartment = 'true';
                $scope.displaySearchedDepartment = 'view';
                $scope.departmentParent = '';
                $scope.departmentTitle = '';
                $scope.reload = true;
                //for showing this name in popup
                $scope.departmentName = '';
                $scope.departmentDescription = '';
                $scope.shiftRotationDays;
                $scope.parentId = 0;
                $scope.selectedDept;
                $scope.msgForDep = '';
                $scope.addDepartmentData = {};
                $scope.listOfModelsOfDateType = [];
                $scope.selectedParent = {id: '0', displayName: 'None'};
                var templateData = DynamicFormService.retrieveSectionWiseCustomFieldInfo("manageDepartment");
                templateData.then(function (section) {
                    // Method modified by Shifa Salheen on 17 April for implementing sequence number
                    $scope.customTemplateDate = angular.copy(section['genralSection']);
                    $scope.genralDepartmentTemplate = $rootScope.getCustomDataInSequence($scope.customTemplateDate);

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
                }, function (reason) {
                }, function (update) {
                });

                $scope.initAddDepartmentForm = function (addDepartmentForm) {
                    $scope.addDepartmentForm = addDepartmentForm;

                };
                //retrieval is for bringing data in tree view
                $rootScope.maskLoading();
                DepartmentService.retrieveDepartment(function (data) {
                    console.log("data.."+JSON.stringify(data))
                    $scope.departmentList = [];
                    if (data != null && angular.isDefined(data) && data.length > 0) {
                        angular.forEach(data, function (item) {
                            //                            item.displayName = $rootScope.translateValue("DPT_NM." + angular.copy(item.displayName));
                            $scope.departmentList.push(item);
                        });
                    }
                    $scope.setRootOption();
                    $scope.addDepartmentForm.$setPristine();
                    for (var i = 0; i < $scope.departmentList.length; i++) {
                        convertTreeToList($scope.departmentList[i]);
                    }
                    if ($scope.selectedDepartment !== undefined && $scope.selectedDepartment.currentNode !== undefined) {
                        $scope.selectedDepartment.currentNode = undefined;
                    }
                    $scope.addDepartmentForm.$dirty = false;
                    $rootScope.unMaskLoading();

                }, function () {
                    $rootScope.unMaskLoading();
                    var msg = "Failed to retrieve department";
                    var type = $rootScope.error;
                    $rootScope.addMessage(msg, type);

                });


            };
            /* Method added by Shifa Salheen on 2 April 2015 for clearing custom fields like multiselect and usermultiselect
             which were not getting cleared after add operation.Also On reset or cancel custom fields need to be cleared
             And On edit from tree u need to reload the directive*/
            $scope.resetCustomFields = function ()
            {
                $scope.addDepartmentData = {};
                var templateData = DynamicFormService.retrieveSectionWiseCustomFieldInfo("manageDepartment");
                templateData.then(function (section) {

                    $scope.customTemplateDate = angular.copy(section['genralSection']);
                    $scope.genralDepartmentTemplate = $rootScope.getCustomDataInSequence($scope.customTemplateDate);

                }, function (reason) {
                }, function (update) {
                });
            };


            $scope.addDataInDepartment = function () {
                var i = 0;
                $scope.reload = false;
                $('#scrollable-dropdown-menu.typeahead').typeahead('val', '');
                $scope.submitted = false;
                if (angular.isDefined($scope.selectedDepartment.currentNode)) {
                    $scope.selectedDepartment.currentNode.selected = undefined;
                }
                $scope.selectedParent = {id: '0', displayName: 'None'};
                $scope.departmentListOption = [];
                $scope.isEditMode = false;
                $scope.showSameDeptIdError = false;
                $scope.displaySearchedDepartment = 'view';
                $scope.departmentParent = '';
                $scope.departmentTitle = '';
                $scope.departmentDescription = '';
                $scope.shiftRotationDays = null;
                $scope.parentId = 0;
                $scope.selectedDept;
                $timeout(function () {
                    $scope.resetCustomFields();
                    $scope.reload = true;
                }, 50);

                $scope.setRootOption();
                $scope.addDepartmentForm.$setPristine();
                for (var i = 0; i < $scope.departmentList.length; i++) {
                    convertTreeToList($scope.departmentList[i]);
                }

            }

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
            //Set 'None' as an option in dropdown
            $scope.setRootOption = function () {

                $scope.departmentListDropdown = [];
                $scope.departmentListDropdown.push({id: '0', displayName: 'None'});
                $scope.departmentListDropdown.displayName = $rootScope.translateValue("DPT_NM." + $scope.departmentListDropdown.displayName);
                $.merge($scope.departmentListDropdown, angular.copy($scope.departmentList));
            };


            //Called when dropdown is clicked
            $scope.setSelectedParent = function (selected) {
                $scope.$on('$locationChangeStart', function (event) {
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
                    $scope.parentId = selected.currentNode.id;
                }
            };

            //  adding department
            $scope.addDepartment = function () {
                console.log("in add dep")
                $scope.reload = false;
                $scope.submitted = true;
                //check if form is valid
                console.log("add Dep error" + $scope.addDepartmentForm.$valid)
                if ($scope.addDepartmentForm.$valid && angular.isDefined($scope.showSameDeptIdError) && !$scope.showSameDeptIdError) {
                    var deptJsonObj = {
                        displayName: $scope.departmentTitle,
                        parentId: $scope.parentId,
                        description: $scope.departmentDescription,
                        departmentCustom: $scope.addDepartmentData,
                        shiftRotationDays: $scope.shiftRotationDays,
                        dbType: $scope.dbType
                    };
                    $rootScope.maskLoading();
                    DepartmentService.addDepartment(deptJsonObj, function (res) {
                        $rootScope.unMaskLoading();
                        $scope.id = res.primaryKey;
                        if (res.result === true) {
                            var msg = $scope.departmentTitle + " " + "already exists";
                            var type = $rootScope.warning;
                            $rootScope.addMessage(msg, type);
                        }
                        else {
                            $scope.submitted = false;
                            //set to default values
                            $scope.addDepartmentForm.$setPristine();
                            $scope.departmentParent = 0;
                            $scope.departmentTitle = '';
                            $scope.departmentDescription = '';
                            $scope.shiftRotationDays = null;
                            $scope.selectedParent = {id: '0', displayName: 'None'};
                            $scope.departmentListOption = [];
                            $scope.initializePage();
                            $scope.reload = true;


                        }
                    }, function () {
                        $rootScope.unMaskLoading();
                        var msg = " Department could not be added. Try again";
                        var type = $rootScope.error;
                        $rootScope.addMessage(msg, type);
                        $scope.reload = true;

                    });
                } else {
                    $scope.reload = true;
                }
            };


//        Called when tree is clicked
            $scope.editDepartment = function (selectedDept) {
                if(selectedDept.currentNode.isDefaultDep !== null && selectedDept.currentNode.isDefaultDep === true)
                {
                    $scope.isDefaultDep=true;
                    
                }else
                {
                    $scope.isDefaultDep = false;
                }
                $scope.reload = false;
                if (!angular.equals(selectedDept, {})) {
//                $scope.addDepartmentData = DynamicFormService.resetSection($scope.genralDepartmentTemplate);
                    $scope.showSameDeptIdError = false;
                    $scope.addDepartmentForm.$invalid = false;
                    if ($scope.selectedDepartment.currentNode !== undefined) {
                        if ($scope.selectedDepartment.currentNode.id !== $scope.departmentList[0].id) {
                            $scope.departmentList[0].selected = undefined;
                        }
                    }
                    $scope.department = angular.copy($scope.selectedDepartment.currentNode);
                    DepartmentService.addCustomDataToDepartmentDataBean(angular.copy($scope.department), function (data) {
                        $scope.resetCustomFields();
                        $scope.addDepartmentData = angular.copy(data.departmentCustom);
                        if (!!$scope.addDepartmentData) {
                            angular.forEach($scope.listOfModelsOfDateType, function (listOfModel)
                            {
                                if ($scope.addDepartmentData.hasOwnProperty(listOfModel))
                                {
                                    if ($scope.addDepartmentData[listOfModel] !== null && $scope.addDepartmentData[listOfModel] !== undefined)
                                    {
                                        $scope.addDepartmentData[listOfModel] = new Date($scope.addDepartmentData[listOfModel]);
                                    } else
                                    {
                                        $scope.addDepartmentData[listOfModel] = '';
                                    }
                                }
                            })
                        }
                        $scope.reload = true;
//                    $scope.addDepartmentData = DynamicFormService.resetSection($scope.genralDepartmentTemplate);
//                    $scope.addDepartmentData = DynamicFormService.convertToViewOnlyData(data.departmentCustom, $scope.genralDepartmentTemplate);
                    });
                    $scope.isEditMode = true;
                    $('#scrollable-dropdown-menu.typeahead').typeahead('val', '');
                    $scope.displaySearchedDepartment = 'view';
                    $scope.invalidParent = false;
                    if (angular.isDefined($scope.department)) {
                        $scope.selectedParent.id = $scope.department.parentId;
                        $scope.selectedParent.displayName = $scope.department.parentName;
                        if ($scope.selectedParent.displayName.length > 35) {
                            $scope.selectedParent.displayName = $scope.selectedParent.displayName.substring(0, 35);
                        }
                        $scope.departmentParent = $scope.department.parentId;
                        $scope.departmentTitle = $scope.department.deptName;
                        $scope.departmentName = angular.copy($scope.departmentTitle);
                        $scope.parentId = $scope.department.parentId;
                        $scope.departmentDescription = $scope.department.description;
                        $scope.shiftRotationDays = $scope.department.shiftRotationDays;
                        $scope.statusDepartment = $scope.department.isActive.toString();

                    }
                    $scope.departmentListDropdown = [];
                    $scope.departmentListDropdown.push({id: '0', displayName: 'None'});
                    $.merge($scope.departmentListDropdown, angular.copy($scope.departmentList));
                    $scope.newDeptList = angular.copy($scope.departmentListDropdown);
                    $scope.deleteSelectedNode($scope.newDeptList);
                    if (angular.isDefined($scope.addDepartmentForm)) {
                        $scope.addDepartmentForm.$setPristine();
                    }
                }

            };
            $scope.deleteSelectedNode = function (node) {

                if (node === null || node === undefined) {
                    return;
                }

                for (var i = 0; i < node.length; i++) {
                    if (angular.isDefined($scope.selectedDepartment.currentNode) && $scope.selectedDepartment.currentNode.id === node[i].id) {
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

                $scope.departmentListDropdown = angular.copy(node);
            }

            $scope.editDepartmentFromSearchBox = function (data) {
                if (!$scope.hasDepartmentAccess) {
                    alert("You Dont have right to access this feature");
                }
                $scope.retrieveById(data);
                $scope.newDeptList = angular.copy($scope.departmentListDropdown);
                $scope.deleteSelectedNode($scope.newDeptList);
            };

            $scope.retrieveById = function (nodeId) {
                for (var i = 0; i < $scope.departmentListOption.length; i++) {
                    if (nodeId == $scope.departmentListOption[i].id) {
                        $scope.showSameDeptIdError = false;
                        $scope.addDepartmentForm.$invalid = false;
                        $scope.displaySearchedDepartment = 'view';
                        $scope.isEditMode = true;
                        $scope.invalidParent = false;
                        var id = {id: nodeId};
                        DepartmentService.addCustomDataToDepartmentDataBean(id, function (data) {
                            $scope.addDepartmentData = data.departmentCustom;
                            if (!!$scope.addDepartmentData) {
                                angular.forEach($scope.listOfModelsOfDateType, function (listOfModel)
                                {
                                    if ($scope.addDepartmentData.hasOwnProperty(listOfModel))
                                    {
                                        if ($scope.addDepartmentData[listOfModel] !== null && $scope.addDepartmentData[listOfModel] !== undefined)
                                        {
                                            $scope.addDepartmentData[listOfModel] = new Date($scope.addDepartmentData[listOfModel]);
                                        } else
                                        {
                                            $scope.addDepartmentData[listOfModel] = '';
                                        }
                                    }
                                })
                            }

                        });
                        $scope.selectedDepartment.currentNode = angular.copy($scope.departmentListOption[i]);
                        $scope.department = angular.copy($scope.selectedDepartment.currentNode);
                        $scope.selectedParent.id = $scope.department.parentId;
                        $scope.selectedParent.displayName = $scope.department.parentName;
                        $scope.departmentParent = $scope.department.parentId;
                        $scope.departmentTitle = $scope.department.displayName;
                        $scope.departmentName = angular.copy($scope.departmentTitle);
                        $scope.lastSelectedId = angular.copy($scope.parentId);
                        $scope.parentId = $scope.department.parentId;
                        $scope.departmentDescription = $scope.department.description;
                        $scope.shiftRotationDays = $scope.department.shiftRotationDays;
                        $scope.statusDepartment = $scope.department.isActive.toString();
                        $scope.departmentListDropdown = [];
                        $scope.departmentListDropdown.push({id: '0', displayName: 'None'});
                        $.merge($scope.departmentListDropdown, angular.copy($scope.departmentList));
                    }
                }

            }
            //called when cancel is clicked
            $scope.onCancel = function () {
                $scope.reload = false;
                $('#scrollable-dropdown-menu.typeahead').typeahead('val', '');
                $scope.addDepartmentForm.departmentTitle.$invalid = false;
                $scope.submitted = false;
                $scope.showSameDeptIdError = false;
                $scope.searchDepName = null;
                $scope.searchDepName = '';
                $scope.isEditMode = false;
                $scope.invalidParent = false;
                $scope.departmentParent = 0;
                $scope.parentId = 0;
                $scope.departmentTitle = '';
                $scope.departmentDescription = '';
                $scope.shiftRotationDays = null;
                $timeout(function () {
                    $scope.resetCustomFields();
                    $scope.reload = true;
                }, 50);
                $scope.setRootOption();
                $scope.selectedParent = {id: '0', displayName: 'None'};
                $scope.addDepartmentForm.$setPristine();
                if ($scope.selectedDepartment.currentNode !== undefined) {
                    $scope.selectedDepartment.currentNode.selected = undefined;
                }

            };
            $scope.saveDepartment = function () {
                console.log("save " + JSON.stringify($scope.addDepartmentForm.$error));
                $scope.submitted = true;
                if ($scope.addDepartmentForm.$valid) {
                    if ($scope.statusDepartment === 'false') {
                        $('#departmentModal').modal('show');
                    }
                    else {
                        $scope.updateDepartment();
                    }
                    $scope.addDepartmentForm.$setPristine();
                }
            }
            $scope.cancelRemove = function () {
                $scope.statusDepartment = 'true';
                $('#departmentModal').modal('hide');
                $rootScope.removeModalOpenCssAfterModalHide();
                $('#departmentActiveUserModal').modal('hide');
                $rootScope.removeModalOpenCssAfterModalHide();
                $('#depRemoveModal').modal('hide');
                $rootScope.removeModalOpenCssAfterModalHide();


            }
//      To update department
            $scope.updateDepartment = function () {
                $scope.reload = false;
                $scope.submitted = true;
                $scope.shouldUpdate = false;
                if (!$scope.addDepartmentForm.$invalid && angular.isDefined($scope.showSameDeptIdError) && !$scope.showSameDeptIdError) {
                    if ($scope.department.id !== $scope.parentId) {
                        var depId = {
                            primaryKey: $scope.department.id
                        }
                        var newJsonObj = {
                            id: $scope.department.id,
                            displayName: $scope.departmentTitle,
                            parentId: $scope.parentId,
                            description: $scope.departmentDescription,
                            shiftRotationDays: $scope.shiftRotationDays,
                            isActive: $scope.statusDepartment,
                            departmentCustom: $scope.addDepartmentData,
                            dbType: $scope.dbType
                        };
                        $('#departmentModal').modal('hide');
                        $rootScope.removeModalOpenCssAfterModalHide();
                        $('#departmentActiveUserModal').modal('hide');
                        $rootScope.removeModalOpenCssAfterModalHide();

                        if ($scope.statusDepartment === 'false') {
                            $rootScope.maskLoading();
                            DepartmentService.checkIfDepartmentIsPresentInAnyFeature(depId, function (response) {
                                console.log("response..." + JSON.stringify(response));
                                console.log("ddd" + jQuery.isEmptyObject(response.data));
                                if (jQuery.isEmptyObject(response.data))
                                {
                                    console.log("empty then reove   ")
                                    DepartmentService.deleteDepartment(depId, function () {
                                        $scope.initializePage();
                                        $rootScope.unMaskLoading();
                                    }, function () {
                                        var msg = "Department could not be removed. Try again.";
                                        var type = $rootScope.error;
                                        $rootScope.addMessage(msg, type);
                                        $rootScope.unMaskLoading();
                                    });

                                } else
                                {
                                    var totalFeature = ["Shift", "Department Configuration", "Employees", "Designation"];
                                    var depInvolvedInFeature = [];
                                    for (var key in response.data)
                                    {
                                        if (key === 'Shift')
                                        {
                                            depInvolvedInFeature.push(key);
                                        }
                                        else
                                        if (key === 'Department Configuration')
                                        {
                                            depInvolvedInFeature.push(key);
                                        }
                                        else
                                        if (key === 'Employees')
                                        {
                                            depInvolvedInFeature.push(key);
                                        }
                                        else
                                        if (key === 'Designation')
                                        {
                                            depInvolvedInFeature.push(key);
                                        }
                                    }
                                    $scope.depInFeature = depInvolvedInFeature;
                                    var remainingFeature = [];
                                    $.grep(totalFeature, function (total) {
                                        if ($.inArray(total, depInvolvedInFeature) === -1) {
                                            remainingFeature.push(total);
                                        }

                                    });
                                    $scope.remainingFeature = remainingFeature;
                                    if ($scope.remainingFeature === null || $scope.remainingFeature === undefined || $scope.remainingFeature.length === 0) {
                                        $scope.msgForDep = "Could not remove department as this department is associated with " + $scope.depInFeature;

                                    } else {
                                        $scope.msgForDep = "Could not remove department as this department is associated with " + $scope.depInFeature + " , please verify " + $scope.remainingFeature + " also";
                                    }
                                    $('#depRemoveModal').modal('show');
                                    console.log("remaining " + $scope.msgForDep)

                                }

                                $rootScope.unMaskLoading();
                            });

                        }
                        else {
                            DepartmentService.updateDepartment(newJsonObj, function (res) {
                                $rootScope.unMaskLoading();
                                if ($scope.selectedDepartment.currentNode.displayName !== $scope.departmentTitle) {

                                    var msg = $scope.departmentTitle + " " + "already exists";
                                    var type = $rootScope.warning;
//                                $rootScope.addMessage(msg, type);
                                }
                                else {


                                    $scope.submitted = false;
                                    $scope.addDepartmentForm.$setPristine();
                                    $scope.onCancel();
                                }

                                $scope.initializePage();
                                $scope.reload = true;

                            },
                                    function () {
                                        $rootScope.unMaskLoading();
                                        var msg = " Department could not be saved. Try again";
                                        var type = $rootScope.error;
                                        $rootScope.addMessage(msg, type);

                                    });
                        }
                    }
                    else {
                        $scope.invalidParent = true;
                    }
                }
            };
            $scope.checkDepartmentNameExist = function (data) {
                for (var i = 0; i < $scope.departmentListOption.length; i++) {
                    if ($scope.selectedDepartment.currentNode !== undefined && $scope.departmentTitle !== undefined && $scope.selectedDepartment.currentNode.displayName.toUpperCase() === $scope.departmentTitle.toUpperCase()) {
                        $scope.showSameDeptIdError = false;
                        $scope.addDepartmentForm.$invalid = false;
                        if (!$scope.isEditMode) {
                            $scope.showSameDeptIdError = true;
                            $scope.addDepartmentForm.$invalid = true;
                        }
                    }
                    else {
                        if (data !== undefined) {
                            if (data.toUpperCase() === $scope.departmentListOption[i].displayName.toUpperCase()) {
                                $scope.showSameDeptIdError = true;
                                $scope.addDepartmentForm.$invalid = true;

                                break;
                            }
                            else {
                                $scope.showSameDeptIdError = false;
                                $scope.addDepartmentForm.$invalid = false;
                            }

                        }
                    }
                }
            };
            $scope.getSearchedDepartmentRecords = function (list) {

                $scope.searchRecords = [];
                var enteredText = $('#scrollable-dropdown-menu.typeahead').typeahead('val');
                if (enteredText.length > 0) {
                    if (enteredText.length < 3) {
                        $scope.searchRecords = [];
                    } else {

                        if (list != null && angular.isDefined(list) && list.length > 0) {
                            angular.forEach(list, function (item) {
                                item.displayName = $rootScope.translateValue("DPT_NM." + item.displayName);
                                $scope.searchRecords.push(item);
                            });
                        }
                    }
                    $scope.isEditMode = true;
                    $scope.displaySearchedDepartment = 'search';
                    $scope.searchDepName = null;
                    $scope.searchDepName = '';
                }

            };


            $scope.selectedDepartment = {};
            $rootScope.unMaskLoading();
        }]);

});

