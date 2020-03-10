package com.shelomi.dogquiz;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import static com.shelomi.dogquiz.MainActivity.isSwitchChecked;

public class IdentifyDogActivity extends AppCompatActivity {
    public static HashMap<String, List<String>> dogBreedMap;
    private static final String LOG_TAG = IdentifyBreedActivity.class.getSimpleName();
    private static String dogToGuess;
    private static String clickedImage,breedToGuess, correctResult,incorrectResult;
    private static String randImageNameOne;
    private static String randImageNameTwo;
    private static String randImageNameThree;
    private static final long START_TIME_IN_MILLS = 11000;
    private CountDownTimer countDownTimer;
    private long timeLeftInMills = START_TIME_IN_MILLS;
    private boolean timerRunning;
    ImageView imgView1,imgView2,imgView3;
    TextView textView,answerView;
    Button submitButton;
    Random rand = new Random();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identify_dog);

        Intent intent = getIntent();
        dogBreedMap = (HashMap<String, List<String>>) intent.getSerializableExtra("map");

        setTimer();

//     using LinkedHashSet to maintain insertion order
        Set<Integer> randNum = new LinkedHashSet<Integer>();
        while (randNum.size() < 3) {
            int next = rand.nextInt(11);
            randNum.add(next);
        }
        Integer[] randomArray = randNum.toArray(new Integer[randNum.size()]);
        //declaring the 3 random dog breed arrays
        int randomOne = randomArray[0];
        int randomTwo = randomArray[1];
        int randomThree = randomArray[2];
        Object[] key = dogBreedMap.keySet().toArray();

        // choosing random dog breed image from each array
        ArrayList randomDogBreed1 = (ArrayList) dogBreedMap.get(key[randomOne]);
        assert randomDogBreed1 != null;
        randImageNameOne = (String) randomDogBreed1.get(rand.nextInt(randomDogBreed1.size()));
        imgView1 = findViewById(R.id.rand_dog_breed_1);
        // setting resource id's for the image view
        int resource_id1 = getResources().getIdentifier(randImageNameOne, "drawable", getPackageName());
        imgView1.setImageResource(resource_id1);
        Log.d(LOG_TAG, "Shown image is " + randImageNameOne);

        // choosing random dog breed image from each array
        ArrayList randomDogBreed2 = (ArrayList) dogBreedMap.get(key[randomTwo]);
        assert randomDogBreed2 != null;
        randImageNameTwo = (String) randomDogBreed2.get(rand.nextInt(randomDogBreed2.size()));
        imgView2 = findViewById(R.id.rand_dog_breed_2);
        // setting resource id's for the image view
        int resource_id2 = getResources().getIdentifier(randImageNameTwo, "drawable", getPackageName());
        imgView2.setImageResource(resource_id2);
        Log.d(LOG_TAG, "Shown image is " + randImageNameTwo);

        // choosing random dog breed image from each array
        ArrayList randomDogBreed3 = (ArrayList) dogBreedMap.get(key[randomThree]);
        assert randomDogBreed3 != null;
        randImageNameThree = (String) randomDogBreed3.get(rand.nextInt(randomDogBreed3.size()));
        imgView3 = findViewById(R.id.rand_dog_breed_3);
        // setting resource id's for the image view
        int resource_id3 = getResources().getIdentifier(randImageNameThree, "drawable", getPackageName());
        imgView3.setImageResource(resource_id3);
        Log.d(LOG_TAG, "Shown image is " + randImageNameThree);

        // getting the name of the bree that should be guessed
        int rnd = rand.nextInt(3);
        ArrayList randomDogBreed = (ArrayList) dogBreedMap.get(key[randomArray[rnd]]);
        breedToGuess = getKey(dogBreedMap, randomDogBreed);
        TextView textView = (TextView) findViewById(R.id.breed_name);
        correctResult = " Choose the \""+ breedToGuess + "\" from the above images";
        textView.setText(correctResult);
        // checking the breed of the dog that needs to be guessed
        if (rnd == 0){
            dogToGuess = randImageNameOne;
        }else if (rnd == 1){
            dogToGuess = randImageNameTwo;
        } else{
            dogToGuess = randImageNameThree;
        }

    }
