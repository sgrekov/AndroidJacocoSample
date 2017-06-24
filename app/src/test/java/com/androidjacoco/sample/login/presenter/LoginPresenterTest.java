package com.androidjacoco.sample.login.presenter;

import com.androidjacoco.sample.login.data.ILoginService;
import com.androidjacoco.sample.login.view.ILoginView;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

import java.net.UnknownHostException;
import java.util.Arrays;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class LoginPresenterTest {

    LoginPresenter presenter;
    ILoginView view;
    ILoginService loginService;

    @Before
    public void setUp() {
        view = mock(ILoginView.class);
        loginService = mock(ILoginService.class);
        presenter = new LoginPresenter(view, loginService, Schedulers.trampoline());
    }

    @Test
    public void validateLogin() {
        Assert.assertEquals(false, presenter.validateLogin(null));
        Assert.assertEquals(false, presenter.validateLogin(""));
        Assert.assertEquals(false, presenter.validateLogin("123"));
        Assert.assertEquals(true, presenter.validateLogin("1234"));
        Assert.assertEquals(true, presenter.validateLogin("1234567891234"));
        Assert.assertEquals(false, presenter.validateLogin("1234567891234567"));
    }

    @Test
    public void validatePass() {
        Assert.assertEquals(false, presenter.validatePass(null));
        Assert.assertEquals(false, presenter.validatePass(""));
        Assert.assertEquals(false, presenter.validatePass("1234"));
        Assert.assertEquals(true, presenter.validatePass("12345"));
        Assert.assertEquals(true, presenter.validatePass("1234567891234"));
    }


    @Test
    public void loginClickInvalidLogin() {
        when(view.getLogin()).thenReturn("111");
        when(view.getPassword()).thenReturn("123");
        presenter.loginBtnClick();

        verify(view).showLoginError();
        verify(view).getLogin();
        verify(view).getPassword();
        verifyNoMoreInteractions(view);
    }

    @Test
    public void loginClickInvalidPass() {
        when(view.getLogin()).thenReturn("1234");
        when(view.getPassword()).thenReturn("1234");
        presenter.loginBtnClick();

        verify(view).showPasswordError();
        verify(view).getLogin();
        verify(view).getPassword();
        verifyNoMoreInteractions(view);
    }

    @Test
    public void loginFailed() {
        when(view.getLogin()).thenReturn("12345");
        when(view.getPassword()).thenReturn("12345");
        when(loginService.login()).thenReturn(Single.just(false));
        presenter.loginBtnClick();

        verify(view).getLogin();
        verify(view).getPassword();
        verify(view).showProgress();
        verify(view).hideError();
        verify(view).hideKeyboard();
        verify(view).hideProgress();
        verify(view).error("Wrong login or password!");
        verifyNoMoreInteractions(view);
    }

    @Test
    public void loginSuccess() {
        when(view.getLogin()).thenReturn("12345");
        when(view.getPassword()).thenReturn("12345");
        when(loginService.login()).thenReturn(Single.just(true));
        presenter.loginBtnClick();

        verify(view, times(2)).getLogin();
        verify(view).getPassword();
        verify(view).showProgress();
        verify(view).hideError();
        verify(view).hideKeyboard();
        verify(view).hideProgress();
        verify(view).onLoginSuccess();
        verify(view).goToMainScreen("12345");
        verifyNoMoreInteractions(view);
    }

    @Test
    public void loginError() {
        when(view.getLogin()).thenReturn("12345");
        when(view.getPassword()).thenReturn("12345");
        when(loginService.login()).thenReturn(Single.<Boolean>error(new UnknownHostException("no connection")));
        presenter.loginBtnClick();

        verify(view).getLogin();
        verify(view).getPassword();
        verify(view).showProgress();
        verify(view).hideError();
        verify(view).hideKeyboard();
        verify(view).hideProgress();
        verify(view).error("Error while login");
        verifyNoMoreInteractions(view);
    }

    @Test
    public void inputInvalidLogin() {
        when(view.getLoginError()).thenReturn("error");

        presenter.addLoginInput(Observable.<CharSequence>fromIterable(Arrays.asList("l", "lo")));
        verify(view, times(2)).disableLoginBtn();
        verify(view, times(4)).getLoginError();
        verify(view, times(2)).hideLoginError();
        verifyNoMoreInteractions(view);
    }

    @Test
    public void inputLoginValid() {
        when(view.getLoginError()).thenReturn("error");
        when(view.getPassword()).thenReturn("123");

        presenter.addLoginInput(Observable.<CharSequence>fromIterable(Arrays.asList("login")));
        verify(view, times(2)).getLoginError();
        verify(view).getPassword();
        verify(view).hideLoginError();
        verify(view).disableLoginBtn();
        verifyNoMoreInteractions(view);
    }

    @Test
    public void inputLoginValidWithValidPass() {
        when(view.getLoginError()).thenReturn("error");
        when(view.getPassword()).thenReturn("12345");

        presenter.addLoginInput(Observable.<CharSequence>fromIterable(Arrays.asList("login")));
        verify(view, times(2)).getLoginError();
        verify(view).getPassword();
        verify(view).hideLoginError();
        verify(view).enableLoginBtn();
        verifyNoMoreInteractions(view);
    }

    @Test
    public void inputInvalidPassword() {
        when(view.getPassError()).thenReturn("error");

        presenter.addPasswordInput(Observable.<CharSequence>fromIterable(Arrays.asList("1")));
        verify(view).disableLoginBtn();
        verify(view, times(2)).getPassError();
        verify(view).hidePasswordError();
        verifyNoMoreInteractions(view);
    }

    @Test
    public void inputPasswordValid() {
        when(view.getPassError()).thenReturn("error");
        when(view.getLogin()).thenReturn("log");

        presenter.addPasswordInput(Observable.<CharSequence>fromIterable(Arrays.asList("12345")));
        verify(view, times(2)).getPassError();
        verify(view).getLogin();
        verify(view).hidePasswordError();
        verify(view).disableLoginBtn();
        verifyNoMoreInteractions(view);
    }

    @Test
    public void inputValidPasswordWithValidPass() {
        when(view.getPassError()).thenReturn("error");
        when(view.getLogin()).thenReturn("login");

        presenter.addPasswordInput(Observable.<CharSequence>fromIterable(Arrays.asList("12345")));
        verify(view, times(2)).getPassError();
        verify(view).getLogin();
        verify(view).hidePasswordError();
        verify(view).enableLoginBtn();
        verifyNoMoreInteractions(view);
    }
}
