var quoteApp=angular.module("hkgMock",[]);
quoteApp.run(["$httpBackend","$rootScope",function(d,a){var c=[{label:"550fad8c64a8e34ee1de33e4",value:"550fb1de64a8e34ee1de33e5",description:"550908c364a8f276937090a4",isActive:true,shortcutCode:null,categoryCustom:{toinvoice_date_range:1426703400000,parcel_number:"parcel",invoice_text:"i",lotID:"L-23032015-0036",invoiceID:"IN-18032015-0036",frominvoice_date_range:1426617000000,lot_number:"lot114",parcelID:"PA-23032015-0017"},dbType:null,custom1:{lot_number:"lot114",lotID:"L-23032015-0036"},custom3:{invoice_text:"i",frominvoice_date_range:1426617000000,toinvoice_date_range:1426703400000,invoiceID:"IN-18032015-0036"},custom4:{parcel_number:"parcel",parcelID:"PA-23032015-0017"},custom5:null,fieldNameIdMap:null,custom2:null}];
var c=[{id:"54f40a84e4b095f067a0b892",invoice:"54e83ae2e4b017ca71f87d79",parcel:"54e83fb7e4b036d7dc8b9fbe",createdBy:3,createdOn:new Date("2015-03-02T07:00:20.673Z"),lastModifiedBy:3,lastModifiedOn:"2015-03-02T07:00:20.673Z",status:"New/Rough",hasPacket:true,fieldValue:{lot_name:"lottwooo",lotID:"L-02032015-0024"},isArchive:false,franchiseId:1}];
var b=[];
d.whenGET(/lots/).respond(c);
d.whenGET(/views\/secure/).passThrough();
d.whenGET(/i18n/).passThrough();
d.whenGET(/views/).passThrough();
d.whenGET(/scripts/).passThrough();
d.whenGET(/api/).passThrough();
d.whenPOST(/api/).passThrough();
d.whenPOST(/j_spring_security_logout/).passThrough();
d.whenPOST(a.masterHkgPath+/j_spring_security_logout/).passThrough();
d.whenPUT(/api/).passThrough()
}]);