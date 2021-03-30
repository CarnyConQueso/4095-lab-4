package me.pgb.a2021_03_17_radio;

import android.media.AudioAttributes;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import me.pgb.a2021_03_17_radio.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.io.IOException;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private final String TAG = "MAIN__";
    private MediaPlayer mediaPlayer;
    private static final String url = "http://stream.whus.org:8000/whusfm"; //";//http://vprbbc.streamguys.net:80/vprbbc24.mp3";

    private Button Play;
    private Button Stop;
    private boolean radioM;

    private Spinner Spinner;
    private ArrayList<String> radioList = new ArrayList<String>();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        radioList.add("http://broadcast.miami/proxy/unidisco?mp=/stream/;");
        radioList.add("http://fmstudio.fairfield.edu:8000/listen");
        radioList.add("http://live.adsciconsolidated.com/wrtchigh.mp3");
        radioList.add("http://peace.str3am.com:6440/");
        Spinner = findViewById(R.id.SpinSpin);
        Spinner.setOnItemSelectedListener(this);

        ArrayAdapter arrayAdapt = new ArrayAdapter(this, android.R.layout.simple_spinner_item,radioList);
        arrayAdapt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner.setAdapter(arrayAdapt);
        mediaPlayer = new MediaPlayer();
        Play = findViewById(R.id.Play);
        Stop = findViewById(R.id.Stop);


        Play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(radioM == false) {
                    mediaPlayer = new MediaPlayer();
                    radioSetup(mediaPlayer, radioList.get(0));
                    mediaPlayer.prepareAsync();
                    radioM = true;
                }
            }
        });

        Stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.release();
                radioM = false;
            }
        });

    }

    public void radioSetup(MediaPlayer mediaPlayer, String url) {
        mediaPlayer.setAudioAttributes(
                new AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .build()
        );

        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                Log.i(TAG, "onPrepared" );
                mediaPlayer.start();
            }
        });

        mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                Log.i(TAG, "onError: " + String.valueOf(what).toString());
                return false;
            }
        });

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Log.i(TAG, "onCompletion" );
                mediaPlayer.reset();
            }
        });

        try {
            mediaPlayer.setDataSource(url);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
        mediaPlayer = null;
    }





    private void setUpMediaPlayer() {
        Handler handler = null;

        HandlerThread handlerThread = new HandlerThread("media player") {
            @Override
            public void onLooperPrepared() {
                Log.i(TAG, "onLooperPrepared");

            }
        };

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int index, long id) {
        if(parent.getId() == R.id.SpinSpin)
        {
            if (radioM == true) {
                mediaPlayer.release();
                String curr = parent.getItemAtPosition(index).toString();
                mediaPlayer = new MediaPlayer();
                radioSetup(mediaPlayer, curr);
                mediaPlayer.prepareAsync();
            }

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}