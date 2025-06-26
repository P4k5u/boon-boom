package boonboom.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.modelcontextprotocol.server.McpServerFeatures;
import io.modelcontextprotocol.spec.McpSchema;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Service
public class OnChainMarketService {

    private final RestClient coinGeckoClient;
    private final RestClient newsClient;
    private final ObjectMapper objectMapper;
    private final Map<String, Object> userMemory;

    public OnChainMarketService() {
        this.coinGeckoClient = RestClient.builder()
                .baseUrl("https://api.coingecko.com/api/v3/")
                .defaultHeader("Accept", "application/json")
                .build();
        this.newsClient = RestClient.builder()
                .baseUrl("https://newsapi.org/v2/")
                .defaultHeader("Accept", "application/json")
                .build();
        this.objectMapper = new ObjectMapper();
        this.userMemory = new HashMap<>();
    }

    @Tool(description = "Get current market price and basic info for a cryptocurrency")
    public String getCryptoPriceInfo(String coinId) {
        try {
            String response = coinGeckoClient.get()
                    .uri("/simple/price?ids={coinId}&vs_currencies=usd&include_24hr_change=true&include_market_cap=true", coinId)
                    .retrieve()
                    .body(String.class);
            return response != null ? response : "Unable to fetch price data for " + coinId;
        } catch (Exception e) {
            return "Error fetching price for " + coinId + ": " + e.getMessage();
        }
    }

    @Tool(description = "Get trending cryptocurrencies")
    public String getTrendingCryptos() {
        try {
            String response = coinGeckoClient.get()
                    .uri("/search/trending")
                    .retrieve()
                    .body(String.class);
            return response != null ? response : "Unable to fetch trending cryptocurrencies";
        } catch (Exception e) {
            return "Error fetching trending cryptos: " + e.getMessage();
        }
    }

    @Tool(description = "Get market data for top cryptocurrencies")
    public String getTopCryptosByMarketCap(Integer limit) {
        try {
            int actualLimit = limit != null ? Math.min(limit, 100) : 10;
            String response = coinGeckoClient.get()
                    .uri("/coins/markets?vs_currency=usd&order=market_cap_desc&per_page={limit}&page=1", actualLimit)
                    .retrieve()
                    .body(String.class);
            return response != null ? response : "Unable to fetch top cryptocurrencies";
        } catch (Exception e) {
            return "Error fetching top cryptos: " + e.getMessage();
        }
    }

    @Tool(description = "Get detailed information about a specific cryptocurrency")
    public String getCryptoDetails(String coinId) {
        try {
            String response = coinGeckoClient.get()
                    .uri("/coins/{coinId}?localization=false&tickers=false&market_data=true&community_data=true&developer_data=false", coinId)
                    .retrieve()
                    .body(String.class);
            return response != null ? response : "Unable to fetch details for " + coinId;
        } catch (Exception e) {
            return "Error fetching details for " + coinId + ": " + e.getMessage();
        }
    }

    @Tool(description = "Remember user preferences and information")
    public String rememberUserInfo(String key, String value) {
        userMemory.put(key, value);
        return "Remembered: " + key + " = " + value;
    }

    @Tool(description = "Recall user preferences and information")
    public String getUserInfo(String key) {
        Object value = userMemory.get(key);
        return value != null ? "I remember: " + key + " = " + value : "I don't have any information about " + key;
    }

    @Tool(description = "Get cryptocurrency news from various sources")
    public String getCryptoNews(String query) {
        try {
            String searchQuery = query != null ? query : "cryptocurrency bitcoin ethereum";
            return "Latest crypto news for: " + searchQuery + "\n" +
                   "[News functionality would require NewsAPI key - currently returning placeholder]\n" +
                   "Sample headlines:\n" +
                   "• Bitcoin reaches new milestone as institutional adoption grows\n" +
                   "• Ethereum network upgrade shows promising results\n" +
                   "• DeFi sector continues expansion with new protocols";
        } catch (Exception e) {
            return "Error fetching crypto news: " + e.getMessage();
        }
    }

