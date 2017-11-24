--
-- PostgreSQL database dump
--

-- Dumped from database version 9.3.4
-- Dumped by pg_dump version 9.3.4
-- Started on 2014-09-09 18:54:54

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

--
-- TOC entry 300 (class 3079 OID 11750)
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- TOC entry 2807 (class 0 OID 0)
-- Dependencies: 300
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- TOC entry 171 (class 1259 OID 95224)
-- Name: email_template_details; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE email_template_details (
    template_id bigint NOT NULL,
    client_id bigint,
    created_by bigint,
    created_on timestamp without time zone,
    custom_1 character varying(255),
    file_name character varying(255),
    is_active boolean NOT NULL,
    is_archive boolean NOT NULL,
    language_code character varying(255),
    modified_by bigint,
    modified_on timestamp without time zone,
    template_name character varying(255)
);


ALTER TABLE public.email_template_details OWNER TO postgres;

--
-- TOC entry 170 (class 1259 OID 95222)
-- Name: email_template_details_template_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE email_template_details_template_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.email_template_details_template_id_seq OWNER TO postgres;

--
-- TOC entry 2808 (class 0 OID 0)
-- Dependencies: 170
-- Name: email_template_details_template_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE email_template_details_template_id_seq OWNED BY email_template_details.template_id;


--
-- TOC entry 172 (class 1259 OID 95233)
-- Name: hk_asset_document_info; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE hk_asset_document_info (
    asset bigint NOT NULL,
    document_path character varying(500) NOT NULL,
    document_title character varying(100),
    is_archive boolean NOT NULL
);


ALTER TABLE public.hk_asset_document_info OWNER TO postgres;

--
-- TOC entry 174 (class 1259 OID 95243)
-- Name: hk_asset_issue_info; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE hk_asset_issue_info (
    id bigint NOT NULL,
    created_by bigint NOT NULL,
    created_on timestamp without time zone NOT NULL,
    franchise bigint NOT NULL,
    is_archive boolean NOT NULL,
    issue_to_instance bigint NOT NULL,
    issue_to_type character varying(5) NOT NULL,
    issued_on timestamp without time zone NOT NULL,
    issued_units integer,
    last_modified_by bigint NOT NULL,
    last_modified_on timestamp without time zone NOT NULL,
    remarks character varying(500),
    status character varying(10) NOT NULL,
    asset bigint NOT NULL
);


ALTER TABLE public.hk_asset_issue_info OWNER TO postgres;

--
-- TOC entry 173 (class 1259 OID 95241)
-- Name: hk_asset_issue_info_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE hk_asset_issue_info_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.hk_asset_issue_info_id_seq OWNER TO postgres;

--
-- TOC entry 2809 (class 0 OID 0)
-- Dependencies: 173
-- Name: hk_asset_issue_info_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE hk_asset_issue_info_id_seq OWNED BY hk_asset_issue_info.id;


--
-- TOC entry 175 (class 1259 OID 95252)
-- Name: hk_asset_purchaser_info; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE hk_asset_purchaser_info (
    asset bigint NOT NULL,
    is_archive boolean NOT NULL,
    purchase_by_instance bigint NOT NULL,
    purchase_by_type character varying(5) NOT NULL
);


ALTER TABLE public.hk_asset_purchaser_info OWNER TO postgres;

--
-- TOC entry 176 (class 1259 OID 95257)
-- Name: hk_event_recipient_info; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE hk_event_recipient_info (
    event bigint NOT NULL,
    reference_instance bigint NOT NULL,
    reference_type character varying(5) NOT NULL,
    franchise bigint NOT NULL,
    is_archive boolean NOT NULL
);


ALTER TABLE public.hk_event_recipient_info OWNER TO postgres;

--
-- TOC entry 178 (class 1259 OID 95264)
-- Name: hk_event_registration_field_info; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE hk_event_registration_field_info (
    id bigint NOT NULL,
    component_type character varying(10) NOT NULL,
    default_values character varying(1000),
    field_name character varying(500) NOT NULL,
    is_archive boolean NOT NULL,
    last_modified_by bigint NOT NULL,
    last_modified_on timestamp without time zone NOT NULL,
    validation_pattern character varying(1000),
    event bigint NOT NULL
);


ALTER TABLE public.hk_event_registration_field_info OWNER TO postgres;

--
-- TOC entry 177 (class 1259 OID 95262)
-- Name: hk_event_registration_field_info_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE hk_event_registration_field_info_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.hk_event_registration_field_info_id_seq OWNER TO postgres;

--
-- TOC entry 2810 (class 0 OID 0)
-- Dependencies: 177
-- Name: hk_event_registration_field_info_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE hk_event_registration_field_info_id_seq OWNED BY hk_event_registration_field_info.id;


--
-- TOC entry 179 (class 1259 OID 95273)
-- Name: hk_event_registration_field_value_info; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE hk_event_registration_field_value_info (
    registration_field bigint NOT NULL,
    user_id bigint NOT NULL,
    event bigint NOT NULL,
    field_value character varying(1000) NOT NULL,
    is_archive boolean NOT NULL
);


ALTER TABLE public.hk_event_registration_field_value_info OWNER TO postgres;

--
-- TOC entry 181 (class 1259 OID 95283)
-- Name: hk_feature_section_rel_info; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE hk_feature_section_rel_info (
    id bigint NOT NULL,
    created_on timestamp without time zone NOT NULL,
    feature bigint NOT NULL,
    is_archive boolean NOT NULL,
    is_view_only boolean NOT NULL,
    section bigint
);


ALTER TABLE public.hk_feature_section_rel_info OWNER TO postgres;

--
-- TOC entry 180 (class 1259 OID 95281)
-- Name: hk_feature_section_rel_info_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE hk_feature_section_rel_info_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.hk_feature_section_rel_info_id_seq OWNER TO postgres;

--
-- TOC entry 2811 (class 0 OID 0)
-- Dependencies: 180
-- Name: hk_feature_section_rel_info_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE hk_feature_section_rel_info_id_seq OWNED BY hk_feature_section_rel_info.id;


--
-- TOC entry 183 (class 1259 OID 95291)
-- Name: hk_field_info; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE hk_field_info (
    id bigint NOT NULL,
    associated_currency integer,
    component_type character varying(15) NOT NULL,
    created_by bigint NOT NULL,
    created_on timestamp without time zone NOT NULL,
    db_base_name character varying(200),
    db_base_type character varying(10),
    db_field_name character varying(100),
    feature bigint NOT NULL,
    field_label character varying(500) NOT NULL,
    field_type character varying(50),
    franchise bigint NOT NULL,
    is_archive boolean NOT NULL,
    is_custom_field boolean NOT NULL,
    last_modified_by bigint NOT NULL,
    last_modified_on timestamp without time zone NOT NULL,
    status character varying(10) NOT NULL,
    ui_field_name character varying(100),
    validation_pattern character varying(1000),
    section bigint
);


ALTER TABLE public.hk_field_info OWNER TO postgres;

--
-- TOC entry 182 (class 1259 OID 95289)
-- Name: hk_field_info_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE hk_field_info_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.hk_field_info_id_seq OWNER TO postgres;

--
-- TOC entry 2812 (class 0 OID 0)
-- Dependencies: 182
-- Name: hk_field_info_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE hk_field_info_id_seq OWNED BY hk_field_info.id;


--
-- TOC entry 185 (class 1259 OID 95302)
-- Name: hk_franchise_min_req_info; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE hk_franchise_min_req_info (
    id bigint NOT NULL,
    acquired_value integer,
    franchise bigint NOT NULL,
    is_archive boolean NOT NULL,
    req_id bigint NOT NULL,
    req_name character varying(100) NOT NULL,
    req_type character varying(5) NOT NULL,
    required_value integer
);


ALTER TABLE public.hk_franchise_min_req_info OWNER TO postgres;

--
-- TOC entry 184 (class 1259 OID 95300)
-- Name: hk_franchise_min_req_info_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE hk_franchise_min_req_info_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.hk_franchise_min_req_info_id_seq OWNER TO postgres;

--
-- TOC entry 2813 (class 0 OID 0)
-- Dependencies: 184
-- Name: hk_franchise_min_req_info_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE hk_franchise_min_req_info_id_seq OWNED BY hk_franchise_min_req_info.id;


--
-- TOC entry 187 (class 1259 OID 95310)
-- Name: hk_leave_approval_info; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE hk_leave_approval_info (
    id bigint NOT NULL,
    attended_by bigint,
    attended_on timestamp without time zone,
    level integer NOT NULL,
    on_date timestamp without time zone,
    remarks character varying(1000),
    status character varying(10) NOT NULL,
    workflow bigint NOT NULL,
    leave_request bigint NOT NULL
);


ALTER TABLE public.hk_leave_approval_info OWNER TO postgres;

--
-- TOC entry 186 (class 1259 OID 95308)
-- Name: hk_leave_approval_info_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE hk_leave_approval_info_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.hk_leave_approval_info_id_seq OWNER TO postgres;

--
-- TOC entry 2814 (class 0 OID 0)
-- Dependencies: 186
-- Name: hk_leave_approval_info_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE hk_leave_approval_info_id_seq OWNED BY hk_leave_approval_info.id;


--
-- TOC entry 189 (class 1259 OID 95321)
-- Name: hk_leave_info; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE hk_leave_info (
    id bigint NOT NULL,
    created_by bigint,
    created_on timestamp without time zone NOT NULL,
    description character varying(500),
    final_remarks character varying(1000),
    for_user bigint NOT NULL,
    franchise bigint NOT NULL,
    frm_dt timestamp without time zone NOT NULL,
    is_archive boolean NOT NULL,
    leave_reason bigint,
    status character varying(10) NOT NULL,
    to_dt timestamp without time zone,
    total_days real
);


ALTER TABLE public.hk_leave_info OWNER TO postgres;

--
-- TOC entry 188 (class 1259 OID 95319)
-- Name: hk_leave_info_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE hk_leave_info_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.hk_leave_info_id_seq OWNER TO postgres;

--
-- TOC entry 2815 (class 0 OID 0)
-- Dependencies: 188
-- Name: hk_leave_info_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE hk_leave_info_id_seq OWNED BY hk_leave_info.id;


--
-- TOC entry 191 (class 1259 OID 95332)
-- Name: hk_location_mst; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE hk_location_mst (
    id bigint NOT NULL,
    company bigint NOT NULL,
    created_by bigint NOT NULL,
    created_on timestamp without time zone NOT NULL,
    is_active boolean NOT NULL,
    is_archive boolean NOT NULL,
    last_modified_by bigint NOT NULL,
    last_modified_on timestamp without time zone NOT NULL,
    location_name character varying(255) NOT NULL,
    location_type character varying(255) NOT NULL,
    parent bigint
);


ALTER TABLE public.hk_location_mst OWNER TO postgres;

--
-- TOC entry 190 (class 1259 OID 95330)
-- Name: hk_location_mst_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE hk_location_mst_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.hk_location_mst_id_seq OWNER TO postgres;

--
-- TOC entry 2816 (class 0 OID 0)
-- Dependencies: 190
-- Name: hk_location_mst_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE hk_location_mst_id_seq OWNED BY hk_location_mst.id;


--
-- TOC entry 192 (class 1259 OID 95341)
-- Name: hk_message_recipent_info; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE hk_message_recipent_info (
    message_obj bigint NOT NULL,
    reference_instance bigint NOT NULL,
    reference_type character varying(255) NOT NULL,
    is_archive boolean NOT NULL
);


ALTER TABLE public.hk_message_recipent_info OWNER TO postgres;

--
-- TOC entry 193 (class 1259 OID 95346)
-- Name: hk_message_recipient_dtl; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE hk_message_recipient_dtl (
    message_obj bigint NOT NULL,
    message_to bigint NOT NULL,
    attended_on timestamp without time zone,
    has_priority boolean NOT NULL,
    is_archive boolean NOT NULL,
    is_attended boolean NOT NULL,
    message_from bigint NOT NULL,
    replied_message_body character varying(500),
    replied_on timestamp without time zone,
    status character varying(255) NOT NULL
);


ALTER TABLE public.hk_message_recipient_dtl OWNER TO postgres;

--
-- TOC entry 195 (class 1259 OID 95356)
-- Name: hk_notification_info; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE hk_notification_info (
    id bigint NOT NULL,
    description character varying(5000) NOT NULL,
    franchise bigint NOT NULL,
    instance_id bigint,
    instance_type character varying(50),
    is_archive boolean NOT NULL,
    notification_type character varying(20) NOT NULL,
    on_date timestamp without time zone NOT NULL
);


ALTER TABLE public.hk_notification_info OWNER TO postgres;

--
-- TOC entry 194 (class 1259 OID 95354)
-- Name: hk_notification_info_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE hk_notification_info_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.hk_notification_info_id_seq OWNER TO postgres;

--
-- TOC entry 2817 (class 0 OID 0)
-- Dependencies: 194
-- Name: hk_notification_info_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE hk_notification_info_id_seq OWNED BY hk_notification_info.id;


--
-- TOC entry 196 (class 1259 OID 95365)
-- Name: hk_notification_user_rel_info; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE hk_notification_user_rel_info (
    for_user bigint NOT NULL,
    notification bigint NOT NULL,
    is_archive boolean NOT NULL,
    is_seen boolean NOT NULL,
    last_modified_on timestamp without time zone NOT NULL,
    seen_on timestamp without time zone,
    status character varying(10) NOT NULL
);


ALTER TABLE public.hk_notification_user_rel_info OWNER TO postgres;

--
-- TOC entry 198 (class 1259 OID 95372)
-- Name: hk_section_info; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE hk_section_info (
    id bigint NOT NULL,
    created_on timestamp without time zone NOT NULL,
    is_archive boolean NOT NULL,
    section_name character varying(200) NOT NULL
);


ALTER TABLE public.hk_section_info OWNER TO postgres;

--
-- TOC entry 197 (class 1259 OID 95370)
-- Name: hk_section_info_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE hk_section_info_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.hk_section_info_id_seq OWNER TO postgres;

--
-- TOC entry 2818 (class 0 OID 0)
-- Dependencies: 197
-- Name: hk_section_info_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE hk_section_info_id_seq OWNED BY hk_section_info.id;


--
-- TOC entry 199 (class 1259 OID 95378)
-- Name: hk_shift_department_info; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE hk_shift_department_info (
    department bigint NOT NULL,
    shift bigint NOT NULL,
    is_archive boolean NOT NULL,
    last_modified_on timestamp without time zone NOT NULL
);


