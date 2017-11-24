/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.assets.transformers;

import com.argusoft.generic.database.exception.GenericDatabaseException;
import com.argusoft.hkg.common.constantutil.HkSystemConstantUtil;
import com.argusoft.hkg.common.functionutil.FolderManagement;
import static com.argusoft.hkg.common.functionutil.FolderManagement.basePath;
import com.argusoft.hkg.common.functionutil.HkSystemFunctionUtil;
import com.argusoft.hkg.core.HkFieldService;
import com.argusoft.hkg.core.HkFoundationService;
import com.argusoft.hkg.core.HkHRService;
import com.argusoft.hkg.model.HkAssetDocumentEntity;
import com.argusoft.hkg.model.HkAssetDocumentEntityPK;
import com.argusoft.hkg.model.HkAssetEntity;
import com.argusoft.hkg.model.HkAssetIssueEntity;
import com.argusoft.hkg.model.HkAssetPurchaserEntity;
import com.argusoft.hkg.model.HkAssetPurchaserEntityPK;
import com.argusoft.hkg.model.HkCategoryEntity;
import com.argusoft.hkg.model.HkValueEntity;
import com.argusoft.hkg.nosql.core.HkCustomFieldService;
import com.argusoft.hkg.nosql.model.CustomField;
import com.argusoft.hkg.web.assets.databeans.AssetDataBean;
import com.argusoft.hkg.web.assets.databeans.CategoryDataBean;
import com.argusoft.hkg.web.assets.databeans.FileDataBean;
import com.argusoft.hkg.web.assets.databeans.IssueAssetDataBean;
import com.argusoft.hkg.web.customfield.transformers.CustomFieldTransformerBean;
import com.argusoft.hkg.web.eventmanagement.databeans.EventRecipientDataBean;
import com.argusoft.hkg.web.center.usermanagement.databeans.LoginDataBean;
import com.argusoft.hkg.web.usermanagement.wrapper.UserManagementServiceWrapper;
import com.argusoft.hkg.web.util.SelectItem;
import com.argusoft.hkg.web.util.SessionUtil;
import com.argusoft.usermanagement.common.core.UMDepartmentService;
import com.argusoft.usermanagement.common.core.UMUserService;
import com.argusoft.usermanagement.common.model.UMDepartment;
import com.argusoft.usermanagement.common.model.UMUser;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import javax.servlet.http.HttpServletResponse;
import org.krysalis.barcode4j.impl.code128.Code128Bean;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 *
 * @author dhwani
 */
@Service
@Transactional
public class AssetTransformerBean {

    @Autowired
    HkFoundationService hkOperationsService;
    @Autowired
    LoginDataBean hkLoginDataBean;
    @Autowired
    HkFoundationService foundationService;
    @Autowired
    SessionUtil sessionUtil;
    @Autowired
    UserManagementServiceWrapper userManagementServiceWrapper;
    @Autowired
    HkHRService hkHRService;
    @Autowired
    HkCustomFieldService customFieldSevice;
    @Autowired
    HkFieldService fieldService;
    @Autowired
    UMUserService userService;
    @Autowired
    UMDepartmentService departmentService;

    @Autowired
    CustomFieldTransformerBean customFieldTransformerBean;

    public List<AssetDataBean> retrieveAssetDataBean(Long categoryId, boolean forIssue) {
        List<AssetDataBean> hkAssetDataBeanList = null;
        if (categoryId != null) {
            List<HkAssetEntity> hkAssetEntityList = hkOperationsService.retrieveAssets(categoryId, hkLoginDataBean.getCompanyId(), forIssue);
            List<HkCategoryEntity> categoryEntitys = foundationService.retrieveAllAssetCategories(hkLoginDataBean.getCompanyId());
            Map<Long, String> categoryIdPrefixMap = null;
            if (!CollectionUtils.isEmpty(categoryEntitys)) {
                categoryIdPrefixMap = new HashMap<>();
                for (HkCategoryEntity hkCategoryEntity : categoryEntitys) {
                    if (StringUtils.hasText(hkCategoryEntity.getCategoryPrefix())) {
                        categoryIdPrefixMap.put(hkCategoryEntity.getId(), hkCategoryEntity.getCategoryPrefix());
                    }
                }
            }
//            Map<Long, Map<HkSystemConstantUtil.SectionNameForCustomField, List<Map<Long, Map<String, Object>>>>> retrieveDocumentByInstanceIds = customFieldSevice.retrieveDocumentByInstanceIds(null, HkSystemConstantUtil.FeatureNameForCustomField.ASSET, hkLoginDataBean.getCompanyId());
            if (!CollectionUtils.isEmpty(hkAssetEntityList)) {
                hkAssetDataBeanList = new ArrayList<>();
                for (HkAssetEntity hkAssetEntity : hkAssetEntityList) {
                    AssetDataBean hkAssetDataBean = this.convertHkAssetEntityToAssetDataBean(hkAssetEntity, null, null);
                    if (forIssue) {
                        if (hkAssetDataBean.isAssetType()) {
                            if (hkAssetDataBean.getCategory() != null && hkAssetDataBean.getSerialNumber() != null) {
                                if (categoryIdPrefixMap.containsKey(hkAssetDataBean.getCategory())) {
                                    String modelName = categoryIdPrefixMap.get(hkAssetDataBean.getCategory()) + hkAssetDataBean.getSerialNumber();
                                    hkAssetDataBean.setModelName(hkAssetDataBean.getModelName() + " - " + modelName);
                                } else {
                                    hkAssetDataBean.setModelName(hkAssetDataBean.getModelName());
                                }
                            } else {
                                hkAssetDataBean.setModelName(hkAssetDataBean.getModelName());
                            }
                        } else {
                            if (hkAssetDataBean.getRemaingUnits() != null) {
                                hkAssetDataBean.setModelName(hkAssetDataBean.getModelName() + " (" + hkAssetDataBean.getRemaingUnits() + " )");
                            }
                        }
                    }
                    hkAssetDataBeanList.add(hkAssetDataBean);
                }
            }
        }
        return hkAssetDataBeanList;
    }

