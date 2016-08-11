package cn.ucai.fulicenter.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Spinner;

import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.utils.DisplayUtils;

public class BuyAddressActivity extends Activity {
    EditText mReceiver,mTel, mAvenue;
    Spinner mSpinner;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_address);
        initView();
    }

    private void initView() {
        DisplayUtils.initBackWithTitle(this,"添写收货地址");
        mReceiver = (EditText) findViewById(R.id.et_receiver);
        mTel = (EditText) findViewById(R.id.et_tel);
        mAvenue = (EditText) findViewById(R.id.et_avenue);
        mSpinner = (Spinner) findViewById(R.id.spin_province);


    }
}
