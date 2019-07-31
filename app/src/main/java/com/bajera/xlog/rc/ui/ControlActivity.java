package com.bajera.xlog.rc.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.bajera.xlog.rc.presenters.ControlActivityPresenter;
import com.bajera.xlog.rc.R;
import com.bajera.xlog.rc.settings.SharedPreferencesManager;

/**
 * Activity where the user is connected to a server and can send various tasks for it to execute.
 */
public class ControlActivity extends AppCompatActivity implements ControlActivityPresenter.View {

    private ControlActivityPresenter presenter;
    private Toolbar toolbar;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);

        presenter = new ControlActivityPresenter(
                this, SharedPreferencesManager.getInstance(getApplicationContext()));
        toolbar = (Toolbar) findViewById(R.id.toolbar_control);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

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
        TextView tvTitle = (TextView) findViewById(R.id.tv_controls_title);
        TextView tvSubtitle = (TextView) findViewById(R.id.tv_controls_subtitle);
        tvTitle.setText(title);
        tvSubtitle.setText(subtitle);
    }
}
