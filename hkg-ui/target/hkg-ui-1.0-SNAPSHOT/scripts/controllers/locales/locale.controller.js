/* 
 * @Author: Satyam Koyani
 * This is the angular controller for the manage locales functionality.
 */

define(['hkg', 'localesService', 'franchiseService'], function (hkg, localesService, franchiseService) {
    hkg.register.controller('LocalesContoller', ["$rootScope", "$scope", "$filter", "ManageLocalesService", 'FranchiseService', function ($rootScope, scope, filter, LocalesService, FranchiseService) {
            $rootScope.maskLoading();
            $rootScope.mainMenu = "manageLink";
            $rootScope.childMenu = "manageLocales";
            $rootScope.activateMenu();
            scope.i18Locales = "Locales.";
            scope.templateUrl = "templates/messages.html";
            scope.categoryTreeId = {};
            /**
             * Comment
             */
            scope.categoryList = [];
            scope.$on('$viewContentLoaded', function () {
                scope.categoryList.push({"id": "LABEL", "displayName": "Labels", "children": null, "parentId": null, "parentName": null});
                scope.categoryList.push({"id": "MESSAGE", "displayName": "Messages", "children": null, "parentId": null, "parentName": null});
                scope.categoryList.push({"id": "MASTER", "displayName": "Masters", "children": null, "parentId": null, "parentName": null});
                scope.categoryList.push({"id": "REPORT", "displayName": "Reports", "children": null, "parentId": null, "parentName": null});
                scope.categoryList.push({"id": "NOTIFICATION", "displayName": "Notifications", "children": null, "parentId": null, "parentName": null});
                scope.categoryList.push({"id": "CONTENT", "displayName": "Content", "children": null, "parentId": null, "parentName": null});
                retrieveAllLanguage();
                defaultSetting();
            });

            /**
             * Comment
             */
            function defaultSetting() {
                var setSelectedIndexFromCatList = 0;
                scope.categoryTreeId.currentNode = scope.categoryList[setSelectedIndexFromCatList];
                scope.categoryTreeId.currentNode.selected = "selected";
                setselectedCategoryOfTree(scope.categoryList[setSelectedIndexFromCatList]);
                scope.setClickedLetter(scope.alphabets[0]);
//                scope.setClickedLetter(scope.alphabets[0]);            
            }
            ;
            /**
             * Comment
             */
            scope.openSelectedCategoryPage = function (selectedCategory) {
                if (angular.isDefined(selectedCategory)) {
                    setselectedCategoryOfTree(selectedCategory);
                    if (selectedCategory.id == 'MESSAGE' || selectedCategory.id == 'CONTENT' || selectedCategory.id == 'LABEL' || selectedCategory.id == 'MASTER' || selectedCategory.id == 'NOTIFICATION' || selectedCategory.id == 'REPORT') {
                        scope.clientSidePgintnOfLocales();
                    }
                }
            };
            /**
             * Comment
             */
            function setselectedCategoryOfTree(selectedCategoryInTree) {
                scope.selectedCategoryInTree = selectedCategoryInTree;
            }
            ;
            /**
             * Comment
             */
            scope.alphabets = ["A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"];
            /**
             * Comment
             */
            scope.setClickedLetter = function (filterByLetter) {
                console.log("Clicked letter : " + filterByLetter);
                scope.searchLetter = filterByLetter;
                scope.localesList = [];
                scope.clientSidePgintnOfLocales();
//               scope.serverSidePgintnOfLocales();
            };
            /**
             * Comment
             */
            function retrieveAllLanguage() {
                var success = function (data) {
                    scope.languageList = data.sort($rootScope.predicateBy("name"));
                    angular.forEach(data, function (row, index) {
//                        console.log("ro" + JSON.stringify(row));
                        if (row.code === 'EN') {
                            scope.selectedLanguage = row;
                        }
                    });
                }
                var failure = function (data) {
                    console.log(" retrieveAllLanguage : failure");
                }
                LocalesService.retrieveAllLanguages(null, success, failure);
            }
            ;
            /**
             * Comment
             */
            scope.updateTranslation =
                    function () {
//                        console.log(JSON.stringify(scope.localesList));
                        var success = function (response) {

                        };
                        var failure = function (response) {

                        };
                        LocalesService.updateAllLocales(scope.localesList, success, failure);
                    };


            /**
             * Comment
             */

            scope.showAllLabels = function () {
                scope.showAll = true;
                scope.clientSidePgintnOfLocales();
            };


            scope.clientSidePgintnOfLocales = function () {
                //Success function executes if record successfully retrieved.  
                scope.isDisplayTable = false;
                var requestParameter = {};
                requestParameter.type = scope.selectedCategoryInTree.id;
                if (scope.showAll == true) {
                    requestParameter.searchText = '';
                }
                else {
                    requestParameter.searchText = scope.searchLetter;
                }
                scope.showAll = false;
                requestParameter.languageDataBean = scope.selectedLanguage;
                if (!angular.isDefined(scope.selectedLanguage)) {
                    requestParameter.languageDataBean = {};
                    requestParameter.languageDataBean.code = 'EN';
                    requestParameter.languageDataBean.country = 'IN';
                    requestParameter.languageDataBean.name = 'English';
                }
                if (scope.selectedCategoryInTree.id == "CONTENT") {
//                    requestParameter.entity = "DEPARTMENT";//scope.selectedContentType.toUpperCase();
//                    requestParameter.languageDataBean.company = 1;
                    requestParameter.entity = scope.selectedContentType.toUpperCase();
//                    requestParameter.languageDataBean.company = scope.selectedFranchise;
                }else if(scope.selectedCategoryInTree.id == "REPORT"){
                    requestParameter.entity = "report";
                }
                if (scope.selectedCategoryInTree.id == "NOTIFICATION") {
                    requestParameter.searchText = '';
                }
                var success = function (res) {
                    $rootScope.unMaskLoading();
                    scope.localesList = res;
                    scope.shortLabel;
                    for (var i = 0; i < scope.localesList.length; i++) {
                        var obj = scope.localesList[i].defaultText;
                        scope.shortLabel = obj.slice(0, 45);
                        scope.localesList[i].shortLabel = scope.shortLabel;
                    }
                    if (res.length > 0) {
                        scope.isDisplayTable = true;
                    }
                }
                //Failure function executes if record could not be retrieved.
                var failure = function (res) {
                    console.log("failure");
                }
                $rootScope.maskLoading();
                LocalesService.retrieveLocalesBySearchFields(requestParameter, success, failure);

            };



            //This function is watching on flag which decide whether the message of serch box should be displayed or not.
            scope.filterTypeahead = function () {
                var searchLocaleFilterData = filter('filter')(scope.categoryList, {name: scope.searchLocale});
                if (searchLocaleFilterData.length > 0) {
                    scope.isInValidSearchLocale = false;
                }
                else {
                    scope.isInValidSearchLocale = true;
                }
            };

            /**
             * Comment
             */
//            scope.serverSidePgintnOfContents = function(data, recordsConfig, callback) {
//                //Success function executes if record successfully retrieved.               
//                var success = function(res) {
//                    scope.serverRecordsOfContent = res.records;
//                    recordsConfig.recordsTotal = 10;
//                    recordsConfig.recordsFiltered = 10;
//                    callback(recordsConfig);
//                }
//                //Failure function executes if record could not be retrieved.
//                var failure = function(res) {
//                    console.log("failure");
//                }
//                //Todo:Change this service as per respective url
//                LocalesService.retrieveContentAsPerScroll(data, success, failure);
//            };
            /**
             * Comment this function will fill the combo while content translation will be shown
             */
            scope.initFnForContentTranslation =
                    function () {
                        console.log(" initFnForContentTranslation calling ");
                        scope.showAll = true;
//                        function retrieveFranchiseList() {
//                            var success = function(data) {
//                                scope.franchiseList = data;
//                                if (data && data.length > 0) {
//                                    scope.selectedFranchise = data[0].id;
//                                }
//                                console.log("frnc=" + scope.selectedFranchise);
//                                console.log("df=" + scope.selectedContentType);
//                                scope.clientSidePgintnOfLocales();
//
//                            }
//                            var failure = function(res) {
//                                console.log(" retrieveFranchiseList failure");
//                            }
//                            FranchiseService.retrieveAllFranchise({tree: false}, success, failure);
//                        }
//                        ;
                        function retrieveContentList() {
                            var success = function (data) {
                                scope.contentTypeList = data.contentlist;
                                if (data && data.contentlist.length > 0) {
                                    scope.selectedContentType = data.contentlist[1];
                                    scope.clientSidePgintnOfLocales();
                                }
//                                retrieveFranchiseList();

                            }
                            var failure = function (res) {
                                console.log("retrieveContentList failure");
                            }
                            LocalesService.retrieveContentTypeList(success, failure);
                        }
                        scope.getListByFranchiseId = function (franchiseId) {
                            scope.selectedFranchise = franchiseId;
                            scope.clientSidePgintnOfLocales();
                        }
                        scope.getListByContentType = function (contentType) {
                            scope.selectedContentType = contentType;
                            scope.clientSidePgintnOfLocales();
                        }

                        retrieveContentList();

                    };
            /**
             * Comment this function will fill the combo while message translation will be shown
             */
            scope.initFnForMessageTranslation =
                    function () {
                        console.log(" initFnForMessageTranslation calling ");
                    };
            /**
             * Comment this function will fill the combo while master translation will be shown
             */
            scope.initFnForMasterTranslation =
                    function () {
                        console.log(" initFnForMasterTranslation calling ");
                    };
            /**
             * Comment this function will fill the combo while report translation will be shown
             */
            scope.initFnForReportTranslation =
                    function () {
                        scope.showAllLabels();
                    };
            /**
             * Comment this function will fill the combo while notification translation will be shown
             */
            scope.initFnForNotificationTranslation =
                    function () {
                        scope.clientSidePgintnOfLocales();
                    };



            hkg.register.filter('localeFilter', function () {
                return  function (items, searchText) {
                    if (angular.isDefined(items)) {
                        if (angular.isDefined(searchText) && searchText !== '') {
                            var filtered = [];
                            for (var i = 0; i < items.length; i++) {
                                var item = items[i];
                                if ((item.defaultText !== null && item.defaultText.toLowerCase().indexOf(searchText.toLowerCase()) >= 0)
                                        || (item.entity !== null && item.entity.toLowerCase().indexOf(searchText.toLowerCase()) >= 0)
                                        || (item.text !== null && item.text.toString().toLowerCase().indexOf(searchText.toLowerCase()) >= 0))
                                {
                                    filtered.push(item);
                                }
                            }
                            return filtered;
                        } else {
                            return items;
                        }
                    }
                };
            });


            $rootScope.unMaskLoading();
        }]);
});
//        ........................................................

