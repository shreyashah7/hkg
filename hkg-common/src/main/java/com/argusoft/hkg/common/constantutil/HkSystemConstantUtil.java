package com.argusoft.hkg.common.constantutil;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * This class specifies the static final constants to be used in application.
 *
 * @author hshah
 */
public class HkSystemConstantUtil {

    public static final Long ZERO_LEVEL_FRANCHISE = 0l;
    public static final String DIAMOND_PARENT_VIEW_FLAG = "#P#";
    public static final Integer MIN_SEARCH_LENGTH = 3;
    public static final String MAX_MESSAGE_SAVE_LENGTH = "MESSAGING_MAX_MSG_PER_USER";
    public static final String CURRENCY_CODE_CUSTOM = "*CurrencyCode";

    public static final String ACTIVE = "A";
    public static final String INACTIVE = "I";
    public static final String ARCHIVED = "D";
    public static final String ALL_USER_CODE = "0:All";

    public static final String DEFAULT_SECTION = "General";
    public static final long DEFAULT_SECTION_ID = -1L;

    public static String CREATE_OPERATION = "CREATE";
    public static String UPDATE_OPERATION = "UPDATE";
    public static String COMPANY_STATUS_PENDING = "P";
    public static String COMPANY_STATUS_ACTIVE = "A";
    public static String SETUP_PENDING = "Setup_Pending";
    public static final String COUNTRY_NZ = "IN";
    public static final String DESIGNATION_KEY_CODE = "DEG";
    public static final String MACHINE_KEY_CODE = "MCH";
    public static final String RESTRICTED_HOLIDAY = "R";
    public static final String RESTRICTED_HOLIDAY_VALUE = "Restricted Holiday";
    public static final String HOLIDAY_VALUE = "Holiday";
    public static final String HOLIDAY = "H";
    public static final String SUCCESS = "SUCCESS";
    public static final String FAILURE = "FAILURE";
    public static final String RESTRICTED_HOLIDAY_NAME = "Restricted";
    public static final String SELECT = "Select";
    public static final String NONE = "None";

    public static final String CATEGORY_TYPE_ASSET = "A";
    public static final String INVOICE_INPUTSTREAM = "invoiceInputStream";
    public static final String BROCHURE_INPUTSTREAM = "brochureInputStream";
    public static final String FILE_PATH_PREFIX = "/home/";
    public static final String FILE_PATH_SUFFIX = "/Desktop/";
    public static final String CATEGORY_TYPE_EVENT = "E";
    public static final String ORDERASC = "ORDERASC";
    public static final String ORDERDESC = "ORDERDESC";
    public static final String NOT_AVAILABLE = "NA";
    public static final String DIAMOND_NOT_AVAILABLE = "N/A";
    public static final String UM_CONTACT_DOCUMENT_TYPE_EMP_PROFILE_PICTURE = "EMP_PROFILE_PICTURE";
    public static final String UM_CONTACT_DOCUMENT_TYPE_EMP_FAMILY_MEM_PICTURE = "FAMILY_MEM_PICT";
    public static final String UM_CONTACT_DOCUMENT_TYPE_EMP_WORK_EXP_SALARY_SLIP = "EMP_EXP_SALARY_SLIP";
    public static final String CURRENT_ADDRESS_TYPE = "CURRENT";
    public static final String NATIVE_ADDRESS_TYPE = "NATIVE";
    public static final String UPLOAD_FOLDER_NAME = "UploadedFiles";
    public static final Integer MAX_RESULT = 10;
    public static final String INVOICE_FILENAME = "invoiceFileName";
    public static final String BROCHURE_FILENAME = "brochureFileName";
    public static final String BROCHURE_DOWNLOAD_FILENAME = "brochureDownloadFileName";
    public static final String INVOICE_DOWNLOAD_FILENAME = "invoiceDownloadFileName";

    public static final String DEFAULT_VALUE = "defaultValue";
    public static final String SEPARATOR_PI = "|";
    public static final String CUSTOM_FIELD_DATE_FORMAT = "dd/MM/yyyy";
    public static final String CUSTOM_FIELD_DATE_TIME_FORMAT = "dd/MM/yyyy hh:mm a";
    public static final String CUSTOM_FIELD_TIME_FORMAT = "hh:mm a";
    public static final String CUSTOM_FIELD_VERSION = "CUSTOM_FIELD_VERSION";
    public static final String UPLOAD_TARGET_URL = "api/fileUpload/uploadFile";
    public static final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
    public static final String NOTIFICATION_DATE_SEPARATOR = "D" + SEPARATOR_PI;
    public static final String ASSET_CATEGORY_MACHINES = "M";
    public static final String ASSET_CATEGORY_MACHINES_NAME = "Machines";

    public static final String SEPARATOR_FOR_GOAL_TEMPLATE = "~!";
    public static final String SEPERATOR_FOR_CUSTOM_FIELD_LABEL = "$";
    public static final String FLOW_MODE_TYPE_AUTOMATIC = "A";
    public static final String FLOW_MODE_TYPE_MANUAL = "M";

    public static final class GoalTemplateType {

        public static final String BOOLEAN = "B";
        public static final String NUMERIC = "N";
    }

    public static class WebSecurityConstant {

        public static final String INVALID_USERNAME_PASS_MSG = "The Username or Password you entered is incorrect";
        public static final String ACCOUNT_DESABLE_MSG = "Your account is locked by Admin. Contact Admin";
        public static final String ACCESS_DENIED_MSG = "You have no designation to access this feature";
        public static final String UNAUTHORIZED_ACCESS_MSG = "Unauthorized access. Contact Admin";
        public static final String LOGIN_REQUIED_MSG = "You have to login first to access this feature";
    }

    public static final class ROLE {

        public static final String SUPER_ADMIN = "Super Admin";
        public static final String FRANCHISE_ADMIN = "Franchise Admin";
        public static final String HK_ADMIN = "HK Admin";
    }

    public static final class USER {

        public static final String SUPER_ADMIN = "superadmin";
    }

    public static final class MasterType {

        public static final String BUILT_IN = "B";
    }

    public static class DBBaseType {

        public static final String MONGO_DB = "MDB";
        public static final String RELATIONAL_DB = "RDB";
    }

    //Recent datys for task
    public static final Integer RECENT_DAYS = 7;

    public static class SHIFT_TYPE {

        public static String MAIN_TIME = "M";
        public static String BREAK_TIME = "B";
    }

    public static class ShiftRuleType {

        public static final String BEGINS = "B";
        public static final String ENDS = "E";
        public static final String DATE_RANGE = "DR";
    }

    public static class PERMISSION_SET {

        public static String MINIMUM = "Minimum";
        public static String MAXIMUM = "Maximum";
    }

    public static class LOCATION_TYPE {

        public static String COUNTRY = "C";
        public static String STATE = "S";
        public static String DISTRICT = "D";
        public static String CITY = "T";
    }

    public static class LeaveStatus {

        public static final String PENDING = "PNDG";
        public static final String APPROVED = "APRVD";
        public static final String DISAPPROVED = "DSAPRVD";
        public static final String CANCELED = "CNCLD";
        public static final String APPROVED_CANCELED = "APRV_CNCL";
        public static final String COMMENTED = "CMMNTD";
        public static String REQUEST_TYPE_LEAVE = "Leave";
        public static String REQUEST_TYPE_CANCELLATION = "Cancellation";
    }

    public final static HashMap<String, String> LEAVE_STATUS_MAP = new HashMap<>();

    static {
        LEAVE_STATUS_MAP.put(LeaveStatus.APPROVED, "Approved");
        LEAVE_STATUS_MAP.put(LeaveStatus.DISAPPROVED, "Disapproved");
        LEAVE_STATUS_MAP.put(LeaveStatus.CANCELED, "Canceled");
        LEAVE_STATUS_MAP.put(LeaveStatus.PENDING, "Pending");
        LEAVE_STATUS_MAP.put(LeaveStatus.COMMENTED, "Commented");
    }

    public static class MasterCode {

        public static String LEAVE_REASON = "LR";
        public static String BLOOD_GROUPS = "BG";
        public static String CASTES = "CASTE";
        public static String EDUCATIONAL_DEGREES = "EDUDEG";
        public static String OTHER_DETAILS_OF_EMPLOYEE = "EMPOTHRDTILS";
        public static String EMPLOYEE_TYPES = "EMPTYPE";
        public static String LANGUAGES = "LANGS";
        public static String EDUCATION_MEDIUMS = "MDIUM";
        public static String MARITAL_STATUS = "MS";
        public static String NATIONALITY = "NTNLTY";
        public static String FAMILY_OCCUPATIONS = "OCCUPSN";
        public static String POLICY_STATUS = "POLICSTS";
        public static String RELATIONS = "RELESN";
        public static String EXPERIENCE_DESIGNATION = "DEG";
        public static String POLICY_COMPANY = "POLICYCMPNY";
        public static String UNIVERSITY_LIST = "UNI";
        public static String MANIFACTURER = "MF";
        public static String REPORT_GROUP = "RG";
    }

    public static class ASSET_SEARCH_CODE {

        public static final String CATEGORY = "@C";
    }

    public static class CUSTOMFIELD_SEARCH_CODE {

        public static final String FEATURE = "@F";
    }

    public static class SHIFT_SEARCH_CODE {

        public static final String DEPARTMENT = "@D";
    }

    public static class DEPARTMENT_CONFIG_SEARCH_CODE {

        public static final String DEPARTMENT = "@D";
    }

    public static class ASSET_STATUS {

        public static final String AVAILABLE = "A";
        public static final String ISSUED = "I";
        public static final String ARCHIVED = "D";
        public static final String INREPAIR = "R";
    }

    public static class EMPLOYEE_STATUS {

        public static final Long SELECTED = 0L;
        public static final Long AWAITING = 1L;
        public static final Long TRANSFERRED = 2L;
        public static final Long RESIGNED = 3L;
    }

    public static class CATEGORY_STATUS {

        public static final String AVAILABLE = "A";
        public static final String ARCHIVED = "D";
    }

    public final static HashMap<String, String> ASSET_STATUS_MAP = new HashMap<>();

    static {
        ASSET_STATUS_MAP.put(ASSET_STATUS.AVAILABLE, "Available");
        ASSET_STATUS_MAP.put(ASSET_STATUS.ISSUED, "Issued");
        ASSET_STATUS_MAP.put(ASSET_STATUS.ARCHIVED, "Archived");
        ASSET_STATUS_MAP.put(ASSET_STATUS.INREPAIR, "InRepair");
    }

    public final static HashMap<Long, String> EMPLOYEE_STATUS_MAP = new HashMap<>();

    static {
        EMPLOYEE_STATUS_MAP.put(EMPLOYEE_STATUS.SELECTED, "Selected");
        EMPLOYEE_STATUS_MAP.put(EMPLOYEE_STATUS.AWAITING, "Awaiting Decision");
        EMPLOYEE_STATUS_MAP.put(EMPLOYEE_STATUS.TRANSFERRED, "Transferred");
        EMPLOYEE_STATUS_MAP.put(EMPLOYEE_STATUS.RESIGNED, "Resigned");
    }

    public static class ASSET_TYPE {

        public static final String MANAGED = "M";
        public static final String NON_MANAGED = "R";
    }

    public static class PRINT_BARCODE_FIELD_TYPE {

        public static final String INVOICE = "Invoice";
        public static final String PARCEL = "Parcel";
        public static final String LOT = "Lot";
        public static final String PACKET = "Packet";
        public static final String SELL = "Sell";
        public static final String TRANSFER = "Transfer";
        public static final String LOT_SLIP = "Lot Slip";
        public static final String PACKET_SLIP = "Packet Slip";
    }

    public final static HashMap<String, String> ASSET_TYPE_MAP = new HashMap<>();

    static {
        ASSET_TYPE_MAP.put(ASSET_TYPE.MANAGED, "Managed");
        ASSET_TYPE_MAP.put(ASSET_TYPE.NON_MANAGED, "Non-Managed");
    }

    public static class FRANCHISE_REQUIREMENT_TYPE {

        public static final String DESIGNATION = "D";
        public static final String MACHINES = "M";
    }

    public static class FeatureTypes {

        public static final String MENU = "M";
        public static final String DYNAMIC_MENU = "DM";
        public static final String DYNAMIC_ENTITY = "DE";
        public static final String MENU_ITEM = "MI";
        public static final String ITEM_ATTRIBUTE = "IA";
        public static final String EXTRA_ITEM = "EI";
        public static final String ENTITY = "E";
    }

    public static class DiamondFeatureTypes {

        public static final String MENU = "DM";
        public static final String MENU_ITEM = "DMI";
        public static final String EXTRA_ITEM = "DEI";
    }

    public static class ReportFeatureTypes {

        public static final String MENU = "RM";
        public static final String MENU_ITEM = "RMI";
    }

    public static class MessageStatus {

        public static final String PENDING = "P";   //  Pending to reply
        public static final String REPLIED = "R";   //  Replied to the message
        public static final String CLOSED = "C";    //  Seen the reply and closing the notification
    }

    public static class TaskStatus {

        public static final String DUE = "D";
        public static final String CANCELLED = "A";
        public static final String COMPLETED = "C";
        public static final String COMPLETED_ARCHIVED = "C_AR";
        public static final String CANCELLED_ARCHIVED = "A_AR";

    }
    public final static HashMap<String, String> TASK_STATUS_MAP = new HashMap<>();

    static {
        TASK_STATUS_MAP.put(TaskStatus.DUE, "Due");
        TASK_STATUS_MAP.put(TaskStatus.CANCELLED, "Cancelled");
        TASK_STATUS_MAP.put(TaskStatus.COMPLETED, "Completed");
        TASK_STATUS_MAP.put(TaskStatus.COMPLETED_ARCHIVED, "Completed Archived");
        TASK_STATUS_MAP.put(TaskStatus.CANCELLED_ARCHIVED, "Cancelled Archived");
    }

    public static class TaskLastUpdate {

        public static final String DUE = "D";
        public static final String UPDATED = "U";
        public static final String CANCELLED = "A";
        public static final String COMPLETED = "C";
        public static final String DELETED = "L";
    }
    public final static HashMap<String, String> TASK_LAST_UPDATE_MAP = new HashMap<>();

    static {
        TASK_LAST_UPDATE_MAP.put(TaskLastUpdate.DUE, "Due");
        TASK_LAST_UPDATE_MAP.put(TaskLastUpdate.UPDATED, "Updated");
        TASK_LAST_UPDATE_MAP.put(TaskLastUpdate.CANCELLED, "Cancelled");
        TASK_LAST_UPDATE_MAP.put(TaskLastUpdate.COMPLETED, "Completed");
        TASK_LAST_UPDATE_MAP.put(TaskLastUpdate.DELETED, "Deleted");

    }

    public static class TaskRepetitiveMode {

        public static final String DAILY = "D";
        public static final String WEEKLY = "W";
        public static final String MONTHLY = "M";
    }

    public final static HashMap<String, String> TASK_REPETITIVE_MODE_MAP = new HashMap<>();

    static {
        TASK_REPETITIVE_MODE_MAP.put(TaskRepetitiveMode.DAILY, "Daily");
        TASK_REPETITIVE_MODE_MAP.put(TaskRepetitiveMode.WEEKLY, "Weekly");
        TASK_REPETITIVE_MODE_MAP.put(TaskRepetitiveMode.MONTHLY, "Monthly");
    }

    public static class TaskEndRepeatMode {

        public static final String ON_DATE = "OD";
        public static final String AFTER_X_DAYS = "AD";
        public static final String AFTER_X_REPETITIONS = "AR";
    }

    public final static HashMap<String, String> TASK_END_REPEAT_MODE_MAP = new HashMap<>();

    static {
        TASK_END_REPEAT_MODE_MAP.put(TaskEndRepeatMode.ON_DATE, "On date");
        TASK_END_REPEAT_MODE_MAP.put(TaskEndRepeatMode.AFTER_X_DAYS, "After days");
        TASK_END_REPEAT_MODE_MAP.put(TaskEndRepeatMode.AFTER_X_REPETITIONS, "After repetitions");
    }

    public static class RecipientCodeType {

        public static final String EMPLOYEE = "E";
        public static final String DEPARTMENT = "D";
        public static final String DESIGNATION = "R";
        public static final String ACTIVITY = "A";
        public static final String FRANCHISE = "F";
        public static final String FRANCHISE_DEPARTMENT = "X";
        public static final String GROUP = "G";
        public static final String SERVICE = "S";
    }

    public final static Map<String, String> RECIPIENT_TEXT = new HashMap<String, String>();

    static {
        RECIPIENT_TEXT.put(RecipientCodeType.EMPLOYEE, "Employee");
        RECIPIENT_TEXT.put(RecipientCodeType.DEPARTMENT, "Deparment");
        RECIPIENT_TEXT.put(RecipientCodeType.DESIGNATION, "Designation");
        RECIPIENT_TEXT.put(RecipientCodeType.ACTIVITY, "Activity");
        RECIPIENT_TEXT.put(RecipientCodeType.FRANCHISE, "Franchise");
        RECIPIENT_TEXT.put(RecipientCodeType.FRANCHISE_DEPARTMENT, "Franchise Department");
        RECIPIENT_TEXT.put(RecipientCodeType.GROUP, "Group");
        RECIPIENT_TEXT.put(RecipientCodeType.SERVICE, "Service");
    }

    public static class CustomField {

