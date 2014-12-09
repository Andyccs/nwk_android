package com.nwk.locopromo;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.nwk.locopromo.adapter.PromotionGridViewAdapter;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

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
        ParseQuery promotionQuery = ParseQuery.getQuery("Promotion");
        promotionQuery.include("retail");
        promotionQuery.findInBackground(new FindCallback() {
            @Override
            public void done(List promotions, ParseException e) {
                Timber.d("Size: " + promotions.size());
                if (e == null) {
                    mGridAdapter.addItems(promotions);
                    mProgress.setVisibility(View.GONE);
                    mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    private void onClickPromotion(ParseObject parseObject) {

//        context.startActivity(intent);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mGridAdapter = new PromotionGridViewAdapter(getActivity());
        mGridView.setAdapter(mGridAdapter);

        initializeData();
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
