# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

**Ikarus** is a comprehensive AI crypto buddy built as a Spring Boot-based MCP (Model Context Protocol) server. The application provides tools and prompts for crypto market analysis, portfolio management, user memory, news aggregation, transaction simulation, and on-chain analytics. Built with Spring AI, it implements tool callbacks and prompt specifications for AI integration.

## Build and Development Commands

### Build and Run
- `./gradlew build` - Build the project
- `./gradlew bootRun` - Run the Spring Boot application
- `./gradlew test` - Run all tests
- `./gradlew test --tests BoonboomApplicationTests` - Run specific test class

### Development
- `./gradlew compileJava` - Compile Java sources
- `./gradlew clean` - Clean build artifacts

## Architecture

### Core Components

**BoonboomApplication.java** - Main Spring Boot application class that configures:
- Tool callbacks from all services as Spring beans
- MCP prompt specifications for AI integration including Ikarus persona

**Service Layer:**
- **OnChainMarketService.java** - Market data, price tracking, trend analysis
- **UserMemoryService.java** - Persistent user preferences and conversation memory
- **NewsAndSocialService.java** - Crypto news aggregation and social sentiment analysis
- **PortfolioService.java** - Portfolio management, analysis, and recommendations
- **TransactionService.java** - Trade simulation, order management, and profit calculations
- **OnChainAnalyticsService.java** - Whale tracking, network health, DeFi metrics

### Configuration

**application.properties** - Configures:
- MCP server settings (name: ikarus-ai-buddy, version: 1.0.0)
- Server type: SYNC mode
- H2 in-memory database for user memory storage
- JPA/Hibernate configuration
- Enables resource, tool, and prompt change notifications
- Disables web application type (runs as standalone server)

### Technology Stack

- **Java 23** with Spring Boot 3.4.4
- **Spring AI 1.0.0** for MCP server integration
- **Spring Data JPA** for persistence
- **H2 Database** for in-memory storage
- **Jackson** for JSON processing
- **RestClient** for external API calls
- **JUnit 5** for testing
- **Gradle** for build management

### Package Structure

```
boonboom/
├── BoonboomApplication.java              # Main application and bean configuration
├── model/
│   └── UserMemory.java                   # JPA entity for user memory storage
├── repository/
│   └── UserMemoryRepository.java         # Spring Data repository
└── service/
    ├── OnChainMarketService.java         # Market data and price tools
    ├── UserMemoryService.java            # Memory and preference management
    ├── NewsAndSocialService.java         # News and social media analysis
    ├── PortfolioService.java             # Portfolio management tools
    ├── TransactionService.java           # Trading simulation tools
    └── OnChainAnalyticsService.java      # Blockchain analytics tools
```

## Development Notes

- The application runs as a standalone MCP server, not a web application
- Tools are automatically registered through Spring AI's ToolCallbacks
- Prompt specifications include Ikarus persona for personalized AI interactions
- User memory is persisted using H2 database with Spring Data JPA
- External API integration is prepared but currently returns simulated data for demo purposes
- All trading functions are simulation-only for educational purposes
- The application provides 30+ crypto-focused tools covering market analysis, portfolio management, news, and on-chain analytics

## Ikarus Features

### 🤖 AI Personality
- Personalized crypto buddy with memory capabilities
- Friendly, knowledgeable conversation style
- Remembers user preferences and past interactions

### 📊 Market Analysis
- Real-time price data and market trends
- Crypto news aggregation and sentiment analysis
- Market intelligence reports

### 💼 Portfolio Management
- Portfolio tracking and analysis
- Investment recommendations
- Risk assessment and diversification insights

### 💰 Trading Tools (Simulation)
- Order simulation and management
- Profit/loss calculations
- Price alerts and notifications

### 🔗 On-Chain Analytics
- Whale activity monitoring
- Network health analysis
- DeFi protocol metrics
- Token distribution analysis