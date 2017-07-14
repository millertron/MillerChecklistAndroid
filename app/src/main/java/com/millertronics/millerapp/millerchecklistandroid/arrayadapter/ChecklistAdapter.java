package com.millertronics.millerapp.millerchecklistandroid.arrayadapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.millertronics.millerapp.millerchecklistandroid.ImplementChecklistActivity;
import com.millertronics.millerapp.millerchecklistandroid.R;
import com.millertronics.millerapp.millerchecklistandroid.models.Checklist;
import com.millertronics.millerapp.millerchecklistandroid.models.ChecklistItem;

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

        final Checklist checklist = data[position];
        textView.setText(checklist.getName());
        if (checklist.isCompleted()){
            textView.setTextColor(ContextCompat.getColor(context,
                    R.color.completeChecklistFont));
            rowView.setBackgroundColor(ContextCompat.getColor(context,
                    R.color.completeChecklistBackground));
        }
        textView.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                if (!checklist.isCompleted()) {
                    ChecklistItem item = checklist.getChecklistItems().get(0);
                    Intent intent = new Intent(context, ImplementChecklistActivity.class);
                    intent.putExtra(ImplementChecklistActivity.CID_KEY, checklist.getId());
                    intent.putExtra(ImplementChecklistActivity.CNAME_KEY, checklist.getName());
                    intent.putExtra(ImplementChecklistActivity.CDESC_KEY,
                            checklist.getDescription());
                    intent.putExtra(ImplementChecklistActivity.IID_KEY, item.getId());
                    intent.putExtra(ImplementChecklistActivity.ITEXT_KEY, item.getText());
                    intent.putExtra(ImplementChecklistActivity.IVTYPE_KEY, item.getValueType());
                    context.startActivity(intent);
                }
            }
        });

        return rowView;
    }
}