    private AssetDataBean convertHkAssetEntityToAssetDataBean(HkAssetEntity hkAssetEntity, AssetDataBean hkAssetDataBean, Map<Long, Map<HkSystemConstantUtil.SectionNameForCustomField, List<Map<Long, Map<String, Object>>>>> retrieveDocumentByInstanceIds) {
        if (hkAssetDataBean == null) {
            hkAssetDataBean = new AssetDataBean();
        }
        hkAssetDataBean.setModelName(hkAssetEntity.getAssetName());

        if (hkAssetEntity.getAssetType() != null && hkAssetEntity.getAssetType().equalsIgnoreCase(HkSystemConstantUtil.ASSET_TYPE.MANAGED)) {
            hkAssetDataBean.setAssetType(true);
            hkAssetDataBean.setAssetName(hkAssetEntity.getAssetName());
        } else {
            hkAssetDataBean.setAssetName(hkAssetEntity.getAssetName() + "(" + hkAssetEntity.getRemainingUnits() + ")");
            hkAssetDataBean.setAssetType(false);
        }
        if (hkAssetEntity.getStatus().equalsIgnoreCase(HkSystemConstantUtil.ASSET_STATUS.AVAILABLE)) {
            hkAssetDataBean.setStatus(HkSystemConstantUtil.ASSET_STATUS.AVAILABLE);
            hkAssetDataBean.setStatusString(HkSystemConstantUtil.ASSET_STATUS_MAP.get(HkSystemConstantUtil.ASSET_STATUS.AVAILABLE));
        }
        if (hkAssetEntity.getStatus().equalsIgnoreCase(HkSystemConstantUtil.ASSET_STATUS.ARCHIVED)) {
            hkAssetDataBean.setStatus(HkSystemConstantUtil.ASSET_STATUS.ARCHIVED);
            hkAssetDataBean.setStatusString(HkSystemConstantUtil.ASSET_STATUS_MAP.get(HkSystemConstantUtil.ASSET_STATUS.ARCHIVED));
        }
        if (hkAssetEntity.getStatus().equalsIgnoreCase(HkSystemConstantUtil.ASSET_STATUS.INREPAIR)) {
            hkAssetDataBean.setStatus(HkSystemConstantUtil.ASSET_STATUS.INREPAIR);
            hkAssetDataBean.setStatusString(HkSystemConstantUtil.ASSET_STATUS_MAP.get(HkSystemConstantUtil.ASSET_STATUS.INREPAIR));
        }
        if (hkAssetEntity.getStatus().equalsIgnoreCase(HkSystemConstantUtil.ASSET_STATUS.ISSUED)) {
            hkAssetDataBean.setStatus(HkSystemConstantUtil.ASSET_STATUS.ISSUED);
            if (hkAssetEntity.getAssetLastIssued() != null) {
                hkAssetDataBean.setIssueToVal(hkAssetEntity.getAssetLastIssued().getIssueToInstance() + ":" + hkAssetEntity.getAssetLastIssued().getIssueToType());
                hkAssetDataBean.setIssuedOn(hkAssetEntity.getAssetLastIssued().getIssuedOn());
            }
            hkAssetDataBean.setStatusString(HkSystemConstantUtil.ASSET_STATUS_MAP.get(HkSystemConstantUtil.ASSET_STATUS.ISSUED));
        }
        hkAssetDataBean.setModelNumber(hkAssetEntity.getModelNumber());
        if (hkAssetEntity.getSerialNumber() != null) {
            String newNum = null;
            if (hkAssetEntity.getCategory().getCategoryPattern() != null) {
                hkAssetDataBean.setPattern(hkAssetEntity.getCategory().getCategoryPattern());
                NumberFormat nf = new DecimalFormat(hkAssetEntity.getCategory().getCategoryPattern());
                newNum = nf.format(hkAssetEntity.getSerialNumber());
            } else {
                hkAssetDataBean.setPattern("00");
                NumberFormat nf = new DecimalFormat("00");
                newNum = nf.format(hkAssetEntity.getSerialNumber());
            }
            hkAssetDataBean.setSerialNumber(newNum);
        }

//        hkAssetDataBean.setSerialNumber(hkAssetEntity.getSerialNumber().toString());
        hkAssetDataBean.setId(hkAssetEntity.getId());
        hkAssetDataBean.setCategory(hkAssetEntity.getCategory().getId());
        if (hkAssetEntity.getCategory() != null) {
            hkAssetDataBean.setParentCategory(hkAssetEntity.getCategory().getCategoryTitle());
            hkAssetDataBean.setPrefix(hkAssetEntity.getCategory().getCategoryPrefix());
        }
        if (hkAssetEntity.getRemainingUnits() != null) {
            hkAssetDataBean.setRemaingUnits(hkAssetEntity.getRemainingUnits());
        }
        if (hkAssetEntity.getPurchaseDt() != null) {
            hkAssetDataBean.setPurchaseDt(HkSystemFunctionUtil.convertToClientDate(hkAssetEntity.getPurchaseDt(), hkLoginDataBean.getClientRawOffsetInMin()));
        }
        if (hkAssetEntity.getInwardDt() != null) {
            hkAssetDataBean.setInwardDt(HkSystemFunctionUtil.convertToClientDate(hkAssetEntity.getInwardDt(), hkLoginDataBean.getClientRawOffsetInMin()));
        }

        if (hkAssetEntity.getImagePath() != null) {
            hkAssetDataBean.setImagePath(hkAssetEntity.getImagePath());
        }

        if (hkAssetEntity.getManufacturer() != null) {
            hkAssetDataBean.setManufacturer(hkAssetEntity.getManufacturer());
        }

        if (hkAssetEntity.getRemarks() != null) {
            hkAssetDataBean.setRemarks(hkAssetEntity.getRemarks());
        }
        hkAssetDataBean.setCanProduceImages(hkAssetEntity.getCanProduceImages());
        if (hkAssetEntity.getHkAssetDocumentEntitySet() != null) {
            List<FileDataBean> fileDataBeans = new ArrayList<>();
            for (Iterator<HkAssetDocumentEntity> it = hkAssetEntity.getHkAssetDocumentEntitySet().iterator(); it.hasNext();) {
                HkAssetDocumentEntity hkAssetDocumentEntity = it.next();
                if (!hkAssetDocumentEntity.getIsArchive()) {
                    FileDataBean fileDataBean = new FileDataBean();
                    fileDataBean.setFilePath(hkAssetDocumentEntity.getHkAssetDocumentEntityPK().getDocumentPath());
                    fileDataBean.setFileName(hkAssetDocumentEntity.getDocumentTitle());
                    fileDataBean.setId(hkAssetDocumentEntity.getHkAssetDocumentEntityPK().getAsset());
                    fileDataBean.setArchiveStatus(hkAssetDocumentEntity.getIsArchive());
                    fileDataBeans.add(fileDataBean);
                }
            }
            hkAssetDataBean.setFileDataBeans(fileDataBeans);
        }
        if (hkAssetEntity.getHkAssetPurchaserEntitySet() != null) {
            //21-10-2014 DMEHTA need to change this logic
            if (hkAssetEntity.getHkAssetPurchaserEntitySet() != null) {
                List<EventRecipientDataBean> eventRecipientDataBeanList = new ArrayList<>();
                String purchasedBy = null;
                List<String> ids = new ArrayList<>();
                for (Iterator<HkAssetPurchaserEntity> it = hkAssetEntity.getHkAssetPurchaserEntitySet().iterator(); it.hasNext();) {
                    HkAssetPurchaserEntity hkAssetPurchaserEntity = it.next();
                    if (!hkAssetPurchaserEntity.getIsArchive()) {
                        ids.add(hkAssetPurchaserEntity.getHkAssetPurchaserEntityPK().getPurchaseByInstance() + ":" + hkAssetPurchaserEntity.getHkAssetPurchaserEntityPK().getPurchaseByType());

//                        ids.add(hkAssetPurchaserEntity.getPurchaseByInstance());
//                        if (purchasedBy != null) {
//                            purchasedBy = purchasedBy + "," + hkAssetPurchaserEntity.getPurchaseByInstance() + ":" + hkAssetPurchaserEntity.getPurchaseByType();
//                        } else {
//                            purchasedBy = hkAssetPurchaserEntity.getPurchaseByInstance() + ":" + hkAssetPurchaserEntity.getPurchaseByType();
//                        }
                    }
                }
                Map<String, String> purchaserNames = userManagementServiceWrapper.retrieveRecipientNames(ids);
                for (HkAssetPurchaserEntity hkAssetPurchaserEntity : hkAssetEntity.getHkAssetPurchaserEntitySet()) {
                    if (!hkAssetPurchaserEntity.getIsArchive()) {
                        EventRecipientDataBean eventRecipientDataBean = new EventRecipientDataBean();
                        eventRecipientDataBean.setRecipientInstance(hkAssetPurchaserEntity.getHkAssetPurchaserEntityPK().getPurchaseByInstance());
                        eventRecipientDataBean.setRecipientType(hkAssetPurchaserEntity.getHkAssetPurchaserEntityPK().getPurchaseByType());
                        eventRecipientDataBean.setRecipientValue(purchaserNames.get(eventRecipientDataBean.getRecipientInstance() + ":" + eventRecipientDataBean.getRecipientType()));
                        eventRecipientDataBeanList.add(eventRecipientDataBean);
                    }
                }
                hkAssetDataBean.setPurchaserDataBeanList(eventRecipientDataBeanList);
            }
        }
        if (!CollectionUtils.isEmpty(retrieveDocumentByInstanceIds)) {
//            System.out.println("Map not null");
            Map<HkSystemConstantUtil.SectionNameForCustomField, List<Map<Long, Map<String, Object>>>> sectionWiseMap = retrieveDocumentByInstanceIds.get(hkAssetEntity.getId());
//            System.out.println("sectionWiseMap : " + sectionWiseMap);
            if (sectionWiseMap != null) {
                List<Map<Long, Map<String, Object>>> maps = sectionWiseMap.get(HkSystemConstantUtil.SectionNameForCustomField.GENERAL);
//                System.out.println("maps :" + maps);
                if (maps != null) {
                    for (Map<Long, Map<String, Object>> map : maps) {
                        hkAssetDataBean.setAddAssetData(map.get(hkAssetEntity.getId()));
                    }
                }
            }
        }
        if (hkAssetDataBean.getAddAssetData() == null) {
            hkAssetDataBean.setAddAssetData(new HashMap<String, Object>());
        }
        hkAssetDataBean.setBarcode(hkAssetEntity.getBarcode());
        hkAssetDataBean.setCanGenerateBarcode(hkAssetEntity.getBarcode() != null ? Boolean.TRUE : Boolean.FALSE);
        return hkAssetDataBean;
    }

