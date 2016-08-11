package cn.ucai.fulicenter.activity;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.pingplusplus.android.PingppLog;
import com.pingplusplus.libone.PaymentHandler;
import com.pingplusplus.libone.PingppOne;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import cn.ucai.fulicenter.D;
import cn.ucai.fulicenter.FuLiCenterApplication;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.bean.CartBean;
import cn.ucai.fulicenter.utils.DisplayUtils;

public class BuyAddressActivity extends FragmentActivity implements PaymentHandler{
    EditText mReceiver,mTel, mAvenue;
    Spinner mSpinner;
    Button mButton;
    private static String URL = "http://218.244.151.190/demo/charge";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_address);
        initView();
        setListener();
        //设置需要使用的支付方式
        PingppOne.enableChannels(new String[]{"wx", "alipay", "upacp", "bfb", "jdpay_wap"});

        // 提交数据的格式，默认格式为json
        // PingppOne.CONTENT_TYPE = "application/x-www-form-urlencoded";
        PingppOne.CONTENT_TYPE = "application/json";

        PingppLog.DEBUG = true;
    }

    private void setListener() {
        mButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String receiverName = mReceiver.getText().toString();
                if (TextUtils.isEmpty(receiverName)) {
                    mReceiver.setError("收货人姓名不能为空");
                    mReceiver.requestFocus();
                    return;
                }
                String tel = mTel.getText().toString();
                if (TextUtils.isEmpty(tel)) {
                    mReceiver.setError("电话号码不能为空");
                    mTel.requestFocus();
                    return;
                }
                if (!tel.matches("[\\d]{11}")) {
                    mReceiver.setError("电话号码格式错误");
                    mTel.requestFocus();
                    return;
                }
                String avenue = mAvenue.getText().toString();
                if (TextUtils.isEmpty(avenue)) {
                    mAvenue.setError("街道不能为空");
                    mAvenue.requestFocus();
                    return;
                }


                String spinProvice = mSpinner.getSelectedItem().toString();
                if (TextUtils.isEmpty(spinProvice)) {
                    Toast.makeText(BuyAddressActivity.this, "收货地区不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                gotoStatement();

            }
        });


    }
    private int getPrice(String price) {
        String s = price.substring(price.indexOf("￥") + 1);
        return Integer.parseInt(s);

    }

    private void gotoStatement() {

        String orderNo = new SimpleDateFormat("yyyyMMddhhmmss")
                .format(new Date());

        // 计算总金额（以分为单位）
        int amount = 0;
        JSONArray billList = new JSONArray();
        ArrayList<CartBean> cartList = FuLiCenterApplication.getInstance().getCartList();
        for (CartBean cart : cartList) {
            amount += (getPrice(cart.getGoods().getRankPrice())) * cart.getCount();
            billList.put(cart.getGoods().getGoodsName() + " x " +cart.getCount());
        }

        // 构建账单json对象
        JSONObject bill = new JSONObject();

        // 自定义的额外信息 选填
        JSONObject extras = new JSONObject();
        try {
            extras.put("extra1", "extra1");
            extras.put("extra2", "extra2");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            bill.put("order_no", orderNo);
            bill.put("amount", amount);
            bill.put("extras", extras);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //壹收款: 创建支付通道的对话框
        PingppOne.showPaymentChannels(getSupportFragmentManager(), bill.toString(), URL, this);
    }

    private void initView() {
        mButton = (Button) findViewById(R.id.order_button);
        DisplayUtils.initBackWithTitle(this,"添写收货地址");
        mReceiver = (EditText) findViewById(R.id.et_receiver);
        mTel = (EditText) findViewById(R.id.et_tel);
        mAvenue = (EditText) findViewById(R.id.et_avenue);
        mSpinner = (Spinner) findViewById(R.id.spin_province);


    }

    @Override
    public void handlePaymentResult(Intent data) {
        if (data != null) {

            // result：支付结果信息
            // code：支付结果码
            //-2:用户自定义错误
            //-1：失败
            // 0：取消
            // 1：成功
            // 2:应用内快捷支付支付结果

            if (data.getExtras().getInt("code") != 2) {
                PingppLog.d(data.getExtras().getString("result") + "  " + data.getExtras().getInt("code"));
            } else {
                String result = data.getStringExtra("result");
                try {
                    JSONObject resultJson = new JSONObject(result);
                    if (resultJson.has("error")) {
                        result = resultJson.optJSONObject("error").toString();
                    } else if (resultJson.has("success")) {
                        result = resultJson.optJSONObject("success").toString();
                    }
                    PingppLog.d("result::" + result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
