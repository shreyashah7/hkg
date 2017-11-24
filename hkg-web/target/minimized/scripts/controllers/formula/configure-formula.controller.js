define(["hkg","jqueryUi","selectize","selectize.directive"],function(hkg){hkg.register.controller("ConfigureFormula",["$rootScope","$scope",function($rootScope,$scope){var math=mathjs();
$scope.i18FormulaConfiguration="FORMULACONFIGURATION";
$scope.selectizeOptions={plugins:["drag_drop"],persist:false,enableDuplicate:true,hideSelected:false,create:true,valueField:"id",labelField:"text",searchField:"text",delimiter:","};
$scope.options=[{id:"field_1",text:"Field 1"},{id:"field_2",text:"Field 2"},{id:"field_3",text:"Field 3"}];
$scope.findField=function(fieldId){for(var i=0;
i<$scope.options.length;
i++){if($scope.options[i].id===fieldId){return true
}}return false
};
$scope.generateFormula=function(){var formula=$scope.formula.split(",");
var generatedFormula="";
var mulitpliedField=0;
for(var i=0;
i<formula.length;
i++){if($scope.findField(formula[i])){mulitpliedField++;
if(mulitpliedField>=2){generatedFormula+="*"
}}else{mulitpliedField=0
}generatedFormula+=formula[i]
}return generatedFormula
};
$scope.saveFormula=function(){alert($scope.generateFormula())
};
$scope.validateFormula=function(){var formula=$scope.generateFormula();
var formulaWithValue=formula;
for(var i=0;
i<$scope.options.length;
i++){if(formula.indexOf($scope.options[i].id)>=0){var re=new RegExp($scope.options[i].id,"g");
formulaWithValue=formulaWithValue.replace(re,"1")
}}try{math.eval(formulaWithValue);
$scope.formulaConfigForm.formula.$setValidity("invalidFormula",true)
}catch(e){console.log(e);
$scope.formulaConfigForm.formula.$setValidity("invalidFormula",false)
}}
}])
});