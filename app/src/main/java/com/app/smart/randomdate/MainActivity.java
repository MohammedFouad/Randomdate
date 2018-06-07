package com.app.smart.randomdate;

import android.nfc.Tag;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
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

EditText dayEnteredNumber, monthEnteredNumber, yearEnteredNumber;


Random random;

int randomDay, randomMonth, randomYear, trialCountNumber;

int dayToGet, monthToGet, yearToGet;

String dayUser, monthUser, yearUser;

CheckBox easyCheckBox, mediumCheckBox, hardCheckBox;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        random = new Random();
        //random day, month and year
        dayRandomNumber      = findViewById(R.id.random_day_text_view);
        monthRandomNumber    = findViewById(R.id.random_month_text_view);
        yearRandomNumber     = findViewById(R.id.random_year_text_view);

        //how many users hit the luck point to get his entered date
        trialCount           = findViewById(R.id.trialCount);

        //Entered user day, month and year
        dayEnteredNumber     = findViewById(R.id.entered_day_text_view);
        monthEnteredNumber   = findViewById(R.id.entered_month_text_view);
        yearEnteredNumber    = findViewById(R.id.entered_year_text_view);

        generateRandomNumber = findViewById(R.id.generate_random_number);

        //check box declaration
        easyCheckBox        = findViewById(R.id.easyCheckBox);
        mediumCheckBox      = findViewById(R.id.mediumCheckBox);
        hardCheckBox        = findViewById(R.id.hardCheckBox);

        //initial value for counter
        trialCountNumber = 0;

        generateRandomNumber.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //user data get from another method
               // getUserDate();

                dayUser     = dayEnteredNumber.getText().toString();
                monthUser   = monthEnteredNumber.getText().toString();
                yearUser    = yearEnteredNumber.getText().toString();


                if(easyCheckBox.isChecked()){

                        if (!dayUser.equals("") &&
                            !monthUser.equals("") &&
                            !yearUser.equals(""))
                        {
                            dayToGet = Integer.parseInt(dayUser);
                            monthToGet = Integer.parseInt(monthUser);
                            yearToGet = Integer.parseInt(yearUser);

                            randomDay = random.nextInt(30)+1 ;
                            randomMonth = random.nextInt(1) +monthToGet;
                            randomYear = random.nextInt(1) +yearToGet;

                            if (randomDay == dayToGet &&
                                    randomMonth == monthToGet &&
                                    randomYear == yearToGet) {

                                trialCount.setTextColor(getResources().getColor(R.color.colorAccent));
                                Toast.makeText(MainActivity.this, " well done ", Toast.LENGTH_LONG).show();
                            }

                            trialCountNumber++;

                            dayRandomNumber.setText(String.valueOf(randomDay));
                            monthRandomNumber.setText(String.valueOf(randomMonth));
                            yearRandomNumber.setText(String.valueOf(randomYear));
                            trialCount.setText(String.valueOf(trialCountNumber));
                        }else{
                            Toast.makeText(MainActivity.this," Please set your date data",Toast.LENGTH_LONG).show();
                        }
                }
                if (mediumCheckBox.isChecked()){


                    if (!dayUser.equals("") &&
                            !monthUser.equals("") &&
                            !yearUser.equals(""))
                    {
                        dayToGet = Integer.parseInt(dayUser);
                        monthToGet = Integer.parseInt(monthUser);
                        yearToGet = Integer.parseInt(yearUser);

                        randomDay = random.nextInt(30)+1 ;
                        randomMonth = random.nextInt(2) +monthToGet;
                        randomYear = random.nextInt(1) +yearToGet;

                        if (randomDay == dayToGet &&
                                randomMonth == monthToGet &&
                                randomYear == yearToGet) {

                            trialCount.setTextColor(getResources().getColor(R.color.colorAccent));
                            Toast.makeText(MainActivity.this, " correct", Toast.LENGTH_LONG).show();

                        }
                        trialCountNumber++;

                        dayRandomNumber.setText(String.valueOf(randomDay));
                        monthRandomNumber.setText(String.valueOf(randomMonth));
                        yearRandomNumber.setText(String.valueOf(randomYear));
                        trialCount.setText(String.valueOf(trialCountNumber));
                    }else{
                        Toast.makeText(MainActivity.this," Please set your date data",Toast.LENGTH_LONG).show();
                    }
                }
                if(hardCheckBox.isChecked()){
                    if (!dayUser.equals("") &&
                            !monthUser.equals("") &&
                            !yearUser.equals(""))
                    {
                        dayToGet = Integer.parseInt(dayUser);
                        monthToGet = Integer.parseInt(monthUser);
                        yearToGet = Integer.parseInt(yearUser);

                        randomDay = random.nextInt(30)+1 ;
                        randomMonth = random.nextInt(11) +1;
                        randomYear = random.nextInt(2) +yearToGet;

                        if (randomDay == dayToGet &&
                                randomMonth == monthToGet &&
                                randomYear == yearToGet) {
                            trialCount.setTextColor(getResources().getColor(R.color.colorAccent));
                            Toast.makeText(MainActivity.this, " correct", Toast.LENGTH_LONG).show();
                        }

                        trialCountNumber++;

                        dayRandomNumber.setText(String.valueOf(randomDay));
                        monthRandomNumber.setText(String.valueOf(randomMonth));
                        yearRandomNumber.setText(String.valueOf(randomYear));
                        trialCount.setText(String.valueOf(trialCountNumber));
                    }else{
                        Toast.makeText(MainActivity.this," Please set your date data",Toast.LENGTH_LONG).show();
                    }

                }
            }
        });

    }



}
