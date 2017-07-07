package com.millertronics.millerapp.millerchecklistandroid.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.millertronics.millerapp.millerchecklistandroid.arrayadapter.ChecklistAdapter;
import com.millertronics.millerapp.millerchecklistandroid.asynctasks.AuthAsyncTask;
import com.millertronics.millerapp.millerchecklistandroid.R;
import com.millertronics.millerapp.millerchecklistandroid.asynctasks.RetrieveChecklistsAsyncTask;
import com.millertronics.millerapp.millerchecklistandroid.models.Checklist;
import com.millertronics.millerapp.millerchecklistandroid.models.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private User user;

    private ViewFlipper viewFlipper;
    //Dashboard views
    private TabHost tabHost;
    private TextView dashboardHeader;

    //Login form views
    private EditText usernameInput;
    private EditText passwordInput;
    private Button loginButton;

    private ListView weeklyChecklistsView;
    private ListView dailyChecklistsView;
    private ListView monthlyChecklistsView;

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
            viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(this,
                    android.R.anim.fade_out));
            viewFlipper.setDisplayedChild(0);
        }

        user = User.getCurrentUser();

        setupTabs();

        dailyChecklistsView = (ListView) findViewById(R.id.daily_list);
        weeklyChecklistsView = (ListView) findViewById(R.id.weekly_list);
        monthlyChecklistsView = (ListView) findViewById(R.id.monthly_list);

        retrieveChecklists(Checklist.FREQUENCY_DAILY);
        retrieveChecklists(Checklist.FREQUENCY_WEEKLY);
        retrieveChecklists(Checklist.FREQUENCY_MONTHLY);

    }

    private void setupTabs(){
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

        for(int i=0;i<tabHost.getTabWidget().getChildCount();i++)
        {
            ((TextView) tabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title))
                    .setTextColor(ContextCompat.getColor(this, R.color.colorDefaultShadeFont));
        }
    }

    public void populateListViewWithChecklists(String frequencyString, List<Checklist> checklists){
        ListView listView = null;
        switch (frequencyString) {
            case "daily":
                listView = dailyChecklistsView;
                break;
            case "weekly":
                listView = weeklyChecklistsView;
                break;
            case "monthly":
                listView = monthlyChecklistsView;
                break;
            default:
                break;
        }
        ChecklistAdapter checklistAdapter = new ChecklistAdapter(
                this,
                checklists.toArray(new Checklist[checklists.size()])
        );
        if (listView != null) {
            listView.setAdapter(checklistAdapter);
            checklistAdapter.notifyDataSetChanged();
        }
    }

    private void retrieveChecklists(String frequency) {
        new RetrieveChecklistsAsyncTask(this, frequency, user).execute();
    }

    private void displayLoginForm() {

        viewFlipper.setInAnimation(AnimationUtils.loadAnimation(this,
                android.R.anim.fade_in));
        viewFlipper.setDisplayedChild(1);

        usernameInput = (EditText) findViewById(R.id.username_input);
        passwordInput = (EditText) findViewById(R.id.password_input);
        loginButton = (Button) findViewById(R.id.login_button);

        loginButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                InputMethodManager inputMethodManager = (InputMethodManager)
                        getSystemService(Activity.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

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
        new AuthAsyncTask(this)
                .execute(usernameInput.getText().toString(), passwordInput.getText().toString());
    }

    public void enableLoginButton(){
        loginButton.setClickable(true);
    }
}
