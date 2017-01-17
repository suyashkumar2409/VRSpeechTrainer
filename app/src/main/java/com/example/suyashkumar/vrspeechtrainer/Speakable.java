package com.example.suyashkumar.vrspeechtrainer;

import android.speech.tts.TextToSpeech;

/**
 * Created by SUYASH KUMAR on 1/17/2017.
 */
public interface Speakable {

    public void initSpeak();
    public void speak(String text);

}
