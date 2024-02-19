package com.govdataDashboard.plugins

import com.govdataDashboard.GovDataFetcher
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.html.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.plugins.statuspages.*
import kotlinx.html.*

fun Application.configureRouting() {

    install(StatusPages) {
        exception<IllegalStateException> { call, cause ->
            call.respondText("App in illegal state as ${cause.message}")
        }
    }

    routing {
        get("/") {
            val organisations = GovDataFetcher().federalMinistriesByPackageSize(includeSubordinates = true)
            call.respondHtml(HttpStatusCode.OK) {
                head {
                    title {
                        +"Departments by dataset count"
                    }
                }
                body {
                    h1 {
                        +"Departments by dataset count"
                    }
                    table {
                        tr {
                            th { +"Name" }
                            th { +"Number of datasets" }
                        }
                        for (organisation in organisations) {
                            tr {
                                td {
                                    +organisation.display_name
                                }
                                td {
                                    +"${organisation.package_count}"
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
