package boonboom.service;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class PortfolioService {
    
    private final Map<String, Map<String, Double>> userPortfolios = new ConcurrentHashMap<>();
    private final OnChainMarketService marketService;
    
    public PortfolioService(OnChainMarketService marketService) {
        this.marketService = marketService;
    }
    
    @Tool(description = "Add or update a cryptocurrency holding in user's portfolio")
    public String addToPortfolio(String coinSymbol, Double amount, String userId) {
        try {
            String actualUserId = userId != null ? userId : "default_user";
            String symbol = coinSymbol.toUpperCase();
            
            userPortfolios.computeIfAbsent(actualUserId, k -> new HashMap<>());
            Map<String, Double> portfolio = userPortfolios.get(actualUserId);
            
            Double currentAmount = portfolio.getOrDefault(symbol, 0.0);
            Double newAmount = currentAmount + amount;
            
            if (newAmount <= 0) {
                portfolio.remove(symbol);
                return "✓ Removed " + symbol + " from portfolio";
            } else {
                portfolio.put(symbol, newAmount);
                return "✓ Portfolio updated: " + symbol + " = " + newAmount + " coins";
            }
            
        } catch (Exception e) {
            return "Error updating portfolio: " + e.getMessage();
        }
    }
    
    @Tool(description = "View user's complete cryptocurrency portfolio")
    public String viewPortfolio(String userId) {
        try {
            String actualUserId = userId != null ? userId : "default_user";
            Map<String, Double> portfolio = userPortfolios.get(actualUserId);
            
            if (portfolio == null || portfolio.isEmpty()) {
                return "📊 Your portfolio is empty. Add some cryptocurrencies to get started!";
            }
            
            StringBuilder result = new StringBuilder("📊 YOUR CRYPTO PORTFOLIO\\n\\n");
            double totalValue = 0.0;
            
            for (Map.Entry<String, Double> entry : portfolio.entrySet()) {
                String symbol = entry.getKey();
                Double amount = entry.getValue();
                
                result.append("💰 ").append(symbol).append(": ").append(amount).append(" coins\\n");
                
                try {
                    String priceInfo = marketService.getCryptoPriceInfo(symbol.toLowerCase());
                    if (priceInfo.contains("usd")) {
                        result.append("   └─ Market data: ").append(priceInfo).append("\\n");
                    }
                } catch (Exception e) {
                    result.append("   └─ Price data unavailable\\n");
                }
                result.append("\\n");
            }
            
            result.append("📈 Portfolio contains ").append(portfolio.size()).append(" different cryptocurrencies\\n");
            result.append("\\n💡 Tip: Use 'analyzePortfolio' to get insights and recommendations!");
            
            return result.toString();
            
        } catch (Exception e) {
            return "Error viewing portfolio: " + e.getMessage();
        }
    }
    
    @Tool(description = "Analyze user's portfolio and provide insights")
    public String analyzePortfolio(String userId) {
        try {
            String actualUserId = userId != null ? userId : "default_user";
            Map<String, Double> portfolio = userPortfolios.get(actualUserId);
            
            if (portfolio == null || portfolio.isEmpty()) {
                return "❌ No portfolio found. Add some cryptocurrencies first!";
            }
            
            StringBuilder analysis = new StringBuilder("📊 PORTFOLIO ANALYSIS\\n\\n");
            
            analysis.append("🔍 COMPOSITION:\\n");
            for (Map.Entry<String, Double> entry : portfolio.entrySet()) {
                String symbol = entry.getKey();
                analysis.append("• ").append(symbol).append(": ").append(entry.getValue()).append(" coins\\n");
            }
            
            analysis.append("\\n📈 DIVERSIFICATION ANALYSIS:\\n");
            if (portfolio.size() == 1) {
                analysis.append("⚠️ Single asset portfolio - Consider diversifying\\n");
            } else if (portfolio.size() <= 3) {
                analysis.append("⚡ Low diversification - Good for focused strategy\\n");
            } else if (portfolio.size() <= 7) {
                analysis.append("✅ Well diversified portfolio\\n");
            } else {
                analysis.append("🎯 Highly diversified - Consider simplifying\\n");
            }
            
            analysis.append("\\n💡 RECOMMENDATIONS:\\n");
            if (portfolio.containsKey("BTC")) {
                analysis.append("• Strong foundation with Bitcoin holdings\\n");
            } else {
                analysis.append("• Consider adding Bitcoin for stability\\n");
            }
            
            if (portfolio.containsKey("ETH")) {
                analysis.append("• Good exposure to smart contract platforms\\n");
            } else {
                analysis.append("• Consider adding Ethereum for DeFi exposure\\n");
            }
            
            analysis.append("• Monitor your holdings regularly\\n");
            analysis.append("• Consider rebalancing quarterly\\n");
            analysis.append("• Set up price alerts for major movements\\n");
            
            analysis.append("\\n🎯 RISK ASSESSMENT: ");
            if (portfolio.size() <= 2) {
                analysis.append("HIGH (Limited diversification)\\n");
            } else if (portfolio.size() <= 5) {
                analysis.append("MEDIUM (Balanced approach)\\n");
            } else {
                analysis.append("LOW (Well diversified)\\n");
            }
            
            return analysis.toString();
            
        } catch (Exception e) {
            return "Error analyzing portfolio: " + e.getMessage();
        }
    }
    
    @Tool(description = "Get portfolio performance and recommendations")
    public String getPortfolioRecommendations(String userId) {
        try {
            String actualUserId = userId != null ? userId : "default_user";
            Map<String, Double> portfolio = userPortfolios.get(actualUserId);
            
            if (portfolio == null || portfolio.isEmpty()) {
                return "❌ No portfolio found. Add some cryptocurrencies first!";
            }
            
            return "🎯 PERSONALIZED RECOMMENDATIONS\\n\\n" +
                   "📊 Based on your current portfolio analysis:\\n\\n" +
                   "🔥 IMMEDIATE ACTIONS:\\n" +
                   "• Review allocation percentages\\n" +
                   "• Check for overexposure to any single asset\\n" +
                   "• Consider taking profits on outperforming assets\\n\\n" +
                   "📈 GROWTH OPPORTUNITIES:\\n" +
                   "• Explore emerging DeFi protocols\\n" +
                   "• Consider Layer 2 solutions\\n" +
                   "• Look into staking opportunities\\n\\n" +
                   "⚖️ RISK MANAGEMENT:\\n" +
                   "• Set stop-loss orders for volatile positions\\n" +
                   "• Maintain emergency fund outside crypto\\n" +
                   "• Regular portfolio rebalancing\\n\\n" +
                   "💡 MARKET INSIGHTS:\\n" +
                   "• Current market favors established coins\\n" +
                   "• DeFi sector showing strong fundamentals\\n" +
                   "• Regulatory clarity improving adoption\\n\\n" +
                   "🎯 NEXT STEPS:\\n" +
                   "1. Use 'analyzeMarketSentiment' for specific coins\\n" +
                   "2. Set up price alerts for key positions\\n" +
                   "3. Review and adjust monthly\\n\\n" +
                   "Note: This is educational content, not financial advice.";
                   
        } catch (Exception e) {
            return "Error generating recommendations: " + e.getMessage();
        }
    }
    
    @Tool(description = "Remove a cryptocurrency from the portfolio")
    public String removeFromPortfolio(String coinSymbol, String userId) {
        try {
            String actualUserId = userId != null ? userId : "default_user";
            String symbol = coinSymbol.toUpperCase();
            
            Map<String, Double> portfolio = userPortfolios.get(actualUserId);
            if (portfolio == null || !portfolio.containsKey(symbol)) {
                return "❌ " + symbol + " not found in your portfolio";
            }
            
            portfolio.remove(symbol);
            return "✓ Removed " + symbol + " from your portfolio";
            
        } catch (Exception e) {
            return "Error removing from portfolio: " + e.getMessage();
        }
    }
}