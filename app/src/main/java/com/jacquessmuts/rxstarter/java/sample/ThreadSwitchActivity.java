package com.jacquessmuts.rxstarter.java.sample;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.jacquessmuts.rxstarter.R;
import com.jacquessmuts.rxstarter.java.BaseActivity;
import com.jakewharton.rxbinding2.view.RxView;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import timber.log.Timber;

public class ThreadSwitchActivity extends BaseActivity {

    // A PublishSubject is a hot observable. You can send emissions to it, and it will propagate to all current observers
    private PublishSubject<Integer> buttonsClickedPublisher = PublishSubject.create();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thread_switch);
    }

    @Override
    protected void onResume() {
        super.onResume();

        TextView textView = findViewById(R.id.textView);
        TextView textViewExplanation = findViewById(R.id.textViewExplanation);

        // this is the same as just setting the onClick normally, inside a try{}catch{]
        rxSubs.add(RxView.clicks(findViewById(R.id.buttonNoThreading))
                .subscribe( input -> {
                    textView.setText(String.valueOf(getRandomNumber()));
                    buttonsClickedPublisher.onNext(1); // this is just for tallying up the clicks
                }, Timber::e));

        // This is the good code. It does threading well
        rxSubs.add(RxView.clicks(findViewById(R.id.buttonGoodThreading))
                .observeOn(Schedulers.computation()) // all following functions will be on computation thread
                .map(input -> getRandomNumber())
                .observeOn(AndroidSchedulers.mainThread()) // all following functions will be on ui thread
                .subscribe( random -> {
                    textView.setText(String.valueOf(random));
                    buttonsClickedPublisher.onNext(1); // this is just for tallying up the clicks
                }, Timber::e));

        // Merge both buttons's emissions and tally the total number of clicks between them both
        rxSubs.add(buttonsClickedPublisher
                .scan((total, nuValue) -> total + nuValue) // keep a running tally.
                .subscribe( tally -> {
                    if (tally > 3){
                        textViewExplanation.setVisibility(View.VISIBLE);
                    }
                }, Timber::e));


    }

    /**
     * Returns a random number from 1-1000, after a 1500 miillisecond wait.
     * Simulates a long-running calculation
     * @return a random int from 0-1000
     */
    private int getRandomNumber(){
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return (int) (Math.random() * (1000 + 1));
    }
}
