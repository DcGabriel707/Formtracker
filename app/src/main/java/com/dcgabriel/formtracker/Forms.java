package com.dcgabriel.formtracker;

import android.net.Uri;

public class Forms {
    private String name;
    private String company;
    private String details;
    private String deadline;
    private String status;
    private Integer id;
    private Uri uri; //to be passed into the recyclerViewAdapter where it will be passed to the editentry activity. so the activity can get the correct data to fill the edittexts

    public Forms() {

    }

    public Forms(String name) {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }


}
