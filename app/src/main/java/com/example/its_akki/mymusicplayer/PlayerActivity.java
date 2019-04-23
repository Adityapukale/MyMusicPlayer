package com.example.its_akki.mymusicplayer;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class PlayerActivity extends AppCompatActivity {
    Button btn_next,btn_previous,btn_pause;
    TextView songTextLabel;
    SeekBar sb;
    ImageButton imageButton;
    static MediaPlayer myMediaPlayer;
    int pos;
    ArrayList<File>mySongs;
    Thread updateSeekBar;
    String sname;
    int change=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        btn_next=(Button)findViewById(R.id.next);
        btn_pause=(Button)findViewById(R.id.pause);
        btn_previous=(Button)findViewById(R.id.previous);
        imageButton=(ImageButton)findViewById(R.id.imgbtn);
        songTextLabel=(TextView) findViewById(R.id.songLable);
        sb=(SeekBar)findViewById(R.id.seekbar);

        updateSeekBar=new Thread(){
           public void run() {
               int totalDuration=myMediaPlayer.getDuration();
               int currentPosition=0;
               while(currentPosition<totalDuration){
                   try{
                       sleep(500);
                       currentPosition=myMediaPlayer.getCurrentPosition();
                       sb.setProgress(currentPosition);
                   }catch (InterruptedException e1){
                       e1.printStackTrace(); } } }
        };
        if(myMediaPlayer!=null){
            myMediaPlayer.stop();
            myMediaPlayer.release(); }
        Intent i=getIntent();
        Bundle bundle=i.getExtras();
        mySongs=(ArrayList) bundle.getParcelableArrayList("songs");
        sname=mySongs.get(pos).getName().toString();
        String songname=i.getStringExtra("mySongs");
        songTextLabel.setText(songname);
        songTextLabel.setSelected(true);
        pos= bundle.getInt("pos1",0);
        Uri u=Uri.parse(mySongs.get(pos).toString());
        myMediaPlayer=MediaPlayer.create(getApplicationContext(),u);
        myMediaPlayer.start();
        sb.setMax(myMediaPlayer.getDuration());
        updateSeekBar.start();
        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) { }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                myMediaPlayer.seekTo(sb.getProgress()); }
        });
        btn_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sb.setMax(myMediaPlayer.getDuration());
                if(myMediaPlayer.isPlaying()){
                    btn_pause.setBackgroundResource(R.drawable.icon_play);
                    myMediaPlayer.pause();
                    Toast.makeText(PlayerActivity.this,"Music Pause",Toast.LENGTH_SHORT).show(); }
                else{
                    btn_pause.setBackgroundResource(R.drawable.icon_pause);
                    myMediaPlayer.start();
                    Toast.makeText(PlayerActivity.this,"Music Start",Toast.LENGTH_SHORT).show(); } }
        });
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myMediaPlayer.stop();
                myMediaPlayer.release();
                pos=(pos+1)%mySongs.size();
                Uri u=Uri.parse(mySongs.get(pos).toString());
                myMediaPlayer=MediaPlayer.create(getApplicationContext(),u);
                sname=mySongs.get(pos).getName().toString();
                songTextLabel.setText(sname);
                myMediaPlayer.start();
                Toast.makeText(PlayerActivity.this,"Next Music Start",Toast.LENGTH_SHORT).show(); }});
        btn_previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myMediaPlayer.stop();
                myMediaPlayer.release();
                pos=((pos-1)<0)?(mySongs.size()-1):(pos-1);
                Uri u=Uri.parse(mySongs.get(pos).toString());
                myMediaPlayer=MediaPlayer.create(getApplicationContext(),u);
                sname=mySongs.get(pos).getName().toString();
                songTextLabel.setText(sname);
                myMediaPlayer.start();
                Toast.makeText(PlayerActivity.this,"Previous Music Started",Toast.LENGTH_SHORT).show(); }});
    }
    public void chngimg(View view) {
        if(change==1) {
            imageButton.setImageResource(R.drawable.music);
            change=0; }
        else {
            imageButton.setImageResource(R.drawable.musicone);
            change=1; } }
}
