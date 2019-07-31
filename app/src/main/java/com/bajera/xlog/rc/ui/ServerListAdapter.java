package com.bajera.xlog.rc.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.bajera.xlog.rc.R;
import com.bajera.xlog.rc.models.Server;

import java.util.ArrayList;

/**
 * A custom ArrayAdapter used to convert Server objects to Views to be used as rows in a ListView.
 */
public class ServerListAdapter extends ArrayAdapter<Server> {

    public ServerListAdapter(Context context, ArrayList<Server> servers) {
        super(context, 0, servers);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Server server = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.server_list_item, parent, false);
        }
        TextView tvHostname = convertView.findViewById(R.id.tv_server_hostname);
        TextView tvAddress = convertView.findViewById(R.id.tv_server_address);
        tvHostname.setText(server.getHostname());
        tvAddress.setText(server.getAddress());
        return convertView;
    }
}