package com.bajera.xlog.rc;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class ControlActivity extends AppCompatActivity implements ControlActivityPresenter.View {

    private ControlActivityPresenter presenter;
    private Toolbar toolbar;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);

        presenter = new ControlActivityPresenter(this);
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
