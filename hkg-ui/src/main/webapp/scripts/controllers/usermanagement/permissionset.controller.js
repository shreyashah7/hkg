define(['hkg', 'designationService', 'featureService', 'uiBootstrap', 'ngload!ngTable', 'alert.directive'], function(hkg, designationService, featureService) {
    hkg.register.controller('ManagePermissionSet', ["$rootScope", "$scope", "RoleManagement", "SysFeature", "$filter", "ngTableParams", "$location", function($rootScope, $scope, RoleManagement, SysFeature, filter, ngTableParams, $location) {
            $rootScope.maskLoading();

            //For Internationalization
            $scope.i18Designation = "DESIGNATION";
            if ($rootScope.permissionFeatureIdMap == undefined) {
                $location.path("/managedesignation");
            } else {
                $scope.deletePermissionId = $rootScope.permissionFeatureIdMap["permissionId"];
                $scope.deleteFeatureId = $rootScope.permissionFeatureIdMap["featureId"];
                $scope.selectedpermissonid = $scope.deletePermissionId;
            }

            $scope.designations = [];
            $scope.finalUpdatedMap = {};
            var data = [];

            RoleManagement.retrieveDesignationsByPermissionId($rootScope.permissionFeatureIdMap, function(res) {
                $scope.designations = res["designations"];
                $scope.permissionSets = res["permissionsets"];

                if ($scope.designations != null) {
                    for (var i; i < $scope.designations.length; i++) {
                        $scope.designations[i].isChange = false;
                        $scope.designations[i].selectedPermission = null;
                    }
                }
                data = $scope.designations;
                $scope.tableParams.reload();
//                toastr.success("succesfull");
            }, function() {
//                console.error("failed");
                var msg = "Failed to load Designation By Permission";
                var type = $rootScope.failure;
                $rootScope.addMessage(msg, type);
            });


            $scope.tableParams = new ngTableParams({
                page: 1, // show first page
                count: 10, // count per page,
                sorting: {
                    title: 'asc'     // initial sorting
                },
                total: $scope.designations.length // length of data
            }, {
                getData: function($defer, params) {

                    var orderedData = params.sorting() ?
                            filter('orderBy')($scope.designations, params.orderBy()) : $scope.designations;
//
                    orderedData = params.filter() ?
                            filter('filter')(orderedData, params.filter()) : orderedData;

                    data = orderedData.slice((params.page() - 1) *
                            params.count(), params.page() * params.count());

                    params.total(orderedData.length); // set total for recal
                    $defer.resolve(data);

                }
            });

            $scope.setSelectedPermission = function(selectedPermissionId, designation) {
                if ($scope.deletePermissionId !== selectedPermissionId) {
                    designation.isChange = true;
                    designation.selectedPermission = selectedPermissionId;

                } else {
                    designation.isChange = false;
                    designation.selectedPermission = null;
                }
            };

            $scope.updateAllPermissionSet = function() {
                $scope.finalMap = {};
                $scope.featurePermissionMap = {};
                if ($scope.designations != null) {
                    for (var i = 0; i < $scope.designations.length; i++) {
                        if ($scope.designations[i].isChange == true) {
                            $scope.finalMap[$scope.designations[i].value] = $scope.designations[i].selectedPermission;
                        }
                    }
                }
                if ($scope.deleteFeatureId !== undefined && $scope.deletePermissionId !== undefined) {
                    $scope.featurePermissionMap[$scope.deleteFeatureId] = $scope.deletePermissionId;
                }
                $scope.finalUpdatedMap = {
                    "featureId": $scope.deleteFeatureId,
                    "oldPermissionId": $scope.deletePermissionId,
                    "updatedMap": $scope.finalMap
                };
                RoleManagement.updateDesignationsPermissionSet($scope.finalUpdatedMap, function(res) {
//                    toastr.success("Permission Set updated succesfully...");
                    var msg = "Permission Set updated succesfully";
                    var type = $rootScope.success;
                    $rootScope.addMessage(msg, type);
                    $location.path("/managedesignation");
                }, function() {
//                    console.log("failed");
                    var msg = "Failed to update Permission Set";
                    var type = $rootScope.failure;
                    $rootScope.addMessage(msg, type);
                });
            };
            $scope.backToHome = function() {
                $location.path("/managedesignation");
            };
            $rootScope.unMaskLoading();
        }]);
});
