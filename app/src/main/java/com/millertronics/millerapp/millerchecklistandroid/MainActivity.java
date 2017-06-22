package com.millertronics.millerapp.millerchecklistandroid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (checkAuthenticationToken()){

        } else {
            displayLoginForm();
        }

    }

    private boolean checkAuthenticationToken() {
        return false;
    }

    private void displayLoginForm() {

    }
}
