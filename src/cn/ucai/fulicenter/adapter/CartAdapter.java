package cn.ucai.fulicenter.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.activity.BoutiqueDetailsActivity;
import cn.ucai.fulicenter.activity.FootHolder;
import cn.ucai.fulicenter.bean.BoutiqueBean;
import cn.ucai.fulicenter.bean.CartBean;
import cn.ucai.fulicenter.utils.ImageUtils;

/**
 * Created by Administrator on 2016/8/3.
 */
public class CartAdapter extends RecyclerView.Adapter {
    ArrayList<CartBean> mArrayList;
    Context mContext;
    RecyclerView.ViewHolder holder;
    static final String TAG = CartAdapter.class.getSimpleName();
    public CartAdapter(Context context, ArrayList<CartBean> boutiqueArrayList) {
        this.mContext = context;
        this.mArrayList = boutiqueArrayList;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.e(TAG, "BoutiqueAdapter.onCreateViewHolder.isChecked");
        View itemBoutique = LayoutInflater.from(mContext).inflate(R.layout.item_boutique, null, false);
        holder = new CartItemHolder(itemBoutique);
        return holder;
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Log.e(TAG, "BoutiqueAdapter.onBindViewHolder.isChecked");
        Log.e(TAG, "BoutiqueAdapter.onBindViewHolder.position="+position);

           final CartBean cartBean = mArrayList.get(position);
            String currencyPrice = cartBean.getGoods().getCurrencyPrice();
            int price = Integer.parseInt(currencyPrice.substring(currencyPrice.indexOf("￥") + 1));
            int allPrice=(cartBean.getCount())*price;
            ((CartItemHolder)holder).tvCartTitle.setText("￥"+allPrice);
                ((CartItemHolder)holder).tvCartCount.setText("("+cartBean.getCount()+")");
                ImageUtils.setGoodAvatar(cartBean.getGoods().getGoodsThumb(),mContext,((CartItemHolder)holder).mCartAvatar);
                ((CartItemHolder)holder).mCartDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, BoutiqueDetailsActivity.class);
                        intent.putExtra(I.NewAndBoutiqueGood.CAT_ID, cartBean.getId());
                        mContext.startActivity(intent);
                    }
                });
            ((CartItemHolder)holder).mCartAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, BoutiqueDetailsActivity.class);
                    intent.putExtra(I.NewAndBoutiqueGood.CAT_ID, cartBean.getId());
                    mContext.startActivity(intent);
                }
            });




    }

    @Override
    public int getItemCount() {
        return mArrayList.size()!=0?mArrayList.size():0;
    }

    public void initBoutiqueData(ArrayList<CartBean> list) {
        if (mArrayList != null) {

            mArrayList.clear();
        }
        mArrayList.addAll(list);
        notifyDataSetChanged();
    }

    class CartItemHolder extends RecyclerView.ViewHolder {
        CheckBox mCheckBox;
        ImageView mCartAvatar,mCartDelete,mCartAdd;
        TextView tvCartTitle,tvCartCount, tvCartPrice;
        public CartItemHolder(View itemView) {
            super(itemView);
            mCheckBox = (CheckBox) itemView.findViewById(R.id.cb_cart);
            mCartAvatar = (ImageView) itemView.findViewById(R.id.cart_avatar);
            mCartDelete = (ImageView) itemView.findViewById(R.id.cart_delete);
            mCartAdd = (ImageView) itemView.findViewById(R.id.cart_add);
            tvCartTitle = (TextView) itemView.findViewById(R.id.name_cart);
            tvCartCount = (TextView) itemView.findViewById(R.id.count_cart);
            tvCartPrice = (TextView) itemView.findViewById(R.id.price_cart);
        }
    }
}
