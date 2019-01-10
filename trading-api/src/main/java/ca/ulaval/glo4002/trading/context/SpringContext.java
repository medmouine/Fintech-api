package ca.ulaval.glo4002.trading.context;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class SpringContext {
  @Bean
  public RestTemplate getRestTemplate() {
    return new RestTemplate();
  }
}
