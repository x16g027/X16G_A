package jp.ac.chiba_fjb.x16g027.x16g_a;

import android.content.Intent;
import android.content.res.TypedArray;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Weather  extends AppCompatActivity implements WeatherReader.OnStarListener, View.OnClickListener {

    final Double KELVIN = 273.15;

    int     num;
    int     cnt;

    String  datetext;
    String  year;
    String  month;
    String  day;
    String  hour;

    int     month_int;
    int     day_int;
    int     hour_int;

    Double  temperature;
    Double  h_temperature;
    Double  l_temperature;

    Double  w_speed;

    ImageView weatherImage;
    FrameLayout f_layout;

    TextView dateText;
    TextView temp;
    TextView windSpeed;

    Button day_before;
    Button hour_before;
    Button hour_after;
    Button day_after;
    Button ctiyChange;


    List<Map> weatherlist;

    int ctiycnt;
    int ctiyID;
    TypedArray ctiyList;
    TypedArray ctiynameList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        weatherImage = findViewById(R.id.weatherImage);
        f_layout = findViewById(R.id.framelayout);

        dateText = findViewById(R.id.dateT);
        temp = findViewById(R.id.temperature);
        windSpeed = findViewById(R.id.windspeed);

        day_before = (Button)findViewById(R.id.before_day);
        hour_before = (Button)findViewById(R.id.before_hour);
        hour_after = (Button)findViewById(R.id.after_hour);
        day_after = (Button)findViewById(R.id.after_day);
        ctiyChange = findViewById(R.id.ctiybutton);

        day_before.setOnClickListener(this);
        hour_before.setOnClickListener(this);
        hour_after.setOnClickListener(this);
        day_after.setOnClickListener(this);
        ctiyChange.setOnClickListener(this);


        day_after.setEnabled(true);
        hour_after.setEnabled(true);
        day_before.setEnabled(false);
        hour_before.setEnabled(false);
        DialogFragment newFragment = new CtiyFragment();
        newFragment.show(getSupportFragmentManager(),null);
    }

    @Override
    public void onStar(List<Map> weather) {
        //イベントの設定
        Map map;

        if(weather!=null) {
            weatherlist = new ArrayList<>();
            for (int z = 0; z < weather.size() ; z++){
                map = weather.get(z);
                weatherlist.add(map);
            }

            map = weather.get(cnt);
            num = Integer.parseInt(map.get("symbol_number").toString());

            if (num >= 800) {
                weatherImage.setImageResource(R.drawable.sunny);
//                f_layout.setBackgroundDrawable(getResources().getDrawable(R.drawable.sunny_sky));
            }else if (num >= 600 && num < 800){
                weatherImage.setImageResource(R.drawable.rainny);
//                f_layout.setBackgroundDrawable(getResources().getDrawable(R.drawable.rainny_sky));
            }else if (num >= 400 && num < 600){
                weatherImage.setImageResource(R.drawable.cloudy);
//                f_layout.setBackgroundDrawable(getResources().getDrawable(R.drawable.cloudy_sky));
            }else{
            }

            //日にちの処理
            datetext = map.get("time_from").toString(); //日にち情報を取得

            //datetextに格納されている年・月・日・時間をそれぞれ切り取って格納
            year = datetext.substring(0,4);     //年
            month = datetext.substring(5,7);    //月
            day = datetext.substring(8,10);     //日
            hour = datetext.substring(11,13);   //時間

            //月・日・時間のデータをint型へ変換
            month_int = Integer.parseInt(month);
            day_int = Integer.parseInt(day);
            hour_int = Integer.parseInt(hour);

            //各データを再結合して表示
            dateText.setText(year + "年 " + month_int + "月 " + day_int + "日 " + hour_int + "時");

            //気温の処理
            temperature = (Double.parseDouble(map.get("temperature_value").toString())) - KELVIN;

            BigDecimal bd = new BigDecimal(temperature);
            BigDecimal temp_bd = bd.setScale(0, BigDecimal.ROUND_HALF_UP);

            temp.setText("気温" + String.valueOf(temp_bd) + "℃");

            //風速の処理
            w_speed = Double.parseDouble(map.get("windSpeed_mps").toString());
            windSpeed.setText("風速" + String.valueOf(w_speed) + "m/h");
        }
    }

    @Override
    public void onClick(View view) {
        Map map;
        day_after.setEnabled(true);
        hour_after.setEnabled(true);
        day_before.setEnabled(true);
        hour_before.setEnabled(true);

        if(view.getId() == R.id.before_day){
            cnt = cnt - 8;
        }else if(view.getId() == R.id.before_hour){
            cnt = cnt - 1;
        }else if(view.getId() == R.id.after_hour){
            cnt = cnt + 1;
        }else if(view.getId() == R.id.after_day) {
            cnt = cnt + 8;
        }else if(view.getId() == R.id.ctiybutton){
            DialogFragment newFragment = new CtiyFragment();
            newFragment.show(getSupportFragmentManager(),null);

        }
        if (cnt > weatherlist.size()-2){
            cnt = weatherlist.size()-1;
            day_after.setEnabled(false);
            hour_after.setEnabled(false);
        }else if(cnt < 1){
            cnt=0;
            day_before.setEnabled(false);
            hour_before.setEnabled(false);
        }
        map = weatherlist.get(cnt);
        num = Integer.parseInt(map.get("symbol_number").toString());

        if (num >= 800) {
            weatherImage.setImageResource(R.drawable.sunny);
//                f_layout.setBackgroundDrawable(getResources().getDrawable(R.drawable.sunny_sky));
        }else if (num >= 600 && num < 800){
            weatherImage.setImageResource(R.drawable.rainny);
//                f_layout.setBackgroundDrawable(getResources().getDrawable(R.drawable.rainny_sky));
        }else if (num >= 400 && num < 600){
            weatherImage.setImageResource(R.drawable.cloudy);
//                f_layout.setBackgroundDrawable(getResources().getDrawable(R.drawable.cloudy_sky));
        }else{
        }

        //日にちの処理
        datetext = map.get("time_from").toString(); //日にち情報を取得

        //datetextに格納されている年・月・日・時間をそれぞれ切り取って格納
        year = datetext.substring(0,4);     //年
        month = datetext.substring(5,7);    //月
        day = datetext.substring(8,10);     //日
        hour = datetext.substring(11,13);   //時間

        //月・日・時間のデータをint型へ変換
        month_int = Integer.parseInt(month);
        day_int = Integer.parseInt(day);
        hour_int = Integer.parseInt(hour);

        //各データを再結合して表示
        dateText.setText(year + "年 " + month_int + "月 " + day_int + "日 " + hour_int + "時");

        //気温の処理
        temperature = (Double.parseDouble(map.get("temperature_value").toString())) - KELVIN;

        BigDecimal bd = new BigDecimal(temperature);
        BigDecimal temp_bd = bd.setScale(0, BigDecimal.ROUND_HALF_UP);

        temp.setText("気温" + String.valueOf(temp_bd) + "℃");

        //風速の処理
        w_speed = Double.parseDouble(map.get("windSpeed_mps").toString());
        windSpeed.setText("風速" + String.valueOf(w_speed) + "m/h");
    }

    // メニューをActivity上に設置する
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // 参照するリソースは上でリソースファイルに付けた名前と同じもの
        getMenuInflater().inflate(R.menu.option, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // メニューが選択されたときの処理
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuItem1:
                Intent intent1 = new Intent(Weather.this, Constellation.class);
                startActivity(intent1);
                return true;

            case R.id.menuItem2:
                Intent intent2 = new Intent(Weather.this, Moon.class);
                startActivity(intent2);
                return true;

            case R.id.menuItem3:
                Intent intent3 = new Intent(Weather.this, Weather.class);
                startActivity(intent3);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void APIWeather(int ctiycnt) {
        Button ctiyname = findViewById(R.id.ctiybutton);
        ctiyList = getResources().obtainTypedArray(R.array.default_ctiyList);
        ctiynameList = getResources().obtainTypedArray(R.array.default_ctiyNameList);
        ctiyID = ctiyList.getInteger(ctiycnt, 0);
        String ctiynl = ctiynameList.getString(ctiycnt);
        cnt = 0;
        day_after.setEnabled(true);
        hour_after.setEnabled(true);
        day_before.setEnabled(false);
        hour_before.setEnabled(false);
        ctiyname.setText(ctiynl);
        //URLをもとに天気情報を取得
        WeatherReader.getWeather("http://api.openweathermap.org/data/2.5/forecast?id="+ctiyID+"&APPID=d21a1076e3577e18ffe577b79bef2496&mode=xml",this);
    }
}
