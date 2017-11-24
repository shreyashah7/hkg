/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.argusoft.reportbuilder.core.bean;

/**
 *
 * @author gautam
 */
public class RbTabularRelationDataBean {
    
    private Long id;
    private String table1;
    private String table2;
    private String table1Column;
    private String table2Column;
    private String joinTable;
    private String joinColumnTable1;
    private String joinColumnTable2;
    private String relationLeftToRight;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTable1() {
        return table1;
    }

    public void setTable1(String table1) {
        this.table1 = table1;
    }

    public String getTable2() {
        return table2;
    }

    public void setTable2(String table2) {
        this.table2 = table2;
    }

    public String getTable1Column() {
        return table1Column;
    }

    public void setTable1Column(String table1Column) {
        this.table1Column = table1Column;
    }

    public String getTable2Column() {
        return table2Column;
    }

    public void setTable2Column(String table2Column) {
        this.table2Column = table2Column;
    }

    public String getJoinTable() {
        return joinTable;
    }

    public void setJoinTable(String joinTable) {
        this.joinTable = joinTable;
    }

    public String getJoinColumnTable1() {
        return joinColumnTable1;
    }

    public void setJoinColumnTable1(String joinColumnTable1) {
        this.joinColumnTable1 = joinColumnTable1;
    }

    public String getJoinColumnTable2() {
        return joinColumnTable2;
    }

    public void setJoinColumnTable2(String joinColumnTable2) {
        this.joinColumnTable2 = joinColumnTable2;
    }

    public String getRelationLeftToRight() {
        return relationLeftToRight;
    }

    public void setRelationLeftToRight(String relationLeftToRight) {
        this.relationLeftToRight = relationLeftToRight;
    }

    @Override
    public String toString() {
        return "RbTabularRelationDataBean{" + "id=" + id + ", table1=" + table1 + ", table2=" + table2 + ", table1Column=" + table1Column + ", table2Column=" + table2Column + ", joinTable=" + joinTable + ", joinColumnTable1=" + joinColumnTable1 + ", joinColumnTable2=" + joinColumnTable2 + ", relationLeftToRight=" + relationLeftToRight + '}';
    }
    
    
}
