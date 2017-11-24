/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


define(['hkg'], function(hkg) {
    hkg.register.factory('Leave', ['$resource', '$rootScope', function(resource, rootScope) {
            var LeaveManagement = resource(
                    rootScope.apipath + 'leave/:action', //url being hit
                    {
                        // action: '@actionName'
                    }, // url perameters

                    {
                        //methods

                        retrieveLeaveById: {
                            method: 'POST',
                            isArray: false,
                            params: {
                                action: "retrieve"
                            }

                        },
                        retrieveLeave: {
                            method: 'GET',
                            isArray: false,
                            params: {
                                action: "retrieve"
                            }

                        },
                        addLeave: {
                            method: 'PUT',
                            params: {
                                action: 'create'
                            }
                        },
                        deleteLeave: {
                            method: 'POST',
                            params: {
                                action: 'delete'
                            }
                        },
                        updateLeave: {
                            method: 'POST',
                            params: {
                                action: 'update'
                            }
                        },
                        retrieveLeaveReason: {
                            method: 'POST',
                            isArray: false,
                            params: {
                                action: "/retrieveLeaveReason"
                            }

                        },
                        archiveLeave: {
                            method: 'POST',
                            params: {
                                action: 'archiveLeave'
                            }
                        },
                        retrieveAllApproval: {
                            method: 'POST',
                            isArray: true,
                            params: {
                                action: "retrieveAllApproval"
                            }

                        },
                        approveLeave: {
                            method: 'POST',
                            params: {
                                action: 'approveLeave'
                            }
                        },
                        searchLeaveForRespondById: {
                            method: 'POST',
                            params: {
                                action: 'searchLeaveForRespondById'
                            }
                        },
                        disApproveLeave: {
                            method: 'POST',
                            params: {
                                action: 'disApproveLeave'
                            }
                        },
                        retriveAllLeaveByUserId: {
                            method: 'POST',
                            isArray: true,
                            params: {
                                action: "retriveAllLeaveByUserId"
                            }

                        },
                        cancelApproveLeave: {
                            method: 'POST',
                            params: {
                                action: 'cancelApproveLeave'
                            }
                        },
                        addCustomDataToLeaveDataBean: {
                            method: 'POST',
                            isArray: false,
                            params: {
                                action: 'addCustomDataToLeaveDataBean'
                            }
                        },
                        sendCommentNotification: {
                            method: 'POST',
                            params: {
                                action: 'sendCommentNotification'
                            }
                        },
                        retrievePendingStock: {
                            method: 'POST',
                            params: {
                                action: 'retrievePendingStock'
                            }
                        },
                        retrieveLeaveByLeaveId: {
                            method: 'POST',
                            params: {
                                action: 'retrieveLeaveById'
                            }
                        },
                        retrieveApprovalByID: {
                            method: 'POST',
                            params: {
                                action: 'retrieveApprovalByID'
                            }
                        }
                    });
            return LeaveManagement;
        }]);
});