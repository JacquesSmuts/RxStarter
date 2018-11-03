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
import com.jacquessmuts.rxstarter.java.RxBus;
import com.jakewharton.rxbinding2.view.RxView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

/**
 * This class is identical to ListActivity, except it uses RxBus instead of passing publishers/observables around.
 *
 * I prefer to avoid EventBus if I can, but it can be useful.
 */
public class ListBusActivity extends BaseActivity {

    private boolean isTimerRunning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        TextView textViewExplanation = findViewById(R.id.textViewExplanation);
        textViewExplanation.setText(R.string.explanation_bus_list);

        setupRecyclerView();
    }

    private void setupRecyclerView(){
        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        int LENGTH = 10000;
        String[] listOfNumbers = new String[LENGTH];
        for (int i = 0; i<LENGTH; i++){
            listOfNumbers[i] = String.valueOf(i);
        }

        recyclerView.setAdapter(new TimerBusAdapter(listOfNumbers, rxSubs));
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
        if (isTimerRunning) {
            findViewById(R.id.textViewExplanation).setVisibility(View.VISIBLE);
            return;
        }
        isTimerRunning = true;

        rxSubs.add(Observable.interval(7, TimeUnit.MILLISECONDS) //emit at 144 frames per second
                .map(i -> getTime()) //get the time as a string
                .subscribe( time -> {
                    RxBus.getInstance().publish(time); //send the string to the eventbus
                }, Timber::e));
    }

    private String getTime(){
        SimpleDateFormat fmt = new SimpleDateFormat("hh:mm:ss.SSS");
        return fmt.format(new Date());
    }
}

/**
 * Normally this adapter would be extracted into its own file, but it's kept here for demonstrative purposes
 */
class TimerBusAdapter extends RecyclerView.Adapter<TimerBusAdapter.TimerViewHolder> {

    private String[] numbers;
    private CompositeDisposable rxSubs;

    @Override
    public int getItemCount() {
        return numbers.length;
    }

    TimerBusAdapter(String[] myDataset, CompositeDisposable rxSubs) {
        numbers = myDataset;
        this.rxSubs = rxSubs;
    }

    @NonNull
    @Override
    public TimerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return TimerViewHolder.newInstance(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull TimerViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.textViewNumber.setText(numbers[position]);
        holder.setTimerListener(rxSubs);
    }


    static class TimerViewHolder extends RecyclerView.ViewHolder {

        //This disposable is used to prevent memory leaks
        private Disposable disposable;

        static TimerViewHolder newInstance(ViewGroup parent){
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

        void setTimerListener(CompositeDisposable rxSubs){

            if (disposable != null){
                rxSubs.delete(disposable);
                disposable.dispose();
            }

            disposable = RxBus.getInstance()
                    .listen(String.class)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(textViewTime::setText, Timber::e);

            rxSubs.add(disposable);
        }


    }

}