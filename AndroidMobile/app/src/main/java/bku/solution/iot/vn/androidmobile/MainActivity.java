package bku.solution.iot.vn.androidmobile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.percent.PercentRelativeLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity{
    PercentRelativeLayout Login, Home, sensorData, deviceData, graphData, graphDataTemp, graphDataHumi, history;
    EditText user, password, phone;
    Button signIn, signUp;
    SwitchCompat switchCompat;
    TextView textForgot, textPhone;
    ImageView imgPhone;
//    Login
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String USERNAME = "userNameKey";
    public static final String PASS = "passKey";
    public static final String REMEMBER = "remember";
    SharedPreferences sharedpreferences;
    String userLogin = "IOT2021";
    String passLogin = "2021";
    CheckBox cbRemember;
    //    Login
//    Home
    Button logOutHome, btnDataHome, btnGraphHome, btnDeviceHome, btnHistory;
//    Home

//    Data Sensor
    Button gobackHome, btnSensortoHome, btnLogOut;
    TextView txtTemp, txtHumi, txtDoor;
    TextView txtDesTemp, txtDesHumi, txtDesDoor;
    TextView txtTimeTemp, txtTimeHumi, txtTimeDoor;
    MQTTHelper mqttHelper;
    ProgressBar progressBar;
//    Data Sensor
//    Device
    SeekBar seekBarTemp;
    Button gobackDevicetoHome, btnDevicetoHome, btnDevicetoLogin ,btnSetTemp, btnMode1, btnMode2, btnMode3;
    TextView txtAirCondition, txtStateHumi;
    ImageView imgDoor, imgLight;
    SwitchCompat swDoor, swLight;
//    Device
//    Graph
    GraphView graphTemp, graphHumi, graphTempLarge, graphHumiLarge;
    private LineGraphSeries<DataPoint> series, series1;
    private boolean flagGraphTemp = false, flagGraphHumi = false;
    private int x_temp = 0, y_temp = 0;
    private int x_humi = 0, y_humi = 0;
    Button fullTemp, fullHumi, gobackGraphtoHome, btnGraphtoHome, btnGraphtoLogin, btnSmallTemp, btnSmallHumi;
//    Graph
//    History
    private ListView listView;
    Button gobackHistory;
    ArrayList<Items> items = new ArrayList<>();
    ListViewAdapter adapter;
//    History
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        InitMapping();
        Login.setBackgroundResource(R.drawable.background);
        startMQTT();
//Start Login
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Editable username = user.getText();
                Editable pass = password.getText();
                if(username.length() == 0){
                    Toast.makeText(MainActivity.this, "Please enter your Username", Toast.LENGTH_SHORT).show();
                }
                else if(pass.length() == 0){
                    Toast.makeText(MainActivity.this, "Please enter your Password", Toast.LENGTH_SHORT).show();
                }
                else{
                    if(cbRemember.isChecked()){
                        saveData(username.toString(), pass.toString());
                    }
                    else{
                        clearData();
                    }
                    if(username.toString().equals(userLogin) && pass.toString().equals(passLogin)){
                        Home.setVisibility(View.VISIBLE);
                        Login.setVisibility(View.GONE);
                    }
                    else{
                        Toast.makeText(MainActivity.this, "Account is not correct", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Editable username = user.getText();
                Editable pass = password.getText();
                Editable phoneNumber = phone.getText();
                if(username.length() <= 3){
                    Toast.makeText(MainActivity.this, "Please enter your Username above 3 character", Toast.LENGTH_SHORT).show();
                }
                else if(pass.length() <= 3){
                    Toast.makeText(MainActivity.this, "Please enter your Password above 3 character", Toast.LENGTH_SHORT).show();
                }
                else if(phoneNumber.length() != 10){
                    Toast.makeText(MainActivity.this, "Phone is not correct", Toast.LENGTH_SHORT).show();
                }
                else{
                    userLogin = username.toString();
                    passLogin = pass.toString();
                    Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                    startActivity(intent);
                }
            }
        });
        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    signIn.setVisibility(buttonView.INVISIBLE);
                    cbRemember.setVisibility(buttonView.INVISIBLE);
                    textForgot.setVisibility(buttonView.INVISIBLE);
                    imgPhone.setVisibility(buttonView.VISIBLE);
                    textPhone.setVisibility(buttonView.VISIBLE);
                    phone.setVisibility(buttonView.VISIBLE);
                    signUp.setVisibility(buttonView.VISIBLE);
                }
                else{
                    signIn.setVisibility(buttonView.VISIBLE);
                    cbRemember.setVisibility(buttonView.VISIBLE);
                    textForgot.setVisibility(buttonView.VISIBLE);
                    imgPhone.setVisibility(buttonView.INVISIBLE);
                    textPhone.setVisibility(buttonView.INVISIBLE);
                    phone.setVisibility(buttonView.INVISIBLE);
                    signUp.setVisibility(buttonView.INVISIBLE);
                }
            }
        });
        //khởi tạo shared preferences
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        loadData();//lấy dữ liệu đã lưu nếu có
//End Login

