Clean Architecture with functional programming in mind
===

This project mainly created to show how we can use functional programming's principles in an Android project with Clean 
Architecture to achieve separation of concerns. I'll complete this README soon.

Functional programming's principles
---
The principles that I want to support here are
- Immutability:
  Where we use `data class` and `val` keyword of `Kotlin` to achieve it.
- Lazy Evaluation:
  Where we use `FlowableTransformer` and `Flowable` of `RxJava` to simulate it.

The principle that I ignored in this project:
- Referential transparency (for a given input, you get the same output always with no side-effects):
  Where is not applicable in this project because main functions are dependent on the response of the server
  and are subject to change.

Modules
---
The same as the previous projects in my github account, this project implements the Clean Architecture too. As you can
read from [https://blog.cleancoder.com](http://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)
the Clean Architecture has an onion shape structure like this

![clean-architecture](https://blog.cleancoder.com/uncle-bob/images/2012-08-13-the-clean-architecture/CleanArchitecture.jpg)

In this project we manage it like this

- `domain` module: includes the `Entity` and `Use Cases`.
- `data` module: includes the `Gateways`, `DB`, `External Interfaces` and `Device`.
- `presentation` modules includes: the `MVVM` architectural pattern, which is the `UI` and `Presenters` in the diagram above.
- `app` module: integrates all classes and assemble the apk file. The modules and providers of Dagger framework live here.

By applying the DIP, dependency inversion principle, both `data` and `presentation` modules depends on the `domain`
module as the heart of the app. Also, the `app` module have to depend on every other modules.

Notice
---
Here I use `Flowable` to implement `LiveData` in `ViewModule`s by using a class named `RxLifecycleHandler`, because the
Google implementation of `LiveData` is not handling back pressure and is not a good fit to our FP style.

Checks
---
To run all the tests and checks please run `./gradlew clean check`.