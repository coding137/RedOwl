package org.hyunbeom.owls_02;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import org.json.JSONArray;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import org.json.JSONObject;
import java.util.List;

public class MainActivity extends ActionBarActivity {

    private ListView diaryListView;
    private DiaryListAdapter adapter;
    private List<Diary> diaryList;

    private static final String TAG_RESULTS = "result";
    private static final String TAG_CONTENTS = "contents";
    private static final String TAG_WRITER = "writer";
    private static final String TAG_DATE = "date";

    private SwipeRefreshLayout swipeContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_main);



        diaryListView = (ListView) findViewById(R.id.diaryListView);
        diaryList = new ArrayList<Diary>();

        adapter = new DiaryListAdapter(getApplicationContext(),diaryList);

        new BackgroundTask().execute();
    }


    class BackgroundTask extends AsyncTask<Void, Void, String> {

        String target;
        Dialog loading;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading = ProgressDialog.show(MainActivity.this, "Please wait", "Loading...");

            target=Config.LOGIN_URL+"list.php";
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
        public void onPostExecute(String response){
            super.onPostExecute(response);


            try{
                JSONObject jsonObject = new JSONObject(response);
                JSONArray jsonArray = jsonObject.getJSONArray("response");
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
            loading.dismiss();
            viewlist();
        }
    }

    public void viewlist (){
//        v = new View(new )

        diaryListView.setAdapter(adapter);
        final LinearLayout diary = (LinearLayout) findViewById(R.id.diary);

    }

}
