package com.adi.gfood.fragments.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.adi.gfood.R;
import com.adi.gfood.activities.SearchNCategoryShowActivity;
import com.adi.gfood.adapters.BestDealAdapter;
import com.adi.gfood.adapters.CategoryAdapter;
import com.adi.gfood.adapters.OfferViewPagerAdapter;

import me.relex.circleindicator.CircleIndicator3;

public class HomeFragment extends Fragment {
    ViewPager2 offerViewPager;
    CircleIndicator3 offerViewPagerIndicator;

    RecyclerView categoryRecyclerView, bestDealRecyclerView;

    OfferViewPagerAdapter offerViewPagerAdapter;

    LinearLayout loadingView;

    ImageView searchBtn;

    public HomeFragment(){
        super(R.layout.home_fragment_layout);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        searchBtn = view.findViewById(R.id.search_home);
        searchBtn.setOnClickListener(v -> startActivity(new Intent(getContext(), SearchNCategoryShowActivity.class)));

        loadingView = view.findViewById(R.id.loading_ll_home);

        offerViewPager = view.findViewById(R.id.offers_view_pager);
        offerViewPagerIndicator = view.findViewById(R.id.circle_indicator_offer_viewpager);
        offerViewPagerAdapter = new OfferViewPagerAdapter(getContext());
        offerViewPagerIndicator.setViewPager(offerViewPager);
        offerViewPager.setAdapter(offerViewPagerAdapter);
        offerViewPagerAdapter.startListening();

        categoryRecyclerView = view.findViewById(R.id.categories_recycler_view_home);
        categoryRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        CategoryAdapter categoryAdapter = new CategoryAdapter(getContext());
        categoryRecyclerView.setAdapter(categoryAdapter);
        categoryAdapter.startListening();

        bestDealRecyclerView = view.findViewById(R.id.best_deals_recycler_view_home);
        bestDealRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        BestDealAdapter bestDealAdapter = new BestDealAdapter(getContext());
        bestDealAdapter.setLoadListener(() -> {
            loadingView.setVisibility(View.GONE);
        });
        bestDealRecyclerView.setAdapter(bestDealAdapter);
        bestDealAdapter.startListening();

        return view;
    }
}
