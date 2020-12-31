package com.medium.octaveapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;


public class MusicPlayActivity extends AppCompatActivity {
    MediaPlayer player;
    TextView tv_name,tv_artist;
    ImageView play_btn, next_btn, prev_btn, shuffle_btn, repeat_btn;
    boolean isPlaying = true;
    SeekBar seekBar;
    ArrayList<MusicClass> musicClasses;
    String name, artist;//, path;
    int id;
    int position;

    boolean repeatOn, shuffleOn;

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

        shuffle_btn = findViewById(R.id.iv_shuffle);
        repeat_btn = findViewById(R.id.iv_repeat);

        show_play(false);

        playMahSong();
        seekBarInit();


        repeat_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(repeatOn){
                    repeatOn = false;
                }
                else{
                    repeatOn = true;

                }
            }
        });

        shuffle_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(shuffleOn){
                    shuffleOn = false;
                }
                else{
                    shuffleOn = true;
                }

            }
        });

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
                      nextSong();      }
        });
        prev_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               prevSong();
            }
        });




    }
    private  void nextSong(){
        if(position<musicClasses.size()) {
            if(shuffleOn){
                position = new Random().nextInt(musicClasses.size() - 1);
            }
            else {
                position++;
            }

            //path = musicClasses.get(position).getPath();
            id = musicClasses.get(position).getId();
            setTextViews(musicClasses.get(position).getTitle(), musicClasses.get(position).getArtist());
            stopPlayer();
            playMahSong();
        }
        else{
            Toast.makeText(MusicPlayActivity.this, "Last song :|", Toast.LENGTH_SHORT).show();
        }
    }
    private void prevSong(){
        if(position>0) {
            position--;

            //path = musicClasses.get(position).getPath();
            id = musicClasses.get(position).getId();
            setTextViews(musicClasses.get(position).getTitle(), musicClasses.get(position).getArtist());
            stopPlayer();
            playMahSong();
        }
        else{
            Toast.makeText(MusicPlayActivity.this, "No song :|", Toast.LENGTH_SHORT).show();
        }

    }

    public Uri getSongUri(int songId) {
        System.out.println("ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, songId).toString() = " + ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, songId).toString());
        return ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, songId);
    }

    public void setUri(Context appContext, int songId) {
        try {
           // player.reset();
            String s = getSongUri(songId).toString();
            player.setDataSource(appContext, Uri.parse(Uri.encode(s)));
            System.out.println("songId = " + songId);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("song" + e.getMessage());
        }
    }

    private void getIntents(){
        if(getIntent().getStringExtra(MainActivity.TITLE) != null) {
            position = getIntent().getIntExtra(MainActivity.THIS, 0);
            musicClasses = getIntent().getParcelableArrayListExtra(MainActivity.ARRAY);

            name = getIntent().getStringExtra(MainActivity.TITLE);
            artist = getIntent().getStringExtra(MainActivity.ARTIST);
           // path = getIntent().getStringExtra(MainActivity.PATH);
            id = getIntent().getIntExtra(MainActivity.ID,0);

            setTextViews(name, artist);

            /*Intent i = new Intent(this,MyJobIntentService.class)
                    .putExtra(MainActivity.ID, );
            MyJobIntentService.enqueueWork(this, i); // start job intent service*/
        }
        else{
            Toast.makeText(this, "Not Found", Toast.LENGTH_SHORT).show();
        }
    }

    private void setTextViews(String name, String artist) {


        tv_name.setText(name);
        if(name.length() >= 20){
            tv_name.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        }
        tv_artist.setText("By : " + artist);
    }

    private void seekBarInit() {

         final Handler mHandler = new Handler();
//Make sure you update Seekbar on UI thread
        MusicPlayActivity.this.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if(player != null){
                    seekBar.setMax(player.getDuration()/1000);
                    int mCurrentPosition = player.getCurrentPosition() / 1000;
                    seekBar.setProgress(mCurrentPosition);
                }
                mHandler.postDelayed(this, 1000);
            }
        });
    }

    private void playMahSong() {

        try {
           /* File videoFile = new File(path);
            final Uri[] uri = {Uri.fromFile(videoFile)};
            MediaScannerConnection.scanFile(this,
                    new String[] { videoFile.getAbsolutePath() }, null,
                    new MediaScannerConnection.OnScanCompletedListener() {
                        public void onScanCompleted(String path, Uri uri2) {
                            uri[0] = uri2;
                        }
                    });*/
            //  Uri uri = Uri.parse("file:///" + path);
          //  System.out.println(uri.toString());

            if (id != -1) {
                if (player == null) {
                   // player = new MediaPlayer();


                   player = MediaPlayer.create(MusicPlayActivity.this, getSongUri(id));
                    //System.out.println("uri[0].toString() = " + uri[0].toString());

                    //  setUri(this,id);
                    player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            if(repeatOn){
                                playThisSong();
                            }
                            else if(shuffleOn){
                                playAnySongShuffle();
                            }
                            else
                            nextSong();
                        }


                    });
                }

                player.start();
                isPlaying = true;
                show_play(false);
            } else {
                Toast.makeText(this, "Not Found", Toast.LENGTH_SHORT).show();
            }
        }
        catch (Exception e){

           // Toast.makeText(this, "Not Found" + path, Toast.LENGTH_LONG).show();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            System.out.println("e.getMessage() = " + e.getMessage());
        }
    }

    private void playAnySongShuffle() {
        Random random = new Random();

        int randomPos = random.nextInt(musicClasses.size()-1);
        position = randomPos;
        id = musicClasses.get(position).getId();
        setTextViews(musicClasses.get(position).getTitle(), musicClasses.get(position).getArtist());
        stopPlayer();
        playMahSong();

    }

    private void playThisSong() {
        id = musicClasses.get(position).getId();
        setTextViews(musicClasses.get(position).getTitle(), musicClasses.get(position).getArtist());
        stopPlayer();
        playMahSong();

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
/*
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        player.start();
    }*/
}