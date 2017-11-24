/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.departmentflow.transformers;

import com.argusoft.hkg.common.constantutil.HkSystemConstantUtil;
import com.argusoft.hkg.nosql.core.HkConfigurationService;
import com.argusoft.hkg.nosql.model.HkAssociatedDeptDocument;
import com.argusoft.hkg.nosql.model.HkAssociatedDesigDocument;
import com.argusoft.hkg.nosql.model.HkDepartmentConfigDocument;
import com.argusoft.hkg.web.center.usermanagement.databeans.LoginDataBean;
import com.argusoft.hkg.web.departmentflow.databeans.DepartmentFlowDataBean;
import com.argusoft.hkg.web.departmentflow.databeans.EdgeDataInfoDataBean;
import com.argusoft.hkg.web.departmentflow.databeans.EdgeDatabean;
import com.argusoft.hkg.web.departmentflow.databeans.NodeDataBean;
import com.argusoft.hkg.web.departmentflow.databeans.NodeDataInfoDataBean;
import com.argusoft.hkg.web.usermanagement.transformers.EmployeeTransformerBean;
import com.argusoft.hkg.web.usermanagement.wrapper.UserManagementServiceWrapper;
import com.argusoft.hkg.web.util.SelectItem;
import com.argusoft.usermanagement.common.model.UMDepartment;
import com.argusoft.usermanagement.common.model.UMRole;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 *
 *
 * var graphJSON = { "nodes": [ {id:'cost_staff', data: {label: 'Cost Staff',
 * background_color: '#EB99EB'}}, {id:'assortment', data: {label: 'Assortment',
 * background_color: '#80FF80'}}, {id:'processing', data: {label: 'processing',
 * background_color: '#80FF80'}},
 *
 *
 * {id:'cost_staff_head', data: {label: 'Cost Staff Head', background_color:
 * '#8AA9B1', isDeg: true}}, {id:'cost_staff_checker', data: {label: 'Cost Staff
 * Checker', background_color: '#8AA9B1', isDeg: true}}, {id:'planning_head',
 * data: {label: 'Planning Head', background_color: '#8AA9B1', isDeg: true}},
 *
 *
 *
 * ], "edges": [ ["cost_staff", "assortment", {color: '#5C5C5D', weight:2,
 * type:'default'}], ["cost_staff", "processing", {color: '#5C5C5D',
 * type:'rev'}], ["assortment", "processing", {color: '#5C5C5D', weight:2,
 * type:'default'}], ["processing", "planning", {color: '#5C5C5D', weight:2,
 * type:'default'}], ["planning", "laser_sawing", {color: '#5C5C5D', weight:2,
 * type:'default'}],
 *
 *
 *
 * ["cost_staff", "cost_staff_head", {color: '#C4C4C5', type:'designation'}],
 * ["cost_staff", "cost_staff_checker", {color: '#C4C4C5', type:'designation'}],
 * ["planning", "planning_head", {color: '#C4C4C5', type:'designation'}],
 *
 * ]
 * };
 *
 * @author shifa
 */
@Service
public class DepartmentFlowTransformer {

    @Autowired
    HkConfigurationService hkConfigurationService;
    @Autowired
    UserManagementServiceWrapper usermanagementServiceWrapper;
    @Autowired
    LoginDataBean loginDataBean;

    public DepartmentFlowDataBean retrieveDataForProcessFlow() {

        Map<Long, String> deptIdToNameMap = new HashMap<>();
        List<UMDepartment> retrievedDepartments = usermanagementServiceWrapper.retrieveDepartments(loginDataBean.getCompanyId(), Boolean.TRUE);
        if (!CollectionUtils.isEmpty(retrievedDepartments)) {
            for (UMDepartment department : retrievedDepartments) {
                deptIdToNameMap.put(department.getId(), department.getDeptName());
            }
        }
        DepartmentFlowDataBean departmentFlowDataBean = new DepartmentFlowDataBean();
        List<HkDepartmentConfigDocument> allDepConfigDocumentsForProcessFlow = hkConfigurationService.retrieveAllDepartmentConfigurationDocumentsForProcessFlow(loginDataBean.getCompanyId());
        List<NodeDataBean> nodeDataBeanList = null;
        List<EdgeDatabean> edgeDataBeanList = null;
        List<Long> totalAssociatedDesignationsIds = null;
        if (!CollectionUtils.isEmpty(allDepConfigDocumentsForProcessFlow)) {
            List<Long> departmentIds = new ArrayList<>();
            //Get existing roles in case any removed from designation section but not from department configuration.
            for (HkDepartmentConfigDocument hkDepartmentConfigDocument : allDepConfigDocumentsForProcessFlow) {
                departmentIds.add(hkDepartmentConfigDocument.getDepartment());
            }
            //Node for Designation is temporary removed.
            Map<Long, Set<Long>> departmentToRoleMap = null;//this.retrieveDesignationByDepartmentIds(departmentIds);

            nodeDataBeanList = new ArrayList<>();
            edgeDataBeanList = new ArrayList<>();
            totalAssociatedDesignationsIds = new ArrayList<>();
            for (HkDepartmentConfigDocument hkDepartmentConfigDocument : allDepConfigDocumentsForProcessFlow) {

                // --------Create node for department starts---------------------------------
                NodeDataBean nodeDataBeanForDept = new NodeDataBean();
                // Add department id
                nodeDataBeanForDept.setId(HkSystemConstantUtil.DEPARTMENT_PROCESS_FLOW.PREFIX_FOR_DEPARTMENT + "_" + hkDepartmentConfigDocument.getDepartment().toString());
                NodeDataInfoDataBean data = new NodeDataInfoDataBean();
                data.setLabel(hkDepartmentConfigDocument.getDepartmentName());
                data.setBackgroundColor(HkSystemConstantUtil.DEPARTMENT_PROCESS_FLOW.BACKGROUND_COLOR_FOR_DEPT);
                nodeDataBeanForDept.setData(data);
                nodeDataBeanList.add(nodeDataBeanForDept);
                //------- Node For Department Ends here--------------------------------------

                // ---------Node for designation logic starts here----------------------------------------
                //Designation is getting retrieved from actual association. not from configuration.
                //Because user can any time change change designation to department relations without affecting configurations.
                if (!CollectionUtils.isEmpty(departmentToRoleMap)) {
                    Set<Long> associatedDesignations = departmentToRoleMap.get(hkDepartmentConfigDocument.getDepartment());
                    if (!CollectionUtils.isEmpty(associatedDesignations)) {
                        for (Long associatedDesignation : associatedDesignations) {
                            // Create a list of associated designation ids throughout the document removing the redundant ones
                            if (!CollectionUtils.isEmpty(totalAssociatedDesignationsIds)) {
                                // First check whether id is already present
                                if (!totalAssociatedDesignationsIds.contains(associatedDesignation)) {
                                    totalAssociatedDesignationsIds.add(associatedDesignation);
                                }
                            } else {
                                // If empty , then directly add it
                                totalAssociatedDesignationsIds.add(associatedDesignation);
                            }

                            // Edges logic for storing department with associated designations
                            EdgeDatabean edgeDatabeanForDesig = new EdgeDatabean();
                            edgeDatabeanForDesig.setFromNode(HkSystemConstantUtil.DEPARTMENT_PROCESS_FLOW.PREFIX_FOR_DEPARTMENT + "_" + hkDepartmentConfigDocument.getDepartment());
                            edgeDatabeanForDesig.setToNode(HkSystemConstantUtil.DEPARTMENT_PROCESS_FLOW.PREFIX_FOR_DESIGNATION + "_" + associatedDesignation);
                            EdgeDataInfoDataBean edgeInfoDataBeanForDesig = new EdgeDataInfoDataBean();
                            edgeInfoDataBeanForDesig.setColor(HkSystemConstantUtil.DEPARTMENT_PROCESS_FLOW.COLOR_FOR_DEP_WITH_DESIG);
                            edgeInfoDataBeanForDesig.setType(HkSystemConstantUtil.DEPARTMENT_PROCESS_FLOW.TYPE_FOR_DESIGNATION);
                            edgeInfoDataBeanForDesig.setWeight(Integer.parseInt(HkSystemConstantUtil.DEPARTMENT_PROCESS_FLOW.WEIGHT));
                            edgeDatabeanForDesig.setData(edgeInfoDataBeanForDesig);
                            edgeDataBeanList.add(edgeDatabeanForDesig);
                        }
                    }
                }

//                if (!CollectionUtils.isEmpty(hkDepartmentConfigDocument.getAssociatedDesignations())) {
//                    List<HkAssociatedDesigDocument> associatedDesignations = hkDepartmentConfigDocument.getAssociatedDesignations();
//
//                    for (HkAssociatedDesigDocument associatedDesignation : associatedDesignations) {
//                        {
//                                // Create a list of associated designation ids throughout the document removing the redundant ones
//                                if (!CollectionUtils.isEmpty(totalAssociatedDesignationsIds)) {
//                                    // First check whether id is already present
//                                    if (!totalAssociatedDesignationsIds.contains(associatedDesignation.getDesignation())) {
//                                        totalAssociatedDesignationsIds.add(associatedDesignation.getDesignation());
//                                    }
//                                } else {
//                                    // If empty , then directly add it
//                                    totalAssociatedDesignationsIds.add(associatedDesignation.getDesignation());
//                                }
//
//                                // Edges logic for storing department with associated designations
//                                EdgeDatabean edgeDatabeanForDesig = new EdgeDatabean();
//                                edgeDatabeanForDesig.setFromNode(HkSystemConstantUtil.DEPARTMENT_PROCESS_FLOW.PREFIX_FOR_DEPARTMENT + "_" + hkDepartmentConfigDocument.getDepartment());
//                                edgeDatabeanForDesig.setToNode(HkSystemConstantUtil.DEPARTMENT_PROCESS_FLOW.PREFIX_FOR_DESIGNATION + "_" + associatedDesignation.getDesignation());
//                                EdgeDataInfoDataBean edgeInfoDataBeanForDesig = new EdgeDataInfoDataBean();
//                                edgeInfoDataBeanForDesig.setColor(HkSystemConstantUtil.DEPARTMENT_PROCESS_FLOW.COLOR_FOR_DEP_WITH_DESIG);
//                                edgeInfoDataBeanForDesig.setType(HkSystemConstantUtil.DEPARTMENT_PROCESS_FLOW.TYPE_FOR_DESIGNATION);
//                                edgeInfoDataBeanForDesig.setWeight(Integer.parseInt(HkSystemConstantUtil.DEPARTMENT_PROCESS_FLOW.WEIGHT));
//                                edgeDatabeanForDesig.setData(edgeInfoDataBeanForDesig);
//                                edgeDataBeanList.add(edgeDatabeanForDesig);
//                            }
//                        }
//                    }
                // ------Edges logic for dependency of department with associated department starts----------------------------
                if (!CollectionUtils.isEmpty(hkDepartmentConfigDocument.getAssociatedDepartments())) {
                    List<HkAssociatedDeptDocument> associatedDepartments = hkDepartmentConfigDocument.getAssociatedDepartments();
                    for (HkAssociatedDeptDocument associatedDepartment : associatedDepartments) {
                        if (!associatedDepartment.getIsArchive()) {

                            NodeDataBean associatedDeptNode = new NodeDataBean();
                            // Add department id
                            associatedDeptNode.setId(HkSystemConstantUtil.DEPARTMENT_PROCESS_FLOW.PREFIX_FOR_DEPARTMENT + "_" + associatedDepartment.getDepartment());
                            NodeDataInfoDataBean data1 = new NodeDataInfoDataBean();
                            data1.setLabel(deptIdToNameMap.get(associatedDepartment.getDepartment()));
                            data1.setBackgroundColor(HkSystemConstantUtil.DEPARTMENT_PROCESS_FLOW.BACKGROUND_COLOR_FOR_DEPT);
                            associatedDeptNode.setData(data1);
                            nodeDataBeanList.add(associatedDeptNode);

                            EdgeDatabean edgeDataBeanForDep = new EdgeDatabean();
                            edgeDataBeanForDep.setFromNode(HkSystemConstantUtil.DEPARTMENT_PROCESS_FLOW.PREFIX_FOR_DEPARTMENT + "_" + hkDepartmentConfigDocument.getDepartment().toString());
                            edgeDataBeanForDep.setToNode(HkSystemConstantUtil.DEPARTMENT_PROCESS_FLOW.PREFIX_FOR_DEPARTMENT + "_" + associatedDepartment.getDepartment());
                            EdgeDataInfoDataBean edgeInfoDataBeanForDep = new EdgeDataInfoDataBean();
                            edgeInfoDataBeanForDep.setColor(HkSystemConstantUtil.DEPARTMENT_PROCESS_FLOW.COLOR_FOR_DESG);
                            edgeInfoDataBeanForDep.setWeight(Integer.parseInt(HkSystemConstantUtil.DEPARTMENT_PROCESS_FLOW.WEIGHT));
                            if (associatedDepartment.getIsDefaultDept() != null && associatedDepartment.getIsDefaultDept()) {
                                edgeInfoDataBeanForDep.setType(HkSystemConstantUtil.DEPARTMENT_PROCESS_FLOW.DEFAULT_DEPARTMENT);
                                edgeInfoDataBeanForDep.setWeight(Integer.parseInt(HkSystemConstantUtil.DEPARTMENT_PROCESS_FLOW.DEFAULT_WEIGHT));
                            } else {
                                edgeInfoDataBeanForDep.setType(HkSystemConstantUtil.DEPARTMENT_PROCESS_FLOW.NOT_DEFAULT_DEPARTMENT);
                            }
                            if (edgeDataBeanForDep.getFromNode().equals(edgeDataBeanForDep.getToNode())) {
                                edgeInfoDataBeanForDep.setIsSelf(Boolean.TRUE);
                            }
                            edgeDataBeanForDep.setData(edgeInfoDataBeanForDep);
                            edgeDataBeanList.add(edgeDataBeanForDep);
                        }
                    }

                }
//--------- Edges logic for dependency of department with associated department ends-------------------------- 
                //-------Ends here
            }
            // Process after whole iteration
            if (!CollectionUtils.isEmpty(totalAssociatedDesignationsIds)) {
                Map<Long, String> mapOfDesgIdWithName = usermanagementServiceWrapper.retrieveMapOfDesgIdWithName(totalAssociatedDesignationsIds);
                if (mapOfDesgIdWithName != null && !mapOfDesgIdWithName.isEmpty()) {
                    for (Map.Entry<Long, String> desigIdWithName : mapOfDesgIdWithName.entrySet()) {

                        NodeDataBean nodeDataBeanForDesig = new NodeDataBean();
                        nodeDataBeanForDesig.setId(HkSystemConstantUtil.DEPARTMENT_PROCESS_FLOW.PREFIX_FOR_DESIGNATION + "_" + desigIdWithName.getKey());
                        NodeDataInfoDataBean desigData = new NodeDataInfoDataBean();
                        desigData.setLabel(desigIdWithName.getValue());
                        desigData.setIsDesignation(Boolean.TRUE);
                        desigData.setBackgroundColor(HkSystemConstantUtil.DEPARTMENT_PROCESS_FLOW.BACKGROUND_COLOR_FOR_DESG);
                        nodeDataBeanForDesig.setData(desigData);
                        nodeDataBeanList.add(nodeDataBeanForDesig);
                    }
                }
            }

            // Set in main databean
            if (!CollectionUtils.isEmpty(nodeDataBeanList)) {
                departmentFlowDataBean.setNodes(nodeDataBeanList);
            }
            if (!CollectionUtils.isEmpty(edgeDataBeanList)) {
                departmentFlowDataBean.setEdges(edgeDataBeanList);
            }

        }
        return departmentFlowDataBean;
    }

    private Map<Long, Set<Long>> retrieveDesignationByDepartmentIds(List<Long> departmentIds) {
        List<UMRole> listUMRole = usermanagementServiceWrapper.retrieveDesignationsByDepartmentIds(departmentIds);
        Map<Long, Set<Long>> result = new HashMap<>();
        if (!CollectionUtils.isEmpty(listUMRole)) {
            for (UMRole umRole : listUMRole) {
                if (umRole.getName() != null) {
                    if (!result.containsKey(umRole.getCustom1())) {
                        result.put(umRole.getCustom1(), new HashSet<>());
                    }
                    result.get(umRole.getCustom1()).add(umRole.getId());
                }
            }
        }
        return result;
    }

}
