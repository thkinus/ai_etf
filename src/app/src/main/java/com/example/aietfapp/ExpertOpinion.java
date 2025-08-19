package com.example.aietfapp;

public class ExpertOpinion {
    private String expertName;
    private String company;
    private String opinion;
    private String rating;
    private String targetPrice;
    private String url;

    public ExpertOpinion(String expertName, String company, String opinion, String rating, String targetPrice, String url) {
        this.expertName = expertName;
        this.company = company;
        this.opinion = opinion;
        this.rating = rating;
        this.targetPrice = targetPrice;
        this.url = url;
    }

    public String getExpertName() {
        return expertName;
    }

    public String getCompany() {
        return company;
    }

    public String getOpinion() {
        return opinion;
    }

    public String getRating() {
        return rating;
    }

    public String getTargetPrice() {
        return targetPrice;
    }

    public String getUrl() {
        return url;
    }
}