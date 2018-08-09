package com.will.picviewer;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.github.nukc.LoadMoreWrapper.LoadMoreAdapter;
import com.github.nukc.LoadMoreWrapper.LoadMoreWrapper;
import com.will.picviewer.base.BaseActivity;
import com.will.picviewer.decoder.HtmlDecoder;
import com.will.picviewer.decoder.bean.ArticleObject;
import com.will.picviewer.main.TitleAdapter;
import com.will.picviewer.network.NetworkHelper;
import com.will.picviewer.network.NetworkServer;
import com.will.picviewer.sp.SPHelper;

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
        setSupportActionBar((Toolbar)findViewById(R.id.activity_title_list_toolbar));
        mRecyclerView = findViewById(R.id.activity_title_list_recycler_view);
        mAdapter = new TitleAdapter();
        mAdapter.setOnItemClickCallback(new TitleAdapter.TitleItemClickCallback() {
            @Override
            public void onClick(ArticleObject object) {
                Intent intent = new Intent(MainActivity.this,ListPicActivity.class);
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
        NetworkHelper.getInstance().getHtml(NetworkServer.getDaguarreTitleListUrl(this,pageIndex), new NetworkHelper.NetworkHelperHtmlCallback() {
            @Override
            public void onSuccess(String html) {
                List<ArticleObject> items = HtmlDecoder.getInstance().decodeTitleFromHtml(html);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.title_toobarl_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.menu_title_set_server){
            final EditText editText = new EditText(this);
            editText.setHint("current server is "+SPHelper.getInstance(this).getCurrentServer());
            editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    return false;
                }
            });
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setView(editText)
                    .setTitle("Set server")
                    .setPositiveButton("confirm", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SPHelper.getInstance(MainActivity.this).setCurrentServer(editText.getText().toString());
                        }
                    })
                    .create().show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRefresh() {
        pageIndex = 1;
        mAdapter.clearItems();
    }
}
