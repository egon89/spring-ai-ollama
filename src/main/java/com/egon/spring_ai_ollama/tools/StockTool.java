package com.egon.spring_ai_ollama.tools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

import java.util.Objects;

public class StockTool {
  private static final Logger log = LoggerFactory.getLogger(StockTool.class);

  /*
   * The returnDirect=true flag indicates that the output of this tool should be returned directly to the user,
   * rather than being processed further by the chat model. This is useful for tools that provide final answers or
   * results that don't require additional context or formatting.
  */
  @Tool(description = "Trade stocks.Example usage: 'Buy 100 shares of AAPL at market price'")
  String tradeStocks(
      @ToolParam(description = "The stock symbol, e.g. AAPL") String stockSymbol,
      @ToolParam(description = "The number of shares to trade") int quantity,
      @ToolParam(description = "The type of trade, e.g. 'buy' or 'sell'") String tradeType) {
    if (quantity <= 0) {
      log.warn("StockTool invoked with invalid quantity: {}", quantity);
      return "Quantity must be greater than zero.";
    }

    if (Objects.isNull(stockSymbol) || stockSymbol.isBlank()) {
      log.warn("StockTool invoked with invalid stock symbol: '{}'", stockSymbol);
      return "Stock symbol cannot be empty.";
    }

    final var price = Math.round(Math.random() * 1000 * 100.0) / 100.0;

    log.info("StockTool invoked: {} {} shares of {}", tradeType, quantity, stockSymbol);

    return String.format("Executed %s order for %d shares of %s at $%.2f per share",
        tradeType, quantity, stockSymbol, price);
  }
}
