package com.example

import com.google.gson.Gson
import java.io.File


/**
 * Contains federal ministries and their subordinated agencies.
 * Matches the provided json.
 */
data class DepartmentList(
    val departments: List<Department>,
) {
    companion object Factory {
        fun create(path: String = "src/main/resources/departments.json"): DepartmentList {
            val json = File(path).readText()
            return Gson().fromJson(json, DepartmentList::class.java)
        }
    }
}

/**
 * A federal ministry
 */
data class Department(
    val name: String,
    val subordinates: List<Subordinate>?,
)

/**
 * A subordinate of a federal ministry
 */
data class Subordinate(
    val name: String,
)

