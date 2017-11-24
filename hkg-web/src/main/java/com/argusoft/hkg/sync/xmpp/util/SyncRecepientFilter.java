/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.sync.xmpp.util;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author shruti
 */
public interface SyncRecepientFilter {

    Set<String> filterRecepient(Map<String, Object> vcards, Map<String, List<String>> queryParams);
}
