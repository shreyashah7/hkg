/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.assets.transformers;

import com.argusoft.hkg.common.constantutil.HkSystemConstantUtil;
import com.argusoft.hkg.core.HKCategoryService;
import com.argusoft.hkg.core.HkEmployeeService;
import com.argusoft.hkg.core.HkFieldService;
import com.argusoft.hkg.core.HkFoundationService;
import com.argusoft.hkg.core.HkHRService;
import com.argusoft.hkg.model.HkCategoryEntity;
import com.argusoft.hkg.nosql.core.HkCustomFieldService;
import com.argusoft.hkg.nosql.model.CustomField;
import com.argusoft.hkg.core.util.CategoryType;
import com.argusoft.hkg.web.assets.databeans.CategoryDataBean;
import com.argusoft.hkg.web.customfield.transformers.CustomFieldTransformerBean;
import com.argusoft.hkg.web.center.usermanagement.databeans.LoginDataBean;
import com.argusoft.hkg.web.usermanagement.wrapper.UserManagementServiceWrapper;
import com.argusoft.hkg.web.util.SelectItem;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 *
 * @author dhwani
 */
@Service
public class CategoryTransformerBean {

    @Autowired
    HKCategoryService hKCategoryService;
    @Autowired
    LoginDataBean loginDataBean;
    @Autowired
    HkFoundationService hkOperationsService;
    @Autowired
    HkEmployeeService hkEmployeeService;
    @Autowired
    HkHRService hrService;
    @Autowired
    HkCustomFieldService customFieldSevice;
    @Autowired
    HkFieldService fieldService;

    @Autowired
    UserManagementServiceWrapper userManagementServiceWrapper;

    @Autowired
    CustomFieldTransformerBean customFieldTransformerBean;

    List<CategoryDataBean> sortedhkCategoryDataBean = null;
    Map<Long, CategoryDataBean> idToCategoryDatabeanMap = null;
    Map<Long, Integer> categoryAssetMap = null;
    Map<Long, Integer> categoryTaskMap = null;

