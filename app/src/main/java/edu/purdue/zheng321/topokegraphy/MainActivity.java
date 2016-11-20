package edu.purdue.zheng321.topokegraphy;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Vibrator;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import co.tanvas.haptics.service.app.*;
import co.tanvas.haptics.service.adapter.*;
import co.tanvas.haptics.service.err.HapticServiceAdapterException;
import co.tanvas.haptics.service.err.NativeHapticObjectException;
import co.tanvas.haptics.service.model.*;
import co.tanvas.haptics.service.util.Log;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MyActivity";
    private static final int MAX_OPTIONS = 3;

    private HapticServiceAdapter serviceAdapter;
    private HapticView mHapticView;
    private HapticTexture[] mHapticTextures;
    private HapticMaterial mHapticMaterial;
    private HapticSprite mHapticSprite;

    // Random number generator
    Random rand;

    // Guess selection buttons
    ImageButton[] guessButtons;
    // Button for submitting guess
    Button submitGuess;
    // List of ImageView guess options
    List<ImageButton> options;
    List<Integer> oldTextures;
    // Array of locations
    int[] locations = new int[]{
            R.drawable.location1,
            R.drawable.location2,
            R.drawable.location3
    };
    // Array of textures
    int[] textures = new int[]{
            R.drawable.texture1,
            R.drawable.texture2,
            R.drawable.texture3
    };
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
    // Player score
    int score;

    private boolean isTopographyShown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);

            mHapticTextures = new HapticTexture[MAX_OPTIONS];
            // Initialize haptics
            initHaptics();

            setContentView(R.layout.activity_main);

            submitGuess = (Button) findViewById(R.id.submitGuess);
            guessButtons = new ImageButton[3];
            guessButtons[0] = (ImageButton) findViewById(R.id.option1);
            guessButtons[1] = (ImageButton) findViewById(R.id.option2);
            guessButtons[2] = (ImageButton) findViewById(R.id.option3);
            map = (ImageView) findViewById(R.id.map);
            options = new ArrayList<>();
            options.add((ImageButton) findViewById(R.id.option1));
            options.add((ImageButton) findViewById(R.id.option2));
            options.add((ImageButton) findViewById(R.id.option3));

            rand = new Random();

		/*MediaPlayer audioWin = MediaPlayer.create(MainActivity.this, R.raw.win);
        MediaPlayer audioLose = MediaPlayer.create(MainActivity.this, R.raw.lose);
		MediaPlayer[] audio = {audioWin, audioLose};*/

            vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

            isTopographyShown = false;

            score = 0;

            setNewLevel();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*public void initHaptics() {
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
    }*/

    public void initHaptics() {
        try {
            // Get the service adapter
            serviceAdapter = HapticApplication.getHapticServiceAdapter();
            // Create a haptic view and activate it
            if (mHapticView == null) {
                mHapticView = HapticView.create(serviceAdapter);
                mHapticView.activate();
            }
            // Set the orientation of the haptic view
            Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
            int rotation = display.getRotation();
            HapticView.Orientation orientation = HapticView.getOrientationFromAndroidDisplayRotation(rotation);

            mHapticView.setOrientation(orientation);

            for (int i = 0; i < MAX_OPTIONS; i++) {
                /*
                String url = "drawable/" + "texture" + i + ".png";
                int texture = getResources().getIdentifier(url, "drawable", getPackageName());
                */
                int texture = 0;
                if (i == 0) { texture = R.drawable.texture1; }
                if (i == 1) { texture = R.drawable.texture2; }
                if (i == 2) { texture = R.drawable.texture3; }

                Bitmap hapticBitmap = BitmapFactory.decodeResource(getResources(), texture);
                byte[] textureData = HapticTexture.createTextureDataFromBitmap(hapticBitmap);
                int textureDataWidth = hapticBitmap.getRowBytes() / 4; // 4 channels, i.e., ARGB
                int textureDataHeight = hapticBitmap.getHeight();
                mHapticTextures[i] = HapticTexture.create(serviceAdapter);
                mHapticTextures[i].setSize(textureDataWidth, textureDataHeight);
                mHapticTextures[i].setData(textureData);
            }

            mHapticMaterial = HapticMaterial.create(serviceAdapter);
            mHapticSprite = HapticSprite.create(serviceAdapter);
            mHapticSprite.setMaterial(mHapticMaterial);
            System.out.println("mHapticSprite = " + mHapticSprite);
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
                System.out.println("mHapticSprite = " + mHapticSprite);
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
        int id = v.getId();
        if (id == R.id.option1 || id == R.id.option2 || id == R.id.option3) {
            int compare = -1;
            if (id == R.id.option1) {
                compare = 0;
            }
            if (id == R.id.option2) {
                compare = 1;
            }
            if (id == R.id.option3) {
                compare = 2;
            }
            if (answer == compare) {
                score++;
                Toast.makeText(MainActivity.this, "Correct answer! :D Score = " + score, Toast.LENGTH_SHORT).show();
                System.out.println("Success. compare: " + compare + ", answer: " + answer);
            } else {
                Toast.makeText(MainActivity.this, "Incorrect answer. :(", Toast.LENGTH_SHORT).show();
                System.out.println("Fail. compare: " + compare + ", answer: " + answer);
                failDialog();
            }
            setNewLevel();
        } else {
            switch (v.getId()) {
                case R.id.submitGuess: {
                    if (!isTopographyShown) {
                        map.setImageResource(textures[answer]);
                    } else {
                        map.setImageResource(0);
                        map.setBackgroundColor(Color.parseColor("#000000"));
                    }
                    isTopographyShown = !isTopographyShown;
                    break;
                }
            }
        }
    }

    public void setNewLevel() {
        int[] options = getThreeRandom();
        ImageButton imageButton = null;

        for (int i = 0; i < options.length; i++) {
            if (i == 0) {
                imageButton = (ImageButton) findViewById(R.id.option1);
                imageButton.setImageResource(locations[options[i]]);
            } else if (i == 1) {
                imageButton = (ImageButton) findViewById(R.id.option2);
                imageButton.setImageResource(locations[options[i]]);
            } else if (i == 2) {
                imageButton = (ImageButton) findViewById(R.id.option3);
                imageButton.setImageResource(locations[options[i]]);
            }
            //imageButton.setImageResource(0);
        }

        int texture = rand.nextInt(3);
        answer = texture;
        System.out.println("answer = " + answer);
        setHaptics(answer);
        map.setImageResource(textures[answer]);
        //Toast.makeText(this, "New level.", Toast.LENGTH_SHORT).show();
    }

    public void setHaptics(Integer index) {
        try {
            mHapticMaterial.setTexture(0, mHapticTextures[index]);

            isTopographyShown = false;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int[] getThreeRandom() {
        final Set<Integer> intSet = new HashSet<>();
        while (intSet.size() < 3) {
            intSet.add(rand.nextInt(MAX_OPTIONS));
        }
        final int[] ints = new int[intSet.size()];
        final Iterator<Integer> iter = intSet.iterator();
        for (int i = 0; iter.hasNext(); ++i) {
            ints[i] = iter.next();
        }
        return ints;
    }

    public void failDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater factory = LayoutInflater.from(MainActivity.this);
        final ImageView imageView = (ImageView) factory.inflate(R.layout.imageview, null);
        imageView.setImageResource(locations[answer]);
        builder.setView(imageView);
        builder.setTitle("Wrong answer. :( Here was the correct option.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
        builder.show();
    }
}
