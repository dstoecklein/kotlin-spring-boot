# Kotlin and Spring Boot

Run and visit `http://localhost:8080`

```kotlin
@RestController
@SpringBootApplication
open class Main {
    @RequestMapping("/")
    fun home() = "Hello World!"
}

fun main(args: Array<String>) {
    runApplication<Main>(*args)
}
```

`@RestController` is a *stereotype* annotation, which provides hints for people reading the code and for Spring that the class play a specific role. In this case, the class is a web `@Controller`, so Spring considers it when handling incoming web requests.

`@RequestMapping` annotation provides routing information. It tells Spring that any HTTP request should be mapped to the `home` method.

> The `@RestController` and `@RequestMapping` annotations are Spring MVC annotations (they are not specific to Spring Boot)

The `@SpringBootApplication` is a *meta-annotation*, it combines `@SpringBootConfiguration`, `@EnableAutoConfiguration` and `@ComponentScan`. `@EnableAutoConfiguration` tells Spring Boot to *guess* how you want to configure Spring, based on the jar dependencies that you have added.

In the `main` method we need to pass `Main`.class as an argument to the run method to tell SpringApplication which is the primary Spring component. 

# Gradle (Kotlin DLS)
