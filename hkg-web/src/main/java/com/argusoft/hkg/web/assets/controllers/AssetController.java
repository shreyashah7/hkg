/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.assets.controllers;

import com.argusoft.generic.database.exception.GenericDatabaseException;
import com.argusoft.hkg.common.constantutil.HkSystemConstantUtil;
import com.argusoft.hkg.common.functionutil.FolderManagement;
import com.argusoft.hkg.core.util.CategoryType;
import com.argusoft.hkg.web.assets.databeans.AssetDataBean;
import com.argusoft.hkg.web.assets.databeans.CategoryDataBean;
import com.argusoft.hkg.web.assets.databeans.IssueAssetDataBean;
import com.argusoft.hkg.web.assets.transformers.AssetTransformerBean;
import com.argusoft.hkg.web.assets.transformers.CategoryTransformerBean;
import com.argusoft.hkg.web.base.PrimaryKey;
import com.argusoft.hkg.web.base.ResponseCode;
import com.argusoft.hkg.web.base.ResponseEntity;
import com.argusoft.hkg.web.base.controllers.BaseController;
import com.argusoft.hkg.web.center.usermanagement.databeans.LoginDataBean;
import com.argusoft.hkg.web.util.SelectItem;
import com.argusoft.hkg.web.util.SessionUtil;
import com.argusoft.usermanagement.common.model.UMDepartment;
import com.argusoft.usermanagement.common.model.UMUser;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
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
 * @author dhwani
 *
 */
@RestController
@RequestMapping("/asset")
public class AssetController extends BaseController<AssetDataBean, Long> {

    @Autowired
    CategoryTransformerBean categoryTransformerBean;
    @Autowired
    AssetTransformerBean assetTransformerBean;
    @Autowired
    LoginDataBean loginDataBean;
    @Autowired
    SessionUtil sessionUtil;

    @RequestMapping(value = "/issueasset", method = RequestMethod.POST, consumes = {"application/json"})
    public ResponseEntity<String> issueAsset(@Valid @RequestBody IssueAssetDataBean issueAssetDataBean) throws GenericDatabaseException {
        Boolean response = assetTransformerBean.issueAsset(issueAssetDataBean);
        if (response) {
            return new ResponseEntity<String>(null, ResponseCode.SUCCESS, "Issued asset successfully", null, true);
        } else {
            return new ResponseEntity<String>(null, ResponseCode.FAILURE, "Asset can not be issued", null);
        }
    }

    @RequestMapping(value = "/getimage/{name}", method = RequestMethod.GET)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public void getFile(@PathVariable("name") String fileName, HttpServletRequest request, HttpServletResponse response) {
        assetTransformerBean.getFileDownload(response, fileName);
    }

    @RequestMapping(value = "/createcategory", method = RequestMethod.POST, consumes = {"application/json"})
    public ResponseEntity<Map> createCategory(@Valid @RequestBody CategoryDataBean categoryDataBean) {
        Map map = new LinkedHashMap<>();
        if (categoryTransformerBean.doesCategoryNameExist(categoryDataBean.getDisplayName(), CategoryType.ASSET, null)) {
            map.put("categoryExsist", categoryDataBean.getDisplayName() + " already taken.");
            return new ResponseEntity<>(map, ResponseCode.WARNING, null, null, false);
        } else if (categoryTransformerBean.doesCategoryPrefixExsist(categoryDataBean.getCategoryPrefix(), CategoryType.ASSET, null)) {
            map.put("prefixExsist", categoryDataBean.getCategoryPrefix() + " already taken.");
            return new ResponseEntity<>(map, ResponseCode.WARNING, null, null, false);
        } else if (categoryTransformerBean.createCategory(categoryDataBean, CategoryType.ASSET)) {
            return new ResponseEntity<>(null, ResponseCode.SUCCESS, "Category created successfully", null, true);
        } else {
            return new ResponseEntity<>(null, ResponseCode.FAILURE, "Category can not be created", null);
        }

    }

    @RequestMapping(value = "/retrievecategories", method = RequestMethod.GET, produces = {"application/json"})
    public List<CategoryDataBean> retrieveAssetCategories() {
        return categoryTransformerBean.retrieveCategoryList(CategoryType.ASSET, false);
    }

    @RequestMapping(value = "/updatecategory", method = RequestMethod.POST, consumes = {"application/json"})
    public ResponseEntity<String> updateCategory(@Valid @RequestBody CategoryDataBean categoryDataBean) {
        if (categoryTransformerBean.doesCategoryNameExist(categoryDataBean.getDisplayName(), CategoryType.ASSET, categoryDataBean.getId())) {
            return new ResponseEntity<>(null, ResponseCode.WARNING, categoryDataBean.getDisplayName() + " already taken.", null, false);
        } else if (categoryTransformerBean.updateCategory(categoryDataBean, CategoryType.ASSET)) {
            return new ResponseEntity<String>(null, ResponseCode.SUCCESS, "Category updated successfully", null, true);
        } else {
            return new ResponseEntity<String>(null, ResponseCode.FAILURE, "Category can not be updated", null, true);
        }
    }

