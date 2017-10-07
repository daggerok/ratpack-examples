package daggerok;

import daggerok.domain.Order;
import daggerok.services.PizzaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import ratpack.func.Action;
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
            "actions": [
                {
                    "GET": "http://localhost:5050"
                },
                {
                    "GET": "http://localhost:5050/get-planet-summary"
                },
                {
                    "GET": "http://localhost:5050/count-standing-orders"
                },
                {
                    "POST": "http://localhost:5050/place-order"
                },
                {
                    "POST": "http://localhost:5050/take-orders"
                }
            ]
        }
        */

        .get("", ctx ->
            render(ctx, singletonMap("actions", asList(
                singletonMap("GET", "http://localhost:5050"),
                singletonMap("GET", "http://localhost:5050/get-planet-summary"),
                singletonMap("GET", "http://localhost:5050/count-standing-orders"),
                singletonMap("POST", "http://localhost:5050/place-order"),
                singletonMap("POST", "http://localhost:5050/take-orders")
            )))
        )


        /*
        http get http://localhost:5050/get-planet-summary

        [
            {
                "counter": 2,
                "name": "Earth"
            }
        ]
         */

        .get("get-planet-summary", ctx ->
            render(ctx, pizzaService.getPlanetSummary())
        )


        /*
        http get http://localhost:5050/count-standing-orders

        2
        */

        .get("count-standing-orders", ctx ->
            render(ctx, pizzaService.countStandingOrders())
        )


        /*
        http post http://localhost:5050/place-order variant=HAWAII size=BIG planet=Earth

        "OK"
        */

        .post("place-order", ctx -> {
          //ctx.parse(Order.class)
          ctx.parse(fromJson(Order.class))
             .onError(throwable -> log.error("oops: {}", throwable.getLocalizedMessage(), throwable))
             .then(order -> {
               log.info("received order: {}", order);
               pizzaService.placeOrder(order.getPlanet(), order.getVariant(), order.getSize());
               render(ctx, "OK");
             });
        })


        /*
        http post http://localhost:5050/take-orders

        [
            {
                "planet": "Earth",
                "size": "BIG",
                "variant": "HAWAII"
            },
            {
                "planet": "Earth",
                "size": "BIG",
                "variant": "HAWAII"
            }
        ]
        */

        .post("take-orders", ctx ->
            render(ctx, pizzaService.takeOrdersFromBestPlanet())
        )
        ;
  }

  public static void main(String[] args) {
    SpringApplication.run(PizzaServiceApplication.class, args);
  }
}
