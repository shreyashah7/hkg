/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.reportbuilder.transformers;

import com.argusoft.hkg.common.constantutil.HkSystemConstantUtil;
import java.awt.Color;
import java.net.URL;
import java.util.Locale;
import java.util.Date;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import net.sf.dynamicreports.report.base.expression.AbstractValueFormatter;
import static net.sf.dynamicreports.report.builder.DynamicReports.*;
import net.sf.dynamicreports.report.builder.ReportTemplateBuilder;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.builder.component.HorizontalListBuilder;
import net.sf.dynamicreports.report.builder.component.TextFieldBuilder;
import net.sf.dynamicreports.report.builder.datatype.BigDecimalType;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.builder.tableofcontents.TableOfContentsCustomizerBuilder;
import net.sf.dynamicreports.report.constant.HorizontalAlignment;
import net.sf.dynamicreports.report.constant.VerticalAlignment;
import net.sf.dynamicreports.report.definition.ReportParameters;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 * General Templates for various reports
 *
 * @author vipul
 */
public class ReportBuilderTemplate {

    public static final ClassLoader cLoader = ReportBuilderTemplate.class.getClassLoader();

    public static final StyleBuilder rootStyle;
    public static final StyleBuilder boldStyle;
    public static final StyleBuilder italicStyle;
    public static final StyleBuilder boldCenteredStyle;
    public static final StyleBuilder bold8CenteredStyle;
    public static final StyleBuilder bold18CenteredUnderlinedStyle;
    public static final StyleBuilder bold12CenteredStyle;
    public static final StyleBuilder bold12CenteredStyleForSubTitle;
    public static final StyleBuilder bold22CenteredStyle;
    public static final StyleBuilder columnStyle;
    public static final StyleBuilder columnTitleStyle;
    public static final StyleBuilder groupStyle;
    public static final StyleBuilder subtotalStyle;
    public static final ReportTemplateBuilder reportTemplate;
    public static final ComponentBuilder<?, ?> dynamicReportsComponent;
//    public static final ComponentBuilder<?, ?> footerComponent;
    public static final StyleBuilder labelStyle;
    public static final StyleBuilder contentStyle;
    public static final StyleBuilder contentRightAlignStyle;
    public static final StyleBuilder contentRightAlignStyleWithNoBorder;
    public static final StyleBuilder labelRightAlignStyle;
    public static final StyleBuilder labelStyleWithNoBorder;
    public static final StyleBuilder contentStyleWithNoBorder;
    public static final StyleBuilder staticColumnTitleStyle;

