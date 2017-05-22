package com.android.alarmclock;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.MediaStore;

import static android.R.attr.data;

public class AlarmClockClass implements Parcelable {
    private String audioArtist;
    private String audioTitle;
    private String audioPath;
    private Uri audioUri;
    private Integer hourClock=12;
    private Integer minuteClock=0;

    public AlarmClockClass(Activity currActivity, Uri audioUri){
        Cursor cursor = null;

        int audioPathIndex,audioArtistIndex,audioTitleIndex ,audioDisplayNameIndex;
        try {
            this.setAudioUri(audioUri);

            String[] proj = { MediaStore.Audio.Media.DATA, MediaStore.Audio.Media.ARTIST, MediaStore.Audio.Media.TITLE };
            cursor = currActivity.getApplicationContext().getContentResolver().query(audioUri,  proj, null, null, null);

            audioPathIndex = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
            audioArtistIndex = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST);
            audioTitleIndex = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE);

            cursor.moveToFirst();

            this.setAudioPath(cursor.getString(audioPathIndex));
            this.setAudioArtist(cursor.getString(audioArtistIndex));
            this.setAudioTitle(cursor.getString(audioTitleIndex));
        }finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public AlarmClockClass(Parcel in) {
        audioUri = (Uri) in.readValue(Uri.class.getClassLoader());
        audioArtist=in.readString();
        audioPath=in.readString();
        audioTitle=in.readString();
        hourClock = in.readInt();
        minuteClock = in.readInt();
    }

    public String getAudioArtist() {
        return audioArtist;
    }

    public void setAudioArtist(String audioArtist) {
        this.audioArtist = audioArtist;
    }

    public String getAudioTitle() {
        return audioTitle;
    }

    public void setAudioTitle(String audioTitle) {
        this.audioTitle = audioTitle;
    }

    public String getAudioPath() {
        return audioPath;
    }

    public void setAudioPath(String audioPath) {
        this.audioPath = audioPath;
    }

    public Uri getAudioUri() {
        return audioUri;
    }

    public void setAudioUri(Uri audioUri) {
        this.audioUri = audioUri;
    }

    public Integer getHourClock() {
        return hourClock;
    }

    public void setHourClock(int hourClock) {
        this.hourClock = hourClock;
    }

    public Integer getMinuteClock() {
        return minuteClock;
    }

    public void setMinuteClock(int minuteClock) {
        this.minuteClock = minuteClock;
    }

    public String getSongNameFromURI(Activity currActivity) {
        Cursor cursor = null;
        String result = "";
        int audioPathIndex,audioArtistIndex,audioTitleIndex ,audioDisplayNameIndex;
        try {
            String[] proj = { MediaStore.Audio.Media.DATA, MediaStore.Audio.Media.ARTIST, MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.DISPLAY_NAME };
            cursor = currActivity.getApplicationContext().getContentResolver().query(audioUri,  proj, null, null, null);

            audioPathIndex = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
            audioArtistIndex = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST);
            audioTitleIndex = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE);
            audioDisplayNameIndex = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME);

            cursor.moveToFirst();
            result= cursor.getString(audioArtistIndex)+" - "+ cursor.getString(audioTitleIndex);

            return result;
            //cursor.moveToFirst();
            //return cursor.getString(column_index);
        } catch (Exception e) {
            result = e.toString();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(audioUri);
        dest.writeString(audioArtist);
        dest.writeString(audioPath);
        dest.writeString(audioTitle);
        dest.writeInt(hourClock);
        dest.writeInt(minuteClock);
    }

    public static final Parcelable.Creator<AlarmClockClass> CREATOR = new Parcelable.Creator<AlarmClockClass>() {

        @Override
        public AlarmClockClass createFromParcel(Parcel source) {
            return new AlarmClockClass(source);
        }

        @Override
        public AlarmClockClass[] newArray(int size) {
            return new AlarmClockClass[size];
        }
    };
}
