package edu.purdue.zheng321.topokegraphy;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import co.tanvas.haptics.service.app.*;
import co.tanvas.haptics.service.adapter.*;
import co.tanvas.haptics.service.model.*;
import co.tanvas.haptics.service.util.Log;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MyActivity";
    private static final int MAX_OPTIONS = 3;

    private HapticServiceAdapter serviceAdapter;
    private HapticView mHapticView;
    private HapticTexture mHapticTexture;
    private HapticMaterial mHapticMaterial;
    private HapticSprite mHapticSprite;

    // Random number generator
    Random rand;

    // Button for submitting guess
    Button submitGuess;
    // List of ImageView guess options
    List<ImageButton> options;
    // List of texture resource ids
    int[] textures;
    List<Integer> oldTextures;
    // List of location image resource ids
    List<Integer> locations;
    List<Integer> oldLocations;
    // Store correct guess
    int answer;
    // Spinner for game mode
    Spinner spinner;
    // Audio players
    MediaPlayer[] audio;
    // Vibrator
    Vibrator vibrator;
    // Context
    Context context;
    // ImageView for topographic map
    private ImageView map;

    private boolean isTopographyShown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize haptics
        initHaptics();

        submitGuess = (Button) findViewById(R.id.submitGuess);
        map = (ImageView) findViewById(R.id.map);
        options = new ArrayList<>();
        options.add((ImageButton) findViewById(R.id.option1));
        options.add((ImageButton) findViewById(R.id.option2));
        options.add((ImageButton) findViewById(R.id.option3));

        this.locations = new LinkedList<>();
        locations.add(R.drawable.location1);
        locations.add(R.drawable.location2);
        locations.add(R.drawable.location3);

        this.textures = new int[]{
                R.drawable.texture1,
                R.drawable.texture2,
                R.drawable.texture3
        };

        rand = new Random();

		/*MediaPlayer audioWin = MediaPlayer.create(MainActivity.this, R.raw.win);
        MediaPlayer audioLose = MediaPlayer.create(MainActivity.this, R.raw.lose);
		MediaPlayer[] audio = {audioWin, audioLose};*/

        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        isTopographyShown = false;
    }

    public void initHaptics() {
        try {
            // Get the service adapter
            serviceAdapter = HapticApplication.getHapticServiceAdapter();
            // Create a haptic view and activate it
            mHapticView = HapticView.create(serviceAdapter);
            mHapticView.activate();
            // Set the orientation of the haptic view
            Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
            int rotation = display.getRotation();
            HapticView.Orientation orientation = HapticView.getOrientationFromAndroidDisplayRotation(rotation);

            mHapticView.setOrientation(orientation);
            // Retrieve texture data from the bitmap
            Bitmap hapticBitmap = BitmapFactory.decodeResource(getResources(),
                    R.drawable.texture2);
            byte[] textureData = HapticTexture.createTextureDataFromBitmap(hapticBitmap);
            // Create a haptic texture with the retrieved texture data
            mHapticTexture = HapticTexture.create(serviceAdapter);
            int textureDataWidth = hapticBitmap.getRowBytes() / 4; // 4 channels, i.e., ARGB
            int textureDataHeight = hapticBitmap.getHeight();
            System.out.printf("width: %d, height: %d\n", textureDataWidth, textureDataHeight);
            Log.i(this.getApplicationContext(), TAG, "width: " + textureDataWidth + ", height: " + textureDataHeight);
            mHapticTexture.setSize(textureDataWidth, textureDataHeight);
            mHapticTexture.setData(textureData);
            // Create a haptic material with the created haptic texture
            mHapticMaterial = HapticMaterial.create(serviceAdapter);
            mHapticMaterial.setTexture(0, mHapticTexture);
            // Create a haptic sprite with the haptic material
            mHapticSprite = HapticSprite.create(serviceAdapter);
            mHapticSprite.setMaterial(mHapticMaterial);
            // Add the haptic sprite to the haptic view
            mHapticView.addSprite(mHapticSprite);
        } catch (Exception e) {
            //Log.e(null, e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        // The activity is gaining focus
        if (hasFocus) {
            try {
                // Set the size and position of the haptic sprite to correspond to the view we created
                View map = findViewById(R.id.map);
                int[] location = new int[2];
                map.getLocationOnScreen(location);
                mHapticSprite.setSize(map.getWidth(), map.getHeight());
                mHapticSprite.setPosition(location[0], location[1]);
            } catch (Exception e) {
                //Log.e(null, e.toString());
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            mHapticView.deactivate();
        } catch (Exception e) {
            //Log.e(null, e.toString());
            e.printStackTrace();
        }
    }

    public void buttonPress(View v) {
        switch (v.getId()) {
            case R.id.submitGuess: {
                /*
                if (!isTopographyShown) {
                    map.setImageResource(R.drawable.texture2);
                } else {
                    map.setImageResource(0);
                    map.setBackgroundColor(Color.parseColor("#000000"));
                }
                isTopographyShown = !isTopographyShown;
                */
                
                break;
            }
        }
    }

    public void setNewLevel() {
        LinkedList<Integer> temp = new LinkedList<>();
        for (int i = 0; i < MAX_OPTIONS; i++) {
            if (locations.size() == 0) {
                locations.addAll(oldLocations);
                oldLocations.clear();
                Collections.sort(locations);
            }
            Integer removedLocation = locations.remove(0);
            temp.add(removedLocation);
        }
        options.get(0).setImageResource(temp.get(0));
        options.get(1).setImageResource(temp.get(1));
        options.get(2).setImageResource(temp.get(2));


        oldLocations.addAll(temp);
        temp.clear();

        answer = rand.nextInt(3);
        setHaptics(answer);
    }

    public void setHaptics(Integer input) {
        try {
            int texture = textures[input];
            Bitmap hapticBitmap = BitmapFactory.decodeResource(getResources(), texture);
            byte[] textureData = HapticTexture.createTextureDataFromBitmap(hapticBitmap);
            // Create a haptic texture with the retrieved texture data
            mHapticTexture = HapticTexture.create(serviceAdapter);
            int textureDataWidth = hapticBitmap.getRowBytes() / 4; // 4 channels, i.e., ARGB
            int textureDataHeight = hapticBitmap.getHeight();
            System.out.printf("width: %d, height: %d\n", textureDataWidth, textureDataHeight);
            co.tanvas.haptics.service.util.Log.i(this.getApplicationContext(), TAG, "width: " + textureDataWidth + ", height: " + textureDataHeight);
            mHapticTexture.setSize(textureDataWidth, textureDataHeight);
            mHapticTexture.setData(textureData);
            // Create a haptic material with the created haptic texture
            mHapticMaterial = HapticMaterial.create(serviceAdapter);
            mHapticMaterial.setTexture(0, mHapticTexture);
            // Create a haptic sprite with the haptic material
            mHapticSprite = HapticSprite.create(serviceAdapter);
            mHapticSprite.setMaterial(mHapticMaterial);
            // Add the haptic sprite to the haptic view
            mHapticView.addSprite(mHapticSprite);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
