package daggerok;

import daggerok.domain.Order;
import daggerok.services.PizzaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import ratpack.func.Action;
import ratpack.handling.ByContentSpec;
import ratpack.handling.Chain;
import ratpack.handling.Context;
import ratpack.http.MutableHeaders;
import ratpack.jackson.Jackson;
import ratpack.spring.config.EnableRatpack;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonMap;
import static ratpack.jackson.Jackson.fromJson;

@Slf4j
@EnableRatpack
@SpringBootApplication
public class PizzaServiceApplication {

  private static MutableHeaders setHeaders(final Context ctx) {

    return ctx.getResponse().getHeaders()
              .set("Access-Control-Allow-Origin", "*")
              .set("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
              .set("Access-Control-Allow-Headers", "x-requested-with, origin, content-type, accept");
  }

  private static <T> void render(final Context ctx, T object) {
    setHeaders(ctx);
    ctx.render(Jackson.json(object));
  }

  /*
  read more: https://ratpack.io/manual/current/jackson.html
   */
  @Bean
  Action<Chain> chainAction(final PizzaService pizzaService) {

    return chain -> chain

        /*
        http :5050

        {
            "uris": [
                "http://localhost:5050/orders"
            ]
        }
        */

        .get("", ctx ->
            render(ctx, singletonMap("uris", asList(
                singletonMap("GET", "http://localhost:5050"),
                singletonMap("GET", "http://localhost:5050/get-orders"),
                singletonMap("POST", "http://localhost:5050/save-order")
            )))
        )


        /*
        http get http://localhost:5050/get-orders variant=HAWAII size=BIG planet=Earth

        [
            {
                "counter": 4,
                "name": "Earth"
            }
        ]
         */

        .get("get-orders", ctx ->
            render(ctx, pizzaService.getPlanetSummary())
        )


        /*
        http post http://localhost:5050/save-order variant=HAWAII size=BIG planet=Earth

        "OK"
         */

        .post("save-order", ctx -> {
          //ctx.parse(Order.class)
          ctx.parse(fromJson(Order.class))
             .onError(throwable -> log.error("oops: {}", throwable.getLocalizedMessage(), throwable))
             .then(order -> {
               log.info("received order: {}", order);
               pizzaService.placeOrder(order.getPlanet(), order.getVariant(), order.getSize());
               render(ctx, "OK");
             });
        })
        ;
  }

  public static void main(String[] args) {
    SpringApplication.run(PizzaServiceApplication.class, args);
  }
}
