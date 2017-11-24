package com.argusoft.hkg.web.common.databeans;

import java.util.List;
import java.util.Map;

/**
 *
 * @author kvithlani
 */
public class DatatableDataBean<T> {

    private Integer draw;
    private List<Map> columns;
    private List<OrderDataBean> order;
    private Integer start;
    private Integer length;
    private SearchDataBean search;
    private T customParameters;

    public T getCustomParameters() {
        return customParameters;
    }

    public void setCustomParameters(T customParameters) {
        this.customParameters = customParameters;
    }

    public Integer getDraw() {
        return draw;
    }

    public void setDraw(Integer draw) {
        this.draw = draw;
    }

    public List<Map> getColumns() {
        return columns;
    }

    public void setColumns(List<Map> columns) {
        this.columns = columns;
    }


    public List<OrderDataBean> getOrder() {
        return order;
    }

    public void setOrder(List<OrderDataBean> order) {
        this.order = order;
    }

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public SearchDataBean getSearch() {
        return search;
    }

    public void setSearch(SearchDataBean search) {
        this.search = search;
    }

    public static class OrderDataBean {

        private Integer column;
        private String dir;

        public Integer getColumn() {
            return column;
        }

        public void setColumn(Integer column) {
            this.column = column;
        }

        public String getDir() {
            return dir;
        }

        public void setDir(String dir) {
            this.dir = dir;
        }

        @Override
        public String toString() {
            return "OrderDataBean{" + "column=" + column + ", dir=" + dir + '}';
        }

    }
    public static class SearchDataBean {

        private String value;
        private boolean regex;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public boolean isRegex() {
            return regex;
        }

        public void setRegex(boolean regex) {
            this.regex = regex;
        }

        @Override
        public String toString() {
            return "SearchDataBean{" + "value=" + value + ", regex=" + regex + '}';
        }
        
    }

    @Override
    public String toString() {
        return "DatatableDataBean{" + "draw=" + draw + ", columns=" + columns + ", order=" + order + ", start=" + start + ", length=" + length + ", search=" + search + ", customParameters=" + customParameters + '}';
    }
    
}
