package com.github.anpnrynn.SAKUA2.DominoPassword;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.media.Session2Command;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.cardview.widget.CardView;

import com.github.anpnrynn.SAKUA2.DominoPassword.databinding.ActivityMainBinding;

import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toolbar;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    Button add;
    Button restore;
    PatternActivity patternActivity;
    MainActivity mainActivity;

    int rows = 0;

    public static String result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mainActivity = this;
        patternActivity = new PatternActivity( );

        //System.out.print(getApplicationInfo().dataDir);

        add = (Button)findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //patternActivity.setSiteAndPin("google.com", "334455");
                Intent intent = new Intent( mainActivity, PatternActivity.class);
                intent.putExtra("Operation" , "Add" );
                intent.putExtra("Site", "");
                intent.putExtra("Pin", "");
                startActivityForResult(intent, 1 );
                //finish();
            }
        });



        restore = (Button)findViewById(R.id.restorebackup);
        restore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //patternActivity.setSiteAndPin("google.com", "334455");
                rows = 0;
                clearCardView();
                restoreBackup();
                fillCardView();
            }
        });

        clearCardView();
        readSavedPasswords();
        fillCardView();

    }

    public HashMap<String,String> sitePinPairs;


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);


        try {
            System.out.println("INFO: received activity result ");


            String op = data.getStringExtra("operation");
            String sop = data.getStringExtra("suboperation");
            String site = data.getStringExtra("retSite");
            String pin = data.getStringExtra("retPin");

            System.out.println("INFO: " + op);
            System.out.println("INFO: " + sop);
            System.out.println("INFO: " + site);
            System.out.println("INFO: " + pin);

            // First we need to check if the requestCode matches the one we used.
            //if (requestCode == 1) {

            // The resultCode is set by the DetailActivity
            // By convention RESULT_OK means that whatever
            // DetailActivity did was executed successfully
            //if (resultCode == Activity.RESULT_OK) {
            if (op.equals("return")) {
                if (sop.equals("delete") &&
                        !site.equals("") && !pin.equals("")) {
                    //sitePinPairs.put( data.getStringExtra("retSite"), data.getStringExtra("retPin") );
                    System.out.println("INFO: Deleting card view");
                    sitePinPairs.remove(site);
                    //CardView cv = createCardView( data.getStringExtra("retSite"), data.getStringExtra("retPin") );
                    LinearLayout l = findViewById(R.id.cards);
                    clearCardView();
                    fillCardView();
                } else if (sop.equals("save") &&
                        !site.equals("") && !pin.equals("")) {
                    System.out.println("INFO: Creating card view");
                    sitePinPairs.put(site, pin);
                    rows++;
                    CardView cv = createCardView(site, pin);
                    LinearLayout l = findViewById(R.id.cards);
                    l.addView(cv);
                }
                savePasswords();
            }
            //} else {
            // setResult wasn't successfully executed by DetailActivity
            // Due to some error or flow of control. No data to retrieve.
            //}
            //}
        } catch ( Exception  e){
            System.out.println("EXCP: " + "Exception happened from return from pattern activity" );
        }
    }

    public CardView createCardView(String sitename, String pinnumber){
        CardView cv = new CardView(this.getBaseContext());
        cv.setRadius(8);
        cv.setPadding(0, 8,0,0 );
        cv.setMinimumHeight( 64 );

        if( rows % 2 == 0 ) {
            //cv.setBackgroundColor();
            cv.setBackgroundColor(Color.WHITE);
        } else {
            cv.setBackgroundColor(0xFFEEEEEE);
        }


        cv.setOnClickListener(new CardView.OnClickListener() {
            public void onClick(View v) {

                                //patternActivity.setSiteAndPin("google.com", "334455");
                Intent intent = new Intent( mainActivity, PatternActivity.class);
                intent.putExtra("Operation" , "Edit" );
                intent.putExtra("Site", sitename);
                intent.putExtra("Pin", pinnumber);
                startActivityForResult(intent, 1 );
                //finish();
            }
        });

        //android:layout_height="match_parent"
        //android:paddingTop="4pt">

        //            <LinearLayout
        LinearLayout l = new LinearLayout(cv.getContext());
        l.setOrientation(LinearLayout.VERTICAL);

        LinearLayout.LayoutParams layoutParams =
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);

        layoutParams.setMargins(8, 8, 8, 8);
        l.setLayoutParams(layoutParams);


        TextView tva = new TextView(l.getContext());
        tva.setTextSize(20);
        tva.setText( sitename );


        TextView tvb = new TextView(l.getContext());
        tvb.setTextSize(16);
        tvb.setText( pinnumber );

        l.addView(tvb, 0);
        l.addView(tva, 0);

        cv.addView( l );
        return cv;

    }

    public void clearCardView() {
        LinearLayout l = (LinearLayout) findViewById(R.id.cards);
        l.removeAllViews();
        rows = 0;
    }

    public void fillCardView( ) {
        LinearLayout l = (LinearLayout) findViewById(R.id.cards);
        String sitename = "";
        String pinnumber = "";
        int i = 0;

        Set<Map.Entry<String,String>> s;
        for (Map.Entry<String, String> e : sitePinPairs.entrySet()) {
            rows++;
            l.addView( createCardView( e.getKey(), e.getValue() ) );
        }
    }

    public void savePasswords( ) {
        System.out.println( "INFO: Saving passwords");
        LinearLayout l = (LinearLayout) findViewById(R.id.cards);
        String sitename = "";
        String pinnumber = "";
        int i = 0;

        //Set<Map.Entry<String,String>> s1;
        try {
            FileOutputStream fos = new FileOutputStream(getApplicationInfo().dataDir+"/dp.txt");
            System.out.println("INFO: Saving to : "+ getApplicationInfo().dataDir+"/dp.txt");
            String s1 = new String();
            for (Map.Entry<String, String> e : sitePinPairs.entrySet()) {
                s1 += e.getKey().toString() + "@" + e.getValue().toString() + "\n";
            }

            fos.write(s1.getBytes());
            fos.close();

        } catch  ( Exception ignored){
            System.out.println( "INFO: " + ignored.toString() );
        }
    }
    public void readAndCreateMap(String filename){

        System.out.println( "INFO: Reading passwords");

        try{

            BufferedReader reader = new BufferedReader(new FileReader(getApplicationInfo().dataDir+"/dp.txt"));
            String line = reader.readLine();

            while (line != null) {
                //System.out.println("INFO: " + line);
                String []nv = line.split("@");
                sitePinPairs.put( nv[0], nv[1]);
                // read next line
                line = reader.readLine();
            }

            reader.close();

            /*
            FileInputStream inStream = new FileInputStream( filename );
            inStream = new FileInputStream();
            int m = inStream.available();
            byte[] b = new byte[m];
            int n = inStream.read(b);

            int i = 0;
            String s = "";
            byte[] d = new byte[1024];
            int j = 0;
            while ( i < n ) {
                if( b[i] == '\n' ) {
                    String []nv = s.split("@");
                    sitePinPairs.put( nv[0], nv[1]);
                    s = "";
                }
                s += Byte.toString(b[i++]);
            }
            inStream.close();
            */
        } catch ( RuntimeException e){
            System.out.println( "INFO: " + e.toString() );
        } catch (FileNotFoundException e) {
            System.out.println( "INFO: " + e.toString() );
        } catch (IOException e) {
            System.out.println( "INFO: " + e.toString() );
        }

        System.out.println( "INFO: Read  passwords");
    }

    public void readSavedPasswords( ){

        System.out.println( "INFO: Backing up passwords");
        try {
            File f1 = new File( getApplicationInfo().dataDir+"/dp.txt");
            File f2 = new File( getApplicationInfo().dataDir+"/dp.txt.bkp ");
            FileInputStream inStream;
            FileChannel inChannel;
            if ( f1.lastModified() >= f2.lastModified() && f1.length() >= f2.length() ) {
               sitePinPairs = new HashMap<String, String>();
               inStream = new FileInputStream(getApplicationInfo().dataDir+"/dp.txt");
               FileOutputStream outStream = new FileOutputStream(getApplicationInfo().dataDir+"/dp.txt.bkp");
               inChannel = inStream.getChannel();
               FileChannel outChannel = outStream.getChannel();
               inChannel.transferTo(0, inChannel.size(), outChannel);
               inStream.close();
               inChannel.close();
               outChannel.close();
               outStream.close();
           }
            readAndCreateMap(getApplicationInfo().dataDir+"/dp.txt");

        } catch (IOException ex) {
            System.out.println( "INFO: " + ex.toString() );
        }

    }

    void restoreBackup( ){
        readAndCreateMap( getApplicationInfo().dataDir+"/dp.txt.bkp");
    }


/*

    public void onClick(View v) {
        Intent intent = new Intent(mainActivity, PatternActivity.class);
        intent.putExtra("Operation" , "Edit" );
        startActivity(intent);
    }*/
}