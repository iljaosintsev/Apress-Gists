package com.turlir.abakgists;

import com.turlir.abakgists.base.BasePresenter;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;
import timber.log.Timber;

public class RoomPresenter extends BasePresenter<RoomActivity> {

    private final AppDatabase mRoom;

    private int start = 0;

    public RoomPresenter(AppDatabase room) {
        mRoom = room;
    }

    public void subscribe() {
        addSubscription(
                mRoom.userDao().getAll()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSubscriber<List<User>>() {
                            @Override
                            public void onNext(List<User> users) {
                                Timber.v("onNext %d", users.size());
                            }

                            @Override
                            public void onError(Throwable t) {
                                t.printStackTrace();
                            }

                            @Override
                            public void onComplete() {
                                Timber.v("onComplete");
                            }
                        })
        );
    }

    public void save() {
        User[] data = new User[15];
        for (int i = 0; i < 15; i++) {
            int q = start + i;
            User u = new User();
            u.setUid(q);
            u.setFirstName("ilja " + q);
            u.setLastName("turlir " + q);
            data[i] = u;
        }
        start += 15;

        DisposableCompletableObserver saveRes = Completable
                .fromCallable(() -> {
                    mRoom.userDao().insertAll(data);
                    return true;
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableCompletableObserver() {
                    @Override
                    public void onComplete() {
                        Timber.v("save onComplete");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e("save", e);
                    }
                });

        addSubscription(saveRes);
    }
}
