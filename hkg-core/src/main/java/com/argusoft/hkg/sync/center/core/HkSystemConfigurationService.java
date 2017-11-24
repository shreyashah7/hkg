/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.sync.center.core;

import com.argusoft.sync.center.model.HkSystemConfigurationDocument;

/**
 *
 * @author akta
 */
public interface HkSystemConfigurationService {
    HkSystemConfigurationDocument getSystemConfigBySystemKey(String sysKey);

    String saveSystemConfig(HkSystemConfigurationDocument systemConfigurationDocument);
}
