package com.example.rexpos;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.rexpos.adapters.PaginationScrollListener;
import com.example.rexpos.adapters.PostRecyclerAdapter;
import com.example.rexpos.api.ApiClient;
import com.example.rexpos.models.KitchenTableTransaction;
import com.example.rexpos.models.response.ResKitchenTableTransaction;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DemoActivity extends BaseActivity  implements SwipeRefreshLayout.OnRefreshListener  {

    private static final String TAG = "DemoActivity";
    RecyclerView mRecyclerView;
    SwipeRefreshLayout swipeRefresh;
    private PostRecyclerAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    public static final int PAGE_START = 1;
    private int currentPage = PAGE_START;
    private boolean isLastPage = false;
    private int totalPage = 10;
    private boolean isLoading = false;
    int itemCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // remove title

        setContentView(R.layout.activity_demo);

        swipeRefresh = findViewById(R.id.swipeRefresh);
        mRecyclerView = findViewById(R.id.mRecyclerView);

        swipeRefresh.setOnRefreshListener(this);


        mRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        mLayoutManager = new GridLayoutManager(this ,2, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);




        mAdapter = new PostRecyclerAdapter(this, new ArrayList<KitchenTableTransaction>());

        mRecyclerView.setAdapter(mAdapter);


        //preparedListItem();
        /**
         * add scroll listener while user reach in bottom load more will call
         */
        mRecyclerView.addOnScrollListener(new PaginationScrollListener(mLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage++;
                preparedListItem();
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });
    }


    private void preparedListItem() {
       getTables(currentPage);
    }


    public void getTables(int pageNum){
        swipeRefresh.setRefreshing(true);
        int userId = mPrefs.getUserID();
        String shop_id = mPrefs.getUserShopId();

        Call<ResKitchenTableTransaction> call = ApiClient.getApiClient(this).getTableAPI(userId, pageNum, "", shop_id);
        call.enqueue(new Callback<ResKitchenTableTransaction>() {
            @Override
            public void onResponse(Call<ResKitchenTableTransaction> call, Response<ResKitchenTableTransaction> response) {
                //hideProgressDialog();
                swipeRefresh.setRefreshing(false);
                ResKitchenTableTransaction result = response.body();

                if (result != null){
                    if (result.errorCode == 0){
                        ResKitchenTableTransaction.Result results = result.results;
                        itemCount += results.tables.size();
                        totalPage = results.totalPage;
                        if (currentPage != PAGE_START) mAdapter.removeLoading();
                        mAdapter.addAll(results.tables);
                        swipeRefresh.setRefreshing(false);
                        if (currentPage < totalPage)
                            mAdapter.addLoading();
                        else
                            isLastPage = true;
                        isLoading = false;
                    }else {
                        Toast.makeText(getBaseContext(), result.errMsg, Toast.LENGTH_SHORT).show();
                    }

                }else {
                    Toast.makeText(getBaseContext(), R.string.server_error, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResKitchenTableTransaction> call, Throwable t) {
                //hideProgressDialog();
                swipeRefresh.setRefreshing(false);
                Toast.makeText(getBaseContext(), R.string.no_internet, Toast.LENGTH_SHORT).show();
            }
        });
    }



    @Override
    protected void onResume() {
        super.onResume();
        itemCount = 0;
        currentPage = PAGE_START;
        isLastPage = false;
        mAdapter.clear();
        preparedListItem();
    }

    @Override
    public void onRefresh() {
        itemCount = 0;
        currentPage = PAGE_START;
        isLastPage = false;
        mAdapter.clear();
        preparedListItem();
    }
}
