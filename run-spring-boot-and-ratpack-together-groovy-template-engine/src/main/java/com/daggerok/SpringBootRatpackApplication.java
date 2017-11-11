package com.daggerok;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import ratpack.exec.Blocking;
import ratpack.groovy.template.MarkupTemplateModule;
import ratpack.handling.Handler;
import ratpack.spring.config.EnableRatpack;

import java.io.Serializable;
import java.util.HashMap;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static ratpack.groovy.Groovy.groovyMarkupTemplate;

@RepositoryRestResource
interface UserRepository extends MongoRepository<User, String> {}

@Data
@Document
@NoArgsConstructor
@RequiredArgsConstructor
class User implements Serializable {

  @Id String id;
  @NonNull String username;
}

@Slf4j
@EnableRatpack
@SpringBootApplication
public class SpringBootRatpackApplication extends RepositoryRestConfigurerAdapter {

  public static void main(String[] args) {
    SpringApplication.run(SpringBootRatpackApplication.class, args);
  }

  /*
    @Bean public Handler handler() {
        return context -> context.render("Hello World");
    }
  */

  @Bean // requires to make groovyMarkupTemplate works
  MarkupTemplateModule markupTemplate() {
    return new MarkupTemplateModule();
  }

  @Bean
  Handler handler(final UserRepository userRepository) {
    //return context -> context.render("index.html");
    //val model = new HashMap<String, Object>();
    return context -> {
      Blocking.get(() -> {
        final HashMap<String, Object> model = new HashMap<String, Object>();
        model.put("message", "This is a groovy template engine from ratpack!");
        model.put("users", userRepository.findAll()
                                         .stream()
                                         .map(User::toString)
                                         .collect(toList()));
        return model;
      }).then(model -> context.render(
          groovyMarkupTemplate(model, "index.tpl", "text/html;charset=UTF-8")
      ));
    };
  }

  @Override
  public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
    config.exposeIdsFor(User.class);
  }

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

  @Controller
  @RequiredArgsConstructor(onConstructor = @__(@Autowired))
  static class IndexController {

    final UserRepository userRepository;

    @RequestMapping("/")
    public String index(Model model) {
      model.addAttribute("message", "this is a groovy template engine from spting-boot!");
      model.addAttribute("users", userRepository.findAll()
                                                .stream()
                                                .map(User::toString)
                                                .collect(toList()));
      return "index";
    }
  }
}