    public List<CategoryDataBean> retrieveCategoryList(CategoryType categoryType, boolean onlyActiveEventCategoryRequired) {
        List<CategoryDataBean> hkCategoryDataBeanList = null;
        List<CategoryDataBean> hkCategoryDataBeanFinalList = null;
        List<HkCategoryEntity> hkCategoryEntityList = null;
        Map<HkCategoryEntity, Integer> eventCategoryMap = null;
//        Map<Long, Map<HkSystemConstantUtil.SectionNameForCustomField, List<Map<Long, Map<String, Object>>>>> retrieveDocumentByInstanceIds = customFieldSevice.retrieveDocumentByInstanceIds(null, HkSystemConstantUtil.FeatureNameForCustomField.CATEGORY, loginDataBean.getCompanyId());
        if (categoryType.equals(CategoryType.ASSET)) {
            hkCategoryEntityList = hKCategoryService.retrieveAllCategories(loginDataBean.getCompanyId(), categoryType);
            categoryAssetMap = hkOperationsService.retrieveAssetCategorywiseCount(loginDataBean.getCompanyId());
        } else if (categoryType.equals(CategoryType.EVENT)) {
            //It will return only active event category
            if (onlyActiveEventCategoryRequired) {
                eventCategoryMap = hrService.retrieveActiveEventsCount(loginDataBean.getCompanyId());
            } else {
                //Return all event categories
                hkCategoryEntityList = hKCategoryService.retrieveAllCategories(loginDataBean.getCompanyId(), categoryType);
            }
        } else if (categoryType.equals(CategoryType.TASK)) {
            hkCategoryEntityList = hKCategoryService.retrieveAllCategoriesByUser(loginDataBean.getCompanyId(), categoryType, loginDataBean.getId());
            categoryTaskMap = hkEmployeeService.retrievePendingTaskCountGroupedByCategory(loginDataBean.getId(), loginDataBean.getCompanyId(), false);
        }

//        System.out.println("Category map size : " + categoryAssetMap.size());
        if (!CollectionUtils.isEmpty(hkCategoryEntityList) || !CollectionUtils.isEmpty(eventCategoryMap)) {
            sortedhkCategoryDataBean = new ArrayList<>();
            idToCategoryDatabeanMap = new HashMap<>();
            hkCategoryDataBeanFinalList = new ArrayList<>();
            hkCategoryDataBeanList = new ArrayList<>();

            //convert model to databean first
            if (categoryType.equals(CategoryType.ASSET)) {
                for (HkCategoryEntity hkCategoryEntity1 : hkCategoryEntityList) {
                    Integer categoryCount = 0;
                    if (categoryAssetMap != null && !categoryAssetMap.isEmpty() && categoryAssetMap.containsKey(hkCategoryEntity1.getId())) {
                        categoryCount = categoryAssetMap.get(hkCategoryEntity1.getId());
                    }
                    hkCategoryDataBeanList.add(this.convertCategoryEntityToCategoryDataBean(hkCategoryEntity1, new CategoryDataBean(), categoryType, categoryCount, null));
                }
            } else if (categoryType.equals(CategoryType.TASK)) {
                for (HkCategoryEntity hkCategoryEntitys : hkCategoryEntityList) {
                    Integer categoryCount = 0;
                    if (categoryTaskMap != null && !categoryTaskMap.isEmpty() && categoryTaskMap.containsKey(hkCategoryEntitys.getId())) {
                        categoryCount = categoryTaskMap.get(hkCategoryEntitys.getId());
                    }
                    hkCategoryDataBeanList.add(this.convertCategoryEntityToCategoryDataBean(hkCategoryEntitys, new CategoryDataBean(), categoryType, categoryCount, null));
                }
            } else if (categoryType.equals(CategoryType.EVENT)) {
                if (onlyActiveEventCategoryRequired) {
                    for (Map.Entry<HkCategoryEntity, Integer> eventCategory : eventCategoryMap.entrySet()) {
                        HkCategoryEntity hkCategoryEntity = eventCategory.getKey();
                        hkCategoryDataBeanList.add(this.convertCategoryEntityToCategoryDataBean(hkCategoryEntity, new CategoryDataBean(), categoryType, eventCategory.getValue(), null));
                    }
                } else {
                    for (HkCategoryEntity hkCategoryEntity1 : hkCategoryEntityList) {
                        hkCategoryDataBeanList.add(this.convertCategoryEntityToCategoryDataBean(hkCategoryEntity1, new CategoryDataBean(), categoryType, 0, null));
                    }
                }
            }
            //made a map of id and associated databean
            for (CategoryDataBean hkCategoryDataBean : hkCategoryDataBeanList) {
                idToCategoryDatabeanMap.put(hkCategoryDataBean.getId(), hkCategoryDataBean);
            }

            // sort accordingly
            // see if parent is added in list, then add its children
            for (CategoryDataBean hkCategoryDataBean : hkCategoryDataBeanList) {
                addCategoryDatabeanToSortedList(hkCategoryDataBean);
            }

            // set children list to parent and add root nodes to rootList
            for (CategoryDataBean categoryDataBean : sortedhkCategoryDataBean) {
                long parentId = categoryDataBean.getParentId();
                if (parentId != 0) {
                    CategoryDataBean parentDataBean = idToCategoryDatabeanMap.get(parentId);
                    if (parentDataBean != null) {
                        List<CategoryDataBean> childrenOfparent = parentDataBean.getChildren();
                        if (childrenOfparent == null) {
                            childrenOfparent = new ArrayList<>();
                            parentDataBean.setChildren(childrenOfparent);
                        }

                        childrenOfparent.add(categoryDataBean);
                    }
                } else {
                    hkCategoryDataBeanFinalList.add(categoryDataBean);
                }
            }

        }

        // added - dmehta for sorting asset tree list
        if (categoryType.equals(CategoryType.ASSET)) {
            if (hkCategoryDataBeanFinalList.size() > 0) {
                for (CategoryDataBean categoryDataBean : hkCategoryDataBeanFinalList) {
                    if (!CollectionUtils.isEmpty(categoryDataBean.getChildren())) {
                        Collections.sort(categoryDataBean.getChildren(), new Comparator<CategoryDataBean>() {
                            @Override
                            public int compare(final CategoryDataBean object1, final CategoryDataBean object2) {
                                return object1.getDisplayName().toLowerCase().compareTo(object2.getDisplayName().toLowerCase());
                            }
                        });

                    }
                }
                Collections.sort(hkCategoryDataBeanFinalList, new Comparator<CategoryDataBean>() {
                    @Override
                    public int compare(final CategoryDataBean object1, final CategoryDataBean object2) {
                        return object1.getDisplayName().toLowerCase().compareTo(object2.getDisplayName().toLowerCase());
                    }
                });
            }
        }

        return hkCategoryDataBeanFinalList;
    }

