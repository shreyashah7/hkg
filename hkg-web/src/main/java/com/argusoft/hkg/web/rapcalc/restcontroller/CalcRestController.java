/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.rapcalc.restcontroller;

import com.argusoft.generic.database.exception.GenericDatabaseException;
import com.argusoft.hkg.common.constantutil.HkSystemConstantUtil;
import static com.argusoft.hkg.common.constantutil.HkSystemConstantUtil.RAP_CALC_FIELD_MAP;
import com.argusoft.hkg.core.HkFieldService;
import com.argusoft.hkg.core.HkFoundationService;
import com.argusoft.hkg.model.HkFieldEntity;
import com.argusoft.hkg.model.HkMasterEntity;
import com.argusoft.hkg.model.HkValueEntity;
import com.argusoft.hkg.nosql.core.HkRapCalcService;
import com.argusoft.hkg.nosql.model.HkCalcBasPriceDocument;
import com.argusoft.hkg.nosql.model.HkCalcFourCDiscountDocument;
import com.argusoft.hkg.nosql.model.HkCalcMasterDocument;
import com.argusoft.hkg.nosql.model.HkCalcMasterMapping;
import com.argusoft.hkg.nosql.model.HkCalcRateDetailDocument;
import com.argusoft.hkg.nosql.model.RangeDocument;
import com.argusoft.hkg.sync.center.core.HkUMSyncService;
import com.argusoft.hkg.web.center.rapcalc.databeans.CalcDicsountDetailsDatabean;
import com.argusoft.hkg.web.customfield.databeans.CustomFieldDataBean;
import com.argusoft.hkg.web.customfield.databeans.CustomFieldInfoDataBean;
import com.argusoft.hkg.web.customfield.transformers.CustomFieldTransformerBean;
import com.argusoft.hkg.web.rapcalc.databeans.HkCalcBasPriceDatabean;
import com.argusoft.hkg.web.rapcalc.databeans.HkCalcFourCDiscountDatabean;
import com.argusoft.hkg.web.rapcalc.databeans.HkCalcMasterDatabean;
import static com.argusoft.hkg.web.rapcalc.restcontroller.WebsocketServer.applicableFromDate;
import com.argusoft.hkg.web.usermanagement.wrapper.UserManagementServiceWrapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author shruti
 */
