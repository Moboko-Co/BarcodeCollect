package com.moboko.barcodecollect;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.moboko.barcodecollect.db.DbOpenHelper;
import com.moboko.barcodecollect.entity.DbFavoriteList;
import com.moboko.barcodecollect.entity.ItemList;
import com.moboko.barcodecollect.model.SelectData;
import com.moboko.barcodecollect.model.UpdateData;
import com.moboko.barcodecollect.view.PostAdapter;

import java.util.ArrayList;
import java.util.List;

import static com.moboko.barcodecollect.util.Consts.*;

public class MainActivity extends AppCompatActivity {

    List<ItemList> itemList = new ArrayList<>();
    PostAdapter postAdapter;
    private DbOpenHelper helper;
    private SQLiteDatabase db;
    LinearLayout rlNormalOption, rlEditOption;
    CheckBox cbShowFavorite, cbAllSelect, cbOnlyFavorite;
    Spinner spSortItem;
    int currentSortOption = 0;
    private AdView mAdView;

    private int mMenuResourceId = R.menu.normal_main_menu;

    // Menuを切り替えたい箇所から呼び出す
    public void changeMenuItem(int menuResourceId) {
        this.mMenuResourceId = menuResourceId;
        supportInvalidateOptionsMenu();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(mMenuResourceId, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                // 戻るボタンをタップ
                if (cbShowFavorite.isChecked() == true) {
                    rvListSet((RecyclerView) findViewById(R.id.item_rv), FAVORITE_SHOW_MODE);
                } else {
                    rvListSet((RecyclerView) findViewById(R.id.item_rv), NORMAL_MODE);
                }
                getSupportActionBar().setDisplayShowHomeEnabled(false);
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                changeMenuItem(R.menu.normal_main_menu);
                break;
            //バーコード検索起動
            case R.id.ib_jan_cd_search:
                callJanCdCaptureActivity();
                break;
            //オプションモード起動
            case R.id.ib_edit_select:
                rvListSet((RecyclerView) findViewById(R.id.item_rv), OPTION_MODE);
                changeMenuItem(R.menu.option_main_menu);
                break;
            //コピーボタン押下
            case R.id.ib_copy_data:
                ArrayList<Integer> mSelectList = postAdapter.getSelectList();
                int count = mSelectList.size();

                if (count == 0) {
                    Toast.makeText(MainActivity.this, COPY_ERROR_MESSAGE, Toast.LENGTH_LONG).show();
                } else {
                    String input;
                    String separateCd = ",";

                    input = "JANコード,商品名,カテゴリ,お気に入り,金額,税率,計算後金額,メモ１,メモ２,登録日\n";

                    for (int i = 0; i < count; i++) {

                        SelectData selectCopyData = new SelectData(helper, db);
                        String _idPrms[] = new String[1];
                        _idPrms[0] = String.valueOf(mSelectList.get(i));
                        List<ItemList> list = selectId(selectCopyData, _idPrms).getData(MODE_DEFAULT);
                        input = input + list.get(0).getJanCd();
                        input = input + separateCd;
                        input = input + list.get(0).getItemNm();
                        input = input + separateCd;
                        input = input + list.get(0).getCategoryCd();
                        input = input + separateCd;
                        input = input + list.get(0).getFavoriteFlag();
                        input = input + separateCd;
                        input = input + list.get(0).getPrice();
                        input = input + separateCd;
                        switch (list.get(0).getTaxDiv()) {
                            case "1":
                                input = input + CLIP_BOARD_TAX_1;
                                input = input + separateCd;
                                input = input + list.get(0).getPrice();
                                break;
                            case "2":
                                input = input + CLIP_BOARD_TAX_2;
                                input = input + separateCd;
                                input = input + (int) Math.floor(list.get(0).getPrice() * 1.1);
                                break;
                            case "3":
                                input = input + CLIP_BOARD_TAX_3;
                                input = input + separateCd;
                                input = input + (int) Math.floor(list.get(0).getPrice() * 1.08);

                                break;
                        }
                        input = input + separateCd;
                        input = input + list.get(0).getMemo1();
                        input = input + separateCd;
                        input = input + list.get(0).getRegisterDay();
                        if (i != count - 1) {
                            input = input + "\n";
                        }
                    }
                    //クリップボードに格納するItemを作成
                    ClipData.Item clipItem = new ClipData.Item(input);
                    //MIMETYPEの作成
                    String[] mimeType = new String[1];
                    mimeType[0] = ClipDescription.MIMETYPE_TEXT_PLAIN;
                    //クリップボードに格納するClipDataオブジェクトの作成
                    ClipData cd = new ClipData(new ClipDescription("text_data", mimeType), clipItem);

                    //クリップボードにデータを格納
                    ClipboardManager cm = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                    cm.setPrimaryClip(cd);

                    Toast.makeText(MainActivity.this, COPY_MESSAGE, Toast.LENGTH_LONG).show();
                }
                break;

            case R.id.ib_delete_data:
                final ArrayList<Integer> mDelList = postAdapter.getSelectList();
                final Integer[] array = mDelList.toArray(new Integer[mDelList.size()]);

                int delCount = mDelList.size();
                if (delCount == 0) {
                    Toast.makeText(MainActivity.this, SELECTED_ERROR, Toast.LENGTH_LONG).show();
                } else {
                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle(DEL_ALERT_TITLE)
                            .setMessage(DEL_ALERT_MESSAGE)
                            .setPositiveButton(ALERT_YES, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    ContentValues values = new ContentValues();
                                    values.put(DELETE_FLAG, "'1'");
                                    connectDb();

                                    for (int i = 0; i < mDelList.size(); i++) {
                                        db.update(ITEM_LIST_TABLE, values, ID + "= ?", new String[]{String.valueOf(array[i])});
                                    }

                                    if (cbShowFavorite.isChecked() == true) {
                                        rvListSet((RecyclerView) findViewById(R.id.item_rv), NORMAL_MODE);
                                        cbShowFavorite.setChecked(false);
                                    } else {
                                        rvListSet((RecyclerView) findViewById(R.id.item_rv), NORMAL_MODE);
                                    }
                                }
                            })
                            .setNegativeButton(ALERT_NO, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .show();

                }
                break;

        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spSortItem = findViewById(R.id.sp_sort_item);
        cbShowFavorite = findViewById(R.id.cb_show_favorite);
        cbAllSelect = findViewById(R.id.cb_all_select);
        cbOnlyFavorite = findViewById(R.id.cb_only_favorite);
        rlNormalOption = findViewById(R.id.rl_normal_option);
        rlEditOption = findViewById(R.id.rl_edit_option);

        Toolbar toolbar = findViewById(R.id.tool_bar_main);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView = findViewById(R.id.main_ad);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        //初期表示
        rvListSet((RecyclerView) findViewById(R.id.item_rv), NORMAL_MODE);

        //ソートspinner表示
        ArrayAdapter spAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item, SORT_TITLE){
            // テキストの位置
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                ((TextView) v).setGravity(Gravity.RIGHT);
           //     ((TextView) v).setTextSize(Float.parseFloat("14sp"));
                return v;
            }