        public static final String TYPE = "type";
        public static final String MODEL = "model";
        public static final String MODEL_WITHOUT_SEPERATOR = "modelWithoutSeperators";
        public static final String LABEL = "label";
        public static final String FEATURE = "featureName";
        public static final String PLACEHOLDER = "placeholder";
        public static final String VALIDATE = "validate";
        public static final String MAX_LENGTH = "maxLength";
        public static final String MIN_LENGTH = "minLength";
        public static final String READ_ONLY = "readonly";
        public static final String REQUIRED = "required";
        public static final String VAL = "val";
        public static final String HINT = "hint";
        public static final String MASKED = "masked";
        public static final String DISABLED = "disabled";
        public static final String CALLBACK = "callback";
        public static final String ATTRIBUTES = "attributes";
        public static final String VALUES = "values";
        public static final String DATE_FORMAT = "format";
        public static final String DEFAULT_DATE = "defaultdate";
        public static final String DEFAULT_TIME = "defaulttime";
        public static final String DB_TYPE = "dbType";
        public static final String CURRENCY_CODE = "currencyCode";
        public static final String CURRENCY_TYPE = "currencytype";
        public static final String CURRENCY_FORMAT = "currencyFormat";
        public static final String CURRENCY_SYMBOL_POSITION = "currencySymbolPosition";
        public static final String CURRENCY_CODE_SYMBOL_MAP = "currencyCodeWithSymbolMap";
        public static final String MAX_RANGE = "max";
        public static final String MIN_RANGE = "min";
        public static final String CHECKBOX_FORMAT = "formatValue";
        public static final String NEGATIVE_ALLOWED = "negativeAllowed";
        public static final String IS_EMPLOYEE = "isEmployee";
        public static final String IS_DEPARTMENT = "isDepartment";
        public static final String IS_DESIGNATION = "isDesignation";
        public static final String IS_MULTISELECT = "isMultiSelect";
        public static final String ID = "fieldId";
        public static final String IS_CONSTRAINT = "isConstraint";
        public static final String CONSTRAINT_VALUE = "constraintValue";
        public static final String IS_CONSTRAINT_ON_SAME_FEATURE = "isConstraintOnSameFeature";
        public static final String LIST_OF_CONSTRAINTS_ON_FIELD = "listOfConstraintsOnField";
        public static final String MAP_DBFIELD_LABEL_FOR_CONST_MSG = "mapOfDbFieldWithLabelForContMsg";
        public static final String MAP_DBFIELD_WITH_CONSTRAINT_DBFIELD = "mapOfDbFieldWithItsConstraintDbField";
        public static final String FORMULA_VALUE = "formulaValue";
        public static final String FORMULA_OPTION_VALUE = "formulaOption";
        public static final String IS_INVOLVED_IN_FORMULA = "isInvolvedInFormula";
        public static final String FORMULAS_IN_WHICH_FIELD_INVOLVED = "formulaWithDbField";
        public static final String FORMULA_ROUNDOFF = "formulaRoundOff";
        public static final String SEQUENCE = "sequenceNum";
        public static final String IS_CURRENCY_INVOLVED_IN_FIELD = "isCurrencyInvolvedInField";
        public static final String SINGLE_FILE = "singleFile";
        public static final String DEFAULT_REGEX_TEXT = "defaultPatternForText";
        public static final String DEFAULT_REGEX_EMAIL = "defaultPatternForEmail";
        public static final String DECIMAL_ALLOW = "decimalallowed";
        public static final String DIGITS_BFR_DECIMAL = "digitsBeforeDecimal";
        public static final String DIGITS_AFTR_DECIMAL = "digitsAfterDecimal";
        public static final String IS_FRANCHISE = "isFranchise";
        public static final String IS_UNIQUE = "isUnique";
        public static final String UNIQUE_FOR_FIELDS = "uniqueForFields";
        public static final String BACKGROUND_COLOR = "backgroundColor";
        public static final String SKIP_HOLIDAYS = "skipHolidays";
        public static final String IS_BARCODE_REQUIRED = "isBarcodeRequired";
        public static final String SELECTED_PARAMETER = "selectedParameter";
        public static final String DEPT_LIST = "deptList";
        public static final String DESG_LIST = "desgList";
        public static final String SKIP_HOLIDAY_FOR_DATE = "skipHolidayForDate";
        public static final String DATE_INVOLVED_FOR_OTHER_DEF_DATE = "dateInvInOtherDefDate";
        public static final String MAP_DEFAULT_DATE_OTHER = "mapDefaultdateOther";
        public static final String MAP_DEFAULT_DATE_NUM = "mapDefaultdateNumber";

        public final static List<String> EXCLUDE_FEATURES_LIST = Arrays.asList("manageDesignation", "manageCustomField", "manageMasters", "manageLocation", "manageActivityFlow", "manageLocales", "manageReferenceRate", "manageActivity", "manageFeature", "manageCaratRange", "manageGoalSheet", "managePriceList", "manageTransferEmployees", "manageSubEntity", "manageReports");

        public final static Map<String, String> CONDITIONAL_OPERATORS_MAP = new LinkedHashMap<>();

        public final static String I18_FIELD = "CustomField";

        static {
            CONDITIONAL_OPERATORS_MAP.put(">", ">(Greater than)");
            CONDITIONAL_OPERATORS_MAP.put("<", "<(Less than)");
            CONDITIONAL_OPERATORS_MAP.put("===", "=(Equal to)");
            CONDITIONAL_OPERATORS_MAP.put(">=", ">=(Greater than equal to)");
            CONDITIONAL_OPERATORS_MAP.put("<=", "<=(Less than equal to)");
        }
        ;

        public final static Map<String, String> ARITHEMETIC_OPERATORS_MAP = new LinkedHashMap<>();

        static {
            ARITHEMETIC_OPERATORS_MAP.put("+", "+");
            ARITHEMETIC_OPERATORS_MAP.put("-", "-");
            ARITHEMETIC_OPERATORS_MAP.put("*", "*");
            ARITHEMETIC_OPERATORS_MAP.put("/", "/");
            ARITHEMETIC_OPERATORS_MAP.put("%", "% OF");
        }

        ;
        
        public static class Pointer {

            public final static String LABEL = "pointerlabel";
            public final static String FIELD_ID = "pointerFieldId";
        };

        public static class Validation {

            public static final String REQUIRE = "isRequired";
            public static final String ONLY_INTEGER = "numbersOnly";
            public static final String PHONE_NUM = "numbers-only";
            public static final String DECIMAL_ALLOW = "decimalallowed";
            public static final String NEGATIVE_ALLOWED = "negativeAllowed";
            public static final String DEPENDANT_MASTER_VALUE = "dependantMasterValue";
            public static final String DEPENDANT_MASTER_ID = "dependantMasterId";
            public static final String IS_DEPENDANT = "isDependant";
            public static final String DEFAULT_VALUE = "defaultValue";
            public static final String MAX_LENGTH = "maxLength";
            public static final String DATE_VALIDATION = "allow";
            public static final String ALLOW_DEFAULT_DATE = "allowDefaultDate";
            public static final String DEFAULT_DATE = "defaultDate";
            public static final String INCLUDE_TIME = "includeTime";
            public static final String DEFAULT_TIME = "defaultTime";
            public static final String MIN_LENGTH = "minLength";
            public static final String ALLOWED_TYPE = "allowedTypes";
            public static final String IS_CASE_SENSITIVE = "isCaseSensitive";
            public static final String ALLOW_FILE_TYPE = "allowFileType";
            public static final String SUPPORTED_TYPES_FOR_OTHER = "supportedTypesForOther";
            public static final String MAX_FILE_SIZE = "maxFileSize";
            public static final String ALLOW_MULTIPLE_FILE = "allowMultipleFile";
            public static final String ALLOW_TO_SELECT_MULTIPLE_FILE = "allowToSelectMultipleFileAtATime";
            public static final String SHOWTHUMBNAIL = "showThumbnail";
            public static final String DISPLAY_FULL_IMAGE = "displayFullImage";
            public static final String HINT_MESSAGE = "hintMessage";
            public static final String POINTER = "pointer";
            public static final String IS_MASKED = "isMasked";
            public static final String DIGITS_BEFORE_DECIMAL = "digitsBefore";
            public static final String DIGITS_AFTER_DECIMAL = "digitsAfter";
            public static final String START_RANGE = "startRange";
            public static final String END_RANGE = "endRange";
            public static final String CHECKBOX_FORMAT = "formatValue";
            public static final String IS_DEPARTMENT = "isDepartment";
            public static final String IS_DESIGNATION = "isDesignation";
            public static final String IS_EMPLOYEE = "isEmployee";
            public static final String IS_MULTISELECT = "isMultiSelect";
            public static final String CURRENCY_CODE = "currencyCode";
            public static final String CURRENCY_SYMBOL_POSITION = "currencySymbolPosition";
            public static final String CURRENCY_FORMAT = "currencyFormat";
            public static final String REGEX = "regexModified";
            public static final String ORIGINAL_REGEX = "regex";
            public static final String IS_FRANCHISE = "isFranchise";
            public static final String LIST_OF_EXCEPTIONS = "allExceptionsDependantOnField";
            public static final String FIELD_HAS_EXCEPTION = "fieldHasException";
            public static final String IS_UNIQUE = "isUnique";
            public static final String UNIQUE_FOR_FIELDS = "uniqueForFields";
            public static final String BACKGROUND_COLOR = "backgroundColor";
            public static final String SKIP_HOLIDAYS = "skipHolidays";
            public static final String IS_BARCODE_REQUIRED = "isBarcodeRequired";
            public static final String SELECTED_PARAMETER = "selectedParameter";
            public static final String DEPT_LIST = "deptList";
            public static final String DESG_LIST = "desgList";
            public static final String MAP_FOR_DEFAULT_DATE = "mapForDefaultDate";
            public static final String DISPLAY_SHORTCUT_CODE = "displayShortcutCode";
        }

        public static class ComponentType {

            public static final String NUMBER = "Number";
            public static final String DROPDOWN = "Dropdown";
            public static final String TEXT_FIELD = "Text field";
            public static final String TEXT_AREA = "Text area";
            public static final String RADIO_BUTTON = "Radio button";
            public static final String DATE = "Date";
            public static final String DATE_RANGE = "Date range";
            public static final String TIME = "Time";
            public static final String TIME_RANGE = "Time range";
            public static final String PASSWORD = "Password";
            public static final String CURRENCY = "Currency";
            public static final String EXCHANGE_RATE = "Exchange rate";
            public static final String UPLOAD = "Upload";
            public static final String IMAGE = "Image";
            public static final String EMAIL = "Email";
            public static final String PHONE = "Phone";
            public static final String MULTISELECT_DROPDOWN = "MultiSelect";
            public static final String PERCENT = "Percent";
            public static final String CHECKBOX = "Checkbox";
            public static final String USER_MULTISELECT = "UserMultiSelect";
            public static final String AUTO_GENERATED = "AutoGenerated";
            public static final String POINTER = "Pointer";
            public static final String ANGLE = "Angle";
            public static final String SUBENTITY = "SubEntity";
            public static final String FORMULA = "Formula";
        }

        public final static HashMap<String, String> COMPONENT_CODE_MAP = new HashMap<>();

        static {
            COMPONENT_CODE_MAP.put(CustomField.ComponentType.NUMBER, "NM");
            COMPONENT_CODE_MAP.put(CustomField.ComponentType.DROPDOWN, "DRP");
            COMPONENT_CODE_MAP.put(CustomField.ComponentType.TEXT_FIELD, "TF");
            COMPONENT_CODE_MAP.put(CustomField.ComponentType.TEXT_AREA, "TA");
            COMPONENT_CODE_MAP.put(CustomField.ComponentType.RADIO_BUTTON, "RB");
            COMPONENT_CODE_MAP.put(CustomField.ComponentType.DATE, "DT");
            COMPONENT_CODE_MAP.put(CustomField.ComponentType.DATE_RANGE, "DR");
            COMPONENT_CODE_MAP.put(CustomField.ComponentType.TIME, "TM");
            COMPONENT_CODE_MAP.put(CustomField.ComponentType.TIME_RANGE, "TR");
            COMPONENT_CODE_MAP.put(CustomField.ComponentType.PASSWORD, "PWD");
            COMPONENT_CODE_MAP.put(CustomField.ComponentType.CURRENCY, "CRN");
            COMPONENT_CODE_MAP.put(CustomField.ComponentType.EXCHANGE_RATE, "EXR");
            COMPONENT_CODE_MAP.put(CustomField.ComponentType.UPLOAD, "UPD");
            COMPONENT_CODE_MAP.put(CustomField.ComponentType.IMAGE, "IMG");
            COMPONENT_CODE_MAP.put(CustomField.ComponentType.EMAIL, "EM");
            COMPONENT_CODE_MAP.put(CustomField.ComponentType.PHONE, "PH");
            COMPONENT_CODE_MAP.put(CustomField.ComponentType.MULTISELECT_DROPDOWN, "MS");
            COMPONENT_CODE_MAP.put(CustomField.ComponentType.PERCENT, "PRC");
            COMPONENT_CODE_MAP.put(CustomField.ComponentType.NUMBER, "NM");
            COMPONENT_CODE_MAP.put(CustomField.ComponentType.CHECKBOX, "CB");
            COMPONENT_CODE_MAP.put(CustomField.ComponentType.USER_MULTISELECT, "UMS");
            COMPONENT_CODE_MAP.put(CustomField.ComponentType.AUTO_GENERATED, "AG");
            COMPONENT_CODE_MAP.put(CustomField.ComponentType.POINTER, "POI");
            COMPONENT_CODE_MAP.put(CustomField.ComponentType.ANGLE, "ANG");
            COMPONENT_CODE_MAP.put(CustomField.ComponentType.SUBENTITY, "SE");
            COMPONENT_CODE_MAP.put(CustomField.ComponentType.FORMULA, "FRM");

        }

        /**
         * Map for deciding the field type from the component type. This map
         * gives the field type of java, not the database type.
         */
        public static final Map<String, String> FIELD_TYPE_MAP = new HashMap<>();

        static {
            FIELD_TYPE_MAP.put(CustomField.ComponentType.CURRENCY, DbFieldType.DOUBLE);
            FIELD_TYPE_MAP.put(CustomField.ComponentType.EXCHANGE_RATE, DbFieldType.DOUBLE);
            FIELD_TYPE_MAP.put(CustomField.ComponentType.DATE, DbFieldType.DATE);
            FIELD_TYPE_MAP.put(CustomField.ComponentType.DATE_RANGE, DbFieldType.DATE);
            FIELD_TYPE_MAP.put(CustomField.ComponentType.DROPDOWN, DbFieldType.LONG);
            FIELD_TYPE_MAP.put(CustomField.ComponentType.IMAGE, DbFieldType.IMAGE);
            FIELD_TYPE_MAP.put(CustomField.ComponentType.NUMBER, DbFieldType.DOUBLE);
            FIELD_TYPE_MAP.put(CustomField.ComponentType.PASSWORD, DbFieldType.STRING);
            FIELD_TYPE_MAP.put(CustomField.ComponentType.CHECKBOX, DbFieldType.BOOLEAN);
            FIELD_TYPE_MAP.put(CustomField.ComponentType.RADIO_BUTTON, DbFieldType.LONG);
            FIELD_TYPE_MAP.put(CustomField.ComponentType.TEXT_AREA, DbFieldType.STRING);
            FIELD_TYPE_MAP.put(CustomField.ComponentType.TEXT_FIELD, DbFieldType.STRING);
            FIELD_TYPE_MAP.put(CustomField.ComponentType.TIME, DbFieldType.DATE);
            FIELD_TYPE_MAP.put(CustomField.ComponentType.TIME_RANGE, DbFieldType.DATE);
            FIELD_TYPE_MAP.put(CustomField.ComponentType.UPLOAD, DbFieldType.FILE);
            FIELD_TYPE_MAP.put(CustomField.ComponentType.EMAIL, DbFieldType.STRING);
            FIELD_TYPE_MAP.put(CustomField.ComponentType.PHONE, DbFieldType.STRING);
            FIELD_TYPE_MAP.put(CustomField.ComponentType.MULTISELECT_DROPDOWN, DbFieldType.STRING);
            FIELD_TYPE_MAP.put(CustomField.ComponentType.PERCENT, DbFieldType.DOUBLE);
            FIELD_TYPE_MAP.put(CustomField.ComponentType.USER_MULTISELECT, DbFieldType.STRING);
            FIELD_TYPE_MAP.put(CustomField.ComponentType.AUTO_GENERATED, DbFieldType.AUTO_GENERATED);
            FIELD_TYPE_MAP.put(CustomField.ComponentType.ANGLE, DbFieldType.DOUBLE);
            FIELD_TYPE_MAP.put(CustomField.ComponentType.FORMULA, DbFieldType.DOUBLE);
            FIELD_TYPE_MAP.put(CustomField.ComponentType.SUBENTITY, DbFieldType.OBJECT_ID);
        }

        public static class DbFieldType {

            public static final String DOUBLE = "Double";
            public static final String DATE = "Date";
            public static final String DATE_TIME = "datetime";
            public static final String TIME = "time";
            public static final String LONG = "Long";
            public static final String STRING = "String";
            public static final String STRING_ARRAY = "StringArray";
            public static final String ARRAY = "Array";
            public static final String INTEGER = "Integer";
            public static final String IMAGE = "Image";
            public static final String FILE = "File";
            public static final String AUTO_GENERATED = "AutoGenerated";
            public static final String BOOLEAN = "Boolean";
            public static final String MULTISELECT_ARRAY = "multiselect";
            public static final String OBJECT_ID = "ObjectId";
        }

        public static class DataType {

            public static final String INT8 = "int8";
            public static final String VARCHAR = "varchar";
            public static final String BOOLEAN = "boolean";
            public static final String TIMESTAMP = "timestamp";
            public static final String DOUBLE = "double precision";
        }
    }

    public static class EventRegistrationStatus {

        public static final String OFFLINE = "OFLN";
        public static final String ONLINE = "ONLN";
    }

    public static class EventUserRegistrationStatus {

        public static final String ATTENDING = "AT";
        public static final String NOT_ATTENDING = "NAT";
    }

    public static class EventStatus {

        public static final String CREATED = "CRTD";
        public static final String UPCOMING = "UPCMNG";
        public static final String CANCELLED = "CNCLD";
        public static final String COMPLETED = "CMPLTD";
        public static final String ONGOING = "ONGNG";
        public static final String COMPLETED_ARCHIVED = "CMP_ARCV";
    }

    public static class GoalTemplateStatus {

        public static final String ACTIVE = "A";
        public static final String PENDING = "P";
        public static final String DISCARDED = "D";
    }
    /**
     * Map for displaying event status on UI
     */
    public static final Map<String, String> EVENT_STATUS_MAP = new HashMap<>();

    static {
        EVENT_STATUS_MAP.put(EventStatus.CREATED, "Created");
        EVENT_STATUS_MAP.put(EventStatus.UPCOMING, "Upcoming");
        EVENT_STATUS_MAP.put(EventStatus.CANCELLED, "Cancelled");
        EVENT_STATUS_MAP.put(EventStatus.COMPLETED, "Completed");
        EVENT_STATUS_MAP.put(EventStatus.ONGOING, "Ongoing");

    }

    public enum FeatureNameForCustomField {

        //Add a featurename for custom fields.
        ASSET, DEPARTMENT, USER, FRANCHISE, ISSUEASSET, CATEGORY,
        LEAVEWORKFLOW, HOLIDAY, LEAVE, RESPONDLEAVE, EVENT, TASK, SHIFT, RULE, INVOICE, SUBENTITY, NOTIFICATION, GOAL, MESSAGE;
    }

    public enum SectionNameForCustomField {

        //Add a featurename for custom fields.
        GENERAL, POLICY, PERSONAL, CONTACT, IDENTIFICATION, OTHER, HKGWORK,
        EDUCATION, EXPERIENCE, FAMILY, REGISTRATION, INVITATIONCARD;

    }

    public static class FranchiseConfiguration {

