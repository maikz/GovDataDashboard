package com.govdataDashboard

import com.google.gson.Gson
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.client.statement.*

/**
 * Fetches the GovData API.
 */
class GovDataFetcher {

    /**
     * Fetches a list of organisations that match those from the departments.json.
     * @return a List of OrganisationResult instances.
     */
     suspend fun fetch(includeSubordinates: Boolean): List<OrganisationResult> {
        val client = HttpClient(CIO) {
            install(Logging) {
                level = LogLevel.INFO
            }
        }

        // Get the list of organisations and include the package field because we need toi display it.
        val response: HttpResponse =
            client.get("https://ckan.govdata.de/api/3/action/organization_list?all_fields=True")
        val returnedJSON = Gson().fromJson(response.bodyAsText(), APIResult::class.java)

        // Filter the list of organisations by those we actually want to see (specified in the departments.json).
        return returnedJSON.result
            .filter { organisationResult ->
                val requiredDepartments = DepartmentList.create()
                if (includeSubordinates) {
                    // Get the list of the department names and the subordinate names.
                    requiredDepartments
                        .nameList()
                        .any { it == organisationResult.display_name }
                } else {
                    requiredDepartments.departments
                        .any { it.name == organisationResult.display_name }
                }
            }
            .sortedByDescending { it.package_count }
    }
}

/**
 * Represents a JSON result from the GovData API call.
 * @property result A list of organisations as JSON objects.
 */
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
data class OrganisationResult(
    val display_name: String,
    val package_count: Int
)