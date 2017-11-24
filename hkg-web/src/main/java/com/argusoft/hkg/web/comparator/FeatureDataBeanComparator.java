/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.comparator;

import com.argusoft.hkg.web.usermanagement.databeans.FeatureDataBean;
import java.util.Comparator;

/**
 * Comparator To Sort FeatureDataBean list based on Sequence Number.
 * 
 * @author kelvin
 */
public enum FeatureDataBeanComparator implements Comparator<FeatureDataBean> {

    SORT_SEQ_NO {
                @Override
                public int compare(FeatureDataBean o1, FeatureDataBean o2) {
                	if(o1.getSeqNo() == null && o2.getSeqNo() == null) return 0;
                	else if(o1.getSeqNo() == null) return -1;
                	else if(o2.getSeqNo() == null) return 1;
                	
                    return o1.getSeqNo().compareTo(o2.getSeqNo());
                }
            };

    public static Comparator<FeatureDataBean> decending(final Comparator<FeatureDataBean> other) {
        return new Comparator<FeatureDataBean>() {
            @Override
            public int compare(FeatureDataBean o1, FeatureDataBean o2) {
                return -1 * other.compare(o1, o2);
            }
        };
    }

    public static Comparator<FeatureDataBean> getComparator(final FeatureDataBeanComparator... multipleOptions) {
        return new Comparator<FeatureDataBean>() {
            @Override
            public int compare(FeatureDataBean o1, FeatureDataBean o2) {
                for (FeatureDataBeanComparator option : multipleOptions) {
                    int result = option.compare(o1, o2);
                    if (result != 0) {
                        return result;
                    }
                }
                return 0;
            }
        };
    }
}
