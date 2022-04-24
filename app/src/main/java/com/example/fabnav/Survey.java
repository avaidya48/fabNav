package com.example.fabnav;

import java.sql.Timestamp;


public class Survey {

    private String parkingId;
    private User.occupancy occupancy;
    private Boolean safety;
    private Long rate;
    private String userName;

    public String getParkingId() {
        return parkingId;
    }

    public void setParkingId(String parkingId) {
        this.parkingId = parkingId;
    }

    public User.occupancy getOccupancy() {
        return occupancy;
    }

    public void setOccupancy(User.occupancy occ) {
        this.occupancy = occ;
    }

    public Boolean getSafety() {
        return safety;
    }

    public void setSafety(Boolean safety) {
        this.safety = safety;
    }

    public Long getRate() {
        return rate;
    }

    public void setRate(Long rate) {
        this.rate = rate;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

}
