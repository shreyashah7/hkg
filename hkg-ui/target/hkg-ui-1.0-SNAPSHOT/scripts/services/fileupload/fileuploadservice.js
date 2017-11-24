define(['hkg'], function(hkg) {
    hkg.register.factory('FileUploadService', ['$resource', '$rootScope', function(resource, rootScope) {
            var user = resource(rootScope.apipath + 'fileUpload/:action', {},
                    {
                       uploadFiles: {
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
                        }
                    });
            return user;
        }]);
});