    static {
        rootStyle = stl.style().setPadding(2);

        boldStyle = stl.style(rootStyle).bold();

        italicStyle = stl.style(rootStyle).italic();

        boldCenteredStyle = stl.style(boldStyle)
                .setAlignment(HorizontalAlignment.CENTER, VerticalAlignment.TOP);

        bold8CenteredStyle = stl.style(boldCenteredStyle)
                .setFontSize(8);

        bold12CenteredStyleForSubTitle = stl.style(boldCenteredStyle)
                .setFontSize(12).setBackgroundColor(Color.LIGHT_GRAY);

        bold12CenteredStyle = stl.style(boldCenteredStyle)
                .setFontSize(12);

        bold22CenteredStyle = stl.style(boldCenteredStyle)
                .setFontSize(22);

        bold18CenteredUnderlinedStyle = stl.style(boldCenteredStyle)
                .setFontSize(18).setUnderline(Boolean.TRUE);

        columnStyle = stl.style(rootStyle).setVerticalAlignment(VerticalAlignment.TOP).setBorder(stl.penThin()).setFontSize(8);
        columnTitleStyle = stl.style(rootStyle).setVerticalAlignment(VerticalAlignment.MIDDLE)
                .setBorder(stl.penThin()).setFontSize(8)
                .setHorizontalAlignment(HorizontalAlignment.CENTER)
                .setBackgroundColor(new Color(177, 154, 204))
                .bold();

        groupStyle = stl.style(boldStyle)
                .setHorizontalAlignment(HorizontalAlignment.LEFT).setBorder(stl.penThin()).setFontSize(8);

        subtotalStyle = stl.style(boldStyle)
                .setTopBorder(stl.pen1Point());

        labelStyleWithNoBorder = stl.style()
                .setHorizontalAlignment(HorizontalAlignment.LEFT).setVerticalAlignment(VerticalAlignment.TOP)
                .setPadding(2).bold().setForegroundColor(Color.DARK_GRAY).setFontSize(10);

        contentStyleWithNoBorder = stl.style()
                .setHorizontalAlignment(HorizontalAlignment.LEFT).setVerticalAlignment(VerticalAlignment.TOP)
                .setPadding(2).setForegroundColor(Color.DARK_GRAY).setFontSize(8);

        labelStyle = stl.style()
                .setHorizontalAlignment(HorizontalAlignment.LEFT).setVerticalAlignment(VerticalAlignment.TOP)
                .setBorder(stl.penThin().setLineColor(Color.BLACK)).setPadding(2).bold()
                .setForegroundColor(Color.DARK_GRAY).setFontSize(8);
        contentStyle = stl.style()
                .setHorizontalAlignment(HorizontalAlignment.LEFT).setVerticalAlignment(VerticalAlignment.TOP)
                .setBorder(stl.penThin()).setPadding(2).setFontSize(8);

        contentRightAlignStyle = stl.style()
                .setHorizontalAlignment(HorizontalAlignment.RIGHT).setVerticalAlignment(VerticalAlignment.TOP)
                .setBorder(stl.penThin()).setPadding(2).setFontSize(8);

        contentRightAlignStyleWithNoBorder = stl.style()
                .setHorizontalAlignment(HorizontalAlignment.RIGHT).setVerticalAlignment(VerticalAlignment.TOP)
                .setPadding(2).setFontSize(8);

        labelRightAlignStyle = stl.style()
                .setHorizontalAlignment(HorizontalAlignment.RIGHT).setVerticalAlignment(VerticalAlignment.TOP)
                .setBorder(stl.penThin()).setPadding(2).bold().setFontSize(8);

        staticColumnTitleStyle = stl.style().setPadding(2)
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .setBorder(stl.penThin()).setFontSize(8)
                .setHorizontalAlignment(HorizontalAlignment.CENTER)
                .setBackgroundColor(Color.LIGHT_GRAY)
                .bold();

        StyleBuilder crosstabGroupStyle = stl.style(columnTitleStyle);
        StyleBuilder crosstabGroupTotalStyle = stl.style(columnTitleStyle)
                .setBackgroundColor(new Color(170, 170, 170));
        StyleBuilder crosstabGrandTotalStyle = stl.style(columnTitleStyle)
                .setBackgroundColor(new Color(140, 140, 140));
        StyleBuilder crosstabCellStyle = stl.style(columnStyle)
                .setBorder(stl.pen1Point());
        TableOfContentsCustomizerBuilder tableOfContentsCustomizer = tableOfContentsCustomizer()
                .setHeadingStyle(0, stl.style(rootStyle).bold());
        reportTemplate = template()
                .setLocale(Locale.ENGLISH)
                .setColumnStyle(columnStyle)
                //                .setColumnTitleStyle(columnTitleStyle)
                .setGroupStyle(groupStyle)
                .setGroupTitleStyle(groupStyle)
                .setSubtotalStyle(subtotalStyle)
                //                .highlightDetailEvenRows()
                .setCrosstabGroupStyle(crosstabGroupStyle)
                .setCrosstabGroupTotalStyle(crosstabGroupTotalStyle)
                .setCrosstabGrandTotalStyle(crosstabGrandTotalStyle)
                .setCrosstabCellStyle(crosstabCellStyle)
                .setTableOfContentsCustomizer(tableOfContentsCustomizer);
//        currencyType = new CurrencyType();
        dynamicReportsComponent = cmp.horizontalList(cmp.verticalList());
//        footerComponent = cmp.horizontalList().add(Templates.content(new Date()+"", 300)).add(cmp.pageXslashY()
//                .setStyle(contentRightAlignStyleWithNoBorder));
    }

    public static TextFieldBuilder<String> content(String text, int size) {
        return label(text, size, contentStyle);
    }

    public static TextFieldBuilder<String> label(String text, int size) {
        return label(text, size, labelStyle);
    }

    public static TextFieldBuilder<String> labelWithNoBorder(String text, int size) {
        return label(text, size, labelStyleWithNoBorder);
    }

    public static TextFieldBuilder<String> labelRightAlign(String text, int size) {
        return label(text, size, labelRightAlignStyle);
    }

    public static TextFieldBuilder<String> contentWithNoBorder(String text, int size) {
        return label(text, size, contentStyleWithNoBorder);
    }

    public static TextFieldBuilder<String> contentRightAlignWithNoBorder(String text, int size) {
        return label(text, size, contentRightAlignStyleWithNoBorder);
    }

    public static TextFieldBuilder<String> contentRightAlign(String text, int size) {
        return label(text, size, contentRightAlignStyle);
    }

