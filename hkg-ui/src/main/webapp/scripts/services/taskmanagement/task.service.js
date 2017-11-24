define(['hkg'], function(hkg) {
    hkg.register.factory('Task', ['$resource', '$rootScope', function(resource, rootScope) {
            var Task = resource(rootScope.apipath + 'task/:action',
                    {
                    },
                    {
                        createTask: {
                            method: 'PUT',
                            params: {
                                action: 'create'
                            }
                        },
                        updateTask: {
                            method: 'POST',
                            params: {
                                action: 'update'
                            }
                        },
                        retrieveTaskCategories: {
                            method: 'GET',
                            isArray: true,
                            params: {
                                action: 'retrievetaskcategories'
                            }
                        },
                        retrieveCategories: {
                            method: 'GET',
                            isArray: true,
                            params: {
                                action: 'retrievetaskcategories'
                            }
                        },
                        createTaskCategory: {
                            method: 'POST',
                            params: {
                                action: 'createtaskcategory'
                            }
                        },
                        retrieveTaskSuggestions: {
                            method: 'GET',
                            isArray: true,
                            params: {
                                action: 'retrievetasksuggestions'
                            }
                        },
                        retrieveAllTasks: {
                            method: 'POST',
                            params: {
                                action: 'retrievealltasks'
                            }
                        },
                        retrieveUsers: {
                            method: 'POST',
                            isArray: true,
                            params: {
                                action: 'retrieveusers'
                            }
                        },
                        attendTask: {
                            method: 'POST',
                            params: {
                                action: 'attendtask'
                            }
                        },
                        completeTask: {
                            method: 'POST',
                            params: {
                                action: 'completetask'
                            }
                        },
                        retrieveTaskRecipients: {
                            method: 'POST',
                            isArray: true,
                            params: {
                                action: 'retrievetaskrecipients'
                            }
                        },
                        retrieveTaskAssignerNames: {
                            method: 'GET',
                            isArray: true,
                            params: {
                                action: 'retrievetaskassignernames'
                            }
                        },
                        retrieveTasksByAssigner: {
                            method: 'POST',
                            isArray: true,
                            params: {
                                action: 'retrievetasksbyassigner'
                            }
                        },
                        editTaskCategory: {
                            method: 'POST',
                            params: {
                                action: 'edittaskcategory'
                            }
                        },
                        retrieveTasksByCategory: {
                            method: 'POST',
                            params: {
                                action: 'retrievetasksbycategory'
                            }
                        },
                        retrieveTasksById: {
                            method: 'POST',
                            params: {
                                action: 'retrieve'
                            }
                        },
                        removeTask: {
                            method: 'POST',
                            params: {
                                action: 'removetask'
                            }
                        },
                        removeTaskOfUserFromList: {
                            method: 'POST',
                            params: {
                                action: 'removetaskfromlist'
                            }
                        },
                        updateTaskCategories: {
                            method: 'POST',
                            params: {
                                action: 'updatetaskcategories'
                            }
                        },
                        updateTaskRecipients: {
                            method: 'POST',
                            params: {
                                action: 'updatetaskrecipients'
                            }
                        },
                        cancelRepeatedTask: {
                            method: 'POST',
                            params: {
                                action: 'cancelrepeatedtask'
                            }
                        },
                        addCustomDataToCategoryDataBean: {
                            method: 'POST',
                            isArray: false,
                            params: {
                                action: 'addCustomDataToCategoryDataBean'
                            }
                        }

                    }
            );
            return Task;
        }]);
});
