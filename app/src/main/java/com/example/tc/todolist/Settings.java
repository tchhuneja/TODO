package com.example.tc.todolist;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

public class Settings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        final Switch s= findViewById(R.id.s);

        if(ActivityCompat.checkSelfPermission(Settings.this, Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(Settings.this, Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED )
            s.setChecked(true);

        s.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    if( !(ActivityCompat.checkSelfPermission(Settings.this, Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED
                            && ActivityCompat.checkSelfPermission(Settings.this, Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED) ){

                        String[] permissions = {Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS};
                        ActivityCompat.requestPermissions(Settings.this,permissions,1);
                    }
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Switch s=findViewById(R.id.s);
        if(requestCode == 1){
            int smsRead = grantResults[0];
            int smsReceive = grantResults[1];
            if( smsRead == PackageManager.PERMISSION_GRANTED && smsReceive == PackageManager.PERMISSION_GRANTED){

                Toast.makeText(this, "Permission Granted!", Toast.LENGTH_SHORT).show();
            }else{
                s.setChecked(false);
                Toast.makeText(this, "Grant Permission", Toast.LENGTH_SHORT).show();

            }
        }
    }
}
