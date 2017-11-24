define(['hkg'], function(hkg) {
    hkg.register.factory('PlanService', ['$resource', '$rootScope', function(resource, rootScope) {
            var plan = resource(rootScope.centerapipath + 'plan/:action', {}, {
                savePlans: {
                    method: 'POST',
                    params: {
                        action: '/save'
                    }
                },
                retrievePlansByPacket: {
                    method: 'POST',
                    isArray: false,
                    params: {
                        action: '/retrievebypacket'
                    }
                },
                retrieveModifiers: {
                    method: 'POST',
                    params: {
                        action: '/retrievemodifiers'
                    }
                },
                retrieveFinalPlans: {
                    method: 'POST',
                    isArray: true,
                    params: {
                        action: '/retrievefinalplans'
                    }
                },
                finalizePlan: {
                    method: 'POST',
                    params: {
                        action: '/finalizeplan'
                    }
                }
            });
            return plan;
        }]);
});