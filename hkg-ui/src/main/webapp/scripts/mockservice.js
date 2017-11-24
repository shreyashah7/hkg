var quoteApp = angular.module("hkgMock", []);
quoteApp.run(["$httpBackend", "$rootScope", function($httpBackend, $rootScope) {
    var lots = [{"label": "550fad8c64a8e34ee1de33e4", "value": "550fb1de64a8e34ee1de33e5", "description": "550908c364a8f276937090a4", "isActive": true, "shortcutCode": null, 
            categoryCustom: {"toinvoice_date_range": 1426703400000, "parcel_number": "parcel", "invoice_text": "i", "lotID": "L-23032015-0036", "invoiceID": "IN-18032015-0036", "frominvoice_date_range": 1426617000000, "lot_number": "lot114", "parcelID": "PA-23032015-0017"}, "dbType": null, "custom1": {"lot_number": "lot114", "lotID": "L-23032015-0036"}, "custom3": {"invoice_text": "i", "frominvoice_date_range": 1426617000000, "toinvoice_date_range": 1426703400000, "invoiceID": "IN-18032015-0036"}, "custom4": {"parcel_number": "parcel", "parcelID": "PA-23032015-0017"}, "custom5": null, "fieldNameIdMap": null, "custom2": null}];
    var lots = [{
            id: "54f40a84e4b095f067a0b892",
            invoice: "54e83ae2e4b017ca71f87d79",
            parcel: "54e83fb7e4b036d7dc8b9fbe",
            createdBy: 3,
            createdOn: new Date("2015-03-02T07:00:20.673Z"),
            lastModifiedBy: 3,
            lastModifiedOn: "2015-03-02T07:00:20.673Z",
            status: "New/Rough",
            hasPacket: true,
            fieldValue: {
                lot_name: "lottwooo",
                lotID: "L-02032015-0024"
            },
            isArchive: false,
            franchiseId: 1
        }];
    var quote = [];
    $httpBackend.whenGET(/lots/).respond(lots);

//    $httpBackend.whenGET(/\/quotes\/[0-9]/).respond(function(method, url) {
//
//        return[200, quote[(url.substring(url.lastIndexOf("/") + 1)) - 1], {}];
//    });
//    $httpBackend.whenGET(/\/quote\/search?/).respond(function(method, url) {
//        return [200, quote[0], {}];
//    });
//    $httpBackend.whenPOST('/quotes').respond(function(method, url, data) {
//        var result = {};
//        if (data !== undefined)
//        {
//            result = angular.fromJson(data);
//        }
//        result.id = quote.length + 1;
//        quote.push(result);
//
//        return [200, {type: 'success', msg: 'Quote saved successfully.', 'result': result}, {}];
//    });
//
//    $httpBackend.whenPUT(/\/quotes\/[0-9]/).respond(function(method, url, data) {
//        var ds = (url.substring(url.lastIndexOf("/") + 1)) - 1;
//        var quotedata = {};
//
//        quotedata = angular.fromJson(data);
//        quotedata.id = ds + 1;
//        var sitelength = 0;
//        for (i = 0; i < quotedata.sites.length; i++)
//        {
//            if (quotedata.sites[i].id === undefined)
//            {
//                sitelength++;
//                quotedata.sites[i].id = sitelength;
//            }
//            if (quotedata.sites[i].groups) {
//                angular.forEach(quotedata.sites[i].groups, function(group, index) {
//                    if (!group.id) {
//                        group.id = index;
//                        if (group.coreTransfers) {
//                            angular.forEach(group.coreTransfers, function(coreTransfer, i) {
//                                coreTransfer.groupId = index;
//                            });
//                        }
//                    }
//                    console.log(JSON.stringify(group));
//                });
//            }
//        }
//        quote[(url.substring(url.lastIndexOf("/") + 1)) - 1] = quotedata;
//        return [200, {type: 'success', msg: 'Quote saved successfully.', 'result': quote}, {}];
//    });
    $httpBackend.whenGET(/views\/secure/).passThrough();
    $httpBackend.whenGET(/i18n/).passThrough();
    $httpBackend.whenGET(/views/).passThrough();
    $httpBackend.whenGET(/scripts/).passThrough();
    $httpBackend.whenGET(/api/).passThrough();
    $httpBackend.whenPOST(/api/).passThrough();
    $httpBackend.whenPOST(/j_spring_security_logout/).passThrough();    
    $httpBackend.whenPOST($rootScope.masterHkgPath + /j_spring_security_logout/).passThrough();
    $httpBackend.whenPUT(/api/).passThrough();
}]);