package com.nwk.locopromo.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nwk.locopromo.PromotionListActivity;
import com.nwk.locopromo.R;
import com.nwk.locopromo.model.Retail;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.squareup.picasso.Picasso;

import org.parceler.Parcel;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import timber.log.Timber;


public class RetailSquareGridViewAdapter extends BaseAdapter {
    private List<Retail> mPromotionList;
    private Context mContext;

    public RetailSquareGridViewAdapter(Context context) {
        mContext = context;
        mPromotionList = new ArrayList<Retail>();
    }

    public List<Retail> getPromotionList() {
        return mPromotionList;
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
        final Retail retail = (Retail) getItem(i);
        ViewHolder viewHolder;

        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.shop_square_item, null);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        if (retail != null) {
            viewHolder.title.setText(retail.getShop_name());

            Picasso.with(mContext)
                    .load(retail.getLogo_url())
                    .into(viewHolder.image);
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, PromotionListActivity.class);
                Parcelable retailParcel = Parcels.wrap(retail);
                intent.putExtra("retail",retailParcel);
                mContext.startActivity(intent);
            }
        });
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
