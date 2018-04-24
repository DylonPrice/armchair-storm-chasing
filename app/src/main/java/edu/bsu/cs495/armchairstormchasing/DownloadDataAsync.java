package edu.bsu.cs495.armchairstormchasing;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Calendar;
import java.util.Date;

class DownloadDataAsync extends AsyncTask<String, String, String> {

    private Context context;
    public IAsyncResponse delegate = null;

    public DownloadDataAsync(Context context){
        this.context = context;
    }

    @Override
    protected String doInBackground(String... inputUrl) {
        int count;
        String filepath = null;
        // Change to joda time
        Date currentTime = Calendar.getInstance().getTime();

        try{
            File file = new File(context.getFilesDir(), "data_" + currentTime);
            URL url = new URL(inputUrl[0]);
            URLConnection connection = url.openConnection();
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            BufferedInputStream inputStream = new BufferedInputStream(connection.getInputStream());
            BufferedOutputStream outputStream = new BufferedOutputStream(fileOutputStream, 8192);
            byte data[] = new byte[1024];

            while ((count = inputStream.read(data)) != -1){
                outputStream.write(data, 0, count);
            }

            outputStream.flush();
            outputStream.close();
            inputStream.close();

            filepath = file.getAbsolutePath();


        } catch (Exception e){
            Log.e("DownloadDataAsync", "Download data failed " + e.toString());
        }

        return filepath;
    }

    @Override
    protected void onProgressUpdate(String... progress){

    }

    @Override
    protected void onPostExecute(String result){
        delegate.onProcessFinish(result);
    }
}
