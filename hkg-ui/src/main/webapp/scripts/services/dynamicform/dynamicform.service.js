/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


angular.module("dynamicformmodule", [])

        .factory('DynamicFormService', ['$rootScope', '$resource', '$q', '$filter', function (rootScope, resource, $q, $filter) {

                var customfieldManagment = resource(
                        rootScope.apipath + 'customfield/:action', //url being hit
//                        'api/customfield/:action', //url being hit
                        {
                            action: '@actionName'
                        }, // url perameters

                {
                    //methods
                    retrieveAllCustomField: {
                        method: 'POST',
                        isArray: false,
                        params: {
                            action: "retrieveallfeaturesection"
                        }
                    },
                    retrieveCustomFieldByFeatureName: {
                        method: 'POST',
                        isArray: false,
                        params: {
                            action: "retrievesectionandcustomfieldtemplate"
                        },
                        url: rootScope.centerapipath + "customfield/:action"
                    },
                    retrieveCustomFieldBySeachCriteria: {
                        method: 'POST',
                        isArray: false,
                        params: {
                            action: "retrievecustomfieldbyseachcriteria"
                        },
                        url: rootScope.centerapipath + "customfield/:action"
                    },
                    retrieveSearchField: {
                        method: 'POST',
                        isArray: false,
                        params: {
                            action: "retrieveSearchField"
                        },
                        url: rootScope.centerapipath + "customfield/:action"
                    },
                    retrieveRecipientNames: {
                        method: 'POST',
                        isArray: false,
                        params: {
                            action: "retrieveRecipientNames"
                        }
                    },
                    retrieveCustomNamesOfComponentIds: {
                        method: 'POST',
                        isArray: false,
                        params: {
                            action: "retrieveCustomNamesOfComponentIds"
                        },
                        url: rootScope.centerapipath + "customfield/:action"
                    },
                    retrieveSubEntitiesByFieldId: {
                        method: 'POST',
                        isArray: true,
                        params: {
                            action: "retrieveSubEntities"
                        }
                    }
                });
                return {
//                
                    storeAllCustomFieldData: function (companyId) {
                        var data = localStorage.getItem("customFieldVersion");
                        var customFieldVersionDetail = {"CUSTOM_FIELD_VERSION": data, "companyId": localStorage.getItem("companyId")};
//                        console.log("Custom Version" + JSON.stringify(customFieldVersionDetail));
                        customfieldManagment.retrieveAllCustomField(customFieldVersionDetail, function (response) {
//                            console.log("Response,," + JSON.stringify(response));
                            if (response.CUSTOM_FIELD_VERSION !== undefined) {
                                clearLocalStorage();
                            }
                            if (response.CUSTOM_FIELD_VERSION !== undefined) {
                                localStorage.setItem("customFieldVersion", response.CUSTOM_FIELD_VERSION);
                            }
                            localStorage.setItem("companyId", companyId);
                            if (response.customFieldData !== undefined) {
                                for (var key in response.customFieldData)
                                {

                                    var encryptedValue = encryptPass(JSON.stringify(response.customFieldData[key]));
                                    try {
                                        localStorage.setItem(key, encryptedValue);
                                    } catch (e) {
                                        //console.log("LIMIT REACHED: (" + i + ")");
                                        break;
                                    }
                                }
                            }

                        }, function () {
                            //console.log("fail to retrieve Custom Field Data");
                        });
                    },
                    retrieveSectionWiseCustomFieldInfo: function (featureName) {
                        var deferred = $q.defer();
                        var data = null;
                        if (featureName) {
                            data = encryptPass(localStorage.getItem(featureName));
                            if (data !== null) {
                                deferred.resolve(JSON.parse(data));
                                return deferred.promise;
                            } else {
                                customfieldManagment.retrieveCustomFieldByFeatureName(featureName, function (response) {
                                    if (response) {
                                        var encryptedValue = encryptPass(JSON.stringify(response));
                                        try {
                                            localStorage.setItem(featureName, encryptedValue);
                                        } catch (e) {
                                            //console.log("LIMIT REACHED: (" + i + ")");
                                        }
                                        deferred.resolve(response);
                                    }
                                });
                                return deferred.promise;
                            }
                        }
                    },
                    resetSection: function (dataMap) {
                        var dbMap = {};
                        if (dataMap) {
                            for (var i = 0; i < dataMap.length; i++) {
                                if (angular.isDefined(dataMap[i].val)) {
                                    dbMap[dataMap[i].model] = dataMap[i].val;
                                } else if (dataMap[i].type === 'password') {
                                    dbMap[dataMap[i].model] = {};
                                }
                            }
                        }
                        return dbMap;
                    },
                    convertToViewOnlyData: function (sectionData, sectionTemplateData) {
                        if (sectionTemplateData) {
                            for (var i = 0; i < sectionTemplateData.length; i++) {
                                if (sectionData[sectionTemplateData[i].model] && sectionTemplateData[i].values) {
                                    sectionData[sectionTemplateData[i].model] = sectionTemplateData[i].values[sectionData[sectionTemplateData[i].model]];
                                }
                            }
                        }
                        return sectionData;
                    },
                    getValuesOfComponentFromId: function (sectionData, sectionTemplateData) {
                        if (sectionTemplateData) {
                            for (var i = 0; i < sectionTemplateData.length; i++) {
                                angular.forEach(sectionData, function (itr) {
                                    if (itr.categoryCustom[sectionTemplateData[i].model] && sectionTemplateData[i].values && sectionTemplateData[i].type === 'multiSelect') {
                                        var displayName = '';
                                        angular.forEach(itr.categoryCustom[sectionTemplateData[i].model], function (mapIterator) {
                                            displayName += mapIterator.text + ",";
                                        });
                                        displayName = displayName.substring(0, displayName.length - 1);
                                        itr.categoryCustom[sectionTemplateData[i].model] = displayName;
                                    }
                                    if (itr.categoryCustom[sectionTemplateData[i].model] && sectionTemplateData[i].values) {
                                        itr.categoryCustom[sectionTemplateData[i].model] = sectionTemplateData[i].values[itr.categoryCustom[sectionTemplateData[i].model]];
                                    }
                                });
                            }
                        }
                        return sectionData;
                    },
                    // Method added By Shifa Salheen for convertor in table (without iterating localstorage)
                    convertorForCustomField: function (sectionData, success, forGrid) {
//                        console.log("Section Dataaaa" + JSON.stringify(sectionData))
                        if (!!sectionData)
                        {
                            if (forGrid === undefined) {
                                forGrid = false;
                            }
                            var selectIds = [];
                            var selectNames = [];
                            var multiSelectIds = [];
                            var multiSelectNames = [];
                            var userMultiSelectIds = [];
                            var userMultiSelectNames = [];
                            var textIds = [];
                            var caratRangeIds = [];
                            var caratRangeNames = [];
                            var subEntityIds = [];
                            var subEntityNames = [];
                            var selectIdMap = new Object(); // or var map = {};
                            var multiSelectIdMap = new Object();
                            var usermultiSelectIdMap = new Object();
                            var checkBoxMap = new Object();
                            var dateMap = new Object();
                            var textMap = new Object();
                            var imageMap = new Object();
                            var fileUploadMap = new Object();
                            var subEntityMap = new Object();
                            var caratRangeIdMap = new Object();
                            angular.forEach(sectionData, function (listOfData)
                            {
                                // categoryCustom contains json object with value as ids
                                var categories = listOfData.categoryCustom;
                                for (var key in categories) {
                                    var keyArr = [];
                                    if (categories.hasOwnProperty(key)) {
                                        keyArr = key.toString().split("$");
                                        var componentType = keyArr[1];
                                        var modelNameWithoutType = keyArr[0];
                                        var val = categories[key];
                                        if (componentType === 'DRP' && val !== undefined && val !== null)
                                        {
                                            // DONT APPEND HERE
                                            if (modelNameWithoutType === 'carate_range_of_lot' || modelNameWithoutType === 'carate_range_of_packet')
                                            {
                                                if (val !== null && val !== undefined) {
                                                    caratRangeIds.push(val);
                                                    // SelectNames contain key along with unique identifier for every record
                                                    caratRangeNames.push(key + "~" + listOfData.value);
                                                    caratRangeIdMap[key + "~" + listOfData.value] = val;
                                                }
                                            } else
                                            {
                                                selectIds.push(val);
                                                // SelectNames contain key along with unique identifier for every record
                                                selectNames.push(key + "~" + listOfData.value);
                                                selectIdMap[key + "~" + listOfData.value] = val;
                                            }
                                        }
                                        else
                                        if (componentType === 'MS' && val !== undefined && val !== null)
                                        {
                                            multiSelectIds.push(val);
                                            multiSelectNames.push(key + "~" + listOfData.value);
                                            multiSelectIdMap[key + "~" + listOfData.value ] = val;
                                        }
                                        else
                                        if (componentType === 'UMS' && val !== undefined && val !== null)
                                        {
                                            userMultiSelectIds.push(val.toString().trim());
                                            userMultiSelectNames.push(key + "~" + listOfData.value);
                                            usermultiSelectIdMap[key + "~" + listOfData.value ] = val.toString();
                                        }
                                        else
                                        if (componentType === 'SE' && val !== undefined && val !== null)
                                        {
                                            // To uniquely identify each record I have added dbfieldName along with it
                                            subEntityIds.push(val);
//                                            console.log("Subids........" + JSON.stringify(subEntityIds));
                                            subEntityMap[key + "~" + listOfData.value] = val;
                                            subEntityNames.push(key + "~" + listOfData.value);
                                        }
                                        else
                                        if (componentType === 'CB' && val !== undefined && val !== null)
                                        {
                                            checkBoxMap[key + "~" + listOfData.value ] = val;
                                        }
                                        else
                                        if (componentType === 'DT' && val !== undefined && val !== null)
                                        {
                                            dateMap[key + "~" + listOfData.value ] = val;
                                        }
                                        else
                                        if (componentType === 'TF' && val !== undefined && val !== null)
                                        {
                                            textIds.push(key);
                                            textMap[key + "~" + listOfData.value ] = val;
                                        }
                                        else
                                        if (componentType === 'IMG' && val !== undefined && val !== null)
                                        {
                                            imageMap[key + "~" + listOfData.value ] = val;
                                        }
                                        else
                                        if (componentType === 'UPD' && val !== undefined && val !== null)
                                        {
                                            fileUploadMap[key + "~" + listOfData.value ] = val;
                                        }


                                    }
                                }
                            });
                            var customMap = new Object(); // or var map = {};
                            customMap['Dropdown'] = selectIds;
                            customMap['MultiSelect'] = multiSelectIds;
                            customMap['UserMultiSelect'] = userMultiSelectIds;
                            customMap['SubEntity'] = subEntityIds;
// Actually text ids contain key i.e. model name because we need to check whether it has masking property or not
                            customMap['Text field'] = textIds;
                            customMap['caratRange'] = caratRangeIds;
                        }
                        if (customMap !== null && customMap !== undefined) {
                            customfieldManagment.retrieveCustomNamesOfComponentIds(customMap, function (response)
                            {
                                var finalConvertedMap = new Object();
                                var idWithNameMap = JSON.parse(angular.toJson(response));
                                if (selectNames.length > 0)
                                {
                                    for (var i = 0; i < selectNames.length; i++)
                                    {
// Fetch first the value of key which we get from server i.. without converted ids
                                        var valueIdSelect = selectIdMap[selectNames[i]];
                                        // fetch converted value
                                        if (valueIdSelect !== undefined && valueIdSelect !== null) {
                                            var convertedIdSelect = idWithNameMap[valueIdSelect.toString()];
                                            finalConvertedMap[selectNames[i].toString()] = convertedIdSelect;
                                        }
                                    }
                                }
                                if (caratRangeNames.length > 0)
                                {
                                    for (var i = 0; i < caratRangeNames.length; i++)
                                    {
// Fetch first the value of key which we get from server i.. without converted ids
                                        var valueIdCarat = caratRangeIdMap[caratRangeNames[i]];
                                        // fetch converted value
                                        var convertedIdCarat = idWithNameMap[valueIdCarat.toString()];
                                        finalConvertedMap[caratRangeNames[i].toString()] = convertedIdCarat;
                                    }
                                }
                                if (subEntityNames.length > 0)
                                {
                                    for (var i = 0; i < subEntityNames.length; i++)
                                    {
// Fetch first the value of key which we get from server i.. without converted ids
                                        var valueIdSubEntity = subEntityMap[subEntityNames[i]];
                                        // fetch converted value
                                        var convertedIdSubEntity = idWithNameMap[valueIdSubEntity.toString()];
                                        finalConvertedMap[subEntityNames[i].toString()] = convertedIdSubEntity;
                                    }
                                }

                                if (multiSelectNames.length > 0)
                                {
                                    for (var j = 0; j < multiSelectNames.length; j++)
                                    {
                                        var valueIdsMultiSelect = multiSelectIdMap[multiSelectNames[j]];
                                        var multiSelectArr = [];
                                        multiSelectArr = valueIdsMultiSelect.split(",");
                                        var multiSelectName = "!";
                                        for (var k = 0; k < multiSelectArr.length; k++)
                                        {
                                            multiSelectName += idWithNameMap[multiSelectArr[k].toString().trim()] + ",";
                                        }
                                        var finalMultiSlectName = multiSelectName.substring(1, multiSelectName.toString().length - 1);
                                        finalConvertedMap[multiSelectNames[j].toString()] = finalMultiSlectName;
                                    }
                                }
                                if (userMultiSelectNames.length > 0)
                                {
                                    for (var u = 0; u < userMultiSelectNames.length; u++)
                                    {
                                        var valueIdsUserMultiSelect = usermultiSelectIdMap[userMultiSelectNames[u]];
                                        var usermultiSelectArr = [];
                                        usermultiSelectArr = valueIdsUserMultiSelect.split(",");
                                        var userMultiSelectName = "!";
                                        for (var l = 0; l < usermultiSelectArr.length; l++)
                                        {
                                            userMultiSelectName += idWithNameMap[usermultiSelectArr[l].toString().trim()] + ",";
                                        }
                                        var finalUserMultiSlectName = userMultiSelectName.substring(1, userMultiSelectName.toString().length - 1);
                                        finalConvertedMap[userMultiSelectNames[u].toString()] = finalUserMultiSlectName;
                                    }
                                }
                                if (checkBoxMap !== null)
                                {
                                    for (var check in checkBoxMap)
                                    {
                                        if (checkBoxMap[check] === true || checkBoxMap[check] === 'true')
                                        {
                                            checkBoxMap[check] = "Yes";
                                        }
                                        if (checkBoxMap[check] === false || checkBoxMap[check] === 'false')
                                        {
                                            checkBoxMap[check] = "No";
                                        }
                                        finalConvertedMap[check.toString()] = checkBoxMap[check];
                                    }

                                }
                                if (dateMap !== null)
                                {
                                    for (var date in dateMap)
                                    {
                                        var oldDate = new Date(dateMap[date]);
                                        var theyear = oldDate.getFullYear();
                                        var themonth = oldDate.getMonth() + 1;
                                        var thetoday = oldDate.getDate();
                                        var formattedDate = thetoday + "/" + themonth + "/" + theyear;
                                        finalConvertedMap[date.toString()] = formattedDate;
                                    }
                                }
                                if (textMap !== null)
                                {
                                    for (var text in textMap)
                                    {
                                        var textElement = textMap[text];
                                        var textArr = [];
                                        textArr = text.split("~");
                                        if (textArr[0] in idWithNameMap)
                                        {
                                            if (idWithNameMap[textArr[0]] === "true")
                                            {
                                                if (rootScope.viewEncryptedData === false)
                                                {
                                                    var encrypt = '~';
                                                    for (var j = 0; j < textElement.length; j++)
                                                    {
                                                        if (/\s/.test(textElement[j])) {
                                                            // Got WhiteSpace
                                                            encrypt += " ";
                                                        }
                                                        else
                                                        {
                                                            encrypt += "*";
                                                        }
                                                    }
                                                    // Remove first character
                                                    encrypt = encrypt.toString().slice(1);
                                                    finalConvertedMap[text] = encrypt;
                                                }
                                            }
                                            else
                                            {
                                                finalConvertedMap[text] = textElement;
                                            }
                                        }

                                    }
                                }
                                if (imageMap !== null)
                                {
                                    for (var img in imageMap)
                                    {
                                        var imageString = imageMap[img];
                                        if (imageString !== undefined && imageString !== null) {
                                            imageFinalString = imageString.toString();
                                            if (forGrid) {
                                                finalConvertedMap[img] = imageFinalString;
                                            } else {
                                                console.log("in this>>>>")
                                                var startlength = imageString.lastIndexOf(")") + 1;
                                                var endLength = imageString.length;
                                                if (startlength > -1 && endLength > -1)
                                                {
                                                    var imageFinalString = imageString.substring(startlength, endLength);
                                                    finalConvertedMap[img] = imageFinalString;
                                                }
                                            }
                                        }
                                    }
                                }
                                if (fileUploadMap !== null)
                                {
                                    for (var file in fileUploadMap)
                                    {
                                        var fileString = fileUploadMap[file];
                                        var fileArray = [];
                                        var tempFileString = "";
                                        fileArray = fileString.toString().split(',');
                                        if (forGrid) {
                                            for (var j = 0; j < fileArray.length; j++)
                                            {
                                                tempFileString += fileArray[j] + " , ";
                                            }
                                        } else {
                                            for (var j = 0; j < fileArray.length; j++)
                                            {
                                                var startlength = fileArray[j].lastIndexOf("~~") + 1;
                                                var endLength = fileArray[j].length;
                                                if (startlength > -1 && endLength > -1)
                                                {
                                                    tempFileString += fileArray[j].substring(startlength, endLength) + " , ";
                                                }

                                            }
                                        }
                                        var fileStringWithoutLastComma = tempFileString.replace(/,(?=[^,]*$)/, '');
                                        finalConvertedMap[file] = fileStringWithoutLastComma;
                                    }
                                }
                                angular.forEach(sectionData, function (searchResult)
                                {
                                    var categoryCustom = searchResult.categoryCustom;
                                    for (var keyCustom in categoryCustom) {
                                        if (categoryCustom.hasOwnProperty(keyCustom)) {
                                            if (finalConvertedMap[keyCustom + "~" + searchResult.value] !== undefined) {
                                                categoryCustom[keyCustom] = finalConvertedMap[keyCustom + "~" + searchResult.value];
                                            }
                                        }
                                    }
                                });
                                success(sectionData);
                            });
                        }
                    },
                    // Method made by Shifa Salheen for search
                    convertSearchData: function (invoiceCustomData, parcelCustomData, lotCustomData, packetCustomData, searchFinal)
                    {
                        var stockDataMap = new Object();
                        if (invoiceCustomData !== null && invoiceCustomData !== undefined && invoiceCustomData.length > 0)
                            angular.forEach(invoiceCustomData, function (invCustom)
                            {
                                // invCustom.model key contains dbType
                                stockDataMap[invCustom.model] = invCustom.dbType;
                                // Component@invCustom.model key contains component type 
                                stockDataMap["Component@" + invCustom.model] = invCustom.type;
                            });
                        if (parcelCustomData !== null && parcelCustomData !== undefined && parcelCustomData.length > 0)
                            angular.forEach(parcelCustomData, function (parcelCustom)
                            {
                                stockDataMap[parcelCustom.model] = parcelCustom.dbType;
                                stockDataMap["Component@" + parcelCustom.model] = parcelCustom.type;
                            });
                        if (lotCustomData !== null && lotCustomData !== undefined && lotCustomData.length > 0)
                            angular.forEach(lotCustomData, function (lotCustom)
                            {
                                stockDataMap[lotCustom.model] = lotCustom.dbType;
                                stockDataMap["Component@" + lotCustom.model] = lotCustom.type;
                            });
                        if (packetCustomData !== null && packetCustomData !== undefined && packetCustomData.length > 0)
                            angular.forEach(invoiceCustomData, function (pcktCustom)
                            {
                                stockDataMap[pcktCustom.model] = pcktCustom.dbType;
                                stockDataMap["Component@" + pcktCustom.model] = pcktCustom.type;
                            });
                        for (var prop in searchFinal) {
                            // For custom field components like Number
                            if (!!stockDataMap[prop] && stockDataMap[prop] === "Integer") {
                                var isValid = parseInt(searchFinal[prop]);
                                if (!!(isValid)) {
                                    searchFinal[prop] = angular.copy(isValid);
                                }

                            }
                            // For custom field components like Angle,Percent
                            else
                            if (!!stockDataMap[prop] && stockDataMap[prop] === "Double")
                            {
                                var isValid = parseFloat(searchFinal[prop]);
                                if (!!(isValid)) {
                                    searchFinal[prop] = angular.copy(isValid);
                                }

                            }
                            // For custom field components like Dropdown(SingleSelect)
                            else
                            if (!!stockDataMap[prop] && stockDataMap[prop] === "Long")
                            {
                                var isValid = parseInt(searchFinal[prop]);
                                if (!!(isValid)) {
                                    searchFinal[prop] = angular.copy(isValid);
                                }
                            }

                            else
                            {
                                if (stockDataMap["Component@" + prop] === "currency")
                                {
                                    if (searchFinal[prop] === undefined)
                                    {

                                        searchFinal[prop + "*CurrencyCode"] = "";
                                    }
                                }
                                /* Other components which are of type String needs no conversion.
                                 The  custom fields of type multiselect,UserMultiSelect,Phone,Formula comes in this category*/
                            }
                        }
                        return searchFinal;
                    },
                    retrieveCustomData: function (sectionTemplateData, fieldIds) {
//console.log("fieldsis..."+JSON.stringify(fieldIds));
                        var templateData = [];
                        var mapToArray = [];

                        var result = Object.keys(fieldIds).map(function (key, value) {
                            angular.forEach(this[key], function (itr) {
                                if (key !== '$promise') {
                                    mapToArray.push({parent: key, fieldId: itr.fieldId, entityName: itr.entityName, sequenceNo: itr.sequenceNo, isEditable: itr.isEditable, isRequired: itr.isRequired});
                                }
                            });
                        }, fieldIds);
                        if (sectionTemplateData) {
                            angular.forEach(sectionTemplateData, function (template) {
                                angular.forEach(mapToArray, function (value) {
                                    if (template.fieldId === value.fieldId) {

                                        if (value.sequenceNo != null) {
                                            template.seq = value.sequenceNo;
                                        } else {
                                            template.seq = 999;
                                        }
                                        if (!value.isEditable) {
//                                            template.attributes = {'class': 'is-disabled'};
                                            template.isViewFromDesignation = true;
                                        }
//                                        console.log("Vlueee."+JSON.stringify(value))
                                        if (value.isRequired === true || value.isRequired === 'true')
                                        {
                                            template.required = true;
                                        } else
                                        {
                                            template.required = false;
                                        }
                                        templateData.push(template);
                                    }

                                });
                            });
                        }
                        templateData.sort(function (a, b) {
                            return (a.seq) - (b.seq);
                        });
                        return templateData;
                    },
                    // Method modified by Shifa Salheen on 13 April for removing server calls
                    retrieveSearchWiseCustomFieldInfo: function (featureName) {

                        var deferred = $q.defer();
                        var data = null;
                        var Result = [];
                        customfieldManagment.retrieveCustomFieldBySeachCriteria(featureName, function (responses) {
                            // responses returns feature wise list of permitted fields
                            // This will remove all "angular specific" functions etc and will return you a clean data object.
//console.log("resposnes"+JSON.stringify(responses))
                            var listOfFieldsBySearchCriteria = angular.fromJson(angular.toJson(responses));
                            var Search = [];
                            var isLocalFound;
                            for (var key in listOfFieldsBySearchCriteria)
                            {
                                // key contains feature name
                                data = encryptPass(localStorage.getItem(key.toString().toLowerCase()));
//                              // data contains feature name corresponding json in local storage.We need to parse it for fetching json.
                                if (data !== null)
                                {
                                    isLocalFound = true;
                                } else
                                {
                                    isLocalFound = false;
                                    break;
                                }
                                if (isLocalFound) {
                                    var localStorageData = JSON.parse(data);
                                    // templateFromLocalStorage contains actual template which is inside json key genralSection
                                    var templateFromLocalStorage = localStorageData['genralSection'];
//                                    console.log("temp from local"+JSON.stringify(templateFromLocalStorage))
//                                    console.log("template from local storage.." + JSON.stringify(templateFromLocalStorage))
                                    // listOfFieldsBySearchCriteria[key] contains list of permitted fields according to key i.e. if key is invoice it will give invoice related permitted fields for search
                                    angular.forEach(listOfFieldsBySearchCriteria[key], function (list)
                                    {
                                        var index = $filter('filter')(templateFromLocalStorage, function (entity) {
//                                            console.log("model.."+JSON.stringify(entity.label))
                                            return list.label.toString() === entity.model.toString().toString();
                                        })[0];
                                        var tempIndex = angular.copy(index);
                                        if (!!tempIndex) {
//                                            console.log("temPndec"+tempIndex['type'])
                                            if (tempIndex['type'] === 'AutoGenerated')
                                            {
                                                tempIndex['placeholder'] = '';
                                            }
                                            else
                                            if (tempIndex['type'] === 'checkbox')
                                            {
                                                // This code written because in search we dont need default value in checkbox
                                                tempIndex['val'] = null;
                                            }
                                            Search.push(tempIndex);
                                        }
                                    });
                                }
                            }
                            ;
                            if (isLocalFound) {
                                Result.genralSection = Search;
                            }
                            else
                            {
                                // Server Calll
                                var data = null;
                                if (featureName) {
                                    customfieldManagment.retrieveSearchField(featureName, function (response) {
                                        if (response) {
                                            deferred.resolve(response);
                                        }
                                    });
                                    return deferred.promise;
                                }
                            }
                            deferred.resolve(Result);
                        });
                        return deferred.promise;
                    },
                    retrieveSubEntities: function (featureId) {
                        var deferred = $q.defer();
                        var data = null;
                        if (featureId) {
                            customfieldManagment.retrieveSubEntitiesByFieldId(featureId, function (response) {
                                if (response) {
                                    var encryptedValue = encryptPass(JSON.stringify(response));
                                    deferred.resolve(response);
                                }
                            });
                            return deferred.promise;
                        }
                    },
                    //Raj: Merge and sort multiple templates
                    //Usage : DynamicFormService.sortTemplates(template1,template2,template3,................);
                    mergeAndSortTemplates: function () {
                        var result = [];
                        if (arguments !== undefined && arguments !== null && arguments.length > 0) {
                            for (var index = 0; index < arguments.length; index++) {
                                result = $.merge(result, arguments[index]);
                            }
                        }
                        return result.sort(function (obj1, obj2) {
                            return obj1.seq - obj2.seq;
                        });
                    }

                };
                return customfieldManagment;
            }]);
