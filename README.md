<h1>Description</h1>
This app is an independent module for parsing OLX pages. Olx is the most visited website in Ukraine. It takes information about the request from the queue and collects information about the number of views, publication date, price, product name etc. As a result, this application creates an Excel file and sends it to Google Drive.

<img width="1210" alt="Screenshot 2023-01-07 at 17 07 52" src="https://user-images.githubusercontent.com/11314278/211157524-1a35ed71-7411-41da-aebc-7178394f9520.png">
This application works together with a [Telegram bot application](https://github.com/sich-mykhailo/superTelegrambot) that includes authorization and allows communication with clients through a Telegram bot.

Processing more than 1000 pages takes approximately 10 minutes. The architecture is designed to handle multiple requests simultaneously. To achieve this, the program utilizes SQS to create a single queue for all requests.
Depending on the number of parser instances, we can reduce the processing time to 10 minutes.
The Telegram bot application contains user information, the number of used requests, registration, and other related features, storing this data in a database.


If there is a parser instance that is not processing any requests and a new element appears in the SQS, it will immediately pick it up and start processing. However, if all parser instances are busy processing requests, the queue will fill up and wait until at least one becomes available. The first instance that becomes available will pick up the first element in the queue for processing.
