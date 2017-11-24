/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.reportbuilder.common.constantutil;

import com.argusoft.hkg.common.constantutil.HkSystemConstantUtil;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author vipul
 */
public class RbReportConstantUtils {

    public static final Integer MINI_TEXT = 5;
    public static final Integer FULL_TEXT = 255;
    public static final String USER_FOREIGN_TABLE = "user_foreign_table";
    public static final String DASHBOARD = "D";
    public static final String NOT_DASHBOARD = "ND";
    public static Set<String> TABLE_NAMES_WITH_IS_ARCHIVE_ABSENT;

    public static enum OrderBy {

        ASC, DESC
    }

    public static class DBBaseType {

        public static final String MONGO_DB = "MDB";
        public static final String RELATIONAL_DB = "RDB";
    }

    /**
     * e.g. 1) Without join: (Local table is um_dept_mst) { FK_TABLE:
     * um_system_user FK_COLUMN: department LOCAL_COLUMN: id }
     *
     * 2) With join: (Local table is um_system_role) { FK_TABLE: um_system_user
     * FK_COLUMN: id LOCAL_COLUMN: id JOIN_TABLE: um_user_role
     * JOINED_LOCAL_COLUMN: role JOINED_FK_COLUMN: userobj }
     */
    public static enum JoinAttributeKey {

        FK_TABLE, FK_COLUMN, LOCAL_COLUMN, REL_TYPE,
        JOIN_TABLE, JOINED_LOCAL_COLUMN, JOINED_FK_COLUMN,
        SHOW_COLUMN, DELIMETER, IS_ANOTHER_CONVERSION
    }

    public static enum CustomAttribute {

        SHOW_TOTAL, SECTION_NAME, COMPONENT_TYPE, HK_FIELD_ID, MASTER_CODE, IS_DFF, INCLUDE_TIME, IS_SUB_FORM_VALUE, HK_PARENT_FIELD_NAME, HK_PARENT_DB_BASE_NAME, IS_RULE, HK_PARENT_FIELD_LABEL
    }

    public static enum SpecialConditionParameter {

        CONDITION_VALUE, CONDITION_DATA_TYPE, CONDITION_COLUMN
    }

    public static enum AddressKey {

        ADDRESS_TABLE, ADDRESS_COLUMN, ADDRESS_TYPE
    }

    public static class RelType {

        public static final String ONE_TO_ONE = "11";
        public static final String ONE_TO_MANY = "1M";
        public static final String MANY_TO_ONE = "M1";
        public static final String MANY_TO_MANY = "MM";
    }

    public static String DATE_FORMAT = "dd/MM/yyyy";

    /**
     * FIXED_FORIEGN_TABLE_ATTRIBUTES : Maintains Relationship of foreign tables
     * with fixed fields available.
     */
    public static final Map<String, Map<String, String>> FIXED_FORIEGN_TABLE_ATTRIBUTES = new HashMap<>();

    /**
     * COMMON_FOREIGN_TABLE_ATTRIBUTES : A common map of fixed fields with type
     * to be included in FIXED_FORIEGN_TABLE_ATTRIBUTES.
     */
    public static final Map<String, String> COMMON_FOREIGN_TABLE_ATTRIBUTES = new HashMap<>();

    static {
        COMMON_FOREIGN_TABLE_ATTRIBUTES.put("createdBy", "bigint");
        COMMON_FOREIGN_TABLE_ATTRIBUTES.put("createdOn", "timestamptz");
        COMMON_FOREIGN_TABLE_ATTRIBUTES.put("lastModifiedBy", "bigint");
        COMMON_FOREIGN_TABLE_ATTRIBUTES.put("lastModifiedOn", "timestamptz");
        COMMON_FOREIGN_TABLE_ATTRIBUTES.put("status", "text");
        COMMON_FOREIGN_TABLE_ATTRIBUTES.put("haveValue", "boolean");
    }

    static {
        //invoice
        FIXED_FORIEGN_TABLE_ATTRIBUTES.put("invoice", new HashMap<>(COMMON_FOREIGN_TABLE_ATTRIBUTES));

        //parcel
        FIXED_FORIEGN_TABLE_ATTRIBUTES.put("parcel", new HashMap<>(COMMON_FOREIGN_TABLE_ATTRIBUTES));
        FIXED_FORIEGN_TABLE_ATTRIBUTES.get("parcel").put("invoice", "name");

        //lot
        FIXED_FORIEGN_TABLE_ATTRIBUTES.put("lot", new HashMap<>(COMMON_FOREIGN_TABLE_ATTRIBUTES));
        FIXED_FORIEGN_TABLE_ATTRIBUTES.get("lot").put("invoice", "name");
        FIXED_FORIEGN_TABLE_ATTRIBUTES.get("lot").put("parcel", "name");
        FIXED_FORIEGN_TABLE_ATTRIBUTES.get("lot").put("hasPacket", "boolean");
        FIXED_FORIEGN_TABLE_ATTRIBUTES.get("lot").put("statusHistoryList", "json");
        FIXED_FORIEGN_TABLE_ATTRIBUTES.get("lot").put("slipHistoryList", "json");
        FIXED_FORIEGN_TABLE_ATTRIBUTES.get("lot").put("allotmentHistoryList", "json");

        //packet
        FIXED_FORIEGN_TABLE_ATTRIBUTES.put("packet", new HashMap<>(COMMON_FOREIGN_TABLE_ATTRIBUTES));
        FIXED_FORIEGN_TABLE_ATTRIBUTES.get("packet").put("invoice", "name");
        FIXED_FORIEGN_TABLE_ATTRIBUTES.get("packet").put("parcel", "name");
        FIXED_FORIEGN_TABLE_ATTRIBUTES.get("packet").put("lot", "name");
        FIXED_FORIEGN_TABLE_ATTRIBUTES.get("packet").put("statusHistoryList", "json");
        FIXED_FORIEGN_TABLE_ATTRIBUTES.get("packet").put("slipHistoryList", "json");
        FIXED_FORIEGN_TABLE_ATTRIBUTES.get("packet").put("allotmentHistoryList", "json");

    }
    /**
     * Goal validation rules that has been represented by a separator in db.
     */
    public static final Set<String> GOAL_TEMPLATE_VALIDATION_SET = new HashSet<>();

    static {
        GOAL_TEMPLATE_VALIDATION_SET.add("hk_system_goal_template_info.general_validation");
        GOAL_TEMPLATE_VALIDATION_SET.add("hk_system_goal_template_info.success_value");
        GOAL_TEMPLATE_VALIDATION_SET.add("hk_system_goal_template_info.failure_value");
    }

    /**
     * Map contains modified base name to actual base name. For inner documents
     * only. (Mongo db) eg. lot status history document.
     */
    public static final Map<String, String> INNER_DOCUMENT_BASE_NAME_MAP = new HashMap<>();

    static {
        //lot document
        INNER_DOCUMENT_BASE_NAME_MAP.put("lot_status_history", "lot");
        //issue document
        INNER_DOCUMENT_BASE_NAME_MAP.put("issue_status_history", "issue");
        INNER_DOCUMENT_BASE_NAME_MAP.put("lot_allotment_history", "lot");
        //packet document
        INNER_DOCUMENT_BASE_NAME_MAP.put("packet_status_history", "packet");
        INNER_DOCUMENT_BASE_NAME_MAP.put("packet_allotment_history", "packet");
        //plan document
        INNER_DOCUMENT_BASE_NAME_MAP.put("plan_status_history", "plan");
    }

    public static final Set<String> INNER_DOCUMENT_USER_CONVERSION_SET = new HashSet<>();

    static {
        //lot document
        INNER_DOCUMENT_USER_CONVERSION_SET.add("lot_status_history.byUser");
        //issue document
        INNER_DOCUMENT_USER_CONVERSION_SET.add("issue_status_history.byUser");
        INNER_DOCUMENT_USER_CONVERSION_SET.add("lot_allotment_history.byUser");
        INNER_DOCUMENT_USER_CONVERSION_SET.add("lot_allotment_history.forUser");
        //packet document
        INNER_DOCUMENT_USER_CONVERSION_SET.add("packet_status_history.byUser");
        INNER_DOCUMENT_USER_CONVERSION_SET.add("packet_allotment_history.byUser");
        INNER_DOCUMENT_USER_CONVERSION_SET.add("packet_allotment_history.forUser");
        //plan document
        INNER_DOCUMENT_USER_CONVERSION_SET.add("plan_status_history.byUser");
    }
    
    public static final Set<String> FIELDS_HANDLED_IN_TRANSFORMER = new HashSet<>();
    static{
        FIELDS_HANDLED_IN_TRANSFORMER.add("hk_notification_info.description");
        FIELDS_HANDLED_IN_TRANSFORMER.add("hk_notification_configuration_info.web_email_message");
        FIELDS_HANDLED_IN_TRANSFORMER.add("hk_notification_configuration_info.notification_criteria");
        FIELDS_HANDLED_IN_TRANSFORMER.add("workallotment.lotNumber");
        FIELDS_HANDLED_IN_TRANSFORMER.add("workallotmentaggregation.lotNumber");
        FIELDS_HANDLED_IN_TRANSFORMER.add("workallotment.packetNumber");
        FIELDS_HANDLED_IN_TRANSFORMER.add("workallotmentaggregation.packetNumber");
        FIELDS_HANDLED_IN_TRANSFORMER.add("workallotment.ruleSuccessed");
        FIELDS_HANDLED_IN_TRANSFORMER.add("fieldsequence.startingIndex");
        FIELDS_HANDLED_IN_TRANSFORMER.add("fieldsequence.currentIndex");
        FIELDS_HANDLED_IN_TRANSFORMER.add("fieldsequence.fieldName");
    }
    
    public static final Set<String> AggragateFields = new HashSet<>();
    static{
        
//        AggragateFields.add("lot_status_history");
//        AggragateFields.add("lot_allotment_history");
//        AggragateFields.add("packet_status_history");
//        AggragateFields.add("packet_allotment_history");
//        AggragateFields.add("issue_status_history");
//        AggragateFields.add("plan_status_history");
//        
//        AggragateFields.add("um_contact_address_dtl");
//        AggragateFields.add("um_contact_document_dtl");
//        AggragateFields.add("um_contact_experience_dtl");
//        AggragateFields.add("um_contact_insurance_dtl");
//        AggragateFields.add("um_contact_language_knwn_dtl");
//        AggragateFields.add("um_contact_education_dtl");
//        AggragateFields.add("um_user_contact");
    }

    /**
     * FeatureRelationship contains both map, -- Map of prefix and Franchise
     * column type -- Map of parent table that would least lead to any
     * franchise.
     */
    public static class FranchiseRelationship {

        public static final String PARENT_TABLE = "PARENT_TABLE";
        public static final String PARENT_COLUMN = "PARENT_COLUMN";
        public static final Map<String, String> FRANCHISE_COLUMN_TYPE = new HashMap<>();

        static {
            FRANCHISE_COLUMN_TYPE.put("hk_", "franchise");
            FRANCHISE_COLUMN_TYPE.put("um_", "company");
            FRANCHISE_COLUMN_TYPE.put("act_", "company");
            FRANCHISE_COLUMN_TYPE.put("invoice", "\"franchiseId\"");
            FRANCHISE_COLUMN_TYPE.put("parcel", "\"franchiseId\"");
            FRANCHISE_COLUMN_TYPE.put("lot", "\"franchiseId\"");
            FRANCHISE_COLUMN_TYPE.put("packet", "\"franchiseId\"");
        }

        public static final Map<String, Object> PARENT_TABLE_MAP = new HashMap<>();

        static {
            //hk_asset_info
            PARENT_TABLE_MAP.put("hk_asset_document_info", "{"
                    + "\"" + PARENT_TABLE + "\" : "
                    + "\"hk_system_asset_info\", "
                    + "\"" + PARENT_COLUMN + "\" : "
                    + "\"id\", "
                    + "\"" + JoinAttributeKey.LOCAL_COLUMN.toString() + "\" : "
                    + "\"asset\"}");
            PARENT_TABLE_MAP.put("hk_asset_purchaser_info", "{"
                    + "\"" + PARENT_TABLE + "\" : "
                    + "\"hk_system_asset_info\", "
                    + "\"" + PARENT_COLUMN + "\" : "
                    + "\"id\", "
                    + "\"" + JoinAttributeKey.LOCAL_COLUMN.toString() + "\" : "
                    + "\"asset\"}");
            //hk_system_event_info
            PARENT_TABLE_MAP.put("hk_event_recipient_info", "{"
                    + "\"" + PARENT_TABLE + "\" : "
                    + "\"hk_system_event_info\", "
                    + "\"" + PARENT_COLUMN + "\" : "
                    + "\"id\", "
                    + "\"" + JoinAttributeKey.LOCAL_COLUMN.toString() + "\" : "
                    + "\"event\"}");
            PARENT_TABLE_MAP.put("hk_event_registration_field_info", "{"
                    + "\"" + PARENT_TABLE + "\" : "
                    + "\"hk_system_event_info\", "
                    + "\"" + PARENT_COLUMN + "\" : "
                    + "\"id\", "
                    + "\"" + JoinAttributeKey.LOCAL_COLUMN.toString() + "\" : "
                    + "\"event\"}");
            PARENT_TABLE_MAP.put("hk_event_registration_field_value_info", "{"
                    + "\"" + PARENT_TABLE + "\" : "
                    + "\"hk_event_registration_field_info\", "
                    + "\"" + PARENT_COLUMN + "\" : "
                    + "\"id\", "
                    + "\"" + JoinAttributeKey.LOCAL_COLUMN.toString() + "\" : "
                    + "\"registration_field\"}");
            PARENT_TABLE_MAP.put("hk_user_event_registration_info", "{"
                    + "\"" + PARENT_TABLE + "\" : "
                    + "\"hk_system_event_info\", "
                    + "\"" + PARENT_COLUMN + "\" : "
                    + "\"id\", "
                    + "\"" + JoinAttributeKey.LOCAL_COLUMN.toString() + "\" : "
                    + "\"event\"}");
            PARENT_TABLE_MAP.put("event", "{"
                    + "\"" + PARENT_TABLE + "\" : "
                    + "\"hk_system_event_info\", "
                    + "\"" + PARENT_COLUMN + "\" : "
                    + "\"id\", "
                    + "\"" + JoinAttributeKey.LOCAL_COLUMN.toString() + "\" : "
                    + "\"instanceId\"}");
            //hk_system_shift_info
            PARENT_TABLE_MAP.put("hk_shift_dtl", "{"
                    + "\"" + PARENT_TABLE + "\" : "
                    + "\"hk_system_shift_info\", "
                    + "\"" + PARENT_COLUMN + "\" : "
                    + "\"id\", "
                    + "\"" + JoinAttributeKey.LOCAL_COLUMN.toString() + "\" : "
                    + "\"shift\"}");
            PARENT_TABLE_MAP.put("hk_shift_rule_info", "{"
                    + "\"" + PARENT_TABLE + "\" : "
                    + "\"hk_system_shift_info\", "
                    + "\"" + PARENT_COLUMN + "\" : "
                    + "\"id\", "
                    + "\"" + JoinAttributeKey.LOCAL_COLUMN.toString() + "\" : "
                    + "\"shift\"}");
            PARENT_TABLE_MAP.put("hk_shift_department_info", "{"
                    + "\"" + PARENT_TABLE + "\" : "
                    + "\"hk_system_shift_info\", "
                    + "\"" + PARENT_COLUMN + "\" : "
                    + "\"id\", "
                    + "\"" + JoinAttributeKey.LOCAL_COLUMN.toString() + "\" : "
                    + "\"shift\"}");
            //um_user_contact
            PARENT_TABLE_MAP.put("um_contact_address_dtl", "{"
                    + "\"" + PARENT_TABLE + "\" : "
                    + "\"um_user_contact\", "
                    + "\"" + PARENT_COLUMN + "\" : "
                    + "\"id\", "
                    + "\"" + JoinAttributeKey.LOCAL_COLUMN.toString() + "\" : "
                    + "\"user_contact\"}");
            PARENT_TABLE_MAP.put("um_contact_document_dtl", "{"
                    + "\"" + PARENT_TABLE + "\" : "
                    + "\"um_user_contact\", "
                    + "\"" + PARENT_COLUMN + "\" : "
                    + "\"id\", "
                    + "\"" + JoinAttributeKey.LOCAL_COLUMN.toString() + "\" : "
                    + "\"user_contact\"}");
            PARENT_TABLE_MAP.put("um_contact_education_dtl", "{"
                    + "\"" + PARENT_TABLE + "\" : "
                    + "\"um_user_contact\", "
                    + "\"" + PARENT_COLUMN + "\" : "
                    + "\"id\", "
                    + "\"" + JoinAttributeKey.LOCAL_COLUMN.toString() + "\" : "
                    + "\"user_contact\"}");
            PARENT_TABLE_MAP.put("um_contact_experience_dtl", "{"
                    + "\"" + PARENT_TABLE + "\" : "
                    + "\"um_user_contact\", "
                    + "\"" + PARENT_COLUMN + "\" : "
                    + "\"id\", "
                    + "\"" + JoinAttributeKey.LOCAL_COLUMN.toString() + "\" : "
                    + "\"user_contact\"}");
            PARENT_TABLE_MAP.put("um_contact_insurance_dtl", "{"
                    + "\"" + PARENT_TABLE + "\" : "
                    + "\"um_user_contact\", "
                    + "\"" + PARENT_COLUMN + "\" : "
                    + "\"id\", "
                    + "\"" + JoinAttributeKey.LOCAL_COLUMN.toString() + "\" : "
                    + "\"user_contact\"}");
            PARENT_TABLE_MAP.put("um_contact_language_knwn_dtl", "{"
                    + "\"" + PARENT_TABLE + "\" : "
                    + "\"um_user_contact\", "
                    + "\"" + PARENT_COLUMN + "\" : "
                    + "\"id\", "
                    + "\"" + JoinAttributeKey.LOCAL_COLUMN.toString() + "\" : "
                    + "\"user_contact\"}");
            //um_dept_mst
            PARENT_TABLE_MAP.put("department", "{"
                    + "\"" + PARENT_TABLE + "\" : "
                    + "\"um_dept_mst\", "
                    + "\"" + PARENT_COLUMN + "\" : "
                    + "\"id\", "
                    + "\"" + JoinAttributeKey.LOCAL_COLUMN.toString() + "\" : "
                    + "\"instanceId\"}");
            //hk_workflow_info
            PARENT_TABLE_MAP.put("hk_workflow_approver_rel_info", "{"
                    + "\"" + PARENT_TABLE + "\" : "
                    + "\"hk_workflow_info\", "
                    + "\"" + PARENT_COLUMN + "\" : "
                    + "\"id\", "
                    + "\"" + JoinAttributeKey.LOCAL_COLUMN.toString() + "\" : "
                    + "\"workflow\"}");
            //hk_price_list_info
            PARENT_TABLE_MAP.put("hk_price_list_dtl", "{"
                    + "\"" + PARENT_TABLE + "\" : "
                    + "\"hk_price_list_info\", "
                    + "\"" + PARENT_COLUMN + "\" : "
                    + "\"id\", "
                    + "\"" + JoinAttributeKey.LOCAL_COLUMN.toString() + "\" : "
                    + "\"price_list\"}");
            //act_activity_flow_version_info
//            PARENT_TABLE_MAP.put("act_activity_flow_version_info", "{"
//                    + "\"" + PARENT_TABLE + "\" : "
//                    + "\"act_activity_flow_info\", "
//                    + "\"" + PARENT_COLUMN + "\" : "
//                    + "\"id\", "
//                    + "\"" + JoinAttributeKey.LOCAL_COLUMN.toString() + "\" : "
//                    + "\"activity\"}");
            //act_activity_flow_group_info
            PARENT_TABLE_MAP.put("act_activity_flow_group_info", "{"
                    + "\"" + PARENT_TABLE + "\" : "
                    + "\"act_activity_flow_version_info\", "
                    + "\"" + PARENT_COLUMN + "\" : "
                    + "\"id\", "
                    + "\"" + JoinAttributeKey.LOCAL_COLUMN.toString() + "\" : "
                    + "\"activity_version\"}");
            //act_activity_flow_node_info
            PARENT_TABLE_MAP.put("act_activity_flow_node_info", "{"
                    + "\"" + PARENT_TABLE + "\" : "
                    + "\"act_activity_flow_version_info\", "
                    + "\"" + PARENT_COLUMN + "\" : "
                    + "\"id\", "
                    + "\"" + JoinAttributeKey.LOCAL_COLUMN.toString() + "\" : "
                    + "\"activity_version\"}");
            //act_activity_flow_node_route_info
            PARENT_TABLE_MAP.put("act_activity_flow_node_route_info", "{"
                    + "\"" + PARENT_TABLE + "\" : "
                    + "\"act_activity_flow_version_info\", "
                    + "\"" + PARENT_COLUMN + "\" : "
                    + "\"id\", "
                    + "\"" + JoinAttributeKey.LOCAL_COLUMN.toString() + "\" : "
                    + "\"activity_version\"}");
            //hk_notification_user_rel_info
            PARENT_TABLE_MAP.put("hk_notification_user_rel_info", "{"
                    + "\"" + PARENT_TABLE + "\" : "
                    + "\"hk_notification_info\", "
                    + "\"" + PARENT_COLUMN + "\" : "
                    + "\"id\", "
                    + "\"" + JoinAttributeKey.LOCAL_COLUMN.toString() + "\" : "
                    + "\"notification\"}");
        }
    }

