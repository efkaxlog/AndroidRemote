package com.bajera.xlog.rc.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bajera.xlog.rc.models.Server;
import com.bajera.xlog.rc.presenters.ControlActivityPresenter;
import com.bajera.xlog.rc.R;
import com.bajera.xlog.rc.settings.SharedPreferencesManager;
import com.bajera.xlog.rc.util.Util;


/**
 * Activity where the user is connected to a server and can send various tasks for it to execute.
 */
public class ControlActivity extends AppCompatActivity implements ControlActivityPresenter.View {

    private ControlActivityPresenter presenter;
    private Toolbar toolbar;
    private Switch pingToggle;
    private TextView tvPing;
    private ImageView ivScreen;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);
        context = getApplicationContext();
        Server server = (Server) getIntent().getSerializableExtra("server");

        presenter = new ControlActivityPresenter(
                this, server, SharedPreferencesManager.getInstance(context));
        toolbar = findViewById(R.id.toolbar_control);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        tvPing = findViewById(R.id.tv_ping);
        pingToggle = findViewById(R.id.switch_ping);
        pingToggle.setOnCheckedChangeListener((compoundButton, isChecked) ->
                presenter.onPingToggle(isChecked));

        ivScreen = findViewById(R.id.ivScreen);

        presenter.onViewCreated();
    }

    public void onTaskClick(View view) {
        String task = view.getTag().toString();
        if (task.equals("shutdown")) {
            new AlertDialog.Builder(this)
                    .setTitle("Shutdown")
                    .setMessage("Do you really want to shutdown server?")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(android.R.string.yes, (dialog, whichButton) ->
                            presenter.onTaskClick(view.getTag().toString()))
                    .setNegativeButton(android.R.string.no, (dialogInterface, i) -> {
                        dialogInterface.cancel();
                    }).show();
        } else {
            presenter.onTaskClick(view.getTag().toString());
        }

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
        TextView tvTitle = toolbar.findViewById(R.id.tv_toolbar_title);
        TextView tvSubtitle = toolbar.findViewById(R.id.tv_toolbar_subtitle);
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

    public void setImage(byte[] data, int width, int height) {
        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
        runOnUiThread(() -> {
            ivScreen.setImageBitmap(bitmap);
        });

    }
}
