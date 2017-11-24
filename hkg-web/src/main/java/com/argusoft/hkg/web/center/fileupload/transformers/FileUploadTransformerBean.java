/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.center.fileupload.transformers;

import com.argusoft.hkg.common.constantutil.HkSystemConstantUtil;
import com.argusoft.hkg.common.functionutil.FolderManagement;
import static com.argusoft.hkg.common.functionutil.FolderManagement.DOT_SEPARATOR;
import com.argusoft.hkg.core.HkFoundationService;
import com.argusoft.hkg.core.HkHRService;
import com.argusoft.hkg.web.center.usermanagement.databeans.LoginDataBean;
import static com.argusoft.hkg.common.functionutil.FolderManagement.TEMP;
import static com.argusoft.hkg.common.functionutil.FolderManagement.checkIsExists;
import com.argusoft.hkg.web.usermanagement.wrapper.UserManagementServiceWrapper;
import com.argusoft.hkg.web.util.SessionUtil;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;
import org.imgscalr.Scalr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author ravi
 */
@Service
public class FileUploadTransformerBean {

    @Autowired
    LoginDataBean loginDatabean;

    private String stringContainsItemFromList(String inputString, String[] items) {

        for (int i = 0; i < items.length; i++) {
            if (inputString.contains(items[i])) {
                inputString = inputString.replace(items[i], "");
            }
        }
        return inputString;
    }

    public Boolean uploadFile(MultipartFile file, String fileName, String modelName, String[] chunkNumber, String[] flowTotalChunks) throws FileNotFoundException, IOException {
        // System.out.println("###Name --"+file.getOriginalFilename());
        System.out.println("###Name1 --" + fileName);
        //    System.out.println("###Name2 --"+modelName);
        fileName = stringContainsItemFromList(fileName, HkSystemConstantUtil.AVOID_CHARACTERS);
        System.out.println("After filename : " + fileName);
        String mapOfFilesKey = System.getProperties().getProperty("user.name") + ")~)" + modelName;
        String mapOfChunksKey = modelName + ")~)" + fileName;
        String chunkName = (System.getProperties().getProperty("user.name") + ")~)" + fileName + ")~)" + chunkNumber[0] + ")~)" + new Date().getTime());

        Map<String, Map<String, String[]>> mapOfFiles = loginDatabean.getMapOfFiles();

        if (mapOfFiles == null) {
            mapOfFiles = new HashMap<>();
            mapOfFiles.put(mapOfFilesKey, new HashMap<String, String[]>());
        }

        //newCode        
        int numberOfChunks = Integer.parseInt(flowTotalChunks[0]);
        Map<String, String[]> mapOfChunks = mapOfFiles.get(mapOfFilesKey);

        if (mapOfChunks == null) {
            mapOfChunks = new HashMap<>();
        }

        if (mapOfChunks.containsKey(mapOfChunksKey)) {
            String[] listofRelatedChunks = mapOfChunks.get(mapOfChunksKey);
            int chunkNum = Integer.parseInt(chunkNumber[0]);
            int index = chunkNum - 1;
            listofRelatedChunks[index] = chunkName;
            mapOfChunks.put(mapOfChunksKey, listofRelatedChunks);
            mapOfFiles.put(mapOfFilesKey, mapOfChunks);
            loginDatabean.setMapOfFiles(mapOfFiles);
        } else {
            String[] listofRelatedChunks;
            listofRelatedChunks = new String[numberOfChunks];
            int chunkNum = Integer.parseInt(chunkNumber[0]);
            int index = chunkNum - 1;
            listofRelatedChunks[index] = chunkName;
            mapOfChunks.put(mapOfChunksKey, listofRelatedChunks);
            mapOfFiles.put(mapOfFilesKey, mapOfChunks);
            loginDatabean.setMapOfFiles(mapOfFiles);
        }
        File chunkFile = new File(chunkName);
        FileOutputStream fos = new FileOutputStream(chunkFile);
        fos.write(file.getBytes());
        fos.close();
        return true;
    }