    public Boolean createAsset(AssetDataBean hkAssetDataBean) throws IOException {
        Boolean result = null;
        if (hkAssetDataBean.getCategory() != null) {
            HkAssetEntity hkAssetEntity = this.convertAssetDataBeanToAssetEntity(hkAssetDataBean, null, new HkCategoryEntity(hkAssetDataBean.getCategory()));
            hkAssetEntity.setCreatedBy(hkLoginDataBean.getId());
            hkAssetEntity.setCreatedOn(new Date());
            hkAssetEntity.setLastModifiedBy(hkLoginDataBean.getId());
            hkAssetEntity.setLastModifiedOn(new Date());
            hkAssetEntity.setIsArchive(Boolean.FALSE);
            hkAssetEntity.setStatus(HkSystemConstantUtil.ASSET_STATUS.AVAILABLE);

            Set<HkAssetDocumentEntity> hkAssetDocumentEntitySet = null;
            Set<HkAssetPurchaserEntity> hkAssetPurchaserEntitySet = null;
            if (sessionUtil.getFileuploadMap() != null) {
                hkAssetDocumentEntitySet = new HashSet<>();
                for (Map.Entry<String, String> file : sessionUtil.getFileuploadMap().entrySet()) {
                    String systemFileName = file.getKey();
                    String actualFileName = file.getValue();
                    HkAssetDocumentEntityPK assetDocumentEntityPk = new HkAssetDocumentEntityPK();
                    assetDocumentEntityPk.setDocumentPath(systemFileName);
                    HkAssetDocumentEntity hkAssetDocumentEntity = new HkAssetDocumentEntity();
                    hkAssetDocumentEntity.setHkAssetDocumentEntityPK(assetDocumentEntityPk);
                    hkAssetDocumentEntity.setIsArchive(false);
                    hkAssetDocumentEntity.setDocumentTitle(actualFileName);
                    hkAssetDocumentEntity.setHkAssetEntity(hkAssetEntity);
                    hkAssetDocumentEntitySet.add(hkAssetDocumentEntity);

                }
            }

            hkAssetEntity.setHkAssetDocumentEntitySet(hkAssetDocumentEntitySet);

            String[] purchaser = hkAssetDataBean.getPurchasedBy().split(",");
            if (purchaser != null) {
                hkAssetPurchaserEntitySet = new HashSet<>();
                HkAssetPurchaserEntity hkAssetPurchaserEntity = null;
                for (String string : purchaser) {
                    hkAssetPurchaserEntity = new HkAssetPurchaserEntity();
                    hkAssetPurchaserEntity.setHkAssetEntity(hkAssetEntity);
                    String[] split = string.split(":");
                    HkAssetPurchaserEntityPK hkAssetPurchaserEntityPK = new HkAssetPurchaserEntityPK();
                    hkAssetPurchaserEntityPK.setPurchaseByInstance(Long.parseLong(split[0]));
                    hkAssetPurchaserEntity.setIsArchive(false);
                    hkAssetPurchaserEntityPK.setPurchaseByType(split[1]);
                    hkAssetPurchaserEntity.setHkAssetPurchaserEntityPK(hkAssetPurchaserEntityPK);
                    hkAssetPurchaserEntitySet.add(hkAssetPurchaserEntity);
                }
            }
            hkAssetEntity.setHkAssetPurchaserEntitySet(hkAssetPurchaserEntitySet);

            result = hkOperationsService.createAsset(hkAssetEntity);
            Long assetId = hkAssetEntity.getId();

            if (hkAssetDataBean.isCanGenerateBarcode()) {
                String changedFileName = FolderManagement.changeFileName(hkAssetDataBean.getBarcode(), assetId);
                StringBuilder tempFilePath = new StringBuilder(basePath);
                tempFilePath.append(File.separator).append("TEMP").append(File.separator).append(hkAssetDataBean.getBarcode());
                FolderManagement.moveFile(tempFilePath.toString(), changedFileName, assetId, false);
                hkAssetEntity.setBarcode(changedFileName);
                hkOperationsService.updateAsset(hkAssetEntity, null);
            }
//            System.out.println("Id is :L " + assetId);
//            List<String> tempFile = new ArrayList<>();
//            for (String fname : hkAssetDataBean.getFileList()) {
//                System.out.println("fname " +fname);
//                String changeFileName = FolderManagement.changeFileName(fname, assetId.toString());
//                System.out.println("Change file name " + changeFileName);
//                tempFile.add(changeFileName);
//            }
            Set<HkAssetDocumentEntity> hkAssetDocumentEntitySet1 = hkAssetEntity.getHkAssetDocumentEntitySet();
            if (hkAssetDocumentEntitySet1 != null) {
                for (HkAssetDocumentEntity hkAssetDocumentEntity : hkAssetDocumentEntitySet1) {
                    String systemFileName = hkAssetDocumentEntity.getHkAssetDocumentEntityPK().getDocumentPath();
//                    System.out.println("Fname : " + systemFileName);
                    hkAssetDocumentEntity.getHkAssetDocumentEntityPK().setDocumentPath(FolderManagement.changeFileName(systemFileName, hkAssetEntity.getId().toString()));
                }
                hkAssetEntity.setHkAssetDocumentEntitySet(hkAssetDocumentEntitySet1);
                hkOperationsService.updateAsset(hkAssetEntity, null);
            }
            FolderManagement.saveFile(null, null, hkAssetEntity.getId(), null, hkAssetDataBean.getFileList(), false);
            if (assetId != null) {
                //Make a map sectionwise id(Key) and customField map(Value)
                Map<Long, Map<String, Object>> val = new HashMap<>();
                val.put(assetId, hkAssetDataBean.getAddAssetData());

                List<String> uiFieldList = new ArrayList<>();
                if (!CollectionUtils.isEmpty(hkAssetDataBean.getDbType())) {
                    for (Map.Entry<String, String> entrySet : hkAssetDataBean.getDbType().entrySet()) {
                        uiFieldList.add(entrySet.getKey());
                    }
                }
                Map<String, String> uiFieldMap = fieldService.retrieveUIFieldNameWithComponentTypes(uiFieldList);
                //Pass this map to makecustomfieldService
                List<CustomField> makeCustomField = customFieldSevice.makeCustomField(val, hkAssetDataBean.getDbType(), uiFieldMap, HkSystemConstantUtil.FeatureNameForCustomField.ASSET.toString(), hkLoginDataBean.getCompanyId(), assetId);
                //After that make Map of Section and there customfield
                Map<HkSystemConstantUtil.SectionNameForCustomField, List<CustomField>> map = new HashMap<>();
                map.put(HkSystemConstantUtil.SectionNameForCustomField.GENERAL, makeCustomField);
                //Pass this map to customFieldSevice.saveOrUpdate method.
                customFieldSevice.saveOrUpdate(assetId, HkSystemConstantUtil.FeatureNameForCustomField.ASSET, hkLoginDataBean.getCompanyId(), map);
                result = Boolean.TRUE;
            }
            userManagementServiceWrapper.createLocaleForEntity(hkAssetDataBean.getModelName(), "Asset", hkLoginDataBean.getId(), hkLoginDataBean.getCompanyId());
            sessionUtil.setFileuploadMap(null);
        } else {
            result = Boolean.FALSE;
        }
        return result;
    }

