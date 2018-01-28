package daggerok;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import ratpack.exec.Promise;
import ratpack.http.client.HttpClient;
import ratpack.http.client.ReceivedResponse;
import ratpack.jackson.Jackson;
import ratpack.rx.RxRatpack;
import ratpack.server.RatpackServer;
import rx.Observable;

import java.net.InetAddress;
import java.net.URI;
import java.time.Duration;
import java.util.Map;
import java.util.function.Function;

import static java.lang.String.format;
import static java.util.Collections.singletonMap;
import static ratpack.http.MediaType.APPLICATION_JSON;

@Slf4j
public class App {

  @SneakyThrows
  public static void main(String[] args) {

    final int port = 8080;
    final String host = "0.0.0.0";
    final HttpClient httpClient = HttpClient.of(client -> client.readTimeout(Duration.ofMinutes(2)));
    final Function<Long, URI> uri = n -> URI.create(format("http://%s:%d/api/v1/fibonacci/%d", host, port, n));
    final Function<Long, Promise<ReceivedResponse>> get = n -> httpClient.get(uri.apply(n));
    final Function<Long, Promise<Long>> fibo = n -> get.apply(n)
                                                       .map(f -> f.getBody().getText())
                                                       .map(Long::valueOf);
    RatpackServer.start(
        server -> server.serverConfig(
            builder -> builder
                .port(port)
                .address(InetAddress.getByName(host))
                .development(false)
                .threads(1)
                .build()
        ).handlers(chain -> chain
            .get(ctx -> {
              final String url = uri.apply(15L).toASCIIString();
              final String message = "Hey! Do you heard about recursive REST Fibonacci? checkout";
              final Map<String, String> body = singletonMap(message, url);
              ctx.getResponse()
                 .contentType(APPLICATION_JSON);
              ctx.render(Jackson.json(body));
            })
            .prefix("api/v1/fibonacci", fib -> fib
                .get(":number", ctx -> {
                  final String string = ctx.getPathTokens().get("number");
                  final Long number = Long.valueOf(string);
                  if (number <= 0) ctx.render(Jackson.json(0));
                  else if (number <= 2) ctx.render(Jackson.json(1));
                  else {

                    final Promise<Long> fib_1 = fibo.apply(number - 1);
                    final Promise<Long> fib_2 = fibo.apply(number - 2);
                    final Observable<Long> fibo1 = RxRatpack.observe(fib_1);
                    final Observable<Long> fibo2 = RxRatpack.observe(fib_2);

                    Observable.zip(fibo1, fibo2, (f1, f2) -> f1 + f2)
                              .subscribe(result -> ctx.render(Jackson.json(result)));
                  }
                }))
        )
    ).start();

    log.info("yo! do you want some recursive Fibonacci? :D");
  }
}
