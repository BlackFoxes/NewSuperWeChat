package cn.ucai.fulicenter.task;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.ArrayList;

import cn.ucai.fulicenter.FuLiCenterApplication;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.bean.CartBean;
import cn.ucai.fulicenter.bean.MessageBean;
import cn.ucai.fulicenter.utils.OkHttpUtils2;

/**
 * Created by Administrator on 2016/8/10.
 */
public class UpdateCartTask {
    static final String TAG = UpdateCartTask.class.getSimpleName();
    Context mContext;
    CartBean mCartBean;

    public UpdateCartTask(Context mContext, CartBean mCartBean) {
        this.mContext = mContext;
        this.mCartBean = mCartBean;
    }

    public void execute() {

        final OkHttpUtils2<MessageBean> utils2 = new OkHttpUtils2<>();

        if (mCartBean.getId() == 0) {
            utils2.setRequestUrl(I.REQUEST_ADD_CART)
                    .addParam(I.Cart.GOODS_ID, String.valueOf(mCartBean.getGoodsId()))
                    .addParam(I.Cart.USER_NAME, mCartBean.getUserName())
                    .addParam(I.Cart.IS_CHECKED, String.valueOf(mCartBean.isChecked()))
                    .addParam(I.Cart.COUNT, String.valueOf(mCartBean.getCount()))
                    .targetClass(MessageBean.class)
                    .execute(new OkHttpUtils2.OnCompleteListener<MessageBean>() {
                        @Override
                        public void onSuccess(MessageBean result) {
                            if (result != null && result.isSuccess()) {
                                Log.e(TAG, "mCartBean.getId() == 0,result=" + result);
                                new DownloadCartListTask(FuLiCenterApplication.getInstance().getUserName(),
                                        mContext).execute();
                            }

                        }
                        @Override
                        public void onError(String error) {
                            Log.e(TAG, "error=" + error);
                        }
                    });
        } else {
            if (mCartBean.getCount() > 0) {
                utils2.setRequestUrl(I.REQUEST_UPDATE_CART)
                        .addParam(I.Cart.ID, String.valueOf(mCartBean.getId()))
                        .addParam(I.Cart.COUNT, String.valueOf(mCartBean.getCount()))
                        .addParam(I.Cart.IS_CHECKED, String.valueOf(mCartBean.isChecked()))
                        .targetClass(MessageBean.class)
                        .execute(new OkHttpUtils2.OnCompleteListener<MessageBean>() {
                            @Override
                            public void onSuccess(MessageBean result) {
                                if (result != null && result.isSuccess()) {
                                    ArrayList<CartBean> cartList = FuLiCenterApplication.getInstance().getCartList();
                                    if (cartList.indexOf(mCartBean)>0) {
                                        cartList.get(cartList.indexOf(mCartBean)).setChecked(mCartBean.isChecked());
                                        cartList.get(cartList.indexOf(mCartBean)).setCount(mCartBean.getCount());
//                                cartList.set(cartList.indexOf(mCartBean), mCartBean);


                                    }
                                    Log.e(TAG, "result=" + result);
                                }
                                mContext.sendStickyBroadcast(new Intent("update_cart_list"));

                            }

                            @Override
                            public void onError(String error) {
                                Log.e(TAG, "error=" + error);


                            }
                        });
            } else if (mCartBean.getCount() <= 0) {
                utils2.setRequestUrl(I.REQUEST_DELETE_CART)
                        .addParam(I.Cart.ID, String.valueOf(mCartBean.getId()))
                        .targetClass(MessageBean.class)
                        .execute(new OkHttpUtils2.OnCompleteListener<MessageBean>() {
                            @Override
                            public void onSuccess(MessageBean result) {
                                if (result != null && result.isSuccess()) {
                                    ArrayList<CartBean> cartList = FuLiCenterApplication.getInstance().getCartList();
                                    if (cartList.indexOf(mCartBean)>0) {
                                        cartList.remove(cartList.indexOf(mCartBean));
//                                cartList.set(cartList.indexOf(mCartBean), mCartBean);


                                    }
                                    Log.e(TAG, "result=" + result);
                                }
                                mContext.sendStickyBroadcast(new Intent("update_cart_list"));


                            }

                            @Override
                            public void onError(String error) {
                                Log.e(TAG, "error=" + error);


                            }
                        });
//            url=http://10.0.2.2:8080/ FuLiCenterServer/Server?request=add_cart&goodsId=&userName=
//            & goodsName=&goodsEnglishName=&goodsThumb=&goodsImg=&addTime=&isSelected=


            }

        }



    }
}