    /**
     * Contains sub-relations like user with its family member.
     * USER_RELATIONSHIP_MAP: will be interpreted as follows. actual column :
     * FK_TABLE.SHOW_COLUMN Join condition : FK_TABLE.LOCAL_COLUMN =
     * FK_TABLE.FK_COLUMN (self join)
     *
     * ADDRESS_RELATION_MAP: Contains relations related to address like native
     * address and current address.
     *
     * SPECIAL_CONDITION_MAP: contains conditions for specific tables in order
     * to avoid archived report.
     *
     */
    public static class SpecialRelations {

        public static final Map<String, Object> USER_RELATIONSHIP_MAP = new HashMap<>();

        public static final Map<String, Object> ADDRESS_RELATION_MAP = new HashMap<>();

        public static final Map<String, Object> SPECIAL_CONDITION_MAP = new HashMap<>();

        static {
            /**
             * um_user_contact
             */
            USER_RELATIONSHIP_MAP.put("um_user_contact.member_blood_group", "{"
                    + "\"" + JoinAttributeKey.FK_TABLE.toString() + "\" : "
                    + "\"um_user_contact\", "
                    + "\"" + JoinAttributeKey.FK_COLUMN.toString() + "\" : "
                    + "\"userobj\", "
                    + "\"" + JoinAttributeKey.LOCAL_COLUMN.toString() + "\" : "
                    + "\"contact_user\", "
                    + "\"" + JoinAttributeKey.SHOW_COLUMN.toString() + "\" : "
                    + "\"blood_group\"}");
            USER_RELATIONSHIP_MAP.put("um_user_contact.member_date_of_birth", "{"
                    + "\"" + JoinAttributeKey.FK_TABLE.toString() + "\" : "
                    + "\"um_user_contact\", "
                    + "\"" + JoinAttributeKey.FK_COLUMN.toString() + "\" : "
                    + "\"userobj\", "
                    + "\"" + JoinAttributeKey.LOCAL_COLUMN.toString() + "\" : "
                    + "\"contact_user\", "
                    + "\"" + JoinAttributeKey.SHOW_COLUMN.toString() + "\" : "
                    + "\"date_of_birth\"}");
            USER_RELATIONSHIP_MAP.put("um_user_contact.member_mobile_number", "{"
                    + "\"" + JoinAttributeKey.FK_TABLE.toString() + "\" : "
                    + "\"um_user_contact\", "
                    + "\"" + JoinAttributeKey.FK_COLUMN.toString() + "\" : "
                    + "\"userobj\", "
                    + "\"" + JoinAttributeKey.LOCAL_COLUMN.toString() + "\" : "
                    + "\"contact_user\", "
                    + "\"" + JoinAttributeKey.SHOW_COLUMN.toString() + "\" : "
                    + "\"mobile_number\"}");
            USER_RELATIONSHIP_MAP.put("um_user_contact.first_name", "{"
                    + "\"" + JoinAttributeKey.FK_TABLE.toString() + "\" : "
                    + "\"um_user_contact\", "
                    + "\"" + JoinAttributeKey.FK_COLUMN.toString() + "\" : "
                    + "\"userobj\", "
                    + "\"" + JoinAttributeKey.LOCAL_COLUMN.toString() + "\" : "
                    + "\"contact_user\", "
                    + "\"" + JoinAttributeKey.SHOW_COLUMN.toString() + "\" : "
                    + "\"first_name:middle_name:last_name\", "
                    + "\"" + JoinAttributeKey.DELIMETER.toString() + "\" : "
                    + "\" \"}");
            USER_RELATIONSHIP_MAP.put("um_user_contact.relation", "{"
                    + "\"" + JoinAttributeKey.FK_TABLE.toString() + "\" : "
                    + "\"um_user_contact\", "
                    + "\"" + JoinAttributeKey.FK_COLUMN.toString() + "\" : "
                    + "\"userobj\", "
                    + "\"" + JoinAttributeKey.LOCAL_COLUMN.toString() + "\" : "
                    + "\"contact_user\", "
                    + "\"" + JoinAttributeKey.SHOW_COLUMN.toString() + "\" : "
                    + "\"relation\"}");
            USER_RELATIONSHIP_MAP.put("um_user_contact.occupation", "{"
                    + "\"" + JoinAttributeKey.FK_TABLE.toString() + "\" : "
                    + "\"um_user_contact\", "
                    + "\"" + JoinAttributeKey.FK_COLUMN.toString() + "\" : "
                    + "\"userobj\", "
                    + "\"" + JoinAttributeKey.LOCAL_COLUMN.toString() + "\" : "
                    + "\"contact_user\", "
                    + "\"" + JoinAttributeKey.SHOW_COLUMN.toString() + "\" : "
                    + "\"occupation\"}");
        }

        static {
            /**
             * Current Address
             */
            ADDRESS_RELATION_MAP.put("um_contact_address_dtl.current_city", "{"
                    + "\"" + AddressKey.ADDRESS_TABLE.toString() + "\" : "
                    + "\"um_contact_address_dtl\", "
                    + "\"" + AddressKey.ADDRESS_COLUMN.toString() + "\" : "
                    + "\"city\", "
                    + "\"" + AddressKey.ADDRESS_TYPE.toString() + "\" : "
                    + "\"CURRENT\"}");
            ADDRESS_RELATION_MAP.put("um_contact_address_dtl.current_address_line1", "{"
                    + "\"" + AddressKey.ADDRESS_TABLE.toString() + "\" : "
                    + "\"um_contact_address_dtl\", "
                    + "\"" + AddressKey.ADDRESS_COLUMN.toString() + "\" : "
                    + "\"address_line1\", "
                    + "\"" + AddressKey.ADDRESS_TYPE.toString() + "\" : "
                    + "\"CURRENT\"}");
            ADDRESS_RELATION_MAP.put("um_contact_address_dtl.current_zip_code", "{"
                    + "\"" + AddressKey.ADDRESS_TABLE.toString() + "\" : "
                    + "\"um_contact_address_dtl\", "
                    + "\"" + AddressKey.ADDRESS_COLUMN.toString() + "\" : "
                    + "\"zip_code\", "
                    + "\"" + AddressKey.ADDRESS_TYPE.toString() + "\" : "
                    + "\"CURRENT\"}");
            /**
             * Native Address
             */
            ADDRESS_RELATION_MAP.put("um_contact_address_dtl.native_city", "{"
                    + "\"" + AddressKey.ADDRESS_TABLE.toString() + "\" : "
                    + "\"um_contact_address_dtl\", "
                    + "\"" + AddressKey.ADDRESS_COLUMN.toString() + "\" : "
                    + "\"city\", "
                    + "\"" + AddressKey.ADDRESS_TYPE.toString() + "\" : "
                    + "\"NATIVE\"}");
            ADDRESS_RELATION_MAP.put("um_contact_address_dtl.native_address_line1", "{"
                    + "\"" + AddressKey.ADDRESS_TABLE.toString() + "\" : "
                    + "\"um_contact_address_dtl\", "
                    + "\"" + AddressKey.ADDRESS_COLUMN.toString() + "\" : "
                    + "\"address_line1\", "
                    + "\"" + AddressKey.ADDRESS_TYPE.toString() + "\" : "
                    + "\"NATIVE\"}");
            ADDRESS_RELATION_MAP.put("um_contact_address_dtl.native_zip_code", "{"
                    + "\"" + AddressKey.ADDRESS_TABLE.toString() + "\" : "
                    + "\"um_contact_address_dtl\", "
                    + "\"" + AddressKey.ADDRESS_COLUMN.toString() + "\" : "
                    + "\"zip_code\", "
                    + "\"" + AddressKey.ADDRESS_TYPE.toString() + "\" : "
                    + "\"NATIVE\"}");
        }

        static {
            SPECIAL_CONDITION_MAP.put("hk_carat_range_mst", "{"
                    + "\"" + SpecialConditionParameter.CONDITION_COLUMN.toString() + "\" : "
                    + "\"status\", "
                    + "\"" + SpecialConditionParameter.CONDITION_DATA_TYPE.toString() + "\" : "
                    + "\"String\", "
                    + "\"" + SpecialConditionParameter.CONDITION_VALUE.toString() + "\" : "
                    + "\"A\"}");
            SPECIAL_CONDITION_MAP.put("subformvalue", "{"
                    + "\"" + SpecialConditionParameter.CONDITION_COLUMN.toString() + "\" : "
                    + "\"isArchive\", "
                    + "\"" + SpecialConditionParameter.CONDITION_DATA_TYPE.toString() + "\" : "
                    + "\"Boolean\", "
                    + "\"" + SpecialConditionParameter.CONDITION_VALUE.toString() + "\" : "
                    + "\"false\"}");
        }
    }

    /**
     * FK_CONVERSION_MAP : Stores relation for conversion e.g.
     * um_system_user.department returns Long value (department Id). Instead It
     * should return department name based on id.
     *
     * key : um_system_user.department FK_TABLE : um_dept_mst FK_COLUMN : id
     * SHOW_COLUMN : dept_name
     *
     * ::Update-- If one wants to use multiple column values in show column. Add
     * it using ":" as separator and a delimiter.
     *
     * IS_ANOTHER_CONVERSION : If set true, It will again search for conversion.
     * Else it will be final conversion.
     *
     * STATIC_CONVERSION_MAP : Stores Relationship for static value conversion.
     * e.g. status Stores "A" but actual value will be "Active", similarly for
     * "P"--> Pending and so on. key --> dbBaseName.dbFieldName valueMap --> Map
     * of short values to actual value.
     *
     * INTRA_TABLE_CONVERSION_MAP: converts a column of a table to another
     * column of same table. No join attribute needed. ** Most useful to be
     * processed by another conversion map.
     *
     *
     * USER_MULTISELECT_UNIQUE_SET: contains RDBMS table name who have user
     * multi select unique to its parent.
     */
    public static class Converter {

        public static final Map<String, Object> FK_CONVERSION_MAP = new HashMap<>();

        public static final Map<String, Object> STATIC_CONVERSION_MAP = new HashMap<>();

        public static final Map<String, String> INTRA_TABLE_CONVERSION_MAP = new HashMap<>();

        public static final Set<String> USER_MULTISELECT_UNIQUE_SET = new HashSet<>();

        static {
            /**
             * ********************* hk_notification_info table
             * *********************
             */
            INTRA_TABLE_CONVERSION_MAP.put("hk_notification_info.description", "hk_notification_info.id");
            INTRA_TABLE_CONVERSION_MAP.put("hk_notification_configuration_info.notification_criteria", "hk_notification_configuration_info.id");
            INTRA_TABLE_CONVERSION_MAP.put("hk_notification_configuration_info.web_email_message", "hk_notification_configuration_info.id");
            INTRA_TABLE_CONVERSION_MAP.put("workallotmentaggregation.lotNumber", "workallotmentaggregation._id");
            INTRA_TABLE_CONVERSION_MAP.put("workallotmentaggregation.packetNumber", "workallotmentaggregation._id");
            INTRA_TABLE_CONVERSION_MAP.put("workallotment.lotNumber", "workallotment._id");
            INTRA_TABLE_CONVERSION_MAP.put("workallotment.packetNumber", "workallotment._id");
            INTRA_TABLE_CONVERSION_MAP.put("fieldsequence.startingIndex", "fieldsequence._id");
            INTRA_TABLE_CONVERSION_MAP.put("fieldsequence.currentIndex", "fieldsequence._id");
            //caret range
            INTRA_TABLE_CONVERSION_MAP.put("lot." + HkSystemConstantUtil.LotStaticFieldName.CARATE_RANGE, "(lot.\"fieldValue\"->>'"+HkSystemConstantUtil.LotStaticFieldName.CARATE_RANGE+"')::numeric");
            INTRA_TABLE_CONVERSION_MAP.put("packet." + HkSystemConstantUtil.PacketStaticFieldName.CARATE_RANGE, "(packet.\"fieldValue\"->>'"+HkSystemConstantUtil.PacketStaticFieldName.CARATE_RANGE+"')::numeric");
            INTRA_TABLE_CONVERSION_MAP.put("plan." + HkSystemConstantUtil.PlanStaticFieldName.CARATE_RANGE, "(plan.\"fieldValue\"->>'"+HkSystemConstantUtil.PlanStaticFieldName.CARATE_RANGE+"')::numeric");

            //lot history status document
            INTRA_TABLE_CONVERSION_MAP.put("lot_status_history.status", "lot.statusHistoryList");
            INTRA_TABLE_CONVERSION_MAP.put("lot_status_history.onDate", "lot.statusHistoryList");
            INTRA_TABLE_CONVERSION_MAP.put("lot_status_history.byUser", "lot.statusHistoryList");
            //issue history status document
            INTRA_TABLE_CONVERSION_MAP.put("issue_status_history.status", "issue.statusHistoryList");
            INTRA_TABLE_CONVERSION_MAP.put("issue_status_history.onDate", "issue.statusHistoryList");
            INTRA_TABLE_CONVERSION_MAP.put("issue_status_history.byUser", "issue.statusHistoryList");
            //lot_allotment_history document
            INTRA_TABLE_CONVERSION_MAP.put("lot_allotment_history.forUser", "lot.allotmentHistoryList");
            INTRA_TABLE_CONVERSION_MAP.put("lot_allotment_history.dueDate", "lot.allotmentHistoryList");
            INTRA_TABLE_CONVERSION_MAP.put("lot_allotment_history.onDate", "lot.allotmentHistoryList");
            INTRA_TABLE_CONVERSION_MAP.put("lot_allotment_history.byUser", "lot.allotmentHistoryList");
            //packet history status document
            INTRA_TABLE_CONVERSION_MAP.put("packet_status_history.status", "packet.statusHistoryList");
            INTRA_TABLE_CONVERSION_MAP.put("packet_status_history.onDate", "packet.statusHistoryList");
            INTRA_TABLE_CONVERSION_MAP.put("packet_status_history.byUser", "packet.statusHistoryList");
            //packet_allotment_history document
            INTRA_TABLE_CONVERSION_MAP.put("packet_allotment_history.forUser", "packet.allotmentHistoryList");
            INTRA_TABLE_CONVERSION_MAP.put("packet_allotment_history.dueDate", "packet.allotmentHistoryList");
            INTRA_TABLE_CONVERSION_MAP.put("packet_allotment_history.onDate", "packet.allotmentHistoryList");
            INTRA_TABLE_CONVERSION_MAP.put("packet_allotment_history.byUser", "packet.allotmentHistoryList");
            //plan history status document
            INTRA_TABLE_CONVERSION_MAP.put("plan_status_history.status", "plan.statusHistoryList");
            INTRA_TABLE_CONVERSION_MAP.put("plan_status_history.onDate", "plan.statusHistoryList");
            INTRA_TABLE_CONVERSION_MAP.put("plan_status_history.byUser", "plan.statusHistoryList");

        }

