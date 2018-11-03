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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.PublishSubject;
import timber.log.Timber;

public class ListActivity extends BaseActivity {

    //normally you would want to lazy-load these
    PublishSubject<String> timePublisher = PublishSubject.create(); //create a publisher you can publish to
    Observable<String> timeObservable = timePublisher.hide(); //create an observer which you can NOT publish to, but you can listen from
    private boolean isTimerRunning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        setupRecyclerView();
    }

    private void setupRecyclerView(){
        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        String[] listOfNumbers = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14"};

        recyclerView.setAdapter(new TimerAdapter(listOfNumbers, timeObservable, rxSubs));
    }

    @Override
    protected void onResume() {
        super.onResume();

        rxSubs.add(RxView.clicks(findViewById(R.id.button))
                .subscribe( tally -> {
                    startTimer();
                }, Timber::e));

    }

    private void startTimer(){
        if (isTimerRunning) return;
        isTimerRunning = true;

        rxSubs.add(Observable.interval(100, TimeUnit.MILLISECONDS) //emit 100 ms
                .map(i -> getTime()) //get the time as a string
                .subscribe( time -> {
                    timePublisher.onNext(time); //send the string to the publisher
                }, Timber::e));

        rxSubs.add(Observable.timer(5, TimeUnit.SECONDS)
                .subscribe( time -> {
                    findViewById(R.id.textViewExplanation).setVisibility(View.VISIBLE);
                    }, Timber::e));
    }

    private String getTime(){
        SimpleDateFormat fmt = new SimpleDateFormat("mm:ss.S");
        return fmt.format(new Date());
    }
}

/**
 * Normally this adapter would be extracted into its own file, but it's kept here for demonstrative purposes
 */
class TimerAdapter extends RecyclerView.Adapter<TimerAdapter.TimerViewHolder> {

    private String[] numbers;
    private Observable<String> timeObservable;
    private CompositeDisposable rxSubs; //this disposable is managed by the parent activity lifecycle.

    @Override
    public int getItemCount() {
        return numbers.length;
    }

    TimerAdapter(String[] myDataset, Observable<String> observable, CompositeDisposable parentDisposables) {
        numbers = myDataset;
        timeObservable = observable;
        rxSubs = parentDisposables;
    }

    @NonNull
    @Override
    public TimerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return TimerViewHolder.newInstance(parent, timeObservable, rxSubs);
    }

    @Override
    public void onBindViewHolder(@NonNull TimerViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.textViewNumber.setText(numbers[position]);
        holder.setTimerListener(timeObservable, rxSubs);
    }


    static class TimerViewHolder extends RecyclerView.ViewHolder {

        //This disposable is used to prevent memory leaks
        private Disposable disposable;

        static TimerViewHolder newInstance(ViewGroup parent, Observable<String> timeObservable, CompositeDisposable rxSubs){
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.viewholder_timer, parent, false);
            return new TimerViewHolder(view);
        }

        TextView textViewNumber;
        final TextView textViewTime;
        TimerViewHolder(View v) {
            super(v);
            textViewNumber = v.findViewById(R.id.textViewName);
            textViewTime = v.findViewById(R.id.textViewTime);
        }

        void setTimerListener(Observable<String> timeObservable, CompositeDisposable rxSubs){

            //if the disposable already exists, that means the ViewHolder is being recycled by recyclerview
            if (disposable != null && !disposable.isDisposed()){
                //So delete it out of the list of disposables in BaseActivity and dispose of it.
                rxSubs.delete(disposable);
                disposable.dispose();
            }

            //
            disposable = timeObservable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(textViewTime::setText, Timber::e);

            rxSubs.add(disposable);
        }


    }

}