ALTER TABLE public.hk_shift_department_info OWNER TO postgres;

--
-- TOC entry 201 (class 1259 OID 95385)
-- Name: hk_shift_dtl; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE hk_shift_dtl (
    id bigint NOT NULL,
    created_by bigint NOT NULL,
    created_on timestamp without time zone NOT NULL,
    effected_end timestamp without time zone,
    effected_frm timestamp without time zone,
    end_time timestamp without time zone NOT NULL,
    is_archive boolean NOT NULL,
    shift_duration_min integer,
    slot_title character varying(100),
    slot_type character varying(5),
    strt_time timestamp without time zone NOT NULL,
    week_days character varying(100) NOT NULL,
    shift bigint NOT NULL
);


ALTER TABLE public.hk_shift_dtl OWNER TO postgres;

--
-- TOC entry 200 (class 1259 OID 95383)
-- Name: hk_shift_dtl_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE hk_shift_dtl_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.hk_shift_dtl_id_seq OWNER TO postgres;

--
-- TOC entry 2819 (class 0 OID 0)
-- Dependencies: 200
-- Name: hk_shift_dtl_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE hk_shift_dtl_id_seq OWNED BY hk_shift_dtl.id;


--
-- TOC entry 202 (class 1259 OID 95391)
-- Name: hk_shift_rule_info; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE hk_shift_rule_info (
    rule_type character varying(5) NOT NULL,
    shift bigint NOT NULL,
    day_cnt integer,
    event_action character varying(5),
    event_instance numeric(19,2),
    event_type character varying(5),
    is_archive boolean NOT NULL
);


ALTER TABLE public.hk_shift_rule_info OWNER TO postgres;

--
-- TOC entry 204 (class 1259 OID 95398)
-- Name: hk_system_asset_info; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE hk_system_asset_info (
    id bigint NOT NULL,
    asset_name character varying(100) NOT NULL,
    asset_type character varying(5) NOT NULL,
    barcode character varying(200),
    can_produce_images boolean NOT NULL,
    created_by bigint NOT NULL,
    created_on timestamp without time zone NOT NULL,
    franchise bigint NOT NULL,
    image_path character varying(500),
    inward_dt timestamp without time zone,
    is_archive boolean NOT NULL,
    last_modified_by bigint NOT NULL,
    last_modified_on timestamp without time zone NOT NULL,
    manufacturer numeric(19,2),
    model_number character varying(100),
    purchase_dt timestamp without time zone,
    remaining_units integer,
    remarks character varying(500),
    serial_number integer,
    status character varying(10) NOT NULL,
    category bigint NOT NULL
);


ALTER TABLE public.hk_system_asset_info OWNER TO postgres;

--
-- TOC entry 203 (class 1259 OID 95396)
-- Name: hk_system_asset_info_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE hk_system_asset_info_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.hk_system_asset_info_id_seq OWNER TO postgres;

--
-- TOC entry 2820 (class 0 OID 0)
-- Dependencies: 203
-- Name: hk_system_asset_info_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE hk_system_asset_info_id_seq OWNED BY hk_system_asset_info.id;


--
-- TOC entry 206 (class 1259 OID 95409)
-- Name: hk_system_category_mst; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE hk_system_category_mst (
    id bigint NOT NULL,
    category_prefix character varying(20),
    category_title character varying(200) NOT NULL,
    category_type character varying(5),
    created_by bigint NOT NULL,
    created_on timestamp without time zone NOT NULL,
    current_index integer,
    description character varying(5000),
    franchise bigint NOT NULL,
    have_diamond_processing_mch boolean,
    is_active boolean NOT NULL,
    is_archive boolean NOT NULL,
    last_modified_by bigint NOT NULL,
    last_modified_on timestamp without time zone NOT NULL,
    start_index integer,
    parent bigint
);


ALTER TABLE public.hk_system_category_mst OWNER TO postgres;

--
-- TOC entry 205 (class 1259 OID 95407)
-- Name: hk_system_category_mst_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE hk_system_category_mst_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.hk_system_category_mst_id_seq OWNER TO postgres;

--
-- TOC entry 2821 (class 0 OID 0)
-- Dependencies: 205
-- Name: hk_system_category_mst_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE hk_system_category_mst_id_seq OWNED BY hk_system_category_mst.id;


--
-- TOC entry 207 (class 1259 OID 95418)
-- Name: hk_system_configuration_info; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE hk_system_configuration_info (
    franchise bigint NOT NULL,
    system_key character varying(255) NOT NULL,
    is_archive boolean NOT NULL,
    key_value character varying(255) NOT NULL,
    modified_by bigint NOT NULL,
    modified_on timestamp without time zone NOT NULL
);


ALTER TABLE public.hk_system_configuration_info OWNER TO postgres;

--
-- TOC entry 209 (class 1259 OID 95428)
-- Name: hk_system_event_info; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE hk_system_event_info (
    id bigint NOT NULL,
    address character varying(1000),
    adult_count integer,
    banner_image_name character varying(500),
    child_count integer,
    content_color character varying(50),
    created_by bigint NOT NULL,
    created_on timestamp without time zone NOT NULL,
    description character varying(1000),
    end_time timestamp without time zone,
    event_title character varying(100) NOT NULL,
    folder_name character varying(100),
    franchise bigint NOT NULL,
    frm_dt timestamp without time zone,
    guest_count integer,
    invitation_template_name character varying(200),
    is_archive boolean NOT NULL,
    label_color character varying(50),
    last_modified_by bigint NOT NULL,
    last_modified_on timestamp without time zone NOT NULL,
    not_attending_count integer,
    published_on timestamp without time zone,
    registration_count integer,
    registration_form_name character varying(255),
    registration_last_dt timestamp without time zone,
    registration_type character varying(5) NOT NULL,
    status character varying(10) NOT NULL,
    strt_time timestamp without time zone,
    to_dt timestamp without time zone,
    category bigint NOT NULL
);


ALTER TABLE public.hk_system_event_info OWNER TO postgres;

--
-- TOC entry 208 (class 1259 OID 95426)
-- Name: hk_system_event_info_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE hk_system_event_info_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.hk_system_event_info_id_seq OWNER TO postgres;

--
-- TOC entry 2822 (class 0 OID 0)
-- Dependencies: 208
-- Name: hk_system_event_info_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE hk_system_event_info_id_seq OWNED BY hk_system_event_info.id;


--
-- TOC entry 211 (class 1259 OID 95439)
-- Name: hk_system_holiday_mst; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE hk_system_holiday_mst (
    id bigint NOT NULL,
    created_by bigint NOT NULL,
    created_on timestamp without time zone NOT NULL,
    description character varying(255),
    end_dt timestamp without time zone NOT NULL,
    franchise bigint NOT NULL,
    holiday_title character varying(255) NOT NULL,
    holiday_type character varying(255),
    is_active boolean NOT NULL,
    is_archive boolean NOT NULL,
    last_modified_by bigint NOT NULL,
    last_modified_on timestamp without time zone NOT NULL,
    start_dt timestamp without time zone NOT NULL
);


ALTER TABLE public.hk_system_holiday_mst OWNER TO postgres;

--
-- TOC entry 210 (class 1259 OID 95437)
-- Name: hk_system_holiday_mst_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE hk_system_holiday_mst_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.hk_system_holiday_mst_id_seq OWNER TO postgres;

--
-- TOC entry 2823 (class 0 OID 0)
-- Dependencies: 210
-- Name: hk_system_holiday_mst_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE hk_system_holiday_mst_id_seq OWNED BY hk_system_holiday_mst.id;


--
-- TOC entry 212 (class 1259 OID 95448)
-- Name: hk_system_key_mst; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE hk_system_key_mst (
    code character varying(50) NOT NULL,
    created_by bigint NOT NULL,
    created_on timestamp without time zone NOT NULL,
    description character varying(1000),
    franchise bigint NOT NULL,
    is_active boolean NOT NULL,
    is_archive boolean NOT NULL,
    is_sensitive boolean NOT NULL,
    last_modified_by bigint NOT NULL,
    last_modified_on timestamp without time zone NOT NULL,
    master_name character varying(100) NOT NULL,
    master_type character varying(5),
    precedence smallint NOT NULL
);


ALTER TABLE public.hk_system_key_mst OWNER TO postgres;

--
-- TOC entry 214 (class 1259 OID 95458)
-- Name: hk_system_message_info; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE hk_system_message_info (
    id bigint NOT NULL,
    created_by bigint NOT NULL,
    created_on timestamp without time zone NOT NULL,
    franchise bigint NOT NULL,
    has_priority boolean NOT NULL,
    is_archive boolean NOT NULL,
    message_body character varying(500),
    status character varying(1) NOT NULL
);


ALTER TABLE public.hk_system_message_info OWNER TO postgres;

--
-- TOC entry 213 (class 1259 OID 95456)
-- Name: hk_system_message_info_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE hk_system_message_info_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.hk_system_message_info_id_seq OWNER TO postgres;

--
-- TOC entry 2824 (class 0 OID 0)
-- Dependencies: 213
-- Name: hk_system_message_info_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE hk_system_message_info_id_seq OWNED BY hk_system_message_info.id;


--
-- TOC entry 216 (class 1259 OID 95469)
-- Name: hk_system_shift_info; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE hk_system_shift_info (
    id bigint NOT NULL,
    created_by bigint NOT NULL,
    created_on timestamp without time zone NOT NULL,
    franchise bigint NOT NULL,
    frm_dt timestamp without time zone,
    has_rule boolean NOT NULL,
    is_archive boolean NOT NULL,
    is_default boolean NOT NULL,
    last_modified_by bigint NOT NULL,
    last_modified_on timestamp without time zone NOT NULL,
    shift_title character varying(100) NOT NULL,
    status character varying(10) NOT NULL,
    to_dt timestamp without time zone,
    week_days character varying(100) NOT NULL,
    temporary_shift_for bigint
);


ALTER TABLE public.hk_system_shift_info OWNER TO postgres;

--
-- TOC entry 215 (class 1259 OID 95467)
-- Name: hk_system_shift_info_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE hk_system_shift_info_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.hk_system_shift_info_id_seq OWNER TO postgres;

--
-- TOC entry 2825 (class 0 OID 0)
-- Dependencies: 215
-- Name: hk_system_shift_info_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE hk_system_shift_info_id_seq OWNED BY hk_system_shift_info.id;


--
-- TOC entry 218 (class 1259 OID 95477)
-- Name: hk_system_task_info; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE hk_system_task_info (
    id bigint NOT NULL,
    after_units integer,
    created_by bigint NOT NULL,
    created_on timestamp without time zone NOT NULL,
    due_dt timestamp without time zone,
    end_date timestamp without time zone,
    end_repeat_mode character varying(5),
    franchise bigint NOT NULL,
    is_archive boolean NOT NULL,
    is_repetative boolean NOT NULL,
    last_modified_on timestamp without time zone NOT NULL,
    monthly_on_day integer,
    parent bigint,
    repeatative_mode character varying(5),
    repetition_cnt integer,
    status character varying(5) NOT NULL,
    task_category bigint,
    task_name character varying(1000) NOT NULL,
    weekly_on_days character varying(50)
);


ALTER TABLE public.hk_system_task_info OWNER TO postgres;

--
-- TOC entry 217 (class 1259 OID 95475)
-- Name: hk_system_task_info_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE hk_system_task_info_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.hk_system_task_info_id_seq OWNER TO postgres;

--
-- TOC entry 2826 (class 0 OID 0)
-- Dependencies: 217
-- Name: hk_system_task_info_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE hk_system_task_info_id_seq OWNED BY hk_system_task_info.id;


--
-- TOC entry 220 (class 1259 OID 95488)
-- Name: hk_system_values_mst; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE hk_system_values_mst (
    id bigint NOT NULL,
    created_by bigint NOT NULL,
    created_on timestamp without time zone NOT NULL,
    franchise bigint NOT NULL,
    is_active boolean NOT NULL,
    is_archive boolean NOT NULL,
    is_often_used boolean NOT NULL,
    last_modified_by bigint NOT NULL,
    last_modified_on timestamp without time zone NOT NULL,
    shortcut_code integer,
    value_name character varying(1000) NOT NULL,
    key_code character varying(50) NOT NULL
);


ALTER TABLE public.hk_system_values_mst OWNER TO postgres;

--
-- TOC entry 219 (class 1259 OID 95486)
-- Name: hk_system_values_mst_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE hk_system_values_mst_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.hk_system_values_mst_id_seq OWNER TO postgres;

--
-- TOC entry 2827 (class 0 OID 0)
-- Dependencies: 219
-- Name: hk_system_values_mst_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE hk_system_values_mst_id_seq OWNED BY hk_system_values_mst.id;


--
-- TOC entry 222 (class 1259 OID 95499)
-- Name: hk_task_recipient_dtl; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE hk_task_recipient_dtl (
    id bigint NOT NULL,
    attended_on timestamp without time zone,
    category bigint,
    completed_on timestamp without time zone,
    due_date timestamp without time zone NOT NULL,
    is_archive boolean NOT NULL,
    on_date timestamp without time zone NOT NULL,
    remarks character varying(1000),
    repetition_count integer,
    status character varying(5) NOT NULL,
    user_id bigint NOT NULL,
    task bigint NOT NULL
);


ALTER TABLE public.hk_task_recipient_dtl OWNER TO postgres;

--
-- TOC entry 221 (class 1259 OID 95497)
-- Name: hk_task_recipient_dtl_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE hk_task_recipient_dtl_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.hk_task_recipient_dtl_id_seq OWNER TO postgres;

--
-- TOC entry 2828 (class 0 OID 0)
-- Dependencies: 221
-- Name: hk_task_recipient_dtl_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE hk_task_recipient_dtl_id_seq OWNED BY hk_task_recipient_dtl.id;


--
-- TOC entry 223 (class 1259 OID 95508)
-- Name: hk_task_recipient_info; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE hk_task_recipient_info (
    reference_instance bigint NOT NULL,
    reference_type character varying(5) NOT NULL,
    task bigint NOT NULL,
    is_archive boolean NOT NULL
);


ALTER TABLE public.hk_task_recipient_info OWNER TO postgres;

