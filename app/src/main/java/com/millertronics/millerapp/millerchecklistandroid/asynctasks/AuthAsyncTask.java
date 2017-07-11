package com.millertronics.millerapp.millerchecklistandroid.asynctasks;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.millertronics.millerapp.millerchecklistandroid.R;
import com.millertronics.millerapp.millerchecklistandroid.activities.MainActivity;
import com.millertronics.millerapp.millerchecklistandroid.models.User;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ProtocolException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by koha.choji on 29/06/2017.
 */

public class AuthAsyncTask extends HttpRequestAsyncTask {

    private MainActivity mainActivity;
    private ProgressDialog progressDialog;

    public AuthAsyncTask(MainActivity mainActivity){
        this.mainActivity = mainActivity;
        progressDialog = new ProgressDialog(this.mainActivity);
    }

    @Override
    protected void onPreExecute(){
        progressDialog.setMessage(mainActivity.getString(R.string.submitting_request));
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    protected String getUrlString(String... params){
        return new StringBuilder().append(mainActivity.getString(R.string.default_mutex_url))
                .append(mainActivity.getString(R.string.api_prefix))
                .append(mainActivity.getString(R.string.api_authentication_path)).toString();
    }

    protected void configureConnection(HttpsURLConnection connection, String... params)
            throws ProtocolException {
            connection.setRequestMethod("POST");
            connection.setRequestProperty(mainActivity.getString(R.string.api_content_type),
                    mainActivity.getString(R.string.api_content_type_json));
            connection.setRequestProperty(mainActivity.getString(R.string.api_auth_id),
                    params[0]);
            connection.setRequestProperty(mainActivity.getString(R.string.api_auth_key),
                    params[1]);
    }

    @Override
    protected void onPostExecute(String result){
        super.onPostExecute(result);
        if (progressDialog.isShowing()){
            progressDialog.dismiss();
        }

        if (StringUtils.isBlank(result)){
            displayErrorDialogOnFail(mainActivity);
            mainActivity.enableLoginButton();
        } else {

            final String[] responseParams = result.split(DELIMITER);

            String message = null;
            final int statusCode = Integer.parseInt(responseParams[0]);
            if (statusCode == 200) {
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
                } catch (Exception e) {
                    Log.e(mainActivity.getClass().getName(), Log.getStackTraceString(e));
                    displayMessageDialog(mainActivity.getString(R.string.auth_error));
                }
            } else {
                message = responseParams[1];
                displayMessageDialog(message);
            }
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
