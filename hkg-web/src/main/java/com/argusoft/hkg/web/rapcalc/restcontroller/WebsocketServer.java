/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.rapcalc.restcontroller;

import com.argusoft.hkg.nosql.model.HkCalcBasPriceDocument;
import com.argusoft.hkg.web.center.rapcalc.databeans.CalcDicsountDetailsDatabean;
import com.argusoft.hkg.web.config.WebApplicationInitializerConfig;
import com.argusoft.hkg.web.rapcalc.databeans.HkCalcBasPriceDatabean;
import com.argusoft.hkg.web.rapcalc.databeans.HkCalcFourCDiscountDatabean;
import com.argusoft.hkg.web.rapcalc.databeans.HkCalcMasterDatabean;
import com.argusoft.hkg.web.rapcalc.databeans.QueryDatabean;
import com.argusoft.hkg.web.rapcalc.databeans.ResponseDataBean;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 *
 * @author shruti
 */
@Service
public class WebsocketServer extends WebSocketServer implements WebsocketInterface {

    @Autowired
    ApplicationContext applicationContext;
    private List<QueryDatabean> queryDatabeans;
    private static String CLOSE_CONNECTION = "Close Connection";
    private static String END_OF_RESULT = "End of result";
    private int count = 0;
    private Gson gson;
    final static String host;
    final static int port = 8887;
    public static Date applicableFromDate;
    @Autowired
    private CalcRestController restController;

    public WebsocketServer() throws UnknownHostException {
        super(new InetSocketAddress(host, port));
        System.out.println("IN SERVERRR>>>>>>>");
        initGsonObject();
        init();
    }

    public WebsocketServer(InetSocketAddress address) {
        super(address);
        initGsonObject();
    }

