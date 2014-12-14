package com.nwk.locopromo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nwk.locopromo.R;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import timber.log.Timber;


public class RetailGridViewAdapter extends BaseAdapter {
    private List<com.parse.ParseObject> mPromotionList;
    private Context mContext;

    public RetailGridViewAdapter(Context context) {
        mContext = context;
        mPromotionList = new ArrayList<com.parse.ParseObject>();
    }

    public void addItems(List list) {
        Timber.d("Size: " + list.size());

            mPromotionList.addAll(list);
            notifyDataSetChanged();
    }

    public void clearItems(){
        mPromotionList = new ArrayList<com.parse.ParseObject>();
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
        ParseObject retail = (ParseObject) getItem(i);
        ViewHolder viewHolder;

        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.normal_item, null);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        if (retail != null) {
            viewHolder.title.setText(retail.get("shopName").toString());

            ParseFile file = retail.getParseFile("logo");
            Picasso.with(mContext)
                    .load(file.getUrl())
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