    public Boolean updateAsset(AssetDataBean hkAssetDataBean) throws IOException {
        Boolean result = null;
        if (hkAssetDataBean.getId() != null && hkAssetDataBean.getCategory() != null) {
            HkAssetEntity hkAssetEntity = hkOperationsService.retrieveAsset(hkAssetDataBean.getId());
            if (hkAssetDataBean.isCanGenerateBarcode()) {
                if (hkAssetEntity.getCategory() != null) {
                    if (!(hkAssetEntity.getCategory().getCategoryPrefix() + hkAssetEntity.getSerialNumber()).equals(hkAssetDataBean.getPrefix() + hkAssetDataBean.getSerialNumber())) {
                        String changedFileName = FolderManagement.changeFileName(hkAssetDataBean.getBarcode(), hkAssetEntity.getId());
                        StringBuilder tempFilePath = new StringBuilder(basePath);
                        tempFilePath.append(File.separator).append("TEMP").append(File.separator).append(hkAssetDataBean.getBarcode());
                        FolderManagement.moveFile(tempFilePath.toString(), changedFileName, hkAssetEntity.getId(), false);
                        hkAssetEntity.setBarcode(changedFileName);
                    }
                }
            } else {
                hkAssetEntity.setBarcode(null);
            }
            this.convertAssetDataBeanToAssetEntity(hkAssetDataBean, hkAssetEntity, new HkCategoryEntity(hkAssetDataBean.getCategory()));
            hkAssetEntity.setLastModifiedBy(hkLoginDataBean.getId());
            hkAssetEntity.setLastModifiedOn(new Date());
            List<Long> notifyUsers = null;
            List<String> featureNames = new ArrayList<>();
            featureNames.add(HkSystemConstantUtil.Feature.MANAGE_ASSET);
            Set<Long> userList = userManagementServiceWrapper.searchUsersByFeatureName(featureNames, hkLoginDataBean.getCompanyId());
            if (userList != null) {
                notifyUsers = new ArrayList<>();
                for (Long id : userList) {
                    notifyUsers.add(id);
                }
            }

            Set<HkAssetDocumentEntity> hkAssetDocumentEntitySet = hkAssetEntity.getHkAssetDocumentEntitySet();
            if (hkAssetDocumentEntitySet == null) {
                hkAssetDocumentEntitySet = new LinkedHashSet<>();
            }
            Map<String, HkAssetDocumentEntity> hkAssetDocumentEntitySetNew = new LinkedHashMap<>();
            List<String> tempFile = null;
            if (sessionUtil.getFileuploadMap() != null) {
                tempFile = new ArrayList<>();
                for (Map.Entry<String, String> file : sessionUtil.getFileuploadMap().entrySet()) {
                    String systemFileName = file.getKey();
                    String actualFileName = file.getValue();
                    HkAssetDocumentEntityPK assetDocumentEntityPk = new HkAssetDocumentEntityPK();
                    assetDocumentEntityPk.setAsset(hkAssetDataBean.getId());
                    HkAssetDocumentEntity hkAssetDocumentEntity = new HkAssetDocumentEntity();
                    assetDocumentEntityPk.setDocumentPath(FolderManagement.changeFileName(systemFileName, hkAssetDataBean.getId().toString()));
                    hkAssetDocumentEntity.setHkAssetDocumentEntityPK(assetDocumentEntityPk);
                    hkAssetDocumentEntity.setIsArchive(false);
                    hkAssetDocumentEntity.setDocumentTitle(actualFileName);
                    hkAssetDocumentEntity.setHkAssetEntity(hkAssetEntity);
                    hkAssetDocumentEntitySetNew.put(assetDocumentEntityPk.toString(), hkAssetDocumentEntity);
                    tempFile.add(systemFileName);
                }
            }

            if (!CollectionUtils.isEmpty(hkAssetDataBean.getFileDataBeans())) {

                for (FileDataBean fileDataBean : hkAssetDataBean.getFileDataBeans()) {
                    String systemFileName = fileDataBean.getFilePath();
                    String actualFileName = fileDataBean.getFileName();
                    HkAssetDocumentEntityPK assetDocumentEntityPk = new HkAssetDocumentEntityPK();
                    assetDocumentEntityPk.setAsset(hkAssetDataBean.getId());
                    assetDocumentEntityPk.setDocumentPath(systemFileName);
                    HkAssetDocumentEntity hkAssetDocumentEntity = new HkAssetDocumentEntity();
                    hkAssetDocumentEntity.setHkAssetDocumentEntityPK(assetDocumentEntityPk);
                    hkAssetDocumentEntity.setIsArchive(fileDataBean.isArchiveStatus());
                    hkAssetDocumentEntity.setDocumentTitle(actualFileName);
                    hkAssetDocumentEntity.setHkAssetEntity(hkAssetEntity);

                    if (!hkAssetDocumentEntity.getIsArchive()) {
                        hkAssetDocumentEntitySetNew.put(assetDocumentEntityPk.toString(), hkAssetDocumentEntity);
                    }
                }
            }

            for (HkAssetDocumentEntity hkAssetDocumentEntity : hkAssetDocumentEntitySet) {

                if (!hkAssetDocumentEntitySetNew.containsKey(hkAssetDocumentEntity.getHkAssetDocumentEntityPK().toString())) {
                    hkAssetDocumentEntity.setIsArchive(true);
                } else {
                    hkAssetDocumentEntitySetNew.remove(hkAssetDocumentEntity.getHkAssetDocumentEntityPK().toString());
                }

            }
            hkAssetDocumentEntitySet.addAll(hkAssetDocumentEntitySetNew.values());
            hkAssetEntity.setHkAssetDocumentEntitySet(hkAssetDocumentEntitySet);
            Set<HkAssetPurchaserEntity> hkAssetPurchaserEntitySet = hkAssetEntity.getHkAssetPurchaserEntitySet();

            String[] purchaser = hkAssetDataBean.getPurchasedBy().split(",");
            if (purchaser != null) {
                Set<HkAssetPurchaserEntity> hkAssetPurchaserEntitySetNew = new LinkedHashSet<>();

                for (String string : purchaser) {
                    HkAssetPurchaserEntity hkAssetPurchaserEntity = new HkAssetPurchaserEntity();
                    hkAssetPurchaserEntity.setHkAssetEntity(hkAssetEntity);
                    String[] split = string.split(":");
                    if (split.length == 2) {
                        HkAssetPurchaserEntityPK hkAssetPurchaserEntityPK = new HkAssetPurchaserEntityPK();
                        hkAssetPurchaserEntityPK.setPurchaseByInstance(Long.parseLong(split[0]));
                        hkAssetPurchaserEntityPK.setPurchaseByType(split[1]);
                        hkAssetPurchaserEntityPK.setAsset(hkAssetEntity.getId());
                        hkAssetPurchaserEntity.setIsArchive(false);
                        hkAssetPurchaserEntity.setHkAssetPurchaserEntityPK(hkAssetPurchaserEntityPK);

                        hkAssetPurchaserEntitySetNew.add(hkAssetPurchaserEntity);
                    }

                }

                if (!CollectionUtils.isEmpty(hkAssetPurchaserEntitySetNew) && !CollectionUtils.isEmpty(hkAssetPurchaserEntitySet)) {
                    for (HkAssetPurchaserEntity hkAssetPurchaserEntity : hkAssetPurchaserEntitySet) {
                        if (!hkAssetPurchaserEntitySetNew.contains(hkAssetPurchaserEntity)) {
                            hkAssetPurchaserEntity.setIsArchive(true);
                        } else {
                            hkAssetPurchaserEntitySetNew.remove(hkAssetPurchaserEntity);
                        }

                    }
                    hkAssetPurchaserEntitySet.addAll(hkAssetPurchaserEntitySetNew);
                }
            }
            hkAssetEntity.setHkAssetPurchaserEntitySet(hkAssetPurchaserEntitySet);
            boolean updateAsset = hkOperationsService.updateAsset(hkAssetEntity, notifyUsers);
            if (tempFile != null && !tempFile.isEmpty()) {
                FolderManagement.saveFile(null, null, hkAssetDataBean.getId(), null, tempFile, false);
            }
            Long assetId = hkAssetEntity.getId();
            if (assetId != null) {
                //Make a map sectionwise id(Key) and customField map(Value)
                Map<Long, Map<String, Object>> val = new HashMap<>();
                val.put(assetId, hkAssetDataBean.getAddAssetData());
                List<String> uiFieldList = new ArrayList<>();
                if (!CollectionUtils.isEmpty(hkAssetDataBean.getDbType())) {
                    for (Map.Entry<String, String> entrySet : hkAssetDataBean.getDbType().entrySet()) {
                        uiFieldList.add(entrySet.getKey());
                    }
                }
                Map<String, String> uiFieldMap = fieldService.retrieveUIFieldNameWithComponentTypes(uiFieldList);
                //Pass this map to makecustomfieldService
                List<CustomField> makeCustomField = customFieldSevice.makeCustomField(val, hkAssetDataBean.getDbType(), uiFieldMap, HkSystemConstantUtil.FeatureNameForCustomField.ASSET.toString(), hkLoginDataBean.getCompanyId(), assetId);
                //After that make Map of Section and there customfield
                Map<HkSystemConstantUtil.SectionNameForCustomField, List<CustomField>> map = new HashMap<>();
                map.put(HkSystemConstantUtil.SectionNameForCustomField.GENERAL, makeCustomField);
                //Pass this map to customFieldSevice.saveOrUpdate method.
                customFieldSevice.saveOrUpdate(assetId, HkSystemConstantUtil.FeatureNameForCustomField.ASSET, hkLoginDataBean.getCompanyId(), map);
            }
            userManagementServiceWrapper.createLocaleForEntity(hkAssetDataBean.getModelName(), "Asset", hkLoginDataBean.getId(), hkLoginDataBean.getCompanyId());
            result = Boolean.TRUE;
        } else {
            result = Boolean.FALSE;
        }
        return result;
    }

