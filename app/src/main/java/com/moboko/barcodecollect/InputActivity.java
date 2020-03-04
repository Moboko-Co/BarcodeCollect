package com.moboko.barcodecollect;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.common.StringUtils;
import com.moboko.barcodecollect.db.DbOpenHelper;
import com.moboko.barcodecollect.entity.DbFavoriteList;
import com.moboko.barcodecollect.entity.DbItemList;
import com.moboko.barcodecollect.entity.ItemList;
import com.moboko.barcodecollect.model.InsertData;
import com.moboko.barcodecollect.model.SelectData;
import com.moboko.barcodecollect.model.UpdateData;
import com.moboko.barcodecollect.util.Checks;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.moboko.barcodecollect.util.Consts.*;

public class InputActivity extends AppCompatActivity {

    Intent intent = new Intent(InputActivity.this, MainActivity.class);

    String[] reqJanCd = new String[1];
    String[] req_id = new String[1];

    String idProc;
    String outItemNm;

    List<ItemList> itemList = new ArrayList<>();

    private DbOpenHelper helper;
    private SQLiteDatabase db;

    EditText evInputPrice, evInputMemo1, evInputMemo2, evInputItemNm, evInputPer;
    TextView tvInputJanCd, tvInputRegisterDay, tvOutputPrice;
    RadioGroup rgTax, rgCategory;
    String sql;
    Button btContinue;
    String url;


