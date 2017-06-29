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

    public static class Builder{
        private Integer id;
        private String frequency;
        private String name;
        private String description;

        public Checklist build(){
            return new Checklist();
        }
    }

}
