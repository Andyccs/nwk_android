package com.nwk.locopromo;

import android.graphics.Paint;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.nwk.locopromo.widget.AspectRatioImageView;
import com.nwk.locopromo.widget.PaperButton;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.Calendar;

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

    private Promotion mPromotion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redeem_detail);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE|ActionBar.DISPLAY_HOME_AS_UP);

        ButterKnife.inject(this);

        Bundle bundle = getIntent().getExtras();
        mPromotion = Parcels.unwrap(bundle.getParcelable("promotion"));
        initializePromotion(mPromotion);
    }


    private void initializePromotion(Promotion promotion) {
        if (promotion != null) {
            mTitle.setText(promotion.getTitle());

            getSupportActionBar().setTitle(promotion.getTitle());

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
