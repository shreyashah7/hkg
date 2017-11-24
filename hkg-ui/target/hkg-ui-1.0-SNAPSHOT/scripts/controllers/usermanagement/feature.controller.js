define(['hkg', 'featureService'], function(hkg, featureService) {
    hkg.register.controller('ManageFeature', ["$rootScope", "$scope", "SysFeature", function($rootScope, $scope, SysFeature) {
        $rootScope.maskLoading();
        $rootScope.mainMenu = "manageLink";
        $rootScope.childMenu = "manageFeature";
        $rootScope.activateMenu();
        $scope.String1 = /^[A-z]+$/;
        $scope.showadd = false;
        $scope.showedit = false;
        $scope.i18EntityFeature = "FEATURE.";
        $scope.submitted = false;
        $scope.viewPage = true;
        $scope.hasFeatureAccess = $rootScope.canAccess('featureAddEdit');
        $scope.precedences = [-1, 0, 1, 2, 3, 4, 5];

        $scope.initFeatureForm = function(feature_form) {
            $scope.feature_form = feature_form;

        };
        $scope.$on('$viewContentLoaded', function() {
            retrieveFeatureCategoryList();
        });
        function retrieveFeatureCategoryList() {
            SysFeature.retreiveFeatureCategoryList(function(data) {
                $scope.featurecategorymap = data;
                $scope.menucategory = 'manage';
                $scope.onChangeCategory();
            });
        }
        $scope.onChangeCategory = function() {
            $scope.newlist = $scope.featurecategorymap[$scope.menucategory];
        };
        $scope.changeSeq = function() {
            $scope.submitted=false;
            $scope.viewPage = true;
        };
        $scope.seqSavechanges = function() {
            var featureids = [];
            angular.forEach($scope.newlist, function(item) {
                featureids.push(item.id);
            });
            SysFeature.saveSequence(featureids);
        };
        $scope.up = function($index) {
            if ($index != 0) {
                var index = $index;
                var temp = $scope.newlist[index];
                $scope.newlist[index] = $scope.newlist[index - 1];
                $scope.newlist[index - 1] = temp;
            }
        };
        $scope.down = function($index, last) {
            if (last != true) {
                var index = $index;
                var temp = $scope.newlist[index];
                $scope.newlist[index] = $scope.newlist[index + 1];
                $scope.newlist[index + 1] = temp;
            }
        };
        $scope.retreiveSystemFeaturesTree = function() {
            var featureList = [];
            var sucess = function(res) {
                angular.forEach(res, function(item) {
                    featureList.push(item);
                });
                $scope.feature_form.$dirty = false;
                $rootScope.unMaskLoading();
            };

            var failure = function() {
                $rootScope.unMaskLoading();
                var msg = "Failed to load features";
                var type = $rootScope.failure;
                $rootScope.addMessage(msg, type);

                //                toastr.error("Failed to load features");
            };
            $rootScope.maskLoading();
            SysFeature.retreiveSystemFeatures(sucess, failure);
            return featureList;
        };

        $scope.featureList = $scope.retreiveSystemFeaturesTree();
        $scope.featureListDropDown = $scope.featureList;

        $scope.save = {
            submit: function(form) {
                $scope.submitted = true;
                if (form.$valid) {
                    if ($scope.showadd) {
                        $scope.addSysFeature();
                    }
                    if ($scope.showedit) {
                        $scope.editSysFeature();
                    }
                    $scope.feature_form.$setPristine();
                }
            }
        };

        $scope.rootClick = function() {
            $scope.feature.parentId = null;
            $scope.feature.parentName = "Root";
            if ($scope.features.currentNode != undefined) {
                $scope.features.currentNode.selected = undefined;
            }
            if ($scope.featuresDropDown.currentNode != undefined) {
                $scope.featuresDropDown.currentNode.selected = undefined;
            }
        };

        $scope.featureClick = function(feature) {
            $scope.feature.parentId = feature.currentNode.id;
            $scope.feature.parentName = feature.currentNode.displayName;
        };

        $scope.showAdd = function() {
            $scope.viewPage = false;
            $scope.showadd = true;
            $scope.showedit = false;
            $scope.feature = {};
            if ($scope.features.currentNode != undefined) {
                $scope.features.currentNode.selected = undefined;
            }
            if ($scope.featuresDropDown.currentNode != undefined) {
                $scope.featuresDropDown.currentNode.selected = undefined;
            }
             $scope.rootClick();
        };

        $scope.addSysFeature = function() {

            var sucess = function(res) {
                if (res.result == 0) {
                    $scope.feature.featureName = "";
                } else {
                    $scope.featureList = $scope.retreiveSystemFeaturesTree();
                    $scope.featureListDropDown = $scope.featureList;
                    $scope.feature = {};
                    $scope.showadd = false;
                    $scope.showedit = false;
                    $scope.submitted = false;
                    if ($scope.features.currentNode != undefined) {
                        $scope.features.currentNode.selected = undefined;
                    }
                    if ($scope.featuresDropDown.currentNode != undefined) {
                        $scope.featuresDropDown.currentNode.selected = undefined;
                    }
                    retrieveFeatureCategoryList();
                }
                $scope.feature_form.$setPristine();
                $rootScope.unMaskLoading();
            };

            var failure = function() {
                var msg = "Failed to create feature";
                var type = $rootScope.error;
                $rootScope.addMessage(msg, type);
                $rootScope.unMaskLoading();
            };
            $rootScope.maskLoading();
            SysFeature.createSysFeature($scope.feature, sucess, failure);
        };

        $scope.showEdit = function(updateSelectedFeature) {
            if (angular.isDefined(updateSelectedFeature)) {
                $scope.viewPage = false;
                $scope.showedit = true;
                $scope.showadd = false;
                $scope.feature = angular.copy(updateSelectedFeature.currentNode);
                if ($scope.feature.menuType === 'MI' || $scope.feature.menuType === 'EI' || $scope.feature.menuType === 'DEI' || $scope.feature.menuType === 'DMI') {
                    $scope.title = '';
                }
            }
        };

        $scope.editSysFeature = function() {

            $scope.feature.selected = undefined;

            var sucess = function(res) {

                if (res.result == 0) {
                    $scope.feature.featureName = "";
                } else {
                    $scope.featureList = $scope.retreiveSystemFeaturesTree();
                    $scope.featureListDropDown = $scope.featureList;
                    $scope.feature = {};
                    $scope.showedit = false;
                    $scope.showadd = false;
                    $scope.submitted = false;
                    if ($scope.features.currentNode != undefined) {
                        $scope.features.currentNode.selected = undefined;
                    }
                    if ($scope.featuresDropDown.currentNode != undefined) {
                        $scope.featuresDropDown.currentNode.selected = undefined;
                    }
                    retrieveFeatureCategoryList();
                }
                $rootScope.unMaskLoading();
            };

            var failure = function() {
                $rootScope.unMaskLoading();
                var msg = "Failed to update feature";
                var type = $rootScope.failure;
                $rootScope.addMessage(msg, type);

            };

            $scope.feature.children = null;
            $rootScope.maskLoading();
            SysFeature.updateSysFeature($scope.feature, sucess, failure);
        };

        $rootScope.unMaskLoading();
    }]);
});
