package nl.froggit.weatherstationservice.model

import java.time.LocalDateTime

data class LocalWeather(val capturedTime: LocalDateTime?, val insideTemp: Double?, val insideHumidity: Double?, val pressureRelative: Double?,
                        val pressureAbsolute: Double?, val outsideTemperature: Double?, val outsideHumidity: Double?,
                        val windDirection: Double?, val windDirectionAverageTenMinutes: Double?, val windSpeed: Double?,
                        val windSpeedAverageTenMinutes: Double?, val windGust: Double?, val windGustDailyMax: Double?,
                        val rainRate: Double?, val eventRainRate: Double?, val hourlyRain: Double?, val dailyRain: Double?,
                        val weeklyRain: Double?, val monthlyRain: Double?, val yearlyRain: Double?, val solarRadiation: Double?,
                        val uvIndex: Double?)