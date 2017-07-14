package com.millertronics.millerapp.millerchecklistandroid.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by koha.choji on 26/06/2017.
 */

public class Checklist{

    private Integer id;
    private String frequency;
    private String name;
    private String description;
    private boolean completed = false;
    private List<ChecklistItem> checklistItems = new ArrayList<ChecklistItem>();

    public static final String FREQUENCY_DAILY = "daily";
    public static final String FREQUENCY_WEEKLY = "weekly";
    public static final String FREQUENCY_MONTHLY = "monthly";


    public Checklist(){

    }

    private Checklist(Builder builder){
        this.id = builder.id;
        this.frequency = builder.frequency;
        this.name = builder.name;
        this.description = builder.description;
        this.completed = builder.completed;
        this.checklistItems = builder.checklistItems;
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

    public Boolean isCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }

    public List<ChecklistItem> getChecklistItems() {
        return checklistItems;
    }

    public void setChecklistItems(List<ChecklistItem> checklistItems) {
        this.checklistItems = checklistItems;
    }

    public static class Builder{
        private Integer id;
        private String frequency;
        private String name;
        private String description;
        private boolean completed = false;
        private List<ChecklistItem> checklistItems = new ArrayList<>();

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

        public Builder addChecklistItem(ChecklistItem item){
            checklistItems.add(item);
            return this;
        }

        public Builder completed(String completedDate){
            this.completed = false;
            if (StringUtils.isNotBlank(completedDate)) {
                DateTimeFormatter formatter = DateTimeFormat
                        .forPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                try {
                    DateTime last_completed_date = formatter.parseDateTime(completedDate);
                    switch (frequency) {
                        case Checklist.FREQUENCY_DAILY:
                            this.completed = last_completed_date.isAfter(new DateTime()
                                    .withTimeAtStartOfDay());
                            break;
                        case Checklist.FREQUENCY_WEEKLY:
                            this.completed = last_completed_date.isAfter(new DateTime()
                                    .withDayOfWeek(DateTimeConstants.MONDAY)
                                    .withTimeAtStartOfDay());
                            break;
                        case Checklist.FREQUENCY_MONTHLY:
                            this.completed = last_completed_date.isAfter(new DateTime()
                                    .dayOfMonth().withMinimumValue());
                            break;
                        default:
                            this.completed = false;
                            break;
                    }
                } catch (Exception e) {
                    Log.e(Checklist.class.getName(), Log.getStackTraceString(e));
                }
            }
            return this;
        }

        public Checklist build(){
            return new Checklist(this);
        }
    }

}
