package com.nwk.locopromo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nwk.locopromo.R;
import com.nwk.locopromo.model.Retail;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import timber.log.Timber;


public class RetailRectangleGridViewAdapter extends BaseAdapter {
    private List<Retail> mPromotionList;
    private Context mContext;

    public RetailRectangleGridViewAdapter(Context context) {
        mContext = context;
        mPromotionList = new ArrayList<Retail>();
    }

    public void setPromotionList(List<Retail> mPromotionList) {
        this.mPromotionList = mPromotionList;
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
        Retail retail = (Retail) getItem(i);
        ViewHolder viewHolder;

        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.shop_rectangle_item, null);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        if (retail != null) {
            viewHolder.title.setText(retail.getShopName());

            Picasso.with(mContext)
                    .load(retail.getLogo_url())
                    .into(viewHolder.image);
        }
        return view;
    }

    public static class ViewHolder {
        @InjectView(R.id.image_view)
        ImageView image;

        @InjectView(R.id.title)
        TextView title;

        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