var printableChars = '~!@#$%^&*()_+QWERTYUIOP{}|ASDFGHJKL:"ZXCVBNM<>?`1234567890-=qwertyuiop[]\\asdfghjkl;\'zxcvbnm,./';
var featureNames = ['manageFranchise', 'manageLeaveWorkflow', 'manageAssets', 'manageHoliday', 'manageLocation', 'manageDepartment', 'manageFeature', 'manageLocales', 'manageDesignation', 'manageGoalSheet', 'manageNotifications', 'manageMessages'
            , 'manageCustomField', 'manageTasks', 'manageMasters', 'manageShift', 'manageActivity', 'manageLeave', 'manageEmployees', 'manageEvents'
            , 'applyLeave', 'invoice', 'parcel', 'lot', 'packet', 'plan', 'purchase'];
//
clearLocalStorage = function () {
    for (var i = 0; i < featureNames.length; i++) {
        localStorage.removeItem(featureNames[i]);
    }
};
encryptPass = function (pass) {
    if (pass !== null)
    {
        var newPass = "";
        for (i = 0; i < pass.length; i++)
        {
            var temp = pass.charAt(i);
            var index = printableChars.indexOf(temp);
            if (index !== -1)
            {
                index = printableChars.length - index;
                temp = printableChars.charAt(index);
                newPass += temp;
            } else {
                newPass += temp;
            }
        }
        return newPass;
    } else {
        return pass;
    }
};


