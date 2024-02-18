import com.example.Department
import com.example.DepartmentList
import com.example.Subordinate
import com.example.module
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import org.junit.Assert.assertEquals
import org.junit.Test
import kotlin.test.assertContains
import kotlin.test.assertTrue


class ApplicationTest {
    @Test
    fun testRoot() = testApplication {
        application {
            module()
        }

        val response = client.get("/")
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals("Hello World!", response.bodyAsText())
    }

    @Test
    fun testNewEndpoint() = testApplication {
        application { module() }

        val response = client.get("/test1")
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals("html", response.contentType()?.contentSubtype)
        assertContains(response.bodyAsText(), "Hello From Ktor")
    }

    @Test
    fun testDepartmentLoading() {
        // Assuming that the departments json is static and doesn't change
        // we check the expected values in memory here.
        val departmentList = DepartmentList.create()
        assertEquals(13, departmentList.departments.count())
        val example = Department(
            name = "Bundesministerium für Arbeit und Soziales",
            subordinates = listOf(Subordinate(name = "Bundesanstalt für Arbeitsschutz und Arbeitsmedizin "))
        )
        assertTrue(departmentList.departments.contains(example))

        assertEquals(30, departmentList.nameList().count())
    }
}