    public List<SelectItem> searchExistingCategoryNames(String categoryName, CategoryType categoryType) {
        List<String> categorySuggestions = hKCategoryService.searchExistingCategoryNames(categoryName, loginDataBean.getCompanyId(), categoryType);
        List<SelectItem> categorySuggestionList = new ArrayList<>();
        if (categorySuggestions != null && !categorySuggestions.isEmpty()) {
            for (String categorySuggestion : categorySuggestions) {
                SelectItem task = new SelectItem(categorySuggestion, categorySuggestion);
                categorySuggestionList.add(task);
            }
        }
        return categorySuggestionList;
    }

    public CategoryDataBean convertCategoryEntityToCategoryDataBean(HkCategoryEntity hkCategoryEntity, CategoryDataBean hkCategoryDataBean, CategoryType categoryType, Integer categoryCount, Map<Long, Map<HkSystemConstantUtil.SectionNameForCustomField, List<Map<Long, Map<String, Object>>>>> retrieveDocumentByInstanceIds) {
        if (hkCategoryDataBean == null) {
            hkCategoryDataBean = new CategoryDataBean();
        }
        String newNum = null;
        if (hkCategoryEntity.getCategoryPattern() != null) {
            hkCategoryDataBean.setPattern(hkCategoryEntity.getCategoryPattern());
            NumberFormat nf = new DecimalFormat(hkCategoryEntity.getCategoryPattern());
            newNum = nf.format(hkCategoryEntity.getCurrentIndex());
        } else {
            hkCategoryDataBean.setPattern("00");
            NumberFormat nf = new DecimalFormat("00");
            newNum = nf.format(hkCategoryEntity.getCurrentIndex());
        }
        hkCategoryDataBean.setStartIndex(newNum);
        hkCategoryDataBean.setId(hkCategoryEntity.getId());
        hkCategoryDataBean.setDescription(hkCategoryEntity.getDescription());
//        if (categoryType.equals(CategoryType.ASSET)) {
//            if (categoryAssetMap != null && !categoryAssetMap.isEmpty() && categoryAssetMap.containsKey(hkCategoryEntity.getId())) {
//                hkCategoryDataBean.setDisplayName(hkCategoryEntity.getCategoryTitle() + "(" + categoryCount + ")");
//            } else {
//                hkCategoryDataBean.setDisplayName(hkCategoryEntity.getCategoryTitle());
//            }
//        } else {
        hkCategoryDataBean.setDisplayName(hkCategoryEntity.getCategoryTitle());
        hkCategoryDataBean.setOriginalName(hkCategoryEntity.getCategoryTitle());
//        }
        hkCategoryDataBean.setCategoryCount(categoryCount);
        hkCategoryDataBean.setCompanyId(hkCategoryEntity.getFranchise());
        if (hkCategoryEntity.getParent() == null) {
            hkCategoryDataBean.setParentId(0L);
            hkCategoryDataBean.setParentName(HkSystemConstantUtil.NONE);
        } else {
            hkCategoryDataBean.setParentId(hkCategoryEntity.getParent().getId());
            hkCategoryDataBean.setParentName(hkCategoryEntity.getParent().getCategoryTitle());
        }
//        hkCategoryDataBean.setStartIndex(hkCategoryEntity.getCurrentIndex().toString());
        hkCategoryDataBean.setCategoryPrefix(hkCategoryEntity.getCategoryPrefix());
        if (hkCategoryEntity.getIsActive()) {
            hkCategoryDataBean.setStatus("Active");
        } else {
            hkCategoryDataBean.setStatus("Remove");
        }
        hkCategoryDataBean.setType(hkCategoryEntity.getCategoryType());

        if (hkCategoryDataBean.getCategoryCustomData() == null) {
            hkCategoryDataBean.setCategoryCustomData(new HashMap<String, Object>());
        }
//        if (hkCategoryEntity.getFranchise().equals(hkLoginDataBean.getCompanyId())) {
//            hkCategoryDataBean.setEditable(Boolean.TRUE);
//        } else {
//            hkCategoryDataBean.setEditable(Boolean.FALSE);
//        }
        hkCategoryDataBean.setHaveDiamondProcessingMch(hkCategoryEntity.getHaveDiamondProcessingMch());
        return hkCategoryDataBean;
    }

