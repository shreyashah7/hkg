package com.argusoft.hkg.web.center.stock.controllers;

import com.argusoft.generic.database.exception.GenericDatabaseException;
import com.argusoft.hkg.common.functionutil.FolderManagement;
import static com.argusoft.hkg.common.functionutil.FolderManagement.basePath;
import com.argusoft.hkg.web.base.PrimaryKey;
import com.argusoft.hkg.web.base.ResponseCode;
import com.argusoft.hkg.web.base.ResponseEntity;
import com.argusoft.hkg.web.base.controllers.BaseController;
import com.argusoft.hkg.web.center.stock.databeans.PrintDataBean;
import com.argusoft.hkg.web.center.stock.transformers.PrintTransformer;
import com.argusoft.hkg.web.util.SelectItem;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.dynamicreports.report.exception.DRException;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author rajkumar
 */
@RestController
@RequestMapping("/print")
public class PrintController extends BaseController<PrintDataBean, Long> {

    @Autowired
    private PrintTransformer printTransformer;

    @Override
    public ResponseEntity<List<PrintDataBean>> retrieveAll() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ResponseEntity<PrintDataBean> retrieveById(PrimaryKey<Long> primaryKey) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ResponseEntity<PrimaryKey<Long>> update(PrintDataBean t) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ResponseEntity<PrimaryKey<Long>> create(PrintDataBean t) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ResponseEntity<PrintDataBean> deleteById(PrimaryKey<Long> primaryKey) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ResponseEntity<Map<String, Object>> retrievePrerequisite() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @RequestMapping(value = "/retrievesearcheddata", method = RequestMethod.POST, produces = {"application/json"})
    public ResponseEntity<List<SelectItem>> retrieveSearchedData(@RequestBody PrintDataBean printDataBean) {
        return new ResponseEntity<>(printTransformer.searchLot(printDataBean), ResponseCode.SUCCESS, "", null, true);
    }

    @RequestMapping(value = "/generatePrintData", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, String> generatePrintData(@RequestBody Map<String, Map<String, String>> payload, HttpServletResponse response, HttpServletRequest request) throws DRException, IOException {
        String fileName = printTransformer.generatePrintData(payload.get("payload"), payload.get("payload1"), payload.get("idToPrint"), response, request);
        if (!StringUtils.isEmpty(fileName)) {
            Map<String, String> result = new HashMap<>();
            result.put("filename", fileName);
            return result;
        }
        return null;
    }

    @RequestMapping(value = "/downloadPDFReport", method = RequestMethod.GET)
    @ResponseBody
    @ResponseStatus(value = HttpStatus.OK)
    public void getFile(@RequestParam("fileName") String fileName,HttpServletRequest request, HttpServletResponse response) {
        //System.out.println("fileName :"+fileName);
        printTransformer.downloadReport(fileName, ".pdf", request, response);

    }

    @RequestMapping(value = "/retrieveSearchedLotsAndPackets", method = RequestMethod.POST)
    public PrintDataBean retrieveSearchedLotsAndPackets(@RequestBody Map<String, List<String>> featureDbfieldNameMap) throws GenericDatabaseException {
        PrintDataBean result = printTransformer.retrieveSearchedLotsAndPacketsNew(featureDbfieldNameMap);
        return result;
    }

    @RequestMapping(value = "/retrievepacketbyid", method = RequestMethod.POST, produces = {"application/json"})
    public ResponseEntity<SelectItem> retrievePacketById(@RequestBody Map<String, List<String>> featureDbfieldNameMap) {
        SelectItem selectItem = printTransformer.retrievePacket(featureDbfieldNameMap);
        return new ResponseEntity<>(selectItem, ResponseCode.SUCCESS, "", null, true);
    }
}
