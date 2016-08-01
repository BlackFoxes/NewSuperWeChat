package cn.ucai.fulicenter.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import java.util.ArrayList;
import java.util.List;

import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.bean.NewGoodBean;

/**
 * Created by Administrator on 2016/8/1.
 */
public class GoodAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    Context mContext;
    ArrayList<NewGoodBean> mArrayList;
    RecyclerView.ViewHolder holder;

    public GoodAdapter(Context mContext, ArrayList<NewGoodBean> list) {
        this.mContext = mContext;
        this.mArrayList = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.new_good_details, null, false);
        holder = new NewGoodHolder(inflate);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        NewGoodBean goodBean = mArrayList.get(position);

        ((NewGoodHolder)holder).mPrice.setText(goodBean.getCurrencyPrice());
        ((NewGoodHolder)holder).mName.setText(goodBean.getGoodsName());
        ((NewGoodHolder)holder).mGoodAvatar.setImageResource(R.drawable.nopic);


    }

    @Override
    public int getItemCount() {
        return mArrayList.size();

    }

    class NewGoodHolder extends RecyclerView.ViewHolder {
        ImageView mGoodAvatar;
        TextView mPrice;
        TextView mName;


        public NewGoodHolder(View itemView) {
            super(itemView);
            mGoodAvatar = (ImageView) itemView.findViewById(R.id.goodAvatar);
            mPrice = (TextView) itemView.findViewById(R.id.tvPrice);
            mName = (TextView) itemView.findViewById(R.id.tvNewGoodName);
        }

    }
}
