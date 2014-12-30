package com.nwk.locopromo.adapter;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nwk.core.api.BackendService;
import com.nwk.core.model.Promotion;
import com.nwk.core.model.PromotionDiscount;
import com.nwk.core.model.PromotionGeneral;
import com.nwk.core.model.PromotionReduction;
import com.nwk.core.model.Retail;
import com.nwk.locopromo.PromoApplication;
import com.nwk.locopromo.PromotionDetailActivity;
import com.nwk.locopromo.R;
import com.nwk.locopromo.util.FavoriteRetailsUtil;
import com.squareup.picasso.Picasso;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;
import org.parceler.Parcels;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import timber.log.Timber;

/**
 * Created by Andy on 12/14/2014.
 */
public class AllPromotionListViewAdapter extends BaseAdapter {
    private List<Promotion> promotions;
    private Activity context;

    public AllPromotionListViewAdapter(Activity context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return promotions==null
                ?0:promotions.size();
    }

    @Override
    public Object getItem(int i) {
        return promotions==null?
                null:promotions.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.promotion_item, null);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        if (promotions.get(i) != null) {
            final Promotion promotion = promotions.get(i);
            viewHolder.title.setText(promotion.getTitle());

            Picasso.with(context)
                    .load(promotion.getImageUrl())
                    .into(viewHolder.image);

            DateTime expiry = new DateTime(promotion.getCreatedAt());
            expiry = expiry.plusMinutes(30);
            if(expiry.isBeforeNow()){
                viewHolder.expiry.setText("Expired");
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(context,"The promotion has expired",Toast.LENGTH_LONG).show();
                    }
                });
            }else{
                Timber.d("Promotion Created At: "+new DateTime(promotion.getCreatedAt()));
                Timber.d("Current Time: "+DateTime.now());
                Timber.d("Expiry Time: "+expiry);
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
                Timber.d("Expiry in: "+ expiryIntervalString);
                viewHolder.expiry.setText("Offer expires in: " + expiryIntervalString);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        BackendService service = ((PromoApplication)context.getApplication()).getService();
                        service.getRetail(
                                ""+FavoriteRetailsUtil.getRetailPrimaryKeyByUrl(promotion.getRetail()),
                                new Callback<Retail>() {
                                    @Override
                                    public void success(Retail retail, Response response) {
                                        Intent intent = new Intent(context, PromotionDetailActivity.class);
                                        intent.putExtra("promotion",Parcels.wrap(promotion));
                                        intent.putExtra("retail",Parcels.wrap(retail));
                                        context.startActivity(intent);
                                    }

                                    @Override
                                    public void failure(RetrofitError error) {
                                        Toast.makeText(context,"Connection error, please try again later.",Toast.LENGTH_LONG).show();
                                    }
                                });
                    }
                });
            }


            String text1 = null;
            String text2 = "";
            if(promotion instanceof PromotionReduction) {
                PromotionReduction pr = (PromotionReduction) promotion;
                text1 = "$" + pr.getOriginalPrice();
                text2 = "$" + pr.getDiscountPrice();
            }
            else if(promotion instanceof PromotionDiscount) {
                PromotionDiscount pd = (PromotionDiscount) promotion;
                text1 = null;
                text2 = pd.getDiscount() + "%";
            }
            else if(promotion instanceof PromotionGeneral) {
                text1 = null;
                text2 = "$" + ((PromotionGeneral) promotion).getPrice();
            }

            if (text1 != null) {
                viewHolder.originalPrice.setVisibility(View.VISIBLE);
                viewHolder.originalPrice.setText(text1);
                viewHolder.originalPrice.setPaintFlags(viewHolder.originalPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            } else {
                viewHolder.originalPrice.setVisibility(View.GONE);
            }
            viewHolder.price.setText(text2);


        }
        return view;
    }

    public void setPromotions(List<Promotion> promotions) {
        this.promotions = promotions;
    }

    public static class ViewHolder {
        @InjectView(R.id.promotion_image)
        ImageView image;

        @InjectView(R.id.promotion_title)
        TextView title;

        @InjectView(R.id.promotion_expiry)
        TextView expiry;

        @InjectView(R.id.promotion_original_price)
        TextView originalPrice;

        @InjectView(R.id.promotion_price)
        TextView price;

        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
