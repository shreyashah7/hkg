package com.argusoft.hkg.web.internationalization.wrapper;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.argusoft.hkg.core.common.HkCoreService;
import com.argusoft.hkg.core.util.Fields;
import com.argusoft.hkg.web.internationalization.LabelType;
import com.argusoft.internationalization.common.core.I18nService;
import com.argusoft.internationalization.common.model.I18nLabelEntity;
import com.argusoft.internationalization.common.model.I18nLabelPKEntity;
import com.googlecode.genericdao.search.Search;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import org.apache.commons.collections.CollectionUtils;

@Transactional
@Service
public class InternationalizationServiceWrapper extends HkCoreService {

    private static class I18nLabelFields implements Fields {

        public static final String COMPANY = "labelPK.company";
        public static final String LABEL = "text";
        public static final String KEY = "labelPK.key";
        public static final String LANGUAGE_CODE = "labelPK.language";
        public static final String CREATED_DATE = "lastModifiedOn";
        public static final String TRANSLATION_PENDING = "translationPending";
        public static final String ENVIRONMENT = "environment";
        public static final String LABEL_PK = "labelPK";
        public static final String TYPE = "labelPK.type";
        public static final String ENTITY = "labelPK.entity";
    }

    @Autowired
    I18nService i18nService;

    /**
     *
     * @param searchString Searches for labels which starts with searchString
     * (if null then will give all labels under by other criteria)
     * @param labelType category of label
     * @param language language to retrieve labels for
     * @param franchise to which the lables belong
     * @return retrieve all labels for the parameters passed
     */
    public List<I18nLabelEntity> retrieveLabels(String searchString, LabelType labelType, String language, long franchise, String entity) {
        Search search = new Search(I18nLabelEntity.class);
        if (searchString != null || !searchString.isEmpty()) {
            search.addFilterILike(I18nLabelFields.LABEL, searchString + "%");
        }
        search.addFilterEqual(I18nLabelFields.COMPANY, franchise);
        search.addFilterEqual(I18nLabelFields.LANGUAGE_CODE, language);
        search.addFilterEqual(I18nLabelFields.TYPE, labelType.name());
        if (entity != null) {
            search.addFilterEqual(I18nLabelFields.ENTITY, entity);
        }
        return commonDao.search(search);
    }

    /**
     *
     * Searches for labels which starts with searchString
     *
     * @param searchString
     * @param labelType
     * @param language
     * @param franchise
     * @param page 0 based index of page
     * @param pageSize records per page
     * @return
     */
    public List<I18nLabelEntity> searchLabels(String searchString, LabelType labelType, String language, long franchise, int page, int pageSize) {
        Search search = new Search(I18nLabelEntity.class);
        search.addFilterLike(I18nLabelFields.LABEL, searchString + "%");
        search.addFilterEqual(I18nLabelFields.COMPANY, franchise);
        search.addFilterEqual(I18nLabelFields.TYPE, labelType.name());
        search.addFilterEqual(I18nLabelFields.LANGUAGE_CODE, language);
        search.setMaxResults(pageSize);
        search.setPage(page);
        return commonDao.search(search);
    }

    /**
     * Global search of labels
     *
     * @param searchString
     * @param franchise
     * @param page 0 based index of page
     * @param pageSize records per page
     * @return
     */
    public List<I18nLabelEntity> searchLabels(String searchString, long franchise, int page, int pageSize) {
        Search search = new Search(I18nLabelEntity.class);
        search.addFilterLike(I18nLabelFields.LABEL, searchString + "%");
        search.addFilterEqual(I18nLabelFields.COMPANY, franchise);
        search.setMaxResults(pageSize);
        search.setMaxResults(page);
        return commonDao.search(search);
    }

    /**
     *
     * @param i18nLanguageEntity
     * @return
     */
    public boolean updateLabels(List<I18nLabelEntity> listOfI18nLabelEntity) {
        commonDao.saveAll(listOfI18nLabelEntity);
        return true;
    }

    /**
     *
     * @param i18nLanguageEntity
     * @return
     */
    public boolean createLabelPendingTranslation(I18nLabelEntity i18nLabelEntity) {

        i18nService.addLabelAsync(i18nLabelEntity);
        return true;
    }

    public void copyLocalesFromFranchise(Long sourceFranchise, Long destinationFranchise, Long modifiedBy) {
        Search search = new Search(I18nLabelEntity.class);
        search.addFilterEqual(I18nLabelFields.COMPANY, sourceFranchise);
        search.addFilterIn(I18nLabelFields.TYPE, Arrays.asList(LabelType.LABEL, LabelType.MESSAGE, LabelType.NOTIFICATION,LabelType.MASTER));
        List<I18nLabelEntity> sourceLabels = commonDao.search(search);
        if (!CollectionUtils.isEmpty(sourceLabels)) {
            List<I18nLabelEntity> destinationLabels = new ArrayList<>();
            Date date = new Date();
            for (I18nLabelEntity i18nLabelEntity : sourceLabels) {
                I18nLabelPKEntity i18nLabelPKEntity = new I18nLabelPKEntity(i18nLabelEntity.getLabelPK().getKey(), i18nLabelEntity.getLabelPK().getLanguage(), i18nLabelEntity.getLabelPK().getCountry(), i18nLabelEntity.getLabelPK().getType(), i18nLabelEntity.getLabelPK().getEntity(), destinationFranchise);
                I18nLabelEntity labelEntity = new I18nLabelEntity(i18nLabelPKEntity, i18nLabelEntity.getText(), modifiedBy, date, i18nLabelEntity.getEnvironment());
                destinationLabels.add(labelEntity);
            }
            commonDao.saveAll(destinationLabels);
        }
    }

    public List<I18nLabelEntity> retrieveLabelsForFranchises(List<String> labelTypes, List<Long> franchises) {
        Search search = new Search(I18nLabelEntity.class);
        search.addFilterIn(I18nLabelFields.COMPANY, franchises);
        if (!CollectionUtils.isEmpty(labelTypes)) {
            search.addFilterIn(I18nLabelFields.TYPE, labelTypes);
        }
        search.addSortAsc(I18nLabelFields.COMPANY);
        return commonDao.search(search);
    }
}