        public static final String MESSAGING_MAX_MSG_PER_USER = "MESSAGING_MAX_MSG_PER_USER";
        public static final String MASTERS_AUTH_PWD = "MASTERS_AUTH_PWD";
        public static final String SHIFT_REMINDER_PERIOD = "SHIFT_REMINDER_PERIOD";
        public static final String TASK_STATUS_ON_LOGIN = "TASK_STATUS_ON_LOGIN";
        public static final String TASK_ARCHIVE_PERIOD = "TASK_ARCHIVE_PERIOD";
        public static final String EMP_SEND_MAIL_TO = "EMP_SEND_MAIL_TO";
        public static final String EMP_MIN_AGE = "EMP_MIN_AGE";
        public static final String EMP_MAX_AGE = "EMP_MAX_AGE";
        public static final String EMP_DEFAULT_IMAGE = "EMP_DEFAULT_IMAGE";
        public static final String LEAVE_MAX_YEARLY_LEAVES = "LEAVE_MAX_YEARLY_LEAVES";
        public static final String ALLOW_LEAVE_BEYOND_LIMIT = "ALLOW_LEAVE_BEYOND_LIMIT";
        public static final String SCHEDULER_LAST_RUN = "SCHEDULER_LAST_RUN";
        public static final String NOTIFICATION_SCHEDULER_LAST_RUN = "NOTIFICATION_SCHEDULER_LAST_RUN";
        public static final String ID_CONFIGURATION = "ID_CONFIGURATION";
        public static final String CURR_VISIBILITY_STATUS = "CURR_VISIBILITY_STATUS";
        public static final String NO_OF_SNAP_IDS = "NO_OF_SNAP_IDS";
        public static final String NO_OF_DIAMOND_ALLOWED_QUEUE = "NO_OF_DIAMOND_ALLOWED_QUEUE";
        public static final String CARRIER_BOY_DESIGNATION = "CARRIER_BOY_DESIGNATION";
        public static final String DEFAULT_XMPP_EMAIL_ADDRESS = "DEFAULT_XMPP_EMAIL_ADDRESS";
        public static final String ANALYTICS_ENGINE_USERNAME = "ANALYTICS_ENGINE_USERNAME";
        public static final String ANALYTICS_ENGINE_PWD = "ANALYTICS_ENGINE_PWD";
        public static final String ANALYTICS_SERVER_URL = "ANALYTICS_SERVER_URL";
        public static final String ALLOW_CARATE_VARIATION = "ALLOW_CARATE_VARIATION";

//        To retrieve series of ID's of employee types append id of employee type to "EMP_TYPE_"
//            e.g. "EMP_TYPE_1","EMP_TYPE_2"....
    }

    public static class NotificationType {

        public static final String HOLIDAY = "HLDY";
        public static final String EVENT = "EVNT";
        public static final String WORKFLOW = "WRKFLW";
        public static final String TASK = "TSK";
        public static final String EMPLOYEE = "EMPLY";
        public static final String LEAVE = "LEAVE";
        public static final String ASSET = "AST";
        public static final String MASTER = "MSTR";
        public static final String DESIGNATION = "ROLE";
        public static final String SHIFT = "SHFT";
        public static final String LOCALE = "LOCALE";
        public static final String NOTIFICATION = "NTFCTN";
        public static final String ALLOT_LOT = "ALLT_LOT";
    }

    public static class NotificationInstanceType {

        public static final String ADD_HOLIDAY = "CRT_HLDY";
        public static final String UPDATE_HOLIDAY = "EDT_HLDY";
        public static final String REMOVE_HOLIDAY = "RMV_HLDY";
        public static final String ADD_EVENT = "CRT_EVNT";
        public static final String UPDATE_EVENT = "EDT_EVNT";
        public static final String UPDATE_EVENT_REG_FORM = "EDT_EVNT_REG_FRM";
        public static final String CANCEL_EVENT = "CNCL_EVNT";
        public static final String PUBLISH_EVENT = "PBLSH_EVNT";
        public static final String ADD_WORKFLOW = "CRT_WRKFLW";
        public static final String UPDATE_WORKFLOW = "EDT_WRKFLW";
        public static final String ADD_TASK = "CRT_TSK";
        public static final String UPDATE_TASK = "EDT_TSK";
        public static final String TASK_DUE_DATE_CROSSED = "TSK_DUE_DT_CRSD";
        public static final String TASK_COMPLETED = "TSK_CMPLTD";
        public static final String TASK_REMOVED = "TSK_RMVD";
        public static final String TASK_CANCELLED = "TSK_CNCLD";
        public static final String ADD_EMP = "CRT_EMP";
        public static final String NEW_EMPLOYEE_REPORTING = "NEW_EMP_RPORTNG";
        public static final String NEW_EMPLOYEE_FIRST_LOGIN = "NEW_EMP_FRST_LGN";
        public static final String UPDATE_EMP = "EDT_EMP";
        public static final String LEAVE_APPLIED = "LEAVE_APLD";
        public static final String LEAVE_RESPONSE_MINE = "LEAVE_RSPNS_MY";
        public static final String LEAVE_RESPONSE_OTHER = "LEAVE_RSPNS_OTHR";
        public static final String UPDATE_LEAVE = "EDT_LEAVE";
        public static final String CANCEL_LEAVE = "CNCL_LEAVE";
        public static final String ADD_SHIFT = "CRT_SHFT";
        public static final String ADD_DEFAULT_SHIFT = "CRT_DEFAULT_SHFT";
        public static final String UPDATE_SHIFT = "EDT_SHFT";
        public static final String PUBLISH_SHIFT = "PBLSH_SHFT";
        public static final String ISSUE_ASSET_MANAGED = "ISU_AST_MNGD";
        public static final String ISSUE_ASSET_NON_MANAGED = "ISU_AST_NON_MNGD";
        public static final String UPDATE_ASSET = "EDT_ASST";
        public static final String UPDATE_DESIGNATION_SELF = "EDT_ROLE";
        public static final String UPDATE_DESIGNATION = "EDT_DESIG";
        public static final String UPDATE_MASTER = "EDT_MSTR";
        public static final String DELETE_MASTER = "DELETE_MSTR";
        public static final String ISSUE_ASSET_MANAGED_EMP = "ISU_AST_MNGD_EMP";
        public static final String ISSUE_ASSET_MANAGED_DPT = "ISU_AST_MNGD_DPT";
        public static final String ISSUE_ASSET_NON_MANAGED_EMP = "ISU_AST_NON_MNGD_EMP";
        public static final String ISSUE_ASSET_NON_MANAGED_DPT = "ISU_AST_NON_MNGD_DPT";
        public static final String ALLOT_LOT = "ALLT_LOT";
        public static final String ROTATE_SHIFT = "ROTATE_SHFT";
        public static final String NOTIFICATION = "NOTIFICATION";
    }

    public static class NotificationMessageParam {

        public static final String HOLIDAY_NAME = "H_NAME";
        public static final String DESIGNATION_NAME = "DESIG_NM";
        public static final String HOLIDAY_DATE = "H_DATE";
        public static final String HOLIDAY_OLD_DATE = "H_OLD_DATE";
        public static final String EVENT_TITLE = "EVNT_TITLE";
        public static final String EVENT_PUBLISH_DATE = "EVNT_PBLSH_DATE";
        public static final String DEPARTMENT_NAME = "DPT_NM";
        public static final String EMP_CODE = "EMP_CD";
        public static final String EMP_NAME = "EMP_NM";
        public static final String FRANCHISE_NAME = "FRNCSE_NM";
        public static final String LEAVE_DAYS = "LEAVE_DYS";
        public static final String LEAVE_START_DATE = "LEAVE_STRT_DT";
        public static final String LEAVE_STATUS = "LEAVE_STTS";
        public static final String APPROVER_NAME = "APPRVER_NAME";
        public static final String MASTER_NAME = "MSTR_NM";
        public static final String LANGUAGE_NAME = "LANG_NM";
        public static final String SHIFT_NAME = "SHFT_NM";
        public static final String SHIFT_START_DATE = "SHFT_STRT_DT";
        public static final String SHIFT_START_TIME = "SHFT_STRT_TM";
        public static final String SHIFT_END_TIME = "SHFT_END_TM";
        public static final String ASSIGNER_CODE = "ASSGNR_ID";
        public static final String ASSIGNER_NAME = "ASSGNR_NM";
        public static final String MODEL_NUMBER = "MDL_NUM";
        public static final String NO_OF_UNITS = "NO_OF_UNTS";
        public static final String ASSET_NAME = "AST_NM";
        public static final String LOT_ID = "LOT_ID";
        public static final String SERVICE_NM = "SERVICE_NM";
        public static final String NOTIFICATION_DESC = "NOTIFICATION_DESC";
    }

    public static class Feature {

        public static final String MANAGE_ASSET = "manageAssets";
        public static final String MANAGE_HOLIDAY = "manageHoliday";
        public static final String MANAGE_TASK = "manageTasks";
        public static final String MANAGE_NOTIFICATION = "manageNotifications";
        public static final String MANAGE_DESIGNATION = "manageDesignation";
        public static final String MANAGE_FRANCHISE = "manageFranchise";
        public static final String MANAGE_EVENT = "manageEvents";
        public static final String EVENT_ADD_EDIT = "eventsAddEdit";
        public static final String HOLIDAY_ADD_EDIT = "holidayAddEdit";
        public static final String FRANCHISE_ADD_EDIT = "franchiseAddEdit";
        public static final String MANAGE_LOCATION = "manageLocation";
        public static final String LOCATION_DETAILS = "locationdetails";
        public static final String DESIGNATION_ADD = "designationAdd";
        public static final String DESIGNATION_EDIT = "designationEdit";
        public static final String FRANCHISE_ACTIVATE_DEACTIVATE = "franchiseActivateDeactivate";
        public static final String MANAGE_FEATURE = "manageFeature";
        public static final String APPLY_LEAVE = "applyLeave";
        public static final String MANAGE_SHIFT = "manageShift";
        public static final String MANAGE_MASTER = "manageMasters";
        public static final String MANAGE_LEAVE_WORKFLOW = "manageLeaveWorkflow";
        public static final String MANAGE_MESSAGE = "manageMessages";
        public static final String MANAGE_ACTIVITY_FLOW = "manageactivityflow";
        public static final String MANAGE_DEPARTMENT = "manageDepartment";
        public static final String CONFIGURE_DEPARTMENT = "configureDepartment";
        public static final String MANAGE_EMPLOYEE = "manageEmployees";
        public static final String MANAGE_APPLY_LEAVE = "applyLeave";
        public static final String MANAGE_RESPOND_LEAVE = "manageLeave";
        public static final String MANAGE_REPORT = "manageReports";
        public static final String REPORT_ADD_EDIT = "reportAddEdit";
        public static final String INVOICE = "invoice";
        public static final String PARCEL = "parcel";
        public static final String LOT = "lot";
        public static final String PACKET = "packet";
        public static final String SELL = "sell";
        public static final String TRANSFER = "transfer";
        public static final String SLIP = "slip";
        public static final String ISSUE = "issue";
        public static final String ISSUERECEIVE = "issueReceive";
        public static final String WRITE = "writeService";
        public static final String FINALIZE = "finalize";
        public static final String PLAN = "plan";
        public static final String RAPCALC = "rapcalc";
        public static final String ROUGHCALC = "roughCalcyEntity";
        public static final String SUB_LOT = "subLotEntity";
        public static final String ALLOTMENT = "allotment";
        public static final String ROUGH_PURCHASE = "purchase";
        public static final String ROUGH_MAKEABLE = "roughMakeableEntity";
        public static final String FINAL_MAKEABLE = "finalMakeableEntity";
        public static final String ESTIMATE_PREDICTION = "predictionEstimate";
    }

    public static class StaticServices {

        public static final String INVOICE = "invoice";
        public static final String LOT = "lot";
        public static final String PARCEL = "parcel";
        public static final String PACKET = "packet";
    }

    public static class AttendanceStatus {

        public static final String LEAVE = "L";
        public static final String HOLIDAY = "H";
        public static final String ABSENT = "A";
        public static final String PRESENT = "P";
        public static final String PRESENT_HALF = "PH";
        public static final String EXCEPTION = "E";

    }

    public final static HashMap<String, String> MASTERS_MAP = new HashMap<>();

    static {
        MASTERS_MAP.put(MasterCode.MANIFACTURER, "Manufacturer");
        MASTERS_MAP.put(MasterCode.OTHER_DETAILS_OF_EMPLOYEE, "Other deatils of Employee");
        MASTERS_MAP.put(MasterCode.EDUCATION_MEDIUMS, "Education medium");
        MASTERS_MAP.put(MasterCode.BLOOD_GROUPS, "Blood Group");
        MASTERS_MAP.put(MasterCode.CASTES, "Caste");
        MASTERS_MAP.put(MasterCode.EDUCATIONAL_DEGREES, "Education Degree");
        MASTERS_MAP.put(MasterCode.EMPLOYEE_TYPES, "Employee type");
        MASTERS_MAP.put(MasterCode.EXPERIENCE_DESIGNATION, "Experience Designation");
        MASTERS_MAP.put(MasterCode.FAMILY_OCCUPATIONS, "Family Occupation");
        MASTERS_MAP.put(MasterCode.LEAVE_REASON, "Leave Reason");
        MASTERS_MAP.put(MasterCode.MARITAL_STATUS, "Marital Status");
        MASTERS_MAP.put(MasterCode.NATIONALITY, "Nationality");
        MASTERS_MAP.put(MasterCode.POLICY_COMPANY, "Policy Company");
        MASTERS_MAP.put(MasterCode.POLICY_STATUS, "Policy Status");
        MASTERS_MAP.put(MasterCode.RELATIONS, "Relations");
        MASTERS_MAP.put(MasterCode.UNIVERSITY_LIST, "University List");
        MASTERS_MAP.put(MasterCode.REPORT_GROUP, "Report Group");
    }

    public static class PrecedenceLevel {

        public static final String LEVEL_1_SUPERADMIN = "1";
        public static final String LEVEL_2_FRANCHISE_ADMIN = "2";
        public static final String LEVEL_3_REGULAR = "3";

    }

    public final static HashMap<String, List<String>> MASTER_FEATURE_MAP = new HashMap<>();

    static {
        MASTER_FEATURE_MAP.put(MasterCode.MANIFACTURER, Arrays.asList("Assets"));
        MASTER_FEATURE_MAP.put(MasterCode.OTHER_DETAILS_OF_EMPLOYEE, Arrays.asList("Employees"));
        MASTER_FEATURE_MAP.put(MasterCode.EDUCATION_MEDIUMS, Arrays.asList("Employees"));
        MASTER_FEATURE_MAP.put(MasterCode.BLOOD_GROUPS, Arrays.asList("Employees"));
        MASTER_FEATURE_MAP.put(MasterCode.CASTES, Arrays.asList("Employees"));
        MASTER_FEATURE_MAP.put(MasterCode.EDUCATIONAL_DEGREES, Arrays.asList("Employees"));
        MASTER_FEATURE_MAP.put(MasterCode.EMPLOYEE_TYPES, Arrays.asList("Employees"));
        MASTER_FEATURE_MAP.put(MasterCode.EXPERIENCE_DESIGNATION, Arrays.asList("Employees"));
        MASTER_FEATURE_MAP.put(MasterCode.FAMILY_OCCUPATIONS, Arrays.asList("Employees"));
        MASTER_FEATURE_MAP.put(MasterCode.LEAVE_REASON, Arrays.asList("Leave"));
        MASTER_FEATURE_MAP.put(MasterCode.MARITAL_STATUS, Arrays.asList("Employees"));
        MASTER_FEATURE_MAP.put(MasterCode.NATIONALITY, Arrays.asList("Employees"));
        MASTER_FEATURE_MAP.put(MasterCode.POLICY_COMPANY, Arrays.asList("Employees"));
        MASTER_FEATURE_MAP.put(MasterCode.POLICY_STATUS, Arrays.asList("Employees"));
        MASTER_FEATURE_MAP.put(MasterCode.RELATIONS, Arrays.asList("Employees"));
        MASTER_FEATURE_MAP.put(MasterCode.UNIVERSITY_LIST, Arrays.asList("Employees"));
        MASTER_FEATURE_MAP.put(MasterCode.REPORT_GROUP, Arrays.asList("Report Builder"));
    }

    public final static List<String> I18N_CONTENT_TYPE_LIST = Collections.unmodifiableList(Arrays.asList("All", "Department", "Employee", "Designation", "Shift", "Event", "Activity", "Franchise", "Asset", "Location", "Feature", "Section", "AssetCategory", "EventCategory", "TaskCategory", "Holiday", "Task", "CustomField"));

    public final static HashMap<String, String> I18N_CONTENT_TYPE_MAP = new HashMap<>();

    static {
        I18N_CONTENT_TYPE_MAP.put("department", NotificationMessageParam.DEPARTMENT_NAME);
        I18N_CONTENT_TYPE_MAP.put("employee", NotificationMessageParam.EMP_NAME);
        I18N_CONTENT_TYPE_MAP.put("designation", NotificationMessageParam.DESIGNATION_NAME);
        I18N_CONTENT_TYPE_MAP.put("shift", NotificationMessageParam.SHIFT_NAME);
        I18N_CONTENT_TYPE_MAP.put("event", NotificationMessageParam.EVENT_TITLE);
        I18N_CONTENT_TYPE_MAP.put("franchise", NotificationMessageParam.FRANCHISE_NAME);
        I18N_CONTENT_TYPE_MAP.put("activity", "ACT_NM");
        I18N_CONTENT_TYPE_MAP.put("asset", NotificationMessageParam.ASSET_NAME);
        I18N_CONTENT_TYPE_MAP.put("location", "LCT_NM");
        I18N_CONTENT_TYPE_MAP.put("feature", "FTR_NM");
        I18N_CONTENT_TYPE_MAP.put("section", "SEC_NM");
        I18N_CONTENT_TYPE_MAP.put("assetcategory", "ASTCTG_NM");
        I18N_CONTENT_TYPE_MAP.put("taskcategory", "TSKCTG_NM");
        I18N_CONTENT_TYPE_MAP.put("eventcategory", "EVNTCTG_NM");
        I18N_CONTENT_TYPE_MAP.put("holiday", NotificationMessageParam.HOLIDAY_NAME);
        I18N_CONTENT_TYPE_MAP.put("task", "TSK_NM");
        I18N_CONTENT_TYPE_MAP.put("report", "RPRT_NM");
        I18N_CONTENT_TYPE_MAP.put("master", NotificationMessageParam.MASTER_NAME);
        I18N_CONTENT_TYPE_MAP.put("customfield", "CSTMFLD_NM");
    }

    public final static HashMap<String, String> I18_NOTIFICATION_MAP = new HashMap<>();

    public final static class I18_ENTITY {

        public final static String NOTIFICATION = "NTN";
    }

