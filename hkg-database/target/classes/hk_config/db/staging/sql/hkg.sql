--  Drop Tables, Stored Procedures and Views 
DROP TABLE IF EXISTS hk_franchise_min_req_info
;
DROP TABLE IF EXISTS hk_system_key_mst
;
DROP TABLE IF EXISTS hk_system_values_mst
;



--  Create Tables 
CREATE TABLE hk_franchise_min_req_info ( 
	value_code bigint NOT NULL,
	franchise bigint NOT NULL,
	required_value integer NULL,
	acquired_value integer NULL
)
;
COMMENT ON TABLE hk_franchise_min_req_info
    IS 'This table is used to store Minimum requirment for the Franchise.'
;

CREATE SEQUENCE hk_system_key_mst_code_seq INCREMENT 1 START 1
;

CREATE TABLE hk_system_key_mst ( 
	code varchar(50) DEFAULT NEXTVAL('"hk_system_key_mst_code_seq"'::TEXT) NOT NULL,    --  Auto generated value 
	master_name varchar(100) NOT NULL,
	description varchar(1000) NULL,
	precedence smallint NOT NULL,
	category varchar(50) NULL,
	is_archive boolean NOT NULL
)
;
COMMENT ON TABLE hk_system_key_mst
    IS 'This table is used to store masters associate with the System.'
;
COMMENT ON COLUMN hk_system_key_mst.code
    IS 'Auto generated value'
;

CREATE SEQUENCE hk_system_values_mst_id_seq INCREMENT 1 START 1
;

CREATE TABLE hk_system_values_mst ( 
	id bigint DEFAULT NEXTVAL('"hk_system_values_mst_id_seq"'::TEXT) NOT NULL,
	key_code varchar(50) NOT NULL,
	value_code varchar(50) NULL,
	value_name varchar(1000) NOT NULL,
	franchise bigint NOT NULL,
	is_active boolean NOT NULL,
	is_archive boolean NOT NULL,
	created_by bigint NOT NULL,
	created_on timestamp NOT NULL,
	last_modified_by bigint NOT NULL,
	last_modified_on timestamp NOT NULL
)
;
COMMENT ON TABLE hk_system_values_mst
    IS 'This table is used to store masters value associated with the Franchise. '
;


--  Create Primary Key Constraints 
ALTER TABLE hk_franchise_min_req_info ADD CONSTRAINT PK_hk_franchise_min_req_info 
	PRIMARY KEY (value_code, franchise)
;


ALTER TABLE hk_system_key_mst ADD CONSTRAINT PK_hk_system_key_mst 
	PRIMARY KEY (code)
;


ALTER TABLE hk_system_values_mst ADD CONSTRAINT PK_hk_system_values_mst 
	PRIMARY KEY (id)
;




--  Create Foreign Key Constraints 
ALTER TABLE hk_franchise_min_req_info ADD CONSTRAINT FK_hk_franchise_min_req_info_hk_system_values_mst 
	FOREIGN KEY (value_code) REFERENCES hk_system_values_mst (id)
;

ALTER TABLE hk_system_values_mst ADD CONSTRAINT FK_hk_system_values_mst_hk_system_key_mst 
	FOREIGN KEY (key_code) REFERENCES hk_system_key_mst (code)
;


-- added by dhwani for leave management


DROP TABLE IF EXISTS hk_system_leave_setting_mst;

CREATE SEQUENCE hk_system_leave_setting_mst_id_seq INCREMENT 1 START 1;

CREATE TABLE hk_system_leave_setting_mst ( 
	id bigint DEFAULT NEXTVAL('"hk_system_leave_setting_mst_id_seq"'::TEXT) NOT NULL,
	total_leaves integer NOT NULL,
	can_override_limit boolean NOT NULL,
	can_carry_forward boolean NOT NULL,
	linked_with bigint NULL,
	is_active boolean NOT NULL,
	is_archive boolean NOT NULL,
	created_by bigint NOT NULL,
	created_on timestamp NOT NULL,
	franchise bigint NOT NULL
);

COMMENT ON TABLE hk_system_leave_setting_mst
    IS 'This table is used to store Leave Settings for the Franchise.';

ALTER TABLE hk_system_leave_setting_mst ADD CONSTRAINT PK_hk_system_leave_setting_mst 
	PRIMARY KEY (id);