    InputMethodManager inputMethodManager;
    private RelativeLayout llInput;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);

        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        llInput = findViewById(R.id.ll_input);

        Intent fromIntent = getIntent();

        btContinue = this.findViewById(R.id.bt_continue);

        rgTax = findViewById(R.id.rg_tax);
        rgCategory = findViewById(R.id.rg_category);
        evInputMemo1 = findViewById(R.id.ev_input_memo1);
        evInputMemo2 = findViewById(R.id.ev_input_memo2);
        tvInputJanCd = findViewById(R.id.tv_input_jan_cd);
        tvInputRegisterDay = findViewById(R.id.tv_input_register_day);
        evInputPrice = findViewById(R.id.ev_input_price);
        tvOutputPrice = findViewById(R.id.tv_output_price);
        evInputItemNm = findViewById(R.id.ev_input_item_nm);
        evInputPer = findViewById(R.id.ev_input_per);


        //DB セットアップ
        if (helper == null) {
            helper = new DbOpenHelper(getApplicationContext());
        }

        if (db == null) {
            db = helper.getWritableDatabase();
        }


        // 画面遷移元特定
        idProc = fromIntent.getStringExtra(ID_PROC);


        SelectData selectCountData = new SelectData(helper, db);
        final SelectData selectData = new SelectData(helper, db);

        // バーコードから遷移
        if (idProc.equals(INSERT_FLAG)) {
            // JAN_CD退避
            reqJanCd[0] = getIntent().getStringExtra(INSERT_PROC);

            sql = SELECT_COUNT_LIST + WHERE_JAN_CD;
            selectCountData.setSql(sql);
            selectCountData.selectSql(reqJanCd);
            int count = selectCountData.getCount();

            if (count > 0) {
                new AlertDialog.Builder(InputActivity.this)
                        .setTitle(ALERT_TITLE)
                        .setMessage(ALERT_MESSAGE)
                        .setPositiveButton(ALERT_YES, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                sql = SELECT_LIST + WHERE_JAN_CD + ORDER_BY_LIST;
                                selectData.setSql(sql);
                                selectData.selectSql(reqJanCd);
                                itemList = selectData.getData(MODE_DEFAULT);

                                // クリックしたときの処理
                                setUpdateValue();
                            }
                        })
                        .setNegativeButton(ALERT_NO, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                setNewValue();
                            }
                        })
                        .show();

            } else {
                url = YAHOO_URL + YAHOO_APP_ID + YAHOO_ADD_JAN_CODE + reqJanCd[0];
                Response res = getItemResults(url);
                outItemNm = getItemNmResponse(res);
                if (outItemNm == null || outItemNm.isEmpty()) {
                    outItemNm = NEW_ITEM_NM;
                }
                setNewValue();
            }
        } else if (idProc.equals(UPDATE_FLAG)) {
            req_id[0] = fromIntent.getStringExtra(UPDATE_PROC);
            sql = SELECT_LIST + WHERE_DELETE_FLAG + WHERE_ID + ORDER_BY_LIST;
            selectData.setSql(sql);
            selectData.selectSql(req_id);
            itemList = selectData.getData(MODE_DEFAULT);

            btContinue.setVisibility(View.GONE);

            setUpdateValue();
        }
        evInputPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                setPrice((RadioButton) findViewById(rgTax.getCheckedRadioButtonId()), getPer(String.valueOf(evInputPer.getText())));
            }

            @Override
            public void afterTextChanged(Editable s) {
                String per = String.valueOf(evInputPer.getText());
                setPrice((RadioButton) findViewById(rgTax.getCheckedRadioButtonId()), getPer(String.valueOf(evInputPer.getText())));
            }
        });

        rgTax.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                setPrice((RadioButton) findViewById(rgTax.getCheckedRadioButtonId()), getPer(String.valueOf(evInputPer.getText())));
            }
        });


        findViewById(R.id.bt_finish).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Checks checks = new Checks();

                InsertUpdateDate();

                setResult(RESULT_OK, intent);
                finish();
            }
        });

        findViewById(R.id.bt_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED, intent);
                finish();
            }
        });

        findViewById(R.id.bt_continue).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InsertUpdateDate();
                setResult(RE_CAPUTRE_RESPONSE, intent);
                finish();
            }
        });
    }

    private String setPrice(RadioButton rbTax, int evPer) {
        int inputPrice = 0;

        Checks checks = new Checks();

        if (checks.isNumber(String.valueOf(evInputPrice.getText()))) {
            inputPrice = Integer.parseInt(String.valueOf(evInputPrice.getText()));
        } else {
            tvOutputPrice.setText(String.valueOf(0));
        }

        String priceStr = new String();
        switch (rbTax.getId()) {
            case R.id.rb_t_1:
                priceStr = getCalcPrice(TAX_PER_0, evPer, inputPrice);
                tvOutputPrice.setText(priceStr);
                tvOutputPrice.setVisibility(View.INVISIBLE);
                return "1";
            case R.id.rb_t_2:
                tvOutputPrice.setVisibility(View.VISIBLE);
                priceStr = getCalcPrice(TAX_PER_8, evPer, inputPrice);
                tvOutputPrice.setText(priceStr);
                return "2";
            case R.id.rb_t_3:
                tvOutputPrice.setVisibility(View.VISIBLE);
                priceStr = getCalcPrice(TAX_PER_10, evPer, inputPrice);
                tvOutputPrice.setText(priceStr);
                return "3";
            default:
                tvOutputPrice.setVisibility(View.INVISIBLE);
                return "1";
        }
    }

    private void InsertUpdateDate() {
        // DbItemListセット
        DbItemList inputItem = new DbItemList();
        DateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        String timeStamp = format.format(new Date());
        switch (rgCategory.getCheckedRadioButtonId()) {
            case R.id.rb_c_1:
                inputItem.setCategoryCd("01");
                break;
            case R.id.rb_c_2:
                inputItem.setCategoryCd("02");
                break;
            case R.id.rb_c_3:
                inputItem.setCategoryCd("03");
                break;
            case R.id.rb_c_4:
                inputItem.setCategoryCd("04");
                break;
        }
        inputItem.setItemNm(String.valueOf(evInputItemNm.getText()));
        inputItem.setSalePer(Integer.parseInt(String.valueOf(evInputPer.getText())));
        inputItem.setPrice(Integer.parseInt(String.valueOf(evInputPrice.getText())));
        inputItem.setTaxDiv(setPrice((RadioButton) findViewById(rgTax.getCheckedRadioButtonId()), getPer(String.valueOf(evInputPer.getText()))));

        int per = getPer(String.valueOf(evInputPer.getText()));

        switch (inputItem.getTaxDiv()) {
            case "1":
                inputItem.setTaxPrice(Integer.parseInt(getCalcPrice(TAX_PER_0, per, inputItem.getPrice())));
                break;
            case "2":
                inputItem.setTaxPrice(Integer.parseInt(getCalcPrice(TAX_PER_8, per, inputItem.getPrice())));
                break;
            case "3":
                inputItem.setTaxPrice(Integer.parseInt(getCalcPrice(TAX_PER_10, per, inputItem.getPrice())));
                break;
        }
        inputItem.setMemo1(String.valueOf(evInputMemo1.getText()));
        inputItem.setMemo2(String.valueOf(evInputMemo2.getText()));
        inputItem.setJanCd(reqJanCd[0]);
        inputItem.setRegisterDay(timeStamp);
        inputItem.setDeleteFlag("0");

        // DbItemListセット
        DbFavoriteList inputItem2 = new DbFavoriteList();
        inputItem2.setFavoriteFlag(0);

        if (idProc.equals(INSERT_FLAG)) {
            InsertData insertData = new InsertData(helper, db);
            insertData.setDbItemList(inputItem);
            insertData.insertDbItemList();
            insertData.setDbFavoriteList(inputItem2);
            insertData.insertDbFavoriteList();
        } else {
            UpdateData updateData = new UpdateData(helper, db);
            updateData.setDbItemList(inputItem);
            updateData.updateDbItemList(req_id);
        }
    }

    private void setUpdateValue() {
        evInputItemNm.setText(itemList.get(0).getItemNm());
        evInputMemo1.setText(itemList.get(0).getMemo1());
        evInputMemo2.setText(itemList.get(0).getMemo2());
        tvInputJanCd.setText(itemList.get(0).getJanCd());
        tvInputRegisterDay.setText(itemList.get(0).getRegisterDay());
        evInputPer.setText(itemList.get(0).getSalePer());
        evInputPrice.setText(String.valueOf(itemList.get(0).getPrice()));

        String priceStr = String.format("%,d", itemList.get(0).getTaxPrice());
        // ラジオボタン
        switch (itemList.get(0).getTaxDiv()) {
            case "1":
                rgTax.check(R.id.rb_t_1);
                break;
            case "2":
                tvOutputPrice.setVisibility(View.VISIBLE);
                tvOutputPrice.setText(priceStr);
                rgTax.check(R.id.rb_t_2);
                break;
            case "3":
                tvOutputPrice.setVisibility(View.VISIBLE);
                tvOutputPrice.setText(priceStr);
                rgTax.check(R.id.rb_t_3);
                break;
        }
        // カテゴリー
        switch (itemList.get(0).getCategoryCd()) {
            case "01":
                rgCategory.check(R.id.rb_c_1);
                break;
            case "02":
                rgCategory.check(R.id.rb_c_2);
                break;
            case "03":
                rgCategory.check(R.id.rb_c_3);
                break;
            case "04":
                rgCategory.check(R.id.rb_c_4);
                break;
        }
    }

    private void setNewValue() {
        tvInputJanCd.setText(reqJanCd[0]);
        rgTax.check(R.id.rb_t_1);
        rgCategory.check(R.id.rb_c_1);
        evInputPer.setText(NEW_DIGIT);
        evInputItemNm.setText(outItemNm);
    }

    // 画面タップ時の処理
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        // キーボードを隠す
        inputMethodManager.hideSoftInputFromWindow(llInput.getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
        // 背景にフォーカスを移す
        llInput.requestFocus();

        return true;

    }

    int getPer(String per) {
        int resPer = 0;
        if (per == null || per.isEmpty()) {
            return resPer;
        }
        return Integer.parseInt(per);
    }

    String getCalcPrice(double tax, int per, int input) {
        return String.format("%,d", (int) Math.floor(input * tax) * ((100 - per) / 100));
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
