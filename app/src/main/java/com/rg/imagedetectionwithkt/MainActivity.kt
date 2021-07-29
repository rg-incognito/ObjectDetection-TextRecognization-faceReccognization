package com.rg.imagedetectionwithkt

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.View
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizerOptions
import kotlinx.android.synthetic.main.activity_main.*
import java.io.IOException
import kotlin.text.StringBuilder as TextStringBuilder

class MainActivity : AppCompatActivity() {
    private val labeler = ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS)
    private val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
    val highAccuracyOpts = FaceDetectorOptions.Builder()
        .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
        .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
        .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
        .build()

    val detector = FaceDetection.getClient(highAccuracyOpts)
    var sb : StringBuilder = StringBuilder();

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        btn.setOnClickListener(View.OnClickListener {
            ImagePicker.with(this)
                .crop()
                .galleryOnly()
                .start(100)

        })
        btn2.setOnClickListener(View.OnClickListener {
            ImagePicker.with(this)
                .crop()
                .galleryOnly()
                .start(200)

        })
        btn3.setOnClickListener(View.OnClickListener {
            ImagePicker.with(this)
                .crop()
                .galleryOnly()
                .start()

        })

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val uri: Uri = data?.data!!
        image.setImageURI(uri)
        val image:InputImage = InputImage.fromFilePath(applicationContext, uri)
     if(requestCode == 100){


         labeler.process(image)
             .addOnSuccessListener { labels ->
                 for(lable in labels){

                     sb.append(lable.text)
                     sb.append("   ")
                 }
                 textView.setText(sb.toString())
                 textView.movementMethod = ScrollingMovementMethod.getInstance()


             }
             .addOnFailureListener { e ->
                 textView.text = "Nothing found"

             }

     }else if(requestCode == 200){
         val result = recognizer.process(image)
             .addOnSuccessListener { visionText ->

                 textView.setText(visionText.text)
                 textView.movementMethod = ScrollingMovementMethod.getInstance()

             }
             .addOnFailureListener { e ->
                 textView.text = "Nothing found"

             }

     }else{
         val result = detector.process(image)
             .addOnSuccessListener { faces ->
                 for(face in faces){

                     sb.append(face)
                     sb.append("   ")
                 }
                 textView.setText(sb.toString())
                 textView.movementMethod = ScrollingMovementMethod.getInstance()

                 // Task completed successfully
                 // ...
             }
             .addOnFailureListener { e ->
                 // Task failed with an exception
                 // ...
                 textView.text = "Nothing found"

             }
     }


    }




}