    private void initGsonObject() {
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

    static {
        if (WebApplicationInitializerConfig.MASTER_IP.startsWith("http")) {
            String str = WebApplicationInitializerConfig.MASTER_IP.split("//")[1];
            host = str.split(":")[0];
        } else {
            host = WebApplicationInitializerConfig.MASTER_IP.split(":")[0];
        }
        System.out.println("HOST: " + host);
    }
//    @PostConstruct

    public void init() {
        System.out.println("IN POST CONSTRUCT:: ");
        this.queryDatabeans = new LinkedList<>();
        QueryDatabean databean = new QueryDatabean();
        databean.setClassName(HkCalcMasterDatabean.class.getName());
        databean.setQueryName("GetMasters");
        databean.setQuery("SELECT PRP.MPRP as customfield, PRP.VAL as name, PRP.SRT as sequence FROM MPRP, PRP WHERE MPRP.PRP = PRP.MPRP AND MPRP.DTA_TYP = 'C' ORDER BY MPRP.PRP ASC");
        //postgres query
//        databean.setQuery("SELECT \"PRP\".\"MPRP\" as customfield, \"PRP\".\"VAL\" as name, \"PRP\".\"SRT\" as sequence FROM \"MPRP\", \"PRP\" WHERE \"MPRP\".\"PRP\" = \"PRP\".\"MPRP\" AND \"MPRP\".\"DTA_TYP\" = 'C' ORDER BY \"MPRP\".\"PRP\" ASC");
        databean.setCountQuery("SELECT count(PRP.MPRP) FROM MPRP, PRP WHERE MPRP.PRP = PRP.MPRP AND MPRP.DTA_TYP = 'C'");
        //Postgres query
//        databean.setCountQuery("SELECT count(\"PRP\".\"MPRP\") FROM \"MPRP\", \"PRP\" WHERE \"MPRP\".\"PRP\" = \"PRP\".\"MPRP\" AND \"MPRP\".\"DTA_TYP\" = 'C'");
        queryDatabeans.add(databean);
//
        databean = new QueryDatabean();
        databean.setClassName(HkCalcFourCDiscountDatabean.class.getName());
        databean.setQueryName("GetFourCdiscount");
        databean.setQuery("SELECT ITM_BSE_PRI.MFG_DIS as discount,ITM_BSE.SHAPE as shape, ITM_BSE.COL as color, ITM_BSE.CLR as clarity, ITM_BSE.WT_MIN as caratfrom, ITM_BSE.WT_MAX as caratto, ITM_BSE_PRI.MFG_RTE as rate, ITM_BSE_PRI.FRM_DTE as fromDate, ITM_BSE_PRI.TO_DTE as toDate FROM ITM_BSE, ITM_BSE_PRI WHERE ITM_BSE.IDN = ITM_BSE_PRI.ITM_BSE_IDN");
//        postgres query
//        databean.setQuery("SELECT \"ITM_BSE_PRI\".\"MFG_DIS\" as discount,\"ITM_BSE\".\"SHAPE\" as shape, \"ITM_BSE\".\"COL\" as color, \"ITM_BSE\".\"CLR\" as clarity, \"ITM_BSE\".\"WT_MIN\" as caratfrom, \"ITM_BSE\".\"WT_MAX\" as caratto, \"ITM_BSE_PRI\".\"MFG_RTE\" as rate, \"ITM_BSE_PRI\".\"FRM_DTE\" as fromDate, \"ITM_BSE_PRI\".\"TO_DTE\" as toDate FROM \"ITM_BSE\", \"ITM_BSE_PRI\" WHERE \"ITM_BSE\".\"IDN\" = \"ITM_BSE_PRI\".\"ITM_BSE_IDN\"");
        databean.setCountQuery("SELECT count(ITM_BSE_PRI.MFG_DIS) FROM ITM_BSE, ITM_BSE_PRI WHERE ITM_BSE.IDN = ITM_BSE_PRI.ITM_BSE_IDN");
//postgres query
//        databean.setCountQuery("SELECT count(\"ITM_BSE_PRI\".\"MFG_DIS\") FROM \"ITM_BSE\", \"ITM_BSE_PRI\" WHERE \"ITM_BSE\".\"IDN\" = \"ITM_BSE_PRI\".\"ITM_BSE_IDN\"");
        queryDatabeans.add(databean);

        databean = new QueryDatabean();
        databean.setClassName(CalcDicsountDetailsDatabean.class.getName());
        databean.setQueryName("GetDiscountDetails");
        databean.setQuery("SELECT MPRI.IDN as idn, MPRI.REV_DTE as loaddate, MPRP.PRP parameter,   decode(MPRP.dta_typ, 'C', val_fr, num_fr) fromValue,  decode(MPRP.dta_typ, 'C', val_to, num_to) toValue,    PRI_GRP_PRP.NME groupname,    MPRI.PCT discountPercentage,    MPRI.VLU  mixValue FROM MPRP   INNER JOIN PRI_DTL   ON MPRP.PRP = PRI_DTL.MPRP   INNER JOIN MPRI   ON MPRI.IDN = PRI_DTL.MPRI_IDN   INNER JOIN PRI_GRP_PRP   ON MPRI.PRI_GRP = PRI_GRP_PRP.NME where  PRI_GRP_PRP.dsp_flg= 'I' order by  PRI_GRP_PRP.NME ");
//postgres query
//        databean.setQuery("SELECT \"MPRI\".\"REV_DTE\" as loaddate,\"MPRI\".\"IDN\" AS idn, \"PRI_DTL\".\"MPRP\" AS parameter, \"PRI_GRP\".\"NME\" AS groupName, \"MPRI\".\"PCT\" AS discountPercentage, \"MPRI\".\"VLU\" as mixValue, \"PRI_DTL\".\"VAL_TO\" as toValue, \"PRI_DTL\".\"VAL_FR\" as fromValue FROM public.\"PRI_DTL\", public.\"MPRI\", public.\"PRI_GRP\" WHERE \"PRI_DTL\".\"MPRI_IDN\" = \"MPRI\".\"IDN\" AND \"MPRI\".\"PRI_GRP\" = \"PRI_GRP\".\"NME\" and \"PRI_DTL\".\"VAL_TO\" is not null and \"PRI_DTL\".\"VAL_FR\" is not null order by \"MPRI\".\"IDN\"");
        databean.setCountQuery("SELECT count(MPRI.IDN) FROM MPRP   INNER JOIN PRI_DTL   ON MPRP.PRP = PRI_DTL.MPRP   INNER JOIN MPRI   ON MPRI.IDN = PRI_DTL.MPRI_IDN   INNER JOIN PRI_GRP_PRP   ON MPRI.PRI_GRP = PRI_GRP_PRP.NME where  PRI_GRP_PRP.dsp_flg= 'I'");
//postgres query
//        databean.setCountQuery("SELECT count(\"MPRI\".\"IDN\") FROM public.\"PRI_DTL\", public.\"MPRI\", public.\"PRI_GRP\" WHERE \"PRI_DTL\".\"MPRI_IDN\" = \"MPRI\".\"IDN\" AND \"MPRI\".\"PRI_GRP\" = \"PRI_GRP\".\"NME\" and \"PRI_DTL\".\"VAL_TO\" is not null and \"PRI_DTL\".\"VAL_FR\" is not null");
        queryDatabeans.add(databean);

        databean = new QueryDatabean();
        databean.setClassName(CalcDicsountDetailsDatabean.class.getName());
        databean.setQueryName("GetBasePrice");
        databean.setQuery("SELECT SHAPE shape, PURITY quality, COLOUR color, WT_MIN caratFrom, WT_MAX caratTo, PRICE, LOAD_DATE loadDate FROM RAPNET");
//postgres query
//        databean.setQuery("SELECT \"SHAPE\" shape, \"PURITY\" quality, \"COLOUR\" color, \"WT_MIN\" caratFrom, \"WT_MAX\" caratTo, \"PRICE\", \"LOAD_DATE\" loadDate FROM \"RAPNET\"");
        queryDatabeans.add(databean);

//        new Runnable() {
//
//            @Override
//            public void run() {
//                WebSocketServer socketServer = new WebsocketServer(new InetSocketAddress(host, port));
//
//                System.out.println("Starting websocket server");
//                socketServer.start();
//
//            }
//        }.run();
        this.start();
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        System.out.println("new connection to " + conn.getRemoteSocketAddress());
        System.out.println("queryDatabeans " + queryDatabeans);
        conn.send(gson.toJson(queryDatabeans.get(0)));
        count = 0;
//        conn.close();
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        System.out.println("closed " + conn.getRemoteSocketAddress() + " with exit code " + code + " additional info: " + reason);
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        System.out.println("received message from " + conn.getRemoteSocketAddress() + ": " + message);
        ResponseDataBean response = gson.fromJson(message, ResponseDataBean.class);
        if (!StringUtils.isEmpty(response.getQueryName())) {
            try {
                System.out.println("Method: " + response.getQueryName());
                Method method = this.getClass().getMethod("eval" + response.getQueryName(), String.class);
                method.setAccessible(true);
                method.invoke(this, response.getData());
                conn.send("text");
            } catch (NoSuchMethodException ex) {
                Logger.getLogger(WebsocketServer.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SecurityException ex) {
                Logger.getLogger(WebsocketServer.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                Logger.getLogger(WebsocketServer.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalArgumentException ex) {
                Logger.getLogger(WebsocketServer.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InvocationTargetException ex) {
                Logger.getLogger(WebsocketServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {

        }
        if (response.getIsLastChunk()) {
            System.out.println("count " + count + " queryDatabeans.size() " + queryDatabeans.size() + " " + queryDatabeans.get(count));
            if (count <= queryDatabeans.size()) {
                conn.send(gson.toJson(queryDatabeans.get(count++)));
            } else {
                QueryDatabean databean = new QueryDatabean();
                databean.setQueryName(CLOSE_CONNECTION);
                count = 0;
            }
        }
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        System.err.println("an error occured on connection ");
        ex.printStackTrace();
    }

    public void evalApplicableFromDate(String data) {
        System.out.println("ApplicableFromDate\ndata : " + data);
        applicableFromDate = new Date(Long.parseLong(data));
        System.out.println("applicabledate: " + applicableFromDate);
    }

    public void evalGetMasters(String data) {
        System.out.println("");
        System.out.println("GetMasters");
//        System.out.println("data: " + data);
        restController = applicationContext.getBean(CalcRestController.class);
        System.out.println("restcontroller " + restController);
        Type typeOfList = new TypeToken<List<HkCalcMasterDatabean>>() {
        }.getType();
        List<HkCalcMasterDatabean> masterDatabeans = gson.fromJson(data, typeOfList);
        restController.convertAndSaveMasters(masterDatabeans);
    }

    public void evalGetFourCdiscount(String data) {
        Type typeOfList = new TypeToken<List<HkCalcFourCDiscountDatabean>>() {
        }.getType();
        List<HkCalcFourCDiscountDatabean> dicsountDetailsDatabeans = gson.fromJson(data, typeOfList);
        System.out.println("dicsountDetailsDatabeans::::" + dicsountDetailsDatabeans);
        restController.convertandSaveRateDetailDocuments(dicsountDetailsDatabeans);
    }

    public void evalGetDiscountDetails(String data) {
        System.out.println("In discount details");
        Type typeOfList = new TypeToken<List<CalcDicsountDetailsDatabean>>() {
        }.getType();
        List<CalcDicsountDetailsDatabean> dicsountDetailsDatabeans = gson.fromJson(data, typeOfList);
        System.out.println("Converted....." + dicsountDetailsDatabeans);
        restController.convertandSaveCalcRateDetailDocuments(dicsountDetailsDatabeans);
    }

    public void evalGetBasePrice(String data) {
        System.out.println("GetBasePrice ");
        final JsonSerializer<Date> dateSerializer = (Date src, Type typeOfSrc, JsonSerializationContext context) -> src == null ? null : new JsonPrimitive(src.getTime());
        final JsonDeserializer<Date> dateDeserializer = (JsonElement json, Type typeOfT, JsonDeserializationContext context) -> {
            if (json != null) {
                try {
                    return new Date(json.getAsLong());
                } catch (NumberFormatException numberFormatException) {
                    SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
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

        Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, dateSerializer)
                .registerTypeAdapter(Date.class, dateDeserializer).create();
        Type typeOfList = new TypeToken<List<HkCalcBasPriceDatabean>>() {
        }.getType();
        List<HkCalcBasPriceDatabean> basPriceDocuments = gson.fromJson(data, typeOfList);
        restController.saveBasePriceDocument(basPriceDocuments, HkCalcBasPriceDocument.class);
    }
}
