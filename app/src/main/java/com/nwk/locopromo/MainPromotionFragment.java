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
import android.widget.TextView;

import com.nwk.locopromo.adapter.RetailSquareGridViewAdapter;
import com.nwk.locopromo.model.Retail;
import com.nwk.locopromo.model.Wrapper;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
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

    @InjectView(R.id.placeholder)
    TextView placeholder;

    private RetailSquareGridViewAdapter mGridAdapter;

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

        mGridAdapter = new RetailSquareGridViewAdapter(getActivity());
        mGridView.setAdapter(mGridAdapter);


        mSwipeRefreshLayout.setColorSchemeColors(R.color.color_red_accent);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(true);
                initializeData();
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mSwipeRefreshLayout.setRefreshing(true);
        initializeData();
    }

    private void initializeData() {
        ((PromoApplication)(getActivity().getApplication())).getService().listRetails(new Callback<Wrapper<List<Retail>>>() {
            @Override
            public void success(Wrapper<List<Retail>> retails, Response response) {
                Timber.d("Size: " + retails.getResults().size());
                mProgress.setVisibility(View.GONE);
                mSwipeRefreshLayout.setRefreshing(false);
                mGridAdapter.setPromotionList(retails.getResults());
                mGridAdapter.notifyDataSetChanged();
                if(retails.getResults().size()>0) {
                    placeholder.setVisibility(View.GONE);
                }else{
                    placeholder.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Timber.e(error.getMessage());
                mProgress.setVisibility(View.GONE);
                mSwipeRefreshLayout.setRefreshing(false);
                placeholder.setVisibility(View.VISIBLE);
            }
        });

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
