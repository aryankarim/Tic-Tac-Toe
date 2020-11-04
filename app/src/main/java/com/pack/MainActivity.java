package com.pack;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;

import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private Button[][] btns = new Button[3][3];
    private boolean p1turn = true;
    private int roundcount;

    static boolean vsPC=false;

    private int p1points;
    private int p2points;

    private TextView tx1;
    private TextView tx2;

    final MediaPlayer[] medias = new MediaPlayer[5];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tx1 = findViewById(R.id.P1);
        tx2 = findViewById(R.id.P2);

        medias[0] = MediaPlayer.create(this,R.raw.horn);
        medias[1] = MediaPlayer.create(this, R.raw.ohh);
        medias[2] = MediaPlayer.create(this,R.raw.profanity);
        medias[3] = MediaPlayer.create(this,R.raw.ohyeah);
        medias[4] = MediaPlayer.create(this,R.raw.sudud);


        for(int i=0; i<3;i++){
            for (int j=0;j<3;j++){
                String btnID = "bt" + i + j;
                //manually created an resourse ID aka bottons on the APP
                //getResources returns mResources obj
                //getIdentifier returns mResourceImpl.getIdentifier obj
                //getIdentifier takes name of resource and type of resource and package name
                int resID = getResources().getIdentifier(btnID,"id",getPackageName());
                //we assign resourceID that we created
                btns[i][j] = findViewById(resID);
                btns[i][j].setBackgroundColor(Color.parseColor("#333654"));
                btns[i][j].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clicked(v);
                    }
                });
            }
        }
        Button vsPC = findViewById(R.id.vsPC);

        vsPC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetGame();
                MainActivity.vsPC=true;
            }
        });

        final Button pvp = findViewById(R.id.pvp);

        pvp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetGame();
                MainActivity.vsPC=false;
            }
        });


        Button reset = findViewById(R.id.reset);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetGame();
            }
        });

    }

   private void clicked(View v){
       if(   !((Button)v).getText().toString().equals("")    ){
           return;
       }

       if (vsPC){
               ((Button)v).setText("X");
               (v).setBackgroundColor(Color.parseColor("#8ae48a"));

               roundcount++;

               if (checkWin()){
                   if (p1turn){
                       p1winz();
                       return;
                   }else{
                       pcwinz();
                       return;
                   }
               }else if(roundcount==9){
                   draw();
                   return;
               }
               p1turn=!p1turn;
               pcPlays();
                   if (checkWin()){
                       if (p1turn){
                           p1winz();
                           return;
                       }else{
                           pcwinz();
                           return;
                       }
                   }else if(roundcount==9){
                       draw();
                       return;
                   }
                p1turn=!p1turn;
                roundcount++;
                return;

       }else {
           if (p1turn){
               ((Button)v).setText("X");
               (v).setBackgroundColor(Color.parseColor("#8ae48a"));
           }else {
               ((Button) v).setText("O");
               (v).setBackgroundColor(Color.parseColor("#ff6363"));
           }
       }

       roundcount++;
       if (checkWin()){
           if (p1turn){
               p1winz();
           }else{
               p2winz();
           }
       }else if(roundcount==9){
           draw();
       }else{
           p1turn = !p1turn;
       }
   }

    private void pcPlays() {
        String[][] linez = new String[3][3];

        for (int i=0; i<3 ;i++){
            for (int j=0; j<3; j++){
                linez[i][j]=btns[i][j].getText().toString();
            }
        }

        if(linez[1][1].equals("")){
            push(1,1);
            return;
        }else{
            for (int i = 0 ; i < 3; i++){
                if(linez[i][0].equals(linez[i][1])&& !linez[i][0].equals("") && linez[i][2].equals("")){
                    push(i,2);
                    return;
                }
                if(linez[i][1].equals(linez[i][2])&& !linez[i][1].equals("") && linez[i][0].equals("")){
                    push(i,0);
                    return;
                }
                if(linez[i][0].equals(linez[i][2])&& !linez[i][0].equals("") && linez[i][1].equals("")){
                    push(i,1);
                    return;
                }
            }
            for (int i = 0 ; i < 3; i++){
                if(linez[0][i].equals(linez[1][i])&& !linez[0][i].equals("") && linez[2][i].equals("")){
                    push(2,i);
                    return;
                }
                if(linez[1][i].equals(linez[2][i])&& !linez[1][i].equals("") && linez[0][i].equals("")){
                    push(0,i);
                    return;
                }
                if(linez[0][i].equals(linez[2][i])&& !linez[0][i].equals("")&& linez[1][i].equals("")){
                    push(1,i);
                    return;
                }
            }
            if (linez[0][0].equals(linez[1][1]) && linez[2][2].equals("")){
                push(2,2);
                return;
            }
            if (linez[1][1].equals(linez[2][2])  && linez[0][0].equals("")){
                push(0,0);
                return;
            }

            if (linez[2][0].equals(linez[1][1])  && linez[0][2].equals("")){
                push(0,2);
                return;
            }
            if (linez[0][2].equals(linez[1][1])  && linez[2][0].equals("")){
                push(2,0);
                return;
            }
        }
            for (int i=0;i<3;i++){
                for (int j=0;j<3;j++){
                    if (linez[i][j].equals("")){
                        push(i,j);
                        return;
                    }
                }
            }
    }

    private void push(int x, int y) {
        btns[x][y].setText("O");
        btns[x][y].setBackgroundColor(Color.parseColor("#ff6363"));
    }

    private boolean checkWin(){
        String[][] linez = new String[3][3];

        for (int i=0; i<3 ;i++){
            for (int j=0; j<3; j++){
                linez[i][j]=btns[i][j].getText().toString();
            }
        }
        for (int i = 0 ; i < 3; i++){
            if(linez[i][0].equals(linez[i][1]) && linez[i][1].equals(linez[i][2]) && !linez[i][1].equals("")){
                return true;
            }
        }
        for (int i = 0 ; i < 3; i++){
            if(linez[0][i].equals(linez[1][i])&& linez[1][i].equals(linez[2][i]) && !linez[1][i].equals("")){
                return true;
            }
        }
        if (linez[0][0].equals(linez[1][1]) && linez[1][1].equals(linez[2][2]) && !linez[1][1].equals("")){
            return true;
        }
        return linez[0][2].equals(linez[1][1]) && linez[1][1].equals(linez[2][0]) && !linez[1][1].equals("");
    }

    private void p1winz(){
        medias[0].start();
        p1points++;
        Toast.makeText(this,"PLAYER 1 WINZ BABY!",Toast.LENGTH_SHORT).show();
        updatePointsText();
        resetBoard();
    }
    private void p2winz(){
        medias[2].start();
        p2points++;
        Toast.makeText(this,"JEEZ PLAYER 2! YOU WIN!",Toast.LENGTH_SHORT).show();
        updatePointsText();
        resetBoard();
    }
    private void pcwinz(){
        medias[4].start();
        p2points++;
        Toast.makeText(this,"SUHHHHHH DUDEEEEEEEE",Toast.LENGTH_SHORT).show();
        updatePointsText();
        resetBoard();
    }
    private void draw(){
        medias[3].start();
        Toast.makeText(this,"DRAWW",Toast.LENGTH_SHORT).show();
        resetBoard();
    }
    private void updatePointsText(){
        tx1.setText("P1: "+p1points);
        tx2.setText("P2: "+p2points);
    }
    private void resetBoard(){
        for (int i = 0; i < 3 ;i++){
            for (int j = 0; j < 3 ; j++){
                btns[i][j].setText("");
                btns[i][j].setBackgroundColor(Color.parseColor("#333654"));
            }
        }
        roundcount=0;
        p1turn=true;
    }

    private void resetGame(){
        medias[1].start();
        p1points=0;
        p2points=0;
        updatePointsText();
        resetBoard();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);

        outState.putInt("roundcount",roundcount);
        outState.putInt("p1points",p1points);
        outState.putInt("p2points",p2points);
        outState.putBoolean("p1turn",p1turn);

    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        roundcount = savedInstanceState.getInt("roundcount");
        p1points = savedInstanceState.getInt("p1points");
        p2points = savedInstanceState.getInt("p2points");
        p1turn = savedInstanceState.getBoolean("p1turn");
        for (int i = 0; i < 3 ;i++){
            for (int j = 0; j < 3 ; j++){
                if(btns[i][j].getText().equals("X")){
                    btns[i][j].setBackgroundColor(Color.parseColor("#8ae48a"));
                }else if(btns[i][j].getText().equals("O")){
                    btns[i][j].setBackgroundColor(Color.parseColor("#ff6363"));
                }

            }
        }
    }
}
