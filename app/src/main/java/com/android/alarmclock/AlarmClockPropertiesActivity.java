package com.android.alarmclock;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;

public class AlarmClockPropertiesActivity extends AppCompatActivity {
    private int TONE_PICKER=3;
    Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
    String ringToneTitle = "";
    private Uri audioUri = null;
    private AlarmClockClass currentAlarm;
    private Integer alarmID=null;
    TimePicker timePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_clock_properties);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        timePicker = (TimePicker) findViewById(R.id.timePicker);
        timePicker.setIs24HourView(true);

        audioUri = null;

        Intent intentElem=getIntent();
        if (intentElem.hasExtra("alarmID")){
            ringToneTitle=intentElem.getStringExtra("alarmAudioName");
            TextView textViewSelectRingtone = (TextView) findViewById(R.id.textViewSelectRingtone);
            textViewSelectRingtone.setText("Selected song: " + ringToneTitle);
            alarmID = intentElem.getIntExtra("alarmID", 0);
            currentAlarm = intentElem.getParcelableExtra("alarmObject");
            audioUri=currentAlarm.getAudioUri();
            timePicker.setCurrentHour(intentElem.getIntExtra("alarmHour",0));
            timePicker.setCurrentMinute(intentElem.getIntExtra("alarmMinute",0));
        }
    }

    public void onBtnSelectSongClick(View view) {
        final Uri currentTone = RingtoneManager.getActualDefaultRingtoneUri(AlarmClockPropertiesActivity.this, RingtoneManager.TYPE_ALARM);

        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_ALARM);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Select Tone");
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, currentTone);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT, false);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, true);
        startActivityForResult(intent, TONE_PICKER);
    }

    public void onBtnCancelClick(View view){
        this.finish();
    }

    public void onBtnSaveClick(View view) {
        if (audioUri == null) {
            showAlertDilog("Error","Audio don't selected. \nPlease select audio.");
        }else {
            currentAlarm.setHourClock(timePicker.getCurrentHour());
            currentAlarm.setMinuteClock(timePicker.getCurrentMinute());
            Intent intent = new Intent(this, MainActivity.class);
            if (alarmID != null) {
                intent.putExtra("alarmID", alarmID);
            }
            intent.putExtra("alarmObject",currentAlarm);
            //startActivity(intent);
            setResult(Activity.RESULT_OK, intent);
            finish();
        }
    }

    private void showAlertDilog(String title,String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(AlarmClockPropertiesActivity.this);
        builder.setTitle(title)
                .setMessage(message)
                .setIcon(R.mipmap.ic_warning)
                .setCancelable(true)
                .setNegativeButton("ОК",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            audioUri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
            if (audioUri != null) {
                currentAlarm = new AlarmClockClass(this, audioUri);
                currentAlarm.setAudioUri(audioUri);
                ringToneTitle = currentAlarm.getAudioArtist() + " - " + currentAlarm.getAudioTitle();
            }
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        TextView textViewSelectRingtone = (TextView) findViewById(R.id.textViewSelectRingtone);
        textViewSelectRingtone.setText("Selected song: " + ringToneTitle);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
