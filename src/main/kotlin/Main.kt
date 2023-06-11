import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@SpringBootApplication
open class Main {

    @RequestMapping("/")
    fun home() = "Hello Worl!"

}

fun main(args: Array<String>) {
    runApplication<Main>(*args)
}