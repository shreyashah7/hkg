define(["angular","colResizable"],function(){globalProvider.compileProvider.directive("colResizeable",["$compile",function(a){return{restrict:"A",link:function(b,c){setTimeout(function(){c.colResizable({liveDrag:true,fixed:false,gripInnerHtml:"<div class='grip'></div>",draggingClass:"dragging"})
})
}}
}])
});