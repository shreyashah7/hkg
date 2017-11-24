(function(){var a=function(){var b=function(d,e,c){d.$on("export-pdf",function(f,g){e.tableExport({type:"pdf",escape:"false"})
});
d.$on("export-excel",function(f,g){console.log("ffhfhfh");
e.tableExport({type:"excel",escape:false})
});
d.$on("export-doc",function(f,g){e.tableExport({type:"doc",escape:false})
})
};
return{restrict:"C",link:b}
};
angular.module("CustomDirectives",[]).directive("exportTable",a)
})();