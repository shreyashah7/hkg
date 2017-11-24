package com.argusoft.hkg.core.impl;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.argusoft.hkg.core.HKCategoryService;
import com.argusoft.hkg.core.config.HkCoreApplicationConfig;
import com.argusoft.hkg.model.HkCategoryEntity;
import com.argusoft.hkg.core.util.CategoryType;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = {HkCoreApplicationConfig.class})
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = false)
@Transactional
public class HkCategoryServiceImplTest {

	@Resource
	HKCategoryService categoryService;
	
	HkCategoryEntity category;
	HkCategoryEntity parentCategory;
	
    @Before
    public void setUp() {
    	parentCategory = new HkCategoryEntity(null,"TestPArent",null,1L,new Date(),1L,new Date(),true,false,0L, false);
    	category = new HkCategoryEntity(null,"Test",parentCategory,1L,new Date(),1L,new Date(),true,false,0L, false);
    }
	


//	@Test
	public void testCreateCategory() {
		categoryService.createCategory(parentCategory, CategoryType.ASSET);
		categoryService.createCategory(category, CategoryType.ASSET);
	}

//	@Test
	public void testUpdateCategory() {
		//testCreateCategory();
		categoryService.createCategory(parentCategory, CategoryType.ASSET);
		categoryService.createCategory(category, CategoryType.ASSET);
		parentCategory.setCategoryTitle("UpdatedParent");
		categoryService.updateCategory(parentCategory, CategoryType.ASSET);
		category.setCategoryTitle("Updated");
		categoryService.updateCategory(category, CategoryType.ASSET);		
	}



//	@Test
	public void testGetAllCategories() {
		categoryService.createCategory(parentCategory, CategoryType.ASSET);
		categoryService.createCategory(category, CategoryType.ASSET);
		assertTrue("we found "+categoryService.retrieveAllCategories(0L, CategoryType.ASSET).size(),categoryService.retrieveAllCategories(0L, CategoryType.ASSET).size() == 2);
	}

//	@Test
	public void testGetAllRootCategories() {
		categoryService.createCategory(parentCategory, CategoryType.ASSET);
		categoryService.createCategory(category, CategoryType.ASSET);
		assertTrue("we found "+categoryService.retrieveAllRootCategories(0L, CategoryType.ASSET).size(),categoryService.retrieveAllRootCategories(0L, CategoryType.ASSET).size() == 1);
	}
	
//	@Test
	public void testGetCategory() {
		categoryService.createCategory(parentCategory, CategoryType.ASSET);
		categoryService.createCategory(category, CategoryType.ASSET);
		
		assertNotNull(categoryService.retrieveCategory(category.getId()));
	}
	
//	@Test
	public void testRemoveCategory() {
		
		categoryService.removeCategory(category.getId());
		assertTrue(categoryService.retrieveAllCategories(0L, CategoryType.ASSET).size() == 2);
	}
        
        @Test
        public void testRetrieveCategoriesByIds() {
            category.setParent(null);
            categoryService.createCategory(category, CategoryType.TASK);
            List<Long> categoryIds = new ArrayList<>();
            categoryIds.add(category.getId());
//            System.out.println("id " + category.getId());
            List<HkCategoryEntity> resultList = categoryService.retrieveCategoriesByIds(categoryIds);
//            System.out.println("categories " + resultList);
//            assertEquals(category, resultList.get(0));
        }

}
