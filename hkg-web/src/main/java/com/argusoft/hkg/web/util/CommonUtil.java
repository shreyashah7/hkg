/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.util;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import org.springframework.util.CollectionUtils;

/**
 *
 * @author mmodi
 */
public class CommonUtil {

    public static List<SelectItem> convertStringMapToSelectItem(Map<String, String> map) {
        List<SelectItem> selectItems = null;
        if (!CollectionUtils.isEmpty(map)) {
            selectItems = new ArrayList<>();
            for (String key : map.keySet()) {
                selectItems.add(new SelectItem(key, map.get(key)));
            }
        }
        return selectItems;
    }
    
    public static Comparator<SelectItem> NameComparator = (SelectItem o1, SelectItem o2) -> o1.getLabel().compareToIgnoreCase(o2.getLabel());

}
