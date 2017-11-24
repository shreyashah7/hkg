define(['hkg'], function(hkg) {
    hkg.register.factory('ReportBuilderService', ['$resource', '$rootScope', function(resource, rootScope) {
            var Report = resource(rootScope.apipath + 'report/:action',
                    {
                    },
                    {
                        retrieveTableNames: {
                            method: 'GET',
                            isArray: false,
                            params: {
                                action: '/retrievetablenames'
                            }
                        },
                        retrieveTableColumns: {
                            method: 'POST',
                            isArray: false,
                            params: {
                                action: '/retrievetablecolumns'
                            }
                        },
                        retrieveReportTable: {
                            method: 'POST',
                            isArray: false,
                            params: {
                                action: '/retrievereporttable'
                            }
                        },
                        configureReport: {
                            method: 'POST',
                            isArray: false,
                            params: {
                                action: '/configurereport'
                            }
                        },
                        saveReport: {
                            method: 'POST',
                            isArray: false,
                            params: {
                                action: '/savereport'
                            }
                        },
                        retrieveAllReports: {
                            method: 'POST',
                            isArray: false,
                            params: {
                                action: '/retrieveAll'
                            }
                        },
                        retrieveReportTitles: {
                            method: 'GET',
                            isArray: true,
                            params: {
                                action: '/retrievereporttitles'
                            }
                        },
                        retrieveReport: {
                            method: 'POST',
                            isArray: false,
                            params: {
                                action: '/retrievereport'
                            }
                        },
                        retrieveReportLink: {
                            method: 'POST',
                            isArray: false,
                            params: {
                                action: '/retrievereportlink'
                            }
                        },
                        generateExcel: {
                            method: 'GET',
                            params: {
                                action: '/generateexcel'
                            }
                        },
                        generatePdf: {
                            method: 'GET',
                            params: {
                                action: '/generatepdf'
                            }
                        },
                        generateQuery: {
                            method: 'POST',
                            isArray: false,
                            params: {
                                action: '/generatequery'
                            }

                        },
                        retrieveFieldsWithFeatureName: {
                            method: 'GET',
                            params: {
                                action: 'retrievefieldswithfeaturename'
                            }
                        },
                        retrieveFeatureNameByIds: {
                            method: 'POST',
                            isArray: false,
                            params: {
                                action: '/retrievefeature'
                            }
                        },
                        updateReport: {
                            method: 'POST',
                            isArray: false,
                            params: {
                                action: '/update'
                            }
                        },
                        retrieveFeatureSectionField: {
                            method: 'GET',
                            params: {
                                action: 'retrievefeaturesectionfieldmap'
                            }
                        },
                        retrieveTableRelationship: {
                            method: 'POST',
                            params: {
                                action: 'retrievetablerelationship'
                            }
                        },
                        retreiveColumnMetadata: {
                            method: 'POST',
                            params: {
                                action: 'retreivecolumnmetadata'
                            }
                        },
                        generateFilteredPdf: {
                            method: 'POST',
                            params: {
                                action: 'generatefilteredpdf'
                            }
                        },
                        retrievePaginatedData: {
                            method: 'POST',
                            params: {
                                action: 'retrievepaginateddata'
                            }
                        },
                        clearReportData: {
                            method: 'GET',
                            params: {
                                action: 'clearreportdata'
                            }
                        },
                        retrieveFilteredData: {
                            method: 'POST',
                            params: {
                                action: 'retrievefiltereddata'
                            }
                        },
                        retrieveReportByFeature: {
                            method: 'POST',
                            params: {
                                action: 'retrievereportbyfeature'
                            }
                        },
                        checkReportNameExists: {
                            method: 'POST',
                            params: {
                                action: 'checkreportnameexists'
                            }
                        },
                        retrieveCurrencyDetails: {
                            method: 'POST',
                            isArray: false,
                            params: {
                                action: 'retrievecurrencydetails'
                            }
                        },
                        retrieveCurrencyConfiguration: {
                            method: 'POST',
                            isArray: false,
                            params: {
                                action: 'retrievecurrencyconfiguration'
                            }
                        },
                        retrieveViewCurrencyDataRightsOfLoggedInUser : {
                            method: 'POST',
                            isArray: false,
                            params: {
                                action: 'viewcurrencydatarights'
                            }
                        },
                        retrieveEmailConfiguration: {
                            method: 'POST',
                            isArray: false,
                            params: {
                                action: '/retrieveemailconfigurationbyreportid'
                            }
                        },
                        saveEmailConfiguration: {
                            method: 'POST',
                            isArray: false,
                            params: {
                                action: '/saveemailconfiguration'
                            }
                        },
                        updateDashboardStatus: {
                            method: 'POST',
                            isArray: false,
                            params: {
                                action: '/updatedashboardstatus'
                            }
                        },
                        retrieveDashboardReports: {
                            method: 'POST',
                            isArray: false,
                            params: {
                                action: '/retrievedashboardreports'
                            }
                        },
                        retrieveDashboardStatus: {
                            method: 'POST',
                            isArray: false,
                            params: {
                                action: '/retrievedashboardstatus'
                            }
                        },
                        retrieveAnalyticsCrendentials: {
                            method: 'POST',
                            isArray: false,
                            params: {
                                action: '/retrieveanalyticscrendential'
                            }
                        },
                        retrieveLimitedColumnValues: {
                            method: 'GET',
                            isArray: false,
                            params: {
                                action: '/retrievelimitedcolumnvalues'
                            }
                        }

                    });
            return Report;
        }]);

    hkg.register.filter('formatCurrency', function() {
        return function(currencyValue, format) {
            var newValue = window.format(format, currencyValue);
            if (newValue === undefined || newValue === '') {
                newValue = 0;
            }
            return newValue;
        };
    });
});
