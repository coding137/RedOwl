package org.hyunbeom.owls_02;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by DIE on 2017-02-21.
 */

public class DiaryListAdapter extends BaseAdapter{

    private Context context;
    private List<Diary> diaryList;

    public DiaryListAdapter(Context context, List<Diary> diaryList) {
        this.context = context;
        this.diaryList = diaryList;
    }

    @Override
    public int getCount() {
        return diaryList.size();
    }

    @Override
    public Object getItem(int i) {
        return diaryList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = View.inflate(context, R.layout.diary, null);
        TextView diaryText = (TextView) v.findViewById(R.id.diaryText);
        TextView nameText = (TextView) v.findViewById(R.id.nameText);
        TextView dateText = (TextView) v.findViewById(R.id.dateText);

        diaryText.setText(diaryList.get(i).getDiary());
        nameText.setText(diaryList.get(i).getName());
        dateText.setText(diaryList.get(i).getDate());

        v.setTag(diaryList.get(i).getDiary());
        return v;

    }

}
