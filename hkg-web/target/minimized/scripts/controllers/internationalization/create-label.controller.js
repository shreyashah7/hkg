define(["hkg","internationalizationService","alert.directive"],function(a,b){a.register.controller("InternationalizationAddLabel",["$rootScope","$scope","Internationalization",function(c,d,e){d.i18Int="INTERNATIONALIZATION.";
d.pageSetup=function(){d.listLabelRowCount=[1,2,3,4,5,10,15,20];
d.selectedRowCount=1;
d.label=[];
d.submitted=false;
e.getConstants(function(f){d.labelTypes=f.types;
d.labelEnvironments=f.environments;
d.labelEntitys=f.entities;
d.setDefaultTable(d.labelTypes,d.labelEnvironments,d.labelEntitys)
},function(){var g="Failed to get Constants";
var f=c.failure;
c.addMessage(g,f)
})
};
d.setDefaultTable=function(h,g,f){d.selectedLabelTypeForAll=Object.keys(h)[0];
d.selectedLabelEnvironmentForAll=Object.keys(g)[0];
d.selectedLabelEntityForAll=f[0];
d.label.push({text:"",type:Object.keys(h)[0],environment:Object.keys(g)[0],entity:f[0]})
};
d.setNumberDropdown=function(){for(var f=0;
f<(d.selectedRowCount-1);
f++){d.label.push({text:"",type:Object.keys(d.labelTypes)[0],environment:Object.keys(d.labelEnvironments)[0],entity:d.labelEntitys[0]})
}};
d.setSelectedType=function(f,g){g.type=f
};
d.setSelectedEnvironment=function(g,f){f.environment=g
};
d.setSelectedEntity=function(f,g){g.entity=f
};
d.getRange=function(){return new Array(d.selectedRowCount)
};
d.setSelectedTypeAll=function(g){d.selectedLabelTypeForAll=g;
for(var f=0;
f<d.selectedRowCount;
f++){d.label[f].type=g
}};
d.setSelectedEnvironmentAll=function(f){d.selectedLabelEnvironmentForAll=f;
for(var g=0;
g<d.selectedRowCount;
g++){d.label[g].environment=f
}};
d.setSelectedEntityAll=function(){for(var f=0;
f<d.selectedRowCount;
f++){d.label[f].entity=d.selectedLabelEntityForAll
}};
d.addLabel=function(){d.submitted=true;
d.textsubmitted=true;
for(var g=0;
g<d.selectedRowCount;
g++){console.log(JSON.stringify(d.label[g]))
}if(d.labelform.$valid){var h=function(){toastr.success("Labels created");
d.textsubmitted=false;
d.labelform.$setPristine();
d.pageSetup()
};
var f=function(){toastr.error("Labels not created")
};
e.createLabel(d.label,h,f)
}};
d.fnCopyLabel=function(){e.copyLabelToPropertyFile(function(){var g="Labels Added Succesfully.";
var f=c.success;
c.addMessage(g,f)
},function(){var g="Failed to Add Labels. ";
var f=c.failure;
c.addMessage(g,f)
})
}
}])
});