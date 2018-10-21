package com.jacquessmuts.rxstarter.java.sample;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jacquessmuts.rxstarter.R;
import com.jacquessmuts.rxstarter.java.BaseActivity;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.concurrent.TimeUnit;

import timber.log.Timber;

public class ButtonClickActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_button_click);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Button button = findViewById(R.id.button);
        TextView textView = findViewById(R.id.textView);
        TextView textViewExplanation = findViewById(R.id.textViewExplanation);

        rxSubs.add(RxView.clicks(button) //get clicks
                .throttleFirst(2000, TimeUnit.MILLISECONDS) //only once per 500ms
                .map( input -> 1) //emit clicks as 1
                .scan((total, nuValue) -> total + nuValue) //keep a running tally.
                .subscribe( tally -> {
                    //set tally to textview
                    textView.setText(String.valueOf(tally));
                    if (tally > 1){
                        textViewExplanation.setVisibility(View.VISIBLE);
                    }
                }, Timber::e));

    }
}
