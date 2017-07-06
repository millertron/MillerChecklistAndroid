package com.millertronics.millerapp.millerchecklistandroid.activities;

import android.app.Activity;
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
import com.millertronics.millerapp.millerchecklistandroid.models.Checklist;
import com.millertronics.millerapp.millerchecklistandroid.models.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
            viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(this,
                    android.R.anim.fade_out));
            viewFlipper.setDisplayedChild(0);
        }

        List<Checklist> userChecklists = retrieveUserChecklists();

        User user = User.getCurrentUser();
        List<Checklist> dailyChecklists = retrieveChecklists(Checklist.Frequency.DAILY);
        List<Checklist> weeklyChecklists = retrieveChecklists(Checklist.Frequency.WEEKLY);
        List<Checklist> monthlyChecklists = retrieveChecklists(Checklist.Frequency.MONTHLY);

        tabHost = (TabHost) findViewById(R.id.tabhost);
        tabHost.setup();

        TabHost.TabSpec spec = tabHost.newTabSpec(getString(R.string.dashboard_tab_daily));
        spec.setContent(R.id.tab_daily);
        spec.setIndicator(getString(R.string.dashboard_tab_daily));
        tabHost.addTab(spec);

        ListView dailyChecklistsView = (ListView) findViewById(R.id.daily_list);
        ChecklistAdapter dAdapter = new ChecklistAdapter(
                this,
                dailyChecklists.toArray(new Checklist[dailyChecklists.size()])
        );
        dailyChecklistsView.setAdapter(dAdapter);
        dAdapter.notifyDataSetChanged();

        spec = tabHost.newTabSpec(getString(R.string.dashboard_tab_weekly));
        spec.setContent(R.id.tab_weekly);
        spec.setIndicator(getString(R.string.dashboard_tab_weekly));
        tabHost.addTab(spec);

        ListView weeklyChecklistsView = (ListView) findViewById(R.id.weekly_list);
        ChecklistAdapter wAdapter = new ChecklistAdapter(
                this,
                weeklyChecklists.toArray(new Checklist[weeklyChecklists.size()])
        );
        weeklyChecklistsView.setAdapter(wAdapter);
        wAdapter.notifyDataSetChanged();

        spec = tabHost.newTabSpec(getString(R.string.dashboard_tab_monthly));
        spec.setContent(R.id.tab_monthly);
        spec.setIndicator(getString(R.string.dashboard_tab_monthly));
        tabHost.addTab(spec);

        ListView monthlyChecklistsView = (ListView) findViewById(R.id.monthly_list);
        ChecklistAdapter mAdapter = new ChecklistAdapter(
                this,
                monthlyChecklists.toArray(new Checklist[monthlyChecklists.size()])
        );
        monthlyChecklistsView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

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

    private List<Checklist> retrieveUserChecklists() {
        return new ArrayList<>();
    }

    private List<Checklist> retrieveChecklists(Checklist.Frequency frequency) {
        List<Checklist> checklists = new ArrayList<>();
        String name = "";
        switch (frequency){
            case DAILY:
                name = "Daily Checklist 1";
                break;
            case WEEKLY:
                name = "Weekly Checklist 1";
                break;
            case MONTHLY:
                name = "Monthly Checklist 1";
                break;
        }
        Checklist checklist = new Checklist.Builder().name(name).build();
        checklists.add(checklist);
        return checklists;
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
        AuthAsyncTask authAsyncTask = new AuthAsyncTask(this);
        authAsyncTask.execute(usernameInput.getText().toString(), passwordInput.getText().toString());
    }

    public void enableLoginButton(){
        loginButton.setClickable(true);
    }
}
