/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.center.rapcalc.transformers;

import com.argusoft.hkg.common.constantutil.HkSystemConstantUtil;
import com.argusoft.hkg.nosql.core.HkCustomFieldService;
import com.argusoft.hkg.nosql.core.HkRapCalcService;
import com.argusoft.hkg.nosql.model.HkCalcBasPriceDocument;
import com.argusoft.hkg.nosql.model.HkCalcFourCDiscountDocument;
import com.argusoft.hkg.nosql.model.HkCalcRateDetailDocument;
import com.argusoft.hkg.nosql.model.RangeDocument;
import com.argusoft.hkg.web.center.rapcalc.databeans.CalcRateDetailDatabean;
import com.argusoft.hkg.web.center.usermanagement.databeans.LoginDataBean;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 *
 * @author shruti
 */
@Service
public class CalcRateDetailTransformer {

    @Autowired
    private HkCustomFieldService customFieldService;

    @Autowired
    private LoginDataBean loginDataBean;

    @Autowired
    private HkRapCalcService calcService;

    public void save(HkCalcRateDetailDocument document) {
//        Map<String, String> dbFieldMapFromFieldValue = CalcPlanTransformer.getDbFieldMapFromFieldValue(document.getFieldValue());
//        BasicBSONObject makeBSONObject = customFieldService.makeBSONObject(document.getFieldValue(), dbFieldMapFromFieldValue, HkSystemConstantUtil.Feature.PLAN, loginDataBean.getCompanyId(), null);
//        document.setFieldValue(makeBSONObject);
        calcService.save(document);
    }

    private boolean isValidGroup(List<String> longDiscountKeyList, Map<String, RangeDocument> toMap) {
        for (Map.Entry<String, RangeDocument> entrySet : toMap.entrySet()) {
            String key = entrySet.getKey();
            if (!longDiscountKeyList.contains(key)) {
                return false;
            }
        }
        return true;
    }

    public void calculateAmount(CalcRateDetailDatabean calcRateDetailDatabean) {
        Map<String, Object> fourCMap = calcRateDetailDatabean.getFourCMap();
        HkCalcBasPriceDocument baseBasPriceDocument = calcService.retrieveCalcBasePriceDocument((double) fourCMap.get(""), (Long) fourCMap.get(""), (Long) fourCMap.get(""), (Long) fourCMap.get(""));

        if (baseBasPriceDocument != null) {
            List<HkCalcRateDetailDocument> calcRateDetailDocuments = calcService.retrieveCalcRateDetailDocument(calcRateDetailDatabean.getDiscountDetailsMap());
            if (!CollectionUtils.isEmpty(calcRateDetailDocuments)) {
                for (HkCalcRateDetailDocument calcRateDetailDocument : calcRateDetailDocuments) {
                    Map<String, RangeDocument> toMap = calcRateDetailDocument.getDiscountDetailsMap();
                    Map<String, Object> discountDetailsMap = calcRateDetailDatabean.getDiscountDetailsMap();
//                    if (isValidGroup(discountDetailsMap, toMap)) {
//                        Double discount = calcRateDetailDocument.getDiscount();
//                        //Calculate discount here.
//                    }
                }
            }
//            Map<String, Map<Long, Double>> sourceDiscountDetailsMap = rateDetailDocument.getDiscountDetailsMap();
//            Map<String, Long> discountDetailsMap = calcRateDetailDatabean.getDiscountDetailsMap();
//            for (Map.Entry<String, Long> entrySet : discountDetailsMap.entrySet()) {
//                String key = entrySet.getKey();
//                Long value = entrySet.getValue();
//                Map<Long, Double> discountMap = sourceDiscountDetailsMap.get(key);
//                Double discount = discountMap.get(value);
//                if (discount != null) {
//                    //calculate total discount
//                }
//            }
        }

    }

