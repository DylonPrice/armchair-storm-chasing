package edu.bsu.cs495.armchairstormchasing;

import android.app.DownloadManager;
import android.os.AsyncTask;

import java.net.URL;
import java.net.URLConnection;
import java.io.*;
import java.util.*;
import android.content.Context;

/**
 * Created by DylonPrice on 4/3/18.
 */

class downloadDataAsync extends AsyncTask<String, String, String> {

    private Context context;
    private BufferedInputStream inputStream;
    private BufferedOutputStream outputStream;
    private FileOutputStream fileOutputStream;
    private File file;
    private URL url;
    private URLConnection connection;
    private Date currentTime;


    public downloadDataAsync(Context context){
        this.context = context;
    }

    @Override
    protected String doInBackground(String... inputUrl) {
        try{
            currentTime = Calendar.getInstance().getTime();
            file = new File(context.getFilesDir(), "data_" + currentTime);
            url = new URL(inputUrl[0]);
            connection = url.openConnection();
            fileOutputStream = new FileOutputStream(file);
            inputStream = new BufferedInputStream(connection.getInputStream());
            // STOPPED HERE
            //outputStream = new BufferedOutputStream(fileOutputStream, DOWNLOAD_BUFFER_SIZE)

        } catch (Exception e){

        }

        return "";

    }

    @Override
    protected void onProgressUpdate(String... progress){

    }

    @Override
    protected void onPostExecute(String result){

    }
}
