package cn.ucai.fulicenter.task;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.ucai.fulicenter.D;
import cn.ucai.fulicenter.FuLiCenterApplication;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.bean.CartBean;
import cn.ucai.fulicenter.bean.GoodDetailsBean;
import cn.ucai.fulicenter.bean.Result;
import cn.ucai.fulicenter.bean.UserAvatar;
import cn.ucai.fulicenter.utils.OkHttpUtils2;
import cn.ucai.fulicenter.utils.Utils;

/**
 * Created by Administrator on 2016/7/22.
 */
public class DownloadCartListTask {
    String username;
    private static final String TAG = DownloadCartListTask.class.getSimpleName();
    Context mContext;

    public DownloadCartListTask(String username, Context context) {
        this.mContext = context;
        this.username = username;
    }
    public void execute() {
        final OkHttpUtils2<CartBean[]> utils2 = new OkHttpUtils2<>();
        utils2.setRequestUrl(I.REQUEST_FIND_CARTS)
                .addParam("userName",username)
                .addParam(I.PAGE_ID, String.valueOf(I.PAGE_ID_DEFAULT))
                .addParam(I.PAGE_SIZE, String.valueOf(I.PAGE_SIZE_DEFAULT))
                .targetClass(CartBean[].class)
                .execute(new OkHttpUtils2.OnCompleteListener<CartBean[]>() {
                    @Override
                    public void onSuccess(CartBean[] carts) {
                        Log.e(TAG, "carts=" + carts);
                        if (carts != null) {
                            ArrayList<CartBean> cartList = Utils.array2List(carts);
                            ArrayList<CartBean> list = FuLiCenterApplication.getInstance().getCartList();
                            for (final CartBean cartbean : cartList) {
                                if (!list.contains(cartbean)) {
                                    OkHttpUtils2<GoodDetailsBean> utils = new OkHttpUtils2<GoodDetailsBean>();
                                    utils.setRequestUrl(I.REQUEST_FIND_GOOD_DETAILS)
                                            .addParam(D.GoodDetails.KEY_GOODS_ID, String.valueOf(cartbean.getGoodsId()))
                                            .targetClass(GoodDetailsBean.class)
                                            .execute(new OkHttpUtils2.OnCompleteListener<GoodDetailsBean>() {
                                                @Override
                                                public void onSuccess(GoodDetailsBean result) {
                                                    cartbean.setGoods(result);
                                                }

                                                @Override
                                                public void onError(String error) {
                                                    Log.e(TAG, "error=" + error);

                                                }
                                            });
                                    list.add(cartbean);
                                } else {
                                    list.get(list.indexOf(cartbean)).setChecked(cartbean.isChecked());
                                    list.get(list.indexOf(cartbean)).setCount(cartbean.getCount());
                                }
                            }
                                    mContext.sendStickyBroadcast(new Intent("update_cart_list"));


                                }
                            }




                    @Override
                    public void onError(String error) {
                        Log.e(TAG, "error="+error);

                    }
                });
    }
}
