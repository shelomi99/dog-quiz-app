package com.shelomi.dogquiz;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    // create map to store dog breeds
    public static HashMap<String, List<String>> dogsMap = new HashMap<>();
    public static Boolean isSwitchChecked = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // creating a list and storing  values
        List<String> beagle = Arrays.asList( "img1", "img2", "img3","img4","img5","img6", "img7", "img8","img9","img10");
        List<String> blenheimSpaniel = Arrays.asList( "img11", "img12", "img13","img14","img15","img16", "img17", "img18","img19","img20");
        List<String> dandieDinmont = Arrays.asList( "img21", "img22", "img23","img24","img25","img26", "img27", "img28","img29","img30");
        List<String> germanShepherd = Arrays.asList( "img31", "img32", "img33","img34","img35","img36","img37", "img38", "img39","img40");
        List<String> goldenRetriever = Arrays.asList( "img41", "img42", "img43","img44","img45","img46", "img47", "img48","img49","img50");
        List<String> norfolkTerrier = Arrays.asList( "img51", "img52", "img53","img54","img55","img56", "img57", "img58","img59","img60");
        List<String> pug = Arrays.asList( "img61", "img62", "img63","img64","img65","img66", "img67", "img68","img69","img70");
        List<String> samoyed = Arrays.asList( "img71", "img72", "img73","img74","img75","img76", "img77", "img78","img79","img80");
        List<String> siberianHusky = Arrays.asList( "img81", "img82", "img83","img84","img85","img86", "img87", "img88","img89","img90");
        List<String> toyPoodle = Arrays.asList( "img91", "img92", "img93","img94","img95","img96", "img97", "img98","img99","img100");
        List<String> yorkshireTerrier = Arrays.asList( "img101", "img102", "img103","img104","img105","img106", "img107", "img108","img109","img110");

        // Adding breed name with the corresponding breed images
        dogsMap.put("Beagle", beagle);
        dogsMap.put("Blenheim spaniel", blenheimSpaniel);
        dogsMap.put("Dandie dinmont", dandieDinmont);
        dogsMap.put("German shepherd", germanShepherd);
        dogsMap.put("Golden retriever", goldenRetriever);
        dogsMap.put("Norfolk terrier", norfolkTerrier);
        dogsMap.put("Pug", pug);
        dogsMap.put("Samoyed", samoyed);
        dogsMap.put("Siberian husky", siberianHusky);
        dogsMap.put("Toy poodle", toyPoodle);
        dogsMap.put("Yorkshire terrier", yorkshireTerrier);

        final Switch levelSwitch = (Switch)findViewById(R.id.switch1) ;
        levelSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            TextView levelText = (TextView) findViewById(R.id.game_level);
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    isSwitchChecked = true;
                    String hardLevel = "Hard";
                    levelText.setText(hardLevel);

                } else {
                    isSwitchChecked = false;
                    String easyLevel = "Easy";
                    levelText.setText(easyLevel);
                }
            }
        });
    }

    public void launchIdentifyBreedActivity(View view) {
        Log.d(LOG_TAG, "Identify Breed Button clicked!");
        //creating the intent object
        Intent identifyBreedIntent = new Intent(this, IdentifyBreedActivity.class);
        //putting extras into the intent
        identifyBreedIntent.putExtra("map",dogsMap);
        startActivity(identifyBreedIntent);
    }

    public void launchIdentifyDogActivity(View view) {
        Log.d(LOG_TAG, "Identify Dog Button clicked!");
        Intent identifyDogIntent = new Intent(this, IdentifyDogActivity.class);
        identifyDogIntent.putExtra("map",dogsMap);
        startActivity(identifyDogIntent);
    }

    public void launchSearchDogActivity(View view) {
        Log.d(LOG_TAG, "Search Dog Button clicked!");
        Intent searchDogIntent = new Intent(this, SearchDogActivity.class);
        searchDogIntent.putExtra("map",dogsMap);
        startActivity(searchDogIntent);
    }


}