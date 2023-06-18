package nl.froggit.weatherstationservice.controller

import nl.froggit.weatherstationservice.service.WeatherDataService
import org.springframework.http.ResponseEntity
import org.springframework.util.MultiValueMap
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.logging.Logger

@RestController
@RequestMapping("/weather")
class WeatherDataController(val weatherDataService: WeatherDataService) {

    companion object {
        val LOG = Logger.getLogger(WeatherDataController::class.java.name)
    }

    @PostMapping()
    fun receiveWeatherData(@RequestParam body: MultiValueMap<String, String>): ResponseEntity<Void> {
        LOG.info("Received request from weather station.")
        weatherDataService.storeRawRequest(body)
        weatherDataService.processIncomingData(body)
        return ResponseEntity.ok().build()
    }
}