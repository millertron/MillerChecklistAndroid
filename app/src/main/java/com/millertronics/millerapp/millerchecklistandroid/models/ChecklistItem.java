package com.millertronics.millerapp.millerchecklistandroid.models;

/**
 * Created by koha.choji on 26/06/2017.
 */

public class ChecklistItem {
    private Integer id;
    private Integer checklistId;
    private String text;
    private String valueType;
    private String metricTargetMax;
    private String metricTargetMin;
    private Boolean mandatory;

    public static final String VALUE_TYPE_BINARY = "binary";
    public static final String VALUE_TYPE_METRIC = "metric";

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getChecklistId() {
        return checklistId;
    }

    public void setChecklistId(Integer checklistId) {
        this.checklistId = checklistId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getValueType() {
        return valueType;
    }

    public void setValueType(String valueType) {
        this.valueType = valueType;
    }

    public String getMetricTargetMax() {
        return metricTargetMax;
    }

    public void setMetricTargetMax(String metricTargetMax) {
        this.metricTargetMax = metricTargetMax;
    }

    public String getMetricTargetMin() {
        return metricTargetMin;
    }

    public void setMetricTargetMin(String metricTargetMin) {
        this.metricTargetMin = metricTargetMin;
    }

    public Boolean getMandatory() {
        return mandatory;
    }

    public void setMandatory(Boolean mandatory) {
        this.mandatory = mandatory;
    }

    private ChecklistItem(Builder builder){
        this.id = builder.id;
        this.checklistId = builder.checklistId;
        this.text = builder.text;
        this.valueType = builder.valueType;
        this.metricTargetMax = builder.metricTargetMax;
        this.metricTargetMin = builder.metricTargetMin;
        this.mandatory = builder.mandatory;
    }

    public static class Builder {
        private Integer id;
        private Integer checklistId;
        private String text;
        private String valueType;
        private String metricTargetMax;
        private String metricTargetMin;
        private Boolean mandatory;

        public Builder(){}

        public Builder id(Integer id){
            this.id = id;
            return this;
        }

        public Builder checklistId(Integer checklistId){
            this.checklistId = checklistId;
            return this;
        }

        public Builder text(String text){
            this.text = text;
            return this;
        }

        public Builder valueType(String valueType){
            this.valueType = valueType;
            return this;
        }

        public Builder metricTargetMax(String metricTargetMax){
            this.metricTargetMax = metricTargetMax;
            return this;
        }

        public Builder metricTargetMin(String metricTargetMin){
            this.metricTargetMin = metricTargetMin;
            return this;
        }

        public Builder mandatory(Boolean mandatory){
            this.mandatory = mandatory;
            return this;
        }

        public ChecklistItem build(){
            return new ChecklistItem(this);
        }
    }
}
