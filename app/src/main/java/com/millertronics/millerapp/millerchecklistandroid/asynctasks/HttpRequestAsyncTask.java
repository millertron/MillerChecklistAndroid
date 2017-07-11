package com.millertronics.millerapp.millerchecklistandroid.asynctasks;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.millertronics.millerapp.millerchecklistandroid.R;
import com.millertronics.millerapp.millerchecklistandroid.activities.MainActivity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ProtocolException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by koha.choji on 06/07/2017.
 */

public abstract class HttpRequestAsyncTask extends AsyncTask<String, Void, String> {
    protected static final String DELIMITER = "%BREAK%";

    protected abstract String getUrlString(String... params);

    protected abstract void configureConnection(HttpsURLConnection connection, String... params)
            throws ProtocolException;

    protected HttpsURLConnection createHttpsURLConnection(String... params){
        try {
            URL authUrl = new URL(getUrlString(params));

            HttpsURLConnection connection = (HttpsURLConnection) authUrl.openConnection();
            connection.setReadTimeout(60 * 1000);
            connection.setConnectTimeout(60 * 1000);
            configureConnection(connection, params);

            return connection;
        } catch (Exception e){
            Log.e(AuthAsyncTask.class.getName(), Log.getStackTraceString(e));
        }
        return null;
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            HttpsURLConnection connection = createHttpsURLConnection(params);
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
        return "";
    }

    protected void displayErrorDialogOnFail(Activity activity){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(R.string.dialog_error);
        builder.setMessage(R.string.dialog_connection_error);
        builder.setPositiveButton(R.string.dialog_ok,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface di, int i) {
                        di.cancel();
                    }
                });
        builder.show();
    }
}
