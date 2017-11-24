/**
 * 
 * Author: Gautam
 * 
 * 
 */

define(['angular', 'designationService', 'messageService'], function() {
    globalProvider.compileProvider.directive('designationTemplate',
            ['$rootScope', '$filter', '$templateCache', 'Designation', 'Messaging', function($rootScope, $filter, $templateCache, Designation, Messaging) {

                    var scope = {
                        diamondSystemFeatureList: '=?',
                        systemFeaturesList: '=?',
                        reportSystemFeatureList: '=?',
                        groupFeatureMap: '=?',
                        featureFieldMap: '=?',
                        featureModifierMap: '=?',
                        uniqueIndex: '@',
                        designationId: '@'
                    };

                    var link = function (scope, element, attrs) {

                    };

                    var controller = ["$scope", "$element", "$attrs", "$rootScope", "$timeout", "$filter", "$location", "$window", "$log", function ($scope, $element, $attrs, $rootScope, $timeout, $filter, $location, $window, $log) {

                            if ($scope.uniqueIndex === undefined || $scope.uniqueIndex === null) {
                                $scope.uniqueIndex = null;
                            }
                            if ($scope.diamondSystemFeatureList === undefined || $scope.diamondSystemFeatureList === null) {
                                $scope.diamondSystemFeatureList = [];
                            }
                            if ($scope.systemFeaturesList === undefined || $scope.systemFeaturesList === null) {
                                $scope.systemFeaturesList = [];
                            }
                            if ($scope.reportSystemFeatureList === undefined || $scope.reportSystemFeatureList === null) {
                                $scope.reportSystemFeatureList = [];
                            }
                            if ($scope.groupFeatureMap === undefined || $scope.groupFeatureMap === null) {
                                $scope.groupFeatureMap = [];
                            }
                            $scope.flags = {};
                            $scope.flags.modifierSubmitted = false;
                            $scope.tmp = {"text": "Show plans", "id": "SP", "value": true};
                            /** Designation part starts **/

                            $scope.fieldList = [];
                            $scope.commonList = [];
                            if ($scope.featureFieldMap === undefined || $scope.featureFieldMap === null) {
                                $scope.featureFieldMap = {};
                            }
                            if ($scope.featureModifierMap === undefined || $scope.featureModifierMap === null) {
                                $scope.featureModifierMap = {};
                            }
                            $scope.isEditing = true;
                            $scope.firstPage = true;

                            //Map used to store modifiers required for features. References to allmodifiers.
                            $scope.fixedFeatureModifiersMap = {
                                'issue_receive': ["medium", "type", "mode", "access"],
                                'allotment': ["designation"],
                                'estimate_prediction': ["plan"]
                            };
                            $scope.AllModifiers = [
                                {"values": [{"text": "Rough", "id": "R", "value": false}, {"text": "Lot", "id": "L", "value": false}, {"text": "Packet", "id": "P", "value": false}], "text": "medium"},
                                {"values": [{"text": "Inward", "id": "IW", "value": false}, {"text": "Outward", "id": "OW", "value": false}], "text": "type"},
                                {"values": [{"text": "Direct", "id": "DR", "value": false}, {"text": "Via StockRoom", "id": "VS", "value": false}], "text": "mode"},
                                {"values": [{"text": "Request", "id": "RQ", "value": false}, {"text": "Collect", "id": "CL", "value": false}, {"text": "Issue", "id": "IS", "value": false}, {"text": "Receive", "id": "RC", "value": false}, {"text": "Return", "id": "RT", "value": false}], "text": "access"},
                                {"values": [{"text": "Designation", "id": "DES", value: ""}], "text": "designation"},
                                {"values": [{"text": "Show plans", "id": "SP", value: ""}, {"text": "Add plans", "id": "AP", value: ""}, {"text": "Copy Plan", "id": "CP", value: ""}, {"text": "Delete Plan", "id": "DP", value: ""}, {"text": "Finalize Plan", "id": "FP", value: ""}], "text": "plan"}
                            ];

                            //Section related constants
                            $scope.featureSectionEntitysMap = {
                                'rough_invoice_add_edit': [
                                    {
                                        sectionId: 'GEN', sectionName: 'General Section',
                                        entitys: [{id: 'Invoice', name: 'Invoice'}]
                                    },
                                    {
                                        sectionId: 'MRP', sectionName: 'Rough Parcel',
                                        entitys: [{id: 'Parcel', name: 'Parcel'}]
                                    }],
                                'rough_purchase_add_edit': [
                                    {
                                        sectionId: 'GEN', sectionName: 'General Section',
                                        entitys: [{id: 'Purchase', name: 'Purchase'}]
                                    }],
                                'link_rough_purchase': [
                                    {
                                        sectionId: 'GEN', sectionName: 'General Section',
                                        entitys: [{id: 'Purchase', name: 'Purchase'}]
                                    },
                                    {
                                        sectionId: 'RPA', sectionName: 'Rough Parcel',
                                        entitys: [{id: 'Parcel', name: 'Parcel'}]
                                    }],
                                'lot_add': [
                                    {
                                        sectionId: 'GEN', sectionName: 'General Section',
                                        entitys: [{id: 'Lot', name: 'Lot'}]
                                    },
                                    {
                                        sectionId: 'ASL', sectionName: 'Associate Sub Lot',
                                        entitys: [{id: 'SubLot', name: 'SubLot'}]
                                    }],
                                'lot_edit': [
                                    {
                                        sectionId: 'GEN', sectionName: 'General Section',
                                        entitys: [{id: 'Lot', name: 'Lot'}]
                                    },
                                    {
                                        sectionId: 'ASL', sectionName: 'Associate Sub Lot',
                                        entitys: [{id: 'SubLot', name: 'SubLot'}]
                                    }],
                                'stock_sell': [
                                    {
                                        sectionId: 'GEN', sectionName: 'General Section',
                                        entitys: [{id: 'Sell', name: 'Sell'}]
                                    }],
                                'rough_sale': [
                                    {
                                        sectionId: 'GEN', sectionName: 'General Section',
                                        entitys: [{id: 'Parcel', name: 'Parcel'}, {id: 'Sell', name: 'Sell'}]
                                    }],
                                'stock_transfer': [
                                    {
                                        sectionId: 'GEN', sectionName: 'General Section',
                                        entitys: [{id: 'Transfer', name: 'Transfer'}]
                                    }],
                                'stock_merge': [
                                    {
                                        sectionId: 'GEN', sectionName: 'General Section',
                                        entitys: [{id: 'Lot', name: 'Lot'}, {id: 'Packet', name: 'Packet'}]
                                    }],
                                'packet_split': [
                                    {
                                        sectionId: 'GEN', sectionName: 'General Section',
                                        entitys: [{id: 'Packet', name: 'Packet'}]
                                    }],
                                'packet_add': [
                                    {
                                        sectionId: 'GEN', sectionName: 'General Section',
                                        entitys: [{id: 'Packet', name: 'Packet'}]
                                    }],
                                'packet_edit': [
                                    {
                                        sectionId: 'GEN', sectionName: 'General Section',
                                        entitys: [{id: 'Packet', name: 'Packet'}]
                                    }],
                                'allotment': [
                                    {
                                        sectionId: 'GEN', sectionName: 'General Section',
                                        entitys: [{id: 'Packet', name: 'Packet'}]
                                    }],
                                'finalize': [
                                    {
                                        sectionId: 'GEN', sectionName: 'General Section',
                                        entitys: [{id: 'Plan', name: 'Plan'}]
                                    }],
                                'generate_barcode': [
                                    {
                                        sectionId: 'GEN', sectionName: 'General Section',
                                        entitys: [{id: 'Lot', name: 'Lot'}, {id: 'Packet', name: 'Packet'}, {id: 'Plan', name: 'Plan'}]
                                    }],
                                'generate_slip': [
                                    {
                                        sectionId: 'GEN', sectionName: 'General Section',
                                        entitys: [{id: 'Lot', name: 'Lot'}, {id: 'Packet', name: 'Packet'}]
                                    }],
                                'issue_receive': [
                                    {
                                        sectionId: 'GEN', sectionName: 'General Section',
                                        entitys: [{id: 'Issue', name: 'Issue'}]
                                    }, {
                                        sectionId: 'II', sectionName: 'Issue Inward',
                                        entitys: [{id: 'Issue', name: 'Issue'}]
                                    }, {
                                        sectionId: 'RI', sectionName: 'Receive Inward',
                                        entitys: [{id: 'Issue', name: 'Issue'}]
                                    }, {
                                        sectionId: 'RQ', sectionName: 'Request',
                                        entitys: [{id: 'Issue', name: 'Issue'}]
                                    }, {
                                        sectionId: 'CL', sectionName: 'Collect',
                                        entitys: [{id: 'Issue', name: 'Issue'}]
                                    }, {
                                        sectionId: 'IS', sectionName: 'Issue',
                                        entitys: [{id: 'Issue', name: 'Issue'}]
                                    }, {
                                        sectionId: 'RC', sectionName: 'Receive',
                                        entitys: [{id: 'Issue', name: 'Issue'}]
                                    }],
                                'write_service': [
                                    {
                                        sectionId: 'GEN', sectionName: 'General Section',
                                        entitys: [{id: 'Plan', name: 'Plan'}]
                                    }],
                                'print_static': [
                                    {
                                        sectionId: 'GEN', sectionName: 'General Section',
                                        entitys: [{id: 'Lot', name: 'Lot'}, {id: 'Packet', name: 'Packet'}]
                                    }],
                                'print_dynamic': [
                                    {
                                        sectionId: 'GEN', sectionName: 'General Section',
                                        entitys: [{id: 'Lot', name: 'Lot'}, {id: 'Packet', name: 'Packet'}]
                                    }],
                                'status_change': [
                                    {
                                        sectionId: 'GEN', sectionName: 'General Section',
                                        entitys: [{id: 'Lot', name: 'Lot'}, {id: 'Packet', name: 'Packet'}]
                                    }],
                                'rough_merge': [
                                    {
                                        sectionId: 'GEN', sectionName: 'General Section',
                                        entitys: [{id: 'Parcel', name: 'Parcel'}]
                                    }],
                                'rough_calcy': [
                                    {
                                        sectionId: 'GEN', sectionName: 'General Section',
                                        entitys: [{id: 'RoughCalcy', name: 'RoughCalcy'}]
                                    }],
                                'estimate_prediction': [
                                    {
                                        sectionId: 'GEN', sectionName: 'General Section',
                                        entitys: [{id: 'RoughCalcy', name: 'Rough Calcy'}]
                                    }],
                                'sub_lot': [
                                    {
                                        sectionId: 'GEN', sectionName: 'General Section',
                                        entitys: [{id: 'SubLot', name: 'SubLot'}]
                                    },
                                    {
                                        sectionId: 'INP', sectionName: 'Invoice/Parcel',
                                        entitys: [{id: 'Invoice', name: 'Invoice'}, {id: 'Parcel', name: 'Parcel'}]
                                    }],
                                'rough_makeable': [
                                    {
                                        sectionId: 'GEN', sectionName: 'General Section',
                                        entitys: [{id: 'Packet', name: 'Packet'}]
                                    },
                                    {
                                        sectionId: 'ARM', sectionName: 'Add Rough Makeable',
                                        entitys: [{id: 'RoughMakeable', name: 'RoughMakeable'}]
                                    },
                                    {
                                        sectionId: 'ARMRC', sectionName: 'Rough Calc',
                                        entitys: [{id: 'RoughCalcy', name: 'RoughCalcy'}]
                                    }
                                ],
                                'final_makeable': [
                                    {
                                        sectionId: 'GEN', sectionName: 'General Section',
                                        entitys: [{id: 'Packet', name: 'Packet'}]
                                    },
                                    {
                                        sectionId: 'AFM', sectionName: 'Add Final Makeable',
                                        entitys: [{id: 'FinalMakeable', name: 'FinalMakeable'}]
                                    },
                                    {
                                        sectionId: 'AFMRC', sectionName: 'Rough Calc',
                                        entitys: [{id: 'RoughCalcy', name: 'RoughCalcy'}]
                                    }
                                ]
                            };

                            //Not in use....just for reference, will be replaced by above configuration soon
                            $scope.visibleFields = {'invoice_add': [{id: 'Invoice', name: 'Invoice'}],
                                'invoice_edit': [{id: 'Invoice', name: 'Invoice'}],
                                'rough_invoice_add_edit': [{id: 'Invoice', name: 'Invoice'}],
                                'parcel_add_edit': [{id: 'Parcel', name: 'Parcel'}],
                                'rough_purchase_add_edit': [{id: 'Purchase', name: 'Purchase'}],
                                'lot_add': [{id: 'Lot', name: 'Lot'}],
                                'lot_edit': [{id: 'Lot', name: 'Lot'}],
                                'stock_sell': [{id: 'Sell', name: 'Sell'}],
                                'rough_sale': [{id: 'Parcel', name: 'Parcel'}, {id: 'Sell', name: 'Sell'}],
                                'stock_transfer': [{id: 'Transfer', name: 'Transfer'}],
                                'stock_merge': [{id: 'Lot', name: 'Lot'}, {id: 'Packet', name: 'Packet'}],
                                'packet_split': [{id: 'Packet', name: 'Packet'}],
                                'packet_add': [{id: 'Packet', name: 'Packet'}],
                                'packet_edit': [{id: 'Packet', name: 'Packet'}],
                                'allotment': [{id: 'Packet', name: 'Packet'}],
                                'finalize': [{id: 'Plan', name: 'Plan'}],
                                'generate_barcode': [{id: 'Lot', name: 'Lot'}, {id: 'Packet', name: 'Packet'}, {id: 'Plan', name: 'Plan'}, {id: 'Issue', name: 'Issue'}],
                                'generate_slip': [{id: 'Lot', name: 'Lot'}, {id: 'Packet', name: 'Packet'}],
                                'issue_receive': [{id: 'Lot', name: 'Lot'}, {id: 'Packet', name: 'Packet'}, {id: 'Issue', name: 'Issue'}],
                                'write_service': [{id: 'Plan', name: 'Plan'}],
                                'print_static': [{id: 'Lot', name: 'Lot'}, {id: 'Packet', name: 'Packet'}],
                                'print_dynamic': [{id: 'Lot', name: 'Lot'}, {id: 'Packet', name: 'Packet'}],
                                'status_change': [{id: 'Lot', name: 'Lot'}, {id: 'Packet', name: 'Packet'}],
                                'rough_merge': [{id: 'Parcel', name: 'Parcel'}],
                                'rough_calcy': [{id: 'RoughCalcy', name: 'Rough Calcy'}],
                                'estimate_prediction': [{id: 'RoughCalcy', name: 'Rough Calcy'}, {id: 'Plan', name: 'Plan'}],
                                                                'sub_lot': [{id: 'SubLot', name: 'SubLot'}, {id: 'Parcel', name: 'Parcel'}, {id: 'Invoice', name: 'Invoice'}],
                                'rough_makeable': [{id: 'Packet', name: 'Packet'}],
                                'final_makeable': [{id: 'Packet', name: 'Packet'}]
                            };

                            $scope.clearSelectedSearchFields = function () {
                                if ($scope.combinedFieldListForSearch !== null && $scope.combinedFieldListForSearch !== undefined && $scope.combinedFieldListForSearch.length > 0) {
                                    angular.forEach($scope.combinedFieldListForSearch, function (item) {
                                        item.ticked = false;
                                    });
                                }
                            };
                            $scope.clearSelectedParentFields = function () {
                                if ($scope.combinedFieldListForParent !== null && $scope.combinedFieldListForParent !== undefined && $scope.combinedFieldListForParent.length > 0) {
                                    angular.forEach($scope.combinedFieldListForParent, function (item) {
                                        item.ticked = false;
                                    });
                                }
                            };
                            //For custom order type to manipulate DOM sorting.
                            //Mapped to respective columns. null, if default required.
                            $scope.dataTableOptions = {
                                "columns": [
                                    null,
                                    null,
                                    {"orderDataType": "dom-text-numeric"}
                                ],
                                "autoWidth": false
                            };

                            $scope.retrieveAllSysFeatures = function () {
                                $rootScope.maskLoading();
                                Designation.retrieveSystemFeatures(function (data) {
                                    $rootScope.unMaskLoading();
                                    $scope.systemFeaturesList = data;
                                    if (!$scope.systemFeaturesList || $scope.systemFeaturesList === null) {
                                        $scope.systemFeaturesList = [];
                                    } else {
                                        $scope.tempSystemFeatureList = [];
                                        $scope.diamondSystemFeatureList = [];
                                        $scope.reportSystemFeatureList = [];
                                        angular.forEach($scope.systemFeaturesList, function (item) {
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
                                }, function () {

                                    $rootScope.unMaskLoading();
                                });
                            };
                            $scope.checkUncheckDiamondIA = function (feature) {
                                var iaLen = feature.iteamAttributesList.length;
                                for (var i = 0; i < iaLen; i++) {
                                    feature.iteamAttributesList[i].checked = feature.checked;
                                }
                                if (feature.configure && !feature.checked) {
                                    feature.configure = false;
                                    for (var key in $scope.featureFieldMap) {
                                        if (key == feature.id) {
                                            delete $scope.featureFieldMap[key];
                                        }
                                    }
                                }

                            };
                            $scope.retrieveFieldsByFeature = function () {
                                Designation.retrieveFieldsByFeature(function (res) {
                                    angular.forEach(res.data, function (item) {
                                        if (item.length > 0) {
                                            angular.forEach(item, function (itemValue) {
                                                $scope.fieldList.push(itemValue);
                                            });
                                        }

                                    });
                                });
                            };

                            $scope.checkAccessModifier = function () {
                                if ($scope.modifiers.mode) {
                                    var isAccess = false;
                                    angular.forEach($scope.modifiers.mode, function (item) {
                                        if (item.id === "VS" && item.value === true) {
                                            isAccess = true;
                                        }
                                    });
                                    if (isAccess) {
                                        $scope.modifiers.showAccessModifiers = true;
                                    } else {
                                        $scope.modifiers.showAccessModifiers = undefined;
                                    }
                                }
                            };
                            $scope.retrieveFieldsByFeature();
                            $scope.combinedFieldListForSearch = [];
                            $scope.configureFeature = function (diamondSystemFeature) {
                                if (diamondSystemFeature.checked) {
                                    $scope.diamondFeature = angular.copy(diamondSystemFeature);
                                    var label = $scope.diamondFeature.menuLabel.split('_');
                                    if (label[1]) {
                                        var menuLabel = label[1] + " " + label[0];
                                        $scope.labels = menuLabel;
                                    } else {
                                        var menuLabel = label[0];
                                        $scope.labels = menuLabel;
                                    }

                                    //Set modifiers.
                                    $scope.modifiers = {};
                                    if ($scope.fixedFeatureModifiersMap[$scope.diamondFeature.menuLabel] !== undefined) {
                                        angular.forEach($scope.AllModifiers, function (value) {
                                            if ($scope.fixedFeatureModifiersMap[$scope.diamondFeature.menuLabel].indexOf(value.text) > -1) {
                                                $scope.modifiers[value.text] = value.values;
                                            }
                                        });
                                    }
                                    if (Object.keys($scope.modifiers).length === 0) {
                                        $scope.modifiers.noRecords = true;
                                    }
//                                    console.log(JSON.stringify($scope.featureModifierMap[$scope.diamondFeature.id]));
                                    if ($scope.featureModifierMap[$scope.diamondFeature.id] !== undefined && $scope.featureModifierMap[$scope.diamondFeature.id] !== null) {
                                        var modifier = $scope.featureModifierMap[$scope.diamondFeature.id];
                                        if (modifier.iRMediums !== undefined && modifier.iRMediums !== null) {
                                            angular.forEach($scope.modifiers.medium, function (item) {
                                                if (modifier.iRMediums.indexOf(item.id) > -1) {
                                                    item.value = true;
                                                }
                                            });
                                        }
                                        if (modifier.iRTypes !== undefined && modifier.iRTypes !== null) {
                                            angular.forEach($scope.modifiers.type, function (item) {
                                                if (modifier.iRTypes.indexOf(item.id) > -1) {
                                                    item.value = true;
                                                }
                                            });
                                        }
                                        if (modifier.iRModes !== undefined && modifier.iRModes !== null) {
                                            angular.forEach($scope.modifiers.mode, function (item) {
                                                if (modifier.iRModes.indexOf(item.id) > -1) {
                                                    item.value = true;
                                                }
                                            });
                                        }
                                        if (modifier.iRVSRAccessRights !== undefined && modifier.iRVSRAccessRights !== null) {
                                            angular.forEach($scope.modifiers.access, function (item) {
                                                if (modifier.iRVSRAccessRights.indexOf(item.id) > -1) {
                                                    item.value = true;
                                                }
                                            });
                                        }
                                        if (modifier.asDesignation !== undefined && modifier.asDesignation !== null) {
                                            angular.forEach(modifier.asDesignation, function (item) {
                                                var a = item + ":R";
                                                if ($scope.modifiers.designation[0].value === "") {
                                                    $scope.modifiers.designation[0].value = $scope.modifiers.designation[0].value + a;
                                                } else {
                                                    $scope.modifiers.designation[0].value = $scope.modifiers.designation[0].value + "," + a;
                                                }

                                            });
                                        }
                                        if (modifier.planAccess !== undefined && modifier.planAccess !== null) {
                                            angular.forEach($scope.modifiers.plan, function (item) {
                                                if (modifier.planAccess.indexOf(item.id) > -1) {
                                                    item.value = true;
                                                }
                                            });
                                            if (modifier.showPlanUsers !== undefined && modifier.showPlanUsers !== null) {
                                                $scope.flags.planAccesArray = modifier.showPlanUsers;
                                                var codeToNames = modifier.recepientCodeToName;
                                                var ind = 0;
                                                angular.forEach($scope.flags.planAccesArray, function(item) {
                                                    var users = [];
                                                    if (angular.isObject(item.users)) {
                                                        if (item.users[0].id !== "") {
                                                            users = item.users[0].id.split(",");
                                                        }
                                                    } else {
                                                        if (item.users !== "") {
                                                            users = item.users.split(",");
                                                        }
                                                    }
                                                    item.autoCompleteInvitees = {
                                                        multiple: true,
                                                        closeOnSelect: false,
                                                        placeholder: 'Select members',
                                                        width: 'resolve',
                                                        initSelection: function(element, callback) {
                                                            var tempValues = [];
                                                            if (users !== undefined && users !== null && users.length > 0) {
                                                                angular.forEach(users, function(item) {
                                                                    tempValues.push({
                                                                        id: item,
                                                                        text: codeToNames[item]
                                                                    });
                                                                });
                                                            }
                                                            callback(tempValues);

                                                        },
                                                        formatResult: function(item) {
                                                            return item.text;
                                                        },
                                                        formatSelection: function(item) {
                                                            return item.text;
                                                        },
                                                        query: function(query) {
                                                            var selected = query.term;
                                                            $scope.names = [];
                                                            var success = function(data) {
                                                                if (data.length !== 0) {
                                                                    $scope.names = data;
                                                                    angular.forEach(data, function(item) {
                                                                        $scope.names.push({
                                                                            id: item.value + ":" + item.description,
                                                                            text: item.label

                                                                        });
                                                                    });
                                                                }
                                                                query.callback({
                                                                    results: $scope.names
                                                                });
                                                            };
                                                            var failure = function() {
                                                            };
                                                            if (selected.substring(0, 2) === '@E' || selected.substring(0, 2) === '@e') {
                                                                var search = query.term.slice(2);
                                                                Messaging.retrieveUserList(search.trim(), success, failure);
                                                            } else if (selected.substring(0, 2) === '@D' || selected.substring(0, 2) === '@d') {
                                                                var search = query.term.slice(2);
                                                                Messaging.retrieveDepartmentList(search.trim(), success, failure);
                                                            } else if (selected.substring(0, 2) === '@R' || selected.substring(0, 2) === '@r') {
                                                                var search = query.term.slice(2);
                                                                Messaging.retrieveRoleList(search.trim(), success, failure);
                                                            }
                                                        }
                                                    }
//                                                    $('#users' + ind).select2('data', users);
                                                    ++ind;
                                                });
                                                $scope.flags.planFlag = true;
                                            }
                                        }
                                    }
                                    $scope.checkAccessModifier();

//                                    console.log(JSON.stringify($scope.diamondFeature));

                                    $scope.type = $scope.diamondFeature.type;
                                    $scope.featureEntity = label[0];
//                    if ($scope.type === "DEI" || $scope.labels === "static print" || $scope.labels === "merge stock") {
//                        $scope.entityList = [{id: 'Lot', name: 'Lot'}, {id: 'Packet', name: 'Packet'}];
                                    $scope.sectionList = $scope.featureSectionEntitysMap[$scope.diamondFeature.menuLabel];
                                    angular.forEach($scope.sectionList, function (section) {
                                        if (section.sectionId === 'GEN') {
                                            $scope.entityList = section.entitys;
                                        }
                                    });
                                    //$scope.entityList = $scope.visibleFields[$scope.diamondFeature.menuLabel];
                                    if (!!$scope.entityList) {
                                        if (!!!$scope.entitys) {
                                            $scope.entitys = {};
                                        }
                                        $scope.entitys.section = 'GEN';
                                        $scope.entitys.name = $scope.entityList[0].id;
                                    }
//                    }
                                    var temp = '$scope.invoiceList';
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
                                    $scope.selectedFieldsForSearch = [];
                                    $scope.clearSelectedSearchFields();
                                    $scope.clearSelectedParentFields();
                                    $scope.selectedFieldsForParent = [];
                                    $scope.combinedFieldListForSearch = [];
                                    $scope.combinedFieldListForParent = [];
                                    $scope.subLotList = [];
                                    $scope.roughMakeableList = [];
                                    $scope.finalMakeableList = [];
//                    $scope.fieldList = [];
                                    $scope.fieldListToSend = [];
                                    $scope.fieldListToSendTemp = [];
//                    Designation.retrieveFieldsByFeature(function (res) {
//                        angular.forEach(res.data, function (item) {
//                            if (item.length > 0) {
//                                angular.forEach(item, function (itemValue) {
//                                    $scope.fieldList.push(itemValue);
//                                });
//                            }
//
//                        });
//                                    $scope.fieldListToSend = angular.copy($scope.fieldList);
                                    angular.forEach($scope.fieldList, function (item) {
                                        item.sectionCode = 'GEN';
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
                                        } else if (item.entity === 'RoughMakeable') {
                                            $scope.roughMakeableList.push(item);
                                        }else if (item.entity === 'FinalMakeable') {
                                            $scope.finalMakeableList.push(item);
                                        }

                                        if ($scope.designationId !== null && $scope.designationId !== undefined) {
                                            item.designation = parseInt($scope.designationId);
                                        }
                                    });
                                    $scope.fieldListToSend = angular.copy($scope.fieldList);
                                    //For temporary purpose. Used for other sections than general.
                                    $scope.fieldListToSendTemp = angular.copy($scope.fieldList);
//                                    console.log(JSON.stringify($scope.fieldList));
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

                                    //Rough makeable fields
                                    if ($scope.roughMakeableList.length > 0) {
                                        $scope.combinedFieldListForSearch.push({
                                            modelName: '<strong>Sub Lot</strong>',
                                            multiSelectGroup: true
                                        });
                                        if ($scope.roughMakeableList.length > 0) {
                                            angular.forEach($scope.roughMakeableList, function(item) {
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
                                    if ($scope.roughMakeableList.length !== 0) {
                                        $scope.combinedFieldListForSearch.push({
                                            multiSelectGroup: false
                                        });
                                    }
                                    
                                    //Final makeable fields
                                     if ($scope.finalMakeableList.length > 0) {
                                        $scope.combinedFieldListForSearch.push({
                                            modelName: '<strong>Sub Lot</strong>',
                                            multiSelectGroup: true
                                        });
                                        if ($scope.finalMakeableList.length > 0) {
                                            angular.forEach($scope.finalMakeableList, function(item) {
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
                                    if ($scope.finalMakeableList.length !== 0) {
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
                                        "Sell": $scope.sellList,
                                        "RoughCalcy": $scope.roughCalcyList,
                                        "SubLot": $scope.subLotList,
                                        "Purchase": $scope.purchaseList,
                                        "RoughMakeable": $scope.roughMakeableList,
                                        "FinalMakeable": $scope.finalMakeableList
                                    };

                                    //----Combining the list to prepare data for multiselect checkbox for parent entities--------------
                                    if ($scope.invoiceList.length > 0) {
                                        $scope.combinedFieldListForParent.push(
                                                {
                                                    modelName: '<strong>Invoice</strong>',
                                                    multiSelectGroup1: true
                                                });
                                        if ($scope.invoiceList.length > 0) {
                                            angular.forEach($scope.invoiceList, function (item) {
                                                $scope.combinedFieldListForParent.push(angular.copy(item));
                                            });
                                        }
                                    }
                                    if ($scope.invoiceList.length !== 0) {
                                        $scope.combinedFieldListForParent.push({
                                            multiSelectGroup1: false
                                        });
                                    }
                                    //parcel fields are added to parent list if parcel entity contains any.
                                    if ($scope.parcelList.length > 0) {
                                        $scope.combinedFieldListForParent.push(
                                                {
                                                    modelName: '<strong>Parcel</strong>',
                                                    multiSelectGroup1: true
                                                });
                                        if ($scope.parcelList.length > 0) {
                                            angular.forEach($scope.parcelList, function (item) {
                                                $scope.combinedFieldListForParent.push(angular.copy(item));
                                            });
                                        }
                                    }

                                    if ($scope.parcelList.length !== 0) {
                                        $scope.combinedFieldListForParent.push({
                                            multiSelectGroup1: false
                                        });
                                    }
                                    //lot fields are added to parent list if lot entity contains any.
                                    if ($scope.lotList.length > 0) {
                                        $scope.combinedFieldListForParent.push(
                                                {
                                                    modelName: '<strong>Lot</strong>',
                                                    multiSelectGroup1: true
                                                });
                                        if ($scope.lotList.length > 0) {
                                            angular.forEach($scope.lotList, function (item) {
                                                $scope.combinedFieldListForParent.push(angular.copy(item));
                                            });
                                        }
                                    }
                                    if ($scope.lotList.length !== 0) {
                                        $scope.combinedFieldListForParent.push({
                                            multiSelectGroup1: false
                                        });
                                    }
                                    //packet fields are added to parent list if packet entity contains any.
                                    if ($scope.packetList.length > 0) {
                                        $scope.combinedFieldListForParent.push({
                                            modelName: '<strong>Packet</strong>',
                                            multiSelectGroup1: true
                                        });
                                        if ($scope.packetList.length > 0) {
                                            angular.forEach($scope.packetList, function (item) {
                                                $scope.combinedFieldListForParent.push(angular.copy(item));
                                            });
                                        }
                                    }
                                    if ($scope.packetList.length !== 0) {
                                        $scope.combinedFieldListForParent.push({
                                            multiSelectGroup1: false
                                        });
                                    }
                                    //issue fields are added to parent list if issue entity contains any.
                                    if ($scope.issueList.length > 0) {
                                        $scope.combinedFieldListForParent.push({
                                            modelName: '<strong>Issue</strong>',
                                            multiSelectGroup1: true
                                        });
                                        if ($scope.issueList.length > 0) {
                                            angular.forEach($scope.issueList, function (item) {
                                                $scope.combinedFieldListForParent.push(angular.copy(item));
                                            });
                                        }
                                    }
                                    if ($scope.issueList.length !== 0) {
                                        $scope.combinedFieldListForParent.push({
                                            multiSelectGroup1: false
                                        });
                                    }
                                    if ($scope.roughCalcyList.length > 0) {
                                        $scope.combinedFieldListForParent.push({
                                            modelName: '<strong>Rough Calcy</strong>',
                                            multiSelectGroup1: true
                                        });
                                        if ($scope.roughCalcyList.length > 0) {
                                            angular.forEach($scope.roughCalcyList, function (item) {
                                                $scope.combinedFieldListForParent.push(angular.copy(item));
                                            });
                                        }
                                    }
                                    if ($scope.roughCalcyList.length !== 0) {
                                        $scope.combinedFieldListForParent.push({
                                            multiSelectGroup1: false
                                        });
                                    }
                                    if ($scope.subLotList.length > 0) {
                                        $scope.combinedFieldListForParent.push({
                                            modelName: '<strong>Sub Lot</strong>',
                                            multiSelectGroup1: true
                                        });
                                        if ($scope.subLotList.length > 0) {
                                            angular.forEach($scope.subLotList, function (item) {
                                                $scope.combinedFieldListForParent.push(angular.copy(item));
                                            });
                                        }
                                    }
                                    if ($scope.subLotList.length !== 0) {
                                        $scope.combinedFieldListForParent.push({
                                            multiSelectGroup1: false
                                        });
                                    }
                                    if ($scope.purchaseList.length > 0) {
                                        $scope.combinedFieldListForParent.push({
                                            modelName: '<strong>Rough Purchase</strong>',
                                            multiSelectGroup1: true
                                        });
                                        if ($scope.purchaseList.length > 0) {
                                            angular.forEach($scope.purchaseList, function (item) {
                                                $scope.combinedFieldListForParent.push(angular.copy(item));
                                            });
                                        }
                                    }
                                    if ($scope.purchaseList.length !== 0) {
                                        $scope.combinedFieldListForParent.push({
                                            multiSelectGroup1: false
                                        });
                                    }
                                    if ($scope.roughMakeableList.length > 0) {
                                        $scope.combinedFieldListForParent.push({
                                            modelName: '<strong>Sub Lot</strong>',
                                            multiSelectGroup1: true
                                        });
                                        if ($scope.roughMakeableList.length > 0) {
                                            angular.forEach($scope.roughMakeableList, function(item) {
                                                $scope.combinedFieldListForParent.push(angular.copy(item));
                                            });
                                        }
                                    }
                                    if ($scope.roughMakeableList.length !== 0) {
                                        $scope.combinedFieldListForParent.push({
                                            multiSelectGroup1: false
                                        });
                                    }
                                     if ($scope.finalMakeableList.length > 0) {
                                        $scope.combinedFieldListForParent.push({
                                            modelName: '<strong>Sub Lot</strong>',
                                            multiSelectGroup1: true
                                        });
                                        if ($scope.finalMakeableList.length > 0) {
                                            angular.forEach($scope.finalMakeableList, function(item) {
                                                $scope.combinedFieldListForParent.push(angular.copy(item));
                                            });
                                        }
                                    }
                                    if ($scope.finalMakeableList.length !== 0) {
                                        $scope.combinedFieldListForParent.push({
                                            multiSelectGroup1: false
                                        });
                                    }
                                    //                        $scope.combinedFieldListForParent = angular.copy($scope.combinedFieldListForSearch);
                                    var label = $scope.diamondFeature.menuLabel.split('_');
                                    var entity = label[0];
                                    $scope.commonList = [];
                                    angular.forEach($scope.sectionList, function (section) {
                                        angular.forEach(section.entitys, function (item) {
                                            if (!!item) {
//                                                console.log(item.id + $scope.valList[item.id].length + section.sectionId);
                                                angular.forEach($scope.valList[item.id], function (item1) {
                                                    var item2 = angular.copy(item1);
                                                    item2.sectionCode = section.sectionId;
                                                    $scope.commonList.push(item2);
                                                });
                                            }
                                        });
                                    });
                                    //CoomonList contains all fields of all sections.
//                                    console.log(JSON.stringify($scope.commonList));
//                                    console.log($scope.commonList.length);


                                    angular.forEach($scope.commonList, function (item) {
                                        item.selected = "hide";
                                        //                        item.isRequired = false;
                                    });
                                    $scope.commonList.sort(SortByFieldSequence);
                                    if ($scope.isEditing || $scope.isCopy || diamondSystemFeature.configure === true) {
                                        $scope.editFieldList = [];
                                        $scope.editFieldList = $scope.featureFieldMap[diamondSystemFeature.id];
                                        angular.forEach($scope.editFieldList, function (editItem) {
                                            if (editItem.searchFlag && editItem.sectionCode === 'GEN') {
                                                editItem.ticked = true;
                                                $scope.selectedFieldsForSearch.push(editItem);
                                                editItem.ticked = false;
                                            }
                                            angular.forEach($scope.combinedFieldListForSearch, function (item) {
                                                if (editItem.field === item.field && editItem.searchFlag) {
                                                    item.ticked = true;
                                                }
                                            });
                                        });
                                        angular.forEach($scope.editFieldList, function (editItem) {
                                            if (editItem.parentViewFlag && editItem.sectionCode === 'GEN') {
                                                editItem.ticked = true;
                                                $scope.selectedFieldsForParent.push(editItem);
                                                editItem.ticked = false;
                                            }
                                            angular.forEach($scope.combinedFieldListForParent, function (item) {
                                                if (editItem.field === item.field && editItem.parentViewFlag) {
                                                    item.ticked = true;
                                                }
                                            });
                                        });
                                    }
                                    //                    console.log("diamondSystemFeature :" + JSON.stringify(diamondSystemFeature));
                                    if (!$scope.isEditing && !$scope.isCopy && diamondSystemFeature.configure === false) {
                                        var length = $scope.diamondSystemFeatureList.length;
                                        for (var key in $scope.featureFieldMap) {
                                            for (var i = 0; i < length; i++) {
                                                if (key === $scope.diamondSystemFeatureList[i].id && diamondSystemFeature.configure === false) {
                                                    var name = $scope.diamondSystemFeatureList[i].menuLabel.split('_');
                                                    if (name[0] === label[0]) {
                                                        $scope.editFieldList = [];
                                                        $scope.editFieldList = $scope.featureFieldMap[key];
                                                        angular.forEach($scope.editFieldList, function (editItem) {
                                                            if (editItem.searchFlag && editItem.sectionCode === 'GEN') {
                                                                editItem.ticked = true;
                                                                $scope.selectedFieldsForSearch.push(editItem);
                                                                editItem.ticked = false;
                                                            }
                                                            angular.forEach($scope.combinedFieldListForSearch, function (item) {
                                                                if (editItem.field === item.field && editItem.searchFlag) {
                                                                    item.ticked = true;
                                                                }
                                                            });
                                                        });
                                                        angular.forEach($scope.editFieldList, function (editItem) {
                                                            if (editItem.parentViewFlag && editItem.sectionCode === 'GEN') {
                                                                editItem.ticked = true;
                                                                $scope.selectedFieldsForParent.push(editItem);
                                                                editItem.ticked = false;
                                                            }
                                                            angular.forEach($scope.combinedFieldListForParent, function (item) {
                                                                if (editItem.field === item.field && editItem.parentViewFlag) {
                                                                    item.ticked = true;
                                                                }
                                                            });
                                                        });
                                                    }
                                                }

                                            }
                                        }
                                    }

//                    }, function () {
//                        alert("failure");
                                    //                    });
                                    $("#configurePopUp" + (($scope.uniqueIndex === null || $scope.uniqueIndex === undefined) ? '' : $scope.uniqueIndex)).modal('show');
                                }

                            };
                            $scope.setSearchField = function (selectedFieldsForSearch) {
                                $scope.selectedFieldsForSearch = angular.copy(selectedFieldsForSearch);
                            };
                            $scope.setParentField = function (selectedFieldsForParent) {
                                $scope.selectedFieldsForParent = angular.copy(selectedFieldsForParent);
                            };

//            $scope.selectAllForParent = function () {
//                angular.forEach($scope.combinedFieldListForParent, function (item) {
//                    $scope.selectedFieldsForParent.push(item);
//                });
//            };
//            $scope.selectAllForSearch = function () {
//                angular.forEach($scope.combinedFieldListForSearch, function (item) {
//                    $scope.selectedFieldsForSearch.push(item);
//                });
//            };
//            
//            $scope.selectNoneForSearch = function () {
//                $scope.selectedFieldsForSearch = [];
//            };
//             $scope.selectNoneForParent = function () {
//                $scope.selectedFieldsForParent = [];
                            //            };
                            $scope.cancelConfirmPopup = function () {
                                $("#notConfiguredPopup").modal('hide');
                                $rootScope.removeModalOpenCssAfterModalHide();

                            };
                            $scope.cancelConfigurationPopup = function () {
                                countContent = 0;
                                $scope.nextPage = false;
                                $scope.firstPage = true;
                                $scope.secondPage = false;
                                //                $scope.diamondFeature = '';
                                $scope.selectedFieldsForSearch = [];
                                $scope.clearSelectedSearchFields();
                                $scope.clearSelectedParentFields();
                                $scope.selectedFieldsForParent = [];
                                $scope.commonList = [];
                                $scope.fieldListToSend = [];
                                $scope.fieldListToSendTemp = [];
                                $scope.entitys = [];
                                for (var i = 0; i < $scope.systemFeaturesList.length; i++) {
                                    $scope.systemFeaturesList[i].configure = false;
                                }
                                $("#configurePopUp" + (($scope.uniqueIndex === null || $scope.uniqueIndex === undefined) ? '' : $scope.uniqueIndex)).modal('hide');
                                $rootScope.removeModalOpenCssAfterModalHide();

                                if (($scope.featureFieldMap[$scope.diamondFeature.id] !== undefined && $scope.featureFieldMap[$scope.diamondFeature.id] !== null) && $scope.featureFieldMap[$scope.diamondFeature.id].length > 0) {
                                    for (var index = 0; index < $scope.featureFieldMap[$scope.diamondFeature.id].length; index++) {
                                        delete $scope.featureFieldMap[$scope.diamondFeature.id][index].ticked;
                                    }
                                }
                            };

                            $scope.setNextPage = function (index, form) {
                                if (index === 1) {
                                    $scope.flags.modifierSubmitted = true;
//                                    console.log(form.$valid);
                                    if (form.$valid) {
                                        $scope.secondPage = true;
                                        $scope.firstPage = false;
                                    }
                                }
                                if (index === 2) {
                                    $scope.nextPage = true;
                                    if ($scope.isEditing) {
                                        if ($scope.editFieldList) {
//                                            console.log(JSON.stringify($scope.editFieldList));
                                            for (var i = 0; i < $scope.editFieldList.length; i++) {
                                                for (var j = 0; j < $scope.commonList.length; j++) {
                                                    if ($scope.editFieldList[i].field === $scope.commonList[j].field && $scope.editFieldList[i].sectionCode === $scope.commonList[j].sectionCode) {
                                                        if ($scope.editFieldList[i].readonlyFlag) {
                                                            $scope.commonList[j].selected = 'view only';
                                                        } else if ($scope.editFieldList[i].editableFlag) {
                                                            $scope.commonList[j].selected = 'view and edit';
                                                        } else {
                                                            $scope.commonList[j].selected = 'hide';
                                                        }
                                                        $scope.commonList[j].sequenceNo = $scope.editFieldList[i].sequenceNo;
                                                        $scope.commonList[j].isRequired = $scope.editFieldList[i].isRequired;
                                                    }
                                                }
                                            }
                                            $scope.commonList.sort(SortByFieldSequence);
                                        }
                                    }
                                }
                            };
                            $scope.setPreviousPage = function (index) {
                                if (index === 2) {
                                    $scope.nextPage = false;
                                }
                                if (index === 1) {
                                    $scope.firstPage = true;
                                    $scope.secondPage = false;
                                }
                            };
                            function SortByFieldSequence(x, y) {
                                if (x.sequenceNo === '' || x.sequenceNo === null) {
                                    return 1;
                                }
                                if (y.sequenceNo === '' || y.sequenceNo === null) {
                                    return -1;
                                }
                                return x.sequenceNo - y.sequenceNo;
                            }

                            $scope.sortSequence = function () {
                                $scope.commonList.sort(SortByFieldSequence);
                            };

                            $scope.prepareModifiers = function () {
                                if ($scope.modifiers.noRecords !== true) {
                                    var modifiers = {};
                                    angular.forEach($scope.modifiers, function (value, key) {
                                        switch (key) {
                                            case "medium":
                                                modifiers.iRMediums = [];
                                                angular.forEach(value, function (item) {
                                                    if (item.value === true) {
                                                        modifiers.iRMediums.push(item.id);
                                                    }
                                                });
                                                break;
                                            case "type":
                                                modifiers.iRTypes = [];
                                                angular.forEach(value, function (item) {
                                                    if (item.value === true) {
                                                        modifiers.iRTypes.push(item.id);
                                                    }
                                                });
                                                break;
                                            case "mode":
                                                modifiers.iRModes = [];
                                                angular.forEach(value, function (item) {
                                                    if (item.value === true) {
                                                        modifiers.iRModes.push(item.id);
                                                    }
                                                });
                                                break;
                                            case "access":
                                                modifiers.iRVSRAccessRights = [];
                                                if ($scope.modifiers.showAccessModifiers === true) {
                                                    angular.forEach(value, function (item) {
                                                        if (item.value === true) {
                                                            modifiers.iRVSRAccessRights.push(item.id);
                                                        }
                                                    });
                                                }
                                                break;
                                            case "designation":
                                                modifiers.asDesignation = [];
                                                var designationStrng = value[0].value;
                                                if (designationStrng !== null && designationStrng !== "" && !(designationStrng instanceof Array)) {
                                                    var designationList = designationStrng.split(',');
                                                    angular.forEach(designationList, function (item) {
                                                        var designationid = item.split(':');
                                                        modifiers.asDesignation.push(designationid[0]);
                                                    });
                                                } else if (designationStrng !== null && designationStrng !== "" && (designationStrng instanceof Array)) {
                                                    angular.forEach(designationStrng, function (item) {
                                                        var designationid = item.id.split(':');
                                                        modifiers.asDesignation.push(designationid[0]);
                                                    });
                                                }

                                                break;
                                            case "plan":
                                                modifiers.planAccess = [];
                                                angular.forEach(value, function (item) {
                                                    if (item.value === true) {
                                                        modifiers.planAccess.push(item.id);
                                                    }
                                                });
                                                if ($scope.flags.planAccesArray !== null && $scope.flags.planAccesArray !== undefined && $scope.flags.planAccesArray.length > 0) {
                                                    modifiers.showPlanUsers = angular.copy($scope.flags.planAccesArray);
                                                    angular.forEach(modifiers.showPlanUsers, function(item) {
                                                        if (angular.isObject(item.users)) {
                                                            var tmpUsr = angular.copy(item.users[0]);
                                                            delete item.users;
                                                            if (tmpUsr !== undefined && tmpUsr !== null) {
                                                                item.users = tmpUsr.id;
                                                            } else {
                                                                item.users = "";
                                                            }
                                                        }
                                                        delete item.autoCompleteInvitees;
                                                    });
                                                }
                                                break;
                                        }
                                    });
                                    return modifiers;
                                }
                                return null;
                            };
                            $scope.savePermission = function () {
                                var sectionFieldList = {};
                                for (var i = 0; i < $scope.commonList.length; i++) {
                                    if ($scope.commonList[i].sectionCode === 'GEN') {
                                        for (var j = 0; j < $scope.fieldListToSend.length; j++) {
                                            if ($scope.commonList[i].field === $scope.fieldListToSend[j].field) {
                                                if ($scope.commonList[i].selected === 'hide') {
                                                } else if ($scope.commonList[i].selected === 'view only') {
                                                    $scope.fieldListToSend[j].readonlyFlag = true;
                                                } else if ($scope.commonList[i].selected === 'view and edit') {
                                                    $scope.fieldListToSend[j].editableFlag = true;
                                                }
                                                $scope.fieldListToSend[j].sequenceNo = $scope.commonList[i].sequenceNo;
                                                $scope.fieldListToSend[j].isRequired = $scope.commonList[i].isRequired;
                                                $scope.fieldListToSend[j].sectionCode = 'GEN';
                                            }
                                        }
                                    } else {
                                        angular.forEach($scope.sectionList, function (section) {
                                            if ($scope.commonList[i].sectionCode === section.sectionId) {
                                                if (sectionFieldList[section.sectionId] === undefined || sectionFieldList[section.sectionId] === null) {
                                                    sectionFieldList[section.sectionId] = {};
                                                    angular.forEach(section.entitys, function (item) {
                                                        if (!!item) {
                                                            console.log(item.id + $scope.valList[item.id].length + section.sectionId);
                                                            if (sectionFieldList[section.sectionId][item.id] === undefined || sectionFieldList[section.sectionId][item.id] === null) {
                                                                sectionFieldList[section.sectionId][item.id] = [];
                                                            }
                                                            angular.forEach($scope.valList[item.id], function (item1) {
                                                                var item2 = angular.copy(item1);
                                                                item2.sectionCode = section.sectionId;
                                                                sectionFieldList[section.sectionId][item.id].push(item2);
                                                            });
                                                        }
                                                    });
                                                }



                                                for (var j = 0; j < sectionFieldList[section.sectionId][$scope.commonList[i].entity].length; j++) {
                                                    if ($scope.commonList[i].field === sectionFieldList[section.sectionId][$scope.commonList[i].entity][j].field) {
                                                        if ($scope.commonList[i].selected === 'hide') {
                                                        } else if ($scope.commonList[i].selected === 'view only') {
                                                            sectionFieldList[section.sectionId][$scope.commonList[i].entity][j].readonlyFlag = true;
                                                        } else if ($scope.commonList[i].selected === 'view and edit') {
                                                            sectionFieldList[section.sectionId][$scope.commonList[i].entity][j].editableFlag = true;
                                                        }
                                                        sectionFieldList[section.sectionId][$scope.commonList[i].entity][j].sequenceNo = $scope.commonList[i].sequenceNo;
                                                        sectionFieldList[section.sectionId][$scope.commonList[i].entity][j].isRequired = $scope.commonList[i].isRequired;
//                                                        sectionFieldList[section.sectionId][$scope.commonList[i].entity][j].sectionCode = section.sectionId;
                                                    }
                                                }
                                            }
                                        });
                                    }
                                }
                                for (var i = 0; i < $scope.selectedFieldsForSearch.length; i++) {
                                    for (var j = 0; j < $scope.fieldListToSend.length; j++) {
                                        if ($scope.selectedFieldsForSearch[i].field === $scope.fieldListToSend[j].field) {
                                            $scope.fieldListToSend[j].searchFlag = true;
                                            $scope.fieldListToSend[j].sectionCode = 'GEN';
                                        }
                                    }
                                }
                                for (var i = 0; i < $scope.selectedFieldsForParent.length; i++) {
                                    for (var j = 0; j < $scope.fieldListToSend.length; j++) {
                                        if ($scope.selectedFieldsForParent[i].field === $scope.fieldListToSend[j].field) {
                                            $scope.fieldListToSend[j].parentViewFlag = true;
                                            $scope.fieldListToSend[j].sectionCode = 'GEN';
                                        }
                                    }
                                }
                                if (Object.keys(sectionFieldList).length > 0) {
                                    angular.forEach(sectionFieldList, function (value) {
                                        angular.forEach(value, function (val1) {
                                            $scope.fieldListToSend.push.apply($scope.fieldListToSend, angular.copy(val1));
//                                        $.merge($scope.fieldListToSend, value);
                                        });

                                    });
                                }
//                                console.log(JSON.stringify(sectionFieldList));
                                $scope.featureFieldMap[$scope.diamondFeature.id] = $scope.fieldListToSend;

                                var modifiers = $scope.prepareModifiers();
                                console.log("modifiers :" + JSON.stringify(modifiers));
                                if (modifiers != null) {
                                    if ($scope.featureModifierMap[$scope.diamondFeature.id] === null || $scope.featureModifierMap[$scope.diamondFeature.id] === undefined) {
                                        $scope.featureModifierMap[$scope.diamondFeature.id] = modifiers;
                                    } else {
                                        angular.forEach(modifiers, function (val, key) {
                                            $scope.featureModifierMap[$scope.diamondFeature.id][key] = val;
                                        });
                                    }

                                }
                                var length = $scope.diamondSystemFeatureList.length;
                                for (var i = 0; i < length; i++) {
                                    if ($scope.diamondSystemFeatureList[i].id === $scope.diamondFeature.id && $scope.diamondSystemFeatureList[i].checked) {
                                        $scope.diamondSystemFeatureList[i].configure = true;
                                    }

                                }
                                //                $scope.nextPage = false;
                                $scope.cancelConfigurationPopup();
                                $("#configurePopUp" + (($scope.uniqueIndex === null || $scope.uniqueIndex === undefined) ? '' : $scope.uniqueIndex)).modal('hide');
                                $rootScope.removeModalOpenCssAfterModalHide();
                                countContent = 0;

//                                console.log(JSON.stringify($scope.featureFieldMap));

                                //                                $scope.diamondSystemFeatureList = $scope.diamondSystemFeatureList;
                            };
                            $scope.commonListDtOptions = {
                                aoColumnDefs: [
                                    {
                                        bSortable: false,
                                        aTargets: [-2, -3]
                                    }
                                ]
                            };

                            $scope.changeEntityList = function () {
                                angular.forEach($scope.sectionList, function (section) {
                                    if (section.sectionId === $scope.entitys.section) {
                                        $scope.entityList = section.entitys;
                                    }
                                });
                                if (!!$scope.entityList) {
                                    $scope.entitys.name = $scope.entityList[0].id;
                                }
                                $scope.fieldTableContent();
                            }

                            var countContent = 0;
                            $scope.fieldTableContent = function () {
                                countContent++;
                                //Method is commented. Below code will never be executed.
                                //Only last line used to get new filtered records.
                                if (countContent <= 0) {
                                    $scope.commonList = [];
//                    angular.forEach($scope.lotList, function(item) {
//                        $scope.commonList.push(item);
//                    });
//                    angular.forEach($scope.packetList, function(item) {
//                        $scope.commonList.push(item);
                                    //                    });
                                    angular.forEach($scope.visibleFields[$scope.diamondFeature.menuLabel], function (item) {

                                        if (!!item) {
                                            if (!!!$scope.commonList || $scope.commonList.length < 1) {
                                                $scope.commonList = [];
                                                $scope.commonList = angular.copy($scope.valList[item.id]);
                                            } else {
                                                $.merge($scope.commonList, $scope.valList[item.id]);
                                            }
                                        }
                                    });
                                    angular.forEach($scope.commonList, function (item) {
                                        item.selected = "hide";
                                        item.isRequired = false;
                                    });
                                    if ($scope.editFieldList) {
                                        for (var i = 0; i < $scope.editFieldList.length; i++) {
                                            for (var j = 0; j < $scope.commonList.length; j++) {
                                                if ($scope.editFieldList[i].field === $scope.commonList[j].field) {
                                                    if ($scope.editFieldList[i].readonlyFlag) {
                                                        $scope.commonList[j].selected = 'view only';
                                                    } else if ($scope.editFieldList[i].editableFlag) {
                                                        $scope.commonList[j].selected = 'view and edit';
                                                    } else {
                                                        $scope.commonList[j].selected = 'hide';
                                                    }
                                                    $scope.commonList[j].sequenceNo = $scope.editFieldList[i].sequenceNo;
                                                    $scope.commonList[j].isRequired = $scope.editFieldList[i].isRequired;
                                                }
                                            }
                                        }
                                    }

                                }
                                //Used as a parameter to data table to determine length of filtered data.
                                $scope.entitys.filteredList = $filter('commonListFilter')($scope.commonList, $scope.entitys.name, $scope.entitys.section);

                            };

                            $scope.popover =
                                    "<NOBR><font color='red;'>Use the shortcuts to search</font></NOBR><br/><table cellpadding='0' cellspacing='0'>" +
                                    "<tr><td>'@R'  </td> <td> &nbsp; &nbsp;</td><td align='left'>Designations</td></tr>\ " +
                                    "</table>\ ";
                            $scope.planpopover =
                                    "<NOBR><font color='red;'>Use the shortcuts to search</font></NOBR><br/><table cellpadding='0' cellspacing='0'>" +
                                    "<tr><td>'@R'  </td> <td> &nbsp; &nbsp;</td><td align='left'>Designations</td></tr>\ " +
                                    "<tr><td>'@E'  </td> <td> &nbsp; &nbsp;</td><td align='left'>Employee</td></tr>\ " +
                                    "<tr><td>'@D'  </td> <td> &nbsp; &nbsp;</td><td align='left'>Department</td></tr>\ " +
                                    "</table>\ ";

                            $scope.initDesignations = function () {
                                //For recipients,
                                $scope.autoCompleteDesignation = {
                                    multiple: true,
                                    closeOnSelect: false,
                                    placeholder: 'Select Designation',
                                    initSelection: function (element, callback) {
                                        var modifierObj = $scope.featureModifierMap[$scope.diamondFeature.id];
                                        if ($scope.modifiers.designation[0].value && modifierObj != null && modifierObj.asDesignationIdName != null) {
                                            var strng = $scope.modifiers.designation[0].value;
                                            if (!(strng instanceof Array)) {
                                                var values = strng.split(',');
                                                if (values.length > 0) {

                                                    var data = [];
                                                    angular.forEach(values, function (item) {
                                                        data.push({
                                                            id: item,
                                                            text: modifierObj.asDesignationIdName[item]
                                                        });
                                                    });
                                                    callback(data);
                                                }
                                            }

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
                                        var success = function (data) {
                                            if (data.length !== 0) {
                                                $scope.names = data;
                                                angular.forEach(data, function (item) {
                                                    $scope.names.push({
                                                        id: item.value + ":" + item.description,
                                                        text: item.label
                                                    });
                                                });
                                            }
                                            query.callback({
                                                results: $scope.names
                                            });
                                        };
                                        var failure = function () {
                                        };

                                        if (selected.substring(0, 2) === '@R' || selected.substring(0, 2) === '@r') {
                                            var search = query.term.slice(2);
                                            var send = {
                                                parentRole: $scope.designationId,
                                                searchRole: search.trim()
                                            };
                                            Designation.retrieveChildRoles(send, success, failure);
                                        } else {
                                            query.callback({
                                                results: $scope.names
                                            });
                                        }
                                    }
                                };
                            };
                            $scope.initDesignations();
                            $scope.changeInPlanModifier = function(value) {
                                if (value !== null && value !== undefined && value.id === "SP") {
                                    if (value.value === true) {
                                        $scope.flags.planFlag = true;
                                        $scope.flags.planAccesArray = [{users: "", autoCompleteInvitees: angular.copy($scope.autoCompleteInvitees)}];
                                    } else {
                                        $scope.flags.planFlag = false;
                                        $scope.flags.planAccesArray = [];
                                    }
                                }
                            }
                            $scope.addnewPlanAccess = function(form) {
                                $scope.submitted = true;
                                if (form.$valid) {
                                    $scope.flags.planAccesArray.push({users: "", autoCompleteInvitees: angular.copy($scope.autoCompleteInvitees)});
                                }
                            };
                            $scope.deletePlanAccess = function(index) {
                                if ($scope.flags.planAccesArray !== undefined && $scope.flags.planAccesArray !== null && $scope.flags.planAccesArray.length > 0) {
                                    $scope.flags.planAccesArray.splice(index, 1);
                                    if ($scope.flags.planAccesArray.length === 0) {
                                        $scope.flags.planAccesArray = [{users: "", autoCompleteInvitees: angular.copy($scope.autoCompleteInvitees)}];
                                    }
                                }
                            }
                            $scope.autoCompleteInvitees = {
                                multiple: true,
                                closeOnSelect: false,
                                placeholder: 'Select members',
                                width: 'resolve',
                                initSelection: function(element, callback) {
                                },
                                formatResult: function(item) {
                                    return item.text;
                                },
                                formatSelection: function(item) {
                                    return item.text;
                                },
                                query: function(query) {
                                    var selected = query.term;
                                    $scope.names = [];
                                    var success = function(data) {
                                        if (data.length !== 0) {
                                            $scope.names = data;
                                            angular.forEach(data, function(item) {
                                                $scope.names.push({
                                                    id: item.value + ":" + item.description,
                                                    text: item.label

                                                });
                                            });
                                        }
                                        query.callback({
                                            results: $scope.names
                                        });
                                    };
                                    var failure = function() {
                                    };
                                    if (selected.substring(0, 2) === '@E' || selected.substring(0, 2) === '@e') {
                                        var search = query.term.slice(2);
                                        Messaging.retrieveUserList(search.trim(), success, failure);
                                    } else if (selected.substring(0, 2) === '@D' || selected.substring(0, 2) === '@d') {
                                        var search = query.term.slice(2);
                                        Messaging.retrieveDepartmentList(search.trim(), success, failure);
                                    } else if (selected.substring(0, 2) === '@R' || selected.substring(0, 2) === '@r') {
                                        var search = query.term.slice(2);
                                        Messaging.retrieveRoleList(search.trim(), success, failure);
                                    }
                                }
                            };
                            /** Designation part ends **/
                            //                            $scope.retrieveAllSysFeatures();
//                            $scope.systemFeaturesList = [];
//                            $scope.diamondSystemFeatureList = angular.copy($scope.diamondSystemFeatureList);

                        }];

                    return {
                        restrict: 'E',
                        link: link,
                        scope: scope,
                        controller: controller,
                        templateUrl: 'scripts/directives/designationtemplate/desigtemplate.tmpl.html'
                    };
                }]);
    globalProvider.filterProvider.register('commonListFilter', function () {
        return  function (items, searchText, section) {
//            console.log(section);

            if (angular.isDefined(items) && angular.isDefined(searchText)) {
                if (angular.isDefined(searchText) && !!searchText) {
                    var filtered = [];
                    for (var i = 0; i < items.length; i++) {
                        var item = items[i];
                        if ((item.entity !== null && item.entity.toLowerCase().indexOf(searchText.toLowerCase()) >= 0) && item.sectionCode === section)
                        {
                            filtered.push(item);
                        }
                    }
                    return filtered;
                }
            } else {
                if ($scope.type === "DMI") {
                    return items;
                } else {
                    return null;
                }
            }


        };
    });

});
