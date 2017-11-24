define(['hkg'], function(hkg) {
    hkg.register.factory('Messaging', ['$resource', '$rootScope', function(resource, rootScope) {
            var Messaging = resource(
                    rootScope.apipath + 'messaging/:action', //url being hit
                    {
                        action: '@actionName'
                    }, // url perameters

            {//methods
                saveMessage: {
                    method: 'POST',
                    isArray: false,
                    params: {
                        action: 'savemessage'
                    }
                },
                retrieveMessages: {
                    method: 'GET',
                    isArray: true,
                    params: {
                        action: 'retrieve/messages'
                    }
                },
                retrieveUserList: {
                    method: 'POST',
                    isArray: true,
                    params: {
                        action: 'retrieve/users'
                    }
                },
                retrieveUserListFranchise: {
                    method: 'POST',
                    isArray: true,
                    params: {
                        action: 'retrieve/usersbyfranchise'
                    }
                },
                retrieveRoleList: {
                    method: 'POST',
                    isArray: true,
                    params: {
                        action: 'retrieve/roles'
                    }
                },
                retrieveRoleListFranchise: {
                    method: 'POST',
                    isArray: true,
                    params: {
                        action: 'retrieve/rolesbyfranchise'
                    }
                },
                retrieveDepartmentList: {
                    method: 'POST',
                    isArray: true,
                    params: {
                        action: 'retrieve/departments'
                    }
                },
                retrieveDepartmentListFranchise: {
                    method: 'POST',
                    isArray: true,
                    params: {
                        action: 'retrieve/departmentsbyfranchise'
                    }
                },
                retrieveActivityList: {
                    method: 'POST',
                    isArray: true,
                    params: {
                        action: 'retrieve/activities'
                    }
                },
                retrieveGroupList: {
                    method: 'POST',
                    isArray: true,
                    params: {
                        action: 'retrieve/groups'
                    }
                },
                retrieveDepartmentListOfOtherFranchise: {
                    method: 'POST',
                    isArray: true,
                    params: {
                        action: 'retrieve/departmentsofotherfranchise'
                    }
                },
                archiveMessages: {
                    method: 'POST',
                    isArray: false,
                    params: {
                        action: 'archiveMessages'
                    }
                },
                archiveTemplate: {
                    method: 'POST',
                    isArray: false,
                    params: {
                        action: 'archiveTemplate'
                    }
                },
                retrieveMessagesByType: {
                    method: 'POST',
                    isArray: true,
                    params: {
                        action: 'retrieve/messagesbytype'
                    }
                },
                retrieve: {
                    method: 'POST',
                    isArray: false,
                    params: {
                        action: 'retrieve'
                    }
                },
                markAsClosed: {
                    method: 'POST',
                    isArray: false,
                    params: {
                        action: 'mark/closed'
                    }
                },
                retrieveUnreadMessages: {
                    method: 'POST',
                    isArray: true,
                    params: {
                        action: 'retrieve/unreadmessages'
                    }
                },
                retrieveMessagesPerPage:{
                   method: 'POST',
                    isArray: true,
                    params: {
                        action: "retrieveMessagesPerPage"
                    }
                },
                retrieveTotalMessagesLength :{
                   method: 'POST',
                    isArray: false,
                    params: {
                        action: "retrievetotalmessageslength"
                    }
                },
                retrieveMessageAsScroll:{
                    method: 'POST',
                    isArray: true,
                    params: {
                        action: "retrieveasperscroll"
                    }
                }

            }
            );
            return Messaging;
        }]);
});


