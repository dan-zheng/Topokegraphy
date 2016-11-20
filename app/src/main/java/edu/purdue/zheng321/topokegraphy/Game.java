package edu.purdue.zheng321.topokegraphy;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Vibrator;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class Game {
    // Button for submitting guess
    Button submitGuess;
    // List of ImageView guess options
    List<ImageButton> options;
    // Spinner for game mode
    Spinner spinner;
    // Audio players
    MediaPlayer[] audio;
    // Vibrator
    Vibrator vibrator;
    // Context
    Context context;

    // Game score
    int score;

    /**
     * Constructor. Initializes the instance variables.
     */
    public Game(Button submitGuess, List<ImageButton> options, Spinner spinner,
                MediaPlayer[] audio, Vibrator vibrator, Context context) {
        this.submitGuess = submitGuess;
        this.options = options;
        this.spinner = spinner;
        this.audio = audio;
        this.vibrator = vibrator;
        this.context = context;
        this.score = 0;
    }

    public Game(Button submitGuess, List<ImageButton> options, Context context) {
        this.submitGuess = submitGuess;
        this.options = options;
        this.context = context;
        this.score = 0;
    }

/*
    public void changeColor(final char player) {
        final char otherPlayer = player == 'O' ? 'X' : 'O';
        final int playerColor = player == 'O' ? colorO : colorX;
        final int otherPlayerColor = otherPlayer == 'X' ? colorX : colorO;
        tempColor = -1;
        final List<CharSequence> temp = new ArrayList<>();
        for (HashMap.Entry<String, Integer> entry : colorsMap.entrySet()) {
            if (entry.getValue() != otherPlayerColor && entry.getValue() != playerColor) {
                System.out.println(entry.getKey());
                temp.add(entry.getKey());
            }
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Select a new color for \'" + player + "\': ")
                .setSingleChoiceItems(temp.toArray(new CharSequence[temp.size()]), -1,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                tempColor = colorsMap.get(temp.get(which));
                            }
                        })
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        if (tempColor != -1) {
                            if (player == 'O') {
                                if (tempColor != colorX) {
                                    colorO = tempColor;
                                } else {
                                    Toast.makeText(context, "Invalid color: same as 'X'.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            } else if (player == 'X') {
                                if (tempColor != colorX) {
                                    colorX = tempColor;
                                } else {
                                    Toast.makeText(context, "Invalid color: same as 'O'.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                            updateColors();
                        }
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
        builder.show();
    }

    public void showWinner(String winner) {
        String winMessage = "The winner is: " + winner + "!";
        textView.setText(winMessage);
        Toast.makeText(context, winMessage, Toast.LENGTH_SHORT).show();
        vibrator.vibrate(500);

        // Make dialog with video
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();

        String uriPath = "";
        if (winner.equals("X")) {
            uriPath = "android.resource://" + context.getPackageName() + "/" + R.raw.x;
        } else if (winner.equals("O")) {
            uriPath = "android.resource://" + context.getPackageName() + "/" + R.raw.o;
        }

        dialog.getWindow().setFormat(PixelFormat.TRANSLUCENT);
        dialog.getWindow().setDimAmount(0.3f);

        VideoView videoView = new VideoView(context);
        videoView.setVideoURI(Uri.parse(uriPath));
        dialog.setContentView(videoView);
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                dialog.dismiss();
            }
        });
        videoView.seekTo(0);
        videoView.start();

        disableButtons();
    }
    */

    public void stopAudio() {
        for (MediaPlayer m : audio) {
            if (m.isPlaying()) {
                m.pause();
            }
        }
    }

    /*
    public void setNewGameText(String mode) {
        Toast.makeText(context, "A new " + mode + " game.", Toast.LENGTH_SHORT).show();
        textView.setText("A new game has been started: " + mode + ".");
        gameMode.setText("Game mode: " + mode);
    }

    @Override
    public void disableButtons() {
        for (ImageButton btn : options) {
            btn.setEnabled(false);
            btn.setBackgroundResource(R.drawable.buttonDisabled);
        }
        submitGuess.setEnabled(false);
        submitGuess.setBackgroundResource(R.drawable.buttonDisabled);
    }

    @Override
    public void gameOver() {
        disableButtons();
        String gameOverMessage = "It's a tie.";
        textView.setText(gameOverMessage);
        Toast.makeText(context, gameOverMessage, Toast.LENGTH_SHORT).show();
        audio[1].seekTo(0);
        audio[1].start();
    }*/
}