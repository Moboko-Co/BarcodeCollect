package com.moboko.barcodecollect.view;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.moboko.barcodecollect.R;
import com.moboko.barcodecollect.entity.ItemList;

import java.util.ArrayList;
import java.util.List;

import static com.moboko.barcodecollect.util.Consts.*;


public class PostAdapter extends RecyclerView.Adapter<PostViewHolder> {

    private Context mContext;
    private List<ItemList> itemLists;
    View baseView;
    View.OnClickListener m_listener;
    int m_line;
    int mSelectMode;


    public PostAdapter(Context context, List<ItemList> itemList, int selectMode) {
        mContext = context;
        itemLists = itemList;
        mSelectMode = selectMode;

    }

    public void setOnItemClickListener(View.OnClickListener listener) {
        m_listener = listener;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        baseView = View.inflate(mContext, R.layout.item_view, null);
        PostViewHolder postViewHolder = new PostViewHolder(baseView, this);
        return postViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final PostViewHolder holder, final int position) {
        final int pos = position;

        final ItemList item = itemLists.get(position);

        holder.cbSelect.setOnCheckedChangeListener(null);

        holder.cbSelect.setChecked(item.getCbSelected());

        if (mSelectMode == NORMAL_MODE) {

        } else if (mSelectMode == OPTION_MODE || mSelectMode == OPTION_ZERO_SELECT
                || mSelectMode == OPTION_ZERO_FAVORITE) {
            setOptionView(holder);
        } else if (mSelectMode == FAVORITE_SHOW_MODE) {

        } else if (mSelectMode == OPTION_ALL_SELECT) {
            setOptionView(holder);
            holder.cbSelect.setChecked(true);
            itemLists.set(position, item);
        } else if (mSelectMode == OPTION_ALL_FAVORITE) {
            setOptionView(holder);
            if (item.getFavoriteFlag() == 1) {
                holder.cbSelect.setChecked(true);
            }
        }
        holder.tvItemMemo1.setText(item.getMemo1());
        holder.bJanCd.setText(item.getJanCd());

        switch (item.getFavoriteFlag()) {
            case 0:
                holder.cbFavoriteButton.setChecked(false);
                break;
            case 1:
                holder.cbFavoriteButton.setChecked(true);
                break;
        }
        String priceStr = "¥" + String.format("%,d", item.getTaxPrice());
        holder.tvItemPrice.setText(priceStr);

        Resources res = mContext.getResources();
        switch (item.getCategoryCd()) {
            case "01":
                holder.ivLabelImg.setBackgroundColor(res.getColor(R.color.category1));
                break;
            case "02":
                holder.ivLabelImg.setBackgroundColor(res.getColor(R.color.category2));
                break;
            case "03":
                holder.ivLabelImg.setBackgroundColor(res.getColor(R.color.category3));
                break;
            case "04":
                holder.ivLabelImg.setBackgroundColor(res.getColor(R.color.category4));
                break;
        }

        holder.llItemDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                m_line = pos; //行数を登録
                m_listener.onClick(view);
            }
        });

        holder.cbFavoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                m_line = pos; //行数を登録
                m_listener.onClick(view);
            }
        });

        holder.cbSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (holder.cbSelect.isPressed()) {
                    item.setCbSelected(isChecked);
                    itemLists.set(position, item);
                }
            }
        });

        holder.bJanCd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uri = MONO_URI_HEAD + item.getJanCd() + MONO_URI_TAIL;
                Intent monoIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(uri));
                mContext.startActivity(monoIntent);
            }
        });
    }

    private void setOptionView(PostViewHolder holder) {
        Resources res = mContext.getResources();
        holder.cbSelect.setVisibility(View.VISIBLE);
        holder.cbFavoriteButton.setButtonDrawable(res.getDrawable(R.drawable.dis_favorite_button));
        holder.cbFavoriteButton.setEnabled(false);
    }

    @Override
    public int getItemCount() {
        return itemLists.size();
    }

    public int getLine() {
        return m_line; //行数を取得
    }

    public ArrayList<Integer> getSelectList() {
        ArrayList<Integer> selectList = new ArrayList<>();
        for (int i = 0; i < itemLists.size(); i++) {
            if(itemLists.get(i).getCbSelected()){
                selectList.add(itemLists.get(i).get_id());
            }
        }
        return selectList;
    }
}