    static {
        I18_NOTIFICATION_MAP.put("CRT_HLDY", "{{H_NAME}} on {{H_DATE}}");
        I18_NOTIFICATION_MAP.put("RMV_HLDY", "{{H_NAME}} on {{H_DATE}} is removed");
        I18_NOTIFICATION_MAP.put("EDT_HLDY", "{{H_NAME}} on {{H_OLD_DATE}} is now on {{H_DATE}}");
        I18_NOTIFICATION_MAP.put("RMV_HLDY", "{{H_NAME}} on {{H_DATE}} is removed");
        I18_NOTIFICATION_MAP.put("CRT_EMP", "{{EMP_CD}}-{{EMP_NM}} has joined {{FRNCSE_NM}}");
        I18_NOTIFICATION_MAP.put("EDT_EMP", "Details of {{EMP_CD}}-{{EMP_NM}} have been modified");
        I18_NOTIFICATION_MAP.put("NEW_EMP_RPORTNG", "{{EMP_CD}}-{{EMP_NM}} will be reporting to you");
        I18_NOTIFICATION_MAP.put("NEW_EMP_FRST_LGN", "{{LOGIN}}");
        I18_NOTIFICATION_MAP.put("CRT_WRKFLW", "Workflow has been created for {{DPT_NM}}");
        I18_NOTIFICATION_MAP.put("EDT_WRKFLW", "Workflow has been updated for {{DPT_NM}}");
        I18_NOTIFICATION_MAP.put("CRT_EVNT", "{{EVNT_TITLE}} event has been created");
        I18_NOTIFICATION_MAP.put("EDT_EVNT", "{{EVNT_TITLE}} event details have been updated");
        I18_NOTIFICATION_MAP.put("PBLSH_EVNT", "{{EVNT_TITLE}} event starts on {{EVNT_PBLSH_DATE}}");
        I18_NOTIFICATION_MAP.put("EDT_EVNT_REG_FRM", "{{EVNT_TITLE}} registration form has been updated");
        I18_NOTIFICATION_MAP.put("CRT_TSK", "New task has been assigned by {{EMP_CD}}-{{EMP_NM}}");
        I18_NOTIFICATION_MAP.put("EDT_TSK", "Task has been updated by {{EMP_CD}}-{{EMP_NM}}");
        I18_NOTIFICATION_MAP.put("TSK_DUE_DT_CRSD", "Task is still due for {{EMP_CD}}-{{EMP_NM}}");
        I18_NOTIFICATION_MAP.put("TSK_CMPLTD", "Task has been completed by {{EMP_CD}}-{{EMP_NM}}");
        I18_NOTIFICATION_MAP.put("TSK_RMVD", "Task has been removed by {{EMP_CD}}-{{EMP_NM}}");
        I18_NOTIFICATION_MAP.put("TSK_CNCLD", "Task has been cancelled by {{EMP_CD}}-{{EMP_NM}}");
        I18_NOTIFICATION_MAP.put("EDT_ROLE", "{{EDIT}}");
        I18_NOTIFICATION_MAP.put("CRT_SHFT", "{{SHFT_NM}} shift has been created for {{DPT_NM}}");
        I18_NOTIFICATION_MAP.put("CRT_DEFAULT_SHFT", "{{SHFT_NM}} shift has been created");
        I18_NOTIFICATION_MAP.put("EDT_SHFT", "{{SHFT_NM}} shift has been updated");
        I18_NOTIFICATION_MAP.put("EDT_MSTR", "{{MSTR_NM}} master has been updated");
        I18_NOTIFICATION_MAP.put("DELETE_MSTR", "{{MSTR_NM}} master has been deleted");
        I18_NOTIFICATION_MAP.put("EDT_DESIG", "{{DESIG_NM}} designation has been updated");
        I18_NOTIFICATION_MAP.put("PBLSH_SHFT", "From {{SHFT_STRT_DT}} the new shift timings  will be {{SHFT_STRT_TM}}-{{SHFT_END_TM}}");
        I18_NOTIFICATION_MAP.put("LEAVE_APLD", "{{EMP_CD}}-{{EMP_NM}} has applied for a leave of {{LEAVE_DYS}} days starting from {{LEAVE_STRT_DT}}");
        I18_NOTIFICATION_MAP.put("EDT_LEAVE", "{{EMP_CD}}-{{EMP_NM}} has updated leave details");
        I18_NOTIFICATION_MAP.put("CNCL_LEAVE", "{{EMP_CD}}-{{EMP_NM}} has cancelled the leave request");
        I18_NOTIFICATION_MAP.put("LEAVE_RSPNS_MY", "Your leave for {{LEAVE_DYS}} days starting from {{LEAVE_STRT_DT}} has been {{LEAVE_STTS}} by {{APPRVER_NAME}}");
        I18_NOTIFICATION_MAP.put("LEAVE_RSPNS_OTHR", "{{EMP_CD}}-{{EMP_NM}}'s leave of {{LEAVE_DYS}} days starting from {{LEAVE_STRT_DT}} has been {{LEAVE_STTS}} by {{APPRVER_NAME}}");
        I18_NOTIFICATION_MAP.put("ISU_AST_MNGD_EMP", "{{AST_NM}}-{{MDL_NUM}} issued to {{EMP_CD}}-{{EMP_NM}} by {{ASSGNR_ID}}-{{ASSGNR_NM}}");
        I18_NOTIFICATION_MAP.put("ISU_AST_MNGD_DPT", "{{AST_NM}}-{{MDL_NUM}} issued to {{DPT_NM}} by {{ASSGNR_ID}}-{{ASSGNR_NM}}");
        I18_NOTIFICATION_MAP.put("ISU_AST_NON_MNGD_EMP", "{{NO_OF_UNTS}} units of {{AST_NM}} issued to {{EMP_CD}}-{{EMP_NM}} by {{ASSGNR_ID}}-{{ASSGNR_NM}}");
        I18_NOTIFICATION_MAP.put("ISU_AST_NON_MNGD_DPT", "{{NO_OF_UNTS}} units of {{AST_NM}} issued to {{DPT_NM}} by {{ASSGNR_ID}}-{{ASSGNR_NM}}");
        I18_NOTIFICATION_MAP.put("EDT_ASST", "Details of {{AST_NM}} has been modified");
        I18_NOTIFICATION_MAP.put("EDT_ASST", "Details of {{AST_NM}} has been modified");
        I18_NOTIFICATION_MAP.put("ALLT_LOT", "{{LOT_ID}} has been allotted to you");
        I18_NOTIFICATION_MAP.put("ROTATE_SHFT", "Your shift time will be {{SHFT_STRT_TM}}-{{SHFT_END_TM}} from tomorrow");
        I18_NOTIFICATION_MAP.put("NOTIFICATION", "{{NOTIFICATION_DESC}}");

    }

    public static final class I18_LANGUAGE {

        public static final String DEFAULT = "EN";
        public static final String ENGLISH = "EN";
        public static final String GUJARATI = "GU";
        public static final String HINDI = "HI";
    }
    public final static HashMap<String, String> CURRENCY_MASTER_MAP = new HashMap<>();

    static {
        CURRENCY_MASTER_MAP.put("ADP", "Andorra-Andorran Peseta,'₧'");
        CURRENCY_MASTER_MAP.put("AED", "United Arab Emirates-Dirham");
        CURRENCY_MASTER_MAP.put("AFN", "Afghanistan-Afghan Afghani,'؋'");
        CURRENCY_MASTER_MAP.put("ALL", "Albania-Albanian Lek,'Lek'");
        CURRENCY_MASTER_MAP.put("AMD", "Armenia-Armenian Dram,'֏'");
        CURRENCY_MASTER_MAP.put("ANG", "Curaçao-Netherlands Antillean Guilder,'ƒ'");
        CURRENCY_MASTER_MAP.put("AOA", "Angola-Angolan Kwanza,'Kz'");
        CURRENCY_MASTER_MAP.put("ARS", "Argentina-Argentine Peso,'$'");
        CURRENCY_MASTER_MAP.put("ATS", "Austria-Austrian Schilling,'S'");
        CURRENCY_MASTER_MAP.put("AUD", "Australia-Australian Dollar,'$'");
        CURRENCY_MASTER_MAP.put("AWG", "Aruba-Aruban Florin,'ƒ'");
        CURRENCY_MASTER_MAP.put("AYM", "AYM");
        CURRENCY_MASTER_MAP.put("AZN", "Azerbaijan-Manat,'ман'");
        CURRENCY_MASTER_MAP.put("BAM", "Bosnia-Bosnia Convertible Mark,'KM'");
        CURRENCY_MASTER_MAP.put("BBD", "Barbados-Barbadian Dollar,'Bds$'");
        CURRENCY_MASTER_MAP.put("BDT", "Bangladesh-Bangladeshi Taka,'৳'");
        CURRENCY_MASTER_MAP.put("BEF", "Kingdom of Belgium-Belgian Franc,'fr'");
        CURRENCY_MASTER_MAP.put("BGL", "Bulgaria-Bulgarian Hard Lev,'лв'");
        CURRENCY_MASTER_MAP.put("BHD", "Bahrain-Bahraini Dinar");
        CURRENCY_MASTER_MAP.put("BIF", "Burundi-Burundian Franc,'FBu'");
        CURRENCY_MASTER_MAP.put("BMD", "British Overseas Territory of Bermuda-Bermudan Dollar,'$'");
        CURRENCY_MASTER_MAP.put("BND", "The Sultanate of Brunei-Brunei Dollar,'B$'");
        CURRENCY_MASTER_MAP.put("BOB", "Bolivia-Bolivian Boliviano,'Bs'");
        CURRENCY_MASTER_MAP.put("BOV", "Bolivian Mvdol");
        CURRENCY_MASTER_MAP.put("BRL", "Brazil-Brazilian Real,'R$'");
        CURRENCY_MASTER_MAP.put("BSD", "The Bahamas-Bahamian Dollar,'$'");
        CURRENCY_MASTER_MAP.put("BTN", "Bhutan-Bhutanese Ngultrum,'Nu'");
        CURRENCY_MASTER_MAP.put("BWP", "Botswana-Botswanan Pula,'P'");
        CURRENCY_MASTER_MAP.put("BYR", "Belarus-Belarusian Ruble,'Br'");
        CURRENCY_MASTER_MAP.put("BZD", "Belize-Belize Dollar,'BZ$'");
        CURRENCY_MASTER_MAP.put("CAD", "Canada-Canadian Dollar,'$'");
        CURRENCY_MASTER_MAP.put("CDF", "Democratic Republic of the Congo-Congolese Franc,'FC'");
        CURRENCY_MASTER_MAP.put("CHF", "Switzerland and Liechtenstein-Swiss Franc,'CHF'");
        CURRENCY_MASTER_MAP.put("CLF", "Chile-Chilean Unit of Account,'UF'");
        CURRENCY_MASTER_MAP.put("CLP", "Chile-Chilean Peso,'$'");
        CURRENCY_MASTER_MAP.put("CNY", "China-Chinese Yuan,'¥'");
        CURRENCY_MASTER_MAP.put("COP", "Colombia-Colombian Peso,'$'");
        CURRENCY_MASTER_MAP.put("CRC", "Costa Rica-Costa Rican Colón,'₡'");
        CURRENCY_MASTER_MAP.put("CUC", "Cuba-Cuban Convertible Peso,'$'");
        CURRENCY_MASTER_MAP.put("CUP", "Cuba-Cuban Peso,'$MN'");
        CURRENCY_MASTER_MAP.put("CVE", "Cape Verde-Cape Verdean Escudo,'$'");
        CURRENCY_MASTER_MAP.put("CYP", "Cyprus-Cypriot Pound,'£'");
        CURRENCY_MASTER_MAP.put("CZK", "Czech Republic-Czech Republic Koruna,'Kč'");
        CURRENCY_MASTER_MAP.put("DEM", "Germany-German Mark,'DM'");
        CURRENCY_MASTER_MAP.put("DJF", "Djibouti-Djiboutian Franc,'Fdj'");
        CURRENCY_MASTER_MAP.put("DKK", "Denmark and Greenland-Danish Krone,'kr'");
        CURRENCY_MASTER_MAP.put("DOP", "Dominican Republic-Dominican Peso,'RD$'");
        CURRENCY_MASTER_MAP.put("DZD", "Algeria-Algerian Dinar,'DA'");
        CURRENCY_MASTER_MAP.put("EEK", "Estonia-Estonian Kroon,'kr'");
        CURRENCY_MASTER_MAP.put("EGP", "Egypt-Egyptian Pound,'E£'");
        CURRENCY_MASTER_MAP.put("ERN", "Eritrea-Eritrean Nakfa,'Nfk'");
        CURRENCY_MASTER_MAP.put("ESP", "Spain-Spanish Peseta,'₧'");
        CURRENCY_MASTER_MAP.put("ETB", "Ethiopia-Ethiopian Birr,'Br'");
        CURRENCY_MASTER_MAP.put("EUR", "Eurozone-Euro,'€'");
        CURRENCY_MASTER_MAP.put("FIM", "Finland-Finnish Markka,'mk'");
        CURRENCY_MASTER_MAP.put("FJD", "Fiji-Fijian Dollar,'FJ$'");
        CURRENCY_MASTER_MAP.put("FKP", "Falkland Islands-Falkland Islands Pound,'£'");
        CURRENCY_MASTER_MAP.put("FRF", "France-French Franc,'F'");
        CURRENCY_MASTER_MAP.put("GBP", "United Kingdom and Jersey-British Pound Sterling,'£'");
        CURRENCY_MASTER_MAP.put("GEL", "Georgia-Georgian Lari");
        CURRENCY_MASTER_MAP.put("GHS", "Ghana-Ghanaian Cedi,'GH₵'");
        CURRENCY_MASTER_MAP.put("GIP", "Gibraltar-Gibraltar Pound,'£'");
        CURRENCY_MASTER_MAP.put("GMD", "The Gambia-Gambian Dalasi,'D'");
        CURRENCY_MASTER_MAP.put("GNF", "Guinea-Guinean Franc,'FG'");
        CURRENCY_MASTER_MAP.put("GRD", "Greece-Greek Drachma,'₯'");
        CURRENCY_MASTER_MAP.put("GTQ", "Guatemala-Guatemalan Quetzal,'Q'");
        CURRENCY_MASTER_MAP.put("GWP", "Guinea-Bissau-Guinea-Bissau Peso");
        CURRENCY_MASTER_MAP.put("GYD", "Guyana-Guyanaese Dollar,'$'");
        CURRENCY_MASTER_MAP.put("HKD", "Hong Kong-Hong Kong Dollar,'$'");
        CURRENCY_MASTER_MAP.put("HNL", "Honduras-Honduran Lempira,'L'");
        CURRENCY_MASTER_MAP.put("HRK", "Croatia-Croatian Kuna,'kn'");
        CURRENCY_MASTER_MAP.put("HTG", "Haiti-Haitian Gourde,'G'");
        CURRENCY_MASTER_MAP.put("HUF", "Hungary-Hungarian Forint,'Ft'");
        CURRENCY_MASTER_MAP.put("IDR", "Indonesia-Indonesian Rupiah,'Rp'");
        CURRENCY_MASTER_MAP.put("IEP", "Ireland-Irish Pound");
        CURRENCY_MASTER_MAP.put("ILS", "Israel-Israeli New Sheqel,'₪'");
        CURRENCY_MASTER_MAP.put("INR", "India-Indian Rupee,'₹'");
        CURRENCY_MASTER_MAP.put("IQD", "Iraq-Iraqi Dinar");
        CURRENCY_MASTER_MAP.put("IRR", "Iran-Iranian Rial,'﷼'");
        CURRENCY_MASTER_MAP.put("ISK", "Iceland-Icelandic Króna,'kr'");
        CURRENCY_MASTER_MAP.put("ITL", "Italy-Italian Lira,'₤'");
        CURRENCY_MASTER_MAP.put("JMD", "Jamaica-Jamaican Dollar,'J$'");
        CURRENCY_MASTER_MAP.put("JOD", "Jordan-Jordanian Dinar");
        CURRENCY_MASTER_MAP.put("JPY", "Japan-Japanese Yen,'¥'");
        CURRENCY_MASTER_MAP.put("KES", "Kenya-Kenyan Shilling,'KSh'");
        CURRENCY_MASTER_MAP.put("KGS", "Kyrgyz Republic-Kyrgystani Som,'лв'");
        CURRENCY_MASTER_MAP.put("KHR", "Cambodia-Cambodian Riel,'៛'");
        CURRENCY_MASTER_MAP.put("KMF", "Comoros-Comorian Franc,'CF'");
        CURRENCY_MASTER_MAP.put("KPW", "North Korea-North Korean Won,'₩'");
        CURRENCY_MASTER_MAP.put("KRW", "South Korea-South Korean Won,'₩'");
        CURRENCY_MASTER_MAP.put("KWD", "Kuwait-Kuwaiti Dinar");
        CURRENCY_MASTER_MAP.put("KYD", "Cayman Islands-Cayman Islands Dollar,'$'");
        CURRENCY_MASTER_MAP.put("KZT", "Kazakhstan-Kazakhstani Tenge,'₸'");
        CURRENCY_MASTER_MAP.put("LAK", "Laos-Laotian Kip,'₭'");
        CURRENCY_MASTER_MAP.put("LBP", "Lebanon-Lebanese Pound");
        CURRENCY_MASTER_MAP.put("LKR", "Sri Lanka-Sri Lankan Rupee'₨");
        CURRENCY_MASTER_MAP.put("LRD", "Liberia-Liberian Dollar,'L$'");
        CURRENCY_MASTER_MAP.put("LSL", "Lesotho-Lesotho Loti,'L'");
        CURRENCY_MASTER_MAP.put("LTL", "Lithuania-Lithuanian Litas,'Lt'");
        CURRENCY_MASTER_MAP.put("LUF", "Luxembourg-Luxembourgian Franc");
        CURRENCY_MASTER_MAP.put("LVL", "Latvia-Latvian Lats,'Ls'");
        CURRENCY_MASTER_MAP.put("LYD", "Libya-Libyan Dinar,'LD'");
        CURRENCY_MASTER_MAP.put("MAD", "Morocco-Moroccan Dirham");
        CURRENCY_MASTER_MAP.put("MDL", "Moldova-Moldovan Leu");
        CURRENCY_MASTER_MAP.put("MGA", "Madagascar-Malagasy Ariary");
        CURRENCY_MASTER_MAP.put("MGF", "Madagascar-Malagasy Franc");
        CURRENCY_MASTER_MAP.put("MKD", "Republic of Macedonia-Macedonian Denar,'ден'");
        CURRENCY_MASTER_MAP.put("MMK", "Burma-Myanma Kyat,'K'");
        CURRENCY_MASTER_MAP.put("MNT", "Mongolia-Mongolian Tugrik,'₮'");
        CURRENCY_MASTER_MAP.put("MOP", "Macau-Macanese Pataca,'MOP$'");
        CURRENCY_MASTER_MAP.put("MRO", "Mauritania-Mauritanian Ouguiya,'UM'");
        CURRENCY_MASTER_MAP.put("MTL", "Malta-Maltese Lira,'Lm'");
        CURRENCY_MASTER_MAP.put("MUR", "Mauritius-Mauritian Rupee,'₨'");
        CURRENCY_MASTER_MAP.put("MVR", "Maldives-Maldivian Rufiyaa,'Rf'");
        CURRENCY_MASTER_MAP.put("MWK", "Malawi-Malawian Kwacha,'MK'");
        CURRENCY_MASTER_MAP.put("MXN", "Mexico-Mexican Peso,'$'");
        CURRENCY_MASTER_MAP.put("MXV", "Mexican Investment Unit");
        CURRENCY_MASTER_MAP.put("MYR", "Malaysia-Malaysian Ringgit,'RM'");
        CURRENCY_MASTER_MAP.put("MZN", "Mozambique-Mozambican Metical,'MT'");
        CURRENCY_MASTER_MAP.put("NAD", "Namibia-Namibian Dollar,'$'");
        CURRENCY_MASTER_MAP.put("NGN", "Nigerian Naira'₦'");
        CURRENCY_MASTER_MAP.put("NIO", "Nicaragua-Nicaraguan Córdoba,'C$'");
        CURRENCY_MASTER_MAP.put("NLG", "Netherlands-Dutch Guilder,'ƒ'");
        CURRENCY_MASTER_MAP.put("NOK", "Norway-Norwegian Krone,'kr'");
        CURRENCY_MASTER_MAP.put("NPR", "Nepal-Nepalese Rupee,'₨");
        CURRENCY_MASTER_MAP.put("NZD", "New Zealand-New Zealand Dollar,'$'");
        CURRENCY_MASTER_MAP.put("OMR", "Oman-Omani Rial,'﷼'");
        CURRENCY_MASTER_MAP.put("PAB", "Panama-Panamanian Balboa");
        CURRENCY_MASTER_MAP.put("PEN", "Peru-Peruvian Nuevo Sol");
        CURRENCY_MASTER_MAP.put("PGK", "Papua New Guinea-Papua New Guinean Kina,'K'");
        CURRENCY_MASTER_MAP.put("PHP", "Philippines-Philippine Peso,'₱'");
        CURRENCY_MASTER_MAP.put("PKR", "Pakistan-Pakistani Rupee,'₨'");
        CURRENCY_MASTER_MAP.put("PLN", "Poland-Polish Zloty,'zł'");
        CURRENCY_MASTER_MAP.put("PTE", "Portugal-Portuguese Escudo");
        CURRENCY_MASTER_MAP.put("PYG", "Paraguay-Paraguayan Guarani,'Gs'");
        CURRENCY_MASTER_MAP.put("QAR", "Qatar-Qatari Rial,'﷼'");
        CURRENCY_MASTER_MAP.put("RON", "Romania-Romanian Leu");
        CURRENCY_MASTER_MAP.put("RSD", "Serbia-Serbian Dinar");
        CURRENCY_MASTER_MAP.put("RUB", "Russia-Russian Ruble");
        CURRENCY_MASTER_MAP.put("RWF", "Rwanda-Rwandan Franc,'FRw'");
        CURRENCY_MASTER_MAP.put("SAR", "Saudi Arabia-Saudi Riyal,'﷼'");
        CURRENCY_MASTER_MAP.put("SBD", "Solomon Islands-Solomon Islands Dollar,'$'");
        CURRENCY_MASTER_MAP.put("SCR", "Seychelles-Seychellois Rupee,'₨'");
        CURRENCY_MASTER_MAP.put("SDG", "Sudan-Sudanese Pound");
        CURRENCY_MASTER_MAP.put("SEK", "Sweden-Swedish Krona,'kr'");
        CURRENCY_MASTER_MAP.put("SGD", "Singapore-Singapore Dollar,'S$'");
        CURRENCY_MASTER_MAP.put("SHP", "Saint Helena and Ascension-Saint Helena Pound,'£'");
        CURRENCY_MASTER_MAP.put("SIT", "Slovenia-Slovenian Tolar");
        CURRENCY_MASTER_MAP.put("SKK", "Slovakia-Slovak Koruna,'Sk'");
        CURRENCY_MASTER_MAP.put("SLL", "Sierra Leone-Sierra Leonean Leone,'Le'");
        CURRENCY_MASTER_MAP.put("SOS", "Somalia-Somali Shilling,'S'");
        CURRENCY_MASTER_MAP.put("SRD", "Suriname-Surinamese Dollar,'$'");
        CURRENCY_MASTER_MAP.put("SRG", "Suriname-Surinamese Guilder,'ƒ'");
        CURRENCY_MASTER_MAP.put("STD", "São Tomé and Príncipe-São Tomé and Príncipe Dobra,'Db'");
        CURRENCY_MASTER_MAP.put("SVC", "El Salvador-Salvadoran Colón,'$'");
        CURRENCY_MASTER_MAP.put("SYP", "Syria-Syrian Pound,'£'");
        CURRENCY_MASTER_MAP.put("SZL", "Swaziland-Swazi Lilangeni,'L'");
        CURRENCY_MASTER_MAP.put("THB", "Thailand-Thai Baht,'฿'");
        CURRENCY_MASTER_MAP.put("TJS", "Tajikistan-Tajikistani Somoni");
        CURRENCY_MASTER_MAP.put("TMT", "Turkmenistan-Turkmenistan Manat,'T'");
        CURRENCY_MASTER_MAP.put("TND", "Tunisia-Tunisian Dinar");
        CURRENCY_MASTER_MAP.put("TOP", "Tonga-Tongan Paʻanga,'T$'");
        CURRENCY_MASTER_MAP.put("TPE", "Portuguese Timor-Timorese Escudo");
        CURRENCY_MASTER_MAP.put("TRY", "Turkey-Turkish Lira,'₤'");
        CURRENCY_MASTER_MAP.put("TTD", "Trinidad and Tobago-Trinidad and Tobago Dollar,'TT$'");
        CURRENCY_MASTER_MAP.put("TWD", "Taiwan-New Taiwan Dollar,'NT$'");
        CURRENCY_MASTER_MAP.put("TZS", "Tanzania-Tanzanian Shilling,'x/y'");
        CURRENCY_MASTER_MAP.put("UAH", "Ukraine-Ukrainian Hryvnia,'₴'");
        CURRENCY_MASTER_MAP.put("UGX", "Uganda-Ugandan Shilling,'USh'");
        CURRENCY_MASTER_MAP.put("USD", "United States-US Dollar,'$'");
        CURRENCY_MASTER_MAP.put("USN", "US Dollar (Next day)");
        CURRENCY_MASTER_MAP.put("USS", "US Dollar (Same day)");
        CURRENCY_MASTER_MAP.put("UYU", "Uruguay-Uruguayan Peso,'$U'");
        CURRENCY_MASTER_MAP.put("UZS", "Uzbekistan-Uzbekistan Som,'лв'");
        CURRENCY_MASTER_MAP.put("VEF", "Venezuela-Venezuelan Bolívar,'Bs'");
        CURRENCY_MASTER_MAP.put("VND", "Vietnam-Vietnamese Dong,'₫'");
        CURRENCY_MASTER_MAP.put("VUV", "Vanuatu-Vanuatu Vatu,'VT'");
        CURRENCY_MASTER_MAP.put("WST", "Samoa-Samoan Tala,'WS$'");
        CURRENCY_MASTER_MAP.put("XAF", "CFA Franc BEAC");
        CURRENCY_MASTER_MAP.put("XAG", "Silver");
        CURRENCY_MASTER_MAP.put("XAU", "Gold");
        CURRENCY_MASTER_MAP.put("XBA", "European Composite Unit");
        CURRENCY_MASTER_MAP.put("XBB", "European Monetary Unit");
        CURRENCY_MASTER_MAP.put("XBC", "European Unit of Account (XBC)");
        CURRENCY_MASTER_MAP.put("XBD", "European Unit of Account (XBD)");
        CURRENCY_MASTER_MAP.put("XCD", "Organisation of Eastern Caribbean States-East Caribbean Dollar,'$'");
        CURRENCY_MASTER_MAP.put("XDR", "Special Drawing Rights");
        CURRENCY_MASTER_MAP.put("XFO", "French Gold Franc");
        CURRENCY_MASTER_MAP.put("XFU", "French UIC-Franc");
        CURRENCY_MASTER_MAP.put("XOF", "CFA Franc BCEAO");
        CURRENCY_MASTER_MAP.put("XPD", "Palladium");
        CURRENCY_MASTER_MAP.put("XPF", "CFP Franc");
        CURRENCY_MASTER_MAP.put("XPT", "Platinum");
        CURRENCY_MASTER_MAP.put("XSU", "XSU");
        CURRENCY_MASTER_MAP.put("XTS", "Testing Currency Code");
        CURRENCY_MASTER_MAP.put("XUA", "XUA");
        CURRENCY_MASTER_MAP.put("XXX", "Unknown Currency");
        CURRENCY_MASTER_MAP.put("YER", "Yemen-Yemeni Rial,'﷼'");
        CURRENCY_MASTER_MAP.put("ZAR", "South Africa-South African Rand,'S'");
        CURRENCY_MASTER_MAP.put("ZMK", "Zambia-Zambian Kwacha,'Z$'");
        CURRENCY_MASTER_MAP.put("ZWN", "ZWN");
    }
    public final static HashMap<String, String> CURRENCY_CODE_MAP = new HashMap<>();

