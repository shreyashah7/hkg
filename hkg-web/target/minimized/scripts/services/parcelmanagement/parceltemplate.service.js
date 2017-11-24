define(["hkg"],function(a){a.register.service("ParcelTemplateService",[function(){var f;
var b;
var d;
var c;
var e;
return{setEditableFields:function(g){f=g
},getEditableFields:function(){return f
},setSelectedParcelData:function(g){b=g
},getSelectedParcelData:function(){return b
},setSelectedParcels:function(h){if(h!==undefined&&h!==null&&h.length>0){d={};
for(var g=0;
g<h.length;
g++){d[h[g].parcel]={carat:h[g].sellCarats,pieces:h[g].sellPieces,exRate:h[g].exchangeRate}
}}},getSelectedParcels:function(){return d
},setFeatureName:function(g){c=g
},getFeatureName:function(){return c
},setEntityName:function(g){e=g
},getEntityName:function(){return e
}}
}])
});