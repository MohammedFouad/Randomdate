package com.app.smart.randomdate;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.nfc.Tag;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.database.ChildEventListener;
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

import static java.lang.Math.floor;
import static java.lang.Math.random;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    SharedPreferences sharedPreferences;

    private DatabaseReference mDatabase;

    TextView dayRandomNumber, monthRandomNumber, yearRandomNumber, trialCount, pointsEarned, levelDisplay;
    Button generateRandomNumber;
    TextView dayEnteredNumber, monthEnteredNumber, yearEnteredNumber;

    int randomDay, randomMonth, randomYear, trialCountNumber;
    int dayToGet, monthToGet, yearToGet;
    String dayUser, monthUser, yearUser;

    Button resetButton;

    boolean counterReset;

    private int year, month, day;

    int point;

    int selectedLevel;

    Toast toast;

    Random random;
    public static final String MyPREFERENCES = "MyPrefs";

    public static final String Pointsgranted = "pointKey";

    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(this.getResources().getString(R.string.admob_interstitial_id));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());


        sharedPreferences = getSharedPreferences(MyPREFERENCES,
                Context.MODE_PRIVATE);

        if (sharedPreferences.contains(Pointsgranted)) {
        pointsEarned.setText(sharedPreferences.getString(Pointsgranted, ""));
        }

        // custom Toast
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.custom_toast,
                (ViewGroup) findViewById(R.id.relativeLayout1));

        //    text = findViewById(R.id.textView1);

        toast = new Toast(this);
        toast.setView(view);
        toast.setDuration(Toast.LENGTH_SHORT);


        pointsEarned = findViewById(R.id.pointsTextView);

        mDatabase = FirebaseDatabase.getInstance().getReference();

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

        resetButton = findViewById(R.id.resetCount);
        resetButton.setVisibility(resetButton.GONE);

        levelDisplay = findViewById(R.id.level);

        //initial value for counter
        startRandom();

        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        showDate(year, month + 1, day);

    }

    public void save() {

        SharedPreferences.Editor editor = sharedPreferences.edit();
        String pointsToString = String.valueOf(point);

        editor.putString(Pointsgranted, pointsToString );
        editor.commit();
        Toast.makeText(MainActivity.this," inside save method  "
                + Pointsgranted + "   "+ pointsToString, Toast.LENGTH_SHORT).show();

        // print saved points to log
     //Log.d(pointsGranted, " points");
    }

    /*
    public void get() {

        sharedPreferences = getSharedPreferences(mypreference,
                Context.MODE_PRIVATE);

sharedPreferences.getInt(pointsGranted,point);
        pointsEarned.setText(pointsGranted);

    }

*/
    // Button to generate random numbers
    public void startRandom() {
        generateRandomNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // not necessary to choose level
                if (selectedLevel == 0) {
                    selectedLevel = 1;
                    levelDisplay.setText("Level 1");
                }


                if (trialCount == null) {
                    trialCountNumber = 0;
                }

                if (trialCountNumber % 10 == 0) {

                    if (mInterstitialAd.isLoaded()) {
                        mInterstitialAd.show();

                        // Toast.makeText(this, " inside ad method", Toast.LENGTH_LONG).show();
                    } else {
                        Log.d("TAG", "The interstitial wasn't loaded yet.  next button");
                    }

                    mInterstitialAd.setAdListener(new AdListener() {
                        @Override
                        public void onAdClosed() {
                            // Load the next interstitial.
                            mInterstitialAd.loadAd(new AdRequest.Builder().build());
                        }
                    });


                }
                // wait until reset button is clicked to reset counter
                if (counterReset) {
                    return;
                }
                // user selected entry date from date picker
                getEntryDate();

                if (selectedLevel == 1) {

                    // change day only for the first level
                    randomDay = random.nextInt(2) + 28;
                    // Keep month and year unchanged value for the first level
                    randomMonth = random.nextInt(1) + monthToGet;
                    randomYear = random.nextInt(1) + yearToGet;

                    if (!randomEqualEntry()) {

                        trialCount.setTextColor(getResources().getColor(R.color.light_green));
                        toast.show();
                        counterReset = true;
                        resetButton.setVisibility(resetButton.VISIBLE);
                        generateRandomNumber.setVisibility(View.GONE);
                        point++;
                        pointsEarned.setText(String.valueOf(point));
                        writeToDatabase(point);
                        save();
                       // Toast.makeText(MainActivity.this," after save mathod", Toast.LENGTH_SHORT).show();

                        // mDatabase.child("points").child("point").setValue(point);
                    }

                    displayRandomDate();
                    trialCountNumber++;

                }
                if (selectedLevel == 2) {

                    randomDay = random.nextInt(30) + 1;
                    randomMonth = random.nextInt(11) + 1;
                    randomYear = random.nextInt(1) + yearToGet;

                    if (!randomEqualEntry()) {
                        Toast.makeText(MainActivity.this, " well done ", Toast.LENGTH_LONG).show();
                        trialCountNumber = 0;
                        point = point + 10;
                        pointsEarned.setText(String.valueOf(point));
                        mDatabase.child("points").child("point").setValue(point);
                    }
                    displayRandomDate();
                    trialCountNumber++;
                }
                if (selectedLevel == 3) {

                    randomDay = random.nextInt(30) + 1;
                    randomMonth = random.nextInt(11) + 1;
                    randomYear = random.nextInt(118) + 1900;

                    if (!randomEqualEntry()) {
                        Toast.makeText(MainActivity.this, " well done ", Toast.LENGTH_LONG).show();
                        trialCountNumber = 0;
                        point = point + 100;
                        pointsEarned.setText(String.valueOf(point));
                    }
                    displayRandomDate();
                    trialCountNumber++;
                }
            }
        });
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

    // set method to reset countdown

    // set method to show points


    // Reset button
    //@param counterReset is a boolean value that change to false when reset is complete

    public void resetCounter(View view) {
        counterReset = false;
        trialCountNumber = 0;

        resetButton.setVisibility(resetButton.GONE);
        generateRandomNumber.setVisibility(View.VISIBLE);
        trialCount.setTextColor(getResources().getColor(R.color.colorTrialCountContinue));
        displayRandomDate();
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

    // write to database

    private void writeToDatabase(int point) {

        mDatabase.child("points").child("points count").setValue(String.valueOf(point));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();

        switch (i) {

            case R.id.level_one:

                selectedLevel = 1;
                levelDisplay.setText("Level 1");

                break;

            case R.id.level_two:

                selectedLevel = 2;
                levelDisplay.setText("Level 2");

                break;

            case R.id.level_three:
                selectedLevel = 3;
                levelDisplay.setText("Level 3");

                break;

            case R.id.reset_counter:
                point = 0;
                pointsEarned.setText(String.valueOf(point));
                writeToDatabase(point);
                break;

            case R.id.action_share:

                Intent myIntent = new Intent(Intent.ACTION_SEND);
                myIntent.setType("text/plain");
                myIntent.putExtra(Intent.EXTRA_TEXT, Uri.parse("https://play.google.com/store/apps/details?id=com.app.smart.randomdate") + "  " + getString(R.string.share_message));
                if (myIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(Intent.createChooser(myIntent, "share "));
                }

            default:
                selectedLevel = 1;

                break;

        }

        return true;
    }

    /*
    @Override
    protected void onStart() {

//        if ((Integer.parseInt(pointsGranted)) >0){
//            get();
//        }


        DatabaseReference scoresRef = FirebaseDatabase.getInstance().getReference("points");
        scoresRef.orderByValue().limitToLast(4).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                //  pointsEarned.setText(String.valueOf(dataSnapshot.getValue()));
                // String test = String.valueOf(dataSnapshot.getValue());

                //point = Integer.parseInt(test);

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        super.onStart();
    }
*/

}

