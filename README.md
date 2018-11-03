# RxStarter
This is a starter project for demonstrating RxJava2

You can download this app on the play store [here](link incoming)

## Useage
The app has a number of samples that you can play around with in the app. There are shorthand explanations on each screen plus more detailed information in the code comments.

Each activity has a Java and Kotlin equivalent. The Kotlin classes are not just conversions of the Java classes. The Kotlin versions contain addoitional nice things such as Kotlin Extension functions.

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