        static {
            USER_MULTISELECT_UNIQUE_SET.add("hk_workflow_approver_rel_info");
            USER_MULTISELECT_UNIQUE_SET.add("hk_asset_issue_info");
        }

        static {
            /**
             * ********************* um_system_user table *********************
             */
            FK_CONVERSION_MAP.put("um_system_user.company", "{"
                    + "\"" + JoinAttributeKey.FK_TABLE.toString() + "\" : "
                    + "\"um_company_info\", "
                    + "\"" + JoinAttributeKey.FK_COLUMN.toString() + "\" : "
                    + "\"id\", "
                    + "\"" + JoinAttributeKey.SHOW_COLUMN.toString() + "\" : "
                    + "\"name\"}");
            FK_CONVERSION_MAP.put("um_system_user.created_by", "{"
                    + "\"" + JoinAttributeKey.FK_TABLE.toString() + "\" : "
                    + "\"um_system_user\", "
                    + "\"" + JoinAttributeKey.FK_COLUMN.toString() + "\" : "
                    + "\"id\", "
                    + "\"" + JoinAttributeKey.SHOW_COLUMN.toString() + "\" : "
                    + "\"first_name:middle_name:last_name\", "
                    + "\"" + JoinAttributeKey.DELIMETER.toString() + "\" : "
                    + "\" \"}");
            FK_CONVERSION_MAP.put("um_system_user.department", "{"
                    + "\"" + JoinAttributeKey.FK_TABLE.toString() + "\" : "
                    + "\"um_dept_mst\", "
                    + "\"" + JoinAttributeKey.FK_COLUMN.toString() + "\" : "
                    + "\"id\", "
                    + "\"" + JoinAttributeKey.SHOW_COLUMN.toString() + "\" : "
                    + "\"dept_name\"}");
            FK_CONVERSION_MAP.put("um_system_user.modified_by", "{"
                    + "\"" + JoinAttributeKey.FK_TABLE.toString() + "\" : "
                    + "\"um_system_user\", "
                    + "\"" + JoinAttributeKey.FK_COLUMN.toString() + "\" : "
                    + "\"id\", "
                    + "\"" + JoinAttributeKey.SHOW_COLUMN.toString() + "\" : "
                    + "\"first_name:middle_name:last_name\", "
                    + "\"" + JoinAttributeKey.DELIMETER.toString() + "\" : "
                    + "\" \"}");
            FK_CONVERSION_MAP.put("um_system_user.custom4", "{"
                    + "\"" + JoinAttributeKey.FK_TABLE.toString() + "\" : "
                    + "\"hk_system_shift_info\", "
                    + "\"" + JoinAttributeKey.FK_COLUMN.toString() + "\" : "
                    + "\"id\", "
                    + "\"" + JoinAttributeKey.SHOW_COLUMN.toString() + "\" : "
                    + "\"shift_title\"}");
            FK_CONVERSION_MAP.put("um_system_user.custom1", "{"
                    + "\"" + JoinAttributeKey.FK_TABLE.toString() + "\" : "
                    + "\"um_company_info\", "
                    + "\"" + JoinAttributeKey.FK_COLUMN.toString() + "\" : "
                    + "\"id\", "
                    + "\"" + JoinAttributeKey.SHOW_COLUMN.toString() + "\" : "
                    + "\"name\"}");

            /**
             * ********************* um_user_contact table
             * *********************
             */
            FK_CONVERSION_MAP.put("um_user_contact.userobj", "{"
                    + "\"" + JoinAttributeKey.FK_TABLE.toString() + "\" : "
                    + "\"um_system_user\", "
                    + "\"" + JoinAttributeKey.FK_COLUMN.toString() + "\" : "
                    + "\"id\", "
                    + "\"" + JoinAttributeKey.SHOW_COLUMN.toString() + "\" : "
                    + "\"first_name:middle_name:last_name\", "
                    + "\"" + JoinAttributeKey.DELIMETER.toString() + "\" : "
                    + "\" \"}");
            FK_CONVERSION_MAP.put("um_user_contact.created_by", "{"
                    + "\"" + JoinAttributeKey.FK_TABLE.toString() + "\" : "
                    + "\"um_system_user\", "
                    + "\"" + JoinAttributeKey.FK_COLUMN.toString() + "\" : "
                    + "\"id\", "
                    + "\"" + JoinAttributeKey.SHOW_COLUMN.toString() + "\" : "
                    + "\"first_name:middle_name:last_name\", "
                    + "\"" + JoinAttributeKey.DELIMETER.toString() + "\" : "
                    + "\" \"}");
            FK_CONVERSION_MAP.put("um_user_contact.modified_by", "{"
                    + "\"" + JoinAttributeKey.FK_TABLE.toString() + "\" : "
                    + "\"um_system_user\", "
                    + "\"" + JoinAttributeKey.FK_COLUMN.toString() + "\" : "
                    + "\"id\", "
                    + "\"" + JoinAttributeKey.SHOW_COLUMN.toString() + "\" : "
                    + "\"first_name:middle_name:last_name\", "
                    + "\"" + JoinAttributeKey.DELIMETER.toString() + "\" : "
                    + "\" \"}");

            /**
             * ********************* um_user_ip_association_dtl table
             * *********************
             */
            FK_CONVERSION_MAP.put("um_user_ip_association_dtl.access_given_by", "{"
                    + "\"" + JoinAttributeKey.FK_TABLE.toString() + "\" : "
                    + "\"um_system_user\", "
                    + "\"" + JoinAttributeKey.FK_COLUMN.toString() + "\" : "
                    + "\"id\", "
                    + "\"" + JoinAttributeKey.SHOW_COLUMN.toString() + "\" : "
                    + "\"first_name:middle_name:last_name\", "
                    + "\"" + JoinAttributeKey.DELIMETER.toString() + "\" : "
                    + "\" \"}");
            FK_CONVERSION_MAP.put("um_user_ip_association_dtl.last_modified_by", "{"
                    + "\"" + JoinAttributeKey.FK_TABLE.toString() + "\" : "
                    + "\"um_system_user\", "
                    + "\"" + JoinAttributeKey.FK_COLUMN.toString() + "\" : "
                    + "\"id\", "
                    + "\"" + JoinAttributeKey.SHOW_COLUMN.toString() + "\" : "
                    + "\"first_name:middle_name:last_name\", "
                    + "\"" + JoinAttributeKey.DELIMETER.toString() + "\" : "
                    + "\" \"}");
            FK_CONVERSION_MAP.put("um_user_ip_association_dtl.user_id", "{"
                    + "\"" + JoinAttributeKey.FK_TABLE.toString() + "\" : "
                    + "\"um_system_user\", "
                    + "\"" + JoinAttributeKey.FK_COLUMN.toString() + "\" : "
                    + "\"id\", "
                    + "\"" + JoinAttributeKey.SHOW_COLUMN.toString() + "\" : "
                    + "\"first_name:middle_name:last_name\", "
                    + "\"" + JoinAttributeKey.DELIMETER.toString() + "\" : "
                    + "\" \"}");
            
            FK_CONVERSION_MAP.put("um_user_role.role", "{"
                    + "\"" + JoinAttributeKey.FK_TABLE.toString() + "\" : "
                    + "\"um_system_role\", "
                    + "\"" + JoinAttributeKey.FK_COLUMN.toString() + "\" : "
                    + "\"id\", "
                     + "\"" + JoinAttributeKey.SHOW_COLUMN.toString() + "\" : "
                    + "\"name\"}");

            /**
             * ********************* um_company_info table
             * *********************
             */
            FK_CONVERSION_MAP.put("um_company_info.activated_by", "{"
                    + "\"" + JoinAttributeKey.FK_TABLE.toString() + "\" : "
                    + "\"um_system_user\", "
                    + "\"" + JoinAttributeKey.FK_COLUMN.toString() + "\" : "
                    + "\"id\", "
                    + "\"" + JoinAttributeKey.SHOW_COLUMN.toString() + "\" : "
                    + "\"first_name:middle_name:last_name\", "
                    + "\"" + JoinAttributeKey.DELIMETER.toString() + "\" : "
                    + "\" \"}");
            FK_CONVERSION_MAP.put("um_company_info.created_by", "{"
                    + "\"" + JoinAttributeKey.FK_TABLE.toString() + "\" : "
                    + "\"um_system_user\", "
                    + "\"" + JoinAttributeKey.FK_COLUMN.toString() + "\" : "
                    + "\"id\", "
                    + "\"" + JoinAttributeKey.SHOW_COLUMN.toString() + "\" : "
                    + "\"first_name:middle_name:last_name\", "
                    + "\"" + JoinAttributeKey.DELIMETER.toString() + "\" : "
                    + "\" \"}");
            FK_CONVERSION_MAP.put("um_company_info.modified_by", "{"
                    + "\"" + JoinAttributeKey.FK_TABLE.toString() + "\" : "
                    + "\"um_system_user\", "
                    + "\"" + JoinAttributeKey.FK_COLUMN.toString() + "\" : "
                    + "\"id\", "
                    + "\"" + JoinAttributeKey.SHOW_COLUMN.toString() + "\" : "
                    + "\"first_name:middle_name:last_name\", "
                    + "\"" + JoinAttributeKey.DELIMETER.toString() + "\" : "
                    + "\" \"}");
            /**
             * ********************* um_contact_address_dtl table
             * *********************
             */
            FK_CONVERSION_MAP.put("um_contact_address_dtl.last_modified_by", "{"
                    + "\"" + JoinAttributeKey.FK_TABLE.toString() + "\" : "
                    + "\"um_system_user\", "
                    + "\"" + JoinAttributeKey.FK_COLUMN.toString() + "\" : "
                    + "\"id\", "
                    + "\"" + JoinAttributeKey.SHOW_COLUMN.toString() + "\" : "
                    + "\"first_name:middle_name:last_name\", "
                    + "\"" + JoinAttributeKey.DELIMETER.toString() + "\" : "
                    + "\" \"}");

            /**
             * ********************* um_contact_document_dtl table
             * *********************
             */
            FK_CONVERSION_MAP.put("um_contact_document_dtl.last_modified_by", "{"
                    + "\"" + JoinAttributeKey.FK_TABLE.toString() + "\" : "
                    + "\"um_system_user\", "
                    + "\"" + JoinAttributeKey.FK_COLUMN.toString() + "\" : "
                    + "\"id\", "
                    + "\"" + JoinAttributeKey.SHOW_COLUMN.toString() + "\" : "
                    + "\"first_name:middle_name:last_name\", "
                    + "\"" + JoinAttributeKey.DELIMETER.toString() + "\" : "
                    + "\" \"}");

            /**
             * ********************* um_contact_education_dtl table
             * *********************
             */
            FK_CONVERSION_MAP.put("um_contact_education_dtl.last_modified_by", "{"
                    + "\"" + JoinAttributeKey.FK_TABLE.toString() + "\" : "
                    + "\"um_system_user\", "
                    + "\"" + JoinAttributeKey.FK_COLUMN.toString() + "\" : "
                    + "\"id\", "
                    + "\"" + JoinAttributeKey.SHOW_COLUMN.toString() + "\" : "
                    + "\"first_name:middle_name:last_name\", "
                    + "\"" + JoinAttributeKey.DELIMETER.toString() + "\" : "
                    + "\" \"}");

            /**
             * ********************* um_contact_experience_dtl table
             * *********************
             */
            FK_CONVERSION_MAP.put("um_contact_experience_dtl.last_modified_by", "{"
                    + "\"" + JoinAttributeKey.FK_TABLE.toString() + "\" : "
                    + "\"um_system_user\", "
                    + "\"" + JoinAttributeKey.FK_COLUMN.toString() + "\" : "
                    + "\"id\", "
                    + "\"" + JoinAttributeKey.SHOW_COLUMN.toString() + "\" : "
                    + "\"first_name:middle_name:last_name\", "
                    + "\"" + JoinAttributeKey.DELIMETER.toString() + "\" : "
                    + "\" \"}");
            /**
             * ********************* um_contact_insurance_dtl table
             * *********************
             */
            FK_CONVERSION_MAP.put("um_contact_insurance_dtl.last_modified_by", "{"
                    + "\"" + JoinAttributeKey.FK_TABLE.toString() + "\" : "
                    + "\"um_system_user\", "
                    + "\"" + JoinAttributeKey.FK_COLUMN.toString() + "\" : "
                    + "\"id\", "
                    + "\"" + JoinAttributeKey.SHOW_COLUMN.toString() + "\" : "
                    + "\"first_name:middle_name:last_name\", " + "\""
                    + JoinAttributeKey.DELIMETER.toString() + "\" : "
                    + "\" \"}");
            FK_CONVERSION_MAP.put("um_contact_insurance_dtl.user_contact", "{"
                    + "\"" + JoinAttributeKey.FK_TABLE.toString() + "\" : "
                    + "\"um_user_contact\", "
                    + "\"" + JoinAttributeKey.FK_COLUMN.toString() + "\" : "
                    + "\"id\", "
                    + "\"" + JoinAttributeKey.SHOW_COLUMN.toString() + "\" : "
                    + "\"userobj\", "
                    + "\"" + JoinAttributeKey.IS_ANOTHER_CONVERSION.toString() + "\" : "
                    + "true}");
            /**
             * ********************* um_contact_language_knwn_dtl table
             * *********************
             */
            FK_CONVERSION_MAP.put("um_contact_language_knwn_dtl.last_modified_by", "{"
                    + "\"" + JoinAttributeKey.FK_TABLE.toString() + "\" : "
                    + "\"um_system_user\", "
                    + "\"" + JoinAttributeKey.FK_COLUMN.toString() + "\" : "
                    + "\"id\", "
                    + "\"" + JoinAttributeKey.SHOW_COLUMN.toString() + "\" : "
                    + "\"first_name:middle_name:last_name\", " + "\""
                    + JoinAttributeKey.DELIMETER.toString() + "\" : "
                    + "\" \"}");

            /**
             * ********************* hk_system_event_info table
             * *********************
             */
            FK_CONVERSION_MAP.put("hk_system_event_info.category", "{"
                    + "\"" + JoinAttributeKey.FK_TABLE.toString() + "\" : "
                    + "\"hk_system_category_mst\", "
                    + "\"" + JoinAttributeKey.FK_COLUMN.toString() + "\" : "
                    + "\"id\", "
                    + "\"" + JoinAttributeKey.SHOW_COLUMN.toString() + "\" : "
                    + "\"category_title\"}");
            FK_CONVERSION_MAP.put("hk_system_event_info.created_by", "{"
                    + "\"" + JoinAttributeKey.FK_TABLE.toString() + "\" : "
                    + "\"um_system_user\", "
                    + "\"" + JoinAttributeKey.FK_COLUMN.toString() + "\" : "
                    + "\"id\", "
                    + "\"" + JoinAttributeKey.SHOW_COLUMN.toString() + "\" : "
                    + "\"first_name:middle_name:last_name\", " + "\""
                    + JoinAttributeKey.DELIMETER.toString() + "\" : "
                    + "\" \"}");
            FK_CONVERSION_MAP.put("hk_system_event_info.last_modified_by", "{"
                    + "\"" + JoinAttributeKey.FK_TABLE.toString() + "\" : "
                    + "\"um_system_user\", "
                    + "\"" + JoinAttributeKey.FK_COLUMN.toString() + "\" : "
                    + "\"id\", "
                    + "\"" + JoinAttributeKey.SHOW_COLUMN.toString() + "\" : "
                    + "\"first_name:middle_name:last_name\", " + "\""
                    + JoinAttributeKey.DELIMETER.toString() + "\" : "
                    + "\" \"}");
            /**
             * ********************* um_dept_mst table *********************
             */
            FK_CONVERSION_MAP.put("um_dept_mst.parent", "{"
                    + "\"" + JoinAttributeKey.FK_TABLE.toString() + "\" : "
                    + "\"um_dept_mst\", "
                    + "\"" + JoinAttributeKey.FK_COLUMN.toString() + "\" : "
                    + "\"id\", "
                    + "\"" + JoinAttributeKey.SHOW_COLUMN.toString() + "\" : "
                    + "\"dept_name\"}");
            FK_CONVERSION_MAP.put("um_dept_mst.created_by", "{"
                    + "\"" + JoinAttributeKey.FK_TABLE.toString() + "\" : "
                    + "\"um_system_user\", "
                    + "\"" + JoinAttributeKey.FK_COLUMN.toString() + "\" : "
                    + "\"id\", "
                    + "\"" + JoinAttributeKey.SHOW_COLUMN.toString() + "\" : "
                    + "\"first_name:middle_name:last_name\", " + "\""
                    + JoinAttributeKey.DELIMETER.toString() + "\" : "
                    + "\" \"}");
            FK_CONVERSION_MAP.put("um_dept_mst.last_modified_by", "{"
                    + "\"" + JoinAttributeKey.FK_TABLE.toString() + "\" : "
                    + "\"um_system_user\", "
                    + "\"" + JoinAttributeKey.FK_COLUMN.toString() + "\" : "
                    + "\"id\", "
                    + "\"" + JoinAttributeKey.SHOW_COLUMN.toString() + "\" : "
                    + "\"first_name:middle_name:last_name\", " + "\""
                    + JoinAttributeKey.DELIMETER.toString() + "\" : "
                    + "\" \"}");
            /**
             * ********************* um_dept_role_dtl table
             * *********************
             */
            FK_CONVERSION_MAP.put("um_dept_role_dtl.dept", "{"
                    + "\"" + JoinAttributeKey.FK_TABLE.toString() + "\" : "
                    + "\"um_dept_mst\", "
                    + "\"" + JoinAttributeKey.FK_COLUMN.toString() + "\" : "
                    + "\"id\", "
                    + "\"" + JoinAttributeKey.SHOW_COLUMN.toString() + "\" : "
                    + "\"dept_name\"}");
            FK_CONVERSION_MAP.put("um_dept_role_dtl.role", "{"
                    + "\"" + JoinAttributeKey.FK_TABLE.toString() + "\" : "
                    + "\"um_system_role\", "
                    + "\"" + JoinAttributeKey.FK_COLUMN.toString() + "\" : "
                    + "\"id\", "
                    + "\"" + JoinAttributeKey.SHOW_COLUMN.toString() + "\" : "
                    + "\"name\"}");
            /**
             * ********************* hk_system_shift_info table
             * *********************
             */
            FK_CONVERSION_MAP.put("hk_system_shift_info.temporary_shift_for", "{"
                    + "\"" + JoinAttributeKey.FK_TABLE.toString() + "\" : "
                    + "\"hk_system_shift_info\", "
                    + "\"" + JoinAttributeKey.FK_COLUMN.toString() + "\" : "
                    + "\"id\", "
                    + "\"" + JoinAttributeKey.SHOW_COLUMN.toString() + "\" : "
                    + "\"shift_title\"}");
            FK_CONVERSION_MAP.put("hk_system_shift_info.created_by", "{"
                    + "\"" + JoinAttributeKey.FK_TABLE.toString() + "\" : "
                    + "\"um_system_user\", "
                    + "\"" + JoinAttributeKey.FK_COLUMN.toString() + "\" : "
                    + "\"id\", "
                    + "\"" + JoinAttributeKey.SHOW_COLUMN.toString() + "\" : "
                    + "\"first_name:middle_name:last_name\", " + "\""
                    + JoinAttributeKey.DELIMETER.toString() + "\" : "
                    + "\" \"}");
            FK_CONVERSION_MAP.put("hk_system_shift_info.last_modified_by", "{"
                    + "\"" + JoinAttributeKey.FK_TABLE.toString() + "\" : "
                    + "\"um_system_user\", "
                    + "\"" + JoinAttributeKey.FK_COLUMN.toString() + "\" : "
                    + "\"id\", "
                    + "\"" + JoinAttributeKey.SHOW_COLUMN.toString() + "\" : "
                    + "\"first_name:middle_name:last_name\", " + "\""
                    + JoinAttributeKey.DELIMETER.toString() + "\" : "
                    + "\" \"}");
            /**
             * ********************* hk_shift_dtl table *********************
             */
            FK_CONVERSION_MAP.put("hk_shift_dtl.created_by", "{"
                    + "\"" + JoinAttributeKey.FK_TABLE.toString() + "\" : "
                    + "\"um_system_user\", "
                    + "\"" + JoinAttributeKey.FK_COLUMN.toString() + "\" : "
                    + "\"id\", "
                    + "\"" + JoinAttributeKey.SHOW_COLUMN.toString() + "\" : "
                    + "\"first_name:middle_name:last_name\", " + "\""
                    + JoinAttributeKey.DELIMETER.toString() + "\" : "
                    + "\" \"}");
            FK_CONVERSION_MAP.put("hk_shift_dtl.last_modified_by", "{"
                    + "\"" + JoinAttributeKey.FK_TABLE.toString() + "\" : "
                    + "\"um_system_user\", "
                    + "\"" + JoinAttributeKey.FK_COLUMN.toString() + "\" : "
                    + "\"id\", "
                    + "\"" + JoinAttributeKey.SHOW_COLUMN.toString() + "\" : "
                    + "\"first_name:middle_name:last_name\", " + "\""
                    + JoinAttributeKey.DELIMETER.toString() + "\" : " + "\" \"}");
            /**
             * ********************* hk_system_task_info table
             * *********************
             */
            FK_CONVERSION_MAP.put("hk_system_task_info.created_by", "{"
                    + "\"" + JoinAttributeKey.FK_TABLE.toString() + "\" : "
                    + "\"um_system_user\", "
                    + "\"" + JoinAttributeKey.FK_COLUMN.toString() + "\" : "
                    + "\"id\", "
                    + "\"" + JoinAttributeKey.SHOW_COLUMN.toString() + "\" : "
                    + "\"first_name:middle_name:last_name\", " + "\""
                    + JoinAttributeKey.DELIMETER.toString() + "\" : "
                    + "\" \"}");
            FK_CONVERSION_MAP.put("hk_system_task_info.task_category", "{"
                    + "\"" + JoinAttributeKey.FK_TABLE.toString() + "\" : "
                    + "\"hk_system_category_mst\", "
                    + "\"" + JoinAttributeKey.FK_COLUMN.toString() + "\" : "
                    + "\"id\", "
                    + "\"" + JoinAttributeKey.SHOW_COLUMN.toString() + "\" : "
                    + "\"category_title\"}");
            /**
             * ********************* um_system_feature table
             * *********************
             */
            
            FK_CONVERSION_MAP.put("um_system_feature.parent", "{"
                    + "\"" + JoinAttributeKey.FK_TABLE.toString() + "\" : "
                    + "\"um_system_feature\", "
                    + "\"" + JoinAttributeKey.FK_COLUMN.toString() + "\" : "
                    + "\"id\", "
                    + "\"" + JoinAttributeKey.SHOW_COLUMN.toString() + "\" : "
                    + "\"menu_label\"}");
            /**
             * ********************* hk_system_category_mst table
             * *********************
             */
            FK_CONVERSION_MAP.put("hk_system_category_mst.created_by", "{"
                    + "\"" + JoinAttributeKey.FK_TABLE.toString() + "\" : "
                    + "\"um_system_user\", "
                    + "\"" + JoinAttributeKey.FK_COLUMN.toString() + "\" : "
                    + "\"id\", "
                    + "\"" + JoinAttributeKey.SHOW_COLUMN.toString() + "\" : "
                    + "\"first_name:middle_name:last_name\", " + "\""
                    + JoinAttributeKey.DELIMETER.toString() + "\" : "
                    + "\" \"}");
            FK_CONVERSION_MAP.put("hk_system_category_mst.last_modified_by", "{"
                    + "\"" + JoinAttributeKey.FK_TABLE.toString() + "\" : "
                    + "\"um_system_user\", "
                    + "\"" + JoinAttributeKey.FK_COLUMN.toString() + "\" : "
                    + "\"id\", "
                    + "\"" + JoinAttributeKey.SHOW_COLUMN.toString() + "\" : "
                    + "\"first_name:middle_name:last_name\", " + "\""
                    + JoinAttributeKey.DELIMETER.toString() + "\" : "
                    + "\" \"}");
            FK_CONVERSION_MAP.put("hk_system_category_mst.parent", "{"
                    + "\"" + JoinAttributeKey.FK_TABLE.toString() + "\" : "
                    + "\"hk_system_category_mst\", "
                    + "\"" + JoinAttributeKey.FK_COLUMN.toString() + "\" : "
                    + "\"id\", "
                    + "\"" + JoinAttributeKey.SHOW_COLUMN.toString() + "\" : "
                    + "\"category_title\"}");
            /**
             * ********************* hk_system_asset_info table
             * *********************
             */
            FK_CONVERSION_MAP.put("hk_system_asset_info.created_by", "{"
                    + "\"" + JoinAttributeKey.FK_TABLE.toString() + "\" : "
                    + "\"um_system_user\", "
                    + "\"" + JoinAttributeKey.FK_COLUMN.toString() + "\" : "
                    + "\"id\", "
                    + "\"" + JoinAttributeKey.SHOW_COLUMN.toString() + "\" : "
                    + "\"first_name:middle_name:last_name\", " + "\""
                    + JoinAttributeKey.DELIMETER.toString() + "\" : "
                    + "\" \"}");
            FK_CONVERSION_MAP.put("hk_system_asset_info.last_modified_by", "{"
                    + "\"" + JoinAttributeKey.FK_TABLE.toString() + "\" : "
                    + "\"um_system_user\", "
                    + "\"" + JoinAttributeKey.FK_COLUMN.toString() + "\" : "
                    + "\"id\", "
                    + "\"" + JoinAttributeKey.SHOW_COLUMN.toString() + "\" : "
                    + "\"first_name:middle_name:last_name\", " + "\""
                    + JoinAttributeKey.DELIMETER.toString() + "\" : "
                    + "\" \"}");
            FK_CONVERSION_MAP.put("hk_system_asset_info.category", "{"
                    + "\"" + JoinAttributeKey.FK_TABLE.toString() + "\" : "
                    + "\"hk_system_category_mst\", "
                    + "\"" + JoinAttributeKey.FK_COLUMN.toString() + "\" : "
                    + "\"id\", "
                    + "\"" + JoinAttributeKey.SHOW_COLUMN.toString() + "\" : "
                    + "\"category_title\"}");
            /**
             * ********************* hk_system_holiday_mst table
             * *********************
             */
            FK_CONVERSION_MAP.put("hk_system_holiday_mst.created_by", "{"
                    + "\"" + JoinAttributeKey.FK_TABLE.toString() + "\" : "
                    + "\"um_system_user\", "
                    + "\"" + JoinAttributeKey.FK_COLUMN.toString() + "\" : "
                    + "\"id\", "
                    + "\"" + JoinAttributeKey.SHOW_COLUMN.toString() + "\" : "
                    + "\"first_name:middle_name:last_name\", " + "\""
                    + JoinAttributeKey.DELIMETER.toString() + "\" : "
                    + "\" \"}");
            FK_CONVERSION_MAP.put("hk_system_holiday_mst.last_modified_by", "{"
                    + "\"" + JoinAttributeKey.FK_TABLE.toString() + "\" : "
                    + "\"um_system_user\", "
                    + "\"" + JoinAttributeKey.FK_COLUMN.toString() + "\" : "
                    + "\"id\", "
                    + "\"" + JoinAttributeKey.SHOW_COLUMN.toString() + "\" : "
                    + "\"first_name:middle_name:last_name\", " + "\""
                    + JoinAttributeKey.DELIMETER.toString() + "\" : "
                    + "\" \"}");
            /**
             * ********************* hk_workflow_info table
             * *********************
             */
            FK_CONVERSION_MAP.put("hk_workflow_info.department", "{"
                    + "\"" + JoinAttributeKey.FK_TABLE.toString() + "\" : "
                    + "\"um_dept_mst\", "
                    + "\"" + JoinAttributeKey.FK_COLUMN.toString() + "\" : "
                    + "\"id\", "
                    + "\"" + JoinAttributeKey.SHOW_COLUMN.toString() + "\" : "
                    + "\"dept_name\"}");
            FK_CONVERSION_MAP.put("hk_workflow_info.created_by", "{"
                    + "\"" + JoinAttributeKey.FK_TABLE.toString() + "\" : "
                    + "\"um_system_user\", "
                    + "\"" + JoinAttributeKey.FK_COLUMN.toString() + "\" : "
                    + "\"id\", "
                    + "\"" + JoinAttributeKey.SHOW_COLUMN.toString() + "\" : "
                    + "\"first_name:middle_name:last_name\", " + "\""
                    + JoinAttributeKey.DELIMETER.toString() + "\" : "
                    + "\" \"}");
            FK_CONVERSION_MAP.put("hk_workflow_info.last_modified_by", "{"
                    + "\"" + JoinAttributeKey.FK_TABLE.toString() + "\" : "
                    + "\"um_system_user\", "
                    + "\"" + JoinAttributeKey.FK_COLUMN.toString() + "\" : "
                    + "\"id\", "
                    + "\"" + JoinAttributeKey.SHOW_COLUMN.toString() + "\" : "
                    + "\"first_name:middle_name:last_name\", " + "\""
                    + JoinAttributeKey.DELIMETER.toString() + "\" : "
                    + "\" \"}");
            /**
             * ********************* hk_work_assignment_info table
             * *********************
             */
            FK_CONVERSION_MAP.put("hk_work_assignment_info.created_by", "{"
                    + "\"" + JoinAttributeKey.FK_TABLE.toString() + "\" : "
                    + "\"um_system_user\", "
                    + "\"" + JoinAttributeKey.FK_COLUMN.toString() + "\" : "
                    + "\"id\", "
                    + "\"" + JoinAttributeKey.SHOW_COLUMN.toString() + "\" : "
                    + "\"first_name:middle_name:last_name\", " + "\""
                    + JoinAttributeKey.DELIMETER.toString() + "\" : "
                    + "\" \"}");
            FK_CONVERSION_MAP.put("hk_work_assignment_info.last_modified_by", "{"
                    + "\"" + JoinAttributeKey.FK_TABLE.toString() + "\" : "
                    + "\"um_system_user\", "
                    + "\"" + JoinAttributeKey.FK_COLUMN.toString() + "\" : "
                    + "\"id\", "
                    + "\"" + JoinAttributeKey.SHOW_COLUMN.toString() + "\" : "
                    + "\"first_name:middle_name:last_name\", " + "\""
                    + JoinAttributeKey.DELIMETER.toString() + "\" : "
                    + "\" \"}");
            /**
             * ********************* hk_user_operation_info table
             * *********************
             */
            FK_CONVERSION_MAP.put("hk_user_operation_info.last_modified_by", "{"
                    + "\"" + JoinAttributeKey.FK_TABLE.toString() + "\" : "
                    + "\"um_system_user\", "
                    + "\"" + JoinAttributeKey.FK_COLUMN.toString() + "\" : "
                    + "\"id\", "
                    + "\"" + JoinAttributeKey.SHOW_COLUMN.toString() + "\" : "
                    + "\"first_name:middle_name:last_name\", " + "\""
                    + JoinAttributeKey.DELIMETER.toString() + "\" : "
                    + "\" \"}");
            /**
             * ********************* hk_system_message_info table
             * *********************
             */
            FK_CONVERSION_MAP.put("hk_system_message_info.created_by", "{"
                    + "\"" + JoinAttributeKey.FK_TABLE.toString() + "\" : "
                    + "\"um_system_user\", "
                    + "\"" + JoinAttributeKey.FK_COLUMN.toString() + "\" : "
                    + "\"id\", "
                    + "\"" + JoinAttributeKey.SHOW_COLUMN.toString() + "\" : "
                    + "\"first_name:middle_name:last_name\", " + "\""
                    + JoinAttributeKey.DELIMETER.toString() + "\" : "
                    + "\" \"}");
            /**
             * ********************* hk_system_goal_template_info table
             * *********************
             */
            FK_CONVERSION_MAP.put("hk_system_goal_template_info.created_by", "{"
                    + "\"" + JoinAttributeKey.FK_TABLE.toString() + "\" : "
                    + "\"um_system_user\", "
                    + "\"" + JoinAttributeKey.FK_COLUMN.toString() + "\" : "
                    + "\"id\", "
                    + "\"" + JoinAttributeKey.SHOW_COLUMN.toString() + "\" : "
                    + "\"first_name:middle_name:last_name\", " + "\""
                    + JoinAttributeKey.DELIMETER.toString() + "\" : "
                    + "\" \"}");
            FK_CONVERSION_MAP.put("hk_system_goal_template_info.last_modified_by", "{"
                    + "\"" + JoinAttributeKey.FK_TABLE.toString() + "\" : "
                    + "\"um_system_user\", "
                    + "\"" + JoinAttributeKey.FK_COLUMN.toString() + "\" : "
                    + "\"id\", "
                    + "\"" + JoinAttributeKey.SHOW_COLUMN.toString() + "\" : "
                    + "\"first_name:middle_name:last_name\", " + "\""
                    + JoinAttributeKey.DELIMETER.toString() + "\" : "
                    + "\" \"}");
            FK_CONVERSION_MAP.put("hk_system_goal_template_info.for_department", "{"
                    + "\"" + JoinAttributeKey.FK_TABLE.toString() + "\" : "
                    + "\"um_dept_mst\", "
                    + "\"" + JoinAttributeKey.FK_COLUMN.toString() + "\" : "
                    + "\"id\", "
                    + "\"" + JoinAttributeKey.SHOW_COLUMN.toString() + "\" : "
                    + "\"dept_name\"}");
            FK_CONVERSION_MAP.put("hk_system_goal_template_info.for_designation", "{"
                    + "\"" + JoinAttributeKey.FK_TABLE.toString() + "\" : "
                    + "\"um_system_role\", "
                    + "\"" + JoinAttributeKey.FK_COLUMN.toString() + "\" : "
                    + "\"id\", "
                    + "\"" + JoinAttributeKey.SHOW_COLUMN.toString() + "\" : "
                    + "\"name\"}");
            FK_CONVERSION_MAP.put("hk_system_goal_template_info.for_service", "{"
                    + "\"" + JoinAttributeKey.FK_TABLE.toString() + "\" : "
                    + "\"act_activity_flow_node_info\", "
                    + "\"" + JoinAttributeKey.FK_COLUMN.toString() + "\" : "
                    + "\"id\", "
                    + "\"" + JoinAttributeKey.SHOW_COLUMN.toString() + "\" : "
                    + "\"associated_service\", "
                    + "\"" + JoinAttributeKey.IS_ANOTHER_CONVERSION.toString() + "\" : "
                    + "true}");
            FK_CONVERSION_MAP.put("hk_system_goal_template_info.realization_rule", "{"
                    + "\"" + JoinAttributeKey.FK_TABLE.toString() + "\" : "
                    + "\"ruleset\", "
                    + "\"" + JoinAttributeKey.FK_COLUMN.toString() + "\" : "
                    + "\"_id\", "
                    + "\"" + JoinAttributeKey.SHOW_COLUMN.toString() + "\" : "
                    + "\"rules\"}");
            /**
             * ********************* hk_system_currency_info table
             * *********************
             */
            FK_CONVERSION_MAP.put("hk_system_currency_info.created_by", "{"
                    + "\"" + JoinAttributeKey.FK_TABLE.toString() + "\" : "
                    + "\"um_system_user\", "
                    + "\"" + JoinAttributeKey.FK_COLUMN.toString() + "\" : "
                    + "\"id\", "
                    + "\"" + JoinAttributeKey.SHOW_COLUMN.toString() + "\" : "
                    + "\"first_name:middle_name:last_name\", " + "\""
                    + JoinAttributeKey.DELIMETER.toString() + "\" : "
                    + "\" \"}");
            FK_CONVERSION_MAP.put("hk_system_currency_info.last_modified_by", "{"
                    + "\"" + JoinAttributeKey.FK_TABLE.toString() + "\" : "
                    + "\"um_system_user\", "
                    + "\"" + JoinAttributeKey.FK_COLUMN.toString() + "\" : "
                    + "\"id\", "
                    + "\"" + JoinAttributeKey.SHOW_COLUMN.toString() + "\" : "
                    + "\"first_name:middle_name:last_name\", " + "\""
                    + JoinAttributeKey.DELIMETER.toString() + "\" : "
                    + "\" \"}");
            /**
             * ********************* hk_sub_entity_field_info table
             * *********************
             */
            FK_CONVERSION_MAP.put("hk_sub_entity_field_info.parent_field", "{"
                    + "\"" + JoinAttributeKey.FK_TABLE.toString() + "\" : "
                    + "\"hk_field_info\", "
                    + "\"" + JoinAttributeKey.FK_COLUMN.toString() + "\" : "
                    + "\"id\", "
                    + "\"" + JoinAttributeKey.SHOW_COLUMN.toString() + "\" : "
                    + "\"field_label\"}");
            FK_CONVERSION_MAP.put("hk_sub_entity_field_info.last_modified_by", "{"
                    + "\"" + JoinAttributeKey.FK_TABLE.toString() + "\" : "
                    + "\"um_system_user\", "
                    + "\"" + JoinAttributeKey.FK_COLUMN.toString() + "\" : "
                    + "\"id\", "
                    + "\"" + JoinAttributeKey.SHOW_COLUMN.toString() + "\" : "
                    + "\"first_name:middle_name:last_name\", " + "\""
                    + JoinAttributeKey.DELIMETER.toString() + "\" : "
                    + "\" \"}");
            /**
             * ********************* hk_notification_configuration_info table
             * *********************
             */
            FK_CONVERSION_MAP.put("hk_notification_configuration_info.created_by", "{"
                    + "\"" + JoinAttributeKey.FK_TABLE.toString() + "\" : "
                    + "\"um_system_user\", "
                    + "\"" + JoinAttributeKey.FK_COLUMN.toString() + "\" : "
                    + "\"id\", "
                    + "\"" + JoinAttributeKey.SHOW_COLUMN.toString() + "\" : "
                    + "\"first_name:middle_name:last_name\", " + "\""
                    + JoinAttributeKey.DELIMETER.toString() + "\" : "
                    + "\" \"}");
            FK_CONVERSION_MAP.put("hk_notification_configuration_info.last_modified_by", "{"
                    + "\"" + JoinAttributeKey.FK_TABLE.toString() + "\" : "
                    + "\"um_system_user\", "
                    + "\"" + JoinAttributeKey.FK_COLUMN.toString() + "\" : "
                    + "\"id\", "
                    + "\"" + JoinAttributeKey.SHOW_COLUMN.toString() + "\" : "
                    + "\"first_name:middle_name:last_name\", " + "\""
                    + JoinAttributeKey.DELIMETER.toString() + "\" : "
                    + "\" \"}");
            /**
             * ********************* hk_location_mst table
             * *********************
             */
            FK_CONVERSION_MAP.put("hk_location_mst.parent", "{"
                    + "\"" + JoinAttributeKey.FK_TABLE.toString() + "\" : "
                    + "\"hk_location_mst\", "
                    + "\"" + JoinAttributeKey.FK_COLUMN.toString() + "\" : "
                    + "\"id\", "
                    + "\"" + JoinAttributeKey.SHOW_COLUMN.toString() + "\" : "
                    + "\"location_name\"}");
            FK_CONVERSION_MAP.put("hk_location_mst.created_by", "{"
                    + "\"" + JoinAttributeKey.FK_TABLE.toString() + "\" : "
                    + "\"um_system_user\", "
                    + "\"" + JoinAttributeKey.FK_COLUMN.toString() + "\" : "
                    + "\"id\", "
                    + "\"" + JoinAttributeKey.SHOW_COLUMN.toString() + "\" : "
                    + "\"first_name:middle_name:last_name\", " + "\""
                    + JoinAttributeKey.DELIMETER.toString() + "\" : "
                    + "\" \"}");
            FK_CONVERSION_MAP.put("hk_location_mst.last_modified_by", "{"
                    + "\"" + JoinAttributeKey.FK_TABLE.toString() + "\" : "
                    + "\"um_system_user\", "
                    + "\"" + JoinAttributeKey.FK_COLUMN.toString() + "\" : "
                    + "\"id\", "
                    + "\"" + JoinAttributeKey.SHOW_COLUMN.toString() + "\" : "
                    + "\"first_name:middle_name:last_name\", " + "\""
                    + JoinAttributeKey.DELIMETER.toString() + "\" : "
                    + "\" \"}");
            /**
             * ********************* hk_leave_info table *********************
             */
            FK_CONVERSION_MAP.put("hk_leave_info.created_by", "{"
                    + "\"" + JoinAttributeKey.FK_TABLE.toString() + "\" : "
                    + "\"um_system_user\", "
                    + "\"" + JoinAttributeKey.FK_COLUMN.toString() + "\" : "
                    + "\"id\", "
                    + "\"" + JoinAttributeKey.SHOW_COLUMN.toString() + "\" : "
                    + "\"first_name:middle_name:last_name\", " + "\""
                    + JoinAttributeKey.DELIMETER.toString() + "\" : "
                    + "\" \"}");
            /**
             * ********************* hk_goal_permission_info table
             * *********************
             */
            FK_CONVERSION_MAP.put("hk_goal_permission_info.last_modified_by", "{"
                    + "\"" + JoinAttributeKey.FK_TABLE.toString() + "\" : "
                    + "\"um_system_user\", "
                    + "\"" + JoinAttributeKey.FK_COLUMN.toString() + "\" : "
                    + "\"id\", "
                    + "\"" + JoinAttributeKey.SHOW_COLUMN.toString() + "\" : "
                    + "\"first_name:middle_name:last_name\", " + "\""
                    + JoinAttributeKey.DELIMETER.toString() + "\" : "
                    + "\" \"}");
            /**
             * ********************* hk_field_sequence_info table
             * *********************
             */
            FK_CONVERSION_MAP.put("hk_field_sequence_info.last_modified_by", "{"
                    + "\"" + JoinAttributeKey.FK_TABLE.toString() + "\" : "
                    + "\"um_system_user\", "
                    + "\"" + JoinAttributeKey.FK_COLUMN.toString() + "\" : "
                    + "\"id\", "
                    + "\"" + JoinAttributeKey.SHOW_COLUMN.toString() + "\" : "
                    + "\"first_name:middle_name:last_name\", " + "\""
                    + JoinAttributeKey.DELIMETER.toString() + "\" : "
                    + "\" \"}");
            /**
             * ********************* hk_feature_field_permission_dtl table
             * *********************
             */
            FK_CONVERSION_MAP.put("hk_feature_field_permission_dtl.last_modified_by", "{"
                    + "\"" + JoinAttributeKey.FK_TABLE.toString() + "\" : "
                    + "\"um_system_user\", "
                    + "\"" + JoinAttributeKey.FK_COLUMN.toString() + "\" : "
                    + "\"id\", "
                    + "\"" + JoinAttributeKey.SHOW_COLUMN.toString() + "\" : "
                    + "\"first_name:middle_name:last_name\", " + "\""
                    + JoinAttributeKey.DELIMETER.toString() + "\" : "
                    + "\" \"}");
            /**
             * ********************* hk_event_registration_field_info table
             * *********************
             */
            FK_CONVERSION_MAP.put("hk_event_registration_field_info.last_modified_by", "{"
                    + "\"" + JoinAttributeKey.FK_TABLE.toString() + "\" : "
                    + "\"um_system_user\", "
                    + "\"" + JoinAttributeKey.FK_COLUMN.toString() + "\" : "
                    + "\"id\", "
                    + "\"" + JoinAttributeKey.SHOW_COLUMN.toString() + "\" : "
                    + "\"first_name:middle_name:last_name\", " + "\""
                    + JoinAttributeKey.DELIMETER.toString() + "\" : "
                    + "\" \"}");
            FK_CONVERSION_MAP.put("hk_event_registration_field_info.event", "{"
                    + "\"" + JoinAttributeKey.FK_TABLE.toString() + "\" : "
                    + "\"hk_system_event_info\", "
                    + "\"" + JoinAttributeKey.FK_COLUMN.toString() + "\" : "
                    + "\"id\", "
                    + "\"" + JoinAttributeKey.SHOW_COLUMN.toString() + "\" : "
                    + "\"event_title\"}");
            /**
             * ********************* hk_user_event_registration_info table
             * *********************
             */
            FK_CONVERSION_MAP.put("hk_user_event_registration_info.user_id", "{"
                    + "\"" + JoinAttributeKey.FK_TABLE.toString() + "\" : "
                    + "\"um_system_user\", "
                    + "\"" + JoinAttributeKey.FK_COLUMN.toString() + "\" : "
                    + "\"id\", "
                    + "\"" + JoinAttributeKey.SHOW_COLUMN.toString() + "\" : "
                    + "\"first_name:middle_name:last_name\", " + "\""
                    + JoinAttributeKey.DELIMETER.toString() + "\" : "
                    + "\" \"}");
            /**
             * ********************* hk_currency_rate_detail table
             * *********************
             */
            FK_CONVERSION_MAP.put("hk_currency_rate_detail.last_modified_by", "{"
                    + "\"" + JoinAttributeKey.FK_TABLE.toString() + "\" : "
                    + "\"um_system_user\", "
                    + "\"" + JoinAttributeKey.FK_COLUMN.toString() + "\" : "
                    + "\"id\", "
                    + "\"" + JoinAttributeKey.SHOW_COLUMN.toString() + "\" : "
                    + "\"first_name:middle_name:last_name\", " + "\""
                    + JoinAttributeKey.DELIMETER.toString() + "\" : "
                    + "\" \"}");
            FK_CONVERSION_MAP.put("hk_currency_rate_detail.prev_currency_rate", "{"
                    + "\"" + JoinAttributeKey.FK_TABLE.toString() + "\" : "
                    + "\"hk_currency_rate_detail\", "
                    + "\"" + JoinAttributeKey.FK_COLUMN.toString() + "\" : "
                    + "\"id\", "
                    + "\"" + JoinAttributeKey.SHOW_COLUMN.toString() + "\" : "
                    + "\"reference_rate\"}");
            FK_CONVERSION_MAP.put("hk_currency_rate_detail.currency", "{"
                    + "\"" + JoinAttributeKey.FK_TABLE.toString() + "\" : "
                    + "\"hk_system_currency_info\", "
                    + "\"" + JoinAttributeKey.FK_COLUMN.toString() + "\" : "
                    + "\"id\", "
                    + "\"" + JoinAttributeKey.SHOW_COLUMN.toString() + "\" : "
                    + "\"code\"}");
            /**
             * ********************* hk_field_info table *********************
             */
            FK_CONVERSION_MAP.put("hk_field_info.created_by", "{"
                    + "\"" + JoinAttributeKey.FK_TABLE.toString() + "\" : "
                    + "\"um_system_user\", "
                    + "\"" + JoinAttributeKey.FK_COLUMN.toString() + "\" : "
                    + "\"id\", "
                    + "\"" + JoinAttributeKey.SHOW_COLUMN.toString() + "\" : "
                    + "\"first_name:middle_name:last_name\", " + "\""
                    + JoinAttributeKey.DELIMETER.toString() + "\" : "
                    + "\" \"}");
            FK_CONVERSION_MAP.put("hk_field_info.last_modified_by", "{"
                    + "\"" + JoinAttributeKey.FK_TABLE.toString() + "\" : "
                    + "\"um_system_user\", "
                    + "\"" + JoinAttributeKey.FK_COLUMN.toString() + "\" : "
                    + "\"id\", "
                    + "\"" + JoinAttributeKey.SHOW_COLUMN.toString() + "\" : "
                    + "\"first_name:middle_name:last_name\", " + "\""
                    + JoinAttributeKey.DELIMETER.toString() + "\" : "
                    + "\" \"}");
            FK_CONVERSION_MAP.put("hk_field_info.section", "{"
                    + "\"" + JoinAttributeKey.FK_TABLE.toString() + "\" : "
                    + "\"hk_section_info\", "
                    + "\"" + JoinAttributeKey.FK_COLUMN.toString() + "\" : "
                    + "\"id\", "
                    + "\"" + JoinAttributeKey.SHOW_COLUMN.toString() + "\" : "
                    + "\"section_name\"}");
            /**
             * ********************* hk_asset_issue_info table
             * *********************
             */
            FK_CONVERSION_MAP.put("hk_asset_issue_info.created_by", "{"
                    + "\"" + JoinAttributeKey.FK_TABLE.toString() + "\" : "
                    + "\"um_system_user\", "
                    + "\"" + JoinAttributeKey.FK_COLUMN.toString() + "\" : "
                    + "\"id\", "
                    + "\"" + JoinAttributeKey.SHOW_COLUMN.toString() + "\" : "
                    + "\"first_name:middle_name:last_name\", " + "\""
                    + JoinAttributeKey.DELIMETER.toString() + "\" : "
                    + "\" \"}");
            FK_CONVERSION_MAP.put("hk_asset_issue_info.last_modified_by", "{"
                    + "\"" + JoinAttributeKey.FK_TABLE.toString() + "\" : "
                    + "\"um_system_user\", "
                    + "\"" + JoinAttributeKey.FK_COLUMN.toString() + "\" : "
                    + "\"id\", "
                    + "\"" + JoinAttributeKey.SHOW_COLUMN.toString() + "\" : "
                    + "\"first_name:middle_name:last_name\", " + "\""
                    + JoinAttributeKey.DELIMETER.toString() + "\" : "
                    + "\" \"}");
            FK_CONVERSION_MAP.put("hk_asset_issue_info.asset", "{"
                    + "\"" + JoinAttributeKey.FK_TABLE.toString() + "\" : "
                    + "\"hk_system_asset_info\", "
                    + "\"" + JoinAttributeKey.FK_COLUMN.toString() + "\" : "
                    + "\"id\", "
                    + "\"" + JoinAttributeKey.SHOW_COLUMN.toString() + "\" : "
                    + "\"asset_name\"}");
            /**
             * ********************* hk_shift_department_info table
             * *********************
             */
            FK_CONVERSION_MAP.put("hk_shift_department_info.department", "{"
                    + "\"" + JoinAttributeKey.FK_TABLE.toString() + "\" : "
                    + "\"um_dept_mst\", "
                    + "\"" + JoinAttributeKey.FK_COLUMN.toString() + "\" : "
                    + "\"id\", "
                    + "\"" + JoinAttributeKey.SHOW_COLUMN.toString() + "\" : "
                    + "\"dept_name\"}");
            FK_CONVERSION_MAP.put("hk_shift_department_info.shift", "{"
                    + "\"" + JoinAttributeKey.FK_TABLE.toString() + "\" : "
                    + "\"hk_system_shift_info\", "
                    + "\"" + JoinAttributeKey.FK_COLUMN.toString() + "\" : "
                    + "\"id\", "
                    + "\"" + JoinAttributeKey.SHOW_COLUMN.toString() + "\" : "
                    + "\"shift_title\"}");
            /**
             * ********************* um_system_role table *********************
             */
            FK_CONVERSION_MAP.put("um_system_role.created_by", "{"
                    + "\"" + JoinAttributeKey.FK_TABLE.toString() + "\" : "
                    + "\"um_system_user\", "
                    + "\"" + JoinAttributeKey.FK_COLUMN.toString() + "\" : "
                    + "\"id\", "
                    + "\"" + JoinAttributeKey.SHOW_COLUMN.toString() + "\" : "
                    + "\"first_name:middle_name:last_name\", " + "\""
                    + JoinAttributeKey.DELIMETER.toString() + "\" : "
                    + "\" \"}");
            FK_CONVERSION_MAP.put("um_system_role.modified_by", "{"
                    + "\"" + JoinAttributeKey.FK_TABLE.toString() + "\" : "
                    + "\"um_system_user\", "
                    + "\"" + JoinAttributeKey.FK_COLUMN.toString() + "\" : "
                    + "\"id\", "
                    + "\"" + JoinAttributeKey.SHOW_COLUMN.toString() + "\" : "
                    + "\"first_name:middle_name:last_name\", " + "\""
                    + JoinAttributeKey.DELIMETER.toString() + "\" : "
                    + "\" \"}");

            /**
             * ***********************Conversion Map for the Diamond
             * Fields************** ********************* invoice document
             * *********************
             */
            FK_CONVERSION_MAP.put("invoice.createdBy", "{"
                    + "\"" + JoinAttributeKey.FK_TABLE.toString() + "\" : "
                    + "\"um_system_user\", "
                    + "\"" + JoinAttributeKey.FK_COLUMN.toString() + "\" : "
                    + "\"id\", "
                    + "\"" + JoinAttributeKey.SHOW_COLUMN.toString() + "\" : "
                    + "\"first_name:middle_name:last_name\", " + "\""
                    + JoinAttributeKey.DELIMETER.toString() + "\" : "
                    + "\" \"}");
            FK_CONVERSION_MAP.put("invoice.lastModifiedBy", "{"
                    + "\"" + JoinAttributeKey.FK_TABLE.toString() + "\" : "
                    + "\"um_system_user\", "
                    + "\"" + JoinAttributeKey.FK_COLUMN.toString() + "\" : "
                    + "\"id\", "
                    + "\"" + JoinAttributeKey.SHOW_COLUMN.toString() + "\" : "
                    + "\"first_name:middle_name:last_name\", " + "\""
                    + JoinAttributeKey.DELIMETER.toString() + "\" : "
                    + "\" \"}");

            /**
             * ********************* parcel document *********************
             */
            FK_CONVERSION_MAP.put("parcel.createdBy", "{"
                    + "\"" + JoinAttributeKey.FK_TABLE.toString() + "\" : "
                    + "\"um_system_user\", "
                    + "\"" + JoinAttributeKey.FK_COLUMN.toString() + "\" : "
                    + "\"id\", "
                    + "\"" + JoinAttributeKey.SHOW_COLUMN.toString() + "\" : "
                    + "\"first_name:middle_name:last_name\", " + "\""
                    + JoinAttributeKey.DELIMETER.toString() + "\" : "
                    + "\" \"}");
            FK_CONVERSION_MAP.put("parcel.lastModifiedBy", "{"
                    + "\"" + JoinAttributeKey.FK_TABLE.toString() + "\" : "
                    + "\"um_system_user\", "
                    + "\"" + JoinAttributeKey.FK_COLUMN.toString() + "\" : "
                    + "\"id\", "
                    + "\"" + JoinAttributeKey.SHOW_COLUMN.toString() + "\" : "
                    + "\"first_name:middle_name:last_name\", " + "\""
                    + JoinAttributeKey.DELIMETER.toString() + "\" : "
                    + "\" \"}");
            FK_CONVERSION_MAP.put("parcel.invoice", "{"
                    + "\"" + JoinAttributeKey.FK_TABLE.toString() + "\" : "
                    + "\"invoice\", "
                    + "\"" + JoinAttributeKey.FK_COLUMN.toString() + "\" : "
                    + "\"_id\", "
                    + "\"" + JoinAttributeKey.SHOW_COLUMN.toString() + "\" : "
                    + "\"\\\"fieldValue\\\" ->> '" + HkSystemConstantUtil.InvoiceStaticFields.INVOICE_ID + "'\"}");

            /**
             * ********************* lot document *********************
             */
            FK_CONVERSION_MAP.put("lot.createdBy", "{"
                    + "\"" + JoinAttributeKey.FK_TABLE.toString() + "\" : "
                    + "\"um_system_user\", "
                    + "\"" + JoinAttributeKey.FK_COLUMN.toString() + "\" : "
                    + "\"id\", "
                    + "\"" + JoinAttributeKey.SHOW_COLUMN.toString() + "\" : "
                    + "\"first_name:middle_name:last_name\", " + "\""
                    + JoinAttributeKey.DELIMETER.toString() + "\" : "
                    + "\" \"}");
            FK_CONVERSION_MAP.put("lot.lastModifiedBy", "{"
                    + "\"" + JoinAttributeKey.FK_TABLE.toString() + "\" : "
                    + "\"um_system_user\", "
                    + "\"" + JoinAttributeKey.FK_COLUMN.toString() + "\" : "
                    + "\"id\", "
                    + "\"" + JoinAttributeKey.SHOW_COLUMN.toString() + "\" : "
                    + "\"first_name:middle_name:last_name\", " + "\""
                    + JoinAttributeKey.DELIMETER.toString() + "\" : "
                    + "\" \"}");
            FK_CONVERSION_MAP.put("lot.invoice", "{"
                    + "\"" + JoinAttributeKey.FK_TABLE.toString() + "\" : "
                    + "\"invoice\", "
                    + "\"" + JoinAttributeKey.FK_COLUMN.toString() + "\" : "
                    + "\"_id\", "
                    + "\"" + JoinAttributeKey.SHOW_COLUMN.toString() + "\" : "
                    + "\"\\\"fieldValue\\\" ->> '" + HkSystemConstantUtil.InvoiceStaticFields.INVOICE_ID + "'\"}");
            FK_CONVERSION_MAP.put("lot.parcel", "{"
                    + "\"" + JoinAttributeKey.FK_TABLE.toString() + "\" : "
                    + "\"parcel\", "
                    + "\"" + JoinAttributeKey.FK_COLUMN.toString() + "\" : "
                    + "\"_id\", "
                    + "\"" + JoinAttributeKey.SHOW_COLUMN.toString() + "\" : "
                    + "\"\\\"fieldValue\\\" ->> '" + HkSystemConstantUtil.ParcelStaticFields.PARCEL_ID + "'\"}");
            FK_CONVERSION_MAP.put("(lot.\"fieldValue\"->>'"+HkSystemConstantUtil.LotStaticFieldName.CARATE_RANGE+"')::numeric", "{"
                    + "\"" + JoinAttributeKey.FK_TABLE.toString() + "\" : "
                    + "\"hk_carat_range_mst\", "
                    + "\"" + JoinAttributeKey.FK_COLUMN.toString() + "\" : "
                    + "\"id\", "
                    + "\"" + JoinAttributeKey.SHOW_COLUMN.toString() + "\" : "
                    + "\"min_value:max_value\", " + "\""
                    + JoinAttributeKey.DELIMETER.toString() + "\" : "
                    + "\"-\"}");
            /**
             * ********************* packet document *********************
             */
            FK_CONVERSION_MAP.put("packet.createdBy", "{"
                    + "\"" + JoinAttributeKey.FK_TABLE.toString() + "\" : "
                    + "\"um_system_user\", "
                    + "\"" + JoinAttributeKey.FK_COLUMN.toString() + "\" : "
                    + "\"id\", "
                    + "\"" + JoinAttributeKey.SHOW_COLUMN.toString() + "\" : "
                    + "\"first_name:middle_name:last_name\", " + "\""
                    + JoinAttributeKey.DELIMETER.toString() + "\" : "
                    + "\" \"}");
            FK_CONVERSION_MAP.put("packet.lastModifiedBy", "{"
                    + "\"" + JoinAttributeKey.FK_TABLE.toString() + "\" : "
                    + "\"um_system_user\", "
                    + "\"" + JoinAttributeKey.FK_COLUMN.toString() + "\" : "
                    + "\"id\", "
                    + "\"" + JoinAttributeKey.SHOW_COLUMN.toString() + "\" : "
                    + "\"first_name:middle_name:last_name\", " + "\""
                    + JoinAttributeKey.DELIMETER.toString() + "\" : "
                    + "\" \"}");
            FK_CONVERSION_MAP.put("packet.invoice", "{"
                    + "\"" + JoinAttributeKey.FK_TABLE.toString() + "\" : "
                    + "\"invoice\", "
                    + "\"" + JoinAttributeKey.FK_COLUMN.toString() + "\" : "
                    + "\"_id\", "
                    + "\"" + JoinAttributeKey.SHOW_COLUMN.toString() + "\" : "
                    + "\"\\\"fieldValue\\\" ->> '" + HkSystemConstantUtil.InvoiceStaticFields.INVOICE_ID + "'\"}");
            FK_CONVERSION_MAP.put("packet.parcel", "{"
                    + "\"" + JoinAttributeKey.FK_TABLE.toString() + "\" : "
                    + "\"parcel\", "
                    + "\"" + JoinAttributeKey.FK_COLUMN.toString() + "\" : "
                    + "\"_id\", "
                    + "\"" + JoinAttributeKey.SHOW_COLUMN.toString() + "\" : "
                    + "\"\\\"fieldValue\\\" ->> '" + HkSystemConstantUtil.ParcelStaticFields.PARCEL_ID + "'\"}");
            FK_CONVERSION_MAP.put("packet.lot", "{"
                    + "\"" + JoinAttributeKey.FK_TABLE.toString() + "\" : "
                    + "\"lot\", "
                    + "\"" + JoinAttributeKey.FK_COLUMN.toString() + "\" : "
                    + "\"_id\", "
                    + "\"" + JoinAttributeKey.SHOW_COLUMN.toString() + "\" : "
                    + "\"\\\"fieldValue\\\" ->> '" + HkSystemConstantUtil.AutoNumber.LOT_ID + "'\"}");
            FK_CONVERSION_MAP.put("(packet.\"fieldValue\"->>'"+HkSystemConstantUtil.PacketStaticFieldName.CARATE_RANGE+"')::numeric", "{"
                    + "\"" + JoinAttributeKey.FK_TABLE.toString() + "\" : "
                    + "\"hk_carat_range_mst\", "
                    + "\"" + JoinAttributeKey.FK_COLUMN.toString() + "\" : "
                    + "\"id\", "
                    + "\"" + JoinAttributeKey.SHOW_COLUMN.toString() + "\" : "
                    + "\"min_value:max_value\", " + "\""
                    + JoinAttributeKey.DELIMETER.toString() + "\" : "
                    + "\"-\"}");
            /**
             * ********************* act_activity_flow_node_route_info document
             * *********************
             */
            FK_CONVERSION_MAP.put("act_activity_flow_node_route_info.node_status", "{"
                    + "\"" + JoinAttributeKey.FK_TABLE.toString() + "\" : "
                    + "\"ruleset\", "
                    + "\"" + JoinAttributeKey.FK_COLUMN.toString() + "\" : "
                    + "\"_id\", "
                    + "\"" + JoinAttributeKey.SHOW_COLUMN.toString() + "\" : "
                    + "\"rules\"}");
            /**
             * ********************* hk_system_key_mst table
             * *********************
             */
            FK_CONVERSION_MAP.put("hk_system_key_mst.created_by", "{"
                    + "\"" + JoinAttributeKey.FK_TABLE.toString() + "\" : "
                    + "\"um_system_user\", "
                    + "\"" + JoinAttributeKey.FK_COLUMN.toString() + "\" : "
                    + "\"id\", "
                    + "\"" + JoinAttributeKey.SHOW_COLUMN.toString() + "\" : "
                    + "\"first_name:middle_name:last_name\", " + "\""
                    + JoinAttributeKey.DELIMETER.toString() + "\" : "
                    + "\" \"}");
            FK_CONVERSION_MAP.put("hk_system_key_mst.last_modified_by", "{"
                    + "\"" + JoinAttributeKey.FK_TABLE.toString() + "\" : "
                    + "\"um_system_user\", "
                    + "\"" + JoinAttributeKey.FK_COLUMN.toString() + "\" : "
                    + "\"id\", "
                    + "\"" + JoinAttributeKey.SHOW_COLUMN.toString() + "\" : "
                    + "\"first_name:middle_name:last_name\", " + "\""
                    + JoinAttributeKey.DELIMETER.toString() + "\" : "
                    + "\" \"}");
            /**
             * ********************* hk_carat_range_mst table
             * *********************
             */
            FK_CONVERSION_MAP.put("hk_carat_range_mst.created_by", "{"
                    + "\"" + JoinAttributeKey.FK_TABLE.toString() + "\" : "
                    + "\"um_system_user\", "
                    + "\"" + JoinAttributeKey.FK_COLUMN.toString() + "\" : "
                    + "\"id\", "
                    + "\"" + JoinAttributeKey.SHOW_COLUMN.toString() + "\" : "
                    + "\"first_name:middle_name:last_name\", " + "\""
                    + JoinAttributeKey.DELIMETER.toString() + "\" : "
                    + "\" \"}");
            FK_CONVERSION_MAP.put("hk_carat_range_mst.last_modified_by", "{"
                    + "\"" + JoinAttributeKey.FK_TABLE.toString() + "\" : "
                    + "\"um_system_user\", "
                    + "\"" + JoinAttributeKey.FK_COLUMN.toString() + "\" : "
                    + "\"id\", "
                    + "\"" + JoinAttributeKey.SHOW_COLUMN.toString() + "\" : "
                    + "\"first_name:middle_name:last_name\", " + "\""
                    + JoinAttributeKey.DELIMETER.toString() + "\" : "
                    + "\" \"}");
            /**
             * ********************* hk_price_list_info table
             * *********************
             */
            FK_CONVERSION_MAP.put("hk_price_list_info.uploaded_by", "{"
                    + "\"" + JoinAttributeKey.FK_TABLE.toString() + "\" : "
                    + "\"um_system_user\", "
                    + "\"" + JoinAttributeKey.FK_COLUMN.toString() + "\" : "
                    + "\"id\", "
                    + "\"" + JoinAttributeKey.SHOW_COLUMN.toString() + "\" : "
                    + "\"first_name:middle_name:last_name\", " + "\""
                    + JoinAttributeKey.DELIMETER.toString() + "\" : "
                    + "\" \"}");
            /**
             * ********************* hk_price_list_dtl table
             * *********************
             */
            FK_CONVERSION_MAP.put("hk_price_list_dtl.carat_range", "{"
                    + "\"" + JoinAttributeKey.FK_TABLE.toString() + "\" : "
                    + "\"hk_carat_range_mst\", "
                    + "\"" + JoinAttributeKey.FK_COLUMN.toString() + "\" : "
                    + "\"id\", "
                    + "\"" + JoinAttributeKey.SHOW_COLUMN.toString() + "\" : "
                    + "\"min_value:max_value\", " + "\""
                    + JoinAttributeKey.DELIMETER.toString() + "\" : "
                    + "\"-\"}");
            /**
             * ********************* hk_notification_info table
             * *********************
             */
            FK_CONVERSION_MAP.put("hk_notification_info.id", "{"
                    + "\"" + JoinAttributeKey.FK_TABLE.toString() + "\" : "
                    + "\"hk_notification_info\", "
                    + "\"" + JoinAttributeKey.FK_COLUMN.toString() + "\" : "
                    + "\"id\", "
                    + "\"" + JoinAttributeKey.SHOW_COLUMN.toString() + "\" : "
                    + "\"instance_type:description\", " + "\""
                    + JoinAttributeKey.DELIMETER.toString() + "\" : "
                    + "\"@@\"}");

            /**
             * ********************* hk_notification_user_rel_info table
             * *********************
             */
            FK_CONVERSION_MAP.put("hk_notification_user_rel_info.for_user", "{"
                    + "\"" + JoinAttributeKey.FK_TABLE.toString() + "\" : "
                    + "\"um_system_user\", "
                    + "\"" + JoinAttributeKey.FK_COLUMN.toString() + "\" : "
                    + "\"id\", "
                    + "\"" + JoinAttributeKey.SHOW_COLUMN.toString() + "\" : "
                    + "\"first_name:middle_name:last_name\", " + "\""
                    + JoinAttributeKey.DELIMETER.toString() + "\" : "
                    + "\" \"}");

            /**
             * ********************* act_activity_flow_node_info table
             * *********************
             */
            FK_CONVERSION_MAP.put("act_activity_flow_node_info.associated_service", "{"
                    + "\"" + JoinAttributeKey.FK_TABLE.toString() + "\" : "
                    + "\"act_service_info\", "
                    + "\"" + JoinAttributeKey.FK_COLUMN.toString() + "\" : "
                    + "\"id\", "
                    + "\"" + JoinAttributeKey.SHOW_COLUMN.toString() + "\" : "
                    + "\"service_name\"}");

            /**
             * ********************* act_activity_flow_node_route_info table
             * *********************
             */
            FK_CONVERSION_MAP.put("act_activity_flow_node_route_info.current_node", "{"
                    + "\"" + JoinAttributeKey.FK_TABLE.toString() + "\" : "
                    + "\"act_activity_flow_node_info\", "
                    + "\"" + JoinAttributeKey.FK_COLUMN.toString() + "\" : "
                    + "\"id\", "
                    + "\"" + JoinAttributeKey.SHOW_COLUMN.toString() + "\" : "
                    + "\"associated_service\", "
                    + "\"" + JoinAttributeKey.IS_ANOTHER_CONVERSION.toString() + "\" : "
                    + "true}");

            FK_CONVERSION_MAP.put("act_activity_flow_node_route_info.next_node", "{"
                    + "\"" + JoinAttributeKey.FK_TABLE.toString() + "\" : "
                    + "\"act_activity_flow_node_info\", "
                    + "\"" + JoinAttributeKey.FK_COLUMN.toString() + "\" : "
                    + "\"id\", "
                    + "\"" + JoinAttributeKey.SHOW_COLUMN.toString() + "\" : "
                    + "\"associated_service\", "
                    + "\"" + JoinAttributeKey.IS_ANOTHER_CONVERSION.toString() + "\" : "
                    + "true}");

            /**
             * ********************* act_activity_flow_info table
             * *********************
             */
            FK_CONVERSION_MAP.put("act_activity_flow_info.company", "{"
                    + "\"" + JoinAttributeKey.FK_TABLE.toString() + "\" : "
                    + "\"um_company_info\", "
                    + "\"" + JoinAttributeKey.FK_COLUMN.toString() + "\" : "
                    + "\"id\", "
                    + "\"" + JoinAttributeKey.SHOW_COLUMN.toString() + "\" : "
                    + "\"name\"}");

            /**
             * ********************* act_activity_flow_node_route_info table
             * *********************
             */
            FK_CONVERSION_MAP.put("act_activity_flow_version_info.created_by", "{"
                    + "\"" + JoinAttributeKey.FK_TABLE.toString() + "\" : "
                    + "\"um_system_user\", "
                    + "\"" + JoinAttributeKey.FK_COLUMN.toString() + "\" : "
                    + "\"id\", "
                    + "\"" + JoinAttributeKey.SHOW_COLUMN.toString() + "\" : "
                    + "\"first_name:middle_name:last_name\", " + "\""
                    + JoinAttributeKey.DELIMETER.toString() + "\" : "
                    + "\" \"}");

            FK_CONVERSION_MAP.put("act_activity_flow_version_info.last_modified_by", "{"
                    + "\"" + JoinAttributeKey.FK_TABLE.toString() + "\" : "
                    + "\"um_system_user\", "
                    + "\"" + JoinAttributeKey.FK_COLUMN.toString() + "\" : "
                    + "\"id\", "
                    + "\"" + JoinAttributeKey.SHOW_COLUMN.toString() + "\" : "
                    + "\"first_name:middle_name:last_name\", " + "\""
                    + JoinAttributeKey.DELIMETER.toString() + "\" : "
                    + "\" \"}");
            /**
             * ********************* usergoalstatus table *********************
             */
            FK_CONVERSION_MAP.put("usergoalstatus.createdBy", "{"
                    + "\"" + JoinAttributeKey.FK_TABLE.toString() + "\" : "
                    + "\"um_system_user\", "
                    + "\"" + JoinAttributeKey.FK_COLUMN.toString() + "\" : "
                    + "\"id\", "
                    + "\"" + JoinAttributeKey.SHOW_COLUMN.toString() + "\" : "
                    + "\"first_name:middle_name:last_name\", " + "\""
                    + JoinAttributeKey.DELIMETER.toString() + "\" : "
                    + "\" \"}");
            FK_CONVERSION_MAP.put("usergoalstatus.lastModifiedBy", "{"
                    + "\"" + JoinAttributeKey.FK_TABLE.toString() + "\" : "
                    + "\"um_system_user\", "
                    + "\"" + JoinAttributeKey.FK_COLUMN.toString() + "\" : "
                    + "\"id\", "
                    + "\"" + JoinAttributeKey.SHOW_COLUMN.toString() + "\" : "
                    + "\"first_name:middle_name:last_name\", " + "\""
                    + JoinAttributeKey.DELIMETER.toString() + "\" : "
                    + "\" \"}");
            FK_CONVERSION_MAP.put("usergoalstatus.forUser", "{"
                    + "\"" + JoinAttributeKey.FK_TABLE.toString() + "\" : "
                    + "\"um_system_user\", "
                    + "\"" + JoinAttributeKey.FK_COLUMN.toString() + "\" : "
                    + "\"id\", "
                    + "\"" + JoinAttributeKey.SHOW_COLUMN.toString() + "\" : "
                    + "\"first_name:middle_name:last_name\", " + "\""
                    + JoinAttributeKey.DELIMETER.toString() + "\" : "
                    + "\" \"}");
            FK_CONVERSION_MAP.put("usergoalstatus.activityGroup", "{"
                    + "\"" + JoinAttributeKey.FK_TABLE.toString() + "\" : "
                    + "\"act_activity_flow_group_info\", "
                    + "\"" + JoinAttributeKey.FK_COLUMN.toString() + "\" : "
                    + "\"id\", "
                    + "\"" + JoinAttributeKey.SHOW_COLUMN.toString() + "\" : "
                    + "\"group_name\"}");
            FK_CONVERSION_MAP.put("usergoalstatus.activityNode", "{"
                    + "\"" + JoinAttributeKey.FK_TABLE.toString() + "\" : "
                    + "\"act_activity_flow_node_info\", "
                    + "\"" + JoinAttributeKey.FK_COLUMN.toString() + "\" : "
                    + "\"id\", "
                    + "\"" + JoinAttributeKey.SHOW_COLUMN.toString() + "\" : "
                    + "\"associated_service\", "
                    + "\"" + JoinAttributeKey.IS_ANOTHER_CONVERSION.toString() + "\" : "
                    + "true}");
            FK_CONVERSION_MAP.put("usergoalstatus.goalTemplate", "{"
                    + "\"" + JoinAttributeKey.FK_TABLE.toString() + "\" : "
                    + "\"hk_system_goal_template_info\", "
                    + "\"" + JoinAttributeKey.FK_COLUMN.toString() + "\" : "
                    + "\"id\", "
                    + "\"" + JoinAttributeKey.SHOW_COLUMN.toString() + "\" : "
                    + "\"template_name\"}");

            /**
             * ********************* workallotment table *********************
             */
            FK_CONVERSION_MAP.put("workallotment.createdBy", "{"
                    + "\"" + JoinAttributeKey.FK_TABLE.toString() + "\" : "
                    + "\"um_system_user\", "
                    + "\"" + JoinAttributeKey.FK_COLUMN.toString() + "\" : "
                    + "\"id\", "
                    + "\"" + JoinAttributeKey.SHOW_COLUMN.toString() + "\" : "
                    + "\"first_name:middle_name:last_name\", " + "\""
                    + JoinAttributeKey.DELIMETER.toString() + "\" : "
                    + "\" \"}");
            FK_CONVERSION_MAP.put("workallotment.lastModifiedBy", "{"
                    + "\"" + JoinAttributeKey.FK_TABLE.toString() + "\" : "
                    + "\"um_system_user\", "
                    + "\"" + JoinAttributeKey.FK_COLUMN.toString() + "\" : "
                    + "\"id\", "
                    + "\"" + JoinAttributeKey.SHOW_COLUMN.toString() + "\" : "
                    + "\"first_name:middle_name:last_name\", " + "\""
                    + JoinAttributeKey.DELIMETER.toString() + "\" : "
                    + "\" \"}");

            FK_CONVERSION_MAP.put("workallotment.forUser", "{"
                    + "\"" + JoinAttributeKey.FK_TABLE.toString() + "\" : "
                    + "\"um_system_user\", "
                    + "\"" + JoinAttributeKey.FK_COLUMN.toString() + "\" : "
                    + "\"id\", "
                    + "\"" + JoinAttributeKey.SHOW_COLUMN.toString() + "\" : "
                    + "\"first_name:middle_name:last_name\", " + "\""
                    + JoinAttributeKey.DELIMETER.toString() + "\" : "
                    + "\" \"}");

            FK_CONVERSION_MAP.put("workallotment.forDesignation", "{"
                    + "\"" + JoinAttributeKey.FK_TABLE.toString() + "\" : "
                    + "\"um_system_role\", "
                    + "\"" + JoinAttributeKey.FK_COLUMN.toString() + "\" : "
                    + "\"id\", "
                    + "\"" + JoinAttributeKey.SHOW_COLUMN.toString() + "\" : "
                    + "\"name\"}");

            FK_CONVERSION_MAP.put("workallotment.currentActivityFlowNode", "{"
                    + "\"" + JoinAttributeKey.FK_TABLE.toString() + "\" : "
                    + "\"act_activity_flow_node_info\", "
                    + "\"" + JoinAttributeKey.FK_COLUMN.toString() + "\" : "
                    + "\"id\", "
                    + "\"" + JoinAttributeKey.SHOW_COLUMN.toString() + "\" : "
                    + "\"associated_service\", "
                    + "\"" + JoinAttributeKey.IS_ANOTHER_CONVERSION.toString() + "\" : "
                    + "true}");

            FK_CONVERSION_MAP.put("workallotment.previousActivityFlowNode", "{"
                    + "\"" + JoinAttributeKey.FK_TABLE.toString() + "\" : "
                    + "\"act_activity_flow_node_info\", "
                    + "\"" + JoinAttributeKey.FK_COLUMN.toString() + "\" : "
                    + "\"id\", "
                    + "\"" + JoinAttributeKey.SHOW_COLUMN.toString() + "\" : "
                    + "\"associated_service\", "
                    + "\"" + JoinAttributeKey.IS_ANOTHER_CONVERSION.toString() + "\" : "
                    + "true}");

            FK_CONVERSION_MAP.put("workallotment.currentActivityFlowGroup", "{"
                    + "\"" + JoinAttributeKey.FK_TABLE.toString() + "\" : "
                    + "\"act_activity_flow_group_info\", "
                    + "\"" + JoinAttributeKey.FK_COLUMN.toString() + "\" : "
                    + "\"id\", "
                    + "\"" + JoinAttributeKey.SHOW_COLUMN.toString() + "\" : "
                    + "\"group_name\"}");

            FK_CONVERSION_MAP.put("workallotment.previousActivityFlowGroup", "{"
                    + "\"" + JoinAttributeKey.FK_TABLE.toString() + "\" : "
                    + "\"act_activity_flow_group_info\", "
                    + "\"" + JoinAttributeKey.FK_COLUMN.toString() + "\" : "
                    + "\"id\", "
                    + "\"" + JoinAttributeKey.SHOW_COLUMN.toString() + "\" : "
                    + "\"group_name\"}");

            FK_CONVERSION_MAP.put("workallotment.activityVersion", "{"
                    + "\"" + JoinAttributeKey.FK_TABLE.toString() + "\" : "
                    + "\"act_activity_flow_version_info\", "
                    + "\"" + JoinAttributeKey.FK_COLUMN.toString() + "\" : "
                    + "\"id\", "
                    + "\"" + JoinAttributeKey.SHOW_COLUMN.toString() + "\" : "
                    + "\"version_code\"}");

            FK_CONVERSION_MAP.put("workallotment._id", "{"
                    + "\"" + JoinAttributeKey.FK_TABLE.toString() + "\" : "
                    + "\"workallotment\", "
                    + "\"" + JoinAttributeKey.FK_COLUMN.toString() + "\" : "
                    + "\"_id\", "
                    + "\"" + JoinAttributeKey.SHOW_COLUMN.toString() + "\" : "
                    + "\"" + "unitType:unitInstance\", " + "\""
                    + JoinAttributeKey.DELIMETER.toString() + "\" : "
                    + "\"_type_@@_\"}");

            /**
             * ********************* workallotmentaggregation table
             * *********************
             */
            FK_CONVERSION_MAP.put("workallotmentaggregation.activityFlowNode", "{"
                    + "\"" + JoinAttributeKey.FK_TABLE.toString() + "\" : "
                    + "\"act_activity_flow_node_info\", "
                    + "\"" + JoinAttributeKey.FK_COLUMN.toString() + "\" : "
                    + "\"id\", "
                    + "\"" + JoinAttributeKey.SHOW_COLUMN.toString() + "\" : "
                    + "\"associated_service\", "
                    + "\"" + JoinAttributeKey.IS_ANOTHER_CONVERSION.toString() + "\" : "
                    + "true}");

            FK_CONVERSION_MAP.put("workallotmentaggregation.activityFlowGroup", "{"
                    + "\"" + JoinAttributeKey.FK_TABLE.toString() + "\" : "
                    + "\"act_activity_flow_group_info\", "
                    + "\"" + JoinAttributeKey.FK_COLUMN.toString() + "\" : "
                    + "\"id\", "
                    + "\"" + JoinAttributeKey.SHOW_COLUMN.toString() + "\" : "
                    + "\"group_name\"}");

            FK_CONVERSION_MAP.put("workallotmentaggregation.activityVersion", "{"
                    + "\"" + JoinAttributeKey.FK_TABLE.toString() + "\" : "
                    + "\"act_activity_flow_version_info\", "
                    + "\"" + JoinAttributeKey.FK_COLUMN.toString() + "\" : "
                    + "\"id\", "
                    + "\"" + JoinAttributeKey.SHOW_COLUMN.toString() + "\" : "
                    + "\"version_code\"}");

            FK_CONVERSION_MAP.put("workallotmentaggregation._id", "{"
                    + "\"" + JoinAttributeKey.FK_TABLE.toString() + "\" : "
                    + "\"workallotmentaggregation\", "
                    + "\"" + JoinAttributeKey.FK_COLUMN.toString() + "\" : "
                    + "\"_id\", "
                    + "\"" + JoinAttributeKey.SHOW_COLUMN.toString() + "\" : "
                    + "\"" + "unitType:unitInstance\", " + "\""
                    + JoinAttributeKey.DELIMETER.toString() + "\" : "
                    + "\"_type_@@_\"}");
            /**
             * ********************* issue table *********************
             */

            FK_CONVERSION_MAP.put("issue.invoice", "{"
                    + "\"" + JoinAttributeKey.FK_TABLE.toString() + "\" : "
                    + "\"invoice\", "
                    + "\"" + JoinAttributeKey.FK_COLUMN.toString() + "\" : "
                    + "\"_id\", "
                    + "\"" + JoinAttributeKey.SHOW_COLUMN.toString() + "\" : "
                    + "\"\\\"fieldValue\\\" ->> '" + HkSystemConstantUtil.InvoiceStaticFields.INVOICE_ID + "'\"}");

            FK_CONVERSION_MAP.put("issue.parcel", "{"
                    + "\"" + JoinAttributeKey.FK_TABLE.toString() + "\" : "
                    + "\"parcel\", "
                    + "\"" + JoinAttributeKey.FK_COLUMN.toString() + "\" : "
                    + "\"_id\", "
                    + "\"" + JoinAttributeKey.SHOW_COLUMN.toString() + "\" : "
                    + "\"\\\"fieldValue\\\" ->> '" + HkSystemConstantUtil.ParcelStaticFields.PARCEL_ID + "'\"}");

            FK_CONVERSION_MAP.put("issue.lot", "{"
                    + "\"" + JoinAttributeKey.FK_TABLE.toString() + "\" : "
                    + "\"lot\", "
                    + "\"" + JoinAttributeKey.FK_COLUMN.toString() + "\" : "
                    + "\"_id\", "
                    + "\"" + JoinAttributeKey.SHOW_COLUMN.toString() + "\" : "
                    + "\"\\\"fieldValue\\\" ->> '" + HkSystemConstantUtil.AutoNumber.LOT_ID + "'\"}");

            FK_CONVERSION_MAP.put("issue.packet", "{"
                    + "\"" + JoinAttributeKey.FK_TABLE.toString() + "\" : "
                    + "\"lot\", "
                    + "\"" + JoinAttributeKey.FK_COLUMN.toString() + "\" : "
                    + "\"_id\", "
                    + "\"" + JoinAttributeKey.SHOW_COLUMN.toString() + "\" : "
                    + "\"\\\"fieldValue\\\" ->> '" + HkSystemConstantUtil.AutoNumber.PACKET_ID + "'\"}");

            FK_CONVERSION_MAP.put("issue.createdBy", "{"
                    + "\"" + JoinAttributeKey.FK_TABLE.toString() + "\" : "
                    + "\"um_system_user\", "
                    + "\"" + JoinAttributeKey.FK_COLUMN.toString() + "\" : "
                    + "\"id\", "
                    + "\"" + JoinAttributeKey.SHOW_COLUMN.toString() + "\" : "
                    + "\"first_name:middle_name:last_name\", " + "\""
                    + JoinAttributeKey.DELIMETER.toString() + "\" : "
                    + "\" \"}");
            FK_CONVERSION_MAP.put("issue.lastModifiedBy", "{"
                    + "\"" + JoinAttributeKey.FK_TABLE.toString() + "\" : "
                    + "\"um_system_user\", "
                    + "\"" + JoinAttributeKey.FK_COLUMN.toString() + "\" : "
                    + "\"id\", "
                    + "\"" + JoinAttributeKey.SHOW_COLUMN.toString() + "\" : "
                    + "\"first_name:middle_name:last_name\", " + "\""
                    + JoinAttributeKey.DELIMETER.toString() + "\" : "
                    + "\" \"}");
            /**
             * ********************* personcapability table
             * *********************
             */
            FK_CONVERSION_MAP.put("personcapability.forPerson", "{"
                    + "\"" + JoinAttributeKey.FK_TABLE.toString() + "\" : "
                    + "\"um_system_user\", "
                    + "\"" + JoinAttributeKey.FK_COLUMN.toString() + "\" : "
                    + "\"id\", "
                    + "\"" + JoinAttributeKey.SHOW_COLUMN.toString() + "\" : "
                    + "\"first_name:middle_name:last_name\", " + "\""
                    + JoinAttributeKey.DELIMETER.toString() + "\" : "
                    + "\" \"}");

            /**
             * ********************* fieldsequence table *********************
             */
            FK_CONVERSION_MAP.put("fieldsequence.forEntity", "{"
                    + "\"" + JoinAttributeKey.FK_TABLE.toString() + "\" : "
                    + "\"um_system_feature\", "
                    + "\"" + JoinAttributeKey.FK_COLUMN.toString() + "\" : "
                    + "\"id\", "
                    + "\"" + JoinAttributeKey.SHOW_COLUMN.toString() + "\" : "
                    + "\"menu_label\", " + "\""
                    + JoinAttributeKey.DELIMETER.toString() + "\" : "
                    + "\" \"}");

            /**
             * ********************* transfer table *********************
             */
            FK_CONVERSION_MAP.put("transfer.invoice", "{"
                    + "\"" + JoinAttributeKey.FK_TABLE.toString() + "\" : "
                    + "\"invoice\", "
                    + "\"" + JoinAttributeKey.FK_COLUMN.toString() + "\" : "
                    + "\"_id\", "
                    + "\"" + JoinAttributeKey.SHOW_COLUMN.toString() + "\" : "
                    + "\"\\\"fieldValue\\\" ->> '" + HkSystemConstantUtil.InvoiceStaticFields.INVOICE_ID + "'\"}");

            FK_CONVERSION_MAP.put("transfer.parcel", "{"
                    + "\"" + JoinAttributeKey.FK_TABLE.toString() + "\" : "
                    + "\"parcel\", "
                    + "\"" + JoinAttributeKey.FK_COLUMN.toString() + "\" : "
                    + "\"_id\", "
                    + "\"" + JoinAttributeKey.SHOW_COLUMN.toString() + "\" : "
                    + "\"\\\"fieldValue\\\" ->> '" + HkSystemConstantUtil.ParcelStaticFields.PARCEL_ID + "'\"}");

            FK_CONVERSION_MAP.put("transfer.lot", "{"
                    + "\"" + JoinAttributeKey.FK_TABLE.toString() + "\" : "
                    + "\"lot\", "
                    + "\"" + JoinAttributeKey.FK_COLUMN.toString() + "\" : "
                    + "\"_id\", "
                    + "\"" + JoinAttributeKey.SHOW_COLUMN.toString() + "\" : "
                    + "\"\\\"fieldValue\\\" ->> '" + HkSystemConstantUtil.AutoNumber.LOT_ID + "'\"}");

            FK_CONVERSION_MAP.put("transfer.createdBy", "{"
                    + "\"" + JoinAttributeKey.FK_TABLE.toString() + "\" : "
                    + "\"um_system_user\", "
                    + "\"" + JoinAttributeKey.FK_COLUMN.toString() + "\" : "
                    + "\"id\", "
                    + "\"" + JoinAttributeKey.SHOW_COLUMN.toString() + "\" : "
                    + "\"first_name:middle_name:last_name\", " + "\""
                    + JoinAttributeKey.DELIMETER.toString() + "\" : "
                    + "\" \"}");
            FK_CONVERSION_MAP.put("transfer.lastModifiedBy", "{"
                    + "\"" + JoinAttributeKey.FK_TABLE.toString() + "\" : "
                    + "\"um_system_user\", "
                    + "\"" + JoinAttributeKey.FK_COLUMN.toString() + "\" : "
                    + "\"id\", "
                    + "\"" + JoinAttributeKey.SHOW_COLUMN.toString() + "\" : "
                    + "\"first_name:middle_name:last_name\", " + "\""
                    + JoinAttributeKey.DELIMETER.toString() + "\" : "
                    + "\" \"}");
            /**
             * ********************* sell table *********************
             */

            FK_CONVERSION_MAP.put("sell.invoice", "{"
                    + "\"" + JoinAttributeKey.FK_TABLE.toString() + "\" : "
                    + "\"invoice\", "
                    + "\"" + JoinAttributeKey.FK_COLUMN.toString() + "\" : "
                    + "\"_id\", "
                    + "\"" + JoinAttributeKey.SHOW_COLUMN.toString() + "\" : "
                    + "\"\\\"fieldValue\\\" ->> '" + HkSystemConstantUtil.InvoiceStaticFields.INVOICE_ID + "'\"}");

            FK_CONVERSION_MAP.put("sell.parcel", "{"
                    + "\"" + JoinAttributeKey.FK_TABLE.toString() + "\" : "
                    + "\"parcel\", "
                    + "\"" + JoinAttributeKey.FK_COLUMN.toString() + "\" : "
                    + "\"_id\", "
                    + "\"" + JoinAttributeKey.SHOW_COLUMN.toString() + "\" : "
                    + "\"\\\"fieldValue\\\" ->> '" + HkSystemConstantUtil.ParcelStaticFields.PARCEL_ID + "'\"}");

            FK_CONVERSION_MAP.put("sell.lot", "{"
                    + "\"" + JoinAttributeKey.FK_TABLE.toString() + "\" : "
                    + "\"lot\", "
                    + "\"" + JoinAttributeKey.FK_COLUMN.toString() + "\" : "
                    + "\"_id\", "
                    + "\"" + JoinAttributeKey.SHOW_COLUMN.toString() + "\" : "
                    + "\"\\\"fieldValue\\\" ->> '" + HkSystemConstantUtil.AutoNumber.LOT_ID + "'\"}");

            FK_CONVERSION_MAP.put("sell.createdBy", "{"
                    + "\"" + JoinAttributeKey.FK_TABLE.toString() + "\" : "
                    + "\"um_system_user\", "
                    + "\"" + JoinAttributeKey.FK_COLUMN.toString() + "\" : "
                    + "\"id\", "
                    + "\"" + JoinAttributeKey.SHOW_COLUMN.toString() + "\" : "
                    + "\"first_name:middle_name:last_name\", " + "\""
                    + JoinAttributeKey.DELIMETER.toString() + "\" : "
                    + "\" \"}");
            FK_CONVERSION_MAP.put("sell.lastModifiedBy", "{"
                    + "\"" + JoinAttributeKey.FK_TABLE.toString() + "\" : "
                    + "\"um_system_user\", "
                    + "\"" + JoinAttributeKey.FK_COLUMN.toString() + "\" : "
                    + "\"id\", "
                    + "\"" + JoinAttributeKey.SHOW_COLUMN.toString() + "\" : "
                    + "\"first_name:middle_name:last_name\", " + "\""
                    + JoinAttributeKey.DELIMETER.toString() + "\" : "
                    + "\" \"}");

            /**
             * ********************* plan table *********************
             */
            FK_CONVERSION_MAP.put("plan.invoice", "{"
                    + "\"" + JoinAttributeKey.FK_TABLE.toString() + "\" : "
                    + "\"invoice\", "
                    + "\"" + JoinAttributeKey.FK_COLUMN.toString() + "\" : "
                    + "\"_id\", "
                    + "\"" + JoinAttributeKey.SHOW_COLUMN.toString() + "\" : "
                    + "\"\\\"fieldValue\\\" ->> '" + HkSystemConstantUtil.InvoiceStaticFields.INVOICE_ID + "'\"}");

            FK_CONVERSION_MAP.put("plan.parcel", "{"
                    + "\"" + JoinAttributeKey.FK_TABLE.toString() + "\" : "
                    + "\"parcel\", "
                    + "\"" + JoinAttributeKey.FK_COLUMN.toString() + "\" : "
                    + "\"_id\", "
                    + "\"" + JoinAttributeKey.SHOW_COLUMN.toString() + "\" : "
                    + "\"\\\"fieldValue\\\" ->> '" + HkSystemConstantUtil.ParcelStaticFields.PARCEL_ID + "'\"}");

            FK_CONVERSION_MAP.put("plan.lot", "{"
                    + "\"" + JoinAttributeKey.FK_TABLE.toString() + "\" : "
                    + "\"lot\", "
                    + "\"" + JoinAttributeKey.FK_COLUMN.toString() + "\" : "
                    + "\"_id\", "
                    + "\"" + JoinAttributeKey.SHOW_COLUMN.toString() + "\" : "
                    + "\"\\\"fieldValue\\\" ->> '" + HkSystemConstantUtil.AutoNumber.LOT_ID + "'\"}");

            FK_CONVERSION_MAP.put("plan.packet", "{"
                    + "\"" + JoinAttributeKey.FK_TABLE.toString() + "\" : "
                    + "\"lot\", "
                    + "\"" + JoinAttributeKey.FK_COLUMN.toString() + "\" : "
                    + "\"_id\", "
                    + "\"" + JoinAttributeKey.SHOW_COLUMN.toString() + "\" : "
                    + "\"\\\"fieldValue\\\" ->> '" + HkSystemConstantUtil.AutoNumber.PACKET_ID + "'\"}");

            FK_CONVERSION_MAP.put("plan.createdBy", "{"
                    + "\"" + JoinAttributeKey.FK_TABLE.toString() + "\" : "
                    + "\"um_system_user\", "
                    + "\"" + JoinAttributeKey.FK_COLUMN.toString() + "\" : "
                    + "\"id\", "
                    + "\"" + JoinAttributeKey.SHOW_COLUMN.toString() + "\" : "
                    + "\"first_name:middle_name:last_name\", " + "\""
                    + JoinAttributeKey.DELIMETER.toString() + "\" : "
                    + "\" \"}");
            FK_CONVERSION_MAP.put("plan.lastModifiedBy", "{"
                    + "\"" + JoinAttributeKey.FK_TABLE.toString() + "\" : "
                    + "\"um_system_user\", "
                    + "\"" + JoinAttributeKey.FK_COLUMN.toString() + "\" : "
                    + "\"id\", "
                    + "\"" + JoinAttributeKey.SHOW_COLUMN.toString() + "\" : "
                    + "\"first_name:middle_name:last_name\", " + "\""
                    + JoinAttributeKey.DELIMETER.toString() + "\" : "
                    + "\" \"}");

            FK_CONVERSION_MAP.put("plan.machine_to_process_in", "{"
                    + "\"" + JoinAttributeKey.FK_TABLE.toString() + "\" : "
                    + "\"hk_system_asset_info\", "
                    + "\"" + JoinAttributeKey.FK_COLUMN.toString() + "\" : "
                    + "\"id\", "
                    + "\"" + JoinAttributeKey.SHOW_COLUMN.toString() + "\" : "
                    + "\"asset_name\", " + "\""
                    + JoinAttributeKey.DELIMETER.toString() + "\" : "
                    + "\" \"}");
            
            FK_CONVERSION_MAP.put("(plan.\"fieldValue\"->>'"+HkSystemConstantUtil.PlanStaticFieldName.CARATE_RANGE+"')::numeric", "{"
                    + "\"" + JoinAttributeKey.FK_TABLE.toString() + "\" : "
                    + "\"hk_carat_range_mst\", "
                    + "\"" + JoinAttributeKey.FK_COLUMN.toString() + "\" : "
                    + "\"id\", "
                    + "\"" + JoinAttributeKey.SHOW_COLUMN.toString() + "\" : "
                    + "\"min_value:max_value\", " + "\""
                    + JoinAttributeKey.DELIMETER.toString() + "\" : "
                    + "\"-\"}");
            
        }

