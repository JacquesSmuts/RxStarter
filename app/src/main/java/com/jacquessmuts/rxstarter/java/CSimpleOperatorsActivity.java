package com.jacquessmuts.rxstarter.java;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.jacquessmuts.rxstarter.R;

import io.reactivex.Single;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class CSimpleOperatorsActivity extends BaseActivity {

    private Disposable singleDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView textView = findViewById(R.id.textView);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Single.just(getAGreeting())
                        .subscribe(new Consumer<String>() {
                            @Override
                            public void accept(String resultString) throws Exception {
                                textView.setText(resultString);
                            }
                        });

                //same as above, but much nicer in a lambda (Java8 required)
                Single.just(getAGreeting())
                        .subscribe(resultString -> textView.setText(resultString));

                //The warning above, "The result of subscribe is not used" happens with any subscription
                // that is not disposed of properly, because of potential memory leaks.
                // You should keep track and dispose of them in your OnDestroy method
                singleDisposable = Single.just(getAGreeting())
                        .subscribe(resultString -> textView.setText(resultString));

            }
        });
    }

    @Override
    protected void onDestroy() {

        //dispose of all your Subscriptions in onDestroy, always
        singleDisposable.dispose();

        super.onDestroy();
    }

    private String getAGreeting() {
        return "Hello ~World~ GDG";
    }

}
