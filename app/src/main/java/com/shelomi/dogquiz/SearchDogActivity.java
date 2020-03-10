package com.shelomi.dogquiz;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class SearchDogActivity extends AppCompatActivity {

    private static final String LOG_TAG = SearchDogActivity.class.getSimpleName();
    public static HashMap<String, List<String>> dogBreedMap;
    private AutoCompleteTextView dogBreedText;
    private static final String[] DOG_BREEDS = new String[]{"Beagle", "Blenheim spaniel", "Dandie dinmont", "German shepherd", "Golden retriever", "Norfolk terrier", "Pug", "Samoyed", "Siberian husky", "Toy poodle", "Yorkshire terrier"};
    private int index=0;
    private static int[] resourceIdArray;
    private static List searchedDogArray;
    private static String keyName,message;
    TextView textView;
    Button stopButton,submitButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_dog);

        Intent intent = getIntent();
        dogBreedMap = (HashMap<String, List<String>>) intent.getSerializableExtra("map");
        dogBreedText = findViewById(R.id.breed_name);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.select_dialog_item, DOG_BREEDS);
        // set the autocomplete suggestions to the search bar
        dogBreedText.setAdapter(adapter);


}
    public void onSearchDogButtonClicked(View view) {
        stopButton = (Button) findViewById(R.id.button_stop);
        submitButton = (Button) findViewById(R.id.button);
        Log.d(LOG_TAG, "Submit button clicked!");
        submitButton.setVisibility(View.INVISIBLE);
        stopButton.setBackgroundColor(getResources().getColor(R.color.red));
        message = dogBreedText.getText().toString().toLowerCase();
        for (String keyName : dogBreedMap.keySet()) {
            // checking if the searched breed type is available in the hash map
            if (keyName.toLowerCase().equals(message)) {
                System.out.println(keyName);
                searchedDogArray = dogBreedMap.get(keyName);
                // to not get the images in a sequential order
                Collections.shuffle(searchedDogArray);
                System.out.println(searchedDogArray);
                resourceIdArray = new int[searchedDogArray.size()];
                for (int j = 0; j < searchedDogArray.size(); j++) {
                    resourceIdArray[j] = getResources().getIdentifier((String) searchedDogArray.get(j), "drawable", getPackageName());
                }
                ShowImageGallery();
            }
            else {
                Log.d(LOG_TAG, "Searched dog breed not available ");
            }
        }
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                openNewActivity();
            }
        });
    }
    public void openNewActivity() {
        Log.d(LOG_TAG, "Stop button clicked!");
        //creating the intent object
        Intent searchDogIntent = new Intent(this, SearchDogActivity.class);
        //putting extras into the intent
        searchDogIntent.putExtra("map", dogBreedMap);
        startActivity(searchDogIntent);
    }

    public void ShowImageGallery() {
        final ImageView dogImage;
        final Timer timer;
        final Handler mHandler = new Handler();
        dogImage = (ImageView)findViewById(R.id.dog_slide);
        final Runnable start = new Runnable() {
            public void run() {
                dogImage.setImageResource(resourceIdArray[index%resourceIdArray.length]);
                index++;
                Animation fadein = AnimationUtils.loadAnimation(SearchDogActivity.this, R.anim.fadein);
                dogImage.startAnimation(fadein);
            }
        };
        int delay = 1000;
        int period = 5000;
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                mHandler.post(start);
            }
        }, delay, period);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putIntArray("resourceIdArray", resourceIdArray);
    }
    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        resourceIdArray = savedInstanceState.getIntArray("resourceIdArray");
        stopButton = (Button) findViewById(R.id.button_stop);
        submitButton = (Button) findViewById(R.id.button);
        for (String keyName : dogBreedMap.keySet()) {
            // checking if the searched breed type is available in the hash map
            if (keyName.toLowerCase().equals(message)) {
                submitButton.setVisibility(View.INVISIBLE);
                stopButton.setBackgroundColor(getResources().getColor(R.color.red));
                stopButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                        openNewActivity();
                    }
                });
                searchedDogArray = dogBreedMap.get(keyName);
                // to not get the images in a sequential order
                Collections.shuffle(searchedDogArray);
                resourceIdArray = new int[searchedDogArray.size()];
                for (int j = 0; j < searchedDogArray.size(); j++) {
                    resourceIdArray[j] = getResources().getIdentifier((String) searchedDogArray.get(j), "drawable", getPackageName());
                }
                ShowImageGallery();
            }
        }
    }
}
