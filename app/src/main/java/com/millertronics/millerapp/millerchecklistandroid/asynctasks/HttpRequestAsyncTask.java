package com.millertronics.millerapp.millerchecklistandroid.asynctasks;

import android.os.AsyncTask;
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
        return "Error in running async task!";
    }
}