    private HkAssetEntity convertAssetDataBeanToAssetEntity(AssetDataBean hkAssetDataBean, HkAssetEntity hkAssetEntity, HkCategoryEntity hkCategoryEntity) {
        boolean isCreate = false;
        if (hkAssetEntity == null) {
            hkAssetEntity = new HkAssetEntity();
            isCreate = true;
        }
        hkAssetEntity.setAssetName(hkAssetDataBean.getModelName());
        if (hkAssetDataBean.getModelNumber() != null) {
            hkAssetEntity.setModelNumber(hkAssetDataBean.getModelNumber());
        }
        if (hkCategoryEntity != null) {
            hkAssetEntity.setCategory(hkCategoryEntity);
        }
        if (hkAssetDataBean.isAssetType()) {
            hkAssetEntity.setAssetType(HkSystemConstantUtil.ASSET_TYPE.MANAGED);
        } else {
            hkAssetEntity.setAssetType(HkSystemConstantUtil.ASSET_TYPE.NON_MANAGED);
        }

        hkAssetEntity.setModelNumber(hkAssetDataBean.getModelNumber());

        hkAssetEntity.setInwardDt(HkSystemFunctionUtil.convertToServerDate(hkAssetDataBean.getInwardDt(), hkLoginDataBean.getClientRawOffsetInMin()));
        hkAssetEntity.setPurchaseDt(HkSystemFunctionUtil.convertToServerDate(hkAssetDataBean.getPurchaseDt(), hkLoginDataBean.getClientRawOffsetInMin()));
        hkAssetEntity.setRemarks(hkAssetDataBean.getRemarks());
        if (hkAssetDataBean.getSerialNumber() != null) {
            hkAssetEntity.setSerialNumber(Integer.parseInt(hkAssetDataBean.getSerialNumber()));
        }
        if (hkAssetDataBean.getManufacturer() != null) {
            hkAssetEntity.setManufacturer(hkAssetDataBean.getManufacturer());
        }

        if (hkAssetDataBean.getCanProduceImages()) {
            hkAssetEntity.setCanProduceImages(true);
            if (hkAssetDataBean.getImagePath() != null) {
                hkAssetEntity.setImagePath(hkAssetDataBean.getImagePath());
            }
        } else {
            hkAssetEntity.setCanProduceImages(false);
        }
        if (hkAssetDataBean.getStatus() != null) {
            hkAssetEntity.setStatus(hkAssetDataBean.getStatus());
        }
        if (hkAssetDataBean.getRemaingUnits() != null) {
            hkAssetEntity.setRemainingUnits(hkAssetDataBean.getRemaingUnits());
        }
//        HkAssetPurchaserEntity hkAssetPurchaserEntity = null;

        //21-10-2014 DMEHTA need to change this logic
//            hkAssetPurchaserEntity = hkAssetEntity.getHkAssetPurchaserEntity();
//            if (hkAssetPurchaserEntity == null) {
//                hkAssetPurchaserEntity = new HkAssetPurchaserEntity();
//                hkAssetPurchaserEntity.setHkAssetEntity(hkAssetEntity);
//            }
//            String[] split = hkAssetDataBean.getPurchasedBy().split(":");
//            hkAssetPurchaserEntity.setPurchaseByInstance(Long.parseLong(split[0]));
//            hkAssetPurchaserEntity.setIsArchive(false);
//            hkAssetPurchaserEntity.setPurchaseByType(split[1]);        
        //21-10-2014 DMEHTA need to change this logic
        // hkAssetEntity.setHkAssetPurchaserEntity(hkAssetPurchaserEntity);
        hkAssetEntity.setManufacturer(hkAssetDataBean.getManufacturer());
        hkAssetEntity.setFranchise(hkLoginDataBean.getCompanyId());
        return hkAssetEntity;
    }

