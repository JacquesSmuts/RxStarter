package com.jacquessmuts.rxstarter.java.sample;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jacquessmuts.rxstarter.R;
import com.jacquessmuts.rxstarter.java.BaseActivity;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class DistinctActivity extends BaseActivity {

    private boolean calculationOngoing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_long_load);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Button button = findViewById(R.id.button);

        TextView textViewExplanation = findViewById(R.id.textViewExplanation);
        textViewExplanation.setText(R.string.explanation_distinct);

        rxSubs.add(RxView.clicks(button)
                .subscribe( tally -> {
                    getDistinctValues();
                    textViewExplanation.setVisibility(View.VISIBLE);
                }, Timber::e));

    }

    private void getDistinctValues() {

        if (calculationOngoing){
            return;
        }
        calculationOngoing = true;

        // Add an observable to change the textview while the api call is loading
        TextView textView = findViewById(R.id.textView);
        ProgressBar progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        textView.setText("");


        rxSubs.add(Observable.interval(200, 200, TimeUnit.MILLISECONDS)
                .observeOn(Schedulers.computation())  // all following parts of this chain will be on computation thread
                .map(input -> getRandomNumber())
                .distinct()
                .observeOn(AndroidSchedulers.mainThread()) // all following parts of this chain will be on UiThread
                .subscribe( result -> {
                    textView.append(result + " "); // append value to textview\
                }, Timber::e));

    }

    private int getRandomNumber() {
        return (int) (Math.random() * 20 + 1);
    }

}
