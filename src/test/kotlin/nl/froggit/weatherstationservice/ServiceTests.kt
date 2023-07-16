package nl.froggit.weatherstationservice

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.verify
import nl.froggit.weatherstationservice.model.LocalWeather
import nl.froggit.weatherstationservice.service.FileService
import nl.froggit.weatherstationservice.service.WeatherDataService
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.util.LinkedMultiValueMap

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = [WeatherstationServiceApplication::class])
@ExtendWith(SpringExtension::class)
class WeatherDataServiceTests {

    @MockkBean
    private lateinit var fileService: FileService

    @Autowired
    private lateinit var weatherDataService: WeatherDataService

    @Test
    fun `Assert that requests get stored raw`() {
        every { fileService.storeRawRequestToFile(any()) } returns Unit

        weatherDataService.storeRawRequest(LinkedMultiValueMap())

        verify { fileService.storeRawRequestToFile(any()) }
    }

    @Test
    fun `Assert that a request gets processed with 1 field of data`() {
        val weatherDataRequest = LinkedMultiValueMap<String,String>()
        weatherDataRequest["tempinf"] = listOf("100")
        val model = LocalWeather(insideTemp = 37.78, windSpeedAverageTenMinutes = null, capturedTime = null, insideHumidity = null,
            pressureRelative = null, pressureAbsolute = null, outsideHumidity = null, outsideTemperature = null, windDirection = null,
            windSpeed = null, windGust = null, windGustDailyMax = null, windDirectionAverageTenMinutes = null, rainRate = null,
            yearlyRain = null, eventRainRate = null, hourlyRain = null, dailyRain = null, weeklyRain = null, uvIndex = null,
            monthlyRain = null, solarRadiation = null)
        every { fileService.storeConvertedWeatherRequestToFile(model) } returns Unit

        weatherDataService.processIncomingData(weatherDataRequest)

        verify { fileService.storeConvertedWeatherRequestToFile(model) }
    }

    @Test
    fun `Assert that a request gets processed with multiple fields of data`() {
        val weatherDataRequest = LinkedMultiValueMap<String,String>()
        weatherDataRequest["tempinf"] = listOf("100")
        weatherDataRequest["windspeedmph"] = listOf("10")
        weatherDataRequest["solarradiation"] = listOf("105.3")
        val model = LocalWeather(insideTemp = 37.78, windSpeedAverageTenMinutes = null, capturedTime = null, insideHumidity = null,
            pressureRelative = null, pressureAbsolute = null, outsideHumidity = null, outsideTemperature = null, windDirection = null,
            windSpeed = 16.1, windGust = null, windGustDailyMax = null, windDirectionAverageTenMinutes = null, rainRate = null,
            yearlyRain = null, eventRainRate = null, hourlyRain = null, dailyRain = null, weeklyRain = null, uvIndex = null,
            monthlyRain = null, solarRadiation = 105.3)
        every { fileService.storeConvertedWeatherRequestToFile(model) } returns Unit

        weatherDataService.processIncomingData(weatherDataRequest)

        verify { fileService.storeConvertedWeatherRequestToFile(model) }
    }
}