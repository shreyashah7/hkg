define(["angular"],function(){angular.module("hkg.directives").directive("agPopover",["$http",function(a){return{restrict:"A",transclude:true,template:"<span ng-transclude></span>",link:function(d,c,b){d.popoverObj=$(c).popover({placement:"bottom",container:"body",html:true,content:function(){return $(c).next(".popper-content").html()
}});
if(b.agPopover!==undefined&&b.agPopover==="mouseover"){d.popoverObj.on("mouseenter",function(){var e=this;
$(this).popover("show");
$(this).siblings(".popover").on("mouseleave",function(){$(e).popover("hide")
})
}).on("mouseleave",function(){var e=this;
setTimeout(function(){$(e).popover("hide")
},100)
})
}}}
}])
});