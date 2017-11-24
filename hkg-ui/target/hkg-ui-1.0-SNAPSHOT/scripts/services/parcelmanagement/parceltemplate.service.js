/**
 * 
 * @param {type} hkg
 * @returns {undefined}
 * Use of this service is to share data between parcelTemplateController and other controllers in which parcel template is getting used.
 */
define(['hkg'], function (hkg) {
    hkg.register.service('ParcelTemplateService', [function () {
            /**
             * 
             * @type editableFieldsArgs
             * For now this fields will remain static and will not be used but can be usable in future
             */
            var editableFields;
            var selectedParcelData;
            /**
             * 
             * @type Array
             * This will be array of ids of parcel
             */
            var selectedParcels;
            var featureName;
            var entityName;
            return{
                setEditableFields: function (editableFieldsArgs) {
                    editableFields = editableFieldsArgs;
                },
                getEditableFields: function () {
                    return editableFields;
                },
                setSelectedParcelData: function (selectedParcelDataArgs) {
                    selectedParcelData = selectedParcelDataArgs;
                },
                getSelectedParcelData: function () {
                    return selectedParcelData;
                },
                setSelectedParcels: function (selectedParcelsArgs) {
                    if (selectedParcelsArgs !== undefined && selectedParcelsArgs !== null && selectedParcelsArgs.length > 0) {
//                    var otherValues = [];
                        selectedParcels = {};
//                    otherValues.push({"parcelId": "55ed3c7184aead1ce5ef9f64", carat: 5, pieces: 5, exRate: 10});
//                    selectedParcelsArgs = otherValues;
                        for (var i = 0; i < selectedParcelsArgs.length; i++) {
                            selectedParcels[selectedParcelsArgs[i].parcel] = {carat: selectedParcelsArgs[i].sellCarats,
                                pieces: selectedParcelsArgs[i].sellPieces, exRate: selectedParcelsArgs[i].exchangeRate};
                        }
                    }
                },
                getSelectedParcels: function () {
                    return selectedParcels;
                },
                setFeatureName: function (featureNameArgs) {
                    featureName = featureNameArgs;
                },
                getFeatureName: function () {
                    return featureName;
                },
                setEntityName: function (entityNameargs) {
                    entityName = entityNameargs;
                },
                getEntityName: function () {
                    return entityName;
                }
            };
        }]);
});
    