    public void addCategoryDatabeanToSortedList(CategoryDataBean categoryToAddIntosortedHkcategoryDatabean) {
        if (categoryToAddIntosortedHkcategoryDatabean != null) {
            if (!sortedhkCategoryDataBean.contains(categoryToAddIntosortedHkcategoryDatabean)) {
                long parentId = 0;
                if (categoryToAddIntosortedHkcategoryDatabean.getParentId() != null) {
                    parentId = categoryToAddIntosortedHkcategoryDatabean.getParentId();
                }
                if (parentId != 0) {
                    CategoryDataBean parentDatabean = idToCategoryDatabeanMap.get(parentId);
                    if (sortedhkCategoryDataBean.contains(parentDatabean)) {
                        sortedhkCategoryDataBean.add(categoryToAddIntosortedHkcategoryDatabean);
                    } else {
                        addCategoryDatabeanToSortedList(parentDatabean);
                        sortedhkCategoryDataBean.add(categoryToAddIntosortedHkcategoryDatabean);
                    }
                } else {
                    sortedhkCategoryDataBean.add(categoryToAddIntosortedHkcategoryDatabean);

                }
            }
        }
    }

    public boolean doesCategoryNameExist(String categoryName, CategoryType categoryType, Long categoryId) {
        return hKCategoryService.doesCategoryNameExist(categoryName, loginDataBean.getCompanyId(), categoryId, categoryType);
    }

    public Boolean createCategory(CategoryDataBean hkCategoryDataBean, CategoryType categoryType) {
        Boolean result = null;
        hkCategoryDataBean.setIsActive(Boolean.TRUE);
        HkCategoryEntity hkCategoryEntity = this.convertCategoryDataBeanToCategory(hkCategoryDataBean, null, HkSystemConstantUtil.CATEGORY_TYPE_ASSET);
        if (hkCategoryEntity != null) {
            hkCategoryEntity.setIsArchive(Boolean.FALSE);
            hkCategoryEntity.setCreatedBy(loginDataBean.getId());
            hkCategoryEntity.setCreatedOn(new Date());
            hKCategoryService.createCategory(hkCategoryEntity, categoryType);
            Long categoryId = hkCategoryEntity.getId();
            userManagementServiceWrapper.createLocaleForEntity(hkCategoryEntity.getCategoryTitle(), categoryType.toString().toLowerCase() + "category", loginDataBean.getId(), loginDataBean.getCompanyId());
            if (categoryId != null) {
                //Make a map sectionwise id(Key) and customField map(Value)
                Map<Long, Map<String, Object>> val = new HashMap<>();
                val.put(categoryId, hkCategoryDataBean.getCategoryCustomData());
                List<String> uiFieldList = new ArrayList<>();
                if (!CollectionUtils.isEmpty(hkCategoryDataBean.getDbTypeForCategory())) {
                    for (Map.Entry<String, String> entrySet : hkCategoryDataBean.getDbTypeForCategory().entrySet()) {
                        uiFieldList.add(entrySet.getKey());
                    }
                }
                Map<String, String> uiFieldMap = fieldService.retrieveUIFieldNameWithComponentTypes(uiFieldList);
                //Pass this map to makecustomfieldService
                List<CustomField> makeCustomField = customFieldSevice.makeCustomField(val, hkCategoryDataBean.getDbTypeForCategory(), uiFieldMap, HkSystemConstantUtil.FeatureNameForCustomField.CATEGORY.toString(), loginDataBean.getCompanyId(), categoryId);
                //After that make Map of Section and there customfield
                Map<HkSystemConstantUtil.SectionNameForCustomField, List<CustomField>> map = new HashMap<>();
                map.put(HkSystemConstantUtil.SectionNameForCustomField.GENERAL, makeCustomField);
                //Pass this map to customFieldSevice.saveOrUpdate method.
                customFieldSevice.saveOrUpdate(categoryId, HkSystemConstantUtil.FeatureNameForCustomField.CATEGORY, loginDataBean.getCompanyId(), map);
                result = Boolean.TRUE;
            } else {
                result = Boolean.FALSE;
            }
        } else {
            result = Boolean.FALSE;
        }
        return result;
    }

