package com.example.aietfapp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class EtfDataProvider {
    
    public static List<EtfData> getAiEtfData() {
        return getEtfDataList();
    }
    
    public static List<EtfData> getEtfDataList() {
        List<EtfData> etfList = new ArrayList<>();
        
        etfList.add(new EtfData("QQQ", "Invesco QQQ Trust ETF", 
            378.52, 1.23, createPriceHistory(378.52, 1.23), createExpertOpinions("QQQ")));
            
        etfList.add(new EtfData("ARKK", "ARK Innovation ETF", 
            47.85, -0.85, createPriceHistory(47.85, -0.85), createExpertOpinions("ARKK")));
            
        etfList.add(new EtfData("BOTZ", "Global X Robotics & AI ETF", 
            28.45, 2.15, createPriceHistory(28.45, 2.15), createExpertOpinions("BOTZ")));
            
        etfList.add(new EtfData("ROBO", "ROBO Global Robotics & Automation ETF", 
            52.30, 1.45, createPriceHistory(52.30, 1.45), createExpertOpinions("ROBO")));
            
        etfList.add(new EtfData("IRBO", "iShares Robotics and AI Multisector ETF", 
            45.67, 0.95, createPriceHistory(45.67, 0.95), createExpertOpinions("IRBO")));
            
        etfList.add(new EtfData("AIEQ", "AI Powered Equity ETF", 
            32.88, 1.67, createPriceHistory(32.88, 1.67), createExpertOpinions("AIEQ")));
            
        etfList.add(new EtfData("ARKQ", "ARK Autonomous & Robotics ETF", 
            42.15, -1.25, createPriceHistory(42.15, -1.25), createExpertOpinions("ARKQ")));
            
        etfList.add(new EtfData("XLK", "Technology Select Sector SPDR Fund", 
            185.25, 2.33, createPriceHistory(185.25, 2.33), createExpertOpinions("XLK")));
            
        etfList.add(new EtfData("VGT", "Vanguard Information Technology ETF", 
            445.78, 1.89, createPriceHistory(445.78, 1.89), createExpertOpinions("VGT")));
            
        etfList.add(new EtfData("FTEC", "Fidelity MSCI Information Technology ETF", 
            122.34, 2.11, createPriceHistory(122.34, 2.11), createExpertOpinions("FTEC")));
            
        return etfList;
    }
    
    private static Map<String, List<Double>> createPriceHistory(double currentPrice, double changePercent) {
        Map<String, List<Double>> priceHistory = new HashMap<>();
        
        // 1일 (시간별 데이터 24개 포인트)
        priceHistory.put("1D", generatePriceData(currentPrice, changePercent, 24, 0.5));
        
        // 1주일 (일별 데이터 7개 포인트)  
        priceHistory.put("1W", generatePriceData(currentPrice, changePercent, 7, 2.0));
        
        // 1달 (일별 데이터 30개 포인트)
        priceHistory.put("1M", generatePriceData(currentPrice, changePercent, 30, 3.0));
        
        // 6개월 (주별 데이터 26개 포인트)
        priceHistory.put("6M", generatePriceData(currentPrice, changePercent, 26, 8.0));
        
        // 1년 (월별 데이터 12개 포인트)
        priceHistory.put("1Y", generatePriceData(currentPrice, changePercent, 12, 15.0));
        
        return priceHistory;
    }
    
    private static List<Double> generatePriceData(double currentPrice, double changePercent, int points, double volatility) {
        List<Double> prices = new ArrayList<>();
        double startPrice = currentPrice * (1 - changePercent / 100);
        
        for (int i = 0; i < points; i++) {
            double progress = (double) i / (points - 1);
            double trend = startPrice + (currentPrice - startPrice) * progress;
            double randomVariation = (Math.random() - 0.5) * volatility;
            double price = trend + randomVariation;
            
            if (price < 0) price = trend * 0.5;
            
            prices.add(price);
        }
        
        prices.set(points - 1, currentPrice);
        
        return prices;
    }
    
    private static List<ExpertOpinion> createExpertOpinions(String symbol) {
        List<ExpertOpinion> opinions = new ArrayList<>();
        
        switch (symbol) {
            case "QQQ":
                opinions.add(new ExpertOpinion("James Chen", "Goldman Sachs", 
                    "QQQ continues to benefit from tech sector strength. Strong holdings in AI leaders.", 
                    "Buy", "$400", "https://www.goldmansachs.com/insights/"));
                opinions.add(new ExpertOpinion("Sarah Kim", "Morgan Stanley", 
                    "Solid long-term growth potential with exposure to mega-cap tech stocks.", 
                    "Overweight", "$390", "https://www.morganstanley.com/ideas/"));
                break;
                
            case "ARKK":
                opinions.add(new ExpertOpinion("Michael Rodriguez", "JPMorgan", 
                    "High-risk, high-reward innovation play. Volatile but strong AI exposure.", 
                    "Hold", "$55", "https://www.jpmorgan.com/insights/"));
                opinions.add(new ExpertOpinion("Lisa Wang", "Barclays", 
                    "Cathie Wood's picks show promise in disruptive technologies including AI.", 
                    "Buy", "$60", "https://www.investmentbank.barclays.com/insights.html"));
                break;
                
            case "BOTZ":
                opinions.add(new ExpertOpinion("David Park", "Deutsche Bank", 
                    "Pure play on robotics and AI automation. Industrial AI adoption accelerating.", 
                    "Buy", "$32", "https://www.db.com/news/"));
                opinions.add(new ExpertOpinion("Emily Johnson", "Credit Suisse", 
                    "Diversified robotics exposure with strong fundamentals.", 
                    "Outperform", "$35", "https://www.credit-suisse.com/about-us/en/reports-research.html"));
                break;
                
            case "ROBO":
                opinions.add(new ExpertOpinion("Robert Lee", "UBS", 
                    "Global robotics theme intact. Manufacturing automation driving growth.", 
                    "Buy", "$58", "https://www.ubs.com/global/en/investment-bank/insights.html"));
                opinions.add(new ExpertOpinion("Jennifer Brown", "Wells Fargo", 
                    "Well-positioned for the automation revolution across industries.", 
                    "Overweight", "$56", "https://www.wellsfargo.com/investment-institute/"));
                break;
                
            case "IRBO":
                opinions.add(new ExpertOpinion("Thomas Wilson", "Bank of America", 
                    "iShares quality with balanced robotics and AI exposure.", 
                    "Buy", "$50", "https://www.bofaml.com/en-us/content/global-research.html"));
                opinions.add(new ExpertOpinion("Amanda Davis", "Citigroup", 
                    "Solid diversification across AI and automation sectors.", 
                    "Hold", "$48", "https://www.citigroup.com/citi/research/"));
                break;
                
            case "AIEQ":
                opinions.add(new ExpertOpinion("Steven Martinez", "RBC Capital", 
                    "AI-driven stock selection provides unique alpha generation.", 
                    "Buy", "$38", "https://www.rbccm.com/en/expertise/"));
                opinions.add(new ExpertOpinion("Rachel Taylor", "BMO Capital", 
                    "Innovative AI approach to portfolio management shows promise.", 
                    "Outperform", "$36", "https://capitalmarkets.bmo.com/en/research/"));
                break;
                
            case "ARKQ":
                opinions.add(new ExpertOpinion("Kevin Anderson", "Jefferies", 
                    "Autonomous technology and robotics theme gaining momentum.", 
                    "Hold", "$48", "https://www.jefferies.com/CMSFiles/Jefferies.com/insights/"));
                opinions.add(new ExpertOpinion("Nicole Garcia", "Raymond James", 
                    "Strong exposure to self-driving and automation technologies.", 
                    "Buy", "$50", "https://www.raymondjames.com/investment-strategy"));
                break;
                
            case "XLK":
                opinions.add(new ExpertOpinion("Mark Thompson", "Oppenheimer", 
                    "Technology sector leadership continues with AI driving growth.", 
                    "Buy", "$200", "https://www.oppenheimer.com/research/"));
                opinions.add(new ExpertOpinion("Crystal White", "Piper Sandler", 
                    "Largest tech names benefit from AI infrastructure spending.", 
                    "Overweight", "$195", "https://www.pipersandler.com/research/"));
                break;
                
            case "VGT":
                opinions.add(new ExpertOpinion("Brian Miller", "Cowen", 
                    "Vanguard's low-cost tech exposure with AI upside potential.", 
                    "Buy", "$470", "https://www.cowen.com/insights/"));
                opinions.add(new ExpertOpinion("Angela Clark", "Stifel", 
                    "Excellent diversification across tech giants driving AI innovation.", 
                    "Hold", "$460", "https://www.stifel.com/insights/"));
                break;
                
            case "FTEC":
                opinions.add(new ExpertOpinion("Daniel Lewis", "Wedbush", 
                    "Fidelity's tech ETF offers broad AI and cloud computing exposure.", 
                    "Outperform", "$130", "https://www.wedbush.com/research/"));
                opinions.add(new ExpertOpinion("Melissa Young", "Evercore ISI", 
                    "Strong fundamentals with focus on profitable tech companies.", 
                    "Buy", "$128", "https://www.evercore.com/research/"));
                break;
        }
        
        return opinions;
    }
    
    public static EtfData createSampleEtf(String symbol, String name) {
        // 기본 가격 및 변동률 생성
        Random random = new Random();
        double basePrice = 50 + random.nextDouble() * 150; // 50-200 사이의 기본 가격
        double changePercent = -5 + random.nextDouble() * 10; // -5%에서 +5% 사이
        
        // 가격 히스토리 생성
        Map<String, List<Double>> priceHistory = createPriceHistory(basePrice, changePercent);
        
        // 기본 전문가 의견 생성
        List<ExpertOpinion> opinions = new ArrayList<>();
        opinions.add(new ExpertOpinion("분석가", "투자회사", 
            "새로 추가된 ETF에 대한 분석이 필요합니다.", 
            "Hold", String.format("$%.0f", basePrice), "https://example.com"));
        opinions.add(new ExpertOpinion("리서치팀", "증권사", 
            "추가 데이터 수집 후 의견을 제공할 예정입니다.", 
            "Neutral", String.format("$%.0f", basePrice * 1.05), "https://example.com"));
        
        return new EtfData(symbol, name, basePrice, changePercent, priceHistory, opinions);
    }
}