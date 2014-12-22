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
//        ParseQuery promotionQuery = ParseQuery.getQuery("Retail");
//        promotionQuery.include("user");
//        promotionQuery.findInBackground(new FindCallback() {
//            @Override
//            public void done(List retail, ParseException e) {
//                Timber.d("Size: " + retail.size());
//                if (e == null) {
//                    mGridAdapter.addItems(retail);
//                    mProgress.setVisibility(View.GONE);
//                    mSwipeRefreshLayout.setVisibility(View.VISIBLE);
//                }
//            }
//        });

        ((PromoApplication)(getActivity().getApplication())).getService().listRetails(new Callback<Wrapper<List<Retail>>>() {
            @Override
            public void success(Wrapper<List<Retail>> retails, Response response) {
                Timber.d("Size: " + retails.getResults().size());
                mGridAdapter.addItems(retails.getResults());
                mProgress.setVisibility(View.GONE);
                mSwipeRefreshLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void failure(RetrofitError error) {
                Timber.e(error.getMessage());
            }
        });

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mGridAdapter = new RetailSquareGridViewAdapter(getActivity());
        mGridView.setAdapter(mGridAdapter);

        initializeData();

        AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), PromotionListActivity.class);
                //TODO add extra intent here
                startActivity(intent);
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
