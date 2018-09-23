package com.jacquessmuts.rxstarter;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import org.reactivestreams.Subscription;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.internal.operators.single.SingleDoOnSuccess;

public class SingleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView textView = findViewById(R.id.textView);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() { //should be replaced with lambda
            @Override
            public void onClick(View v) {

                Single.just(getAGreeting())
                        .subscribe(new Consumer<String>() {
                            @Override
                            public void accept(String resultString) throws Exception {
                                textView.setText(resultString);
                            }
                        });

                //same as above, but much nicer in a lambda
                Single.just(getAGreeting())
                        .subscribe(resultString -> textView.setText(resultString));

                //The warning "The result of subscribe is not used" happens with Single, because of potential memory leaks
                //You can add .dispose() at the end, to dispose of the subscription when it's done, like so:
                Single.just(getAGreeting())
                        .subscribe(resultString -> textView.setText(resultString))
                        .dispose();

            }
        });
    }

    private String getAGreeting() {
        return "Hello ~World~ GDG";
    }

}
