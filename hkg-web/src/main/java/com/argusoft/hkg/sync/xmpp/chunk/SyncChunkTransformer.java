/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.sync.xmpp.chunk;

import com.argusoft.hkg.sync.xmpp.util.SyncHelper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import org.jivesoftware.smack.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 *
 * @author shruti
 */
@Service
public class SyncChunkTransformer {

    private static final Logger LOGGER = LoggerFactory.getLogger(SyncChunkTransformer.class);
    public static final String SYNC_PREFIX = "Sync";
    private Gson gson;

    public SyncChunkTransformer() {
        gson = new GsonBuilder().create();
    }

    public synchronized String[] divideMessageIntoChunks(File file, Long id, int seek) {
        List<String> chunks = new LinkedList<>();
        if (file != null && file.exists()) {
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
                char[] chars;
                if (seek > 0) {
                    chars = new char[seek];
                    br.read(chars);
                }
                chars = new char[SyncChunkFrame.getChunkSize()];
                int i = -1;
                int read = br.read(chars);

                while (read != -1) {
                    SyncChunkFrame chunkFrame = new SyncChunkFrame();
                    chunkFrame.setChunkId(++i);
                    chunkFrame.setData(new String(chars));
//                    System.out.println("DATA:::: -" + chunkFrame.getData() + "- length: " + chunkFrame.getData().length());
//                     chunkFrame.setSize();
                    chunkFrame.setId(id);
                    chars = new char[SyncChunkFrame.getChunkSize()];
                    read = br.read(chars);
                    chunkFrame.setFin(2);
                    if (i == 0) {
                        chunkFrame.setFin(0);
                    }
                    if (read == -1) {
                        chunkFrame.setFin(1);
                        chunkFrame.setData(StringUtils.returnIfNotEmptyTrimmed(chunkFrame.getData()));
                        LOGGER.debug("StringUtils.returnIfNotEmptyTrimmed(chunkFrame.getData() " + StringUtils.returnIfNotEmptyTrimmed(chunkFrame.getData()) + "...........");

//                        chunkFrame.setData(StringUtils.trimTrailingCharacter(chunkFrame.getData(), '#'));
                    }

                    chunks.add(gson.toJson(chunkFrame));
                }
            } catch (FileNotFoundException exception) {
                LOGGER.warn(exception + "");
            } catch (IOException ex) {
                LOGGER.error(ex + "");
            }
        } else {
            LOGGER.warn("File " + file + " not found");
        }
        String[] chunkArray = new String[chunks.size()];
        chunks.toArray(chunkArray);
        return chunkArray;
    }

    public void extractDataFromChunk(SyncChunkFrame chunkFrame) {
        File file = new File(SyncHelper.SYNC_MAIN_DIR_PATH + SYNC_PREFIX + chunkFrame.getId());
        if (chunkFrame.getChunkId() == 0 && file.exists()) {
            file.delete();
            try {
                file.createNewFile();
            } catch (IOException ex) {
                java.util.logging.Logger.getLogger(SyncChunkTransformer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(file, true)))) {
            out.append(chunkFrame.getData());
            out.flush();
        } catch (FileNotFoundException ex) {
            LOGGER.error(ex + "");
        } catch (IOException ex) {
            LOGGER.error(ex + "");
        }
    }

    public InputStream concateChunksInMessage(SyncChunkFrame[] chunkFrames) {
        if (chunkFrames != null && chunkFrames.length > 0) {
            if (chunkFrames.length > 1) {
                File file = new File(SyncHelper.SYNC_MAIN_DIR_PATH + SYNC_PREFIX + chunkFrames[0].getId());
                try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(file, true)))) {
                    for (int i = 0; i < chunkFrames.length; i++) {
                        out.append(chunkFrames[i].getData());
                    }
                    return new FileInputStream(file);
                } catch (FileNotFoundException ex) {
                    LOGGER.error(ex + "");
                } catch (IOException ex) {
                    LOGGER.error(ex + "");
                }
            } else {
                return new ByteArrayInputStream(chunkFrames[0].getData().getBytes());
            }
        }
        return null;
    }
}
