package com.androidjacoco.sample.main.presenter;

import com.androidjacoco.sample.main.view.IMainView;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.verify;

public class MainPresenterTest {

    MainPresenter presenter;
    IMainView view;

    @Before
    public void setUp() {
        view = Mockito.mock(IMainView.class);
        presenter = new MainPresenter(view, "John Show");
    }

    @Test
    public void showHello() {
        presenter.showWelcome();
        verify(view).showUserWelcome("Hello, John Show!");
    }
}
