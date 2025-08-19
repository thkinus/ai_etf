package com.example.aietfapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EtfSymbolDatabase {
    
    private static Map<String, String> etfSymbols = new HashMap<>();
    
    static {
        // AI & Technology ETFs
        etfSymbols.put("QQQ", "Invesco QQQ Trust ETF");
        etfSymbols.put("QTEC", "First Trust NASDAQ-100-Technology Sector Index Fund");
        etfSymbols.put("QQQM", "Invesco NASDAQ 100 ETF");
        etfSymbols.put("QQQJ", "Invesco NASDAQ Next Gen 100 ETF");
        
        // AI Focused ETFs
        etfSymbols.put("ARKK", "ARK Innovation ETF");
        etfSymbols.put("ARKQ", "ARK Autonomous Technology & Robotics ETF");
        etfSymbols.put("ARKW", "ARK Next Generation Internet ETF");
        etfSymbols.put("ARKG", "ARK Genomics Revolution ETF");
        etfSymbols.put("ARKF", "ARK Fintech Innovation ETF");
        
        // Robotics & Automation ETFs
        etfSymbols.put("BOTZ", "Global X Robotics & Artificial Intelligence ETF");
        etfSymbols.put("ROBO", "ROBO Global Robotics and Automation Index ETF");
        etfSymbols.put("ROBH", "First Trust Nasdaq Artificial Intelligence and Robotics ETF");
        etfSymbols.put("ROKT", "SPDR S&P Kensho Final Frontiers ETF");
        
        // Cloud Computing & Software ETFs
        etfSymbols.put("CLOU", "Global X Cloud Computing ETF");
        etfSymbols.put("SKYY", "First Trust Cloud Computing ETF");
        etfSymbols.put("WCLD", "WisdomTree Cloud Computing Fund");
        etfSymbols.put("IGPT", "Roundhill Generative AI & Technology ETF");
        etfSymbols.put("CHAT", "Roundhill Chat GPT & AI ETF");
        
        // Technology Sector ETFs
        etfSymbols.put("XLK", "Technology Select Sector SPDR Fund");
        etfSymbols.put("VGT", "Vanguard Information Technology ETF");
        etfSymbols.put("FTEC", "Fidelity MSCI Information Technology Index ETF");
        etfSymbols.put("IYW", "iShares U.S. Technology ETF");
        etfSymbols.put("TECL", "Direxion Daily Technology Bull 3X Shares");
        
        // Semiconductor ETFs
        etfSymbols.put("SMH", "VanEck Vectors Semiconductor ETF");
        etfSymbols.put("SOXX", "iShares PHLX Semiconductor ETF");
        etfSymbols.put("SOXL", "Direxion Daily Semiconductor Bull 3X Shares");
        etfSymbols.put("PSI", "Invesco Dynamic Semiconductors ETF");
        
        // Cybersecurity ETFs
        etfSymbols.put("HACK", "ETFMG Prime Cyber Security ETF");
        etfSymbols.put("CIBR", "First Trust NASDAQ Cybersecurity ETF");
        etfSymbols.put("BUG", "Global X Cybersecurity ETF");
        etfSymbols.put("IHAK", "iShares Cybersecurity and Tech ETF");
        
        // Gaming & Esports ETFs
        etfSymbols.put("ESPO", "VanEck Video Gaming and eSports ETF");
        etfSymbols.put("NERD", "Roundhill BITKRAFT Esports & Digital Entertainment ETF");
        etfSymbols.put("GAMR", "Wedbush ETFMG Video Game Tech ETF");
        
        // Blockchain & Crypto ETFs
        etfSymbols.put("BLOK", "Amplify Transformational Data Sharing ETF");
        etfSymbols.put("BITQ", "Bitwise Crypto Industry Innovators ETF");
        etfSymbols.put("LEGR", "First Trust Indxx Innovative Transaction & Process ETF");
        
        // Internet & Digital ETFs
        etfSymbols.put("FDN", "First Trust Dow Jones Internet Index Fund");
        etfSymbols.put("PNQI", "Invesco NASDAQ Internet ETF");
        etfSymbols.put("XITK", "SPDR FactSet Innovative Technology ETF");
        
        // Fintech ETFs
        etfSymbols.put("FINX", "Global X FinTech ETF");
        etfSymbols.put("IPAY", "ETFMG Prime Mobile Payments ETF");
        etfSymbols.put("TPAY", "Tortoise Digital Payments Infrastructure Fund");
        
        // Clean Energy & Electric Vehicle ETFs
        etfSymbols.put("ICLN", "iShares Global Clean Energy ETF");
        etfSymbols.put("PBW", "Invesco WilderHill Clean Energy ETF");
        etfSymbols.put("DRIV", "Global X Autonomous & Electric Vehicles ETF");
        etfSymbols.put("IDRV", "iShares Self-Driving EV and Tech ETF");
        
        // Space & Defense ETFs  
        etfSymbols.put("UFO", "Procure Space ETF");
        etfSymbols.put("ARKX", "ARK Space Exploration & Innovation ETF");
        etfSymbols.put("ITA", "iShares U.S. Aerospace & Defense ETF");
        
        // Additional Popular ETFs
        etfSymbols.put("SPY", "SPDR S&P 500 ETF Trust");
        etfSymbols.put("VOO", "Vanguard S&P 500 ETF");
        etfSymbols.put("VTI", "Vanguard Total Stock Market ETF");
        etfSymbols.put("IVV", "iShares Core S&P 500 ETF");
        etfSymbols.put("NVDA", "NVIDIA Corporation");
        etfSymbols.put("TSLA", "Tesla Inc");
        etfSymbols.put("MSFT", "Microsoft Corporation");
        etfSymbols.put("GOOGL", "Alphabet Inc Class A");
        etfSymbols.put("AMZN", "Amazon.com Inc");
        etfSymbols.put("META", "Meta Platforms Inc");
        etfSymbols.put("AAPL", "Apple Inc");
    }
    
    public static List<EtfSymbolSuggestion> searchSymbols(String query) {
        List<EtfSymbolSuggestion> suggestions = new ArrayList<>();
        
        if (query == null || query.trim().isEmpty()) {
            return suggestions;
        }
        
        String upperQuery = query.toUpperCase().trim();
        
        // 심볼로 검색 (우선순위 높음)
        for (Map.Entry<String, String> entry : etfSymbols.entrySet()) {
            String symbol = entry.getKey();
            String name = entry.getValue();
            
            if (symbol.startsWith(upperQuery)) {
                suggestions.add(new EtfSymbolSuggestion(symbol, name, true));
            }
        }
        
        // 이름으로 검색 (우선순위 낮음)
        for (Map.Entry<String, String> entry : etfSymbols.entrySet()) {
            String symbol = entry.getKey();
            String name = entry.getValue();
            
            if (!symbol.startsWith(upperQuery) && 
                name.toUpperCase().contains(upperQuery)) {
                suggestions.add(new EtfSymbolSuggestion(symbol, name, false));
            }
        }
        
        return suggestions;
    }
    
    public static List<EtfSymbolSuggestion> searchByNameOrSymbol(String query) {
        List<EtfSymbolSuggestion> results = new ArrayList<>();
        
        if (query == null || query.trim().isEmpty()) {
            return results;
        }
        
        String[] keywords = query.toUpperCase().trim().split("\\s+");
        
        for (Map.Entry<String, String> entry : etfSymbols.entrySet()) {
            String symbol = entry.getKey();
            String name = entry.getValue().toUpperCase();
            
            boolean matchFound = false;
            boolean symbolMatch = false;
            
            // 심볼 매칭 체크
            for (String keyword : keywords) {
                if (symbol.contains(keyword)) {
                    matchFound = true;
                    symbolMatch = true;
                    break;
                }
            }
            
            // 이름 매칭 체크 (심볼 매칭이 없는 경우에만)
            if (!matchFound) {
                for (String keyword : keywords) {
                    if (name.contains(keyword)) {
                        matchFound = true;
                        break;
                    }
                }
            }
            
            if (matchFound) {
                results.add(new EtfSymbolSuggestion(symbol, entry.getValue(), symbolMatch));
            }
        }
        
        // 결과를 심볼 매칭 우선으로 정렬
        results.sort((a, b) -> {
            if (a.isMatchedBySymbol() && !b.isMatchedBySymbol()) return -1;
            if (!a.isMatchedBySymbol() && b.isMatchedBySymbol()) return 1;
            return a.getSymbol().compareTo(b.getSymbol());
        });
        
        return results;
    }
    
    public static boolean symbolExists(String symbol) {
        return etfSymbols.containsKey(symbol.toUpperCase());
    }
    
    public static String getSymbolName(String symbol) {
        return etfSymbols.get(symbol.toUpperCase());
    }
    
    public static class EtfSymbolSuggestion {
        private String symbol;
        private String name;
        private boolean matchedBySymbol;
        
        public EtfSymbolSuggestion(String symbol, String name, boolean matchedBySymbol) {
            this.symbol = symbol;
            this.name = name;
            this.matchedBySymbol = matchedBySymbol;
        }
        
        public String getSymbol() {
            return symbol;
        }
        
        public String getName() {
            return name;
        }
        
        public boolean isMatchedBySymbol() {
            return matchedBySymbol;
        }
    }
}