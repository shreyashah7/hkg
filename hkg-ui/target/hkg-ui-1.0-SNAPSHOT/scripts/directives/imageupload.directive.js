/**
 * @author this directive will be used as a attribute with input type=file when we want to generate a url of uploaded image and 
 * need to be used at another place as image source
 */

define(['angular'], function() {
    angular.module('hkg.directives').directive('agImageUpload', ["$q", function($q) {
            var fileToDataURL = function(file) {
                var deferred = $q.defer();
                var reader = new FileReader();
                reader.onload = function(e) {
                    deferred.resolve(e.target.result);
                };
                reader.readAsDataURL(file);
                return deferred.promise;
            };
            return {
                restrict: 'A',
                scope: {
                    agImageUpload: '='
                },
                link: function postLink(scope, element, attrs, ctrl) {
                    var applyScope = function(imageResult) {
                        scope.$apply(function() {
                            if (attrs.multiple)
                                scope.agImageUpload.push(imageResult);
                            else {
                                scope.agImageUpload = imageResult;
                            }
                        });
                    };


                    element.bind('change', function(evt) {
                        //when multiple always return an array of images
                        if (attrs.multiple)
                            scope.agImageUpload = [];

                        var files = evt.target.files;
                        for (var i = 0; i < files.length; i++) {
                            //create a result object for each file in files
                            var imageResult = {
                                file: files[i],
                                url: URL.createObjectURL(files[i])
                            };

                            fileToDataURL(files[i]).then(function(dataURL) {
                                imageResult.dataURL = dataURL;
                            });

                            applyScope(imageResult);
                        }
                    });
                }
            };
        }]);
});

