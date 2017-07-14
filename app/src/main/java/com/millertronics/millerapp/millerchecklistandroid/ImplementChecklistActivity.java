package com.millertronics.millerapp.millerchecklistandroid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.millertronics.millerapp.millerchecklistandroid.models.ChecklistItem;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Text;

public class ImplementChecklistActivity extends AppCompatActivity {

    public static final String CID_KEY = "checklist_id_key";
    public static final String IVTYPE_KEY = "item_value_type_key";
    public static final String ITEXT_KEY = "item_text_key";
    public static final String IID_KEY = "item_id_key";
    public static final String CDESC_KEY = "checklist_description_key";
    public static final String CNAME_KEY = "checklist_name_key";

    private LinearLayout binaryChecklistForm;
    private LinearLayout metricChecklistForm;
    private Button submitButton;
    private TextView checklistNameTextView;
    private TextView checklistDescriptionTextView;
    private TextView binaryChecklistTextView;
    private TextView metricChecklistTextView;
    private CheckBox binaryChecklistCheckBox;
    private EditText metricChecklistInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_implement_checklist);

        checklistNameTextView = (TextView) findViewById(R.id.checklist_name_text);
        checklistNameTextView.setText(getIntent().getStringExtra(CNAME_KEY));
        checklistDescriptionTextView = (TextView) findViewById(R.id.checklist_description_text);
        checklistDescriptionTextView.setText(getIntent().getStringExtra(CDESC_KEY));

        binaryChecklistForm = (LinearLayout) findViewById(R.id.binary_checklist_form);
        binaryChecklistCheckBox = (CheckBox) findViewById(R.id.binary_checklist_item_checkbox);
        binaryChecklistTextView = (TextView) findViewById(R.id.binary_checklist_item_text);

        metricChecklistForm = (LinearLayout) findViewById(R.id.metric_checklist_form);
        metricChecklistInput = (EditText) findViewById(R.id.metric_checklist_input);
        metricChecklistTextView = (TextView) findViewById(R.id.metric_checklist_item_text);

        String valueType = getIntent().getStringExtra(IVTYPE_KEY);
        if (StringUtils.isNotBlank(valueType)){
            switch (valueType){
                case ChecklistItem.VALUE_TYPE_METRIC:
                    changeFormVisility(metricChecklistForm, View.VISIBLE);
                    metricChecklistTextView.setText(getIntent().getStringExtra(ITEXT_KEY));
                    break;
                case ChecklistItem.VALUE_TYPE_BINARY:
                    binaryChecklistTextView.setText(getIntent().getStringExtra(ITEXT_KEY));
                    changeFormVisility(binaryChecklistForm, View.VISIBLE);
                    break;
                default:
                    break;
            }
        }
    }

    private void changeFormVisility(LinearLayout form, int visibility){
        form.setVisibility(visibility);
        for (int i=0; i<form.getChildCount(); i++){
            form.getChildAt(i).setVisibility(visibility);
        }
    }
}
