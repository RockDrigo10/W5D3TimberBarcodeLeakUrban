package com.example.admin.apptimbertest;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

/**
 * Created by Admin on 8/31/2017.
 */

public class MyAsyncTask extends AsyncTask<Object, String, String> {
    private Context context;

    @Override
    protected String doInBackground(Object... params) {
        context = (Context)params[0];

        // Invoke the leak!
        SingletonSavesContext.getInstance().setContext(context);

        // Simulate long running task
        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
        }

        return "result";
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Intent newActivity = new Intent(context, SecondActivity.class);
        context.startActivity(newActivity);
    }
}