    //Same method as above but takes input of byte array rather than multipart. Used for web-cam upload.
    public Boolean uploadWebcamFile(byte[] file, String fileName, String modelName, String[] chunkNumber, String[] flowTotalChunks) throws FileNotFoundException, IOException {
        String mapOfFilesKey = System.getProperties().getProperty("user.name") + ")~)" + modelName;
        String mapOfChunksKey = modelName + ")~)" + fileName;
        String chunkName = (System.getProperties().getProperty("user.name") + ")~)" + fileName + ")~)" + chunkNumber[0] + ")~)" + new Date().getTime());

        Map<String, Map<String, String[]>> mapOfFiles = loginDatabean.getMapOfFiles();

        if (mapOfFiles == null) {
            mapOfFiles = new HashMap<>();
            mapOfFiles.put(mapOfFilesKey, new HashMap<String, String[]>());
        }

        //newCode        
        int numberOfChunks = Integer.parseInt(flowTotalChunks[0]);
        Map<String, String[]> mapOfChunks = mapOfFiles.get(mapOfFilesKey);

        if (mapOfChunks == null) {
            mapOfChunks = new HashMap<>();
        }

        if (mapOfChunks.containsKey(mapOfChunksKey)) {
            String[] listofRelatedChunks = mapOfChunks.get(mapOfChunksKey);
            int chunkNum = Integer.parseInt(chunkNumber[0]);
            int index = chunkNum - 1;
            listofRelatedChunks[index] = chunkName;
            mapOfChunks.put(mapOfChunksKey, listofRelatedChunks);
            mapOfFiles.put(mapOfFilesKey, mapOfChunks);
            loginDatabean.setMapOfFiles(mapOfFiles);
        } else {
            String[] listofRelatedChunks;
            listofRelatedChunks = new String[numberOfChunks];
            int chunkNum = Integer.parseInt(chunkNumber[0]);
            int index = chunkNum - 1;
            listofRelatedChunks[index] = chunkName;
            mapOfChunks.put(mapOfChunksKey, listofRelatedChunks);
            mapOfFiles.put(mapOfFilesKey, mapOfChunks);
            loginDatabean.setMapOfFiles(mapOfFiles);
        }
        File chunkFile = new File(chunkName);
        FileOutputStream fos = new FileOutputStream(chunkFile);
        fos.write(file);
        fos.close();
        return true;
    }

    public Boolean onCancel(String fileName, String modelName) throws IOException {
        String mapOfFilesKey = System.getProperties().getProperty("user.name") + ")~)" + modelName;

        Map<String, Map<String, String[]>> mapOfFiles = loginDatabean.getMapOfFiles();

        Boolean flagForRemovel = false;
        if (mapOfFiles.containsKey(mapOfFilesKey)) {
            Map<String, String[]> mapOfChunks = mapOfFiles.get(mapOfFilesKey);
            String mapOfChunksKey = modelName + ")~)" + fileName;
            if (mapOfChunks.containsKey(mapOfChunksKey)) {
                String[] filesToRemove = mapOfChunks.get(mapOfChunksKey);
                for (int i = 0; i < filesToRemove.length; i++) {
                    String chunkName = filesToRemove[i];
                    if (chunkName != null) {
                        File fileToremove = new File(chunkName);
                        if (fileToremove.exists()) {
                            fileToremove.delete();
                        }
                    }
                }
                mapOfChunks.remove(mapOfChunksKey);
            } else {
                flagForRemovel = true;
            }
        } else {
            flagForRemovel = true;
        }
        if (flagForRemovel) {
            File fileToremove = new File(fileName);
            if (fileToremove.exists()) {
                fileToremove.delete();
            }
        }

        return true;

    }

