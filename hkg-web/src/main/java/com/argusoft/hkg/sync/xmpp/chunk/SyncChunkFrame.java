/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.sync.xmpp.chunk;

/**
 *
 * @author shruti
 */
public class SyncChunkFrame {
//This property holds transactionId
    private Long id;
    private int size;
    private int fin;
    private int chunkId;
    static private int chunkSize;
    private String data;

    public SyncChunkFrame(Long id, int fin, int chunkId, String data) {
        this.id = id;
        this.fin = fin;
        this.chunkId = chunkId;
        this.data = data;
    }

    public SyncChunkFrame() {
        chunkSize = 6000;

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getFin() {
        return fin;
    }

    public void setFin(int fin) {
        this.fin = fin;
    }

    public int getChunkId() {
        return chunkId;
    }

    public void setChunkId(int chunkId) {
        this.chunkId = chunkId;
    }

    public static int getChunkSize() {
        return chunkSize;
    }

    public static void setChunkSize(int chunkSize) {
        SyncChunkFrame.chunkSize = chunkSize;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "SyncChunkFrame{" + "id=" + id + ", size=" + size + ", fin=" + fin + ", chunkId=" + chunkId + ", data=" + data + '}';
    }

}
