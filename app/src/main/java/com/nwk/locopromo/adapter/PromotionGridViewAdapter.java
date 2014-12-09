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
import com.parse.ParseObject;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import timber.log.Timber;


public class PromotionGridViewAdapter extends BaseAdapter {
    private List<Promotion> mPromotionList;
    private Context mContext;


    public PromotionGridViewAdapter(Context context) {
        mContext = context;
        mPromotionList = new ArrayList<Promotion>();
    }

    public void addItems(List list) {
        Timber.d("Size: " + list.size());

        for (Object o : list) {
            ParseObject object = (ParseObject) o;
            Promotion promotion = new Promotion();
            promotion.setId(object.getObjectId());
            promotion.setTitle(object.getString("title"));
            promotion.setDescription(object.getString("description"));
            promotion.setImage(object.getParseFile("image").getUrl());
            promotion.setQuantity(object.getInt("quantity"));
            promotion.setTimeExpiry(object.getDate("timeExpiry"));
            promotion.setDiscountPrice(object.getInt("discountPrice"));
            promotion.setOriginalPrice(object.getInt("originalPrice"));
            promotion.setPercentage(object.getInt("percentage"));
            promotion.setType(object.getInt("type"));
            promotion.setRetail(object.getParseObject("retail").getObjectId());
            mPromotionList.add(promotion);
        }
        notifyDataSetChanged();
    }

    public void clearItems(){
        mPromotionList = new ArrayList<Promotion>();
    }

    @Override
    public int getCount() {
        return mPromotionList.size();
    }

    @Override
    public Object getItem(int i) {
        return mPromotionList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Promotion promotion = (Promotion) getItem(i);
        ViewHolder viewHolder;

        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.normal_item, null);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        if (promotion != null) {
            viewHolder.title.setText(promotion.getTitle());

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
            Picasso.with(mContext)
                    .load(promotion.getImage())
                    .into(viewHolder.image);

            if (text1 != null) {
                viewHolder.text1.setVisibility(View.VISIBLE);
                viewHolder.text1.setText(text1);
                viewHolder.text1.setPaintFlags(viewHolder.text1.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            } else {
                viewHolder.text1.setVisibility(View.GONE);
            }
            viewHolder.text2.setText(text2);
        }
        return view;
    }

    public static class ViewHolder {
        @InjectView(R.id.image_view)
        ImageView image;
        @InjectView(R.id.title)
        TextView title;
        @InjectView(R.id.text1)
        TextView text1;
        @InjectView(R.id.text2)
        TextView text2;

        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
