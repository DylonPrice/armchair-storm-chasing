package edu.bsu.cs495.armchairstormchasing;

import android.os.AsyncTask;

import java.net.URL;
import java.net.URLConnection;
import java.io.*;
import java.util.*;
import android.util.Log;
import android.content.Context;

/**
 * Created by DylonPrice on 4/3/18.
 */

class DownloadDataAsync extends AsyncTask<String, String, String> {

    private Context context;
    public IAsyncResponse delegate = null;

    public DownloadDataAsync(Context context){
        this.context = context;
    }

    @Override
    protected String doInBackground(String... inputUrl) {
        int count;
        String filepath;
        String result = "";
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

            result = getStringFromFile(filepath);

        } catch (Exception e){
            Log.e("DownloadDataAsync", "Download data failed " + e.toString());
        }

        return result;
    }

    public static String convertStreamToString(InputStream inputStream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder builder = new StringBuilder();
        String line = null;
        Boolean firstLine = true;
        while ((line = reader.readLine()) != null){
            if (firstLine){
                builder.append(line);
                firstLine = false;
            } else {
                builder.append("\n").append(line);
            }
        }

        reader.close();
        return builder.toString();
    }

    public static String getStringFromFile(String filepath) throws IOException {
        File file = new File(filepath);
        FileInputStream fileInputStream = new FileInputStream(file);
        String result = convertStreamToString(fileInputStream);
        fileInputStream.close();
        return result;
    }


    @Override
    protected void onProgressUpdate(String... progress){

    }

    @Override
    protected void onPostExecute(String result){
        delegate.onProcessFinish(result);
    }
}