        static {
            /**
             * hk_location_mst.location_type
             */
            STATIC_CONVERSION_MAP.put("hk_location_mst.location_type", "{"
                    + "\"" + "C" + "\" : "
                    + "\"Country\", "
                    + "\"" + "S" + "\" : "
                    + "\"State\", "
                    + "\"" + "D" + "\" : "
                    + "\"District\", "
                    + "\"" + "T" + "\" : "
                    + "\"City\"}");

            /**
             * hk_system_task_info.status
             */
            STATIC_CONVERSION_MAP.put("hk_system_task_info.status", "{"
                    + "\"" + "D" + "\" : "
                    + "\"Due\", "
                    + "\"" + "A" + "\" : "
                    + "\"Cancelled\", "
                    + "\"" + "C" + "\" : "
                    + "\"Completed\", "
                    + "\"" + "C_AR" + "\" : "
                    + "\"Completed Archived\", "
                    + "\"" + "A_AR" + "\" : "
                    + "\"Cancelled Archived\"}");
            /**
             * hk_system_task_info.repeatative_mode
             */
            STATIC_CONVERSION_MAP.put("hk_system_task_info.repeatative_mode", "{"
                    + "\"" + "D" + "\" : "
                    + "\"Daily\", "
                    + "\"" + "W" + "\" : "
                    + "\"Weekly\", "
                    + "\"" + "M" + "\" : "
                    + "\"Monthly\"}");

            /**
             * hk_shift_dtl.slot_type
             */
            STATIC_CONVERSION_MAP.put("hk_shift_dtl.slot_type", "{"
                    + "\"" + "M" + "\" : "
                    + "\"Main Time\", "
                    + "\"" + "B" + "\" : "
                    + "\"Break Time\"}");

            /**
             * hk_leave_info.status
             */
            STATIC_CONVERSION_MAP.put("hk_leave_info.status", "{"
                    + "\"" + "APRVD" + "\" : "
                    + "\"Approved\", "
                    + "\"" + "DSAPRVD" + "\" : "
                    + "\"Disapproved\", "
                    + "\"" + "CNCLD" + "\" : "
                    + "\"Canceled\", "
                    + "\"" + "PNDG" + "\" : "
                    + "\"Pending\", "
                    + "\"" + "CMMNTD" + "\" : "
                    + "\"Commented\", "
                    + "\"" + "APRV_CNCL" + "\" : "
                    + "\"Approved Cancelled\"}");

            /**
             * hk_system_event_info.status
             */
            STATIC_CONVERSION_MAP.put("hk_system_event_info.status", "{"
                    + "\"" + "CRTD" + "\" : "
                    + "\"Created\", "
                    + "\"" + "UPCMNG" + "\" : "
                    + "\"Upcoming\", "
                    + "\"" + "CNCLD" + "\" : "
                    + "\"Cancelled\", "
                    + "\"" + "CMPLTD" + "\" : "
                    + "\"Completed\", "
                    + "\"" + "ONGNG" + "\" : "
                    + "\"Ongoing\", "
                    + "\"" + "CMP_ARCV" + "\" : "
                    + "\"Completed Archived\"}");

            /**
             * hk_system_asset_info.asset_type
             */
            STATIC_CONVERSION_MAP.put("hk_system_asset_info.asset_type", "{"
                    + "\"" + "M" + "\" : "
                    + "\"Managed\", "
                    + "\"" + "R" + "\" : "
                    + "\"Non-Managed\"}");

            /**
             * hk_system_asset_info.status
             */
            STATIC_CONVERSION_MAP.put("hk_system_asset_info.status", "{"
                    + "\"" + "A" + "\" : "
                    + "\"Available\", "
                    + "\"" + "D" + "\" : "
                    + "\"Archived\", "
                    + "\"" + "R" + "\" : "
                    + "\"InRepair\", "
                    + "\"" + "I" + "\" : "
                    + "\"Issued\"}");

            /**
             * hk_system_message_info.status
             */
            STATIC_CONVERSION_MAP.put("hk_system_message_info.status", "{"
                    + "\"" + "P" + "\" : "
                    + "\"Pending\", "
                    + "\"" + "C" + "\" : "
                    + "\"Closed\", "
                    + "\"" + "A" + "\" : "
                    + "\"Active\", "
                    + "\"" + "I" + "\" : "
                    + "\"InActive\", "
                    + "\"" + "D" + "\" : "
                    + "\"Archived\", "
                    + "\"" + "R" + "\" : "
                    + "\"Replied\"}");

            /**
             * hk_system_task_info.end_repeat_mode
             */
            STATIC_CONVERSION_MAP.put("hk_system_task_info.end_repeat_mode", "{"
                    + "\"" + "OD" + "\" : "
                    + "\"On Date\", "
                    + "\"" + "AD" + "\" : "
                    + "\"After X-Days\", "
                    + "\"" + "AR" + "\" : "
                    + "\"After X-repetition\"}");

            /**
             * hk_system_event_info.registration_type
             */
            STATIC_CONVERSION_MAP.put("hk_system_event_info.registration_type", "{"
                    + "\"" + "OFLN" + "\" : "
                    + "\"Offline\", "
                    + "\"" + "ONLN" + "\" : "
                    + "\"Online\"} ");

            /**
             * hk_user_event_registration_info.status
             */
            STATIC_CONVERSION_MAP.put("hk_user_event_registration_info.status", "{"
                    + "\"" + "AT" + "\" : "
                    + "\"Attending\", "
                    + "\"" + "NAT" + "\" : "
                    + "\"Not Attending\"} ");
            /**
             * hk_notification_info.notification_type
             */
            STATIC_CONVERSION_MAP.put("hk_notification_info.notification_type", "{"
                    + "\"" + "HLDY" + "\" : "
                    + "\"HOLIDAY\", "
                    + "\"" + "EVNT" + "\" : "
                    + "\"EVENT\", "
                    + "\"" + "WRKFLW" + "\" : "
                    + "\"WORKFLOW\", "
                    + "\"" + "TSK" + "\" : "
                    + "\"TASK\", "
                    + "\"" + "EMPLY" + "\" : "
                    + "\"EMPLOYEE\", "
                    + "\"" + "AST" + "\" : "
                    + "\"ASSET\", "
                    + "\"" + "MSTR" + "\" : "
                    + "\"MASTER\", "
                    + "\"" + "ROLE" + "\" : "
                    + "\"DESIGNATION\", "
                    + "\"" + "SHFT" + "\" : "
                    + "\"SHIFT\", "
                    + "\"" + "LOCALE" + "\" : "
                    + "\"LOCALE\", "
                    + "\"" + "NTFCTN" + "\" : "
                    + "\"NOTIFICATION\", "
                    + "\"" + "LEAVE" + "\" : "
                    + "\"LEAVE\"}");
            /**
             * hk_system_goal_template_info.status
             */
            STATIC_CONVERSION_MAP.put("hk_system_goal_template_info.status", "{"
                    + "\"" + "A" + "\" : "
                    + "\"Active\", "
                    + "\"" + "P" + "\" : "
                    + "\"Pending\", "
                    + "\"" + "D" + "\" : "
                    + "\"Discarded\"}");
            /**
             * hk_system_goal_template_info.template_type
             */
            STATIC_CONVERSION_MAP.put("hk_system_goal_template_info.template_type", "{"
                    + "\"" + "B" + "\" : "
                    + "\"Boolean\", "
                    + "\"" + "N" + "\" : "
                    + "\"Numeric\"}");
            /**
             * usergoalstatus.goalType
             */
            STATIC_CONVERSION_MAP.put("usergoalstatus.goalType", "{"
                    + "\"" + "B" + "\" : "
                    + "\"Boolean\", "
                    + "\"" + "N" + "\" : "
                    + "\"Numeric\"}");
            /**
             * act_activity_flow_info.status
             */
            STATIC_CONVERSION_MAP.put("act_activity_flow_info.status", "{"
                    + "\"" + "A" + "\" : "
                    + "\"ACTIVE\", "
                    + "\"" + "D" + "\" : "
                    + "\"ARCHIVED\", "
                    + "\"" + "I" + "\" : "
                    + "\"INACTIVE\"}");
            /**
             * act_activity_flow_version_info.status
             */
            STATIC_CONVERSION_MAP.put("act_activity_flow_version_info.status", "{"
                    + "\"" + "A" + "\" : "
                    + "\"ACTIVE\", "
                    + "\"" + "D" + "\" : "
                    + "\"ARCHIVED\", "
                    + "\"" + "I" + "\" : "
                    + "\"INACTIVE\"}");
            /**
             * hk_price_list_info.status
             */
            STATIC_CONVERSION_MAP.put("hk_price_list_info.status", "{"
                    + "\"" + "A" + "\" : "
                    + "\"Active\", "
                    + "\"" + "D" + "\" : "
                    + "\"Archived\", "
                    + "\"" + "I" + "\" : "
                    + "\"Inactive\"}");

            /**
             * plan.planType
             */
            STATIC_CONVERSION_MAP.put("plan.planType", "{"
                    + "\"" + "PL" + "\" : "
                    + "\"Plan\", "
                    + "\"" + "ES" + "\" : "
                    + "\"Estimation\", "
                    + "\"" + "PA" + "\" : "
                    + "\"Parameter\"}");
        }
    }
    
    
    
