define(["angular"],function(){globalProvider.provide.factory("CameraService",["$window",function(c){var b=function(){return !!a()
};
var a=function(){navigator.getUserMedia=(c.navigator.getUserMedia||c.navigator.webkitGetUserMedia||c.navigator.mozGetUserMedia||c.navigator.msGetUserMedia);
return navigator.getUserMedia
};
return{hasUserMedia:b(),getUserMedia:a}
}]);
globalProvider.controllerProvider.register("CameraController",["$scope","CameraService",function(b,a){b.hasUserMedia=a.hasUserMedia
}]);
globalProvider.compileProvider.directive("camera",["CameraService","Imageuploadservice","$modal",function(a,c,b){return{restrict:"EA",replace:true,transclude:true,scope:{modelValue:"=",modelName:"@",featureName:"@",fileType:"@",uniqueId:"@"},templateUrl:"scripts/directives/webcapture/webcam.tmpl.html",link:function(e,f,d){$(document).ready(function(){$("video").bind("contextmenu",function(){return false
})
})
},controller:["$scope","$q","$timeout","$attrs","$rootScope","$element",function(h,g,k,e,d,f){h.deviceNotFound=false;
h.permissionDenied=false;
h.otherError=false;
function i(l){return l.replace(/(!|"|#|\$|%|\'|\(|\)|\*|\+|\,|\.|\/|\:|\;|\?|@)/g,function(m,n){return"\\"+n
})
}h.elementId=(h.uniqueId===undefined?h.modelName:h.uniqueId)+"-play";
h.videoElementId=i(h.elementId)+"-video";
try{h.isCustom=JSON.parse(e.isCustom.toLowerCase())
}catch(j){h.isCustom=false;
console.log("Can not parse isCustom"+j)
}try{h.isThumbnail=JSON.parse(e.isThumbnail.toLowerCase())
}catch(j){h.isThumbnail=false;
console.log("Can not parse is thumbnail"+j)
}if(e.height!==undefined){h.height=e.height
}else{h.height=null
}if(e.width!==undefined){h.width=e.width
}else{h.width=null
}if(h.featureName===undefined){h.featureName=null
}if(h.fileType===undefined){h.fileType=null
}h.takeSnapshot=function(){var m=document.createElement("canvas"),l=m.getContext("2d"),n=document.getElementById(h.videoElementId);
m.width=h.w;
m.height=h.h;
k(function(){l.fillRect(0,0,h.w,h.h);
l.drawImage(n,0,0,h.w,h.h);
var o={};
o.file=m.toDataURL("image/jpeg");
o.fileName="web_cam_capture_"+new Date().getTime()+".jpeg";
o.modelName=h.modelName;
m.remove();
h.hidePopup();
if(h.isCustom){c.uploadFile(o,function(p){var q=[o.fileName,o.modelName];
c.submitFile(q,function(r){if(r.res){h.modelValue=[r.res]
}},function(r){alert("Image transfer failed")
})
},function(p){alert("Image upload failed")
})
}else{c.uploadFile(o,function(p){var q=[o.fileName,h.fileType,h.featureName];
c.getTempFileName(q,function(s){var r=s.data;
var t=[o.fileName,o.modelName,r,h.isThumbnail];
if(h.width!==null&&h.height!==null){t.push(h.width);
t.push(h.height)
}c.submitFile(t,function(u){if(u.res){h.modelValue=u.res
}},function(u){alert("Image transfer failed")
})
},function(r){alert("Image upload failed.")
})
},function(p){alert("Image upload failed")
})
}},0)
};
h.playVideo=function(n){var m=document.getElementById(h.videoElementId);
if(navigator.mozGetUserMedia){m.mozSrcObject=n
}else{var l=window.URL||window.webkitURL;
m.src=window.URL.createObjectURL(n)
}m.play()
};
h.hidePopup=function(){if(h.videoComponentModal){h.videoComponentModal.dismiss()
}d.removeModalOpenCssAfterModalHide();
d.stream.stop()
};
h.showPopup=function(m){var l="videoComponent.html";
h.videoComponentModal=b.open({templateUrl:l,scope:h});
k(function(){h.playVideo(m)
});
h.videoComponentModal.result.then(function(){},function(){if(angular.isDefined(d.stream)){d.stream.stop()
}})
};
h.hideErrorPopup=function(){if(h.videoComponentErrorModal){h.videoComponentErrorModal.dismiss()
}d.removeModalOpenCssAfterModalHide()
};
h.showErrorPopup=function(){var l="videoComponentError.html";
h.videoComponentErrorModal=b.open({templateUrl:l,scope:h})
};
h.initWebCam=function(){h.deviceNotFound=false;
h.permissionDenied=false;
h.otherError=false;
var l=e.width||500,n=e.height||370;
if(!a.hasUserMedia){return
}var o=function(p){d.stream=p;
h.showPopup(p)
};
var m=function(p){if(p.name==="DevicesNotFoundError"){h.deviceNotFound=true
}else{if(p.name==="PermissionDeniedError"){h.permissionDenied=true
}else{h.otherError=true
}}h.$apply();
h.showErrorPopup()
};
navigator.getUserMedia({video:{mandatory:{maxHeight:380}},audio:false},o,m);
h.w=l;
h.h=n
}
}]}
}]);
globalProvider.provide.factory("Imageuploadservice",["$resource","$rootScope",function(c,b){var a=c(b.apipath+"fileUpload/:action",{action:"@actionName"},{submitFile:{method:"POST",isArray:false,params:{action:"onsubmit"}},cancelFile:{method:"POST",isArray:false,params:{action:"oncancel"}},removeImageFile:{method:"POST",params:{action:"removeImageFile"}},cancelAll:{method:"POST",isArray:false,params:{action:"oncancelall"}},uploadFile:{method:"POST",isArray:false,params:{action:"uploadWebcamFile"}},getTempFileName:{method:"POST",isArray:false,params:{action:"getTempFileName"}}});
return a
}])
});