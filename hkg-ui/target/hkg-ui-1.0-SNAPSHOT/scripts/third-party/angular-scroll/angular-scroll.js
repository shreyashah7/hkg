/**
 * x is a value between 0 and 1, indicating where in the animation you are.
 */
var duScrollDefaultEasing = function(x) {
    'use strict';

    if (x < 0.5) {
        return Math.pow(x * 2, 2) / 2;
    }
    return 1 - Math.pow((1 - x) * 2, 2) / 2;
};

angular.module('duScroll', [
//    'duScroll.scrollspy',
    'duScroll.smoothScroll',
//    'duScroll.scrollContainer',
//    'duScroll.spyContext',
    'duScroll.scrollHelpers'
])
        //Default animation duration for smoothScroll directive
        .value('duScrollDuration', 350)
        //Scrollspy debounce interval, set to 0 to disable
        .value('duScrollSpyWait', 100)
        //Wether or not multiple scrollspies can be active at once
        .value('duScrollGreedy', false)
        //Default offset for smoothScroll directive
        .value('duScrollOffset', 0)
        //Default easing function for scroll animation
        .value('duScrollEasing', duScrollDefaultEasing);


angular.module('duScroll.scrollHelpers', ['duScroll.requestAnimation'])
        .run(["$window", "$q", "cancelAnimation", "requestAnimation", "duScrollEasing", "duScrollDuration", "duScrollOffset", function($window, $q, cancelAnimation, requestAnimation, duScrollEasing, duScrollDuration, duScrollOffset) {
                'use strict';

                var proto = {};

                var isDocument = function(el) {
                    return (typeof HTMLDocument !== 'undefined' && el instanceof HTMLDocument) || (el.nodeType && el.nodeType === el.DOCUMENT_NODE);
                };

                var isElement = function(el) {
                    return (typeof HTMLElement !== 'undefined' && el instanceof HTMLElement) || (el.nodeType && el.nodeType === el.ELEMENT_NODE);
                };

                var unwrap = function(el) {
                    return isElement(el) || isDocument(el) ? el : el[0];
                };

                proto.duScrollTo = function(left, top, duration, easing) {
                    var aliasFn;
                    if (angular.isElement(left)) {
                        aliasFn = this.duScrollToElement;
                    } else if (angular.isDefined(duration)) {
                        aliasFn = this.duScrollToAnimated;
                    }
                    if (aliasFn) {
                        return aliasFn.apply(this, arguments);
                    }
                    var el = unwrap(this);
                    if (isDocument(el)) {
                        return $window.scrollTo(left, top);
                    }
                    el.scrollLeft = left;
                    el.scrollTop = top;
                };

                var scrollAnimation, deferred;
                proto.duScrollToAnimated = function(left, top, duration, easing) {
                    if (duration && !easing) {
                        easing = duScrollEasing;
                    }
                    var startLeft = this.duScrollLeft(),
                            startTop = this.duScrollTop(),
                            deltaLeft = Math.round(left - startLeft),
                            deltaTop = Math.round(top - startTop);

                    var startTime = null, progress = 0;
                    var el = this;

                    var cancelOnEvents = 'scroll mousedown mousewheel touchmove keydown';
                    var cancelScrollAnimation = function($event) {
                        if (!$event || (progress && $event.which > 0)) {
                            el.unbind(cancelOnEvents, cancelScrollAnimation);
                            cancelAnimation(scrollAnimation);
                            deferred.reject();
                            scrollAnimation = null;
                        }
                    };

                    if (scrollAnimation) {
                        cancelScrollAnimation();
                    }
                    deferred = $q.defer();

                    if (duration === 0 || (!deltaLeft && !deltaTop)) {
                        if (duration === 0) {
                            el.duScrollTo(left, top);
                        }
                        deferred.resolve();
                        return deferred.promise;
                    }

                    var animationStep = function(timestamp) {
                        if (startTime === null) {
                            startTime = timestamp;
                        }

                        progress = timestamp - startTime;
                        var percent = (progress >= duration ? 1 : easing(progress / duration));

                        el.scrollTo(
                                startLeft + Math.ceil(deltaLeft * percent),
                                startTop + Math.ceil(deltaTop * percent)
                                );
                        if (percent < 1) {
                            scrollAnimation = requestAnimation(animationStep);
                        } else {
                            el.unbind(cancelOnEvents, cancelScrollAnimation);
                            scrollAnimation = null;
                            deferred.resolve();
                        }
                    };

                    //Fix random mobile safari bug when scrolling to top by hitting status bar
                    el.duScrollTo(startLeft, startTop);

                    el.bind(cancelOnEvents, cancelScrollAnimation);

                    scrollAnimation = requestAnimation(animationStep);
                    return deferred.promise;
                };

                proto.duScrollToElement = function(target, offset, duration, easing) {
                    var el = unwrap(this);
                    if (!angular.isNumber(offset) || isNaN(offset)) {
                        offset = duScrollOffset;
                    }
                    var top = this.duScrollTop() + unwrap(target).getBoundingClientRect().top - offset;
                    if (isElement(el)) {
                        top -= el.getBoundingClientRect().top;
                    }
                    return this.duScrollTo(0, top, duration, easing);
                };

                proto.duScrollLeft = function(value, duration, easing) {
                    if (angular.isNumber(value)) {
                        return this.duScrollTo(value, this.duScrollTop(), duration, easing);
                    }
                    var el = unwrap(this);
                    if (isDocument(el)) {
                        return $window.scrollX || document.documentElement.scrollLeft || document.body.scrollLeft;
                    }
                    return el.scrollLeft;
                };
                proto.duScrollTop = function(value, duration, easing) {
                    if (angular.isNumber(value)) {
                        return this.duScrollTo(this.duScrollLeft(), value, duration, easing);
                    }
                    var el = unwrap(this);
                    if (isDocument(el)) {
                        return $window.scrollY || document.documentElement.scrollTop || document.body.scrollTop;
                    }
                    return el.scrollTop;
                };

                proto.duScrollToElementAnimated = function(target, offset, duration, easing) {
                    return this.duScrollToElement(target, offset, duration || duScrollDuration, easing);
                };

                proto.duScrollTopAnimated = function(top, duration, easing) {
                    return this.duScrollTop(top, duration || duScrollDuration, easing);
                };

                proto.duScrollLeftAnimated = function(left, duration, easing) {
                    return this.duScrollLeft(left, duration || duScrollDuration, easing);
                };

                angular.forEach(proto, function(fn, key) {
                    angular.element.prototype[key] = fn;

                    //Remove prefix if not already claimed by jQuery / ui.utils
                    var unprefixed = key.replace(/^duScroll/, 'scroll');
                    if (angular.isUndefined(angular.element.prototype[unprefixed])) {
                        angular.element.prototype[unprefixed] = fn;
                    }
                });
            }]);


