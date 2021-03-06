package com.example.suyashkumar.vrspeechtrainer;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by SUYASH KUMAR on 1/17/2017.
 */

public class QuizCardAdapter extends RecyclerView.Adapter<QuizCardAdapter.ViewHolder> {

    private Cursor cursor;
    private boolean speechEnabled;
    private Context ctx;
    private final int REQ_CODE_SPEECH_INPUT = 100;

    private Speakable speakable;
    private Listenable listenable;
    private Turnable turnable;

    public QuizCardAdapter(Cursor cursor, Context ctx, Speakable sp, Listenable ls, Turnable turnable) {
        super();
        this.cursor = cursor;
        cursor.moveToFirst();
        speakable = sp;
        listenable = ls;
        this.turnable = turnable;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView cv = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.quiz_card, parent, false);
        return new ViewHolder(cv);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final CardView cardView = holder.cardView;
        if(cursor.moveToPosition(position))
        {
            final TextView word = (TextView)cardView.findViewById(R.id.card_word);
            Button speakButton = (Button)cardView.findViewById(R.id.card_speak);
            Button listenButton = (Button)cardView.findViewById(R.id.card_listen);
            Button skipButton = (Button)cardView.findViewById(R.id.card_skip);


            word.setText(cursor.getString(cursor.getColumnIndex(DatabaseHelper.word_table_word)));

//            Toast.makeText(cardView.getContext(), "Working",Toast.LENGTH_SHORT ).show();
            speakButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String text = (String)word.getText();
                    speakable.speak(text);
                }
            });

            listenButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String text = (String)word.getText();
                    listenable.listen(cardView,text);
                }
            });

            skipButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    turnable.move();
                }
            });
        }
        else
        {
            Toast.makeText(ctx, "Cant Move", Toast.LENGTH_SHORT).show();
        }
    }



    @Override
    public int getItemCount() {
        return cursor==null?0:cursor.getCount();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private CardView cardView;

        public ViewHolder(CardView itemView) {
            super(itemView);
            cardView = itemView;
        }
    }
}
