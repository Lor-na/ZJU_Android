package com.my.helloworld;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final RadioButton rbtn = findViewById(R.id.rbtn_man);
        rbtn.setChecked(true);
        final CheckBox box_vis = findViewById(R.id.box_vis);
        box_vis.setChecked(true);
        Button btn_commit = findViewById(R.id.btn_commit);
        btn_commit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                EditText edit_name = findViewById(R.id.edit_name);
                TextView text_res = findViewById(R.id.text_res);
                String name = edit_name.getText().toString();
                if(name.isEmpty()){
                    edit_name.setError("Name cannot be empty!");
                    text_res.setText(R.string.res_fail);
                    return;
                }
                EditText edit_pass = findViewById(R.id.edit_pass);
                String pass = edit_pass.getText().toString();
                if(pass.isEmpty()){
                    edit_pass.setError("Password cannot be empty!");
                    text_res.setText(R.string.res_fail);
                    return;
                }
                String gender;
                if(rbtn.isChecked()) gender = "Man";
                else gender = "Woman";
                Boolean visible = box_vis.isChecked();
                Log.d("name", name);
                Log.d("password", pass);
                Log.d("gender", gender);
                if(visible) Log.d("Note", "Information Visible");
                text_res.setText(R.string.res_success);
            }
        });
    }
}
