package com.moboko.barcodecollect;

import android.os.AsyncTask;

import com.moboko.barcodecollect.entity.ItemList;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.Response;


class FetchPostsTask extends AsyncTask<Map<String,String>, Void, List<ItemList>> {

    List<ItemList> resItemList = new ArrayList<>();
    private CallBackTask callbacktask;

    @Override
    protected List<ItemList> doInBackground(Map<String, String>... map) {


        return resItemList;
    }

    private void setAmazonResponse(Response amazonResponse) {

    }

    @Override
    protected void onPostExecute(List<ItemList> s){
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
        public void CallBack(List<ItemList> result) {
        }
    }
}
