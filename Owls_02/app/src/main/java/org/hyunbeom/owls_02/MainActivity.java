package org.hyunbeom.owls_02;

import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;

import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends ActionBarActivity {

    private ListView diaryListView;
    private DiaryListAdapter adapter;
    private List<Diary> diaryList;

    private static final String TAG_RESULTS = "result";
    private static final String TAG_CONTENTS = "contents";
    private static final String TAG_WRITER = "writer";
    private static final String TAG_DATE = "date";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        diaryListView = (ListView) findViewById(R.id.diaryListView);
        diaryList = new ArrayList<Diary>();

        adapter = new DiaryListAdapter(getApplicationContext(),diaryList);
        diaryListView.setAdapter(adapter);

        new BackgroundTask().execute();

    }

    class BackgroundTask extends AsyncTask<Void, Void, String> {

        String target;

        @Override
        protected void onPreExecute() {
        target="http://192.168.0.16/list.php";
    }

        @Override
        protected String doInBackground(Void... voids) {

            try{
                URL url = new URL(target);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String temp;
                StringBuilder stringBuilder = new StringBuilder();

                while((temp = bufferedReader.readLine()) != null)
                {
                    stringBuilder.append(temp + "\n");
                }

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return stringBuilder.toString().trim();

            }catch (Exception e){
                e.printStackTrace();
            }

            return null;
        }

        @Override
        public void onProgressUpdate(Void... values){
            super.onProgressUpdate();
        }

        @Override
        public void onPostExecute(String result){

            try{
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("result");
                int count = 0;
                String diaryContent, diaryName, diaryDate;

                while (count < jsonArray.length())
                {
                    JSONObject object = jsonArray.getJSONObject(count);
                    diaryName = object.getString("writer");
                    diaryDate = object.getString("date");
                    diaryContent = object.getString("contents");
                    Diary diary = new Diary(diaryName,diaryDate,diaryContent);
                    diaryList.add(diary);
                    count++;
                }

            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

}
