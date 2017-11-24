/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.usermanagement.controllers;

import com.argusoft.generic.database.exception.GenericDatabaseException;
import com.argusoft.hkg.common.constantutil.HkSystemConstantUtil;
import com.argusoft.hkg.common.constantutil.HkSystemConstantUtil.MasterCode;
import com.argusoft.hkg.web.base.PrimaryKey;
import com.argusoft.hkg.web.base.ResponseCode;
import com.argusoft.hkg.web.base.ResponseEntity;
import com.argusoft.hkg.web.base.controllers.BaseController;
import com.argusoft.hkg.web.usermanagement.databeans.EmployeeDataBean;
import com.argusoft.hkg.web.center.usermanagement.databeans.LoginDataBean;
import com.argusoft.hkg.web.usermanagement.transformers.DepartmentTransformerBean;
import com.argusoft.hkg.web.usermanagement.transformers.EmployeeTransformerBean;
import com.argusoft.hkg.common.functionutil.FolderManagement;
import com.argusoft.hkg.web.internationalization.transformers.LocalesTransformerBean;
import com.argusoft.hkg.web.usermanagement.databeans.DepartmentDataBean;
import com.argusoft.hkg.web.usermanagement.databeans.DesignationDataBean;
import com.argusoft.hkg.web.usermanagement.databeans.ProfileDataBean;
import com.argusoft.hkg.web.usermanagement.databeans.ThemeDataBean;
import com.argusoft.hkg.web.usermanagement.transformers.DesignationTransformerBean;
import com.argusoft.hkg.web.usermanagement.transformers.FranchiseTransformerBean;
import com.argusoft.hkg.web.usermanagement.transformers.LocationTransformerBean;
import com.argusoft.hkg.web.util.SelectItem;
import com.argusoft.usermanagement.common.exception.UMUserManagementException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
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
 * @author mansi
 */
@RestController
@RequestMapping("/employee")
public class EmployeeController extends BaseController<EmployeeDataBean, Long> {

    @Autowired
    EmployeeTransformerBean employeeTransformerBean;
    @Autowired
    DepartmentTransformerBean departmentTransformerBean;
    @Autowired
    DesignationTransformerBean designationTransformer;
    @Autowired
    LocationTransformerBean locationTransformerBean;
    @Autowired
    FranchiseTransformerBean franchiseTransformerBean;
    @Autowired
    LocalesTransformerBean localesTransformerBean;

    @Autowired
    LoginDataBean hkLoginDataBean;

    @RequestMapping(value = "/retrieve/combovaluesbykeycodes", method = RequestMethod.GET)
    public Map<String, List<SelectItem>> retrieveComboValuesOfEmpPage() {
        List<String> keyCodeList = new ArrayList<>();
        keyCodeList.add(MasterCode.BLOOD_GROUPS);
        keyCodeList.add(MasterCode.CASTES);
        keyCodeList.add(MasterCode.EDUCATIONAL_DEGREES);
        keyCodeList.add(MasterCode.OTHER_DETAILS_OF_EMPLOYEE);
        keyCodeList.add(MasterCode.EMPLOYEE_TYPES);
        keyCodeList.add(MasterCode.EDUCATION_MEDIUMS);
        keyCodeList.add(MasterCode.MARITAL_STATUS);
        keyCodeList.add(MasterCode.NATIONALITY);
        keyCodeList.add(MasterCode.FAMILY_OCCUPATIONS);
        keyCodeList.add(MasterCode.RELATIONS);
        keyCodeList.add(MasterCode.EXPERIENCE_DESIGNATION);
        keyCodeList.add(MasterCode.POLICY_COMPANY);
        keyCodeList.add(MasterCode.UNIVERSITY_LIST);
        Map<String, List<SelectItem>> retrieveComboValuesOfEmpPage = employeeTransformerBean.retrieveComboValuesOfKeyCodes(keyCodeList);
        return retrieveComboValuesOfEmpPage;
    }

