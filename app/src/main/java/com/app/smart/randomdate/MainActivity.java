package com.app.smart.randomdate;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.nfc.Tag;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Random;

import static java.lang.Math.random;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    TextView dayRandomNumber, monthRandomNumber, yearRandomNumber, trialCount;
    Button generateRandomNumber;
    TextView dayEnteredNumber, monthEnteredNumber, yearEnteredNumber;
    CheckBox easyCheckBox, mediumCheckBox, hardCheckBox;

    int randomDay, randomMonth, randomYear, trialCountNumber;
    int dayToGet, monthToGet, yearToGet;
    String dayUser, monthUser, yearUser;

    Button resetButton;

    boolean counterReset;

    private int year, month, day;

    int point;

    Random random;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Firebase real time database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");

        random = new Random();
        //random day, month and year
        dayRandomNumber = findViewById(R.id.random_day_text_view);
        monthRandomNumber = findViewById(R.id.random_month_text_view);
        yearRandomNumber = findViewById(R.id.random_year_text_view);

        //how many users hit the luck point to get his entered date
        trialCount = findViewById(R.id.trialCount);

        //Entered user day, month and year
        dayEnteredNumber = findViewById(R.id.entered_day_text_view);
        monthEnteredNumber = findViewById(R.id.entered_month_text_view);
        yearEnteredNumber = findViewById(R.id.entered_year_text_view);

        generateRandomNumber = findViewById(R.id.generate_random_number);

        //check box declaration
        easyCheckBox = findViewById(R.id.easyCheckBox);
        mediumCheckBox = findViewById(R.id.mediumCheckBox);
        hardCheckBox = findViewById(R.id.hardCheckBox);

        resetButton = findViewById(R.id.resetCount);
        resetButton.setVisibility(resetButton.GONE);

        //initial value for counter
        trialCountNumber = 0;
        generateRandomNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //check validated data entry
                if (!validateEntryData()) {
                    return;
                }

                if(counterReset){
                    return;
                }

                getEntryDate();

                if (easyCheckBox.isChecked()) {

                    randomDay = random.nextInt(30) + 1;
                    randomMonth = random.nextInt(1) + monthToGet;
                    randomYear = random.nextInt(1) + yearToGet;

                    if (!randomEqualEntry()) {

                        YoYo.with(Techniques.Tada)
                                .duration(700)
                                .repeat(10)
                                .playOn(findViewById(R.id.trialCount));
                        trialCount.setTextColor(getResources().getColor(R.color.colorTrialCount));
                       counterReset = true;
                        resetButton.setVisibility(resetButton.VISIBLE);
                        generateRandomNumber.setVisibility(View.GONE);
                        point++;


                    }

                    displayRandomDate();
                    trialCountNumber++;

                }
                if (mediumCheckBox.isChecked()) {

                    randomDay = random.nextInt(30) + 1;
                    randomMonth = random.nextInt(2) + monthToGet;
                    randomYear = random.nextInt(1) + yearToGet;

                    if (!randomEqualEntry()) {
                        Toast.makeText(MainActivity.this, " well done ", Toast.LENGTH_LONG).show();
                        trialCountNumber = 0;
                        point = point + 10;
                    }
                    displayRandomDate();
                    trialCountNumber++;
                }
                if (hardCheckBox.isChecked()) {

                    randomDay = random.nextInt(30) + 1;
                    randomMonth = random.nextInt(11) + 1;
                    randomYear = random.nextInt(2) + yearToGet;

                    if (!randomEqualEntry()) {
                        Toast.makeText(MainActivity.this, " well done ", Toast.LENGTH_LONG).show();
                        trialCountNumber = 0;
                        point = point + 100;
                    }
                    displayRandomDate();
                    trialCountNumber++;
                }
            }
        });


        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        showDate(year, month + 1, day);

    }

    @SuppressWarnings("deprecation")
    public void setDate(View view) {
        showDialog(999);
        Toast.makeText(getApplicationContext(), "ca",
                Toast.LENGTH_SHORT)
                .show();
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this,
                    myDateListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    // TODO Auto-generated method stub
                    // arg1 = year
                    // arg2 = month
                    // arg3 = day
                    showDate(arg1, arg2 + 1, arg3);
                }
            };


    private void showDate(int year1, int month1, int day1) {

        dayEnteredNumber.setText(String.valueOf(day1));
        monthEnteredNumber.setText(String.valueOf(month1));
        yearEnteredNumber.setText(String.valueOf(year1));
    }

    // set method to countdown timer when user guess success

    // set method to reset countdown

    // set method to show points

    public void resetCounter(View view){
        counterReset = false;
        trialCountNumber = 0;

        resetButton.setVisibility(resetButton.GONE);
        generateRandomNumber.setVisibility(View.VISIBLE);
        trialCount.setTextColor(getResources().getColor(R.color.colorTrialCountContinue));
        displayRandomDate();


    }
/*
    private boolean delayTimeFinished(){
        final boolean[] isTimeFinished = {true};
    new CountDownTimer(30000, 1000) {

        public void onTick(long millisUntilFinished) {
            isTimeFinished[0] = false;
        }
        public void onFinish() {
            isTimeFinished[0] = false;
        }

    }.start();

    return isTimeFinished[0];
    }
*/
    private boolean validateEntryData() {
        boolean result = true;
        if (TextUtils.isEmpty(dayEnteredNumber.getText().toString())) {
            dayEnteredNumber.setError("Required");
            result = false;
        } else {
            dayEnteredNumber.setError(null);
        }
        if (TextUtils.isEmpty(monthEnteredNumber.getText().toString())) {
            monthEnteredNumber.setError("Required");
            result = false;
        }
        if (TextUtils.isEmpty(yearEnteredNumber.getText().toString())) {
            yearEnteredNumber.setError("Required");
            result = false;
        }
        return result;
    }

    private void getEntryDate() {
        dayUser = dayEnteredNumber.getText().toString();
        monthUser = monthEnteredNumber.getText().toString();
        yearUser = yearEnteredNumber.getText().toString();

        dayToGet = Integer.parseInt(dayUser);
        monthToGet = Integer.parseInt(monthUser);
        yearToGet = Integer.parseInt(yearUser);
    }

    private boolean randomEqualEntry() {
        boolean result = true;
        if (randomDay == dayToGet &&
                randomMonth == monthToGet &&
                randomYear == yearToGet) {

            result = false;

        }
        return result;
    }

    private void displayRandomDate() {

        YoYo.with(Techniques.Tada)
                .duration(700)
                .repeat(3)
                .playOn(findViewById(R.id.random_day_text_view));

        dayRandomNumber.setText(String.valueOf(randomDay));

        YoYo.with(Techniques.Tada)
                .duration(700)
                .repeat(3)
                .playOn(findViewById(R.id.random_month_text_view));

        monthRandomNumber.setText(String.valueOf(randomMonth));

        YoYo.with(Techniques.Tada)
                .duration(700)
                .repeat(3)
                .playOn(findViewById(R.id.random_year_text_view));

        yearRandomNumber.setText(String.valueOf(randomYear));
        trialCount.setText(String.valueOf(trialCountNumber));
    }
}
