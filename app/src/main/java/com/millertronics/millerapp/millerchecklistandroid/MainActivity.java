package com.millertronics.millerapp.millerchecklistandroid;

import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v4.os.AsyncTaskCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {

    private EditText usernameInput;
    private EditText passwordInput;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (checkAuthenticationToken()) {
            navigateToDashboard();
        } else {
            displayLoginForm();
        }

    }

    private boolean checkAuthenticationToken() {
        return false;
    }

    private void navigateToDashboard() {

    }

    private void displayLoginForm() {
        setContentView(R.layout.activity_main);

        usernameInput = (EditText) findViewById(R.id.username_input);
        passwordInput = (EditText) findViewById(R.id.password_input);
        loginButton = (Button) findViewById(R.id.login_button);

        loginButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                loginButton.setClickable(false);
                try {
                    submitAndProcessAuthenticationRequest();
                }catch (Exception e){
                    Log.e(MainActivity.class.getName(),
                            "Failed to submit and process authentication request!");
                    Log.e(MainActivity.class.getName(),
                            Log.getStackTraceString(e));
                }
            }
        });
    }

    private void submitAndProcessAuthenticationRequest() throws IOException {

        AuthAsyncTask authAsyncTask = new AuthAsyncTask();
        authAsyncTask.execute(this, usernameInput.getText().toString(), passwordInput.getText().toString());

        loginButton.setClickable(true);
    }

    private class AuthAsyncTask extends AsyncTask<String, Void, String> {

        private Context context;
        private static final String DELIMITER = "%BREAK%";

        public void execute(Context context, String... params){
            this.context = context;
            execute(params);
        }

        @Override
        protected String doInBackground(String... params) {
            String authUrlString = getString(R.string.default_mutex_url)
                    + getString(R.string.api_prefix)
                    + getString(R.string.api_authentication_path);
            try {
                URL authUrl = new URL(authUrlString);
                HttpsURLConnection connection = (HttpsURLConnection) authUrl.openConnection();
                connection.setReadTimeout(60 * 1000);
                connection.setConnectTimeout(60 * 1000);
                connection.setRequestMethod("POST");
                connection.setRequestProperty(getString(R.string.api_content_type),
                        getString(R.string.api_content_type_json));
                connection.setRequestProperty(getString(R.string.api_auth_id),
                        params[0]);
                connection.setRequestProperty(getString(R.string.api_auth_key),
                        params[1]);
                int responseCode = connection.getResponseCode();

                BufferedReader in = new BufferedReader(
                        new InputStreamReader( responseCode == 200 ? connection.getInputStream()
                        : connection.getErrorStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                response.append(responseCode);
                response.append(DELIMITER);
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                return response.toString();

            } catch (Exception e){
                Log.e(AuthAsyncTask.class.getName(), Log.getStackTraceString(e));
            }
            return "Error in running async task!";
        }

        @Override
        protected void onPostExecute(String result){
            super.onPostExecute(result);

            String[] responseParams = result.split(DELIMITER);

            String message = null;
            if (responseParams[0].equals("200")){
                try {
                    JSONObject jsonObj = new JSONObject(responseParams[1]);
                    StringBuilder sb = new StringBuilder();
                    sb.append("Welcome ")
                            .append(jsonObj.getString("first_name"))
                            .append(" ")
                            .append(jsonObj.getString("last_name"))
                            .append("!");
                    message = sb.toString();
                } catch (Exception e){
                    message = responseParams[1];
                }
            }else {
                message = responseParams[1];
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage(message);
            builder.setCancelable(false);
            builder.setPositiveButton(R.string.dialog_ok,
                    new DialogInterface.OnClickListener(){
                        public void onClick(DialogInterface di, int i){
                            di.cancel();
                        }
                    });
            builder.show();
        }
    }
}
