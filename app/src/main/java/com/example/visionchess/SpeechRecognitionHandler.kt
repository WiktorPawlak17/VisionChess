package com.example.visionchess

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer

class SpeechRecognitionHandler(private val context: Context){
    private val speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context)
    private val recognitionListener = MyRecognitionListener()
    var recognizedMessage = ""
    var youDone = false

    fun startRecognition() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        speechRecognizer.setRecognitionListener(recognitionListener)
        speechRecognizer.startListening(intent)
    }

    fun stopRecognition() {
        speechRecognizer.stopListening()
    }

    fun cancelRecognition() {
        speechRecognizer.cancel()
    }
    private inner class MyRecognitionListener : RecognitionListener {
        // Implement the recognition listener methods as needed
        override fun onReadyForSpeech(p0: Bundle?) {
            val spokenText = p0?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
            //Set the text of the TextView to the spoken text
            recognizedMessage = spokenText.toString()
        }

        override fun onBeginningOfSpeech() {

        }

        override fun onRmsChanged(p0: Float) {

        }

        override fun onBufferReceived(p0: ByteArray?) {

        }

        override fun onEndOfSpeech() {
            //Tell the user that the speech has ended and to stop the timer
        youDone = true
        }

        override fun onError(p0: Int) {

        }

        override fun onResults(p0: Bundle?) {
            //Get the results from the speech recognition
            val results = p0?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
            //Get the first result
            val spokenText = results?.get(0)
            //Set the text of the TextView to the spoken text
            recognizedMessage = spokenText.toString()
        }

        override fun onPartialResults(p0: Bundle?) {

        }

        override fun onEvent(p0: Int, p1: Bundle?) {

        }
    }

}