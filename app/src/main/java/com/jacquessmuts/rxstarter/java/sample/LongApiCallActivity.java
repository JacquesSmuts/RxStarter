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
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class LongApiCallActivity extends BaseActivity {

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
        textViewExplanation.setText(R.string.explanation_long_load);

        rxSubs.add(RxView.clicks(button)
                .subscribe( any -> {
                    doApiCall();
                    textViewExplanation.setVisibility(View.VISIBLE);
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

        // intervalDisposable sets Random Text every 3 seconds, forever, unless we dispose of it
        Disposable intervalDisposable = Observable.interval(3, 3, TimeUnit.SECONDS)
                .map(input -> getRandomNumber()) //get a random number
                .subscribe(randomNumber -> {
                    setRandomText(randomNumber); //uses random number to choose a message. Can be replaced with method call.
                 }, Timber::e);

        // The timer will stop once rxSubs gets cleared in [BaseActivity]'s onPause method.
        rxSubs.add(intervalDisposable);

        /*
         * Notice how the method of getting the randomNumber call onto a different thread is
         * different from the one in [com.jacquessmuts.rxstarter.java.sample.FailingApiCallActivity].
         * RxJava usually has more than one way to do the same thing
         */
        rxSubs.add(Observable.fromCallable(this::getRandomNumberSlowly)
                .subscribeOn(Schedulers.computation()) // the initial callable is put on the computation thread
                .observeOn(AndroidSchedulers.mainThread()) // all following parts of this chain will be on UiThread
                .subscribe( result -> {
                    finishWithMessage("SUCCESS! Result = " + result);
                    intervalDisposable.dispose(); // stop the interval by disposing of it
                }, error -> {
                    Timber.e(error);
                    intervalDisposable.dispose(); // stop the interval by disposing of it
                }));

    }

    private void finishWithMessage(String message) {
        apiCallOngoing = false;
        TextView textView = findViewById(R.id.textView);
        ProgressBar progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        textView.setText(message);
    }

    private void setRandomText(int number){
        TextView textView = findViewById(R.id.textView);

        String[] messages = getResources().getStringArray(R.array.loading_messages);
        textView.setText(messages[number]);
    }
    
    /**
     * Returns a random number from 1-10, after 4-40 seconds
     * Simulates an api call which keeps failing
     * @return a random int or throws an exception
     */
    private int getRandomNumberSlowly() {
        try {
            Thread.sleep(getRandomNumber()*4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return getRandomNumber();
    }

    private int getRandomNumber() {
        return (int) (Math.random() * 9 + 1);
    }

}
