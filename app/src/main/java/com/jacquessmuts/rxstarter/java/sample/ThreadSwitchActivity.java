package com.jacquessmuts.rxstarter.java.sample;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.jacquessmuts.rxstarter.R;
import com.jacquessmuts.rxstarter.java.BaseActivity;
import com.jakewharton.rxbinding2.view.RxView;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class ThreadSwitchActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thread_switch);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Button buttonNoThreading = findViewById(R.id.buttonNoThreading);
        Button buttonGoodThreading = findViewById(R.id.buttonGoodThreading);

        TextView textView = findViewById(R.id.textView);



        rxSubs.add(RxView.clicks(buttonNoThreading)
                .subscribe( random -> {
                    //set tally to textview
                    textView.setText(String.valueOf(getRandomNumber()));
                }, Timber::e));


        rxSubs.add(RxView.clicks(buttonGoodThreading)
                .observeOn(Schedulers.computation()) //following functions will be on computation thread
                .map(input -> getRandomNumber())
                .observeOn(AndroidSchedulers.mainThread()) //following functions will be on ui thread
                .subscribe( random -> {
                    textView.setText(String.valueOf(random));
                }, Timber::e));

    }

    private int getRandomNumber(){
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return (int) (Math.random() * (1000 + 1));
    }
}
