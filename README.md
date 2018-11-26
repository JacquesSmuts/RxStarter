# RxStarter
This is a starter project for demonstrating RxJava2

You can download this app on the play store [here](https://play.google.com/store/apps/details?id=com.jacquessmuts.rxstarter)

## Getting into RxJava

Work your way through these links to get into RxJava.

| Link | Reason to Read |
| ------- | ---------|
| [Observer Pattern](https://en.wikipedia.org/wiki/Observer_pattern) | Background on Observer pattern and why a naive implementation can easily result in memory leaks. Luckily RxJava avoids this by design. |
| [ReactiveX Intro](http://reactivex.io/intro.html) | RX's official intro to Reactive Programming. You've probably seen this and it feels lacking. |
| [Functional reactive](https://android.jlelse.eu/how-to-wrap-your-imperative-brain-around-functional-reactive-programming-in-rxjava-91ac89a4eccf) | An article which will explain the differences in mindset between reactive and imperative programming |
| [Froussios Intro to RxJava](https://github.com/Froussios/Intro-To-RxJava) | A very definitive intro to RxJava basics. Must Read. | 
| [Froussios Intro to RxJava](https://github.com/Froussios/Intro-To-RxJava) | Seriously. This is the one. Read it. | 
| [Android Samples](https://github.com/amitshekhariitbhu/RxJava2-Android-Samples) | Samples of RxJava in Android. Ugly due to lack of Lambdas, but extensive examples. |
| [RxJava repo](https://github.com/ReactiveX/RxJava) | This is the official repo but the documentation here is actually not that great for starting out. Don't feel bad if this doens't make a lot of sense. |

## Useage
The app has a number of samples that you can play around with in the app. There are shorthand explanations on each screen plus more detailed information in the code comments.

Each activity has a Java and Kotlin equivalent. The Kotlin classes are not just conversions of the Java classes. The Kotlin versions contain additional nice things such as Kotlin Extension functions.

This table lists the places in the code where each RxJava function is called.

| Function  | Activities |
| ------------- | ------------- |
| .subscribe()  | All of them  |
| .map()  | Most of them  |
| .observeOn()  | Most of them  |
| RxView.clicks()  | Most of them  |
| .subscribeOn() | [LongApiCallActivity](https://github.com/JacquesSmuts/RxStarter/blob/master/app/src/main/java/com/jacquessmuts/rxstarter/java/sample/LongApiCallActivity.java) |
| .dispose()  | [LongApiCallActivity](https://github.com/JacquesSmuts/RxStarter/blob/master/app/src/main/java/com/jacquessmuts/rxstarter/java/sample/LongApiCallActivity.java) and [BaseActivity](https://github.com/JacquesSmuts/RxStarter/blob/master/app/src/main/java/com/jacquessmuts/rxstarter/java/BaseActivity.java) |
| .fromCallable()  | [LongApiCallActivity](https://github.com/JacquesSmuts/RxStarter/blob/master/app/src/main/java/com/jacquessmuts/rxstarter/java/sample/LongApiCallActivity.java)  |
| .interval()  | [LongApiCallActivity](https://github.com/JacquesSmuts/RxStarter/blob/master/app/src/main/java/com/jacquessmuts/rxstarter/java/sample/LongApiCallActivity.java)  |
| .throttleFirst()  | [ButtonRapidClickActivity](https://github.com/JacquesSmuts/RxStarter/blob/master/app/src/main/java/com/jacquessmuts/rxstarter/java/sample/ButtonRapidClickActivity.java)  |
| .scan()  | [ButtonRapidClickActivity](https://github.com/JacquesSmuts/RxStarter/blob/master/app/src/main/java/com/jacquessmuts/rxstarter/java/sample/ButtonRapidClickActivity.java)  |
| .distinct()  | [DistinctActivity](https://github.com/JacquesSmuts/RxStarter/blob/master/app/src/main/java/com/jacquessmuts/rxstarter/java/sample/DistinctActivity.java)  |
| .doOnError()  | [FailingApiActivity](https://github.com/JacquesSmuts/RxStarter/blob/master/app/src/main/java/com/jacquessmuts/rxstarter/java/sample/FailingApiCallActivity.java)  |
| .distinct()  | [DistinctActivity](https://github.com/JacquesSmuts/RxStarter/blob/master/app/src/main/java/com/jacquessmuts/rxstarter/java/sample/DistinctActivity.java)  |
| .merge()  | [ThreadSwitchActivity](https://github.com/JacquesSmuts/RxStarter/blob/master/app/src/main/java/com/jacquessmuts/rxstarter/java/sample/ThreadSwitchActivity.java)  |
| .mergeWith()  | [PinActivity](https://github.com/JacquesSmuts/RxStarter/blob/master/app/src/main/java/com/jacquessmuts/rxstarter/java/sample/PinActivity.java)  |
| .buffer()  | [PinActivity](https://github.com/JacquesSmuts/RxStarter/blob/master/app/src/main/java/com/jacquessmuts/rxstarter/java/sample/PinActivity.java)  |
| .donOnNext()  | [PinActivity](https://github.com/JacquesSmuts/RxStarter/blob/master/app/src/main/java/com/jacquessmuts/rxstarter/java/sample/PinActivity.java)  |
