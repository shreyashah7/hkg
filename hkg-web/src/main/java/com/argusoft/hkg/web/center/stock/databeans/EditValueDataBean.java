package com.argusoft.hkg.web.center.stock.databeans;


import com.argusoft.hkg.web.util.SelectItem;
import java.util.List;

/**
 *
 * @author rajkumar
 */
public class EditValueDataBean {

    private List<SelectItem> stockList;

    private String stockId;
    private String stockType;
    private String planId;
    private String planNumber;
    private Object price;
    private Object previousPrice;
    private String color;
    private Long colorId;
    private String clarity;
    private Long clarityId;
    private String cut;
    private Long cutId;
    private String carat;
    private Long caratId;
    private String fluroscene;
    private Long flurosceneId;

    public List<SelectItem> getStockList() {
        return stockList;
    }

    public void setStockList(List<SelectItem> stockList) {
        this.stockList = stockList;
    }

    public String getPlanId() {
        return planId;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
    }

    public String getPlanNumber() {
        return planNumber;
    }

    public void setPlanNumber(String planNumber) {
        this.planNumber = planNumber;
    }

    public Object getPrice() {
        return price;
    }

    public void setPrice(Object price) {
        this.price = price;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getClarity() {
        return clarity;
    }

    public void setClarity(String clarity) {
        this.clarity = clarity;
    }

    public String getCut() {
        return cut;
    }

    public void setCut(String cut) {
        this.cut = cut;
    }

    public String getCarat() {
        return carat;
    }

    public void setCarat(String carat) {
        this.carat = carat;
    }

    public String getFluroscene() {
        return fluroscene;
    }

    public void setFluroscene(String fluroscene) {
        this.fluroscene = fluroscene;
    }

    public Object getPreviousPrice() {
        return previousPrice;
    }

    public void setPreviousPrice(Object previousPrice) {
        this.previousPrice = previousPrice;
    }

    public String getStockId() {
        return stockId;
    }

    public void setStockId(String stockId) {
        this.stockId = stockId;
    }

    public String getStockType() {
        return stockType;
    }

    public void setStockType(String stockType) {
        this.stockType = stockType;
    }

    public Long getColorId() {
        return colorId;
    }

    public void setColorId(Long colorId) {
        this.colorId = colorId;
    }

    public Long getClarityId() {
        return clarityId;
    }

    public void setClarityId(Long clarityId) {
        this.clarityId = clarityId;
    }

    public Long getCutId() {
        return cutId;
    }

    public void setCutId(Long cutId) {
        this.cutId = cutId;
    }

    public Long getCaratId() {
        return caratId;
    }

    public void setCaratId(Long caratId) {
        this.caratId = caratId;
    }

    public Long getFlurosceneId() {
        return flurosceneId;
    }

    public void setFlurosceneId(Long flurosceneId) {
        this.flurosceneId = flurosceneId;
    }

    @Override
    public String toString() {
        return "EditValueDataBean{" + "stockList=" + stockList + ", stockId=" + stockId + ", stockType=" + stockType + ", planId=" + planId + ", planNumber=" + planNumber + ", price=" + price + ", previousPrice=" + previousPrice + ", color=" + color + ", colorId=" + colorId + ", clarity=" + clarity + ", clarityId=" + clarityId + ", cut=" + cut + ", cutId=" + cutId + ", carat=" + carat + ", caratId=" + caratId + ", fluroscene=" + fluroscene + ", flurosceneId=" + flurosceneId + '}';
    }



}
