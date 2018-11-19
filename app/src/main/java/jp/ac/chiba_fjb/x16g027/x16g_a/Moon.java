package jp.ac.chiba_fjb.x16g027.x16g_a;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class Moon extends AppCompatActivity implements MoonReader.OnMoonListener, View.OnClickListener {

    Calendar calendar; //年月日取得用のカレンダー

    ArrayList<Integer> moonAPI; //取得した月相をint型で格納
    String get; //月相変換用
    Object set; //月相変換用
    int mphase; //月相変換用
    double dphase; //月相変換用

    int ddata; //カレンダー日
    int mdata; //カレンダー月
    int ydata; //カレンダー年
    TextView time; //年月日

    int mselect; //月相ID（仮）

    TypedArray imgArray; //画像用リスト
    ImageView mImg; //画像表示用
    Drawable mcount; //リストにナンバーＩＤを付与

    TypedArray infoArray; //月相の名前リスト
    TextView minfo; //表示用
    String mtext; //String挿入用

    ArrayList<Integer> moonlist; //画像及び状態ID格納用
    ArrayList<Integer> moongetlist; //取得した1週間前後の月相
    int getmoon;


    Button upday;
    Button downday;
    int day ;
    int Moon ;

    public void DayCalendar(int day){
        calendar=Calendar.getInstance(); //内容リセット
        calendar.add(Calendar.DAY_OF_MONTH,day+1); //年月日加算
        mdata=calendar.get(Calendar.MONTH)+1; //月
        ddata=calendar.get(Calendar.DAY_OF_MONTH); //日
        upday.setText(mdata+"/"+ddata); //年月日表示]
        calendar=Calendar.getInstance(); //内容リセット
        calendar.add(Calendar.DAY_OF_MONTH,day-1); //年月日加算
        mdata=calendar.get(Calendar.MONTH)+1; //月
        ddata=calendar.get(Calendar.DAY_OF_MONTH); //日
        downday.setText(mdata+"/"+ddata); //年月日表示
        calendar=Calendar.getInstance(); //内容リセット
    }


    public void Moon(int a, int b){
        calendar.add(Calendar.DAY_OF_MONTH,a); //年月日加算
        ydata=calendar.get(Calendar.YEAR); //年
        mdata=calendar.get(Calendar.MONTH)+1; //月
        ddata=calendar.get(Calendar.DAY_OF_MONTH); //日
        time.setText(ydata+"/"+mdata+"/"+ddata); //年月日表示
        calendar=Calendar.getInstance(); //内容リセット

        mselect=moonlist.get(b);//月相状態ID

        mtext = infoArray.getString(mselect);//IDから対応したTextを取り出す
        minfo.setText(mtext); //minfoに結果を送る

        mcount = imgArray.getDrawable(mselect);//IDから対応したimgを取り出す
        mImg.setImageDrawable(mcount); //mImgに結果を送る
    }
    @Override
    protected  void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moon);

        calendar = Calendar.getInstance(); //生成
        calendar.add(Calendar.DAY_OF_MONTH,-6);
        ydata = calendar.get(Calendar.YEAR); //年
        mdata = calendar.get(Calendar.MONTH) +1 ; //月
        ddata = calendar.get(Calendar.DAY_OF_MONTH); //日
        moonAPI = new ArrayList<Integer>();
        MoonReader.getMoon(this);

        day = 6;
        Moon = 0;

        upday = findViewById(R.id.Plus);
        upday.setOnClickListener(this);
        downday = findViewById(R.id.Minus);
        downday.setOnClickListener(this);
        upday.setEnabled(false);
        downday.setEnabled(false);

    }

    @Override
    public void onMoon(List<Map> Moons) {

        mImg = (ImageView)findViewById(R.id.Moonimg); //Moonimg(月相画像)
        minfo = (TextView)findViewById(R.id.Mooninfo);//Mooninfo(月相状態)
        time = (TextView) findViewById(R.id.MoonTime);//MoonTime(指定時刻)
        imgArray = getResources().obtainTypedArray(R.array.default_moonimg2); //画像アレイリスト準備
        infoArray = getResources().obtainTypedArray(R.array.default_moontext); //状態アレイリスト準備

        calendar = Calendar.getInstance(); //生成
        ydata = calendar.get(Calendar.YEAR); //年
        mdata = calendar.get(Calendar.MONTH) +1 ; //月
        ddata = calendar.get(Calendar.DAY_OF_MONTH); //日
        time.setText(ydata+"/"+mdata+"/"+ddata); //年月日表示

        if(Moons!=null) {
            //13日分の月相情報をmoonAPIに
            for (Map map : Moons) {
                set = map.get("moon_phase");        //月相取得
                get = set.toString();               //String型に変換
                dphase = Double.parseDouble(get);   //Double型に変換
                mphase = (int)dphase;               //Doubleをint型に
                moonAPI.add(mphase);                //moonAPIに格納
            }
        }else{
            minfo.setText("エラー\n");
            return;
        }

//        SeekBar day = (SeekBar)findViewById(R.id.DayChange); //シークバー

        calendar = Calendar.getInstance(); //生成
        ydata = calendar.get(Calendar.YEAR); //年
        mdata = calendar.get(Calendar.MONTH) +1 ; //月
        ddata = calendar.get(Calendar.DAY_OF_MONTH); //日
        time.setText(ydata+"/"+mdata+"/"+ddata); //年月日表示


        moongetlist = new ArrayList(); //月相格納用アレイリスト
        int b =0;
        for(int z =0;z<13; z++ ) {
            b = moonAPI.get(z);
            moongetlist.add(b);
        }

        //月相判定結果格納アレイリスト
        moonlist = new ArrayList();
        //月相判定
        for(int i=0; i<13; i++) {
            getmoon = moongetlist.get(i);
            if (((getmoon >= 0)&&(getmoon <= 4)) || ((getmoon >= 356))) {
                moonlist.add(0);//新月
            } else if ((getmoon >= 84)&&(getmoon <= 96)) {
                moonlist.add(7);//上弦
            } else if ((getmoon >= 174)&&(getmoon <= 186)) {
                moonlist.add(14);//満月
            } else if ((getmoon >= 204)&&(getmoon <= 216)) {
                moonlist.add(21);//下弦
            } else if ((getmoon >= 0) && (getmoon <= 15)) {
                moonlist.add(1);
            } else if ((getmoon >= 15) && (getmoon <= 30)) {
                moonlist.add(2);
            } else if ((getmoon >= 30) && (getmoon <= 45)) {
                moonlist.add(3);
            } else if ((getmoon >= 45) && (getmoon <= 60)) {
                moonlist.add(4);
            } else if ((getmoon >= 60) && (getmoon <= 75)) {
                moonlist.add(5);
            } else if ((getmoon >= 75) && (getmoon <= 90)) {
                moonlist.add(6);
            } else if ((getmoon >= 90) && (getmoon <= 105)) {
                moonlist.add(8);
            } else if ((getmoon >= 105) && (getmoon <= 120)) {
                moonlist.add(9);
            } else if ((getmoon >= 120) && (getmoon <= 135)) {
                moonlist.add(10);
            } else if ((getmoon >= 135) && (getmoon <= 150)) {
                moonlist.add(11);
            } else if ((getmoon >= 150) && (getmoon <= 165)) {
                moonlist.add(12);
            } else if ((getmoon >= 165) && (getmoon <= 180)) {
                moonlist.add(13);
            } else if ((getmoon >= 180) && (getmoon <= 195)) {
                moonlist.add(15);
            } else if ((getmoon >= 195) && (getmoon <= 210)) {
                moonlist.add(16);
            } else if ((getmoon >= 210) && (getmoon <= 225)) {
                moonlist.add(17);
            } else if ((getmoon >= 225) && (getmoon <= 240)) {
                moonlist.add(18);
            } else if ((getmoon >= 240) && (getmoon <= 255)) {
                moonlist.add(19);
            } else if ((getmoon >= 255) && (getmoon <= 270)) {
                moonlist.add(20);
            } else if ((getmoon >= 270) && (getmoon <= 285)) {
                moonlist.add(22);
            } else if ((getmoon >= 285) && (getmoon <= 300)) {
                moonlist.add(23);
            } else if ((getmoon >= 300) && (getmoon <= 315)) {
                moonlist.add(24);
            } else if ((getmoon >= 315) && (getmoon <= 330)) {
                moonlist.add(25);
            } else if ((getmoon >= 330) && (getmoon <= 345)) {
                moonlist.add(26);
            } else if (getmoon >= 345) {
                moonlist.add(27);
            }

        }
        mselect=moonlist.get(6);//月相状態ID

        mtext = infoArray.getString(mselect);//IDから対応したTextを取り出す
        minfo.setText(mtext); //minfoに結果を送る

        mcount = imgArray.getDrawable(mselect);//IDから対応したimgを取り出す
        mImg.setImageDrawable(mcount); //mImgに結果を送る
        DayCalendar(0);
        upday.setEnabled(true);
        downday.setEnabled(true);

/*
        day.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mImg = (ImageView)findViewById(R.id.Moonimg); //Moonimg(月相画像)
                minfo = (TextView)findViewById(R.id.Mooninfo);//Mooninfo(月相状態)
                time = (TextView) findViewById(R.id.MoonTime);//MoonTime(指定時刻)
                imgArray = getResources().obtainTypedArray(R.array.default_moonimg2); //画像アレイリスト準備
                infoArray = getResources().obtainTypedArray(R.array.default_moontext); //状態アレイリスト準備

                //シークバー0～12
                if(progress == 0) {
                    Moon(-6,0);
                }else if(progress == 1) {
                    Moon(-5,1);
                }else if(progress == 2) {
                    Moon(-4,2);
                }else if(progress == 3) {
                    Moon(-3,3);
                }else if(progress == 4) {
                    Moon(-2,4);
                }else if(progress == 5) {
                    Moon(-1,5);
                }else if(progress == 6){
                    Moon(0,6);
                }else if(progress == 7){
                    Moon(1,7);
                }else if(progress == 8){
                    Moon(2,8);
                }else if(progress == 9){
                    Moon(3,9);
                }else if(progress == 10){
                    Moon(4,10);
                }else if(progress == 11){
                    Moon(5,11);
                }else if(progress == 12){
                    Moon(6,12);
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }

        });
*/
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
                Intent intent1 = new Intent(Moon.this, Constellation.class);
                startActivity(intent1);
                return true;

            case R.id.menuItem2:
                Intent intent2 = new Intent(Moon.this, Moon.class);
                startActivity(intent2);
                return true;

            case R.id.menuItem3:
                Intent intent3 = new Intent(Moon.this, Weather.class);
                startActivity(intent3);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.Plus:
                upday.setEnabled(true);
                downday.setEnabled(true);
                day++;
                Moon++;
                if (day==12){
                    Moon(Moon,day);
                    DayCalendar(Moon);
                    upday.setEnabled(false);
                    upday.setText("");
                    return;
                }
                DayCalendar(Moon);
                Moon(Moon,day);
                break;

            case R.id .Minus:
                upday.setEnabled(true);
                downday.setEnabled(true);
                day--;
                Moon--;
               if (day==0){
                    Moon(Moon,day);
                    DayCalendar(Moon);
                    downday.setEnabled(false);
                    downday.setText("");
                    return;
                }
                DayCalendar(Moon);
                Moon(Moon,day);
                break;
        }
    }
}

