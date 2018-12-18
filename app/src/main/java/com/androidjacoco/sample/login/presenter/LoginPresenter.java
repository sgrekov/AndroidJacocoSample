package com.androidjacoco.sample.login.presenter;

import com.androidjacoco.sample.login.data.ILoginService;
import com.androidjacoco.sample.login.view.ILoginView;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class LoginPresenter {
    private final ILoginView loginView;
    private final ILoginService loginService;
    private final Scheduler scheduler;

    public LoginPresenter(ILoginView loginView, ILoginService loginService, Scheduler scheduler) {
        this.loginView = loginView;
        this.loginService = loginService;
        this.scheduler = scheduler;
    }


    public void loginBtnClick() {
        if (loginService == null) {
            loginView.error("Something wrong happened!");
            return;
        }

        final String login = loginView.getLogin();
        String pass = loginView.getPassword();
        if (!validateLogin(login)) {
            loginView.showLoginError();
            return;
        }
        if (!validatePass(pass)) {
            loginView.showPasswordError();
            return;
        }

        loginService.login()
                .observeOn(scheduler)
                .subscribe(new SingleObserver<Boolean>() {

                    @Override
                    public void onSubscribe(Disposable d) {
                        loginView.showProgress();
                        loginView.hideError();
                        loginView.hideKeyboard();
                    }

                    @Override
                    public void onSuccess(Boolean logged) {
                        if (logged) {
                            loginView.hideProgress();
                            loginView.onLoginSuccess();
                            loginView.goToMainScreen(loginView.getLogin());
                        } else {
                            loginView.hideProgress();
                            loginView.error("Wrong login or password!");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        loginView.hideProgress();
                        loginView.error("Error while login");
                    }
                });
    }

    boolean validatePass(CharSequence pass) {
        return !(pass == null
                || pass.length() <= 4);
    }

    boolean validateLogin(CharSequence login) {
        return !(login == null || login.length() <= 3 || login.length() > 15);
    }

    public void addLoginInput(Observable<CharSequence> logintextViewText) {
        logintextViewText.subscribe(new Consumer<CharSequence>() {
            @Override
            public void accept(CharSequence login) throws Exception {
                if (loginView.getLoginError() != null
                        && loginView.getLoginError().length() > 0) {
                    loginView.hideLoginError();
                }

                if (!validateLogin(login)) {
                    loginView.disableLoginBtn();
                    return;
                }
                if (!validatePass(loginView.getPassword())) {
                    loginView.disableLoginBtn();
                    return;
                }

                loginView.enableLoginBtn();
            }
        });
    }

    public void addPasswordInput(Observable<CharSequence> passValueObservable) {
        passValueObservable.subscribe(new Consumer<CharSequence>() {
            @Override
            public void accept(CharSequence pass) throws Exception {
                if (loginView.getPassError() != null
                        && loginView.getPassError().length() > 0) {
                    loginView.hidePasswordError();
                }

                if (!validatePass(pass)) {
                    loginView.disableLoginBtn();
                    return;
                }
                if (!validateLogin(loginView.getLogin())) {
                    loginView.disableLoginBtn();
                    return;
                }

                loginView.enableLoginBtn();
            }
        });
    }
}
