/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.common.functionutil;

import com.argusoft.hkg.common.constantutil.HkSystemConstantUtil;
import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import org.springframework.util.StringUtils;

/**
 *
 * @author mansi
 * <br/>saveFile(null, null, null, null, temp);pass only filename in list: it
 * will search for file in temp and move to folder structure.
 * <br/>saveFile(new Long("2"), FEATURE.USER, new Long("3"), map, null);pass all
 * the details and it will store file directly in folder structure.
 * <br/>retrieveMediaFiles(new Long("2"), FEATURE.EVENT, new Long("1"),
 * subfeature1);retrieve files of feature
 * <br/>removeFiles(new Long("2"), FEATURE.USER, new Long("3"), null);remove
 * files of feature, pass list if you want to delete only those files.
 *
 */
public class FolderManagement {

    public static String basePath = System.getProperty("user.home");
    public static final String TEMP = "TEMP";
    public static String UNIQUE_SEPARATOR = "~@";
    public static String NAME_SEPARATOR = "~~";
    public static String DOT_SEPARATOR = "\\.";
    private static int thumbWidth = 96;
    private static int thumbHeight = 96;

    public static final class FEATURE {

        public static final String FRANCHISE = "FRANCHISE";
        public static final String ASSET = "ASSET";
        public static final String EVENT = "EVENT";
        public static final String USER = "USER";
        public static final String COMMON = "COMMON";
    }

    public static final class CONSTANT {

        public static final String BANNER = "BANNER";
        public static final String BACKGROUND = "BACKGROUND";
        public static final String GALLERY = "GALLERY";
        public static final String PROFILE = "PROFILE";
        public static final String SALARYSLIP = "SALARYSLIP";
        public static final String OTHER = "OTHER";
    }

    public static String getBasePath() {
        return basePath;
    }

    public static void createBaseFolder() {
        String fileName = "jdbc.properties";

        PropertyFileReader propertyFileReader = new PropertyFileReader();
        String folderName = propertyFileReader.getPropertyValue("repository.folder.name", fileName);
        basePath = System.getProperty("user.home") + File.separator + folderName;
        File dir = new File(basePath);
        if (!dir.exists()) {
            dir.mkdir();
        }
        checkIsExists(basePath, FEATURE.FRANCHISE, null);
    }

    public static String getTempFileName(Long franchiseId, String feature, String subFeature, Object loginUserId, String filename) throws IOException {
        String randomName = getRandomName();
        StringBuilder random = new StringBuilder();
        random.append(feature).append(UNIQUE_SEPARATOR).append(franchiseId).append(UNIQUE_SEPARATOR);
        if (!feature.equalsIgnoreCase(FEATURE.COMMON)) {
            random.append(loginUserId).append(UNIQUE_SEPARATOR);
        }
        if (subFeature != null) {
            random.append(subFeature).append(UNIQUE_SEPARATOR);
        }
        random.append(randomName);
        if (!StringUtils.isEmpty(filename)) {
            random.append(NAME_SEPARATOR).append(filename);
        }
        return random.toString();
    }

    public static String getTempFileNameCustom(Long franchiseId, String feature, String subFeature, Object loginUserId, String filename) throws IOException {
        StringBuilder random = new StringBuilder();
        random.append(feature).append(UNIQUE_SEPARATOR).append(franchiseId).append(UNIQUE_SEPARATOR);
        if (!feature.equalsIgnoreCase(FEATURE.COMMON) && loginUserId != null) {
            random.append(loginUserId).append(UNIQUE_SEPARATOR);
        }
        if (subFeature != null) {
            random.append(subFeature).append(UNIQUE_SEPARATOR);
        }
        random.append(NAME_SEPARATOR).append(filename);
        return random.toString();
    }

    public static void storeFileInTemp(String filename, byte[] data, boolean createThumbnail) throws IOException {
        File tempDir = checkIsExists(basePath, TEMP, null);
        StringBuilder filePath = new StringBuilder(tempDir.getPath());
        filePath.append(File.separator).append(filename.toString());
        writeByteData(filePath.toString(), data, createThumbnail);
    }