--
-- TOC entry 224 (class 1259 OID 95513)
-- Name: hk_user_attendance_info; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE hk_user_attendance_info (
    on_date date NOT NULL,
    user_id bigint NOT NULL,
    attendance_type character varying(5) NOT NULL,
    franchise bigint NOT NULL,
    is_archive boolean NOT NULL,
    login_hours real NOT NULL,
    office_hours real NOT NULL,
    shift_hours real NOT NULL
);


ALTER TABLE public.hk_user_attendance_info OWNER TO postgres;

--
-- TOC entry 225 (class 1259 OID 95518)
-- Name: hk_user_event_registration_info; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE hk_user_event_registration_info (
    event bigint NOT NULL,
    user_id bigint NOT NULL,
    adult_count integer NOT NULL,
    child_count integer NOT NULL,
    guest_count integer NOT NULL,
    guest_info character varying(5000),
    is_archive boolean NOT NULL,
    last_modified_on timestamp without time zone NOT NULL,
    reason character varying(500),
    status character varying(5) NOT NULL
);


ALTER TABLE public.hk_user_event_registration_info OWNER TO postgres;

--
-- TOC entry 227 (class 1259 OID 95528)
-- Name: hk_user_histry_dtl; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE hk_user_histry_dtl (
    id bigint NOT NULL,
    department bigint NOT NULL,
    designation character varying(255),
    effected_frm timestamp without time zone,
    effected_to timestamp without time zone,
    is_archive boolean NOT NULL,
    reports_to character varying(255),
    shift bigint NOT NULL,
    user_id bigint NOT NULL,
    linked_with bigint
);


ALTER TABLE public.hk_user_histry_dtl OWNER TO postgres;

--
-- TOC entry 226 (class 1259 OID 95526)
-- Name: hk_user_histry_dtl_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE hk_user_histry_dtl_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.hk_user_histry_dtl_id_seq OWNER TO postgres;

--
-- TOC entry 2829 (class 0 OID 0)
-- Dependencies: 226
-- Name: hk_user_histry_dtl_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE hk_user_histry_dtl_id_seq OWNED BY hk_user_histry_dtl.id;


--
-- TOC entry 228 (class 1259 OID 95537)
-- Name: hk_user_operation_info; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE hk_user_operation_info (
    created_on timestamp without time zone NOT NULL,
    event_code integer NOT NULL,
    user_id bigint NOT NULL,
    comments character varying(1000),
    franchise bigint NOT NULL,
    is_archive boolean NOT NULL,
    is_attended boolean NOT NULL,
    last_modified_by bigint,
    last_modified_on timestamp without time zone NOT NULL,
    on_time timestamp without time zone NOT NULL,
    shift_dtl bigint NOT NULL,
    status character varying(5) NOT NULL
);


ALTER TABLE public.hk_user_operation_info OWNER TO postgres;

--
-- TOC entry 229 (class 1259 OID 95545)
-- Name: hk_usertype_seq_mst; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE hk_usertype_seq_mst (
    franchise bigint NOT NULL,
    user_type bigint NOT NULL,
    current_value integer NOT NULL,
    is_archive boolean NOT NULL
);


ALTER TABLE public.hk_usertype_seq_mst OWNER TO postgres;

--
-- TOC entry 230 (class 1259 OID 95550)
-- Name: hk_workflow_approver_rel_info; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE hk_workflow_approver_rel_info (
    reference_instance bigint NOT NULL,
    reference_type character varying(5) NOT NULL,
    workflow bigint NOT NULL,
    is_archive boolean NOT NULL,
    level bigint NOT NULL
);


ALTER TABLE public.hk_workflow_approver_rel_info OWNER TO postgres;

--
-- TOC entry 232 (class 1259 OID 95557)
-- Name: hk_workflow_info; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE hk_workflow_info (
    id bigint NOT NULL,
    created_by bigint NOT NULL,
    created_on timestamp without time zone NOT NULL,
    department bigint,
    franchise bigint NOT NULL,
    is_archive boolean NOT NULL,
    last_level integer,
    last_modified_by bigint NOT NULL,
    last_modified_on timestamp without time zone NOT NULL
);


ALTER TABLE public.hk_workflow_info OWNER TO postgres;

--
-- TOC entry 231 (class 1259 OID 95555)
-- Name: hk_workflow_info_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE hk_workflow_info_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.hk_workflow_info_id_seq OWNER TO postgres;

--
-- TOC entry 2830 (class 0 OID 0)
-- Dependencies: 231
-- Name: hk_workflow_info_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE hk_workflow_info_id_seq OWNED BY hk_workflow_info.id;


--
-- TOC entry 233 (class 1259 OID 95563)
-- Name: internationalization_country_master; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE internationalization_country_master (
    code character varying(255) NOT NULL,
    created_by bigint NOT NULL,
    created_on timestamp without time zone NOT NULL,
    inactive_reason character varying(255),
    is_active boolean NOT NULL,
    is_archive boolean NOT NULL,
    modified_by bigint,
    modified_on timestamp without time zone,
    name character varying(255) NOT NULL
);


ALTER TABLE public.internationalization_country_master OWNER TO postgres;

--
-- TOC entry 234 (class 1259 OID 95571)
-- Name: internationalization_currency_master; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE internationalization_currency_master (
    code character varying(255) NOT NULL,
    country character varying(255) NOT NULL,
    created_by bigint NOT NULL,
    created_on timestamp without time zone NOT NULL,
    decimal_digit smallint NOT NULL,
    inactive_reason character varying(255),
    is_active boolean NOT NULL,
    is_archive boolean NOT NULL,
    modified_by bigint,
    modified_on timestamp without time zone,
    name character varying(255) NOT NULL,
    symbol character varying(255)
);


ALTER TABLE public.internationalization_currency_master OWNER TO postgres;

--
-- TOC entry 235 (class 1259 OID 95579)
-- Name: internationalization_label_master; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE internationalization_label_master (
    company bigint NOT NULL,
    country character varying(255) NOT NULL,
    entity character varying(255) NOT NULL,
    key character varying(255) NOT NULL,
    language character varying(255) NOT NULL,
    type character varying(255) NOT NULL,
    custom3b boolean,
    environment character varying(255) NOT NULL,
    last_modified_by bigint NOT NULL,
    last_modified_on timestamp without time zone NOT NULL,
    text character varying(255) NOT NULL,
    translation_pending boolean
);


ALTER TABLE public.internationalization_label_master OWNER TO postgres;

--
-- TOC entry 236 (class 1259 OID 95587)
-- Name: internationalization_language_master; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE internationalization_language_master (
    code character varying(255) NOT NULL,
    country character varying(255) NOT NULL,
    character_encoding character varying(255),
    created_by bigint NOT NULL,
    created_on timestamp without time zone NOT NULL,
    inactive_reason character varying(255),
    is_active boolean NOT NULL,
    is_archive boolean NOT NULL,
    is_left_to_right boolean NOT NULL,
    modified_by bigint,
    modified_on timestamp without time zone,
    name character varying(255) NOT NULL
);


ALTER TABLE public.internationalization_language_master OWNER TO postgres;

--
-- TOC entry 238 (class 1259 OID 95597)
-- Name: internationalization_timezone_location_detail; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE internationalization_timezone_location_detail (
    id bigint NOT NULL,
    created_by bigint NOT NULL,
    created_on timestamp without time zone NOT NULL,
    inactive_reason character varying(255),
    is_active boolean NOT NULL,
    is_archive boolean NOT NULL,
    location character varying(255) NOT NULL,
    modified_by bigint,
    modified_on timestamp without time zone,
    timezone character varying(255)
);


ALTER TABLE public.internationalization_timezone_location_detail OWNER TO postgres;

--
-- TOC entry 237 (class 1259 OID 95595)
-- Name: internationalization_timezone_location_detail_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE internationalization_timezone_location_detail_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.internationalization_timezone_location_detail_id_seq OWNER TO postgres;

--
-- TOC entry 2831 (class 0 OID 0)
-- Dependencies: 237
-- Name: internationalization_timezone_location_detail_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE internationalization_timezone_location_detail_id_seq OWNED BY internationalization_timezone_location_detail.id;


--
-- TOC entry 239 (class 1259 OID 95606)
-- Name: internationalization_timezone_master; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE internationalization_timezone_master (
    id character varying(255) NOT NULL,
    created_by bigint NOT NULL,
    created_on timestamp without time zone NOT NULL,
    g_m_t_representation character varying(255),
    inactive_reason character varying(255),
    is_active boolean NOT NULL,
    is_archive boolean NOT NULL,
    modified_by bigint,
    modified_on timestamp without time zone,
    name character varying(255) NOT NULL,
    country character varying(255) NOT NULL
);


ALTER TABLE public.internationalization_timezone_master OWNER TO postgres;

--
-- TOC entry 240 (class 1259 OID 95614)
-- Name: rb_field_converter_dtl; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE rb_field_converter_dtl (
    converter_key character varying(50) NOT NULL,
    report_field bigint NOT NULL,
    converter_value character varying(500) NOT NULL,
    is_archive boolean,
    last_modified_by bigint NOT NULL,
    last_modified_on timestamp without time zone NOT NULL
);


ALTER TABLE public.rb_field_converter_dtl OWNER TO postgres;

--
-- TOC entry 241 (class 1259 OID 95622)
-- Name: rb_field_formatter_dtl; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE rb_field_formatter_dtl (
    field_formatter character varying(50) NOT NULL,
    report_field bigint NOT NULL,
    attributes character varying(255),
    is_archive boolean,
    last_modified_by bigint NOT NULL,
    last_modified_on timestamp without time zone NOT NULL
);


ALTER TABLE public.rb_field_formatter_dtl OWNER TO postgres;

--
-- TOC entry 243 (class 1259 OID 95629)
-- Name: rb_report_access_dtl; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE rb_report_access_dtl (
    id bigint NOT NULL,
    access_to bigint NOT NULL,
    access_to_type character varying(50) NOT NULL,
    is_archive boolean NOT NULL,
    last_modified_by bigint NOT NULL,
    last_modified_on timestamp without time zone NOT NULL,
    report bigint NOT NULL
);


ALTER TABLE public.rb_report_access_dtl OWNER TO postgres;

--
-- TOC entry 242 (class 1259 OID 95627)
-- Name: rb_report_access_dtl_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE rb_report_access_dtl_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.rb_report_access_dtl_id_seq OWNER TO postgres;

--
-- TOC entry 2832 (class 0 OID 0)
-- Dependencies: 242
-- Name: rb_report_access_dtl_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE rb_report_access_dtl_id_seq OWNED BY rb_report_access_dtl.id;


--
-- TOC entry 245 (class 1259 OID 95637)
-- Name: rb_report_field_dtl; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE rb_report_field_dtl (
    id bigint NOT NULL,
    col_i18n_req boolean NOT NULL,
    db_field_name character varying(200) NOT NULL,
    field_data_type character varying(255) NOT NULL,
    is_archive boolean NOT NULL,
    is_default_visible boolean NOT NULL,
    last_modified_by bigint NOT NULL,
    last_modified_on timestamp without time zone NOT NULL,
    report_field_name character varying(500) NOT NULL,
    row_i18n_req boolean NOT NULL,
    report bigint NOT NULL
);


ALTER TABLE public.rb_report_field_dtl OWNER TO postgres;

--
-- TOC entry 244 (class 1259 OID 95635)
-- Name: rb_report_field_dtl_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE rb_report_field_dtl_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.rb_report_field_dtl_id_seq OWNER TO postgres;

--
-- TOC entry 2833 (class 0 OID 0)
-- Dependencies: 244
-- Name: rb_report_field_dtl_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE rb_report_field_dtl_id_seq OWNED BY rb_report_field_dtl.id;


--
-- TOC entry 247 (class 1259 OID 95648)
-- Name: rb_report_mst; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE rb_report_mst (
    id bigint NOT NULL,
    created_by bigint NOT NULL,
    created_on timestamp without time zone NOT NULL,
    custom1 numeric(19,2),
    custom2 character varying(1000),
    description character varying(500),
    is_active boolean NOT NULL,
    is_archive boolean NOT NULL,
    is_editable boolean,
    is_external_report boolean NOT NULL,
    last_modified_by bigint NOT NULL,
    last_modified_on timestamp without time zone NOT NULL,
    linked_with numeric(19,2),
    report_code character varying(100),
    report_name character varying(50) NOT NULL,
    report_query character varying(255),
    status character varying(5) NOT NULL
);


ALTER TABLE public.rb_report_mst OWNER TO postgres;

--
-- TOC entry 246 (class 1259 OID 95646)
-- Name: rb_report_mst_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE rb_report_mst_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.rb_report_mst_id_seq OWNER TO postgres;

--
-- TOC entry 2834 (class 0 OID 0)
-- Dependencies: 246
-- Name: rb_report_mst_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE rb_report_mst_id_seq OWNED BY rb_report_mst.id;


--
-- TOC entry 249 (class 1259 OID 95659)
-- Name: sync_client_feature_details; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE sync_client_feature_details (
    id bigint NOT NULL,
    client_id bigint NOT NULL,
    created_by bigint NOT NULL,
    created_on timestamp without time zone NOT NULL,
    custom1 character varying(255),
    custom2 character varying(255),
    is_active boolean NOT NULL,
    is_archive boolean NOT NULL,
    modified_by bigint NOT NULL,
    modified_on timestamp without time zone NOT NULL,
    synced_on bigint,
    feature_config_id bigint NOT NULL
);


ALTER TABLE public.sync_client_feature_details OWNER TO postgres;

--
-- TOC entry 248 (class 1259 OID 95657)
-- Name: sync_client_feature_details_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE sync_client_feature_details_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.sync_client_feature_details_id_seq OWNER TO postgres;

--
-- TOC entry 2835 (class 0 OID 0)
-- Dependencies: 248
-- Name: sync_client_feature_details_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE sync_client_feature_details_id_seq OWNED BY sync_client_feature_details.id;


--
-- TOC entry 251 (class 1259 OID 95670)
-- Name: sync_config_master; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE sync_config_master (
    id bigint NOT NULL,
    config_name character varying(255) NOT NULL,
    created_by bigint NOT NULL,
    created_on timestamp without time zone NOT NULL,
    custom1 character varying(255),
    custom2 character varying(255),
    is_active boolean NOT NULL,
    is_archive boolean NOT NULL,
    modified_by bigint NOT NULL,
    modified_on timestamp without time zone NOT NULL
);