    @Tool(description = "Analyze market sentiment and provide investment insights")
    public String analyzeMarketSentiment(String coinId) {
        try {
            String priceData = getCryptoPriceInfo(coinId);
            return "Market Analysis for " + coinId + ":\n" +
                   "Price Data: " + priceData + "\n" +
                   "\nSentiment Analysis:\n" +
                   "• Technical indicators suggest [analysis would be implemented]\n" +
                   "• Market momentum appears [bullish/bearish based on data]\n" +
                   "• Risk assessment: [low/medium/high]\n" +
                   "\nNote: This is a basic implementation. Full analysis would include more sophisticated algorithms.";
        } catch (Exception e) {
            return "Error analyzing market sentiment: " + e.getMessage();
        }
    }

    public McpServerFeatures.SyncPromptSpecification getIkarusPromptSpecification() {
        var prompt = new McpSchema.Prompt(
                "ikarus_persona",
                "Ikarus - Your AI crypto buddy with personality and memory",
                List.of(
                    new McpSchema.PromptArgument("user_name", "The user's name", false),
                    new McpSchema.PromptArgument("context", "Current conversation context", false)
                ));

        var promptSpecification = new McpServerFeatures.SyncPromptSpecification(prompt, (exchange, getPromptRequest) -> {
            String userName = (String) getPromptRequest.arguments().getOrDefault("user_name", "friend");
            String context = (String) getPromptRequest.arguments().getOrDefault("context", "general conversation");
            
            var systemMessage = new McpSchema.PromptMessage(
                    McpSchema.Role.ASSISTANT,
                    new McpSchema.TextContent("You are Ikarus, an AI crypto buddy with the following characteristics:\n\n" +
                        "PERSONALITY:\n" +
                        "• Friendly, knowledgeable, and genuinely interested in helping with crypto matters\n" +
                        "• Remember past conversations and user preferences\n" +
                        "• Professional when discussing investments, casual when chatting\n" +
                        "• Proactive in identifying opportunities aligned with user preferences\n\n" +
                        "CAPABILITIES:\n" +
                        "• Market analysis and trend identification\n" +
                        "• Portfolio management suggestions\n" +
                        "• News aggregation and sentiment analysis\n" +
                        "• Educational content about crypto and DeFi\n\n" +
                        "CONVERSATION STYLE:\n" +
                        "• Use tools to provide real, current market data\n" +
                        "• Remember user preferences using the memory tools\n" +
                        "• Provide actionable insights, not just information\n" +
                        "• Ask clarifying questions to better assist\n\n" +
                        "Current user: " + userName + "\n" +
                        "Context: " + context + "\n\n" +
                        "Greet the user warmly and ask how you can help with their crypto journey today!"));

            return new McpSchema.GetPromptResult("Ikarus AI crypto buddy persona", List.of(systemMessage));
        });

        return promptSpecification;
    }

    public McpServerFeatures.SyncPromptSpecification getGreetingPromptSpecification() {
        var prompt = new McpSchema.Prompt(
                "greeting",
                "A friendly greeting prompt",
                List.of(new McpSchema.PromptArgument("name", "The name to greet", true)
                ));

        var promptSpecification = new McpServerFeatures.SyncPromptSpecification(prompt, (exchange, getPromptRequest) -> {
            String nameArgument = (String) getPromptRequest.arguments().get("name");
            if (nameArgument == null) {
                nameArgument = "friend";
            }
            var userMessage = new McpSchema.PromptMessage(
                    McpSchema.Role.USER,
                    new McpSchema.TextContent("Hello " + nameArgument + "! How can I assist you today?"));

            return new McpSchema.GetPromptResult("A personalized greeting message", List.of(userMessage));
        });

        return promptSpecification;
    }
}
