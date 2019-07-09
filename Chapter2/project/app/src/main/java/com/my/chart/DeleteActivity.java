package com.my.chart;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DeleteActivity extends AppCompatActivity {
    private Button btnBack;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete);
        btnBack = findViewById(R.id.BackFromDelete);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText NoEdit = findViewById(R.id.DeleteNoEdit);
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
                Intent intent = new Intent();
                intent.putExtra("No", No);
                setResult(3, intent);
                finish();
            }
        });
    }
}
