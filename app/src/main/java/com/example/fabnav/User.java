package com.example.fabnav;

public class User {
    public enum occupancy {
        EMPTY,
        SOMEWHAT_FULL,
        HALF_FULL,
        ALMOST_FULL,
        FULL
    }

    private String userName;
    private String password;
    private Integer paymentMax;
    private Boolean disabilityFriendly;
    private Integer radius;
    private occupancy occupancyPreference;
    private Boolean safetyPreference;
    private Integer completedSurveys;
    private Boolean eligibleForRewards;

    public User(String userName, String password) {
        super();
        this.userName = userName;
        this.password = password;
    }

    public User(){
        super();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getPaymentMax() {
        return paymentMax;
    }

    public void setPaymentMax(Integer paymentMax) {
        this.paymentMax = paymentMax;
    }

    public Boolean getDisabilityFriendly() {
        return disabilityFriendly;
    }

    public void setDisabilityFriendly(Boolean disabilityFriendly) {
        this.disabilityFriendly = disabilityFriendly;
    }

    public Integer getRadius() {
        return radius;
    }

    public occupancy getOccupancyPreference() {
        return occupancyPreference;
    }

    public void setOccupancyPreference(occupancy occupancyPreference) {
        this.occupancyPreference = occupancyPreference;
    }

    public void setRadius(Integer radius) {
        this.radius = radius;
    }

    public Boolean getSafetyPreference() {
        return safetyPreference;
    }

    public void setSafetyPreference(Boolean safetyPreference) {
        this.safetyPreference = safetyPreference;
    }

    public Integer getCompletedSurveys() {
        return completedSurveys;
    }

    public void setCompletedSurveys(Integer completedSurveys) {
        this.completedSurveys = completedSurveys;
    }

    public Boolean getEligibleForRewards() {
        return eligibleForRewards;
    }

    public void setEligibleForRewards(Boolean eligibleForRewards) {
        this.eligibleForRewards = eligibleForRewards;
    }


}

