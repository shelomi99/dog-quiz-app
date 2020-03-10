package com.shelomi.dogquiz;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import static com.shelomi.dogquiz.MainActivity.isSwitchChecked;
public class IdentifyBreedActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final String LOG_TAG = IdentifyBreedActivity.class.getSimpleName();
    public static HashMap<String, List<String>> dogBreedMap;
    private static final long START_TIME_IN_MILLS = 11000;
    private CountDownTimer countDownTimer;
    private long timeLeftInMills = START_TIME_IN_MILLS;
    private static String selectedBreed,correctAnswer,dogImageName,correctResult,wrongResult,wrongAnswer;
    private boolean timerRunning;
    private Button button;
    private Spinner mySpinner;
    private TextView textView,answerView;
    private static String buttonText = "Submit";
    private static boolean clickedSubmit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identify_breed);

        setTimer();
        Intent intent = getIntent();
        dogBreedMap = (HashMap<String, List<String>>) intent.getSerializableExtra("map");


        Random randomNumber = new Random();
        // to get a random image based on the image file name
        int randDogImage = randomNumber.nextInt(101) + 1;
        dogImageName = "img" + randDogImage;
        // randomly setting an image to an ImageView
        ImageView imgView = findViewById(R.id.dog_breed);
        int resource_id = getResources().getIdentifier(dogImageName, "drawable", getPackageName());
        imgView.setImageResource(resource_id);
        Log.d(LOG_TAG, "Clicked image is " + dogImageName);

        //create the spinner
        Spinner spinner = findViewById(R.id.dog_breed_spinner);

        if (spinner != null) {
            spinner.setOnItemSelectedListener(this);
        }
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.dog_name_array, android.R.layout.simple_spinner_item);
        // Specify the layout when the list of options appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        if (spinner != null) {
            spinner.setAdapter(adapter);
        }
    }
    // setting the timer
    public void setTimer(){
        // checking if the switch is enabled
        // referencing --> URL - https://www.youtube.com/watch?v=MDuGwI6P-X8&t=654s
        if(isSwitchChecked){
            Log.d(LOG_TAG, "Hard level is chosen ");
            textView = (TextView)findViewById(R.id.timer);
            textView.setVisibility(View.VISIBLE);
            countDownTimer = new CountDownTimer(timeLeftInMills,1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    long secondsLeft = millisUntilFinished / 1000;
                    textView.setText("" + secondsLeft);
                    if (secondsLeft<6){
                        textView.setTextColor(Color.rgb(215, 15, 0));
                    }
                }
                @Override
                public void onFinish() {
                    textView.setText("Game over");
                    textView.setTextSize(35);
                    submitButton();
                }

            }.start();
            timerRunning = true;
        }
        // end of referencing code
        else {
            Log.d(LOG_TAG, "Easy level is chosen ");
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String spinnerLabel = parent.getItemAtPosition(position).toString();
        Log.d(LOG_TAG, "Chosen breed is " + spinnerLabel);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Log.d(LOG_TAG, "No item is selected");
    }
    // method to get the key of the corresponding dog image value
    public static  <K, V> K getKey(Map<K, V> map, V value) {
        for (Map.Entry<K, V> entry : map.entrySet()) {
            if (value.equals(entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }

    public void onSubmitButtonClicked(View view) {
        submitButton();
    }
    public void submitButton(){
        mySpinner = (Spinner) findViewById(R.id.dog_breed_spinner);
        selectedBreed = mySpinner.getSelectedItem().toString();
        textView = (TextView) findViewById(R.id.test_result);
        answerView = (TextView) findViewById(R.id.correct_breed_name);
        textView.setVisibility(View.VISIBLE);

        // to iterate through the list values of the hashMap
        for (List mapValue : dogBreedMap.values()) {
            // to iterate through the specific array of the shown dog image breed
            for (int i = 0; i < mapValue.size(); i++) {
                // checking if the name of the image is equal to the array element
                if (mapValue.get(i).equals(dogImageName)) {
                    correctAnswer = getKey(dogBreedMap, mapValue);
                    Log.d(LOG_TAG, "Correct breed is "+ correctAnswer);
                    // checking if the given answer is equal to the correct breed name
                    System.out.println(selectedBreed);
                    System.out.println(correctAnswer);
                    if (selectedBreed.equals(correctAnswer)) {
                        textView = (TextView) findViewById(R.id.test_result);
                        textView.setTextColor(Color.rgb(30, 130, 0));
                        correctResult = " Your answer is Correct";
                        textView.setText(correctResult);
                    } else {
                        textView.setTextColor(Color.rgb(215, 15, 0));
                        wrongAnswer = "Answer is Wrong";
                        textView.setText(wrongAnswer);
                        answerView = (TextView) findViewById(R.id.correct_breed_name);
                        wrongResult = " Correct breed is " + correctAnswer;
                        answerView.setText(wrongResult);
                        answerView.setVisibility(View.VISIBLE);
                    }
                }
            }
        }
        //to disable the spinner after a value is selected
        mySpinner.setEnabled(false);
        Button submitButton = (Button) findViewById(R.id.button_submit);
        submitButton.setText("Next");
        buttonText = "Next";
        if (isSwitchChecked) {
            // to pause the timer
            countDownTimer.cancel();
        }
        submitButton.setBackgroundColor(Color.rgb(36, 26, 126));
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickedSubmit = true;
                finish();
                openNewActivity();
            }
        });
    }

    public void openNewActivity() {
        clickedSubmit = false;
        //creating the intent object
        Intent identifyBreedIntent = new Intent(this, IdentifyBreedActivity.class);
        //putting extras into the intent
        identifyBreedIntent.putExtra("map", dogBreedMap);
        startActivity(identifyBreedIntent);
    }
