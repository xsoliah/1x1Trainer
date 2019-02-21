package com.example.manuel.a1x1trainer.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.manuel.a1x1trainer.AppServices.AppService;
import com.example.manuel.a1x1trainer.AppServices.IsUserAllowedService;
import com.example.manuel.a1x1trainer.R;
import com.example.manuel.a1x1trainer.Ressources.ConstantStrings;
import com.example.manuel.a1x1trainer.Ressources.GameMode;
import com.example.manuel.a1x1trainer.Ressources.RuntimeConstants;
import com.example.manuel.a1x1trainer.Ressources.UserCredentials;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class LoginActivity extends Activity {
    private TextView usernameInput;
    private TextView passwordInput;
    private TextView errorMessage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        // map properties
        usernameInput = findViewById(R.id.login_username_input);
        passwordInput = findViewById(R.id.login_password_input);
        errorMessage = findViewById(R.id.login_error_msg);

        Button loginBackButton = (Button)findViewById(R.id.login_back_btn);
        loginBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(0, 0);
            }
        });

        Button loginButton = (Button)findViewById(R.id.login_login_btn);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performLogin();
            }
        });
    }

    private void performLogin() {
        if (!isUserNameValid() || !isPasswordValid())
        {
            setErrorMessage(getString(R.string.login_invalid_input_err_msg));
            return;
        }

        IsUserAllowedService is = new IsUserAllowedService(usernameInput.getText().toString(), passwordInput.getText().toString());
        UserManagerWebService uws = new UserManagerWebService(is);
        uws.execute();
    }

    private void successfulLogin() {
        Intent intent = new Intent(LoginActivity.this, OnboardingActivity.class);
        intent.putExtra(getString(R.string.intent_extra_game_mode), GameMode.TRAINING.toString());
        startActivity(intent);
        overridePendingTransition(0, 0);
        this.finish();
    }

    private boolean isUserNameValid() {
        String userNameInputString = usernameInput.getText().toString();
        // checking username length
        if (userNameInputString.length() == 0 || userNameInputString.length() > RuntimeConstants.MAX_USERNAME_INPUT_STRING_LENGTH)
            return false;
        return true;
    }

    private boolean isPasswordValid() {
        String passwordInputString = passwordInput.getText().toString();
        // checking password length
        if (passwordInputString.length() == 0 || passwordInputString.length() > RuntimeConstants.MAX_PASSWORD_INPUT_STRING_LENGTH)
            return false;
        return true;
    }

    private void setErrorMessage(String message)
    {
        errorMessage.setText(message);
    }

    class UserManagerWebService extends AsyncTask<Void, Void, Boolean> {
        private com.example.manuel.a1x1trainer.AppServices.AppService AppService;

        public UserManagerWebService(AppService appService) {
            this.AppService = appService;
        }

        @Override
        protected void onPostExecute(Boolean b) {
            if (b)
                successfulLogin();
            else
                setErrorMessage(getString(R.string.login_login_failed_err_msg));
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            SoapSerializationEnvelope envelope =  new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.implicitTypes = true;
            envelope.setAddAdornments(false);
            envelope.setOutputSoapObject(this.AppService.createSoapObject());

            HttpTransportSE httpTransportSE = new HttpTransportSE(RuntimeConstants.USERMANAGER_URL);
            try {
                httpTransportSE.call(this.AppService.getSoapAction(), envelope);
                SoapObject isUserAllowedResponse = (SoapObject)envelope.bodyIn;
                if (isUserAllowedResponse != null)
                {
                    SoapObject loginCredentials = (SoapObject)isUserAllowedResponse.getProperty(0);
                    if (loginCredentials != null)
                    {
                        boolean accepted = (boolean)loginCredentials.getProperty(getString(R.string.login_response_prop_accepted));
                        String hmac = (String)loginCredentials.getProperty(getString(R.string.login_response_prop_hmac));
                        int idUser = (int)loginCredentials.getProperty(getString(R.string.login_response_prop_id_user));
                        String message = (String)loginCredentials.getProperty(getString(R.string.login_response_prop_message));

                        UserCredentials.IsUserAllowed = accepted;
                        UserCredentials.UserId = idUser;

                        if (!UserCredentials.IsUserAllowed || UserCredentials.UserId == 0)
                            return false;
                        else
                            return true;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            return false;
        }
    }
}