    public static TextFieldBuilder<String> columnTitle(String text, int size) {
        return label(text, size, staticColumnTitleStyle);
    }

    public static TextFieldBuilder<String> label(String text, int size, StyleBuilder style) {
        if (text == null || text.equals("null")) {
            text = "BLANK";
        }
//        else{
//            text = text;
//        }
        TextFieldBuilder<String> label = cmp.text(text)
                .setFixedWidth(size);
        if (style != null) {
            label.setStyle(style);
        }
        return label;
    }

    /**
     * Creates custom component which is possible to add to any report footer
     * component
     */
    public static ComponentBuilder<?, ?> createFooterComponent() {
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
        String date = df.format(new Date());
        return cmp.horizontalList().add(cmp.line().setPen(stl.pen1Point())).newRow().add(cmp.text("Generated on " + date).setFixedWidth(300).setStyle(stl.style()
                .setHorizontalAlignment(HorizontalAlignment.LEFT).setVerticalAlignment(VerticalAlignment.MIDDLE)
                .setPadding(2).setFontSize(8)))
                //                .add(cmp.text("Page -").setStyle(contentRightAlignStyleWithNoBorder.setPadding(0).setFontSize(10).setVerticalAlignment(VerticalAlignment.MIDDLE)))
                .add(cmp.pageXslashY().setStyle(contentRightAlignStyleWithNoBorder.setVerticalAlignment(VerticalAlignment.MIDDLE).setPadding(0).setFontSize(10)));
    }

    public static ComponentBuilder<?, ?> createHeaderComponent(List<Map<String, Object>> filters, List<Map<String, Object>> resultMap, List<String> hiddenFields) {
        HorizontalListBuilder horizontalListBuilder = cmp.horizontalList().
                add(cmp.text("Filters for the report").setStyle(boldStyle)).newRow();
        String firstValue = null;
        String secondValue = null;
        if (!CollectionUtils.isEmpty(filters)) {
            for (int i = 0; i < filters.size(); i++) {
                if (!CollectionUtils.isEmpty(filters.get(i))) {
                    //System.out.println("inisde if---");
                    String fieldLabel = filters.get(i).get("label").toString();
                    String operator = filters.get(i).get("operator").toString();
                    if (filters.get(i).containsKey("filterValue")) {
                        firstValue = filters.get(i).get("filterValue").toString();
                    }
                    if (filters.get(i).containsKey("filterValueSecond")) {
                        secondValue = filters.get(i).get("filterValueSecond").toString();
                    }
                    if (operator.equalsIgnoreCase("between")) {
                        String value = fieldLabel + " " + HkSystemConstantUtil.operatorSymbolMap.get(operator) + " " + firstValue + " " + "and" + " " + secondValue;
                        horizontalListBuilder.add(cmp.text(value).setFixedWidth(287).setStyle(stl.style().setHorizontalAlignment(HorizontalAlignment.LEFT).setVerticalAlignment(VerticalAlignment.TOP))).newRow();
                    } else {
                        //System.out.println("inside loop");
                        String value = fieldLabel + " " + HkSystemConstantUtil.operatorSymbolMap.get(operator) + " " + firstValue;
                        horizontalListBuilder.add(cmp.text(value).setFixedWidth(287).setStyle(stl.style().setHorizontalAlignment(HorizontalAlignment.LEFT).setVerticalAlignment(VerticalAlignment.TOP))).newRow();
                    }

                }
                if ((i + 1) < filters.size()) {
                    //System.out.println("inside i + 1");
                    if (!CollectionUtils.isEmpty(filters.get(i + 1))) {
                        String fieldLabel = filters.get(i + 1).get("label").toString();
                        String operator = filters.get(i + 1).get("operator").toString();
                        if (filters.get(i + 1).containsKey("filterValue")) {
                            firstValue = filters.get(i + 1).get("filterValue").toString();
                        }
                        if (filters.get(i + 1).containsKey("filterValueSecond")) {
                            secondValue = filters.get(i + 1).get("filterValueSecond").toString();
                        }
                        if (operator.equalsIgnoreCase("between")) {
                            String value = fieldLabel + " " + HkSystemConstantUtil.operatorSymbolMap.get(operator) + " " + firstValue + " " + "and" + " " + secondValue;
                            horizontalListBuilder.add(cmp.text(value).setFixedWidth(287).setStyle(stl.style().setHorizontalAlignment(HorizontalAlignment.LEFT).setVerticalAlignment(VerticalAlignment.TOP))).newRow();
                        } else {
                            //System.out.println("inside loop");
                            String value = fieldLabel + " " + HkSystemConstantUtil.operatorSymbolMap.get(operator) + " " + firstValue;
                            horizontalListBuilder.add(cmp.text(value).setFixedWidth(287).setStyle(stl.style().setHorizontalAlignment(HorizontalAlignment.LEFT).setVerticalAlignment(VerticalAlignment.TOP))).newRow();
                        }
                    }
                    i++;
                }
                //System.out.println("new row added");
                horizontalListBuilder.newRow();
            }
        }

        if (!CollectionUtils.isEmpty(resultMap) && !CollectionUtils.isEmpty(hiddenFields)) {
            horizontalListBuilder.add(cmp.text("Hidden Fields for the report").setStyle(boldStyle)).newRow();
            for (String hiddenField : hiddenFields) {
                horizontalListBuilder.add(cmp.text(hiddenField + ":").setFixedWidth(80).setStyle(stl.style().setHorizontalAlignment(HorizontalAlignment.LEFT).setVerticalAlignment(VerticalAlignment.TOP)));
                List<String> data = new LinkedList<>();
                for (Map<String, Object> resultMap1 : resultMap) {
                    if (!CollectionUtils.isEmpty(resultMap1) && resultMap1.containsKey(hiddenField) && resultMap1.get(hiddenField) != null) {
                        if (!StringUtils.isEmpty(resultMap1.get(hiddenField).toString())) {
                            data.add(resultMap1.get(hiddenField).toString());
                        }
                    }
                }
                int index = 0;
                String dataToBePLaced = "";
                for (String data1 : data) {
                    if (!StringUtils.isEmpty(data1)) {
                        if (index == 0) {
                            dataToBePLaced = data1;
                        } else {
                            dataToBePLaced = dataToBePLaced + "," + data1;
                        }
                    }
                    index++;
                }
                horizontalListBuilder.add(cmp.text(dataToBePLaced).setFixedWidth(495).setStyle(stl.style().setHorizontalAlignment(HorizontalAlignment.LEFT).setVerticalAlignment(VerticalAlignment.TOP))).newRow();
            }
        }
        return horizontalListBuilder.add(cmp.line().setPen(stl.pen1Point())).newRow();
    }