    static {
        CURRENCY_CODE_MAP.put("ADP", "ADP(₧)");
        CURRENCY_CODE_MAP.put("AED", "AED");
        CURRENCY_CODE_MAP.put("AFN", "AFN(؋)");
        CURRENCY_CODE_MAP.put("ALL", "ALL(Lek)");
        CURRENCY_CODE_MAP.put("AMD", "AMD(֏)");
        CURRENCY_CODE_MAP.put("ANG", "ANG(ƒ)");
        CURRENCY_CODE_MAP.put("AOA", "AOA(Kz)");
        CURRENCY_CODE_MAP.put("ARS", "ARS($)");
        CURRENCY_CODE_MAP.put("ATS", "ATS(S)");
        CURRENCY_CODE_MAP.put("AUD", "AUD($)");
        CURRENCY_CODE_MAP.put("AWG", "AWG(ƒ)");
        CURRENCY_CODE_MAP.put("AYM", "AYM(AYM)");
        CURRENCY_CODE_MAP.put("AZN", "AZN(ман'");
        CURRENCY_CODE_MAP.put("BAM", "BAM(KM)");
        CURRENCY_CODE_MAP.put("BBD", "BBD(Bds$)");
        CURRENCY_CODE_MAP.put("BDT", "BDT(৳)");
        CURRENCY_CODE_MAP.put("BEF", "BEF(fr)");
        CURRENCY_CODE_MAP.put("BGL", "BGL(лв)");
        CURRENCY_CODE_MAP.put("BGN", "BGN(лв.)");
        CURRENCY_CODE_MAP.put("BHD", "BHD(.د.ب )");
        CURRENCY_CODE_MAP.put("BIF", "BIF(FBu)");
        CURRENCY_CODE_MAP.put("BMD", "BMD($)");
        CURRENCY_CODE_MAP.put("BND", "BND(B$)");
        CURRENCY_CODE_MAP.put("BOB", "BOB(Bs)");
        CURRENCY_CODE_MAP.put("BOV", "BOV");
        CURRENCY_CODE_MAP.put("BRL", "BRL(R$)");
        CURRENCY_CODE_MAP.put("BSD", "BSD($)");
        CURRENCY_CODE_MAP.put("BTN", "BTN(Nu)");
        CURRENCY_CODE_MAP.put("BWP", "BWP(P)");
        CURRENCY_CODE_MAP.put("BYR", "BYR(Br)");
        CURRENCY_CODE_MAP.put("BZD", "BZD(BZ$)");
        CURRENCY_CODE_MAP.put("CAD", "CAD($)");
        CURRENCY_CODE_MAP.put("CDF", "CDF(FC)");
        CURRENCY_CODE_MAP.put("CHF", "CHF(CHF)");
        CURRENCY_CODE_MAP.put("CLF", "CLF(UF)");
        CURRENCY_CODE_MAP.put("CLP", "CLP($)");
        CURRENCY_CODE_MAP.put("CNY", "CNY(¥)");
        CURRENCY_CODE_MAP.put("COP", "COP($)");
        CURRENCY_CODE_MAP.put("CRC", "CRC(₡)");
        CURRENCY_CODE_MAP.put("CUC", "CUC($)");
        CURRENCY_CODE_MAP.put("CUP", "CUP($MN)");
        CURRENCY_CODE_MAP.put("CVE", "CVE($)");
        CURRENCY_CODE_MAP.put("CYP", "CYP(£)");
        CURRENCY_CODE_MAP.put("CZK", "CZK(Kč)");
        CURRENCY_CODE_MAP.put("DEM", "DEM(DM)");
        CURRENCY_CODE_MAP.put("DJF", "DJF(Fdj)");
        CURRENCY_CODE_MAP.put("DKK", "DKK(kr)");
        CURRENCY_CODE_MAP.put("DOP", "DOP(RD$)");
        CURRENCY_CODE_MAP.put("DZD", "DZD(DA)");
        CURRENCY_CODE_MAP.put("EEK", "EEK(kr)");
        CURRENCY_CODE_MAP.put("EGP", "EGP(E£)");
        CURRENCY_CODE_MAP.put("ERN", "ERN(Nfk)");
        CURRENCY_CODE_MAP.put("ESP", "ESP(₧)");
        CURRENCY_CODE_MAP.put("ETB", "ETB(Br)");
        CURRENCY_CODE_MAP.put("EUR", "EUR(€)");
        CURRENCY_CODE_MAP.put("FIM", "FIM(mk)");
        CURRENCY_CODE_MAP.put("FJD", "FJD(FJ$)");
        CURRENCY_CODE_MAP.put("FKP", "FKP(£)");
        CURRENCY_CODE_MAP.put("FRF", "FRF(F)");
        CURRENCY_CODE_MAP.put("GBP", "GBP(£)");
        CURRENCY_CODE_MAP.put("GEL", "GEL(GEL)");
        CURRENCY_CODE_MAP.put("GHS", "GHS(GH₵)");
        CURRENCY_CODE_MAP.put("GIP", "GIP(£)");
        CURRENCY_CODE_MAP.put("GMD", "GMD(D)");
        CURRENCY_CODE_MAP.put("GNF", "GNF(FG)");
        CURRENCY_CODE_MAP.put("GRD", "GRD(₯)");
        CURRENCY_CODE_MAP.put("GTQ", "GTQ(Q)");
        CURRENCY_CODE_MAP.put("GWP", "GWP(GWP)");
        CURRENCY_CODE_MAP.put("GYD", "GYD($)");
        CURRENCY_CODE_MAP.put("HKD", "HKD($)");
        CURRENCY_CODE_MAP.put("HNL", "HNL(L)");
        CURRENCY_CODE_MAP.put("HRK", "HRK(kn)");
        CURRENCY_CODE_MAP.put("HTG", "HTG(G)");
        CURRENCY_CODE_MAP.put("HUF", "HUF(Ft)");
        CURRENCY_CODE_MAP.put("IDR", "IDR(Rp)");
        CURRENCY_CODE_MAP.put("IEP", "IEP(IEP)");
        CURRENCY_CODE_MAP.put("ILS", "ILS(₪)");
        CURRENCY_CODE_MAP.put("INR", "INR(₹)");
        CURRENCY_CODE_MAP.put("IQD", "IQD(ع.د)");
        CURRENCY_CODE_MAP.put("IQD", "IQD(IQD)");
        CURRENCY_CODE_MAP.put("IRR", "IRR(﷼)");
        CURRENCY_CODE_MAP.put("ISK", "ISK(kr)");
        CURRENCY_CODE_MAP.put("ITL", "ITL(₤)");
        CURRENCY_CODE_MAP.put("JMD", "JMD(J$)");
        CURRENCY_CODE_MAP.put("JOD", "JOD(JOD)");
        CURRENCY_CODE_MAP.put("JPY", "JPY(¥)");
        CURRENCY_CODE_MAP.put("KES", "KES(KSh)");
        CURRENCY_CODE_MAP.put("KGS", "KGS(лв)");
        CURRENCY_CODE_MAP.put("KHR", "KHR(៛)");
        CURRENCY_CODE_MAP.put("KMF", "KMF(CF)");
        CURRENCY_CODE_MAP.put("KPW", "KPW(₩)");
        CURRENCY_CODE_MAP.put("KRW", "KRW(₩)");
        CURRENCY_CODE_MAP.put("KWD", "KWD(K.D.)");
        CURRENCY_CODE_MAP.put("KYD", "KYD($)");
        CURRENCY_CODE_MAP.put("KZT", "KZT(₸)");
        CURRENCY_CODE_MAP.put("LAK", "LAK(₭)");
        CURRENCY_CODE_MAP.put("LBP", "LBP(LBP)");
        CURRENCY_CODE_MAP.put("LKR", "LKR(₨)");
        CURRENCY_CODE_MAP.put("LRD", "LRD(L$)");
        CURRENCY_CODE_MAP.put("LSL", "LSL(L)");
        CURRENCY_CODE_MAP.put("LTL", "LTL(Lt)");
        CURRENCY_CODE_MAP.put("LUF", "LUF(LUF)");
        CURRENCY_CODE_MAP.put("LVL", "LVL(Ls)");
        CURRENCY_CODE_MAP.put("LYD", "LYD(LD)");
        CURRENCY_CODE_MAP.put("MAD", "MAD(د.م.)");
        CURRENCY_CODE_MAP.put("MDL", "MDL(MDL)");
        CURRENCY_CODE_MAP.put("MGA", "MGA(MGA)");
        CURRENCY_CODE_MAP.put("MGF", "MGF(MGF)");
        CURRENCY_CODE_MAP.put("MKD", "MKD(ден)");
        CURRENCY_CODE_MAP.put("MMK", "MMK(K)");
        CURRENCY_CODE_MAP.put("MNT", "MNT(₮)");
        CURRENCY_CODE_MAP.put("MOP", "MOP(MOP$)");
        CURRENCY_CODE_MAP.put("MRO", "MRO(UM)");
        CURRENCY_CODE_MAP.put("MTL", "MTL(Lm)");
        CURRENCY_CODE_MAP.put("MUR", "MUR(₨)");
        CURRENCY_CODE_MAP.put("MVR", "MVR(Rf)");
        CURRENCY_CODE_MAP.put("MWK", "MWK(MK)");
        CURRENCY_CODE_MAP.put("MXN", "MXN($)");
        CURRENCY_CODE_MAP.put("MXV", "MXV(MXV)");
        CURRENCY_CODE_MAP.put("MYR", "MYR(RM)");
        CURRENCY_CODE_MAP.put("MZN", "MZN(MT)");
        CURRENCY_CODE_MAP.put("NAD", "NAD($)");
        CURRENCY_CODE_MAP.put("NGN", "NGN(₦)");
        CURRENCY_CODE_MAP.put("NIO", "NIO(C$)");
        CURRENCY_CODE_MAP.put("NLG", "NLG(ƒ)");
        CURRENCY_CODE_MAP.put("NOK", "NOK(kr)");
        CURRENCY_CODE_MAP.put("NPR", "NPR(₨)");
        CURRENCY_CODE_MAP.put("NZD", "NZD($)");
        CURRENCY_CODE_MAP.put("OMR", "OMR(﷼)");
        CURRENCY_CODE_MAP.put("PAB", "PAB(B/.)");
        CURRENCY_CODE_MAP.put("PEN", "PEN(S/.)");
        CURRENCY_CODE_MAP.put("PGK", "PGK(K)");
        CURRENCY_CODE_MAP.put("PHP", "PHP(₱)");
        CURRENCY_CODE_MAP.put("PKR", "PKR(₨)");
        CURRENCY_CODE_MAP.put("PLN", "PLN(zł)");
        CURRENCY_CODE_MAP.put("PTE", "PTE(PTE)");
        CURRENCY_CODE_MAP.put("PYG", "PYG(Gs)");
        CURRENCY_CODE_MAP.put("QAR", "QAR(﷼)");
        CURRENCY_CODE_MAP.put("RON", "RON(RON)");
        CURRENCY_CODE_MAP.put("RSD", "RSD(Дин.)");
        CURRENCY_CODE_MAP.put("RUB", "RUB(RUB)");
        CURRENCY_CODE_MAP.put("RWF", "RWF(FRw)");
        CURRENCY_CODE_MAP.put("SAR", "SAR(﷼)");
        CURRENCY_CODE_MAP.put("SBD", "SBD($)");
        CURRENCY_CODE_MAP.put("SCR", "SCR(₨)");
        CURRENCY_CODE_MAP.put("SDG", "SDG(ج.س.)");
        CURRENCY_CODE_MAP.put("SEK", "SEK(kr)");
        CURRENCY_CODE_MAP.put("SGD", "SGD(S$)");
        CURRENCY_CODE_MAP.put("SHP", "SHP(£)");
        CURRENCY_CODE_MAP.put("SIT", "SIT(SIT)");
        CURRENCY_CODE_MAP.put("SKK", "SKK(Sk)");
        CURRENCY_CODE_MAP.put("SLL", "SLL(Le)");
        CURRENCY_CODE_MAP.put("SOS", "SOS(S)");
        CURRENCY_CODE_MAP.put("SRD", "SRD($)");
        CURRENCY_CODE_MAP.put("SRG", "SRG(ƒ)");
        CURRENCY_CODE_MAP.put("STD", "STD(Db)");
        CURRENCY_CODE_MAP.put("SVC", "SVC($)");
        CURRENCY_CODE_MAP.put("SYP", "SYP(£)");
        CURRENCY_CODE_MAP.put("SZL", "SZL(L)");
        CURRENCY_CODE_MAP.put("THB", "THB(฿)");
        CURRENCY_CODE_MAP.put("TJS", "TJS(TJS)");
        CURRENCY_CODE_MAP.put("TMT", "TMT(T)");
        CURRENCY_CODE_MAP.put("TND", "TND(د.ت)");
        CURRENCY_CODE_MAP.put("TOP", "TOP(T$)");
        CURRENCY_CODE_MAP.put("TPE", "TPE(TPE)");
        CURRENCY_CODE_MAP.put("TRY", "TRY(₤)");
        CURRENCY_CODE_MAP.put("TTD", "TTD(TT$)");
        CURRENCY_CODE_MAP.put("TWD", "TWD(NT$)");
        CURRENCY_CODE_MAP.put("TZS", "TZS(x/y)");
        CURRENCY_CODE_MAP.put("UAH", "UAH(₴)");
        CURRENCY_CODE_MAP.put("UGX", "UGX(USh)");
        CURRENCY_CODE_MAP.put("USD", "USD($)");
        CURRENCY_CODE_MAP.put("USN", "USN");
        CURRENCY_CODE_MAP.put("USS", "USS");
        CURRENCY_CODE_MAP.put("UYU", "UYU($U)");
        CURRENCY_CODE_MAP.put("UZS", "UZS(лв)");
        CURRENCY_CODE_MAP.put("VEF", "VEF(Bs)");
        CURRENCY_CODE_MAP.put("VND", "VND(₫)");
        CURRENCY_CODE_MAP.put("VUV", "VUV(VT)");
        CURRENCY_CODE_MAP.put("WST", "WST(WS$)");
        CURRENCY_CODE_MAP.put("XAF", "XAF(XAF)");
        CURRENCY_CODE_MAP.put("XAG", "XAG(XAG)");
        CURRENCY_CODE_MAP.put("XAU", "XAU(XAU)");
        CURRENCY_CODE_MAP.put("XBA", "XBA(XBA)");
        CURRENCY_CODE_MAP.put("XBB", "XBB(XBB)");
        CURRENCY_CODE_MAP.put("XBC", "XBC(XBC)");
        CURRENCY_CODE_MAP.put("XBD", "XBD(XBD)");
        CURRENCY_CODE_MAP.put("XCD", "XCD($)");
        CURRENCY_CODE_MAP.put("XDR", "XDR(XDR)");
        CURRENCY_CODE_MAP.put("XFO", "XFO(XFO)");
        CURRENCY_CODE_MAP.put("XFU", "XFU(XFU)");
        CURRENCY_CODE_MAP.put("XOF", "XOF(XOF)");
        CURRENCY_CODE_MAP.put("XPD", "XPD(XPD)");
        CURRENCY_CODE_MAP.put("XPF", "XPF(XPF)");
        CURRENCY_CODE_MAP.put("XPT", "XPT(XPT)");
        CURRENCY_CODE_MAP.put("XSU", "XSU(XSU)");
        CURRENCY_CODE_MAP.put("XTS", "XTS(XTS)");
        CURRENCY_CODE_MAP.put("XUA", "XUA(XUA)");
        CURRENCY_CODE_MAP.put("XXX", "XXX(XXX)");
        CURRENCY_CODE_MAP.put("YER", "YER(﷼)");
        CURRENCY_CODE_MAP.put("ZAR", "ZAR(S)");
        CURRENCY_CODE_MAP.put("ZMK", "ZMK(Z$)");
        CURRENCY_CODE_MAP.put("ZWN", "ZWN(ZWN)");
    }
    public final static Map<String, String> operatorMap = new HashMap<>();
    public final static Map<String, String> ruleOperatorMap = new HashMap<>();

