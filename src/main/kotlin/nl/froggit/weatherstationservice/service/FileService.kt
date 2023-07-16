package nl.froggit.weatherstationservice.service

import WeatherFileDto
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.exc.MismatchedInputException
import com.fasterxml.jackson.module.kotlin.readValue
import nl.froggit.weatherstationservice.model.LocalWeather
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.ByteArrayResource
import org.springframework.core.io.Resource
import org.springframework.stereotype.Service
import org.springframework.util.MultiValueMap
import java.io.File
import java.nio.file.Paths
import java.time.LocalDate
import java.util.logging.Logger
import kotlin.io.path.readBytes

@Service
class FileService(val mapper: ObjectMapper) {

    @Value("\${file-location-path}")
    lateinit var fileLocationPath: String

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
        val file = File("$fileLocationPath$name")
        val path = Paths.get(file.absolutePath)
        return ByteArrayResource(path.readBytes())
    }

    private fun createFileName(rawRequest: Boolean = false) : String {
        val currentDate = LocalDate.now()
        val fileExtension = if(rawRequest) ".txt" else ".json"
        return "weatherstationdata-$currentDate$fileExtension"
    }

    private fun createFile(rawRequest: Boolean = false) : File{
        val fileName = createFileName(rawRequest)
        val file = File("$fileLocationPath$fileName")
        if(file.createNewFile()) {
            LOG.info("Created new file with name $fileName")
        }
        return file
    }

    private fun loadResources() : List<File> {
        val files = File(fileLocationPath).listFiles()
        return files?.filter { file -> file.name.contains("log") }.orEmpty().toList()
    }

    private fun isFileRawResponse(file: File) : Boolean {
        return file.name.contains("raw")
    }
}