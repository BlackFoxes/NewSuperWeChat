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



    }
}
