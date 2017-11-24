/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.nosql.core.impl;

import com.argusoft.generic.database.common.MongoGenericDao;
import com.argusoft.hkg.common.constantutil.HkSystemConstantUtil;
import com.argusoft.hkg.nosql.model.GenericDocument;
import com.argusoft.hkg.nosql.model.HkInvoiceDocument;
import com.argusoft.hkg.nosql.model.HkLotDocument;
import com.argusoft.hkg.nosql.model.HkPacketDocument;
import com.argusoft.hkg.nosql.model.HkParcelDocument;
import com.argusoft.hkg.nosql.model.HkSubLotDocument;
import com.argusoft.sync.center.model.HkFieldDocument;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 *
 * @author shifa
 */
@Service
public class HkFormulaExecution {

    @Autowired
    HkCustomFieldServiceImpl customFieldService;

    @Autowired
    private MongoGenericDao mongoGenericDao;

    private static class StaticFields {

        private static final String IS_ARCHIVE = "isArchive";
        private static final String FRANCHISE = "franchiseId";

    }

    private static class InvoiceFields {

        private static final String IS_ARCHIVE = "isArchive";
        private static final String FRANCHISE = "franchiseId";
        private static final String FIELD_VALUE = "fieldValue";
        private static final String OBJECT_ID = "_id";
        private static final String HAVE_VALUE = "haveValue";
        private static final String HAVE_LOT = "haveLot";
        private static final String STATUS = "status";
        private static final String INVOICE_DATE = "invoiceDate$DT$Date";
        private static final String YEAR = "year";
        private static final String SEQUENCE_NUMBER = "sequenceNumber";
    }

    private static class ParcelFields {

        private static final String IS_ARCHIVE = "isArchive";
        private static final String FRANCHISE = "franchiseId";
        private static final String INVOICE_ID = "invoice";
        private static final String FIELD_VALUE = "fieldValue";
        private static final String OBJECT_ID = "_id";

    }

    private static class LotFields {

        private static final String FIELD_VALUE = "fieldValue";
        private static final String INVOICE_ID = "invoice";
        private static final String PARCEL_ID = "parcel";
        private static final String LOT_Id = "_id";

    }

    private static class PacketFields {

        private static final String IS_ARCHIVE = "isArchive";
        private static final String FRANCHISE = "franchiseId";
        private static final String FIELD_VALUE = "fieldValue";
        private static final String INVOICE_ID = "invoice";
        private static final String PARCEL_ID = "parcel";
        private static final String LOT_ID = "lot";
        private static final String PACKET_ID = "id";

    }

