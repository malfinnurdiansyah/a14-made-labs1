package com.dicoding.picodiploma.myasynctask;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;


import java.lang.ref.WeakReference;

public class MainActivity extends AppCompatActivity {
    static final String DEMO_ASYNC = "DemoAsync";

    TextView tvStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvStatus = (TextView) findViewById(R.id.tv_status);

        DemoAsync demoAsync = new DemoAsync(tvStatus);

        // Execute asynctask dengan parameter string 'Halo Ini Demo AsyncTask'
        demoAsync.execute("Halo Ini Demo AsyncTask");
    }

    /**
     * 3 parameter generic <String, Void, String>
     * 1. Params, parameter input yang bisa dikirimkan
     * 2. Progress, digunakan untuk publish informasi sudah sampai mana proses background berjalan
     * 3. Result, object yang dikirimkan ke onPostExecute / hasil dari proses doInBackground
     */
    private static class DemoAsync extends AsyncTask<String, Void, String> {

        // Penggunaan weakreference disarankan untuk menghindari memory leaks
        WeakReference<TextView> tvStatus;

        DemoAsync(TextView tvStatus) {
            this.tvStatus = new WeakReference<>(tvStatus);
        }

        /*
        onPreExecute digunakan untuk persiapan asynctask
        berjalan di Main Thread, bisa akses ke view karena masih di dalam Main Thread
         */

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            TextView tvStatus = this.tvStatus.get();
            if (tvStatus != null) {
                tvStatus.setText("status : onPreExecute");
            }
        }

        /*
        doInBackground digunakan untuk menjalankan proses secara async
        berjalan di background thread, tidak bisa akses ke view karena sudah beda thread
         */
        @Override
        protected String doInBackground(String... params) {
            Log.d(DEMO_ASYNC, "status : doInBackground");

            // 5000 miliseconds = 5 detik
            try {
                Thread.sleep(5000);
            } catch (Exception e) {
                Log.d(DEMO_ASYNC, e.getMessage());
            }

            /*
            params[0] adalah 'Halo Ini Demo AsyncTask'
             */
            return params[0];
        }

        /*
        onPostExecute dijalankan ketika proses doInBackground telah selesai
        berjalan di Main Thread
         */
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            TextView tvStatus = this.tvStatus.get();
            if (tvStatus != null) {
                tvStatus.setText("status : onPostExecute : " + s);
            }
        }
    }

}
