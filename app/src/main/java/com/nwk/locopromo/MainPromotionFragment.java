package com.nwk.locopromo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.nwk.locopromo.adapter.PromotionGridViewAdapter;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.parceler.Parcels;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import timber.log.Timber;


public class MainPromotionFragment extends Fragment {

    @InjectView(R.id.grid_view)
    GridView mGridView;

    @InjectView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @InjectView(R.id.content)
    View mContent;

    @InjectView(R.id.progress)
    View mProgress;

    private PromotionGridViewAdapter mGridAdapter;

    private Context context;

    public MainPromotionFragment() {

    }

    public static MainPromotionFragment newInstance() {
        MainPromotionFragment fragment = new MainPromotionFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main_promotion, container, false);
        ButterKnife.inject(this, view);

        mSwipeRefreshLayout.setColorSchemeColors(R.color.color_red_accent);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initializeData();
            }
        });
        mSwipeRefreshLayout.setRefreshing(true);

        return view;
    }

    private void initializeData() {
        ParseQuery promotionQuery = ParseQuery.getQuery("Retail");
        promotionQuery.include("user");
        promotionQuery.findInBackground(new FindCallback() {
            @Override
            public void done(List retail, ParseException e) {
                Timber.d("Size: " + retail.size());
                if (e == null) {
                    mGridAdapter.addItems(retail);
                    mProgress.setVisibility(View.GONE);
                    mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mGridAdapter = new PromotionGridViewAdapter(getActivity());
        mGridView.setAdapter(mGridAdapter);

        initializeData();

        AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                ParseQuery<ParseObject> promotedQuery = ParseQuery.getQuery("Promotion");
                promotedQuery.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> list, ParseException e) {
                        Intent intent = new Intent(getActivity(), PromotionDetailActivity.class);

                        ParseObject object = list.get(0);
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

                        intent.putExtra("promotion", Parcels.wrap(promotion));
                        startActivity(intent);
                    }
                });

            }
        };

        mGridView.setOnItemClickListener(onItemClickListener);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context = activity;
    }


    @Override
    public void onDetach() {
        super.onDetach();
    }
}
