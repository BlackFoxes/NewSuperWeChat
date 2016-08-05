package cn.ucai.fulicenter.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.activity.BoutiqueDetailsActivity;
import cn.ucai.fulicenter.activity.FootHolder;
import cn.ucai.fulicenter.bean.BoutiqueBean;
import cn.ucai.fulicenter.utils.ImageUtils;
/**
 * Created by Administrator on 2016/8/3.
 */
public class BoutiqueAdapter extends RecyclerView.Adapter {
    ArrayList<BoutiqueBean> mArrayList;
    Context mContext;
    RecyclerView.ViewHolder holder;
    String tvHint;
    static final String TAG = BoutiqueAdapter.class.getSimpleName();
    public BoutiqueAdapter(Context context, ArrayList<BoutiqueBean> boutiqueArrayList) {
        this.mContext = context;
        this.mArrayList = boutiqueArrayList;
    }
    public void setTvHint(String tvHint) {
        this.tvHint = tvHint;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.e(TAG, "BoutiqueAdapter.onCreateViewHolder.isChecked");
        switch (viewType) {
            case I.TYPE_FOOTER:
                View footer = LayoutInflater.from(mContext).inflate(R.layout.load_footer, null, false);
                holder = new FootHolder(footer);
                break;
            case I.TYPE_ITEM:
                View itemBoutique = LayoutInflater.from(mContext).inflate(R.layout.item_boutique, null, false);
                holder = new BoutiqueItemHolder(itemBoutique);
                break;
        }
        return holder;
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Log.e(TAG, "BoutiqueAdapter.onBindViewHolder.isChecked");
        Log.e(TAG, "BoutiqueAdapter.onBindViewHolder.position="+position);
        if (holder instanceof FootHolder) {
            ((FootHolder)holder).mFooter.setText(tvHint);
        } else if (holder instanceof BoutiqueItemHolder) {
           final BoutiqueBean boutiqueBean = mArrayList.get(position);
            ((BoutiqueItemHolder)holder).tvBoutiqueTitle.setText(boutiqueBean.getTitle());
                ((BoutiqueItemHolder)holder).tvBoutiqueDs.setText(boutiqueBean.getDescription());
                ((BoutiqueItemHolder)holder).tvBoutiqueName.setText(boutiqueBean.getName());
                ImageUtils.setGoodAvatar(boutiqueBean.getImageurl(),mContext,((BoutiqueItemHolder)holder).mBoutiqueAvatar);
                ((BoutiqueItemHolder)holder).mBoutiqueAvatar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, BoutiqueDetailsActivity.class);
                        intent.putExtra(I.NewAndBoutiqueGood.CAT_ID, boutiqueBean.getId());
                        mContext.startActivity(intent);
                    }
                });


            }

    }

    @Override
    public int getItemCount() {
        return mArrayList.size()!=0?mArrayList.size()+1:0;
    }

    @Override
    public int getItemViewType(int position) {
        Log.e(TAG, "getItemViewType,getItemCount=" + getItemCount());
        if (position == getItemCount()-1) {
            return I.TYPE_FOOTER;
        }
        return I.TYPE_ITEM;
    }
    public void initBoutiqueData(ArrayList<BoutiqueBean> list) {
        if (mArrayList != null) {

            mArrayList.clear();
        }
        mArrayList.addAll(list);
        notifyDataSetChanged();
    }

    class BoutiqueItemHolder extends RecyclerView.ViewHolder {
        ImageView mBoutiqueAvatar;
        TextView tvBoutiqueTitle,tvBoutiqueDs, tvBoutiqueName;
        public BoutiqueItemHolder(View itemView) {
            super(itemView);
            mBoutiqueAvatar = (ImageView) itemView.findViewById(R.id.ivBoutiqueAva);
            tvBoutiqueName = (TextView) itemView.findViewById(R.id.tvBoutiqueName);
            tvBoutiqueTitle = (TextView) itemView.findViewById(R.id.tvBoutiqueTitle);
            tvBoutiqueDs = (TextView) itemView.findViewById(R.id.tvBoutiqueDescription);
        }
    }
}
