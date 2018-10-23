package com.jacquessmuts.rxstarter.java.sample;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jacquessmuts.rxstarter.R;
import com.jacquessmuts.rxstarter.java.BaseActivity;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.concurrent.TimeoutException;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class LongLoadingActivity extends BaseActivity {

    private boolean apiCallOngoing = false;

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

        //merge both buttons's emissions and tally the total number of clicks between them both
        rxSubs.add(RxView.clicks(button)
                .map(input -> 1).scan((total, nuValue) -> total + nuValue) //keep a running tally.
                .subscribe( tally -> {
                    doApiCall();
                    if (tally > 1){
                        textViewExplanation.setVisibility(View.VISIBLE);
                    }
                }, Timber::e));

    }

    private void doApiCall() {

        if (apiCallOngoing){
            return;
        }
        apiCallOngoing = true;

        //Add an observable to change the textview while the api call is loading
        TextView textView = findViewById(R.id.textView);
        ProgressBar progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        textView.setText("");

        //if you add the api call to an rxSub disposable, the api call's result will be ignored if the activity closes
        rxSubs.add(Observable.just(true)
                .observeOn(Schedulers.computation())
                .map(input -> getRandomNumberSlowly()) //do the slow api call which could fail
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(error -> {
                    if (error instanceof TimeoutException){
                        setRandomText();
                    } else {
                        finishWithMessage(error.getLocalizedMessage());
                    }
                })
                .retryWhen( error -> error.flatMap(this::checkResponseType) ) //retry the api call if the error is the right type
                .subscribe( result -> {
                    finishWithMessage("SUCCESS");
                }, Timber::e));
    }

    private void finishWithMessage(String message) {
        apiCallOngoing = false;
        TextView textView = findViewById(R.id.textView);
        ProgressBar progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        textView.setText(message);
    }

    private void setRandomText(){
        TextView textView = findViewById(R.id.textView);

        String[] messages = getResources().getStringArray(R.array.loading_messages);
        textView.setText(messages[getRandomNumber()]);
    }


    /**
     * Returns a random number from 1-10, after a 1500 miillisecond wait.
     * Simulates an api call which keeps failing
     * @return a random int or throws an exception
     */
    private int getRandomNumberSlowly() throws TimeoutException, UnsupportedOperationException {
        try {

            Thread.sleep(getRandomNumber()*1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        int number = getRandomNumber();
        if (number < 2){
            throw new UnsupportedOperationException("Invalid Api Call");
        } else if (number < 8){
            throw new TimeoutException();
        }
        Timber.d("%s", number);
        return number;
    }

    private int getRandomNumber() {
        return (int) (Math.random() * (10));
    }

    Observable<Boolean> checkResponseType(Throwable response ) {
        if ( response instanceof TimeoutException ) {
            return Observable.just( Boolean.TRUE );
        }
        return Observable.error( response );
    }
}