    @RequestMapping(value = "/removecategory", method = RequestMethod.POST, consumes = {"application/json"})
    public ResponseEntity<String> removeCategory(@RequestBody CategoryDataBean categoryDataBean) {
        if (assetTransformerBean.removeCategory(categoryDataBean.getId())) {
            return new ResponseEntity<String>(null, ResponseCode.SUCCESS, "Category updated successfully", null, true);
        } else {
            return new ResponseEntity<String>(null, ResponseCode.FAILURE, "Category can not be updated", null, true);
        }
    }

    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    public ResponseEntity<Map> uploadFile(@RequestBody String[] info) throws FileNotFoundException, IOException {
        String fileName = info[0];
        String fileType = info[1];
        String filenameformat = assetTransformerBean.uploadFile(fileName, fileType);
        Map result = new HashMap();
        result.put("filename", filenameformat);
        return new ResponseEntity<>(result, ResponseCode.SUCCESS, "", null);
    }

    @RequestMapping(value = "/retrieveassets", method = RequestMethod.POST, produces = {"application/json"})
    public List<AssetDataBean> retrieveAssets(@RequestBody CategoryDataBean category) {
        return assetTransformerBean.retrieveAssetDataBean(category.getId(), false);
    }

    @RequestMapping(value = "/retrieveassetsforissue", method = RequestMethod.POST, produces = {"application/json"})
    public ResponseEntity<List<AssetDataBean>> retrieveAssetsForIssue(@RequestBody CategoryDataBean category) {
        return new ResponseEntity<>(assetTransformerBean.retrieveAssetDataBean(category.getId(), true), null, null, null);
    }

    @RequestMapping(value = "/retrievemanifacturer", method = RequestMethod.GET, produces = {"application/json"})
    public List<SelectItem> retrieveManifacturer() {
        List<SelectItem> retrieveComboValuesOfManifacturer = assetTransformerBean.retrieveComboValuesOfManifacturer(HkSystemConstantUtil.MasterCode.MANIFACTURER);
        return retrieveComboValuesOfManifacturer;
    }

    @RequestMapping(value = "/cancelform", method = RequestMethod.POST, produces = {"application/json"})
    @ResponseStatus(value = HttpStatus.OK)
    public void cancelForm() {
        if (sessionUtil.getFileuploadMap() != null) {
            sessionUtil.setFileuploadMap(null);
        }
    }

    @RequestMapping(value = "/downloadDocument", method = RequestMethod.GET, produces = {"application/vnd.ms-excel"})
    @ResponseStatus(value = HttpStatus.OK)
    public void downloadHolidayTemplate(HttpServletResponse response, @RequestParam("filename") String fileName) {
        assetTransformerBean.downloadDocument(response, fileName);
    }

    @RequestMapping(value = "/fillstatuslist", method = RequestMethod.POST, produces = {"application/json"})
    public List<SelectItem> fillStatusList(@RequestBody String status) {
        return assetTransformerBean.fillStatusList(status);
    }

    @Override
    public ResponseEntity<List<AssetDataBean>> retrieveAll() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ResponseEntity<AssetDataBean> retrieveById(PrimaryKey<Long> primaryKey) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ResponseEntity<PrimaryKey<Long>> update(@RequestBody AssetDataBean assetDataBean) {
        try {
            if (assetTransformerBean.doesSerialNumberExistForCategory(Integer.parseInt(assetDataBean.getSerialNumber()), assetDataBean.getCategory(), assetDataBean.getId())) {
                return new ResponseEntity<>(null, ResponseCode.WARNING, assetDataBean.getSerialNumber() + " already taken.", null, false);
            } else if (assetTransformerBean.updateAsset(assetDataBean)) {
                return new ResponseEntity<>(null, ResponseCode.SUCCESS, "Asset updated successfully", null, true);
            } else {
                return new ResponseEntity<>(null, ResponseCode.FAILURE, "Asset can not be updated", null, true);
            }
        } catch (IOException ex) {
            return new ResponseEntity<>(null, ResponseCode.FAILURE, "Asset can not be updated", null, true);
        }
    }

