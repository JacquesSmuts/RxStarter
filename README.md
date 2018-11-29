
[![CircleCI](https://circleci.com/gh/JacquesSmuts/RxStarter/tree/master.svg?style=svg)](https://circleci.com/gh/JacquesSmuts/RxStarter/tree/master) [![ktlint](https://img.shields.io/badge/code%20style-%E2%9D%A4-FF4081.svg)](https://ktlint.github.io/) [![codecov](https://codecov.io/gh/JacquesSmuts/RxStarter/branch/master/graph/badge.svg)](https://codecov.io/gh/JacquesSmuts/RxStarter)

# RxStarter
This is a starter project for demonstrating RxJava2

You can download this app on the play store [here](https://play.google.com/store/apps/details?id=com.jacquessmuts.rxstarter)

## Getting into RxJava

Work your way through these links to get into RxJava.

| Link | Reason to Read |
| ------- | ---------|
| [Observer Pattern](https://en.wikipedia.org/wiki/Observer_pattern) | Background on Observer pattern and why a naive implementation can easily result in memory leaks. Luckily RxJava avoids this by design. |
| [ReactiveX Intro](http://reactivex.io/intro.html) | RX's official intro to Reactive Programming. You've probably seen this and it feels lacking. |
| [Functional Reactive](https://android.jlelse.eu/how-to-wrap-your-imperative-brain-around-functional-reactive-programming-in-rxjava-91ac89a4eccf) | An article which will explain the differences in mindset between reactive and imperative programming |
| [Froussios Intro to RxJava](https://github.com/Froussios/Intro-To-RxJava) | A very definitive intro to RxJava basics. Must Read. | 
| [Froussios Intro to RxJava](https://github.com/Froussios/Intro-To-RxJava) | Seriously. This is the one. Read it. | 
| [Android Samples](https://github.com/amitshekhariitbhu/RxJava2-Android-Samples) | Samples of RxJava in Android. Ugly due to lack of Lambdas, but extensive examples. |
| [RxJava repo](https://github.com/ReactiveX/RxJava) | This is the official repo but the documentation here is actually not that great for starting out. Don't feel bad if this doens't make a lot of sense. |

## Usage
The app has a number of samples that you can play around with in the app. There are shorthand explanations on each screen plus more detailed information in the code comments.

Each activity has a Java and Kotlin equivalent. The Kotlin classes are not just conversions of the Java classes. The Kotlin versions contain additional nice things such as Kotlin Extension functions.

The below tables lists the places in the code where each RxJava function is called. Observable Creation shows how you can go about creating an Observable that emits items.

| Observable Creation  | Description | Example |
| ------------- | ------------- | ------------- |
| RxView.clicks() | Creates an Observable out of clicks on any View | Most of them. [ButtonRapidClickActivity](https://github.com/JacquesSmuts/RxStarter/blob/master/app/src/main/java/com/jacquessmuts/rxstarter/java/sample/ButtonRapidClickActivity.java)  |
| .just() | Create an Observable/Single from a function which returns a result. The function is fired once and the result retained. Easily used as substitute for Async. | [SingleActivity](https://github.com/JacquesSmuts/RxStarter/blob/master/app/src/main/java/com/jacquessmuts/rxstarter/java/sample/SingleActivity.java) |
| .fromCallable() | Create an Observable/Single from a function which returns a result. The function is fired only after subscribing, and again with initial subscriptions. Easily used as substitute for Async. | [LongApiCallActivity](https://github.com/JacquesSmuts/RxStarter/blob/master/app/src/main/java/com/jacquessmuts/rxstarter/java/sample/LongApiCallActivity.java)  |
| .interval() | Create an observable which emits an object every x milliseconds | [LongApiCallActivity](https://github.com/JacquesSmuts/RxStarter/blob/master/app/src/main/java/com/jacquessmuts/rxstarter/java/sample/LongApiCallActivity.java), [DistinctActivity](https://github.com/JacquesSmuts/RxStarter/blob/master/app/src/main/java/com/jacquessmuts/rxstarter/java/sample/DistinctActivity.java)  |
| .merge() | Merge two or more observables into one. Useful for running various events through the same filter. | [ThreadSwitchActivity](https://github.com/JacquesSmuts/RxStarter/blob/master/app/src/main/java/com/jacquessmuts/rxstarter/java/sample/ThreadSwitchActivity.java)  |
| .mergeWith() | Same as merge, just a different syntax. | [PinActivity](https://github.com/JacquesSmuts/RxStarter/blob/master/app/src/main/java/com/jacquessmuts/rxstarter/java/sample/PinActivity.java)  |
| PublishSubject.create() | Create an Observable you can send events to and subscribe to. | [ListActivity](https://github.com/JacquesSmuts/RxStarter/blob/master/app/src/main/java/com/jacquessmuts/rxstarter/java/sample/ListActivity.java), [ThreadSwitchActivity](https://github.com/JacquesSmuts/RxStarter/blob/master/app/src/main/java/com/jacquessmuts/rxstarter/java/sample/ThreadSwitchActivity.java) |
| publisher.hide() | Turn a PublishSubject such as the one above into an observable that you can only listen/subscribe to. | [ListActivity](https://github.com/JacquesSmuts/RxStarter/blob/master/app/src/main/java/com/jacquessmuts/rxstarter/java/sample/ListActivity.java) |

The Rx Operators Table list operators that can be applied to an rxJava chain.

| Rx Operators  | Description | Example | 
| ------------- | ------------- | ------------- |
| .subscribe()  | final action to take for each event. Can contain onNext(), onError() and/or onComplete() | All of them  |
| .map() | Turn a given input into something else. | Most of them. [ButtonRapidClickActivity](https://github.com/JacquesSmuts/RxStarter/blob/master/app/src/main/java/com/jacquessmuts/rxstarter/java/sample/ButtonRapidClickActivity.java)  |
| .observeOn() | Switches between threads, such as background and UI thread. | Most of them, but especially [ThreadSwitchActivity](https://github.com/JacquesSmuts/RxStarter/blob/master/app/src/main/java/com/jacquessmuts/rxstarter/java/sample/ThreadSwitchActivity.java)  |
| .subscribeOn() | Switches the initial Observable to a specified thread. | [LongApiCallActivity](https://github.com/JacquesSmuts/RxStarter/blob/master/app/src/main/java/com/jacquessmuts/rxstarter/java/sample/LongApiCallActivity.java) |
| .dispose() | Clean up a subscription for Garbage Collection.  Essential for preventing memory leaks. | [LongApiCallActivity](https://github.com/JacquesSmuts/RxStarter/blob/master/app/src/main/java/com/jacquessmuts/rxstarter/java/sample/LongApiCallActivity.java), [BaseActivity](https://github.com/JacquesSmuts/RxStarter/blob/master/app/src/main/java/com/jacquessmuts/rxstarter/java/BaseActivity.java) |
| .throttleFirst() | Filters out any subsequent emissions past the first one in a given timeframe. | [ButtonRapidClickActivity](https://github.com/JacquesSmuts/RxStarter/blob/master/app/src/main/java/com/jacquessmuts/rxstarter/java/sample/ButtonRapidClickActivity.java)  |
| .scan() | Keeps a single object which can be changed by each subsequent emission. Usually to keep a running tally.  | [ButtonRapidClickActivity](https://github.com/JacquesSmuts/RxStarter/blob/master/app/src/main/java/com/jacquessmuts/rxstarter/java/sample/ButtonRapidClickActivity.java)  |
| .distinct() | Filters out any emission which has occurred in the past. | [DistinctActivity](https://github.com/JacquesSmuts/RxStarter/blob/master/app/src/main/java/com/jacquessmuts/rxstarter/java/sample/DistinctActivity.java)  |
| .distinct() | Filters out any emission which has occurred in the past. | [DistinctActivity](https://github.com/JacquesSmuts/RxStarter/blob/master/app/src/main/java/com/jacquessmuts/rxstarter/java/sample/DistinctActivity.java)  |
| .buffer() | Keep emissions from reaching downstream until a given number or time has been reached. Useful for batching items. | [PinActivity](https://github.com/JacquesSmuts/RxStarter/blob/master/app/src/main/java/com/jacquessmuts/rxstarter/java/sample/PinActivity.java)  |
| .donOnNext() | Do something on each emission which doesn't affect the stream. Useful for side-effects or for doing something regardless of whether the end-result is filtered out or not. | [PinActivity](https://github.com/JacquesSmuts/RxStarter/blob/master/app/src/main/java/com/jacquessmuts/rxstarter/java/sample/PinActivity.java)  |
| .doOnError() | If there is any error, determines what should be done before reaching .subscribe() as an error | [FailingApiActivity](https://github.com/JacquesSmuts/RxStarter/blob/master/app/src/main/java/com/jacquessmuts/rxstarter/java/sample/FailingApiCallActivity.java)  |
