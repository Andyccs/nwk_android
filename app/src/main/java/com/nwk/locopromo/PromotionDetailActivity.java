package com.nwk.locopromo;

import android.graphics.Paint;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nwk.core.model.Promotion;
import com.nwk.core.model.PromotionDiscount;
import com.nwk.core.model.PromotionGeneral;
import com.nwk.core.model.PromotionReduction;
import com.nwk.core.model.Retail;
import com.nwk.locopromo.widget.AspectRatioImageView;
import com.squareup.picasso.Picasso;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;
import org.parceler.Parcels;

import butterknife.ButterKnife;
import butterknife.InjectView;
import icepick.Icicle;

public class PromotionDetailActivity extends ActionBarActivity {
    @InjectView(R.id.title)
    TextView mTitle;

    @InjectView(R.id.description)
    TextView mDescription;

    @InjectView(R.id.text1)
    TextView mText1;

    @InjectView(R.id.text2)
    TextView mText2;

    @InjectView(R.id.image)
    AspectRatioImageView mImage;

    @InjectView(R.id.count_down)
    TextView mCountDown;

    @InjectView(R.id.offer_expired)
    View mOfferExpired;

    @InjectView(R.id.grab_button)
    Button mGrabButton;

    @InjectView(R.id.offer_expired_layout)
    LinearLayout offerExpiredLayout;

    @InjectView(R.id.location)
    TextView location;

    Promotion mPromotion;
    Retail retail;

    @Icicle
    Parcelable promotionParcelable;

    @Icicle
    Parcelable retailParcelable;

    private String grabId;
    boolean canGrab = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promotion_detail);
        ButterKnife.inject(this);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE|ActionBar.DISPLAY_HOME_AS_UP);
        actionBar.setDisplayHomeAsUpEnabled(true);

        if(promotionParcelable==null||retailParcelable==null){
            Bundle bundle = getIntent().getExtras();
            promotionParcelable = bundle.getParcelable("promotion");
            retailParcelable = bundle.getParcelable("retail");
        }
        mPromotion = Parcels.unwrap(promotionParcelable);
        retail = Parcels.unwrap(retailParcelable);

        initializePromotion();

        final View.OnClickListener claimOffer = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (grabId != null) {
//                    AlertDialog.Builder builder = new AlertDialog.Builder(PromotionDetailActivity.this);
//                    View v = LayoutInflater.from(PromotionDetailActivity.this).inflate(R.layout.claim_promotion_layout, null);
//                    AlertDialog dialog = builder.create();
//                    dialog.setView(v);
//                    dialog.show();
//
//                    ImageView imageView = (ImageView) dialog.findViewById(R.id.image);
//                    String url = getQrCodeUrl(grabId);
//                    Timber.d("Url: " + url);
//                    Picasso.with(PromotionDetailActivity.this).load(url).into(imageView);
//                }
            }
        };

    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshExpiryTime();
    }

    private void initializePromotion() {
        mTitle.setText(mPromotion.getTitle());
        mDescription.setText(mPromotion.getDescription());

        getSupportActionBar().setTitle(mPromotion.getTitle());

        // set expiry time
        refreshExpiryTime();

        //set price
        String text1 = null, text2 = "";
        if(mPromotion instanceof PromotionReduction) {
            PromotionReduction pr = (PromotionReduction) mPromotion;
            text1 = "$" + pr.getOriginalPrice();
            text2 = "$" + pr.getDiscountPrice();
        }
        else if(mPromotion instanceof PromotionDiscount) {
            PromotionDiscount pd = (PromotionDiscount) mPromotion;
            text1 = null;
            text2 = pd.getDiscount() + "%";
        }
        else if(mPromotion instanceof PromotionGeneral) {
            text1 = null;
            text2 = "$" + ((PromotionGeneral) mPromotion).getPrice();
        }
        if (text1 != null) {
            mText1.setVisibility(View.VISIBLE);
            mText1.setText(text1);
            mText1.setPaintFlags(mText1.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            mText1.setVisibility(View.GONE);
        }
        mText2.setText(text2);

        //set image
        Picasso.with(this)
                .load(mPromotion.getImageUrl())
                .into(mImage);

        //set location
        location.setText("Location: Level "+retail.getLocationLevel()+", #"+retail.getLocationUnit());
    }

    private void refreshExpiryTime() {
        DateTime expiry = new DateTime(mPromotion.getCreatedAt());
        expiry = expiry.plusMinutes(30);
        if(expiry.isBeforeNow()){
            mOfferExpired.setVisibility(View.GONE);
            mCountDown.setVisibility(View.GONE);
            offerExpiredLayout.setVisibility(View.GONE);
        }else{
            Interval interval = new Interval(DateTime.now().getMillis(),expiry.getMillis());
            PeriodFormatter daysHoursMinutes = new PeriodFormatterBuilder()
                    .appendDays()
                    .appendSuffix("d", "d")
                    .appendSeparator(" ")
                    .appendMinutes()
                    .appendSuffix("m", "m")
                    .appendSeparator(" ")
                    .appendSeconds()
                    .appendSuffix("s", "s")
                    .toFormatter();
            String expiryIntervalString = daysHoursMinutes.print(interval.toPeriod());
            mCountDown.setText(expiryIntervalString);
        }
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id == android.R.id.home){
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
