package nl.froggit.weatherstationservice.service

import nl.froggit.weatherstationservice.model.LocalWeather
import org.springframework.stereotype.Service
import org.springframework.util.MultiValueMap
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.logging.Logger

@Service
class WeatherDataService(val fileService: FileService) {

    companion object {
        val LOG = Logger.getLogger(WeatherDataService::class.java.name)
    }

    fun processIncomingData(body: MultiValueMap<String, String>) {
        LOG.info("Converting request and storing this as JSON")
        val localWeather = toLocalWeather(body)
        fileService.storeConvertedWeatherRequestToFile(localWeather)
    }

    fun storeRawRequest(body: MultiValueMap<String, String>) {
        LOG.info("Storing request as-is received")
        fileService.storeRawRequestToFile(body)
    }

    private fun String.toDoubleOrZero() : Double {
        if(this.isEmpty()) return 0.0
        return toDouble()
    }

    private fun toLocalWeather(body: MultiValueMap<String, String>): LocalWeather {
        val capturedTime =
            convertToLocalDateTime(body)
        val insideTemp = convertFahrenheitToCelsius(retrieveValue("tempinf", body))
        val insideHumidity = retrieveValue("humidityin", body).toDoubleOrZero()
        val pressureRelative = convertMercuryInchesToHectoPascal(retrieveValue("baromrelin", body))
        val pressureAbsolute = convertMercuryInchesToHectoPascal(retrieveValue("baromabsin", body))
        val outsideTemperature = convertFahrenheitToCelsius(retrieveValue("tempf", body))
        val outsideHumidity = retrieveValue("humidity", body).toDoubleOrZero()
        val windDirection = retrieveValue("winddir", body).toDoubleOrZero()
        val windDirectionAverageTenMinutes = retrieveValue("winddir_avg10m", body).toDoubleOrZero()
        val windSpeed = convertMilesToKms(retrieveValue("windspeedmph", body))
        val windSpeedAverageTenMinutes = convertMilesToKms(retrieveValue("windspdmph_avg10m", body))
        val windGust = convertMilesToKms(retrieveValue("windgustmph", body))
        val windGustDailyMax = convertMilesToKms(retrieveValue("maxdailygust", body))
        val rainRate = convertInchesToMillimeters(retrieveValue("rainratein", body))
        val eventRainRate = convertInchesToMillimeters(retrieveValue("eventrainin", body))
        val hourlyRain = convertInchesToMillimeters(retrieveValue("hourlyrainin", body))
        val dailyRain = convertInchesToMillimeters(retrieveValue("dailyrainin", body))
        val weeklyRain = convertInchesToMillimeters(retrieveValue("weeklyrainin", body))
        val monthlyRain = convertInchesToMillimeters(retrieveValue("monthlyrainin", body))
        val yearlyRain = convertInchesToMillimeters(retrieveValue("yearlyrainin", body))
        val solarRadiation = retrieveValue("solarradiation", body).toDoubleOrZero()
        val uvIndex = retrieveValue("uv", body).toDoubleOrZero()
        return LocalWeather(
            capturedTime = capturedTime, insideTemp = insideTemp,
            insideHumidity = insideHumidity, pressureRelative = pressureRelative,
            pressureAbsolute = pressureAbsolute, outsideTemperature = outsideTemperature,
            outsideHumidity = outsideHumidity, windDirection = windDirection,
            windDirectionAverageTenMinutes = windDirectionAverageTenMinutes,
            windSpeed = windSpeed, windSpeedAverageTenMinutes = windSpeedAverageTenMinutes,
            windGust = windGust, windGustDailyMax = windGustDailyMax,
            rainRate = rainRate, eventRainRate = eventRainRate, hourlyRain = hourlyRain,
            dailyRain = dailyRain, weeklyRain = weeklyRain, monthlyRain = monthlyRain,
            yearlyRain = yearlyRain, solarRadiation = solarRadiation, uvIndex = uvIndex
        )
    }

    private fun convertToLocalDateTime(body: MultiValueMap<String, String>): LocalDateTime {
    if(retrieveValue("dateutc",body).isEmpty()) return LocalDateTime.now()
    return LocalDateTime.parse(retrieveValue("dateutc", body), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
}

    private fun retrieveValue(key: String, body: MultiValueMap<String, String>): String {
        return body.getFirst(key).orEmpty()
    }

    private fun convertFahrenheitToCelsius(fahrenheit: String): Double {
        if(fahrenheit.isEmpty()) return 0.0
        val doubleValue = fahrenheit.toDouble()
        return String.format("%.2f", (doubleValue - 32) / 1.8).toDouble()
    }

    private fun convertMilesToKms(miles: String): Double {
        if(miles.isEmpty()) return 0.0
        val doubleValue = miles.toDouble()
        return String.format("%.1f", doubleValue * 1.60934).toDouble()
    }

    private fun convertInchesToMillimeters(inches: String, decimal: String = "1"): Double {
        if(inches.isEmpty()) return 0.0
        val doubleValue = inches.toDouble()
        return String.format("%." + decimal + "f", doubleValue * 25.4).toDouble()
    }

    private fun convertMercuryInchesToHectoPascal(inches: String): Double {
        if(inches.isEmpty()) return 0.0
        val doubleValue = inches.toDouble()
        return String.format("%.2f", doubleValue * 33.86388666666).toDouble()
    }
}
