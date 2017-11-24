define(["hkg","employeeService","fileUploadService"],function(b,a,c){b.register.controller("ThemeController",["$rootScope","$scope","Employee","FileUploadService",function(e,f,h,g){e.maskLoading();
e.mainMenu="";
e.childMenu="";
e.activateMenu();
f.entity="EMPLOYEE.";
f.today=new Date();
f.$on("$viewContentLoaded",function(){d()
});
function d(){e.maskLoading();
h.retrieveThemes(function(k){var l=e.session.theme;
angular.forEach(k,function(m){if(l!==null&&l===m.folderName){m.isChecked=true;
f.selectedtheme=m.folderName
}else{m.isChecked=false
}});
f.themes=k;
i();
e.unMaskLoading()
},function(){e.unMaskLoading()
})
}f.showGalleryPopup=function(){f.selectedTab=1;
$("#addGalleryPopup").modal("show")
};
f.hideGalleryPopup=function(){$("#addGalleryPopup").modal("hide");
e.removeModalOpenCssAfterModalHide()
};
f.resetTheme=function(){f.selectedtheme="default";
f.setTheme();
delete e.defaultWallpaperName
};
f.setTheme=function(){e.maskLoading();
var k={folderName:f.selectedtheme};
h.setTheme(k,function(l){e.setThemeFolder(k.folderName);
e.setDefaultWallpaper();
e.unMaskLoading()
},function(){e.unMaskLoading()
})
};
f.saveWallpaper=function(k){var l={file:k};
h.createPhotoGallery(l,function(m){e.unMaskLoading();
e.retrieveWallpaper();
i();
f.hideGalleryPopup()
},function(){e.unMaskLoading()
})
};
f.galleryUploadFile={target:e.apipath+"fileUpload/uploadFile",singleFile:true,testChunks:true,query:{fileType:f.seletedFileType,model:"Franchise"}};
f.galleryFileAdded=function(n,l){f.galleryFlow=l;
f.galleryUploadFile.query.fileType="BACKGROUND";
var k=5000000;
var m=n.size;
if((n.getExtension()!=="jpg")&&(n.getExtension()!=="jpeg")&&(n.getExtension()!=="png")&&(n.getExtension()!=="gif")){f.validFileFlag=true;
f.fileNames.push(n.name);
alert("Only images are supported");
return false
}else{if(k<m){f.validFileFlag=true;
f.fileNames.push(n.name);
alert("You can upload a file upto 5 MB ");
return false
}return true
}};
f.galleryFileUploaded=function(n,l,p){var m=[n.name,r,f.seletedFileType];
var s="BACKGROUND";
var r="Franchise";
var o=[n.name,s];
var q;
var k="false";
var m;
h.uploadFile(o,function(t){q=t.filename;
m=[n.name,r,q,k];
g.uploadFiles(m,function(u){j(l,u.res)
})
})
};
function j(k,l){e.maskLoading();
if(k.files.length>0){var m={file:l,store:""};
h.createPhotoGallery(m,function(n){e.unMaskLoading();
f.hideGalleryPopup();
i();
e.retrieveWallpaper();
k.cancel()
},function(){e.unMaskLoading()
})
}}function i(){f.photos=[];
if(f.photos.length===0){h.retrieveImageThumbnailPaths(function(k){var l=k.src;
angular.forEach(l,function(m){var n=m.substring(0,m.lastIndexOf("."));
n+="_T.jpg";
f.photos.push({src:e.appendAuthToken(e.apipath+"employee/getimage?file_name="+n),name:m,isChecked:false})
})
})
}}f.removeWallpaper=function(l,k){if(k===e.defaultWallpaperName){$("#activeWallpaperModal").modal("show")
}else{f.photos.splice(l,1);
h.removeFileFromTemp(k)
}};
f.hideModal=function(){$("#activeWallpaperModal").modal("hide");
e.removeModalOpenCssAfterModalHide()
};
$(function(){$(window).bind("mousewheel",function(k,l){$("#sidemenu").height($("#mainPanel").height())
});
$(window).bind("scroll",function(k){$("#sidemenu").height($("#mainPanel").height())
})
});
f.scrollTo=function(t){var r=p();
var k=u(t);
var l=k>r?k-r:r-k;
if(l<100){scrollTo(0,k-60);
return
}var o=Math.round(l/100);
if(o>=20){o=20
}var n=Math.round(l/25);
var s=k>r?r+n:r-n;
var m=0;
if(k>r){for(var q=r;
q<k;
q+=n){setTimeout("window.scrollTo(0, "+(s-60)+")",m*o);
s+=n;
if(s>k){s=k
}m++
}return
}for(var q=r;
q>k;
q-=n){setTimeout("window.scrollTo(0, "+(s-60)+")",m*o);
s-=n;
if(s<k){s=k
}m++
}function p(){if(self.pageYOffset){return self.pageYOffset
}if(document.documentElement&&document.documentElement.scrollTop){return document.documentElement.scrollTop
}if(document.body.scrollTop){return document.body.scrollTop
}return 0
}function u(v){var z=document.getElementById(v);
var x=z.offsetTop;
var w=z;
while(w.offsetParent&&w.offsetParent!=document.body){w=w.offsetParent;
x+=w.offsetTop
}return x
}};
e.unMaskLoading()
}])
});