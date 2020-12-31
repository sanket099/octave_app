package com.medium.octaveapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MyAdapter.OnNoteList{
    public static final int PERMISSION = 1;
    public static final String PATH = "PATH";
    public static final String TITLE = "TITLE";
    public static final String ARTIST = "ARTIST";
    public static final String THIS = "THIS";
    public static final String ARRAY = "Array";
    public static final String ID = "ID";
    ArrayList<MusicClass> arrayList;
    RecyclerView recyclerView;
    MyAdapter adapter;
    EditText et_search;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler_view);
        et_search = findViewById(R.id.et_search);

        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},PERMISSION);
        }
        else{
            someStuff();
            search();
        }
    }
    public void search() {

        et_search.setVisibility(View.VISIBLE);

        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());

            }
        });


    }
    private void filter(String s){
        ArrayList<MusicClass> filteredlist = new ArrayList<>();
        for(MusicClass countries : arrayList){
            if(countries.getTitle().toLowerCase().contains(s.toLowerCase())){
                filteredlist.add(countries);
            }

        }

        adapter.filteredlist(filteredlist);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void getSomeMusic() {
        ContentResolver contentResolver = getContentResolver();
        Uri song = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {
                MediaStore.Audio.Media._ID,
               // MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST

                /*MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.ALBUM,*/


        };
        Cursor cursor = contentResolver.query(song,null,null,null);
        if(cursor != null && cursor.moveToFirst()){
            int songTitle = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int songArtist = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int songId = cursor.getColumnIndex(MediaStore.Audio.Media._ID);
            int size = cursor.getColumnIndex(MediaStore.Audio.Media.SIZE);

            //int duration = cursor.getColumnIndex(MediaStore.Audio.Media.DURATION);

            // int  data = cursor.getColumnIndex(MediaStore.Audio.Media.DATA);


            do {
                String title = cursor.getString(songTitle);
                String artist = cursor.getString(songArtist);
                //String dura =
                int id = cursor.getInt(songId);
                //String datas = cursor.getString(data);
                System.out.println("id = " + id);
                //array add
                MusicClass musicClass = new MusicClass(title,artist,id,"");
                arrayList.add(musicClass);
            }
            while (cursor.moveToNext());
        }
        else{
            Toast.makeText(this, "Media Not Found", Toast.LENGTH_SHORT).show();
        }
        cursor.close();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case PERMISSION : {
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_GRANTED){
                        someStuff();
                    }
                }
                else{
                    Toast.makeText(this, "Please Grant Permission", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void someStuff() {
        arrayList = new ArrayList<>();

        getSomeMusic();
        adapter = new MyAdapter(MainActivity.this,arrayList,this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));

        //init adapter
    }


    @Override
    public void OnnoteClick(MusicClass userClass, int position) {

        System.out.println("userClass.getId() = " + userClass.getId());

        startActivity(new Intent(MainActivity.this,MusicPlayActivity.class)
                .putParcelableArrayListExtra(ARRAY,arrayList)
                .putExtra(TITLE, userClass.getTitle())
                .putExtra(THIS,position)
                .putExtra(ARTIST,userClass.getArtist())
                //.putExtra(PATH,userClass.getPath())
                .putExtra(ID,userClass.getId()));



              //  .putExtra(PATH,userClass.getPath()));



    }
}