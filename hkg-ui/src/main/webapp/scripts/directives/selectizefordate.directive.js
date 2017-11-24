/**
 * @author Shifa Salheen
 * 
 * Created by Shifa Salheen
 * 
 * It is used in formula configuration currently.
 * 
 * It can be used for select box with tagging support.
 * It provides support for moving cursor's caret and adding new items in select box.
 * 
 * 
 * For more information see this api documentation of selectize
 * https://github.com/brianreavis/selectize.js/blob/master/docs/api.md
 * 
 * 
 */
define(['angular'], function () {
    globalProvider.compileProvider.directive('agSelectizedate', ["$filter", function ($filter) {
            return {
                restrict: 'A',
                require: '?ngModel',
                link: function (scope, element, attrs, controller) {
                    var selectize;
                    scope.selectize = scope.selectizeOptionsForDateField;
                    console.log("date " + scope.dateoptions + "itemssss" + scope.dateitems)
                    scope.selectize.options = scope.dateoptions;
                    scope.selectize.items = scope.dateitems;
                    var modelCtrl = controller;
                    var $select = element.selectize(scope.selectize);
                    selectize = $select[0].selectize;


                    scope.$watch('options', function (newTags, oldTags) {

                        if (newTags.length < oldTags.length) {
                            angular.forEach(oldTags, function (obj, index) {
                                if (JSON.stringify(newTags).indexOf(JSON.stringify(obj)) < 0) {

//                                selectize.removeOption(obj.id);
                                }
                            })
                        } else {

                        }
                    }, true);
                    scope.$watch('pointerIdIncrement', function ()
                    {
                        console.log("pomiter d"+scope.dateitems);
                        var items = [];
                        if (scope.dateitems !== null && scope.dateitems !== undefined && scope.dateitems.length > 0) {
                            items = scope.dateitems.split('|');
                            console.log("itemsss."+items)
                            angular.forEach(items, function (result)
                            {
                               console.log("optiosss"+scope.dateoptions)
                                var index = $filter('filter')(scope.dateoptions, function (tag) {
                                     console.log("tag id.."+tag.id+"---"+result)
                                    return tag.id.toString() === result.toString();

                                })[0];
                                console.log("option..." + index)
                                if (index !== null && index !== undefined) {
                                    selectize.addOption(index);
                                    selectize.addItem(index.id);
                                }
                            });
                        }

                    });

                    scope.$watch('optgroups', function (newGroups, oldGroups) {
                        if (newGroups !== undefined) {
                            if (newGroups.length < oldGroups.length) {
                                angular.forEach(oldGroups, function (obj, index) {
                                    if (JSON.stringify(newGroups).indexOf(JSON.stringify(obj)) < 0) {
                                        angular.forEach(scope.selectize.options, function (option, index) {
                                            if (option[scope.selectize.optgroupField] === obj.id) {
                                                selectize.removeOption(option.id);
                                            }
                                        });
                                    }
                                })
                            } else {
                                angular.forEach(newGroups, function (tag, index) {
                                    selectize.addOptionGroup(tag.id, tag);
                                });
                            }
                        }
                    }, true);

                }
            };
        }]);
});