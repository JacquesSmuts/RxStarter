package com.jacquessmuts.rxstarter.java.sample;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.widget.TextView;

import com.jacquessmuts.rxstarter.R;
import com.jacquessmuts.rxstarter.java.BaseActivity;

import io.reactivex.Single;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class SingleActivity extends BaseActivity {

    private Disposable singleDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single);

        TextView textView = findViewById(R.id.textView);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(v -> {

            // A single is the simplest of Observables. It runs once and completes with a result.
            Single.just(getAGreeting())
                    .subscribe(new Consumer<String>() {
                        @Override
                        public void accept(String resultString) throws Exception {
                            textView.setText(resultString);
                        }
                    });

            // This is the same as above, but much nicer in a lambda (Java8 required)
            Single.just(getAGreeting())
                    .subscribe(resultString -> textView.setText(resultString));

            /* The warning above, "The result of subscribe is not used" happens with any subscription
             * that is not disposed of properly, because of potential memory leaks.
             * You should keep track and dispose of them in your OnDestroy method
             */
            singleDisposable = Single.just(getAGreeting())
                    .subscribe(resultString -> textView.setText(resultString));

        });
    }

    @Override
    protected void onDestroy() {

        // Dispose of all your Subscriptions in onDestroy, always.
        if (singleDisposable != null) {
            singleDisposable.dispose();
        }

        // But it's easiest to use rxSubs, which we declare and manage in BaseActivity.

        super.onDestroy();
    }

    private String getAGreeting() {
        return "Hello World. \n\n\nThis activity shows the simplest possible Observable. A \'Single\' which runs when you press that button.";
    }

}