ALTER TABLE public.sync_config_master OWNER TO postgres;

--
-- TOC entry 250 (class 1259 OID 95668)
-- Name: sync_config_master_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE sync_config_master_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.sync_config_master_id_seq OWNER TO postgres;

--
-- TOC entry 2836 (class 0 OID 0)
-- Dependencies: 250
-- Name: sync_config_master_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE sync_config_master_id_seq OWNED BY sync_config_master.id;


--
-- TOC entry 253 (class 1259 OID 95681)
-- Name: sync_config_parameter_details; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE sync_config_parameter_details (
    id bigint NOT NULL,
    created_by bigint NOT NULL,
    created_on timestamp without time zone NOT NULL,
    custom1 character varying(255),
    custom2 character varying(255),
    is_active boolean NOT NULL,
    is_archive boolean NOT NULL,
    modified_by bigint NOT NULL,
    modified_on timestamp without time zone NOT NULL,
    parameter_id bigint NOT NULL,
    parameter_value character varying(255),
    config_id bigint NOT NULL
);


ALTER TABLE public.sync_config_parameter_details OWNER TO postgres;

--
-- TOC entry 252 (class 1259 OID 95679)
-- Name: sync_config_parameter_details_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE sync_config_parameter_details_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.sync_config_parameter_details_id_seq OWNER TO postgres;

--
-- TOC entry 2837 (class 0 OID 0)
-- Dependencies: 252
-- Name: sync_config_parameter_details_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE sync_config_parameter_details_id_seq OWNED BY sync_config_parameter_details.id;


--
-- TOC entry 255 (class 1259 OID 95692)
-- Name: sync_feature_config_details; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE sync_feature_config_details (
    id bigint NOT NULL,
    config_id bigint,
    created_by bigint NOT NULL,
    created_on timestamp without time zone NOT NULL,
    custom1 character varying(255),
    custom2 character varying(255),
    feature_id bigint NOT NULL,
    is_active boolean NOT NULL,
    is_archive boolean NOT NULL,
    modified_by bigint NOT NULL,
    modified_on timestamp without time zone NOT NULL
);


ALTER TABLE public.sync_feature_config_details OWNER TO postgres;

--
-- TOC entry 254 (class 1259 OID 95690)
-- Name: sync_feature_config_details_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE sync_feature_config_details_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.sync_feature_config_details_id_seq OWNER TO postgres;

--
-- TOC entry 2838 (class 0 OID 0)
-- Dependencies: 254
-- Name: sync_feature_config_details_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE sync_feature_config_details_id_seq OWNED BY sync_feature_config_details.id;


--
-- TOC entry 257 (class 1259 OID 95703)
-- Name: sync_feature_master; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE sync_feature_master (
    id bigint NOT NULL,
    created_by bigint NOT NULL,
    created_on timestamp without time zone NOT NULL,
    custom1 integer,
    custom2 character varying(255),
    description character varying(255),
    feature_name character varying(255) NOT NULL,
    is_active boolean NOT NULL,
    is_archive boolean NOT NULL,
    modified_by bigint,
    modified_on timestamp without time zone
);


ALTER TABLE public.sync_feature_master OWNER TO postgres;

--
-- TOC entry 256 (class 1259 OID 95701)
-- Name: sync_feature_master_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE sync_feature_master_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.sync_feature_master_id_seq OWNER TO postgres;

--
-- TOC entry 2839 (class 0 OID 0)
-- Dependencies: 256
-- Name: sync_feature_master_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE sync_feature_master_id_seq OWNED BY sync_feature_master.id;


--
-- TOC entry 259 (class 1259 OID 95714)
-- Name: sync_parameter_master; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE sync_parameter_master (
    id bigint NOT NULL,
    created_by bigint NOT NULL,
    created_on timestamp without time zone NOT NULL,
    custom1 character varying(255),
    custom2 character varying(255),
    description character varying(255),
    is_active boolean NOT NULL,
    is_archive boolean NOT NULL,
    modified_by bigint,
    modified_on timestamp without time zone,
    parameter_default_value character varying(255),
    parameter_name character varying(255) NOT NULL
);


ALTER TABLE public.sync_parameter_master OWNER TO postgres;

--
-- TOC entry 258 (class 1259 OID 95712)
-- Name: sync_parameter_master_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE sync_parameter_master_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.sync_parameter_master_id_seq OWNER TO postgres;

--
-- TOC entry 2840 (class 0 OID 0)
-- Dependencies: 258
-- Name: sync_parameter_master_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE sync_parameter_master_id_seq OWNED BY sync_parameter_master.id;


--
-- TOC entry 261 (class 1259 OID 95725)
-- Name: sync_sync_status; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE sync_sync_status (
    id bigint NOT NULL,
    client_request_time timestamp without time zone NOT NULL,
    client_status character varying(255) NOT NULL,
    created_on timestamp without time zone NOT NULL,
    custom1 character varying(255),
    custom2 integer,
    custom3 character varying(255),
    error_message character varying(255),
    is_active boolean NOT NULL,
    is_archive boolean NOT NULL,
    json_data_recieved character varying(255) NOT NULL,
    json_data_sent character varying(255) NOT NULL,
    modified_on timestamp without time zone NOT NULL,
    server_response_time timestamp without time zone,
    server_status character varying(255),
    client_feture_details_id bigint NOT NULL
);


ALTER TABLE public.sync_sync_status OWNER TO postgres;

--
-- TOC entry 260 (class 1259 OID 95723)
-- Name: sync_sync_status_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE sync_sync_status_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.sync_sync_status_id_seq OWNER TO postgres;

--
-- TOC entry 2841 (class 0 OID 0)
-- Dependencies: 260
-- Name: sync_sync_status_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE sync_sync_status_id_seq OWNED BY sync_sync_status.id;


--
-- TOC entry 263 (class 1259 OID 95736)
-- Name: um_company_info; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE um_company_info (
    id bigint NOT NULL,
    activated_by bigint,
    activated_on timestamp without time zone,
    active_users integer,
    address character varying(255),
    alternate_contact_number character varying(255),
    city character varying(255),
    contact_number character varying(500),
    country character varying(255),
    created_by bigint NOT NULL,
    created_on timestamp without time zone NOT NULL,
    custom1 bigint,
    custom2 character varying(255),
    custom3 timestamp without time zone,
    description character varying(255),
    email_address character varying(500),
    is_active boolean NOT NULL,
    is_archive boolean NOT NULL,
    modified_by bigint,
    modified_on timestamp without time zone,
    name character varying(255) NOT NULL,
    province character varying(255),
    status character varying(255) NOT NULL,
    website_url character varying(255),
    zip_code character varying(255)
);


ALTER TABLE public.um_company_info OWNER TO postgres;

--
-- TOC entry 262 (class 1259 OID 95734)
-- Name: um_company_info_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE um_company_info_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.um_company_info_id_seq OWNER TO postgres;

--
-- TOC entry 2842 (class 0 OID 0)
-- Dependencies: 262
-- Name: um_company_info_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE um_company_info_id_seq OWNED BY um_company_info.id;


--
-- TOC entry 265 (class 1259 OID 95747)
-- Name: um_contact_address_dtl; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE um_contact_address_dtl (
    id bigint NOT NULL,
    address_line1 character varying(255),
    address_line2 character varying(255),
    address_type character varying(255),
    city character varying(255),
    custom1 bigint,
    custom2 boolean,
    custom3 character varying(255),
    custom4 timestamp without time zone,
    district character varying(255),
    is_archive boolean NOT NULL,
    landmark character varying(255),
    last_modified_by bigint NOT NULL,
    last_modified_on timestamp without time zone NOT NULL,
    state character varying(255),
    user_contact bigint,
    zip_code character varying(255)
);


ALTER TABLE public.um_contact_address_dtl OWNER TO postgres;

--
-- TOC entry 264 (class 1259 OID 95745)
-- Name: um_contact_address_dtl_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE um_contact_address_dtl_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.um_contact_address_dtl_id_seq OWNER TO postgres;

--
-- TOC entry 2843 (class 0 OID 0)
-- Dependencies: 264
-- Name: um_contact_address_dtl_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE um_contact_address_dtl_id_seq OWNED BY um_contact_address_dtl.id;


--
-- TOC entry 267 (class 1259 OID 95758)
-- Name: um_contact_document_dtl; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE um_contact_document_dtl (
    id bigint NOT NULL,
    custom1 bigint,
    custom2 boolean,
    custom3 character varying(255),
    custom4 timestamp without time zone,
    document_name character varying(255),
    document_type character varying(255),
    is_archive boolean NOT NULL,
    last_modified_by bigint NOT NULL,
    last_modified_on timestamp without time zone NOT NULL,
    remarks character varying(255),
    user_contact bigint
);


ALTER TABLE public.um_contact_document_dtl OWNER TO postgres;

--
-- TOC entry 266 (class 1259 OID 95756)
-- Name: um_contact_document_dtl_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE um_contact_document_dtl_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.um_contact_document_dtl_id_seq OWNER TO postgres;

--
-- TOC entry 2844 (class 0 OID 0)
-- Dependencies: 266
-- Name: um_contact_document_dtl_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE um_contact_document_dtl_id_seq OWNED BY um_contact_document_dtl.id;


--
-- TOC entry 269 (class 1259 OID 95769)
-- Name: um_contact_education_dtl; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE um_contact_education_dtl (
    id bigint NOT NULL,
    custom1 bigint,
    custom2 boolean,
    custom3 character varying(255),
    custom4 timestamp without time zone,
    degree character varying(255) NOT NULL,
    is_archive boolean NOT NULL,
    last_modified_by bigint NOT NULL,
    last_modified_on timestamp without time zone NOT NULL,
    medium character varying(255),
    percentage numeric(19,2),
    state character varying(255),
    university character varying(255),
    user_contact bigint,
    year_of_passing integer
);


ALTER TABLE public.um_contact_education_dtl OWNER TO postgres;

--
-- TOC entry 268 (class 1259 OID 95767)
-- Name: um_contact_education_dtl_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE um_contact_education_dtl_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.um_contact_education_dtl_id_seq OWNER TO postgres;

--
-- TOC entry 2845 (class 0 OID 0)
-- Dependencies: 268
-- Name: um_contact_education_dtl_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE um_contact_education_dtl_id_seq OWNED BY um_contact_education_dtl.id;


--
-- TOC entry 271 (class 1259 OID 95780)
-- Name: um_contact_experience_dtl; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE um_contact_experience_dtl (
    id bigint NOT NULL,
    company character varying(255),
    custom1 bigint,
    custom2 boolean,
    custom3 character varying(255),
    custom4 timestamp without time zone,
    designation character varying(255),
    employment_type character varying(255),
    is_archive boolean NOT NULL,
    last_modified_by bigint NOT NULL,
    last_modified_on timestamp without time zone NOT NULL,
    reason_of_leaving character varying(255),
    remarks character varying(255),
    salary numeric(19,2),
    salary_slip_file_name character varying(255),
    started_from timestamp without time zone,
    user_contact bigint,
    worked_till timestamp without time zone
);


ALTER TABLE public.um_contact_experience_dtl OWNER TO postgres;

--
-- TOC entry 270 (class 1259 OID 95778)
-- Name: um_contact_experience_dtl_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE um_contact_experience_dtl_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.um_contact_experience_dtl_id_seq OWNER TO postgres;

--
-- TOC entry 2846 (class 0 OID 0)
-- Dependencies: 270
-- Name: um_contact_experience_dtl_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE um_contact_experience_dtl_id_seq OWNED BY um_contact_experience_dtl.id;


--
-- TOC entry 273 (class 1259 OID 95791)
-- Name: um_contact_insurance_dtl; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE um_contact_insurance_dtl (
    id bigint NOT NULL,
    company character varying(255),
    custom1 bigint,
    custom2 boolean,
    custom3 character varying(255),
    custom4 timestamp without time zone,
    is_archive boolean NOT NULL,
    last_modified_by bigint NOT NULL,
    last_modified_on timestamp without time zone NOT NULL,
    policy_name character varying(255),
    policy_number character varying(255),
    status character varying(255),
    user_contact bigint
);


ALTER TABLE public.um_contact_insurance_dtl OWNER TO postgres;

--
-- TOC entry 272 (class 1259 OID 95789)
-- Name: um_contact_insurance_dtl_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE um_contact_insurance_dtl_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.um_contact_insurance_dtl_id_seq OWNER TO postgres;

--
-- TOC entry 2847 (class 0 OID 0)
-- Dependencies: 272
-- Name: um_contact_insurance_dtl_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE um_contact_insurance_dtl_id_seq OWNED BY um_contact_insurance_dtl.id;


--
-- TOC entry 275 (class 1259 OID 95802)
-- Name: um_contact_language_knwn_dtl; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE um_contact_language_knwn_dtl (
    id bigint NOT NULL,
    can_read boolean NOT NULL,
    can_speak boolean NOT NULL,
    can_write boolean NOT NULL,
    custom1 bigint,
    custom2 boolean,
    custom3 character varying(255),
    custom4 timestamp without time zone,
    is_archive boolean NOT NULL,
    language_name character varying(255),
    last_modified_by bigint NOT NULL,
    last_modified_on timestamp without time zone NOT NULL,
    user_contact bigint
);


ALTER TABLE public.um_contact_language_knwn_dtl OWNER TO postgres;

--
-- TOC entry 274 (class 1259 OID 95800)
-- Name: um_contact_language_knwn_dtl_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE um_contact_language_knwn_dtl_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.um_contact_language_knwn_dtl_id_seq OWNER TO postgres;

--
-- TOC entry 2848 (class 0 OID 0)
-- Dependencies: 274
-- Name: um_contact_language_knwn_dtl_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE um_contact_language_knwn_dtl_id_seq OWNED BY um_contact_language_knwn_dtl.id;


--
-- TOC entry 277 (class 1259 OID 95813)
-- Name: um_dept_mst; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE um_dept_mst (
    id bigint NOT NULL,
    active_users integer,
    company bigint,
    created_by bigint,
    created_on timestamp without time zone NOT NULL,
    dept_code character varying(255),
    dept_name character varying(255) NOT NULL,
    description character varying(255),
    is_active boolean NOT NULL,
    is_archive boolean NOT NULL,
    last_modified_by bigint NOT NULL,
    last_modified_on timestamp without time zone,
    parent bigint
);


ALTER TABLE public.um_dept_mst OWNER TO postgres;

