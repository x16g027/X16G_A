package jp.ac.chiba_fjb.x16g027.x16g_a;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

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
    int     hour_int;

    Double  temperature;
    Double  h_temperature;
    Double  l_temperature;

    ImageView weatherImage;
    FrameLayout f_layout;

    TextView dateText;
    TextView temp;
    TextView temp2;

    Button day_before;
    Button hour_before;
    Button hour_after;
    Button day_after;
    Map map;
    List<Map> weatherlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        weatherImage = findViewById(R.id.weatherImage);
        f_layout = findViewById(R.id.framelayout);
        weatherlist = new ArrayList<>();

        dateText = findViewById(R.id.dateT);
        temp = findViewById(R.id.temperature);
        temp2 = findViewById(R.id.temperature2);

        cnt = 0;

        //URLをもとに天気情報を取得
        WeatherReader.getWeather("http://api.openweathermap.org/data/2.5/forecast?id=1850147&APPID=d21a1076e3577e18ffe577b79bef2496&mode=xml",this);

        day_before = (Button)findViewById(R.id.before_day);
        hour_before = (Button)findViewById(R.id.before_hour);
        hour_after = (Button)findViewById(R.id.after_hour);
        day_after = (Button)findViewById(R.id.after_day);

        day_before.setOnClickListener(this);
        hour_before.setOnClickListener(this);
        hour_after.setOnClickListener(this);
        day_after.setOnClickListener(this);

        day_before.setEnabled(false);
        hour_before.setEnabled(false);


    }


    @Override
    public void onStar(List<Map> weather) {
        //イベントの設定

        if(weather!=null) {
            for (int z = 0; z < weather.size() ; z++){
                map = weather.get(z);
                weatherlist.add(map);
            }
            map = weather.get(0);
            num = 400;

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

            datetext = map.get("time_from").toString();
            year = datetext.substring(0,4);
            month = datetext.substring(5,7);
            day = datetext.substring(8,10);
            hour = datetext.substring(11,13);
            hour_int = Integer.parseInt(hour);
            dateText.setText(year + "年" + month + "月" + day + "日 " + hour_int + "時");

            temperature = (Double.parseDouble(map.get("temperature_value").toString())) - KELVIN;
            temp.setText(String.valueOf(temperature));

        }
    }

    @Override
    public void onClick(View view) {
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
        }else{
        }
        if (cnt >= weatherlist.size()){
            cnt = 38;
            day_after.setEnabled(false);
            hour_after.setEnabled(false);
        }else if(cnt < 0){
            day_before.setEnabled(false);
            hour_before.setEnabled(false);
            cnt=0;
        }
        map = weatherlist.get(cnt);
        datetext = map.get("time_from").toString();
        year = datetext.substring(0,4);
        month = datetext.substring(5,7);
        day = datetext.substring(8,10);
        hour = datetext.substring(11,13);
        hour_int = Integer.parseInt(hour);
        dateText.setText(year + "年" + month + "月" + day + "日 " + hour_int + "時");

        temperature = Double.parseDouble(map.get("temperature_value").toString());
        temp.setText(String.valueOf(temperature));
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
}
