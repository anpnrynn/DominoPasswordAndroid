package com.github.anpnrynn.SAKUA2.DominoPassword;

import android.app.Activity;
import android.content.Intent;
//import android.graphics.Canvas;
//import android.graphics.ColorFilter;
//import android.graphics.drawable.Drawable;
//import android.graphics.drawable.Icon;
import android.os.Bundle;

//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
//import android.webkit.WebResourceRequest;
import android.webkit.WebView;
//import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.navigation.ui.AppBarConfiguration;

import com.github.anpnrynn.SAKUA2.DominoPassword.databinding.ActivityPatternBinding;
//import com.google.android.material.button.MaterialButton;

public class PatternActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityPatternBinding binding;


    WebView Pattern;

    String  password;
    String  site = "" ;
    String  pin = "";

    String origSite = "";
    String origPin  = "";
    String  indices = "";

    EditText Password;
    EditText Site;
    EditText Pin;


    //Gen1 InitData String
    //var initdata = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz`~!@#$%^&*()_+-={}|[]\\:\";'<>?,./";
    //Gen2 InitData String
    //Caution: Do not use Gen2 InitData String for crypto functions as its for password generation only
    private String initdata = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvwxyz!$'*+,-./:;<=>?^_`~$'*+,-./:;<=>?^_`";

    private String shuffledData ="";

    private  String  rotate(String srotate, int n){
        String s1 = srotate.substring (0, n);
        String s2 = srotate.substring (n, srotate.length());
        return s2+s1;
    }

    private String swapchars( String s, int m, int n )
    {
        StringBuilder sb = new StringBuilder( s );
        char c = s.charAt(m);
        sb.setCharAt(m, s.charAt(n) );
        sb.setCharAt(n, c );;
        return sb.toString();
    }

    private String shuffle (String s, int pin){
        String stemp = s;
        int n = stemp.length();
        int k = 1;
        for ( int j = 0; j < pin; j++ ) {
            String s1 = stemp.substring( 0, n/2);
            String s2 = stemp.substring( n/2, n);

            for ( int i = 0; i < n/4; i++ ){
                if( k % 2  == 0 ){
                    s1 = swapchars( s1, i, n/2-i-1 );
                    s2 = swapchars( s2, i, n/2-i-1);
                } else {
                    s1 = swapchars( s1, i, n/2-i-2 );
                    s2 = swapchars( s2, i, n/2-i-2 );
                }

                k++;
            }
            s1 = rotate( s1, 13 );
            s2 = rotate( s2, 17 );
            stemp = s1 + s2;

            if( s == stemp ){
                //console.log("strings match: " + stemp);
            }
            stemp = rotate( stemp, 23 );
        }



        if( stemp.length() != s.length() ){

            System.out.println( "INFO: lengths don't match ");
            System.out.println(stemp);
            System.out.println(s);
        } else {
            System.out.println( "INFO: lengths match ");
            System.out.println(stemp);
        }

        String extendstring = stemp;
        //System.out.println("INFO: OrigString = "+ s);
        //System.out.println("INFO: CryptoString = "+ extendstring);
        shuffledData = extendstring;
        //setShuffledData(shuffledData);
        return extendstring;
    }


    int patButtonValue[] = new int[108];
    int curPattern = 1;
    protected void onPatButtonClick(){

    }

    void loadCurPatternValues(){
        int i = 0;


        int ids[] = { 0,
                R.id.patbutton_1,
                R.id.patbutton_2,
                R.id.patbutton_3,
                R.id.patbutton_4,
                R.id.patbutton_5,
                R.id.patbutton_6,
                R.id.patbutton_7,
                R.id.patbutton_8,
                R.id.patbutton_9,
                R.id.patbutton_10,
                R.id.patbutton_11,
                R.id.patbutton_12,
                R.id.patbutton_13,
                R.id.patbutton_14,
                R.id.patbutton_15,
                R.id.patbutton_16,
                R.id.patbutton_17,
                R.id.patbutton_18,
                R.id.patbutton_19,
                R.id.patbutton_20,
                R.id.patbutton_21,
                R.id.patbutton_22,
                R.id.patbutton_23,
                R.id.patbutton_24,
                R.id.patbutton_25,
                R.id.patbutton_26,
                R.id.patbutton_27,
                R.id.patbutton_28,
                R.id.patbutton_29,
                R.id.patbutton_30,
                R.id.patbutton_31,
                R.id.patbutton_32,
                R.id.patbutton_33,
                R.id.patbutton_34,
                R.id.patbutton_35,
                R.id.patbutton_36
        };

        int start =  (curPattern - 1) * 36;
        for( i = 1; i <= 36; i++ ){
            ImageButton patButton = (ImageButton) findViewById(ids[i]);
            try{

                if( patButtonValue[start] == 1 ){
                    if( curPattern == 1 )
                        patButton.setImageResource(R.mipmap.red);
                    else if ( curPattern == 2 ){
                        patButton.setImageResource(R.mipmap.green);
                    } else {
                        patButton.setImageResource(R.mipmap.blue);
                    }
                } else {
                    patButton.setImageResource(R.mipmap.untouched);
                }
            }catch (Exception e ){

            }
            start++;
        }
    }

    protected void setIconOnClick(ImageButton mb, int index ) {
        int cLoc = (curPattern - 1) * 36 + index;
        if( cLoc < 1 || cLoc > 108 || shuffledData.length() <= 0 ) {
            return;
        }
        patButtonValue[cLoc-1] = 1;
        //MaterialButton mb = (MaterialButton) b;
        if (curPattern == 1){
            //b.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.red, 0, 0, 0);
            mb.setImageResource( R.mipmap.red );
            String tmp = "";
            tmp += shuffledData.charAt( cLoc - 1);
            Password.setText(Password.getText().toString()+ tmp );

        }
        else if ( curPattern == 2 ){
            //b.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.green,0,0,0);
            mb.setImageResource( R.mipmap.green);
            String tmp = "";
            tmp += shuffledData.charAt( cLoc - 1);
            Password.setText(Password.getText().toString()+ tmp );
        } else {
            //b.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.blue,0,0,0);
            mb.setImageResource( R.mipmap.blue);
            String tmp = "";
            tmp += shuffledData.charAt( cLoc - 1);
            Password.setText(Password.getText().toString()+ tmp );
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int i = 0;
        for( i= 0; i< 108; i++ ){
            patButtonValue[i] = 0;
        }



        curPattern = 3;
        loadCurPatternValues();
        curPattern = 2;
        loadCurPatternValues();
        curPattern = 1;
        loadCurPatternValues();

        binding = ActivityPatternBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //setResult(0);

        //Pattern = (WebView) findViewById(R.id.Pattern);
        Password = (EditText) findViewById(R.id.Password);
        //Pattern.getSettings().setJavaScriptEnabled(true);
        //pattern.zoomIn();
        //pattern.loadUrl("file:///android_asset/DominoPassword.html");


        site = this.getIntent().getStringExtra("Site");
        pin = this.getIntent().getStringExtra("Pin");
        String op = this.getIntent().getStringExtra("Operation");
        System.out.println("INFO: Operation = " + op );

        origSite = site;
        origPin  = pin;

        Site = (EditText)findViewById(R.id.Name);
        Site.setText(site);
        Pin = (EditText)findViewById(R.id.Pin);
        Pin.setText(pin);

        if( op.equals("Edit") ){
            int mypin = Integer.parseInt(pin);
            shuffle( initdata, mypin);
        }

        Button reset = (Button) findViewById(R.id.Reset);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Pattern.stopLoading();
                //Pattern.loadUrl("file:///android_asset/DominoPassword.html");
                Password.setText("");
                Pin.setText ("");
                Site.setText("");
                pin = origPin;
                password = "";
                indices = "";
                site = origSite;
                shuffledData = "";

                int i = 0;
                for(i=0; i<108; i++ ){
                    patButtonValue[i] = 0;
                }
                curPattern = 1;
                loadCurPatternValues();
            }
        });

        Button save = (Button) findViewById(R.id.Save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( !Site.getText().toString().equals("")) {
                    Intent newIntent = new Intent();
                    System.out.println("INFO : Saving the information ");
                    newIntent.putExtra("retSite", Site.getText().toString());
                    newIntent.putExtra("retPin", pin);
                    newIntent.putExtra("retIndices", indices);
                    newIntent.putExtra("retPassword", password);
                    newIntent.putExtra("operation", "return");
                    newIntent.putExtra("suboperation", "save");

                    setResult(Activity.RESULT_OK, newIntent);
                    finishActivity(Activity.RESULT_OK);
                    //finish();
                    finish();
                }
            }
        });


        Button copy = (Button) findViewById(R.id.Copy);
        copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getBaseContext()
                        .getSystemService(getBaseContext().CLIPBOARD_SERVICE);
                android.content.ClipData clip = android.content.ClipData.newPlainText("DominoPassword", Password.getText().toString());
                clipboard.setPrimaryClip(clip);
            }
        });

        Button delete = (Button) findViewById(R.id.Delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( !Site.getText().toString().equals("")) {
                    Intent newIntent = new Intent();
                    System.out.println("INFO : Saving the information ");
                    newIntent.putExtra("retSite", Site.getText().toString());
                    newIntent.putExtra("retPin", pin);
                    newIntent.putExtra("retIndices", indices);
                    newIntent.putExtra("retPassword", password);
                    newIntent.putExtra("operation", "return");
                    newIntent.putExtra("suboperation", "delete");
                    setResult(Activity.RESULT_OK, newIntent);
                    //finish();
                    finishActivity(Activity.RESULT_OK);
                    finish();
                }
            }
        });


        Button previous = (Button) findViewById(R.id.Previous);
        previous.setOnClickListener( new View.OnClickListener () {
            @Override
            public void onClick(View v ) {
                if( curPattern > 1 ) {
                    curPattern--;
                }
                loadCurPatternValues();
                EditText b = (EditText) findViewById(R.id.patternEdit );
                b.setText( "Pattern - "+ Integer.toString(curPattern) +"/3" );
            }
        });

        Button next = (Button) findViewById(R.id.Next);
        next.setOnClickListener( new View.OnClickListener () {
            @Override
            public void onClick(View v ) {
                if (curPattern < 3) {
                    curPattern++;
                }
                EditText b = (EditText) findViewById(R.id.patternEdit );
                b.setText( "Pattern - "+ Integer.toString(curPattern) +"/3" );
                loadCurPatternValues();
            }
        });

        Button gen = (Button) findViewById(R.id.Generate);
        gen.setOnClickListener( new View.OnClickListener () {
            @Override
            public void onClick(View v ) {
                pin = Pin.getText().toString();
                int mypin = Integer.parseInt(pin);
                shuffle( initdata, mypin);
            }
        });

        ImageButton patButton_1 = (ImageButton) findViewById(R.id.patbutton_1);
        patButton_1.setOnClickListener( new View.OnClickListener () {
            @Override
            public void onClick(View v ) {
                setIconOnClick(patButton_1, 1);
            }
        });

        ImageButton patButton_2 = (ImageButton) findViewById(R.id.patbutton_2);
        patButton_2.setOnClickListener( new View.OnClickListener () {
            @Override
            public void onClick(View v ) {
                setIconOnClick(patButton_2, 2);
            }
        });

        ImageButton patButton_3 = (ImageButton) findViewById(R.id.patbutton_3);
        patButton_3.setOnClickListener( new View.OnClickListener () {
            @Override
            public void onClick(View v ) {
                setIconOnClick(patButton_3, 3);
            }
        });

        ImageButton patButton_4 = (ImageButton) findViewById(R.id.patbutton_4);
        patButton_4.setOnClickListener( new View.OnClickListener () {
            @Override
            public void onClick(View v ) {
                setIconOnClick(patButton_4, 4);
            }
        });

        ImageButton patButton_5 = (ImageButton) findViewById(R.id.patbutton_5);
        patButton_5.setOnClickListener( new View.OnClickListener () {
            @Override
            public void onClick(View v ) {
                setIconOnClick(patButton_5, 5);
            }
        });

        ImageButton patButton_6 = (ImageButton) findViewById(R.id.patbutton_6);
        patButton_6.setOnClickListener( new View.OnClickListener () {
            @Override
            public void onClick(View v ) {
                setIconOnClick(patButton_6, 6);
            }
        });

        ImageButton patButton_7 = (ImageButton) findViewById(R.id.patbutton_7);
        patButton_7.setOnClickListener( new View.OnClickListener () {
            @Override
            public void onClick(View v ) {
                setIconOnClick(patButton_7, 7);
            }
        });

        ImageButton patButton_8 = (ImageButton) findViewById(R.id.patbutton_8);
        patButton_8.setOnClickListener( new View.OnClickListener () {
            @Override
            public void onClick(View v ) {
                setIconOnClick(patButton_8, 8);
            }
        });

        ImageButton patButton_9 = (ImageButton) findViewById(R.id.patbutton_9);
        patButton_9.setOnClickListener( new View.OnClickListener () {
            @Override
            public void onClick(View v ) {
                setIconOnClick(patButton_9, 9);
            }
        });

        ImageButton patButton_10 = (ImageButton) findViewById(R.id.patbutton_10);
        patButton_10.setOnClickListener( new View.OnClickListener () {
            @Override
            public void onClick(View v ) {
                setIconOnClick(patButton_10, 10);
            }
        });

        ImageButton patButton_11 = (ImageButton) findViewById(R.id.patbutton_11);
        patButton_11.setOnClickListener( new View.OnClickListener () {
            @Override
            public void onClick(View v ) {
                setIconOnClick(patButton_11, 11);
            }
        });

        ImageButton patButton_12 = (ImageButton) findViewById(R.id.patbutton_12);
        patButton_12.setOnClickListener( new View.OnClickListener () {
            @Override
            public void onClick(View v ) {
                setIconOnClick(patButton_12, 12);
            }
        });

        ImageButton patButton_13 = (ImageButton) findViewById(R.id.patbutton_13);
        patButton_13.setOnClickListener( new View.OnClickListener () {
            @Override
            public void onClick(View v ) {
                setIconOnClick(patButton_13, 13);
            }
        });

        ImageButton patButton_14 = (ImageButton) findViewById(R.id.patbutton_14);
        patButton_14.setOnClickListener( new View.OnClickListener () {
            @Override
            public void onClick(View v ) {
                setIconOnClick(patButton_14, 14);
            }
        });

        ImageButton patButton_15 = (ImageButton) findViewById(R.id.patbutton_15);
        patButton_15.setOnClickListener( new View.OnClickListener () {
            @Override
            public void onClick(View v ) {
                setIconOnClick(patButton_15, 15);
            }
        });

        ImageButton patButton_16 = (ImageButton) findViewById(R.id.patbutton_16);
        patButton_16.setOnClickListener( new View.OnClickListener () {
            @Override
            public void onClick(View v ) {
                setIconOnClick(patButton_16, 16);
            }
        });

        ImageButton patButton_17 = (ImageButton) findViewById(R.id.patbutton_17);
        patButton_17.setOnClickListener( new View.OnClickListener () {
            @Override
            public void onClick(View v ) {
                setIconOnClick(patButton_17, 17);
            }
        });

        ImageButton patButton_18 = (ImageButton) findViewById(R.id.patbutton_18);
        patButton_18.setOnClickListener( new View.OnClickListener () {
            @Override
            public void onClick(View v ) {
                setIconOnClick(patButton_18, 18);
            }
        });

        ImageButton patButton_19 = (ImageButton) findViewById(R.id.patbutton_19);
        patButton_19.setOnClickListener( new View.OnClickListener () {
            @Override
            public void onClick(View v ) {
                setIconOnClick(patButton_19, 19);
            }
        });

        ImageButton patButton_20 = (ImageButton) findViewById(R.id.patbutton_20);
        patButton_20.setOnClickListener( new View.OnClickListener () {
            @Override
            public void onClick(View v ) {
                setIconOnClick(patButton_20, 20);
            }
        });

        ImageButton patButton_21 = (ImageButton) findViewById(R.id.patbutton_21);
        patButton_21.setOnClickListener( new View.OnClickListener () {
            @Override
            public void onClick(View v ) {
                setIconOnClick(patButton_21, 21);
            }
        });

        ImageButton patButton_22 = (ImageButton) findViewById(R.id.patbutton_22);
        patButton_22.setOnClickListener( new View.OnClickListener () {
            @Override
            public void onClick(View v ) {
                setIconOnClick(patButton_22, 22);
            }
        });

        ImageButton patButton_23 = (ImageButton) findViewById(R.id.patbutton_23);
        patButton_23.setOnClickListener( new View.OnClickListener () {
            @Override
            public void onClick(View v ) {
                setIconOnClick(patButton_23, 23);
            }
        });

        ImageButton patButton_24 = (ImageButton) findViewById(R.id.patbutton_24);
        patButton_24.setOnClickListener( new View.OnClickListener () {
            @Override
            public void onClick(View v ) {
                setIconOnClick(patButton_24, 24);
            }
        });

        ImageButton patButton_25 = (ImageButton) findViewById(R.id.patbutton_25);
        patButton_25.setOnClickListener( new View.OnClickListener () {
            @Override
            public void onClick(View v ) {
                setIconOnClick(patButton_25, 25);
            }
        });

        ImageButton patButton_26 = (ImageButton) findViewById(R.id.patbutton_26);
        patButton_26.setOnClickListener( new View.OnClickListener () {
            @Override
            public void onClick(View v ) {
                setIconOnClick(patButton_26, 26);
            }
        });

        ImageButton patButton_27 = (ImageButton) findViewById(R.id.patbutton_27);
        patButton_27.setOnClickListener( new View.OnClickListener () {
            @Override
            public void onClick(View v ) {
                setIconOnClick(patButton_27, 27);
            }
        });

        ImageButton patButton_28 = (ImageButton) findViewById(R.id.patbutton_28);
        patButton_28.setOnClickListener( new View.OnClickListener () {
            @Override
            public void onClick(View v ) {
                setIconOnClick(patButton_28, 28);
            }
        });

        ImageButton patButton_29 = (ImageButton) findViewById(R.id.patbutton_29);
        patButton_29.setOnClickListener( new View.OnClickListener () {
            @Override
            public void onClick(View v ) {
                setIconOnClick(patButton_29, 29);
            }
        });

        ImageButton patButton_30 = (ImageButton) findViewById(R.id.patbutton_30);
        patButton_30.setOnClickListener( new View.OnClickListener () {
            @Override
            public void onClick(View v ) {
                setIconOnClick(patButton_30, 30);
            }
        });

        ImageButton patButton_31 = (ImageButton) findViewById(R.id.patbutton_31);
        patButton_31.setOnClickListener( new View.OnClickListener () {
            @Override
            public void onClick(View v ) {
                setIconOnClick(patButton_31, 31);
            }
        });

        ImageButton patButton_32 = (ImageButton) findViewById(R.id.patbutton_32);
        patButton_32.setOnClickListener( new View.OnClickListener () {
            @Override
            public void onClick(View v ) {
                setIconOnClick(patButton_32, 32);
            }
        });

        ImageButton patButton_33 = (ImageButton) findViewById(R.id.patbutton_33);
        patButton_33.setOnClickListener( new View.OnClickListener () {
            @Override
            public void onClick(View v ) {
                setIconOnClick(patButton_33, 33);
            }
        });

        ImageButton patButton_34 = (ImageButton) findViewById(R.id.patbutton_34);
        patButton_34.setOnClickListener( new View.OnClickListener () {
            @Override
            public void onClick(View v ) {
                setIconOnClick(patButton_34, 34);
            }
        });

        ImageButton patButton_35 = (ImageButton) findViewById(R.id.patbutton_35);
        patButton_35.setOnClickListener( new View.OnClickListener () {
            @Override
            public void onClick(View v ) {
                setIconOnClick(patButton_35, 35);
            }
        });

        ImageButton patButton_36 = (ImageButton) findViewById(R.id.patbutton_36);
        patButton_36.setOnClickListener( new View.OnClickListener () {
            @Override
            public void onClick(View v ) {
                setIconOnClick(patButton_36, 36);
            }
        });
        //Password = (EditText)findViewById(R.id.Password);
        //Password.setText();

        //if(  pin != "" )
        //    shuffle(initdata, Integer.parseInt(pin) );


    }
}