    public List<SelectItem> fillStatusList(String status) {
        List<SelectItem> statusList = new ArrayList<>();
        statusList.add(new SelectItem(HkSystemConstantUtil.ASSET_STATUS.ARCHIVED, HkSystemConstantUtil.ASSET_STATUS_MAP.get(HkSystemConstantUtil.ASSET_STATUS.ARCHIVED)));
        statusList.add(new SelectItem(HkSystemConstantUtil.ASSET_STATUS.AVAILABLE, HkSystemConstantUtil.ASSET_STATUS_MAP.get(HkSystemConstantUtil.ASSET_STATUS.AVAILABLE)));
        statusList.add(new SelectItem(HkSystemConstantUtil.ASSET_STATUS.INREPAIR, HkSystemConstantUtil.ASSET_STATUS_MAP.get(HkSystemConstantUtil.ASSET_STATUS.INREPAIR)));
        if (status.equalsIgnoreCase(HkSystemConstantUtil.ASSET_STATUS.ISSUED)) {
            statusList.add(new SelectItem(HkSystemConstantUtil.ASSET_STATUS.ISSUED, HkSystemConstantUtil.ASSET_STATUS_MAP.get(HkSystemConstantUtil.ASSET_STATUS.ISSUED)));
        }
        return statusList;
    }

    public List<SelectItem> retrieveComboValuesOfManifacturer(String code) {
        List<SelectItem> valuesOfKey = new ArrayList<>();
        List<HkValueEntity> masterValues = foundationService.retrieveMasterValuesByCode(hkLoginDataBean.getCompanyId(), Arrays.asList(code));

        if (!CollectionUtils.isEmpty(masterValues)) {
            for (HkValueEntity hkValueEntity : masterValues) {
                if (hkValueEntity.getIsActive()) {
                    String valueName = hkValueEntity.getValueName();
                    if (hkValueEntity.getTranslatedValueName() != null && !hkValueEntity.getTranslatedValueName().isEmpty()) {
                        valueName = hkValueEntity.getTranslatedValueName();
                    }
                    if (hkValueEntity.getShortcutCode() != null) {
                        valuesOfKey.add(new SelectItem(hkValueEntity.getId(), valueName, hkValueEntity.getShortcutCode()));
                    } else {
                        valuesOfKey.add(new SelectItem(hkValueEntity.getId(), valueName, 0));
                    }
                }
            }
        }

        return valuesOfKey;
    }

    public Boolean issueAsset(IssueAssetDataBean issueAssetDataBean) throws GenericDatabaseException {
        Boolean response = Boolean.FALSE;
        List<HkAssetIssueEntity> issueEntityList = null;
        String employeeCode = null;
        String employeeName = null;
        String departmentName = null;
        String[] issueTo = issueAssetDataBean.getIssueTo().split(":");
        Long instance = Long.parseLong(issueTo[0]);
        String type = issueTo[1];
        if (type.equalsIgnoreCase(HkSystemConstantUtil.RecipientCodeType.EMPLOYEE)) {
            UMUser user = userService.retrieveUserById(instance, hkLoginDataBean.getCompanyId());
            employeeCode = user.getUserCode();
            if (user.getLastName() != null) {
                employeeName = user.getFirstName() + " " + user.getLastName();
            } else {
                employeeName = user.getFirstName();
            }
        }
        if (type.equalsIgnoreCase(HkSystemConstantUtil.RecipientCodeType.DEPARTMENT)) {
            UMDepartment department = departmentService.retrieveDepartmentById(instance, false, false, false);
            departmentName = department.getDeptName();
        }
        if (issueAssetDataBean.getAssetDataBeanList() != null && !issueAssetDataBean.getAssetDataBeanList().isEmpty()) {
            issueEntityList = new ArrayList<>();
            for (AssetDataBean assetDataBean : issueAssetDataBean.getAssetDataBeanList()) {
                if (assetDataBean.isAssetType()) {
                    HkAssetIssueEntity assetIssueEntity = this.convertAssetIssueEntityToIssueAssetDataBean(null, issueAssetDataBean, employeeCode, employeeName, departmentName);
                    assetIssueEntity.setAsset(new HkAssetEntity(assetDataBean.getId()));
                    issueEntityList.add(assetIssueEntity);
                }
            }
        }
        if (issueAssetDataBean.getNonManagedAssetDataBeans() != null && !issueAssetDataBean.getNonManagedAssetDataBeans().isEmpty()) {
            if (issueEntityList == null) {
                issueEntityList = new ArrayList<>();
            } else {
                for (AssetDataBean assetDataBean : issueAssetDataBean.getNonManagedAssetDataBeans()) {
                    HkAssetIssueEntity assetIssueEntity = this.convertAssetIssueEntityToIssueAssetDataBean(null, issueAssetDataBean, employeeCode, employeeName, departmentName);
                    assetIssueEntity.setAsset(new HkAssetEntity(assetDataBean.getId()));
                    assetIssueEntity.setIssuedUnits(assetDataBean.getUnits());
                    issueEntityList.add(assetIssueEntity);
                }
            }
        }

        if (issueEntityList != null && !issueEntityList.isEmpty()) {

            String assignerCode = hkLoginDataBean.getUserCode();
            String assignerName = null;
            if (hkLoginDataBean.getLastName() != null && !hkLoginDataBean.getLastName().isEmpty()) {
                assignerName = hkLoginDataBean.getFirstName() + " " + hkLoginDataBean.getLastName();
            } else {
                assignerName = hkLoginDataBean.getFirstName();
            }
            List<Long> notifyUserList = null;
//            List<String> featureNames = new ArrayList<>();
//            featureNames.add(HkSystemConstantUtil.Feature.MANAGE_ASSET);
//            Set<Long> userList = userManagementServiceWrapper.searchUsersByFeatureName(featureNames, hkLoginDataBean.getCompanyId());
            Set<Long> userList = userManagementServiceWrapper.retrieveRecipientIds(Arrays.asList(issueAssetDataBean.getIssueTo()));
//            System.out.println("List of user : "+userList);
            if (userList != null) {
                notifyUserList = new ArrayList<>();
                for (Long id : userList) {
                    notifyUserList.add(id);
                }
            }
            response = hkHRService.issueAssets(issueEntityList, employeeCode, employeeName, assignerCode, assignerName, departmentName, notifyUserList);
            List<Long> instanceIds = new ArrayList<>();
            Map<HkSystemConstantUtil.SectionNameForCustomField, List<CustomField>> map = new HashMap<>();
            Map<Long, Map<String, Object>> val = new HashMap<>();

            for (HkAssetIssueEntity hkAssetIssueEntity : issueEntityList) {
                instanceIds.add(hkAssetIssueEntity.getId());
                val.put(hkAssetIssueEntity.getId(), hkAssetIssueEntity.getIssueCustomData());
                List<String> uiFieldList = new ArrayList<>();
                if (!CollectionUtils.isEmpty(hkAssetIssueEntity.getDbTypeForIssue())) {
                    for (Map.Entry<String, String> entrySet : hkAssetIssueEntity.getDbTypeForIssue().entrySet()) {
                        uiFieldList.add(entrySet.getKey());
                    }
                }
                Map<String, String> uiFieldMap = fieldService.retrieveUIFieldNameWithComponentTypes(uiFieldList);
                //Pass this map to makecustomfieldService
                List<CustomField> makeCustomField = customFieldSevice.makeCustomField(val, hkAssetIssueEntity.getDbTypeForIssue(), uiFieldMap, HkSystemConstantUtil.FeatureNameForCustomField.ASSET.toString(), hkLoginDataBean.getCompanyId(), hkAssetIssueEntity.getId());
                //After that make Map of Section and there customfield
                map.put(HkSystemConstantUtil.SectionNameForCustomField.GENERAL, makeCustomField);
            }
            //Pass this map to customFieldSevice.saveOrUpdate method.
            customFieldSevice.saveAll(instanceIds, HkSystemConstantUtil.FeatureNameForCustomField.ISSUEASSET, hkLoginDataBean.getCompanyId(), map);
        }
        return response;
    }

