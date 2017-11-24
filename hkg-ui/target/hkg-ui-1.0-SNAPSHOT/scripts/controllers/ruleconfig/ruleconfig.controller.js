/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
define(['hkg', 'ruleConfigService', 'colorpicker.directive', 'designationService', 'ruleTemplate'], function (hkg) {
    hkg.register.controller('RuleConfigController', ["$rootScope", "$scope", "RuleConfigService", "Designation", "$filter", "$q", "$timeout", function ($rootScope, $scope, RuleConfigService, $filter, Designation, $q, $timeout) {
            $scope.showExceptionLink = true;
            $scope.statusList = ["Active", "Remove"];
            $rootScope.mainMenu = "manageLink";
            $rootScope.childMenu = "configureRule";
            $rootScope.activateMenu();
            $scope.entity = 'CONFIG_RULE.';
            var self = this;
            $scope.open = function ($event) {
                $event.preventDefault();
                $event.stopPropagation();

            };

            $scope.openToDate = function ($event) {
                $event.preventDefault();
                $event.stopPropagation();

            };

            $scope.dateOptions = {
                formatYear: 'yy',
                startingDay: 1
            };

            $scope.format = $rootScope.dateFormat;
            $scope.$on('$viewContentLoaded', function () {
                $scope.reset();
                RuleConfigService.retrievePrerequisite(function (response)
                {
                    $scope.fieldList = [];

                    if (response !== null && response !== undefined && response.data !== null && response.data !== undefined)
                    {
                        $scope.featureList = response.data.featureList;

                        $scope.ruleTypes = response.data.ruleTypes;
                        $scope.ruleTypesFromDb = angular.copy(response.data.ruleTypes);
                        console.log("===========" + JSON.stringify(response.data.ruleTypes));
                        $scope.rules = response.data.rules;
                        if (response.data.ruleTypes !== null && response.data.ruleTypes !== undefined)
                        {
                            $scope.ruleTypes = [];
                            $scope.ruleTypesForTree = [];
                            for (var key in response.data.ruleTypes)
                            {
                                $scope.ruleTypes.push({
                                    id: key,
                                    text: response.data.ruleTypes[key]
                                });
                            }
                            ;
                            $scope.ruleTypesForTree = angular.copy($scope.ruleTypes);
                            $scope.ruleTypesForTree.push({id: "All", text: "All"});
                            $scope.ruleTypeForTree = "All";
                            console.log("asd" + JSON.stringify($scope.ruleTypesForTree))


                        }
                        ;
                        if (response.data.featureList !== null && response.data.featureList !== undefined)
                        {

                            $scope.autoCompleteFeature = {
                                allowClear: true,
                                multiple: true,
                                closeOnSelect: false,
                                placeholder: 'Select purchased by',
                                initSelection: function (element, callback) {
                                    if ($scope.isEditFlag)
                                    {
                                        $scope.names = [];
                                        console.log("in eit" + $scope.rule.features)
                                        $scope.temp = $scope.rule.features;
                                        $scope.fArray = [];
                                        $scope.fArray = $scope.rule.features.split(",");
                                        angular.forEach($scope.featureList, function (item) {
                                            for (var k in $scope.fArray) {
                                                if (item.value.toString() === $scope.fArray[k]) {
                                                    $scope.names.push({
                                                        id: item.value,
                                                        text: item.label
                                                    });
                                                }
                                            }
                                        });
                                        $scope.rule.features = $scope.temp;
                                        callback($scope.names);
                                    }

                                },
                                formatResult: function (item) {
                                    return item.text;
                                },
                                formatSelection: function (item) {
                                    return item.text;
                                },
                                query: function (query) {
                                    var selected = query.term;
                                    $scope.names = [];
                                    if ($scope.featureList.length !== 0) {

                                        angular.forEach($scope.featureList, function (item) {
                                            if (selected !== null && selected !== undefined && selected.length > 0) {
                                                if (item.label.toString().toUpperCase().indexOf(selected.toString().toUpperCase()) > -1) {
                                                    $scope.names.push({
                                                        id: item.value,
                                                        text: item.label
                                                    });
                                                }
                                            } else {
                                                $scope.names.push({
                                                    id: item.value,
                                                    text: item.label
                                                });
                                            }
                                        });
                                    }
                                    query.callback({
                                        results: $scope.names
                                    });


                                }
                            };
                        }
// Field permission information code starts
                        if (response.data.fieldFeatureMap !== null && response.data.fieldFeatureMap !== undefined)
                        {
                            angular.forEach(response.data.fieldFeatureMap, function (item) {
                                if (item.length > 0) {
                                    angular.forEach(item, function (itemValue) {
                                        $scope.fieldList.push(itemValue);
                                    });
                                }

                            });
                            $scope.fieldListToSend = [];

                            $scope.fieldListToSend = angular.copy($scope.fieldList);
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
                            $scope.roughCalcyList = [];
                            $scope.purchaseList = [];
                            $scope.subLotList = [];
                            $scope.selectedFieldsForSearch = [];

                            $scope.selectedFieldsForParent = [];
                            $scope.combinedFieldListForSearch = [];
                            $scope.combinedFieldListForParent = [];
                            angular.forEach($scope.fieldList, function (item) {
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
                                } else if (item.entity === 'RoughCalcy') {
                                    $scope.roughCalcyList.push(item);
                                } else if (item.entity === 'SubLot') {
                                    $scope.subLotList.push(item);
                                } else if (item.entity === 'Purchase') {
                                    $scope.purchaseList.push(item);
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
                                    angular.forEach($scope.invoiceList, function (item) {
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
                                    angular.forEach($scope.parcelList, function (item) {
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
                                    angular.forEach($scope.lotList, function (item) {
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
                                    angular.forEach($scope.packetList, function (item) {
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
                                    angular.forEach($scope.issueList, function (item) {
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
                                    angular.forEach($scope.transferList, function (item) {
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
                                    angular.forEach($scope.sellList, function (item) {
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
                            //Roughcalcy fields
                            if ($scope.roughCalcyList.length > 0) {
                                $scope.combinedFieldListForSearch.push({
                                    modelName: '<strong>Rough Calcy</strong>',
                                    multiSelectGroup: true
                                });
                                if ($scope.roughCalcyList.length > 0) {
                                    angular.forEach($scope.roughCalcyList, function (item) {
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
                            if ($scope.roughCalcyList.length !== 0) {
                                $scope.combinedFieldListForSearch.push({
                                    multiSelectGroup: false
                                });
                            }
                            //subLot fields
                            if ($scope.subLotList.length > 0) {
                                $scope.combinedFieldListForSearch.push({
                                    modelName: '<strong>Sub Lot</strong>',
                                    multiSelectGroup: true
                                });
                                if ($scope.subLotList.length > 0) {
                                    angular.forEach($scope.subLotList, function (item) {
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
                            if ($scope.subLotList.length !== 0) {
                                $scope.combinedFieldListForSearch.push({
                                    multiSelectGroup: false
                                });
                            }
                            //Roughcalcy fields
                            if ($scope.purchaseList.length > 0) {
                                $scope.combinedFieldListForSearch.push({
                                    modelName: '<strong>Rough Purchase</strong>',
                                    multiSelectGroup: true
                                });
                                if ($scope.purchaseList.length > 0) {
                                    angular.forEach($scope.purchaseList, function (item) {
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
                            if ($scope.purchaseList.length !== 0) {
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


                        }
// Field permission information code ends
                        if (response.data.rules !== null && response.data.rules !== undefined)
                        {
// for treee
                            $scope.ruleList = response.data.rules.R;
                            $scope.finalRuleList = response.data.rules.R;
                            $scope.exceptionList = response.data.rules.E;
                            // For Rule ui-select2
                            $scope.autoCompleteSkipRule = {
                                allowClear: true,
                                multiple: true,
                                closeOnSelect: false,
                                placeholder: 'Select purchased by',
                                initSelection: function (element, callback) {
                                    if ($scope.isEditFlag)
                                    {
                                        $scope.names = [];
                                        console.log("in eit" + $scope.rule.features)
                                        $scope.tempRules = $scope.rule.skipRules;
                                        $scope.rArray = [];
                                        $scope.rArray = $scope.rule.skipRules.split(",");
                                        angular.forEach($scope.ruleList, function (item) {
                                            for (var r in $scope.rArray) {
                                                if (item.id.toString() === $scope.rArray[r]) {
                                                    $scope.names.push({
                                                        id: item.id,
                                                        text: item.id + "-" + item.ruleName
                                                    });
                                                }
                                            }
                                        });
                                        $scope.rule.features = $scope.temp;
                                        callback($scope.names);
                                    }

                                },
                                formatResult: function (item) {
                                    return item.text;
                                },
                                formatSelection: function (item) {
                                    return item.text;
                                },
                                query: function (query) {
                                    var selected = query.term;
                                    console.log("ss" + selected)
                                    var success = function (response) {
                                        $scope.names = [];
                                        console.log("rs.." + JSON.stringify(response))
                                        if (response.length !== 0) {

                                            angular.forEach(response, function (item) {
                                                if (item.category !== null && item.category === 'General') {
                                                    $scope.names.push({
                                                        id: item.id,
                                                        text: item.id + "-" + item.ruleName
                                                    });
                                                }
                                            });
                                        }
                                        query.callback({
                                            results: $scope.names
                                        });
                                    }
                                    var failure = function ()
                                    {

                                    }
                                    RuleConfigService.searchrules({q: selected}, success, failure, function (response)
                                    {


                                    });


                                }
                            };


                        }
                    }
                });


            });
            $scope.clearTreeNodeSelectedForRule = function ()
            {
                console.log("in treeeee..." + JSON.stringify($scope.selectedRule));
                if ($scope.selectedRule !== undefined && $scope.selectedRule.currentNode !== undefined) {

                    $scope.selectedRule.currentNode.selected = undefined;
                }
                ;
            };
            $scope.clearTreeNodeSelectedForExcption = function ()
            {
                if ($scope.selectedException !== undefined && $scope.selectedException.currentNode !== undefined) {

                    $scope.selectedException.currentNode.selected = undefined;
                }
                ;
            };
            $scope.cancel = function ()
            {
                $scope.setRuleAsFeature();
            };
            $scope.clearSelectedSearchFields = function () {
                if ($scope.combinedFieldListForSearch !== null && $scope.combinedFieldListForSearch !== undefined && $scope.combinedFieldListForSearch.length > 0) {
                    angular.forEach($scope.combinedFieldListForSearch, function (item) {
                        item.ticked = false;
                    });
                }
            };
            $scope.reset = function ()
            {
                $scope.rule = {};
                $scope.submittedForm = false;
                $scope.isRulePage = true;
                $scope.isEditFlag = false;
                $scope.reloadFeature = false;
                $timeout(function () {
                    $scope.reloadFeature = true;
                }, 50);

                $scope.reloadSkipRule = false;
                $timeout(function () {
                    $scope.reloadSkipRule = true;
                }, 50);
//                if ($scope.selectedRule !== undefined && $scope.selectedRule.currentNode !== undefined) {
//
//                    $scope.selectedRule.currentNode.selected = undefined;
//                }
//                ;
                $scope.displayRulepage = 'view';
                $scope.reloadTree = true;
                $scope.clearSelectedSearchFields();
                //$scope.selectedFieldsForSearch = [];
            };
            $scope.setRuleAsFeature = function ()
            {
                $scope.reset();
                // These two flags are only for maintaining link
                $scope.setRuleParameters();
                $scope.clearTreeNodeSelectedForExcption();
                $scope.clearTreeNodeSelectedForRule();

            };
            $scope.setRuleParameters = function ()
            {
                $scope.showRuleLink = false;
                $scope.showExceptionLink = true;

                $scope.isRulePage = true;
                $scope.isExceptionPage = false;

            }
            $scope.setExceptionAsFeature = function ()
            {
                $scope.reset();

                $scope.setExceptionParameters();
                $scope.clearTreeNodeSelectedForExcption();
                $scope.clearTreeNodeSelectedForRule();
            };
            $scope.setExceptionParameters = function ()
            {
                $scope.showRuleLink = true;
                $scope.showExceptionLink = false;
                $scope.isExceptionPage = true;
                $scope.isRulePage = false;
            }
            $scope.saveRule = function (form)
            {
                $scope.submittedForm = true;
                if ($scope.rule.status === 'Remove') {
                    $scope.confirmationForRemoveRule();
                } else {
                    if ($scope.rule.ruleList !== null && $scope.rule.ruleList !== undefined)
                    {
//                        console.log("rulelist...." + JSON.stringify($scope.rule.ruleList))
                        angular.forEach($scope.rule.ruleList, function (ruleList)
                        {
                            if (ruleList.rowsubmitted === false)
                            {
                                ruleList.rowsubmitted = true;
                            }
                        });

                    }
                    if (form.$valid)
                    {

                        var ruleDataBean = {};
//                        console.log("rule page" + $scope.isRulePage);
                        if ($scope.isRulePage)
                        {
                            ruleDataBean.category = "R";
                        }
                        if ($scope.isExceptionPage)
                        {
                            ruleDataBean.category = "E";
                            var skipRuleIds = [];
                            if ($scope.rule.skipRules !== null && $scope.rule.skipRules !== undefined)
                            {
                                skipRuleIds = $scope.rule.skipRules.split(",");
                                ruleDataBean.skipRules = skipRuleIds;
                            }
                        }
                        ruleDataBean.ruleName = $scope.rule.ruleName;
                        ruleDataBean.ruleNumber = $scope.rule.ruleNumber;
                        ruleDataBean.type = $scope.rule.type;
                        ruleDataBean.description = $scope.rule.description;
                        ruleDataBean.fromDate = $scope.rule.fromDate;
                        ruleDataBean.toDate = $scope.rule.toDate;
                        ruleDataBean.status = "A";
                        ruleDataBean.tooltipMsg = $scope.rule.tooltipMsg;
                        ruleDataBean.validationMessage = $scope.rule.validationMessage;
                        ruleDataBean.colorCode = $scope.rule.colorCode;
                        console.log(JSON.stringify($scope.rule.criterias));
                        ruleDataBean.criterias = $scope.rule.criterias;
                        if ($scope.entityId !== undefined && $scope.entityId !== null && $scope.entityId < 1) {
                            ruleDataBean.apply = new Date($rootScope.getCurrentServerDate());
                            ruleDataBean.entityName = "Login";
                            var ids = [];
                            ids.push(-1);
                            ruleDataBean.features = ids;
                            console.log(ruleDataBean.features);
                        } else {
                            ruleDataBean.apply = $scope.rule.apply;
                        }
                        var featureIds = [];
                        var fieldIdsToBeApplied = [];
                        if ($scope.rule.features !== null && $scope.rule.features !== undefined && $scope.rule.features.length > 0)
                        {
                            featureIds = $scope.rule.features.split(",");
                            console.log("----------------------" + featureIds)
                            ruleDataBean.features = featureIds;

                        }
                        if ($scope.selectedFieldsForSearch !== null && $scope.selectedFieldsForSearch !== undefined)
                        {
                            angular.forEach($scope.selectedFieldsForSearch, function (selectedFieldsForSearch)
                            {
                                fieldIdsToBeApplied.push(selectedFieldsForSearch.field);
                            });
                            ruleDataBean.fieldsToBeApplied = angular.copy(fieldIdsToBeApplied);
                        }
                        console.log("in save method" + JSON.stringify(ruleDataBean));
//                        $rootScope.unMaskLoading();
                        RuleConfigService.saveRule(ruleDataBean, function ()
                        {
                            $scope.cancel();
                            $scope.reset();
//                            alert('clear');
                            $scope.clearTreeNodeSelectedForExcption();
                            $scope.clearTreeNodeSelectedForRule();
                            $scope.reteieveAllRules();
//                         
                        });

                    }
                }
            };
            $scope.editRule = function (ruleObj, redirectFrom)
            {
                $scope.reset();
//                console.log(JSON.stringify($scope.combinedFieldListForSearch));
                $scope.reloadFeature = false;
                $scope.reloadSkipRule = false;
                $scope.isEditFlag = true;
                console.log("ruleObj..." + JSON.stringify(ruleObj));
                console.log("edit rule///true" + $scope.isEditFlag)
                var id;
                if (redirectFrom === "tree")
                {
                    id = ruleObj.currentNode.id;
                }
                if (redirectFrom === "exceptionLink")
                {
                    id = ruleObj.value;
                }
                if (redirectFrom === "editRuleFromSearch")
                {
                    id = ruleObj;
                }

                console.log("as" + id);
                console.log("id" + id)

                RuleConfigService.retrieveRulebyRulenumber(id, function (response)
                {

                    var ruleData = angular.copy(response.data);
                    if (ruleData !== undefined && ruleData !== null && ruleData.criterias !== undefined && ruleData.criterias !== null) {
                        if (ruleData.criterias[0].entity < 0) {
                            $scope.entityId = ruleData.criterias[0].entity;
                        }
                    }
                    $scope.rule = {};
                    if (ruleData.category !== null && ruleData.category === 'R')
                    {
                        $scope.setRuleParameters();
                        $scope.rule.features = response.data.features.toString().replace("[", "").replace("]", "");
                        $scope.reloadFeature = true;
                        if (ruleData.exceptionList !== null && ruleData.exceptionList !== undefined)
                        {
                            $scope.exceptionListForRule = angular.copy(ruleData.exceptionList);

                        }
                        if ($scope.selectedException !== undefined && $scope.selectedException.currentNode !== undefined) {

                            $scope.selectedException.currentNode.selected = undefined;
                        }
                        ;
                        $scope.searchedRuleTree = response.data.ruleNumber;
                        $scope.searchedExceptionTree = "";
                    }
                    if (ruleData.category !== null && ruleData.category === 'E')
                    {
                        $scope.setExceptionParameters();
                        $scope.rule.skipRules = response.data.skipRules.toString().replace("[", "").replace("]", "");
                        console.log("dsfds" + $scope.rule.skipRules)
                        $scope.reloadSkipRule = true;
                        console.log("exc selcted" + JSON.stringify($scope.selectedRule))

                        if ($scope.selectedRule !== undefined && $scope.selectedRule.currentNode !== undefined) {

                            $scope.selectedRule.currentNode.selected = undefined;
                        }
                        ;
                        $scope.searchedExceptionTree = response.data.ruleNumber;
                        $scope.searchedRuleTree = "";
                    }
                    $scope.rule.id = response.data.id;
                    $scope.rule.ruleName = response.data.ruleName;

                    $scope.rule.ruleNumber = response.data.ruleNumber;
                    $scope.rule.category = response.data.category;
                    $scope.rule.description = response.data.description;
                    $scope.rule.type = response.data.type;
                    $scope.rule.fromDate = response.data.fromDate;
                    $scope.rule.toDate = response.data.toDate;
                    $scope.rule.apply = response.data.apply;
                    if (response.data.status === "A") {
                        $scope.rule.status = "Active";
                    }
                    $scope.rule.criterias = response.data.criterias;
                    $scope.rule.validationMessage = response.data.validationMessage;
                    $scope.rule.colorCode = response.data.colorCode;
                    $scope.rule.tooltipMsg = response.data.tooltipMsg;
                    $scope.rule.apply = response.data.apply;

                    var fieldToBeApplied = [];
                    fieldToBeApplied = response.data.fieldsToBeApplied;
                    $scope.modelName = [];
//                        console.log("combine" + JSON.stringify($scope.combinedFieldListForSearch))
                    if ($scope.combinedFieldListForSearch !== null && $scope.combinedFieldListForSearch !== undefined) {
                        angular.forEach($scope.combinedFieldListForSearch, function (editItem) {

                            if (editItem.modelName !== null && editItem.modelName !== undefined && editItem.multiSelectGroup !== null && editItem.multiSelectGroup !== undefined) {
                            } else
                            {
                                if (editItem.field !== null && editItem.field !== undefined) {
                                    for (var k in fieldToBeApplied)
                                    {
                                        if (editItem.field.toString() === fieldToBeApplied[k].toString())
                                        {
                                            editItem.ticked = true;
                                            $scope.modelName.push(editItem);
                                        }
                                    }
                                }
                            }


                        });
                    }
                    $scope.selectedFieldsForSearch = angular.copy($scope.modelName);




//                    console.log("Response...." + JSON.stringify(response))
                    console.log("edit rule///true after...." + $scope.isEditFlag)
                })
            };
            $scope.setSearchField = function (selectedFieldsForSearch) {
                $scope.selectedFieldsForSearch = angular.copy(selectedFieldsForSearch);
            };
            $scope.redirectToException = function (exception)
            {
                console.log("exception///" + JSON.stringify(exception));
                if ($scope.selectedRule !== undefined && $scope.selectedRule.currentNode !== undefined) {

                    $scope.selectedRule.currentNode.selected = undefined;
                }
                ;
                $scope.editRule(exception, "exceptionLink");
            };
            $scope.sortTreeByType = function (ruleType)
            {

                $scope.ruleList = [];
                if ($scope.finalRuleList !== null && $scope.finalRuleList !== undefined)
                {
                    console.log("rrr" + ruleType)
                    console.log("tttt" + JSON.stringify($scope.finalRuleList))
                    if (ruleType !== "All") {
                        angular.forEach($scope.finalRuleList, function (tmp)
                        {
                            if (tmp.type.toString() === ruleType.toString())
                            {
                                $scope.ruleList.push(tmp);
                            }
                        });
                    } else
                    {
                        $scope.ruleList = angular.copy($scope.finalRuleList);
                    }
                }
            };
            $scope.editRuleFromSearch = function (id)
            {

                console.log("id. in serachhhh.." + id)
                $scope.editRule(id, "editRuleFromSearch");
            };
            $scope.setViewFlag = function (flag) {
                if (flag !== "search") {
                    $('#scrollable-dropdown-menu.typeahead').typeahead('val', '');
                }
                $scope.displayRulepage = flag;
            };
            $scope.getSearchedRules = function (list) {
                $scope.clearTreeNodeSelectedForExcption();
                $scope.clearTreeNodeSelectedForRule();
                $scope.customLabel = "";
                var enteredText = $('#searchCustomField.typeahead').typeahead('val');
                if (enteredText.length > 0) {
                    if (enteredText.length < 3) {
                        $scope.searchRecords = [];
                    } else {
                        $scope.searchRecords = [];
                        angular.forEach(list, function (item) {
                            console.log("itemmmm" + JSON.stringify(item))
//                            var fieldDetails = item.text.split(",");


                            $scope.searchRecords.push(item);
                        });
                    }
                    $scope.setViewFlag('search');
                }
            };
            $scope.confirmationForRemoveRule = function () {
                $('#messageModal').modal('show');
            };
            $scope.removeRule = function ()
            {
                $rootScope.maskLoading();
                $scope.disableOkForRemove = true;
                RuleConfigService.removeRule($scope.rule.ruleNumber, function ()
                {

                    $('#messageModal').modal('hide');
                    $rootScope.removeModalOpenCssAfterModalHide();
                    $('.modal-backdrop').remove();
                    $rootScope.unMaskLoading();
                    $scope.disableOkForRemove = false;
                    $scope.reset();
                    $scope.clearTreeNodeSelectedForExcption();
                    $scope.clearTreeNodeSelectedForRule();
                    var msg = "Rule removed successfully";
                    var type = $rootScope.success;
                    $rootScope.addMessage(msg, type);
                });

            };
            $scope.hideconformationForRemoveRule = function () {
                $('#messageModal').modal('hide');
                $rootScope.removeModalOpenCssAfterModalHide();
                $('.modal-backdrop').remove();
                $scope.rule.status = 'Active';
            };
            $scope.reteieveAllRules = function ()
            {
                RuleConfigService.retrieveAllRuleList(function (response)
                {
                    if (response.R !== null && response.R !== undefined)
                    {
                        $scope.ruleList = response.R;
                        $scope.finalRuleList = response.R;
                        $scope.exceptionList = response.E;
                        $scope.reloadTree = false;
                        $timeout(function () {
                            $scope.reloadTree = true;
                        }, 50);

                    }
                    ;
                });
            };
        }]);
});




