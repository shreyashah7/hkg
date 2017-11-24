define(['angular', 'colResizable'], function () {
    globalProvider.compileProvider.directive('colResizeable', ['$compile', function ($compile) {
            return {
                restrict: 'A',
                link: function (scope, elem) {
                    setTimeout(function () {
                        elem.colResizable({
                            liveDrag: true,
                            fixed:false,
                            gripInnerHtml: "<div class='grip'></div>",
                            draggingClass: "dragging"});
                    });
                }
            };
        }]);
});