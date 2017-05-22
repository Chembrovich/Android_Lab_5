package com.android.alarmclock;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.Calendar;
import java.util.HashMap;


import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<AlarmClockClass> alarmList= new ArrayList<AlarmClockClass>();
    private final int BTN_SAVE=1;
    private final int ITEM_CLICK=2;
    ArrayList<HashMap<String, String>> alarmArrList;
    ListView listViewAlarms;
    SimpleAdapter adapter;
    SparseBooleanArray mCheckStates ;
    Calendar calendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listViewAlarms = (ListView) findViewById(R.id.listViewAlarms);
        listViewAlarms.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        alarmArrList = new ArrayList<HashMap<String, String>>();

        adapter = new SimpleAdapter(this, alarmArrList, android.R.layout.simple_list_item_2,
                new String[] {"Time", "Song"},
                new int[] {android.R.id.text1, android.R.id.text2});

        listViewAlarms.setAdapter(adapter);

        listViewAlarms.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               // Log.d("myLog", "itemClick: position = " + position + ", id = " + id);
                Intent intent = new Intent(MainActivity.this, AlarmClockPropertiesActivity.class);
                intent.putExtra("alarmHour", alarmList.get(position).getHourClock());
                intent.putExtra("alarmMinute", alarmList.get(position).getMinuteClock());
                intent.putExtra("alarmAudioName", alarmArrList.get(position).get("Song"));
                intent.putExtra("alarmID", position);

                intent.putExtra("alarmObject", alarmList.get(position));
               // intent.putExtra("alarmHour", alarmList.get(position).getHourClock());
                startActivityForResult(intent,ITEM_CLICK);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if((requestCode == BTN_SAVE || requestCode == ITEM_CLICK  ) && resultCode == Activity.RESULT_OK) {
            try {
                AlarmClockClass alarmObject = (AlarmClockClass) data.getParcelableExtra("alarmObject");


                HashMap<String, String> map = new HashMap<String, String>();
                String delimeter=new String();
                if (alarmObject.getMinuteClock()<10){
                    delimeter=":0";
                } else {
                    delimeter = ":";
                }
                map.put("Time", alarmObject.getHourClock().toString() + delimeter + alarmObject.getMinuteClock().toString());
                map.put("Song", alarmObject.getAudioArtist() + " - " + alarmObject.getAudioTitle());

                if (data.hasExtra("alarmID")) {
                    int alarmID = data.getIntExtra("alarmID", 0);
                    alarmList.set(alarmID, alarmObject);
                    alarmArrList.set(alarmID, map);
                }else{
                    alarmArrList.add(map);
                    alarmList.add(alarmObject);
                }

                calendar.set(Calendar.HOUR_OF_DAY, alarmObject.getHourClock());
                calendar.set(Calendar.MINUTE, alarmObject.getMinuteClock());
                calendar.set(Calendar.SECOND, 0);

                Intent alarmIntent = new Intent(getApplicationContext(), AlarmClockManager.class);
                alarmIntent.putExtra("alarmObject", alarmObject);

                PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

                adapter.notifyDataSetChanged();
            }catch (Exception e){
                e.toString();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void onBtnAddAlarmClockClick(View view){
        Intent intent = new Intent(this, AlarmClockPropertiesActivity.class);
        startActivityForResult(intent, BTN_SAVE);
    }

    public void onBtnDeleteAlarmClick(View v)
    {
        SparseBooleanArray checkedItemPositions = listViewAlarms.getCheckedItemPositions();
        int itemCount = listViewAlarms.getCount();

        for(int i=itemCount-1; i >= 0; i--){
            if(checkedItemPositions.get(i)){
                alarmArrList.remove(i);
                alarmList.remove(i);
            }
        }
        checkedItemPositions.clear();
        adapter.notifyDataSetChanged();
    }
}
