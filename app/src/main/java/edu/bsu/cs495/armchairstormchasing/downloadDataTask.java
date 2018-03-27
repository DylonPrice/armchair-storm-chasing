package edu.bsu.cs495.armchairstormchasing;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import android.os.AsyncTask;

/**
 * Created by DylonPrice on 3/27/18.
 */

class downloadDataTask extends AsyncTask<URL, String, String> {
    @Override
    protected String doInBackground(URL... urls) {
        URL url = urls[0];
        BufferedReader reader = null;
        StringBuilder builder = new StringBuilder();
        try{
            reader = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
            for (String line; (line = reader.readLine()) != null;){
                builder.append(line.trim());
            }
        }

        catch (IOException e) {

        }

        finally {
            if (reader != null){
                try {
                    reader.close();
                } catch (IOException logOrIgnore){

                }
            }
        }
        return builder.toString();
    }

    @Override
    protected void onProgressUpdate(String... progress){

    }

    @Override
    protected void onPostExecute(String result){

    }
}