    public String onSubmit(String fileName, String modelName, String nameOfFile, String thumbnail, Integer imgWidth, Integer imgHeight) throws IOException {
        fileName = stringContainsItemFromList(fileName, HkSystemConstantUtil.AVOID_CHARACTERS);
        String mapOfFilesKey = System.getProperties().getProperty("user.name") + ")~)" + modelName;
        String mapOfChunksKey = modelName + ")~)" + fileName;
        Map<String, Map<String, String[]>> mapOfFiles1 = loginDatabean.getMapOfFiles();
        if (mapOfFiles1 != null) {
            Map<String, String[]> mapOfChunks1 = mapOfFiles1.get(mapOfFilesKey);
            if (mapOfChunks1 != null) {
                String[] listOfChunks = mapOfChunks1.get(mapOfChunksKey);

                if (nameOfFile.isEmpty()) {
                    nameOfFile = new Date().getTime() + "@" + mapOfChunksKey;
                }

                if (listOfChunks != null) {
                    File tempDir = checkIsExists(FolderManagement.getBasePath(), TEMP, null);
                    StringBuilder filePath = new StringBuilder(tempDir.getPath());
                    filePath.append(File.separator).append(nameOfFile);
                    File createFile = new File(filePath.toString());

                    boolean success = true;
                    FileOutputStream fos1 = new FileOutputStream(createFile);
                    for (String chunkNames : listOfChunks) {
                        if (chunkNames != null) {
                            try {
                                Path path = Paths.get(chunkNames);
                                byte[] data = Files.readAllBytes(path);
                                try {
                                    fos1.write(data);
                                } catch (Exception e) {
                                }
                                File fileToremove = new File(chunkNames);
                                if (fileToremove.exists()) {
                                    fileToremove.delete();
                                }
                            } catch (Exception e) {
                                success = false;
                                break;
                            }
                        } else {
                            success = false;
                            break;
                        }
                    }
                    if (imgHeight != null && imgWidth != null) {
                        BufferedImage image = ImageIO.read(createFile);
//        BufferedImage thumbnail = Scalr.resize(image, 150);
                        BufferedImage imgFixWidth
                                = Scalr.resize(image, Scalr.Method.AUTOMATIC, Scalr.Mode.FIT_EXACT,
                                        imgWidth, imgHeight, Scalr.OP_ANTIALIAS);
                        String[] split = filePath.toString().split(DOT_SEPARATOR);
                        ImageIO.write(imgFixWidth, "png", new File(split[0] + ".png"));
                    }

                    if (success) {
                        if (!thumbnail.isEmpty() && thumbnail.equals("true")) {
                            FolderManagement.createThumbnail(createFile, filePath.toString());
                        }
                        return nameOfFile;
                    } else {
                        File fileToremove = new File(nameOfFile);
                        if (fileToremove.exists()) {
                            fileToremove.delete();
                        }

                        return null;
                    }
                }
            }
        }

        return nameOfFile;
    }

    public Boolean onCancelAll(String modelName, List<String> fileList) {
        String mapOfFilesKey = System.getProperties().getProperty("user.name") + ")~)" + modelName;
        Map<String, Map<String, String[]>> mapOfFiles = loginDatabean.getMapOfFiles();

        if (mapOfFiles.containsKey(mapOfFilesKey)) {
            Map<String, String[]> mapOfChunks = mapOfFiles.get(mapOfFilesKey);
            if (mapOfChunks != null) {
                for (Map.Entry<String, String[]> entry : mapOfChunks.entrySet()) {
                    String[] filesToRemove = entry.getValue();
                    for (int i = 0; i < filesToRemove.length; i++) {
                        String chunkName = filesToRemove[i];
                        if (chunkName != null) {
                            File fileToremove = new File(chunkName);
                            if (fileToremove.exists()) {
                                fileToremove.delete();
                            }
                        }
                    }
                }
            }
        }
        if (fileList != null && (fileList.size() > 0)) {
            for (String fileNameToremove : fileList) {
                if (fileNameToremove != null) {
                    File fileToremove = new File(fileNameToremove);
                    if (fileToremove.exists()) {
                        fileToremove.delete();
                    }
                }
            }
        }
        return true;

    }
}
