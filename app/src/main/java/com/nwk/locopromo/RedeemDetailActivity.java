package com.nwk.locopromo;

import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.TextView;

import com.nwk.core.model.CredentialPreferences;
import com.nwk.core.model.GrabPromotion;
import com.nwk.core.model.OldPromotion;
import com.nwk.core.model.Retail;
import com.nwk.locopromo.util.DateTimeUtil;
import com.nwk.locopromo.widget.AspectRatioImageView;
import com.squareup.picasso.Picasso;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;
import org.parceler.Parcels;
import org.w3c.dom.Text;

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


    private void initializePromotion(GrabPromotion promotion) {
        mTitle.setText(promotion.getPromotion().getTitle());

        getSupportActionBar().setTitle(promotion.getPromotion().getTitle());

//        int type = promotion.getType();
//
//        String text1 = null, text2 = "";
//
//        switch (type) {
//            case 1:
//                text1 = "$" + promotion.getOriginalPrice();
//                text2 = "$" + promotion.getDiscountPrice();
//                break;
//            case 2:
//                text1 = null;
//                text2 = promotion.getPercentage() + "%";
//                break;
//            case 3:
//                text1 = null;
//                text2 = "$" + promotion.getOriginalPrice();
//        }
        Picasso.with(this)
                .load(promotion.getPromotion().getImageUrl())
                .into(mImage);

//        if (text1 != null) {
//            mText1.setVisibility(View.VISIBLE);
//            mText1.setText(text1);
//            mText1.setPaintFlags(mText1.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
//        } else {
//            mText1.setVisibility(View.GONE);
//        }
//        mText2.setText(text2);

        venue.setText("Location: Level "+mRetail.getLocationLevel()+", #"+mRetail.getLocationUnit());

        emailAddress.setText(CredentialPreferences.getEmail(this));

        grabId.setText("grab id: "+promotion.getId());


        String redeemBy = DateTimeUtil.toDayMonthYearHourMinute(promotion.getRedeemTime());

        redeemTime.setText("Redeem by " + redeemBy);
    }
}
