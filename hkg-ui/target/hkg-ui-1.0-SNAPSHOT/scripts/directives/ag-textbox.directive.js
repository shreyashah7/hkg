define(['angular'], function() {
    angular.module('hkg.directives').directive('agTextbox', ["$parse", "$compile", function($parse, $compile) {
            return {
                restrict: 'E',
//                replace: true,
                link: function linkFn(scope, element, attrs) {

//                    console.log('=======linkingFn(', scope, element, attrs, ')========');
                    var formName = $(element).closest("form").attr('name');
                    var cmpntName = attrs.agName;
                    var cmpntId = attrs.agId;
                    var required = attrs.required === undefined ? false : true;
                    var reqMsg = attrs.reqMsg;
                    var validMsg = attrs.validMsg;
                    var agModel = attrs.agModel;
                    var gridClass = attrs.agGridClass;

//                    scope.$watch(attrs, function() {
                    var firstDiv = "";
                    var inputText = "";
                    var otherDiv = "";

                    if (required) {
                        firstDiv = "<div class='" + gridClass + "' ng-class={'has-error':" + formName + "." + cmpntName + ".$invalid}>";
                        inputText = "<input type=text class=form-control id=" + cmpntId + " name=" + cmpntName + " ng-model=" + agModel + " required /> ";
                        otherDiv = "<div class='error help-block' ng-show='" + formName + "." + cmpntName + ".$dirty && " + formName + "." + cmpntName + ".$invalid'><span class='help-block' ng-show='" + formName + "." + cmpntName + ".$error.required'>" + reqMsg + "</span><span class='help-block' ng-show=" + formName + "." + cmpntName + ".$error.pattern>" + validMsg + "</span> </div></div>";
                    } else {
                        firstDiv = "<div class='" + gridClass + "'>";
                        inputText = "<input type=text class=form-control id=" + cmpntId + " name=" + cmpntName + " ng-model=" + agModel + " /> ";
                        otherDiv = "";
                    }
//                        element.html('').append($compile(inputText)(scope));
                    element.html('').append(firstDiv + inputText + otherDiv);
                    $compile(element.contents())(scope);

//                        alert($(element).closest("form").attr('name'));
//                        alert(document.getElementById('menulabel'));
//                        alert(myform.MenuLabel);


//                        element.html('').append(inputText);
//                        $compile($('#newformid').html())(scope);

//                        var firstDiv = "<ng-form name=newform><div class='col-md-8' ng-class={'has-error':myform." + attrs.name + ".$invalid}>"
//                        var divToAppend = "<div class='error help-block' ng-show='newform." + attrs.name + ".$dirty && newform." + attrs.name + ".$invalid'><span class='help-block' ng-show='newform." + attrs.name + ".$error.required'>" + attrs.reqMsg + "</span> </div></div></ng-form>";
//                        $(element).before(firstDiv);
//                        $(element).append($compile(divToAppend)(scope));
//                    });
                }
            };
        }]);
});


/**  Demo - How to use

<ag-textbox ag-grid-class="col-md-4" ag-id="menulabel" ag-name="MenuLabel"
                    ag-model="feature.menuLabel" required req-msg="Enter menu label" valid-msg="Enter valid menu label">
                </ag-textbox> */