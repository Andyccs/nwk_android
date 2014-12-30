package com.nwk.locopromo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nwk.core.model.GrabPromotion;
import com.nwk.locopromo.R;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Andy on 12/14/2014.
 */
public class RedeemHistoryListViewAdapter extends BaseAdapter {
    private List<GrabPromotion> promotions;
    private Context context;

    public RedeemHistoryListViewAdapter(Context context) {
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
            view = LayoutInflater.from(context).inflate(R.layout.redeem_history_item, null);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        if (promotions.get(i) != null) {
            final GrabPromotion promotion = promotions.get(i);

            //TODO integrate to backend
//            viewHolder.title.setText(promotion.getTitle());
//
//            Picasso.with(context)
//                    .load(promotion.getImage())
//                    .into(viewHolder.image);
        }

        view.setClickable(false);

        return view;
    }

    public void setPromotions(List<GrabPromotion> promotions) {
        this.promotions = promotions;
    }

    public static class ViewHolder {
        @InjectView(R.id.promotion_image)
        ImageView image;

        @InjectView(R.id.promotion_title)
        TextView title;

        @InjectView(R.id.point)
        TextView point;

        @InjectView(R.id.redeem_time)
        TextView redeemTime;

        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