    @Override
    public ResponseEntity<List<EmployeeDataBean>> retrieveAll() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ResponseEntity<EmployeeDataBean> retrieveById(@RequestBody PrimaryKey<Long> primaryKey) {
        try {
            EmployeeDataBean employeeBean = employeeTransformerBean.retrieveAllDetailsOfEmployeeById(primaryKey.getPrimaryKey(), true);
            return new ResponseEntity<>(employeeBean, ResponseCode.SUCCESS, null, null);
        } catch (UMUserManagementException | GenericDatabaseException ex) {
            Logger.getLogger(EmployeeController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public ResponseEntity<PrimaryKey<Long>> update(@Valid @RequestBody EmployeeDataBean employeeDataBean) {
        String response = employeeTransformerBean.updateEmployee(employeeDataBean);
        if (response.equalsIgnoreCase(HkSystemConstantUtil.SUCCESS)) {
            return new ResponseEntity<>(null, ResponseCode.SUCCESS, "Employee updated successfully", null, true);
        } else {
            return new ResponseEntity<>(null, ResponseCode.FAILURE, "Employee can not be updated", null, true);
        }
    }

    @RequestMapping(value = "/updateProfile", method = RequestMethod.POST)
    public ResponseEntity<PrimaryKey<Long>> updateProfile(@Valid @RequestBody ProfileDataBean profileDataBean) {
        String response = employeeTransformerBean.updateProfile(profileDataBean);
        if (response.equalsIgnoreCase(HkSystemConstantUtil.SUCCESS)) {
            return new ResponseEntity<>(null, ResponseCode.SUCCESS, "Profile updated successfully", null, true);
        } else {
            return new ResponseEntity<>(null, ResponseCode.FAILURE, "Profile can not be updated", null, true);
        }
    }
    
    @RequestMapping(value = "/retrieveByIdForTransfer", method = RequestMethod.POST)
    public ResponseEntity<EmployeeDataBean> retrieveByIdForTransfer(@RequestBody PrimaryKey<Long> primaryKey) {
        try {
            EmployeeDataBean employeeBean = employeeTransformerBean.retrieveAllDetailsOfEmployeeByIdForTransfer(primaryKey.getPrimaryKey(), true);
            System.out.println("retrieveByIdForTransfer :"+employeeBean);
            return new ResponseEntity<>(employeeBean, ResponseCode.SUCCESS, null, null);
        } catch (UMUserManagementException | GenericDatabaseException ex) {
            Logger.getLogger(EmployeeController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @RequestMapping(value = "/updateProfileOfSuperAdmin", method = RequestMethod.POST)
    public ResponseEntity<PrimaryKey<Long>> updateProfileOfSuperAdmin(@Valid @RequestBody Map<String, Object> superadminprofile) throws ParseException {
        String response = employeeTransformerBean.updateSuperAdminProfile(superadminprofile);
        if (response.equalsIgnoreCase(HkSystemConstantUtil.SUCCESS)) {
            return new ResponseEntity<>(null, ResponseCode.SUCCESS, "Profile updated successfully", null, true);
        } else {
            return new ResponseEntity<>(null, ResponseCode.FAILURE, "Profile can not be updated", null, true);
        }
    }

    @Override
    public ResponseEntity<PrimaryKey<Long>> create(@Valid @RequestBody EmployeeDataBean employeeDataBean) {
        String response = employeeTransformerBean.createEmployee(employeeDataBean);
        if (!response.equals(HkSystemConstantUtil.FAILURE)) {
            Map userIdMap = new HashMap<>();
            userIdMap.put("userId", "EMP_NM." + response);
            //System.out.println("Respons"+response);
            //System.out.println("UserIdMAp"+userIdMap);
            return new ResponseEntity<>(null, ResponseCode.SUCCESS, "Employee created successfully. Userid is {{userId}}.", userIdMap, null, true);
        } else {
            return new ResponseEntity<>(null, ResponseCode.FAILURE, "Employee can not be created", null, true);
        }
    }

    @Override
    public ResponseEntity<EmployeeDataBean> deleteById(PrimaryKey<Long> primaryKey) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public List<EmployeeDataBean> searchEmployee(@RequestParam("q") String search,@RequestParam(value = "extraparam",required = false) String extraparam) {
        Long franchiseId = null;
        Long status = null;
        Boolean isActive = Boolean.TRUE;
        if(!StringUtils.isEmpty(extraparam)){
            franchiseId = new Long(extraparam);
            status = 3l;
            isActive = Boolean.FALSE;
        }else{
            franchiseId = hkLoginDataBean.getCompanyId();
        }
        List<EmployeeDataBean> searchUser = employeeTransformerBean.searchUser(search,franchiseId,status,isActive);
        return searchUser;
    }
    
    @RequestMapping(value = "/terminate", method = RequestMethod.POST)
    public ResponseEntity<PrimaryKey<Long>> terminateEmployee(@RequestBody EmployeeDataBean employeeDataBean) throws ParseException {
        String response = employeeTransformerBean.updateEmployee(employeeDataBean);
        if (response.equalsIgnoreCase(HkSystemConstantUtil.SUCCESS)) {
            return new ResponseEntity<>(null, ResponseCode.SUCCESS, "" + employeeDataBean.getUserId() + " - " + employeeDataBean.getEmpName() + " has been marked for termination", null, true);
        } else {
            return new ResponseEntity<>(null, ResponseCode.FAILURE, "Employee can not be terminated", null, true);
        }
    }

    @RequestMapping(value = "/uploadfile", method = RequestMethod.POST)
    public ResponseEntity<Map> uploadFile(@RequestBody String[] info) throws FileNotFoundException, IOException {
        String fileName = info[0];
        String fileType = info[1];
        String filenameformat = employeeTransformerBean.uploadFile(fileName, fileType);
        Map result = new HashMap();
        result.put("filename", filenameformat);
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

    @RequestMapping(value = "/getprofilepicture/{login_id}", method = RequestMethod.GET)
    @ResponseBody
    public FileSystemResource getProfileImage(@PathVariable("login_id") Long id, HttpServletRequest request) {
        String fileName = employeeTransformerBean.getProfileFullPath(id);
        if (fileName != null && !fileName.equals("")) {
            return new FileSystemResource(fileName);
        } else {
            String retrieveAvatar = employeeTransformerBean.retrieveAvatar();
            if (retrieveAvatar != null) {
                return new FileSystemResource(retrieveAvatar);
            } else {
                return new FileSystemResource(request.getSession().getServletContext().getRealPath("images") + "/drmehta.gif");
            }
        }
    }

    @RequestMapping(value = "/doesempnameexist", method = RequestMethod.PUT, produces = {"application/json"})
    public ResponseEntity<Boolean> doesEmployeeNameExist(@RequestBody String name) throws GenericDatabaseException {
        return new ResponseEntity<>(employeeTransformerBean.doesEmployeeNameExist(name, hkLoginDataBean.getCompanyId()), ResponseCode.SUCCESS, null, null, false);
    }

    @RequestMapping(value = "/doesuseridexist", method = RequestMethod.PUT, produces = {"application/json"})
    public ResponseEntity<Boolean> doesUserIdExist(@RequestBody String name) throws GenericDatabaseException {
        return new ResponseEntity<>(employeeTransformerBean.doesUserIdExist(name), ResponseCode.SUCCESS, null, null, false);
    }

    @RequestMapping(value = "/retrieve/shiftsofdep", method = RequestMethod.POST)
    public ResponseEntity<List<SelectItem>> retrieveShiftOfDepartment(@RequestBody PrimaryKey<Long> primaryKey) {
        List<SelectItem> retrieveShiftByDepartment = employeeTransformerBean.retrieveShiftByDepartment(primaryKey.getPrimaryKey());
        return new ResponseEntity<>(retrieveShiftByDepartment, ResponseCode.SUCCESS, null, null);
    }

    @RequestMapping(value = "/retrieve/avatar", method = RequestMethod.GET)
    public FileSystemResource retrieveAvatar(HttpServletRequest request) {
        String retrieveAvatar = employeeTransformerBean.retrieveAvatar();
        if (retrieveAvatar != null) {
            return new FileSystemResource(retrieveAvatar);
        } else {
            return new FileSystemResource(request.getSession().getServletContext().getRealPath("images") + "/drmehta.gif");
        }
    }

    @RequestMapping(value = "/changepassword", method = RequestMethod.POST, consumes = {"application/json"})
    public ResponseEntity<PrimaryKey<Long>> changePassword(@RequestBody Map map) {
        String id = (String) map.get("id");
        String password = (String) map.get("password");
        String userid = (String) map.get("userid");
        Long companyId = hkLoginDataBean.getCompanyId();
        String response = employeeTransformerBean.changePassword(new Long(id), companyId, password, userid);
        if (HkSystemConstantUtil.SUCCESS.equalsIgnoreCase(response)) {
            return new ResponseEntity<>(null, ResponseCode.SUCCESS, "Password changed successfully.", null, true);
        } else {
            return new ResponseEntity<>(null, ResponseCode.FAILURE, "Password could not be changed.", null, true);
        }
    }

    @RequestMapping(value = "/removeFileFromTemp", method = RequestMethod.POST, consumes = {"application/json"})
    @ResponseStatus(value = HttpStatus.OK)
    public void removeFileFromTemp(@RequestBody String name) throws IOException {
        FolderManagement.removeFile(name);
    }

    @RequestMapping(value = "/download", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public void download(HttpServletResponse response, HttpServletRequest httpRequest) {
        Map<String, String[]> parameterMap = httpRequest.getParameterMap();
        String[] fileNames = parameterMap.get("filename");
        String fileName = fileNames[0];
        try {
            response.setContentType("application/octet-stream");
            //System.out.println("Start index...0" + fileName.indexOf(FolderManagement.NAME_SEPARATOR) + 1);
            //System.out.println("End index" + fileName.length());
            //System.out.println("The file name is...." + fileName);
            //System.out.println("Substring<<<<<<<<<" + fileName.substring(fileName.indexOf(FolderManagement.NAME_SEPARATOR) + 1, fileName.length()));
            String headerFileName = fileName.substring(fileName.indexOf(FolderManagement.NAME_SEPARATOR) + 1, fileName.length());
            //System.out.println("Header" + headerFileName);
            response.setHeader("Content-Disposition", "attachment;filename =" + headerFileName);

            String pathFromImageName = FolderManagement.getPathOfImage(fileName);
            //System.out.println("The file name 4444 is...." + fileName);
            Path pathVarible = Paths.get(pathFromImageName);
            byte[] data = Files.readAllBytes(pathVarible);
            response.getOutputStream().write(data);
        } catch (IOException e) {
        }
    }

    @Override
    public ResponseEntity<Map<String, Object>> retrievePrerequisite() {
        Map<String, Object> prerequisiteMap = new HashMap();
        Map<String, List<SelectItem>> comboValues = retrieveComboValuesOfEmpPage();
        List<DesignationDataBean> designations = designationTransformer.retrieveDesignations(true);

        List<SelectItem> locations = locationTransformerBean.retrieveLocationHeirarchyByType("T", Boolean.TRUE);
        List<SelectItem> fillStatusList = employeeTransformerBean.fillEmployeeStatusList();
        List<SelectItem> franchiseNames = franchiseTransformerBean.retrieveAllFranchiseNames();
        List<DepartmentDataBean> departments = departmentTransformerBean.retrieveDepartmentListInTreeViewSimple(hkLoginDataBean.getId());
        Map<String, String> emp = employeeTransformerBean.retrieveEmployeeConfiguration();
        Map<String, String> retrieveDOBConfiguration = employeeTransformerBean.retrieveDOBConfiguration();
        prerequisiteMap.put("combovalues", comboValues);
        prerequisiteMap.put("designations", designations);
        prerequisiteMap.put("locations", locations);
        prerequisiteMap.put("franchise", franchiseNames);
        prerequisiteMap.put("departments", departments);
        prerequisiteMap.put("employeeConfig", emp);
        prerequisiteMap.put("agelimit", retrieveDOBConfiguration);
        prerequisiteMap.put("employeeStatus", fillStatusList);

        return new ResponseEntity<>(prerequisiteMap, ResponseCode.SUCCESS, "", null, true);
    }

    @RequestMapping(value = "/profile/retrieveprerequisite", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> retrievePrerequisiteForProfile() throws UMUserManagementException, GenericDatabaseException {
        Map<String, Object> prerequisiteMap = new HashMap();
        Map<String, List<SelectItem>> comboValues = retrieveComboValuesOfEmpPage();
        List<SelectItem> locations = locationTransformerBean.retrieveLocationHeirarchyByType("T", Boolean.TRUE);
        EmployeeDataBean employeeBean = employeeTransformerBean.retrieveAllDetailsOfEmployeeById(hkLoginDataBean.getId(), false);
        Map<String, String> emp = employeeTransformerBean.retrieveEmployeeConfiguration();
        Map<String, String> retrieveDOBConfiguration = employeeTransformerBean.retrieveDOBConfiguration();
        List<SelectItem> languages = localesTransformerBean.retrieveLanguages();
        prerequisiteMap.put("combovalues", comboValues);
        prerequisiteMap.put("locations", locations);
        prerequisiteMap.put("employeedetail", employeeBean);
        prerequisiteMap.put("employeeConfig", emp);
        prerequisiteMap.put("agelimit", retrieveDOBConfiguration);
        prerequisiteMap.put("languages", languages);
        return new ResponseEntity<>(prerequisiteMap, ResponseCode.SUCCESS, "", null, true);
    }

    @RequestMapping(value = "/getwallpaper", method = RequestMethod.GET)
    public String getWallpaper(HttpServletRequest request) throws IOException, GenericDatabaseException {
        String background = employeeTransformerBean.getBackground();
        return background;
    }

    @RequestMapping(value = "/retrievethemes", method = RequestMethod.GET)
    public List<ThemeDataBean> retrieveThemes() throws IOException {
        List<ThemeDataBean> retrieveAllThemes = employeeTransformerBean.retrieveAllThemes(HkSystemConstantUtil.ACTIVE);
        return retrieveAllThemes;
    }

    @RequestMapping(value = "/settheme", method = RequestMethod.POST)
    public ResponseEntity<PrimaryKey<Long>> setTheme(@RequestBody ThemeDataBean dataBean) throws IOException, GenericDatabaseException {
        employeeTransformerBean.setTheme(dataBean);
        return new ResponseEntity<>(null, ResponseCode.SUCCESS, "Theme applied successfully", null, true);
    }

    @RequestMapping(value = "/createphotogallery", method = RequestMethod.POST)
    public ResponseEntity<PrimaryKey<Long>> createPhotoGallery(@RequestBody Map<String, String> map) throws IOException {
        String file = map.get("file");
        String save = map.get("store");
        Boolean store = false;
        if (save != null) {
            store = true;
        }
        employeeTransformerBean.createPhotoGallery(file, store);
        return new ResponseEntity<>(null, ResponseCode.SUCCESS, "Wallpaper set successfully.", null);
    }

    @RequestMapping(value = "/retrieveimagethumbnailpaths", method = RequestMethod.GET)
    public ResponseEntity<Map<String, List<String>>> retrieveImageThumbnailPaths() throws IOException {
        Map<String, List<String>> retrieveImagePaths = employeeTransformerBean.retrieveImagePaths(hkLoginDataBean.getId());
        return new ResponseEntity<>(retrieveImagePaths, ResponseCode.SUCCESS, null, null);
    }

    @RequestMapping(value = "/retrieveDesgByDept", method = RequestMethod.POST)
    public List<SelectItem> retrieveDesignationByDepartment(@RequestBody Long deptId) {
        List<SelectItem> desgList = employeeTransformerBean.retrieveDesignationByDepartment(deptId);
        return desgList;
    }
}
