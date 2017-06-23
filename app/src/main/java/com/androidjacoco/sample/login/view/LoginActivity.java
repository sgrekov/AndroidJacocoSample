package com.androidjacoco.sample.login.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.androidjacoco.sample.R;
import com.androidjacoco.sample.login.FakeLoginService;
import com.androidjacoco.sample.login.presenter.LoginPresenter;
import com.androidjacoco.sample.main.view.MainActivity;
import com.jakewharton.rxbinding2.widget.RxTextView;
import io.reactivex.schedulers.Schedulers;

public class LoginActivity extends AppCompatActivity implements ILoginView {
    LoginPresenter presenter;
    TextInputLayout loginInput;
    EditText loginText;
    TextInputLayout passwordInput;
    EditText passwordText;
    Button loginBtn;
    TextView errorTxt;
    ProgressBar loginProgress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_main);
        presenter = new LoginPresenter(this, new FakeLoginService(Schedulers.io()));

        loginInput = (TextInputLayout) findViewById(R.id.login_til);
        loginText = (EditText) findViewById(R.id.login);
        passwordInput = (TextInputLayout) findViewById(R.id.password_til);
        passwordText = (EditText) findViewById(R.id.password);
        loginBtn = (Button) findViewById(R.id.login_btn);
        errorTxt = (TextView) findViewById(R.id.error);
        loginProgress = (ProgressBar) findViewById(R.id.login_progress);

        presenter.addLoginInput(RxTextView.textChanges(loginText));
        presenter.addPasswordInput(RxTextView.textChanges(passwordText));
        loginBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.loginBtnClick();
            }
        });

    }


    @Override
    public void error(String error) {
        errorTxt.setText(error);
    }

    @Override
    public void showProgress() {
        loginProgress.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        loginProgress.setVisibility(View.GONE);
    }

    @Override
    public void onLoginSuccess() {
        //go to main screen
    }

    @Override
    public void showPasswordError() {
        passwordInput.setError("Password is not valid");
    }

    @Override
    public void showLoginError() {
        loginInput.setError("Login is not valid");
    }

    @Override
    public String getLogin() {
        return loginText.getText().toString();
    }

    @Override
    public String getPassword() {
        return passwordText.getText().toString();
    }

    @Override
    public void hideError() {
        errorTxt.setVisibility(View.GONE);
    }

    @Override
    public CharSequence getLoginError() {
        return loginInput.getError();
    }

    @Override
    public CharSequence getPassError() {
        return passwordInput.getError();
    }

    @Override
    public void hideLoginError() {
        loginInput.setError("");
    }

    @Override
    public void hidePasswordError() {
        passwordInput.setError("");
    }

    @Override
    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        if (imm != null) {
            imm.hideSoftInputFromWindow(loginText.getWindowToken(), 0);
        }
    }

    @Override
    public void disableLoginBtn() {
        loginBtn.setEnabled(false);
    }

    @Override
    public void enableLoginBtn() {
        loginBtn.setEnabled(true);
    }

    @Override
    public void goToMainScreen(String username) {
        Intent i = new Intent(this, MainActivity.class);
        i.putExtra(MainActivity.USERNAME_KEY, username);
        startActivity(i);
    }
}
