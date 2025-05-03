package com.shisui.debloater;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import androidx.appcompat.app.AppCompatActivity;

import java.io.DataOutputStream;
import java.io.IOException;

public class Bacth extends AppCompatActivity {
    private TextInputEditText ed;
    private Integer k,x;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.batch);
        ed=(TextInputEditText)findViewById(R.id.editt);
    }
    public void cc(View v) {
        String currentString = ed.getText().toString();
        String[] separated = currentString.split("\n");
        String[] lines = currentString.split("\n");
        k = lines.length;
        x = 0;
        while (x != k) {
            try{
                Process su = Runtime.getRuntime().exec("su");
                DataOutputStream outputStream = new DataOutputStream(su.getOutputStream());
                outputStream.writeBytes("su -c 'pm uninstall -k --user 0'"+' '+separated[x]+"\n");
                outputStream.flush();

                outputStream.writeBytes("exit\n");
                outputStream.flush();

            }catch(IOException e){}

            x++;
        }Toast.makeText(this,"Operation Complete\nDbloat-x by Shisui", Toast.LENGTH_SHORT).show();
    }}
