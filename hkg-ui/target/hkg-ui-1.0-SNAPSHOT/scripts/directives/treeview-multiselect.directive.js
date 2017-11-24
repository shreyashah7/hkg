/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
define(['angular'], function() {
    /**
     * @author Akta Kalariya
     * 
     * @description <agTreeviewMultiselect val="collection"></agTreeviewMultiselect> is directive
     * used to generate treeview in case of nested objects and will allow us to select multiple
     * item at time.
     * Example:
     *<div class="btn-group">
     <button data-toggle="dropdown" class="btn dropdown-toggle" >action<span class="caret"></span></button>
     <ul class="dropdown-menu" ng-click="$event.stopPropagation()" style="white-space: nowrap">
     <li style="width: 100%">
     <ag-treeview-multiselect val="treeData" selecteditem="selectedItemList">
     </ag-treeview-multiselect>
     </li>
     </ul></div>
     */

    globalProvider.compileProvider.directive('agTreeviewMultiselect', ["$compile", function($compile) {
            return {
                restrict: 'E',
                terminal: true, transclude: true,
//            scope: {val: '=', parentData: '=', parentDataObject: '='},
                link: function(scope, element, attrs) {
                    if (scope.treeViewMultiselectTemplate === undefined) {
                        scope.treeViewMultiselectTemplate = '';
                        //                    scope.treeViewMultiselectTemplate = '<button class="btn-link btn-small" ng-click="checkOrUncheckAllItems(true)"><i class="icon-ok"></i> Check all</button><button class="btn-link btn-small" ng-click="checkOrUncheckAllItems(false)"><i class="icon-remove"></i> Uncheck all</button><br/><input type="text" ng-model="searchItem"/><br/>';
                        var treeViewMultiselectTemplate = scope.treeViewMultiselectTemplate;
                        scope.mainTreeViewData = scope[attrs.val];
                        scope.firstScopeObj = scope;
                        scope.myob = {};
                        scope.myob.defaultSelectedids = scope[attrs.defaultselectedids];
                    }
                    else {
                        treeViewMultiselectTemplate = "";
                        scope.myob.defaultSelectedids = scope[attrs.defaultselectedids];
                    }
                    var val = attrs.val;
                    var parentData = attrs.parentData;
                    var parentDataObject = attrs.parentDataObject;
                    var selecteditem = attrs.selecteditem;
//                scope.loadMainObject(val);
                    var initTemplate = treeViewMultiselectTemplate;
//                var temp = '<i ng-class="{\'icon-ok\': (' + val + '.isChecked || keepMeCheked(' + val + ')), \'icon-empty\': !' + val + '.isChecked}" class="icon-empty"></i>';
                    var temp = '<span style="display : inline-block" title="No Children Exists" ng-class="{\'ng-click-active\':(' + val + '.isChecked )}" class="glyphicon glyphicon-file text-hkg ng-click-active" data-ng-hide="node.children.length" data-ng-click="selectedDepartmentDropdown.selectNodeLabel(node, $event)"></span>'
                    var template = initTemplate + '<input ng-show="false" type="checkbox" ng-click="checkOrUncheckMe(' + val + '.isChecked)" ng-model="' + val + '.isChecked"/>\n\
' + temp + '<span ng-class = "' + val + '.isChecked ? \'\':\'tree-model-list\'"><span ng-click="checkOrUncheckMe(!' + val + '.isChecked)" ng-class = "' + val + '.isChecked ? \'hkg-selected\':\'\'" style="cursor:pointer;width:90%;float:right;">{{' + val + '.text}}</span></span>';
                    if (angular.isArray(scope[val].items)) {
                        template += '<ul class="indent" style="list-style-type: none;">\n\
                            <li ng-repeat="item in ' + val + '.items| filter:searchItem" style="margin: 3px 0px;">\n\
                            <ag-treeview-multiselect val="item" parent-data="' + val + '.items" parent-data-object="' + val + '" selecteditem=' + selecteditem + '>\n\
                            </ag-treeview-multiselect></li>\n\
                            </ul>';
                    }

                    scope.$watch(attrs.defaultselectedids, function(newVal, old) {
                        checkElementsTrue(scope.mainTreeViewData.items, newVal);
                    }, true);

                    var checkElementsTrue = function(list, ary) {
                        if (list !== undefined && list !== null) {
                            for (var i = 0; i < list.length; i++) {
                                var elem = list[i];
                                if ($.inArray(elem.id + "", ary) !== -1 || $.inArray(elem.id, ary) !== -1) {
                                    elem.isChecked = true;
                                }
                                else {
                                    elem.isChecked = false;
                                }
                                if (angular.isDefined(elem.items) && elem.items !== null) {
                                    checkElementsTrue(elem.items, ary);
                                }
                            }
                        }
                    };

                    scope.checkOrUncheckMe = function(chkUnkval) {
                        var currentSelectedVal = scope[val];
                        currentSelectedVal.isChecked = chkUnkval;
                        if (currentSelectedVal.items && chkUnkval) {
                            for (var i = 0; i < currentSelectedVal.items.length; i++) {
                                currentSelectedVal.items[i].isChecked = chkUnkval;
                                if (currentSelectedVal.items[i].items) {
                                    scope.iterateChild(currentSelectedVal.items[i].items, chkUnkval);
                                }
                            }
                        }

                        else if (chkUnkval && scope.parentDataObject === undefined) {
                            scope.isChecked = true;
                        }
                        if (!chkUnkval) {
                            scope.verifyOnUncheck();
                            if (scope[val].items) {
                                scope.iterateChild(scope[val].items, false);
                            }
                        }
                        else if (chkUnkval) {
                            scope.verifyOnCheck();
                        }
                        scope.toggleData(selecteditem);
                    };
                    scope.iterateChild = function(childItems, chkUnkval) {
                        var currentitems = childItems;
                        for (var i = 0; i < currentitems.length; i++) {
                            currentitems[i].isChecked = chkUnkval;
                            if (currentitems[i].items) {
                                scope.iterateChild(currentitems[i].items, chkUnkval);
                            }
                        }
                    };

                    scope.checkOrUncheckAllItems = function(isCheckValue) {
                        var currentSelectedVal = scope.mainTreeViewData;
                        currentSelectedVal.isChecked = isCheckValue;
                        if (currentSelectedVal.items) {
                            for (var i = 0; i < currentSelectedVal.items.length; i++) {
                                currentSelectedVal.items[i].isChecked = isCheckValue;
                                if (currentSelectedVal.items[i].items) {
                                    scope.iterateChild(currentSelectedVal.items[i].items, isCheckValue);
                                }
                            }
                        }
                        scope.toggleData(selecteditem);
                    };

                    var newElement = angular.element(template);
                    $compile(newElement)(scope);
                    element.replaceWith(newElement);
//            },
//            controller: function($scope, treeService, $rootScope) {
//                $scope.loadMainObject = function(val) {
//                    if (treeService.getMainObject() === null) {
//                        treeService.setMainObject($scope[val]);
//
////-----------------------------------------------------------------------------------------------------------------------                            
//                        $scope.$parent[$scope.selecteditem] = $scope.selecteditem;
//                        treeService.setFirstScope($scope);
//                    }
//                    return treeService.getMainObject();
//                };
//                $scope.loadOneTimeHtml = function() {
//                    if (treeService.getButtonHtml() === null) {
//                        treeService.setButtonHtml('<button class="btn-link btn-small" ng-click="checkOrUncheckAllItems(true)"><i class="icon-ok"></i> Check all</button><button class="btn-link btn-small" ng-click="checkOrUncheckAllItems(false)"><i class="icon-remove"></i> Uncheck all</button><br/><input type="text" ng-model="searchItem"/><br/>');
//                        return treeService.getButtonHtml();
//                    }
//                    else if (treeService.getButtonHtml() !== null) {
//                        return "";
//                    }
//                };
                    scope.verifyOnUncheck = function() {
                        var mainObject = scope.mainTreeViewData;
                        if (mainObject.items) {
                            var returnval = scope.processChild(mainObject, 0, mainObject.items.length, 0);
                        }
                    };
                    scope.processChild = function(mainObject, checkedCount, length, decreaseCount) {
                        for (var i = 0; i < mainObject.items.length; i++) {
                            if (mainObject.items[i].items) {
                                if (mainObject.items[i].isChecked) {
                                    checkedCount++;
                                }
                                var returnval = scope.processChild(mainObject.items[i], 0, mainObject.items[i].items.length, 0);
                                checkedCount = checkedCount + returnval[0];
                                if (mainObject.items[i + 1] === undefined) {
                                    if (checkedCount !== length) {
                                        mainObject.isChecked = false;
                                        decreaseCount = -1;
                                    }
                                }
                            }
                            else {
                                if (mainObject.items[i].isChecked) {
                                    checkedCount++;
                                }
                                if (mainObject.items[i + 1] === undefined) {
                                    if (checkedCount !== length) {
                                        mainObject.isChecked = false;
                                        decreaseCount = -1;
                                    }
                                }

                            }
                        }
                        return [decreaseCount, length, mainObject.items[0]];
                    };
                    scope.verifyOnCheck = function() {
                        var mainObject = scope.mainTreeViewData;
                        if (mainObject.items) {
                            scope.processChildVerifyOnCheck(mainObject, 0, mainObject.items.length, 0);
                        }
                    };
                    scope.processChildVerifyOnCheck = function(mainObject, checkedCount, length, decreaseCount) {
                        for (var i = 0; i < mainObject.items.length; i++) {
                            if (mainObject.items[i].items) {
                                var returnval = scope.processChildVerifyOnCheck(mainObject.items[i], 0, mainObject.items[i].items.length, 0);
                                checkedCount = checkedCount + returnval[0];
                                if (mainObject.items[i + 1] === undefined) {
                                    if (checkedCount === length) {
                                        mainObject.isChecked = true;
                                        decreaseCount = 1;
                                    }
                                }
                            }
                            else {
                                if (mainObject.items[i].isChecked) {
                                    checkedCount++;
                                }
                                if (mainObject.items[i + 1] === undefined) {
                                    if (checkedCount === length) {
                                        mainObject.isChecked = true;
                                        decreaseCount = 1;
                                    }
                                }

                            }
                        }
                        return [decreaseCount, length, mainObject.items[0]];
                    };

                    scope.toggleData = function(selecteditem) {
                        scope.tempdata = angular.copy(scope.mainTreeViewData);
                        if (scope.tempdata.items && !scope.tempdata.isChecked) {
                            scope.processItems(scope.tempdata.items, scope.tempdata);
                        }
                        scope.firstScopeObj.$parent[selecteditem] = scope.tempdata;
                    };
                    scope.processItems = function(currentItems, parentArray) {
                        for (var i = 0; i < currentItems.length; i++) {
                            if (currentItems[i].isChecked && currentItems[i].items) {
                            }
                            else if (currentItems[i].items && !currentItems[i].isChecked) {
                                scope.processItems(currentItems[i].items, currentItems[i]);
                                if (parentArray.items === undefined) {
                                    var itemIndex = parentArray.indexOf(currentItems[i]);
                                    if (currentItems[i].items.length === 0) {
                                        parentArray.splice(itemIndex, 1);
                                        i--;
                                    }
                                }
                                else {
                                    var itemIndex = parentArray.items.indexOf(currentItems[i]);
                                    if (currentItems[i].items.length === 0) {
                                        parentArray.items.splice(itemIndex, 1);
                                        i--;
                                    }
                                }
                            }
                            else {
                                var returnVal = scope.processItem(currentItems[i], parentArray);
                                i = i - returnVal;
                            }
                        }
                    };
                    scope.processItem = function(currentItem, parentArray) {
                        if (!currentItem.isChecked) {
                            var itemIndex = parentArray.items.indexOf(currentItem);
                            parentArray.items.splice(itemIndex, 1);
                            return 1;
                        }
                        return 0;
                    };
                }
            };
        }]);
//            .service("treeService", function() {
//                var mainObject = null;
//                var buttonHtml = null;
//                var firstscope = null;
//                return {
//                    getMainObject: function() {
//                        return mainObject;
//                    },
//                    setMainObject: function(value) {
//                        mainObject = value;
//                    },
//                    getButtonHtml: function() {
//                        return buttonHtml;
//                    },
//                    setButtonHtml: function(buttonhtml) {
//                        buttonHtml = buttonhtml;
//                    },
//                    getFirstScope: function() {
//                        return firstscope;
//                    },
//                    setFirstScope: function(sc) {
//                        firstscope = sc;
//                    }
//                };
//            });
});


