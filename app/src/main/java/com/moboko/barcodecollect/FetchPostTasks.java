package com.moboko.barcodecollect;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FetchPostTasks  extends AsyncTask<String, Void, String> {

    String resItemNm = new String();
    private CallBackTask callbacktask;

    private static final String ns = null;

    @Override
    protected String doInBackground(String... url) {
        Response res = getItemResults(url[0]);
        resItemNm = getItemNmResponse(res);
        return resItemNm;
    }


    @Override
    protected void onPostExecute(String s){
        super.onPostExecute(s);
        callbacktask.CallBack(s);

    }

    public void setOnCallBack(CallBackTask _cbj) {
        callbacktask = _cbj;
    }

    /**
     * コールバック用のstaticなclass
     */
    public static class CallBackTask {
        public void CallBack(String result) {
        }
    }

    private Response getItemResults(String url) {
        OkHttpClient client = new OkHttpClient();

        Request builder = new Request.Builder()
                .url(url)
                .build();
        try {
            Response response = client.newCall(builder).execute();
            return response;
        } catch (IOException e) {
            Log.d("SEARCHITEM : HTTP", e.getMessage());
            return null;
        }
    }

    private String getItemNmResponse(Response response) {
        String data = new String();
        String str = new String();
        try {
            data = response.body().string();
            JSONObject rootObject = new JSONObject(data);
            JSONObject resultSetObject = rootObject.getJSONObject("ResultSet");
            JSONObject result0Object = resultSetObject.getJSONObject("0");
            JSONObject resultObject = result0Object.getJSONObject("Result");
            JSONObject itemObject = resultObject.getJSONObject("0");
            str = itemObject.getString("Name");
        } catch (JSONException | IOException e) {
            e.printStackTrace();
            Log.d("SEARCHITEM : TOJSON", e.getMessage());
            return null;
        }
        return str;
    }
}
