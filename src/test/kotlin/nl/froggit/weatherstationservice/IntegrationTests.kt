import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.verify
import nl.froggit.weatherstationservice.WeatherstationServiceApplication
import nl.froggit.weatherstationservice.controller.FileController
import nl.froggit.weatherstationservice.service.FileService
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.core.io.ByteArrayResource
import org.springframework.http.HttpStatus
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.io.File

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = [WeatherstationServiceApplication::class])
@ExtendWith(SpringExtension::class)
class FileControllerIntegrationTests {

    @MockkBean
    private lateinit var fileService: FileService

    @Autowired
    private lateinit var controller: FileController

    @Autowired
    private lateinit var restTemplate: TestRestTemplate
    @Test
    fun `Assert HomePage loads with no files`() {
        every { fileService.retrieveAllFiles() } returns emptyList()

        val entity = restTemplate.getForEntity("/",String::class.java)
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body).contains("List of files")
    }

    @Test
    fun `Assert HomePage loads with 1 file`() {
        val file = listOf(WeatherFileDto(File(""),true,"weatherReport"))
        every {fileService.retrieveAllFiles()} returns file

        val entity = restTemplate.getForEntity("/",String::class.java)
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body).contains("List of files")
        assertThat(entity.body).contains("weatherReport")
    }

    @Test
    fun `Assert HomePage loads with multiple files`() {
        val files = listOf(WeatherFileDto(File(""), true, "weatherReport1"), WeatherFileDto(File(""), true, "weatherReport2"))

        every {fileService.retrieveAllFiles()} returns files

        val entity = restTemplate.getForEntity("/",String::class.java)
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body).contains("List of files")
        assertThat(entity.body).contains("weatherReport")
    }

    @Test
    fun `Assert file can be downloaded`() {
        val test = "test"
        val testFile = ByteArrayResource(ByteArray(0),null)
        every { fileService.getResourceByName(test) } returns testFile
        val entity = controller.downloadFile(test)
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body).isEqualTo(testFile)
        verify { fileService.getResourceByName(test) }
    }

}