//Start Home
        logOutHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Home.setVisibility(View.GONE);
                Login.setVisibility(View.VISIBLE);
            }
        });
        btnDataHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Home.setVisibility(View.GONE);
                sensorData.setVisibility(View.VISIBLE);
            }
        });
        btnDeviceHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Home.setVisibility(View.GONE);
                deviceData.setVisibility(View.VISIBLE);
            }
        });
        btnGraphHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Home.setVisibility(View.GONE);
                graphData.setVisibility(View.VISIBLE);
            }
        });
        btnHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Home.setVisibility(View.GONE);
                history.setVisibility(View.VISIBLE);
            }
        });

//End Home

//Start SensorData
        gobackHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sensorData.setVisibility(View.GONE);
                Home.setVisibility(View.VISIBLE);
            }
        });
        btnSensortoHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sensorData.setVisibility(View.GONE);
                Home.setVisibility(View.VISIBLE);
            }
        });
        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sensorData.setVisibility(View.GONE);
                Login.setVisibility(View.VISIBLE);
            }
        });
//End SensorData

//Start Device
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
        gobackDevicetoHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deviceData.setVisibility(View.GONE);
                Home.setVisibility(View.VISIBLE);
            }
        });
        btnDevicetoHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deviceData.setVisibility(View.GONE);
                Home.setVisibility(View.VISIBLE);
            }
        });
        btnDevicetoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deviceData.setVisibility(View.GONE);
                Login.setVisibility(View.VISIBLE);
            }
        });
        btnSetTemp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mqttHelper.publishToTopic("iot.refresher", Integer.toString(seekBarTemp.getProgress()));
            }
        });
        btnMode1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mqttHelper.publishToTopic("iot.fan", "0");
                txtStateHumi.setText("OFF");
            }
        });
        btnMode2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mqttHelper.publishToTopic("iot.fan", "1");
                txtStateHumi.setText("SLOW SPEED");
            }
        });
        btnMode3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mqttHelper.publishToTopic("iot.fan", "2");
                txtStateHumi.setText("HIGH SPEED");
            }
        });
        swDoor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    mqttHelper.publishToTopic("iot.autoDoor", "1");
                    imgDoor.setImageDrawable(getDrawable(R.drawable.ic_opendoor));
                }
                else {
                    mqttHelper.publishToTopic("iot.autoDoor", "0");
                    imgDoor.setImageDrawable(getDrawable(R.drawable.ic_closedoor));
                }
            }
        });
        swLight.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    mqttHelper.publishToTopic("iot.light", "1");
                    imgLight.setImageDrawable(getDrawable(R.drawable.light_on));
                }
                else{
                    mqttHelper.publishToTopic("iot.light", "0");
                    imgLight.setImageDrawable(getDrawable(R.drawable.light_off));
                }
            }
        });
