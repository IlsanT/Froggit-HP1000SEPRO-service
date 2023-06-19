package nl.froggit.weatherstationservice.controller

import nl.froggit.weatherstationservice.service.FileService
import org.springframework.core.io.Resource
import org.springframework.http.*
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod

@Controller
@RequestMapping("/")
class FileController(val fileService: FileService) {

    @RequestMapping(method = [RequestMethod.GET])
    fun index(model: Model): String {
        model.addAttribute("files",fileService.retrieveAllFiles())
        return "index"
    }

    @RequestMapping(method = [RequestMethod.GET], path = ["download/{name}"])
    fun downloadFile(@PathVariable("name") name: String): ResponseEntity<Resource> {
        val file = fileService.getResourceByName(name)
        val headers = createHeaders(file, name)
        return ResponseEntity<Resource>(file, headers, HttpStatus.OK)
    }

    private fun createMediaType(resource: Resource): MediaType {
        return MediaTypeFactory
            .getMediaType(resource)
            .orElse(MediaType.APPLICATION_OCTET_STREAM)
    }

    private fun createHeaders(resource: Resource, fileName: String): HttpHeaders {
        val headers = HttpHeaders()
        headers.contentType = createMediaType(resource)
        val disposition = ContentDisposition
            .attachment()
            .filename(fileName)
            .build()
        headers.contentDisposition = disposition
        return headers
    }
}