package com.nwk.locopromo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nwk.core.model.Retail;
import com.nwk.locopromo.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class RetailRectangleGridViewAdapter extends BaseAdapter {
    private List<Retail> mPromotionList;
    private Context mContext;

    private Hashtable<Integer,Boolean> selected;

    public RetailRectangleGridViewAdapter(Context context) {
        mContext = context;
        mPromotionList = new ArrayList<Retail>();
        selected = new Hashtable<>();
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
        final ViewHolder viewHolder;

        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.shop_rectangle_item, null);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        if (retail != null) {
            selected.put(retail.getId(),false);

            viewHolder.title.setText(retail.getShopName());

            Picasso.with(mContext)
                    .load(retail.getLogo_url())
                    .into(viewHolder.image);

            viewHolder.like.setVisibility(View.INVISIBLE);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(selected.get(retail.getId())) {
                        viewHolder.like.setVisibility(View.INVISIBLE);
                        selected.put(retail.getId(),false);
                    }else{
                        viewHolder.like.setVisibility(View.VISIBLE);
                        selected.put(retail.getId(),true);
                    }
                }
            });
        }
        return view;
    }

    public List<String> getSelectedRetails(){
        List<String> results = new ArrayList<>();
        for(Integer i : selected.keySet()){
            if(selected.get(i)){
                results.add(""+i);
            }
        }
        return results;
    }

    public static class ViewHolder {
        @InjectView(R.id.image_view)
        ImageView image;

        @InjectView(R.id.title)
        TextView title;

        @InjectView(R.id.like)
        ImageView like;

        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
