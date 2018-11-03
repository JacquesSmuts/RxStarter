package com.jacquessmuts.rxstarter.java;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by jacquessmuts on 2018/11/03
 * This is an EventBus powered by RxJava. Not the recommended way of doing things, but works
 */
public class RxBus {

    private static final RxBus INSTANCE = new RxBus();

    public static RxBus getInstance() {
        return INSTANCE;
    }

    public RxBus() {
    }

    private PublishSubject<Object> publisher = PublishSubject.create();

    public <T> void publish(T o) {
        publisher.onNext(o);
    }

    // Listen should return an Observable and not the publisher
    // Using ofType we filter only events that match that class type
    public <T> Observable<T> listen(Class<T> eventType){
        return publisher
                .ofType(eventType)
                .hide(); //publisher can not be accessed directly
    }

}