    @Override
    public ResponseEntity<PrimaryKey<Long>> create(@Valid @RequestBody AssetDataBean assetDataBean) {
        if (assetDataBean.isAssetType() && assetTransformerBean.doesSerialNumberExistForCategory(Integer.parseInt(assetDataBean.getSerialNumber()), assetDataBean.getCategory(), null)) {
            return new ResponseEntity<>(null, ResponseCode.WARNING, assetDataBean.getSerialNumber() + " already taken.", null, false);
        } else {
            try {
                if (assetTransformerBean.createAsset(assetDataBean)) {
                    return new ResponseEntity<>(null, ResponseCode.SUCCESS, "Asset added successfully", null, true);
                } else {
                    return new ResponseEntity<>(null, ResponseCode.FAILURE, "Asset can not be added", null, true);
                }
            } catch (IOException ex) {
                Logger.getLogger(AssetController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return new ResponseEntity<>(null, ResponseCode.FAILURE, "Asset can not be added", null, true);
    }

    @Override
    public ResponseEntity<AssetDataBean> deleteById(PrimaryKey<Long> primaryKey) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @RequestMapping(value = "/retrievesamenamesuggestion", method = RequestMethod.POST)
    public ResponseEntity<List<String>> retrieveSameNameSuggestion(@RequestBody String assetName) {
        return new ResponseEntity<>(assetTransformerBean.retrieveSameNameSuggestion(assetName), ResponseCode.SUCCESS, null, null, false);
    }

    @RequestMapping(value = "/doesserialnumberexist", method = RequestMethod.POST)
    public ResponseEntity<Boolean> doesSerialNumberExistForCategory(@RequestBody AssetDataBean assetDataBean) {
        if (assetTransformerBean.doesSerialNumberExistForCategory(Integer.parseInt(assetDataBean.getSerialNumber()), assetDataBean.getCategory(), assetDataBean.getId())) {
            return new ResponseEntity<>(Boolean.TRUE, ResponseCode.WARNING, assetDataBean.getSerialNumber() + " already taken.", null, false);
        } else {
            return new ResponseEntity<>(Boolean.FALSE, ResponseCode.SUCCESS, null, null, false);
        }
    }

    @RequestMapping(value = "/searchByAssetName", method = RequestMethod.GET, produces = {"application/json"})
    public List<AssetDataBean> searchByAssetName(@RequestParam("q") String assetName) {
        List<AssetDataBean> assetDataBeans = assetTransformerBean.searchAsset(assetName);
        return assetDataBeans;
    }

    @RequestMapping(value = "/retriveAssetById", method = RequestMethod.POST, produces = {"application/json"})
    public AssetDataBean retriveAssetById(@RequestBody Long id) {
        return assetTransformerBean.retriveAssetById(id);
    }

    @RequestMapping(value = "/addCustomDataToCategoryDataBean", method = RequestMethod.POST)
    public CategoryDataBean addCustomDataToCategoryDataBean(@RequestBody CategoryDataBean categoryDataBean) {
        return categoryTransformerBean.addCustomDataToCategoryDataBean(categoryDataBean);
    }

    @Override
    public ResponseEntity<Map<String, Object>> retrievePrerequisite() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @RequestMapping(value = "/addCustomDataToAssetDataBean", method = RequestMethod.POST)
    public AssetDataBean addCustomDataToAssetDataBean(@RequestBody AssetDataBean assetDataBean) {
        return assetTransformerBean.addCustomDataToAssetDb(assetDataBean);
    }

    @RequestMapping(value = "/removeFileFromSession", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    public void removeFileFromSession(@RequestBody String fileName) {
        assetTransformerBean.removeFileFromSession(fileName);
    }

    @RequestMapping(value = "/retrieveusers", method = RequestMethod.POST, consumes = {"application/json"})
    public ResponseEntity<List<SelectItem>> retrieveUsers(@RequestBody(required = false) String user) {
        Long companyId = loginDataBean.getCompanyId();
        List<UMUser> umUsers = assetTransformerBean.retrieveUsersByCompanyByStatus(companyId, user, Boolean.TRUE);
        List<SelectItem> hkSelectItems = assetTransformerBean.getSelectItemListFromUserList(umUsers);
        return new ResponseEntity<>(hkSelectItems, ResponseCode.SUCCESS, "", null);
    }

    @RequestMapping(value = "/retrieveDepartmentList", method = RequestMethod.POST, consumes = {"application/json"})
    public ResponseEntity<List<SelectItem>> retrieveDepartmentList(@RequestBody(required = false) String department) {

        Long companyId = loginDataBean.getCompanyId();
        List<UMDepartment> uMDepartments = assetTransformerBean.retrieveDepartmentsByCompanyByStatus(companyId, department, Boolean.TRUE);
        List<SelectItem> hkSelectItems = new ArrayList<>();
        if (!CollectionUtils.isEmpty(uMDepartments)) {
            for (UMDepartment uMDepartment : uMDepartments) {
                hkSelectItems.add(new SelectItem(uMDepartment.getId(), uMDepartment.getDeptName(), HkSystemConstantUtil.RecipientCodeType.DEPARTMENT));
            }
        }
        return new ResponseEntity<>(hkSelectItems, ResponseCode.SUCCESS, "", null);
    }

    @RequestMapping(value = "/generateBarcode", method = RequestMethod.POST)
    public Map<String, String> generateBarcode(@RequestBody String payload) throws IOException {
        if (!StringUtils.isEmpty(payload)) {
            String generateBarcode = assetTransformerBean.generateBarcode(payload);
            Map<String, String> tempFilePath = new HashMap<>();
            tempFilePath.put("tempFilePath", generateBarcode);
            return tempFilePath;
        }
        return null;
    }

}
