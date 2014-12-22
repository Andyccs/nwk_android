package com.nwk.locopromo.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nwk.locopromo.model.Promotion;
import com.nwk.locopromo.R;
import com.nwk.locopromo.RedeemDetailActivity;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Andy on 12/14/2014.
 */
public class ShoppingCartListViewAdapter extends BaseAdapter {
    private List<Promotion> promotions;
    private Context context;

    public ShoppingCartListViewAdapter(Context context) {
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
            view = LayoutInflater.from(context).inflate(R.layout.shopping_cart_item, null);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        if (promotions.get(i) != null) {
            final Promotion promotion = promotions.get(i);
            viewHolder.title.setText(promotion.getTitle());

            Picasso.with(context)
                    .load(promotion.getImage())
                    .into(viewHolder.image);

            viewHolder.expiry.setText("Redeem by 12 September 2014, 17:15");

            viewHolder.venue.setText("Marie's Shop, 02-225");

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, RedeemDetailActivity.class);
                    intent.putExtra("promotion",Parcels.wrap(promotion));
                    context.startActivity(intent);
                }
            });
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

        @InjectView(R.id.promotion_venue)
        TextView venue;

        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
