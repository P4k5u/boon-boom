package boonboom.service;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class TransactionService {
    
    private final Map<String, Map<String, Object>> pendingTransactions = new ConcurrentHashMap<>();
    private final UserMemoryService userMemoryService;
    
    public TransactionService(UserMemoryService userMemoryService) {
        this.userMemoryService = userMemoryService;
    }
    
    @Tool(description = "Simulate cryptocurrency purchase (demo mode - no real transactions)")
    public String simulateBuyOrder(String coinSymbol, Double amount, Double priceLimit, String userId) {
        try {
            String actualUserId = userId != null ? userId : "default_user";
            String symbol = coinSymbol.toUpperCase();
            
            String transactionId = UUID.randomUUID().toString().substring(0, 8);
            
            Map<String, Object> transaction = new HashMap<>();
            transaction.put("type", "BUY");
            transaction.put("symbol", symbol);
            transaction.put("amount", amount);
            transaction.put("priceLimit", priceLimit);
            transaction.put("status", "PENDING");
            transaction.put("timestamp", LocalDateTime.now());
            transaction.put("userId", actualUserId);
            
            pendingTransactions.put(transactionId, transaction);
            
            return "🔄 SIMULATED BUY ORDER CREATED\\n\\n" +
                   "📋 Transaction ID: " + transactionId + "\\n" +
                   "💰 Asset: " + symbol + "\\n" +
                   "📊 Amount: " + amount + " coins\\n" +
                   "💵 Price Limit: $" + (priceLimit != null ? priceLimit : "Market Price") + "\\n" +
                   "📅 Created: " + LocalDateTime.now() + "\\n\\n" +
                   "⚠️ DEMO MODE: This is a simulation for educational purposes.\\n" +
                   "Real trading would require:\\n" +
                   "• Exchange API integration\\n" +
                   "• Proper authentication\\n" +
                   "• Risk management controls\\n" +
                   "• Regulatory compliance\\n\\n" +
                   "Use 'checkTransactionStatus' to view order status.";
                   
        } catch (Exception e) {
            return "Error creating buy order: " + e.getMessage();
        }
    }
    
    @Tool(description = "Simulate cryptocurrency sale (demo mode - no real transactions)")
    public String simulateSellOrder(String coinSymbol, Double amount, Double priceLimit, String userId) {
        try {
            String actualUserId = userId != null ? userId : "default_user";
            String symbol = coinSymbol.toUpperCase();
            
            String transactionId = UUID.randomUUID().toString().substring(0, 8);
            
            Map<String, Object> transaction = new HashMap<>();
            transaction.put("type", "SELL");
            transaction.put("symbol", symbol);
            transaction.put("amount", amount);
            transaction.put("priceLimit", priceLimit);
            transaction.put("status", "PENDING");
            transaction.put("timestamp", LocalDateTime.now());
            transaction.put("userId", actualUserId);
            
            pendingTransactions.put(transactionId, transaction);
            
            return "🔄 SIMULATED SELL ORDER CREATED\\n\\n" +
                   "📋 Transaction ID: " + transactionId + "\\n" +
                   "💰 Asset: " + symbol + "\\n" +
                   "📊 Amount: " + amount + " coins\\n" +
                   "💵 Price Limit: $" + (priceLimit != null ? priceLimit : "Market Price") + "\\n" +
                   "📅 Created: " + LocalDateTime.now() + "\\n\\n" +
                   "⚠️ DEMO MODE: This is a simulation for educational purposes.\\n" +
                   "Use 'checkTransactionStatus' to view order status.";
                   
        } catch (Exception e) {
            return "Error creating sell order: " + e.getMessage();
        }
    }
    
    @Tool(description = "Check the status of a transaction")
    public String checkTransactionStatus(String transactionId) {
        try {
            Map<String, Object> transaction = pendingTransactions.get(transactionId);
            
            if (transaction == null) {
                return "❌ Transaction not found: " + transactionId;
            }
            
            return "📋 TRANSACTION STATUS\\n\\n" +
                   "🆔 ID: " + transactionId + "\\n" +
                   "🔄 Type: " + transaction.get("type") + "\\n" +
                   "💰 Asset: " + transaction.get("symbol") + "\\n" +
                   "📊 Amount: " + transaction.get("amount") + " coins\\n" +
                   "💵 Price Limit: $" + transaction.get("priceLimit") + "\\n" +
                   "📈 Status: " + transaction.get("status") + "\\n" +
                   "📅 Created: " + transaction.get("timestamp") + "\\n\\n" +
                   "⚠️ This is a simulated transaction.";
                   
        } catch (Exception e) {
            return "Error checking transaction status: " + e.getMessage();
        }
    }
    
    @Tool(description = "Cancel a pending transaction")
    public String cancelTransaction(String transactionId) {
        try {
            Map<String, Object> transaction = pendingTransactions.get(transactionId);
            
            if (transaction == null) {
                return "❌ Transaction not found: " + transactionId;
            }
            
            String status = (String) transaction.get("status");
            if (!"PENDING".equals(status)) {
                return "❌ Cannot cancel transaction in " + status + " status";
            }
            
            transaction.put("status", "CANCELLED");
            
            return "✅ Transaction " + transactionId + " has been cancelled.";
            
        } catch (Exception e) {
            return "Error cancelling transaction: " + e.getMessage();
        }
    }
    
    @Tool(description = "Get transaction history for user")
    public String getTransactionHistory(String userId) {
        try {
            String actualUserId = userId != null ? userId : "default_user";
            
            StringBuilder history = new StringBuilder("📊 TRANSACTION HISTORY\\n\\n");
            
            boolean hasTransactions = false;
            for (Map.Entry<String, Map<String, Object>> entry : pendingTransactions.entrySet()) {
                Map<String, Object> transaction = entry.getValue();
                if (actualUserId.equals(transaction.get("userId"))) {
                    hasTransactions = true;
                    history.append("🆔 ").append(entry.getKey())
                           .append(" | ").append(transaction.get("type"))
                           .append(" ").append(transaction.get("symbol"))
                           .append(" | ").append(transaction.get("status"))
                           .append("\\n");
                }
            }
            
            if (!hasTransactions) {
                return "📊 No transaction history found.\\n\\nStart trading to see your transaction history here!";
            }
            
            history.append("\\n💡 Use 'checkTransactionStatus <ID>' for detailed info.");
            return history.toString();
            
        } catch (Exception e) {
            return "Error fetching transaction history: " + e.getMessage();
        }
    }
    
    @Tool(description = "Set up price alerts for cryptocurrencies")
    public String setPriceAlert(String coinSymbol, Double targetPrice, String alertType, String userId) {
        try {
            String actualUserId = userId != null ? userId : "default_user";
            String symbol = coinSymbol.toUpperCase();
            String type = alertType != null ? alertType.toUpperCase() : "ABOVE";
            
            String alertKey = "price_alert_" + symbol + "_" + type;
            String alertValue = "Alert when " + symbol + " goes " + type.toLowerCase() + " $" + targetPrice;
            
            userMemoryService.rememberUserInfo(alertKey, alertValue, actualUserId);
            
            return "🚨 PRICE ALERT SET\\n\\n" +
                   "💰 Asset: " + symbol + "\\n" +
                   "🎯 Target Price: $" + targetPrice + "\\n" +
                   "🔔 Alert Type: " + type + "\\n" +
                   "👤 User: " + actualUserId + "\\n\\n" +
                   "✅ Alert has been saved to your memory.\\n" +
                   "💡 In a real implementation, this would:\\n" +
                   "• Monitor live price feeds\\n" +
                   "• Send notifications when triggered\\n" +
                   "• Track alert history\\n\\n" +
                   "Use 'getAllUserMemories' to view all your alerts.";
                   
        } catch (Exception e) {
            return "Error setting price alert: " + e.getMessage();
        }
    }
    
    @Tool(description = "Calculate potential profit/loss for a trade")
    public String calculateTradeProfit(String coinSymbol, Double buyPrice, Double sellPrice, Double amount) {
        try {
            String symbol = coinSymbol.toUpperCase();
            
            if (buyPrice <= 0 || sellPrice <= 0 || amount <= 0) {
                return "❌ Invalid input: All values must be positive";
            }
            
            double grossProfit = (sellPrice - buyPrice) * amount;
            double profitPercentage = ((sellPrice - buyPrice) / buyPrice) * 100;
            
            // Simulate trading fees (0.1% each for buy and sell)
            double tradingFees = (buyPrice * amount * 0.001) + (sellPrice * amount * 0.001);
            double netProfit = grossProfit - tradingFees;
            
            String profitStatus = netProfit > 0 ? "📈 PROFIT" : netProfit < 0 ? "📉 LOSS" : "➖ BREAK-EVEN";
            
            return "💰 TRADE CALCULATION for " + symbol + "\\n\\n" +
                   "📊 TRADE DETAILS:\\n" +
                   "• Buy Price: $" + String.format("%.2f", buyPrice) + "\\n" +
                   "• Sell Price: $" + String.format("%.2f", sellPrice) + "\\n" +
                   "• Amount: " + amount + " coins\\n\\n" +
                   "💵 FINANCIAL ANALYSIS:\\n" +
                   "• Gross P&L: $" + String.format("%.2f", grossProfit) + "\\n" +
                   "• Trading Fees: $" + String.format("%.2f", tradingFees) + "\\n" +
                   "• Net P&L: $" + String.format("%.2f", netProfit) + "\\n" +
                   "• Return: " + String.format("%.2f", profitPercentage) + "%\\n\\n" +
                   "🎯 RESULT: " + profitStatus + "\\n\\n" +
                   "💡 Note: Calculation includes estimated 0.1% trading fees.";
                   
        } catch (Exception e) {
            return "Error calculating trade profit: " + e.getMessage();
        }
    }
}