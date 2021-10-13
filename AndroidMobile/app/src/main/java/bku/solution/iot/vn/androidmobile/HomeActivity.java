package bku.solution.iot.vn.androidmobile;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class HomeActivity extends AppCompatActivity {
    Button logOut, btnData, btnGraph, btnDevice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        logOut = (Button)findViewById(R.id.btnlogOut);
        btnData = (Button)findViewById(R.id.btnSensor);
        btnGraph = (Button)findViewById(R.id.btnGraph);
        btnDevice = (Button)findViewById(R.id.btnDevice);


        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        btnData.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, DataActivity.class);
                startActivity(intent);
            }
        });
        btnGraph.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, GraphActivity.class);
                startActivity(intent);
            }
        });
        btnDevice.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, DeviceActivity.class);
                startActivity(intent);
            }
        });
    }
}