    /**
     * This method calculates graph and expected diamond price(base price ,mix
     * price and discount)
     *
     * @author prabhat
     * @param calcRateDetailDatabean
     */
    public CalcRateDetailDatabean calculateDiamondPrice(CalcRateDetailDatabean calcRateDetailDatabean) {
        //price calculation
        Map<String, Object> fourCMap = calcRateDetailDatabean.getFourCMap();
        HkCalcBasPriceDocument basePriceDocument = calcService.retrieveCalcBasePriceDocument(Double.valueOf(fourCMap.get(HkSystemConstantUtil.FourC.CARAT).toString()), Long.valueOf(fourCMap.get(HkSystemConstantUtil.FourC.SHAPE).toString()), Long.valueOf(fourCMap.get(HkSystemConstantUtil.FourC.QUALITY).toString()), Long.valueOf(fourCMap.get(HkSystemConstantUtil.FourC.COLOR).toString()));
        HkCalcFourCDiscountDocument fourCDiscountDocument = calcService.retrieveFourCDiscountDocument(Double.valueOf(fourCMap.get(HkSystemConstantUtil.FourC.CARAT).toString()), Long.valueOf(fourCMap.get(HkSystemConstantUtil.FourC.SHAPE).toString()), Long.valueOf(fourCMap.get(HkSystemConstantUtil.FourC.QUALITY).toString()), Long.valueOf(fourCMap.get(HkSystemConstantUtil.FourC.COLOR).toString()));
        List<HkCalcRateDetailDocument> calcRateDetailDocuments = calcService.retrieveCalcRateDetailDocument(calcRateDetailDatabean.getDiscountDetailsMap());
        CalcRateDetailDatabean calculateAmount = calculateAmount(basePriceDocument, calcRateDetailDocuments, calcRateDetailDatabean.getDiscountDetailsMap(), calcRateDetailDatabean, false, fourCDiscountDocument);

        //graph price calculation
        Map<String, Object> graphFourCMap = calcRateDetailDatabean.getGraphFourCMap();
        if (fourCMap.get(HkSystemConstantUtil.FourC.GRAPH_CARAT) != null) {
            HkCalcBasPriceDocument graphBasePriceDocument = calcService.retrieveCalcBasePriceDocument(Double.valueOf(fourCMap.get(HkSystemConstantUtil.FourC.GRAPH_CARAT).toString()), Long.valueOf(fourCMap.get(HkSystemConstantUtil.FourC.SHAPE).toString()), Long.valueOf(fourCMap.get(HkSystemConstantUtil.FourC.QUALITY).toString()), Long.valueOf(fourCMap.get(HkSystemConstantUtil.FourC.COLOR).toString()));
            HkCalcFourCDiscountDocument graphFourCDiscountDocument = calcService.retrieveFourCDiscountDocument(Double.valueOf(fourCMap.get(HkSystemConstantUtil.FourC.CARAT).toString()), Long.valueOf(fourCMap.get(HkSystemConstantUtil.FourC.SHAPE).toString()), Long.valueOf(fourCMap.get(HkSystemConstantUtil.FourC.QUALITY).toString()), Long.valueOf(fourCMap.get(HkSystemConstantUtil.FourC.COLOR).toString()));
            if (graphBasePriceDocument != null) {
                List<HkCalcRateDetailDocument> graphCalcRateDetailDocuments = calcService.retrieveCalcRateDetailDocument(calcRateDetailDatabean.getDiscountDetailsMap());
                return calculateAmount(graphBasePriceDocument, graphCalcRateDetailDocuments, calcRateDetailDatabean.getGraphDiscountDetailsMap(), calcRateDetailDatabean, true, graphFourCDiscountDocument);
            }
        }
        System.out.println("BasePrice Document:: " + basePriceDocument);

        System.out.println("CALCULATE AMOUNT:: " + calculateAmount);
        return calculateAmount;
    }

