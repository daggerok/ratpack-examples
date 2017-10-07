package daggerok;

import daggerok.config.RestRepositoryConfig;
import daggerok.config.WebInterceptorConfig;
import daggerok.data.Item;
import daggerok.data.ItemRestRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import ratpack.func.Action;
import ratpack.handling.Chain;
import ratpack.http.MutableHeaders;
import ratpack.jackson.Jackson;
import ratpack.spring.config.EnableRatpack;

import java.time.LocalDateTime;
import java.util.stream.Stream;

@EnableRatpack
@SpringBootApplication
@Import({ RestRepositoryConfig.class, WebInterceptorConfig.class })
public class RetpackJwtApplication {

    @Bean
    CommandLineRunner testData(ItemRestRepository items) {

        return args -> Stream.of("1st", "2nd", "3rd")
                .map(Item::of)
                .forEach(items::save);
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(RetpackJwtApplication.class, args);
    }

    @Configuration
    public static class SpringRatpackConfig {

        @Bean
        public String message() {
            return "holla, ratpack!";
        }

        @Bean
        Action<Chain> chainAction() {

            return chain -> chain
                    .get(ctx -> {
                        MutableHeaders headers = ctx.getResponse().getHeaders();
                        headers.set("Access-Control-Allow-Origin", "*");
                        headers.set("Access-Control-Allow-Headers", "x-requested-with, origin, content-type, accept");
                        ctx.render(Jackson.json(message()));
                    })
                    .get("rat", ctx -> {
                        ctx.getResponse().getHeaders().set("Access-Control-Allow-Origin", "*");
                        ctx.render(Jackson.json(LocalDateTime.now() + " is " + System.currentTimeMillis()));
                    });
        }
    }
}
