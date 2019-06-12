package com.bajera.xlog.rc;

import java.io.IOException;

/**
 * Presenter for the ControlActivity class. The reason why some methods create and run a new thread
 * is because they contain network calls and Android doesn't allow network operations on main thread.
 */
public class ControlActivityPresenter {

    private View view;
    private String toolbarTitle = "Controls";

    public ControlActivityPresenter(View view) {
        this.view = view;
    }

    public void onViewCreated() {
        view.setToolbarText(toolbarTitle, Client.getServer().getHostname());
    }

    public void onTaskClick(final String task) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Client.send(Tasks.get(task));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void onResume() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Client.bind();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void onStop() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Client.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public interface View {
        void setToolbarText(String title, String subtitle);
    }
}