--
-- TOC entry 276 (class 1259 OID 95811)
-- Name: um_dept_mst_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE um_dept_mst_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.um_dept_mst_id_seq OWNER TO postgres;

--
-- TOC entry 2849 (class 0 OID 0)
-- Dependencies: 276
-- Name: um_dept_mst_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE um_dept_mst_id_seq OWNED BY um_dept_mst.id;


--
-- TOC entry 279 (class 1259 OID 95824)
-- Name: um_dept_role_dtl; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE um_dept_role_dtl (
    id bigint NOT NULL,
    dept bigint,
    is_active boolean NOT NULL,
    role bigint
);


ALTER TABLE public.um_dept_role_dtl OWNER TO postgres;

--
-- TOC entry 278 (class 1259 OID 95822)
-- Name: um_dept_role_dtl_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE um_dept_role_dtl_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.um_dept_role_dtl_id_seq OWNER TO postgres;

--
-- TOC entry 2850 (class 0 OID 0)
-- Dependencies: 278
-- Name: um_dept_role_dtl_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE um_dept_role_dtl_id_seq OWNED BY um_dept_role_dtl.id;


--
-- TOC entry 280 (class 1259 OID 95830)
-- Name: um_group_contact; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE um_group_contact (
    contact bigint NOT NULL,
    user_group bigint NOT NULL,
    is_active boolean NOT NULL,
    is_archive boolean NOT NULL
);


ALTER TABLE public.um_group_contact OWNER TO postgres;

--
-- TOC entry 282 (class 1259 OID 95837)
-- Name: um_role_feature; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE um_role_feature (
    id bigint NOT NULL,
    allow_to_create boolean NOT NULL,
    allow_to_delete boolean NOT NULL,
    allow_to_update boolean NOT NULL,
    company bigint,
    description character varying(255),
    feature bigint,
    is_active boolean NOT NULL,
    is_archive boolean NOT NULL,
    role bigint
);


ALTER TABLE public.um_role_feature OWNER TO postgres;

--
-- TOC entry 281 (class 1259 OID 95835)
-- Name: um_role_feature_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE um_role_feature_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.um_role_feature_id_seq OWNER TO postgres;

--
-- TOC entry 2851 (class 0 OID 0)
-- Dependencies: 281
-- Name: um_role_feature_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE um_role_feature_id_seq OWNED BY um_role_feature.id;


--
-- TOC entry 283 (class 1259 OID 95843)
-- Name: um_system_configuration; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE um_system_configuration (
    system_key character varying(255) NOT NULL,
    is_active boolean NOT NULL,
    key_value character varying(255) NOT NULL
);


ALTER TABLE public.um_system_configuration OWNER TO postgres;

--
-- TOC entry 285 (class 1259 OID 95853)
-- Name: um_system_feature; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE um_system_feature (
    id bigint NOT NULL,
    company bigint DEFAULT 0,
    created_by bigint NOT NULL,
    created_on timestamp without time zone NOT NULL,
    description character varying(255),
    feature_url character varying(255),
    is_active boolean NOT NULL,
    is_archive boolean NOT NULL,
    is_crud boolean NOT NULL,
    menu_category character varying(255),
    menu_label character varying(255),
    menu_type character varying(255),
    modified_by bigint,
    modified_on timestamp without time zone,
    name character varying(255) NOT NULL,
    parent bigint,
    precedence integer DEFAULT 0 NOT NULL,
    seq_no integer,
    webservice_url character varying(10000)
);


ALTER TABLE public.um_system_feature OWNER TO postgres;

--
-- TOC entry 284 (class 1259 OID 95851)
-- Name: um_system_feature_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE um_system_feature_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.um_system_feature_id_seq OWNER TO postgres;

--
-- TOC entry 2852 (class 0 OID 0)
-- Dependencies: 284
-- Name: um_system_feature_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE um_system_feature_id_seq OWNED BY um_system_feature.id;


--
-- TOC entry 287 (class 1259 OID 95866)
-- Name: um_system_role; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE um_system_role (
    id bigint NOT NULL,
    company bigint DEFAULT 0,
    created_by bigint NOT NULL,
    created_on timestamp without time zone NOT NULL,
    custom1 bigint,
    custom2 character varying(255),
    custom3 timestamp without time zone,
    description character varying(255),
    inactive_reason character varying(255),
    ip_based_access boolean,
    is_active boolean NOT NULL,
    is_archive boolean NOT NULL,
    modified_by bigint,
    modified_on timestamp without time zone,
    name character varying(255) NOT NULL,
    precedence integer DEFAULT 0 NOT NULL,
    swap_based_access boolean
);


ALTER TABLE public.um_system_role OWNER TO postgres;

--
-- TOC entry 286 (class 1259 OID 95864)
-- Name: um_system_role_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE um_system_role_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.um_system_role_id_seq OWNER TO postgres;

--
-- TOC entry 2853 (class 0 OID 0)
-- Dependencies: 286
-- Name: um_system_role_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE um_system_role_id_seq OWNED BY um_system_role.id;


--
-- TOC entry 289 (class 1259 OID 95879)
-- Name: um_system_user; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE um_system_user (
    id bigint NOT NULL,
    company bigint,
    contact bigint,
    created_by bigint NOT NULL,
    created_on timestamp without time zone NOT NULL,
    custom1 bigint,
    custom2 character varying(255),
    custom3 timestamp without time zone,
    custom4 bigint,
    department bigint,
    email_address character varying(255),
    expired_on timestamp without time zone,
    first_name character varying(255),
    inactive_reason character varying(255),
    is_active boolean NOT NULL,
    is_archive boolean NOT NULL,
    joining_date timestamp without time zone,
    last_login_on timestamp without time zone,
    last_name character varying(255),
    linked_with_user bigint,
    middle_name character varying(255),
    mobile_number character varying(255),
    modified_by bigint,
    modified_on timestamp without time zone,
    password character varying(255),
    preferred_language bigint,
    status character varying(255),
    type character varying(255),
    user_code character varying(255),
    user_id character varying(255) NOT NULL
);


ALTER TABLE public.um_system_user OWNER TO postgres;

--
-- TOC entry 288 (class 1259 OID 95877)
-- Name: um_system_user_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE um_system_user_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.um_system_user_id_seq OWNER TO postgres;

--
-- TOC entry 2854 (class 0 OID 0)
-- Dependencies: 288
-- Name: um_system_user_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE um_system_user_id_seq OWNED BY um_system_user.id;


--
-- TOC entry 290 (class 1259 OID 95888)
-- Name: um_user_access_dtl; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE um_user_access_dtl (
    on_time timestamp without time zone NOT NULL,
    user_id bigint NOT NULL,
    created_by bigint,
    created_on timestamp without time zone,
    event_type character varying(255) NOT NULL,
    remarks character varying(255)
);


ALTER TABLE public.um_user_access_dtl OWNER TO postgres;

--
-- TOC entry 292 (class 1259 OID 95898)
-- Name: um_user_contact; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE um_user_contact (
    id bigint NOT NULL,
    account_number character varying(255),
    adhaar_number character varying(255),
    alternate_contact_number character varying(255),
    alternate_email_address character varying(255),
    bank_name character varying(255),
    blood_group character varying(255),
    caste character varying(255),
    company bigint,
    computer_lietracy character varying(255),
    contact_user bigint,
    created_by bigint NOT NULL,
    created_on timestamp without time zone NOT NULL,
    custom1 bigint,
    custom2 character varying(255),
    custom3 timestamp without time zone,
    date_of_birth timestamp without time zone,
    display_name character varying(255),
    email_address character varying(255),
    facebook_page character varying(255),
    first_name character varying(255),
    gender character varying(255),
    id_card_number character varying(255),
    identification_mark character varying(255),
    inactive_reason character varying(255),
    is_active boolean NOT NULL,
    is_archive boolean NOT NULL,
    last_name character varying(255),
    last_puc_on timestamp without time zone,
    license_number character varying(255),
    marital_status character varying(255),
    middle_name character varying(255),
    mobile_number character varying(255),
    modified_by bigint,
    modified_on timestamp without time zone,
    nationality character varying(255),
    occupation character varying(255),
    pan_number character varying(255),
    passport_expires_on timestamp without time zone,
    passport_issued_on timestamp without time zone,
    passport_number character varying(255),
    puc_expires_on timestamp without time zone,
    relation character varying(255),
    send_email_notification boolean,
    send_sms_notification boolean,
    timezone character varying(255),
    userobj bigint,
    vehicle_number character varying(255)
);


ALTER TABLE public.um_user_contact OWNER TO postgres;

--
-- TOC entry 291 (class 1259 OID 95896)
-- Name: um_user_contact_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE um_user_contact_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.um_user_contact_id_seq OWNER TO postgres;

--
-- TOC entry 2855 (class 0 OID 0)
-- Dependencies: 291
-- Name: um_user_contact_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE um_user_contact_id_seq OWNED BY um_user_contact.id;


--
-- TOC entry 294 (class 1259 OID 95909)
-- Name: um_user_feature; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE um_user_feature (
    id bigint NOT NULL,
    allow_to_create boolean NOT NULL,
    allow_to_delete boolean NOT NULL,
    allow_to_modify boolean NOT NULL,
    company bigint,
    description character varying(255),
    feature bigint,
    is_active boolean NOT NULL,
    is_archive boolean NOT NULL,
    userobj bigint
);


ALTER TABLE public.um_user_feature OWNER TO postgres;

--
-- TOC entry 293 (class 1259 OID 95907)
-- Name: um_user_feature_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE um_user_feature_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.um_user_feature_id_seq OWNER TO postgres;

--
-- TOC entry 2856 (class 0 OID 0)
-- Dependencies: 293
-- Name: um_user_feature_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE um_user_feature_id_seq OWNED BY um_user_feature.id;


--
-- TOC entry 296 (class 1259 OID 95917)
-- Name: um_user_group; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE um_user_group (
    id bigint NOT NULL,
    company bigint,
    created_by bigint NOT NULL,
    created_on timestamp without time zone NOT NULL,
    custom1 bigint,
    custom2 character varying(255),
    custom3 timestamp without time zone,
    custom4 character varying(255),
    description character varying(255),
    inactive_reason character varying(255),
    is_active boolean NOT NULL,
    is_archive boolean NOT NULL,
    modified_by bigint,
    modified_on timestamp without time zone,
    name character varying(255) NOT NULL,
    userobj bigint
);


ALTER TABLE public.um_user_group OWNER TO postgres;

--
-- TOC entry 295 (class 1259 OID 95915)
-- Name: um_user_group_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE um_user_group_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.um_user_group_id_seq OWNER TO postgres;

--
-- TOC entry 2857 (class 0 OID 0)
-- Dependencies: 295
-- Name: um_user_group_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE um_user_group_id_seq OWNED BY um_user_group.id;


--
-- TOC entry 298 (class 1259 OID 95928)
-- Name: um_user_ip_association_dtl; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE um_user_ip_association_dtl (
    id bigint NOT NULL,
    access_given_by bigint NOT NULL,
    access_given_on timestamp without time zone NOT NULL,
    ip_address character varying(255) NOT NULL,
    is_active boolean NOT NULL,
    last_modified_by bigint NOT NULL,
    last_modified_on timestamp without time zone NOT NULL,
    user_id bigint NOT NULL
);


ALTER TABLE public.um_user_ip_association_dtl OWNER TO postgres;

--
-- TOC entry 297 (class 1259 OID 95926)
-- Name: um_user_ip_association_dtl_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE um_user_ip_association_dtl_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.um_user_ip_association_dtl_id_seq OWNER TO postgres;

--
-- TOC entry 2858 (class 0 OID 0)
-- Dependencies: 297
-- Name: um_user_ip_association_dtl_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE um_user_ip_association_dtl_id_seq OWNED BY um_user_ip_association_dtl.id;


--
-- TOC entry 299 (class 1259 OID 95934)
-- Name: um_user_role; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE um_user_role (
    role bigint NOT NULL,
    userobj bigint NOT NULL,
    is_active boolean NOT NULL,
    is_archive boolean NOT NULL
);


ALTER TABLE public.um_user_role OWNER TO postgres;

--
-- TOC entry 2290 (class 2604 OID 95227)
-- Name: template_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY email_template_details ALTER COLUMN template_id SET DEFAULT nextval('email_template_details_template_id_seq'::regclass);


--
-- TOC entry 2291 (class 2604 OID 95246)
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY hk_asset_issue_info ALTER COLUMN id SET DEFAULT nextval('hk_asset_issue_info_id_seq'::regclass);


--
-- TOC entry 2292 (class 2604 OID 95267)
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY hk_event_registration_field_info ALTER COLUMN id SET DEFAULT nextval('hk_event_registration_field_info_id_seq'::regclass);


--
-- TOC entry 2293 (class 2604 OID 95286)
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY hk_feature_section_rel_info ALTER COLUMN id SET DEFAULT nextval('hk_feature_section_rel_info_id_seq'::regclass);


--
-- TOC entry 2294 (class 2604 OID 95294)
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY hk_field_info ALTER COLUMN id SET DEFAULT nextval('hk_field_info_id_seq'::regclass);


--
-- TOC entry 2295 (class 2604 OID 95305)
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY hk_franchise_min_req_info ALTER COLUMN id SET DEFAULT nextval('hk_franchise_min_req_info_id_seq'::regclass);


--
-- TOC entry 2296 (class 2604 OID 95313)
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY hk_leave_approval_info ALTER COLUMN id SET DEFAULT nextval('hk_leave_approval_info_id_seq'::regclass);


--
-- TOC entry 2297 (class 2604 OID 95324)
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY hk_leave_info ALTER COLUMN id SET DEFAULT nextval('hk_leave_info_id_seq'::regclass);


--
-- TOC entry 2298 (class 2604 OID 95335)
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY hk_location_mst ALTER COLUMN id SET DEFAULT nextval('hk_location_mst_id_seq'::regclass);


--
-- TOC entry 2299 (class 2604 OID 95359)
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY hk_notification_info ALTER COLUMN id SET DEFAULT nextval('hk_notification_info_id_seq'::regclass);


--
-- TOC entry 2300 (class 2604 OID 95375)
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY hk_section_info ALTER COLUMN id SET DEFAULT nextval('hk_section_info_id_seq'::regclass);


