package com.example.aietfapp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class KoreanEtfDataProvider {
    
    public static List<EtfData> getKoreanAiEtfData() {
        List<EtfData> etfList = new ArrayList<>();
        
        // KODEX 200 IT ETF
        etfList.add(new EtfData("069500", "KODEX 200", 
            42350, 1.45, createPriceHistory(42350, 1.45), createExpertOpinions("069500")));
            
        // TIGER 코스닥150 IT ETF
        etfList.add(new EtfData("091160", "TIGER 코스닥150", 
            16850, 2.1, createPriceHistory(16850, 2.1), createExpertOpinions("091160")));
            
        // KODEX 코스닥150 선물인버스
        etfList.add(new EtfData("251340", "KODEX 코스닥150선물인버스", 
            3245, -0.8, createPriceHistory(3245, -0.8), createExpertOpinions("251340")));
            
        // KBSTAR 코스닥150
        etfList.add(new EtfData("091170", "KBSTAR 코스닥150", 
            16420, 1.92, createPriceHistory(16420, 1.92), createExpertOpinions("091170")));
            
        // ARIRANG 코스닥150
        etfList.add(new EtfData("152100", "ARIRANG 코스닥150", 
            14890, 1.85, createPriceHistory(14890, 1.85), createExpertOpinions("152100")));
            
        // KODEX 인터넷 ETF
        etfList.add(new EtfData("229200", "KODEX 인터넷", 
            8965, 2.3, createPriceHistory(8965, 2.3), createExpertOpinions("229200")));
            
        // TIGER 게임콘텐츠
        etfList.add(new EtfData("261070", "TIGER 게임콘텐츠", 
            12650, 3.1, createPriceHistory(12650, 3.1), createExpertOpinions("261070")));
            
        // KODEX 헬스케어 테크놀로지
        etfList.add(new EtfData("289480", "KODEX 헬스케어테크놀로지", 
            9870, 1.6, createPriceHistory(9870, 1.6), createExpertOpinions("289480")));
            
        // TIGER 인공지능
        etfList.add(new EtfData("365040", "TIGER 인공지능", 
            11240, 2.8, createPriceHistory(11240, 2.8), createExpertOpinions("365040")));
            
        // KODEX K-뉴딜 테마 ETF
        etfList.add(new EtfData("381170", "KODEX K-뉴딜테마", 
            10450, 1.2, createPriceHistory(10450, 1.2), createExpertOpinions("381170")));
        
        return etfList;
    }
    
    private static Map<String, List<Double>> createPriceHistory(double currentPrice, double changePercent) {
        Map<String, List<Double>> priceHistory = new HashMap<>();
        
        // 1일 (시간별 데이터 24개 포인트)
        priceHistory.put("1D", generatePriceData(currentPrice, changePercent, 24, 1.0));
        
        // 1주일 (일별 데이터 7개 포인트)
        priceHistory.put("1W", generatePriceData(currentPrice, changePercent, 7, 2.0));
        
        // 1달 (일별 데이터 30개 포인트)
        priceHistory.put("1M", generatePriceData(currentPrice, changePercent, 30, 4.0));
        
        // 6개월 (주별 데이터 26개 포인트)
        priceHistory.put("6M", generatePriceData(currentPrice, changePercent, 26, 10.0));
        
        // 1년 (월별 데이터 12개 포인트)
        priceHistory.put("1Y", generatePriceData(currentPrice, changePercent, 12, 20.0));
        
        return priceHistory;
    }
    
    private static List<Double> generatePriceData(double currentPrice, double changePercent, int points, double volatility) {
        List<Double> prices = new ArrayList<>();
        double startPrice = currentPrice * (1 - changePercent / 100);
        Random random = new Random();
        
        for (int i = 0; i < points; i++) {
            double progress = (double) i / (points - 1);
            double trend = startPrice + (currentPrice - startPrice) * progress;
            double noise = (random.nextDouble() - 0.5) * 2 * volatility * currentPrice / 100;
            double price = Math.max(trend + noise, currentPrice * 0.5);
            prices.add(price);
        }
        
        return prices;
    }
    
    private static List<ExpertOpinion> createExpertOpinions(String symbol) {
        List<ExpertOpinion> opinions = new ArrayList<>();
        
        switch (symbol) {
            case "069500":
                opinions.add(new ExpertOpinion("김영수", "삼성증권", 
                    "대형주 중심의 안정적인 수익률을 기대할 수 있는 ETF", 
                    "Buy", "43,000원", "https://www.samsungpop.com"));
                opinions.add(new ExpertOpinion("이미경", "NH투자증권", 
                    "IT 섹터 비중 확대로 장기 성장성 우수", 
                    "Hold", "42,500원", "https://www.nhqv.com"));
                break;
                
            case "091160":
                opinions.add(new ExpertOpinion("박정훈", "키움증권", 
                    "코스닥 대표 IT 기업들의 성장성에 주목", 
                    "Buy", "17,200원", "https://www.kiwoom.com"));
                opinions.add(new ExpertOpinion("최수정", "미래에셋증권", 
                    "반도체, 게임 섹터 회복세로 상승 모멘텀 확보", 
                    "Overweight", "17,000원", "https://www.miraeasset.com"));
                break;
                
            case "091170":
                opinions.add(new ExpertOpinion("정민호", "한국투자증권", 
                    "코스닥150 추종으로 중소형 성장주 투자에 적합", 
                    "Buy", "16,800원", "https://www.truefriend.com"));
                opinions.add(new ExpertOpinion("윤서연", "대신증권", 
                    "기술주 중심 포트폴리오로 AI 테마 수혜 기대", 
                    "Hold", "16,500원", "https://www.daishin.com"));
                break;
                
            case "229200":
                opinions.add(new ExpertOpinion("강태욱", "하나금융투자", 
                    "인터넷 기업들의 디지털 전환 가속화로 성장 잠재력 높음", 
                    "Buy", "9,200원", "https://www.hanaw.com"));
                opinions.add(new ExpertOpinion("임지혜", "신한투자증권", 
                    "e커머스, 플랫폼 기업 중심으로 안정적 수익 창출", 
                    "Overweight", "9,100원", "https://www.shinhaninvest.com"));
                break;
                
            case "261070":
                opinions.add(new ExpertOpinion("오성민", "KB증권", 
                    "국내 게임 산업의 글로벌 경쟁력 강화로 장기 성장", 
                    "Buy", "13,000원", "https://www.kbsec.co.kr"));
                opinions.add(new ExpertOpinion("한나영", "메리츠증권", 
                    "메타버스, NFT 등 신기술 접목으로 새로운 성장 동력", 
                    "Hold", "12,800원", "https://www.meritz.co.kr"));
                break;
                
            case "289480":
                opinions.add(new ExpertOpinion("서동현", "유진투자증권", 
                    "헬스테크 분야 혁신으로 의료 산업 패러다임 변화", 
                    "Buy", "10,200원", "https://www.eugeneib.com"));
                opinions.add(new ExpertOpinion("조은별", "IBK투자증권", 
                    "바이오 헬스케어와 IT 기술 융합으로 성장성 우수", 
                    "Overweight", "10,000원", "https://www.ibks.com"));
                break;
                
            case "365040":
                opinions.add(new ExpertOpinion("문재석", "교보증권", 
                    "국내 AI 기업들의 기술력 향상으로 투자 매력도 증가", 
                    "Buy", "11,500원", "https://www.iprovest.com"));
                opinions.add(new ExpertOpinion("배지원", "SK증권", 
                    "AI 관련 정부 정책 지원으로 섹터 성장 가속화", 
                    "Strong Buy", "11,800원", "https://www.sks.co.kr"));
                break;
                
            case "381170":
                opinions.add(new ExpertOpinion("홍준표", "신영증권", 
                    "K-뉴딜 정책 수혜로 디지털 뉴딜 관련 기업들 수혜", 
                    "Hold", "10,600원", "https://www.shinyoung.com"));
                opinions.add(new ExpertOpinion("류성아", "유안타증권", 
                    "그린뉴딜과 디지털뉴딜 테마 모두 포함한 균형 투자", 
                    "Buy", "10,700원", "https://www.yuanta.co.kr"));
                break;
                
            default:
                opinions.add(new ExpertOpinion("분석가", "증권사", 
                    "한국 ETF 시장의 성장성을 주목하고 있습니다.", 
                    "Hold", "N/A", "https://example.com"));
                break;
        }
        
        return opinions;
    }
}