    public HkAssetIssueEntity convertAssetIssueEntityToIssueAssetDataBean(HkAssetIssueEntity assetIssueEntity, IssueAssetDataBean issueAssetDataBean, String employeeCode, String employeeName, String departmentName) throws GenericDatabaseException {
        if (assetIssueEntity == null) {
            assetIssueEntity = new HkAssetIssueEntity();
        }
        assetIssueEntity.setCreatedBy(hkLoginDataBean.getId());
        assetIssueEntity.setCreatedOn(new Date());
        assetIssueEntity.setLastModifiedBy(hkLoginDataBean.getId());
        assetIssueEntity.setLastModifiedOn(new Date());
        assetIssueEntity.setFranchise(hkLoginDataBean.getCompanyId());
        assetIssueEntity.setIsArchive(false);
        assetIssueEntity.setIssuedOn(HkSystemFunctionUtil.convertToServerDate(issueAssetDataBean.getIssuedOn(), hkLoginDataBean.getClientRawOffsetInMin()));
        String[] issueTo = issueAssetDataBean.getIssueTo().split(":");
        assetIssueEntity.setIssueToInstance(Long.parseLong(issueTo[0]));
        assetIssueEntity.setIssueToType(issueTo[1]);
        if (assetIssueEntity.getIssueToType().equalsIgnoreCase(HkSystemConstantUtil.RecipientCodeType.EMPLOYEE)) {
            UMUser user = userService.retrieveUserById(assetIssueEntity.getIssueToInstance(), hkLoginDataBean.getCompanyId());
            employeeCode = user.getUserCode();
            if (user.getLastName() != null) {
                employeeName = user.getFirstName() + " " + user.getLastName();
            } else {
                employeeName = user.getFirstName();
            }
        }
        if (assetIssueEntity.getIssueToType().equalsIgnoreCase(HkSystemConstantUtil.RecipientCodeType.DEPARTMENT)) {
            UMDepartment department = departmentService.retrieveDepartmentById(assetIssueEntity.getIssueToInstance(), false, false, false);
            departmentName = department.getDeptName();
        }
        assetIssueEntity.setStatus(HkSystemConstantUtil.ASSET_STATUS.AVAILABLE);
        assetIssueEntity.setRemarks(issueAssetDataBean.getRemarks());
        assetIssueEntity.setIssueCustomData(issueAssetDataBean.getIssueCustomData());
        assetIssueEntity.setDbTypeForIssue(issueAssetDataBean.getDbTypeForIssue());
        return assetIssueEntity;
    }

    public String uploadFile(String fileName, String fileType) throws FileNotFoundException, IOException {

        String tempFileName = FolderManagement.getTempFileName(hkLoginDataBean.getCompanyId(), FolderManagement.FEATURE.ASSET, fileType, hkLoginDataBean.getId(), fileName);
//        System.out.println("tempFile name :  " + tempFileName);
//        FolderManagement.storeFileInTemp(tempFileName, file.getBytes(), false);
        if (sessionUtil.getFileuploadMap() != null) {
//            System.out.println("in if ............ ");
            sessionUtil.getFileuploadMap().put(tempFileName, fileName);
        } else {
            Map<String, String> fileUploadMap = new HashMap<String, String>();
            fileUploadMap.put(tempFileName, fileName);
            sessionUtil.setFileuploadMap(fileUploadMap);
        }
        return tempFileName;
    }

    public List<Long> retrieveChildrenFromParent(CategoryDataBean hkCategoryDataBean, List<Long> categoryIds) {
        categoryIds.add(hkCategoryDataBean.getId());
        if (hkCategoryDataBean.getChildren() != null) {
            for (CategoryDataBean tempHkCategoryDataBean : hkCategoryDataBean.getChildren()) {
                retrieveChildrenFromParent(tempHkCategoryDataBean, categoryIds);
            }
        }
        return categoryIds;
    }

    public void downloadDocument(HttpServletResponse response, String fileName) {
        try {
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment;filename =" + fileName.substring(fileName.indexOf("#") + 1, fileName.length()));
            String pathFromImageName = FolderManagement.getPathOfImage(fileName);
//            System.out.println("The total file name ....." + pathFromImageName);
            Path pathVarible = Paths.get(pathFromImageName);
            byte[] data = Files.readAllBytes(pathVarible);
            response.getOutputStream().write(data);
        } catch (IOException e) {
        }
    }

    public boolean removeCategory(Long categoryId) {
        return hkOperationsService.removeAssetCategory(categoryId);
    }

    public List<String> retrieveSameNameSuggestion(String assetName) {
        return hkOperationsService.retrieveSimilarNameSuggestion(assetName, hkLoginDataBean.getCompanyId());
    }

    public boolean doesSerialNumberExistForCategory(Integer serialNumber, Long category, Long skipAsset) {
        return hkOperationsService.doesSerialNumberAlreadyExistForCategory(serialNumber, category, skipAsset, hkLoginDataBean.getCompanyId());
    }

