/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.center.stock.controllers;

import com.argusoft.generic.database.exception.GenericDatabaseException;
import com.argusoft.hkg.common.constantutil.HkSystemConstantUtil;
import com.argusoft.hkg.common.functionutil.FolderManagement;
import com.argusoft.hkg.web.center.stock.databeans.PrintBarcodeDataBean;
import com.argusoft.hkg.web.center.stock.transformers.PrintBarcodeTransformer;
import com.argusoft.hkg.web.center.util.GenerateBarcodeUtil;
import com.argusoft.hkg.web.util.SelectItem;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.dynamicreports.report.exception.DRException;
import net.sf.jasperreports.engine.JRException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author shreya
 */
@RestController
@RequestMapping("/printbarcode")
public class PrintBarcodeController {

    @Autowired
    PrintBarcodeTransformer printBarcodeTransformer;
    @Autowired
    GenerateBarcodeUtil generateBarcodeUtil;

    @RequestMapping(value = "/searchlotsorpackets", method = RequestMethod.POST, consumes = {"application/json"})
    public List<SelectItem> retrieveSearchedStock(@RequestBody PrintBarcodeDataBean printBarcodeDataBean) throws GenericDatabaseException {

        List<String> invoiceDbFieldName = new ArrayList<>();
        List<String> parcelDbFieldName = new ArrayList<>();
        List<String> lotDbFieldName = new ArrayList<>();
        List<String> packetDbFieldName = new ArrayList<>();
        List<String> sellDbFieldName = new ArrayList<>();
        List<String> transferDbFieldName = new ArrayList<>();
        for (Map.Entry<String, Object> entrySet : printBarcodeDataBean.getFeatureMap().entrySet()) {
            String dbFieldName = entrySet.getKey();
            Object feature = entrySet.getValue();
            if (feature.equals(HkSystemConstantUtil.Feature.LOT)) {
                lotDbFieldName.add(dbFieldName);
            } else if (feature.equals(HkSystemConstantUtil.Feature.INVOICE)) {
                invoiceDbFieldName.add(dbFieldName);
            } else if (feature.equals(HkSystemConstantUtil.Feature.PARCEL)) {
                parcelDbFieldName.add(dbFieldName);
            } else if (feature.equals(HkSystemConstantUtil.Feature.PACKET)) {
                packetDbFieldName.add(dbFieldName);
            } else if (feature.equals(HkSystemConstantUtil.Feature.SELL)) {
                sellDbFieldName.add(dbFieldName);
            } else if (feature.equals(HkSystemConstantUtil.Feature.TRANSFER)) {
                transferDbFieldName.add(dbFieldName);
            }
        }
        List<SelectItem> selectItems = printBarcodeTransformer.searchLotsOrPackets(printBarcodeDataBean, invoiceDbFieldName, lotDbFieldName, packetDbFieldName, parcelDbFieldName, sellDbFieldName, transferDbFieldName);
        return selectItems;
    }

    @RequestMapping(value = "/preparebarcode", method = RequestMethod.POST, consumes = {"application/json"})
    public String prepareBarcode(@RequestBody PrintBarcodeDataBean printBarcodeDataBean, HttpServletResponse response, HttpServletRequest request) throws GenericDatabaseException, IOException, DRException, FileNotFoundException, JRException {
        String result = "F";
        if (printBarcodeDataBean != null) {
            if (!CollectionUtils.isEmpty(printBarcodeDataBean.getFieldIds())) {
                result = printBarcodeTransformer.prepareBarcode(printBarcodeDataBean.getFieldIds(), response, request);
            }
        }
        return result;
    }

    @RequestMapping(value = "/printbarcode", method = RequestMethod.POST)
    public Map<String, String> printBarcode(@RequestBody String payload) throws IOException, FileNotFoundException, DRException, JRException {
        Map<String, String> tempFilePath = new HashMap<>();
        if (!StringUtils.isEmpty(payload)) {
            String filePath = printBarcodeTransformer.printBarcode(payload);
            tempFilePath.put("tempFilePath", filePath);
        }
        return tempFilePath;
    }

    @RequestMapping(value = "/downloadpdfofbarcodes", method = RequestMethod.GET)
    @ResponseBody
    @ResponseStatus(value = HttpStatus.OK)
    public void downloadPdfOfBarcodes(HttpServletRequest request, HttpServletResponse response) {
        String fileName = "Barcodes";
        generateBarcodeUtil.downloadReport(fileName, ".pdf", request, response);
    }

    @RequestMapping(value = "/getimage/{name}", method = RequestMethod.GET, produces = {"application/pdf"})
    @ResponseBody
    public FileSystemResource getFile(@PathVariable("name") String fileName, HttpServletRequest request) {
        if (fileName != null && !fileName.equals("")) {
            String pathFromImageName = FolderManagement.getOnlyPathOfFile(fileName + ".pdf");
            return new FileSystemResource(pathFromImageName);
        }
        return null;
    }

}
