package daggerok;

import daggerok.user.User;
import daggerok.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.stream.Stream;

@Slf4j
@Configuration
public class TestDataConfig {

  @Bean
  CommandLineRunner runner(final UserRepository userRepository) {

    Stream.of("Max", "Fax", "Bax")
          .map(User::new)
          .forEach(userRepository::save);

    return args -> userRepository.findAll()
                                 .stream()
                                 .map(Object::toString)
                                 .forEach(log::info);
  }
}
