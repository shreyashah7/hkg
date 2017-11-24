/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.center.sync.restclient;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 *
 * @author shruti
 */
@Service
public class SyncRestUtil {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    public void saveFile(String uri, String sourceFilename, String authToken, String destinationFileName, String contentType) {
        LOGGER.debug("In save file");
        try {
            HttpPost httpPost = new HttpPost(uri);
            if (authToken != null) {
                httpPost.setHeader("X-Auth-Token", authToken);
            }
            if (!StringUtils.isEmpty(destinationFileName)) {
                String dirToCreate = destinationFileName.substring(0, destinationFileName.lastIndexOf(File.separator));
                new File(dirToCreate).mkdirs();
            }
            LOGGER.debug("Filename: " + sourceFilename);
            httpPost.addHeader("Content-Type", contentType);
            httpPost.addHeader("Accept", SyncRestClient.CONTENT_TYPE + ", */*");
            BasicHttpEntity basicHttpEntity = new BasicHttpEntity();
            basicHttpEntity.setContent(new ByteArrayInputStream(sourceFilename.getBytes()));
            httpPost.setEntity(basicHttpEntity);
//            httpGet.setParams(new BasicHttpParams().setParameter("fileName", "/home/shruti/HKG (copy)/trunk/hkg-ui/src/main/webapp/i18n/EN(IN)0.json"));
            HttpClient httpClient = new DefaultHttpClient();
            HttpResponse httpResponse = httpClient.execute(httpPost);
            int statusCode = httpResponse.getStatusLine().getStatusCode();
//            LOGGER.debug("$$status= " + statusCode);
            if (statusCode == 200) {
                HttpEntity entity = httpResponse.getEntity();
//                LOGGER.debug("##### entity.isStreaming() " + entity.isStreaming());
                File file = new File(destinationFileName);
                if (!file.exists()) {
                    file.createNewFile();
                }
                try (FileOutputStream fileOutputStream = new FileOutputStream(file, false)) {

                    entity.writeTo(fileOutputStream);
                    fileOutputStream.flush();
                }
            }
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(SyncRestClient.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }
}