//Adapted from https://gist.github.com/paulirish/1579671
angular.module('duScroll.polyfill', [])
        .factory('polyfill', ["$window", function($window) {
                'use strict';

                var vendors = ['webkit', 'moz', 'o', 'ms'];

                return function(fnName, fallback) {
                    if ($window[fnName]) {
                        return $window[fnName];
                    }
                    var suffix = fnName.substr(0, 1).toUpperCase() + fnName.substr(1);
                    for (var key, i = 0; i < vendors.length; i++) {
                        key = vendors[i] + suffix;
                        if ($window[key]) {
                            return $window[key];
                        }
                    }
                    return fallback;
                };
            }]);

angular.module('duScroll.requestAnimation', ['duScroll.polyfill'])
        .factory('requestAnimation', ["polyfill", "$timeout", function(polyfill, $timeout) {
                'use strict';

                var lastTime = 0;
                var fallback = function(callback, element) {
                    var currTime = new Date().getTime();
                    var timeToCall = Math.max(0, 16 - (currTime - lastTime));
                    var id = $timeout(function() {
                        callback(currTime + timeToCall);
                    },
                            timeToCall);
                    lastTime = currTime + timeToCall;
                    return id;
                };

                return polyfill('requestAnimationFrame', fallback);
            }])
        .factory('cancelAnimation', ["polyfill", "$timeout", function(polyfill, $timeout) {
                'use strict';

                var fallback = function(promise) {
                    $timeout.cancel(promise);
                };

                return polyfill('cancelAnimationFrame', fallback);
            }]);

angular.module('duScroll.scrollContainerAPI', [])
        .factory('scrollContainerAPI', ["$document", function($document) {
                'use strict';

                var containers = {};

                var setContainer = function(scope, element) {
                    var id = scope.$id;
                    containers[id] = element;
                    return id;
                };

                var getContainerId = function(scope) {
                    if (containers[scope.$id]) {
                        return scope.$id;
                    }
                    if (scope.$parent) {
                        return getContainerId(scope.$parent);
                    }
                    return;
                };

                var getContainer = function(scope) {
                    var id = getContainerId(scope);
                    return id ? containers[id] : $document;
                };

                var removeContainer = function(scope) {
                    var id = getContainerId(scope);
                    if (id) {
                        delete containers[id];
                    }
                };

                return {
                    getContainerId: getContainerId,
                    getContainer: getContainer,
                    setContainer: setContainer,
                    removeContainer: removeContainer
                };
            }]);


angular.module('duScroll.smoothScroll', ['duScroll.scrollHelpers', 'duScroll.scrollContainerAPI'])
        .directive('duSmoothScroll', ["$window","duScrollDuration", "duScrollOffset", "scrollContainerAPI", function($window,duScrollDuration, duScrollOffset, scrollContainerAPI) {
                'use strict';

                return {
                    link: function($scope, $element, $attr) {
                        $element.hide();
                        angular.element($window).bind("scroll", function() {
                            if ($(this).scrollTop() > 100) {
                                $element.fadeIn();
                            } else {
                                $element.fadeOut();
                            }
                        });
                        $element.on('click', function(e) {
                            if (!$attr.href || $attr.href.indexOf('#') === -1)
                                return;

                            var target = document.getElementById($attr.href.replace(/.*(?=#[^\s]+$)/, '').substring(1));
                            if (!target || !target.getBoundingClientRect)
                                return;

                            if (e.stopPropagation)
                                e.stopPropagation();
                            if (e.preventDefault)
                                e.preventDefault();

                            var offset = $attr.offset ? parseInt($attr.offset, 10) : duScrollOffset;
                            var duration = $attr.duration ? parseInt($attr.duration, 10) : duScrollDuration;
                            var container = scrollContainerAPI.getContainer($scope);

                            container.duScrollToElement(
                                    angular.element(target),
                                    isNaN(offset) ? 0 : offset,
                                    isNaN(duration) ? 0 : duration
                                    );
                        });
                    }
                };
            }]);
