package com.argusoft.hkg.web.pricelist.controllers;

import com.argusoft.hkg.web.base.PrimaryKey;
import com.argusoft.hkg.web.base.ResponseEntity;
import com.argusoft.hkg.web.base.controllers.BaseController;
import com.argusoft.hkg.web.pricelist.databeans.PriceListDataBean;
import com.argusoft.hkg.web.pricelist.transformers.PriceListTransformerBean;
import com.argusoft.hkg.web.util.TreeViewDataBean;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author rajkumar
 */
@RestController
@RequestMapping("/pricelist")
public class PriceListController extends BaseController<PriceListDataBean, Long> {

    @Autowired
    private PriceListTransformerBean priceListTransformerBean;

    @Override
    public ResponseEntity<List<PriceListDataBean>> retrieveAll() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ResponseEntity<PriceListDataBean> retrieveById(PrimaryKey<Long> primaryKey) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ResponseEntity<PrimaryKey<Long>> update(PriceListDataBean t) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ResponseEntity<PrimaryKey<Long>> create(PriceListDataBean t) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ResponseEntity<PriceListDataBean> deleteById(PrimaryKey<Long> primaryKey) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ResponseEntity<Map<String, Object>> retrievePrerequisite() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @RequestMapping(value = "downloadtemplate", method = RequestMethod.GET, produces = {"application/vnd.ms-excel"})
    @ResponseStatus(value = HttpStatus.OK)
    public void downloadTemplate(HttpServletResponse response) throws IOException {
        String filePath = priceListTransformerBean.downloadTemplate();
        FileSystemResource fileSystemResource = new FileSystemResource(filePath);

        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment; filename=PriceListXcel.xlsx");
        OutputStream out = response.getOutputStream();
//        out.write(fileSystemResource.getOutputStream());
        InputStream inputStream = fileSystemResource.getInputStream();
        
        byte[] buf = new byte[inputStream.available()];
        for (int readNum; (readNum = inputStream.read(buf)) != -1;) {
            out.write(buf, 0, readNum); //no doubt here is 0
            //Writes len bytes from the specified byte array starting at offset off to this byte array output stream.
        }

        response.flushBuffer();
        response.getOutputStream().flush();
        response.getOutputStream().close();
        out.flush();
        out.close();
    }

    @RequestMapping(value = "uploadpricelist", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity uploadpricelist(@RequestBody MultipartFile file, @RequestParam("flowFilename") String fileName, @RequestParam("model") String modelName, HttpServletRequest httpRequest) throws IOException {
        Map<String, String[]> parameterMap = httpRequest.getParameterMap();
        String[] chunkNumber = parameterMap.get("flowChunkNumber");
        String[] flowTotalChunks = parameterMap.get("flowTotalChunks");
        return priceListTransformerBean.uploadPriceList(file, fileName, modelName, chunkNumber, flowTotalChunks);
    }

    @RequestMapping(value = "savepricelist", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    public void savePriceList(@RequestBody PriceListDataBean priceListDataBean) throws IOException {
        priceListTransformerBean.savePriceList(priceListDataBean);
    }

    @RequestMapping(value = "retrieveallpricelist", method = RequestMethod.POST)
    public List<TreeViewDataBean> retrieveAllPriceList() {
        return priceListTransformerBean.retrievePriceLists();
    }

    @RequestMapping(value = "retrievepricelistByMonthYear", method = RequestMethod.POST)
    public List<PriceListDataBean> retrievepricelistByMonthYear(@RequestBody TreeViewDataBean treeViewDataBean) {
        return priceListTransformerBean.retrievepricelistByMonthYear(treeViewDataBean);
    }

}
