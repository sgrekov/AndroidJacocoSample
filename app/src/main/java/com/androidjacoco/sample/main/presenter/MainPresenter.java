package com.androidjacoco.sample.main.presenter;

import com.androidjacoco.sample.main.view.IMainView;

public class MainPresenter {

    private final IMainView view;
    private final String username;

    public MainPresenter(IMainView view, String username) {
        this.view = view;
        this.username = username;
    }


    public void showWelcome() {
        view.showUserWelcome("Hello, " + username + "!");
    }
}
