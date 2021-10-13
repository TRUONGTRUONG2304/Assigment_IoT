package bku.solution.iot.vn.androidmobile;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.w3c.dom.Text;

public class DeviceActivity extends AppCompatActivity {
    MQTTHelper mqttHelperDevice;
    SeekBar seekBarTemp;
    Button gobackDevice, btnHomeDevice, btnLogOutDevice ,btnSetTemp, btnMode1, btnMode2, btnMode3;
    TextView txtAirCondition, txtStateHumi;
    ImageView imgDoor, imgLight;
    SwitchCompat swDoor, swLight;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_device);
        Init();
        startMQTT();
        imgDoor.setImageDrawable(getDrawable(R.drawable.ic_closedoor));
        imgLight.setImageDrawable(getDrawable(R.drawable.light_off));
        seekBarTemp.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser){
                    txtAirCondition.setText(progress + "°C");
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        gobackDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DeviceActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });
        btnHomeDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DeviceActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });
        btnLogOutDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DeviceActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        btnSetTemp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mqttHelperDevice.publishToTopic("BBC_TEMP", Integer.toString(seekBarTemp.getProgress()));
            }
        });
        btnMode1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mqttHelperDevice.publishToTopic("BBC_LED", "0"));
                txtStateHumi.setText("OFF");
            }
        });
        btnMode2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mqttHelperDevice.publishToTopic("BBC_LED", "1");
                txtStateHumi.setText("SLOW SPEED");
            }
        });
        btnMode3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mqttHelperDevice.publishToTopic("BBC_LED", "2");
                txtStateHumi.setText("HIGH SPEED");
            }
        });
        swDoor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
//                    mqttHelperDevice.publishToTopic("BBC_DOOR", "1");
                    imgDoor.setImageDrawable(getDrawable(R.drawable.ic_opendoor));
                }
                else {
//                    mqttHelperDevice.publishToTopic("BBC_DOOR", "0");
                    imgDoor.setImageDrawable(getDrawable(R.drawable.ic_closedoor));
                }
            }
        });
        swLight.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
//                    mqttHelperDevice.publishToTopic("BBC_LIGHT", "1");
                    imgLight.setImageDrawable(getDrawable(R.drawable.light_on));
                }
                else{
//                    mqttHelperDevice.publishToTopic("BBC_LIGHT", "0");
                    imgLight.setImageDrawable(getDrawable(R.drawable.light_off));
                }
            }
        });
    }
    private void startMQTT(){
        mqttHelperDevice = new MQTTHelper(getApplicationContext(), "55555");
        mqttHelperDevice.setCallback(new MqttCallbackExtended() {
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
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });
    }
    void Init(){
        gobackDevice = (Button)findViewById(R.id.btnGobackDevice);
        btnHomeDevice = (Button)findViewById(R.id.btnHomeDevice);
        btnLogOutDevice = (Button)findViewById(R.id.btnlogOutDevice);
        seekBarTemp = (SeekBar)findViewById(R.id.seekBarTemp);
        btnSetTemp = (Button)findViewById(R.id.btnSetTemp);
        txtAirCondition = (TextView)findViewById(R.id.txtAirConditioner);
        txtStateHumi = (TextView)findViewById(R.id.txtStateHumiDevice);
        btnMode1 = (Button)findViewById(R.id.btnmode1);
        btnMode2 = (Button)findViewById(R.id.btnmode2);
        btnMode3 = (Button)findViewById(R.id.btnmode3);
        imgDoor = (ImageView)findViewById(R.id.imgDoor);
        imgLight = (ImageView)findViewById(R.id.imgLight);
        swDoor = (SwitchCompat)findViewById(R.id.swDoor);
        swLight = (SwitchCompat)findViewById(R.id.swLight);
    }
}
