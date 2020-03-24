package com.moboko.barcodecollect.view;


import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.moboko.barcodecollect.R;

public class PostViewHolder extends RecyclerView.ViewHolder{

    public CheckBox cbSelect,cbFavoriteButton;
//    public View ivLabelImg;
    public TextView tvItemPrice,tvItemNm,tvRegisterDay,ivLabelImg;
    public LinearLayout llItemDetail,llItemDetailLeft,llItemDetailRight;
    public ImageButton ibDeleteData;
    public Button bJanCd;

    public PostAdapter mAdapter;

    public PostViewHolder(@NonNull View itemView, PostAdapter postAdapter) {
        super(itemView);

        mAdapter = postAdapter;

//        tvNo = itemView.findViewById(R.id.tv_no);
        cbSelect = itemView.findViewById(R.id.cb_select);
        ivLabelImg = itemView.findViewById(R.id.iv_label_img);
        bJanCd = itemView.findViewById(R.id.b_jan_cd);
        tvItemNm = itemView.findViewById(R.id.tv_item_nm);
        tvItemPrice = itemView.findViewById(R.id.tv_item_price);
        cbFavoriteButton = itemView.findViewById(R.id.cb_favorite_button);
        llItemDetail = itemView.findViewById(R.id.ll_item_detail);
        llItemDetailLeft = itemView.findViewById(R.id.ll_item_detail_left);
        llItemDetailRight = itemView.findViewById(R.id.ll_item_detail_right);
        ibDeleteData = itemView.findViewById(R.id.ib_delete_data);
        tvRegisterDay = itemView.findViewById(R.id.tv_register_day);

    }
}
