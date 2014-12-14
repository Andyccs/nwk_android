package com.nwk.locopromo.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nwk.locopromo.Promotion;
import com.nwk.locopromo.R;
import com.parse.ParseFile;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Andy on 12/14/2014.
 */
public class PromotionListViewAdapter extends BaseAdapter {
    private List<Promotion> promotions;
    private Context context;

    public PromotionListViewAdapter(Context context) {
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
            view = LayoutInflater.from(context).inflate(R.layout.normal_item, null);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        if (promotions.get(i) != null) {
            Promotion promotion = promotions.get(i);
            viewHolder.title.setText(promotion.getTitle());

            Picasso.with(context)
                    .load(promotion.getImage())
                    .into(viewHolder.image);

            viewHolder.expiry.setText("Offer expires in: "+"2h 20m");

            String text1 = null;
            String text2 = "";
            switch (promotion.getType()) {
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
