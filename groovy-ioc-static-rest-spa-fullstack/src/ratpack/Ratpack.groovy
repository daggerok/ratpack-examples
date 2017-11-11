import daggerok.JavaClasses
import org.slf4j.LoggerFactory
import ratpack.groovy.template.MarkupTemplateModule
import ratpack.jackson.Jackson

import static ratpack.groovy.Groovy.groovyMarkupTemplate
import static ratpack.groovy.Groovy.ratpack

ratpack {

  bindings {
    module MarkupTemplateModule
    bind JavaClasses.MyService, JavaClasses.MyServiceImpl
  }

  handlers {

    // like Interceptors or Filters in java
    all {
      // RequestLogger.ncsa()
      def log = LoggerFactory.getLogger(this.class)
      log.info("hey")
      next()
    }

    // SPA
    get {
      def myService = registry.get(JavaClasses.MyService)

      render groovyMarkupTemplate("index.gtpl", title: myService.myMethod("My Ratpack App"))
    }

    // REST API
    get("api/:whatever?") {

      def payload = it.allPathTokens.getOrDefault("whatever", "my hi to u!")
      def myService = registry.get(JavaClasses.MyService)
      def responseBody = Jackson.json([
        message: myService.myMethod(payload),
      ])
      //// setting content type probably not necessary with Jackson
      //it.response.contentType("application/json")
      render responseBody
    }

    // serve static content from public folder
    files {
      dir "public"
    }
  }
}