    public static ComponentBuilder<?, ?> createHeaderComponent(Map<String, String> headers) {
        HorizontalListBuilder horizontalListBuilder = cmp.horizontalList();
        int newRow = 0;
        for (Map.Entry<String, String> entrySet : headers.entrySet()) {
            newRow++;
            String key = entrySet.getKey();
            String value = entrySet.getValue();
            String dataToBePlaced = key + " " + ":" + " " + value;
            if (newRow % 2 == 0) {
                horizontalListBuilder.add(cmp.text(dataToBePlaced).setFixedWidth(287).setStyle(stl.style().setHorizontalAlignment(HorizontalAlignment.LEFT).setVerticalAlignment(VerticalAlignment.TOP))).newRow();
            } else {
                horizontalListBuilder.add(cmp.text(dataToBePlaced).setFixedWidth(287).setStyle(stl.style().setHorizontalAlignment(HorizontalAlignment.LEFT).setVerticalAlignment(VerticalAlignment.TOP)));
            }
        }
        return horizontalListBuilder.add(cmp.line().setPen(stl.pen1Point())).newRow();
    }

    /**
     * Creates custom component which is possible to add to any report band
     * component
     */
//    public static ComponentBuilder<?, ?> createTitleComponent(String label) {
////        label = CommonUtilBean.getLabelMessageForReport(label);
//        return cmp.horizontalList()
//                .add(cmp.text(label).setStyle(bold12CenteredStyle).setHorizontalAlignment(HorizontalAlignment.CENTER))
//                .newRow()
//                .add(cmp.line())
//                .newRow()
//                .add(cmp.verticalGap(10));
//    }
    public static ComponentBuilder<?, ?> createTitleComponent(String barcodeURL, String label, HttpServletRequest request, String align, Boolean isLogoRequired) {

        URL resource = cLoader.getResource("hk-logo.png");
        HorizontalListBuilder horizontalListBuilder = cmp.horizontalList();
        if (isLogoRequired) {
            if (request != null) {
                horizontalListBuilder
                        .add(cmp.image(request.getServletContext().getRealPath("/") + "/images/hk-logo.png"));
            } else {
                horizontalListBuilder
                        .add(cmp.image(resource.getPath()));
            }
        }
        if (!StringUtils.isEmpty(barcodeURL)) {
            horizontalListBuilder
                    .add(cmp.image(barcodeURL).setHorizontalAlignment(HorizontalAlignment.RIGHT));
        }
        if (!StringUtils.isEmpty(label)) {
            if (!StringUtils.isEmpty(align)) {
                if (align.equals("RIGHT")) {
                    horizontalListBuilder
                            .add(cmp.text(label).setStyle(bold12CenteredStyle).setHorizontalAlignment(HorizontalAlignment.RIGHT));
                } else if (align.equals("CENTER")) {
                    horizontalListBuilder
                            .add(cmp.text(label).setStyle(bold12CenteredStyle).setHorizontalAlignment(HorizontalAlignment.CENTER));
                } else if (align.equals("LEFT")) {
                    horizontalListBuilder
                            .add(cmp.text(label).setStyle(bold12CenteredStyle).setHorizontalAlignment(HorizontalAlignment.LEFT));
                }
            } else {
                horizontalListBuilder
                        .add(cmp.text(label).setStyle(bold12CenteredStyle).setHorizontalAlignment(HorizontalAlignment.RIGHT));
            }

        }
        horizontalListBuilder.newRow()
                .add(cmp.line())
                .newRow()
                .add(cmp.verticalGap(10));
        return horizontalListBuilder;
    }

