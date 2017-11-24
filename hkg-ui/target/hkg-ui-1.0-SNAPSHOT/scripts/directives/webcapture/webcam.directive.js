/**
 * 
 * Author: Gautam
 * 
 * Objective : Take snap-shot from web cam.
 * 
 */

define(['angular'], function() {
    globalProvider.provide.factory('CameraService', ["$window", function($window) {
            var hasUserMedia = function() {
                return !!getUserMedia();
            };

            var getUserMedia = function() {
                navigator.getUserMedia = ($window.navigator.getUserMedia ||
                        $window.navigator.webkitGetUserMedia ||
                        $window.navigator.mozGetUserMedia ||
                        $window.navigator.msGetUserMedia);
                return navigator.getUserMedia;
            };

            return {
                hasUserMedia: hasUserMedia(),
                getUserMedia: getUserMedia
            };
        }]);

    globalProvider.controllerProvider.register('CameraController', ["$scope", "CameraService", function($scope, CameraService) {
            $scope.hasUserMedia = CameraService.hasUserMedia;
        }]);

    globalProvider.compileProvider.directive('camera', ["CameraService", "Imageuploadservice", "$modal", function(CameraService, Imageuploadservice, $modal) {
            return {
                restrict: 'EA',
                replace: true,
                transclude: true,
                scope: {
                    modelValue: '=',
                    modelName: '@',
                    featureName: '@',
                    fileType: '@',
                    uniqueId: '@'
                },
                templateUrl: 'scripts/directives/webcapture/webcam.tmpl.html',
                link: function(scope, ele, attrs) {
                    //Disable right-click on video element
                    $(document).ready(function() {
                        $("video").bind("contextmenu", function() {
                            return false;
                        });
                    });

                },
                controller: ["$scope", "$q", "$timeout", "$attrs", "$rootScope", "$element", function($scope, $q, $timeout, $attrs, $rootScope, $element) {
                        $scope.deviceNotFound = false;
                        $scope.permissionDenied = false;
                        $scope.otherError = false;

                        //Function to escape meta-characters from id in jquery.
                        function escapeMetaChars(selector) {
                            return selector.replace(/(!|"|#|\$|%|\'|\(|\)|\*|\+|\,|\.|\/|\:|\;|\?|@)/g, function($1, $2) {
                                return "\\" + $2;
                            });
                        }

                        $scope.elementId = ($scope.uniqueId === undefined ? $scope.modelName : $scope.uniqueId) + '-play';
                        $scope.videoElementId = escapeMetaChars($scope.elementId) + '-video';
                        try {
                            $scope.isCustom = JSON.parse($attrs.isCustom.toLowerCase());
                        } catch (exception) {
                            $scope.isCustom = false;
                            console.log('Can not parse isCustom' + exception);
                        }
                        try {
                            $scope.isThumbnail = JSON.parse($attrs.isThumbnail.toLowerCase());
                        } catch (exception) {
                            $scope.isThumbnail = false;
                            console.log('Can not parse is thumbnail' + exception);
                        }
                        if ($attrs.height !== undefined) {
                            $scope.height = $attrs.height;
                        } else {
                            $scope.height = null;
                        }
                        if ($attrs.width !== undefined) {
                            $scope.width = $attrs.width;
                        } else {
                            $scope.width = null;
                        }
                        if ($scope.featureName === undefined) {
                            $scope.featureName = null;
                        }
                        if ($scope.fileType === undefined) {
                            $scope.fileType = null;
                        }

                        $scope.takeSnapshot = function() {
                            var canvas = document.createElement('canvas'),
                                    ctx = canvas.getContext('2d'),
                                    videoElement = document.getElementById($scope.videoElementId);

                            canvas.width = $scope.w;
                            canvas.height = $scope.h;

                            $timeout(function() {
                                ctx.fillRect(0, 0, $scope.w, $scope.h);
                                ctx.drawImage(videoElement, 0, 0, $scope.w, $scope.h);
//                        console.log(canvas.toDataURL('image/jpeg'));
                                var dataTosent = {};
                                dataTosent.file = canvas.toDataURL('image/jpeg');
                                dataTosent.fileName = "web_cam_capture_" + new Date().getTime() + ".jpeg";
                                dataTosent.modelName = $scope.modelName;
                                canvas.remove();
                                $scope.hidePopup();
                                //For custom image fields.
                                if ($scope.isCustom) {
                                    Imageuploadservice.uploadFile(dataTosent, function(res) {
                                        var info = [dataTosent.fileName, dataTosent.modelName];
                                        Imageuploadservice.submitFile(info, function(result) {
//                                    console.log(JSON.stringify(result));
                                            if (result.res) {
                                                $scope.modelValue = [result.res];
                                            }
                                        }, function(ress) {
                                            alert('Image transfer failed');
                                        });
                                    }, function(res) {
                                        alert('Image upload failed');
                                    });
                                } else {
                                    //For non-diamond fields
                                    Imageuploadservice.uploadFile(dataTosent, function(res) {
                                        var fileDetail = [dataTosent.fileName, $scope.fileType, $scope.featureName];
                                        Imageuploadservice.getTempFileName(fileDetail, function(res) {
//                                    console.log(JSON.stringify(res));
                                            var tempFileName = res.data;
                                            var info = [dataTosent.fileName, dataTosent.modelName, tempFileName, $scope.isThumbnail];
                                            if ($scope.width !== null && $scope.height !== null) {
                                                info.push($scope.width);
                                                info.push($scope.height);
                                            }
                                            Imageuploadservice.submitFile(info, function(result) {
//                                        console.log(JSON.stringify(result));
                                                if (result.res) {
                                                    $scope.modelValue = result.res;
                                                }
                                            }, function(ress) {
                                                alert('Image transfer failed');
                                            });
                                        }, function(res) {
                                            alert('Image upload failed.');
                                        });
                                    }, function(res) {
                                        alert('Image upload failed');
                                    });
                                }
                            }, 0);
                        };
                        $scope.playVideo = function(stream) {
                            var videoElement = document.getElementById($scope.videoElementId);
                            if (navigator.mozGetUserMedia) {
                                videoElement.mozSrcObject = stream;
                            } else {
                                var vendorURL = window.URL || window.webkitURL;
                                videoElement.src = window.URL.createObjectURL(stream);
                            }
                            // Just to make sure it autoplays
                            videoElement.play();
                        };
                        //Hide modal.
                        $scope.hidePopup = function() {
                            //$('#' + $scope.elementId).modal('hide');
                            if ($scope.videoComponentModal) {
                                $scope.videoComponentModal.dismiss();
                            }
                            $rootScope.removeModalOpenCssAfterModalHide();
                            $rootScope.stream.stop();
                        };
                        $scope.showPopup = function(stream) {
//                            $('#' + $scope.elementId).modal('show');
                            var t = "videoComponent.html";
                            $scope.videoComponentModal = $modal.open({
                                templateUrl: t,
                                scope: $scope
                            });
                            $timeout(function() {
                                $scope.playVideo(stream);
                            });

                            //Register modal close event to close stream.
                            $scope.videoComponentModal.result.then(function() {
                            }, function() {
                                //To stop stream of web cam if enabled by any page.
                                if (angular.isDefined($rootScope.stream)) {
                                    $rootScope.stream.stop();
                                }
                            });
                        };
                        $scope.hideErrorPopup = function() {
//                            $('#' + $scope.elementId + 'ErrorModal').modal('hide');
                            if ($scope.videoComponentErrorModal) {
                                $scope.videoComponentErrorModal.dismiss();
                            }
                            $rootScope.removeModalOpenCssAfterModalHide();
                        };
                        $scope.showErrorPopup = function() {
//                            $('#' + $scope.elementId + 'ErrorModal').modal('show');
                            var t = "videoComponentError.html";
                            $scope.videoComponentErrorModal = $modal.open({
                                templateUrl: t,
                                scope: $scope
                            });
                        };
                        $scope.initWebCam = function() {
                            //alert($scope.modelValue);
                            $scope.deviceNotFound = false;
                            $scope.permissionDenied = false;
                            $scope.otherError = false;
                            var w = $attrs.width || 500,
                                    h = $attrs.height || 370;
                            if (!CameraService.hasUserMedia)
                                return;

                            // If the stream works
                            var onSuccess = function(stream) {
                                $rootScope.stream = stream;

                                $scope.showPopup(stream);
                            };
                            // If there is an error
                            var onFailure = function(err) {
                                if (err.name === "DevicesNotFoundError") {
                                    $scope.deviceNotFound = true;
                                } else if (err.name === "PermissionDeniedError") {
                                    $scope.permissionDenied = true;
                                } else {
                                    $scope.otherError = true;
                                }
                                $scope.$apply();
                                $scope.showErrorPopup();
//                        console.error(err);
                            };
                            // Make the request for the media
                            navigator.getUserMedia({
                                video: {
                                    mandatory: {
                                        maxHeight: 380
                                    }
                                },
                                audio: false
                            }, onSuccess, onFailure);

                            $scope.w = w;
                            $scope.h = h;
                        };
                    }]
            };
        }]);

    globalProvider.provide.factory('Imageuploadservice', ['$resource', '$rootScope', function(resource, rootScope) {
            var user = resource(
                    rootScope.apipath + 'fileUpload/:action', //url being hit
                    {
                        action: '@actionName'
                    }, // url perameters

            {
                submitFile: {
                    method: 'POST',
                    isArray: false,
                    params: {
                        action: 'onsubmit'
                    }
                },
                cancelFile: {
                    method: 'POST',
                    isArray: false,
                    params: {
                        action: 'oncancel'
                    }
                },
                removeImageFile: {
                    method: 'POST',
                    params: {
                        action: 'removeImageFile'
                    }
                },
                cancelAll: {
                    method: 'POST',
                    isArray: false,
                    params: {
                        action: 'oncancelall'
                    }
                },
                uploadFile: {
                    method: 'POST',
                    isArray: false,
                    params: {
                        action: 'uploadWebcamFile'

                    }
                },
                getTempFileName: {
                    method: 'POST',
                    isArray: false,
                    params: {
                        action: 'getTempFileName'

                    }
                }
            });
            return user;
        }]);
});
