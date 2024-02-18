import com.govdataDashboard.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import org.junit.Assert.assertEquals
import org.junit.Test
import kotlin.test.assertTrue


class ApplicationTest {
    @Test
    fun testRoot() = testApplication {
        application {
            module()
        }

        val response = client.get("/")
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals("html", response.contentType()?.contentSubtype)

        val body = response.bodyAsText()

        // Check if we're generally on the page we expect.
        assertTrue(body.contains("Departments by dataset count"))

        val names = listOf("mCLOUD",
            "Statistisches Bundesamt",
            "Bundesministerium des Innern und Heimat",
            "Bundesamt für Justiz",
            "Bundesministerium für Bildung und Forschung",
            "Bundesministerium für Arbeit und Soziales",
            "Bundesministerium für Familie, Senioren, Frauen und Jugend",
            "Bundesministerium der Finanzen",
            "Bundesministerium für Wirtschaft und Klimaschutz",
            "Deutsches Patent- und Markenamt",
            "Bundesamt für Verbraucherschutz und Lebensmittelsicherheit",
            "Bundesanstalt für Arbeitsschutz und Arbeitsmedizin",
            "Bundesministerium für Ernährung und Landwirtschaft",
            "Bundesinstitut für Bau-, Stadt- und Raumforschung (BBSR) im Bundesamt für Bauwesen und Raumordnung (BBR)",
            "Auswärtiges Amt",
            "Generalzolldirektion",
            "Bundesamt für Wirtschaft und Ausfuhrkontrolle",
            "Bundesanstalt für Materialforschung und -prüfung (BAM)",
            "Bundesministerium der Verteidigung",
            "Bundesministerium für wirtschaftliche Zusammenarbeit und Entwicklung",
            "Bundesausgleichsamt",
            "Bundessortenamt",
            "Bundesverwaltungsamt",
            "Bundeszentralamt für Steuern",
            "ITZ-Bund",
            "Max Rubner-Institut"
        )
        for (name in names) {
            assertTrue(body.contains(name))
        }
    }

    @Test
    fun testDepartmentLoading() {
        // Assuming that the departments json is static and doesn't change
        // we check the expected values in memory here.
        val ministryList = MinistryList.create()
        assertEquals(13, ministryList.ministries.count())
        val example = Ministry(
            name = "Bundesministerium für Arbeit und Soziales",
            subordinates = listOf(Subordinate(name = "Bundesanstalt für Arbeitsschutz und Arbeitsmedizin "))
        )
        assertTrue(ministryList.ministries.contains(example))

        assertEquals(30, ministryList.nameList().count())
    }
}