    public Boolean updateCategory(CategoryDataBean hkCategoryDataBean, CategoryType categoryType) {
        Boolean result = null;
        if (hkCategoryDataBean.getId() != null) {
            HkCategoryEntity hkCategoryEntity = hKCategoryService.retrieveCategory(hkCategoryDataBean.getId());
            if (hkCategoryDataBean.getStatus() != null && hkCategoryDataBean.getStatus().equalsIgnoreCase("Active")) {
                hkCategoryDataBean.setIsActive(Boolean.TRUE);
            } else {
                hkCategoryDataBean.setIsActive(Boolean.FALSE);
            }
            hkCategoryEntity = this.convertCategoryDataBeanToCategory(hkCategoryDataBean, hkCategoryEntity, HkSystemConstantUtil.CATEGORY_TYPE_ASSET);
            hKCategoryService.updateCategory(hkCategoryEntity, categoryType);
            userManagementServiceWrapper.createLocaleForEntity(hkCategoryEntity.getCategoryTitle(), categoryType.toString().toLowerCase() + "category", loginDataBean.getId(), loginDataBean.getCompanyId());
            Map<Long, Map<String, Object>> val = new HashMap<>();
            Long categoryId = hkCategoryEntity.getId();
            val.put(categoryId, hkCategoryDataBean.getCategoryCustomData());
            List<String> uiFieldList = new ArrayList<>();
            if (!CollectionUtils.isEmpty(hkCategoryDataBean.getDbTypeForCategory())) {
                for (Map.Entry<String, String> entrySet : hkCategoryDataBean.getDbTypeForCategory().entrySet()) {
                    uiFieldList.add(entrySet.getKey());
                }
            }
            Map<String, String> uiFieldMap = fieldService.retrieveUIFieldNameWithComponentTypes(uiFieldList);
            //Pass this map to makecustomfieldService
            List<CustomField> makeCustomField = customFieldSevice.makeCustomField(val, hkCategoryDataBean.getDbTypeForCategory(),uiFieldMap, HkSystemConstantUtil.FeatureNameForCustomField.CATEGORY.toString(), loginDataBean.getCompanyId(), categoryId);
            //After that make Map of Section and there customfield
            Map<HkSystemConstantUtil.SectionNameForCustomField, List<CustomField>> map = new HashMap<>();
            map.put(HkSystemConstantUtil.SectionNameForCustomField.GENERAL, makeCustomField);
            //Pass this map to customFieldSevice.saveOrUpdate method.
            customFieldSevice.saveOrUpdate(categoryId, HkSystemConstantUtil.FeatureNameForCustomField.CATEGORY, loginDataBean.getCompanyId(), map);
            result = Boolean.TRUE;
        } else {
            result = Boolean.FALSE;
        }
        return result;
    }