//@RestController
//@RequestMapping("/calcinit")
@Service
public class CalcRestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CalcRestController.class);
    private final String URL = "http://localhost:8084/HKGDB//api/v1";
    boolean isFirstRequest = false;
    private Date lastSyncDate = new Date();
    private int statusCode = 200;
    private Type typeOfList = new TypeToken<List<String>>() {
    }.getType();
    private Gson gson;
    public final static String CONTENT_TYPE = "application/json;charset=UTF-8";

    @Autowired
    private HkUMSyncService uMSyncService;

    @Autowired
    private HkRapCalcService calcService;

    @Autowired
    private CustomFieldTransformerBean customFieldTransformerBean;

    @Autowired
    private HkFoundationService foundationService;

    @Autowired
    private HkFieldService fieldService;

    @Autowired
    private UserManagementServiceWrapper userManagementServiceWrapper;

    public CalcRestController() {
        final JsonSerializer<Date> dateSerializer = (Date src, Type typeOfSrc, JsonSerializationContext context) -> src == null ? null : new JsonPrimitive(src.getTime());
        final JsonDeserializer<Date> dateDeserializer = (JsonElement json, Type typeOfT, JsonDeserializationContext context) -> {
            if (json != null) {
                try {
                    return new Date(json.getAsLong());
                } catch (NumberFormatException numberFormatException) {
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    try {
                        return format.parse(json.getAsString());
//                    return new Date(json.getAsString());
                    } catch (ParseException ex) {
                        java.util.logging.Logger.getLogger(CalcRestController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    return new Date(json.getAsString());
                }
            } else {
                return null;
            }
        };

        gson = new GsonBuilder().registerTypeAdapter(Date.class, dateSerializer)
                .registerTypeAdapter(Date.class, dateDeserializer).create();
    }

    @RequestMapping(value = "/fetchdata", method = RequestMethod.POST)
    public void fetchData(@RequestBody Map<String, String> paramMap, HttpServletResponse httpServletResponse) {
        //Retrieve database name, username and password from paramMap.
        String username = paramMap.get("username");
        String password = paramMap.get("password");
        String dbname = paramMap.get("dbname");

        //Check if this is initial setup.
        long countofActiveDocuments = uMSyncService.getCountofActiveDocuments(HkCalcBasPriceDocument.class, "isActive", "isArchive");
        if (countofActiveDocuments == 0) {
            isFirstRequest = true;
            lastSyncDate = new Date(0l);
        }

//        new Runnable() {
//            @Override
//            public void run() {
//                Object object = null;
////                try {
////                    object = executeRequest(new HttpGet(), URL + "/sql?sql=SELECT%20%22PRP%22.%22MPRP%22%20as%20customfield,%20%22PRP%22.%22VAL%22%20as%20name,%20%22PRP%22.%22SRT%22%20as%20sequence%20FROM%20%22MPRP%22,%20%22PRP%22%20WHERE%20%22MPRP%22.%22PRP%22%20=%20%22PRP%22.%22MPRP%22%20AND%20%22MPRP%22.%22DTA_TYP%22%20=%20%27C%27%20ORDER%20BY%20%22MPRP%22.%22PRP%22%20ASC;", null, null, HkCalcMasterDatabean.class);
////                } catch (IOException ex) {
////                    java.util.logging.Logger.getLogger(CalcRestController.class.getName()).log(Level.SEVERE, null, ex);
////                }
////                //convert json -> list<String> to List<CalcBasePriceDocument> using Gson API.
////                if (object instanceof ResponseEntity) {
//////            return object;
////                }
////                if (object != null) {
////                    HkCalcMasterDatabean masterDatabean = (HkCalcMasterDatabean) object;
////                    convertAndSaveMasters(masterDatabean.getData());
////                }
////                try {
//////                    object = executeRequest(new HttpGet(), URL + "/sql?sql=SELECT MPRP.PRP parameter,   decode(MPRP.dta_typ, 'C', val_fr, num_fr) fromValue,  decode(MPRP.dta_typ, 'C', val_to, num_to) toValue,    PRI_GRP_PRP.NME groupname,    MPRI.PCT discountPercentage,    MPRI.VLU  mixValue FROM MPRP   INNER JOIN PRI_DTL   ON MPRP.PRP = PRI_DTL.MPRP   INNER JOIN MPRI   ON MPRI.IDN = PRI_DTL.MPRI_IDN   INNER JOIN PRI_GRP_PRP   ON MPRI.PRI_GRP = PRI_GRP_PRP.NME where  PRI_GRP_PRP.dsp_flg= 'I' order by  PRI_GRP_PRP.NME;", null, null, typeOfList);
////                    object = executeRequest(new HttpGet(), URL + "/sql?sql=SELECT%20%22ITM_BSE_PRI%22.%22MFG_DIS%22%20as%20discount,%22ITM_BSE%22.%22SHAPE%22%20as%20shape,%20%22ITM_BSE%22.%22COL%22%20as%20color,%20%22ITM_BSE%22.%22CLR%22%20as%20clarity,%20%22ITM_BSE%22.%22WT_MIN%22%20as%20caratfrom,%20%22ITM_BSE%22.%22WT_MAX%22%20as%20caratto,%20%22ITM_BSE_PRI%22.%22MFG_RTE%22%20as%20rate,%20%22ITM_BSE_PRI%22.%22FRM_DTE%22%20as%20fromDate,%20%22ITM_BSE_PRI%22.%22TO_DTE%22%20as%20toDate%20FROM%20%22ITM_BSE%22,%20%22ITM_BSE_PRI%22%20WHERE%20%22ITM_BSE%22.%22IDN%22%20=%20%22ITM_BSE_PRI%22.%22ITM_BSE_IDN%22;", null, null, HkCalcFourCDiscountDatabean.class);
////                    //convert json -> list<String> to List<CalcBasePriceDocument> using Gson API.
////                    if (object instanceof ResponseEntity) {
//////            return object;
////                    }
////                    HkCalcFourCDiscountDatabean fourCDiscountDatabean = (HkCalcFourCDiscountDatabean) object;
////                    convertandSaveRateDetailDocuments(fourCDiscountDatabean.getData());
////                } catch (IOException ex) {
////                    java.util.logging.Logger.getLogger(CalcRestController.class.getName()).log(Level.SEVERE, null, ex);
////                }
//                //Send request to retrieve raprate from last sync date.
////                long queryCount = 0;
////                try {
//////                    object = executeRequest(new HttpGet(), URL + "/sql?sql=SELECT MPRP.PRP parameter,   decode(MPRP.dta_typ, 'C', val_fr, num_fr) fromValue,  decode(MPRP.dta_typ, 'C', val_to, num_to) toValue,    PRI_GRP_PRP.NME groupname,    MPRI.PCT discountPercentage,    MPRI.VLU  mixValue FROM MPRP   INNER JOIN PRI_DTL   ON MPRP.PRP = PRI_DTL.MPRP   INNER JOIN MPRI   ON MPRI.IDN = PRI_DTL.MPRI_IDN   INNER JOIN PRI_GRP_PRP   ON MPRI.PRI_GRP = PRI_GRP_PRP.NME where  PRI_GRP_PRP.dsp_flg= 'I' order by  PRI_GRP_PRP.NME;", null, null, typeOfList);
//////                    object = executeRequest(new HttpGet(), URL + "/sql?sql=SELECT%20%22MPRI%22.%22REV_DTE%22%20as%20loaddate,%20%22MPRI%22.%22IDN%22%20AS%20idn,%20%22MPRP%22.%22PRP%22%20AS%20parameter,%20%22PRI_GRP_PRP%22.%22NME%22%20AS%20groupName,%20%22MPRI%22.%22PCT%22%20AS%20discountPercentage,%20%22MPRI%22.%22VLU%22%20as%20mixValue,%20%22PRI_DTL%22.%22VAL_TO%22%20as%20toValue,%20%22PRI_DTL%22.%22VAL_FR%22%20as%20fromValue%20FROM%20public.%22MPRP%22,%20public.%22PRI_DTL%22,%20public.%22PRI_GRP_PRP%22,%20public.%22MPRI%22%20WHERE%20%22MPRP%22.%22PRP%22%20=%20%22PRI_DTL%22.%22MPRP%22%20AND%20%22PRI_DTL%22.%22MPRI_IDN%22%20=%20%22MPRI%22.%22IDN%22%20AND%20%22MPRI%22.%22PRI_GRP%22%20=%20%22PRI_GRP_PRP%22.%22NME%22%20and%20%22MPRP%22.%22DTA_TYP%22%20=%20%27C%27%20and%20%22PRI_GRP_PRP%22.%22DSP_FLG%22=%27I%27%20and%20%22PRI_DTL%22.%22VAL_TO%22%20is%20not%20null%20and%20%22PRI_DTL%22.%22VAL_FR%22%20is%20not%20null%20order%20by%20%22MPRI%22.%22IDN%22;", null, null, CalcDicsountDetailsDatabean.class);
////
////                    object = executeRequest(new HttpGet(), URL + "/sql?sql=SELECT%20count(%22MPRI%22.%22IDN%22)%20FROM%20public.%22PRI_DTL%22,%20public.%22MPRI%22,%20public.%22PRI_GRP%22%20WHERE%20%22PRI_DTL%22.%22MPRI_IDN%22%20=%20%22MPRI%22.%22IDN%22%20AND%20%22MPRI%22.%22PRI_GRP%22%20=%20%22PRI_GRP%22.%22NME%22%20and%20%22PRI_DTL%22.%22VAL_TO%22%20is%20not%20null%20and%20%22PRI_DTL%22.%22VAL_FR%22%20is%20not%20null%20;", null, null, QueryCount.class);
////                    //convert json -> list<String> to List<CalcBasePriceDocument> using Gson API.
////                    if (object instanceof ResponseEntity) {
//////            return object;
////                    }
////                    QueryCount count = (QueryCount) object;
////                    queryCount = count.getData().get(0).getCount();
////                } catch (IOException ex) {
////                    java.util.logging.Logger.getLogger(CalcRestController.class.getName()).log(Level.SEVERE, null, ex);
////                }
////                try {
////                    long offset = 0;
////                    int limit = 10000;
////                    while (offset <= queryCount) {
//////                    object = executeRequest(new HttpGet(), URL + "/sql?sql=SELECT MPRP.PRP parameter,   decode(MPRP.dta_typ, 'C', val_fr, num_fr) fromValue,  decode(MPRP.dta_typ, 'C', val_to, num_to) toValue,    PRI_GRP_PRP.NME groupname,    MPRI.PCT discountPercentage,    MPRI.VLU  mixValue FROM MPRP   INNER JOIN PRI_DTL   ON MPRP.PRP = PRI_DTL.MPRP   INNER JOIN MPRI   ON MPRI.IDN = PRI_DTL.MPRI_IDN   INNER JOIN PRI_GRP_PRP   ON MPRI.PRI_GRP = PRI_GRP_PRP.NME where  PRI_GRP_PRP.dsp_flg= 'I' order by  PRI_GRP_PRP.NME;", null, null, typeOfList);
//////                    object = executeRequest(new HttpGet(), URL + "/sql?sql=SELECT%20%22MPRI%22.%22REV_DTE%22%20as%20loaddate,%20%22MPRI%22.%22IDN%22%20AS%20idn,%20%22MPRP%22.%22PRP%22%20AS%20parameter,%20%22PRI_GRP_PRP%22.%22NME%22%20AS%20groupName,%20%22MPRI%22.%22PCT%22%20AS%20discountPercentage,%20%22MPRI%22.%22VLU%22%20as%20mixValue,%20%22PRI_DTL%22.%22VAL_TO%22%20as%20toValue,%20%22PRI_DTL%22.%22VAL_FR%22%20as%20fromValue%20FROM%20public.%22MPRP%22,%20public.%22PRI_DTL%22,%20public.%22PRI_GRP_PRP%22,%20public.%22MPRI%22%20WHERE%20%22MPRP%22.%22PRP%22%20=%20%22PRI_DTL%22.%22MPRP%22%20AND%20%22PRI_DTL%22.%22MPRI_IDN%22%20=%20%22MPRI%22.%22IDN%22%20AND%20%22MPRI%22.%22PRI_GRP%22%20=%20%22PRI_GRP_PRP%22.%22NME%22%20and%20%22MPRP%22.%22DTA_TYP%22%20=%20%27C%27%20and%20%22PRI_GRP_PRP%22.%22DSP_FLG%22=%27I%27%20and%20%22PRI_DTL%22.%22VAL_TO%22%20is%20not%20null%20and%20%22PRI_DTL%22.%22VAL_FR%22%20is%20not%20null%20order%20by%20%22MPRI%22.%22IDN%22;", null, null, CalcDicsountDetailsDatabean.class);
////                        object = executeRequest(new HttpGet(), URL + "/sql?sql=SELECT%20%22MPRI%22.%22REV_DTE%22%20as%20loaddate,%22MPRI%22.%22IDN%22%20AS%20idn,%20%22PRI_DTL%22.%22MPRP%22%20AS%20parameter,%20%22PRI_GRP%22.%22NME%22%20AS%20groupName,%20%22MPRI%22.%22PCT%22%20AS%20discountPercentage,%20%22MPRI%22.%22VLU%22%20as%20mixValue,%20%22PRI_DTL%22.%22VAL_TO%22%20as%20toValue,%20%22PRI_DTL%22.%22VAL_FR%22%20as%20fromValue%20FROM%20public.%22PRI_DTL%22,%20public.%22MPRI%22,%20public.%22PRI_GRP%22%20WHERE%20%22PRI_DTL%22.%22MPRI_IDN%22%20=%20%22MPRI%22.%22IDN%22%20AND%20%22MPRI%22.%22PRI_GRP%22%20=%20%22PRI_GRP%22.%22NME%22%20and%20%22PRI_DTL%22.%22VAL_TO%22%20is%20not%20null%20and%20%22PRI_DTL%22.%22VAL_FR%22%20is%20not%20null%20order%20by%20%22MPRI%22.%22IDN%22%20limit%20" + limit + "%20offset%20" + offset + ";", null, null, CalcDicsountDetailsDatabean.class);
////                        offset += limit;
////                        System.out.println("OFFSET:: " + offset + " count:" + queryCount);
////                        //convert json -> list<String> to List<CalcBasePriceDocument> using Gson API.
////                        if (object instanceof ResponseEntity) {
//////            return object;
////                        }
////                CalcDicsountDetailsDatabean calcDicsountDetailsDatabean = (CalcDicsountDetailsDatabean) object;
////                convertandSaveCalcRateDetailDocuments(calcDicsountDetailsDatabean);
////                    }
////                } catch (IOException ex) {
////                    java.util.logging.Logger.getLogger(CalcRestController.class.getName()).log(Level.SEVERE, null, ex);
////                }
//                try {
//                    final JsonSerializer<Date> dateSerializer = (Date src, Type typeOfSrc, JsonSerializationContext context) -> src == null ? null : new JsonPrimitive(src.getTime());
//                    final JsonDeserializer<Date> dateDeserializer = (JsonElement json, Type typeOfT, JsonDeserializationContext context) -> {
//                        if (json != null) {
//                            try {
//                                return new Date(json.getAsLong());
//                            } catch (NumberFormatException numberFormatException) {
//                                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
//                                try {
//                                    return format.parse(json.getAsString());
////                    return new Date(json.getAsString());
//                                } catch (ParseException ex) {
//                                    java.util.logging.Logger.getLogger(CalcRestController.class.getName()).log(Level.SEVERE, null, ex);
//                                }
//                                return new Date(json.getAsString());
//                            }
//                        } else {
//                            return null;
//                        }
//                    };
//
//                    gson = new GsonBuilder().registerTypeAdapter(Date.class, dateSerializer)
//                            .registerTypeAdapter(Date.class, dateDeserializer).create();
//                    object = executeRequest(new HttpGet(), URL + "/sql?sql=SELECT%20%22SHAPE%22%20shape,%20%22PURITY%22%20quality,%20%22COLOUR%22%20color,%20%22WT_MIN%22%20caratFrom,%20%22WT_MAX%22%20caratTo,%20%22PRICE%22,%20%22LOAD_DATE%22%20loadDate%20FROM%20%22RAPNET%22;", null, null, HkCalcBasPriceDatabean.class);
//                } catch (IOException ex) {
//                    java.util.logging.Logger.getLogger(CalcRestController.class.getName()).log(Level.SEVERE, null, ex);
//                }
//                //convert json -> list<String> to List<CalcBasePriceDocument> using Gson API.
//                if (object instanceof ResponseEntity) {
////            return object;
//                }
//                if (object != null) {
//
//                    List<HkCalcBasPriceDatabean> basPriceDatabean = (List<HkCalcBasPriceDatabean>) object;
////                    saveBasePriceDocument(basPriceDatabean, HkCalcBasPriceDocument.class);
//                }
//
////                //Get discount details for 4c
//            }
//        }.run();
        //Send request to fetch discount details.
        //Convert json -> list<String> to List<databeans>
        //convert databean to calc rate document. As the querie orders data by mprp. group name use it to reduce retrievals and checking.
    }

    public void convertandSaveRateDetailDocuments(List<HkCalcFourCDiscountDatabean> fourCDiscountRecords) {
        HkCalcMasterDocument colorMaster = null;
        HkCalcMasterDocument shapeMaster = null;
        HkCalcMasterDocument qualMaster = null;

        //Retrieve masterValues for color, shape,Quality and set them in map. This map is used to decrease databse calls. It shall contain value:masterdocumentlist
        Map<String, HkCalcMasterDocument> colorMap = new HashMap<>();
        Map<String, HkCalcMasterDocument> shapeMap = new HashMap<>();
        Map<String, HkCalcMasterDocument> qualityMap = new HashMap<>();

        List<HkCalcMasterDocument> masterDocuments = calcService.getCalcMastersyMasterCode(((HkCalcMasterMapping) uMSyncService.getDocumentById("COL", HkCalcMasterMapping.class)).getMasterId());
        for (HkCalcMasterDocument masterDocument : masterDocuments) {
            colorMap.put(masterDocument.getValue().toString(), masterDocument);
        }
        masterDocuments = calcService.getCalcMastersyMasterCode(((HkCalcMasterMapping) uMSyncService.getDocumentById("SHAPE", HkCalcMasterMapping.class)).getMasterId());
        for (HkCalcMasterDocument masterDocument : masterDocuments) {
            shapeMap.put(masterDocument.getValue().toString(), masterDocument);
        }
        masterDocuments = calcService.getCalcMastersyMasterCode(((HkCalcMasterMapping) uMSyncService.getDocumentById("CLR", HkCalcMasterMapping.class)).getMasterId());
        for (HkCalcMasterDocument masterDocument : masterDocuments) {
            qualityMap.put(masterDocument.getValue().toString(), masterDocument);
        }
        try {
            for (HkCalcFourCDiscountDatabean fourCDiscountDatabean : fourCDiscountRecords) {
                System.out.println("fourCDiscountDatabean:::" + fourCDiscountDatabean);
//                HkCalcBasPriceDatabean basPriceDatabean = gson.fromJson(documentStringList1, HkCalcBasPriceDatabean.class);

                //Get masters for color,quality and shape
//                List<String> masterCodes = new LinkedList<>();
//                masterCodes.add(HkSystemConstantUtil.RAP_CALC_FIELD_MAP.get(HkSystemConstantUtil.FourC.COLOR));
//                masterCodes.add(HkSystemConstantUtil.RAP_CALC_FIELD_MAP.get(HkSystemConstantUtil.FourC.SHAPE));
//                masterCodes.add(HkSystemConstantUtil.RAP_CALC_FIELD_MAP.get(HkSystemConstantUtil.FourC.QUALITY));
//                Map<String, HkMasterEntity> retrieveMaster = foundationService.retrieveMaster(masterCodes);
//                System.out.println("");
                colorMaster = colorMap.get(fourCDiscountDatabean.getColor().trim());
                shapeMaster = shapeMap.get(fourCDiscountDatabean.getShape().trim());
                qualMaster = qualityMap.get(fourCDiscountDatabean.getClarity().trim());
//                System.out.println("COLORMASTER:: " + colorMaster + " shapemaster: " + shapeMaster + " qual: " + qualMaster);
//                if (colorMaster == null) {
//                    colorMaster = addMasterValue(fourCDiscountRecord.getColor(), retrieveMaster.get(HkSystemConstantUtil.RAP_CALC_FIELD_MAP.get(HkSystemConstantUtil.FourC.COLOR)), colorSequance++);
//                    colorMap.put(fourCDiscountRecord.getColor(), colorMaster);
//                }
//                if (qualMaster == null) {
//                    qualMaster = addMasterValue(fourCDiscountRecord.getClarity(), retrieveMaster.get(HkSystemConstantUtil.RAP_CALC_FIELD_MAP.get(HkSystemConstantUtil.FourC.QUALITY)), qualSequance++);
//                    qualityMap.put(fourCDiscountRecord.getClarity(), qualMaster);
//                }
//                if (shapeMaster == null) {
//                    shapeMaster = addMasterValue(fourCDiscountRecord.getShape(), retrieveMaster.get(HkSystemConstantUtil.RAP_CALC_FIELD_MAP.get(HkSystemConstantUtil.FourC.SHAPE)), shapeSequance++);
//                    shapeMap.put(fourCDiscountRecord.getShape(), shapeMaster);
//                }
                HkCalcFourCDiscountDocument fourCDiscountDocument = new HkCalcFourCDiscountDocument();
                fourCDiscountDocument.setCaratFrom(fourCDiscountDatabean.getCaratfrom());
                fourCDiscountDocument.setCaratTo(fourCDiscountDatabean.getCaratto());
                fourCDiscountDocument.setDiscount(fourCDiscountDatabean.getDiscount());
                fourCDiscountDocument.setRate(fourCDiscountDatabean.getRate());
                fourCDiscountDocument.setColor(colorMaster.getMasterValueId());
                fourCDiscountDocument.setClarity(qualMaster.getMasterValueId());
                fourCDiscountDocument.setShape(shapeMaster.getMasterValueId());
                fourCDiscountDocument.setCreatedOn(new Date());
                fourCDiscountDocument.setModifiedOn(new Date());
                fourCDiscountDocument.setFromDate(applicableFromDate);
                if (fourCDiscountDatabean.getTodate().compareTo(applicableFromDate) > 0) {
                    fourCDiscountDocument.setToDate(fourCDiscountDatabean.getTodate());
                } else {
                    fourCDiscountDocument.setToDate(applicableFromDate);
                }
                fourCDiscountDocument.setIsActive(true);
                fourCDiscountDocument.setIsActive(true);
                System.out.println("Saving fourCDiscountDocument.........");
                uMSyncService.saveOrUpdateDocument(fourCDiscountDocument);
            }
//                        }
        } catch (Exception e) {
            LOGGER.info("Exception: ");
            e.printStackTrace();
            statusCode = 500;
        }

    }

    public void convertAndSaveMasters(List<HkCalcMasterDatabean> masters) {
        Long rapCalcFeatureId = null;
        Map<Long, String> retrieveFeatureIdWithNameMap = userManagementServiceWrapper.retrieveFeatureIdWithNameMap();
        for (Map.Entry<Long, String> entrySet : retrieveFeatureIdWithNameMap.entrySet()) {
            Long key = entrySet.getKey();
            String value = entrySet.getValue();
            if (HkSystemConstantUtil.Feature.ROUGHCALC.equals(value)) {
                rapCalcFeatureId = key;
            }
        }
        Map<String, Map<String, HkCalcMasterDocument>> customfieldMap = new HashMap<>();

        Map<String, String> masterMap = new HashMap<>();

        List<String> masterCodes = new LinkedList<>();
        List<HkCalcMasterMapping> retrieveAllDocuments = (List<HkCalcMasterMapping>) uMSyncService.retrieveAllDocuments(HkCalcMasterMapping.class);
        if (!CollectionUtils.isEmpty(retrieveAllDocuments)) {
            for (HkCalcMasterMapping masterMapping : retrieveAllDocuments) {
                masterMap.put(masterMapping.getId(), masterMapping.getMasterId());
                //set mastercode
                masterCodes.add(masterMapping.getMasterId());
                List<HkCalcMasterDocument> masterDocuments = calcService.getCalcMastersyMasterCode(masterMapping.getMasterId());
                Map<String, HkCalcMasterDocument> calcMasterMap = new HashMap<>();
                if (!CollectionUtils.isEmpty(masterDocuments)) {
                    for (HkCalcMasterDocument masterDocument : masterDocuments) {
                        calcMasterMap.put(masterDocument.getValue().toString(), masterDocument);
                    }
                }
                customfieldMap.put(masterMapping.getId(), calcMasterMap);
            }
        }

        Map<String, HkMasterEntity> calcMasterDocuments = foundationService.retrieveMaster(masterCodes);
        for (HkCalcMasterDatabean master : masters) {

            System.out.println("master:" + master);

            if (!masterMap.containsKey(master.getCustomfield().trim())) {
                try {
                    createCustomField(master.getCustomfield().trim(), "", HkSystemConstantUtil.Feature.RAPCALC, "genralSection", "Dropdown", "Long", "{\"formula\":\"\",\"defaultValue\":\"\"}", rapCalcFeatureId);

                    HkCalcMasterMapping masterMapping = new HkCalcMasterMapping();
                    masterMapping.setId(master.getCustomfield());
                    List<HkFieldEntity> fieldEntitys = fieldService.retrieveFieldByFieldLabel(master.getCustomfield(), rapCalcFeatureId);
                    if (!CollectionUtils.isEmpty(fieldEntitys)) {
                        createPointersForSublot(master.getCustomfield().trim(), rapCalcFeatureId, fieldEntitys.get(0));
                        masterMapping.setMasterId(fieldEntitys.get(0).getId().toString());
                        uMSyncService.saveOrUpdateDocument(masterMapping);
                        masterMap.put(masterMapping.getId(), masterMapping.getMasterId());
                        customfieldMap.put(masterMapping.getId(), new HashMap<>());
                        masterCodes.add(masterMapping.getMasterId());
                        calcMasterDocuments = foundationService.retrieveMaster(masterCodes);
                    }
                } catch (Exception ex) {
                    java.util.logging.Logger.getLogger(CalcRestController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            //check if master value has already been added. if not then add it.
            Map<String, HkCalcMasterDocument> map = customfieldMap.get(master.getCustomfield());

            if (!map.containsKey(master.getName().trim())) {
//                System.out.println("calcMasterDocuments.get(masterMap.get(master.getCustomfield())) " + calcMasterDocuments.get(masterMap.get(master.getCustomfield())) + "   " + masterMap.get(master.getCustomfield().trim()));
                addMasterValue(master.getName(), calcMasterDocuments.get(masterMap.get(master.getCustomfield().trim())), master.getSequence());
            }
        }
    }

    public void convertandSaveCalcRateDetailDocuments(List<CalcDicsountDetailsDatabean> dicsountDetailsDatabeans) {

        HkCalcMasterDocument prevRdateDoc = calcService.retrieveLastCalcMasterDocument();
        HkCalcMasterDocument rDateCalcMasterDocument;
        String rdateMasterCode;

        Map<String, String> masterMapping = new HashMap<>();
        if (!CollectionUtils.isEmpty(dicsountDetailsDatabeans)) {
            try {
                Map<Long, String> retrieveFeatureIdWithNameMap = userManagementServiceWrapper.retrieveFeatureIdWithNameMap();
                Long rapCalcFeatureId = null;
                Long sublotFeatureId = null;
                for (Map.Entry<Long, String> entrySet : retrieveFeatureIdWithNameMap.entrySet()) {
                    Long key = entrySet.getKey();
                    String value = entrySet.getValue();
                    if (HkSystemConstantUtil.Feature.ROUGHCALC.equals(value)) {
                        rapCalcFeatureId = key;
                    } else if (HkSystemConstantUtil.Feature.SUB_LOT.equals(value)) {
                        sublotFeatureId = key;
                    }
                }

                List<String> masterCodes = new LinkedList<>();
                //Define and fill custom fieldmap. This map will contain mapping of groupname:map<value:master>
                Map<String, Map<String, HkCalcMasterDocument>> customfieldMap = new HashMap<>();
                List<HkCalcMasterMapping> retrieveAllDocuments = (List<HkCalcMasterMapping>) uMSyncService.retrieveAllDocuments(HkCalcMasterMapping.class);
                for (HkCalcMasterMapping master : retrieveAllDocuments) {
                    masterMapping.put(master.getId(), master.getMasterId());
                    //set mastercode
                    masterCodes.add(master.getMasterId());
                    List<HkCalcMasterDocument> masterDocuments = calcService.getCalcMastersyMasterCode(master.getMasterId());
                    Map<String, HkCalcMasterDocument> calcMasterMap = new HashMap<>();
                    if (!CollectionUtils.isEmpty(masterDocuments)) {
                        for (HkCalcMasterDocument masterDocument : masterDocuments) {
                            calcMasterMap.put(masterDocument.getValue().toString(), masterDocument);
                        }
                    }
                    customfieldMap.put(master.getId(), calcMasterMap);
                }
                System.out.println("CUSTOMFIELDMAP:::: " + customfieldMap);
                System.out.println("mastermapping:::: " + masterMapping);
                Map<String, HkMasterEntity> calcMasterDocuments = foundationService.retrieveMaster(masterCodes);
                if (prevRdateDoc != null) {
                    rdateMasterCode = prevRdateDoc.getCode();
                    rDateCalcMasterDocument = addMasterValue(applicableFromDate.toString(), calcMasterDocuments.get(prevRdateDoc.getCode()), prevRdateDoc.getSequence() + 1);
                } else {
                    rdateMasterCode = RAP_CALC_FIELD_MAP.get(HkSystemConstantUtil.FourC.R_DATE);
                    rDateCalcMasterDocument = addMasterValue(applicableFromDate.toString(), calcMasterDocuments.get(RAP_CALC_FIELD_MAP.get(HkSystemConstantUtil.FourC.R_DATE)), 0);
                }
                String idn = "";
                String values = "";

                HkCalcRateDetailDocument document = null;
                CalcDicsountDetailsDatabean calcDiscountDatabean = null;
                Map<String, RangeDocument> discountDetailMap = new HashMap<>();
                for (CalcDicsountDetailsDatabean discountDatabean : dicsountDetailsDatabeans) {
                    System.out.println("discountDatabean::" + discountDatabean);
//                    System.out.println("GROUPNAME:: " + discountDatabean.getGroupname() + "   PARAM:::: " + discountDatabean.getParameter());
                    discountDatabean.setParameter(discountDatabean.getParameter().trim());
                    //TODO: replace dbfieldname with mastermapping
                    if (!masterMapping.containsKey(discountDatabean.getParameter())) {
                        createCustomField(discountDatabean.getParameter(), values, HkSystemConstantUtil.Feature.RAPCALC, "genralSection", "Dropdown", "Long", "{\"formula\":\"\",\"defaultValue\":\"\"}", rapCalcFeatureId);
                        try {

//                          
//retrieve custom field and fill customfield map and hksystemconstantutil rapcalcfield map
//                            Map< String, String> tmpMap = foundationService.retrieveCustomfieldMasterMappingForCalc(Arrays.asList(rapCalcFeatureId));
//                            if (!CollectionUtils.isEmpty(tmpMap)) {
//                                HkSystemConstantUtil.RAP_CALC_FIELD_MAP = tmpMap;
//                                System.out.println("TMPNOT NULL " + RAP_CALC_FIELD_MAP);
//                                }
                            HkCalcMasterMapping masterMappingDocument = new HkCalcMasterMapping();
                            masterMappingDocument.setId(discountDatabean.getParameter());
                            List<HkFieldEntity> fieldEntitys = fieldService.retrieveFieldByFieldLabel(discountDatabean.getParameter(), rapCalcFeatureId);
                            if (!CollectionUtils.isEmpty(fieldEntitys)) {
                                createPointersForSublot(discountDatabean.getParameter(), sublotFeatureId, fieldEntitys.get(0));
                                masterMappingDocument.setMasterId(fieldEntitys.get(0).getId().toString());
                                uMSyncService.saveOrUpdateDocument(masterMappingDocument);
                                masterMapping.put(masterMappingDocument.getId(), masterMappingDocument.getMasterId());
                                customfieldMap.put(masterMappingDocument.getId(), new HashMap<>());
                                masterCodes.add(masterMappingDocument.getMasterId());
                                calcMasterDocuments = foundationService.retrieveMaster(masterCodes);
                            }

                        } catch (Exception e) {
                            java.util.logging.Logger.getLogger(CalcRestController.class.getName()).log(Level.SEVERE, null, e);
                        }
                    }
                    Map<String, HkCalcMasterDocument> map = customfieldMap.get(discountDatabean.getParameter());
//                    if (!map.containsKey(dicsountDetailsDatabean.getParameter())) {
                    String mastercode = masterMapping.get(discountDatabean.getParameter());
//                        addMasterValue(dicsountDetailsDatabean.getParameter(), calcMasterDocuments.get(mastercode), 0);
//
//                    System.out.println("MAP::: " + map);
                    HkCalcMasterDocument fromValueMaster = null;
                    HkCalcMasterDocument toValueMaster = null;
                    if (!CollectionUtils.isEmpty(map)) {
                        fromValueMaster = map.get(discountDatabean.getFromvalue().trim());

                    } else {
                        map = new HashMap<>();
                    }
//                    System.out.println("DBFIELDNAME: " + discountDatabean.getParameter());
//                    System.out.println("MASTERCOED: " + mastercode);
                    if (!StringUtils.isEmpty(mastercode)) {
                        if (fromValueMaster == null) {
                            fromValueMaster = addMasterValue(discountDatabean.getFromvalue(), calcMasterDocuments.get(mastercode), 0);
                            map.put(discountDatabean.getParameter(), fromValueMaster);
                            customfieldMap.put(discountDatabean.getParameter(), map);
                        }
                        toValueMaster = map.get(discountDatabean.getTovalue().trim());
                        if (toValueMaster == null) {
                            toValueMaster = addMasterValue(discountDatabean.getTovalue(), calcMasterDocuments.get(mastercode), 0);
                            map.put(discountDatabean.getParameter(), toValueMaster);
                            customfieldMap.put(discountDatabean.getParameter(), map);
                        }
                        if (calcDiscountDatabean == null) {
                            RangeDocument rangeDocument = new RangeDocument(fromValueMaster, toValueMaster);
                            discountDetailMap = new HashMap<>();
                            discountDetailMap.put(mastercode, rangeDocument);
                            calcDiscountDatabean = discountDatabean;
                            idn = discountDatabean.getIdn().trim();

                        } else {
                            if (idn.equals(discountDatabean.getIdn().trim())) {
                                RangeDocument rangeDocument = new RangeDocument(fromValueMaster, toValueMaster);
                                discountDetailMap.put(mastercode, rangeDocument);
                            } else {
                                discountDetailMap.put(rdateMasterCode, new RangeDocument(rDateCalcMasterDocument, rDateCalcMasterDocument));
                                document = convertDiscountDatabeanToCalcRateDetailDocument(calcDiscountDatabean, discountDetailMap);
                                uMSyncService.saveOrUpdateDocument(document);
                                Object existingRateDocument = uMSyncService.getDocumentById(discountDatabean.getIdn(), HkCalcRateDetailDocument.class);
                                if (existingRateDocument == null) {
                                    discountDetailMap = new HashMap<>();
                                } else {
                                    discountDetailMap = ((HkCalcRateDetailDocument) existingRateDocument).getDiscountDetailsMap();
                                }
                                RangeDocument rangeDocument = new RangeDocument(fromValueMaster, toValueMaster);
                                discountDetailMap.put(mastercode, rangeDocument);
                                calcDiscountDatabean = discountDatabean;
                                idn = discountDatabean.getIdn();
//                                break;
//                        document = calcService.getCalcRateDetailDocumentsByGroupName(groupName);
//                        document = convertDiscountDatabeanToCalcRateDetailDocument(rateRecord, discountDetailMap);
                            }
                        }
                    } else {
//                        System.out.println("MASTRCODE NOT FOUND FOR DBFIELDNAME: " + discountDatabean.getParameter() + "---");
                    }
                }
                System.out.println("Saving document......." + document);
                uMSyncService.saveOrUpdateDocument(document);
            } catch (Exception e) {
                LOGGER.info("Exception: ", e);
                statusCode = 500;
            }
        }
    }

    public void createCustomField(String name, String values, String featureName, String section, String type, String fieldType, String validationPattern, Long featureId) {
        try {
            CustomFieldDataBean customFieldDataBean = new CustomFieldDataBean();
            customFieldDataBean.setFeatureName(featureName);
            customFieldDataBean.setIsDependable(false);
            customFieldDataBean.setOldLabelName(name);
            customFieldDataBean.setIsEditable(true);
            customFieldDataBean.setIsNewField(true);
            customFieldDataBean.setIsPrivate(false);
            customFieldDataBean.setLabel(name);
            customFieldDataBean.setDbFieldName(name);
            customFieldDataBean.setSectionName(section);
            if (fieldType != null) {
                customFieldDataBean.setFieldType(fieldType);
            }
            if (type != null) {
                customFieldDataBean.setType(type);
            }
            customFieldDataBean.setValidationPattern(validationPattern);
            if (values != null) {
                customFieldDataBean.setValues(values);
            }
            CustomFieldInfoDataBean customFieldInfoDataBean = new CustomFieldInfoDataBean();
            customFieldInfoDataBean.setFeatureId(featureId);
            customFieldInfoDataBean.setSectionId(-1l);
            customFieldInfoDataBean.setCustomFieldDataBean(customFieldDataBean);
            customFieldTransformerBean.createCustomField(customFieldInfoDataBean, true, 1l, 1l);
        } catch (GenericDatabaseException ex) {
            java.util.logging.Logger.getLogger(CalcRestController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception e) {
            java.util.logging.Logger.getLogger(CalcRestController.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    private void createPointersForSublot(String featureName, Long featureId, HkFieldEntity fieldEntity) {

        //Create from field
        createCustomField(featureName + "_FROM", null, HkSystemConstantUtil.Feature.SUB_LOT, "genralSection", "Pointer", null, "{\"pointer\":\"" + fieldEntity.getId() + "\",\"pointerOption\":[{\"id\":\"" + fieldEntity.getId() + "\",\"text\":\"RoughCalcy." + fieldEntity.getFieldLabel() + "\"}]}", featureId);

        //Create to field
        createCustomField(featureName + "_TO", null, HkSystemConstantUtil.Feature.SUB_LOT, "genralSection", "Pointer", null, "{\"pointer\":\"" + fieldEntity.getId() + "\",\"pointerOption\":[{\"id\":\"" + fieldEntity.getId() + "\",\"text\":\"RoughCalcy." + fieldEntity.getFieldLabel() + "\"}]}", featureId);
    }

    private HkCalcRateDetailDocument convertDiscountDatabeanToCalcRateDetailDocument(CalcDicsountDetailsDatabean discountDatabean, Map<String, RangeDocument> discountDetailMap) {
        HkCalcRateDetailDocument rateDetailDocument = new HkCalcRateDetailDocument();
        rateDetailDocument.setId(discountDatabean.getIdn());
//        rateDetailDocument.setCraetedOn(new Date());
        rateDetailDocument.setDiscount(discountDatabean.getDiscountpercentage());
        rateDetailDocument.setGroupName(discountDatabean.getGroupname());
        rateDetailDocument.setMixamount(discountDatabean.getMixValue());
        if (discountDatabean.getLoaddate() != null) {
//            discountDetailMap.put("", new RangeDocument(null, null));
            rateDetailDocument.setCraetedOn(discountDatabean.getLoaddate());
            rateDetailDocument.setCraetedOn(discountDatabean.getLoaddate());
        } else {
            rateDetailDocument.setCraetedOn(new Date());
            rateDetailDocument.setCraetedOn(new Date());
        }
        discountDetailMap.put("", new RangeDocument(null, null));
        rateDetailDocument.setDiscountDetailsMap(discountDetailMap);
        return rateDetailDocument;
    }

    public int saveBasePriceDocument(List<HkCalcBasPriceDatabean> basPriceDatabeans, Class<?> class1) {
//        System.out.println("calcrecords:: " + calcRecords);
        HkCalcMasterDocument colorMaster = null;
        HkCalcMasterDocument shapeMaster = null;
        HkCalcMasterDocument qualMaster = null;
        //Retrieve masterValues for color, shape,Quality and set them in map. This map is used to decrease databse calls. It shall contain value:masterdocumentlist
        Map<String, HkCalcMasterDocument> colorMap = new HashMap<>();
        Map<String, HkCalcMasterDocument> shapeMap = new HashMap<>();
        Map<String, HkCalcMasterDocument> qualityMap = new HashMap<>();

        List<String> masterCodes = new LinkedList<>();

        String colorMasterId = ((HkCalcMasterMapping) uMSyncService.getDocumentById("COL", HkCalcMasterMapping.class
        )).getMasterId();
        masterCodes.add(colorMasterId);
        List<HkCalcMasterDocument> masterDocuments = calcService.getCalcMastersyMasterCode(colorMasterId);

        for (HkCalcMasterDocument masterDocument : masterDocuments) {
            colorMap.put(masterDocument.getValue().toString(), masterDocument);
        }
        String shapeMasterId = ((HkCalcMasterMapping) uMSyncService.getDocumentById("SHAPE", HkCalcMasterMapping.class)).getMasterId();

        masterCodes.add(shapeMasterId);
        masterDocuments = calcService.getCalcMastersyMasterCode(shapeMasterId);
        for (HkCalcMasterDocument masterDocument : masterDocuments) {
            shapeMap.put(masterDocument.getValue().toString(), masterDocument);
        }

        String qualMasterId = ((HkCalcMasterMapping) uMSyncService.getDocumentById("CLR", HkCalcMasterMapping.class)).getMasterId();

        masterCodes.add(qualMasterId);
        masterDocuments = calcService.getCalcMastersyMasterCode(qualMasterId);
        for (HkCalcMasterDocument masterDocument : masterDocuments) {
            qualityMap.put(masterDocument.getValue().toString(), masterDocument);
        }
        Map<String, HkMasterEntity> retrieveMaster = foundationService.retrieveMaster(masterCodes);

        try {
            for (HkCalcBasPriceDatabean basPriceDatabean : basPriceDatabeans) {
//                HkCalcBasPriceDatabean basPriceDatabean = gson.fromJson(documentStringList1, HkCalcBasPriceDatabean.class);

                //Get masters for color,quality and shape
                colorMaster = colorMap.get(basPriceDatabean.getColor().trim());
                shapeMaster = shapeMap.get(basPriceDatabean.getShape().trim());
                qualMaster = qualityMap.get(basPriceDatabean.getQuality().trim());

                String id = basPriceDatabean.getColor().trim() + "-" + basPriceDatabean.getShape().trim() + "-" + basPriceDatabean.getQuality().trim();
                basPriceDatabean.setId(id);
                //Check if master given color, shape and quality master values exists if not then add it
                //I hvae passed random number as sequence as the real sequence is not available at this time
                //For real-time data this block of code should not be reqired.
                if (colorMaster == null) {
                    colorMaster = addMasterValue(basPriceDatabean.getColor(), retrieveMaster.get(colorMasterId), (int) Math.random());
                    colorMap.put(basPriceDatabean.getColor(), colorMaster);
                }
                if (qualMaster == null) {
                    qualMaster = addMasterValue(basPriceDatabean.getQuality(), retrieveMaster.get(qualMasterId), (int) Math.random());
                    qualityMap.put(basPriceDatabean.getQuality(), qualMaster);
                }
                if (shapeMaster == null) {
                    shapeMaster = addMasterValue(basPriceDatabean.getShape(), retrieveMaster.get(shapeMasterId), (int) Math.random());
                    shapeMap.put(basPriceDatabean.getShape(), shapeMaster);
                }
                //master value check end.

                HkCalcBasPriceDocument basPriceDocument = new HkCalcBasPriceDocument();
                basPriceDocument.setBasePrice(Double.parseDouble(basPriceDatabean.getPRICE().trim()));
                basPriceDocument.setCaratFrom(Double.parseDouble(basPriceDatabean.getCaratfrom().trim()));
                basPriceDocument.setCaratTo(Double.parseDouble(basPriceDatabean.getCaratto().trim()));
                basPriceDocument.setColor(colorMaster.getMasterValueId());
                basPriceDocument.setShape(shapeMaster.getMasterValueId());
                basPriceDocument.setQuality(qualMaster.getMasterValueId());
                basPriceDocument.setModifiedOn(new Date());
                basPriceDocument.setLoadDate(applicableFromDate);
//                basPriceDocument.setId(hkCalcRecord.getId());
                System.out.println("Saving basPriceDocument........." + basPriceDocument);
                uMSyncService.saveOrUpdateDocument(basPriceDocument);
            }
//                        }
            LOGGER.debug(class1.getName() + " inserted successfully.");
        } catch (Exception e) {
            LOGGER.info("Exception: ", e);
            e.printStackTrace();
            statusCode = 500;
        }
        return statusCode;
    }

    private HkCalcMasterDocument addMasterValue(String value, HkMasterEntity masterCode, int shortcutCode) {
        HkValueEntity valueEntity = new HkValueEntity();
        valueEntity.setCreatedBy(1l);
        valueEntity.setLastModifiedBy(1l);
        valueEntity.setCreatedOn(new Date());
        valueEntity.setLastModifiedOn(new Date());
        valueEntity.setFranchise(0l);
        valueEntity.setIsActive(true);
        valueEntity.setIsArchive(false);
        valueEntity.setIsOftenUsed(false);
        valueEntity.setKeyCode(masterCode);
        valueEntity.setShortcutCode(shortcutCode);
        valueEntity.setTranslatedValueName(value);
        valueEntity.setValueName(value);
        foundationService.saveMasterValueEntity(valueEntity);
        HkCalcMasterDocument calcMasterDocument = new HkCalcMasterDocument();
        calcMasterDocument.setCode(masterCode.getCode());
        calcMasterDocument.setCreatedOn(new Date());
        calcMasterDocument.setIsActive(true);
        calcMasterDocument.setIsArchive(false);
        calcMasterDocument.setModifiedOn(new Date());
        calcMasterDocument.setMasterValueId(valueEntity.getId());
        //TODO: find sequence
        calcMasterDocument.setSequence(shortcutCode);
        String valueName = value;
        calcMasterDocument.setValue(value);
        calcMasterDocument.setId(masterCode.getCode() + "-" + valueName);
        uMSyncService.saveOrUpdateDocument(calcMasterDocument);
        return calcMasterDocument;
    }
}