    public static final class RULE_OPERATOR {

        public final static String EQUALS_TO = "equals to";
        public final static String NOT_EQUAL_TO = "not equal to";
        public final static String LESS_THAN_ = "less than";
        public final static String GREATER_THAN = "greater than";
        public final static String LESS_THAN_EQUAL_TO = "less than equal to";
        public final static String GREATER_THAN_EQUAL_TO = "greater than equal to";
        public final static String HAS_NO_VALUE = "has no value";
        public final static String HAS_ANY_VALUE = "has any value";
        public final static String HAS_ANY_VALUE_FROM = "has any value from";
        public final static String HAS_ALL_VALUES = "has all value";
        public final static String IN_BETWEEN = "in between";
        public final static String NOT_IN_BETWEEN = "not in between";
        public final static String IS_BEFORE = "is before";
        public final static String IS_AFTER = "is after";
        public final static String HAS_PASSED = "has passed";
        public final static String HAS_NOT_PASSED = "has not passed";
        public final static String IS_IN_NEXT = "is in next";
        public final static String HAS_PASSED_SINCE = "has passed since";
        public final static String LIKE = "like";
        public final static String IS_ABSENT_TODAY = "IsAbsentToday";
        public final static String RESTRICT_BY_IP = "RestrictByIP";
        public final static String ABSENT_WITHOUT_INFO = "AbsentWithoutInfo";
    }

    static {
        operatorMap.put(RULE_OPERATOR.EQUALS_TO, "equals to");
        operatorMap.put(RULE_OPERATOR.LESS_THAN_, "less than");
        operatorMap.put(RULE_OPERATOR.GREATER_THAN, "greater than");
        operatorMap.put(RULE_OPERATOR.GREATER_THAN_EQUAL_TO, "greater than equal to");
        operatorMap.put(RULE_OPERATOR.LESS_THAN_EQUAL_TO, "less than equal to");
        operatorMap.put(RULE_OPERATOR.NOT_EQUAL_TO, "not equal to");
        operatorMap.put(RULE_OPERATOR.HAS_NO_VALUE, "has no value");
        operatorMap.put(RULE_OPERATOR.HAS_ANY_VALUE, "has any value");
        operatorMap.put(RULE_OPERATOR.IN_BETWEEN, "in between");
        operatorMap.put(RULE_OPERATOR.HAS_PASSED, "has passed");
        operatorMap.put(RULE_OPERATOR.HAS_NOT_PASSED, "has not passed");
        operatorMap.put(RULE_OPERATOR.IS_IN_NEXT, "is in next");
        operatorMap.put(RULE_OPERATOR.HAS_PASSED_SINCE, "has passed since");
        operatorMap.put(RULE_OPERATOR.LIKE, "like");
    }

    static {
        ruleOperatorMap.put(RULE_OPERATOR.EQUALS_TO, "=");
        ruleOperatorMap.put(RULE_OPERATOR.LESS_THAN_, "lt");
        ruleOperatorMap.put(RULE_OPERATOR.GREATER_THAN, "gt");
        ruleOperatorMap.put(RULE_OPERATOR.GREATER_THAN_EQUAL_TO, "gte");
        ruleOperatorMap.put(RULE_OPERATOR.LESS_THAN_EQUAL_TO, "lte");
        ruleOperatorMap.put(RULE_OPERATOR.NOT_EQUAL_TO, "ne");
        ruleOperatorMap.put(RULE_OPERATOR.HAS_NO_VALUE, "hnv");
        ruleOperatorMap.put(RULE_OPERATOR.HAS_ANY_VALUE, "hav");
        ruleOperatorMap.put(RULE_OPERATOR.IN_BETWEEN, "ib");
        ruleOperatorMap.put(RULE_OPERATOR.NOT_IN_BETWEEN, "nib");
        ruleOperatorMap.put(RULE_OPERATOR.IS_BEFORE, "ibefore");
        ruleOperatorMap.put(RULE_OPERATOR.IS_AFTER, "iafter");
        ruleOperatorMap.put(RULE_OPERATOR.HAS_ANY_VALUE_FROM, "in");
        ruleOperatorMap.put(RULE_OPERATOR.IS_ABSENT_TODAY, "iat");
        ruleOperatorMap.put(RULE_OPERATOR.RESTRICT_BY_IP, "rbi");
        ruleOperatorMap.put(RULE_OPERATOR.ABSENT_WITHOUT_INFO, "awi");
    }

    public final static Map<String, String> featureDocumentMap = new HashMap<>();

    static {
        featureDocumentMap.put(Feature.MANAGE_ASSET, "asset");
        featureDocumentMap.put(Feature.MANAGE_DEPARTMENT, "department");
        featureDocumentMap.put(Feature.MANAGE_EMPLOYEE, "user");
        featureDocumentMap.put(Feature.MANAGE_EVENT, "event");
        featureDocumentMap.put(Feature.MANAGE_FRANCHISE, "franchise");
        featureDocumentMap.put(Feature.MANAGE_HOLIDAY, "holiday");
        featureDocumentMap.put(Feature.MANAGE_LEAVE_WORKFLOW, "leaveworkflow");
        featureDocumentMap.put(Feature.MANAGE_SHIFT, "shift");
        featureDocumentMap.put(Feature.MANAGE_TASK, "task");
        featureDocumentMap.put(Feature.MANAGE_APPLY_LEAVE, "leave");
        featureDocumentMap.put(Feature.MANAGE_RESPOND_LEAVE, "respondLeave");
        featureDocumentMap.put(Feature.MANAGE_MESSAGE, "message");
        featureDocumentMap.put(Feature.INVOICE, "invoice");
        featureDocumentMap.put(Feature.PARCEL, "parcel");
        featureDocumentMap.put(Feature.LOT, "lot");
        featureDocumentMap.put(Feature.PACKET, "packet");
        featureDocumentMap.put(Feature.SELL, "sell");
        featureDocumentMap.put(Feature.TRANSFER, "transfer");
        featureDocumentMap.put(Feature.ISSUE, "issue");
        featureDocumentMap.put(Feature.PLAN, "plan");

    }

    public final static class Section {

        public final static String ISSUE_ASSET = "issueAsset";
        public final static String CATEGORY = "category";
        public final static String POLICY = "POLICY";
        public final static String PERSONAL = "PERSONAL";
        public final static String IDENTIFICATION = "IDENTIFICATION";
        public final static String CONTACT = "CONTACT";
        public final static String OTHER = "OTHER";
        public final static String EDUCATION = "EDUCATION";
        public final static String HKGWORK = "HKGWORK";
        public final static String EXPERIENCE = "EXPERIENCE";
        public final static String FAMILY = "FAMILY";
        public final static String REGISTRATION = "registration";
        public final static String INVITATIONCARD = "invitationcard";
        public final static String GENERAL_SECTION_DISPLAY = "General Section";
    }
    public final static Map<String, String> sectionDocumentMap = new HashMap<>();

    static {
        sectionDocumentMap.put(Section.ISSUE_ASSET, "issueasset");
        sectionDocumentMap.put(Section.CATEGORY, "category");
        sectionDocumentMap.put(Section.POLICY, "user");
        sectionDocumentMap.put(Section.PERSONAL, "user");
        sectionDocumentMap.put(Section.IDENTIFICATION, "user");
        sectionDocumentMap.put(Section.CONTACT, "user");
        sectionDocumentMap.put(Section.OTHER, "user");
        sectionDocumentMap.put(Section.EDUCATION, "user");
        sectionDocumentMap.put(Section.HKGWORK, "user");
        sectionDocumentMap.put(Section.EXPERIENCE, "user");
        sectionDocumentMap.put(Section.FAMILY, "user");
        sectionDocumentMap.put(Section.REGISTRATION, "event");
        sectionDocumentMap.put(Section.INVITATIONCARD, "event");

    }

    public final static class AutoNumber {

        public final static String INVOICE_ID = "invoiceId$AG$String";
        public final static String PARCEL_ID = "parcelId$AG$String";
        public final static String LOT_ID = "lotID$AG$String";
        public final static String PACKET_ID = "packetID$AG$String";
        public final static String LOT_SLIP_ID = "lotSlipID$AG$String";
        public final static String PACKET_SLIP_ID = "packetSlipID$AG$String";
        public final static String SELL_ID = "sellID$AG$String";
        public final static String TRANSFER_ID = "transferID$AG$String";
        public final static String PLAN_ID = "planID$AG$String";
        public final static String ROUGH_ID = "roughPurchaseID$AG$String";
    }

    public final static Map<String, String> operatorSymbolMap = new HashMap<>();

    static {
        operatorSymbolMap.put("=", RULE_OPERATOR.EQUALS_TO);
        operatorSymbolMap.put("<", RULE_OPERATOR.LESS_THAN_);
        operatorSymbolMap.put(">", RULE_OPERATOR.GREATER_THAN);
        operatorSymbolMap.put(">=", RULE_OPERATOR.GREATER_THAN_EQUAL_TO);
        operatorSymbolMap.put("<=", RULE_OPERATOR.LESS_THAN_EQUAL_TO);
        operatorSymbolMap.put("!=", RULE_OPERATOR.NOT_EQUAL_TO);
        operatorSymbolMap.put("not in", RULE_OPERATOR.HAS_NO_VALUE);
        operatorSymbolMap.put("in", RULE_OPERATOR.HAS_ANY_VALUE);
        operatorSymbolMap.put("between", RULE_OPERATOR.IN_BETWEEN);
        operatorSymbolMap.put("like", RULE_OPERATOR.LIKE);

    }

    public final static class StockStatus {

        public final static String NEW_ROUGH = "New/Rough";
        public final static String FINISHED = "Finished";
        public final static String REJECTED = "Rejected";
        public final static String SOLD = "Sold";
        public final static String PARTIALLY_SOLD = "Partially Sold";
        public final static String TRANSFERRED = "Transferred";
        public final static String TERMINATED = "Terminated";
        public final static String MERGED = "Merged";
        public final static String PARTIALLY_MERGED = "Partially Merged";
        public final static String PARTIAL_LOT_ADDED = "Partial Lot Added";
        public final static String SPLITTED = "Splitted";
        public final static String IN_PRODUCTION = "In Production";
        public final static String COMPLETED = "Completed";
        public final static String ARCHIVED = "Archived";
        public final static String PENDING = "Pending";
    }

    public final static Map<String, String> lotStatusMap = new HashMap<>();

    static {
        lotStatusMap.put(StockStatus.NEW_ROUGH, StockStatus.NEW_ROUGH);
        lotStatusMap.put(StockStatus.FINISHED, StockStatus.FINISHED);
        lotStatusMap.put(StockStatus.REJECTED, StockStatus.REJECTED);
        lotStatusMap.put(StockStatus.SOLD, StockStatus.SOLD);
        lotStatusMap.put(StockStatus.TRANSFERRED, StockStatus.TRANSFERRED);
        lotStatusMap.put(StockStatus.TERMINATED, StockStatus.TERMINATED);
        lotStatusMap.put(StockStatus.MERGED, StockStatus.MERGED);
        lotStatusMap.put(StockStatus.SPLITTED, StockStatus.SPLITTED);
        lotStatusMap.put(StockStatus.IN_PRODUCTION, StockStatus.IN_PRODUCTION);
    }
    public final static Map<String, String> invoiceAndParceStatusMap = new HashMap<>();

    static {
        invoiceAndParceStatusMap.put(StockStatus.NEW_ROUGH, StockStatus.NEW_ROUGH);
        invoiceAndParceStatusMap.put(StockStatus.FINISHED, StockStatus.FINISHED);

    }

    public final static class ActivityServiceStatus {

        public final static String IN_PROCESS = "In Process";
        public final static String PARTIAL_COMPLETED = "Partial Completed";
        public final static String COMPLETED = "Completed";
        public final static String TERMINATED = "Terminated";
        public final static String IN_TRANSIT = "In-Transit";
    }

    public final static Map<Long, String> activityServiceSymbolMap = new HashMap<>();

