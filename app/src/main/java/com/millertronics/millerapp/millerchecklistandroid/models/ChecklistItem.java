package com.millertronics.millerapp.millerchecklistandroid.models;

/**
 * Created by koha.choji on 26/06/2017.
 */

public class ChecklistItem {
    private Integer id;
    private Integer checklistId;
    private String text;
    private String metricTargetMax;
    private String metricTargetMin;
    private Boolean mandatory;

    private ChecklistItem(Builder builder){
        this.id = builder.id;
        this.checklistId = builder.checklistId;
        this.text = builder.text;
        this.metricTargetMax = builder.metricTargetMax;
        this.metricTargetMin = builder.metricTargetMin;
        this.mandatory = builder.mandatory;
    }

    public static class Builder {
        private Integer id;
        private Integer checklistId;
        private String text;
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
