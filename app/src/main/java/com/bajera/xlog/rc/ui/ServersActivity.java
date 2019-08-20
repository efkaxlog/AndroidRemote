package com.bajera.xlog.rc.ui;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.bajera.xlog.rc.R;
import com.bajera.xlog.rc.models.Server;
import com.bajera.xlog.rc.presenters.ServersActivityPresenter;
import com.bajera.xlog.rc.settings.SharedPreferencesManager;

import java.util.ArrayList;


/**
 * Activity where the user can invoke searching for online servers on the network.
 * If any servers are found they will be presented in a ListView for the user to choose.
 * If auto-connect to last used server is active, this activity upon launching first time will
 * signal the presenter to try to connect to it and if successful, will open ControlActivity.
 */
public class ServersActivity extends AppCompatActivity implements ServersActivityPresenter.View {

    private ServersActivityPresenter presenter;
    private ServerListAdapter adapter;

    private ListView serversListView;
    private Toolbar toolbar;
    private SwipeRefreshLayout pullRefresh;

    private AdapterView.OnItemClickListener listViewItemClickListener =
            (adapterView, view, i, l) -> listViewItemClicked(view);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_servers);

        presenter = new ServersActivityPresenter(
                this, SharedPreferencesManager.getInstance(getApplicationContext()));
        serversListView = findViewById(R.id.lv_servers);
        serversListView.setOnItemClickListener(listViewItemClickListener);
        toolbar = findViewById(R.id.toolbar_servers);
        setToolbarText();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false); // adding a TextView instead
        TextView emptyServers = findViewById(R.id.tv_empty_servers);
        serversListView.setEmptyView(emptyServers);
        pullRefresh = findViewById(R.id.pull_refresh);
        pullRefresh.setOnRefreshListener(() -> {
            presenter.serverSearch();
        });
        presenter.onViewCreated();
    }

    private void listViewItemClicked(View item) {
        TextView tvHostname = item.findViewById(R.id.tv_server_hostname);
        TextView tvAddress = item.findViewById(R.id.tv_server_address);
        presenter.onListViewItemClicked(
                tvHostname.getText().toString(), tvAddress.getText().toString());
    }

    private void setToolbarText() {
        TextView title = findViewById(R.id.tv_toolbar_title);
        TextView subtitle = findViewById(R.id.tv_toolbar_subtitle);
        title.setText(R.string.label_servers);
        subtitle.setText(R.string.label_choose_server);
    }

    @Override
    protected void onResume() {
        presenter.serverSearch();
        super.onResume();
    }

    @Override
    public void setupServersListView(ArrayList<Server> items) {
        adapter = new ServerListAdapter(this, items);
        serversListView.setAdapter(adapter);
    }

    @Override
    public void serverSearchDone() {
        pullRefresh.setRefreshing(false);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void notifyServerListChanged() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void openControlsActivity() {
        Intent intent = new Intent(this, ControlActivity.class);
        startActivity(intent);
    }
}