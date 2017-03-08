package com.nimrag.kevin.aweweico.ui;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Created by kevin on 2017/3/2.
 */

public abstract class EndlessScrollListener extends RecyclerView.OnScrollListener {

    private boolean isLoading = false;
    private int passedVisibleItemCount;
    private int currentVisibleItemCount;
    private int totalItemCount;
    private int prevItemTotalCount;

    public abstract void onLoadMoreData();

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        //只处理向下滚动
        if (dy > 0) {
            LinearLayoutManager layoutManager = null;
            if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
                layoutManager = (LinearLayoutManager)recyclerView.getLayoutManager();
            }
            if (layoutManager != null) {
                passedVisibleItemCount = layoutManager.findFirstCompletelyVisibleItemPosition();
                currentVisibleItemCount = layoutManager.getChildCount();
                totalItemCount = layoutManager.getItemCount();
                if (isLoading && totalItemCount > prevItemTotalCount) {
                    isLoading = false;
                    prevItemTotalCount = totalItemCount;
                }
                if (!isLoading && (currentVisibleItemCount + passedVisibleItemCount >= totalItemCount)) {
                    isLoading = true;
                    onLoadMoreData();
                }
            }
        }
    }
}
