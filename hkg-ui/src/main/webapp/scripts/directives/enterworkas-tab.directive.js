define(['angular'], function() {
    /**
     * @author ashish
     * pdkNextInputOnEnter - Directive for Replace behaviour of Enter Key to Tab Key for Entire System
     * Usage : <body ng-controller="MainController" pdk-next-input-on-enter>
     */
    angular.module('hkg.directives').directive('pdkNextInputOnEnter', [function() {
//            var includeTags = ['INPUT', 'SELECT', 'TEXTAREA'];
            function link(scope, element, attrs) {
                $('body').on('shown.bs.modal', '.modal', function() {
                    $('input,select,button,[multi-select],[ui-select2],[data-toggle],textarea', this).filter(':visible:first').focus();
                })
                element.on('keydown', function(e) {
                    // Go to next form element on enter and only for included tags
                    if (e.keyCode == 13) {
                        if (e.shiftKey) {
                            return true;
                        }
                        // Find all form elements that can receive focus
                        var focusable = element[0].querySelectorAll('input,select,button,[multi-select],[ui-select2],[data-toggle],textarea');
                        // Get the index of the currently focused element
                        var currentIndex = Array.prototype.indexOf.call(focusable, e.target);
                        // Find the next items in the list
                        var nextIndex = currentIndex == focusable.length - 1 ? 0 : currentIndex + 1;
                        // Focus the next element

                        if (nextIndex >= 0 && nextIndex < focusable.length) {
                            if (focusable[currentIndex] !== null && focusable[currentIndex] !== undefined && (focusable[currentIndex].localName !== "button" || (focusable[currentIndex].localName === "button") && (focusable[currentIndex].className.indexOf("btn-hkg") === -1))) {
                                while ((focusable[nextIndex].offsetWidth === 0 && focusable[nextIndex].offsetHeight === 0) || (focusable[nextIndex].id !== undefined && focusable[nextIndex].id !== null && element.offsetWidth === 0 && element.offsetHeight === 0) || (!focusable[nextIndex].id && $(focusable[nextIndex].localName).is(":visible") === false) || (focusable[nextIndex].style.visibility === "hidden") || ((focusable[nextIndex].localName === "button") && (focusable[nextIndex].className.indexOf("btn-hkg") === -1) && (focusable[nextIndex].className.indexOf("dropdown-toggle") === -1 && focusable[nextIndex].className.indexOf("multiSelectButton") === -1)) || ((focusable[nextIndex].localName === "input") && (focusable[nextIndex].className.indexOf("inputFilter") > -1)) || ((focusable[nextIndex].localName === "input") && (focusable[nextIndex].className.indexOf("checkbox") > -1)) || (focusable[nextIndex].tabIndex === -1))
                                {
                                    nextIndex++;
                                }
                                if (focusable[currentIndex].className.indexOf("dropdown-toggle") !== -1) {
                                    $('[data-toggle="dropdown"]').parent().removeClass('open');
                                }
                                focusable[nextIndex].focus();
                                return false;
                            } else {
                                return true;
                            }
                        }

                    }
                });
            }
            return {
                restrict: 'A',
                link: link
            };
        }]);
});
