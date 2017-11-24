package com.argusoft.hkg.core;

import com.argusoft.hkg.core.util.CategoryType;
import java.util.List;

import com.argusoft.hkg.model.HkCategoryEntity;

public interface HKCategoryService {

    /**
     * CategoryType Enum.
     *
     * @author Tejas
     *
     */
    // IF YOU ARE ADDING ANY METHODS WHICH MAY RETURN LIST OF CATEGORIES, MAKE SURE TO ADD CATEGORYTYPE AS ARGUMENT.
    /**
     * Get Category (with child categories) by id
     *
     * @param categoryId
     * @return
     */
    public HkCategoryEntity retrieveCategory(long categoryId);

    /**
     * Check whether a cateogry with specific name exists or not in franchise.
     *
     * @param categoryName
     * @param companyId
     * @param skipCategory
     * @param categoryType
     * @return true is any 'non-archived' category exists for the given name,
     * false otherwise
     */
    public boolean doesCategoryNameExist(String categoryName, long companyId, Long skipCategory, CategoryType categoryType);
    
    /**
     * Check whether a cateogry with specific prefix exists or not in franchise.
     *
     * @param categoryPrefix
     * @param companyId
     * @param skipCategory
     * @param categoryType
     * @return true is any 'non-archived' category exists for the given name,
     * false otherwise
     */
    public boolean doesCategoryPrefixExist(String categoryPrefix, long companyId, Long skipCategory, CategoryType categoryType);

    /**
     *
     * @param companyId
     * @param categoryType
     * @return list of all categories which belong to the category type
     * specified. Child categories will not be fetched.
     */
    public List<HkCategoryEntity> retrieveAllCategories(long companyId, CategoryType categoryType);

    /**
     * This method retrieves all categories based on given criteria.
     *
     * @param companyId The id of the franchise.
     * @param categoryType The type of the category.
     * @param createdBy The id of the user who has created the category.
     * @return list of all categories which belong to the category type
     * specified. Child categories will not be fetched.
     */
    public List<HkCategoryEntity> retrieveAllCategoriesByUser(long companyId, CategoryType categoryType, Long createdBy);

    /**
     *
     * @param companyId
     * @param categoryType
     * @return
     */
    public List<HkCategoryEntity> retrieveAllRootCategories(long companyId, CategoryType categoryType);
    
     /**
     * This method returns child categories of the specified category
     * @param parentId
     * @return list of categories
     */
    public List<HkCategoryEntity> retrieveAllChildCategories(long parentId);

    /**
     *
     * @param category category entity to be updated
     * @param categoryType categoryType to be applied to category entity. Pass
     * null if you are not making any change to it.
     * @return true if category was updated successfully, false otherwise.
     */
    public boolean updateCategory(HkCategoryEntity category, CategoryType categoryType);

    /**
     *
     * @param categoryId to identify which category should be removed.
     * @return true if category was removed successfully, false otherwise.
     */
    public boolean removeCategory(long categoryId);

    /**
     *
     * @param category category entity to be created
     * @param categoryType categoryType to be applied to category entity.
     * @return true if category was created successfully, false otherwise.
     */
    boolean createCategory(HkCategoryEntity category, CategoryType categoryType);

    /**
     * This method retrieves the categories by given list of ids.
     *
     * @param categoryIds The list of ids.
     * @return Returns the list of category entities.
     */
    public List<HkCategoryEntity> retrieveCategoriesByIds(List<Long> categoryIds);

    /**
     * This method retrieves the names of the existing categories of given
     * category type.
     *
     * @param categoryPrefix The prefix code of the category.
     * @param franchise The id of the franchise.
     * @param categoryType The type of the category.
     * @return Returns the Category Object with the name specified
     */
    public HkCategoryEntity retrieveCategoryByPrefix(String categoryPrefix, Long franchise, CategoryType categoryType);

    /**
     * This method retrieves the names of the existing categories of given
     * category type.
     *
     * @param categoryName The name of the category.
     * @param franchise The id of the franchise.
     * @param categoryType The type of the category.
     * @return Returns the names of the categories that match the existing
     * category name anywhere.
     */
    public List<String> searchExistingCategoryNames(String categoryName, Long franchise, CategoryType categoryType);

    /**
     * retrieve Categories based on the given Search Text
     *
     * @param searchText Search Criteria
     * @param companyId franchise id
     * @param categoryType The type of the category.
     * @return list of category references
     */
    public List<HkCategoryEntity> retrieveCategoryBasedonSearchCriteria(String searchText, Long companyId, CategoryType categoryType);
}
