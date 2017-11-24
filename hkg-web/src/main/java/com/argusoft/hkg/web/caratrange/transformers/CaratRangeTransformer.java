package com.argusoft.hkg.web.caratrange.transformers;

import com.argusoft.hkg.common.constantutil.HkSystemConstantUtil;
import com.argusoft.hkg.core.HkFoundationService;
import com.argusoft.hkg.model.HkCaratRangeEntity;
import com.argusoft.hkg.nosql.core.HkStockService;
import com.argusoft.hkg.nosql.model.HkPlanDocument;
import com.argusoft.hkg.web.caratrange.databeans.CaratRangeDataBean;
import com.argusoft.hkg.web.center.usermanagement.databeans.LoginDataBean;
import com.argusoft.hkg.web.usermanagement.wrapper.UserManagementServiceWrapper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.bson.BasicBSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 *
 * @author rajkumar
 */
@Service
public class CaratRangeTransformer {

    @Autowired
    private HkFoundationService foundationService;

    @Autowired
    private LoginDataBean loginDataBean;

    @Autowired
    private HkStockService planService;
    
    @Autowired
    UserManagementServiceWrapper userManagementServiceWrapper;

    public List<CaratRangeDataBean> retrieveAllActiveCaratRanges() {
        List<HkCaratRangeEntity> activeCaratRanges = foundationService.retrieveCaratRangeByFranchiseAndStatus(loginDataBean.getCompanyId(), Arrays.asList(HkSystemConstantUtil.ACTIVE),null);
        if (!CollectionUtils.isEmpty(activeCaratRanges)) {
            List<CaratRangeDataBean> caratRangeDataBeans = new ArrayList<>();
            for (HkCaratRangeEntity hkCaratRangeEntity : activeCaratRanges) {
                CaratRangeDataBean caratRangeDataBean = this.convertCaratRangeEntityToDataBean(hkCaratRangeEntity, null);
                caratRangeDataBean.setNewadded(Boolean.FALSE);
                caratRangeDataBeans.add(caratRangeDataBean);
            }

            caratRangeDataBeans.sort((CaratRangeDataBean o1, CaratRangeDataBean o2) -> {
                if (o1.getMinValue() < o2.getMinValue()) {
                    return -1;
                } else if (o1.getMinValue() > o2.getMinValue()) {
                    return 1;
                } else {
                    return 0;
                }
            });
            return caratRangeDataBeans;
        }
        return null;
    }

    private CaratRangeDataBean convertCaratRangeEntityToDataBean(HkCaratRangeEntity hkCaratRangeEntity, CaratRangeDataBean caratRangeDataBean) {
        if (caratRangeDataBean == null) {
            caratRangeDataBean = new CaratRangeDataBean();
        }

        caratRangeDataBean.setId(hkCaratRangeEntity.getId());
        caratRangeDataBean.setMinValue(hkCaratRangeEntity.getMinValue());
        caratRangeDataBean.setMaxValue(hkCaratRangeEntity.getMaxValue());
        caratRangeDataBean.setFranchise(hkCaratRangeEntity.getFranchise());
        caratRangeDataBean.setStatus(hkCaratRangeEntity.getStatus());

        return caratRangeDataBean;

    }