    private HkCategoryEntity convertCategoryDataBeanToCategory(CategoryDataBean hkCategoryDataBean, HkCategoryEntity hkCategoryEntity, String categoryType) {
        if (hkCategoryEntity == null) {
            hkCategoryEntity = new HkCategoryEntity();
        }

        hkCategoryEntity.setCategoryTitle(hkCategoryDataBean.getDisplayName());
        hkCategoryEntity.setCategoryType(categoryType);
        hkCategoryEntity.setDescription(hkCategoryDataBean.getDescription());
        hkCategoryEntity.setFranchise(loginDataBean.getCompanyId());
        hkCategoryEntity.setIsActive(hkCategoryDataBean.getIsActive());
        if (hkCategoryDataBean.getIsActive()) {
            hkCategoryEntity.setIsArchive(Boolean.FALSE);
        } else {
            hkCategoryEntity.setIsArchive(Boolean.TRUE);
        }
        hkCategoryEntity.setLastModifiedBy(loginDataBean.getId());
        hkCategoryEntity.setLastModifiedOn(new Date());
        //        Set null for root category
        if (hkCategoryDataBean.getParentId() == null || hkCategoryDataBean.getParentId().equals(0l)) {
            hkCategoryEntity.setParent(null);
        } else {
            hkCategoryEntity.setParent(new HkCategoryEntity(hkCategoryDataBean.getParentId()));
        }
        if (hkCategoryDataBean.isHaveDiamondProcessingMch() != null) {
            hkCategoryEntity.setHaveDiamondProcessingMch(hkCategoryDataBean.isHaveDiamondProcessingMch());
        } else {
            hkCategoryEntity.setHaveDiamondProcessingMch(Boolean.FALSE);
        }
        hkCategoryEntity.setCategoryPrefix(hkCategoryDataBean.getCategoryPrefix());

        if (hkCategoryDataBean.getStartIndex() == null) {
            hkCategoryDataBean.setStartIndex("01");
            hkCategoryDataBean.setPattern("00");
        } else {
            String pattern = "";
            if (hkCategoryDataBean.getStartIndex().toString().length() > 0) {
                for (int i = 0; i < hkCategoryDataBean.getStartIndex().toString().length(); i++) {
                    pattern = pattern + "0";
                }
            }
            hkCategoryDataBean.setPattern(pattern);
        }
        hkCategoryEntity.setStartIndex(Integer.parseInt(hkCategoryDataBean.getStartIndex()));
        hkCategoryEntity.setCategoryPattern(hkCategoryEntity.getCategoryPattern());
        hkCategoryEntity.setCurrentIndex(Integer.parseInt(hkCategoryDataBean.getStartIndex()));
        if (hkCategoryDataBean.getPattern() != null) {
            hkCategoryEntity.setCategoryPattern(hkCategoryDataBean.getPattern());
        }
        return hkCategoryEntity;
    }

    public CategoryDataBean addCustomDataToCategoryDataBean(CategoryDataBean categoryDataBean) {
        if (categoryDataBean.getId() != null) {
            HkCategoryEntity categoryEntity = hKCategoryService.retrieveCategory(categoryDataBean.getId());
            if (categoryEntity != null) {
                CategoryType categoryType = null;
                if (categoryEntity.getCategoryType().equalsIgnoreCase(CategoryType.ASSET.toString())) {
                    categoryType = CategoryType.ASSET;
                }
                if (categoryEntity.getCategoryType().equalsIgnoreCase(CategoryType.TASK.toString())) {
                    categoryType = CategoryType.TASK;
                }
                if (categoryEntity.getCategoryType().equalsIgnoreCase(CategoryType.EVENT.toString())) {
                    categoryType = CategoryType.EVENT;
                }
                categoryDataBean = this.convertCategoryEntityToCategoryDataBean(categoryEntity, categoryDataBean, categoryType, null, null);
            }
        }
        Map<HkSystemConstantUtil.SectionNameForCustomField, List<Map<Long, Map<String, Object>>>> retrieveDocumentByInstanceId = customFieldTransformerBean.retrieveDocumentByInstanceId(categoryDataBean.getId(), HkSystemConstantUtil.FeatureNameForCustomField.CATEGORY, loginDataBean.getCompanyId());
        if (!CollectionUtils.isEmpty(retrieveDocumentByInstanceId)) {
            List<Map<Long, Map<String, Object>>> maps = retrieveDocumentByInstanceId.get(HkSystemConstantUtil.SectionNameForCustomField.GENERAL);
            if (maps != null) {
                for (Map<Long, Map<String, Object>> map : maps) {
                    categoryDataBean.setCategoryCustomData(map.get(categoryDataBean.getId()));
                }
            }
        }
        return categoryDataBean;
    }

    public boolean doesCategoryPrefixExsist(String prefix, CategoryType categoryType, Long categoryId) {
        return hKCategoryService.doesCategoryPrefixExist(prefix, loginDataBean.getCompanyId(), categoryId, categoryType);
    }
}
