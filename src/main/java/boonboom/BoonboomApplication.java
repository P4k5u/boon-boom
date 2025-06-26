package boonboom;

import boonboom.service.OnChainMarketService;
import boonboom.service.UserMemoryService;
import boonboom.service.NewsAndSocialService;
import boonboom.service.PortfolioService;
import boonboom.service.TransactionService;
import boonboom.service.OnChainAnalyticsService;
import io.modelcontextprotocol.server.McpServerFeatures;
import org.springframework.ai.support.ToolCallbacks;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;
import java.util.ArrayList;

@SpringBootApplication
public class BoonboomApplication {

	public static void main(String[] args) {
		SpringApplication.run(BoonboomApplication.class, args);
	}

	@Bean
	public List<ToolCallback> ikarusTools(
			OnChainMarketService onChainMarketService,
			UserMemoryService userMemoryService,
			NewsAndSocialService newsAndSocialService,
			PortfolioService portfolioService,
			TransactionService transactionService,
			OnChainAnalyticsService onChainAnalyticsService) {
		
		List<ToolCallback> tools = new ArrayList<>();
		tools.addAll(List.of(ToolCallbacks.from(onChainMarketService)));
		tools.addAll(List.of(ToolCallbacks.from(userMemoryService)));
		tools.addAll(List.of(ToolCallbacks.from(newsAndSocialService)));
		tools.addAll(List.of(ToolCallbacks.from(portfolioService)));
		tools.addAll(List.of(ToolCallbacks.from(transactionService)));
		tools.addAll(List.of(ToolCallbacks.from(onChainAnalyticsService)));
		
		return tools;
	}

	@Bean
	public List<McpServerFeatures.SyncPromptSpecification> ikarusPrompts(OnChainMarketService onChainMarketService) {
		return List.of(
			onChainMarketService.getIkarusPromptSpecification(),
			onChainMarketService.getGreetingPromptSpecification()
		);
	}
}