    static {
        activityServiceSymbolMap.put(-1L, ActivityServiceStatus.IN_PROCESS);
        activityServiceSymbolMap.put(-2L, ActivityServiceStatus.COMPLETED);
        activityServiceSymbolMap.put(-3L, ActivityServiceStatus.TERMINATED);

    }

    public final static Map<String, Long> activityServiceSymbolMapReverse = new HashMap<>();

    static {
        activityServiceSymbolMapReverse.put(ActivityServiceStatus.IN_PROCESS, -1L);
        activityServiceSymbolMapReverse.put(ActivityServiceStatus.COMPLETED, -2L);
        activityServiceSymbolMapReverse.put(ActivityServiceStatus.TERMINATED, -3L);
        activityServiceSymbolMapReverse.put(ActivityServiceStatus.PARTIAL_COMPLETED, -1L);

    }

    public static class FeatureMenuLabel {

        public static final String ASSET = "Assets";
        public static final String DEPARTMENT = "Department";
        public static final String LOT = "Lot";
        public static final String EVENT = "Events";
        public static final String EMPLOYEE = "Employees";
        public static final String INVOICE = "Invoice";
        public static final String TASK = "Tasks";
        public static final String COATED_ROUGH = "Coated Rough";
        public static final String LEAVE = "Leave";
        public static final String ISSUE = "Issue";
        public static final String PACKET = "Packet";
        public static final String DIAMOND = "Diamond";
        public static final String PARCEL = "Parcel";
        public static final String PLAN = "Plan";
        public static final String FRANCHISE = "Franchise";
        public static final String HOLIDAY = "Holiday";
        public static final String DESIGNATION = "Designation";
        public static final String LEAVE_WORKFLOW = "Leave Workflow";
        public static final String LOCALES = "Locales";
        public static final String MASTERS = "Masters";
        public static final String MESSAGES = "Messages";
        public static final String NOTIFICATION = "Notifications";
        public static final String SHIFT = "Shift";
        public static final String CUSTOM_FIELD = "Custom Field";
        public static final String APPLY_LEAVE = "Apply Leave";
        public static final String LOCATION = "Location";
        public static final String ISSUE_ASSET = "Issue Asset";
        public static final String CATEGORY = "Category";
        public static final String RULE = "Rule";
        public static final String REPORT = "Report";
    }

    public final static Map<String, String> ActualFeatureCustomFieldFeatureMap = new HashMap<>();

    static {
        ActualFeatureCustomFieldFeatureMap.put(FeatureMenuLabel.ASSET, "ASSET");
        ActualFeatureCustomFieldFeatureMap.put(FeatureMenuLabel.DEPARTMENT, "DEPARTMENT");
        ActualFeatureCustomFieldFeatureMap.put(FeatureMenuLabel.EMPLOYEE, "USER");
        ActualFeatureCustomFieldFeatureMap.put(FeatureMenuLabel.EVENT, "EVENT");
        ActualFeatureCustomFieldFeatureMap.put(FeatureMenuLabel.FRANCHISE, "FRANCHISE");
        ActualFeatureCustomFieldFeatureMap.put(FeatureMenuLabel.HOLIDAY, "HOLIDAY");
        ActualFeatureCustomFieldFeatureMap.put(FeatureMenuLabel.LEAVE_WORKFLOW, "LEAVEWORKFLOW");
        ActualFeatureCustomFieldFeatureMap.put(FeatureMenuLabel.SHIFT, "SHIFT");
        ActualFeatureCustomFieldFeatureMap.put(FeatureMenuLabel.TASK, "TASK");
        ActualFeatureCustomFieldFeatureMap.put(FeatureMenuLabel.APPLY_LEAVE, "LEAVE");
        ActualFeatureCustomFieldFeatureMap.put(FeatureMenuLabel.LEAVE, "RESPONDLEAVE");
        ActualFeatureCustomFieldFeatureMap.put(FeatureMenuLabel.CATEGORY, "CATEGORY");
        ActualFeatureCustomFieldFeatureMap.put(FeatureMenuLabel.ISSUE_ASSET, "ISSUEASSET");
        ActualFeatureCustomFieldFeatureMap.put(FeatureMenuLabel.RULE, "RULE");
        ActualFeatureCustomFieldFeatureMap.put(FeatureMenuLabel.INVOICE, "INVOICE");

    }

    public static class SubEntityActions {

        public static final String UPDATE = "update";
        public static final String CREATE = "create";
        public static final String DELETE = "delete";
    }

    public static class SERVICE_CODE {

        /*static service list*/
        public static String MANAGE_ROUGH_INVOICE = "MRI";
        public static String MANAGE_ROUGH_PARCEL = "MRP";
        public static String MANAGE_LOT = "MNGL";
        public static String CHANGE_STATUS = "CS";
        public static String RE_ROUTE_STOCK = "RRS";
        public static String PRINT_STATIC = "PRNTS";
        public static String ADD_PACKET = "AP";
        public static String EDIT_PACKET = "EP";

        /*dynamic service list*/
        public static String ALLOT = "ALLT";
        public static String ISSUE_RECEIVE_SERVICE = "IRS";
        public static String GENERATE_SLIP_NUM = "GSN";
        public static String PRINT_BARCODE = "PB";
        public static String WRITE_SERVICE = "WS";
        public static String FINALIZE = "FNLZ";
        public static String MERGE_STOCK = "MS";
        public static String SPLIT_STOCK = "SS";
        public static String SELL_LOT = "SL";
        public static String TRANSFER_LOT = "TL";
        public static String PRINT_DYNAMIC = "PRNTD";
    }
    public final static HashMap<String, String> SERVICE_CODE_NAME_MAP = new HashMap<>();

    static {
        /*static service list*/
        SERVICE_CODE_NAME_MAP.put(SERVICE_CODE.MANAGE_ROUGH_INVOICE, "Manage Rough Invoice");
        SERVICE_CODE_NAME_MAP.put(SERVICE_CODE.MANAGE_ROUGH_PARCEL, "Manage Rough Parcel");
        SERVICE_CODE_NAME_MAP.put(SERVICE_CODE.MANAGE_LOT, "Manage Lot");
        SERVICE_CODE_NAME_MAP.put(SERVICE_CODE.CHANGE_STATUS, "Change Status");
        SERVICE_CODE_NAME_MAP.put(SERVICE_CODE.RE_ROUTE_STOCK, "Re-Route Stock");
        SERVICE_CODE_NAME_MAP.put(SERVICE_CODE.PRINT_STATIC, "Print Details");
        SERVICE_CODE_NAME_MAP.put(SERVICE_CODE.ADD_PACKET, "Add Packet");
        SERVICE_CODE_NAME_MAP.put(SERVICE_CODE.EDIT_PACKET, "Edit Packet");

        /*dynamic service list*/
        SERVICE_CODE_NAME_MAP.put(SERVICE_CODE.ALLOT, "Allot");
        SERVICE_CODE_NAME_MAP.put(SERVICE_CODE.ISSUE_RECEIVE_SERVICE, "Issue Or Recieve");
        SERVICE_CODE_NAME_MAP.put(SERVICE_CODE.GENERATE_SLIP_NUM, "Generate Slip Number");
        SERVICE_CODE_NAME_MAP.put(SERVICE_CODE.PRINT_BARCODE, "Print Barcode");
        SERVICE_CODE_NAME_MAP.put(SERVICE_CODE.WRITE_SERVICE, "Write");
        SERVICE_CODE_NAME_MAP.put(SERVICE_CODE.FINALIZE, "Finalize");
        SERVICE_CODE_NAME_MAP.put(SERVICE_CODE.MERGE_STOCK, "Merge Stock");
        SERVICE_CODE_NAME_MAP.put(SERVICE_CODE.SPLIT_STOCK, "Split Stock");
        SERVICE_CODE_NAME_MAP.put(SERVICE_CODE.SELL_LOT, "Sell Stock");
        SERVICE_CODE_NAME_MAP.put(SERVICE_CODE.TRANSFER_LOT, "Transfer Stock");
        SERVICE_CODE_NAME_MAP.put(SERVICE_CODE.PRINT_DYNAMIC, "Print");
    }

    public final static HashMap<String, Map<String, String>> SERVICE_CODE_MODIFIER_MAP = new HashMap<>();
    public final static HashMap<String, Map<String, String>> SERVICE_CODE_NON_MNDTRY_MODIFIER_MAP = new HashMap<>();

    static {
        // Mandatory Modifiers
        Map<String, String> modifiersMap = new LinkedHashMap<>();
        modifiersMap.put(SERVICE_MODIFIER.MANUAL_ALLOCATION, "Manual");
        modifiersMap.put(SERVICE_MODIFIER.AUTOMATIC_ALLOCATION, "Automatic");
        SERVICE_CODE_MODIFIER_MAP.put(SERVICE_CODE.ALLOT, modifiersMap);
        modifiersMap = new LinkedHashMap<>();
        modifiersMap.put(SERVICE_MODIFIER.PLAN, "Plan");
        modifiersMap.put(SERVICE_MODIFIER.ESTIMATION, "Estimation");
        modifiersMap.put(SERVICE_MODIFIER.PARAMETER, "Parameter");
        SERVICE_CODE_MODIFIER_MAP.put(SERVICE_CODE.WRITE_SERVICE, modifiersMap);
        SERVICE_CODE_MODIFIER_MAP.put(SERVICE_CODE.FINALIZE, modifiersMap);

        //Non-Mandatory Modifiers
        Map<String, String> nmModifiersMap = new LinkedHashMap<>();
        nmModifiersMap.put(SERVICE_MODIFIER.SHOW_PLANS, "Show Plans");
        nmModifiersMap.put(SERVICE_MODIFIER.SHOW_HKPRICECALC, "Show HKPriceCalc");
        nmModifiersMap.put(SERVICE_MODIFIER.QUEUEING, "Queuing");
        SERVICE_CODE_NON_MNDTRY_MODIFIER_MAP.put(SERVICE_CODE.WRITE_SERVICE, nmModifiersMap);

        SERVICE_CODE_NON_MNDTRY_MODIFIER_MAP.put(SERVICE_CODE.FINALIZE, nmModifiersMap);

        nmModifiersMap = new LinkedHashMap<>();
        nmModifiersMap.put(SERVICE_MODIFIER.QUEUEING, "Queuing");
        SERVICE_CODE_NON_MNDTRY_MODIFIER_MAP.put(SERVICE_CODE.ALLOT, nmModifiersMap);
        SERVICE_CODE_NON_MNDTRY_MODIFIER_MAP.put(SERVICE_CODE.ISSUE_RECEIVE_SERVICE, nmModifiersMap);
        SERVICE_CODE_NON_MNDTRY_MODIFIER_MAP.put(SERVICE_CODE.PRINT_BARCODE, nmModifiersMap);
        SERVICE_CODE_NON_MNDTRY_MODIFIER_MAP.put(SERVICE_CODE.PRINT_DYNAMIC, nmModifiersMap);
        SERVICE_CODE_NON_MNDTRY_MODIFIER_MAP.put(SERVICE_CODE.ADD_PACKET, nmModifiersMap);
        SERVICE_CODE_NON_MNDTRY_MODIFIER_MAP.put(SERVICE_CODE.MERGE_STOCK, nmModifiersMap);
        SERVICE_CODE_NON_MNDTRY_MODIFIER_MAP.put(SERVICE_CODE.SPLIT_STOCK, nmModifiersMap);
        SERVICE_CODE_NON_MNDTRY_MODIFIER_MAP.put(SERVICE_CODE.SELL_LOT, nmModifiersMap);
        SERVICE_CODE_NON_MNDTRY_MODIFIER_MAP.put(SERVICE_CODE.TRANSFER_LOT, nmModifiersMap);
        SERVICE_CODE_NON_MNDTRY_MODIFIER_MAP.put(SERVICE_CODE.EDIT_PACKET, nmModifiersMap);

    }

    public static class SERVICE_MODIFIER {

        public static String MANUAL_ALLOCATION = "MA";
        public static String AUTOMATIC_ALLOCATION = "AA";
        public static String PLAN = "PL";
        public static String ESTIMATION = "ES";
        public static String PARAMETER = "PA";
        public static String SHOW_PLANS = "SP";
        public static String SHOW_HKPRICECALC = "SHPC";
        public static String QUEUEING = "Q";
    }

    public final static Map<String, String> typeOfPlanMap = new HashMap<>();

    static {
        typeOfPlanMap.put(SERVICE_MODIFIER.PLAN, "Plan");
        typeOfPlanMap.put(SERVICE_MODIFIER.ESTIMATION, "Estimation");
        typeOfPlanMap.put(SERVICE_MODIFIER.PARAMETER, "Parameter");

    }

    // Don't use for normal feature
    public static class FeatureForFormulaEvaluation {

        public static final String ADD_INVOICE = "Add Invoice";
        public static final String EDIT_INVOICE = "Edit Invoice";
        public static final String ADD_PARCEL = "Add Parcel";
        public static final String EDIT_PARCEL = "Edit Parcel";
        public static final String ADD_LOT = "Add Lot";
        public static final String EDIT_LOT = "Edit Lot";
        public static final String ADD_PACKET = "Add Packet";
        public static final String EDIT_PACKET = "Edit Packet";
        public static final String ADD_PLAN = "Add Plan";
        public static final String EDIT_PLAN = "Edit Plan";
        public static final String SELL_STOCK = "Sell Stock";
        public static final String TRANSFER_STOCK = "Transfer Stock";
        public static final String ISSUE_STOCK = "Issue Stock";
        public static final String EDIT_SUB_LOT = "Edit Sublot";
    }

    public static class AggregrateFunctions {

        public static final String SUM = "SUM";
        public static final String MIN = "MIN";
        public static final String MAX = "MAX";
        public static final String AVG = "AVG";
        public static final String COUNT = "COUNT";
    }

    public static class DocumentsForFormula {

        public static final String INVOICE_DOCUMENT = "Invoice Document";
        public static final String PARCEL_DOCUMENT = "Parcel Document";
        public static final String LOT_DOCUMENT = "Lot Document";
        public static final String PACKET_DOCUMENT = "Packet Document";
        public static final String PLAN_DOCUMENT = "Plan Document";
        public static final String SELL_DOCUMENT = "Sell Document";
        public static final String TRANSFER_DOCUMENT = "Transfer Document";
        public static final String SUBLOT_DOCUMENT = "SubLot Document";

    }

    public static class NotificationConfigurationType {

        public static final String RULE_BASED = "rule";
        public static final String TIME_BASED = "time";

    }

    public static class PlanStatus {

        public static final String ALL = "A";
        public static final String FINALIZED = "F";
        public static final String SUBMITTED = "S";
    }

    public static class IssueStatus {

        public static final String PENDING = "Pending";
        public static final String IN_TRANSIT = "In-Transit";
        public static final String RECIEVED = "Recieved";
        public static final String RETURNED = "Returned";

    }

    public static class EODIssueStatus {

        public static final String IN_TRANSIT_SAFE = "In-Transit Safe";
        public static final String IN_SAFE = "In-Safe";
        public static final String RETURN_FROM_SAFE = "Return from Safe";
        public static final String IN_TRANSIT_ISSUER = "In-Transit Issuer";
        public static final String WITH_ISSUER = "With Issuer";
        public static final String RETURN_FROM_ISSUER = "Return from Issuer";
    }

    public final static HashMap<String, String> PLAN_STATUS = new HashMap<>();

    public static class ShowPlanStatus {

        public static final String ENTERED = "Entered";
        public static final String FINALIZED = "Finalized";
//        public static final String FINISHED = "Finished";
        public static final String BEST_PLAN = "Best Plan";
        public static final String SUBMITTED = "Submitted";
        public static final String ARCHIVED = "Archived";
    }

    static {
        PLAN_STATUS.put(ShowPlanStatus.ENTERED, ShowPlanStatus.ENTERED);
        PLAN_STATUS.put(ShowPlanStatus.FINALIZED, ShowPlanStatus.FINALIZED);
        PLAN_STATUS.put(ShowPlanStatus.SUBMITTED, ShowPlanStatus.SUBMITTED);
    }
    public final static HashMap<String, String> ISSUE_STATUS = new HashMap<>();

    static {
        ISSUE_STATUS.put(IssueStatus.RECIEVED, IssueStatus.RECIEVED);
        ISSUE_STATUS.put(IssueStatus.PENDING, IssueStatus.PENDING);
        ISSUE_STATUS.put(IssueStatus.IN_TRANSIT, IssueStatus.IN_TRANSIT);
        ISSUE_STATUS.put(IssueStatus.RETURNED, IssueStatus.RETURNED);

    }

    public static class LotStaticFieldName {

        public static final String ALLOT_TO = "allot_to_lot$UMS$String";
        public static final String IN_STOCK_OF = "in_stock_of_lot$UMS$String";
        public static final String DUE_DATE = "due_date_of_lot$DT$Date";
        //used in dynamic form directive
        public static final String CARATE_RANGE = "carate_range_of_lot$DRP$Long";
        public static final String LOT_ID = "lotID$AG$String";
        public static final String CARAT = "carat_of_lot$NM$Double";
        public static final String PIECES = "no_of_pieces_of_lot$NM$Long";
        public static final String STOCK_CARAT = "stockCarat";
        public static final String STOCK_PIECES = "stockPieces";
        public static final String IN_STOCK_OF_DEPT = "in_stock_of_dept_lot$UMS$String";
    }

    public static class IssueStaticFieldName {

        public static final String IN_STOCK_OF_DEPT = "in_stock_of_dept$UMS$String";
        public static final String IN_STOCK_OF = "in_stock_of_issue$UMS$String";
        public static final String CARRIER_BOY = "carrier_boy$UMS$String";
    }

    public static class PacketStaticFieldName {

        public static final String ALLOT_TO = "allot_to_packet$UMS$String";
        public static final String IN_STOCK_OF = "in_stock_of_packet$UMS$String";
        public static final String DUE_DATE = "due_date_of_packet$DT$Date";
        //used in dynamic form directive
        public static final String CARATE_RANGE = "carate_range_of_packet$DRP$Long";
        public static final String CARAT = "carat_of_packet$NM$Double";
        public static final String PIECES = "no_of_pieces_of_packet$NM$Long";
        public static final String GRADE = "grade$DRP$Long";
        public static final String PACKET_ID = "packetID$AG$String";
        public static final String IN_STOCK_OF_DEPT = "in_stock_of_dept_packet$UMS$String";

    }

    public static class PacketStaticValueOfFields {

        public static final Long PIECES = 1l;
    }

    public static class PlanStaticFieldName {

        public static final String BREAKAGE = "breakage$PRC$Double";
        public static final String CARATE_RANGE = "carate_range_of_plan$DRP$Long";
        public static final String CUT = "cut$DRP$Long";
        public static final String CARAT = "carat";
        public static final String CLARITY = "clarity$DRP$Long";
        public static final String COLOR = "color$DRP$Long";
        public static final String FLUROSCENE = "fluroscene$DRP$Long";

