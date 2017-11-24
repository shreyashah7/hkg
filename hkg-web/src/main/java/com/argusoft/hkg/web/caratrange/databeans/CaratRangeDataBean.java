package com.argusoft.hkg.web.caratrange.databeans;

/**
 *
 * @author rajkumar
 */
public class CaratRangeDataBean {

    private Long id;
    private Float minValue;
    private Float maxValue;
    private String status;
    private Long franchise;
    private Boolean newadded;
    private Boolean edited;
    private Boolean toDelete;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getMinValue() {
        return minValue;
    }

    public void setMinValue(Float minValue) {
        this.minValue = minValue;
    }

    public Float getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(Float maxValue) {
        this.maxValue = maxValue;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getFranchise() {
        return franchise;
    }

    public void setFranchise(Long franchise) {
        this.franchise = franchise;
    }

    public Boolean getNewadded() {
        return newadded;
    }

    public void setNewadded(Boolean newadded) {
        this.newadded = newadded;
    }

    public Boolean getEdited() {
        return edited;
    }

    public void setEdited(Boolean edited) {
        this.edited = edited;
    }

    public Boolean getToDelete() {
        return toDelete;
    }

    public void setToDelete(Boolean toDelete) {
        this.toDelete = toDelete;
    }

    @Override
    public String toString() {
        return "CaratRangeDataBean{" + "id=" + id + ", minValue=" + minValue + ", maxValue=" + maxValue + ", status=" + status + ", franchise=" + franchise + ", newadded=" + newadded + ", edited=" + edited + ", toDelete=" + toDelete + '}';
    }

}