//End Device
//Start Graph
        //Graph Temp
        series = new LineGraphSeries<DataPoint>();
        series.setTitle("Nhiệt độ");
        series.setThickness(8);
        series.setDrawAsPath(true);
        series.setColor(Color.GREEN);
        series.setDrawDataPoints(true);
        graphTemp.addSeries(series);
        graphTempLarge.addSeries(series);
        // customize a little bit viewport
        Viewport viewport = graphTemp.getViewport();
        viewport.setYAxisBoundsManual(true);
        viewport.setMinY(-5);
        viewport.setMaxY(30);
        viewport.setScrollable(true);
        graphTemp.setTitle("BIỂU ĐỒ NHIỆT ĐỘ");
        graphTemp.setBackgroundColor(Color.WHITE);
        Viewport viewport1 = graphTempLarge.getViewport();
        viewport1.setYAxisBoundsManual(true);
        viewport1.setMinY(-20);
        viewport1.setMaxY(60);
        viewport1.setScrollable(true);
        graphTempLarge.setTitle("BIỂU ĐỒ NHIỆT ĐỘ");
        //Graph Humi
        series1 = new LineGraphSeries<DataPoint>();
        series1.setDrawDataPoints(true);
        series1.setTitle("Nhiệt độ");
        series1.setThickness(8);
        series1.setDrawAsPath(true);
        series1.setColor(Color.RED);
        graphHumi.addSeries(series1);
        graphHumiLarge.addSeries(series1);
        // customize a little bit viewport
        viewport = graphHumi.getViewport();
        viewport.setYAxisBoundsManual(true);
        viewport.setMinY(0);
        viewport.setMaxY(60);
        graphHumi.setTitle("BIỂU ĐỒ ĐỘ ẨM");
        graphHumi.setBackgroundColor(Color.WHITE);
        viewport1 = graphHumiLarge.getViewport();
        viewport1.setYAxisBoundsManual(true);
        viewport1.setMinY(0);
        viewport1.setMaxY(100);
        graphHumiLarge.setTitle("BIỂU ĐỒ ĐỘ ẨM");
        fullTemp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                graphData.setVisibility(View.GONE);
                graphDataTemp.setVisibility(View.VISIBLE);
            }
        });
        fullHumi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                graphData.setVisibility(View.GONE);
                graphDataHumi.setVisibility(View.VISIBLE);
            }
        });
        btnSmallTemp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                graphData.setVisibility(View.VISIBLE);
                graphDataTemp.setVisibility(View.GONE);
            }
        });
        btnSmallHumi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                graphData.setVisibility(View.VISIBLE);
                graphDataHumi.setVisibility(View.GONE);
            }
        });
        gobackGraphtoHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                graphData.setVisibility(View.GONE);
                Home.setVisibility(View.VISIBLE);
            }
        });
        btnGraphtoHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                graphData.setVisibility(View.GONE);
                Home.setVisibility(View.VISIBLE);
            }
        });
        btnGraphtoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                graphData.setVisibility(View.GONE);
                Login.setVisibility(View.VISIBLE);
            }
        });
//End Graph
//Start history
        adapter = new ListViewAdapter(items,MainActivity.this);
        listView.setAdapter(adapter);
        gobackHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                history.setVisibility(View.GONE);
                Home.setVisibility(View.VISIBLE);
            }
        });