        public static final String CURRENCY_CODE = "value_of_plan$NM$Integer@CurrencyCode";

        public static final String VALUE_OF_PLAN = "value_of_plan$NM$Integer";
        public static final String SNAP_ID = "snap_id$TF$Long";
        public static final String MACHINE_TO_PROCESS = "machine_to_process_in$DRP$Long";
        public static final String PRICE = "price";
    }

    public static class ParcelStaticFieldName {

        public static final String IN_STOCK_OF = "in_stock_of_parcel$UMS$String";
        public static final String ORIGINAL_CARAT = "origCarat$NM$Double";
        public static final String ORIGINAL_PIECES = "origPieces$NM$Long";
        public static final String ORIGINAL_RATES = "origRateInDollar$NM$Double";
        public static final String ORIGINAL_AMOUNT = "origAmountInDollar$FRM$Double";
        public static final String STOCK_CARAT = "stockCarat";
        public static final String STOCK_PIECES = "stockPieces";
        public static final String IN_STOCK_OF_DEPT = "in_stock_of_dept_parcel$UMS$String";
    }

    public static class InvoiceStaticFieldName {

        public static final String IN_STOCK_OF = "in_stock_of_invoice$UMS$String";
    }

    public static class WORKALLOTEMENT_UNIT_TYPE {

        public static final String LOT = "L";
        public static final String PACKET = "P";
    }

    public final static HashMap<String, List<String>> PROPOSED_STATUS_MAP = new HashMap<>();

    static {
        PROPOSED_STATUS_MAP.put(StockStatus.IN_PRODUCTION, Arrays.asList(StockStatus.TERMINATED));
        PROPOSED_STATUS_MAP.put(StockStatus.TERMINATED,
                Arrays.asList(StockStatus.FINISHED, StockStatus.REJECTED, StockStatus.SOLD, StockStatus.TRANSFERRED));
        PROPOSED_STATUS_MAP.put(StockStatus.REJECTED,
                Arrays.asList(StockStatus.FINISHED, StockStatus.SOLD, StockStatus.TRANSFERRED));
        PROPOSED_STATUS_MAP.put(StockStatus.FINISHED,
                Arrays.asList(StockStatus.SOLD, StockStatus.REJECTED, StockStatus.TRANSFERRED));
        PROPOSED_STATUS_MAP.put(StockStatus.SOLD,
                Arrays.asList(StockStatus.TERMINATED));
        PROPOSED_STATUS_MAP.put(StockStatus.TRANSFERRED,
                Arrays.asList(StockStatus.TERMINATED));
    }
    public final static HashMap<Integer, String> ACTIVITY_SERVICE_ENTITY_MAP = new HashMap<>();

    public static class ActivityServiceEntity {

        public static final String CURRENT_ACTIVITY = "CurrentActivity";
        public static final String NEXT_ACTIVITY = "NextActivity";
        public static final String PREVIOUS_ACTIVITY = "PreviousActivity";
        public static final String CURRENT_SERVICE = "CurrentService";
        public static final String NEXT_SERVICE = "NextService";
        public static final String PREVIOUS_SERVICE = "PreviousService";
    }

    static {
        ACTIVITY_SERVICE_ENTITY_MAP.put(-1, ActivityServiceEntity.CURRENT_ACTIVITY);
        ACTIVITY_SERVICE_ENTITY_MAP.put(-2, ActivityServiceEntity.NEXT_ACTIVITY);
        ACTIVITY_SERVICE_ENTITY_MAP.put(-3, ActivityServiceEntity.PREVIOUS_ACTIVITY);
        ACTIVITY_SERVICE_ENTITY_MAP.put(-4, ActivityServiceEntity.CURRENT_SERVICE);
        ACTIVITY_SERVICE_ENTITY_MAP.put(-5, ActivityServiceEntity.NEXT_SERVICE);
        ACTIVITY_SERVICE_ENTITY_MAP.put(-6, ActivityServiceEntity.PREVIOUS_SERVICE);
    }
    public final static HashMap<Integer, String> ACTIVITY_SERVICE_FIELD_ENTITY_MAP = new HashMap<>();

    public static class ActivityServiceFieldEntity {

        public static final String STATUS = "Status";
        public static final String NO_OF_OCCURANCE = "NoOfOccurance";
        public static final String TIME = "Time";
        public static final String NAME = "Name";
    }

    static {
        ACTIVITY_SERVICE_FIELD_ENTITY_MAP.put(-1, ActivityServiceFieldEntity.STATUS);
        ACTIVITY_SERVICE_FIELD_ENTITY_MAP.put(-2, ActivityServiceFieldEntity.NO_OF_OCCURANCE);
        ACTIVITY_SERVICE_FIELD_ENTITY_MAP.put(-3, ActivityServiceFieldEntity.TIME);
        ACTIVITY_SERVICE_FIELD_ENTITY_MAP.put(-4, ActivityServiceFieldEntity.NAME);
    }

    public final static HashMap<String, List<String>> FEATURE_NAME_MANDATORY_FIELD_MAP = new HashMap<>();

    public static class MANDATORY_FIELD {

//        mandatory fields for issue/receive feature
        public static final String COURIER_BOY = "carrier_boy$UMS$String";
        public static final String ISSUED_TO = "issued_to$UMS$String";

        //        mandatory fields for write service
        public static final String CUT = "cut$DRP$Long";
        public static final String CARAT = "carat$NM$Double";
        public static final String CLARITY = "clarity$DRP$Long";
        public static final String COLOR = "color$DRP$Long";
//        mandatory fields for write service ends here
        //        mandatory fields for allot service
        public static final String ALLOT_TO = "allot_to";
//        mandatory fields for allot service ends here
        //        mandatory fields for sell service
        public static final String SELL_TO = "sell_to";
//        mandatory fields for sell service ends here
        //        mandatory fields for transfer service
        public static final String TRANSFER_TO = "transfer_to";
//        mandatory fields for transfer service ends here
        //        mandatory fields for invoice service
        public static final String CARAT_OF_INVOICE = "carat_of_invoice$NM$Double";
//        mandatory fields for transfer service ends here

        //-----------------mandatory fields for parcel service-----------
        public static final String ORIGINAL_PIECES = "origPieces$NM$Long";
        public static final String ORIGINAL_CARAT = "origCarat$NM$Double";
        public static final String ORIGINAL_RATES = "origRateInDollar$NM$Double";
        public static final String ORIGINAL_AMOUNT = "origAmountInDollar$FRM$Double";
        public static final String CARAT_OF_PARCEL = "carat_of_parcel$NM$Double";
        //-----------------mandatory fields for parcel service ends here---------

        //        mandatory fields for lot service
        public static final String CARAT_OF_LOT = "carat_of_lot$NM$Double";
//        mandatory fields for transfer service ends here
        //        mandatory fields for packet service
        public static final String CARAT_OF_PACKET = "carat_of_packet$NM$Double";
//        mandatory fields for transfer service ends here

    }

    static {
        FEATURE_NAME_MANDATORY_FIELD_MAP.put(SERVICE_CODE.ISSUE_RECEIVE_SERVICE, Arrays.asList(MANDATORY_FIELD.COURIER_BOY, MANDATORY_FIELD.ISSUED_TO));
        FEATURE_NAME_MANDATORY_FIELD_MAP.put(SERVICE_CODE.WRITE_SERVICE, Arrays.asList(MANDATORY_FIELD.CUT, MANDATORY_FIELD.CARAT, MANDATORY_FIELD.CLARITY, MANDATORY_FIELD.COLOR));
        FEATURE_NAME_MANDATORY_FIELD_MAP.put(SERVICE_CODE.ALLOT, Arrays.asList(MANDATORY_FIELD.ALLOT_TO));
        FEATURE_NAME_MANDATORY_FIELD_MAP.put(SERVICE_CODE.SELL_LOT, Arrays.asList(MANDATORY_FIELD.SELL_TO));
        FEATURE_NAME_MANDATORY_FIELD_MAP.put(SERVICE_CODE.TRANSFER_LOT, Arrays.asList(MANDATORY_FIELD.TRANSFER_TO));

    }

    public static class LEAVE_STATUS_FOR_ALLOCATION {

        public static final String PRESENT = "Present";
        public static final String LEAVE_APPLIED = "Leave applied";
        public static final String LEAVE_APPROVED = "Leave approved";
    }

    public static class FranchiseSetupEntity {

        public static final String CUSTOM_FIELD = "CF";
        public static final String ACTIVITY_FLOW = "ACT";
        public static final String REPORT = "R";
    }

    public static class UserGoalTemplateStatus {

        public static final String PENDING = "Pending";
        public static final String ATTENDED = "Attended";
        public static final String NOT_ATTENDED = "Not Attended";
        public static final String COMPLETED = "Completed";
        public static final String SUBMITTED = "Submitted";
    }

    public final static HashMap<String, String> GOAL_TEMPLATE_MODIFIERS_MAP = new HashMap<>();

    public static class GoalTemplateModifiers {

        public static final String PREV_SET_VALUE = "prev_set_value";
        public static final String PREV_ACHIEVED_VALUE = "prev_achieved_value";
    }

    static {
        GOAL_TEMPLATE_MODIFIERS_MAP.put(GoalTemplateModifiers.PREV_ACHIEVED_VALUE, GoalTemplateModifiers.PREV_ACHIEVED_VALUE);
        GOAL_TEMPLATE_MODIFIERS_MAP.put(GoalTemplateModifiers.PREV_SET_VALUE, GoalTemplateModifiers.PREV_SET_VALUE);

    }

    public static class NotificationSendType {

        public static final String WEB = "web";
        public static final String EMAIL = "email";
    }

    public static class NotificationEmailStatus {

        public static final String NOT_REQUIRED = "NR";
        public static final String PENDING = "P";
        public static final String SENT = "S";
    }

    public static class ReportEmailStatus {

        public static final String EXPIRED = "E";
        public static final String PENDING = "P";
        public static final String SENT = "C";
    }

    public static class InvoiceStaticFields {

        public static final String IS_HIDDEN = "is_hidden$CB$Boolean";
        public static final String TYPE_OF_INVOICE = "type_of_invoice$DRP$Long";
        public static final String INVOICE_ID = "invoiceId$AG$String";
        public static final String INVOICE_DATE = "invoiceDate$DT$Date";

    }

    public static class RoughPurchaseStaticFields {

        public static final String ROUGH_PURCHASE_ID = "roughPurchaseID$AG$String";

    }

    public static class ParcelStaticFields {

        public static final String PARCEL_ID = "parcelId$AG$String";
        public static final String NO_OF_PIECES = "no_of_pieces_of_parcel$NM$Long";

    }

    public static class SellStaticFields {

        public static final String TOTAL_AMOUNT_IN_DOLLAR = "totalAmountInDollar";
        public static final String TOTAL_AMOUNT_IN_RS = "totalAmountInRs";
        public static final String TOTAL_CARAT = "totalCarat";
        public static final String TOTAL_PIECES = "totalPieces";

    }

    public static class RoughStockActions {

        public static final String DEBIT = "D";
        public static final String CREDIT = "C";

    }

    public static class RoughStockOperations {

        public static final String ROUGH = "Rough";
        public static final String SELL = "Sell";
        public static final String MERGE = "Merge";
        public static final String MEMO = "Memo";
        public static final String LOT = "Lot";
        public static final String COLLECT = "Collect";
        public static final String RECEIVE = "Receive";
        public static final String DIRECT_ISSUE = "Direct Issue";
        public static final String DIRECT_RECEIVE = "Direct Receive";

    }

    public static class PurchaseStaticFields {

        public static final String PURCHASE_ID = "purchaseId$AG$Long";

    }

    public static class DEPLOY_TIME_POINTERS_APPENDED_NAME {

        public static final String PACKET_PRIORITY = "packet_priority$DRP$Long$POI";
        public static final String REASON_TO_REJECT_PACKET = "reason_to_reject_packet$DRP$Long$POI";
        public static final String QUALITY_FROM = "quality_from$DRP$Long$POI";
        public static final String COLOR_TO = "color_to$DRP$Long$POI";
    }

    public static class DEPLOY_TIME_POINTERS_ORIGINAL_NAME {

        public static final String PACKET_PRIORITY = "packet_priority";
        public static final String REASON_TO_REJECT_PACKET = "reason_to_reject_packet";
        public static final String COLOR_TO = "color_to";
        public static final String QUALITY_FROM = "quality_from";
    }
    public final static HashMap<String, String> DEPLOY_TIME_POINTERS_MAP = new HashMap<>();

    static {
        DEPLOY_TIME_POINTERS_MAP.put(DEPLOY_TIME_POINTERS_ORIGINAL_NAME.PACKET_PRIORITY, DEPLOY_TIME_POINTERS_APPENDED_NAME.PACKET_PRIORITY);
        DEPLOY_TIME_POINTERS_MAP.put(DEPLOY_TIME_POINTERS_ORIGINAL_NAME.REASON_TO_REJECT_PACKET, DEPLOY_TIME_POINTERS_APPENDED_NAME.REASON_TO_REJECT_PACKET);
        DEPLOY_TIME_POINTERS_MAP.put(DEPLOY_TIME_POINTERS_ORIGINAL_NAME.COLOR_TO, DEPLOY_TIME_POINTERS_APPENDED_NAME.COLOR_TO);
        DEPLOY_TIME_POINTERS_MAP.put(DEPLOY_TIME_POINTERS_ORIGINAL_NAME.QUALITY_FROM, DEPLOY_TIME_POINTERS_APPENDED_NAME.QUALITY_FROM);

    }

    public static final String SWITCH_ON = "on";
    public static final String SWITCH_OFF = "off";

    public static class DEPARTMENT_PROCESS_FLOW {

        public static final String PREFIX_FOR_DEPARTMENT = "Dept";
        public static final String PREFIX_FOR_DESIGNATION = "Desg";
        public static final String BACKGROUND_COLOR_FOR_DEPT = "#EB99EB";
        public static final String BACKGROUND_COLOR_FOR_DESG = "#8AA9B1";
        public static final String COLOR_FOR_DESG = "#0F0F0F";
        public static final String DEFAULT_WEIGHT = "3";
        public static final String WEIGHT = "2";
        public static final String DEFAULT_DEPARTMENT = "default";
        public static final String NOT_DEFAULT_DEPARTMENT = "rev";
        public static final String COLOR_FOR_DEP_WITH_DESIG = "#767676";
        public static final String TYPE_FOR_DESIGNATION = "designation";
    }

    public static class RuleConfiguration {

        public static class RuleType {

            public static final String PRE = "PR";
            public static final String POST = "PO";
            public static final String SCREEN = "S";
        }

        public static final String RULE_CATEGORY = "R";
        public static final String EXCEPTION_CATEGORY = "E";
        public static final String DEPARTMENT_CATEGORY = "D";

        public static class CONSTANT {

            public static final String LOGIN = "login";
            public static final String IsAbsentToday = "isabsenttoday";
            public static final String RESTRICT_BY_IP = "restrictbyip";
            public static final String ABSENT_WITHOUT_INFO = "absentwithoutinfo";

        }
    }

    public static class SELL_PARCEL_DETAIL_FIELDS {

        public static final String SELL_CARATS = "sellCarats";
        public static final String SELL_PIECES = "sellPieces";
        public static final String ID = "id";
        public static final String PARCEL = "parcel";
        public static final String EXCHANGE_RATE = "exchangeRate";
    }
    public static Map<String, String> RAP_CALC_FIELD_MAP = new HashMap<>();

    public static final class FourC {

        public static final String CARAT = "size$DRP$Long";
        public static final String GRAPH_CARAT = "gcaratValue$DRP$Long";
        public static final String COLOR = "COL$DRP$Long";
        public static final String SHAPE = "SHAPE$DRP$Long";
        public static final String QUALITY = "CLR$DRP$Long";
        public static final String R_DATE = "rdate$DRP$Long";

    }

    //This Map stores list of master values sorted by their sequence no for Shape, Quality and Color
    public static Map<String, List<Long>> RAP_CALC_FOURC_VALUE_MAP = new HashMap<>();

    public static final class IssueReceiveOperations {

        public static final String ISSUE_INWARD = "II";
        public static final String RECEIVE_INWARD = "RI";
        public static final String REQUEST = "RQ";
        public static final String COLLECT = "CL";
        public static final String ISSUE = "IS";
        public static final String RECEIVE = "RC";
        public static final String RETURN = "RT";

    }

    public static final class IssueReceiveTypes {

        public static final String REQUEST = "Request";
        public static final String DIRECT_ISSUE = "Direct Issue";
        public static final String DIRECT_RECEIVE = "Direct Receive";

    }

    public static final class IssueReceiveStatus {

        public static final String PENDING = "Pending";
        public static final String REJECTED = "Rejected";
        public static final String COLLECTED = "Collected";
        public static final String ISSUE = "Issue";
        public static final String COMPLETE = "Complete";

    }

    public static final class Modifiers {

        public static final class Medium {

            public static final String ROUGH = "R";
            public static final String LOT = "L";
            public static final String PACKET = "P";
        }

        public static final class Type {

            public static final String INWARD = "IW";
            public static final String OUTWARD = "OW";
        }

        public static final class Mode {

            public static final String DIRECT = "DR";
            public static final String VIA_STOCKROOM = "VS";
        }

        public static final class Access {

            public static final String REQUEST = "RQ";
            public static final String COLLECT = "CL";
            public static final String ISSUE = "IS";
            public static final String RECEIVE = "RC";
            public static final String RETURN = "RT";
        }
    }

    public final static class IssueReceiveStockStatus {

        public final static String COMPLETED = "C";
        public final static String PENDING = "P";
        public final static String REJECTED = "R";
    }

    public static final class EstimatedPlanModifier {

        public static String SHOW_PLANS = "SP";
        public static String COPY_PLAN = "CP";
        public static String DELETE_PLAN = "DP";
        public static String ADD_PLAN = "AP";
        public static String FINALIZE_PLAN = "FP";

    }

    public static final class StockName {

        public static final String INVOICE = "Invoice";
        public static final String PARCEL = "Parcel";
        public static final String LOT = "Lot";
        public static final String PACKET = "Packet";

    }

    public static final class PlanConstants {

        public static final String USERS = "users";
        public static final String STATUS = "status";
        public static final String PLAN_ID = "planId";
        public static final String TAG = "tag";
        public static final String INDEX = "index";
        public static final String COPIED_FROM = "copiedFrom";
        public static final String IS_ACTIVE = "isActive";

    }

    public static final String[] AVOID_CHARACTERS = {"#", "<", "$ ", "+", "%", ">", "!", "`", "&",
        "*", "‘", "|", "{", "?", "\"", "=", "}", "/", ":", "\'", "@"};
}