// method to set the timer
    public void setTimer(){
        if(isSwitchChecked){
            Log.d(LOG_TAG, "Hard level is chosen ");
            textView = (TextView)findViewById(R.id.timer);
            submitButton = (Button) findViewById(R.id.button_next);
            textView.setVisibility(View.VISIBLE);
            countDownTimer = new CountDownTimer(timeLeftInMills,1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    long secondsLeft = millisUntilFinished / 1000;
                    textView.setText("" + secondsLeft);
                    if (secondsLeft<6){
                        textView.setTextColor(getResources().getColor(R.color.red));
                    }
                }
                @Override
                public void onFinish() {
                    textView.setText("Game over");
                    submitButton.setVisibility(View.VISIBLE);
                    textView.setTextSize(35);
                    TextView answerView = (TextView) findViewById(R.id.identify_dog_result);
                    answerView.setVisibility(View.VISIBLE);
                    answerView.setText("Wrong");
                    answerView.setTextColor(getResources().getColor(R.color.red));
                    Button submitButton = (Button) findViewById(R.id.button_next);
                    submitButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finish();
                            openNewActivity();
                        }
                    });
                }
            }.start();
            timerRunning = true;
        }
        else {
            Log.d(LOG_TAG, "Easy level is chosen ");
        }
    }
    // method to find the key of a given value
    public static  <K, V> K getKey(Map<K, V> map, V value) {
        for (Map.Entry<K, V> entry : map.entrySet()) {
            if (value.equals(entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }


    public void onImageClickedOne(View view) {
        // checking if the selected dog breed correct
        if (isSwitchChecked) {
            countDownTimer.cancel();
        }
        clickedImage = randImageNameOne;
        disableClickButton();
        validateAnswer();
        submitButton.setVisibility(View.VISIBLE);
    }

    public void onImageClickedTwo(View view) {
        if (isSwitchChecked) {
            countDownTimer.cancel();
        }
        clickedImage = randImageNameTwo;
        disableClickButton();
        validateAnswer();
        submitButton.setVisibility(View.VISIBLE);

    }

    public void onImageClickedThree(View view) {
        if (isSwitchChecked) {
            countDownTimer.cancel();
        }
        clickedImage = randImageNameThree;
        disableClickButton();
        validateAnswer();
        submitButton.setVisibility(View.VISIBLE);


    }
    public void disableClickButton(){
        imgView1 = findViewById(R.id.rand_dog_breed_1);
        imgView1.setEnabled(false);
        imgView2 = findViewById(R.id.rand_dog_breed_2);
        imgView2.setEnabled(false);
        imgView3 = findViewById(R.id.rand_dog_breed_3);
        imgView3.setEnabled(false);

    }

    public void validateAnswer(){
        TextView answerView = (TextView) findViewById(R.id.identify_dog_result);
        submitButton = (Button) findViewById(R.id.button_next);
        answerView.setVisibility(View.VISIBLE);
        // if the guessed image is correct
        if (dogToGuess.equals(clickedImage)){
            answerView.setTextColor(Color.rgb(30, 130, 0));
            correctResult = " Your answer is Correct";
            answerView.setText(correctResult);
            answerView.setEnabled(false);
        }
        // if the guessed image is incorrect
        else{
            answerView.setTextColor(Color.rgb(215, 15, 0));
            incorrectResult = " Your answer is Incorrect";
            answerView.setText(incorrectResult);
            submitButton.setVisibility(View.VISIBLE);

        }
        // to make the images transparent after its selected
        imgView1.setAlpha((float) 0.5);
        imgView2.setAlpha((float) 0.5);
        imgView3.setAlpha((float) 0.5);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                openNewActivity();
            }
        });

    }
    public void openNewActivity() {
        //creating the intent object
        Intent identifyDogIntent = new Intent(this, IdentifyDogActivity.class);
        //putting extras into the intent
        identifyDogIntent.putExtra("map", dogBreedMap);
        startActivity(identifyDogIntent);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("randomDogOne", randImageNameOne);
        outState.putString("randomDogTwo", randImageNameTwo);
        outState.putString("randomDogThree", randImageNameThree);
        outState.putString("breedToGuess", breedToGuess);
        outState.putString("showBreedName", correctResult);
        outState.putString("dogToGuess",dogToGuess );
        outState.putString("clickedImage", clickedImage);
        outState.putString("incorrectResult",incorrectResult);
    }
    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        imgView1 = findViewById(R.id.rand_dog_breed_1);
        imgView2 = findViewById(R.id.rand_dog_breed_2);
        imgView3 = findViewById(R.id.rand_dog_breed_3);
        randImageNameOne = savedInstanceState.getString("randomDogOne",randImageNameOne  );
        randImageNameTwo = savedInstanceState.getString("randomDogTwo",randImageNameTwo  );
        randImageNameThree = savedInstanceState.getString("randomDogThree",randImageNameThree  );
        int resource_id1 = getResources().getIdentifier(randImageNameOne, "drawable", getPackageName());
        imgView1.setImageResource(resource_id1);
        int resource_id2 = getResources().getIdentifier(randImageNameTwo, "drawable", getPackageName());
        imgView2.setImageResource(resource_id2);
        int resource_id3 = getResources().getIdentifier(randImageNameThree, "drawable", getPackageName());
        imgView3.setImageResource(resource_id3);

        breedToGuess = savedInstanceState.getString("breedToGuess", breedToGuess);
        correctResult = savedInstanceState.getString("showBreedName", correctResult);
        dogToGuess = savedInstanceState.getString("dogToGuess", dogToGuess);
        clickedImage = savedInstanceState.getString("clickedImage",clickedImage);
        incorrectResult = savedInstanceState.getString("incorrectResult",incorrectResult);
        if (randImageNameOne.equals(clickedImage) || randImageNameTwo.equals(clickedImage) || randImageNameThree.equals(clickedImage)) {
            disableClickButton();
            validateAnswer();
        }
        TextView textView = (TextView) findViewById(R.id.breed_name);
        correctResult = " Choose the \""+ breedToGuess + "\" from the above images";
        textView.setText(correctResult);

    }
}