    public List<AssetDataBean> searchAsset(String assetName) {
        List<AssetDataBean> assetDataBeans = new ArrayList();
        List<HkAssetEntity> assetEntitys = hkOperationsService.retrieveAssetBasedonSearchCriteria(assetName, hkLoginDataBean.getCompanyId());
        if (assetEntitys != null && !assetEntitys.isEmpty()) {
            assetDataBeans = new ArrayList<>();
            for (HkAssetEntity hkAssetEntity : assetEntitys) {
                AssetDataBean assetDataBean = new AssetDataBean();
                if (hkAssetEntity.getAssetType() != null && hkAssetEntity.getAssetType().equalsIgnoreCase(HkSystemConstantUtil.ASSET_TYPE.MANAGED)) {
                    assetDataBean.setAssetType(true);
                } else {
                    assetDataBean.setAssetType(false);
                }
                assetDataBean.setId(hkAssetEntity.getId());
                assetDataBean.setModelName(hkAssetEntity.getAssetName());
                assetDataBean.setCategory(hkAssetEntity.getCategory().getId());
                assetDataBean.setParentCategory(hkAssetEntity.getCategory().getCategoryTitle());
                assetDataBean.setCanProduceImages(hkAssetEntity.getCanProduceImages());
                assetDataBeans.add(assetDataBean);
            }
        }
        return assetDataBeans;
    }

    public AssetDataBean retriveAssetById(Long id) {
        HkAssetEntity assetEntity = hkOperationsService.retrieveAsset(id);
        AssetDataBean assetDataBean = null;
        Map<HkSystemConstantUtil.SectionNameForCustomField, List<Map<Long, Map<String, Object>>>> retrieveDocumentByInstanceId = customFieldTransformerBean.retrieveDocumentByInstanceId(id, HkSystemConstantUtil.FeatureNameForCustomField.ASSET, hkLoginDataBean.getCompanyId());
        Map<Long, Map<HkSystemConstantUtil.SectionNameForCustomField, List<Map<Long, Map<String, Object>>>>> map = new HashMap<>();
        map.put(id, retrieveDocumentByInstanceId);
        if (assetEntity != null) {
            assetDataBean = this.convertHkAssetEntityToAssetDataBean(assetEntity, null, map);
        }
        return assetDataBean;
    }

    public AssetDataBean addCustomDataToAssetDb(AssetDataBean assetDataBean) {
        if (assetDataBean.getStatus().equals(HkSystemConstantUtil.ASSET_STATUS.ISSUED)) {
            if (assetDataBean.getIssueToVal() != null) {
                StringTokenizer tokenizer = new StringTokenizer(assetDataBean.getIssueToVal(), ":");
                if (tokenizer.countTokens() == 2) {
                    Long id = new Long(tokenizer.nextToken());
                    String type = tokenizer.nextToken();
                    if (type.equalsIgnoreCase("D")) {
                        UMDepartment department = userManagementServiceWrapper.retrieveDepartment(id);
                        assetDataBean.setIssueToName(department.getDeptName());
                    }
                    if (type.equalsIgnoreCase("E")) {
                        UMUser user = userService.retrieveUserById(id, hkLoginDataBean.getCompanyId());
                        if (user.getLastName() != null) {
                            assetDataBean.setIssueToName(user.getUserCode() + " - " + user.getFirstName() + " " + user.getLastName());
                        } else {
                            assetDataBean.setIssueToName(user.getUserCode() + " - " + user.getFirstName());
                        }
                    }
                }
            }
        }
        Map<HkSystemConstantUtil.SectionNameForCustomField, List<Map<Long, Map<String, Object>>>> retrieveDocumentByInstanceId = customFieldTransformerBean.retrieveDocumentByInstanceId(assetDataBean.getId(), HkSystemConstantUtil.FeatureNameForCustomField.ASSET, hkLoginDataBean.getCompanyId());
        if (!CollectionUtils.isEmpty(retrieveDocumentByInstanceId)) {
            List<Map<Long, Map<String, Object>>> maps = retrieveDocumentByInstanceId.get(HkSystemConstantUtil.SectionNameForCustomField.GENERAL);
            if (maps != null) {
                for (Map<Long, Map<String, Object>> map : maps) {
                    assetDataBean.setAddAssetData(map.get(assetDataBean.getId()));
                }
            }
        }
        return assetDataBean;
    }

    public void removeFileFromSession(String fileName) {
        if (!CollectionUtils.isEmpty(sessionUtil.getFileuploadMap()) && sessionUtil.getFileuploadMap().containsKey(fileName)) {
            sessionUtil.getFileuploadMap().remove(fileName);
        }
    }

    public List<UMUser> retrieveUsersByCompanyByStatus(Long companyId, String user, Boolean isActive) {
        List<UMUser> users = userManagementServiceWrapper.retrieveUsersByCompanyByStatus(companyId, user, isActive, true, hkLoginDataBean.getId());
        return users;
    }

    public List<SelectItem> getSelectItemListFromUserList(List<UMUser> umUsers) {
        List<SelectItem> hkSelectItems = new ArrayList<>();
        if (!CollectionUtils.isEmpty(umUsers)) {
            for (UMUser uMUser : umUsers) {
                StringBuilder name = new StringBuilder();
                if (uMUser.getUserCode() != null) {
                    name.append(uMUser.getUserCode()).append(" - ");
                }
                name.append(uMUser.getFirstName());
                if (uMUser.getLastName() != null) {
                    name.append(" ").append(uMUser.getLastName());
                }
                hkSelectItems.add(new SelectItem(uMUser.getId(),
                        name.toString(),
                        HkSystemConstantUtil.RecipientCodeType.EMPLOYEE));
            }
        }
        return hkSelectItems;
    }

    public List<UMDepartment> retrieveDepartmentsByCompanyByStatus(Long companyId, String department, Boolean isActive) {
        List<UMDepartment> departments = userManagementServiceWrapper.retrieveDepartmentsByCompanyByStatus(companyId, department, isActive);
        return departments;
    }

    /**
     * Raj: Generate barcode for asset
     *
     * @param payload
     * @return path of barcode file
     * @throws IOException
     */
    public String generateBarcode(String payload) throws IOException {
        String tempFileName = FolderManagement.getTempFileName(hkLoginDataBean.getCompanyId(), FolderManagement.FEATURE.ASSET, "Barcode", hkLoginDataBean.getId(), payload + "_barcode.jpg");
        Code128Bean code128Bean = new Code128Bean();
        //Output stream to which store barcode data
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        code128Bean.doQuietZone(true);
        try {
            // Set up the canvas provider for monochrome JPEG output
            BitmapCanvasProvider canvas = new BitmapCanvasProvider(out,
                    "image/jpeg", 350, BufferedImage.TYPE_BYTE_BINARY,
                    false, 0);
            // Generate the barcode
            code128Bean.generateBarcode(canvas, payload);
            // Signal end of generation
            canvas.finish();
            FolderManagement.storeFileInTemp(tempFileName, out.toByteArray(), false);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.close();
        }
        return tempFileName;
    }

    public void getFileDownload(HttpServletResponse response, String fileName) {
        try {
            if (fileName != null && !fileName.equals("")) {
                String pathFromImageName = FolderManagement.getPathOfImage(fileName + ".jpg");
                System.out.println("pathFromImageName :" + pathFromImageName);
                FileInputStream inputStream = new FileInputStream(pathFromImageName);
                byte[] byteFile = new byte[inputStream.available()];
                inputStream.read(byteFile);
                response.setContentType("image/jpeg");
                response.getOutputStream().write(byteFile);
                response.flushBuffer();
                response.getOutputStream().flush();
                response.getOutputStream().close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