    /**
     * Creates custom component which is possible to add to any report band
     * component
     */
    public static ComponentBuilder<?, ?> createFormTitleComponent(String label) {
//        label = CommonUtilBean.getLabelMessageForReport(label);
        return cmp.horizontalList()
                .add(cmp.text(label).setStyle(bold18CenteredUnderlinedStyle).setHorizontalAlignment(HorizontalAlignment.CENTER))
                .newRow()
                .add(cmp.verticalGap(10));
    }

    public static ComponentBuilder<?, ?> createSubTitleComponent(String label) {
//        label = CommonUtilBean.getLabelMessageForReport(label);
        return cmp.horizontalList()
                .newRow(15)
                .add(cmp.text(label).setStyle(bold12CenteredStyleForSubTitle).setHorizontalAlignment(HorizontalAlignment.CENTER))
                .newRow()
                .add(cmp.verticalGap(10));
    }

    public static CurrencyValueFormatter createCurrencyValueFormatter(String label, String pattern) {
        return new CurrencyValueFormatter(label, pattern);

    }
    public static StringCurrencyValueFormatter createStringCurrencyValueFormatter(String label, String pattern) {
        return new StringCurrencyValueFormatter(label, pattern);

    }

    public static StringValueFormatter createStringValueFormatter(String label) {
        return new StringValueFormatter(label);

    }
    public static ValueFormatter createValueFormatter(String label) {
        return new ValueFormatter(label);

    }

    public static class CurrencyType extends BigDecimalType {

        private String pattern;

        public CurrencyType(String pattern) {
            this.pattern = pattern;
        }

        private static final long serialVersionUID = 1L;

        @Override
        public String getPattern() {
            return pattern;
        }
    }

    private static class CurrencyValueFormatter extends AbstractValueFormatter<String, Number> {

        private static final long serialVersionUID = 1L;
        private String label;
        private String pattern;

        public CurrencyValueFormatter(String label, String pattern) {
            this.label = label;
            this.pattern = pattern;
        }

        @Override
        public String format(Number value, ReportParameters reportParameters) {
            CurrencyType currencyType = new CurrencyType(pattern);
            return currencyType.valueToString(value, reportParameters.getLocale()) + label;
        }
    }
    private static class StringCurrencyValueFormatter extends AbstractValueFormatter<String, String> {

        private static final long serialVersionUID = 1L;
        private String label;
        private String pattern;

        public StringCurrencyValueFormatter(String label, String pattern) {
            this.label = label;
            this.pattern = pattern;
        }

        @Override
        public String format(String value, ReportParameters reportParameters) {
//            CurrencyType currencyType = new CurrencyType(pattern);
            return value + label;
        }
    }

    private static class ValueFormatter extends AbstractValueFormatter<String, Number> {

        private static final long serialVersionUID = 1L;
        private String label;

        public ValueFormatter(String label) {
//            label = CommonUtilBean.getLabelMessageForReport(label);
            this.label = label;
        }

        @Override
        public String format(Number value, ReportParameters reportParameters) {
            return type.doubleType().valueToString(value, reportParameters.getLocale()) + label;
        }
    }
    private static class StringValueFormatter extends AbstractValueFormatter<String, String> {

        private static final long serialVersionUID = 1L;
        private String label;

        public StringValueFormatter(String label) {
//            label = CommonUtilBean.getLabelMessageForReport(label);
            this.label = label;
        }

        @Override
        public String format(String value, ReportParameters reportParameters) {
            return value + label;
        }
    }
}
