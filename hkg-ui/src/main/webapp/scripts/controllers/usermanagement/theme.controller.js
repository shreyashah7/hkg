/**
 * This controller is for manage employee feature
 * Author : Mansi Parekh
 * Date : 30 July 2014
 */
define(['hkg', 'employeeService', 'fileUploadService'], function (hkg, employeeService, fileUploadService) {

    hkg.register.controller('ThemeController', ["$rootScope", "$scope", "Employee", "FileUploadService", function ($rootScope, $scope, Employee, FileUploadService) {
            $rootScope.maskLoading();
            $rootScope.mainMenu = "";
            $rootScope.childMenu = "";
            $rootScope.activateMenu();
            $scope.entity = "EMPLOYEE.";
            $scope.today = new Date();
            $scope.$on('$viewContentLoaded', function () {
                retrievePrerequisite();
            });

            function retrievePrerequisite() {
                $rootScope.maskLoading();
                Employee.retrieveThemes(function (res) {
                    var theme = $rootScope.session.theme;
                    angular.forEach(res, function (item) {
                        if (theme !== null && theme === item.folderName) {
                            item.isChecked = true;
                            $scope.selectedtheme = item.folderName;
                        } else {
                            item.isChecked = false;
                        }
                    });
                    $scope.themes = res;
                    displayGallery();
                    $rootScope.unMaskLoading();
                }, function () {
                    $rootScope.unMaskLoading();
                });
            }

            //To display gallery popup
            $scope.showGalleryPopup = function () {
                $scope.selectedTab = 1;
                $("#addGalleryPopup").modal("show");
            };

            //To hide gallery popup
            $scope.hideGalleryPopup = function () {
                $("#addGalleryPopup").modal("hide");
                $rootScope.removeModalOpenCssAfterModalHide();
            };

            $scope.resetTheme = function () {
                $scope.selectedtheme = "default";
                $scope.setTheme();
                delete $rootScope.defaultWallpaperName;
            };

            $scope.setTheme = function () {
                $rootScope.maskLoading();
                var theme = {folderName: $scope.selectedtheme};
                Employee.setTheme(theme, function (res) {
                    $rootScope.setThemeFolder(theme.folderName);
                    $rootScope.setDefaultWallpaper();
                    $rootScope.unMaskLoading();
                }, function () {
                    $rootScope.unMaskLoading();
                });

            };

            $scope.saveWallpaper = function (file) {
                var filemap = {file: file};
                Employee.createPhotoGallery(filemap, function (res) {
                    $rootScope.unMaskLoading();
                    $rootScope.retrieveWallpaper();
                    displayGallery();
                    $scope.hideGalleryPopup();
                }, function () {
                    $rootScope.unMaskLoading();
                });
            };
            //          For gallery file upload
            $scope.galleryUploadFile = {
                target: $rootScope.apipath+'fileUpload/uploadFile',
                singleFile: true,
                testChunks: true,
                query: {
                    fileType: $scope.seletedFileType,
                    model: 'Franchise'
                }
            };
            //For gallery file 
            $scope.galleryFileAdded = function (file, flow) {
                $scope.galleryFlow = flow;
                $scope.galleryUploadFile.query.fileType = "BACKGROUND";
                var maxsize = 5000000;
                var filesize = file.size;
                if ((file.getExtension() !== "jpg") && (file.getExtension() !== "jpeg") && (file.getExtension() !== "png") && (file.getExtension() !== "gif")) {
                    $scope.validFileFlag = true;
                    $scope.fileNames.push(file.name);
                    alert('Only images are supported');
                    return false;
                } else {
                    if (maxsize < filesize) {
                        $scope.validFileFlag = true;
                        $scope.fileNames.push(file.name);
                        alert('You can upload a file upto 5 MB ');
                        return false;
                    }
                    return true;
                }
            };
            // //gallery file upload success
            $scope.galleryFileUploaded = function (file, flow, response) {

                var info = [file.name, modelName, $scope.seletedFileType];
                var fileType = "BACKGROUND";
                var modelName = 'Franchise';
                var fileDetail = [file.name, fileType];
                var filenameformat;
                var thumbnail = "false";
                var info;

                Employee.uploadFile(fileDetail, function (result)
                {
                    filenameformat = result.filename;
                    info = [file.name, modelName, filenameformat, thumbnail];
                    FileUploadService.uploadFiles(info, function (response) {
                        submitFilesAndSaveGallery(flow, response.res);
                    });
                });
            };
            //save gallery files in folder as well as database if not updated
            function submitFilesAndSaveGallery(flow, file) {
                $rootScope.maskLoading();
                if (flow.files.length > 0) {
                    var filemap = {file: file, store: ''};
                    Employee.createPhotoGallery(filemap, function (res) {
                        $rootScope.unMaskLoading();
                        $scope.hideGalleryPopup();
                        displayGallery();
                        $rootScope.retrieveWallpaper();
                        flow.cancel();
                    }, function () {
                        $rootScope.unMaskLoading();
                    });
                }
            }
            ;
            function displayGallery() {
                $scope.photos = [];
                if ($scope.photos.length === 0) {
                    Employee.retrieveImageThumbnailPaths(function (res) {
                        var src = res.src;
                        angular.forEach(src, function (item) {
                            var thumbnail = item.substring(0, item.lastIndexOf("."));
                            thumbnail += "_T.jpg";
                            $scope.photos.push({
                                src: $rootScope.appendAuthToken($rootScope.apipath+'employee/getimage?file_name=' + thumbnail),
                                name: item,
                                isChecked: false
                            });
                        });
                    });
                }
            }

            $scope.removeWallpaper = function (index, name) {
                if (name === $rootScope.defaultWallpaperName) {
                    $("#activeWallpaperModal").modal("show");
                } else {
                    $scope.photos.splice(index, 1);
                    Employee.removeFileFromTemp(name);
                }
            };

            $scope.hideModal = function () {
                $("#activeWallpaperModal").modal("hide");
                $rootScope.removeModalOpenCssAfterModalHide();
            };

            $(function () {
                $(window).bind('mousewheel', function (event, delta) {
                    $("#sidemenu").height($("#mainPanel").height());
                });

                $(window).bind('scroll', function (event) {
                    $("#sidemenu").height($("#mainPanel").height());
                });
            });

            $scope.scrollTo = function (eID) {
                // This scrolling function 
                // is from http://www.itnewb.com/tutorial/Creating-the-Smooth-Scroll-Effect-with-JavaScript

                var startY = currentYPosition();
                var stopY = elmYPosition(eID);
                var distance = stopY > startY ? stopY - startY : startY - stopY;
                if (distance < 100) {
                    scrollTo(0, stopY - 60);
                    return;
                }
                var speed = Math.round(distance / 100);
                if (speed >= 20)
                    speed = 20;
                var step = Math.round(distance / 25);
                var leapY = stopY > startY ? startY + step : startY - step;
                var timer = 0;
                if (stopY > startY) {
                    for (var i = startY; i < stopY; i += step) {
                        setTimeout("window.scrollTo(0, " + (leapY - 60) + ")", timer * speed);
                        leapY += step;
                        if (leapY > stopY)
                            leapY = stopY;
                        timer++;
                    }
                    return;
                }
                for (var i = startY; i > stopY; i -= step) {
                    setTimeout("window.scrollTo(0, " + (leapY - 60) + ")", timer * speed);
                    leapY -= step;
                    if (leapY < stopY)
                        leapY = stopY;
                    timer++;
                }

                function currentYPosition() {
                    // Firefox, Chrome, Opera, Safari
                    if (self.pageYOffset)
                        return self.pageYOffset;
                    // Internet Explorer 6 - standards mode
                    if (document.documentElement && document.documentElement.scrollTop)
                        return document.documentElement.scrollTop;
                    // Internet Explorer 6, 7 and 8
                    if (document.body.scrollTop)
                        return document.body.scrollTop;
                    return 0;
                }

                function elmYPosition(eID) {
                    var elm = document.getElementById(eID);
                    var y = elm.offsetTop;
                    var node = elm;
                    while (node.offsetParent && node.offsetParent != document.body) {
                        node = node.offsetParent;
                        y += node.offsetTop;
                    }
                    return y;
                }

            };

            $rootScope.unMaskLoading();
        }]);

});
