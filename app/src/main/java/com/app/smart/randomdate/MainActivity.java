package com.app.smart.randomdate;

import android.nfc.Tag;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Random;

import static java.lang.Math.random;

public class MainActivity extends AppCompatActivity {

TextView dayRandomNumber,monthRandomNumber,yearRandomNumber, trialCount;
Button generateRandomNumber;

Random r;

int randomDay, randomMonth, randomYear, trialCountNumber;

int max = 30;

int min = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        r = new Random();

        dayRandomNumber= findViewById(R.id.random_day_text_view);

        monthRandomNumber= findViewById(R.id.random_month_text_view);

        yearRandomNumber= findViewById(R.id.random_year_text_view);

        trialCount = findViewById(R.id.trialCount);

        generateRandomNumber = findViewById(R.id.generate_random_number);

        //final int result = ((int)(Math.random() * 10)) + 1;

        trialCountNumber = 0;

        //Log.d("TAG", getString(random));

        generateRandomNumber.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                randomDay =r.nextInt(30 ) + 1;

                randomMonth = r.nextInt(12)+1;

                randomYear = r.nextInt(118) +1900 ;

            if (randomMonth == 1 && randomDay==30){
                randomDay =r.nextInt(27 ) + 1;
            }

            if (randomMonth == 1 && randomDay==29){
                randomDay =r.nextInt(25 ) + 1;
            }

            if (randomMonth == 1 && randomDay==28){
                randomDay =r.nextInt(26 ) + 1;
            }

            trialCountNumber++;
            dayRandomNumber.setText(String.valueOf(randomDay));
            monthRandomNumber.setText(String.valueOf(randomMonth));
            yearRandomNumber.setText(String.valueOf(randomYear));
            trialCount.setText(String.valueOf(trialCountNumber));
            }
        });

    }



}