--
-- TOC entry 2301 (class 2604 OID 95388)
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY hk_shift_dtl ALTER COLUMN id SET DEFAULT nextval('hk_shift_dtl_id_seq'::regclass);


--
-- TOC entry 2302 (class 2604 OID 95401)
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY hk_system_asset_info ALTER COLUMN id SET DEFAULT nextval('hk_system_asset_info_id_seq'::regclass);


--
-- TOC entry 2303 (class 2604 OID 95412)
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY hk_system_category_mst ALTER COLUMN id SET DEFAULT nextval('hk_system_category_mst_id_seq'::regclass);


--
-- TOC entry 2304 (class 2604 OID 95431)
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY hk_system_event_info ALTER COLUMN id SET DEFAULT nextval('hk_system_event_info_id_seq'::regclass);


--
-- TOC entry 2305 (class 2604 OID 95442)
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY hk_system_holiday_mst ALTER COLUMN id SET DEFAULT nextval('hk_system_holiday_mst_id_seq'::regclass);


--
-- TOC entry 2306 (class 2604 OID 95461)
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY hk_system_message_info ALTER COLUMN id SET DEFAULT nextval('hk_system_message_info_id_seq'::regclass);


--
-- TOC entry 2307 (class 2604 OID 95472)
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY hk_system_shift_info ALTER COLUMN id SET DEFAULT nextval('hk_system_shift_info_id_seq'::regclass);


--
-- TOC entry 2308 (class 2604 OID 95480)
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY hk_system_task_info ALTER COLUMN id SET DEFAULT nextval('hk_system_task_info_id_seq'::regclass);


--
-- TOC entry 2309 (class 2604 OID 95491)
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY hk_system_values_mst ALTER COLUMN id SET DEFAULT nextval('hk_system_values_mst_id_seq'::regclass);


--
-- TOC entry 2310 (class 2604 OID 95502)
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY hk_task_recipient_dtl ALTER COLUMN id SET DEFAULT nextval('hk_task_recipient_dtl_id_seq'::regclass);


--
-- TOC entry 2311 (class 2604 OID 95531)
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY hk_user_histry_dtl ALTER COLUMN id SET DEFAULT nextval('hk_user_histry_dtl_id_seq'::regclass);


--
-- TOC entry 2312 (class 2604 OID 95560)
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY hk_workflow_info ALTER COLUMN id SET DEFAULT nextval('hk_workflow_info_id_seq'::regclass);


--
-- TOC entry 2313 (class 2604 OID 95600)
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY internationalization_timezone_location_detail ALTER COLUMN id SET DEFAULT nextval('internationalization_timezone_location_detail_id_seq'::regclass);


--
-- TOC entry 2314 (class 2604 OID 95632)
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY rb_report_access_dtl ALTER COLUMN id SET DEFAULT nextval('rb_report_access_dtl_id_seq'::regclass);


--
-- TOC entry 2315 (class 2604 OID 95640)
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY rb_report_field_dtl ALTER COLUMN id SET DEFAULT nextval('rb_report_field_dtl_id_seq'::regclass);


--
-- TOC entry 2316 (class 2604 OID 95651)
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY rb_report_mst ALTER COLUMN id SET DEFAULT nextval('rb_report_mst_id_seq'::regclass);


--
-- TOC entry 2317 (class 2604 OID 95662)
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY sync_client_feature_details ALTER COLUMN id SET DEFAULT nextval('sync_client_feature_details_id_seq'::regclass);


--
-- TOC entry 2318 (class 2604 OID 95673)
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY sync_config_master ALTER COLUMN id SET DEFAULT nextval('sync_config_master_id_seq'::regclass);


--
-- TOC entry 2319 (class 2604 OID 95684)
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY sync_config_parameter_details ALTER COLUMN id SET DEFAULT nextval('sync_config_parameter_details_id_seq'::regclass);


--
-- TOC entry 2320 (class 2604 OID 95695)
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY sync_feature_config_details ALTER COLUMN id SET DEFAULT nextval('sync_feature_config_details_id_seq'::regclass);


--
-- TOC entry 2321 (class 2604 OID 95706)
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY sync_feature_master ALTER COLUMN id SET DEFAULT nextval('sync_feature_master_id_seq'::regclass);


--
-- TOC entry 2322 (class 2604 OID 95717)
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY sync_parameter_master ALTER COLUMN id SET DEFAULT nextval('sync_parameter_master_id_seq'::regclass);


--
-- TOC entry 2323 (class 2604 OID 95728)
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY sync_sync_status ALTER COLUMN id SET DEFAULT nextval('sync_sync_status_id_seq'::regclass);


--
-- TOC entry 2324 (class 2604 OID 95739)
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY um_company_info ALTER COLUMN id SET DEFAULT nextval('um_company_info_id_seq'::regclass);


--
-- TOC entry 2325 (class 2604 OID 95750)
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY um_contact_address_dtl ALTER COLUMN id SET DEFAULT nextval('um_contact_address_dtl_id_seq'::regclass);


--
-- TOC entry 2326 (class 2604 OID 95761)
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY um_contact_document_dtl ALTER COLUMN id SET DEFAULT nextval('um_contact_document_dtl_id_seq'::regclass);


--
-- TOC entry 2327 (class 2604 OID 95772)
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY um_contact_education_dtl ALTER COLUMN id SET DEFAULT nextval('um_contact_education_dtl_id_seq'::regclass);


--
-- TOC entry 2328 (class 2604 OID 95783)
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY um_contact_experience_dtl ALTER COLUMN id SET DEFAULT nextval('um_contact_experience_dtl_id_seq'::regclass);


--
-- TOC entry 2329 (class 2604 OID 95794)
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY um_contact_insurance_dtl ALTER COLUMN id SET DEFAULT nextval('um_contact_insurance_dtl_id_seq'::regclass);


--
-- TOC entry 2330 (class 2604 OID 95805)
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY um_contact_language_knwn_dtl ALTER COLUMN id SET DEFAULT nextval('um_contact_language_knwn_dtl_id_seq'::regclass);


--
-- TOC entry 2331 (class 2604 OID 95816)
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY um_dept_mst ALTER COLUMN id SET DEFAULT nextval('um_dept_mst_id_seq'::regclass);


--
-- TOC entry 2332 (class 2604 OID 95827)
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY um_dept_role_dtl ALTER COLUMN id SET DEFAULT nextval('um_dept_role_dtl_id_seq'::regclass);


--
-- TOC entry 2333 (class 2604 OID 95840)
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY um_role_feature ALTER COLUMN id SET DEFAULT nextval('um_role_feature_id_seq'::regclass);


--
-- TOC entry 2334 (class 2604 OID 95856)
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY um_system_feature ALTER COLUMN id SET DEFAULT nextval('um_system_feature_id_seq'::regclass);


--
-- TOC entry 2337 (class 2604 OID 95869)
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY um_system_role ALTER COLUMN id SET DEFAULT nextval('um_system_role_id_seq'::regclass);


--
-- TOC entry 2340 (class 2604 OID 95882)
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY um_system_user ALTER COLUMN id SET DEFAULT nextval('um_system_user_id_seq'::regclass);


--
-- TOC entry 2341 (class 2604 OID 95901)
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY um_user_contact ALTER COLUMN id SET DEFAULT nextval('um_user_contact_id_seq'::regclass);


--
-- TOC entry 2342 (class 2604 OID 95912)
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY um_user_feature ALTER COLUMN id SET DEFAULT nextval('um_user_feature_id_seq'::regclass);


--
-- TOC entry 2343 (class 2604 OID 95920)
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY um_user_group ALTER COLUMN id SET DEFAULT nextval('um_user_group_id_seq'::regclass);


--
-- TOC entry 2344 (class 2604 OID 95931)
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY um_user_ip_association_dtl ALTER COLUMN id SET DEFAULT nextval('um_user_ip_association_dtl_id_seq'::regclass);


--
-- TOC entry 2346 (class 2606 OID 95232)
-- Name: email_template_details_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY email_template_details
    ADD CONSTRAINT email_template_details_pkey PRIMARY KEY (template_id);


--
-- TOC entry 2348 (class 2606 OID 95240)
-- Name: hk_asset_document_info_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY hk_asset_document_info
    ADD CONSTRAINT hk_asset_document_info_pkey PRIMARY KEY (asset, document_path);


--
-- TOC entry 2350 (class 2606 OID 95251)
-- Name: hk_asset_issue_info_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY hk_asset_issue_info
    ADD CONSTRAINT hk_asset_issue_info_pkey PRIMARY KEY (id);


--
-- TOC entry 2352 (class 2606 OID 95256)
-- Name: hk_asset_purchaser_info_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY hk_asset_purchaser_info
    ADD CONSTRAINT hk_asset_purchaser_info_pkey PRIMARY KEY (asset);


--
-- TOC entry 2354 (class 2606 OID 95261)
-- Name: hk_event_recipient_info_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY hk_event_recipient_info
    ADD CONSTRAINT hk_event_recipient_info_pkey PRIMARY KEY (event, reference_instance, reference_type);


--
-- TOC entry 2356 (class 2606 OID 95272)
-- Name: hk_event_registration_field_info_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY hk_event_registration_field_info
    ADD CONSTRAINT hk_event_registration_field_info_pkey PRIMARY KEY (id);


--
-- TOC entry 2358 (class 2606 OID 95280)
-- Name: hk_event_registration_field_value_info_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY hk_event_registration_field_value_info
    ADD CONSTRAINT hk_event_registration_field_value_info_pkey PRIMARY KEY (registration_field, user_id);


--
-- TOC entry 2360 (class 2606 OID 95288)
-- Name: hk_feature_section_rel_info_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY hk_feature_section_rel_info
    ADD CONSTRAINT hk_feature_section_rel_info_pkey PRIMARY KEY (id);


--
-- TOC entry 2362 (class 2606 OID 95299)
-- Name: hk_field_info_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY hk_field_info
    ADD CONSTRAINT hk_field_info_pkey PRIMARY KEY (id);


--
-- TOC entry 2364 (class 2606 OID 95307)
-- Name: hk_franchise_min_req_info_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY hk_franchise_min_req_info
    ADD CONSTRAINT hk_franchise_min_req_info_pkey PRIMARY KEY (id);


--
-- TOC entry 2366 (class 2606 OID 95318)
-- Name: hk_leave_approval_info_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY hk_leave_approval_info
    ADD CONSTRAINT hk_leave_approval_info_pkey PRIMARY KEY (id);


--
-- TOC entry 2368 (class 2606 OID 95329)
-- Name: hk_leave_info_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY hk_leave_info
    ADD CONSTRAINT hk_leave_info_pkey PRIMARY KEY (id);


--
-- TOC entry 2370 (class 2606 OID 95340)
-- Name: hk_location_mst_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY hk_location_mst
    ADD CONSTRAINT hk_location_mst_pkey PRIMARY KEY (id);


--
-- TOC entry 2372 (class 2606 OID 95345)
-- Name: hk_message_recipent_info_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY hk_message_recipent_info
    ADD CONSTRAINT hk_message_recipent_info_pkey PRIMARY KEY (message_obj, reference_instance, reference_type);


--
-- TOC entry 2374 (class 2606 OID 95353)
-- Name: hk_message_recipient_dtl_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY hk_message_recipient_dtl
    ADD CONSTRAINT hk_message_recipient_dtl_pkey PRIMARY KEY (message_obj, message_to);


--
-- TOC entry 2376 (class 2606 OID 95364)
-- Name: hk_notification_info_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY hk_notification_info
    ADD CONSTRAINT hk_notification_info_pkey PRIMARY KEY (id);


--
-- TOC entry 2378 (class 2606 OID 95369)
-- Name: hk_notification_user_rel_info_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY hk_notification_user_rel_info
    ADD CONSTRAINT hk_notification_user_rel_info_pkey PRIMARY KEY (for_user, notification);


--
-- TOC entry 2380 (class 2606 OID 95377)
-- Name: hk_section_info_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY hk_section_info
    ADD CONSTRAINT hk_section_info_pkey PRIMARY KEY (id);


--
-- TOC entry 2382 (class 2606 OID 95382)
-- Name: hk_shift_department_info_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY hk_shift_department_info
    ADD CONSTRAINT hk_shift_department_info_pkey PRIMARY KEY (department, shift);


--
-- TOC entry 2384 (class 2606 OID 95390)
-- Name: hk_shift_dtl_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY hk_shift_dtl
    ADD CONSTRAINT hk_shift_dtl_pkey PRIMARY KEY (id);


--
-- TOC entry 2386 (class 2606 OID 95395)
-- Name: hk_shift_rule_info_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY hk_shift_rule_info
    ADD CONSTRAINT hk_shift_rule_info_pkey PRIMARY KEY (rule_type, shift);


--
-- TOC entry 2388 (class 2606 OID 95406)
-- Name: hk_system_asset_info_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY hk_system_asset_info
    ADD CONSTRAINT hk_system_asset_info_pkey PRIMARY KEY (id);


--
-- TOC entry 2390 (class 2606 OID 95417)
-- Name: hk_system_category_mst_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY hk_system_category_mst
    ADD CONSTRAINT hk_system_category_mst_pkey PRIMARY KEY (id);


--
-- TOC entry 2392 (class 2606 OID 95425)
-- Name: hk_system_configuration_info_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY hk_system_configuration_info
    ADD CONSTRAINT hk_system_configuration_info_pkey PRIMARY KEY (franchise, system_key);


--
-- TOC entry 2394 (class 2606 OID 95436)
-- Name: hk_system_event_info_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY hk_system_event_info
    ADD CONSTRAINT hk_system_event_info_pkey PRIMARY KEY (id);


--
-- TOC entry 2396 (class 2606 OID 95447)
-- Name: hk_system_holiday_mst_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY hk_system_holiday_mst
    ADD CONSTRAINT hk_system_holiday_mst_pkey PRIMARY KEY (id);


--
-- TOC entry 2398 (class 2606 OID 95455)
-- Name: hk_system_key_mst_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY hk_system_key_mst
    ADD CONSTRAINT hk_system_key_mst_pkey PRIMARY KEY (code);


--
-- TOC entry 2400 (class 2606 OID 95466)
-- Name: hk_system_message_info_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY hk_system_message_info
    ADD CONSTRAINT hk_system_message_info_pkey PRIMARY KEY (id);


