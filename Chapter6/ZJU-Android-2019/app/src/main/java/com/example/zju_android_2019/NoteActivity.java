package com.example.zju_android_2019;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import java.util.Calendar;

public class NoteActivity extends AppCompatActivity {

    private Button confirmBtn;
    private EditText contentText;
    private RadioGroup radioGroup;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        confirmBtn = findViewById(R.id.confirm_button);
        contentText = findViewById(R.id.content_editor);
        radioGroup = findViewById(R.id.RG);

        RadioButton low_btn = findViewById(R.id.r_btn_low);
        low_btn.setChecked(true);

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String content;
                content = contentText.getText().toString();
                if(content.isEmpty()){
                    contentText.setError("不能为空");
                    return;
                }

                Calendar calendar = Calendar.getInstance();
                String dateString;
                dateString = calendar.get(Calendar.YEAR) + "年";
                dateString += calendar.get(Calendar.MONTH) + "月";
                dateString += calendar.get(Calendar.DAY_OF_MONTH) + "日";

                int priority = getPriority();

                Intent intent = new Intent();
                intent.putExtra("Content", content);
                intent.putExtra("Date", dateString);
                intent.putExtra("Done", false);
                intent.putExtra("Priority", priority);
                setResult(1, intent);
                finish();
            }
        });
    }

    private int getPriority(){
        switch (radioGroup.getCheckedRadioButtonId()) {
            case R.id.r_btn_high:
                return 3;
            case R.id.r_btn_medium:
                return 2;
            default:
                return 1;
        }
    }

}
