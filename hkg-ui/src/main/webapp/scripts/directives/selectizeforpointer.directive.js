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
    globalProvider.compileProvider.directive('agSelectizepointer', ["$filter", function ($filter) {
        return {
            restrict: 'A',
            require: '?ngModel',
            link: function (scope, element, attrs, controller) {
                var selectize;
                scope.selectize = scope.selectizeOptionsForPointer;
                scope.selectize.options = scope.options;
                scope.selectize.items = scope.items;
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
                    console.log("pomiter d");
                    var items = [];
                    if (scope.items !== null && scope.items !== undefined && scope.items.length >0) {
                        items = scope.items.split('|');
                        angular.forEach(items, function (result)
                        {
                            var index = $filter('filter')(scope.options, function (tag) {
                                return tag.id.toString() === result.toString();

                            })[0];
                            selectize.addOption(index);
                            selectize.addItem(index.id);
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