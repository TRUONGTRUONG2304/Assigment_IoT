package bku.solution.iot.vn.androidmobile;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import java.util.Calendar;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class DataActivity extends AppCompatActivity{
    Button goback, btnHome, btnLogOut;
    TextView txtTemp, txtHumi, txtDoor;
    TextView txtDesTemp, txtDesHumi, txtDesDoor;
    TextView txtTimeTemp, txtTimeHumi, txtTimeDoor;
    MQTTHelper mqttHelper;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        Init();

        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DataActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DataActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });
        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DataActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        startMQTT();
    }
    void Init(){
        goback = (Button)findViewById(R.id.btngoback);
        btnHome = (Button)findViewById(R.id.btnHome);
        btnLogOut = (Button)findViewById(R.id.btnlogOut);
        txtTemp = (TextView)findViewById(R.id.txtTemp);
        txtHumi = (TextView)findViewById(R.id.txtHumi);
        txtDoor = (TextView)findViewById(R.id.txtDoor);
        txtDesTemp = (TextView)findViewById(R.id.txtDesTemp);
        txtDesHumi = (TextView)findViewById(R.id.txtDesHumi);
        txtDesDoor = (TextView)findViewById(R.id.txtDesDoor);
        txtTimeTemp = (TextView)findViewById(R.id.txtTimeTemp);
        txtTimeHumi = (TextView)findViewById(R.id.txtTimeHumi);
        txtTimeDoor = (TextView)findViewById(R.id.txtTimeDoor);
        progressBar = (ProgressBar)findViewById(R.id.progressBarHumi);
    }
    private void startMQTT(){
        mqttHelper = new MQTTHelper(getApplicationContext(), "55555");
        mqttHelper.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean reconnect, String serverURI) {
                Log.d("mqtt", "Kết nối thành công!");
            }
            @Override
            public void connectionLost(Throwable cause) {
                Log.d("mqtt", "Ngắt kết nối thành công!");
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                if(topic.contains("BBC_TEMP")){
                    txtTemp.setText(message.toString() + "°C");
                    txtTimeTemp.setText(getTime());
                    int temp = Integer.parseInt(message.toString());
                    if(temp >= 30 || temp <= 5){
                        txtDesTemp.setText("Không thích hợp");
                        txtDesTemp.setTextColor(Color.RED);
                    }
                    else{
                        txtDesTemp.setText("Thích hợp");
                        txtDesTemp.setTextColor(Color.GREEN);
                    }

                }
                if(topic.contains("BBC_HUMI")){
                    txtHumi.setText(message.toString() + "%");
                    txtTimeHumi.setText(getTime());
                    progressBar.setProgress(Integer.parseInt(message.toString()));
                    int humi = Integer.parseInt(message.toString());
                    if(humi >= 35 || humi <= 5){
                        txtDesHumi.setText("Không thích hợp");
                        txtDesHumi.setTextColor(Color.RED);
                    }
                    else{
                        txtDesHumi.setText("Thích hợp");
                        txtDesHumi.setTextColor(Color.GREEN);
                    }
                }
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });
    }
    private String getTime(){
        String time;
        Calendar c = Calendar.getInstance();
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        int year = c.get(Calendar.YEAR);
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        int second = c.get(Calendar.SECOND);
        int millis = c.get(Calendar.MILLISECOND);
        time = Integer.toString(month+1) + "/" + Integer.toString(day) + "/" + Integer.toString(year) + "  "
                + hour + ":" + minute + ":" + second + ":" + millis;
        return time;
    }
}
