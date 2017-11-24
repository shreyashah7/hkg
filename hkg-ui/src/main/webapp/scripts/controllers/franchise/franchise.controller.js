define(['hkg', 'franchiseService', 'addMasterValue', 'dynamicForm', 'whitespace-remove.directive'], function(hkg, franchiseService) {
    hkg.register.controller('FranchiseController', ["$rootScope", "$scope", "FranchiseService", "DynamicFormService", "$timeout", function($rootScope, $scope, FranchiseService, DynamicFormService, $timeout) {
            $rootScope.maskLoading();
            $scope.searchRecords = [];
            $rootScope.mainMenu = "manageLink";
            $rootScope.childMenu = "manageFranchise";
            $rootScope.activateMenu();
            $scope.allLocations = [];
            $scope.select2Locations = [];
            $scope.entity = "FRANCHISE.";
            $scope.franchiseModelList = [];
            $scope.isCreate = true;
            $scope.reload = true;
            $scope.$on('$viewContentLoaded', function() {
                $scope.initializtion();
                retrieveAccessRightsForFranchise();
                retrieveLocations();
            });

            $scope.initCreateForm = function(franchiseCreateForm) {
                $scope.franchiseCreateForm = franchiseCreateForm;
            };
            /* Method added by Shifa Salheen on 2 April 2015 for clearing custom fields like multiselect and usermultiselect
             which were not getting cleared after add operation.Also On reset or cancel custom fields need to be cleared
             And On edit from tree u need to reload the directive*/
            $scope.resetCustomFields = function()
            {
                $scope.addFranchiseData = {};
                var templateData = DynamicFormService.retrieveSectionWiseCustomFieldInfo("manageFranchise");
                $scope.dbType = {};
                templateData.then(function(section) {
                    $scope.generalFranchiseTemplate = section['genralSection'];


                }, function(reason) {

                }, function(update) {

                });
            };
            function retrieveAccessRightsForFranchise()
            {
                $scope.isAccessCreateOrUpdate = true;
                if (!$rootScope.canAccess('franchiseAddEdit'))
                {
                    $scope.isAccessCreateOrUpdate = false;
                }
                $scope.franchiseActivateDeactivate = true;

                if (!$rootScope.canAccess('franchiseActivateDeactivate'))
                {
                    $scope.franchiseActivateDeactivate = false;
                }
            }
            $scope.initializtion = function() {
                $scope.reload = false;
                $scope.selectedTab = 1;
                $scope.franchise = {};
                $scope.editFranchise = {};
                $scope.submitted = false;
                $scope.isCreate = true;
                $scope.isMatched = false;
                $scope.isuserMatched = true;
                $scope.isDesignationSelected = false;
                $scope.isEmailValidate = true;
                $scope.isPhoneValidate = true;
                $scope.retrieveAllFranchise();
                $scope.retrieveLocations();
                $scope.designations = [];
                $scope.machines = [];
                $scope.editdesignations = [];
                $scope.editmachines = [];
                $scope.resetSelection();
                $scope.notUniqueFranchise = false;
                $scope.notUniqueUsername = false;
                $scope.searchtext = '';
                $scope.isInValidSearch = false;
                $scope.displayFlag = 'view';
                $scope.showTab = true;
                $scope.searchFranchiseId = undefined;
                $scope.canActivated = false;
                $timeout(function() {
                    $scope.resetCustomFields();
                    $scope.reload = true;
                }, 50);

//               
                $("#country").select2("data", "");
//                $scope.addFranchiseData = {};

                var templateData = DynamicFormService.retrieveSectionWiseCustomFieldInfo("manageFranchise");
                $scope.dbType = {};
                templateData.then(function(section) {
                    $scope.generalFranchiseTemplate = section['genralSection'];
                    $scope.addFranchiseData = DynamicFormService.resetSection($scope.generalFranchiseTemplate);

                }, function(reason) {

                }, function(update) {

                });

            };
            $scope.multiLocations = {
                multiple: false,
                closeOnSelect: false,
                placeholder: "Select District",
                allowClear: true,
                initSelection: function(element, callback) {
//                    if ($scope.addAssetForm.recipients123.$dirty) {
//                        $scope.addAssetForm.recipients123.$dirty = false
//                    }
                    if (!$scope.isCreate) {
                        var data = {};
                        data = {
                            id: $scope.editFranchise.location.id,
                            text: $scope.editFranchise.location.text
                        };

                        callback(data);
                    }
                },
                data: function() {
                    return {'results': $scope.select2Locations};
                }
            };
            function retrieveLocations() {
                var locationType = 'T';
                FranchiseService.retrieveLocationsByType({type: locationType}, function(l) {
                    $scope.allLocations = l.sort($scope.predicateBy("label"));
                    angular.forEach($scope.allLocations, function(locationData) {
                        $scope.select2Locations.push({id: locationData.value, text: locationData.label});
                    });
                    if ($scope.allLocations.length == 0) {
                        $rootScope.addMessage("Please enter the locations to create franchise", 1);
                    }
                    $("#location").select2('val', null);
                });
            }
            $scope.openPage = function(operation) {
                $scope.selectedTab = 1;
                if (operation == 'C') {
                    $scope.isCreate = true;
                    $scope.searchtext = undefined;
                    $scope.resetSelection();
                    $scope.designations = [];
                    $scope.machines = [];
//                    $scope.addFranchiseData = {};
                    $scope.addFranchiseData = DynamicFormService.resetSection($scope.generalFranchiseTemplate);
                } else {
                    $scope.isCreate = false;
                }
                $scope.displayFlag = 'view';
                $scope.retrievePreData();
            };
            $scope.retrievePreData = function() {
                $scope.submitted = false;
                $scope.franchise = {};
            };
            $scope.resetSelection = function() {
                if ($scope.selectedFranchiseTree != undefined) {
                    $scope.selectedFranchiseTree.selected = undefined;
                }
                if ($scope.selectedFranchiseTree != undefined && $scope.selectedFranchiseTree.currentNode != undefined) {
                    $scope.selectedFranchiseTree.currentNode.selected = undefined;
                    $scope.selectedFranchiseTree.currentNode = undefined;
                }
            };
            $scope.isValidFormat = function(name) {
                if (angular.isDefined(name)) {
                    $scope.invalidname = true;
                    var split = name.trim().split(" ");
                    $scope.firstName = split[0];
                    $scope.middleName = split[1];
                    $scope.lastName = split[2];
                    if (split.length == 3) {
                        $scope.invalidname = false;
                    } else {
                        $scope.invalidname = true;
                    }
                }
            }
            $scope.createFranchise = function(franchiseForm) {
                $scope.relaod = false;

//                console.log('create'+JSON.stringify(franchiseForm));
//                 $scope.frnachiseNameDetails={ "franchiseName":$scope.franchise.franchiseName};
//                $scope.doesFranchiseNameExist( $scope.frnachiseNameDetails);
                $scope.submitted = true;
                $scope.isDesignationSelected = true;
                if ($scope.designations === null || $scope.designations.length === 0) {
                    $scope.isDesignationSelected = false;
                } else {
                    for (var i = 0; i < $scope.designations.length; i++) {
                        var temp = $scope.designations[i];
                        if (angular.isDefined(temp.requiredValue) && temp.requiredValue !== null && temp.requiredValue.toString().length > 0) {
                            $scope.isDesignationSelected = false;
                            break;
                        }
                    }
                }
                $scope.isValidFormat($scope.franchise.adminName);
                if ($scope.isDesignationSelected)
                {

                    $scope.selectedTab = 2;
                }
                if (franchiseForm.$valid && !$scope.invalidname && $scope.isEmailValidate && $scope.isPhoneValidate && !$scope.notUniqueFranchise) {
                    if ($scope.existingFranchiseList.length === 0) {
                        $scope.isDesignationSelected = false;
                    }
                    if (!$scope.isDesignationSelected) {
                        $rootScope.maskLoading();
                        var createFranchise = angular.copy($scope.franchise);
                        createFranchise.firstName = $scope.firstName;
                        createFranchise.middleName = $scope.middleName;
                        createFranchise.lastName = $scope.lastName;
                        var locationArray = createFranchise.location.toString().split('#');
                        if (locationArray.length === 3) {
                            createFranchise.city = locationArray[0];
                            createFranchise.state = locationArray[1];
                            createFranchise.country = locationArray[2];
                        } else if (locationArray.length === 4) {
                            createFranchise.city = locationArray[0];
                            createFranchise.district = locationArray[1];
                            createFranchise.state = locationArray[2];
                            createFranchise.country = locationArray[3];
                        }
                        createFranchise.adminRetypePwd = undefined;
                        var finalDeg = angular.copy($scope.designations);
                        angular.forEach($scope.machines, function(machine) {
                            finalDeg.push(machine);
                        });
                        createFranchise.franchiseMinReq = angular.copy(finalDeg);
                        createFranchise.franchiseCustom = $scope.addFranchiseData;
                        createFranchise.franchiseDbType = $scope.dbType;
                        createFranchise.firstFranchise = !($scope.existingFranchiseList.length > 0);
                        createFranchise.adminName = undefined;

                        var success = function(res) {
                            $scope.resetCustomFields();
                            $scope.reload = true;
                            $scope.selectedTab = 1;
                            franchiseForm.$setPristine();
                            $scope.initializtion();
                            $scope.searchtext = undefined;
                            $rootScope.unMaskLoading();
                        };
                        var failure = function() {
                            $scope.resetCustomFields();
                            $scope.reload = true;
                            $rootScope.addMessage("Could not save details, please try again.", 1);
                            $rootScope.unMaskLoading();
                        };
                        FranchiseService.createFranchise(createFranchise, success, failure);

                    }
                } else if (!franchiseForm.$valid) {
                    for (var key in franchiseForm) {
                        if (key.indexOf("$") !== 0) {
                            if (franchiseForm[key].$invalid) {
                                $scope.scrollTo(key.toString());
                                break;
                            }
                        }
                    }
                }
            };
            $scope.changeSelectedTab = function(id)
            {
                $scope.selectedTab = id;
            }
            $scope.confirmPasswordValidation = function() {
                if (!angular.equals($scope.franchise.password, $scope.franchise.adminRetypePwd)) {
                    $scope.isMatched = true;
                }
                else {
                    $scope.isMatched = false;
                }
            };
            $scope.userNameValidationCheck = function() {
                if ($scope.franchise.adminUserName && $scope.franchise.password)
                {
                    if (angular.equals($scope.franchise.adminUserName, $scope.franchise.password)) {
                        $scope.isuserMatched = false;
                    }
                    else {
                        $scope.isuserMatched = true;
                    }
                }
            };
            $scope.cancelFranchise = function(form) {
                form.$setPristine();
                $scope.initializtion();
            };
            $scope.emailValidate = function(emailid)
            {
                if (emailid)
                {
                    var emails;
                    emails = emailid.split(',');

                    for (var i = 0; i < emails.length; i++) {
                        if (emails[i].length > 0) {
                            if (!validateEmail(emails[i])) {
                                $scope.isEmailValidate = false;
                                return;
                            }
                            else {
                                $scope.isEmailValidate = true;
                            }
                        }
                        else {
                            $scope.isEmailValidate = true;
                        }

                    }
                }
            };
            function validateEmail(email) {
                var reg = /^([A-Za-z0-9_\-\.])+\@([A-Za-z0-9_\-\.])+\.([A-Za-z]{2,4})$/;
                if (!reg.test(email)) {
                    return false;
                }
                else {
                    return true;
                }
            }
            ;
            $scope.phoneValidate = function(contactnum) {
                if (contactnum) {
                    var contact = contactnum.split(',');
                    for (var i = 0; i < contact.length; i++) {
                        if (contact[i].length >= 10) {
                            if (!validateContact(contact[i])) {

                                $scope.isPhoneValidate = false;
                                return;
                            }
                            else {
                                $scope.isPhoneValidate = true;
                            }
                        }
                        else if (contact[i].length === 0) {

                        }
                        else {
                            $scope.isPhoneValidate = false;
                            return;
                        }
                    }
                }

            };
            function validateContact(contact) {
                var reg = /^\d{10}$/;
                if (contact.match(reg)) {
                    return true;
                }
                else
                {
                    return false;
                }
            }
            ;
            $scope.retrieveAllFranchise = function() {
                $rootScope.maskLoading();
                FranchiseService.retrieveAllFranchise({tree: false}, function(res1) {
                    var res = [];
                    angular.forEach(res1, function(item) {
                        item.franchiseName = $rootScope.translateValue("FRNCSE_NM." + item.franchiseName);
                        res.push(item);
                    });
                    $scope.franchiseModelList = res;

                    $scope.existingFranchiseList = [];
                    for (var i = 0; i < res.length; i++) {
                        $scope.existingFranchiseList.push({"id": res[i].id, "displayName": res[i].franchiseName});
                    }

                    $rootScope.unMaskLoading();
                }, function() {
                    $rootScope.addMessage("Failed to retrieve franchises", 1);
                    $rootScope.unMaskLoading();
                });

            };
            $scope.predicateBy = function(prop) {
                return function(a, b) {
                    if (a[prop] > b[prop]) {
                        return 1;
                    } else if (a[prop] < b[prop]) {
                        return -1;
                    }
                    return 0;
                };
            };
            $scope.doesFranchiseNameExist = function(franchise, form, isCreateOrUpdate) {
                if (franchise != null)
                {
//                franchise.location=null;
                    $scope.frnachiseNameDetails = {"franchiseName": franchise, "id": $scope.retrievedFranchiseId};
//                $scope.doesFranchiseNameExist( $scope.frnachiseNameDetails);
                    if ($scope.frnachiseNameDetails.franchiseName && $scope.frnachiseNameDetails.franchiseName.length > 0) {
                        FranchiseService.doesFranchiseNameExist($scope.frnachiseNameDetails, function(resp) {
                            if (resp.data) {
                                $scope.notUniqueFranchise = true;
                            } else {
                                $scope.notUniqueFranchise = false;
                                if (isCreateOrUpdate == 'create') {
                                    $scope.createFranchise(form);
                                }
                                else
                                {
                                    $scope.updateFranchise(form);
                                }
                            }
                        }, function() {
                        });
                    } else {
                        $scope.notUniqueFranchise = false;
                    }
                } else
                {
                    if (isCreateOrUpdate == 'create') {
                        $scope.createFranchise(form);
                    }
                    else
                    {
                        $scope.updateFranchise(form);
                    }
                }
            };
            $scope.doesUserNameExist = function(name) {
                if (name && name.length > 0) {
                    FranchiseService.doesUserNameExist(name, function(resp) {
                        if (resp.data) {
                            $scope.notUniqueUsername = true;
                        } else {
                            $scope.notUniqueUsername = false;
                        }
                    }, function() {
                    });
                } else {
                    $scope.notUniqueUsername = false;
                }
            };

            $scope.retrieveLocations = function() {
                $rootScope.maskLoading();
                FranchiseService.retrieveAllLocations({active: "false"}, function(data) {
                    $scope.locationMap = data.locationMap;
                    $scope.countryList = data.countryList;
                    $rootScope.unMaskLoading();
                }, function() {
                    var msg = "Fail to load Location Master";
                    var type = $rootScope.failure;
                    $rootScope.addMessage(msg, type);
                    $rootScope.unMaskLoading();
                });
            };
            $scope.getChilds = function(parent, retrievalType) {
                if (retrievalType === 'S') {
                    $scope.stateList = $scope.locationMap[parent];
                } else if (retrievalType === 'D') {
                    $scope.districtList = $scope.locationMap[parent];
                } else if (retrievalType === 'C') {
                    $scope.cityList = $scope.locationMap[parent];
                }

            };

            $scope.selectFranchise = function(selected) {
                $scope.reload = false;
                $scope.selectedTab = 1;
                if (angular.isDefined($scope.franchiseCreateForm)) {
                    $scope.franchiseCreateForm.$setPristine();
                }
                $rootScope.maskLoading();
                $scope.searchtext = '';
                $scope.setViewFlag('view');
                if (angular.isDefined(selected) && selected.id !== null) {
                    var id = selected.id;
                    $scope.isDesignationSelected = false;
//                    $scope.addFranchiseData = {};
                    $scope.addFranchiseData = DynamicFormService.resetSection($scope.generalFranchiseTemplate);

                    $scope.editdesignations = null;
                    $scope.editmachines = null;
//         
                    var success = function(res) {
                        $scope.retrievedFranchiseId = res.id;
                        $scope.editFranchise = angular.copy(res);
                        $scope.editFranchise.location = res.city + '#' + res.district + '#' + res.state + '#' + res.country;
                        $("#multiLocations").select2("data", []);
                        angular.forEach($scope.select2Locations, function(item) {
                            if (item.id === res.city + '#' + res.district + '#' + res.state + '#' + res.country) {
                                $scope.editFranchise.location = item;
                            }
                        });

                        $scope.editFranchise.adminName = res.firstName + " " + res.middleName + " " + res.lastName;
                        $scope.franchiseName = angular.copy(res.franchiseName);
                        $scope.created = false;
                        $scope.getChilds(res.country, 'S');
                        $scope.getChilds(res.state, 'D');
                        $scope.getChilds(res.district, 'C');
                        if (angular.isDefined(res.franchiseCustom) && res.franchiseCustom != null) {
                            $scope.resetCustomFields();
                            $scope.addFranchiseData = angular.copy(res.franchiseCustom);
                            $scope.reload = true;
                        }
                        var original = angular.copy($scope.editFranchise.franchiseMinReq);
                        $scope.isNotEmply = true;
                        if (original === null) {
                            $scope.isNotEmply = false;
                            $scope.editdesignations = null;
                            $scope.editmachines = null;
                        } else {
                            var depList = [];
                            var machineList = [];
                            angular.forEach(original, function(item) {
                                if (item.requirementType == 'DEG') {
                                    depList.push(item);
                                } else {
                                    machineList.push(item);
                                }
                            });
                            $scope.editdesignations = angular.copy(depList);
                            $scope.editmachines = angular.copy(machineList);
                        }
                        //
                        $scope.editFranchise.franchiseMinReq = [];
                        if (res.status == 'P') {
                            $scope.created = true;
                            $scope.canBeActivated();
                        }
                        $scope.openPage('E');
                        $rootScope.unMaskLoading();
                    };
                    var failure = function() {
                        $rootScope.unMaskLoading();
                    };
                    FranchiseService.retrieveFranchiseDetailById({primaryKey: id}, success, failure);
                } else {
                    $scope.openPage('C');
                }
            };
            $scope.updateFranchise = function(franchiseForm) {
                if ($scope.editFranchise.status === 'R') {
                    $scope.franchiseId = $scope.editFranchise.id;
                    $('#removeModal').modal('show');
                } else {
                    $scope.submitted = true;
                    $scope.isDesignationSelected = true;
                    if ($scope.editdesignations == null || $scope.editdesignations.length == 0) {
                        $scope.isDesignationSelected = false;
                    } else {
                        for (var i = 0; i < $scope.editdesignations.length; i++) {
                            var temp = $scope.editdesignations[i];
                            if (angular.isDefined(temp.requiredValue) && temp.requiredValue !== null && temp.requiredValue.toString().length > 0) {
                                $scope.isDesignationSelected = false;
                                break;
                            }
                        }
                    }
                    if (franchiseForm.$valid && !$scope.isDesignationSelected) {
                        $rootScope.maskLoading();
                        if ($scope.editFranchise.status == 'P' || $scope.canActivated) {
                            var finalMachine = angular.copy($scope.editdesignations);
                            angular.forEach($scope.editmachines, function(machine) {
                                finalMachine.push(machine);
                            });
                            $scope.editFranchise.franchiseMinReq = angular.copy(finalMachine);
                        }
                        $scope.editFranchise.franchiseCustom = $scope.addFranchiseData;
                        $scope.editFranchise.franchiseDbType = $scope.dbType;
//                        var split = $scope.editFranchise.adminName.trim().split(" ");
//                        $scope.firstName = split[0];
//                        $scope.lastName = split[1];
//                        $scope.editFranchise.firstName = $scope.firstName;
//                        $scope.editFranchise.lastName = $scope.lastName;
                        $scope.editFranchise.adminName = undefined;
                        var locationArray = $scope.editFranchise.location.toString().split('#');
                        if (locationArray.length === 3) {
                            $scope.editFranchise.city = locationArray[0];
                            $scope.editFranchise.state = locationArray[1];
                            $scope.editFranchise.country = locationArray[2];
                        } else if (locationArray.length === 4) {
                            $scope.editFranchise.city = locationArray[0];
                            $scope.editFranchise.district = locationArray[1];
                            $scope.editFranchise.state = locationArray[2];
                            $scope.editFranchise.country = locationArray[3];
                        }
                        if ($scope.editFranchise.location instanceof Object) {
                            var franchiselocation = '';
                            angular.forEach($scope.editFranchise.location, function(franchise) {
                                if (franchiselocation === '') {
                                    franchiselocation = franchise.id;
                                } else {
                                    franchiselocation = franchiselocation + ',' + franchise.id;
                                }
                            });
                            $scope.editFranchise.location = franchiselocation;
                        }
                        FranchiseService.updateFranchise($scope.editFranchise, function(res) {
                            franchiseForm.$setPristine();
                            $scope.initializtion();
                            $scope.searchtext = undefined;
                            $rootScope.unMaskLoading();
                        }, function() {
                            $rootScope.addMessage("Could not save details, please try again.", 1);
                            $rootScope.unMaskLoading();
                        });
                    } else if (!franchiseForm.$valid) {
                        for (var key in franchiseForm) {
                            if (key.indexOf("$") !== 0) {
                                if (franchiseForm[key].$invalid) {
                                    $scope.scrollTo(key.toString());
                                    break;
                                }
                            }
                        }
                    }
                }
            };
            $scope.onModelChange = function(id) {
                $scope.showTab = true;
                if (angular.isDefined(id)) {
                    $scope.designations = [];
                    $scope.machines = [];
                    if (id !== 0 && id !== -1 && id != null) {
                        $scope.setDesignationAndMachine(id);
                    }
                }
            };
            $scope.setDesignationAndMachine = function(id) {
                $scope.designations = [];
                $scope.editdesignations = [];
                $scope.machines = [];
                $scope.editmachines = [];

                var success = function(res) {
                    var original = angular.copy(res);
                    if (original == null || original.length == 0) {
                    } else {
                        var depList = [];
                        var machineList = [];
                        angular.forEach(original, function(item) {
                            if (item.requirementType == 'DEG') {
                                depList.push(item);
                            } else {

                                machineList.push(item);
                            }
                        });
                        $scope.designations = angular.copy(depList);
                        $scope.machines = angular.copy(machineList);
                        $scope.editmachines = angular.copy(depList);
                        $scope.editdesignations = angular.copy(machineList);
                    }
                };
                var failure = function() {
                };
                FranchiseService.retrieveAllFranchiseMinReq({"id": id}, success, failure);
            };
            $scope.canBeActivated = function() {
                $scope.canActivated = false;
                var further = true;
                if (!(($scope.editdesignations !== null && $scope.editdesignations.length > 0) || ($scope.editmachines !== null && $scope.editmachines.length > 0))) {
                    further = false;
                }
                angular.forEach($scope.editdesignations, function(item) {
                    if (further && !(angular.isDefined(item.acquiredValue) && angular.isDefined(item.requiredValue) && item.acquiredValue >= item.requiredValue)) {
                        further = false;
                    }
                });
                if (further) {
                    angular.forEach($scope.editmachines, function(item) {
                        if (further && !(angular.isDefined(item.acquiredValue) && angular.isDefined(item.requiredValue) && item.acquiredValue >= item.requiredValue)) {
                            further = false;
                        }
                    });
                }
                $scope.canActivated = further;
                if (!further) {
                    $scope.editFranchise.status = 'P';
                }
            }
            $scope.isThereAnyLinkWithFranchise = function() {
                var id = $scope.franchiseId;
//                var success = function(res) {
//                    $('#removeModal').modal('hide');
//                    if (res.data === true) {
//                        $('#infoModal').modal('show');
//                    } else {
//                        var id = $scope.franchiseId;
                $scope.removeFranchise(id);
//                    }
//                };

//                FranchiseService.isThereAnyLinkWithFranchise({primaryKey: id}, success, failure);
            };
            $scope.removeFranchise = function(id) {
                var success = function() {
                    $('#removeModal').modal('hide');
                    $rootScope.removeModalOpenCssAfterModalHide();
                    $('.modal-backdrop').remove();
                    $scope.initializtion();
                };
                var failure = function() {
                };
                FranchiseService.deleteFranchise({primaryKey: id}, success, failure);
            };
            $scope.hideRemovePanel = function() {
                $scope.editFranchise.status = 'P';
                $('#removeModal').modal('hide');
                $rootScope.removeModalOpenCssAfterModalHide();

            };
            $scope.ok = function() {
                $scope.editFranchise.status = 'P';
                $('#infoModal').modal('hide');
                $rootScope.removeModalOpenCssAfterModalHide();

            };
            $scope.setViewFlag = function(flag) {
                if (flag !== "search") {
                    $('#scrollable-dropdown-menu.typeahead').typeahead('val', '');
                }
                $scope.displayFlag = flag;
            };
            $scope.getSearchedFranchise = function(list) {
                $scope.resetSelection();
                var enteredText = $('#scrollable-dropdown-menu.typeahead').typeahead('val');
                if (enteredText.length > 0) {
                    if (enteredText.length < 3) {
                        $scope.searchRecords = [];
                    } else {
                        $scope.searchRecords = [];
                        angular.forEach(list, function(item) {
//                            item.franchiseName = $rootScope.translateValue("FRNCSE_NM." + item.franchiseName);
                            $scope.searchRecords.push(item);
                        });
                    }
                    $scope.setViewFlag('search');
                }
            };
            $scope.setFranchiseForEdit = function(id) {

                var res = {id: id};
                $scope.searchFranchiseId = id;
                $scope.selectFranchise(res);
            };
            $rootScope.unMaskLoading();

            $scope.doesFranchiseCodeExists = function(code, franchiseCreateForm) {
                if (code !== undefined && code !== null) {
                    $scope.submitted = false;
                    if (franchiseCreateForm) {
                        $scope.franchiseCreateForm.franchiseCode.$setValidity('exists', true);
                    }
                    if ($scope.franchiseModelList !== undefined && $scope.franchiseModelList !== null && $scope.franchiseModelList.length > 0) {
                        for (var itr = 0; itr < $scope.franchiseModelList.length; itr++) {
                            if ($scope.franchiseModelList[itr].franchiseCode !== undefined && $scope.franchiseModelList[itr].franchiseCode !== null && $scope.franchiseModelList[itr].franchiseCode.toUpperCase() === code.toUpperCase()) {
                                $scope.franchiseCreateForm.franchiseCode.$setValidity('exists', false);
                                break;
                            }
                        }
                    }
                }
            };
        }]);

});
