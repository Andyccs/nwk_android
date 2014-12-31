package com.nwk.merchant;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.nwk.core.api.BackendService;
import com.nwk.core.api.UrlUtil;
import com.nwk.core.model.GrabPromotionNotInclude;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import timber.log.Timber;


public class ClaimActivity extends ActionBarActivity {

    @InjectView(R.id.claim_qr_code)
    Button claimQRCodeButton;

    @InjectView(R.id.status)
    TextView status;

    @InjectView(R.id.main_layout)
    RelativeLayout main;

    @OnClick(R.id.claim_qr_code)
    public void onClickQRCodeButton(){
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
        integrator.setPrompt("");
        integrator.setResultDisplayDuration(0);
        integrator.setScanningRectangle(500,500);
        integrator.initiateScan();

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_claim);

        ButterKnife.inject(this);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if(scanResult!=null){
            //grab_id:customer_id:promotion_id
            String content = scanResult.getContents();
            String[] splited = content.split(":");
            if(splited.length!=3){
                status.setText("Failed!");
                return;
            }

            Timber.d(content);

            final View v = LayoutInflater.from(this).inflate(R.layout.loading_with_black_mask,null);
            main.addView(v);

            BackendService service = ((MerchantApplication)getApplication()).getService();
            service.approvePromotions(
                    splited[0],
                    UrlUtil.getConsumerUrl(splited[1]),
                    UrlUtil.getPromotionUrl(splited[2]),
                    BackendService.isApproved.YES,
                    new Callback<GrabPromotionNotInclude>() {
                        @Override
                        public void success(GrabPromotionNotInclude grabPromotion, Response response) {
                            main.removeView(v);
                            status.setText("Success!");
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            main.removeView(v);
                            status.setText("Failed!");
                            Timber.e(error.getMessage());
                        }
                    }
            );

            status.setText(content);
        }
    }
}
