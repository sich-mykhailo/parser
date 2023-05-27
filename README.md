<h1>Description</h1>
This app is an independent module for parsing OLX pages. It takes information about the request from the queue and collects information about the number of views, publication date, price, product name etc. As a result, this application creates an Excel file and sends it to Google Drive.
<img width="1173" alt="211543063-7e26ca6e-a803-469d-8b68-f929100c690a" src="https://github.com/sich-mykhailo/parser/assets/11314278/33670c42-6d2d-45e8-918d-45803643b546">

This application works together with a [Telegram bot application](https://github.com/sich-mykhailo/superTelegrambot) that includes authorization and allows communication with clients through a Telegram bot.

Processing more than 1000 pages takes approximately 10 minutes. The architecture is designed to handle multiple requests simultaneously. To achieve this, the program utilizes SQS to create a single queue for all requests.
Depending on the number of parser instances, we can reduce the processing time to 10 minutes.
The Telegram bot application contains user information, the number of used requests, registration, and other related features, storing this data in a database.


If there is a parser instance that is not processing any requests and a new element appears in the SQS, it will immediately pick it up and start processing. However, if all parser instances are busy processing requests, the queue will fill up and wait until at least one becomes available. The first instance that becomes available will pick up the first element in the queue for processing.

<h2>The technologies used are:</h2>

* Spring Boot: a Java framework for building stand-alone, production-grade applications.
* Jsoup: a Java library for parsing HTML and manipulating HTML documents.
* Apache POI: a Java library for reading and writing Microsoft Office file formats, such as Excel and Word.
* Amazon SQS: a fully managed message queuing service provided by Amazon Web Services (AWS).
* Lombok: a Java library that helps reduce boilerplate code by automatically generating getter, setter, and other utility   methods during compilation.
* Google API: a collection of APIs provided by Google for interacting with various Google services, such as Google Drive, Gmail.

<h2>Run applicatiion</h2>
1. Install a Docker on your computer.<br>
2. Create a Docker image with any name.<br>
3. Run a Docker container with the following environment variables:

a) Set the port:
* PORT

b) Set the number of pages the parser will process (maximum 25, minimum 1):

* PAGES_TO_PROCESS

c) Configure the Google Play Cloud for the Google Drive API application:
* GOOGLE_DRIVE_SECRET
* GOOGLE_DRIVE_CLIENT_ID
* GOOGLE_DRIVE_REFRESH_TOKEN
* PARSER_ADMIN_EMAIL

d) Insert the admin email:

* PARSER_ADMIN_EMAIL
* MAIL_USER_NAME
* MAIL_PASSWORD

e) Insert the AWS credentials that you use in the Super Parser Bot application:

* AWS_SQS_URI
* AWS_ACCESS_KEY
* AWS_SECRET_KEY

f) Register on OLX and use your Bearer Token (you can use any valid token, but then the statistics on views will not work, and the column in the table will be zero
* OLX_TOKEN