--
-- TOC entry 2402 (class 2606 OID 95474)
-- Name: hk_system_shift_info_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY hk_system_shift_info
    ADD CONSTRAINT hk_system_shift_info_pkey PRIMARY KEY (id);


--
-- TOC entry 2404 (class 2606 OID 95485)
-- Name: hk_system_task_info_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY hk_system_task_info
    ADD CONSTRAINT hk_system_task_info_pkey PRIMARY KEY (id);


--
-- TOC entry 2406 (class 2606 OID 95496)
-- Name: hk_system_values_mst_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY hk_system_values_mst
    ADD CONSTRAINT hk_system_values_mst_pkey PRIMARY KEY (id);


--
-- TOC entry 2408 (class 2606 OID 95507)
-- Name: hk_task_recipient_dtl_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY hk_task_recipient_dtl
    ADD CONSTRAINT hk_task_recipient_dtl_pkey PRIMARY KEY (id);


--
-- TOC entry 2410 (class 2606 OID 95512)
-- Name: hk_task_recipient_info_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY hk_task_recipient_info
    ADD CONSTRAINT hk_task_recipient_info_pkey PRIMARY KEY (reference_instance, reference_type, task);


--
-- TOC entry 2412 (class 2606 OID 95517)
-- Name: hk_user_attendance_info_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY hk_user_attendance_info
    ADD CONSTRAINT hk_user_attendance_info_pkey PRIMARY KEY (on_date, user_id);


--
-- TOC entry 2414 (class 2606 OID 95525)
-- Name: hk_user_event_registration_info_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY hk_user_event_registration_info
    ADD CONSTRAINT hk_user_event_registration_info_pkey PRIMARY KEY (event, user_id);


--
-- TOC entry 2416 (class 2606 OID 95536)
-- Name: hk_user_histry_dtl_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY hk_user_histry_dtl
    ADD CONSTRAINT hk_user_histry_dtl_pkey PRIMARY KEY (id);


--
-- TOC entry 2418 (class 2606 OID 95544)
-- Name: hk_user_operation_info_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY hk_user_operation_info
    ADD CONSTRAINT hk_user_operation_info_pkey PRIMARY KEY (created_on, event_code, user_id);


--
-- TOC entry 2420 (class 2606 OID 95549)
-- Name: hk_usertype_seq_mst_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY hk_usertype_seq_mst
    ADD CONSTRAINT hk_usertype_seq_mst_pkey PRIMARY KEY (franchise, user_type);


--
-- TOC entry 2422 (class 2606 OID 95554)
-- Name: hk_workflow_approver_rel_info_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY hk_workflow_approver_rel_info
    ADD CONSTRAINT hk_workflow_approver_rel_info_pkey PRIMARY KEY (reference_instance, reference_type, workflow);


--
-- TOC entry 2424 (class 2606 OID 95562)
-- Name: hk_workflow_info_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY hk_workflow_info
    ADD CONSTRAINT hk_workflow_info_pkey PRIMARY KEY (id);


--
-- TOC entry 2426 (class 2606 OID 95570)
-- Name: internationalization_country_master_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY internationalization_country_master
    ADD CONSTRAINT internationalization_country_master_pkey PRIMARY KEY (code);


--
-- TOC entry 2428 (class 2606 OID 95578)
-- Name: internationalization_currency_master_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY internationalization_currency_master
    ADD CONSTRAINT internationalization_currency_master_pkey PRIMARY KEY (code, country);


--
-- TOC entry 2430 (class 2606 OID 95586)
-- Name: internationalization_label_master_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY internationalization_label_master
    ADD CONSTRAINT internationalization_label_master_pkey PRIMARY KEY (company, country, entity, key, language, type);


--
-- TOC entry 2432 (class 2606 OID 95594)
-- Name: internationalization_language_master_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY internationalization_language_master
    ADD CONSTRAINT internationalization_language_master_pkey PRIMARY KEY (code, country);


--
-- TOC entry 2434 (class 2606 OID 95605)
-- Name: internationalization_timezone_location_detail_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY internationalization_timezone_location_detail
    ADD CONSTRAINT internationalization_timezone_location_detail_pkey PRIMARY KEY (id);


--
-- TOC entry 2436 (class 2606 OID 95613)
-- Name: internationalization_timezone_master_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY internationalization_timezone_master
    ADD CONSTRAINT internationalization_timezone_master_pkey PRIMARY KEY (id);


--
-- TOC entry 2438 (class 2606 OID 95621)
-- Name: rb_field_converter_dtl_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY rb_field_converter_dtl
    ADD CONSTRAINT rb_field_converter_dtl_pkey PRIMARY KEY (converter_key, report_field);


--
-- TOC entry 2440 (class 2606 OID 95626)
-- Name: rb_field_formatter_dtl_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY rb_field_formatter_dtl
    ADD CONSTRAINT rb_field_formatter_dtl_pkey PRIMARY KEY (field_formatter, report_field);


--
-- TOC entry 2442 (class 2606 OID 95634)
-- Name: rb_report_access_dtl_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY rb_report_access_dtl
    ADD CONSTRAINT rb_report_access_dtl_pkey PRIMARY KEY (id);


--
-- TOC entry 2444 (class 2606 OID 95645)
-- Name: rb_report_field_dtl_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY rb_report_field_dtl
    ADD CONSTRAINT rb_report_field_dtl_pkey PRIMARY KEY (id);


--
-- TOC entry 2446 (class 2606 OID 95656)
-- Name: rb_report_mst_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY rb_report_mst
    ADD CONSTRAINT rb_report_mst_pkey PRIMARY KEY (id);


--
-- TOC entry 2448 (class 2606 OID 95667)
-- Name: sync_client_feature_details_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY sync_client_feature_details
    ADD CONSTRAINT sync_client_feature_details_pkey PRIMARY KEY (id);


--
-- TOC entry 2450 (class 2606 OID 95678)
-- Name: sync_config_master_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY sync_config_master
    ADD CONSTRAINT sync_config_master_pkey PRIMARY KEY (id);


--
-- TOC entry 2452 (class 2606 OID 95689)
-- Name: sync_config_parameter_details_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY sync_config_parameter_details
    ADD CONSTRAINT sync_config_parameter_details_pkey PRIMARY KEY (id);


--
-- TOC entry 2454 (class 2606 OID 95700)
-- Name: sync_feature_config_details_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY sync_feature_config_details
    ADD CONSTRAINT sync_feature_config_details_pkey PRIMARY KEY (id);


--
-- TOC entry 2456 (class 2606 OID 95711)
-- Name: sync_feature_master_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY sync_feature_master
    ADD CONSTRAINT sync_feature_master_pkey PRIMARY KEY (id);


--
-- TOC entry 2458 (class 2606 OID 95722)
-- Name: sync_parameter_master_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY sync_parameter_master
    ADD CONSTRAINT sync_parameter_master_pkey PRIMARY KEY (id);


--
-- TOC entry 2460 (class 2606 OID 95733)
-- Name: sync_sync_status_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY sync_sync_status
    ADD CONSTRAINT sync_sync_status_pkey PRIMARY KEY (id);


--
-- TOC entry 2462 (class 2606 OID 95744)
-- Name: um_company_info_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY um_company_info
    ADD CONSTRAINT um_company_info_pkey PRIMARY KEY (id);


--
-- TOC entry 2464 (class 2606 OID 95755)
-- Name: um_contact_address_dtl_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY um_contact_address_dtl
    ADD CONSTRAINT um_contact_address_dtl_pkey PRIMARY KEY (id);


--
-- TOC entry 2466 (class 2606 OID 95766)
-- Name: um_contact_document_dtl_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY um_contact_document_dtl
    ADD CONSTRAINT um_contact_document_dtl_pkey PRIMARY KEY (id);


--
-- TOC entry 2468 (class 2606 OID 95777)
-- Name: um_contact_education_dtl_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY um_contact_education_dtl
    ADD CONSTRAINT um_contact_education_dtl_pkey PRIMARY KEY (id);


--
-- TOC entry 2470 (class 2606 OID 95788)
-- Name: um_contact_experience_dtl_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY um_contact_experience_dtl
    ADD CONSTRAINT um_contact_experience_dtl_pkey PRIMARY KEY (id);


--
-- TOC entry 2472 (class 2606 OID 95799)
-- Name: um_contact_insurance_dtl_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY um_contact_insurance_dtl
    ADD CONSTRAINT um_contact_insurance_dtl_pkey PRIMARY KEY (id);


--
-- TOC entry 2474 (class 2606 OID 95810)
-- Name: um_contact_language_knwn_dtl_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY um_contact_language_knwn_dtl
    ADD CONSTRAINT um_contact_language_knwn_dtl_pkey PRIMARY KEY (id);


--
-- TOC entry 2476 (class 2606 OID 95821)
-- Name: um_dept_mst_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY um_dept_mst
    ADD CONSTRAINT um_dept_mst_pkey PRIMARY KEY (id);


--
-- TOC entry 2478 (class 2606 OID 95829)
-- Name: um_dept_role_dtl_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY um_dept_role_dtl
    ADD CONSTRAINT um_dept_role_dtl_pkey PRIMARY KEY (id);


--
-- TOC entry 2480 (class 2606 OID 95834)
-- Name: um_group_contact_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY um_group_contact
    ADD CONSTRAINT um_group_contact_pkey PRIMARY KEY (contact, user_group);


--
-- TOC entry 2482 (class 2606 OID 95842)
-- Name: um_role_feature_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY um_role_feature
    ADD CONSTRAINT um_role_feature_pkey PRIMARY KEY (id);


--
-- TOC entry 2484 (class 2606 OID 95850)
-- Name: um_system_configuration_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY um_system_configuration
    ADD CONSTRAINT um_system_configuration_pkey PRIMARY KEY (system_key);


--
-- TOC entry 2486 (class 2606 OID 95863)
-- Name: um_system_feature_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY um_system_feature
    ADD CONSTRAINT um_system_feature_pkey PRIMARY KEY (id);


--
-- TOC entry 2488 (class 2606 OID 95876)
-- Name: um_system_role_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY um_system_role
    ADD CONSTRAINT um_system_role_pkey PRIMARY KEY (id);


--
-- TOC entry 2490 (class 2606 OID 95887)
-- Name: um_system_user_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY um_system_user
    ADD CONSTRAINT um_system_user_pkey PRIMARY KEY (id);


--
-- TOC entry 2492 (class 2606 OID 95895)
-- Name: um_user_access_dtl_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY um_user_access_dtl
    ADD CONSTRAINT um_user_access_dtl_pkey PRIMARY KEY (on_time, user_id);


--
-- TOC entry 2494 (class 2606 OID 95906)
-- Name: um_user_contact_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY um_user_contact
    ADD CONSTRAINT um_user_contact_pkey PRIMARY KEY (id);


--
-- TOC entry 2496 (class 2606 OID 95914)
-- Name: um_user_feature_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY um_user_feature
    ADD CONSTRAINT um_user_feature_pkey PRIMARY KEY (id);


--
-- TOC entry 2498 (class 2606 OID 95925)
-- Name: um_user_group_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY um_user_group
    ADD CONSTRAINT um_user_group_pkey PRIMARY KEY (id);


--
-- TOC entry 2500 (class 2606 OID 95933)
-- Name: um_user_ip_association_dtl_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY um_user_ip_association_dtl
    ADD CONSTRAINT um_user_ip_association_dtl_pkey PRIMARY KEY (id);


--
-- TOC entry 2502 (class 2606 OID 95938)
-- Name: um_user_role_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY um_user_role
    ADD CONSTRAINT um_user_role_pkey PRIMARY KEY (role, userobj);


--
-- TOC entry 2527 (class 2606 OID 96059)
-- Name: fk10b96f3e632b6a3d; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY hk_user_histry_dtl
    ADD CONSTRAINT fk10b96f3e632b6a3d FOREIGN KEY (linked_with) REFERENCES hk_user_histry_dtl(id);


--
-- TOC entry 2543 (class 2606 OID 96139)
-- Name: fk19a4147fc0254449; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY um_contact_education_dtl
    ADD CONSTRAINT fk19a4147fc0254449 FOREIGN KEY (user_contact) REFERENCES um_user_contact(id);


--
-- TOC entry 2516 (class 2606 OID 96004)
-- Name: fk1ccbf5828cf83c9d; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY hk_shift_department_info
    ADD CONSTRAINT fk1ccbf5828cf83c9d FOREIGN KEY (shift) REFERENCES hk_system_shift_info(id);


--
-- TOC entry 2504 (class 2606 OID 95944)
-- Name: fk1ec3e4bff5de2ef9; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY hk_asset_issue_info
    ADD CONSTRAINT fk1ec3e4bff5de2ef9 FOREIGN KEY (asset) REFERENCES hk_system_asset_info(id);


--
-- TOC entry 2549 (class 2606 OID 96169)
-- Name: fk2a84f1e6aecb8f24; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY um_dept_role_dtl
    ADD CONSTRAINT fk2a84f1e6aecb8f24 FOREIGN KEY (role) REFERENCES um_system_role(id);


--
-- TOC entry 2548 (class 2606 OID 96164)
-- Name: fk2a84f1e6e05845cf; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY um_dept_role_dtl
    ADD CONSTRAINT fk2a84f1e6e05845cf FOREIGN KEY (dept) REFERENCES um_dept_mst(id);


--
-- TOC entry 2529 (class 2606 OID 96069)
-- Name: fk2e516274a1c42c9; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY internationalization_currency_master
    ADD CONSTRAINT fk2e516274a1c42c9 FOREIGN KEY (country) REFERENCES internationalization_country_master(code);


--
-- TOC entry 2509 (class 2606 OID 95969)
-- Name: fk2f5312934e14dda3; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY hk_feature_section_rel_info
    ADD CONSTRAINT fk2f5312934e14dda3 FOREIGN KEY (section) REFERENCES hk_section_info(id);


--
-- TOC entry 2539 (class 2606 OID 96119)
-- Name: fk3171a8d26818d865; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY sync_feature_config_details
    ADD CONSTRAINT fk3171a8d26818d865 FOREIGN KEY (config_id) REFERENCES sync_config_master(id);


