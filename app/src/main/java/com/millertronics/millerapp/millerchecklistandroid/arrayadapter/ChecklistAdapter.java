package com.millertronics.millerapp.millerchecklistandroid.arrayadapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.millertronics.millerapp.millerchecklistandroid.R;
import com.millertronics.millerapp.millerchecklistandroid.models.Checklist;

/**
 * Created by Koha on 2017/07/01.
 */

public class ChecklistAdapter extends ArrayAdapter<Checklist> {

    private Context context;
    Checklist[] data = null;

    public ChecklistAdapter(Context context, Checklist[] data) {
        super(context, R.layout.checklist_list_view_row, data);
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.checklist_list_view_row, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.checklist_row_text);

        Checklist checklist = data[position];
        textView.setText(checklist.getName());

        textView.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){

            }
        });

        return rowView;
    }
}
