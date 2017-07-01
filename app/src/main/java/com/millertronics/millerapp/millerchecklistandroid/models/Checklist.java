package com.millertronics.millerapp.millerchecklistandroid.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by koha.choji on 26/06/2017.
 */

public class Checklist {
    private Integer id;
    private String frequency;
    private String name;
    private String description;
    private List<ChecklistItem> checklistItems = new ArrayList<ChecklistItem>();

    public Checklist(){

    }

    private Checklist(Builder builder){
        this.id = builder.id;
        this.frequency = builder.frequency;
        this.name = builder.name;
        this.description = builder.description;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<ChecklistItem> getChecklistItems() {
        return checklistItems;
    }

    public void setChecklistItems(List<ChecklistItem> checklistItems) {
        this.checklistItems = checklistItems;
    }

    public enum Frequency {
        DAILY, WEEKLY, MONTHLY
    }

    public static class Builder{
        private Integer id;
        private String frequency;
        private String name;
        private String description;

        public Builder(){

        }

        public Builder id(Integer id){
            this.id = id;
            return this;
        }

        public Builder frequency(String frequency) {
            this.frequency = frequency;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Checklist build(){
            return new Checklist(this);
        }
    }

}
