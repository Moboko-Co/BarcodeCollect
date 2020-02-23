package com.moboko.barcodecollect.view;


import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.moboko.barcodecollect.R;

public class PostViewHolder extends RecyclerView.ViewHolder{

    public CheckBox cbSelect,cbFavoriteButton;
    public View ivLabelImg;
    public TextView tvJanCd,tvItemPrice,tvItemMemo1;
    public LinearLayout llItemDetail;
    public ImageButton ibDeleteData;

    public PostAdapter mAdapter;

    public PostViewHolder(@NonNull View itemView, PostAdapter postAdapter) {
        super(itemView);

        mAdapter = postAdapter;

        cbSelect = itemView.findViewById(R.id.cb_select);
        ivLabelImg = itemView.findViewById(R.id.iv_label_img);
        tvJanCd = itemView.findViewById(R.id.tv_jan_cd);
        tvItemMemo1 = itemView.findViewById(R.id.tv_item_memo_1);
        tvItemPrice = itemView.findViewById(R.id.tv_item_price);
        cbFavoriteButton = itemView.findViewById(R.id.cb_favorite_button);
        llItemDetail = itemView.findViewById(R.id.ll_item_detail);
        ibDeleteData = itemView.findViewById(R.id.ib_delete_data);

    }
}
