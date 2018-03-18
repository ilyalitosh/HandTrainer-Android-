package com.litosh.ilya.handtrainer.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.litosh.ilya.handtrainer.R;
import com.litosh.ilya.handtrainer.User;
import com.litosh.ilya.handtrainer.db.DBService;
import com.litosh.ilya.handtrainer.db.models.Note;

import java.util.List;

/**
 * Created by ilya_ on 17.03.2018.
 */

public class StatsListAdapter extends BaseAdapter {

    private Context context;
    private List<Note> notes;
    private DBService dbService;
    private LayoutInflater layoutInflater;

    public StatsListAdapter(Context context){
        this.context = context;
        dbService = new DBService();
        notes = dbService.getNotesById(User.getUserId());
        layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return notes.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View newView = view;
        if(newView == null){
            newView = layoutInflater.inflate(R.layout.statslist_item, viewGroup, false);
        }
        String[] s = notes.get(i).getTask().split("/");
        if(s.length == 2){
            TextView textView1 = newView.findViewById(R.id.goal_statslist_item);
            textView1.setText(s[1]);
            TextView textView2 = newView.findViewById(R.id.finished_statslist_item);
            textView2.setText(s[0]);
        }else{
            TextView textView1 = newView.findViewById(R.id.goal_statslist_item);
            textView1.setVisibility(View.GONE);
            TextView textView11 = newView.findViewById(R.id.goal_title_statslist_item);
            textView11.setVisibility(View.GONE);
            TextView textView2 = newView.findViewById(R.id.finished_statslist_item);
            textView2.setText(s[0]);
        }
        TextView textView3 = newView.findViewById(R.id.comment_statslist_item);
        textView3.setText(notes.get(i).getComment());
        TextView textView4 = newView.findViewById(R.id.duration_statslist_item);
        textView4.setText(notes.get(i).getDuration() + " сек.");
        TextView textView5 = newView.findViewById(R.id.date_statslist_item);
        textView5.setText(notes.get(i).getDate());
        return newView;
    }

}
