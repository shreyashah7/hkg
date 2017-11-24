package com.argusoft.hkg.core.impl;

import java.util.List;

import com.argusoft.generic.database.search.SearchFactory;
import com.argusoft.hkg.core.util.CategoryType;
import com.argusoft.hkg.core.HKCategoryService;
import com.argusoft.hkg.core.common.HkCoreService;
import com.argusoft.hkg.model.HkCategoryEntity;
import com.googlecode.genericdao.search.Search;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@Transactional
public class HkCategoryServiceImpl extends HkCoreService  implements HKCategoryService {

    private static class Fields {

        public static final String ID = "id";
        public static final String PREFIX = "categoryPrefix";
        public static final String TITLE = "categoryTitle";
        public static final String TYPE = "categoryType";
        public static final String DESCRIPTION = "description";
        public static final String START_INDEX = "startIndex";
        public static final String CURRENT_INDEX = "currentIndex";
        public static final String PARENT = "parent";
        public static final String CREATED_BY = "createdBy";
        public static final String CREATED_ON = "createdOn";
        public static final String LAST_MODIFIED_BY = "lastModifiedBy";
        public static final String LAST_MODIFIED_ON = "lastModifiedOn";
        public static final String IS_ACTIVE = "isActive";
        public static final String IS_ARCHIVE = "isArchive";
        public static final String HAVE_DIAMOND_PROCESSING_MACHINE = "haveDiamondProcessingMch";
        public static final String FRANCHISE = "franchise";
        public static final String ASSETENTITY_SET = "hkAssetEntitySet";
    }   

    @Override
    public HkCategoryEntity retrieveCategory(long categoryId) {

        return commonDao.find(HkCategoryEntity.class, categoryId);

    }

    @Override
    public List<HkCategoryEntity> retrieveAllCategories(long franchiseId, CategoryType categoryType) {
        Search search = SearchFactory.getSearch(HkCategoryEntity.class);
        search.addFilterEqual(Fields.FRANCHISE, franchiseId);
        search.addFilterEqual(Fields.TYPE, categoryType.name());
        return commonDao.search(search);
    }
    
    @Override
    public List<HkCategoryEntity> retrieveAllCategoriesByUser(long franchiseId, CategoryType categoryType, Long createdBy) {
        Search search = SearchFactory.getSearch(HkCategoryEntity.class);
        search.addFilterEqual(Fields.FRANCHISE, franchiseId);
        search.addFilterEqual(Fields.TYPE, categoryType.name());
        search.addFilterEqual(Fields.CREATED_BY, createdBy);
        return commonDao.search(search);        
    }

    @Override
    public List<HkCategoryEntity> retrieveAllRootCategories(long franchiseId, CategoryType categoryType) {
        Search search = SearchFactory.getSearch(HkCategoryEntity.class);
        search.addFilterEqual(Fields.FRANCHISE, franchiseId);
        search.addFilterEqual(Fields.TYPE, categoryType.name());
        search.addFilterEmpty(Fields.PARENT);
        return commonDao.search(search);
    }
    
    @Override
    public List<HkCategoryEntity> retrieveAllChildCategories(long parentId) {
        Search search = SearchFactory.getSearch(HkCategoryEntity.class);          
        search.addFilterEqual(Fields.PARENT,new HkCategoryEntity(parentId));
        return commonDao.search(search);
    }

    @Override
    public boolean createCategory(HkCategoryEntity category, CategoryType categoryType) {
        category.setCategoryType(categoryType.name());
        return commonDao.save(category);

    }

    @Override
    public boolean updateCategory(HkCategoryEntity category, CategoryType categoryType) {
        if (categoryType != null) {
            category.setCategoryType(categoryType.name());
        }
        return commonDao.save(category);
    }

    @Override
    public boolean removeCategory(long categoryId) {
        HkCategoryEntity category = commonDao.find(HkCategoryEntity.class, categoryId);
        category.setIsArchive(true);
        return commonDao.save(category);
    }

    @Override
    public boolean doesCategoryNameExist(String categoryName, long franchiseId, Long skipCategory, CategoryType categoryType) {
        Search search = SearchFactory.getSearch(HkCategoryEntity.class);
        search.addFilterEqual(Fields.FRANCHISE, franchiseId);
        search.addFilterILike(Fields.TITLE, categoryName);
        search.addFilterILike(Fields.TYPE, categoryType.name());
        if (skipCategory != null) {
            search.addFilterNotEqual(Fields.ID, skipCategory);
        }
        return commonDao.count(search) > 0;

    }
    
    @Override
    public boolean doesCategoryPrefixExist(String categoryPrefix, long franchiseId, Long skipCategory, CategoryType categoryType) {
        Search search = SearchFactory.getSearch(HkCategoryEntity.class);
        search.addFilterEqual(Fields.FRANCHISE, franchiseId);
        search.addFilterILike(Fields.PREFIX, categoryPrefix);
        search.addFilterILike(Fields.TYPE, categoryType.name());
        if (skipCategory != null) {
            search.addFilterNotEqual(Fields.ID, skipCategory);
        }
        return commonDao.count(search) > 0;

    }

    @Override
    public List<HkCategoryEntity> retrieveCategoriesByIds(List<Long> categoryIds) {
        Search search = new Search(HkCategoryEntity.class);
        search.addFilterNotIn(Fields.ID, categoryIds);
        return commonDao.search(search);
    }

    @Override
    public List<String> searchExistingCategoryNames(String categoryName, Long franchise, CategoryType categoryType) {
        Search search = SearchFactory.getSearch(HkCategoryEntity.class);
        search.addFilterEqual(Fields.FRANCHISE, franchise);
        if (categoryName != null) {
            search.addFilterILike(Fields.TITLE, "%" + categoryName + "%");
        }
        search.addFilterILike(Fields.TYPE, categoryType.name());
        search.addField(Fields.TITLE);
        return commonDao.search(search);
    }
    
    @Override
    public HkCategoryEntity retrieveCategoryByPrefix(String categoryPrefix, Long franchise, CategoryType categoryType) {
        Search search = SearchFactory.getSearch(HkCategoryEntity.class);
        search.addFilterEqual(Fields.FRANCHISE, franchise);
        if (StringUtils.hasText(categoryPrefix)) {
            search.addFilterEqual(Fields.PREFIX, categoryPrefix);
        }
        search.addFilterEqual(Fields.TYPE, categoryType.name());        
        return (HkCategoryEntity) commonDao.searchUnique(search);
    }
    
    @Override
    public List<HkCategoryEntity> retrieveCategoryBasedonSearchCriteria(String searchText, Long companyId, CategoryType categoryType) {
        Search search = SearchFactory.getSearch(HkCategoryEntity.class);
        search.addFilterEqual(Fields.FRANCHISE, companyId);
        if (searchText != null) {
            search.addFilterILike(Fields.TITLE, "%" + searchText + "%");
        }
        search.addFilterILike(Fields.TYPE, categoryType.name());
        
        return commonDao.search(search);
    }

}
