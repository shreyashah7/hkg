define(['hkg'], function(hkg) {
    hkg.register.factory('EstPredictService', ['$resource', '$rootScope', function(resource, rootScope) {
            var estPredict = resource(rootScope.centerapipath + 'estpredict/:action', {}, {
            });
            return estPredict;
        }]);
});