// to save restore variables when screen is rotated
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("dogName", dogImageName);
        outState.putString("selectedBreed",selectedBreed);
        outState.putString("correctAnswer",correctAnswer);
        outState.putString("correctResult",correctResult);
        outState.putString("wrongAnswer",wrongAnswer);
        outState.putString("wrongResult",wrongResult);
        outState.putString("submitText", buttonText);
        outState.putBoolean("clickedSubmit", clickedSubmit);

    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        ImageView imgView = findViewById(R.id.dog_breed);
        textView = (TextView) findViewById(R.id.test_result);
        answerView = (TextView) findViewById(R.id.correct_breed_name);
        mySpinner = (Spinner) findViewById(R.id.dog_breed_spinner);
        dogImageName = savedInstanceState.getString("dogName",dogImageName  );
        selectedBreed = savedInstanceState.getString("selectedBreed",selectedBreed);
        correctAnswer = savedInstanceState.getString("correctAnswer",correctAnswer);
        correctResult = savedInstanceState.getString("correctResult",correctResult);
        wrongAnswer = savedInstanceState.getString("wrongAnswer",wrongAnswer);
        wrongResult = savedInstanceState.getString("wrongResult",wrongResult);
        buttonText = savedInstanceState.getString("submitText", buttonText);
        clickedSubmit = savedInstanceState.getBoolean("clickedSubmit",clickedSubmit);
        Button submitButton = (Button) findViewById(R.id.button_submit);
        System.out.println(buttonText);
        setTimer();
        if (buttonText.equals("Submit")){
            submitButton.setText("Submit");
        }else if (clickedSubmit){
            submitButton.setText("Next");
            mySpinner.setEnabled(false);
            submitButton.setBackgroundColor(Color.rgb(36, 26, 126));
            for (List mapValue : dogBreedMap.values()) {
                // to iterate through the specific array of the shown dog image breed
                for (int i = 0; i < mapValue.size(); i++) {
                    // checking if the name of the image is equal to the array element
                    if (mapValue.get(i).equals(dogImageName)) {
                        correctAnswer = getKey(dogBreedMap, mapValue);
                        Log.d(LOG_TAG, "Correct breed is "+ correctAnswer);
                        // checking if the given answer is equal to the correct breed name
                        String spinnerHint = "Choose the breed";
                        if (selectedBreed != null) {
                            if (!selectedBreed.equals(spinnerHint) && selectedBreed.equals(correctAnswer) ) {
                                textView = (TextView) findViewById(R.id.test_result);
                                textView.setTextColor(Color.rgb(30, 130, 0));
                                correctResult = " Your answer is Correct";
                                textView.setText(correctResult);
                            } else {
                                textView.setTextColor(Color.rgb(215, 15, 0));
                                wrongAnswer = "Answer is Wrong";
                                textView.setText(wrongAnswer);
                                answerView = (TextView) findViewById(R.id.correct_breed_name);
                                wrongResult = " Correct breed is " + correctAnswer;
                                answerView.setText(wrongResult);
                                answerView.setVisibility(View.VISIBLE);
                                textView.setVisibility(View.VISIBLE);

                            }
                        }
                    }
                }
                submitButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                        openNewActivity();

                    }
                });
            }
            clickedSubmit = false;
        }
        int resource_id = getResources().getIdentifier(dogImageName, "drawable", getPackageName());
        imgView.setImageResource(resource_id);


    }

}