//End history
    }
    private void clearData() {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.clear();
        editor.commit();
    }
    private void saveData(String username, String Pass) {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(USERNAME, username);
        editor.putString(PASS, Pass);
        editor.putBoolean(REMEMBER,cbRemember.isChecked());
        editor.commit();
    }
    private void loadData() {
        if(sharedpreferences.getBoolean(REMEMBER,false)) {
            user.setText(sharedpreferences.getString(USERNAME, ""));
            password.setText(sharedpreferences.getString(PASS, ""));
            cbRemember.setChecked(true);
        }
        else
            cbRemember.setChecked(false);

    }

    public class Items{
        private String type;
        private String time;
        private String number;
        public Items(String type, String time, String number){
            this.type = type;
            this.time = time;
            this.number = number;
        }
        public String getType(){
            return type;
        }
        public String getTime(){
            return time;
        }
        public String getNumber(){
            return number;
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(flagGraphTemp) {
                                addEntry(x_temp, y_temp, 0);
                                flagGraphTemp = false;
                            }
                            if(flagGraphHumi) {
                                addEntry(x_humi, y_humi, 1);
                                flagGraphHumi = false;
                            }
                        }
                    });
                }
            }
        }).start();
    }
    private void addEntry(int x, int y, int check) {
        // here, we choose to display max 10 points on the viewport and we scroll to end
        if(check == 0) {
            series.appendData(new DataPoint(x, y), false, 10);
        }
        else if(check == 1){
            series1.appendData(new DataPoint(x, y), false, 10);
        }
    }
    void InitMapping(){
        Login = (PercentRelativeLayout)findViewById(R.id.Login);
        Home = (PercentRelativeLayout)findViewById(R.id.Home);
        sensorData = (PercentRelativeLayout)findViewById(R.id.SensorData);
        deviceData = (PercentRelativeLayout)findViewById(R.id.DeviceData);
        graphData = (PercentRelativeLayout)findViewById(R.id.GraphData);
        graphDataTemp = (PercentRelativeLayout)findViewById(R.id.GraphDataTemp);
        graphDataHumi = (PercentRelativeLayout)findViewById(R.id.GraphDataHumi);
//        Start Login
        user = (EditText)findViewById(R.id.userName);
        password = (EditText)findViewById(R.id.passWord);
        signIn = (Button)findViewById(R.id.btnSubmit);
        switchCompat = (SwitchCompat)findViewById(R.id.btnSign);
        phone = (EditText)findViewById(R.id.phoneNum);
        signUp = (Button)findViewById(R.id.btnSub);
        textForgot = (TextView)findViewById(R.id.textView11);
        textPhone = (TextView)findViewById(R.id.textView12);
        imgPhone = (ImageView)findViewById(R.id.imageView6);
        cbRemember = (CheckBox)findViewById(R.id.cbRemember);
//        End Login
//        Start Home
        logOutHome = (Button)findViewById(R.id.btnHometologOut);
        btnDataHome = (Button)findViewById(R.id.btnHometoSensor);
        btnGraphHome = (Button)findViewById(R.id.btnHometoGraph);
        btnDeviceHome = (Button)findViewById(R.id.btnHometoDevice);
        btnHistory = (Button)findViewById(R.id.btnHometoHistory);
//        End Home
//        Start Sensor Data
        gobackHome = (Button)findViewById(R.id.btnGoback);
        btnSensortoHome= (Button)findViewById(R.id.btnSensortoHome);
        btnLogOut = (Button)findViewById(R.id.btnSensortologIn);
        txtTemp = (TextView)findViewById(R.id.txtSensorTemp);
        txtHumi = (TextView)findViewById(R.id.txtSensorHumi);
        txtDoor = (TextView)findViewById(R.id.txtSensorDoor);
        txtDesTemp = (TextView)findViewById(R.id.txtSensorDesTemp);
        txtDesHumi = (TextView)findViewById(R.id.txtSensorDesHumi);
        txtDesDoor = (TextView)findViewById(R.id.txtSensorDesDoor);
        txtTimeTemp = (TextView)findViewById(R.id.txtSensorTimeTemp);
        txtTimeHumi = (TextView)findViewById(R.id.txtSensorTimeHumi);
        txtTimeDoor = (TextView)findViewById(R.id.txtSensorTimeDoor);
        progressBar = (ProgressBar)findViewById(R.id.progressSensorBarHumi);
//        End Sensor Data
//        Start Device
        gobackDevicetoHome = (Button)findViewById(R.id.btnGobackDevicetoHome);
        btnDevicetoHome = (Button)findViewById(R.id.btnDevicetoHome);
        btnDevicetoLogin = (Button)findViewById(R.id.btnDevicetoLogIn);
        seekBarTemp = (SeekBar)findViewById(R.id.seekBarTempDevice);
        btnSetTemp = (Button)findViewById(R.id.btnSetTempDevice);
        txtAirCondition = (TextView)findViewById(R.id.txtAirConditionerDevice);
        txtStateHumi = (TextView)findViewById(R.id.txtStateHumiDevice);
        btnMode1 = (Button)findViewById(R.id.btnMode1Device);
        btnMode2 = (Button)findViewById(R.id.btnMode2Device);
        btnMode3 = (Button)findViewById(R.id.btnMode3Device);
        imgDoor = (ImageView)findViewById(R.id.imgDoorDevice);
        imgLight = (ImageView)findViewById(R.id.imgLightDevice);
        swDoor = (SwitchCompat)findViewById(R.id.swDoorDevice);
        swLight = (SwitchCompat)findViewById(R.id.swLightDevice);
//        End Device
//        Start Graph
        graphTemp = (GraphView) findViewById(R.id.graphTEMP);
        graphHumi = (GraphView) findViewById(R.id.graphHUMI);
        graphTempLarge = (GraphView) findViewById(R.id.graphLargeTemp);
        graphHumiLarge = (GraphView) findViewById(R.id.graphLargeHumi);
        fullTemp = (Button) findViewById(R.id.btnFullGraphTemp);
        fullHumi = (Button) findViewById(R.id.btnFullGraphHumi);
        gobackGraphtoHome = (Button)findViewById(R.id.btnGraphGoback);
        btnGraphtoHome = (Button)findViewById(R.id.btnGraphtoHome);
        btnGraphtoLogin = (Button)findViewById(R.id.btnGraphtologIn);
        btnSmallTemp = (Button)findViewById(R.id.btnSmallTemp);
        btnSmallHumi = (Button)findViewById(R.id.btnSmallHumi);
//        End Graph
//        History
        history = (PercentRelativeLayout)findViewById(R.id.History);
        listView = (ListView)findViewById(R.id.listView);
        gobackHistory = (Button)findViewById(R.id.btnHistoryGoback);
//        History
    }
    private void startMQTT(){
        mqttHelper = new MQTTHelper(getApplicationContext(), "55556");
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
                if(topic.contains("iot.temp")){
                    txtTemp.setText(message.toString() + "°C");
                    String timeNow = getTime();
                    txtTimeTemp.setText(timeNow);
                    int temp = Integer.parseInt(message.toString());
                    if(temp >= 25 || temp <= 18){
                        txtDesTemp.setText("Không thích hợp");
                        txtDesTemp.setTextColor(Color.RED);
                    }
                    else{
                        txtDesTemp.setText("Thích hợp");
                        txtDesTemp.setTextColor(Color.GREEN);
                    }
                    x_temp++;
                    y_temp = temp;
                    flagGraphTemp = true;
                    items.add(new Items("TEMP", timeNow, message.toString()));
                    adapter.notifyDataSetChanged();

                }
                if(topic.contains("iot.door")){
                    if(message.toString().equals("1")) {
                        txtDoor.setText("ON");
                        txtDesHumi.setText("Đóng cửa ngay");
                        txtDesHumi.setTextColor(Color.RED);
                    }
                    else{
                        txtDoor.setText("OFF");
                        txtDesHumi.setText("OK");
                        txtDesHumi.setTextColor(Color.GREEN);
                    }
                    String timeNow = getTime();
                    txtTimeDoor.setText(timeNow);
                }
                if(topic.contains("iot.humidity")){
                    txtHumi.setText(message.toString() + "%");
                    String timeNow = getTime();
                    txtTimeHumi.setText(timeNow);
                    progressBar.setProgress(Integer.parseInt(message.toString()));
                    int humi = Integer.parseInt(message.toString());
                    if(humi >= 45 || humi <= 35){
                        txtDesHumi.setText("Không thích hợp");
                        txtDesHumi.setTextColor(Color.RED);
                    }
                    else{
                        txtDesHumi.setText("Thích hợp");
                        txtDesHumi.setTextColor(Color.GREEN);
                    }
                    x_humi++;
                    y_humi = humi;
                    flagGraphHumi = true;
                    items.add(new Items("HUMI", timeNow, message.toString()));
                    adapter.notifyDataSetChanged();
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
