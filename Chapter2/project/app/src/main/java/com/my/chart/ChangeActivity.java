package com.my.chart;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChangeActivity extends AppCompatActivity {

    private Button btnBack;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change);
        btnBack = findViewById(R.id.BackFromChange);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText NoEdit = findViewById(R.id.ChangeNoEdit);
                String NoStr = NoEdit.getText().toString();
                if(NoStr.isEmpty()){
                    NoEdit.setError("不能为空");
                    return;
                }
                Pattern p = Pattern.compile("[0-9]*");
                Matcher m = p.matcher(NoStr);
                int No;
                if(m.matches()){
                    No = Integer.valueOf(NoStr);
                }else{
                    NoEdit.setError("只能输入数字");
                    return;
                }
                if(No <= 0 || No > 20){
                    NoEdit.setError("只能输入1-20之间的数字");
                    return;
                }
                EditText hotEdit = findViewById(R.id.ChangeHotEdit);
                String hotStr = hotEdit.getText().toString();
                if(hotStr.isEmpty()){
                    hotEdit.setError("不能为空");
                    return;
                }
                m = p.matcher(hotStr);
                int hot;
                if(m.matches()){
                    hot = Integer.valueOf(hotStr);
                }else{
                    hotEdit.setError("只能输入数字");
                    return;
                }
                if(hot <= 0){
                    hotEdit.setError("只能输入大于0的数字");
                    return;
                }
                EditText titleEdit = findViewById(R.id.ChangeTitleEdit);
                String title = titleEdit.getText().toString();
                if(title.isEmpty()){
                    titleEdit.setError("标题不能为空");
                    return;
                }
                Intent intent = new Intent();
                intent.putExtra("No", No);
                intent.putExtra("hot", hot);
                intent.putExtra("title", title);
                setResult(1, intent);
                finish();
            }
        });
    }
}
