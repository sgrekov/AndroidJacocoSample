package com.androidjacoco.sample.login.view;

public interface ILoginView {
    void error(String errorText);

    void showProgress();

    void hideProgress();

    void onLoginSuccess();

    void showPasswordError();

    void showLoginError();

    String getLogin();

    String getPassword();

    void hideError();

    CharSequence getLoginError();

    CharSequence getPassError();

    void hideLoginError();

    void hidePasswordError();

    void hideKeyboard();

    void disableLoginBtn();

    void enableLoginBtn();

    void goToMainScreen(String login);
}
