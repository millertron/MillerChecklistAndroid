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

    public Checklist(Integer id, String frequency, String name, String description){
        this.id = id;
        this.frequency = frequency;
        this.name = name;
        this.description = description;
    }

    public static class Builder{
        private Integer id;
        private String frequency;
        private String name;
        private String description;

        public void setId(Integer id){
            this.id = id;
        }

        public void setFrequency(String frequency){
            this.frequency = frequency;
        }

        public void setName(String name){
            this.name = name;
        }

        public void setDescription(String description){
            this.description = description;
        }

        public Checklist build(){
            return new Checklist();
        }
    }

}
