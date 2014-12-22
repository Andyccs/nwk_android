package com.nwk.locopromo;

import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.TextView;

import com.nwk.locopromo.model.Promotion;
import com.nwk.locopromo.widget.AspectRatioImageView;
import com.nwk.locopromo.widget.PaperButton;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.Calendar;

import butterknife.ButterKnife;
import butterknife.InjectView;

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
    PaperButton mGrabButton;

    private Promotion mPromotion;
    private String grabId;
    boolean canGrab = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promotion_detail);
        ButterKnife.inject(this);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE|ActionBar.DISPLAY_HOME_AS_UP);

        Bundle bundle = getIntent().getExtras();
        mPromotion = Parcels.unwrap(bundle.getParcelable("promotion"));
        initializePromotion(mPromotion);

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

    private void initializePromotion(Promotion promotion) {
        if (promotion != null) {
            mTitle.setText(promotion.getTitle());
            mDescription.setText(promotion.getDescription());

            getSupportActionBar().setTitle(promotion.getTitle());

            long diff = mPromotion.getTimeExpiry().getTime() - Calendar.getInstance().getTime().getTime();

            if (diff > 0) {

                long secondsInMilli = 1000;
                long minutesInMilli = secondsInMilli * 60;
                long hoursInMilli = minutesInMilli * 60;
                long daysInMilli = hoursInMilli * 24;

                long elapsedDays = diff / daysInMilli;
                diff = diff % daysInMilli;

                long elapsedHours = diff / hoursInMilli;
                diff = diff % hoursInMilli;

                long elapsedMinutes = diff / minutesInMilli;
                diff = diff % minutesInMilli;


                StringBuilder sb = new StringBuilder();
                sb.append(elapsedDays > 0 ? elapsedDays + "d " : "")
                        .append(elapsedHours > 0 ? elapsedHours + "h " : "")
                        .append(elapsedMinutes > 0 ? elapsedMinutes + "m" : "");
                mCountDown.setText(sb.toString());
            } else {
                mOfferExpired.setVisibility(View.INVISIBLE);
                mCountDown.setText("Expired!");
            }

            int type = promotion.getType();

            String text1 = null, text2 = "";

            switch (type) {
                case 1:
                    text1 = "$" + promotion.getOriginalPrice();
                    text2 = "$" + promotion.getDiscountPrice();
                    break;
                case 2:
                    text1 = null;
                    text2 = promotion.getPercentage() + "%";
                    break;
                case 3:
                    text1 = null;
                    text2 = "" + promotion.getOriginalPrice();
            }
            Picasso.with(this)
                    .load(promotion.getImage())
                    .into(mImage);

            if (text1 != null) {
                mText1.setVisibility(View.VISIBLE);
                mText1.setText(text1);
                mText1.setPaintFlags(mText1.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            } else {
                mText1.setVisibility(View.GONE);
            }
            mText2.setText(text2);
        }
    }
}