    public static String userMultiselectSubQuery = "(select ???._id \"objid\",um_system_user.id \"userid\" from ???\n"
            + "left join (select json_array_elements(json_extract_path(???.\"fieldValue\",'~??~')) \"boom\", _id from ???) a\n"
            + "on a._id = ???._id\n"
            + "left join\n"
            + "\n"
            + "(select a.id \"user\",b.id \"dept\",c.role \"role\" from um_system_user a\n"
            + "left join um_dept_mst b \n"
            + "on a.department = b.id\n"
            + "left join um_user_role c\n"
            + "on c.userobj = a.id) io\n"
            + "\n"
            + "on io.user = split_part(replace(a.\"boom\"::text, '\"', ''), ':', 1)::numeric \n"
            + "and split_part(replace(a.\"boom\"::text, '\"', ''), ':', 2) <> 'D'\n"
            + "and split_part(replace(a.\"boom\"::text, '\"', ''), ':', 2) <> 'R'\n"
            + "\n"
            + "left join\n"
            + "\n"
            + "(select a.id \"user\",b.id \"dept\",c.role \"role\" from um_system_user a\n"
            + "left join um_dept_mst b \n"
            + "on a.department = b.id\n"
            + "left join um_user_role c\n"
            + "on c.userobj = a.id) io1\n"
            + "\n"
            + "on io1.dept = split_part(replace(a.\"boom\"::text, '\"', ''), ':', 1)::numeric \n"
            + "and split_part(replace(a.\"boom\"::text, '\"', ''), ':', 2) <> 'E'\n"
            + "and split_part(replace(a.\"boom\"::text, '\"', ''), ':', 2) <> 'R'\n"
            + "\n"
            + "left join\n"
            + "\n"
            + "(select a.id \"user\",b.id \"dept\",c.role \"role\" from um_system_user a\n"
            + "left join um_dept_mst b \n"
            + "on a.department = b.id\n"
            + "left join um_user_role c\n"
            + "on c.userobj = a.id) io2\n"
            + "\n"
            + "on io2.role = split_part(replace(a.\"boom\"::text, '\"', ''), ':', 1)::numeric \n"
            + "and split_part(replace(a.\"boom\"::text, '\"', ''), ':', 2) <> 'D'\n"
            + "and split_part(replace(a.\"boom\"::text, '\"', ''), ':', 2) <> 'E'\n"
            + "\n"
            + "left join um_system_user\n"
            + "on um_system_user.id = CASE WHEN concat(io.user,io1.user,io2.user)='' THEN null\n"
            + "ELSE concat(io.user,io1.user,io2.user)::bigint\n"
            + "END) ~table~ ";
}
