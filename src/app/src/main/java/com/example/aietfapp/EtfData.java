package com.example.aietfapp;

import java.util.List;
import java.util.Map;

public class EtfData {
    private String symbol;
    private String name;
    private double currentPrice;
    private double changePercent;
    private Map<String, List<Double>> priceHistoryByPeriod;
    private List<ExpertOpinion> expertOpinions;
    private boolean enabled;

    public EtfData(String symbol, String name, double currentPrice, double changePercent, 
                   Map<String, List<Double>> priceHistoryByPeriod, List<ExpertOpinion> expertOpinions) {
        this.symbol = symbol;
        this.name = name;
        this.currentPrice = currentPrice;
        this.changePercent = changePercent;
        this.priceHistoryByPeriod = priceHistoryByPeriod;
        this.expertOpinions = expertOpinions;
        this.enabled = true; // 기본값으로 활성화
    }

    public String getSymbol() {
        return symbol;
    }

    public String getName() {
        return name;
    }

    public double getCurrentPrice() {
        return currentPrice;
    }

    public double getChangePercent() {
        return changePercent;
    }

    public List<Double> getPriceHistoryByPeriod(String period) {
        return priceHistoryByPeriod.get(period);
    }

    public Map<String, List<Double>> getAllPriceHistory() {
        return priceHistoryByPeriod;
    }

    public List<ExpertOpinion> getExpertOpinions() {
        return expertOpinions;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}