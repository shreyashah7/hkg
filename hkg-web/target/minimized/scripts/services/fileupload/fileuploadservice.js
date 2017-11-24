define(["hkg"],function(a){a.register.factory("FileUploadService",["$resource","$rootScope",function(d,c){var b=d(c.apipath+"fileUpload/:action",{},{uploadFiles:{method:"POST",isArray:false,params:{action:"onsubmit"}},cancelFile:{method:"POST",isArray:false,params:{action:"oncancel"}},removeImageFile:{method:"POST",params:{action:"removeImageFile"}},cancelAll:{method:"POST",isArray:false,params:{action:"oncancelall"}}});
return b
}])
});