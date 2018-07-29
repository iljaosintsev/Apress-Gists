package com.turlir.abakgists;

import org.junit.Test;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DefaultObserver;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.observers.ResourceObserver;
import io.reactivex.subscribers.DefaultSubscriber;
import io.reactivex.subscribers.ResourceSubscriber;

public class RxTest {

    //region Observable

    @Test
    public void rawObserver() {
        Observable.range(1, 5)
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        System.out.println("onSubscribe!");
                    }
                    @Override
                    public void onNext(Integer t) {
                        System.out.println(t);
                    }
                    @Override
                    public void onError(Throwable t) {
                        t.printStackTrace();
                    }
                    @Override
                    public void onComplete() {
                        System.out.println("Done!");
                    }
                });
    }

    @Test
    public void defaultObserver() {
        Observable.range(1, 5)
                .subscribe(new DefaultObserver<Integer>() {
                    @Override
                    public void onStart() {
                        System.out.println("Start!");
                    }
                    @Override
                    public void onNext(Integer t) {
                        if (t == 3) cancel(); // different: self-stoppable
                        System.out.println(t);
                    }
                    @Override
                    public void onError(Throwable t) {
                        t.printStackTrace();
                    }
                    @Override
                    public void onComplete() {
                        System.out.println("Done!");
                    }
                });
    }

    @Test
    public void resourceObserver() {
        ResourceObserver<Integer> resource = new ResourceObserver<Integer>() {
            @Override
            public void onNext(Integer t) {
                System.out.println(t);
            }

            @Override
            public void onError(Throwable t) {
                t.printStackTrace();
            }

            @Override
            public void onComplete() {
                System.out.println("Done!");
            }
        };
        Observable.range(1, 5).subscribeWith(resource);

        resource.add(
                Observable.range(6, 8).subscribe(integer -> {
                    System.out.println("onNext " + integer);
                })
        );

        resource.dispose();
        Observable.range(1, 5).subscribe(resource); // doesn't work
    }

    @Test
    public void disposableObserver() {
        DisposableObserver disposableObserver = Observable.range(1, 10)
                .subscribeWith(new DisposableObserver<Integer>() {
                    @Override
                    public void onNext(Integer t) {
                        System.out.println(t);
                    }
                    @Override
                    public void onError(Throwable t) {
                        t.printStackTrace();
                    }
                    @Override
                    public void onComplete() {
                        System.out.println("Done!");
                    }
                });
        CompositeDisposable acc = new CompositeDisposable();
        acc.add(disposableObserver);
        acc.clear();
    }

    //endregion


    //region Flowable

    @Test
    public void rawSubscriber() {
        Flowable.range(1, 10)
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        s.request(Long.MAX_VALUE);
                    }

                    @Override
                    public void onNext(Integer t) {
                        System.out.println(t);
                    }

                    @Override
                    public void onError(Throwable t) {
                        t.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        System.out.println("Done");
                    }
                });
    }

    @Test
    public void defaultSubscriber() {
        Flowable.range(1, 10)
                .subscribe(new DefaultSubscriber<Integer>() {
                    @Override
                    public void onNext(Integer t) {
                        if (t == 3) cancel(); // self-state
                        System.out.println(t);
                    }

                    @Override
                    public void onError(Throwable t) {
                        t.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        System.out.println("Done");
                    }
                });
    }

    @Test
    public void resourceSubscriber() {
        Disposable resourceSubscriber = Flowable.range(1, 10)
                .subscribeWith(new ResourceSubscriber<Integer>() {
                    @Override
                    public void onNext(Integer t) {
                        System.out.println(t);
                    }
                    @Override
                    public void onError(Throwable t) {
                        t.printStackTrace();
                    }
                    @Override
                    public void onComplete() {
                        System.out.println("Done");
                    }
                });
        CompositeDisposable acc = new CompositeDisposable();
        acc.add(resourceSubscriber);
        acc.clear();
    }

    //endregion

}
