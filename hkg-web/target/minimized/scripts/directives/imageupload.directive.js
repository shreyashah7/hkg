define(["angular"],function(){angular.module("hkg.directives").directive("agImageUpload",["$q",function(b){var c=function(f){var e=b.defer();
var d=new FileReader();
d.onload=function(g){e.resolve(g.target.result)
};
d.readAsDataURL(f);
return e.promise
};
return{restrict:"A",scope:{agImageUpload:"="},link:function a(g,e,d,h){var f=function(i){g.$apply(function(){if(d.multiple){g.agImageUpload.push(i)
}else{g.agImageUpload=i
}})
};
e.bind("change",function(k){if(d.multiple){g.agImageUpload=[]
}var m=k.target.files;
for(var l=0;
l<m.length;
l++){var j={file:m[l],url:URL.createObjectURL(m[l])};
c(m[l]).then(function(i){j.dataURL=i
});
f(j)
}})
}}
}])
});