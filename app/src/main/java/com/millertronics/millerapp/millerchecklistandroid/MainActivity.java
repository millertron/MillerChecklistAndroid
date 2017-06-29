package com.millertronics.millerapp.millerchecklistandroid;

import android.app.ProgressDialog;
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

    public void displayDashboard() {
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
        dashboardHeader.setText("Checklists for \n"
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

    public void enableLoginButton(){
        loginButton.setClickable(true);
    }
}
