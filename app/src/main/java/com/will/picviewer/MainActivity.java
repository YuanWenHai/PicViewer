package com.will.picviewer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.github.nukc.LoadMoreWrapper.LoadMoreAdapter;
import com.github.nukc.LoadMoreWrapper.LoadMoreWrapper;
import com.will.picviewer.base.BaseActivity;
import com.will.picviewer.decoder.HtmlDecoder;
import com.will.picviewer.decoder.bean.TitleObject;
import com.will.picviewer.list.TitleAdapter;
import com.will.picviewer.network.NetworkHelper;
import com.will.picviewer.network.NetworkServer;

import java.util.List;

public class MainActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener{

    private TitleAdapter mAdapter;
    private RecyclerView mRecyclerView;
    SwipeRefreshLayout mRefreshLayout;
    private int pageIndex = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title_list);
        initializeView();
    }

    private void initializeView(){
        mRecyclerView = findViewById(R.id.activity_title_list_recycler_view);
        mAdapter = new TitleAdapter();
        mAdapter.setOnItemClickCallback(new TitleAdapter.TitleItemClickCallback() {
            @Override
            public void onClick(TitleObject object) {
                Intent intent = new Intent(MainActivity.this,ImageListActivity.class);
                intent.putExtra("title",object);
                startActivity(intent);
            }
        });
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, RecyclerView.VERTICAL);
        mRecyclerView.addItemDecoration(dividerItemDecoration);
        LoadMoreWrapper.with(mAdapter)
                .setFooterView(R.layout.item_loading) // view or layout resource
                .setListener(new LoadMoreAdapter.OnLoadMoreListener() {
                    @Override
                    public void onLoadMore(LoadMoreAdapter.Enabled enabled) {
                        loadList();
                        // do something
                        // you can enabled.setLoadMoreEnabled(false) when do not need load more
                        // you can enabled.setLoadFailed(true) when load failed
                    }}).into(mRecyclerView);
        mRefreshLayout = findViewById(R.id.activity_title_list_refresh_layout);
        mRefreshLayout.setOnRefreshListener(this);
    }
    private void loadList(){
        NetworkHelper.getInstance().getHtml(NetworkServer.getDaguarreTitleListUrl(pageIndex), new NetworkHelper.NetworkHelperHtmlCallback() {
            @Override
            public void onSuccess(String html) {
                List<TitleObject> items = HtmlDecoder.getInstance().decodeTitleFromHtml(html);
                mAdapter.addItems(items);
                mAdapter.notifyDataSetChanged();
                pageIndex++;
                mRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure() {
                Log.e("on","failure");
            }
        });
    }

    @Override
    public void onRefresh() {
        pageIndex = 1;
        mAdapter.clearItems();
    }
}