//            /**
//             * Comment
//             */
//            scope.serverSidePgintnOfLocales = function(data, recordsConfig, callback) {
//                //Success function executes if record successfully retrieved.               
//                if (angular.isDefined(data)) {
//                    data.customParameters = {"language": "EN", "category": scope.selectedCategoryInTree.id};
//                    scope.responceComplete = false;
//                    var success = function(res) {
//                        angular.forEach(res, function(row, index) {
//                            recordsConfig.data.push([row.defaultText, row.entity, "<input type='text' value="+row.text+"></input>"]);
//                        });
////                        console.log("::: " + JSON.stringify(res));
//                        scope.serverRecordsOfLocales = res.records;
//                        recordsConfig.recordsTotal = res.length;
//                        recordsConfig.recordsFiltered = res.length;
//                        scope.responceComplete = true;
//                        callback(recordsConfig);
//                    }
//                    //Failure function executes if record could not be retrieved.
//                    var failure = function(res) {
//                        console.log("failure");
//                    }
//                    LocalesService.retrieveLabelsAsPerScroll(data, success, failure);
//                }
//            };


//LocalesService.retrieveLabelsAsPerScroll({"draw":1,"columns":[{"data":0,"name":"","searchable":true,"orderable":true,"search":{"value":"","regex":false}},{"data":1,"name":"","searchable":true,"orderable":true,"search":{"value":"","regex":false}},{"data":2,"name":"","searchable":true,"orderable":true,"search":{"value":"","regex":false}},{"data":3,"name":"","searchable":true,"orderable":true,"search":{"value":"","regex":false}}],"order":[{"column":0,"dir":"asc"}],"start":0,"length":99,"search":{"value":"","regex":false},"customParameters":{"language":"EN","category":"LABEL"}},function (res){alert(res);},function (){});