--
-- TOC entry 2554 (class 2606 OID 96194)
-- Name: fk3477516d93aa68; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY um_system_feature
    ADD CONSTRAINT fk3477516d93aa68 FOREIGN KEY (parent) REFERENCES um_system_feature(id);


--
-- TOC entry 2517 (class 2606 OID 96009)
-- Name: fk3846f0638cf83c9d; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY hk_shift_dtl
    ADD CONSTRAINT fk3846f0638cf83c9d FOREIGN KEY (shift) REFERENCES hk_system_shift_info(id);


--
-- TOC entry 2545 (class 2606 OID 96149)
-- Name: fk3a5c6ab1c0254449; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY um_contact_insurance_dtl
    ADD CONSTRAINT fk3a5c6ab1c0254449 FOREIGN KEY (user_contact) REFERENCES um_user_contact(id);


--
-- TOC entry 2503 (class 2606 OID 95939)
-- Name: fk3c7a9907f5de2ef9; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY hk_asset_document_info
    ADD CONSTRAINT fk3c7a9907f5de2ef9 FOREIGN KEY (asset) REFERENCES hk_system_asset_info(id);


--
-- TOC entry 2555 (class 2606 OID 96199)
-- Name: fk479380b46535acbd; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY um_system_user
    ADD CONSTRAINT fk479380b46535acbd FOREIGN KEY (contact) REFERENCES um_user_contact(id);


--
-- TOC entry 2547 (class 2606 OID 96159)
-- Name: fk4da9385ba4d45034; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY um_dept_mst
    ADD CONSTRAINT fk4da9385ba4d45034 FOREIGN KEY (parent) REFERENCES um_dept_mst(id);


--
-- TOC entry 2557 (class 2606 OID 96209)
-- Name: fk52fc8df3a5d2a34f; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY um_user_contact
    ADD CONSTRAINT fk52fc8df3a5d2a34f FOREIGN KEY (userobj) REFERENCES um_system_user(id);


--
-- TOC entry 2556 (class 2606 OID 96204)
-- Name: fk52fc8df3fac38e8d; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY um_user_contact
    ADD CONSTRAINT fk52fc8df3fac38e8d FOREIGN KEY (contact_user) REFERENCES um_system_user(id);


--
-- TOC entry 2525 (class 2606 OID 96049)
-- Name: fk54c9ebd269ffcb77; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY hk_task_recipient_info
    ADD CONSTRAINT fk54c9ebd269ffcb77 FOREIGN KEY (task) REFERENCES hk_system_task_info(id);


--
-- TOC entry 2542 (class 2606 OID 96134)
-- Name: fk5bf015bec0254449; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY um_contact_document_dtl
    ADD CONSTRAINT fk5bf015bec0254449 FOREIGN KEY (user_contact) REFERENCES um_user_contact(id);


--
-- TOC entry 2550 (class 2606 OID 96174)
-- Name: fk672948f96535acbd; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY um_group_contact
    ADD CONSTRAINT fk672948f96535acbd FOREIGN KEY (contact) REFERENCES um_user_contact(id);


--
-- TOC entry 2551 (class 2606 OID 96179)
-- Name: fk672948f982ea5747; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY um_group_contact
    ADD CONSTRAINT fk672948f982ea5747 FOREIGN KEY (user_group) REFERENCES um_user_group(id);


--
-- TOC entry 2512 (class 2606 OID 95984)
-- Name: fk68016dc0b7cd7fcc; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY hk_location_mst
    ADD CONSTRAINT fk68016dc0b7cd7fcc FOREIGN KEY (parent) REFERENCES hk_location_mst(id);


--
-- TOC entry 2521 (class 2606 OID 96029)
-- Name: fk6e4417075ff576a9; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY hk_system_event_info
    ADD CONSTRAINT fk6e4417075ff576a9 FOREIGN KEY (category) REFERENCES hk_system_category_mst(id);


--
-- TOC entry 2541 (class 2606 OID 96129)
-- Name: fk71a8e94bc0254449; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY um_contact_address_dtl
    ADD CONSTRAINT fk71a8e94bc0254449 FOREIGN KEY (user_contact) REFERENCES um_user_contact(id);


--
-- TOC entry 2505 (class 2606 OID 95949)
-- Name: fk72e8b787f5de2ef9; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY hk_asset_purchaser_info
    ADD CONSTRAINT fk72e8b787f5de2ef9 FOREIGN KEY (asset) REFERENCES hk_system_asset_info(id);


--
-- TOC entry 2514 (class 2606 OID 95994)
-- Name: fk775b8642aa4a7f1f; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY hk_message_recipient_dtl
    ADD CONSTRAINT fk775b8642aa4a7f1f FOREIGN KEY (message_obj) REFERENCES hk_system_message_info(id);


--
-- TOC entry 2510 (class 2606 OID 95974)
-- Name: fk7fb6218f4e14dda3; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY hk_field_info
    ADD CONSTRAINT fk7fb6218f4e14dda3 FOREIGN KEY (section) REFERENCES hk_section_info(id);


--
-- TOC entry 2561 (class 2606 OID 96229)
-- Name: fk8663e253a5d26812; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY um_user_ip_association_dtl
    ADD CONSTRAINT fk8663e253a5d26812 FOREIGN KEY (user_id) REFERENCES um_system_user(id);


--
-- TOC entry 2528 (class 2606 OID 96064)
-- Name: fk8967850a2afed76b; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY hk_workflow_approver_rel_info
    ADD CONSTRAINT fk8967850a2afed76b FOREIGN KEY (workflow) REFERENCES hk_workflow_info(id);


--
-- TOC entry 2530 (class 2606 OID 96074)
-- Name: fk91a485004a1c42c9; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY internationalization_language_master
    ADD CONSTRAINT fk91a485004a1c42c9 FOREIGN KEY (country) REFERENCES internationalization_country_master(code);


--
-- TOC entry 2540 (class 2606 OID 96124)
-- Name: fk994d067289e8e12; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY sync_sync_status
    ADD CONSTRAINT fk994d067289e8e12 FOREIGN KEY (client_feture_details_id) REFERENCES sync_client_feature_details(id);


--
-- TOC entry 2552 (class 2606 OID 96184)
-- Name: fk9bc89e7418b1cb4; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY um_role_feature
    ADD CONSTRAINT fk9bc89e7418b1cb4 FOREIGN KEY (feature) REFERENCES um_system_feature(id);


--
-- TOC entry 2553 (class 2606 OID 96189)
-- Name: fk9bc89e74aecb8f24; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY um_role_feature
    ADD CONSTRAINT fk9bc89e74aecb8f24 FOREIGN KEY (role) REFERENCES um_system_role(id);


--
-- TOC entry 2511 (class 2606 OID 95979)
-- Name: fk9dc3b8665384b797; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY hk_leave_approval_info
    ADD CONSTRAINT fk9dc3b8665384b797 FOREIGN KEY (leave_request) REFERENCES hk_leave_info(id);


--
-- TOC entry 2537 (class 2606 OID 96109)
-- Name: fka13b17a9bcc84064; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY sync_client_feature_details
    ADD CONSTRAINT fka13b17a9bcc84064 FOREIGN KEY (feature_config_id) REFERENCES sync_feature_config_details(id);


--
-- TOC entry 2526 (class 2606 OID 96054)
-- Name: fka1634b57b1b356cd; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY hk_user_event_registration_info
    ADD CONSTRAINT fka1634b57b1b356cd FOREIGN KEY (event) REFERENCES hk_system_event_info(id);


--
-- TOC entry 2562 (class 2606 OID 96234)
-- Name: fka50d9f83a5d2a34f; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY um_user_role
    ADD CONSTRAINT fka50d9f83a5d2a34f FOREIGN KEY (userobj) REFERENCES um_system_user(id);


--
-- TOC entry 2522 (class 2606 OID 96034)
-- Name: fka6dd0e1fe19f6f39; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY hk_system_shift_info
    ADD CONSTRAINT fka6dd0e1fe19f6f39 FOREIGN KEY (temporary_shift_for) REFERENCES hk_system_shift_info(id);


--
-- TOC entry 2538 (class 2606 OID 96114)
-- Name: fka7c381136818d865; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY sync_config_parameter_details
    ADD CONSTRAINT fka7c381136818d865 FOREIGN KEY (config_id) REFERENCES sync_config_master(id);


--
-- TOC entry 2535 (class 2606 OID 96099)
-- Name: fkac715c1db1fdbb01; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY rb_report_access_dtl
    ADD CONSTRAINT fkac715c1db1fdbb01 FOREIGN KEY (report) REFERENCES rb_report_mst(id);


--
-- TOC entry 2523 (class 2606 OID 96039)
-- Name: fkae77eec5bbf03a5c; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY hk_system_values_mst
    ADD CONSTRAINT fkae77eec5bbf03a5c FOREIGN KEY (key_code) REFERENCES hk_system_key_mst(code);


--
-- TOC entry 2519 (class 2606 OID 96019)
-- Name: fkb28964715ff576a9; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY hk_system_asset_info
    ADD CONSTRAINT fkb28964715ff576a9 FOREIGN KEY (category) REFERENCES hk_system_category_mst(id);


--
-- TOC entry 2546 (class 2606 OID 96154)
-- Name: fkb353758c0254449; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY um_contact_language_knwn_dtl
    ADD CONSTRAINT fkb353758c0254449 FOREIGN KEY (user_contact) REFERENCES um_user_contact(id);


--
-- TOC entry 2544 (class 2606 OID 96144)
-- Name: fkb37db50dc0254449; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY um_contact_experience_dtl
    ADD CONSTRAINT fkb37db50dc0254449 FOREIGN KEY (user_contact) REFERENCES um_user_contact(id);


--
-- TOC entry 2524 (class 2606 OID 96044)
-- Name: fkb8698a3869ffcb77; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY hk_task_recipient_dtl
    ADD CONSTRAINT fkb8698a3869ffcb77 FOREIGN KEY (task) REFERENCES hk_system_task_info(id);


--
-- TOC entry 2533 (class 2606 OID 96089)
-- Name: fkbbaa47a96fea855c; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY rb_field_converter_dtl
    ADD CONSTRAINT fkbbaa47a96fea855c FOREIGN KEY (report_field) REFERENCES rb_report_field_dtl(id);


--
-- TOC entry 2513 (class 2606 OID 95989)
-- Name: fkbd08bdc5aa4a7f1f; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY hk_message_recipent_info
    ADD CONSTRAINT fkbd08bdc5aa4a7f1f FOREIGN KEY (message_obj) REFERENCES hk_system_message_info(id);


--
-- TOC entry 2536 (class 2606 OID 96104)
-- Name: fkbe8b21fbb1fdbb01; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY rb_report_field_dtl
    ADD CONSTRAINT fkbe8b21fbb1fdbb01 FOREIGN KEY (report) REFERENCES rb_report_mst(id);


--
-- TOC entry 2534 (class 2606 OID 96094)
-- Name: fkc2e1c9d36fea855c; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY rb_field_formatter_dtl
    ADD CONSTRAINT fkc2e1c9d36fea855c FOREIGN KEY (report_field) REFERENCES rb_report_field_dtl(id);


--
-- TOC entry 2532 (class 2606 OID 96084)
-- Name: fkc3e2893f4a1c42c9; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY internationalization_timezone_master
    ADD CONSTRAINT fkc3e2893f4a1c42c9 FOREIGN KEY (country) REFERENCES internationalization_country_master(code);


--
-- TOC entry 2531 (class 2606 OID 96079)
-- Name: fkccf959de5fa7f85b; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY internationalization_timezone_location_detail
    ADD CONSTRAINT fkccf959de5fa7f85b FOREIGN KEY (timezone) REFERENCES internationalization_timezone_master(id);


--
-- TOC entry 2508 (class 2606 OID 95964)
-- Name: fkd3e5edc623ddf6a8; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY hk_event_registration_field_value_info
    ADD CONSTRAINT fkd3e5edc623ddf6a8 FOREIGN KEY (registration_field) REFERENCES hk_event_registration_field_info(id);


--
-- TOC entry 2520 (class 2606 OID 96024)
-- Name: fkd9421ba1219dc255; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY hk_system_category_mst
    ADD CONSTRAINT fkd9421ba1219dc255 FOREIGN KEY (parent) REFERENCES hk_system_category_mst(id);


--
-- TOC entry 2558 (class 2606 OID 96214)
-- Name: fkdfe7e4c918b1cb4; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY um_user_feature
    ADD CONSTRAINT fkdfe7e4c918b1cb4 FOREIGN KEY (feature) REFERENCES um_system_feature(id);


--
-- TOC entry 2559 (class 2606 OID 96219)
-- Name: fkdfe7e4c9a5d2a34f; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY um_user_feature
    ADD CONSTRAINT fkdfe7e4c9a5d2a34f FOREIGN KEY (userobj) REFERENCES um_system_user(id);


--
-- TOC entry 2506 (class 2606 OID 95954)
-- Name: fkeb8ca515b1b356cd; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY hk_event_recipient_info
    ADD CONSTRAINT fkeb8ca515b1b356cd FOREIGN KEY (event) REFERENCES hk_system_event_info(id);


--
-- TOC entry 2518 (class 2606 OID 96014)
-- Name: fkedc0ad588cf83c9d; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY hk_shift_rule_info
    ADD CONSTRAINT fkedc0ad588cf83c9d FOREIGN KEY (shift) REFERENCES hk_system_shift_info(id);


--
-- TOC entry 2507 (class 2606 OID 95959)
-- Name: fkf1c31738b1b356cd; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY hk_event_registration_field_info
    ADD CONSTRAINT fkf1c31738b1b356cd FOREIGN KEY (event) REFERENCES hk_system_event_info(id);


--
-- TOC entry 2515 (class 2606 OID 95999)
-- Name: fkfae33af0f5ac03c3; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY hk_notification_user_rel_info
    ADD CONSTRAINT fkfae33af0f5ac03c3 FOREIGN KEY (notification) REFERENCES hk_notification_info(id);


--
-- TOC entry 2560 (class 2606 OID 96224)
-- Name: fkfc0cb912a5d2a34f; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY um_user_group
    ADD CONSTRAINT fkfc0cb912a5d2a34f FOREIGN KEY (userobj) REFERENCES um_system_user(id);