    public static String getPathOfImage(String imageName) {
        StringBuilder tempPath = new StringBuilder(basePath);
        tempPath.append(File.separator).append(TEMP).append(File.separator).append(imageName);
        File file = new File(tempPath.toString());
        if (file.exists()) {
            return tempPath.toString();
        } else {
            StringBuilder path = new StringBuilder();
            String[] split = imageName.split(UNIQUE_SEPARATOR);
            if (split.length > 1) {
//                path.append(basePath).append(File.separator).append(FEATURE.FRANCHISE).append(File.separator).append(split[1]).append(File.separator).append(split[0]);
//                if (!split[0].equalsIgnoreCase(FEATURE.COMMON)) {
//                    path.append(File.separator).append(split[2]);
//                }
//                if (split.length > 4) {
//                    path.append(File.separator).append(split[3]);
//                }
//                path.append(File.separator).append(imageName);
//                return path.toString();

                if (!split[0].equalsIgnoreCase(HkSystemConstantUtil.Feature.LOT)
                        && !split[0].equalsIgnoreCase(HkSystemConstantUtil.Feature.PARCEL)
                        && !split[0].equalsIgnoreCase(HkSystemConstantUtil.Feature.PACKET)
                        && !split[0].equalsIgnoreCase(HkSystemConstantUtil.Feature.INVOICE)) {
                    return getPathOfImageForCommonAndNonDiamond(split, path, imageName);
                } else {
                    return getPathOfImageForDiamond(split, path);
                }
            } else {
                return "";
            }
        }
    }

    public static String getOnlyPathOfFile(String fileName) {
        StringBuilder tempPath = new StringBuilder(basePath);
        tempPath.append(File.separator).append(TEMP).append(File.separator).append(fileName);
        File file = new File(tempPath.toString());
        if (file.exists()) {
            return tempPath.toString();
        } else {
            StringBuilder path = new StringBuilder();
            String[] split = fileName.split(UNIQUE_SEPARATOR);
            if (split.length > 1) {
                path.append(basePath).append(File.separator).append(FEATURE.FRANCHISE).append(File.separator).append(split[1]).append(File.separator).append(split[0]);
                if (!split[0].equalsIgnoreCase(FEATURE.COMMON)) {
                    path.append(File.separator).append(split[2]);
                }
                if (split.length > 4) {
                    path.append(File.separator).append(split[3]);
                }
                path.append(File.separator);
                return path.toString();
            } else {
                return "";
            }
        }
    }

    public static void removeFile(String imageName) throws IOException {
        String pathOfImage = getPathOfImage(imageName);
        File file = new File(pathOfImage);
        if (file.exists()) {
            Path path = Paths.get(pathOfImage.toString());
            path.toFile().delete();
            String[] split = pathOfImage.split(DOT_SEPARATOR);
            boolean isImage = false;
            File subFile = new File(split[0] + ".png");
            if (subFile.exists()) {
                Path subPath = Paths.get(split[0] + ".png");
                System.out.println("path :::" + subPath);
                subPath.toFile().delete();
            }
        }
        int i = imageName.lastIndexOf(".");
        String image = imageName.substring(0, imageName.length() - (imageName.length() - i));
        StringBuffer thumbnailImageName = new StringBuffer(image);
        thumbnailImageName.append("_T.jpg");
        String thumbnailPath = getPathOfImage(thumbnailImageName.toString());
        if (thumbnailPath != null) {
            File thumbnailfile = new File(thumbnailPath);
            if (thumbnailfile.exists()) {
                Path path1 = Paths.get(thumbnailPath.toString());
                path1.toFile().delete();
            }
        }
    }

