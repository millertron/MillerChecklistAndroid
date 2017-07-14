package com.millertronics.millerapp.millerchecklistandroid.asynctasks;

import android.os.AsyncTask;
import android.util.Log;

import com.millertronics.millerapp.millerchecklistandroid.R;
import com.millertronics.millerapp.millerchecklistandroid.activities.MainActivity;
import com.millertronics.millerapp.millerchecklistandroid.models.Checklist;
import com.millertronics.millerapp.millerchecklistandroid.models.ChecklistItem;
import com.millertronics.millerapp.millerchecklistandroid.models.User;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by koha.choji on 06/07/2017.
 */

public class RetrieveChecklistsAsyncTask extends HttpRequestAsyncTask{

    private MainActivity mainActivity;
    private User user;
    private String frequency;

    public RetrieveChecklistsAsyncTask(MainActivity mainActivity, String frequency, User user){
        this.mainActivity = mainActivity;
        this.frequency = frequency;
        this.user = user;
    }

    protected String getUrlString(String... params){
        return new StringBuilder()
                .append(mainActivity.getString(R.string.default_mutex_url))
                .append(mainActivity.getString(R.string.api_prefix))
                .append(mainActivity.getString(R.string.api_checklists_path))
                .append("?").append(mainActivity.getString(R.string.api_owner_id))
                .append("=").append(String.valueOf(user.getId()))
                .append("&").append(mainActivity.getString(R.string.api_frequency))
                .append("=").append(frequency).toString();
    }

    protected void configureConnection(HttpsURLConnection connection, String... params)
            throws ProtocolException {
        connection.setRequestMethod("GET");
        connection.setRequestProperty(mainActivity.getString(R.string.api_content_type),
                mainActivity.getString(R.string.api_content_type_json));
        connection.setRequestProperty(mainActivity.getString(R.string.api_auth_header),
                new StringBuilder().append(mainActivity.getString(R.string.api_auth_token))
                        .append(user.getApiKey()).toString());
    }

    protected void onPostExecute(String result){
        super.onPostExecute(result);
        if (StringUtils.isBlank(result)){
            displayErrorDialogOnFail(mainActivity);
        } else {
            final String[] responseParams = result.split(DELIMITER);

            final int statusCode = Integer.parseInt(responseParams[0]);
            if (statusCode == 200) {
                try {
                    List<Checklist> checklists = new ArrayList<>();
                    JSONArray jsonArray = new JSONArray(responseParams[1]);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonClObject = jsonArray.getJSONObject(i);
                        JSONArray jsonCliArray = jsonClObject.getJSONArray("checklist_items");
                        JSONObject jsonCliObject = jsonCliArray.getJSONObject(0);
                        checklists.add(
                                new Checklist.Builder()
                                        .id(jsonClObject.getInt("id"))
                                        .frequency(jsonClObject.getString("frequency"))
                                        .name(jsonClObject.getString("name"))
                                        .description(jsonClObject.getString("description"))
                                        .completed(jsonClObject.getString("last_implemented_date"))
                                        .addChecklistItem(
                                                new ChecklistItem.Builder()
                                                        .id(jsonCliObject.getInt("id"))
                                                        .checklistId(jsonCliObject
                                                                .getInt("checklist_id"))
                                                        .text(jsonCliObject.getString("text"))
                                                        .valueType(jsonCliObject
                                                                .getString("value_type"))
                                                        .metricTargetMax(jsonCliObject
                                                                .getString("metric_target_max"))
                                                        .metricTargetMin(jsonCliObject
                                                                .getString("metric_target_min"))
                                                        .mandatory(jsonCliObject
                                                                .getBoolean("mandatory"))
                                                        .build()
                                        )
                                        .build()
                        );
                    }
                    mainActivity.populateListViewWithChecklists(frequency, checklists);
                } catch (Exception e) {
                    Log.e(mainActivity.getClass().getName(), Log.getStackTraceString(e));
                }
            }
        }
    }

}
