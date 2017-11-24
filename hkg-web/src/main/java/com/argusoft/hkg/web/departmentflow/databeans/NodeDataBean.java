/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.departmentflow.databeans;

/**
 *
 * {id:'cost_staff', data: {label: 'Cost Staff', background_color: '#EB99EB'}},
 *
 * {id:'cost_staff_head', data: {label: 'Cost Staff Head', background_color:
 * '#8AA9B1', isDeg: true}},
 *
 * @author shifa
 */
public class NodeDataBean {
    private String id;
    private NodeDataInfoDataBean data;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public NodeDataInfoDataBean getData() {
        return data;
    }

    public void setData(NodeDataInfoDataBean data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "NodeDataBean{" + "id=" + id + ", data=" + data + '}';
    }
    

}
