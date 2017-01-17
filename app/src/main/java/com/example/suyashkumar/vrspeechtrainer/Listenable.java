package com.example.suyashkumar.vrspeechtrainer;

import android.support.v7.widget.CardView;

/**
 * Created by SUYASH KUMAR on 1/17/2017.
 */
public interface Listenable {
    int REQ_CODE_SPEECH_INPUT = 100;
    public void listen(CardView cardView, String trueWord);
}
