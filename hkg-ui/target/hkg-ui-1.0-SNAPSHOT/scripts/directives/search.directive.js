define(['angular'], function() {
    angular.module('hkg.directives').directive('agSearch', ['$compile', '$filter', '$rootScope', '$location', function($compile, $filter, $rootScope, $location) {
            return {
                restrict: 'A',
                scope: {'agSearch': '@', 'searchListArray': '=', 'onSelect': '=', 'onEnter': '=', 'onReset': '='},
                link: function(scope, element, attrs) {
                    scope.isSelect = false;
                    scope.typedWord = "";
                    scope.localSearchedResultForDisplay = [];
                    scope.localSearchedResult = [];
                    scope.searchListArray = [];
                    scope.serverSearchList = []
                    scope.isServerSearch = true;
                    var checkForDate = function(fieldValue) {
                        var resultantField;
                        var isDate = new Date(fieldValue);
                        if (typeof fieldValue !== "string" && Object.prototype.toString.call(isDate) === "[object Date]") {
                            if (isNaN(isDate.getTime())) {  // d.valueOf() could also work
                                resultantField = fieldValue;
                            }
                            else {
                                resultantField = $rootScope.ConvertTimeStamptodate(fieldValue);
                            }
                        }
                        else {
                            resultantField = fieldValue;
                        }
                        return resultantField;
                    }
                    var convertTimestampToStringFormat = function(timestamp) {
                        var startDt = new Date(timestamp);
                        var start_day = startDt.getDate();
                        start_day = (start_day < 10) ? '0' + start_day : start_day;
                        var start_month = startDt.getMonth() + 1;
                        start_month = (start_month < 10) ? '0' + start_month : start_month;
                        var monthNames = ["january", "february", "march", "april", "may", "june",
                            "july", "august", "september", "october", "november", "december"];

                        return start_day + '' + monthNames[startDt.getMonth()] + '' + startDt.getFullYear();
                    }
                    var convertTimestampToStringFormatWithSuffix = function(timestamp) {
                        var startDt = new Date(timestamp);
                        var start_day = startDt.getDate();
                        start_day = (start_day < 10) ? '0' + start_day : start_day;
                        var start_month = startDt.getMonth() + 1;
                        start_month = (start_month < 10) ? '0' + start_month : start_month;
                        var monthNames = ["january", "february", "march", "april", "may", "june",
                            "july", "august", "september", "october", "november", "december"];
                        if (start_day === "01") {
                            start_day += "st";
                        } else if (start_day === "02") {
                            start_day += "nd";
                        } else if (start_day === "03") {
                            start_day += "rd";
                        } else if (start_day === "04") {
                            start_day += "th";
                        } else if (start_day === "05") {
                            start_day += "th";
                        } else if (start_day === "06") {
                            start_day += "th";
                        } else if (start_day === "07") {
                            start_day += "th";
                        } else if (start_day === "08") {
                            start_day += "th";
                        } else if (start_day === "09") {
                            start_day += "th";
                        } else if (start_day === 10) {
                            start_day += "th";
                        } else if (start_day === 11) {
                            start_day += "th";
                        } else if (start_day === 12) {
                            start_day += "th";
                        } else if (start_day === 13) {
                            start_day += "th";
                        } else if (start_day === 14) {
                            start_day += "th";
                        } else if (start_day === 15) {
                            start_day += "th";
                        } else if (start_day === 16) {
                            start_day += "th";
                        } else if (start_day === 17) {
                            start_day += "th";
                        } else if (start_day === 18) {
                            start_day += "th";
                        } else if (start_day === 19) {
                            start_day += "th";
                        } else if (start_day === 20) {
                            start_day += "th";
                        } else if (start_day === 21) {
                            start_day += "st";
                        } else if (start_day === 22) {
                            start_day += "nd";
                        } else if (start_day === 23) {
                            start_day += "rd";
                        } else if (start_day === 24) {
                            start_day += "th";
                        } else if (start_day === 25) {
                            start_day += "th";
                        } else if (start_day === 26) {
                            start_day += "th";
                        } else if (start_day === 27) {
                            start_day += "th";
                        } else if (start_day === 28) {
                            start_day += "th";
                        } else if (start_day === 29) {
                            start_day += "th";
                        } else if (start_day === 30) {
                            start_day += "th";
                        }
                        return start_day + '' + monthNames[startDt.getMonth()] + '' + startDt.getFullYear();

                    };
                    var convertTimestampToHyphen = function(timestamp) {
                        var startDt = new Date(timestamp);
                        var start_day = startDt.getDate();
                        start_day = (start_day < 10) ? '0' + start_day : start_day;
                        var start_month = startDt.getMonth() + 1;
                        start_month = (start_month < 10) ? '0' + start_month : start_month;
                        return start_day + '-' + start_month + '-' + startDt.getFullYear();
                    }
                    var convertTimestampToSlashShortYear = function(timestamp) {
                        var startDt = new Date(timestamp);
                        var start_day = startDt.getDate();
                        start_day = (start_day < 10) ? '0' + start_day : start_day;
                        var start_month = startDt.getMonth() + 1;
                        start_month = (start_month < 10) ? '0' + start_month : start_month;
                        return start_day + '/' + start_month + '/' + (startDt.getFullYear()) % 100;
                    }
                    var convertTimestampToHyphenShortYear = function(timestamp) {
                        var startDt = new Date(timestamp);
                        var start_day = startDt.getDate();
                        start_day = (start_day < 10) ? '0' + start_day : start_day;
                        var start_month = startDt.getMonth() + 1;
                        start_month = (start_month < 10) ? '0' + start_month : start_month;
                        return start_day + '-' + start_month + '-' + (startDt.getFullYear()) % 100;
                    }
                    var checkMatchedRecords = function(records, tokens, fields) {
                        var words = tokens;
                        words = words.split(" ");
                        var matchedCounter = 0;
                        for (var indx = 0; indx < words.length; indx++) {
                            for (var ind = 0; ind < fields.length; ind++) {
                                var fieldValue = records[fields[ind]];
                                var tmp = fieldValue.toString();
                                if (typeof fieldValue !== "string" && (typeof fieldValue === "number" && tmp.length > 11)) {
                                    fieldValue = checkForDate(fieldValue);
                                } else if (typeof fieldValue === "number") {
                                    fieldValue = fieldValue.toString();
                                }

                                words[indx] = words[indx].replace(/\s+/g, ' ').trim();

                                if (fieldValue.toUpperCase().indexOf(words[indx].toUpperCase()) >= 0 ||
                                        records.dateHyphenFormat.toUpperCase().indexOf(words[indx].toUpperCase()) >= 0 ||
                                        records.dateStringFormat.toUpperCase().indexOf(words[indx].toUpperCase()) >= 0 ||
                                        records.dateStringFormatWithSuffix.toUpperCase().indexOf(words[indx].toUpperCase()) >= 0 ||
                                        records.dateShortYearHyphenFormat.toUpperCase().indexOf(words[indx].toUpperCase()) >= 0 ||
                                        records.dateShortYearSlashFormat.toUpperCase().indexOf(words[indx].toUpperCase()) >= 0) {
                                    matchedCounter++;
                                    break;
                                }

                            }
                        }
                        if (matchedCounter >= words.length) {
                            return true;
                        } else {
                            return false;
                        }
                    }

//                    var removeDuplicatesRecords = function(list) {
//                        var distinctArr = list.filter(function(el) {
//                            var isDup = el.inArray;
//                            el.inArray = true;
//                            return !isDup;
//                        });
//                        distinctArr.forEach(function(el) {
//                            delete el.inArray;
//                        });
//                        console.log("unique=" + JSON.stringify(distinctArr));
//                        return distinctArr;
//                        //   console.log("unique=" + JSON.stringify(uniqueNames));
//                    }
                    if (angular.isDefined(scope.agSearch) && angular.isDefined(attrs.resultFormat)) {
                        var searchEngine = new Bloodhound({
                            datumTokenizer: Bloodhound.tokenizers.obj.whitespace('name'),
                            queryTokenizer: Bloodhound.tokenizers.whitespace,
                            limit: 50,
                            // prefetch: '../data/films/post_1960.json',
//                            local: $.map(states, function(state) {
//                                return {name: state};
//                            })

                            // ,
                            remote: {
                                
                                url: $rootScope.appendAuthToken(scope.agSearch + '?q=%QUERY'),
                                rateLimitBy: 'throttle',
                                replace: function(url, query) {
                                    searchEngine.clearRemoteCache();
                                    scope.typedWord = query;
                                    var spaceSeparetedWords = scope.typedWord.split(" ");
                                    var wordsLength = spaceSeparetedWords[1];
                                    var lastword = spaceSeparetedWords[spaceSeparetedWords.length - 1];
                                    if (angular.isDefined(wordsLength) && wordsLength.length > 0 &&
                                            spaceSeparetedWords[0].length >= 3 || spaceSeparetedWords.length >= 3) {
                                        scope.localSearchedResult = [];
                                        scope.localSearchedResultForDisplay = [];
                                        var fields = attrs.resultFormat.split(",");
                                        var formatedString = '';
                                        scope.typedWord = scope.typedWord.replace(/\s+/g, ' ').trim();
                                        angular.forEach(scope.serverSearchList, function(key, value) {
                                            var isMatched = checkMatchedRecords(key, scope.typedWord, fields);
                                            if (isMatched)//fieldValue.indexOf(spaceSeparetedWords[sercheWordIndex]) === 0) {
                                            {
                                                for (var i = 0; i < fields.length; i++) {
                                                    var fieldValueNew;
                                                    fieldValueNew = key[fields[i]];
                                                    var tmp = fieldValueNew.toString();
                                                    if (typeof fieldValueNew !== "string" && (typeof fieldValueNew === "number" && tmp.length > 11)) {
                                                        fieldValueNew = checkForDate(fieldValueNew);
                                                    }
                                                    if (i === fields.length - 1) {
                                                        formatedString += fieldValueNew;
                                                    } else {
                                                        formatedString += fieldValueNew + ", ";
                                                    }
                                                }
                                                formatedString = formatedString.replace(/\s+/g, ' ').trim();
                                                scope.localSearchedResult.push({id: key.id, name: formatedString});
                                                scope.localSearchedResultForDisplay.push(key);
                                                formatedString = '';
                                            }
                                        })
                                        return [];
                                    } else {
                                        console.log("attrs.agExtraparam :"+attrs.agExtraparam);
                                        if(attrs.agExtraparam !== undefined && attrs.agExtraparam !== null && attrs.agExtraparam !== ''){
                                            return $rootScope.appendAuthToken(scope.agSearch + '?q=' + query.replace(/[/]/g, '%2F') + '&extraparam=' + attrs.agExtraparam);
                                        }else{
                                            return $rootScope.appendAuthToken(scope.agSearch + '?q=' + query.replace(/[/]/g, '%2F'));
                                        }
                                        
                                    }
                                },
                                filter: function(list) {
                                    var spaceSeparetedWords = scope.typedWord.split(" ");
                                    var wordsLength = spaceSeparetedWords[1];
                                    if (angular.isDefined(wordsLength) && wordsLength.length > 0 &&
                                            spaceSeparetedWords[0].length >= 3 || spaceSeparetedWords.length >= 3) {
                                        scope.searchListArray.splice(0, scope.searchListArray.length);
                                        scope.isServerSearch = false;
                                        angular.forEach(scope.localSearchedResultForDisplay, function(key, value) {
                                            scope.searchListArray.push(key);
                                        });


                                        return $.map(scope.localSearchedResult, function(data) {
                                            return {id: data.id, name: data.name};
                                        });
                                    } else {
                                        // scope.searchListArray = [];
                                        scope.searchListArray.splice(0, scope.searchListArray.length);
                                        scope.serverSearchList = [];
                                        return $.map(list, function(data) {
                                            // scope.searchListArray.push(data);
                                            // scope.searchListCopy.push(data);
                                            var fields = attrs.resultFormat.split(",");
                                            var formatedString = '';
                                            for (var i = 0; i < fields.length; i++) {
                                                var isDate = new Date(data[fields[i]]);
                                                if (data[fields[i]] != null) {
                                                    if (typeof data[fields[i]] !== "string" && Object.prototype.toString.call(isDate) === "[object Date]") {
                                                        // it is a date
                                                        var tmp = data[fields[i]].toString();
                                                        if (isNaN(isDate.getTime()) || tmp.length < 11) {  // d.valueOf() could also work
                                                            if (i === fields.length - 1) {
                                                                formatedString += data[fields[i]];
                                                            } else {
                                                                formatedString += data[fields[i]] + ", ";
                                                            }
                                                        }
                                                        else {
                                                            if (i === fields.length - 1) {
                                                                formatedString += $rootScope.ConvertTimeStamptodate(data[fields[i]]);
                                                            } else {
                                                                formatedString += $rootScope.ConvertTimeStamptodate(data[fields[i]]) + ", ";
                                                            }
                                                            data.dateHyphenFormat = convertTimestampToHyphen(data[fields[i]]);
                                                            data.dateStringFormat = convertTimestampToStringFormat(data[fields[i]]);
                                                            data.dateStringFormatWithSuffix = convertTimestampToStringFormatWithSuffix(data[fields[i]]);
                                                            data.dateShortYearHyphenFormat = convertTimestampToHyphenShortYear(data[fields[i]]);
                                                            data.dateShortYearSlashFormat = convertTimestampToSlashShortYear(data[fields[i]]);
                                                        }
                                                    }
                                                    else {
                                                        data.dateHyphenFormat = "";
                                                        data.dateStringFormat = "";
                                                        data.dateStringFormatWithSuffix = "";
                                                        data.dateShortYearHyphenFormat = "";
                                                        data.dateShortYearSlashFormat = "";
                                                        if (i === fields.length - 1) {
                                                            formatedString += data[fields[i]];
                                                        } else {
                                                            formatedString += data[fields[i]] + ", ";
                                                        }
                                                    }
                                                }else{
                                                    if (i === fields.length - 1) {
                                                            formatedString += "N/A";
                                                        } else {
                                                            formatedString += "N/A" + ", ";
                                                        }
                                                }
                                            }
                                            scope.searchListArray.push(data);
                                            scope.serverSearchList.push(data);
                                            formatedString = formatedString.replace(/\s+/g, ' ').trim();
                                            return {id: data.id, name: formatedString};
                                        });
                                    }
                                }
                            }
                        }
                        );
                        var promise = searchEngine.initialize(true);
                        promise
                                .done(function() {
//                                    console.log('success!');
                                })
                                .fail(function() {
//                                    console.log('err!');
                                });
                        var divID;
                        if (angular.isDefined(attrs.id)) {
                            divID = attrs.id;
                        }
                        $('#' + divID + ' .typeahead').typeahead({
                            hint: true,
                            highlight: true,
                            minLength: 3
                        },
                        {
                            name: 'searchWord',
                            displayKey: 'name',
                            source: searchEngine.ttAdapter(),
                            templates: {
                                empty: [
                                    '<div class="empty-message">',
                                    'unable to find any records',
                                    '</div>'
                                ].join('\n')//,
                                        // suggestion:$compile('<p><strong>name</strong> – b</p>')
                            }
                        });
                        $('#' + divID + ' .typeahead').on('typeahead:selected', function(evt, data) {
                            scope.isSelect = true;
                            if (angular.isDefined(attrs.onSelect)) {
                                scope.$apply(function() {
                                    scope.onSelect(data.id);
                                });
                            }
                            $('#' + divID + '.typeahead').typeahead('val', '');
                            // $('#' + divID + '.typeahead').typeahead('destroy');
                        });
                        $('#' + divID + ' .typeahead').on('keydown', function(event) {
                            var e = jQuery.Event("keydown");
                            var wordstyped = $('#' + divID + '.typeahead').typeahead('val');
                            e.keyCode = e.which = 9; // 9 == tab
                            if (angular.isDefined(attrs.onEnter) && event.which === 13 && !scope.isSelect) {
                                $('#' + divID + ' .typeahead').typeahead('close');
                                scope.$apply(function() {
                                    if (!scope.isServerSearch) {
                                        scope.onEnter(scope.localSearchedResultForDisplay);
                                        scope.isServerSearch = true;
                                    } else {
                                        if (wordstyped.length < 3) {
                                            scope.searchListArray = [];
                                            scope.onEnter(scope.searchListArray);
                                        } else {
                                            scope.onEnter(scope.searchListArray);
                                        }
                                    }
                                })

                                //  $('#' + divID + '.typeahead').typeahead('val', '');
                                scope.isSelect = false;
                            }

                            if (angular.isDefined(attrs.onReset) && wordstyped.length === 1) {
                                $('#' + divID + ' .typeahead').typeahead('close');
                                scope.$apply(function() {
                                    scope.onReset();
                                });
                            }
                            scope.isSelect = false;
                        });

                    }
                }
            };


        }]);
});
