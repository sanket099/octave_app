package com.medium.octaveapp;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class MusicPlayActivity extends AppCompatActivity {
    MediaPlayer player;
    TextView tv_name,tv_artist;
    ImageView play_btn, next_btn, prev_btn;
    boolean isPlaying = true;
    SeekBar seekBar;
    ArrayList<MusicClass> musicClasses;
    String name, artist, path;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_play);
        tv_name = findViewById(R.id.tv_name);
        tv_artist = findViewById(R.id.tv_singer);
        play_btn = findViewById(R.id.iv_play);
        seekBar = findViewById(R.id.seek_bar);
        next_btn = findViewById(R.id.iv_next);
        prev_btn = findViewById(R.id.iv_prev);

        getIntents();





        show_play(false);

        playMahSong();
        seekBarInit();

        play_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isPlaying){
                    if (player != null) {
                        player.pause();
                        show_play(true);
                        isPlaying = false;
                    }

                }
                else{
                    isPlaying = true;
                    show_play(false);
                    playMahSong();
                }
            }
        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(player != null && fromUser){
                    player.seekTo(progress * 1000);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(position<musicClasses.size()) {
                    position++;

                    path = musicClasses.get(position).getPath();
                    setTextViews(musicClasses.get(position).getTitle(), musicClasses.get(position).getArtist());
                    stopPlayer();
                    playMahSong();
                }
                else{
                    Toast.makeText(MusicPlayActivity.this, "Last song :|", Toast.LENGTH_SHORT).show();
                }

            }
        });
        prev_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(position>0) {
                    position--;

                    path = musicClasses.get(position).getPath();
                    setTextViews(musicClasses.get(position).getTitle(), musicClasses.get(position).getArtist());
                    stopPlayer();
                    playMahSong();
                }
                else{
                    Toast.makeText(MusicPlayActivity.this, "No song :|", Toast.LENGTH_SHORT).show();
                }


            }
        });




    }
    private  void getIntents(){
        position  = getIntent().getIntExtra(MainActivity.THIS,0);
        musicClasses = getIntent().getParcelableArrayListExtra(MainActivity.ARRAY);

        name = getIntent().getStringExtra(MainActivity.TITLE);
        artist = getIntent().getStringExtra(MainActivity.ARTIST);
        path = getIntent().getStringExtra(MainActivity.PATH);

        setTextViews(name,artist);
    }

    private void setTextViews(String name, String artist) {


        tv_name.setText(name);
        tv_artist.setText(artist);
    }

    private void seekBarInit() {
        seekBar.setMax(player.getDuration()/1000);
         final Handler mHandler = new Handler();
//Make sure you update Seekbar on UI thread
        MusicPlayActivity.this.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if(player != null){
                    int mCurrentPosition = player.getCurrentPosition() / 1000;
                    seekBar.setProgress(mCurrentPosition);
                }
                mHandler.postDelayed(this, 1000);
            }
        });
    }

    private void playMahSong() {

        Uri uri= Uri.parse("file:///"+path);
        System.out.println("path = " + path);
        if (player == null) {
            player = MediaPlayer.create(MusicPlayActivity.this, uri);
            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    stopPlayer();
                }
            });
        }
        player.start();
        isPlaying = true;
        show_play(false);
    }

    private void show_play(boolean b){
        if(b)
        play_btn.setImageResource(R.drawable.ic_play);
        else
            play_btn.setImageResource(R.drawable.ic_pause);
    }
   /* public void play(View v) {
        String path = getIntent().getStringExtra(MainActivity.PATH);
        Uri uri= Uri.parse("file:///"+path);
        System.out.println("path = " + path);
        if (player == null) {
            player = MediaPlayer.create(this, uri);
            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    stopPlayer();
                }
            });
        }
        player.start();
    }
    public void pause(View v) {
        if (player != null) {
            player.pause();
        }
    }
    public void stop(View v) {
        stopPlayer();
    }*/
    private void stopPlayer() {
        if (player != null) {
            player.release();
            player = null;
           // Toast.makeText(this, "MediaPlayer released", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopPlayer();
    }

    public void stopPlaying(View view) {
        stopPlayer();
    }
}