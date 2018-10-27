package com.jacquessmuts.rxstarter.java.sample;

import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jacquessmuts.rxstarter.R;
import com.jacquessmuts.rxstarter.java.BaseActivity;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class PinActivity extends BaseActivity {

    private Vibrator vibrator;
    List<Integer> savedPin = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin);

        try {
            vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        } catch (Throwable ignored) {
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        Button button1 = findViewById(R.id.button1);
        Button button2 = findViewById(R.id.button2);
        Button button3 = findViewById(R.id.button3);
        Button button4 = findViewById(R.id.button4);
        Button button5 = findViewById(R.id.button5);
        Button button6 = findViewById(R.id.button6);
        Button button7 = findViewById(R.id.button7);
        Button button8 = findViewById(R.id.button8);
        Button button9 = findViewById(R.id.button9);
        TextView textView = findViewById(R.id.textView);
        TextView textViewExplanation = findViewById(R.id.textViewExplanation);


        rxSubs.add(RxView.clicks(button1).map(in-> 1)
                .mergeWith(RxView.clicks(button2).map(in->2)) //Each button emits its own mapped value into the same stream
                .mergeWith(RxView.clicks(button3).map(in->3))
                .mergeWith(RxView.clicks(button4).map(in->4))
                .mergeWith(RxView.clicks(button5).map(in->5))
                .mergeWith(RxView.clicks(button6).map(in->6))
                .mergeWith(RxView.clicks(button7).map(in->7))
                .mergeWith(RxView.clicks(button8).map(in->8))
                .mergeWith(RxView.clicks(button9).map(in->9))
                .doOnNext(output -> button1.post(() -> { //do on next happens after each emission, regardless of result or filter
                    //This happens AFTER subscribe
                    if (vibrator != null) {
                        vibrator.vibrate(100 * output); //vibrates phone each click
                    }
                }))
                .map(input-> {
                    textView.setText(""); //clear the textview
                    return input; //map is an easy way to cause a side-effect BEFORE a filter or subscribe
                })
                .buffer(4) //only emits once 4 buttons are clicked, then collects them in a list<>
                .subscribe( pin -> { //final result is a list of items, each item being a list of 4 ints

                    if (pin.size() != 4) throw new IllegalStateException(); //buffer should prevent this from ever happening

                    textViewExplanation.setVisibility(View.VISIBLE);
                    if (pin.equals(savedPin)){
                        textView.setText("PIN MATCHES PREVIOUS");
                    } else {
                        savedPin = pin;
                        textView.setText("****");
                    }

                }, Timber::e));

    }
}
