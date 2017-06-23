package com.androidjacoco.sample.login;

import io.reactivex.Scheduler;
import io.reactivex.Single;

import java.util.concurrent.TimeUnit;

public class FakeLoginService implements ILoginService {

    private final Scheduler scheduler;

    public FakeLoginService(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    @Override
    public Single<Boolean> login() {
        return Single.just(true).delay(5L, TimeUnit.SECONDS, scheduler);
    }
}
