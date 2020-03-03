package com.moboko.barcodecollect;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.moboko.barcodecollect.db.DbOpenHelper;
import com.moboko.barcodecollect.entity.DbFavoriteList;
import com.moboko.barcodecollect.entity.DbItemList;
import com.moboko.barcodecollect.entity.ItemList;
import com.moboko.barcodecollect.model.InsertData;
import com.moboko.barcodecollect.model.SelectData;
import com.moboko.barcodecollect.model.UpdateData;
import com.moboko.barcodecollect.util.Checks;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.moboko.barcodecollect.util.Consts.ALERT_MESSAGE;
import static com.moboko.barcodecollect.util.Consts.ALERT_TITLE;
import static com.moboko.barcodecollect.util.Consts.ID_PROC;
import static com.moboko.barcodecollect.util.Consts.INSERT_FLAG;
import static com.moboko.barcodecollect.util.Consts.INSERT_PROC;
import static com.moboko.barcodecollect.util.Consts.MODE_DEFAULT;
import static com.moboko.barcodecollect.util.Consts.ORDER_BY_LIST;
import static com.moboko.barcodecollect.util.Consts.RE_CAPUTRE_RESPONSE;
import static com.moboko.barcodecollect.util.Consts.SELECT_COUNT_LIST;
import static com.moboko.barcodecollect.util.Consts.SELECT_LIST;
import static com.moboko.barcodecollect.util.Consts.UPDATE_FLAG;
import static com.moboko.barcodecollect.util.Consts.UPDATE_PROC;
import static com.moboko.barcodecollect.util.Consts.WHERE_DELETE_FLAG;
import static com.moboko.barcodecollect.util.Consts.WHERE_ID;
import static com.moboko.barcodecollect.util.Consts.WHERE_JAN_CD;

public class InputActivity extends AppCompatActivity {

    Intent intent = new Intent(InputActivity.this, MainActivity.class);

    String[] reqJanCd = new String[1];
    String[] req_id = new String[1];

    String idProc;

    FetchPostsTask fetchPostsTask;

    List<ItemList> itemList = new ArrayList<>();

    private DbOpenHelper helper;
    private SQLiteDatabase db;

    EditText evInputPrice, evInputMemo1, evInputMemo2;
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
                        .setPositiveButton("はい", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                sql = SELECT_LIST + WHERE_JAN_CD + ORDER_BY_LIST;
                                selectData.setSql(sql);
                                selectData.selectSql(reqJanCd);
                                itemList = selectData.getData(MODE_DEFAULT);

                                // クリックしたときの処理
                                setUpdateValue();
                            }
                        })
                        .setNegativeButton("いいえ", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                setNewValue();
                            }
                        })
                        .show();

            } else {
                fetchPostsTask = new FetchPostsTask();
                fetchPostsTask.setOnCallBack();


                fetchPostsTask.execute(url);
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
                setPrice((RadioButton) findViewById(rgTax.getCheckedRadioButtonId()));
            }

            @Override
            public void afterTextChanged(Editable s) {
                setPrice((RadioButton) findViewById(rgTax.getCheckedRadioButtonId()));
            }
        });

        rgTax.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                setPrice((RadioButton) findViewById(rgTax.getCheckedRadioButtonId()));
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

    private String setPrice(RadioButton rbTax) {
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
                priceStr = priceStr + String.format("%,d", inputPrice);
                tvOutputPrice.setText(priceStr);
                tvOutputPrice.setVisibility(View.INVISIBLE);
                return "1";
            case R.id.rb_t_2:
                tvOutputPrice.setVisibility(View.VISIBLE);
                priceStr = priceStr + String.format("%,d", (int) Math.floor(inputPrice * 1.08));
                tvOutputPrice.setText(priceStr);
                return "2";
            case R.id.rb_t_3:
                tvOutputPrice.setVisibility(View.VISIBLE);
                priceStr = priceStr + String.format("%,d", (int) Math.floor(inputPrice * 1.1));
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
        inputItem.setPrice(Integer.parseInt(String.valueOf(evInputPrice.getText())));
        inputItem.setTaxDiv(setPrice((RadioButton) findViewById(rgTax.getCheckedRadioButtonId())));
        switch (inputItem.getTaxDiv()) {
            case "1":
                inputItem.setTaxPrice(inputItem.getPrice());
                break;
            case "2":
                inputItem.setTaxPrice((int) Math.floor(inputItem.getPrice() * 1.08));
                break;
            case "3":
                inputItem.setTaxPrice((int) Math.floor(inputItem.getPrice() * 1.1));
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
        evInputMemo1.setText(itemList.get(0).getMemo1());
        evInputMemo2.setText(itemList.get(0).getMemo2());
        tvInputJanCd.setText(itemList.get(0).getJanCd());
        tvInputRegisterDay.setText(itemList.get(0).getRegisterDay());

        String priceStr = String.format("%,d", itemList.get(0).getTaxPrice());
        // ラジオボタン初期化
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
        evInputPrice.setText(String.valueOf(itemList.get(0).getPrice()));
    }

    private void setNewValue() {
        tvInputJanCd.setText(reqJanCd[0]);
        rgTax.check(R.id.rb_t_1);
        rgCategory.check(R.id.rb_c_1);
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
}
