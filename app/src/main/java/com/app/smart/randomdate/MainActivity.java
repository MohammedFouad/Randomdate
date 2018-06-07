package com.app.smart.randomdate;

import android.nfc.Tag;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.MobileAds;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Random;

import static java.lang.Math.random;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    TextView dayRandomNumber, monthRandomNumber, yearRandomNumber, trialCount;
    Button generateRandomNumber;
    EditText dayEnteredNumber, monthEnteredNumber, yearEnteredNumber;
    CheckBox easyCheckBox, mediumCheckBox, hardCheckBox;

    int randomDay, randomMonth, randomYear, trialCountNumber;
    int dayToGet, monthToGet, yearToGet;
    String dayUser, monthUser, yearUser;

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

        //initial value for counter
        trialCountNumber = 0;
        generateRandomNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //check validated data entry
                if (!validateEntryData()) {
                    return;
                }


                getEntryDate();

                if (easyCheckBox.isChecked()) {

                    randomDay = random.nextInt(30) + 1;
                    randomMonth = random.nextInt(1) + monthToGet;
                    randomYear = random.nextInt(1) + yearToGet;

                    if (!randomEqualEntry()) {
                        Toast.makeText(MainActivity.this, " well done ", Toast.LENGTH_LONG).show();
                        trialCountNumber = 0;
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
                    }
                    displayRandomDate();
                    trialCountNumber++;
                }
            }
        });

    }
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

    private boolean randomEqualEntry(){
        boolean result = true;
        if (randomDay == dayToGet &&
                randomMonth == monthToGet &&
                randomYear == yearToGet){

            result = false;

        }
      return result;
    }

    private void displayRandomDate() {

        dayRandomNumber.setText(String.valueOf(randomDay));
        monthRandomNumber.setText(String.valueOf(randomMonth));
        yearRandomNumber.setText(String.valueOf(randomYear));
        trialCount.setText(String.valueOf(trialCountNumber));
    }
}
