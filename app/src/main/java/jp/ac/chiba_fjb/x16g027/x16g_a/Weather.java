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
    String  w_speed_rank;

    ImageView weatherImage;
    FrameLayout f_layout;

    TextView dateText;
    TextView temp;
    TextView windSpeed;

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
        windSpeed = findViewById(R.id.windspeed);

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
        if(weather!=null) {
            for (int z = 0; z < weather.size() ; z++){
                map = weather.get(z);
                weatherlist.add(map);
            }
            //天候情報表示処理
            WeatherInfo();
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
        if (cnt >= weatherlist.size()-1){;
            day_after.setEnabled(false);
            hour_after.setEnabled(false);
            cnt = weatherlist.size() - 1;
        }else if(cnt <= 0){
            day_before.setEnabled(false);
            hour_before.setEnabled(false);
            cnt=0;
        }
        //天候情報表示処理
        WeatherInfo();
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

    //天候情報表示処理
    private void WeatherInfo() {

//        map = weather.get(cnt);
        map = weatherlist.get(cnt);
        num = Integer.parseInt(map.get("symbol_number").toString());

        if (num >= 200 && num < 300) {
            weatherImage.setImageResource(R.drawable.sunny2);
//                f_layout.setBackgroundDrawable(getResources().getDrawable(R.drawable.sunny_sky));
        }else if (num >= 300 && num < 400){
            weatherImage.setImageResource(R.drawable.rainny);
//                f_layout.setBackgroundDrawable(getResources().getDrawable(R.drawable.rainny_sky));
        }else if (num >= 400 && num < 500){
            weatherImage.setImageResource(R.drawable.cloudy);
//                f_layout.setBackgroundDrawable(getResources().getDrawable(R.drawable.cloudy_sky));
        }else if (num >= 500 && num < 600){

        }else if (num >= 600 && num < 700){

        }else if (num >= 700 && num < 800){

        }else if (num >= 800 && num < 900){



            if(num == 800){
                weatherImage.setImageResource(R.drawable.sunny2);
            }

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

        temp.setText("気温　" + String.valueOf(temp_bd) + "℃");

        //風速の処理
        w_speed = Double.parseDouble(map.get("windSpeed_mps").toString());

        //風のレベル設定
        if(w_speed >= 0 && w_speed <= 0.4){
            w_speed_rank = "無風";
        }else if(w_speed > 0.4 && w_speed <= 10){
            w_speed_rank = "弱い";
        }else if(w_speed > 10 && w_speed <= 15){
            w_speed_rank = "やや強い";
        }else if(w_speed > 15 && w_speed <= 20){
            w_speed_rank = "強い";
        }else if(w_speed > 20 && w_speed <= 25){
            w_speed_rank = "非常に強い";
        }else{
            w_speed_rank = "猛烈に強い";
        }

        windSpeed.setText("風の強さ　" + String.valueOf(w_speed_rank));
    }
}


