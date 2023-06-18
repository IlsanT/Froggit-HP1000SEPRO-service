package nl.froggit.weatherstationservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class WeatherstationServiceApplication

fun main(args: Array<String>) {
	runApplication<WeatherstationServiceApplication>(*args)
}
