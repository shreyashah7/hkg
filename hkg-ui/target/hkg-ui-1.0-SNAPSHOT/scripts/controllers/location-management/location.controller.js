define(['hkg', 'locationService'], function (hkg, locationService) {
    hkg.register.controller('LocationController', ["$rootScope", "$scope", "LocationService", "$filter", function ($rootScope, $scope, LocationService, $filter) {
        $rootScope.maskLoading();
        $rootScope.mainMenu = "manageLink";
        $rootScope.childMenu = "manageLocation";
        $rootScope.activateMenu();
        $scope.entity = 'LOCATIONS.';
        $scope.defaultButtons = true;
        $scope.activate = false;
        $scope.activeChildren = false;
        $scope.parentActived = true;
        LocationService.retriveAllLocations({active: "false"}, function (data) {
            $scope.locationMap = data.locationMap;
            $scope.countryList = data.countryList;
            $scope.addLocationForm.$dirty = false;
            if ($scope.editLocationForm !== undefined)
                $scope.editLocationForm.$dirty = false;
        }, function () {
            var msg = "Fail to load Location Master";
            var type = $rootScope.failure;
            $rootScope.addMessage(msg, type);
        });

        $scope.initAddLocationForm = function (addLocationForm) {
            $scope.addLocationForm = addLocationForm;
        };

        $scope.initEditLocationForm = function (editLocationForm) {
            $scope.editLocationForm = editLocationForm;
        }

        $scope.retrieveLocations = function (isShow) {
            $scope.districtHierarchy = false;
            $scope.countryHierarchy = false;
            $scope.stateHierarchy = false;
            $scope.cityHierarchy = false;
            LocationService.retriveAllLocations({active: isShow}, function (data) {
                $scope.locationMap = data.locationMap;
                $scope.countryList = data.countryList;
                $scope.retriveLocationstree("true");
                $scope.addLocations = true;
            }, function () {
                var msg = "Fail to load location master";
                var type = $rootScope.failure;
                $rootScope.addMessage(msg, type);
            });
        };

        $scope.retriveLocationstree = function (isShow) {
            LocationService.retriveLocationstree({key: isShow}, function (data) {
                $scope.locationTree = data;
                $scope.locationTree = $filter('orderBy')($scope.locationTree, 'locationName');
                $scope.treeToListJson = [];
                function convertTreeToList(treeToList) {
                    if (treeToList !== undefined && treeToList !== null) {
                        var element = treeToList;
                        if (element.id !== undefined && element.id !== null) {
                            $scope.treeToListJson.push({key: element.id, isActive: element.isActive, parent: element.parentId});
                        }
                        if (element.children !== undefined && element.children !== null) {
                            for (var i = 0; i < element.children.length > 0; i++) {
                                convertTreeToList(element.children[i]);
                            }
                        }
                    }
                }
                $scope.tree = angular.copy(data);
                for (var i = 0; i < $scope.tree.length; i++) {
                    convertTreeToList($scope.tree[i]);
                }
            }, function () {
                var msg = "Fail to load location tree";
                var type = $rootScope.failure;
                $rootScope.addMessage(msg, type);
            });
        };

        $scope.addTheLocation = function () {
            //Set forms pristine.
            if(angular.isDefined($scope.editLocationForm)){
                $scope.editLocationForm.$setPristine();
            }
            $scope.activeChildren = false;
            $scope.parentActived = true;
            $scope.addLocationForm.$dirty = false;
            $scope.defaultButtons = true;
            $('#scrollable-dropdown-menu.typeahead').typeahead('val', '');
            $scope.displaySearchedLocation = '';
            $scope.submitted = false;
            $scope.addCountry = false;
            $scope.districtHierarchy = false;
            $scope.countryHierarchy = false;
            $scope.stateHierarchy = false;
            $scope.cityHierarchy = false;
            $scope.selectHierarchy = null;
            $scope.addLocations = true;
            $scope.editlocations = false;
            $scope.managelocations = false;
            if ($scope.locationval != undefined) {
                $scope.locationval.selected = undefined;
            }
            if ($scope.locationval != undefined && $scope.locationval.currentNode) {
                $scope.locationval.currentNode.selected = undefined;
                $scope.locationval.currentNode = undefined;
            }
            $scope.clear();
            $scope.selected = null;

        };

        $scope.updateTheLocation = function () {
            $scope.submitted = false;
            $scope.districtHierarchy = false;
            $scope.countryHierarchy = false;
            $scope.stateHierarchy = false;
            $scope.cityHierarchy = false;
            $scope.selectHierarchy = null;
            $scope.managelocations = false;
            $scope.editlocations = false;
            $scope.addLocations = true;
            $scope.clear();
            $scope.selected = null;
        };

        $scope.hierarchies = ['Country', 'State', 'District', 'City'];

        $scope.$watch('selectHierarchy', function () {
            $scope.showHierarchy = $scope.selectHierarchy;
            $scope.defaultButtons = false;
            if ($scope.showHierarchy === "Country") {

                $scope.addCountry = true;
                $scope.addState = false;
                $scope.addDistrict = false;
                $scope.addCity = false;
                $scope.clear();
                $scope.selected = null;
                $scope.updateCountry = true;
            }
            else if ($scope.showHierarchy === "State") {

                $scope.updateCountry = true;
                $scope.addCountry = false;
                $scope.addState = true;
                $scope.addDistrict = false;
                $scope.addCity = false;
                $scope.clear();
                $scope.selected = null;
            }
            else if ($scope.showHierarchy === "District") {

                $scope.updateCountry = true;
                $scope.addCountry = false;
                $scope.addDistrict = true;
                $scope.addState = false;
                $scope.addCity = false;
                $scope.clear();
                $scope.selected = null;
            }
            else if ($scope.showHierarchy === "City") {

                $scope.updateCountry = true;
                $scope.addCountry = false;
                $scope.addState = false;
                $scope.addDistrict = false;
                $scope.addCity = true;
                $scope.clear();
                $scope.selected = null;
            }
            else
            {
                $scope.country = null;
                $scope.defaultButtons = true;
                $scope.updateCountry = false;
                $scope.addCountry = false;
                $scope.addState = false;
                $scope.addDistrict = false;
                $scope.addCity = false;
                $scope.countryHierarchy = false;
                $scope.stateHierarchy = false;
                $scope.districtHierarchy = false;
                $scope.cityHierarchy = false;

            }
        });


        $scope.$watch('activate', function (activate)
        {
            if ($scope.activate === true)
            {
                $scope.retrieveLocations("true");
                $scope.clear();
            }
            else
            {
                $scope.retrieveLocations("false");
                $scope.clear();
            }
        });

        $scope.status = [{"id": true, "name": "Active"}, {"id": false, "name": "InActive"}];

        $scope.getChildsUpdate = function (selected, retrievalType) {
            $scope.selectedValue = [];
            if (selected === null || typeof (selected) === 'undefined' || selected === 'Select')
            {
                if (retrievalType === 'S') {
                    $scope.state = null;
                    $scope.district = null;
                    $scope.city = null;
                    $scope.stateByCountry = [];
                    $scope.districtByState = [];
                    $scope.cityByDistrict = [];
                } else if (retrievalType === 'D') {
                    $scope.district = null;
                    $scope.city = null;
                    $scope.districtByState = [];
                    $scope.cityByDistrict = [];
                } else if (retrievalType === 'T') {
                    $scope.city = null;
                    $scope.cityByDistrict = [];
                }
                $scope.showTable = false;
            }
            else
            {
                if (($scope.selectHierarchy === 'Country' && retrievalType === 'S')
                        || ($scope.selectHierarchy === 'State' && retrievalType === 'D')
                        || ($scope.selectHierarchy === 'District' && retrievalType === 'T')
                        || ($scope.selectHierarchy === 'City' && retrievalType === 'C')) {
                    $scope.selectedValue = angular.copy(selected);
                    $scope.showTable = true;
                } else {
                    $scope.showTable = false;
                }
                $scope.getChilds(selected.value, retrievalType);
            }

        };

        $scope.getChilds = function (parent, retrievalType)
        {
            if (parent === null || typeof (parent) === 'undefined')
            {
                if (retrievalType === 'S') {
                    $scope.stateId = null;
                    $scope.districtId = null;
                    $scope.cityId = null;
                    $scope.stateByCountry = [];
                    $scope.districtByState = [];
                    $scope.cityByDistrict = [];
                } else if (retrievalType === 'D') {
                    $scope.districtId = null;
                    $scope.cityId = null;
                    $scope.districtByState = [];
                    $scope.cityByDistrict = [];
                } else if (retrievalType === 'T') {
                    $scope.cityByDistrict = [];
                    $scope.cityId = null;
                }
            }
            else
            {
                if (retrievalType === 'S') {
                    $scope.stateByCountry = $scope.locationMap[parent];
                } else if (retrievalType === 'D') {
                    $scope.districtByState = $scope.locationMap[parent]
                } else if (retrievalType === 'T') {
                    $scope.cityByDistrict = $scope.locationMap[parent]
                }
            }
            if ($scope.selected === "Select")
            {
                $scope.showTable = false;
            }

        };

        $scope.setPristine = function () {
            $scope.submitted = false;
            $scope.addLocationForm.$setPristine();
        }

        $scope.createLocation = function (location1) {
            $scope.submitted = true;
            if ($scope.addLocationForm.$valid) {
                $scope.districtHierarchy = false;
                $scope.countryHierarchy = false;
                $scope.stateHierarchy = false;
                $scope.cityHierarchy = false;
                var location = {
                };
                if (location1 != null) {
                    location.locationName = location1.locationName;
                    if ($scope.selectHierarchy === 'Country')
                    {
                        location.locationType = 'Country';
                    }
                    if ($scope.selectHierarchy === 'State')
                    {
                        location.id = $scope.countryId;
                        location.locationType = 'State';
                    }
                    if ($scope.selectHierarchy === 'District')
                    {
                        location.id = $scope.stateId;
                        location.locationType = 'District';
                    }
                    if ($scope.selectHierarchy === 'City')
                    {
                        location.id = $scope.districtId;
                        location.locationType = 'City';
                    }
                }
                var failure = function () {
                    var msg = "Failed to create system feature";
                    var type = $rootScope.failure;
                    $rootScope.addMessage(msg, type);
                };

                var sucess = function (data) {
                    $scope.retrieveLocations('false');
                    location.id = null;
                    location.locationName = null;
                    $scope.clear();
                    $scope.managelocations = false;
                    $scope.addLocations = true;
                    $scope.editlocations = false;
                    $scope.updateCountry = false;
                    $scope.addCountry = false;
                    $scope.addState = false;
                    $scope.addDistrict = false;
                    $scope.addCity = false;
                    $scope.selectHierarchy = false;
                };

                LocationService.saveAllLocations(location, sucess, failure);
                $scope.submitted = false;
                $scope.addLocationForm.$dirty = false;
            }
        };
        $scope.newLocation = {};
        $scope.treeEditLocation = function (location) {
            //Set forms pristine.
            if(angular.isDefined($scope.addLocationForm)){
                $scope.addLocationForm.$setPristine();
            }

            $('#scrollable-dropdown-menu.typeahead').typeahead('val', '');
            $scope.displaySearchedLocation = '';
//            $scope.selectHierarchy = null;
            $scope.submitted = false;
            $scope.managelocations = false;
            $scope.addLocations = false;
            $scope.editlocations = true;
            if (location.currentNode !== '' && location.currentNode !== undefined) {
                if (location.currentNode.locationType === "country")
                {
                    $scope.countryHierarchy = true;
                    $scope.stateHierarchy = false;
                    $scope.districtHierarchy = false;
                    $scope.cityHierarchy = false;
                    $scope.newLocation = angular.copy(location);
                } else if (location.currentNode.locationType === "states")
                {
                    $scope.countryHierarchy = false;
                    $scope.stateHierarchy = true;
                    $scope.districtHierarchy = false;
                    $scope.cityHierarchy = false;
                    $scope.newLocation = angular.copy(location);
                } else if (location.currentNode.locationType === "district")
                {
                    $scope.districtHierarchy = true;
                    $scope.countryHierarchy = false;
                    $scope.stateHierarchy = false;
                    $scope.cityHierarchy = false;
                    $scope.newLocation = angular.copy(location);
                } else if (location.currentNode.locationType === "city")
                {
                    $scope.cityHierarchy = true;
                    $scope.countryHierarchy = false;
                    $scope.stateHierarchy = false;
                    $scope.districtHierarchy = false;
                    $scope.newLocation = angular.copy(location);
                }
            } else {
                if (location.locationType === "country")
                {
                    $scope.countryHierarchy = true;
                    $scope.stateHierarchy = false;
                    $scope.districtHierarchy = false;
                    $scope.cityHierarchy = false;
                    $scope.newLocation.currentNode = angular.copy(location);
                } else if (location.locationType === "states")
                {
                    $scope.countryHierarchy = false;
                    $scope.stateHierarchy = true;
                    $scope.districtHierarchy = false;
                    $scope.cityHierarchy = false;
                    $scope.newLocation.currentNode = angular.copy(location);
                } else if (location.locationType === "district")
                {
                    $scope.districtHierarchy = true;
                    $scope.countryHierarchy = false;
                    $scope.stateHierarchy = false;
                    $scope.cityHierarchy = false;
                    $scope.newLocation.currentNode = angular.copy(location);
                } else if (location.locationType === "city")
                {
                    $scope.cityHierarchy = true;
                    $scope.countryHierarchy = false;
                    $scope.stateHierarchy = false;
                    $scope.districtHierarchy = false;
                    $scope.newLocation.currentNode = angular.copy(location);
                }
            }
        };

        $scope.editLoc = function (location1) {
            $scope.submitted = true;
            if ($scope.editLocationForm.$valid) {
                $scope.activeChildren = false;
                $scope.parentActived = true;
                var cheked = false;
                var getChildren = function (child) {
                    if (!$scope.activeChildren && child !== undefined && child !== null) {
                        for (var i = 0; i < child.length; i++) {
                            var elem = child[i];
                            if (!$scope.activeChildren && elem.isActive) {
                                $scope.activeChildren = true;
                                cheked = true;
                                break;
                            }
                            if (!$scope.activeChildren && elem.children && elem.children !== null) {
                                getChildren(elem.children);
                            } else {
                                cheked = true;
                            }
                        }
                    }
                    else {
                        cheked = true;
                    }
                };
                var child = location1.currentNode.children;
                if (!location1.currentNode.isActive) {
                    getChildren(child);

                }
                if (location1.currentNode.isActive) {
                    var getParent = function (parent) {
                        if (parent !== undefined && parent !== null && $scope.treeToListJson !== undefined && $scope.treeToListJson !== null) {
                            for (var i = 0; i < $scope.treeToListJson.length; i++) {
                                if (parent === $scope.treeToListJson[i].key && !$scope.treeToListJson[i].isActive) {
                                    $scope.parentActived = false;
                                }
                                if ($scope.parentActived && $scope.treeToListJson[i].parentId !== undefined && $scope.treeToListJson[i].parentId !== null) {
                                    getParent($scope.treeToListJson[i].parentId);
                                } else {
                                    cheked = true;
                                }
                            }
                        } else {
                            cheked = true;
                        }
                    };

                    var parent = location1.currentNode.parentId;
                    getParent(parent);
                }
                if ($scope.activeChildren && cheked) {
                    $("#overridePopUp").modal('show');
                }
                if (!$scope.parentActived && cheked) {
                    $("#overridePopUp").modal('show');
                }
                if ((location1.currentNode.isActive && $scope.parentActived && cheked) || (!$scope.activeChildren && cheked && !location1.currentNode.isActive)) {
                    var location = {
                        id: location1.currentNode.id,
                        locationName: location1.currentNode.locationName,
                        isActive: location1.currentNode.isActive,
                        parentId: location1.currentNode.parentId,
                        children: location1.currentNode.children

                    };
                    var failure = function () {
                        var msg = "Failed to create system feature";
                        var type = $rootScope.failure;
                        $rootScope.addMessage(msg, type);
                    };

                    var sucess = function (data) {
                        var activate = $scope.activate.toString();
                        $scope.retrieveLocations(activate);
                        $scope.clear();
                        $scope.managelocations = false;
                        $scope.addLocations = true;
                        $scope.editlocations = false;
                        $scope.updateCountry = false;
                        $scope.addCountry = false;
                        $scope.addState = false;
                        $scope.addDistrict = false;
                        $scope.addCity = false;
                        $scope.selectHierarchy = false;
                    };
                    LocationService.updateLocations(location, sucess, failure);
                    $scope.submitted = false;
                } else {

                }
            }
        };


        $scope.updateLocation = function (selected) {
            $scope.submitted = true;
            if ($scope.manageLocationsForm.$valid) {
                var parentId;
                if ($scope.selectHierarchy === 'Country')
                {
                    parentId = null;
                }
                if ($scope.selectHierarchy === 'State')
                {
                    parentId = $scope.country.value;
                }
                if ($scope.selectHierarchy === 'District')
                {
                    parentId = $scope.state.value;
                }
                if ($scope.selectHierarchy === 'City')
                {
                    parentId = $scope.district.value;
                }

                var location = {
                    id: selected.value,
                    locationName: selected.label,
                    isActive: selected.isActive,
                    parentId: parentId
                };
                var failure = function () {
                    var msg = "Failed to create system feature";
                    var type = $rootScope.failure;
                    $rootScope.addMessage(msg, type);
                };
                var sucess = function (data) {
                    var activate = $scope.activate.toString();
                    $scope.retrieveLocations(activate);
                    $scope.managelocations = false;
                    $scope.addLocations = true;
                    $scope.editlocations = false;
                    $scope.clear();
                };
                LocationService.updateLocations(location, sucess, failure);
                $scope.submitted = false;
            }

        };

        $scope.cancelAddLocForm = function () {
            $scope.activeChildren = false;
            $scope.parentActived = true;
            $scope.defaultButtons = true;
            $scope.location = {};
            $scope.setPristine();
            $('#scrollable-dropdown-menu.typeahead').typeahead('val', '');
            $scope.clear();
            $scope.managelocations = false;
            $scope.addLocations = true;
            $scope.editlocations = false;
            $scope.selectHierarchy = null;
            $scope.countryHierarchy = false;
            $scope.stateHierarchy = false;
            $scope.districtHierarchy = false;
            $scope.cityHierarchy = false;
            $scope.addCountry = false;
            $scope.addState = false;
            $scope.addDistrict = false;
            $scope.addCity = false;

            if ($scope.locationval != undefined) {
                $scope.locationval.selected = undefined;
            }
            if ($scope.locationval != undefined && $scope.locationval.currentNode) {
                $scope.locationval.currentNode.selected = undefined;
                $scope.locationval.currentNode = undefined;
            }
            //Set forms pristine.
            if(angular.isDefined($scope.addLocationForm)){
                $scope.addLocationForm.$setPristine();
            }
            if(angular.isDefined($scope.editLocationForm)){
                $scope.editLocationForm.$setPristine();
            }
        };

        $scope.clear = function ()
        {
            $scope.showTable = false;
            //for update selection
            $scope.district = null;
            $scope.state = null;
            $scope.country = null;
            $scope.city = null;

            //for add selection and update details
            $scope.countryId = null;
            $scope.stateId = null;
            $scope.districtId = null;
            $scope.cityId = null;
            $scope.selected = null;
            $scope.stateByCountry = [];

            $scope.location = null;
            $scope.cityByDistrict = [];
            $scope.districtByState = [];
        };

        $scope.editSearchedLoc = function (id) {
            $scope.submitted = false;
            $scope.managelocations = false;
            $scope.addLocations = false;
            $scope.editlocations = true;
            $scope.districtHierarchy = false;
            $scope.countryHierarchy = false;
            $scope.stateHierarchy = false;
            $scope.cityHierarchy = false;
            var got = false;
            var currentNode = {};
            angular.forEach($scope.countryList, function (value) {
                if (value.value === id) {
                    got = true;
                    currentNode.id = value.value;
                    currentNode.locationType = "country";
                    currentNode.parentId = null;
                    currentNode.isActive = value.isActive;
                    currentNode.locationName = value.label;
                    $scope.countryHierarchy = true;
                }
            });
            if (!got) {
                angular.forEach($scope.locationMap, function (value, key) {
                    if (!got) {
                        angular.forEach(value, function (item) {
                            if (item.value == id) {
                                currentNode.id = item.value;
                                currentNode.parentId = key;
                                currentNode.isActive = item.isActive;
                                currentNode.locationName = item.label;
                                if (item.description === 'S') {
                                    $scope.stateHierarchy = true;
                                } else if (item.description === 'D') {
                                    $scope.districtHierarchy = true;
                                } else if (item.description === 'T') {
                                    $scope.cityHierarchy = true;
                                }
                            }
                        });
                    }
                });
            }
            $scope.newLocation = {};
            $scope.newLocation.currentNode = angular.copy(currentNode);
            $scope.displaySearchedLocation = '';
        };

//        $scope.getSearchedLocation = function(list) {
//            var enteredText = $('#scrollable-dropdown-menu.typeahead').typeahead('val');
//            if (enteredText.length > 0) {
//                $scope.displaySearchedLocation = 'search';
//                if (enteredText.length < 3) {
//                    $scope.searchedLocationRecord = [];
//                    $scope.editlocations = false;
//                    $scope.editlocations = true;
//                } else {
//                    $scope.searchedLocationRecord = angular.copy(list);
//                    $scope.editlocations = false;
//                    $scope.editlocations = true;
//                }
//            }
//        };
        $scope.hidePopUp = function () {
            $("#overridePopUp").modal('hide');
            $rootScope.removeModalOpenCssAfterModalHide();

        };
        $rootScope.unMaskLoading();
    }]);
});
