package com.nwk.locopromo;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import com.nwk.core.api.BackendService;
import com.nwk.locopromo.adapter.RetailSquareGridViewAdapter;
import com.nwk.core.model.Retail;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import timber.log.Timber;


public class AllRetailsWithCategoryFragment extends Fragment {

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

    private String category;

    public AllRetailsWithCategoryFragment() {

    }

    public static AllRetailsWithCategoryFragment newInstance(String category) {
        AllRetailsWithCategoryFragment fragment = new AllRetailsWithCategoryFragment();
        Bundle args = new Bundle();
        args.putString("category",category);
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        category = getArguments().getString("category");
        if(category == null ||(
                !category.equals(BackendService.Category.OTHER)&&
                !category.equals(BackendService.Category.FASHION)&&
                !category.equals(BackendService.Category.FOOD)&&
                !category.equals(BackendService.Category.LIFESTYLE))){
            throw new IllegalArgumentException("Category of AllRetailsWithCategoryFragment must be provided," +
                    "and it must be one of OTHER, FASION, FOOD, or LIFESTYLE\n" +
                    "Category: "+category);
        }


    }

    @Override
    public void onResume() {
        super.onResume();
        mSwipeRefreshLayout.setRefreshing(true);
        initializeData();
    }

    private void initializeData() {
        ((PromoApplication)(getActivity().getApplication())).getService().listRetails(category, new Callback<List<Retail>>() {
            @Override
            public void success(List<Retail> retails, Response response) {
                Timber.d("Size: " + retails.size());
                mProgress.setVisibility(View.GONE);
                mSwipeRefreshLayout.setRefreshing(false);
                mGridAdapter.setPromotionList(retails);
                mGridAdapter.notifyDataSetChanged();
                if (retails.size() > 0) {
                    placeholder.setVisibility(View.GONE);
                } else {
                    placeholder.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Timber.e(error.getMessage());
                mGridAdapter.setPromotionList(null);
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