            // 選択肢の位置
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View v = super.getDropDownView(position, convertView,parent);
                ((TextView) v).setGravity(Gravity.RIGHT);
            //    ((TextView) v).setTextSize(Float.parseFloat("14sp"));
                return v;
            }
        };
        //  spAdapter.setDropDownViewResource(R.layout.sort_spinner_dropdown_item);
        spSortItem.setAdapter(spAdapter);
        spSortItem.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currentSortOption = position;
                if (cbShowFavorite.isChecked()) {
                    rvListSet((RecyclerView) findViewById(R.id.item_rv), FAVORITE_SHOW_MODE);
                } else {
                    rvListSet((RecyclerView) findViewById(R.id.item_rv), NORMAL_MODE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //お気に入りのみ表示
        cbShowFavorite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked && cbShowFavorite.isPressed()) {
                    rvListSet((RecyclerView) findViewById(R.id.item_rv), FAVORITE_SHOW_MODE);
                } else if (!isChecked && cbShowFavorite.isPressed()) {
                    rvListSet((RecyclerView) findViewById(R.id.item_rv), NORMAL_MODE);
                }
            }
        });

        //オプションモード｜全て選択
        cbAllSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    rvListSet((RecyclerView) findViewById(R.id.item_rv), OPTION_ALL_SELECT);
                } else if (!isChecked && cbAllSelect.isPressed()) {
                    rvListSet((RecyclerView) findViewById(R.id.item_rv), OPTION_ZERO_SELECT);
                }
            }
        });

        //オプションモード｜お気に入りのみ選択
        cbOnlyFavorite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    rvListSet((RecyclerView) findViewById(R.id.item_rv), OPTION_ALL_FAVORITE);
                } else if (!isChecked && cbOnlyFavorite.isPressed()) {
                    rvListSet((RecyclerView) findViewById(R.id.item_rv), OPTION_ZERO_FAVORITE);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case CAPTURE_REQUEST:
                if (resultCode == RESULT_OK) {
                    Intent intent = new Intent(MainActivity.this, InputActivity.class);
                    intent.putExtra(ID_PROC, INSERT_FLAG);
                    intent.putExtra(INSERT_PROC, data.getStringExtra(INSERT_PROC));
                    startActivityForResult(intent, INSERT_REQUEST);
                } else if (resultCode == RESULT_CANCELED) {
                    Toast.makeText(this, CANCELED_MESSAGE, Toast.LENGTH_LONG).show();
                }
                break;
            case INSERT_REQUEST:
                if (resultCode == RE_CAPUTRE_RESPONSE) {
                    Toast.makeText(this, SUCCESS_MESSAGE, Toast.LENGTH_LONG).show();
                    callJanCdCaptureActivity();
                } else if (resultCode == RESULT_OK) {
                    Toast.makeText(this, SUCCESS_MESSAGE, Toast.LENGTH_LONG).show();
                } else if (resultCode == RESULT_CANCELED) {
                    Toast.makeText(this, CANCELED_MESSAGE, Toast.LENGTH_LONG).show();
                }
                rvListSet((RecyclerView) findViewById(R.id.item_rv), NORMAL_MODE);
                postAdapter.notifyDataSetChanged();
                break;
        }
    }

    protected void callJanCdCaptureActivity() {
        Intent intent = new Intent(MainActivity.this, CaptureActivity.class);
        startActivityForResult(intent, CAPTURE_REQUEST);
    }

    protected void rvListSet(RecyclerView rvList, final int selectMode) {
        connectDb();

        if (selectMode == FAVORITE_SHOW_MODE) {
            setViewNormal();
            SelectData selectFavoriteShow = new SelectData(helper, db);
            itemList = selectFavorite(selectFavoriteShow, null).getData(FAVORITE_SHOW_MODE);

        } else if (selectMode == NORMAL_MODE) {
            setViewNormal();
            SelectData selectNormalShow = new SelectData(helper, db);
            itemList = selectNormal(selectNormalShow, null).getData(NORMAL_MODE);

        } else if (selectMode == OPTION_MODE) {
            //非表示
            rlNormalOption.setVisibility(View.GONE);

            //表示
            rlEditOption.setVisibility(View.VISIBLE);

            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            SelectData selectNormalShow = new SelectData(helper, db);
            itemList = selectNormal(selectNormalShow, null).getData(OPTION_MODE);

        } else if (selectMode == OPTION_ALL_FAVORITE) {
            cbAllSelect.setChecked(false);
            SelectData selectNormalShow = new SelectData(helper, db);
            itemList = selectNormal(selectNormalShow, null).getData(OPTION_ALL_FAVORITE);
        } else if (selectMode == OPTION_ZERO_FAVORITE) {
            SelectData selectNormalShow = new SelectData(helper, db);
            itemList = selectNormal(selectNormalShow, null).getData(OPTION_ZERO_FAVORITE);
        } else if (selectMode == OPTION_ALL_SELECT) {
            cbOnlyFavorite.setChecked(false);
            SelectData selectNormalShow = new SelectData(helper, db);
            itemList = selectNormal(selectNormalShow, null).getData(OPTION_ALL_SELECT);
        } else if (selectMode == OPTION_ZERO_SELECT) {
            SelectData selectNormalShow = new SelectData(helper, db);
            itemList = selectNormal(selectNormalShow, null).getData(OPTION_ZERO_SELECT);
        } else {
            SelectData selectNormalShow = new SelectData(helper, db);
            itemList = selectNormal(selectNormalShow, null).getData(MODE_DEFAULT);
        }

        rvList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        postAdapter = new PostAdapter(this, itemList, selectMode);

        postAdapter.setOnItemClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.cb_favorite_button:
                        String updateFavoritePrm[] = new String[1];

                        DbFavoriteList favoriteList = new DbFavoriteList();
                        updateFavoritePrm[0] = String.valueOf(itemList.get(postAdapter.getLine()).get_id());
                        int favoriteFlag;
                        itemList.get(postAdapter.getLine()).getFavoriteFlag();
                        if (itemList.get(postAdapter.getLine()).getFavoriteFlag() == 0) {
                            favoriteFlag = 1;
                        } else {
                            favoriteFlag = 0;
                        }
                        favoriteList.setFavoriteFlag(favoriteFlag);
                        connectDb();
                        UpdateData updateData = new UpdateData(helper, db);
                        updateData.setDbFavoriteList(favoriteList);
                        updateData.updateDbFavoriteList(updateFavoritePrm);
                        break;
                    case R.id.ll_item_detail_left:
                    case R.id.ll_item_detail_right:
                        Intent intent = new Intent(MainActivity.this, InputActivity.class);
                        intent.putExtra(UPDATE_PROC, String.valueOf(itemList.get(postAdapter.getLine()).get_id()));
                        intent.putExtra(ID_PROC, UPDATE_FLAG);
                        startActivityForResult(intent, INSERT_REQUEST);
                        break;
                    case R.id.cb_select:
                        postAdapter.setmSelectMode(OPTION_MODE);
                        if (selectMode == OPTION_ALL_SELECT) {
                            cbAllSelect.setChecked(false);
                        } else if (selectMode == OPTION_ALL_FAVORITE) {
                            cbOnlyFavorite.setChecked(false);
                        }

                        break;
                }
            }
        });
        rvList.setAdapter(postAdapter);
        postAdapter.notifyDataSetChanged();

    }

    private void setViewNormal() {
        //オプションモードセット初期化
        cbAllSelect.setChecked(false);
        cbOnlyFavorite.setChecked(false);

        //非表示
        rlNormalOption.setVisibility(View.VISIBLE);

        //表示
        rlEditOption.setVisibility(View.GONE);
    }

    private SelectData selectFavorite(SelectData getRes, String Prms[]) {
        String sql = SELECT_LIST + WHERE_DELETE_FLAG + ORDER_BY.get(currentSortOption);
        getRes.setSql(sql);
        getRes.selectSql(Prms);
        return getRes;
    }

    private SelectData selectNormal(SelectData getRes, String Prms[]) {
        String sql = SELECT_LIST + WHERE_DELETE_FLAG + ORDER_BY.get(currentSortOption);
        getRes.setSql(sql);
        getRes.selectSql(Prms);
        return getRes;
    }

    private SelectData selectId(SelectData getRes, String Prms[]) {
        String sql = SELECT_LIST + WHERE_DELETE_FLAG + WHERE_ID;
        getRes.setSql(sql);
        getRes.selectSql(Prms);
        return getRes;
    }


    protected void connectDb() {
        if (helper == null) {
            helper = new DbOpenHelper(getApplicationContext());
        }

        if (db == null) {
            db = helper.getWritableDatabase();
        }
    }
}