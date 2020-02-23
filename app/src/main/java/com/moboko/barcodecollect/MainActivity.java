package com.moboko.barcodecollect;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

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
    ImageButton ibJanCdSearch, ibEditSelect, ibCopyData, ibDeleteData;
    Button btBack;
    RelativeLayout rlNormalOption, rlEditOption;
    CheckBox cbShowFavorite, cbAllSelect, cbOnlyFavorite;
    SelectData selectData;
    String favPrms[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cbShowFavorite = findViewById(R.id.cb_show_favorite);
        cbAllSelect = findViewById(R.id.cb_all_select);
        cbOnlyFavorite = findViewById(R.id.cb_only_favorite);
        ibJanCdSearch = findViewById(R.id.ib_jan_cd_search);
        ibEditSelect = findViewById(R.id.ib_edit_select);
        ibCopyData = findViewById(R.id.ib_copy_data);
        ibDeleteData = findViewById(R.id.ib_delete_data);
        rlNormalOption = findViewById(R.id.rl_normal_option);
        rlEditOption = findViewById(R.id.rl_edit_option);
        btBack = findViewById(R.id.bt_back);

        //初期表示
        rvListSet((RecyclerView) findViewById(R.id.item_rv), NORMAL_MODE);

        //お気に入りのみ表示
        cbShowFavorite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked && cbShowFavorite.isPressed()) {
                    rvListSet((RecyclerView) findViewById(R.id.item_rv), FAVORITE_SHOW_MODE);
                } else if(!isChecked && cbShowFavorite.isPressed()){
                    rvListSet((RecyclerView) findViewById(R.id.item_rv), NORMAL_MODE);
                }
            }
        });

        //バーコード検索起動
        ibJanCdSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callJanCdCaptureActivity();
            }
        });

        //オプションモード起動
        ibEditSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rvListSet((RecyclerView) findViewById(R.id.item_rv), OPTION_MODE);
            }
        });

        //オプションモード｜戻るボタン
        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cbShowFavorite.isChecked() == true) {
                    rvListSet((RecyclerView) findViewById(R.id.item_rv), FAVORITE_SHOW_MODE);
                } else {
                    rvListSet((RecyclerView) findViewById(R.id.item_rv), NORMAL_MODE);
                }
            }
        });

        //オプションモード｜削除ボタン
        ibDeleteData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Integer> mSelectList = postAdapter.getSelectList();
                //postAdapter.setSelectList();
                Integer[] array = mSelectList.toArray(new Integer[mSelectList.size()]);

                int count = mSelectList.size();
                if (count == 0) {
                    Toast.makeText(MainActivity.this, SELECTED_ERROR, Toast.LENGTH_LONG).show();
                } else {

                    ContentValues values = new ContentValues();
                    values.put(DELETE_FLAG, "'1'");
                    connectDb();

                    for (int i = 0; i < mSelectList.size(); i++) {
                        db.update(ITEM_LIST_TABLE, values, ID + "= ?", new String[]{String.valueOf(array[i])});
                    }
                }
                if (cbShowFavorite.isChecked() == true) {
                    rvListSet((RecyclerView) findViewById(R.id.item_rv), NORMAL_MODE);
                    cbShowFavorite.setChecked(false);
                } else {
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

    protected void rvListSet(RecyclerView rvList, int selectMode) {
        connectDb();

        selectData = new SelectData(helper, db);

        if (selectMode == FAVORITE_SHOW_MODE) {
            favPrms = new String[1];
            favPrms[0] = "1";
            setViewNormal();
            selectFavorite();

        } else if (selectMode == NORMAL_MODE) {
            setViewNormal();
            selectNormal();

        } else if (selectMode == OPTION_MODE) {
            //非表示
            ibJanCdSearch.setVisibility(View.GONE);
            ibEditSelect.setVisibility(View.GONE);
            rlNormalOption.setVisibility(View.GONE);

            //表示
            ibCopyData.setVisibility(View.VISIBLE);
            ibDeleteData.setVisibility(View.VISIBLE);
            rlEditOption.setVisibility(View.VISIBLE);

            selectNormal();
        } else if (selectMode == OPTION_ALL_FAVORITE) {
            cbAllSelect.setChecked(false);
            selectNormal();
        } else if (selectMode == OPTION_ZERO_FAVORITE) {
            selectNormal();
        } else if (selectMode == OPTION_ALL_SELECT) {
            cbOnlyFavorite.setChecked(false);
            selectNormal();
        } else if (selectMode == OPTION_ZERO_SELECT) {
            selectNormal();
        }
        itemList = selectData.getData();

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
                    case R.id.ll_item_detail:
                        Intent intent = new Intent(MainActivity.this, InputActivity.class);
                        intent.putExtra(UPDATE_PROC, String.valueOf(itemList.get(postAdapter.getLine()).get_id()));
                        intent.putExtra(ID_PROC, UPDATE_FLAG);
                        startActivityForResult(intent, INSERT_REQUEST);
                        break;
                }
            }
        });
        rvList.setAdapter(postAdapter);

    }

    private void setViewNormal() {
        //オプションモードセット初期化
        cbAllSelect.setChecked(false);
        cbOnlyFavorite.setChecked(false);

        //非表示
        ibJanCdSearch.setVisibility(View.VISIBLE);
        ibEditSelect.setVisibility(View.VISIBLE);
        rlNormalOption.setVisibility(View.VISIBLE);

        //表示
        ibCopyData.setVisibility(View.GONE);
        ibDeleteData.setVisibility(View.GONE);
        rlEditOption.setVisibility(View.GONE);
    }

    private void selectFavorite() {
        String sql = SELECT_LIST + WHERE_DELETE_FLAG + WHERE_FAVORITE_FLAG + ORDER_BY_LIST;
        selectData.setSql(sql);
        selectData.selectSql(favPrms);
    }

    private void selectNormal() {
        String sql = SELECT_LIST + WHERE_DELETE_FLAG + ORDER_BY_LIST;
        selectData.setSql(sql);
        selectData.selectSql(null);
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