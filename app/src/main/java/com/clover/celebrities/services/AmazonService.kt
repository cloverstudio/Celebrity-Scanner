package com.clover.celebrities.services

import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.rekognition.AmazonRekognitionClient
import com.clover.celebrities.Constants

object AmazonService {

    val rekognitionClient = AmazonRekognitionClient(
        BasicAWSCredentials(
            Constants.accessKey,
            Constants.secretKey
        )
    )

}