    /**
     * This method performs actual calculation for graph and expected diamond
     * price(base price ,mix price and discount)
     *
     * @author prabhat
     *
     * @param basePriceDocument
     * @param calcRateDetailDocuments
     * @param discountDetailsMap
     * @param targetCalcRateDetailDatabean reference to response
     *                                     CalcRateDetailDatabean
     * @param isGraphCalc                  true indicates graph calculation
     */
    private CalcRateDetailDatabean calculateAmount(
            HkCalcBasPriceDocument basePriceDocument,
            List<HkCalcRateDetailDocument> calcRateDetailDocuments,
            Map<String, Object> discountDetailsMap,
            CalcRateDetailDatabean targetCalcRateDetailDatabean,
            boolean isGraphCalc,
            HkCalcFourCDiscountDocument fourCDiscountDocument) {
        System.out.println("FOURCDISCOUNTDOCUMENT: " + fourCDiscountDocument);
        if (basePriceDocument != null) {
          
            double totalDiscount = 0.00;
            if (fourCDiscountDocument != null) {
              
                totalDiscount = fourCDiscountDocument.getDiscount();
            }

            double totalMixAmount = 0.00;

            if (!CollectionUtils.isEmpty(calcRateDetailDocuments)) {
                List<String> discountDetailsKeys = new LinkedList<>();
                for (Map.Entry<String, Object> entrySet : discountDetailsMap.entrySet()) {
                    String key = entrySet.getKey();
                    String longKey = HkSystemConstantUtil.RAP_CALC_FIELD_MAP.get(key);
                    if (longKey != null) {
                        discountDetailsKeys.add(longKey);
                    }
                }
                List<String> groupNameList = new LinkedList<>();

                for (HkCalcRateDetailDocument calcRateDetailDocument : calcRateDetailDocuments) {
                    Map<String, RangeDocument> toMap = calcRateDetailDocument.getDiscountDetailsMap();
                    if (!groupNameList.contains(calcRateDetailDocument.getGroupName())) {
                        groupNameList.add(calcRateDetailDocument.getGroupName());
                        if (isValidGroup(discountDetailsKeys, toMap)) {
                            System.out.println("VALID CALCRATEDETAILDOCUMENT:: " + calcRateDetailDocument);
                            Double discount = calcRateDetailDocument.getDiscount();
                            Double mixAmount = calcRateDetailDocument.getMixamount();
                            //code to apply discount(NP = LP (1 - DR1 / 100) (1 - DR2 / 100) ... (1 - DRN / 100)   //NP = Net Price, LP = List Price, DR = dicount rate (%))
                            if (discount != null && discount != 0.0) {
                                totalDiscount += discount;
                            }
                            //code to calculate mix
                            if (mixAmount != null && mixAmount != 0.0) {
                                totalMixAmount += mixAmount;
                            }

                        }
                    }
                }
            }
            System.out.println( " dis: " + totalDiscount);
            //update CalcRateDetailDatabean with result
            if (isGraphCalc) {
                targetCalcRateDetailDatabean.setGraphAmount(basePriceDocument.getBasePrice() * (1 + (totalDiscount / 100.00)));
                targetCalcRateDetailDatabean.setGraphDiscount(totalDiscount);
                targetCalcRateDetailDatabean.setGraphBaseAmount(basePriceDocument.getBasePrice());
                targetCalcRateDetailDatabean.setGraphMixAmount(totalMixAmount);

            } else {
                targetCalcRateDetailDatabean.setAmount(basePriceDocument.getBasePrice() * (1 + (totalDiscount / 100.00)));
                targetCalcRateDetailDatabean.setBaseAmount(basePriceDocument.getBasePrice());
                targetCalcRateDetailDatabean.setDiscount(totalDiscount);
                targetCalcRateDetailDatabean.setMixAmount(totalMixAmount);
            }
        }
        targetCalcRateDetailDatabean.setDiscountDetailsMap(null);
        return targetCalcRateDetailDatabean;
    }

    /**
     * This method calculates off percentage
     *
     * @author prabhat
     *
     * @param originalPrice
     * @param offAmount
     * @return offPercentage
     */
    private double getOffPercent(Double originalPrice, Double offAmount) {
        double offPercentage = 0.0;
        if (originalPrice != null && offAmount != null && offAmount != 0.0 && originalPrice != 0.0) {
            offPercentage = (offAmount * 100.0) / originalPrice;
        }
        return offPercentage;
    }

}
