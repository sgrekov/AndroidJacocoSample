package com.androidjacoco.sample.login.data;

import io.reactivex.Single;

public interface ILoginService {

    Single<Boolean> login();
}
