package com.govdataDashboard

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.io.File


/**
 * Contains federal ministries and their subordinated agencies.
 * Matches the provided json.
 */
@Serializable
data class MinistryList(
    val ministries: List<Ministry>,
) {
    companion object Factory {
        fun create(path: String = "src/main/resources/departments.json"): MinistryList {
            val json = File(path).readText()
            return Json.decodeFromString<MinistryList>(json)
        }
    }

    /**
     * A flat list of the ministry names and the names of their subordinates.
     */
    fun nameList(): List<String> {
        return this.ministries
            .flatMap { ministry ->
                val subordinateNames = ministry.subordinates.map { it.name }
                listOf(ministry.name) + subordinateNames
            }
    }
}

/**
 * A federal ministry
 */
@Serializable
data class Ministry(
    val name: String,
    val subordinates: List<Subordinate> = emptyList(),
)

/**
 * A subordinate of a federal ministry
 */
@Serializable
data class Subordinate(
    val name: String,
)

