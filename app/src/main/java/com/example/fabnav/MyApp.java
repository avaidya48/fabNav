package com.example.fabnav;

import android.app.Application;
import android.os.Bundle;

public class MyApp extends Application {
    private Boolean cancelRedirect = false;

    private Double destLat;
    private Double destLong;

    private User user;
    private Survey survey;
    private String parkingId;

    public Boolean getCancelRedirect() {
        return cancelRedirect;
    }

    public void setCancelRedirect(Boolean cancelRedirect) {
        this.cancelRedirect = cancelRedirect;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Double getDestLat() {
        return destLat;
    }

    public void setDestLat(Double destLat) {
        this.destLat = destLat;
    }

    public Double getDestLong() {
        return destLong;
    }

    public void setDestLong(Double destLong) {
        this.destLong = destLong;
    }

    public Survey getSurvey() {
        return survey;
    }

    public void setSurvey(Survey survey) {
        this.survey = survey;
    }

    public String getParkingId() {
        return parkingId;
    }

    public void setParkingId(String parkingId) {
        this.parkingId = parkingId;
    }
}
