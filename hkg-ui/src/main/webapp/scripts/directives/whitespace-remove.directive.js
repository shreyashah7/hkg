define(['angular'], function() {
    globalProvider.compileProvider.directive('whitespace',
            ['$parse', '$compile', function($parse, $compile) {
                    return {
                        require: ['^form', 'ngModel'],
                        link: function(scope, element, attrs, ctrls) {
                            var modelCtrl = ctrls[1];
                            element.bind("keydown keypress", function(event) {
                                if (event.which === 32) {
                                    event.preventDefault();
                                }
                            });
                        }
                    };
                }]);
});
