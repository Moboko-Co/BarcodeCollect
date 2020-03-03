package com.moboko.barcodecollect;

import android.os.AsyncTask;

import com.moboko.barcodecollect.entity.ItemList;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.Response;


class FetchPostsTask extends AsyncTask<String, Void, List<ItemList>> {

    List<ItemList> resItemList = new ArrayList<>();
    private CallBackTask callbacktask;

    @Override
    protected List<ItemList> doInBackground(String... map) {


        return resItemList;
    }


    @Override
    protected void onPostExecute(List<ItemList> s){
        super.onPostExecute(s);
        callbacktask.CallBack(s);

    }

    public void setOnCallBack(CallBackTask _cbj) {
        callbacktask = _cbj;
    }

    public void setOnCallBack() {
    }

    /**
     * コールバック用のstaticなclass
     */
    public static class CallBackTask {
        public void CallBack(List<ItemList> result) {
        }
    }
}
