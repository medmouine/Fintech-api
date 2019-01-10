package ca.ulaval.glo4002.trading;

import org.springframework.boot.builder.SpringApplicationBuilder;

public class TradingServer implements Runnable {

  private static final String CONFIG_NAME = "trading-api";

  private final String[] args;

  public TradingServer(final String[] args) {
    this.args = args;
  }

  public static void main(final String[] args) {
    new TradingServer(args).run();
  }

  @Override
  public void run() {
    new SpringApplicationBuilder(TradingSpringApplication.class)
            .properties(String.format("spring.config.name:%s", CONFIG_NAME))
            .build()
            .run(this.args);
  }
}
