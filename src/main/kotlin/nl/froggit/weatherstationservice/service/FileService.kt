package nl.froggit.weatherstationservice.service

import WeatherFileDto
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.exc.MismatchedInputException
import com.fasterxml.jackson.module.kotlin.readValue
import nl.froggit.weatherstationservice.model.LocalWeather
import org.springframework.core.io.Resource
import org.springframework.core.io.ResourceLoader
import org.springframework.core.io.support.ResourcePatternUtils
import org.springframework.stereotype.Service
import org.springframework.util.MultiValueMap
import java.io.File
import java.time.LocalDate
import java.util.logging.Logger

@Service
class FileService(val mapper: ObjectMapper, val resourceLoader: ResourceLoader) {

    companion object {
        val LOG = Logger.getLogger(FileService::class.java.name)
    }

    fun retrieveAllFiles(): List<WeatherFileDto> {
        val files = loadResources()
        val weatherFiles = mutableListOf<WeatherFileDto>()
        files.forEach { file ->
            weatherFiles.add(WeatherFileDto(file, isFileRawResponse(file), file.name))
        }
        return weatherFiles
    }
    fun storeRawRequestToFile(request: MultiValueMap<String, String>) {
        val file = createFile(true)
        file.appendText("$request\n")
        LOG.info("Incoming request has been persisted to file with name ${file.name}")
    }

    fun storeConvertedWeatherRequestToFile(model: LocalWeather) {
        val file = createFile()
        val results = try {
            mapper.readValue<List<LocalWeather>>(file).toMutableList()
        } catch (e: MismatchedInputException) { mutableListOf() }
        results.add(model)
        val res = mapper.writeValueAsString(results)
        file.writeText(res)
    }

    fun getResourceByName(name: String): Resource {
        return resourceLoader.getResource("classpath:/$name")
    }

    private fun createFileName(rawRequest: Boolean = false) : String {
        val currentDate = LocalDate.now()
        val rawStringAddition = if(rawRequest) "-raw" else ""
        return "weatherstationdata$rawStringAddition-$currentDate.log"
    }

    private fun createFile(rawRequest: Boolean = false) : File{
        val fileName = createFileName(rawRequest)
        val file = File(fileName)
        if(file.createNewFile()) {
            LOG.info("Created new file with name $fileName")
        }
        return file
    }

    private fun loadResources() : List<File> {
        val resourcesPattern = "classpath:/*.log"
        return ResourcePatternUtils.getResourcePatternResolver(resourceLoader)
            .getResources(resourcesPattern)
            .map { resource -> resource.file }
    }

    private fun isFileRawResponse(file: File) : Boolean {
        return file.name.contains("raw")
    }
}