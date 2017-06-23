package com.millertronics.millerapp.millerchecklistandroid;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.ViewFlipper;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {

    private ViewFlipper viewFlipper;
    //Dashboard views
    private TabHost tabHost;
    private TextView dashboardHeader;

    //Login form views
    private EditText usernameInput;
    private EditText passwordInput;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewFlipper = (ViewFlipper) findViewById(R.id.view_flipper);

        if (checkAuthentication()) {
            displayDashboard();
        } else {
            displayLoginForm();
        }

    }

    private boolean checkAuthentication() {
        if (User.getCurrentUser() != null){
            return true;
        } else return false;
    }

    private void displayDashboard() {
        if (viewFlipper.getDisplayedChild() != 0) {
            viewFlipper.setDisplayedChild(0);
        }
        User user = User.getCurrentUser();
        tabHost = (TabHost) findViewById(R.id.tabhost);
        tabHost.setup();

        TabHost.TabSpec spec = tabHost.newTabSpec(getString(R.string.dashboard_tab_daily));
        spec.setContent(R.id.tab_daily);
        spec.setIndicator(getString(R.string.dashboard_tab_daily));
        tabHost.addTab(spec);

        spec = tabHost.newTabSpec(getString(R.string.dashboard_tab_weekly));
        spec.setContent(R.id.tab_weekly);
        spec.setIndicator(getString(R.string.dashboard_tab_weekly));
        tabHost.addTab(spec);

        spec = tabHost.newTabSpec(getString(R.string.dashboard_tab_monthly));
        spec.setContent(R.id.tab_monthly);
        spec.setIndicator(getString(R.string.dashboard_tab_monthly));
        tabHost.addTab(spec);

        dashboardHeader = (TextView) findViewById(R.id.dashboard_heading);
        dashboardHeader.setText("Checklists for "
            + user.getFirstName() + " " + user.getLastName()
                + " (" + user.getUsername() + ")");

    }

    private void displayLoginForm() {

        viewFlipper.setDisplayedChild(1);

        usernameInput = (EditText) findViewById(R.id.username_input);
        passwordInput = (EditText) findViewById(R.id.password_input);
        loginButton = (Button) findViewById(R.id.login_button);

        loginButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                disableLoginButton();
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

    private void disableLoginButton() {
        loginButton.setClickable(false);
    }

    private void submitAndProcessAuthenticationRequest() throws IOException {
        AuthAsyncTask authAsyncTask = new AuthAsyncTask(this);
        authAsyncTask.execute(usernameInput.getText().toString(), passwordInput.getText().toString());
    }

    private class AuthAsyncTask extends AsyncTask<String, Void, String> {

        private MainActivity mainActivity;
        private static final String DELIMITER = "%BREAK%";

        public AuthAsyncTask(MainActivity mainActivity){
            this.mainActivity = mainActivity;
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

                    User user = new User(jsonObj.getInt("id"),
                            jsonObj.getString("username"),
                            jsonObj.getString("first_name"),
                            jsonObj.getString("last_name"),
                            jsonObj.getString("email"),
                            jsonObj.getString("api_key"));

                    User.setCurrentUser(user);

                } catch (Exception e){
                    Log.e(mainActivity.getClass().getName(), Log.getStackTraceString(e));
                    message = responseParams[1];
                }
            }else {
                message = responseParams[1];
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
            builder.setMessage(message);
            builder.setCancelable(false);
            builder.setPositiveButton(R.string.dialog_ok,
                    new DialogInterface.OnClickListener(){
                        public void onClick(DialogInterface di, int i){
                        mainActivity.enableLoginButton();
                        mainActivity.displayDashboard();
                        di.cancel();
                        }
                    });
            builder.show();
        }
    }

    public void enableLoginButton(){
        loginButton.setClickable(true);
    }
}
