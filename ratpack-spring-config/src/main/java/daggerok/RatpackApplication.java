package daggerok;

import daggerok.service.MessageService;
import lombok.SneakyThrows;
import ratpack.server.RatpackServer;
import ratpack.spring.Spring;

public class RatpackApplication {

  @SneakyThrows
  public static void main(String[] args) {
    RatpackServer.start(ratpackServerSpec -> ratpackServerSpec
        .registry(Spring.spring(SpringApplicationConfig.class))
        .handlers(chain -> chain
            .get(ctx -> {

              final MessageService messageService = ctx.get(MessageService.class);
              final String helloMessage = messageService.upper("Hello!");

              ctx.render(helloMessage);
            })
            .get(":message", ctx -> {

              final MessageService messageService = ctx.get(MessageService.class);
              final String requestMessage = ctx.getPathTokens().get("message");
              final String responseMessage = messageService.upper(requestMessage);

              ctx.render(responseMessage);
            })
        )
    );
  }
}
