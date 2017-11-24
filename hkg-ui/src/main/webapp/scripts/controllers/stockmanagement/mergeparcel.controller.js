define(['hkg', 'customFieldService', 'mergeparcelService', 'ngload!uiGrid', 'addMasterValue', 'customsearch.directive', 'lotService', 'accordionCollapse', 'ParcelTemplateController'], function (hkg) {
    hkg.register.controller('MergeParcelController', ["$rootScope", "$scope", "DynamicFormService", "MergeParcelService", "CustomFieldService", "ParcelTemplateService","$route", function ($rootScope, $scope, DynamicFormService, MergeParcelService, CustomFieldService, ParcelTemplateService, $route) {
            $rootScope.mainMenu = "stockLink";
            $rootScope.childMenu = "mergeParcelMenu";
            ParcelTemplateService.setEntityName("MERGEPARCEL.");
            $rootScope.activateMenu();
            $scope.mergeParcelDataBean = {};
            ParcelTemplateService.setFeatureName("parcelmerge");
            $scope.mergeParcel = function () {
                $scope.data = $scope.getSelectedData();
                $scope.mergeParcelDataBean.roughStockDetailDataBeans = [];
                if($scope.data !== undefined && $scope.data.footerData !== undefined){
                    $scope.mergeParcelDataBean.mergedCarat = $scope.data.footerData.changedCarat;
                    $scope.mergeParcelDataBean.mergedPieces = $scope.data.footerData.changedPieces;
                    $scope.mergeParcelDataBean.mergedAmountInDollar = $scope.data.footerData.changedAmountInDollar;
                    $scope.mergeParcelDataBean.mergedRate = $scope.data.footerData.changedRate;
                }
                $scope.mergeParcelDataBean.roughStockDetailDataBeans = angular.copy($scope.data.roughStock);
                MergeParcelService.mergeParcel($scope.mergeParcelDataBean, function (result) {
                    $scope.resetPage();
                });
            };
            
            $scope.getSelectedData = function(){
                return ParcelTemplateService.getSelectedParcelData();
            };
            $scope.resetSelectedData = function(){
                ParcelTemplateService.setSelectedParcelData({});
                ParcelTemplateService.setSelectedParcels({});
            };
            $scope.resetSelectedData();
            $scope.resetPage = function(){
                $route.reload();
            };
        }]);
});