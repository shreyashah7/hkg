/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.reportbuilder.model;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author mital
 */
@Entity
@Table(name = "rb_tabular_relation_info")
public class RbTabularRelationEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Long id;
    @Basic(optional = false)
    @Column(name = "table1", nullable = false, length = 100)
    private String table1;
    @Basic(optional = false)
    @Column(name = "table2", nullable = false, length = 100)
    private String table2;
    @Basic(optional = false)
    @Column(name = "table1_column", nullable = false, length = 100)
    private String table1Column;
    @Basic(optional = false)
    @Column(name = "table2_column", nullable = false, length = 100)
    private String table2Column;
    @Column(name ="join_table", length = 100)
    private String joinTable;
    @Column(name ="join_column_table1", length = 100)
    private String joinColumnTable1;
    @Column(name ="join_column_table2", length = 100)
    private String joinColumnTable2;
    @Basic(optional = false)
    @Column(name = "relation_left_to_right", nullable = false, length = 5)
    private String relationLeftToRight;
    @Basic(optional = false)
    @Column(name = "is_archive", nullable = false)
    private boolean isArchive;

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

    public boolean isIsArchive() {
        return isArchive;
    }

    public void setIsArchive(boolean isArchive) {
        this.isArchive = isArchive;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final RbTabularRelationEntity other = (RbTabularRelationEntity) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "RbTabularRelationEntity{" + "id=" + id + ", table1=" + table1 + ", table2=" + table2 + ", table1Column=" + table1Column + ", table2Column=" + table2Column + ", relationLeftToRight=" + relationLeftToRight + ", isArchive=" + isArchive + '}';
    }
}
