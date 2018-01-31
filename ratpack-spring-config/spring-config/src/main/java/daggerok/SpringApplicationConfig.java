package daggerok;

import daggerok.service.MessageService;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;

@SpringBootApplication
public class SpringApplicationConfig {

  public static void main(String[] args) {
    // stub
  }

  @Bean
  public MessageService upperCaseMessageServiceImpl() {
    return source -> {
      requireNonNull(source, "source may not be null");
      return format("upper: %s", source.toUpperCase());
    };
  }
}
