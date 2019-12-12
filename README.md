# Celebrity Scanner

Celebrity Scanner is an Android App which utilizes the AmazonRecognitionService in order to identify the captured celebrities.

## Getting Started

### Prerequisites

Since this app utilizes the functionality of the AmazonRecognitionService, setup your AWS Account and SDKs by following [this link](https://docs.aws.amazon.com/rekognition/latest/dg/celebrities-procedure-image.html).

After you have successfully registered your AWS Account and obtained the aws_secret_key and aws_access_key, proceed to the installation part.

### Installing

Clone this GutHub repository to your local machine:
```
git clone https://github.com/cloverstudio/Celebrities.git
```
Open the com.example.celebrities.Constants file and paste your Credentials:
```
object Constants {
    val accessKey = "your_access_key"
    val secretKey = "your_secret_key"
}
```

Run the app on your Android device.

## Built With

* [Android](https://developer.android.com/)
* [Kotlin](https://kotlinlang.org/)
* [Amazon Rekognition](https://aws.amazon.com/rekognition/) - Image Analysis
* [Glide](https://github.com/bumptech/glide) - Image Loading Framework
* [Jsoup](https://github.com/jhy/jsoup) - HTTP Parser
