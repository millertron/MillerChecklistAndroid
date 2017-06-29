package com.millertronics.millerapp.millerchecklistandroid;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by koha.choji on 29/06/2017.
 */

public class AuthAsyncTask extends AsyncTask<String, Void, String> {

    private MainActivity mainActivity;
    private static final String DELIMITER = "%BREAK%";
    private ProgressDialog progressDialog;

    public AuthAsyncTask(MainActivity mainActivity){
        this.mainActivity = mainActivity;
        progressDialog = new ProgressDialog(this.mainActivity);
    }

    @Override
    protected void onPreExecute(){
        progressDialog.setMessage(mainActivity.getString(R.string.submitting_request));
        progressDialog.show();
    }

    @Override
    protected String doInBackground(String... params) {
        String authUrlString = mainActivity.getString(R.string.default_mutex_url)
                + mainActivity.getString(R.string.api_prefix)
                + mainActivity.getString(R.string.api_authentication_path);
        try {
            URL authUrl = new URL(authUrlString);
            HttpsURLConnection connection = (HttpsURLConnection) authUrl.openConnection();
            connection.setReadTimeout(60 * 1000);
            connection.setConnectTimeout(60 * 1000);
            connection.setRequestMethod("POST");
            connection.setRequestProperty(mainActivity.getString(R.string.api_content_type),
                    mainActivity.getString(R.string.api_content_type_json));
            connection.setRequestProperty(mainActivity.getString(R.string.api_auth_id),
                    params[0]);
            connection.setRequestProperty(mainActivity.getString(R.string.api_auth_key),
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
        if (progressDialog.isShowing()){
            progressDialog.dismiss();
        }
        final String[] responseParams = result.split(DELIMITER);

        String message = null;
        final int statusCode = Integer.parseInt(responseParams[0]);
        if (statusCode == 200){
            try {
                JSONObject jsonObj = new JSONObject(responseParams[1]);
                User user = new User(jsonObj.getInt("id"),
                        jsonObj.getString("username"),
                        jsonObj.getString("first_name"),
                        jsonObj.getString("last_name"),
                        jsonObj.getString("email"),
                        jsonObj.getString("api_key"));

                User.setCurrentUser(user);

                mainActivity.displayDashboard();
            } catch (Exception e){
                Log.e(mainActivity.getClass().getName(), Log.getStackTraceString(e));
                displayMessageDialog(mainActivity.getString(R.string.auth_error));
            }
        }else {
            message = responseParams[1];
            displayMessageDialog(message);
        }
    }

    private void displayMessageDialog(String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.dialog_ok,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface di, int i) {
                        mainActivity.enableLoginButton();
                        di.cancel();
                    }
                });
        builder.show();
    }
}
