/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
define(['hkg', 'accordionCollapse', 'ruleService', 'designationService', 'departmentConfigService', 'ruleTemplate', 'designationTemplate', 'departmentFlowService'], function(hkg) {
    hkg.register.controller('DeptConfigController', ["$rootScope", "$scope", "RuleService", "Designation", "DepartmentConfigService", "DepartmentFlowService", "$filter", "$q", "$location", function($rootScope, $scope, RuleService, Designation, DepartmentConfigService, DepartmentFlowService, $filter, $q, $location) {
            $rootScope.maskLoading();

            $scope.config = {};
            $scope.loadMultiselect = false;
            $rootScope.mainMenu = "manageLink";
            $rootScope.childMenu = "configureDepartment";
            $rootScope.activateMenu();
            $scope.entity = 'CONFIG_DEPT.';

            $scope.convertTreeviewToNormalList = function convertTreeviewToNormalList(treeData) {
                angular.forEach(treeData, function(department) {
                    if (department.children === null || department.children === undefined) {
                        $scope.departmentListToSelect.push(department);
                    } else {
                        var temp = angular.copy(department);
                        temp.children = null;
                        $scope.departmentListToSelect.push(temp);
                        convertTreeviewToNormalList(department.children);
                    }
                });
            };

            $scope.initializePage = function() {
                $rootScope.maskLoading();
                $scope.isDepartmentSelected = false;
                $scope.loadMultiselect = false;
                $scope.selectedDepartment = undefined;
                $scope.config = {};
                $scope.config.flowMode = 'A';
                $scope.config.status = 'A';
                $scope.config.configSubmitted = false;
                $scope.config.designationList = [];
                $scope.associatedDeptListEntity = undefined;
                $scope.config.displaySearchedDepartment = 'view';
                $scope.config.ruleSet = [];

                $scope.mediums = [
                    {id: "Rough", text: "Rough"}, {id: "Lot", text: "Lot"}, {id: "Packet", text: "Packet"}
                ];
                if ($scope.configForm !== undefined && $scope.configForm !== null) {
                    $scope.configForm.$setPristine();
                }

                DepartmentConfigService.retrieveAllDepartments(function(response) {
                    $rootScope.unMaskLoading();
                    $scope.departmentList = response;
                    $scope.departmentListToSelect = [];
                    $scope.convertTreeviewToNormalList(response);
                    $scope.possibleAssociatedDepartments = angular.copy($scope.departmentListToSelect);
//                    console.log('$scope.departmentListToSelect----' + JSON.stringify($scope.departmentListToSelect));
                    $scope.loadMultiselect = true;
//                    setTimeout(function() {
//                        registerMultiSelectCallback();
//                    });
                }, function(error) {
                    $rootScope.unMaskLoading();
                });

                DepartmentFlowService.retrieveDataForProcessFlow(function(res) {
                    $scope.departmentFlowGraph = res.data;
                    //alert($location.path());
                    if($location.path() === '/printflow'){
                        $scope.initDepartmentFlow();
                    }
                    //console.log('success---' + JSON.stringify(res));
                }, function(err) {
                    //console.log('error---' + JSON.stringify(err));
                });

            };

            $(window).resize(function() {
                setTimeout(function() {
                    var canvas = document.getElementById("springydemo");

                    if (canvas !== null && canvas !== undefined) {
                        if (canvas.width !== $('#springydemo').parent().width()) {
                            canvas.width = $('#springydemo').parent().width();
                        }
                    }
                }, 300);
            });

            $scope.initDepartmentFlow = function() {

                $scope.config.displaySearchedDepartment = 'flow';
//                setTimeout(function() {
//                    $scope.showDepartmentFlowPopup();
                    setTimeout(function() {
                        var canvas = document.getElementById("springydemo");
                        canvas.height = 500;
                        //canvas.width = 500;
                        //console.log($('#springydemo').parent().width());
                        canvas.width = $('#springydemo').parent().width();

                        setTimeout(function() {
                            var graph = new Springy.Graph();
                            jQuery(function() {
                                graph.loadJSON($scope.departmentFlowGraph);
                                var springy = window.springy = jQuery('#springydemo').springy({
                                    graph: graph
//                                nodeSelected: function(node) {
//                                    console.log('Node selected: ' + JSON.stringify(node.data));
//                                }
                                });
                            });

                        });
                    }, 400);
//                });

            };

            function setFeatureFieldPermission(destination, origin) {
                if (origin !== undefined && origin !== null && origin.length > 0) {
                    var featureFieldMap = {};
                    angular.forEach(origin, function(origDeg) {
                        var fieldObj = {
                            designation: origDeg.designation,
                            feature: origDeg.feature,
                            searchFlag: origDeg.searchFlag,
                            parentViewFlag: origDeg.parentViewFlag,
                            readonlyFlag: origDeg.readonlyFlag,
                            editableFlag: origDeg.editableFlag,
                            isRequired: origDeg.isRequired,
                            sequenceNo: origDeg.sequenceNo,
                            entity: origDeg.entityName,
                            field: origDeg.fieldId,
                            sectionCode: origDeg.sectionCode
                        };
                        if (featureFieldMap[origDeg.feature] === undefined || featureFieldMap[origDeg.feature] === null) {
                            featureFieldMap[origDeg.feature] = [];
                        }
                        featureFieldMap[origDeg.feature].push(fieldObj);

                    });
                    angular.forEach(featureFieldMap, function(val, key) {
                        angular.forEach(destination.features, function(feature) {
                            //Allow checked by default.
//                            feature.checked = true;
                            if (feature.id == key) {
                                //feature.checked = true;
                                feature.configure = true;
                            }
                        });
                    });
//                    console.log(JSON.stringify(featureFieldMap));
                    destination.featureFieldMap = angular.copy(featureFieldMap);
                }

            }

            function setFeatureModifiers(designation, modifiers) {
                if (modifiers !== null && modifiers !== undefined) {
                    var featureModifierMap = {};
                    angular.forEach(modifiers, function(item) {
                        if (item.designation === designation.id) {
                            featureModifierMap[item.feature] = item;
                        }
                    });
                    return featureModifierMap;
                } else {
                    return {};
                }
            }

            $scope.fillEditMode = function(config) {
                //console.log('config' + JSON.stringify(config));
                if (config !== null && config !== undefined) {

                    $scope.loadDefaultRuleTemplate = false;
                    $scope.loadMultiselect = false;


                    if (config.ruleList !== null && config.ruleList !== undefined) {
                        $scope.config.ruleSet = config.ruleList;
                    }
                    $scope.config.configId = config.configId;
                    $scope.config.assocStckDept = config.stockRoom;
                    $scope.config.flowMode = config.flowMode;
                    $scope.config.status = config.status;
                    $scope.config.noPhysicalDiamonds = config.noPhysicalDiamonds;

                    //Associated departments
                    if (config.associatedDepartments !== null && config.associatedDepartments.length > 0) {
                        var depts = null;
                        $scope.associatedDeptListEntity = config.associatedDepartments;
                        angular.forEach(config.associatedDepartments, function(associatedDept) {
                            if (depts === null) {
                                depts = associatedDept.department + '';
                            } else {
                                depts += ',' + associatedDept.department;
                            }
                        });
                        $scope.config.associatedDepartments = depts;
                    }

                    //Designation
                    if ($scope.config.designationList.length > 0 && config.associatedDesignations !== null && config.associatedDesignations.length > 0) {
                        for (var j = 0; j < $scope.config.designationList.length; j++) {
                            var designation = $scope.config.designationList[j];
                            for (var i = 0; i < config.associatedDesignations.length; i++) {
                                if (config.associatedDesignations[i].designation === designation.id) {
                                    designation.level = config.associatedDesignations[i].level;
                                    
                                    setFeatureFieldPermission(designation, config.associatedDesignations[i].featureFieldPermissionDataBeans);
                                    break;
                                }
                            }
                            var featureModifierMap = setFeatureModifiers(designation, config.roleFeatureModifiers);
                            designation.featureModifierMap = featureModifierMap;
                        }
                    }

                    setTimeout(function() {
                        $scope.loadMultiselect = true;
                    });

//                    console.log('config' + JSON.stringify($scope.config));
                }
            };

            //Define lavel depending on parent (Designation)
            function findDesignationLevel(designationParentId, designationIds, level, desigIdParentMap) {
                if (level === undefined || level === null) {
                    level = 1;
                }
                if (designationIds.indexOf(designationParentId) > -1) {
                    level = findDesignationLevel(desigIdParentMap[designationParentId], designationIds, level + 1, desigIdParentMap);
                }
                return level;
            }

            $scope.editDepartment = function(selectedDepartment) {
                if (selectedDepartment.currentNode === null || selectedDepartment.currentNode === undefined)
                    return;
                if ($scope.selectedDepartment !== undefined && $scope.selectedDepartment.id === selectedDepartment.currentNode.id)
                    return;
                $scope.isDepartmentSelected = true;
                $scope.selectedDepartment = selectedDepartment.currentNode;
                $scope.config = {};
                $scope.config.flowMode = 'A';
                $scope.config.status = 'A';
                $scope.config.configSubmitted = false;
                $scope.config.designationList = [];
                $scope.associatedDeptList = [];
                $scope.associatedDeptListEntity = undefined;
                $scope.config.ruleSet = [];
                $scope.config.associatedDepartments = '';
                $('#scrollable-dropdown-menu.typeahead').typeahead('val', '');
                $scope.config.displaySearchedDepartment = 'view';
                $scope.customLabel = '';
                var promise = $scope.retrieveAllSysFeatures();
                promise.then(function() {
                    $rootScope.maskLoading();
                    DepartmentConfigService.retrieveDesignationByDept($scope.selectedDepartment.id, function(response) {
                        $rootScope.unMaskLoading();
                        //console.log('designation retrieved' + JSON.stringify(response));
                        var desgIds = [];
                        var designationIdParentMap = {};
                        if (response !== undefined && response !== null) {
                            angular.forEach(response, function(item) {
                                desgIds.push(parseInt(item.value));
                                designationIdParentMap[item.value] = item.otherId;
                            });
                        }
                        //console.log(desgIds);
                        Designation.retrieveFeaturesSelectedForDesignation(desgIds, function(responseData)
                        {
//                            console.log("response data..." + JSON.stringify(responseData));
                            if (responseData.data !== null && responseData.data !== undefined) {
                            } else {
                                responseData.data = {};
                            }

                            angular.forEach(response, function (item) {
                                var v = findDesignationLevel(item.otherId, desgIds, null, designationIdParentMap);
                                //console.log(v + JSON.stringify(item));
                                var designation = {id: item.value, name: item.label, level: v, features: responseData.data[item.value], featureFieldMap: {}, featureModifierMap: {}};
                                angular.forEach(designation.features, function (feature) {
                                    //Allow checked by default.
                                    feature.checked = true;
                                });
                                $scope.config.designationList.push(designation);
                            });
                            

                            //Code to retrieve existing configuration
                            DepartmentConfigService.retrieveConfigDetailByDepId($scope.selectedDepartment.id, function(response1) {
//                                console.log("asdas" + JSON.stringify(response1));
                                $scope.fillEditMode(response1.data);

                            }, function(error) {
                                $rootScope.addMessage("Some error occured, please try again", $rootScope.failure);
//                                console.log('error-----------' + JSON.stringify(error));
                            });
                        })




                    }, function(error) {
                        $rootScope.unMaskLoading();
                    });
                });
                //update associated department list
                $scope.possibleAssociatedDepartments = [];
                angular.forEach($scope.departmentListToSelect, function(dep) {
                    if (dep.id !== $scope.selectedDepartment.id) {
                        $scope.possibleAssociatedDepartments.push(dep);
                    }
                });
                //console.log(JSON.stringify(selectedDepartment));
            };


            $scope.autoCompleteAssociteDept = {
                allowClear: true,
                multiple: true,
                placeholder: 'Select associated department',
                initSelection: function(element, callback) {
                    if ($scope.config.associatedDepartments !== undefined && $scope.config.associatedDepartments !== null && $scope.config.associatedDepartments.length > 0) {
                        if ($scope.config.associatedDepartments instanceof Object && angular.isArray($scope.config.associatedDepartments)) {

                        } else {
                            var data = [];
                            var values = $scope.config.associatedDepartments.split(',');
                            angular.forEach(values, function(val) {
                                angular.forEach($scope.possibleAssociatedDepartments, function(item) {
                                    if (item.id == val) {
                                        data.push({id: item.id, text: item.displayName});
                                    }
                                });
                            });
//                            console.log('data----' + JSON.stringify(data));
                            callback(data);
                        }
                    }
                },
                query: function(query) {

                    $scope.names = [];

                    angular.forEach($scope.possibleAssociatedDepartments, function(item) {
                        if (query.term !== null && query.term !== "" && item.displayName.toLowerCase().indexOf(query.term.toLowerCase().trim()) !== -1) {
                            $scope.names.push({
                                id: item.id,
                                text: item.displayName
                            });
                        }
                    });
                    query.callback({
                        results: $scope.names
                    });
                }
            };
            //Not in use
            function registerMultiSelectCallback() {
                $("#associatedDept").on("select2-selecting", function(e) {
                    angular.forEach($scope.possibleAssociatedDepartments, function(item1) {
                        if (item1.id === e.val) {

                            if ($scope.associatedDeptList === null || $scope.associatedDeptList === undefined) {
                                $scope.associatedDeptList = [];
                            }
                            $scope.associatedDeptList.push({id: e.val, name: e.object.text});
                        }
                    });
                });

                $("#associatedDept").on("select2-removing", function(e) {
                    angular.forEach($scope.possibleAssociatedDepartments, function(item1) {
                        if (item1.id === e.val) {
                            var indexes = $.map($scope.associatedDeptList, function(obj, index) {
                                if (obj.id === e.val && obj.name === e.choice.text) {
                                    return index;
                                }
                            })
                            var index = indexes[0];
                            if (index > -1) {
                                $scope.associatedDeptList.splice(index, 1);
                            }
                        }
                    });
                });
            }

            $scope.$watch('config.associatedDepartments', function(value) {
                if (value !== undefined && value !== null) {
                    if (value instanceof Object && angular.isArray(value)) {
                        var stringVal = '';
                        angular.forEach(value, function(item) {
                            stringVal += item.id + ",";
                        });
                        stringVal = stringVal.substring(0, stringVal.length - 1);
                        $scope.config.associatedDepartments = stringVal;
                    } else {
                        if (value.length > 0) {
                            var values1 = value.split(',');
                            var values = [];
                            angular.forEach(values1, function(val) {
                                values.push(parseInt(val));
                            });
                            var alreadyExistingIds = [];
                            for (var i = $scope.associatedDeptList.length - 1; i >= 0; i--) {
                                if (values.indexOf($scope.associatedDeptList[i].id) === -1) {
                                    $scope.associatedDeptList.splice(i, 1);
                                } else {
                                    alreadyExistingIds.push($scope.associatedDeptList[i].id);
                                }
                            }
                            angular.forEach(values, function(item) {
                                if (alreadyExistingIds.indexOf(item) === -1) {
                                    angular.forEach($scope.possibleAssociatedDepartments, function(item1) {
                                        if (item === item1.id) {
                                            var entityDept = null;
                                            if ($scope.associatedDeptListEntity !== undefined) {
                                                for (var j = 0; j < $scope.associatedDeptListEntity.length; j++) {
                                                    if ($scope.associatedDeptListEntity[j].department === item) {
                                                        entityDept = $scope.associatedDeptListEntity[j];
                                                        break;
                                                    }
                                                }
                                            }
                                            if (entityDept === null) {
                                                $scope.associatedDeptList.push({id: item1.id, name: item1.displayName, ruleSet: []});
                                            } else {
                                                //For edit mode...
                                                $scope.associatedDeptList.push({
                                                    id: item1.id,
                                                    name: item1.displayName,
                                                    ruleSet: entityDept.rules === null ? [] : entityDept.rules,
                                                    isDefault: entityDept.isDefaultDept === null ? undefined : entityDept.isDefaultDept,
                                                    medium: entityDept.medium
                                                });
                                            }
                                        }
                                    });
                                }
                            });
//                            console.log(JSON.stringify($scope.associatedDeptList));
                        } else {
                            $scope.associatedDeptList = [];
                        }
                    }
                } else {
                    $scope.associatedDeptList = [];
                }
            });

            $scope.hideshow = function(which) {
                if (!document.getElementById)
                    return
                var id = document.getElementById(which);
                //console.log(id);
                if (id.style.display === "block")
                    id.style.display = "none"
                else
                    id.style.display = "block"
            };


            /** Designation part starts **/

            $scope.retrieveAllSysFeatures = function() {
                var deferred = $q.defer();
                var promise = deferred.promise;
                $rootScope.maskLoading();
                Designation.retrieveSystemFeatures(function(data) {
                    $rootScope.unMaskLoading();
                    $scope.systemFeaturesList = data;
                    if (!$scope.systemFeaturesList || $scope.systemFeaturesList === null) {
                        $scope.systemFeaturesList = [];
                    } else {
                        $scope.tempSystemFeatureList = [];
                        $scope.diamondSystemFeatureList = [];
                        $scope.reportSystemFeatureList = [];
                        angular.forEach($scope.systemFeaturesList, function(item) {
                            if (item.type === "DMI" || item.type === "DEI") {
                                $scope.diamondSystemFeatureList.push(item);
                            } else if (item.type === "RMI") {
                                $scope.reportSystemFeatureList.push(item);
                            } else {
                                $scope.tempSystemFeatureList.push(item);
                            }
                        });
                        $scope.systemFeaturesList = [];
                        $scope.systemFeaturesList = angular.copy($scope.tempSystemFeatureList);
                    }
                    deferred.resolve();
                }, function() {

                    $rootScope.unMaskLoading();
                    deferred.reject();
                });
                return promise;
            };

            /** Designation part ends **/

            $scope.$watch('showRightColumn', function(value) {
                if (value === true) {
                    $("#right-column").stop().slideDown(700);
                    $("#left-column").removeClass('col-xs-12');
                    $("#left-column").addClass('col-xs-9');
                } else {
                    $("#right-column").stop().slideUp(500).hide();
                    $("#left-column").removeClass('col-xs-9');
                    $("#left-column").addClass('col-xs-12');
                }
            });

            $scope.checkDefaultDepartment = function(index, isDefault) {
                if (index !== undefined && isDefault === true) {
                    angular.forEach($scope.associatedDeptList, function(dept) {
                        dept.isDefault = false;
                    });
                    $scope.associatedDeptList[index].isDefault = true;
                }
            };

            //Default department warning code
            $scope.scrollToElement = function(id) {
                var container = $('html,body'), scrollTo = $('#' + id), header = $('#header');

//                container.scrollTop(scrollTo.offset().top - container.offset().top + container.scrollTop());

                // Or you can animate the scrolling:
                if (scrollTo) {
                    container.animate({
                        scrollTop: scrollTo.offset().top - container.offset().top + container.scrollTop() - header.height()
                    });
                }
            };

            $scope.cancelDefaultWaringPopup = function() {
                $('#defaultWarningPopup').modal('hide');
                $rootScope.removeModalOpenCssAfterModalHide();
            };

            $scope.cancelDeactivatePopup = function(deactivate) {
                $('#deactivatePopup').modal('hide');
                $rootScope.removeModalOpenCssAfterModalHide();
                if (deactivate === true) {
                } else {
                    $scope.config.status = 'A';
                }
            };

            $scope.hideDepartmentFlowPopup = function() {
                $('#departmentFlowPopup').modal('hide');
                $rootScope.removeModalOpenCssAfterModalHide();
                setTimeout(function() {
                    $scope.config.displaySearchedDepartment = 'view';
                });
            };
            $scope.showDepartmentFlowPopup = function() {
                $('#departmentFlowPopup').modal('show');
            };

            $('#departmentFlowPopup').on('hidden.bs.modal', function(e) {
                $scope.config.displaySearchedDepartment = 'view';
            });

            $scope.checkStatusOfConfig = function() {
                if ($scope.config.status === 'I') {
                    $('#deactivatePopup').modal('show');
                }
            };

            $scope.checkValidationForm = function(form) {
                var isValid = true;
                angular.forEach(form.$error, function(value) {
                    angular.forEach(value, function(val) {
                        if (val.$name !== 'addRuleForm' && val.$name !== 'configDesignationForm') {
                            isValid = false;
                        }
                    });
                });
                return isValid;
            };


            $scope.checkParameters = function(form) {
                $scope.config.configSubmitted = true;
//                console.log(JSON.stringify(form));
                var isValid = true;

                if (form.$valid) {
                    isValid = true;
                } else {
                    isValid = $scope.checkValidationForm(form);
                }
//                console.log(isValid);
                if (isValid) {
                    var defaultDept = null;
                    if ($scope.associatedDeptList.length > 0) {
                        for (var i = 0; i < $scope.associatedDeptList.length; i++) {
                            if ($scope.associatedDeptList[i].isDefault === true) {
                                defaultDept = $scope.associatedDeptList[i].id;
                                break;
                            }
                        }
                    }
                    if (defaultDept === null && $scope.associatedDeptList.length > 0) {
                        $('#defaultWarningPopup').modal('show');
                    } else {
                        $scope.prepareModelDataAndSave(defaultDept);
                    }
                } else {
                    var v = $("input.ng-invalid, select.ng-invalid, textarea.ng-invalid");
                    var j;
                    for (j = 0; j < v.length; ++j) {
                        //Discard rule-template invalid fields.
                        if (v.eq(j).parents('.rule-template').length === 0) {
                            v.eq(j).focus();
                            break;
                        }
                    }
                }
            };


            $scope.prepareModelDataAndSave = function(defaultDept) {

                var associatedDepartments = [];
                if ($scope.associatedDeptList.length > 0) {
                    angular.forEach($scope.associatedDeptList, function(associatedDept) {
                        var deptObj = {
                            department: associatedDept.id,
                            medium: associatedDept.medium,
                            isDefaultDept: associatedDept.isDefault,
                            rules: associatedDept.ruleSet
                        };
                        associatedDepartments.push(deptObj);
                    });
                }

                var associatedDesignations = [];
                var roleFeatureModifiers = [];
                if ($scope.config.designationList.length > 0) {
                    angular.forEach($scope.config.designationList, function(designation) {
                        var designationId = designation.id;
                        var featureFieldPermissionDataBeans = [];

                        if (Object.keys(designation.featureFieldMap).length > 0) {
                            angular.forEach(designation.featureFieldMap, function(fieldList, featureId) {
                                if (fieldList.length > 0) {
                                    angular.forEach(fieldList, function(field) {
                                        var fieldId = field.field;
                                        var fieldObj = {
                                            id: designationId + '-' + featureId + '-' + field.sectionCode + '-' + fieldId,
                                            designation: designationId,
                                            feature: featureId,
                                            searchFlag: field.searchFlag,
                                            parentViewFlag: field.parentViewFlag,
                                            readonlyFlag: field.readonlyFlag,
                                            editableFlag: field.editableFlag,
                                            isRequired: field.isRequired,
                                            sequenceNo: field.sequenceNo,
                                            entityName: field.entity,
                                            fieldId: fieldId,
                                            sectionCode: field.sectionCode
                                        };
                                        featureFieldPermissionDataBeans.push(fieldObj);
                                    });
                                }
                            });
                        }
                        if (Object.keys(designation.featureModifierMap).length > 0) {
                            angular.forEach(designation.featureModifierMap, function(modifier, featureId) {
                                modifier.designation = designationId;
                                modifier.feature = featureId;
                                roleFeatureModifiers.push(modifier);

                            });
                        }
                        var designationObj = {
                            designation: designation.id,
                            level: designation.level,
                            skipAssociatedDepartment: [],
                            featureFieldPermissionDataBeans: featureFieldPermissionDataBeans
                        };
                        associatedDesignations.push(designationObj);
                    });
                }

                $scope.configToSend = {
                    //Obj id
                    configId: $scope.config.configId,
                    department: $scope.selectedDepartment.id,
                    departmentName: $scope.selectedDepartment.displayName,
                    stockRoom: $scope.config.assocStckDept,
                    defaultDepartment: defaultDept,
                    flowMode: $scope.config.flowMode,
                    status: $scope.config.status,
                    noPhysicalDiamonds: $scope.config.noPhysicalDiamonds,
                    ruleList: $scope.config.ruleSet,
                    associatedDepartments: associatedDepartments,
                    associatedDesignations: associatedDesignations,
                    roleFeatureModifiers: roleFeatureModifiers
                };

//                console.log(JSON.stringify($scope.configToSend));
                $rootScope.maskLoading();
                DepartmentConfigService.updateDepartmentConfig($scope.configToSend, function(response) {
                    $rootScope.unMaskLoading();
                    //console.log('success-----------');
                    $scope.initializePage();
                }, function(error) {
                    $rootScope.unMaskLoading();
                    //console.log('failed-----------');
                });
            };

            $scope.getSearchedDepartmentRecords = function(list) {

                $scope.searchRecords = [];
                var enteredText = $('#scrollable-dropdown-menu.typeahead').typeahead('val');
                if (enteredText.length > 0) {
                    if (enteredText.length < 3) {
                        $scope.searchRecords = [];
                    } else {

                        if (list != null && angular.isDefined(list) && list.length > 0) {
                            angular.forEach(list, function(item) {
                                item.displayName = $rootScope.translateValue("DPT_NM." + item.displayName);
                                $scope.searchRecords.push(item);
                            });
                        }
                    }
                    $scope.config.displaySearchedDepartment = 'search';
                    $scope.selectedDepartment = undefined;
                    $scope.customLabel = '';
                    if ($scope.selectedDept.currentNode !== undefined) {
                        $scope.selectedDept.currentNode.selected = undefined;
                    }
                }

            };

            $scope.editDepartmentFromSearchBox = function(data) {
                //console.log(JSON.stringify(data));
                if (data !== undefined && data !== null) {
                    $scope.selectedDepartment = undefined;
                    var department = null;
                    angular.forEach($scope.departmentListToSelect, function(dep) {
                        if (dep.id === data) {
                            department = dep;
                        }
                    });
                    $scope.config.displaySearchedDepartment = 'view';
                    if ($scope.selectedDept.currentNode !== undefined) {
                        $scope.selectedDept.currentNode.selected = undefined;
                    }
                    if (department !== null) {
                        $scope.editDepartment({currentNode: department});
                    }
                }
//                if (!$scope.hasDepartmentAccess) {
//                    alert("You Dont have right to access this feature");
//                }
//                $scope.retrieveById(data);
//                $scope.newDeptList = angular.copy($scope.departmentListDropdown);
//                $scope.deleteSelectedNode($scope.newDeptList);
            };

            $scope.initializePage();

//            $rootScope.unMaskLoading();
        }]);
});

