package com.jacquessmuts.rxstarter.java.sample;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jacquessmuts.rxstarter.R;
import com.jacquessmuts.rxstarter.java.BaseActivity;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.PublishSubject;
import timber.log.Timber;

public class ListActivity extends BaseActivity {

    //normally you would want to lazy-load these
    PublishSubject<String> textPublisher = PublishSubject.create(); //create a publisher you can publish to and subscribe to
    Observable<String> textObservable = textPublisher.hide(); //create an observer which you can NOT publish to, but you can subscribe to
    private boolean isTimerRunning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        setupRecyclerView();
    }

    private void setupRecyclerView(){
        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        int LENGTH = 10000;
        String[] listOfNumbers = new String[LENGTH];
        for (int i = 0; i<LENGTH; i++){
            listOfNumbers[i] = String.valueOf(i);
        }

        recyclerView.setAdapter(new CounterAdapter(listOfNumbers, textObservable, rxSubs));
    }

    @Override
    protected void onResume() {
        super.onResume();

        rxSubs.add(RxView.clicks(findViewById(R.id.button))
                .subscribe( any -> startTimer(), Timber::e));
    }

    private void startTimer(){
        if (isTimerRunning) {
            findViewById(R.id.textViewExplanation).setVisibility(View.VISIBLE);
            return;
        }
        isTimerRunning = true;

        rxSubs.add(Observable.interval(7, TimeUnit.MILLISECONDS) //emit 144 times (frames) per second
                .map(i -> 1)
                .scan((total, nuValue) -> total + nuValue)
                .subscribe( total -> {
                    textPublisher.onNext(numberToString(total)); //send the string to the publisher
                }, Timber::e));
    }

    /**
     * This function takes an input number, and turns it into a base-144 numbering system, because
     * we expect about 144 updates per second.
     * @param startingNumber any number smaller than Integer.MAX_VALUE
     * @return a String representing a base 144 counting system
     */

    private String numberToString(int startingNumber) {
        int number = startingNumber;
        int remainder;
        StringBuilder toReturn = new StringBuilder();
        while (number > 0){
            int maxNumber = 144;
            remainder = number % maxNumber;
            toReturn.append(Character.toString((char) (remainder+8800)));
            number /= maxNumber;
        }
        return toReturn.reverse().toString();
    }
}

/**
 * Normally this adapter would be extracted into its own file, but it's kept here for demonstrative purposes
 */
class CounterAdapter extends RecyclerView.Adapter<CounterAdapter.CounterViewHolder> {

    private String[] numbers;
    private Observable<String> timeObservable;
    private CompositeDisposable rxSubs; //this disposable is managed by the parent activity lifecycle.

    @Override
    public int getItemCount() {
        return numbers.length;
    }

    CounterAdapter(String[] myDataset, Observable<String> observable, CompositeDisposable parentDisposables) {
        numbers = myDataset;
        timeObservable = observable;
        rxSubs = parentDisposables;
    }

    @NonNull
    @Override
    public CounterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return CounterViewHolder.newInstance(parent, timeObservable, rxSubs);
    }

    @Override
    public void onBindViewHolder(@NonNull CounterViewHolder holder, int position) {
        holder.textViewNumber.setText(numbers[position]);
        holder.setTimerListener(timeObservable, rxSubs);
    }


    static class CounterViewHolder extends RecyclerView.ViewHolder {

        // This disposable is used along with rxSubs to prevent memory leaks
        private Disposable disposable;

        static CounterViewHolder newInstance(ViewGroup parent, Observable<String> timeObservable, CompositeDisposable rxSubs){
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.viewholder_timer, parent, false);
            return new CounterViewHolder(view);
        }

        TextView textViewNumber;
        final TextView textViewTime;
        CounterViewHolder(View v) {
            super(v);
            textViewNumber = v.findViewById(R.id.textViewName);
            textViewTime = v.findViewById(R.id.textViewTime);
        }

        /**
         * This method cleans up prior observables on this viewholder and sets new ones
         * @param textObservable the observable which will emit 144 strings per second
         * @param rxSubs the compositedisposable managed by the parent activity's lifecycle.
         */
        void setTimerListener(Observable<String> textObservable, CompositeDisposable rxSubs){

            //if the disposable already exists, that means the ViewHolder is being recycled by recyclerview
            if (disposable != null && !disposable.isDisposed()){
                //So delete it out of the list of disposables in BaseActivity and dispose of it.
                rxSubs.delete(disposable);
                disposable.dispose();
            }

            // Now we can set a new disposable/subscription to update the textview on each emission
            disposable = textObservable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(textViewTime::setText, Timber::e);

            //and make sure the subscription is disposed of if the activity's lifecycle requires it
            rxSubs.add(disposable);
        }


    }

}