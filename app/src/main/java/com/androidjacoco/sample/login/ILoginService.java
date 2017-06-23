package com.androidjacoco.sample.login;

import io.reactivex.Single;

public interface ILoginService {

    Single<Boolean> login();
}