    public void saveAll(List<CaratRangeDataBean> caratRangeDataBeans) {
        List<HkCaratRangeEntity> newAdded = new ArrayList<>();
        List<HkCaratRangeEntity> edited = new ArrayList<>();
        List<HkCaratRangeEntity> deleted = new ArrayList<>();
        List<Long> caratToArchive = new ArrayList<>();
        if (!CollectionUtils.isEmpty(caratRangeDataBeans)) {
            for (CaratRangeDataBean caratRangeDataBean : caratRangeDataBeans) {
                if (caratRangeDataBean.getNewadded() != null && caratRangeDataBean.getNewadded()) {
                    newAdded.add(this.convertCaratDataBeanToEntity(caratRangeDataBean, null));
                }
                if (caratRangeDataBean.getEdited() != null && caratRangeDataBean.getEdited()) {
                    HkCaratRangeEntity caratRangeEntity = this.convertCaratDataBeanToEntity(caratRangeDataBean, null);
                    caratRangeEntity.setId(null);
                    edited.add(caratRangeEntity);
                    caratToArchive.add(caratRangeDataBean.getId());
                }
                if (caratRangeDataBean.getToDelete() != null && caratRangeDataBean.getToDelete()) {
                    HkCaratRangeEntity toDelete = this.convertCaratDataBeanToEntity(caratRangeDataBean, null);
                    toDelete.setId(caratRangeDataBean.getId());
                    toDelete.setStatus(HkSystemConstantUtil.ARCHIVED);
                    deleted.add(toDelete);
                }
            }
            if (!CollectionUtils.isEmpty(caratToArchive)) {
                foundationService.deactivateCaratRanges(caratToArchive);
            }

            List<HkCaratRangeEntity> finalList = new ArrayList<>();
            if (!CollectionUtils.isEmpty(deleted)) {
                finalList.addAll(deleted);
            }
            if (!CollectionUtils.isEmpty(edited)) {
                finalList.addAll(edited);
            }
            if (!CollectionUtils.isEmpty(newAdded)) {
                finalList.addAll(newAdded);
            }
            if (!CollectionUtils.isEmpty(finalList)) {
                foundationService.addCaratRanges(finalList);
            }
            if (!CollectionUtils.isEmpty(finalList)) {

                List<HkPlanDocument> allPlans = planService.retrievePlansByIds(null, loginDataBean.getCompanyId());
                List<Double> listOfCarats = new ArrayList<>();
                for (HkPlanDocument hkPlanDocument : allPlans) {
                    
                    if (hkPlanDocument.getFieldValue().containsKey(HkSystemConstantUtil.PlanStaticFieldName.CARATE_RANGE)) {
                        double carat = hkPlanDocument.getFieldValue().getDouble(HkSystemConstantUtil.PlanStaticFieldName.CARATE_RANGE);
                        listOfCarats.add(carat);
                    }
                }
                if (!CollectionUtils.isEmpty(listOfCarats)) {
                    Map<Double, Long> map = foundationService.retrieveMapOfCaratRangeToId(listOfCarats, loginDataBean.getCompanyId());

                    for (HkPlanDocument hkPlanDocument : allPlans) {
                        if (hkPlanDocument.getFieldValue().containsKey(HkSystemConstantUtil.PlanStaticFieldName.CARATE_RANGE)) {
                            double carat = hkPlanDocument.getFieldValue().getDouble(HkSystemConstantUtil.PlanStaticFieldName.CARATE_RANGE);
                            if (map.get(carat) != null) {
                                hkPlanDocument.getFieldValue().put(HkSystemConstantUtil.PlanStaticFieldName.CARAT, map.get(carat));
                            } else {
                                hkPlanDocument.getFieldValue().put(HkSystemConstantUtil.PlanStaticFieldName.CARAT, null);
                            }
                        }
                    }
//                    planService.updateAllPlans(allPlans);

                }
            }
        }
    }

    public HkCaratRangeEntity convertCaratDataBeanToEntity(CaratRangeDataBean caratRangeDataBean, HkCaratRangeEntity caratRangeEntity) {
        if (caratRangeEntity == null) {
            caratRangeEntity = new HkCaratRangeEntity();
        }
        Long companyId = 0l; 
        caratRangeEntity.setMinValue(caratRangeDataBean.getMinValue());
        caratRangeEntity.setMaxValue(caratRangeDataBean.getMaxValue());
        caratRangeEntity.setStatus(HkSystemConstantUtil.ACTIVE);
        caratRangeEntity.setCreatedBy(loginDataBean.getId());
        caratRangeEntity.setCreatedOn(new Date());
        caratRangeEntity.setLastModifiedBy(loginDataBean.getId());
        caratRangeEntity.setLastModifiedOn(new Date());
        caratRangeEntity.setFranchise(companyId);

        return caratRangeEntity;
    }

    public List<String> retrieveCaratWithNoRange() {
//        System.out.println("loginDataBean.getCompanyId():::" + loginDataBean.getCompanyId());
//        List<HkPlanDocument> plans = planService.retrievePlansWithNoCaratRange(loginDataBean.getCompanyId());
        List<String> result = new ArrayList<>();
////        System.out.println("plans before:::" + plans);
//        if (!CollectionUtils.isEmpty(plans)) {
////            System.out.println("plans:::" + plans);
//
//            for (HkPlanDocument hkPlanDocument : plans) {
//                BasicBSONObject fieldValue = hkPlanDocument.getFieldValue();
////                System.out.println("fieldValue::" + fieldValue);
//                result.add(fieldValue.get(HkSystemConstantUtil.PlanStaticFieldName.CARATE_RANGE).toString());
//            }
//        }

        return result;
    }
}
