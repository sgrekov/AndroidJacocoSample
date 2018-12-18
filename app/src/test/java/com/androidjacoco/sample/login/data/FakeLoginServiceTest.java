package com.androidjacoco.sample.login.data;

import io.reactivex.schedulers.Schedulers;
import org.junit.Test;

public class FakeLoginServiceTest {

    ILoginService loginService = new FakeLoginService(Schedulers.trampoline());


    @Test
    public void login() throws InterruptedException {
        loginService
                .login()
                .test()
                .assertValue(true)
                .assertComplete();
    }
}
