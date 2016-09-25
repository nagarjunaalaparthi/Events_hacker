package com.events;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.events.database.Constants;
import com.events.database.DatabaseHelper;

public class BaseActivity extends AppCompatActivity {
    private Dialog mProgressDialog;
    protected DatabaseHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        initProgressDialog();
        initDatabase();
    }

    protected void initDatabase() {
        if (helper == null) {
            helper = new DatabaseHelper(this, Constants.DATABASE_NAME, null, 1);
        }
    }

    private void initProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new Dialog(this, R.style.CustomDialogAnimTheme);
        }
        mProgressDialog.setContentView(R.layout.progress_bar);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setCanceledOnTouchOutside(false);
    }

    public void showProgressDialog() {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (mProgressDialog == null)
                        initProgressDialog();
                    if (!mProgressDialog.isShowing())
                        mProgressDialog.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    public void hideProgressDialog() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (mProgressDialog != null && mProgressDialog.isShowing())
                        mProgressDialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    mProgressDialog = null;
                }
            }
        });
    }
}
