/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.common.controllers;

import com.argusoft.hkg.web.base.ResponseCode;
import com.argusoft.hkg.web.base.ResponseEntity;
import com.argusoft.hkg.web.center.usermanagement.databeans.LoginDataBean;
import com.argusoft.hkg.common.functionutil.FolderManagement;
import com.argusoft.hkg.web.center.fileupload.transformers.FileUploadTransformerBean;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.codec.binary.Base64;
import org.json.JSONArray;
import org.json.JSONException;
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
 * @author ravi
 */
@RestController
@RequestMapping("/fileUpload")
public class FileUploadController {

    @Autowired
    FileUploadTransformerBean fileUploadTransformerBean;
    @Autowired
    LoginDataBean hkLoginDataBean;
  

    /**
     *
     * @param file
     * @param fileName
     * @param modelName need to be sent using query parameter
     * @param httpRequest
     * @throws FileNotFoundException
     * @throws IOException
     */
    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    public void uploadFile(@RequestBody MultipartFile file, @RequestParam("flowFilename") String fileName, @RequestParam("model") String modelName, HttpServletRequest httpRequest) throws FileNotFoundException, IOException {
        Map<String, String[]> parameterMap = httpRequest.getParameterMap();
        String[] chunkNumber = parameterMap.get("flowChunkNumber");
        String[] flowTotalChunks = parameterMap.get("flowTotalChunks");
        fileUploadTransformerBean.uploadFile(file, fileName, modelName, chunkNumber, flowTotalChunks);

    }
    /**
     * Method to be used to upload web camera snapshot.
     * @param requestMap
     * @param httpRequest
     * @throws FileNotFoundException
     * @throws IOException 
     */
    @RequestMapping(value = "/uploadWebcamFile", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    public void uploadWebcamFile(@RequestBody Map<String, String> requestMap, HttpServletRequest httpRequest) throws FileNotFoundException, IOException {
        Map<String, String[]> parameterMap = httpRequest.getParameterMap();
        //Total chunk will be always 1.
        String[] chunkNumber = new String[]{"1"};
        String[] flowTotalChunks = new String[]{"1"};
        String file = requestMap.get("file");
        String modelName = requestMap.get("modelName");
        String fileName = requestMap.get("fileName");
        file = file.substring("data:image/jpeg;base64,"
                .length());
        byte[] newbytes = Base64.decodeBase64(file.getBytes());
        fileUploadTransformerBean.uploadWebcamFile(newbytes, fileName, modelName, chunkNumber, flowTotalChunks);

    }
    /**
     * A common method to be used by web-cam directive for all non-diamond features to get temp file name.
     * @param requestParams
     * @return String of file name.
     * @throws IOException 
     */
    @RequestMapping(value = "/getTempFileName", method = RequestMethod.POST, consumes = {"application/json"})
    public ResponseEntity<String> getTempFileName(@RequestBody String[] requestParams) throws IOException {
        String tempFileName = null;
        String fileName = requestParams[0];
        String fileType = null;
        String featureName = null;
        if (requestParams.length > 1) {
            fileType = requestParams[1];
        }
        if (requestParams.length > 2) {
            featureName = requestParams[2];
        }
        tempFileName = FolderManagement.getTempFileName(hkLoginDataBean.getCompanyId(), featureName, fileType, hkLoginDataBean.getId(), fileName);
        return new ResponseEntity<>(tempFileName, ResponseCode.SUCCESS, "", null, null, false);
    }

    /**
     *
     * @param info [filename,modelname]
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     */
    @RequestMapping(value = "/onsubmit", method = RequestMethod.POST)
    public ResponseEntity<Map> onSubmit(@RequestBody String[] info) throws FileNotFoundException, IOException {
        int length = info.length;
        Integer imgWidth = null;
        Integer imgHeight = null;
        String nameOfFile = "";
        String thumbnail = "";
        String fileName = info[0];
        String modelName = info[1];
        if (length > 2) {
            nameOfFile = info[2];
            thumbnail = info[3];
        }
        if (length > 4) {
            imgWidth = Integer.parseInt(info[4]);
            imgHeight = Integer.parseInt(info[5]);
        }
        System.out.println("$$NAme "+nameOfFile);
        String fileSubmited = fileUploadTransformerBean.onSubmit(fileName, modelName, nameOfFile, thumbnail, imgWidth, imgHeight);
        Map result = new HashMap();
        result.put("res", fileSubmited);
        return new ResponseEntity<>(result, ResponseCode.SUCCESS, "", null);
    }

    @RequestMapping(value = "/getimage", method = RequestMethod.GET)
    @ResponseBody
    public FileSystemResource getFile(@RequestParam("file_name") String fileName) {
        if (fileName != null && !fileName.equals("")) {
            String pathFromImageName = FolderManagement.getPathOfImage(fileName);
            return new FileSystemResource(pathFromImageName);

        }
        return null;
    }

    /**
     *
     * @param info [filename,modelname]
     * @throws FileNotFoundException
     * @throws IOException
     */
    @RequestMapping(value = "/oncancel", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    public void onCancel(@RequestBody String[] info) throws FileNotFoundException, IOException {
        String fileName = info[0];
        String modelName = info[1];
        Boolean onCancel = fileUploadTransformerBean.onCancel(fileName, modelName);
    }

    @RequestMapping(value = "/removeImageFile", method = RequestMethod.POST, consumes = {"application/json"})
    @ResponseStatus(value = HttpStatus.OK)
    public void removeImageFile(@RequestBody String name) throws IOException {
        FolderManagement.removeFile(name);
    }

    /**
     * to remove all files from server.
     *
     * @param info array of fileList and model
     * @throws FileNotFoundException
     * @throws IOException
     */
    @RequestMapping(value = "/oncancelall", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    public void onCancelAll(@RequestBody String[] info) throws FileNotFoundException, IOException {
        String modelName = info[info.length - 1];
        List<String> fileList = new ArrayList<>();
        for (int i = 0; i < (info.length - 1); i++) {
            fileList.add(info[i]);
        }

        Boolean onCancelAll = fileUploadTransformerBean.onCancelAll(modelName, fileList);
    }

    /**
     *
     * @param response
     * @param httpRequest contains name of file to be downloaded in parameter
     * filename.
     */
    @RequestMapping(value = "/download", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public void download(HttpServletResponse response, HttpServletRequest httpRequest) {
        Map<String, String[]> parameterMap = httpRequest.getParameterMap();
        String[] fileNames = parameterMap.get("filename");
        String fileName = fileNames[0];
        try {
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment;filename =" + fileName.substring(fileName.lastIndexOf("@") + 1, fileName.length()));
            String pathFromImageName = FolderManagement.getPathOfImage(fileName);
            Path pathVarible = Paths.get(pathFromImageName);
            byte[] data = Files.readAllBytes(pathVarible);
            response.getOutputStream().write(data);
        } catch (IOException e) {
        }
    }

    /**
     *
     * @param response
     * @param httpRequest contains array of fileName to be downloaded in
     * parameter filenames.
     * @throws JSONException
     */
    @RequestMapping(value = "/downloadAll", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public void downloadAll(HttpServletResponse response, HttpServletRequest httpRequest) throws JSONException {
        Map<String, String[]> parameterMap = httpRequest.getParameterMap();

        String[] fileNames = parameterMap.get("filenames");
        String ArrayOfFiles = fileNames[0];
        ArrayList<String> listOfFiles = new ArrayList<String>();
        JSONArray jsonArray = new JSONArray(ArrayOfFiles);
        if (jsonArray != null) {
            int len = jsonArray.length();
            for (int i = 0; i < len; i++) {
                listOfFiles.add(jsonArray.get(i).toString());
            }
        }

        try {
            try (FileOutputStream fos = new FileOutputStream("data.zip"); ZipOutputStream zos = new ZipOutputStream(fos)) {
                for (String fileName : listOfFiles) {
                    addToZipFile(fileName, zos);
                }
            }
        } catch (IOException e) {
        }
        try {
            String fileName = "data.zip";
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment;filename =" + fileName.substring(fileName.indexOf(FolderManagement.NAME_SEPARATOR) + 1, fileName.length()));
            Path path = Paths.get(fileName);
            byte[] data = Files.readAllBytes(path);
            response.getOutputStream().write(data);
        } catch (IOException e) {
        }
    }

    public void addToZipFile(String fileName, ZipOutputStream zos) throws FileNotFoundException, IOException {

        String shortFilename;
//        try {
//            shortFilename = fileName.substring(fileName.lastIndexOf("@") + 1);
//        } catch (Exception e) {
//            shortFilename = fileName;
//        }

        String pathFromImageName = FolderManagement.getPathOfImage(fileName);
        File file = new File(pathFromImageName);

//        File file = new File(fileName);
        try (FileInputStream fis = new FileInputStream(file)) {
            ZipEntry zipEntry = new ZipEntry(fileName);
            zos.putNextEntry(zipEntry);

            byte[] bytes = new byte[1024];
            int length;
            while ((length = fis.read(bytes)) >= 0) {
                zos.write(bytes, 0, length);
            }

            zos.closeEntry();
        }
    }

}
