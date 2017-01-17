package com.example.suyashkumar.vrspeechtrainer;


import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.app.Fragment;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class QuizFragment extends Fragment implements Listenable, Speakable, Turnable {

    private TextToSpeech tts;
    private boolean speechEnabled = false;
    private Context ctx;
    private View view;
    private RecyclerView rv;
    private String trueWord;

    private Cursor cursor;
    private DatabaseHelper helper;
    public QuizFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_quiz, container, false);
        rv = (RecyclerView) view.findViewById(R.id.quiz_recycler);
        ctx = view.getContext();

        initSpeak();

        helper= new DatabaseHelper(ctx);

        try {
            SQLiteDatabase db = helper.getReadableDatabase();
            cursor = db.query(
                    DatabaseHelper.word_table,
                    new String[]{"_id", DatabaseHelper.word_table_word},
                    null,null,null,null,null
            );

            QuizCardAdapter adapter = new QuizCardAdapter(cursor, ctx, this, this, this);
            rv.setAdapter(adapter);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false){
                @Override
                public boolean canScrollHorizontally(){
                    return true;
                }
            };
            rv.setLayoutManager(layoutManager);
//            view.addItemDecoration(new RecyclerView.ItemDecoration() {
//
//                @Override
//                public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
//                    if (view instanceof CardView) {
//                        int totalWidth = parent.getWidth();
//                        int cardWidth = getResources().getDimensionPixelOffset(parent.getWidth());
//                        int sidePadding = (totalWidth - cardWidth) / 2;
//                        sidePadding = Math.max(0, sidePadding);
//                        outRect.set(sidePadding, 0, sidePadding, 0);
//                    }
//                }
//            });
        }
        catch(SQLiteException e)
        {
            Toast.makeText(ctx, "Database Unavailable", Toast.LENGTH_SHORT).show();
        }
        return view;
    }


    @Override
    public void listen(CardView cardView, String trueWord) {
        this.trueWord = trueWord;
        promptSpeechInput(cardView);
    }

    @Override
    public void initSpeak() {
        tts = new TextToSpeech(ctx, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {

                    int result = tts.setLanguage(Locale.getDefault());

                    if (result == TextToSpeech.LANG_MISSING_DATA
                            || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "This Language is not supported");
                        speechEnabled = false;
                    } else {
                        speechEnabled = true;
                    }

                } else {
                    Log.e("TTS", "Initilization Failed!");
                }
            }
        });
    }

    @Override
    public void speak(String text) {
        if(speechEnabled)
        {
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        }
        else
        {
            Toast.makeText(ctx, "Speech Unavailable", Toast.LENGTH_SHORT);
        }
    }

    private void promptSpeechInput(CardView cardView) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak!");
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(view.getContext(),
                    "Listening Unavailable",
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Receiving speech input
     * */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    String result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS).get(0);
                    // Listening stuff here
                    float confidence = data.getFloatArrayExtra(RecognizerIntent.EXTRA_CONFIDENCE_SCORES)[0];

                    TextView resultDisplay = (TextView)view.findViewById(R.id.matches_mark);
                    String setVal = String.valueOf(confidence);
                    if(result.equalsIgnoreCase(trueWord))
                    {
                        setVal += " - MATCHES";
                    }

                    resultDisplay.setText(setVal);
                    resultDisplay.setVisibility(View.VISIBLE);
                }
                break;
            }

        }
    }

    @Override
    public void move() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity)ctx).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        rv.smoothScrollBy(displayMetrics.widthPixels, 0);
    }
}
