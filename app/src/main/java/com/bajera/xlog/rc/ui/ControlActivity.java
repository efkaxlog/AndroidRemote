package com.bajera.xlog.rc.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bajera.xlog.rc.presenters.ControlActivityPresenter;
import com.bajera.xlog.rc.R;
import com.bajera.xlog.rc.settings.SharedPreferencesManager;


/**
 * Activity where the user is connected to a server and can send various tasks for it to execute.
 */
public class ControlActivity extends AppCompatActivity implements ControlActivityPresenter.View {

    private ControlActivityPresenter presenter;
    private Toolbar toolbar;
    private Switch pingToggle;
    private TextView tvPing;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);

        presenter = new ControlActivityPresenter(
                this, SharedPreferencesManager.getInstance(getApplicationContext()));
        toolbar = (Toolbar) findViewById(R.id.toolbar_control);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        tvPing = findViewById(R.id.tv_ping);
        pingToggle = findViewById(R.id.switch_ping);
        pingToggle.setOnCheckedChangeListener((compoundButton, isChecked) ->
                presenter.onPingToggle(isChecked));

        presenter.onViewCreated();
    }

    public void onTaskClick(View view) {
        presenter.onTaskClick(view.getTag().toString());
    }

    @Override
    public void onResume() {
        presenter.onResume();
        super.onResume();
    }

    @Override
    public void onStop() {
        presenter.onStop();
        super.onStop();
    }

    @Override
    public void setToolbarText(String title, String subtitle) {
        TextView tvTitle = (TextView) toolbar.findViewById(R.id.tv_toolbar_title);
        TextView tvSubtitle = (TextView) toolbar.findViewById(R.id.tv_toolbar_subtitle);
        tvTitle.setText(title);
        tvSubtitle.setText(subtitle);
    }

    @Override
    public void postConnectAttempt(boolean success, String hostname) {
        CharSequence text;
        if (success) {
            text = "Connected to " + hostname;
        } else {
            text = "Could not connect to " + hostname;
        }
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setPingText(String pingText) {
        /*
        Calling runOnUiThread because this can (and most likely will) be called from a background
        thread that is running a loop where it is pinging a server and notifying the ui thread.
        */
        runOnUiThread(() -> {
            tvPing.setText(pingText);
        });
    }
}
