package com.nwk.locopromo;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.WriterException;
import com.nwk.core.model.CredentialPreferences;
import com.nwk.core.model.GrabPromotion;
import com.nwk.core.model.Retail;
import com.nwk.locopromo.util.DateTimeUtil;
import com.nwk.locopromo.util.QRCode;
import com.nwk.locopromo.widget.AspectRatioImageView;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.io.IOException;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class RedeemDetailActivity extends ActionBarActivity {
    @InjectView(R.id.title)
    TextView mTitle;

    @InjectView(R.id.text1)
    TextView mText1;

    @InjectView(R.id.text2)
    TextView mText2;

    @InjectView(R.id.image)
    AspectRatioImageView mImage;

    @InjectView(R.id.venue)
    TextView venue;

    @InjectView(R.id.grab_id)
    TextView grabId;

    @InjectView(R.id.email_address)
    TextView emailAddress;

    @InjectView(R.id.redeem_time)
    TextView redeemTime;

    @InjectView(R.id.qrcode)
    ImageView qrcode;

    private GrabPromotion mPromotion;

    private Retail mRetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redeem_detail);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE|ActionBar.DISPLAY_HOME_AS_UP);

        ButterKnife.inject(this);

        Bundle bundle = getIntent().getExtras();
        mPromotion = Parcels.unwrap(bundle.getParcelable("grabPromotion"));
        mRetail = Parcels.unwrap(bundle.getParcelable("retail"));
        initializePromotion(mPromotion);
    }


    private void initializePromotion(final GrabPromotion promotion) {
        mTitle.setText(promotion.getPromotion().getTitle());

        getSupportActionBar().setTitle(promotion.getPromotion().getTitle());

        Picasso.with(this)
                .load(promotion.getPromotion().getImageUrl())
                .into(mImage);

        venue.setText("Location: Level "+mRetail.getLocationLevel()+", #"+mRetail.getLocationUnit());

        emailAddress.setText(CredentialPreferences.getEmail(this));

        grabId.setText("grab id: "+promotion.getId());

        String redeemBy = DateTimeUtil.toDayMonthYearHourMinute(promotion.getRedeemTime());

        redeemTime.setText("Redeem by " + redeemBy);

        Runnable generateQRCodeRunnable = new Runnable() {
            @Override
            public void run() {

                try {
                    final Bitmap bitmap = QRCode.generate(300, 300,
                            "" + promotion.getId()+":"
                                    +CredentialPreferences.getCustomerPrimaryKey(getApplicationContext())+":"
                                    +promotion.getPromotion().getId());

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            qrcode.setImageBitmap(bitmap);
                        }
                    });
                } catch (WriterException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        Thread generateQrCodeThread = new Thread(generateQRCodeRunnable);
        generateQrCodeThread.run();
    }
}
