package com.nwk.locopromo.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nwk.core.api.BackendService;
import com.nwk.core.api.UrlUtil;
import com.nwk.core.model.GrabPromotion;
import com.nwk.core.model.Retail;
import com.nwk.locopromo.PromoApplication;
import com.nwk.locopromo.R;
import com.nwk.locopromo.RedeemDetailActivity;
import com.nwk.locopromo.util.DateTimeUtil;
import com.squareup.picasso.Picasso;

import org.joda.time.DateTime;
import org.parceler.Parcels;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Andy on 12/14/2014.
 */
public class ShoppingCartListViewAdapter extends BaseAdapter {
    private List<GrabPromotion> grabPromotions;
    private Context context;

    public ShoppingCartListViewAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return grabPromotions ==null
                ?0: grabPromotions.size();
    }

    @Override
    public Object getItem(int i) {
        return grabPromotions ==null?
                null: grabPromotions.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.shopping_cart_item, null);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        if (grabPromotions.get(i) != null) {
            final GrabPromotion grabPromotion = grabPromotions.get(i);
            viewHolder.title.setText(grabPromotion.getPromotion().getTitle());

            Picasso.with(context)
                    .load(grabPromotion.getPromotion().getImageUrl())
                    .into(viewHolder.image);

            DateTime redeemByDateTime = new DateTime(grabPromotion.getRedeemTime());
            if(redeemByDateTime.isBeforeNow()){
                viewHolder.expiry.setText("Expired");
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(context,"Expired",Toast.LENGTH_LONG).show();
                    }
                });
            }else{

                String redeemBy = DateTimeUtil.toDayMonthYearHourMinute(grabPromotion.getRedeemTime());

                viewHolder.expiry.setText("Redeem by "+redeemBy);

                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Integer retailPrimaryKey =
                                UrlUtil.getRetailPrimaryKeyByUrl(
                                        grabPromotion.getPromotion().getRetail());
                        BackendService service = ((PromoApplication)((Activity)context).getApplication()).getService();
                        service.getRetail("" + retailPrimaryKey, new Callback<Retail>() {
                            @Override
                            public void success(Retail retail, Response response) {
                                Intent intent = new Intent(context, RedeemDetailActivity.class);
                                intent.putExtra("grabPromotion", Parcels.wrap(grabPromotion));
                                intent.putExtra("retail", Parcels.wrap(retail));
                                context.startActivity(intent);
                            }

                            @Override
                            public void failure(RetrofitError error) {
                                Toast.makeText(context,"Error: "+error.getMessage(),Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                });
            }

            viewHolder.venue.setText(" ");


        }
        return view;
    }

    public void setGrabPromotions(List<GrabPromotion> grabPromotions) {
        this.grabPromotions = grabPromotions;
    }

    public static class ViewHolder {
        @InjectView(R.id.promotion_image)
        ImageView image;

        @InjectView(R.id.promotion_title)
        TextView title;

        @InjectView(R.id.promotion_expiry)
        TextView expiry;

        @InjectView(R.id.promotion_venue)
        TextView venue;

        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
