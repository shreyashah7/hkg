define(["hkg","designationService","featureService","uiBootstrap","ngload!ngTable","alert.directive"],function(a,b,c){a.register.controller("ManagePermissionSet",["$rootScope","$scope","RoleManagement","SysFeature","$filter","ngTableParams","$location",function(e,f,d,k,h,g,j){e.maskLoading();
f.i18Designation="DESIGNATION";
if(e.permissionFeatureIdMap==undefined){j.path("/managedesignation")
}else{f.deletePermissionId=e.permissionFeatureIdMap.permissionId;
f.deleteFeatureId=e.permissionFeatureIdMap.featureId;
f.selectedpermissonid=f.deletePermissionId
}f.designations=[];
f.finalUpdatedMap={};
var i=[];
d.retrieveDesignationsByPermissionId(e.permissionFeatureIdMap,function(m){f.designations=m.designations;
f.permissionSets=m.permissionsets;
if(f.designations!=null){for(var l;
l<f.designations.length;
l++){f.designations[l].isChange=false;
f.designations[l].selectedPermission=null
}}i=f.designations;
f.tableParams.reload()
},function(){var m="Failed to load Designation By Permission";
var l=e.failure;
e.addMessage(m,l)
});
f.tableParams=new g({page:1,count:10,sorting:{title:"asc"},total:f.designations.length},{getData:function(m,n){var l=n.sorting()?h("orderBy")(f.designations,n.orderBy()):f.designations;
l=n.filter()?h("filter")(l,n.filter()):l;
i=l.slice((n.page()-1)*n.count(),n.page()*n.count());
n.total(l.length);
m.resolve(i)
}});
f.setSelectedPermission=function(l,m){if(f.deletePermissionId!==l){m.isChange=true;
m.selectedPermission=l
}else{m.isChange=false;
m.selectedPermission=null
}};
f.updateAllPermissionSet=function(){f.finalMap={};
f.featurePermissionMap={};
if(f.designations!=null){for(var l=0;
l<f.designations.length;
l++){if(f.designations[l].isChange==true){f.finalMap[f.designations[l].value]=f.designations[l].selectedPermission
}}}if(f.deleteFeatureId!==undefined&&f.deletePermissionId!==undefined){f.featurePermissionMap[f.deleteFeatureId]=f.deletePermissionId
}f.finalUpdatedMap={featureId:f.deleteFeatureId,oldPermissionId:f.deletePermissionId,updatedMap:f.finalMap};
d.updateDesignationsPermissionSet(f.finalUpdatedMap,function(m){var o="Permission Set updated succesfully";
var n=e.success;
e.addMessage(o,n);
j.path("/managedesignation")
},function(){var n="Failed to update Permission Set";
var m=e.failure;
e.addMessage(n,m)
})
};
f.backToHome=function(){j.path("/managedesignation")
};
e.unMaskLoading()
}])
});