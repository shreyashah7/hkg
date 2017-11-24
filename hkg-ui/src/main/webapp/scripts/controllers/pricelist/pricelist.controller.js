define(['hkg', 'priceListService'], function(hkg) {
    hkg.register.controller('PriceListController', ["$rootScope", "$scope", "PriceListService", function($rootScope, $scope, PriceListService) {
            $rootScope.maskLoading();
            $rootScope.mainMenu = "manageLink";
            $rootScope.childMenu = "managePriceList";
            $rootScope.activateMenu();
            $scope.entity = "PRICELIST.";
//            $scope.url = "api/pricelist/downloadtemplate";
            $scope.successUpload = false;

            $scope.uploadFile = {
                target: $rootScope.appendAuthToken($rootScope.apipath + 'pricelist/uploadpricelist'),
                singleFile: true,
                testChunks: true,
                query: {
                    fileType: $scope.seletedFileType,
                    model: 'PriceList'
                }
            };
            $scope.initData = function() {
                $scope.uploadPricelist = true;
                PriceListService.retrieveallPriceList(function(data) {
                    $scope.allPriceList = data;
                }, function() {
                })
            };
            $scope.priceListFileUploaded = function(file, flow, form, message) {

                $scope.responseData = JSON.parse(message);
                if (!!$scope.responseData) {
                    if ($scope.responseData.messages[0].responseCode === 1) {
                        var msg = $scope.responseData.messages[0].message;
                        var type = $rootScope.failure;
                        $rootScope.addMessage(msg, type);
                        $scope.successUpload = false;
                        flow.files = [];
                    }
                    if ($scope.responseData.messages[0].responseCode === 0) {
                        $scope.fileData = $scope.responseData.data;
                        $scope.successUpload = true;
                    }
                }
            };
            $scope.priceListFileAdded = function(file, flow) {
                $scope.priceListUploaded = false;
                if ((file.getExtension() !== "xls") && (file.getExtension() !== "xlsx")) {
                    var msg = "Not a valid file. Upload only xls or xlsx file";
                    var type = $rootScope.failure;
                    $rootScope.addMessage(msg, type);
                } else {
                    //Check file size greater than 5 MB
                    $scope.seletedFileType = file.getExtension();
                    if (file.size > 5242880) {
                        var msg = "File size too large. Upload file with size less than 5MB.";
                        var type = $rootScope.failure;
                        $rootScope.addMessage(msg, type);
                        return false;
                    }
                }
                return !!{
                    xls: 1,
                    xlsx: 1
                }
                [file.getExtension()];
            };
            $scope.savePriceList = function($flow) {
                $rootScope.maskLoading();
                PriceListService.savePriceList($scope.fileData, function() {
                    $rootScope.unMaskLoading();
                    $flow.files = [];
                    delete $scope.fileData;
                    $scope.successUpload = false;
                    $scope.initData();
                    var msg = "Price list saved successfully";
                    var type = $rootScope.success;
                    $rootScope.addMessage(msg, type);
                }, function() {
                    $rootScope.unMaskLoading();
                    var msg = "Problem while saving price list.";
                    var type = $rootScope.failure;
                    $rootScope.addMessage(msg, type);
                });
            };
            $scope.cancelPriceList = function($flow) {
                $flow.files = [];
                delete $scope.fileData;
                $scope.successUpload = false;
            };
            $scope.selectPriceList = function(selected) {
                if (!!selected.currentNode.id) {
                    delete selected.currentNode.selected;
                    $rootScope.maskLoading();
                    PriceListService.retrievepricelistByMonthYear(selected.currentNode, function(data) {
                        $rootScope.unMaskLoading();

                        $scope.uploadPricelist = false;
                        $scope.listData = data;
                    }, function() {
                        $rootScope.unMaskLoading();
                        var msg = "Failed to retrieve price list.";
                        var type = $rootScope.failure;
                        $rootScope.addMessage(msg, type);
                    });
                }
            };
            $scope.addClick = function($flow) {
                $flow.files = [];
                delete $scope.fileData;
                $scope.successUpload = false;
                $scope.uploadPricelist = true;
            };
            $rootScope.unMaskLoading();
        }]);
});