/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.rule;

import com.argusoft.hkg.nosql.model.HkRuleCriteriaDocument;
import com.argusoft.hkg.nosql.model.HkRuleDocument;
import com.argusoft.hkg.nosql.model.HkRuleSetDocument;
import com.google.gson.Gson;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.rmi.RemoteException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.rules.InvalidRuleSessionException;
import javax.rules.RuleExecutionSetNotFoundException;
import javax.rules.RuleRuntime;
import javax.rules.RuleServiceProvider;
import javax.rules.RuleServiceProviderManager;
import javax.rules.RuleSessionCreateException;
import javax.rules.RuleSessionTypeUnsupportedException;
import javax.rules.StatelessRuleSession;
import javax.rules.admin.RuleAdministrator;
import javax.rules.admin.RuleExecutionSet;
import javax.rules.admin.RuleExecutionSetCreateException;
import javax.rules.admin.RuleExecutionSetRegisterException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import org.hibernate.internal.util.config.ConfigurationException;

/**
 *
 * @author mansi
 */
public class RuleLogic {

    private static HkRuleSetDocument hkRuleSetDocument;
    private static final String RULE_SERVICE_PROVIDER = "org.jcp.jsr94.jess";
    String PATH = System.getProperty("user.home");

    public void run(String ruleJson, Map<Long, String> entityIdMap, Map<Long, List<Map<String, Object>>> entityFieldMap, Map<String, Object> data) throws RemoteException, javax.rules.ConfigurationException {
        try {
            System.out.println("=============================prepare data");
            prepareData(ruleJson, entityIdMap, entityFieldMap, data);
            System.out.println("=============================write data");
            writeToFile();
            List<Object> input = new ArrayList<>();
            DynamicClass inputclass = new DynamicClass("test", data);
            input.add(inputclass);
            System.out.println("=============================execute rule");
            executeRule(input);
        } catch (RuleExecutionSetCreateException ex) {
            Logger.getLogger(RuleLogic.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(RuleLogic.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RuleExecutionSetRegisterException ex) {
            Logger.getLogger(RuleLogic.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RuleSessionTypeUnsupportedException ex) {
            Logger.getLogger(RuleLogic.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RuleSessionCreateException ex) {
            Logger.getLogger(RuleLogic.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RuleExecutionSetNotFoundException ex) {
            Logger.getLogger(RuleLogic.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidRuleSessionException ex) {
            Logger.getLogger(RuleLogic.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(RuleLogic.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchFieldException ex) {
            Logger.getLogger(RuleLogic.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ConfigurationException ex) {
            Logger.getLogger(RuleLogic.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<DynamicField.Pair> convert(List<Map<String, Object>> list) {
        List<DynamicField.Pair> pairs = new ArrayList<>();
        for (Map<String, Object> map : list) {
            DynamicField.Pair pair = new DynamicField.Pair();
            pair.setId((Long) map.get("id"));
            pair.setLabel((String) map.get("fieldLabel"));
            pair.setType((String) map.get("componentType"));
            pair.setValues((List<Object>) map.get("fieldValues"));
            pairs.add(pair);
        }
        return pairs;
    }

    public void prepareData(String json, Map<Long, String> entityMap, Map<Long, List<Map<String, Object>>> entityFieldMap, Map<String, Object> data) {
        Gson gson = new Gson();
        hkRuleSetDocument = gson.fromJson(json, HkRuleSetDocument.class);
//        for (Map.Entry<Long, String> entry : entityMap.entrySet()) {
//            List<Map<String, Object>> get = entityFieldMap.get(entry.getKey());
//            if (get != null) {
//                DynamicField fromJson = new DynamicField();
//                List<DynamicField.Pair> convert = convert(get);
//                fromJson.setList(convert);
//                fromJson.setEntityName(entry.getValue());
//                fromJson.setId(entry.getKey());
//                fromJson.setMap(null);
//                fromJson.convert();
//                List<HkRuleCriteriaDocument> rules = ruleClass.getRules();
//                for (int i = 0; i < rules.size(); i++) {
//                    HkRuleCriteriaDocument rule = rules.get(i);
//                    if (rule.getEntity().equals(entry.getKey())) {
////                    rules.get(i).setType(fromJson.getMap().get(rule.getField()));
//                    }
//                }
//            }
//
//        }

    }

    public static String convertObjToJess() throws ClassNotFoundException, NoSuchFieldException {
        String conditionalOp = null;
        StringBuilder builder = new StringBuilder("(defclass com.argusoft.hkg.rule.DynamicClass com.argusoft.hkg.rule.DynamicClass) \n(defclass model.DateClass model.DateClass) \n");
        builder.append("(defclass com.argusoft.hkg.rule.RuleLogic com.argusoft.hkg.rule.RuleLogic)");
        builder.append("(defglobal ?*flag* = FALSE)");

        for (HkRuleDocument ruleClass : hkRuleSetDocument.getRules()) {
            builder.append("(bind ?*flag* ");
            if (ruleClass.getApply().equals("all")) {
                conditionalOp = "and";
                builder.append("TRUE");
            } else if (ruleClass.getApply().equals("any")) {
                conditionalOp = "or";
                builder.append("FALSE");
            } else {
                System.out.println("Invalid conditional operator.");
                System.exit(1);
            }
            builder.append(")\n" + " (defrule ").append(ruleClass.getRuleName()).append(" ?model-someclass <- (com.argusoft.hkg.rule.DynamicClass ( map ?map) )\n"
                    + "        =>\n"
                    + "(bind ?iterator ((?map keySet) iterator))\n"
                    + "        (while (?iterator hasNext) \n"
                    + "          \n"
                    + "        (bind ?key (?iterator next))\n"
                    + "        (bind ?value (?map get ?key))"
            );

            List<HkRuleCriteriaDocument> rules = ruleClass.getCriterias();
            for (int i = 0; i < rules.size(); i++) {
                HkRuleCriteriaDocument rule = rules.get(i);
//            isValid(rule);
                builder.append("(if  (eq ?key \"" + rule.getField()
                        + "\") then");
                builder.append("  (if (" + rule.getOperator() + " ?value ");
                if (rule.getFieldType().equalsIgnoreCase("String")) {
                    builder.append("  (if (" + rule.getOperator() + " ?value \"" + rule.getValue() + "\" )");
                } else if (rule.getFieldType().equalsIgnoreCase("Date") || rule.getFieldType().equalsIgnoreCase("Date range")) {
                    builder.append("(bind ?com-argusoft-hkg-rule-ruleLogic  (new com.argusoft.hkg.rule.RuleLogic))(bind ?customDateFirstInstance (?com-argusoft-hkg-rule-ruleLogic parseDate \"" + rule.getValue() + "\"))(bind ?customDateValue (?com-argusoft-hkg-rule-ruleLogic parseDate ?value))");
                    builder.append("  (if (" + rule.getOperator() + " ?customDateValue ?customDateFirstInstance)");
                } else {
                    builder.append("  (if (" + rule.getOperator() + " ?value " + rule.getValue() + ")");
                }
                builder.append(" then \n (bind ?*flag* (" + conditionalOp + " ?*flag* TRUE) ) \n"
                        + "        else \n"
                        + "        (bind ?*flag* (" + conditionalOp + " ?*flag* FALSE))\n"
                        + "        ) \n"
                        + "        )");
                builder.append("        )"
                        + "        (printout t   ?*flag* crlf) "
                        + "        )");

            }

        }

        return builder.toString();
    }

    public void writeToFile() throws ClassNotFoundException, NoSuchFieldException {
        RuleSet ruleSet = new RuleSet("RuleExecutionSet1", "Stateless RuleExecutionSet", convertObjToJess());
        File file = new File(PATH + "/file.xml");
        JAXBContext jaxbContext = null;

        try {
            jaxbContext = JAXBContext.newInstance(RuleSet.class
            );
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,
                    true);
            jaxbMarshaller.marshal(ruleSet, file);
        } catch (JAXBException e) {
            e.printStackTrace(System.out);
        }
    }

    public void executeRule(List<Object> data) throws ClassNotFoundException, ConfigurationException, RemoteException, RuleExecutionSetCreateException, IOException, RuleExecutionSetRegisterException, RuleSessionTypeUnsupportedException, RuleSessionCreateException, RuleExecutionSetNotFoundException, InvalidRuleSessionException, javax.rules.ConfigurationException {
        Class.forName("org.jcp.jsr94.jess.RuleServiceProviderImpl");

        // Get the rule service provider from the provider manager.
        RuleServiceProvider serviceProvider = RuleServiceProviderManager.getRuleServiceProvider(RULE_SERVICE_PROVIDER);

        // get the RuleAdministrator
        RuleAdministrator ruleAdministrator = serviceProvider.getRuleAdministrator();

//            // get an input stream to a test XML ruleset
//            // This rule execution set is part of the TCK.
        InputStream inStream = new FileInputStream(PATH + "/file.xml");
//        InputStream inStream = this.getClass().getClassLoader().getResourceAsStream("/file.xml");
        // parse the ruleset from the XML document

        RuleExecutionSet res1 = ruleAdministrator.getLocalRuleExecutionSetProvider(null).createRuleExecutionSet(inStream, null);

        inStream.close();
        // register the RuleExecutionSet
        String uri = res1.getName();
        ruleAdministrator.registerRuleExecutionSet(uri, res1, null);

        // Get a RuleRuntime and invoke the rule engine.
        RuleRuntime ruleRuntime = serviceProvider.getRuleRuntime();

        // create a StatelessRuleSession
        StatelessRuleSession statelessRuleSession
                = (StatelessRuleSession) ruleRuntime.createRuleSession(uri,
                        new HashMap(), RuleRuntime.STATELESS_SESSION_TYPE);

        // call executeRules with some input objects
        // Print the input.
        // Execute the rules without a filter.
        statelessRuleSession.executeRules(data);

        // Release the session.
        statelessRuleSession.release();
//            System.out.println("Released Stateless Rule Session.");
        System.out.println();
    }

    public Date parseDate(String date) {
        try {
            Date dt = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(date);
            System.out.println("" + dt);
            return dt;
        } catch (ParseException ex) {
            ex.printStackTrace(System.out);
        }
        return null;
    }
}