    /**
     *
     * @param totalFormulaList
     * @param franchise is the companyId
     * @param invoiceDocument is invoiceDocument
     * @param parcelDocument is parcelDocument
     * @param lotDocument is lotDocument
     * @param packetDocument is packetDocument
     * @param genericDocument
     * @param subLotDocument is subLotDocument
     * @param invoiceId is the invoice id associated
     * @param parcelId is the parcel Id associated
     * @param featureId is the featureId
     * @param featureName featureName i.e ADD InVOICE or EDIT INVOICE
     * @param lotId is the lot Id associated
     * @return the map containing key as dbfield and formula evaluated result as
     * value
     */
    public Map<String, Double> mapOfEvaluatedFormulaValueWithDbField(List<HkFieldDocument> totalFormulaList, Long franchise, HkInvoiceDocument invoiceDocument, HkParcelDocument parcelDocument, HkLotDocument lotDocument, HkPacketDocument packetDocument, GenericDocument genericDocument, ObjectId invoiceId, ObjectId parcelId, ObjectId lotId, Long featureId, String featureName) {
        //System.out.println("In second service method");
       HkSubLotDocument subLotDocument  = null;
        if (featureName.equals(HkSystemConstantUtil.FeatureForFormulaEvaluation.EDIT_SUB_LOT)) {
         subLotDocument = (HkSubLotDocument) genericDocument;
        }

        Map<String, Double> finalFormulaEvalMap = null;
        Map<String, String> simplifiedDbFieldWithFormulaMap = null;
        Map<String, String> basicDbFieldWithFormulaMap = null;
        Map<ObjectId, HkInvoiceDocument> invoiceIdAndDocumentMap = null;
        Map<ObjectId, HkParcelDocument> parcelIdAndDocumentMap = null;
        Map<ObjectId, HkLotDocument> lotIdAndDocumentMap = null;

        Map<String, String> validFormulaMapForFeature = this.retrieveValidFormulasOfTheFeature(totalFormulaList, featureName, featureId);
        Map<String, String> formulaMapForAllFeatures = this.mapOfDbFieldAndFormulaForAllFeatures(totalFormulaList);

        Map<ObjectId, List<HkParcelDocument>> invoiceIdAndParcelDocumentMap = this.retrieveInvoiceIdAndParcelDocumentMap(franchise, Boolean.FALSE);

        Map<ObjectId, List<HkLotDocument>> parcelIdAndLotDocumentMap = this.retrieveParcelIdAndLotDocumentMap(franchise, Boolean.FALSE);
        Map<ObjectId, List<HkPacketDocument>> lotIdAndPacketDocumentMap = this.retrieveLotIdAndPacketDocumentMap(franchise, Boolean.FALSE);
        List<ObjectId> objectInvIds = new ArrayList<>();
        objectInvIds.add(invoiceId);
        List<ObjectId> objectParcelIds = new ArrayList<>();
        objectParcelIds.add(parcelId);
        //System.out.println("db fiels with formula map.. this is meeeee.. " + validFormulaMapForFeature);
        if (validFormulaMapForFeature != null && !validFormulaMapForFeature.isEmpty()) {
            //System.out.println("here i am...");
            finalFormulaEvalMap = new HashMap<>();
            // simplifiedDbFieldWithFormulaMap contains complex formulas in nested simplified form
            simplifiedDbFieldWithFormulaMap = new HashMap<>();

            // simplifiedDbFieldWithFormulaMap contains simple formulas 
            basicDbFieldWithFormulaMap = new HashMap<>();
            List<String> listOfDocumentsForEvaluation = this.retrieveListOfDocumentsNeededForFormualEvaluation(validFormulaMapForFeature);
            Map<String, String> roundOffWithDbfield = customFieldService.retrieveRoundOffValueForField(franchise, featureId, HkSystemConstantUtil.CustomField.ComponentType.FORMULA);
            ScriptEngineManager manager = new ScriptEngineManager();
            ScriptEngine engine = manager.getEngineByName("js");
//            List<ObjectId> objectIds = new ArrayList<>();
//            objectIds.add(invoiceDocument.getId());

            if (!CollectionUtils.isEmpty(listOfDocumentsForEvaluation)) {
                invoiceIdAndDocumentMap = new HashMap<>();
                parcelIdAndDocumentMap = new HashMap<>();
                lotIdAndDocumentMap = new HashMap<>();
                if (listOfDocumentsForEvaluation.contains(HkSystemConstantUtil.DocumentsForFormula.INVOICE_DOCUMENT)) {
                    invoiceIdAndDocumentMap = this.retrieveInvoiceIdAndDocumentMap(franchise, Boolean.FALSE);
                    if (invoiceDocument == null) {
                        if (invoiceIdAndDocumentMap.containsKey(invoiceId)) {
                            invoiceDocument = invoiceIdAndDocumentMap.get(invoiceId);
                        }
                    }
                }
                if (listOfDocumentsForEvaluation.contains(HkSystemConstantUtil.DocumentsForFormula.PARCEL_DOCUMENT)) {
                    parcelIdAndDocumentMap = this.retrieveParcelIdAndDocumentMap(franchise, Boolean.FALSE);
                    if (parcelDocument == null) {
                        if (parcelIdAndDocumentMap.containsKey(parcelId)) {
                            parcelDocument = parcelIdAndDocumentMap.get(parcelId);
                        }
                    }
                }
                if (listOfDocumentsForEvaluation.contains(HkSystemConstantUtil.DocumentsForFormula.LOT_DOCUMENT)) {
                    lotIdAndDocumentMap = this.retrieveLotIdAndDocumentMap(franchise, Boolean.FALSE);
                    if (lotDocument == null) {
                        if (lotIdAndDocumentMap.containsKey(lotId)) {
                            lotDocument = lotIdAndDocumentMap.get(lotId);
                        }
                    }
                }

            }
            for (Map.Entry<String, String> dbFieldFrmMap : validFormulaMapForFeature.entrySet()) {
                String dbKey = dbFieldFrmMap.getKey();
                String Frm = dbFieldFrmMap.getValue();
                //System.out.println("Initiallllyyy" + dbFieldFrmMap);

                // Call this method so that aggregrate functions are resolved
                //System.out.println("1.... Frmmm" + Frm);
                Frm = this.parseFormulaToFetchAggregrateFunctionAndSimplify(Frm, franchise, invoiceIdAndParcelDocumentMap, parcelIdAndLotDocumentMap, lotIdAndPacketDocumentMap, engine, invoiceDocument, parcelDocument, lotDocument, formulaMapForAllFeatures, roundOffWithDbfield);
//                //System.out.println("1. FFrm" + Frm);
                String recursiveResForSimplifyingFormula = recursiveMthodForSimplifyingFormula(formulaMapForAllFeatures, Frm, roundOffWithDbfield);
//                //System.out.println("2.. Recursive" + recursiveResForSimplifyingFormula);
                // Nested formulas have been simplified by adding { and } along with roundOFFValue with ~~ delimiter
                // Get no of occurence of { so a sto get number of complex formulas
                int countOccurrencesOf = StringUtils.countOccurrencesOf(recursiveResForSimplifyingFormula, "{");
                if (countOccurrencesOf > 0) {
                    for (int a = 1; a <= countOccurrencesOf; a++) {
                        // Get start and end index
                        int endidx = recursiveResForSimplifyingFormula.indexOf("}");
                        int openingBracesIndex = 0;
                        for (int i = endidx; i > 0; i--) {
                            if (recursiveResForSimplifyingFormula.charAt(i) == ('{')) {
                                openingBracesIndex = i;
                                break;
                            }

                        }
                        String subStr = recursiveResForSimplifyingFormula.substring(openingBracesIndex, endidx + 1);
//                        //System.out.println("3..Substr" + subStr);
                        String[] roundArr = subStr.split("~~");
                        // get the round off parameter

                        if (roundArr.length > 1) {
                            String evalString = roundArr[0].replaceAll("\\,", "|");
                            String roundVAl = roundArr[1].replace("}", "");
//                            //System.out.println("eval string" + evalString);
//                            //System.out.println("round value...." + roundVAl);

                            // Evaluate formula by replacing fields with values
                            // If any aggregrate functions are present,evaluate them first
                            evalString = this.parseFormulaToFetchAggregrateFunctionAndSimplify(evalString, franchise, invoiceIdAndParcelDocumentMap, parcelIdAndLotDocumentMap, lotIdAndPacketDocumentMap, engine, invoiceDocument, parcelDocument, lotDocument, formulaMapForAllFeatures, roundOffWithDbfield);
//                            //System.out.println("4....eval" + evalString);
                            Double complexFrmRes = this.calculateFormula(HkSystemConstantUtil.FeatureForFormulaEvaluation.EDIT_INVOICE, evalString.replace("{", "").replace("}", ""), roundOffWithDbfield, invoiceDocument, engine, dbKey, parcelDocument, invoiceIdAndDocumentMap, invoiceId, parcelIdAndDocumentMap, parcelId, lotDocument, lotIdAndDocumentMap, lotId, packetDocument, subLotDocument);
//                            //System.out.println("5. its valll edit incoivvve" + complexFrmRes);

                            String temp = recursiveResForSimplifyingFormula;
//                            //System.out.println("5 and Su/bStr to replac..." + subStr + "-------" + complexFrmRes);
                            recursiveResForSimplifyingFormula = temp.replace(subStr, complexFrmRes.toString());
//                            //System.out.println("6.nowwwwwwwwww" + recursiveResForSimplifyingFormula);
                        }
                    }
//                    //System.out.println("7...recursie" + recursiveResForSimplifyingFormula);
                    Boolean isEvalRemaining = false;
                    try {
                        Double.parseDouble(engine.eval(recursiveResForSimplifyingFormula.replace("\\,", "")).toString());
                    } catch (ScriptException | NumberFormatException e) {
                        isEvalRemaining = true;
                    }
                    if (isEvalRemaining) { // After evaluating all {} there might be some basic formulas left...For their evaluation below is the code

                        // If any aggregrate functions are present,evaluate them first
                        recursiveResForSimplifyingFormula = this.parseFormulaToFetchAggregrateFunctionAndSimplify(recursiveResForSimplifyingFormula.replaceAll("\\,", "|"), franchise, invoiceIdAndParcelDocumentMap, parcelIdAndLotDocumentMap, lotIdAndPacketDocumentMap, engine, invoiceDocument, parcelDocument, lotDocument, formulaMapForAllFeatures, roundOffWithDbfield);
//                        //System.out.println("8....in this::::" + recursiveResForSimplifyingFormula);
                        Double complexFrmResRemaining = this.calculateFormula(HkSystemConstantUtil.FeatureForFormulaEvaluation.EDIT_INVOICE, recursiveResForSimplifyingFormula.replaceAll("\\,", "|"), roundOffWithDbfield, invoiceDocument, engine, dbKey, parcelDocument, invoiceIdAndDocumentMap, invoiceId, parcelIdAndDocumentMap, parcelId, lotDocument, lotIdAndDocumentMap, lotId, packetDocument, subLotDocument);

//                        //System.out.println("9. its valll addddd remainingh" + complexFrmResRemaining);
                        recursiveResForSimplifyingFormula = recursiveResForSimplifyingFormula.replace(recursiveResForSimplifyingFormula, complexFrmResRemaining.toString());
//                        //System.out.println("10nowwwwwwwwww rema" + recursiveResForSimplifyingFormula);

                    }
                    // simplifiedDbFieldWithFormulaMap contains values of complex formulas
                    simplifiedDbFieldWithFormulaMap.put(dbKey, recursiveResForSimplifyingFormula.replaceAll("\\,", "").replace("{", "").replace("}", ""));

//               
                } else {
                    // basicDbFieldWithFormulaMap contains basic formulaas
                    basicDbFieldWithFormulaMap.put(dbKey, Frm);
                }
                //System.out.println("11. Simpl map" + simplifiedDbFieldWithFormulaMap);
            }
            // Evaluate basic formula Map
            if (basicDbFieldWithFormulaMap != null && !basicDbFieldWithFormulaMap.isEmpty()) {
                for (Map.Entry<String, String> dbFieldWithFormula : basicDbFieldWithFormulaMap.entrySet()) {
                    String key = dbFieldWithFormula.getKey();
                    String value = dbFieldWithFormula.getValue().trim();
                    // If any aggregrate functions are present,evaluate them first
                    value = this.parseFormulaToFetchAggregrateFunctionAndSimplify(value, franchise, invoiceIdAndParcelDocumentMap, parcelIdAndLotDocumentMap, lotIdAndPacketDocumentMap, engine, invoiceDocument, parcelDocument, lotDocument, formulaMapForAllFeatures, roundOffWithDbfield);
                    //System.out.println("11... basicc///" + value);
                    Double formulaResultInv = this.calculateFormula(HkSystemConstantUtil.FeatureForFormulaEvaluation.EDIT_INVOICE, value, roundOffWithDbfield, invoiceDocument, engine, key, parcelDocument, invoiceIdAndDocumentMap, invoiceId, parcelIdAndDocumentMap, parcelId, lotDocument, lotIdAndDocumentMap, lotId, packetDocument, subLotDocument);
//                    //System.out.println("12....." + formulaResultInv);
                    finalFormulaEvalMap.put(key, formulaResultInv);

                }
            }
            // Evaluate simplified formula MAp
            if (simplifiedDbFieldWithFormulaMap != null && !simplifiedDbFieldWithFormulaMap.isEmpty()) {
                // Put complex formulas values
                Double res = 0.0;
                for (Map.Entry<String, String> entrySet : simplifiedDbFieldWithFormulaMap.entrySet()) {
                    try {
                        //changed by Dhwani for formula issue - evaluate formula 2 times - 09/10/2015
                        String removeDelimiter = entrySet.getValue().replaceAll("\\|", "");
                        res = Double.parseDouble(engine.eval(removeDelimiter).toString());
                        String roundOffVal = null;
                        if (roundOffWithDbfield != null && !roundOffWithDbfield.isEmpty() && roundOffWithDbfield.containsKey(entrySet.getKey())) {

                            roundOffVal = roundOffWithDbfield.get(entrySet.getKey());

                        } else {
                            roundOffVal = "2";
                        }
                        res = roundValues(res, Integer.parseInt(roundOffVal));
                    } catch (ScriptException ex) {

                        Logger.getLogger(HkStockServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    finalFormulaEvalMap.put(entrySet.getKey(), res);
                }
            }

        } else {
            //System.out.println("I m going to return from here.. mind it..");
        }
        return finalFormulaEvalMap;
    }

    // This method parse formula and check if any aggregrate function is there,if it is present pass it into appropriate method ,get the result and then replace formula string
    private String parseFormulaToFetchAggregrateFunctionAndSimplify(String Frm, Long franchise, Map<ObjectId, List<HkParcelDocument>> invoiceIdAndParcelDocumentMap, Map<ObjectId, List<HkLotDocument>> parcelIdAndLotDocumentMap, Map<ObjectId, List<HkPacketDocument>> lotIdAndPacketDocumentMap, ScriptEngine engine, HkInvoiceDocument invoiceDocument, HkParcelDocument parcelDocument, HkLotDocument lotDocument, Map<String, String> formulaMapForAllFeatures, Map<String, String> roundOffWithDbfield) {
        //System.out.println("parsing 1..Frm:::::" + Frm);
        List<HkParcelDocument> parcelDocumentsForInvoice = null;
        String[] seperateorArr = Frm.split("\\|");
        for (int l = 0; l < seperateorArr.length; l++) {
            //System.out.println("parsing 2/....." + seperateorArr[l]);
            if (seperateorArr[l].contains(HkSystemConstantUtil.AggregrateFunctions.SUM) || seperateorArr[l].contains(HkSystemConstantUtil.AggregrateFunctions.MIN)
                    || seperateorArr[l].contains(HkSystemConstantUtil.AggregrateFunctions.MAX)
                    || seperateorArr[l].contains(HkSystemConstantUtil.AggregrateFunctions.AVG) || seperateorArr[l].contains(HkSystemConstantUtil.AggregrateFunctions.COUNT)) {
//             e.g. SUM|(|parcel.carat|)\ --split by | so at j+2 index we will get field
                String frmVal = seperateorArr[l + 2];
//                //System.out.println("parsing 3...."+frmVal);
                String substring = seperateorArr[l] + "|" + seperateorArr[l + 1] + "|" + seperateorArr[l + 2] + "|" + seperateorArr[l + 3];
//                //System.out.println("parsing 4...."+substring);
//                String actualField = null;
//                if (isNumeric(frmVal)) {
//                } else {
//                    String[] internalField = frmVal.split("\\.");
//
//                    if (internalField.length > 0) {
//                        actualField = internalField[1];
//                    }
//                }
                if (frmVal.contains(HkSystemConstantUtil.Feature.PARCEL)) {
//                    //System.out.println("parsing 5...inside");
//                    //System.out.println("Invoice id and parcel Doc map...."+invoiceIdAndParcelDocumentMap);
//                    //System.out.println("Invoice id...."+invoiceDocument.getId());
                    if (invoiceIdAndParcelDocumentMap != null && invoiceIdAndParcelDocumentMap.containsKey(invoiceDocument.getId())) {
                        //System.out.println("parsing 6 in map...");
                        parcelDocumentsForInvoice = invoiceIdAndParcelDocumentMap.get(invoiceDocument.getId());

                        Double resultFrParcel = simplifyAggregrateForParcel(invoiceDocument, parcelDocumentsForInvoice, seperateorArr[l], formulaMapForAllFeatures, frmVal, null, roundOffWithDbfield, engine, parcelIdAndLotDocumentMap, lotIdAndPacketDocumentMap);
//                        //System.out.println("parsing 6..."+resultFrParcel);
                        Frm = Frm.replace(substring, resultFrParcel.toString());
                    }

                }
                if (frmVal.contains(HkSystemConstantUtil.Feature.LOT)) {
                    if (parcelIdAndLotDocumentMap != null && parcelIdAndLotDocumentMap.containsKey(parcelDocument.getId())) {
                        List<HkLotDocument> lotDocumentsForParcel = parcelIdAndLotDocumentMap.get(parcelDocument.getId());
                        Double resultFrLot = simplifyAggregrateForLot(invoiceDocument, parcelDocument, lotDocumentsForParcel, seperateorArr[l], formulaMapForAllFeatures, frmVal, null, roundOffWithDbfield, engine, lotIdAndPacketDocumentMap);

                        Frm = Frm.replace(substring, resultFrLot.toString());
                    }

                }
                if (frmVal.contains(HkSystemConstantUtil.Feature.PACKET)) {
                    if (lotIdAndPacketDocumentMap != null && lotIdAndPacketDocumentMap.containsKey(lotDocument.getId())) {
                        List<HkPacketDocument> packetDocuments = lotIdAndPacketDocumentMap.get(lotDocument.getId());
                        Double resultFrLot = simplifyAggregrateForPacket(invoiceDocument, parcelDocument, lotDocument, packetDocuments, seperateorArr[l], formulaMapForAllFeatures, frmVal, null, roundOffWithDbfield, engine);

                        Frm = Frm.replace(substring, resultFrLot.toString());
                    }

                }
            }
        }
        return Frm;
    }

// This method returns the list of entities so that we can decide about number of server calls
    private List<String> retrieveListOfDocumentsNeededForFormualEvaluation(Map<String, String> formulaMap) {
        List<String> documentList = null;
        int invoiceCount = 0;
        int parcelCount = 0;
        int lotCount = 0;
        int packetCount = 0;
        int planCount = 0;
        int sellCount = 0;
        int transferCount = 0;
        int subLotCount = 0;
        if (formulaMap != null && !formulaMap.isEmpty()) {
            documentList = new ArrayList<>();
            for (Map.Entry<String, String> formulaentrySet : formulaMap.entrySet()) {
                String formula = formulaentrySet.getValue();
                // If the formula contains any invoice field we incremented invoicecount and if invoicecount>0 we added it to the list which means we need invoice document from mongo
                if (formula.contains(HkSystemConstantUtil.Feature.INVOICE)) {
                    invoiceCount++;
                }
                if (formula.contains(HkSystemConstantUtil.Feature.PARCEL)) {
                    parcelCount++;
                }
                if (formula.contains(HkSystemConstantUtil.Feature.LOT)) {
                    lotCount++;
                }
                if (formula.contains(HkSystemConstantUtil.Feature.PACKET)) {
                    packetCount++;
                }
                if (formula.contains(HkSystemConstantUtil.Feature.PLAN)) {
                    planCount++;
                }
                if (formula.contains(HkSystemConstantUtil.Feature.SELL)) {
                    sellCount++;
                }
                if (formula.contains(HkSystemConstantUtil.Feature.TRANSFER)) {
                    transferCount++;
                }
                if (formula.contains(HkSystemConstantUtil.Feature.SUB_LOT)) {
                    subLotCount++;
                }
            }
            if (invoiceCount > 0) {
                documentList.add(HkSystemConstantUtil.DocumentsForFormula.INVOICE_DOCUMENT);
            }
            if (parcelCount > 0) {
                documentList.add(HkSystemConstantUtil.DocumentsForFormula.PARCEL_DOCUMENT);
            }
            if (lotCount > 0) {
                documentList.add(HkSystemConstantUtil.DocumentsForFormula.LOT_DOCUMENT);
            }
            if (packetCount > 0) {
                documentList.add(HkSystemConstantUtil.DocumentsForFormula.PACKET_DOCUMENT);
            }
            if (planCount > 0) {
                documentList.add(HkSystemConstantUtil.DocumentsForFormula.PLAN_DOCUMENT);
            }
            if (sellCount > 0) {
                documentList.add(HkSystemConstantUtil.DocumentsForFormula.SELL_DOCUMENT);
            }
            if (transferCount > 0) {
                documentList.add(HkSystemConstantUtil.DocumentsForFormula.TRANSFER_DOCUMENT);
            }
            if (subLotCount > 0) {
                documentList.add(HkSystemConstantUtil.DocumentsForFormula.SUBLOT_DOCUMENT);
            }

        }

        return documentList;
    }

    private String recursiveMthodForSimplifyingFormula(Map<String, String> dbFieldWithFormulaMap, String Frm, Map<String, String> roundOffWithDbfield) {
//        //System.out.println("map in recusrion"+dbFieldWithFormulaMap);
//        //System.out.println("original key and value///" + Frm);
        String[] dbformulaValueArray = Frm.split("\\|");
        List<String> changedFrmArry = new ArrayList<>();

        for (String dbformulaValueArray1 : dbformulaValueArray) {
            String changedArr = dbformulaValueArray1;
            if (isNumeric(dbformulaValueArray1)) {
                changedFrmArry.add(dbformulaValueArray1);
            } else {
                String[] arrr = dbformulaValueArray1.split("\\.");
//                //System.out.println("arrrrr" + arrr.toString() + "----len" + arrr.length);
                if (arrr.length > 1) {
//                    //System.out.println("in ind" + arrr[1]);
                    changedArr = arrr[1];
                }

                if (dbFieldWithFormulaMap.containsKey(changedArr)) {

                    String s = dbFieldWithFormulaMap.get(changedArr);
//                    //System.out.println("2......." + s);
                    String roundValuee = null;
                    if (roundOffWithDbfield != null && !roundOffWithDbfield.isEmpty() && roundOffWithDbfield.containsKey(changedArr)) {
                        roundValuee = roundOffWithDbfield.get(changedArr.trim());
                    } else {
                        roundValuee = "2";
                    }
                    changedFrmArry.add("{(|" + s + "|)~~" + roundValuee + "}");

                } else {
                    changedFrmArry.add(dbformulaValueArray1);
                }
            }
//            //System.out.println("1:::::::::::changed frm array" + changedFrmArry);
        }
        String finalString = changedFrmArry.toString().replace("[", "").replace("]", "");
//        //System.out.println("final string::::::"+finalString);
        String change = finalString;
        String againCheck[] = change.split("\\,");
        int resolveFormulaLeft = 0;
        for (String againCheck1 : againCheck) {
            String[] splitByPipe = againCheck1.split("\\|");
            for (String splitByPipe1 : splitByPipe) {
//              //System.out.println("again check 1"+againCheck1);
                String secChange = null;
                if (isNumeric(splitByPipe1)) {
                } else {
                    String[] arrrC = splitByPipe1.split("\\.");
//                //System.out.println("arrrrr" + arrrC + "----len" + arrrC.length);
                    if (arrrC.length > 1) {
//                    //System.out.println("in ind" + arrrC[1]);
                        secChange = arrrC[1];
                    }
//                //System.out.println("sec change...."+secChange);
                    if (dbFieldWithFormulaMap.containsKey(secChange)) {

                        resolveFormulaLeft++;
//                     //System.out.println("resolve d"+resolveFormulaLeft);

                    }

                }
            }

        }
//        //System.out.println("total left::::" + resolveFormulaLeft);
        if (resolveFormulaLeft > 0) {
            finalString = finalString.replace("[", "").replace("]", "").replaceAll("\\,", "|");
//            //System.out.println("calll again:::::::" + finalString.replace("[", "").replace("]", "").replaceAll("\\,", "|"));
            return recursiveMthodForSimplifyingFormula(dbFieldWithFormulaMap, finalString.replace("[", "").replace("]", "").replaceAll("\\,", "|"), roundOffWithDbfield);
        } else {
            return finalString;
        }

    }

    private static boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");  //match a number with optional '-' and decimal.
    }

    // Method for evaluating formula for invoice
    private Double calculateFormula(String featureName, String formula, Map<String, String> roundOffWithDbfield, HkInvoiceDocument invoiceDocument, ScriptEngine engine, String key, HkParcelDocument parcelDocument, Map<ObjectId, HkInvoiceDocument> invoiceIdAndDocumentMap, ObjectId invoiceId, Map<ObjectId, HkParcelDocument> parcelIdAndDocumentMap, ObjectId parcelId, HkLotDocument lotDocument, Map<ObjectId, HkLotDocument> lotIdAndDocumentMap, ObjectId lotId, HkPacketDocument packetDocument, HkSubLotDocument subLotDocument) {
        Double resultInv = 0.0;
        List<String> newFormulaValueArray = new ArrayList<>();
        List<String> InvoiceDbField = new ArrayList<>();
        List<String> ParcelDbField = new ArrayList<>();
        List<String> LotDbField = new ArrayList<>();
        List<String> PacketDbField = new ArrayList<>();
        List<String> SubLotDbField = new ArrayList<>();
        Map<String, Double> formulaEvaluatedMap = new HashMap<>();
        String[] formulaValueArray = formula.split("\\|");
        switch (featureName) {
            case HkSystemConstantUtil.FeatureForFormulaEvaluation.EDIT_INVOICE:
                break;
            case HkSystemConstantUtil.FeatureForFormulaEvaluation.ADD_PARCEL:
            case HkSystemConstantUtil.FeatureForFormulaEvaluation.EDIT_PARCEL:
                if (invoiceIdAndDocumentMap != null && invoiceIdAndDocumentMap.containsKey(invoiceId)) {
                    invoiceDocument = invoiceIdAndDocumentMap.get(invoiceId);
                }
                break;
            case HkSystemConstantUtil.FeatureForFormulaEvaluation.ADD_LOT:
            case HkSystemConstantUtil.FeatureForFormulaEvaluation.EDIT_LOT:
                if (invoiceIdAndDocumentMap != null && invoiceIdAndDocumentMap.containsKey(invoiceId)) {
                    invoiceDocument = invoiceIdAndDocumentMap.get(invoiceId);
                }
                if (parcelIdAndDocumentMap != null && parcelIdAndDocumentMap.containsKey(parcelId)) {
                    parcelDocument = parcelIdAndDocumentMap.get(parcelId);
                }
                break;
            case HkSystemConstantUtil.FeatureForFormulaEvaluation.ADD_PACKET:
            case HkSystemConstantUtil.FeatureForFormulaEvaluation.EDIT_PACKET:
                if (invoiceIdAndDocumentMap != null && invoiceIdAndDocumentMap.containsKey(invoiceId)) {
                    invoiceDocument = invoiceIdAndDocumentMap.get(invoiceId);
                }
                if (parcelIdAndDocumentMap != null && parcelIdAndDocumentMap.containsKey(parcelId)) {
                    parcelDocument = parcelIdAndDocumentMap.get(parcelId);
                }
                if (lotIdAndDocumentMap != null && lotIdAndDocumentMap.containsKey(lotId)) {
                    lotDocument = lotIdAndDocumentMap.get(lotId);
                }
                break;
        }
        for (int j = 0; j < formulaValueArray.length; j++) {
            //changed by Dhwani for formula issue - evaluate formula 2 times - 09/10/2015

            formulaValueArray[j] = formulaValueArray[j].trim();

            // Write methods according to feature
            if (formulaValueArray[j].contains(HkSystemConstantUtil.Feature.INVOICE)) {
                InvoiceDbField.add(formulaValueArray[j].replace(HkSystemConstantUtil.Feature.INVOICE + ".", ""));
            }
            if (formulaValueArray[j].contains(HkSystemConstantUtil.Feature.PARCEL)) {
                ParcelDbField.add(formulaValueArray[j].replace(HkSystemConstantUtil.Feature.PARCEL + ".", ""));
            }
            if (formulaValueArray[j].contains(HkSystemConstantUtil.Feature.LOT)) {
                LotDbField.add(formulaValueArray[j].replace(HkSystemConstantUtil.Feature.LOT + ".", ""));
            }
            if (formulaValueArray[j].contains(HkSystemConstantUtil.Feature.PACKET)) {
                PacketDbField.add(formulaValueArray[j].replace(HkSystemConstantUtil.Feature.PACKET + ".", ""));
            }
            if (formulaValueArray[j].contains(HkSystemConstantUtil.Feature.SUB_LOT)) {
                SubLotDbField.add(formulaValueArray[j].replace(HkSystemConstantUtil.Feature.SUB_LOT + ".", ""));
            }

        }
        if (!CollectionUtils.isEmpty(InvoiceDbField)) {
//                         For Invoice fields we will fetch from invoiceDocument which is coming from save itself

            Map invMap = invoiceDocument.getFieldValue().toMap();
            //System.out.println("Invoice Map::" + invMap);
            for (String Invoice : InvoiceDbField) {
                //System.out.println("Invoice:::" + Invoice);
                if (invMap.containsKey(Invoice.trim())) {
                    //System.out.println("it contains");
                    Double InvVal = Double.parseDouble(invMap.get(Invoice.trim()).toString());
                    //System.out.println("Its val:::" + InvVal);
                    formulaEvaluatedMap.put(HkSystemConstantUtil.Feature.INVOICE + "." + Invoice.trim(), InvVal);
                }
            }
        }
        if (!CollectionUtils.isEmpty(ParcelDbField)) {
            // For Parcel fields we will fetch from parcelDocument which is coming from save itself
            if (parcelDocument != null) {
                Map parcelMap = parcelDocument.getFieldValue().toMap();
                //System.out.println("parcel map..." + parcelMap);
                for (String Parcel : ParcelDbField) {
                    //System.out.println("parcccc" + Parcel);
                    if (parcelMap.containsKey(Parcel.trim())) {
                        Double parcelVal = Double.parseDouble(parcelMap.get(Parcel.trim()).toString());
                        //System.out.println("parcel val..." + parcelVal);
                        formulaEvaluatedMap.put(HkSystemConstantUtil.Feature.PARCEL + "." + Parcel.trim(), parcelVal);
                    }
                }
            }
        }
        if (!CollectionUtils.isEmpty(LotDbField)) {
            // For Lot fields we will fetch from lotDocument which is coming from save itself
            if (lotDocument != null) {
                Map lotMap = lotDocument.getFieldValue().toMap();
                for (String Lot : LotDbField) {
                    if (lotMap.containsKey(Lot.trim())) {
                        Double lotVal = Double.parseDouble(lotMap.get(Lot.trim()).toString());
                        formulaEvaluatedMap.put(HkSystemConstantUtil.Feature.LOT + "." + Lot.trim(), lotVal);
                    }
                }
            }
        }
        if (!CollectionUtils.isEmpty(PacketDbField)) {
            if (packetDocument != null) {
                Map packetMap = packetDocument.getFieldValue().toMap();
                for (String packet : PacketDbField) {
                    if (packetMap.containsKey(packet.trim())) {
                        Double packetVal = Double.parseDouble(packetMap.get(packet.trim()).toString());
                        formulaEvaluatedMap.put(HkSystemConstantUtil.Feature.PACKET + "." + packet.trim(), packetVal);
                    }
                }
            }
        }
        if (!CollectionUtils.isEmpty(SubLotDbField)) {
//                         For Invoice fields we will fetch from invoiceDocument which is coming from save itself
            if (subLotDocument != null) {
                Map subLotMap = subLotDocument.getFieldValue().toMap();
                for (String SubLot : SubLotDbField) {
//                        
                    if (subLotMap.containsKey(SubLot.trim())) {
                        Double subLotVal = Double.parseDouble(subLotMap.get(SubLot.trim()).toString());
                        formulaEvaluatedMap.put(HkSystemConstantUtil.Feature.SUB_LOT + "." + SubLot.trim(), subLotVal);
                    }
                }
            }
        }

        if (!formulaEvaluatedMap.isEmpty()) {

            for (String form : formulaValueArray) {
                if (!(form.equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.SUM)) && !(form.equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MIN))
                        && !(form.equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MAX)) && !(form.equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.AVG))
                        //                                    && !(form.equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.COUNT)) && !(form.equalsIgnoreCase("(")) && !(form.equalsIgnoreCase(")"))) {
                        && !(form.equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.COUNT))) {

                    newFormulaValueArray.add(form);
                }
            }

            if (newFormulaValueArray.size() > 0) {

                for (int i = 0; i < newFormulaValueArray.size(); i++) {
                    if (formulaEvaluatedMap.containsKey(newFormulaValueArray.get(i))) {
                        if (formulaEvaluatedMap.containsKey(newFormulaValueArray.get(i))) {
                            Double Value = formulaEvaluatedMap.get(newFormulaValueArray.get(i));
                            if (Value != null) {
                                newFormulaValueArray.set(i, Value.toString());
                            }
                        } else {
                            //System.out.println("its in lese");
                        }

                    }

                }
            }

        }
        //changed by Dhwani for formula issue - evaluate formula 2 times - 09/10/2015
        String finalFormulaString = null;
        if (newFormulaValueArray.size() > 0) {
            String[] newFormula = new String[newFormulaValueArray.size()];
            newFormula = newFormulaValueArray.toArray(newFormula);

            String finalFormula = Arrays.toString(newFormula).replaceAll("|", "").replaceAll(",", "");
            finalFormulaString = finalFormula.replace("[", "").replace("]", "");
        } else {
            finalFormulaString = formula.replace("|", "");
            //System.out.println("before engineeee" + finalFormulaString);

        }
        try {
            //changed by Dhwani for formula issue - evaluate formula 2 times - 09/10/2015
            resultInv = Double.parseDouble(engine.eval(finalFormulaString).toString());
            String roundOffVal;
            if (roundOffWithDbfield != null && !roundOffWithDbfield.isEmpty() && roundOffWithDbfield.containsKey(key)) {

                roundOffVal = roundOffWithDbfield.get(key);

            } else {
                roundOffVal = "2";
            }
            resultInv = roundValues(resultInv, Integer.parseInt(roundOffVal));
        } catch (ScriptException ex) {

            Logger.getLogger(HkStockServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

        return resultInv;
    }

    private Map<ObjectId, HkInvoiceDocument> retrieveInvoiceIdAndDocumentMap(Long franchise, Boolean isArchive) {
        List<Criteria> criterias = new ArrayList<>();
        Map<ObjectId, HkInvoiceDocument> idInvoiceDocument = null;
        List<HkInvoiceDocument> documents = null;
        criterias.add(Criteria.where(StaticFields.FRANCHISE).in(this.getCompnies(franchise)));
        if (isArchive != null) {
            criterias.add(Criteria.where(StaticFields.IS_ARCHIVE).is(isArchive));

        }
        documents = mongoGenericDao.findByCriteria(criterias, HkInvoiceDocument.class
        );
        if (!CollectionUtils.isEmpty(documents)) {
            idInvoiceDocument = new HashMap<>();
            for (HkInvoiceDocument document : documents) {
                idInvoiceDocument.put(document.getId(), document);
            }
        }
        return idInvoiceDocument;
    }

    @SuppressWarnings("unchecked")
    private Map<ObjectId, HkParcelDocument> retrieveParcelIdAndDocumentMap(Long franchise, Boolean isArchive) {
        List<Criteria> criterias = new ArrayList<>();
        Map<ObjectId, HkParcelDocument> idParcelDocument = null;
        List<HkParcelDocument> parceldocuments = null;
        criterias.add(Criteria.where(StaticFields.FRANCHISE).in(this.getCompnies(franchise)));
        if (isArchive != null) {
            criterias.add(Criteria.where(StaticFields.IS_ARCHIVE).is(isArchive));

        }
        parceldocuments = mongoGenericDao.findByCriteria(criterias, HkParcelDocument.class
        );
        if (!CollectionUtils.isEmpty(parceldocuments)) {
            idParcelDocument = new HashMap<>();
            for (HkParcelDocument document : parceldocuments) {
                idParcelDocument.put(document.getId(), document);
            }
        }
        return idParcelDocument;
    }

    private Map<ObjectId, HkLotDocument> retrieveLotIdAndDocumentMap(Long franchise, Boolean isArchive
    ) {
        List<Criteria> criterias = new ArrayList<>();
        Map<ObjectId, HkLotDocument> idLotDocument = null;
        List<HkLotDocument> documents = null;
        criterias.add(Criteria.where(StaticFields.FRANCHISE).is(franchise));
        if (isArchive != null) {
            criterias.add(Criteria.where(StaticFields.IS_ARCHIVE).is(isArchive));

        }
        documents = mongoGenericDao.findByCriteria(criterias, HkLotDocument.class
        );
        if (!CollectionUtils.isEmpty(documents)) {
            idLotDocument = new HashMap<>();
            for (HkLotDocument document : documents) {
                idLotDocument.put(document.getId(), document);
            }
        }
        return idLotDocument;
    }

    private List<Long> getCompnies(Long companyId) {
        List<Long> companyIds = new ArrayList<>();
        companyIds.add(0l);
        if (companyId != null && !companyId.equals(0l)) {
            companyIds.add(companyId);
        }
        return companyIds;
    }
//e.g. SUM(Parcel.amount) and pass round off value

    private Double simplifyAggregrateForParcel(HkInvoiceDocument invoiceDocument, List<HkParcelDocument> parcelDocuments, String OperationType, Map<String, String> formulaMapForAllFeatures, String formula, String roundOffValue, Map<String, String> roundOffWithDbfield, ScriptEngine engine, Map<ObjectId, List<HkLotDocument>> parcelIdAndLotDocumentMap, Map<ObjectId, List<HkPacketDocument>> lotIdAndPacketDocumentMap) {
        //System.out.println("simplify 1..." + formula);
        Double result = null;
        String simplifiedFormula = recursiveMthodForSimplifyingFormula(formulaMapForAllFeatures, formula, roundOffWithDbfield);
        //System.out.println("simplify 2..." + formulaMapForAllFeatures);
        //System.out.println("simplify 3..." + simplifiedFormula);
        int countOccurrencesOf = StringUtils.countOccurrencesOf(simplifiedFormula, "{");
        List<Double> eachParcelSimplifiedValue = new ArrayList<>();

        if (countOccurrencesOf > 0) {

            for (HkParcelDocument parcelDocument : parcelDocuments) {

                String parcelSimplifieldFormula = simplifiedFormula;
                for (int a = 1; a <= countOccurrencesOf; a++) {
                    // Get start and end index
                    int endidx = parcelSimplifieldFormula.indexOf("}");
                    int openingBracesIndex = 0;
                    for (int i = endidx; i > 0; i--) {
                        if (parcelSimplifieldFormula.charAt(i) == ('{')) {
                            openingBracesIndex = i;
                            break;
                        }

                    }
                    String subStr = parcelSimplifieldFormula.substring(openingBracesIndex, endidx + 1);
                    //System.out.println("simplifu 3,,Substr" + subStr);
                    String[] roundArr = subStr.split("~~");
                    // get the round off parameter

                    if (roundArr.length > 1) {
                        String evalString = roundArr[0].replaceAll("\\,", "|");
                        String roundVAl = roundArr[1].replace("}", "");
                        //System.out.println("simplifyy  eval string" + evalString);
                        //System.out.println("simplify...round value...." + roundVAl);

                        String[] seperateorArr = evalString.split("\\|");
                        for (int l = 0; l < seperateorArr.length; l++) {
                            if (seperateorArr[l].contains(HkSystemConstantUtil.AggregrateFunctions.SUM) || seperateorArr[l].contains(HkSystemConstantUtil.AggregrateFunctions.MIN)
                                    || seperateorArr[l].contains(HkSystemConstantUtil.AggregrateFunctions.MAX)
                                    || seperateorArr[l].contains(HkSystemConstantUtil.AggregrateFunctions.AVG) || seperateorArr[l].contains(HkSystemConstantUtil.AggregrateFunctions.COUNT)) {
                                String frm = seperateorArr[l + 2];
                                String substring = seperateorArr[l] + "|" + seperateorArr[l + 1] + "|" + seperateorArr[l + 2] + "|" + seperateorArr[l + 3];
                                if (frm.contains(HkSystemConstantUtil.Feature.PARCEL)) {
                                    Double resultFrParcel = simplifyAggregrateForParcel(invoiceDocument, parcelDocuments, seperateorArr[l], formulaMapForAllFeatures, frm, roundOffValue, roundOffWithDbfield, engine, parcelIdAndLotDocumentMap, lotIdAndPacketDocumentMap);

                                    evalString = evalString.replace(substring, resultFrParcel.toString());
                                }
                                if (frm.contains(HkSystemConstantUtil.Feature.LOT)) {
                                    if (parcelIdAndLotDocumentMap != null && parcelIdAndLotDocumentMap.containsKey(parcelDocument.getId())) {
                                        List<HkLotDocument> lotDocumentsForParcel = parcelIdAndLotDocumentMap.get(parcelDocument.getId());
                                        Double resultFrLot = simplifyAggregrateForLot(invoiceDocument, parcelDocument, lotDocumentsForParcel, seperateorArr[l], formulaMapForAllFeatures, frm, roundOffValue, roundOffWithDbfield, engine, lotIdAndPacketDocumentMap);

                                        evalString = evalString.replace(substring, resultFrLot.toString());
                                    }

                                }
                            }
                        }
                        String execute = evalString.replace("{", "").replace("}", "");

                        Double resultEval = this.evaluateInternalFormulasForAggregrate(invoiceDocument, parcelDocument, null, null, execute, engine, roundVAl);
                        parcelSimplifieldFormula = parcelSimplifieldFormula.replace(subStr, resultEval.toString());
//                        }

//                    }
                        // Evaluate formula by replacing fields with values
                    }
                }

                Boolean isEvalRemaining = false;
                try {
                    Double.parseDouble(engine.eval(parcelSimplifieldFormula.replace("\\,", "")).toString());
                } catch (ScriptException | NumberFormatException e) {
                    isEvalRemaining = true;
                }
                if (isEvalRemaining) { // After evaluating all {} there might be some basic formulas left...For their evaluation below is the code
                    //System.out.println("in this::::" + parcelSimplifieldFormula.replaceAll("\\,", "|"));
                    Double complexFrmResRemaining = this.evaluateInternalFormulasForAggregrate(invoiceDocument, parcelDocument, null, null, parcelSimplifieldFormula, engine, "2");

//                    //System.out.println("its valll addddd remainingh" + complexFrmResRemaining);
//                    parcelSimplifieldFormula = parcelSimplifieldFormula.replace(parcelSimplifieldFormula, complexFrmResRemaining.toString());
//                    //System.out.println("nowwwwwwwwww rema" + parcelSimplifieldFormula);
                    parcelSimplifieldFormula = complexFrmResRemaining.toString();

                }
                eachParcelSimplifiedValue.add(Double.parseDouble(parcelSimplifieldFormula));

            }
        } else {
            //System.out.println("simplify else...");
            for (HkParcelDocument parcelDocument : parcelDocuments) {

                Map fieldValueMap = parcelDocument.getFieldValue().toMap();
                //System.out.println("fieldvalue map..." + fieldValueMap + "----");
                //System.out.println("simpl " + simplifiedFormula);

                String formulaToEval = null;
                if (isNumeric(simplifiedFormula)) {
                } else {
                    String[] dbFieldArray = simplifiedFormula.split("\\.");
                    if (dbFieldArray.length > 0) {
                        formulaToEval = dbFieldArray[1];
                    }

                }

                //System.out.println("formula to eval" + formulaToEval);
                if (fieldValueMap.containsKey(formulaToEval)) {

                    String valueOfParcelField = parcelDocument.getFieldValue().get(formulaToEval).toString();
                    //System.out.println("value/..." + valueOfParcelField);
                    eachParcelSimplifiedValue.add(Double.parseDouble(valueOfParcelField));
                }

//                String[] seperateorArr = simplifiedFormula.split("\\|");
//                for (int l = 0; l < seperateorArr.length; l++) {
//                    if (seperateorArr[l].contains(HkSystemConstantUtil.AggregrateFunctions.SUM) || seperateorArr[l].contains(HkSystemConstantUtil.AggregrateFunctions.MIN)
//                            || seperateorArr[l].contains(HkSystemConstantUtil.AggregrateFunctions.MAX)
//                            || seperateorArr[l].contains(HkSystemConstantUtil.AggregrateFunctions.AVG) || seperateorArr[l].contains(HkSystemConstantUtil.AggregrateFunctions.COUNT)) {
//                        String frm = seperateorArr[l + 2];
//                        String substring = seperateorArr[l]+ "|"+ seperateorArr[l + 1] +"|"+ seperateorArr[l + 2] +"|"+ seperateorArr[l + 3];
//                        if (frm.contains(HkSystemConstantUtil.Feature.PARCEL)) {
//                            Double resultFrParcel = simplifyAggregrateForParcel(invoiceDocument, parcelDocuments, seperateorArr[l], dbFieldWithFormulaMap, frm, roundOffValue, roundOffWithDbfield, engine, parcelIdAndLotDocumentMap, lotIdAndPacketDocumentMap);
//
//                            simplifiedFormula = simplifiedFormula.replace(substring, resultFrParcel.toString());
//                        }
//                        if (frm.contains(HkSystemConstantUtil.Feature.LOT)) {
//                            if (parcelIdAndLotDocumentMap != null && parcelIdAndLotDocumentMap.containsKey(parcelDocument.getId())) {
//                                List<HkLotDocument> lotDocumentsForParcel = parcelIdAndLotDocumentMap.get(parcelDocument.getId());
//                                Double resultFrLot = simplifyAggregrateForLot(invoiceDocument, parcelDocument, lotDocumentsForParcel, seperateorArr[l], dbFieldWithFormulaMap, frm, roundOffValue, roundOffWithDbfield, engine, lotIdAndPacketDocumentMap);
//
//                                simplifiedFormula = simplifiedFormula.replace(substring, resultFrLot.toString());
//                            }
//
//                        }
//                    }
//                }
//                String execute = simplifiedFormula.replace("{", "").replace("}", "");
//
//                Double resultEval = this.evaluateInternalFormulasForAggregrate(invoiceDocument, parcelDocument, null, null, execute, engine, null);
//                eachParcelSimplifiedValue.add(resultEval);
            }

        }
        Double finalResult = null;
        if (!CollectionUtils.isEmpty(eachParcelSimplifiedValue)) {

            finalResult = this.calculateAggregrateFunction(eachParcelSimplifiedValue, OperationType);

        }
        return finalResult;

    }

    //e.g. SUM(Lot.amount) and pass round off value
    private Double simplifyAggregrateForLot(HkInvoiceDocument invoiceDocument, HkParcelDocument parcelDocument, List<HkLotDocument> lotDocuments, String OperationType, Map<String, String> formulaMapForAllFeatures, String formula, String roundOffValue, Map<String, String> roundOffWithDbfield, ScriptEngine engine, Map<ObjectId, List<HkPacketDocument>> lotIdAndPacketDocumentMap) {
        Double result = null;
        String simplifiedFormula = recursiveMthodForSimplifyingFormula(formulaMapForAllFeatures, formula, roundOffWithDbfield);
        int countOccurrencesOf = StringUtils.countOccurrencesOf(simplifiedFormula, "{");
        List<Double> eachLotSimplifiedValue = new ArrayList<>();
        if (countOccurrencesOf > 0) {

            for (HkLotDocument lotDocument : lotDocuments) {

                String lotSimplifieldFormula = simplifiedFormula;
                for (int a = 1; a <= countOccurrencesOf; a++) {
                    // Get start and end index
                    int endidx = lotSimplifieldFormula.indexOf("}");
                    int openingBracesIndex = 0;
                    for (int i = endidx; i > 0; i--) {
                        if (lotSimplifieldFormula.charAt(i) == ('{')) {
                            openingBracesIndex = i;
                            break;
                        }

                    }
                    String subStr = lotSimplifieldFormula.substring(openingBracesIndex, endidx + 1);
                    //System.out.println("Substr" + subStr);
                    String[] roundArr = subStr.split("~~");
                    // get the round off parameter

                    if (roundArr.length > 1) {
                        String evalString = roundArr[0].replaceAll("\\,", "|");
                        String roundVAl = roundArr[1].replace("}", "");
                        //System.out.println("eval string" + evalString);
                        //System.out.println("round value...." + roundVAl);

                        String[] seperateorArr = evalString.split("\\|");
                        for (int l = 0; l < seperateorArr.length; l++) {
                            if (seperateorArr[l].contains(HkSystemConstantUtil.AggregrateFunctions.SUM) || seperateorArr[l].contains(HkSystemConstantUtil.AggregrateFunctions.MIN)
                                    || seperateorArr[l].contains(HkSystemConstantUtil.AggregrateFunctions.MAX)
                                    || seperateorArr[l].contains(HkSystemConstantUtil.AggregrateFunctions.AVG) || seperateorArr[l].contains(HkSystemConstantUtil.AggregrateFunctions.COUNT)) {
                                String frm = seperateorArr[l + 2];
                                String substring = seperateorArr[l] + "|" + seperateorArr[l + 1] + "|" + seperateorArr[l + 2] + "|" + seperateorArr[l + 3];
                                if (frm.contains(HkSystemConstantUtil.Feature.LOT)) {
                                    Double resultFrLot = simplifyAggregrateForLot(invoiceDocument, parcelDocument, lotDocuments, seperateorArr[l], formulaMapForAllFeatures, frm, roundOffValue, roundOffWithDbfield, engine, lotIdAndPacketDocumentMap);

                                    evalString = evalString.replace(substring, resultFrLot.toString());
                                }
                                if (frm.contains(HkSystemConstantUtil.Feature.PACKET)) {
                                    if (lotIdAndPacketDocumentMap != null && lotIdAndPacketDocumentMap.containsKey(lotDocument.getId())) {
                                        List<HkPacketDocument> packetDocuments = lotIdAndPacketDocumentMap.get(lotDocument.getId());
                                        Double resultFrLot = simplifyAggregrateForPacket(invoiceDocument, parcelDocument, lotDocument, packetDocuments, seperateorArr[l], formulaMapForAllFeatures, frm, roundOffValue, roundOffWithDbfield, engine);

                                        evalString = evalString.replace(substring, resultFrLot.toString());
                                    }

                                }
                            }
                        }
                        String execute = evalString.replace("{", "").replace("}", "");

                        Double resultEval = this.evaluateInternalFormulasForAggregrate(invoiceDocument, parcelDocument, lotDocument, null, execute, engine, roundVAl);
                        lotSimplifieldFormula = lotSimplifieldFormula.replace(subStr, resultEval.toString());
//                        }

//                    }
                        // Evaluate formula by replacing fields with values
                    }
                }

                Boolean isEvalRemaining = false;
                try {
                    Double.parseDouble(engine.eval(lotSimplifieldFormula.replace("\\,", "")).toString());
                } catch (ScriptException | NumberFormatException e) {
                    isEvalRemaining = true;
                }
                if (isEvalRemaining) { // After evaluating all {} there might be some basic formulas left...For their evaluation below is the code
                    //System.out.println("in this::::" + lotSimplifieldFormula.replaceAll("\\,", "|"));
                    Double complexFrmResRemaining = this.evaluateInternalFormulasForAggregrate(invoiceDocument, parcelDocument, lotDocument, null, lotSimplifieldFormula, engine, "2");

//                    //System.out.println("its valll addddd remainingh" + complexFrmResRemaining);
//                    parcelSimplifieldFormula = parcelSimplifieldFormula.replace(parcelSimplifieldFormula, complexFrmResRemaining.toString());
//                    //System.out.println("nowwwwwwwwww rema" + parcelSimplifieldFormula);
                    lotSimplifieldFormula = complexFrmResRemaining.toString();

                }
                eachLotSimplifiedValue.add(Double.parseDouble(lotSimplifieldFormula));

            }
        } else {
            for (HkLotDocument lotDocument : lotDocuments) {
                Map fieldValueMap = lotDocument.getFieldValue().toMap();
                if (fieldValueMap.containsKey(simplifiedFormula)) {
                    String valueOfLotField = lotDocument.getFieldValue().get(simplifiedFormula).toString();
                    eachLotSimplifiedValue.add(Double.parseDouble(valueOfLotField));
                }
//                String[] seperateorArr = simplifiedFormula.split("\\|");
//                for (int l = 0; l < seperateorArr.length; l++) {
//                    if (seperateorArr[l].contains(HkSystemConstantUtil.AggregrateFunctions.SUM) || seperateorArr[l].contains(HkSystemConstantUtil.AggregrateFunctions.MIN)
//                            || seperateorArr[l].contains(HkSystemConstantUtil.AggregrateFunctions.MAX)
//                            || seperateorArr[l].contains(HkSystemConstantUtil.AggregrateFunctions.AVG) || seperateorArr[l].contains(HkSystemConstantUtil.AggregrateFunctions.COUNT)) {
//                        String frm = seperateorArr[l + 2];
//                        String substring = seperateorArr[l] + "|" + seperateorArr[l + 1] + "|" + seperateorArr[l + 2] + "|" + seperateorArr[l + 3];
//                        if (frm.contains(HkSystemConstantUtil.Feature.LOT)) {
//                            Double resultFrLot = simplifyAggregrateForLot(invoiceDocument, parcelDocument, lotDocuments, seperateorArr[l], dbFieldWithFormulaMap, frm, roundOffValue, roundOffWithDbfield, engine, lotIdAndPacketDocumentMap);
//
//                            simplifiedFormula = simplifiedFormula.replace(substring, resultFrLot.toString());
//                        }
//                        if (lotIdAndPacketDocumentMap != null && lotIdAndPacketDocumentMap.containsKey(lotDocument.getId())) {
//                            List<HkPacketDocument> packetDocuments = lotIdAndPacketDocumentMap.get(lotDocument.getId());
//                            Double resultFrLot = simplifyAggregrateForPacket(invoiceDocument, parcelDocument, lotDocument, packetDocuments, seperateorArr[l], dbFieldWithFormulaMap, frm, roundOffValue, roundOffWithDbfield, engine);
//
//                            simplifiedFormula = simplifiedFormula.replace(substring, resultFrLot.toString());
//                        }
//
//                    }
//                }
//                String execute = simplifiedFormula.replace("{", "").replace("}", "");
//
//                Double resultEval = this.evaluateInternalFormulasForAggregrate(invoiceDocument, parcelDocument, lotDocument, null, execute, engine, null);
//                eachLotSimplifiedValue.add(resultEval);
            }

        }
        Double finalResult = null;
        if (!CollectionUtils.isEmpty(eachLotSimplifiedValue)) {

            finalResult = this.calculateAggregrateFunction(eachLotSimplifiedValue, OperationType);

        }
        return finalResult;

    }

    //e.g. SUM(Lot.amount) and pass round off value
    private Double simplifyAggregrateForPacket(HkInvoiceDocument invoiceDocument, HkParcelDocument parcelDocument, HkLotDocument lotDocument, List<HkPacketDocument> packetDocuments, String OperationType, Map<String, String> formulaMapForAllFeatures, String formula, String roundOffValue, Map<String, String> roundOffWithDbfield, ScriptEngine engine) {
        Double result = null;
        String simplifiedFormula = recursiveMthodForSimplifyingFormula(formulaMapForAllFeatures, formula, roundOffWithDbfield);
        int countOccurrencesOf = StringUtils.countOccurrencesOf(simplifiedFormula, "{");
        List<Double> eachPacketSimplifiedValue = new ArrayList<>();
        if (countOccurrencesOf > 0) {

            for (HkPacketDocument packetDocument : packetDocuments) {

                String packetSimplifieldFormula = simplifiedFormula;
                for (int a = 1; a <= countOccurrencesOf; a++) {
                    // Get start and end index
                    int endidx = packetSimplifieldFormula.indexOf("}");
                    int openingBracesIndex = 0;
                    for (int i = endidx; i > 0; i--) {
                        if (packetSimplifieldFormula.charAt(i) == ('{')) {
                            openingBracesIndex = i;
                            break;
                        }

                    }
                    String subStr = packetSimplifieldFormula.substring(openingBracesIndex, endidx + 1);
                    //System.out.println("Substr" + subStr);
                    String[] roundArr = subStr.split("~~");
                    // get the round off parameter

                    if (roundArr.length > 1) {
                        String evalString = roundArr[0].replaceAll("\\,", "|");
                        String roundVAl = roundArr[1].replace("}", "");
                        //System.out.println("eval string" + evalString);
                        //System.out.println("round value...." + roundVAl);

                        String[] seperateorArr = evalString.split("\\|");
                        for (int l = 0; l < seperateorArr.length; l++) {
                            if (seperateorArr[l].contains(HkSystemConstantUtil.AggregrateFunctions.SUM) || seperateorArr[l].contains(HkSystemConstantUtil.AggregrateFunctions.MIN)
                                    || seperateorArr[l].contains(HkSystemConstantUtil.AggregrateFunctions.MAX)
                                    || seperateorArr[l].contains(HkSystemConstantUtil.AggregrateFunctions.AVG) || seperateorArr[l].contains(HkSystemConstantUtil.AggregrateFunctions.COUNT)) {
                                String frm = seperateorArr[l + 2];
                                String substring = seperateorArr[l] + "|" + seperateorArr[l + 1] + "|" + seperateorArr[l + 2] + "|" + seperateorArr[l + 3];
                                if (frm.contains(HkSystemConstantUtil.Feature.PACKET)) {
                                    Double resultFrLot = simplifyAggregrateForPacket(invoiceDocument, parcelDocument, lotDocument, packetDocuments, seperateorArr[l], formulaMapForAllFeatures, frm, roundOffValue, roundOffWithDbfield, engine);

                                    evalString = evalString.replace(substring, resultFrLot.toString());
                                }
                            }
                        }
                        String execute = evalString.replace("{", "").replace("}", "");

                        Double resultEval = this.evaluateInternalFormulasForAggregrate(invoiceDocument, parcelDocument, lotDocument, packetDocument, execute, engine, roundVAl);
                        packetSimplifieldFormula = packetSimplifieldFormula.replace(subStr, resultEval.toString());
//                        }

//                    }
                        // Evaluate formula by replacing fields with values
                    }
                }

                Boolean isEvalRemaining = false;
                try {
                    Double.parseDouble(engine.eval(packetSimplifieldFormula.replace("\\,", "")).toString());
                } catch (ScriptException | NumberFormatException e) {
                    isEvalRemaining = true;
                }
                if (isEvalRemaining) { // After evaluating all {} there might be some basic formulas left...For their evaluation below is the code
                    //System.out.println("in this::::" + packetSimplifieldFormula.replaceAll("\\,", "|"));
                    Double complexFrmResRemaining = this.evaluateInternalFormulasForAggregrate(invoiceDocument, parcelDocument, lotDocument, packetDocument, packetSimplifieldFormula, engine, null);

//                    //System.out.println("its valll addddd remainingh" + complexFrmResRemaining);
//                    parcelSimplifieldFormula = parcelSimplifieldFormula.replace(parcelSimplifieldFormula, complexFrmResRemaining.toString());
//                    //System.out.println("nowwwwwwwwww rema" + parcelSimplifieldFormula);
                    packetSimplifieldFormula = complexFrmResRemaining.toString();

                }
                eachPacketSimplifiedValue.add(Double.parseDouble(packetSimplifieldFormula));

            }
        } else {
            for (HkPacketDocument packetDocument : packetDocuments) {

                Map fieldValueMap = packetDocument.getFieldValue().toMap();
                if (fieldValueMap.containsKey(simplifiedFormula)) {
                    String valueOfLotField = packetDocument.getFieldValue().get(simplifiedFormula).toString();
                    eachPacketSimplifiedValue.add(Double.parseDouble(valueOfLotField));
                }
//                String[] seperateorArr = simplifiedFormula.split("\\|");
//                for (int l = 0; l < seperateorArr.length; l++) {
//                    if (seperateorArr[l].contains(HkSystemConstantUtil.AggregrateFunctions.SUM) || seperateorArr[l].contains(HkSystemConstantUtil.AggregrateFunctions.MIN)
//                            || seperateorArr[l].contains(HkSystemConstantUtil.AggregrateFunctions.MAX)
//                            || seperateorArr[l].contains(HkSystemConstantUtil.AggregrateFunctions.AVG) || seperateorArr[l].contains(HkSystemConstantUtil.AggregrateFunctions.COUNT)) {
//                        String frm = seperateorArr[l + 2];
//                        String substring = seperateorArr[l] + "|" + seperateorArr[l + 1] + "|" + seperateorArr[l + 2] + "|" + seperateorArr[l + 3];
//                        if (frm.contains(HkSystemConstantUtil.Feature.PARCEL)) {
//                            Double resultFrLot = simplifyAggregrateForPacket(invoiceDocument, parcelDocument, lotDocument, packetDocuments, seperateorArr[l], dbFieldWithFormulaMap, frm, roundOffValue, roundOffWithDbfield, engine);
//
//                            simplifiedFormula = simplifiedFormula.replace(substring, resultFrLot.toString());
//                        }
//                    }
//                }
//                String execute = simplifiedFormula.replace("{", "").replace("}", "");
//
//                Double resultEval = this.evaluateInternalFormulasForAggregrate(invoiceDocument, parcelDocument, lotDocument, packetDocument, execute, engine, null);
//                eachPacketSimplifiedValue.add(resultEval);
            }

        }
        Double finalResult = null;
        if (!CollectionUtils.isEmpty(eachPacketSimplifiedValue)) {

            finalResult = this.calculateAggregrateFunction(eachPacketSimplifiedValue, OperationType);

        }
        return finalResult;

    }

    private Double evaluateInternalFormulasForAggregrate(HkInvoiceDocument invoiceDocument, HkParcelDocument parcelDocument, HkLotDocument lotDocument, HkPacketDocument packetDocument, String execute, ScriptEngine engine, String roundVAl) {

        Map<String, Double> formulaEvaluatedMap = new HashMap<>();

        String[] formulaValueArray = execute.split("\\|");
        List<String> InvoiceDbField = new ArrayList<>();
        List<String> ParcelDbField = new ArrayList<>();
        List<String> LotDbField = new ArrayList<>();
        List<String> PacketDbField = new ArrayList<>();
        for (String formulaVal : formulaValueArray) {
            if (formulaVal.contains(HkSystemConstantUtil.Feature.INVOICE)) {
                InvoiceDbField.add(formulaVal.replace(HkSystemConstantUtil.Feature.INVOICE + ".", ""));
            }
            if (formulaVal.contains(HkSystemConstantUtil.Feature.PARCEL)) {
                ParcelDbField.add(formulaVal.replace(HkSystemConstantUtil.Feature.PARCEL + ".", ""));
            }
            if (formulaVal.contains(HkSystemConstantUtil.Feature.LOT)) {
                LotDbField.add(formulaVal.replace(HkSystemConstantUtil.Feature.LOT + ".", ""));
            }
            if (formulaVal.contains(HkSystemConstantUtil.Feature.PACKET)) {
                PacketDbField.add(formulaVal.replace(HkSystemConstantUtil.Feature.PACKET + ".", ""));
            }

        }
        Map invMap = null;
        Map parcelMap = null;
        Map lotMap = null;
        Map packetmap = null;
        if (invoiceDocument != null) {
            invMap = invoiceDocument.getFieldValue().toMap();
        }
        if (parcelDocument != null) {
            parcelMap = parcelDocument.getFieldValue().toMap();
        }
        if (lotDocument != null) {
            lotMap = lotDocument.getFieldValue().toMap();

        }
        if (packetDocument != null) {
            packetmap = packetDocument.getFieldValue().toMap();
        }

        if (!CollectionUtils.isEmpty(InvoiceDbField)) {
            for (String Invoice : InvoiceDbField) {
                if (invMap.containsKey(Invoice.trim())) {
                    Double invoiceVal = Double.parseDouble(invMap.get(Invoice.trim()).toString());
                    formulaEvaluatedMap.put(HkSystemConstantUtil.Feature.INVOICE + "." + Invoice.trim(), invoiceVal);
                }
            }
        }
        if (!CollectionUtils.isEmpty(ParcelDbField)) {

            for (String Parcel : ParcelDbField) {
                if (parcelMap.containsKey(Parcel.trim())) {
                    Double parcelVal = Double.parseDouble(parcelMap.get(Parcel.trim()).toString());
                    formulaEvaluatedMap.put(HkSystemConstantUtil.Feature.PARCEL + "." + Parcel, parcelVal);
                }
            }
        }
        if (!CollectionUtils.isEmpty(LotDbField)) {

            for (String Lot : LotDbField) {
                if (lotMap.containsKey(Lot.trim())) {
                    Double lotVal = Double.parseDouble(lotMap.get(Lot.trim()).toString());
                    formulaEvaluatedMap.put(HkSystemConstantUtil.Feature.LOT + "." + Lot.trim(), lotVal);
                }
            }
        }

        // For Packet fields we will fetch from packetDocument which is coming from save itself
        if (!CollectionUtils.isEmpty(PacketDbField)) {
            for (String packet : PacketDbField) {
                if (packetmap.containsKey(packet.trim())) {
                    Double packetVal = Double.parseDouble(packetmap.get(packet.trim()).toString());
                    formulaEvaluatedMap.put(HkSystemConstantUtil.Feature.PACKET + "." + packet.trim(), packetVal);
                }
            }
        }

        if (!formulaEvaluatedMap.isEmpty()) {
            for (int i = 0; i < formulaValueArray.length; i++) {
                if (formulaEvaluatedMap.containsKey(formulaValueArray[i])) {
                    Double Value = formulaEvaluatedMap.get(formulaValueArray[i]);
                    formulaValueArray[i] = Value.toString();
                }
            }
        }
        String finalFormula = Arrays.toString(formulaValueArray).replaceAll("|", "").replaceAll(",", "");
        String finalFormulaString = finalFormula.replace("[", "").replace("]", "");
        Double resultEval = null;
        try {
            resultEval = Double.parseDouble(engine.eval(finalFormulaString).toString());
        } catch (ScriptException ex) {
            Logger.getLogger(HkFormulaExecution.class.getName()).log(Level.SEVERE, null, ex);
        }
        resultEval = roundValues(resultEval, Integer.parseInt(roundVAl));
        return resultEval;
    }

    private Double calculateAggregrateFunction(List<Double> valuesToBeEvaluated, String OperationType) {
        Double aggregrateResult = null;
        if (!CollectionUtils.isEmpty(valuesToBeEvaluated)) {
            switch (OperationType) {
                case HkSystemConstantUtil.AggregrateFunctions.SUM:
                    Double sum = (double) 0;
                    for (Double value : valuesToBeEvaluated) {
                        //System.out.println("in here" + value);
                        sum = sum + value;
                    }
                    aggregrateResult = sum;
                    break;
                case HkSystemConstantUtil.AggregrateFunctions.COUNT:
                    Double count = new Double(0);
                    for (Double value : valuesToBeEvaluated) {
                        count++;
                    }
                    aggregrateResult = count;
                    break;
                case HkSystemConstantUtil.AggregrateFunctions.AVG:
                    Double avg = (double) 0;
                    Double totalNum = (double) 0;
                    Double Sum_totalNum = (double) 0;
                    for (Double value : valuesToBeEvaluated) {
                        totalNum++;
                        Sum_totalNum = Sum_totalNum + value;
                    }
                    Double averageVal = Sum_totalNum / totalNum;
                    aggregrateResult = averageVal;
                    break;
                case HkSystemConstantUtil.AggregrateFunctions.MIN:
                    aggregrateResult = Collections.min(valuesToBeEvaluated);
                    break;
                case HkSystemConstantUtil.AggregrateFunctions.MAX:
                    aggregrateResult = Collections.max(valuesToBeEvaluated);
                    break;

            }
        }
        return aggregrateResult;
    }

    private Map<ObjectId, List<HkLotDocument>> retrieveParcelIdAndLotDocumentMap(Long franchise, Boolean isArchive) {
        Map<ObjectId, List<HkLotDocument>> parcelIdLotDocs = null;
        List<HkLotDocument> listOfLotDocs = this.retrieveAllLotFieldValues(franchise, isArchive);
        if (!CollectionUtils.isEmpty(listOfLotDocs)) {
            parcelIdLotDocs = new HashMap<>();
            for (HkLotDocument listOfLotDoc : listOfLotDocs) {

                if (parcelIdLotDocs.containsKey(listOfLotDoc.getParcel())) {
                    List<HkLotDocument> get = parcelIdLotDocs.get(listOfLotDoc.getParcel());
                    get.add(listOfLotDoc);
                    parcelIdLotDocs.put(listOfLotDoc.getParcel(), get);
                } else {
                    List<HkLotDocument> lotDoc = new ArrayList<>();
                    lotDoc.add(listOfLotDoc);
                    parcelIdLotDocs.put(listOfLotDoc.getParcel(), lotDoc);
                }

            }
        }
        return parcelIdLotDocs;
    }

    private List<HkLotDocument> retrieveAllLotFieldValues(Long franchise, Boolean isArchive) {
        List<HkLotDocument> documents = null;
        Query query = new Query();
        query.addCriteria(Criteria.where(ParcelFields.FRANCHISE).in(this.getCompnies(franchise)));
        query.addCriteria((Criteria.where(ParcelFields.IS_ARCHIVE).is(isArchive)));
        query.fields().include(LotFields.LOT_Id);
        query.fields().include(LotFields.INVOICE_ID);
        query.fields().include(LotFields.PARCEL_ID);
        query.fields().include(LotFields.FIELD_VALUE);

        documents = mongoGenericDao.getMongoTemplate().find(query, HkLotDocument.class);

        return documents;
    }

    private Map<ObjectId, List<HkPacketDocument>> retrieveLotIdAndPacketDocumentMap(Long franchise, Boolean isArchive
    ) {
        Map<ObjectId, List<HkPacketDocument>> lotIdPacketDocs = null;
        List<HkPacketDocument> packetDocLists = this.retrieveAllPacketFieldValues(franchise, isArchive);
        if (!CollectionUtils.isEmpty(packetDocLists)) {
            lotIdPacketDocs = new HashMap<>();
            for (HkPacketDocument packetDoc : packetDocLists) {

                if (lotIdPacketDocs.containsKey(packetDoc.getLot())) {
                    List<HkPacketDocument> get = lotIdPacketDocs.get(packetDoc.getLot());
                    get.add(packetDoc);
                    lotIdPacketDocs.put(packetDoc.getLot(), get);
                } else {
                    List<HkPacketDocument> packetDocs = new ArrayList<>();
                    packetDocs.add(packetDoc);
                    lotIdPacketDocs.put(packetDoc.getLot(), packetDocs);
                }

            }
        }
        return lotIdPacketDocs;

    }

    private List<HkPacketDocument> retrieveAllPacketFieldValues(Long franchise, Boolean isArchive) {
        List<HkPacketDocument> documents = null;
        Query query = new Query();
        query.addCriteria(Criteria.where(PacketFields.FRANCHISE).in(this.getCompnies(franchise)));
        query.addCriteria((Criteria.where(PacketFields.IS_ARCHIVE).is(isArchive)));
        query.fields().include(PacketFields.PACKET_ID);
        query.fields().include(PacketFields.INVOICE_ID);
        query.fields().include(PacketFields.PARCEL_ID);
        query.fields().include(PacketFields.LOT_ID);
        query.fields().include(PacketFields.FIELD_VALUE);
        documents = mongoGenericDao.getMongoTemplate().find(query, HkPacketDocument.class);
        return documents;
    }

    private Map<String, String> createFieldNameWithComponentType(Map<String, Object> fieldValues
    ) {
        Map<String, String> componentCodeMap = new HashMap<>(HkSystemConstantUtil.CustomField.COMPONENT_CODE_MAP);
        Map<String, String> codeMap = null;
        if (componentCodeMap != null && !componentCodeMap.isEmpty()) {
            codeMap = new HashMap<>();
            for (Map.Entry<String, String> entry : componentCodeMap.entrySet()) {
                codeMap.put(entry.getValue(), entry.getKey());

            }
        }
        Map<String, String> fieldWithComponentMap = new HashMap<>();
        if (fieldValues != null && !fieldValues.isEmpty()) {
            for (Map.Entry<String, Object> custom : fieldValues.entrySet()) {
                String[] split = custom.getKey().split("\\" + HkSystemConstantUtil.SEPERATOR_FOR_CUSTOM_FIELD_LABEL);
                if (split != null && split.length >= 1) {
                    fieldWithComponentMap.put(custom.getKey(), codeMap.get(split[1]));
                }
            }
        }
        return fieldWithComponentMap;
    }

    private Map<ObjectId, List<HkParcelDocument>> retrieveInvoiceIdAndParcelDocumentMap(Long franchise, Boolean isArchive
    ) {

        Map<ObjectId, List<HkParcelDocument>> invoiceIdParcelDocs = null;
        List<HkParcelDocument> listOfParcelDocs = this.retrieveAllParcelFieldValues(franchise, isArchive);
        if (!CollectionUtils.isEmpty(listOfParcelDocs)) {
            invoiceIdParcelDocs = new HashMap<>();
            for (HkParcelDocument listOfParcelDoc : listOfParcelDocs) {

                if (invoiceIdParcelDocs.containsKey(listOfParcelDoc.getInvoice())) {
                    List<HkParcelDocument> get = invoiceIdParcelDocs.get(listOfParcelDoc.getInvoice());
                    get.add(listOfParcelDoc);
                    invoiceIdParcelDocs.put(listOfParcelDoc.getInvoice(), get);
                } else {
                    List<HkParcelDocument> lotDoc = new ArrayList<>();
                    lotDoc.add(listOfParcelDoc);
                    invoiceIdParcelDocs.put(listOfParcelDoc.getInvoice(), lotDoc);
                }

            }
        }
        return invoiceIdParcelDocs;
    }

    private List<HkParcelDocument> retrieveAllParcelFieldValues(Long franchise, Boolean isArchive) {

        List<HkParcelDocument> documents;
        Query query = new Query();

        query.addCriteria(Criteria.where(ParcelFields.FRANCHISE).in(this.getCompnies(franchise)));
        query.addCriteria((Criteria.where(ParcelFields.IS_ARCHIVE).is(isArchive)));
        query.fields().include(ParcelFields.OBJECT_ID);
        query.fields().include(ParcelFields.INVOICE_ID);
        query.fields().include(ParcelFields.FIELD_VALUE);

        documents = mongoGenericDao.getMongoTemplate().find(query, HkParcelDocument.class);

        return documents;
    }
// This method accept all formulas,then for the passed feature it returns accepted formulas

    private Map<String, String> retrieveValidFormulasOfTheFeature(List<HkFieldDocument> fieldDocuments, String featureType, long featureId) {
        List<String> invoiceList = new ArrayList<>();
        List<String> parcelList = new ArrayList<>();
        List<String> lotList = new ArrayList<>();
        List<String> packetList = new ArrayList<>();
        List<String> planList = new ArrayList<>();
        List<String> sellList = new ArrayList<>();
        List<String> transferList = new ArrayList<>();
        List<String> issueList = new ArrayList<>();
        List<String> subLotList = new ArrayList<>();
        int countInvalid = 0;
        Map<String, String> validFormulaMap = null;

        if (!CollectionUtils.isEmpty(fieldDocuments)) {
            validFormulaMap = new HashMap<>();
            for (HkFieldDocument field : fieldDocuments) {
                if (field.getFeature() == (featureId)) {
                    String formulaValue = field.getFormulaValue();
                    String[] formulaArray = formulaValue.split("\\|");
                    ////System.out.println("In core formula Array" + formulaArray);
                    for (String formula : formulaArray) {
                        if (formula.contains(HkSystemConstantUtil.Feature.INVOICE)) {
                            invoiceList.add(formula.replace(HkSystemConstantUtil.Feature.INVOICE + ".", ""));
                            ////System.out.println("InvoiceList" + invoiceList);
                        }
                        if (formula.contains(HkSystemConstantUtil.Feature.PARCEL)) {
                            parcelList.add(formula.replace(HkSystemConstantUtil.Feature.PARCEL + ".", ""));
                            ////System.out.println("Parcel List" + parcelList);
                        }
                        if (formula.contains(HkSystemConstantUtil.Feature.LOT)) {
                            lotList.add(formula.replace(HkSystemConstantUtil.Feature.LOT + ".", ""));
                        }
                        if (formula.contains(HkSystemConstantUtil.Feature.PACKET)) {
                            packetList.add(formula.replace(HkSystemConstantUtil.Feature.PACKET + ".", ""));
                        }
                        if (formula.contains(HkSystemConstantUtil.Feature.PLAN)) {
                            planList.add(formula.replace(HkSystemConstantUtil.Feature.PLAN + ".", ""));
                        }
                        if (formula.contains(HkSystemConstantUtil.Feature.SELL)) {
                            sellList.add(formula.replace(HkSystemConstantUtil.Feature.SELL + ".", ""));
                        }
                        if (formula.contains(HkSystemConstantUtil.Feature.TRANSFER)) {
                            transferList.add(formula.replace(HkSystemConstantUtil.Feature.TRANSFER + ".", ""));
                        }
                        if (formula.contains(HkSystemConstantUtil.Feature.ISSUE)) {
                            issueList.add(formula.replace(HkSystemConstantUtil.Feature.ISSUE + ".", ""));
                        }
                        if (formula.contains(HkSystemConstantUtil.Feature.SUB_LOT)) {
                            subLotList.add(formula.replace(HkSystemConstantUtil.Feature.SUB_LOT + ".", ""));
                        }
                        if (subLotList.size() > 0) {
                            //System.out.println("sublot list is not empty");
                        }

                    }
                    switch (featureType) {
                        case HkSystemConstantUtil.FeatureForFormulaEvaluation.ADD_LOT:
                        case HkSystemConstantUtil.FeatureForFormulaEvaluation.EDIT_LOT:
                            if (!CollectionUtils.isEmpty(invoiceList)) {
                                for (String inv_formula : invoiceList) {
                                    int indexOf = Arrays.asList(formulaArray).indexOf(inv_formula);
                                    if (indexOf >= 4) {
                                        if ((formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.SUM))
                                                || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MIN))
                                                || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MAX))
                                                || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.AVG))
                                                || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.COUNT))) {

                                            countInvalid++;
                                        } else {
//                                        validFormula += "true";
                                        }

                                    } else {
//                                    validFormula += "true";
                                    }
                                }
                            }
                            if (!CollectionUtils.isEmpty(parcelList)) {
                                for (String par_formula : parcelList) {
                                    int indexOf = Arrays.asList(formulaArray).indexOf(par_formula);
                                    if (indexOf >= 4) {
                                        if ((formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.SUM))
                                                || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MIN))
                                                || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MAX))
                                                || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.AVG))
                                                || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.COUNT))) {

                                            countInvalid++;
                                        } else {
//                                        validFormula += "true";
                                        }

                                    } else {
//                                    validFormula += "true";
                                    }
                                }
                            }
                            if (!CollectionUtils.isEmpty(lotList)) {
                                for (String lot_formula : lotList) {
                                    int indexOf = Arrays.asList(formulaArray).indexOf(lot_formula);
                                    if (indexOf >= 4) {
                                        if ((formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.SUM))
                                                || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MIN))
                                                || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MAX))
                                                || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.AVG))
                                                || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.COUNT))) {

                                            countInvalid++;
                                        } else {
//                                        validFormula += "true";
                                        }

                                    } else {
//                                    validFormula += "true";
                                    }
                                }
                            }
                            if (!CollectionUtils.isEmpty(packetList)) {
                                for (String packet_formula : packetList) {
                                    int indexOf = Arrays.asList(formulaArray).indexOf(packet_formula);
                                    if (indexOf >= 4) {
                                        if ((formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.SUM))
                                                || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MIN))
                                                || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MAX))
                                                || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.AVG))
                                                || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.COUNT))) {

//                                        validFormula += "true";
                                        } else {
                                            countInvalid++;
//                                        validFormula += "false";
                                        }

                                    } else {
                                        countInvalid++;
//                                    validFormula += "false";
                                    }
                                }
                            }
                            if (countInvalid > 1) {
                            } else {
                                validFormulaMap.put(field.getDbFieldName(), formulaValue);
                            }

                            break;
                        case HkSystemConstantUtil.FeatureForFormulaEvaluation.ADD_PARCEL:
                        case HkSystemConstantUtil.FeatureForFormulaEvaluation.EDIT_PARCEL:
                            if (!CollectionUtils.isEmpty(invoiceList)) {
                                for (String inv_formula : invoiceList) {
                                    int indexOf = Arrays.asList(formulaArray).indexOf(inv_formula);
                                    if (indexOf >= 4) {
                                        if ((formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.SUM))
                                                || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MIN))
                                                || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MAX))
                                                || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.AVG))
                                                || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.COUNT))) {

                                            countInvalid++;
                                        } else {
//                                        validFormula += "true";
                                        }

                                    } else {
//                                    validFormula += "true";
                                    }
                                }
                            }
                            if (!CollectionUtils.isEmpty(parcelList)) {
                                for (String par_formula : parcelList) {
                                    int indexOf = Arrays.asList(formulaArray).indexOf(par_formula);
                                    if (indexOf >= 4) {
                                        if ((formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.SUM))
                                                || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MIN))
                                                || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MAX))
                                                || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.AVG))
                                                || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.COUNT))) {

                                            countInvalid++;
                                        } else {
//                                        validFormula += "true";
                                        }

                                    } else {
//                                    validFormula += "true";
                                    }
                                }
                            }
                            if (!CollectionUtils.isEmpty(lotList)) {
                                for (String lot_formula : lotList) {
                                    int indexOf = Arrays.asList(formulaArray).indexOf(lot_formula);
                                    if (indexOf >= 4) {
                                        if ((formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.SUM))
                                                || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MIN))
                                                || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MAX))
                                                || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.AVG))
                                                || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.COUNT))) {

                                        } else {
                                            countInvalid++;
                                        }

                                    } else {
                                        countInvalid++;
                                    }
                                }
                            }
                            if (!CollectionUtils.isEmpty(packetList)) {
                                for (String packet_formula : packetList) {
                                    int indexOf = Arrays.asList(formulaArray).indexOf(packet_formula);
                                    if (indexOf >= 4) {
                                        if ((formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.SUM))
                                                || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MIN))
                                                || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MAX))
                                                || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.AVG))
                                                || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.COUNT))) {

//                                        validFormula += "true";
                                        } else {
                                            countInvalid++;
//                                        validFormula += "false";
                                        }

                                    } else {
                                        countInvalid++;
//                                    validFormula += "false";
                                    }
                                }
                            }
                            if (countInvalid > 1) {
                            } else {
                                validFormulaMap.put(field.getDbFieldName(), formulaValue);
                            }

                            break;
                        case HkSystemConstantUtil.FeatureForFormulaEvaluation.ADD_PACKET:
                        case HkSystemConstantUtil.FeatureForFormulaEvaluation.EDIT_PACKET:
                            if (!CollectionUtils.isEmpty(invoiceList)) {
                                for (String inv_formula : invoiceList) {
                                    int indexOf = Arrays.asList(formulaArray).indexOf(inv_formula);
                                    if (indexOf >= 4) {
                                        if ((formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.SUM))
                                                || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MIN))
                                                || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MAX))
                                                || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.AVG))
                                                || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.COUNT))) {

                                            countInvalid++;
                                        } else {
//                                        validFormula += "true";
                                        }

                                    } else {
//                                    validFormula += "true";
                                    }
                                }
                            }
                            if (!CollectionUtils.isEmpty(parcelList)) {
                                for (String par_formula : parcelList) {
                                    int indexOf = Arrays.asList(formulaArray).indexOf(par_formula);
                                    if (indexOf >= 4) {
                                        if ((formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.SUM))
                                                || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MIN))
                                                || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MAX))
                                                || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.AVG))
                                                || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.COUNT))) {

                                            countInvalid++;
                                        } else {
//                                        validFormula += "true";
                                        }

                                    } else {
//                                    validFormula += "true";
                                    }
                                }
                            }
                            if (!CollectionUtils.isEmpty(lotList)) {
                                for (String lot_formula : lotList) {
                                    int indexOf = Arrays.asList(formulaArray).indexOf(lot_formula);
                                    if (indexOf >= 4) {
                                        if ((formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.SUM))
                                                || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MIN))
                                                || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MAX))
                                                || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.AVG))
                                                || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.COUNT))) {
                                            countInvalid++;

                                        } else {

                                        }

                                    } else {

                                    }
                                }
                            }
                            if (!CollectionUtils.isEmpty(packetList)) {
                                for (String packet_formula : packetList) {
                                    int indexOf = Arrays.asList(formulaArray).indexOf(packet_formula);
                                    if (indexOf >= 4) {
                                        if ((formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.SUM))
                                                || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MIN))
                                                || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MAX))
                                                || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.AVG))
                                                || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.COUNT))) {

                                            countInvalid++;
                                        } else {
//                                        validFormula += "false";
                                        }

                                    } else {
//                                    validFormula += "false";
                                    }
                                }
                            }
                            if (countInvalid > 1) {
                            } else {
                                validFormulaMap.put(field.getDbFieldName(), formulaValue);
                            }

                            break;
                        case HkSystemConstantUtil.FeatureForFormulaEvaluation.ADD_PLAN:
                        case HkSystemConstantUtil.FeatureForFormulaEvaluation.EDIT_PLAN:
                            if (!CollectionUtils.isEmpty(invoiceList)) {
                                for (String inv_formula : invoiceList) {
                                    int indexOf = Arrays.asList(formulaArray).indexOf(inv_formula);
                                    if (indexOf >= 4) {
                                        if ((formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.SUM))
                                                || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MIN))
                                                || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MAX))
                                                || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.AVG))
                                                || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.COUNT))) {

                                            countInvalid++;
                                        } else {
//                                        validFormula += "true";
                                        }

                                    } else {
//                                    validFormula += "true";
                                    }
                                }
                            }
                            if (!CollectionUtils.isEmpty(parcelList)) {
                                for (String par_formula : parcelList) {
                                    int indexOf = Arrays.asList(formulaArray).indexOf(par_formula);
                                    if (indexOf >= 4) {
                                        if ((formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.SUM))
                                                || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MIN))
                                                || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MAX))
                                                || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.AVG))
                                                || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.COUNT))) {

                                            countInvalid++;
                                        } else {
//                                        validFormula += "true";
                                        }

                                    } else {
//                                    validFormula += "true";
                                    }
                                }
                            }
                            if (!CollectionUtils.isEmpty(lotList)) {
                                for (String lot_formula : lotList) {
                                    int indexOf = Arrays.asList(formulaArray).indexOf(lot_formula);
                                    if (indexOf >= 4) {
                                        if ((formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.SUM))
                                                || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MIN))
                                                || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MAX))
                                                || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.AVG))
                                                || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.COUNT))) {
                                            countInvalid++;

                                        } else {

                                        }

                                    } else {

                                    }
                                }
                            }
                            if (!CollectionUtils.isEmpty(packetList)) {
                                for (String packet_formula : packetList) {
                                    int indexOf = Arrays.asList(formulaArray).indexOf(packet_formula);
                                    if (indexOf >= 4) {
                                        if ((formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.SUM))
                                                || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MIN))
                                                || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MAX))
                                                || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.AVG))
                                                || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.COUNT))) {

                                            countInvalid++;
                                        } else {
//                                        validFormula += "false";
                                        }

                                    } else {
//                                    validFormula += "false";
                                    }
                                }
                            }
                            if (!CollectionUtils.isEmpty(planList)) {
                                for (String plan_formula : planList) {
                                    int indexOf = Arrays.asList(formulaArray).indexOf(plan_formula);
                                    if (indexOf >= 4) {
                                        if ((formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.SUM))
                                                || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MIN))
                                                || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MAX))
                                                || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.AVG))
                                                || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.COUNT))) {

                                            countInvalid++;
                                        } else {
//                                        validFormula += "false";
                                        }

                                    } else {
//                                    validFormula += "false";
                                    }
                                }
                            }
                            if (countInvalid > 1) {
                            } else {
                                validFormulaMap.put(field.getDbFieldName(), formulaValue);
                            }

                            break;
                        case HkSystemConstantUtil.FeatureForFormulaEvaluation.SELL_STOCK:
                            if (!CollectionUtils.isEmpty(invoiceList)) {
                                for (String inv_formula : invoiceList) {
                                    int indexOf = Arrays.asList(formulaArray).indexOf(inv_formula);
                                    if (indexOf >= 4) {
                                        if ((formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.SUM))
                                                || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MIN))
                                                || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MAX))
                                                || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.AVG))
                                                || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.COUNT))) {

                                            countInvalid++;
                                        } else {
//                                        validFormula += "true";
                                        }

                                    } else {
//                                    validFormula += "true";
                                    }
                                }
                            }
                            if (!CollectionUtils.isEmpty(parcelList)) {
                                for (String par_formula : parcelList) {
                                    int indexOf = Arrays.asList(formulaArray).indexOf(par_formula);
                                    if (indexOf >= 4) {
                                        if ((formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.SUM))
                                                || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MIN))
                                                || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MAX))
                                                || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.AVG))
                                                || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.COUNT))) {

                                            countInvalid++;
                                        } else {
//                                        validFormula += "true";
                                        }

                                    } else {
//                                    validFormula += "true";
                                    }
                                }
                            }
                            if (!CollectionUtils.isEmpty(lotList)) {
                                for (String lot_formula : lotList) {
                                    int indexOf = Arrays.asList(formulaArray).indexOf(lot_formula);
                                    if (indexOf >= 4) {
                                        if ((formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.SUM))
                                                || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MIN))
                                                || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MAX))
                                                || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.AVG))
                                                || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.COUNT))) {
                                            countInvalid++;

                                        } else {

                                        }

                                    } else {

                                    }
                                }
                            }
                            if (!CollectionUtils.isEmpty(packetList)) {
                                for (String packet_formula : packetList) {
                                    int indexOf = Arrays.asList(formulaArray).indexOf(packet_formula);
                                    if (indexOf >= 4) {
                                        if ((formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.SUM))
                                                || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MIN))
                                                || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MAX))
                                                || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.AVG))
                                                || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.COUNT))) {

                                            countInvalid++;
                                        } else {
//                                        validFormula += "false";
                                        }

                                    } else {
//                                    validFormula += "false";
                                    }
                                }
                            }
                            if (!CollectionUtils.isEmpty(sellList)) {
                                for (String sell_formula : sellList) {
                                    int indexOf = Arrays.asList(formulaArray).indexOf(sell_formula);
                                    if (indexOf >= 4) {
                                        if ((formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.SUM))
                                                || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MIN))
                                                || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MAX))
                                                || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.AVG))
                                                || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.COUNT))) {

                                            countInvalid++;
                                        } else {
//                                        validFormula += "false";
                                        }

                                    } else {
//                                    validFormula += "false";
                                    }
                                }
                            }
                            if (countInvalid > 1) {
                            } else {
                                validFormulaMap.put(field.getDbFieldName(), formulaValue);
                            }

                            break;
                        case HkSystemConstantUtil.FeatureForFormulaEvaluation.TRANSFER_STOCK:
                            if (!CollectionUtils.isEmpty(invoiceList)) {
                                for (String inv_formula : invoiceList) {
                                    int indexOf = Arrays.asList(formulaArray).indexOf(inv_formula);
                                    if (indexOf >= 4) {
                                        if ((formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.SUM))
                                                || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MIN))
                                                || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MAX))
                                                || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.AVG))
                                                || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.COUNT))) {

                                            countInvalid++;
                                        } else {
//                                        validFormula += "true";
                                        }

                                    } else {
//                                    validFormula += "true";
                                    }
                                }
                            }
                            if (!CollectionUtils.isEmpty(parcelList)) {
                                for (String par_formula : parcelList) {
                                    int indexOf = Arrays.asList(formulaArray).indexOf(par_formula);
                                    if (indexOf >= 4) {
                                        if ((formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.SUM))
                                                || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MIN))
                                                || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MAX))
                                                || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.AVG))
                                                || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.COUNT))) {

                                            countInvalid++;
                                        } else {
//                                        validFormula += "true";
                                        }

                                    } else {
//                                    validFormula += "true";
                                    }
                                }
                            }
                            if (!CollectionUtils.isEmpty(lotList)) {
                                for (String lot_formula : lotList) {
                                    int indexOf = Arrays.asList(formulaArray).indexOf(lot_formula);
                                    if (indexOf >= 4) {
                                        if ((formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.SUM))
                                                || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MIN))
                                                || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MAX))
                                                || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.AVG))
                                                || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.COUNT))) {
                                            countInvalid++;

                                        } else {

                                        }

                                    } else {

                                    }
                                }
                            }
                            if (!CollectionUtils.isEmpty(packetList)) {
                                for (String packet_formula : packetList) {
                                    int indexOf = Arrays.asList(formulaArray).indexOf(packet_formula);
                                    if (indexOf >= 4) {
                                        if ((formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.SUM))
                                                || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MIN))
                                                || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MAX))
                                                || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.AVG))
                                                || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.COUNT))) {

                                            countInvalid++;
                                        } else {
//                                        validFormula += "false";
                                        }

                                    } else {
//                                    validFormula += "false";
                                    }
                                }
                            }
                            if (!CollectionUtils.isEmpty(transferList)) {
                                for (String transfer_formula : transferList) {
                                    int indexOf = Arrays.asList(formulaArray).indexOf(transfer_formula);
                                    if (indexOf >= 4) {
                                        if ((formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.SUM))
                                                || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MIN))
                                                || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MAX))
                                                || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.AVG))
                                                || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.COUNT))) {

                                            countInvalid++;
                                        } else {
//                                        validFormula += "false";
                                        }

                                    } else {
//                                    validFormula += "false";
                                    }
                                }
                            }
                            if (countInvalid > 1) {
                            } else {
                                validFormulaMap.put(field.getDbFieldName(), formulaValue);
                            }

                            break;
                        case HkSystemConstantUtil.FeatureForFormulaEvaluation.ISSUE_STOCK:
                            if (!CollectionUtils.isEmpty(invoiceList)) {
                                for (String inv_formula : invoiceList) {
                                    int indexOf = Arrays.asList(formulaArray).indexOf(inv_formula);
                                    if (indexOf >= 4) {
                                        if ((formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.SUM))
                                                || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MIN))
                                                || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MAX))
                                                || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.AVG))
                                                || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.COUNT))) {

                                            countInvalid++;
                                        } else {
//                                        validFormula += "true";
                                        }

                                    } else {
//                                    validFormula += "true";
                                    }
                                }
                            }
                            if (!CollectionUtils.isEmpty(parcelList)) {
                                for (String par_formula : parcelList) {
                                    int indexOf = Arrays.asList(formulaArray).indexOf(par_formula);
                                    if (indexOf >= 4) {
                                        if ((formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.SUM))
                                                || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MIN))
                                                || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MAX))
                                                || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.AVG))
                                                || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.COUNT))) {

                                            countInvalid++;
                                        } else {
//                                        validFormula += "true";
                                        }

                                    } else {
//                                    validFormula += "true";
                                    }
                                }
                            }
                            if (!CollectionUtils.isEmpty(lotList)) {
                                for (String lot_formula : lotList) {
                                    int indexOf = Arrays.asList(formulaArray).indexOf(lot_formula);
                                    if (indexOf >= 4) {
                                        if ((formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.SUM))
                                                || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MIN))
                                                || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MAX))
                                                || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.AVG))
                                                || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.COUNT))) {
                                            countInvalid++;

                                        } else {

                                        }

                                    } else {

                                    }
                                }
                            }
                            if (!CollectionUtils.isEmpty(packetList)) {
                                for (String packet_formula : packetList) {
                                    int indexOf = Arrays.asList(formulaArray).indexOf(packet_formula);
                                    if (indexOf >= 4) {
                                        if ((formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.SUM))
                                                || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MIN))
                                                || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MAX))
                                                || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.AVG))
                                                || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.COUNT))) {

                                            countInvalid++;
                                        } else {
//                                        validFormula += "false";
                                        }

                                    } else {
//                                    validFormula += "false";
                                    }
                                }
                            }
                            if (!CollectionUtils.isEmpty(issueList)) {
                                for (String issue_formula : issueList) {
                                    int indexOf = Arrays.asList(formulaArray).indexOf(issue_formula);
                                    if (indexOf >= 4) {
                                        if ((formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.SUM))
                                                || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MIN))
                                                || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MAX))
                                                || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.AVG))
                                                || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.COUNT))) {

                                            countInvalid++;
                                        } else {
//                                        validFormula += "false";
                                        }

                                    } else {
//                                    validFormula += "false";
                                    }
                                }
                            }
                            if (countInvalid > 1) {
                            } else {
                                validFormulaMap.put(field.getDbFieldName(), formulaValue);
                            }

                            break;

                        // Code added for sublot on 20 October 2015
                        case HkSystemConstantUtil.FeatureForFormulaEvaluation.EDIT_SUB_LOT:
                            if (!CollectionUtils.isEmpty(subLotList)) {
                                for (String sublot_formula : subLotList) {
                                    //System.out.println("sublot form" + sublot_formula);
                                    int indexOf = Arrays.asList(formulaArray).indexOf(sublot_formula);
                                    if (indexOf >= 4) {
                                        if ((formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.SUM))
                                                || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MIN))
                                                || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MAX))
                                                || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.AVG))
                                                || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.COUNT))) {

                                            //System.out.println("inside ayaaaaaaaa");
                                            countInvalid++;
                                        } else {
//                                        validFormula += "false";
                                        }

                                    } else {
//                                    validFormula += "false";
                                    }
                                }
                            }
                            //System.out.println("count validddd" + countInvalid);
                            if (countInvalid > 1) {
                            } else {
                                validFormulaMap.put(field.getDbFieldName(), formulaValue);
                            }
                            //System.out.println("valid frm amp" + validFormulaMap);
                            break;

                        case HkSystemConstantUtil.FeatureForFormulaEvaluation.ADD_INVOICE:
                        case HkSystemConstantUtil.FeatureForFormulaEvaluation.EDIT_INVOICE:
                            if (!CollectionUtils.isEmpty(invoiceList)) {
                                for (String inv_formula : invoiceList) {
                                    int indexOf = Arrays.asList(formulaArray).indexOf(inv_formula);
                                    if (indexOf >= 4) {
                                        if ((formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.SUM))
                                                || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MIN))
                                                || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MAX))
                                                || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.AVG))
                                                || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.COUNT))) {

                                            countInvalid++;
                                        } else {
//                                        validFormula += "true";
                                        }

                                    } else {
//                                    validFormula += "true";
                                    }
                                }
                            }
                            if (!CollectionUtils.isEmpty(parcelList)) {
                                for (String par_formula : parcelList) {
                                    int indexOf = Arrays.asList(formulaArray).indexOf(par_formula);
                                    if (indexOf >= 4) {
                                        if ((formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.SUM))
                                                || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MIN))
                                                || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MAX))
                                                || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.AVG))
                                                || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.COUNT))) {

                                        } else {
                                            countInvalid++;
                                        }

                                    } else {
                                        countInvalid++;
//                                    validFormula += "true";
                                    }
                                }
                            }
                            if (!CollectionUtils.isEmpty(lotList)) {
                                for (String lot_formula : lotList) {
                                    int indexOf = Arrays.asList(formulaArray).indexOf(lot_formula);
                                    if (indexOf >= 4) {
                                        if ((formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.SUM))
                                                || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MIN))
                                                || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MAX))
                                                || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.AVG))
                                                || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.COUNT))) {

                                        } else {
                                            countInvalid++;
                                        }

                                    } else {
                                        countInvalid++;
                                    }
                                }
                            }
                            if (!CollectionUtils.isEmpty(packetList)) {
                                for (String packet_formula : packetList) {
                                    int indexOf = Arrays.asList(formulaArray).indexOf(packet_formula);
                                    if (indexOf >= 4) {
                                        if ((formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.SUM))
                                                || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MIN))
                                                || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.MAX))
                                                || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.AVG))
                                                || (formulaArray[indexOf - 4].equalsIgnoreCase(HkSystemConstantUtil.AggregrateFunctions.COUNT))) {

                                        } else {
                                            countInvalid++;
                                        }

                                    } else {
                                        countInvalid++;
                                    }
                                }
                            }
                            //System.out.println("count valid" + countInvalid);
                            if (countInvalid > 1) {
                            } else {
                                validFormulaMap.put(field.getDbFieldName(), formulaValue);
                            }

                            break;

                    }
                }
            }
        }
        return validFormulaMap;

    }

    private Map<String, String> mapOfDbFieldAndFormulaForAllFeatures(List<HkFieldDocument> fieldDocuments) {
        Map<String, String> mapOfDbFieldAndFormulaForAllFeatures = null;
        if (!CollectionUtils.isEmpty(fieldDocuments)) {
            mapOfDbFieldAndFormulaForAllFeatures = new HashMap<>();
            for (HkFieldDocument fieldDocument : fieldDocuments) {
                mapOfDbFieldAndFormulaForAllFeatures.put(fieldDocument.getDbFieldName(), fieldDocument.getFormulaValue());
            }
        }
        return mapOfDbFieldAndFormulaForAllFeatures;

    }

    private static Double roundValues(double value, int numberOfDigitsAfterDecimalPoint) {
        BigDecimal bigDecimal = new BigDecimal(value);
        bigDecimal = bigDecimal.setScale(numberOfDigitsAfterDecimalPoint,
                BigDecimal.ROUND_HALF_UP);
        return bigDecimal.doubleValue();
    }
}
