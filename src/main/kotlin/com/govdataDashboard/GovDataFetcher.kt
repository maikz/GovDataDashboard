package com.govdataDashboard

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

/**
 * Fetches the GovData API.
 */
class GovDataFetcher {

    private val jsonDecoder = Json { ignoreUnknownKeys = true }
    private val client = HttpClient(CIO) {
        install(Logging) {
            level = LogLevel.INFO
        }
    }

    /**
     * Fetches a list of organisations that match the federal ministries
     * from the departments.json ordered by number of packages (datasets).
     * @param includeSubordinates specifies if subordinate organisations should be included in the returned list.
     * @return a List of OrganisationResult instances.
     */
     suspend fun federalMinistriesByPackageSize(includeSubordinates: Boolean): List<OrganisationResult> {
        // Get the list of organisations and include the package field because we need toi display it.
        val response: HttpResponse =
            this.client.get("https://ckan.govdata.de/api/3/action/organization_list?all_fields=True")
        val returnedJSON = this.jsonDecoder.decodeFromString<APIResult>(response.bodyAsText())

        // Filter the list of organisations by those we actually want to see (specified in the departments.json).
        return returnedJSON.result
            .filter { organisationResult ->
                val requiredDepartments = MinistryList.create()
                if (includeSubordinates) {
                    // Get the list of the department names and the subordinate names.
                    requiredDepartments
                        .nameList()
                        .any { it == organisationResult.display_name }
                } else {
                    requiredDepartments.ministries
                        .any { it.name == organisationResult.display_name }
                }
            }
            .sortedByDescending { it.package_count }
    }
}

/**
 * Represents a JSON result from the GovData API call.
 * Note: This is named kind of generally for now since there is only one API call so far.
 * @property result A list of organisations as JSON objects.
 */
@Serializable
data class APIResult(
    val help: String,
    val success: Boolean,
    val result: List<OrganisationResult>
)

/**
 * Represents one organisation in the returned JSON from the API.
 * @property display_name The name for display matching the department's name in the departments.json resource.
 * @property package_count Number of datasets this organisation has published.
 */
@Serializable
data class OrganisationResult(
    val display_name: String,
    val package_count: Int
)