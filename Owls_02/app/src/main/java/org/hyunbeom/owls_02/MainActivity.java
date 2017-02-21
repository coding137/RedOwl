package org.hyunbeom.owls_02;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView diaryListView;
    private DiaryListAdapter adapter;
    private List<Diary>diaryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        diaryListView = (ListView) findViewById(R.id.diaryListView);
        diaryList = new ArrayList<Diary>();
        diaryList.add(new Diary("First text","HyunBeom","2016-06-28"));
        diaryList.add(new Diary("First text","HyunBeom","2016-06-28"));
        diaryList.add(new Diary("First text","HyunBeom","2016-06-28"));
        diaryList.add(new Diary("First text","HyunBeom","2016-06-28"));
        diaryList.add(new Diary("First text","HyunBeom","2016-06-28"));
        diaryList.add(new Diary("First text","HyunBeom","2016-06-28"));
        diaryList.add(new Diary("First text","HyunBeom","2016-06-28"));
        diaryList.add(new Diary("First text","HyunBeom","2016-06-28"));
        diaryList.add(new Diary("First text","HyunBeom","2016-06-28"));
        diaryList.add(new Diary("First text","HyunBeom","2016-06-28"));
        diaryList.add(new Diary("First text","HyunBeom","2016-06-28"));

        adapter = new DiaryListAdapter(getApplicationContext(),diaryList);
        diaryListView.setAdapter(adapter);

    }
}
