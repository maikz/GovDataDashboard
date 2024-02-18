# GovData Dashboard 
This is a small web application that provides a dashboard showing how many data sets each federal ministry has made available on [GovData](https://www.govdata.de/).
The list of organisations is sorted from most to least datasets. 
States, municipalities and other institutions excluded from the dashboard.

## Setup

This application is built using Ktor with git as version control system.
Please clone or download the project from the repository first.

To run it via command line or *intelliJ* please follow the instructions on the [Ktor website](https://ktor.io/docs/server-create-a-new-project.html#unpacking).

Open a browser and open http://0.0.0.0:8080 to load the dashboard.

## Future Work

### Use idiomatic Kotlin code where applicable
This is my first time using ktor and my first time writing Kotlin in a long time. 
So I am expecting there being better solutions in terms of syntactic sugar, language idioms, and best practices 
that are uncovered with experience with that toolset.

### Error handling
Currently, when the API call fails or the data is not in the expected form 
the whole application crashes with the respective exception. 
The website is expected to fail more gracefully and show a status to the user when an exception occurs. 

### More efficient API calls
Currently, the application fetches all organisations from the API which can be quite inefficient
and should be reconsidered. I would be interesting to evaluate if fetching individual organisations by name or id is more efficient with this number of organisations 

