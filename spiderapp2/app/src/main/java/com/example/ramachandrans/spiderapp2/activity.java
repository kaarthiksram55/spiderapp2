package com.example.ramachandrans.spiderapp2;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class activity extends Activity {

    TextView display, chances, wg, scoreview, bs;
    Button check;
    EditText input;
    String movie,m;
    SharedPreferences sp;
    SharedPreferences.Editor spe;
    char[] correctguesses,wrongguesses;
    int c=0,w=0;
    StringBuilder hidden = new StringBuilder("");
    int dashes = 0;
    int chance = 10;
    int letterflag = 0, repeatflag = 0;
    char letter;
    int score = 0, bestscore;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        display = findViewById(R.id.display);
        input = findViewById(R.id.input);
        chances = findViewById(R.id.chances);
        check = findViewById(R.id.check);
        wg = findViewById(R.id.wg);
        scoreview = findViewById(R.id.score);
        bs = findViewById(R.id.bs);

        sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        spe = sp.edit();

        String[] movieslist = {"thor", "iron man", "the avengers", "ant man", "thor ragnarok", "spiderman homecoming"};
        movie = movieslist[(int)(Math.random()*movieslist.length)];
        correctguesses = new char[movie.length()];
        wrongguesses = new char[movie.length()];
        display.setText("");
        bestscore = sp.getInt("score", score);
        bs.setText("best score: "+bestscore);
        scoreview.setText("score: "+score);
        for(int i=0;i<movie.length();i++){
            if (movie.charAt(i) == ' ') {
                hidden.append("  ");
            }
            else {
                hidden.append(" -");
                dashes++;
            }
        }
        hidden.append(" ");
        display.setText(hidden.toString());

    }
    public void wordchwck(){
        if(input.getText().toString().length()>1)
            input.setText(input.getText().toString().charAt(0));
    }

    public void check(View v){
        m = input.getText().toString();
        if(m.length()>1) {
            Toast.makeText(getApplicationContext(), "Enter only 1 letter", Toast.LENGTH_LONG).show();
            input.setText("");
        }
        else if(m.length()==0) {
            Toast.makeText(getApplicationContext(), "Enter a letter", Toast.LENGTH_LONG).show();
        }
        else{
            letter = m.charAt(0);
            for(int i=0;i<movie.length();i++){
                if(letter==correctguesses[i]||letter==wrongguesses[i]) {
                    repeatflag = 1;
                    Toast.makeText(getApplicationContext(),"already guessed",Toast.LENGTH_LONG).show();
                    break;
                }
            }
            for(int i=0;i<movie.length();i++){
                if(movie.charAt(i)==letter&&repeatflag==0){
                    hidden.replace(2*i+1,2*i+2,Character.toString(letter));
                    display.setText(hidden.toString());
                    dashes--;
                    score+=10;
                    scoreview.setText("score: "+score);
                    letterflag = 1;
                }
                else if(i==movie.length()-1&&letterflag==0&&repeatflag==0){
                    Toast.makeText(getApplicationContext(),"wrong guess",Toast.LENGTH_LONG).show();
                    chance--;
                    chances.setText("chances left: "+chance);
                    wg.setText(wg.getText().toString()+Character.toString(letter)+",");
                    score-=5;
                    scoreview.setText("score: "+score);
                    break;
                }
            }
            if(letterflag==1&&repeatflag==0){
                correctguesses[c] = letter;
                c++;
            }
            else if(letterflag==0&&repeatflag==0){
                wrongguesses[w] = letter;
                w++;
            }
            if(dashes==0){
                Toast.makeText(getApplicationContext(),"you did it!!!",Toast.LENGTH_LONG).show();
                check.setEnabled(false);
            }
            if(chance==0){
                Toast.makeText(getApplicationContext(),"Better luck next time",Toast.LENGTH_LONG).show();
                check.setEnabled(false);
            }
            bestScoreUpdate();
            letterflag = 0;
            repeatflag = 0;
        }
    }

    public void bestScoreUpdate(){
        if(score>bestscore){
            spe.putInt("score", score);
            spe.apply();
        }
    }


}
