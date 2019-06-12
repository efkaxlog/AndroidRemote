package com.bajera.xlog.rc;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

public class ServersActivity extends AppCompatActivity implements ServersActivityPresenter.View {

    private ServersActivityPresenter presenter;
    private ServerListAdapter adapter;

    private ListView serversListView;
    private Toolbar toolbar;
    private SwipeRefreshLayout pullRefresh;

    private AdapterView.OnItemClickListener listViewItemClickListener =
            new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            listViewItemClicked(view);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_servers);

        presenter = new ServersActivityPresenter(this);
        serversListView = findViewById(R.id.lv_servers);
        serversListView.setOnItemClickListener(listViewItemClickListener);
        toolbar = findViewById(R.id.toolbar_servers);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false); // adding a TextView instead
        TextView emptyServers = (TextView) findViewById(R.id.tv_empty_servers);
        serversListView.setEmptyView(emptyServers);
        pullRefresh = (SwipeRefreshLayout) findViewById(R.id.pull_refresh);
        pullRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.refresh();
            }
        });

        presenter.onViewCreated();
    }


    private void listViewItemClicked(View item) {
        TextView tvHostname = (TextView) item.findViewById(R.id.tv_server_hostname);
        TextView tvAddress = (TextView) item.findViewById(R.id.tv_server_address);
        presenter.onListViewItemClicked(
                tvHostname.getText().toString(), tvAddress.getText().toString());
    }

    @Override
    public void setupServersListView(ArrayList<Server> items) {
        adapter = new ServerListAdapter(this, items);
        serversListView.setAdapter(adapter);
    }

    @Override
    public void notifyServerListChanged() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void startServerSearch() {

    }

    @Override
    public void stopServerSearch() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                pullRefresh.setRefreshing(false);
            }
        });
    }

    @Override
    public void openControlsActivity() {
        Intent intent = new Intent(this, ControlActivity.class);
        startActivity(intent);
    }
}