    public static String changeFileName(String fileName, Object id) {
        String[] split = fileName.split(UNIQUE_SEPARATOR);
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < split.length; i++) {
            if (i == 2) {
                split[i] = id.toString();
            }
            builder.append(split[i]);
            if (i < split.length - 1) {
                builder.append(UNIQUE_SEPARATOR);
            }
        }
        return builder.toString();
    }

    public static void writeByteData(String path, byte[] data, boolean createThumbnail) throws FileNotFoundException, IOException {
        File createFile = new File(path);
        FileOutputStream fileOutputStream = new FileOutputStream(createFile);
        fileOutputStream.write(data);
        String[] split = path.split(DOT_SEPARATOR);
        boolean isImage = false;
        if (split[split.length - 1].equalsIgnoreCase("jpg") || split[split.length - 1].equalsIgnoreCase("png") || split[split.length - 1].equalsIgnoreCase("jpeg") || split[split.length - 1].equalsIgnoreCase("gif")|| split[split.length - 1].equalsIgnoreCase("bmp")) {
            isImage = true;
        }
        if (createThumbnail && isImage) {
            createThumbnail(createFile, path);
        }
        fileOutputStream.flush();
        fileOutputStream.close();
    }

    public static void createThumbnail(File createFile, String path) throws IOException {

        BufferedImage img = javax.imageio.ImageIO.read(createFile);
        int x2 = img.getWidth();
        int y2 = img.getHeight();
        int cropMargin = (int) Math.abs(Math.round(((img.getWidth() - img.getHeight()) / 2.0)));
//determine the crop coordinates
        int x1 = 0;
        int y1 = 0;
        if (x2 > y2) {
            x1 = cropMargin;
            x2 = x1 + y2;
        } else {
            y1 = cropMargin;
            y2 = y1 + x2;
        }
// call the crop method to get a square
        int type = img.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : img.getType();
        int nNewWidth = x2 - x1;
        int nNewHeight = y2 - y1;
        BufferedImage img2 = new BufferedImage(nNewWidth, nNewHeight, type);
        Graphics2D g = img2.createGraphics();

        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.setComposite(AlphaComposite.Src);

        g.drawImage(img, 0, 0, nNewWidth, nNewHeight, x1, y1, x2, y2, null);
        g.dispose();

        int nHeight = 50 * img.getHeight() / img.getWidth();

        String[] split = path.split(DOT_SEPARATOR);
        javax.imageio.ImageIO.write(img2, "JPG", new File(split[0] + "_T." + "jpg"));
    }

    public static String getRandomName() {
        long currentTimeMillis = System.currentTimeMillis();
        return String.valueOf(currentTimeMillis);
    }

    /**
     *
     * @param franchiseId
     * @param feature
     * @param featureId
     * @param map
     * @param tempfiles<br/>
     * pass franchiseId,feature,featureId and map<subfeature ,
     * map<filename,byte[]>> to store byte[] directly in folder structure<br/>
     * pass only files names in list, it will search for those files in temp,
     * and then move to folder structure
     */
    public static void saveFile(Long franchiseId, String feature, Long featureId, Map<String, Map<String, byte[]>> map, List<String> tempfiles, boolean createThumbnail) throws IOException {
        if (franchiseId != null) {
            File mainFranchiseDir = checkIsExists(basePath, FEATURE.FRANCHISE, null);
            File franchiseDir = checkIsExists(mainFranchiseDir.getPath(), null, franchiseId);
            if (feature != null && featureId != null && !map.isEmpty()) {
                File mainFeatureDir = checkIsExists(franchiseDir.getPath(), feature, null);
                File featureDir = checkIsExists(mainFeatureDir.getPath(), null, featureId);
                for (Map.Entry<String, Map<String, byte[]>> entry : map.entrySet()) {
                    String key = entry.getKey();
                    if (key != null && key.length() > 1) {
                        File subfeature = checkIsExists(featureDir.getPath(), key, null);
                        createFile(subfeature.getPath(), entry.getValue(), createThumbnail);
                    } else {
                        createFile(featureDir.getPath(), entry.getValue(), false);
                    }
                }
            }
        }
        if (tempfiles != null && tempfiles.size() > 0) {
            copyFilesFromTemp(tempfiles, featureId, createThumbnail);
        }
    }

    /**
     *
     * @param franchiseId
     * @param feature
     * @param featureId
     * @param map
     * @param tempfiles<br/>
     * move files from temp to folder structure, if file is of Event, it will
     * create thumbnail also.
     */
    public static void copyFilesFromTemp(List<String> tempfiles, Long featureId, boolean createThumbnail) throws IOException {
        for (int i = 0; i < tempfiles.size(); i++) {
            String string = tempfiles.get(i);
            StringBuilder tempPath = new StringBuilder(basePath);
            tempPath.append(File.separator).append(TEMP).append(File.separator).append(string);
            moveFile(tempPath.toString(), string, featureId, createThumbnail);
        }
    }

    public static void copyFilesCustom(String tempfile, String newName, Object featureId, boolean createThumbnail) throws IOException {
        StringBuilder tempPath = new StringBuilder(basePath);
        tempPath.append(File.separator).append(TEMP).append(File.separator).append(tempfile);
        moveFile(tempPath.toString(), newName, featureId, createThumbnail);
    }

    /*
     Created by :  harshit
     Create this method for sync image and file.
     */
    public static boolean isNewFile(String tempfile) throws IOException {
        StringBuilder tempPath = new StringBuilder(basePath);
        tempPath.append(File.separator).append(TEMP).append(File.separator).append(tempfile);
        File file = new File(tempPath.toString());
        if (file.exists()) {
            return true;
        }
        return false;
    }

    /**
     *
     * @param fileData
     * @param newName
     * @param featureId
     * @param createThumbnail
     */
    public static void moveFile(String tempPath, String newName, Object featureId, boolean createThumbnail) throws IOException {
        File file = new File(tempPath);
        File mainFranchiseDir = checkIsExists(basePath, FEATURE.FRANCHISE, null);
        if (file.exists()) {
            Path path = Paths.get(tempPath.toString());
            byte[] fileData = Files.readAllBytes(path);
            if (fileData != null) {

                String[] split = newName.split(UNIQUE_SEPARATOR);
                String featurename = split[0];
                File franchiseDir = checkIsExists(mainFranchiseDir.getPath(), split[1], null);

                File mainFeatureDir;
                mainFeatureDir = checkIsExists(franchiseDir.getPath(), featurename, null);
                File featureDir = null;
                String replaceAll = null;
                if (featureId != null) {
                    StringBuilder foldername = new StringBuilder();
                    foldername.append(featureId);
                    featureDir = checkIsExists(mainFeatureDir.getPath(), foldername.toString(), null);
                    replaceAll = FolderManagement.changeFileName(newName, featureId);
                } else {
                    featureDir = mainFeatureDir;
                    replaceAll = newName;
                }
                if (featureDir != null) {
                    StringBuilder finalPath = new StringBuilder();
                    if (split.length > 4) {
                        File subfeatureDir = checkIsExists(featureDir.getPath(), split[3], null);
                        finalPath.append(subfeatureDir.getPath()).append(File.separator).append(replaceAll);
                    } else {
                        finalPath.append(featureDir.getPath()).append(File.separator).append(replaceAll);
                    }
                    writeByteData(finalPath.toString(), fileData, createThumbnail);
                    path.toFile().delete();
                }
            }
        }
    }

    public String getNewNameOfFile(String oldName, Long id) {
        String newName = null;
        String[] split = oldName.split(UNIQUE_SEPARATOR);
        newName = FolderManagement.changeFileName(oldName, id.toString());
        return newName;
    }

    /**
     *
     * @param path
     * @param foldername
     * @param id
     * @return
     * <br/> checks if folder-name is avaliable on path, if not, it will make
     * directory
     */
    public static File checkIsExists(String path, String foldername, Long id) {
        StringBuilder checkPath = new StringBuilder(path);
        checkPath.append(File.separator);
        if (foldername != null) {
            checkPath.append(foldername);
        }
        if (id != null) {
            checkPath.append(id);
        }
        File dir = new File(checkPath.toString());
        if (!dir.exists()) {
            dir.mkdir();
        }
        return dir;
    }

    /**
     *
     * @param path
     * @param files
     * @param createThumbnail
     * <br/> reads files from map and creates on path, if createThumbnail=true,
     * it will create thumbnail also
     */
    public static void createFile(String path, Map<String, byte[]> files, boolean createThumbnail) {
        for (Map.Entry<String, byte[]> file : files.entrySet()) {
            try {
                String fileName = file.getKey();
                byte[] fileData = file.getValue();
                StringBuilder filePath = new StringBuilder(path);
                filePath.append(File.separator).append(fileName);
                writeByteData(filePath.toString(), fileData, createThumbnail);

            } catch (FileNotFoundException ex) {
                Logger.getLogger(FolderManagement.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(FolderManagement.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static byte[] getFileDataFromDirectPath(String filePath) throws IOException {
        File file = new File(filePath);
        if (file.exists()) {
            Path path = Paths.get(filePath);
            byte[] readAllBytes = Files.readAllBytes(path);
            return readAllBytes;
        } else {
            return null;
        }
    }

    public static List<String> getAllFilesOfFolder(String foldername, Boolean onlyThumbnail) throws IOException {
        String pathOfFolder = getPathOfFolder(foldername);
        List<String> list = new ArrayList<>();
        if (pathOfFolder != null && pathOfFolder.trim().length() > 0) {
            getAllFilesOfFolder(pathOfFolder, list, onlyThumbnail);
        }
        return list;
    }

    public static List<String> getAllFilesOfFolder(String path, List<String> list, Boolean onlyThumbnail) throws IOException {
        File dir = new File(path);
        if (dir.exists() && dir.isDirectory()) {
            File[] listFiles = dir.listFiles();
            if (listFiles != null && listFiles.length > 0) {
                for (File file : listFiles) {
                    boolean put = false;
                    if (onlyThumbnail != null) {
                        boolean isthumbnail = isThumbnail(file.getPath());
                        if (onlyThumbnail && isthumbnail) {
                            put = true;
                        } else if (!onlyThumbnail && !isthumbnail) {
                            put = true;
                        }
                    } else {
                        put = true;
                    }
                    if (put) {
                        list.add(file.getName());
                    }
                    if (file.isDirectory()) {
                        getAllFilesOfFolder(file.getPath(), list, onlyThumbnail);
                    }
                }
            }
        }
        return list;
    }

    public static String getPathOfFileForSync(String fileName) {

        StringBuilder path = new StringBuilder();
        String[] split = fileName.split(UNIQUE_SEPARATOR);
        if (split.length > 1) {
            path.append(path).append(basePath).append(File.separator).append(FEATURE.FRANCHISE).append(File.separator).append(split[1]).append(File.separator).append(split[0]);
            path.append(File.separator).append(fileName);
            return path.toString();
        } else {
            return "";
        }
    }

    public static String getPathOfFolder(String folder) {
        StringBuilder tempPath = new StringBuilder(basePath);
        tempPath.append(File.separator).append(TEMP).append(File.separator).append(folder);
        File file = new File(tempPath.toString());
        if (file.exists()) {
            return tempPath.toString();
        } else {
            StringBuilder path = new StringBuilder();
            String[] split = folder.split(UNIQUE_SEPARATOR);
            if (split.length > 1) {

                path.append(path).append(basePath).append(File.separator).append(FEATURE.FRANCHISE).append(File.separator).append(split[1]).append(File.separator).append(split[0]);
                if (!split[0].equalsIgnoreCase(FEATURE.COMMON)) {
                    path.append(File.separator).append(split[2]);
                }
                if (split.length > 3) {
                    path.append(File.separator).append(split[3]);
                }
                return path.toString();
            } else {
                return "";
            }
        }
    }

    public static boolean isThumbnail(String filename) {
        boolean isthumbnail = false;
        int indexOf = filename.indexOf("_T.jpg");
        if (filename.length() - indexOf == 6) {
            isthumbnail = true;
        }
        return isthumbnail;
    }

    public static String getPathOfImageForCommonAndNonDiamond(String[] split, StringBuilder path, String imageName) {
        if (split.length > 1) {
            path.append(basePath).append(File.separator).append(FEATURE.FRANCHISE).append(File.separator).append(split[1]).append(File.separator).append(split[0]);
            if (!split[0].equalsIgnoreCase(FEATURE.COMMON)) {
                path.append(File.separator).append(split[2]);
            }
            if (split.length > 4) {
                path.append(File.separator).append(split[3]);
            }
            path.append(File.separator).append(imageName);
            return path.toString();
        } else {
            return "";
        }
    }

    public static String getPathOfImageForDiamond(String[] split, StringBuilder path) {
        if (split.length > 1) {
            path.append(basePath).append(File.separator).append(FEATURE.FRANCHISE).append(File.separator).append(split[1]).append(File.separator).append(split[0]);
            String pathForFile = split[0] + UNIQUE_SEPARATOR + split[1] + UNIQUE_SEPARATOR;
            path.append(File.separator).append(pathForFile).append(split[2]);
            return path.toString();
        } else {
            return "";
        }
    }
}
