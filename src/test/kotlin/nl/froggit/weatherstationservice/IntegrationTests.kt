import nl.froggit.weatherstationservice.WeatherstationServiceApplication
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpStatus

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = [WeatherstationServiceApplication::class])
class FileControllerIntegrationTests(@Autowired val restTemplate: TestRestTemplate) {

    @Test
    fun `Assert HomePage loads`() {
        val entity = restTemplate.getForEntity("/",String::class.java)
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body).contains("List of files")
    }

}