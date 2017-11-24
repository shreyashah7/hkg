define(["hkg"],function(a){a.register.factory("CustomFieldService",["$resource","$rootScope",function(d,b){var c=d(b.apipath+"customfield/:action",{action:"@actionName"},{retrieveFeatures:{method:"GET",isArray:false,params:{action:"retrievefeatures"}},retrieveSectionAndCustomFieldInfoByFeatureId:{method:"POST",isArray:false,params:{action:"retrievesectionandcustomfields"}},retrieveSectionAndCustomFieldInfoTemplateByFeatureId:{method:"POST",isArray:false,params:{action:"retrievesectionandcustomfieldtemplate"}},create:{method:"PUT",isArray:false,params:{action:"create"}}});
return c
}])
});