package com.adityaamk.ballclicker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class MainActivity extends AppCompatActivity {

    ImageView ball;
    TextView counterTv, bpsTv;
    Button menuBtn, name;
    static AtomicInteger ballCounter;
    SharedPreferences sharedPreferences;
    Animation fadeIn, fadeOut;
    boolean upgradeFader = true;
    boolean upgradeFader2 = true;
    boolean upgradeFader3 = true;
    boolean upgradeFader4 = true;
    boolean upgradeFader5 = true;
    boolean upgradeFader6 = true;
    String m_Text;
    int ballspersecond = 0;
    static ConstraintLayout constraintLayout;

    ArrayList<PowerUps> powerUps, powerUps2;
    //NameDialog Name Button

    public String BALLCOUNTER_KEY = "BALLCOUNTER_KEY";
    public String UPGRADEFADER_KEY = "UPGRADEFADER_KEY";
    public String UPGRADEFADER_KEY2 = "UPGRADEFADER2_KEY";

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(BALLCOUNTER_KEY, ballCounter.get());
        //editor.putBoolean(UPGRADEFADER_KEY, upgradeFader);
        editor.apply();
    }

//    @Override
//    protected void onPostResume() {
//        super.onPostResume();
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putInt(BALLCOUNTER_KEY, ballCounter.get());
//        //editor.putBoolean(UPGRADEFADER_KEY, upgradeFader);
//        editor.apply();
//    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ball = findViewById(R.id.id_ball);
        menuBtn = findViewById(R.id.id_menuBtn);
        menuBtn.setTextColor(Color.RED);
        counterTv = findViewById(R.id.id_counter);
        bpsTv = findViewById(R.id.id_bps);
        bpsTv.setTextColor(Color.RED);
        bpsTv.setVisibility(View.INVISIBLE);
        sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        ballCounter = new AtomicInteger(sharedPreferences.getInt(BALLCOUNTER_KEY, 0));
        counterTv.setText(ballCounter+" Balls");
        counterTv.setTextColor(Color.RED);
        name = findViewById(R.id.id_name);
        name.setTextColor(Color.RED);
        name.getBackground().setAlpha(0);
        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Enter Name");

                final EditText input = new EditText(MainActivity.this);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        m_Text = input.getText().toString();
                        name.setText(m_Text + "'s Tennis Academy");
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            }
        });

        menuBtn.getBackground().setAlpha(0);
        //upgradeFader = sharedPreferences.getBoolean(UPGRADEFADER_KEY, true);
        Toast intro = Toast.makeText(this, "Click on Upgrades!", Toast.LENGTH_SHORT);
        intro.setGravity(Gravity.CENTER, 0, 0);
        intro.show();
        Toast.makeText(MainActivity.this, "There are limits on the number of upgrades!", Toast.LENGTH_SHORT).show();

        constraintLayout = findViewById(R.id.id_layout);
        constraintLayout.setBackgroundResource(R.drawable.background);
        final ScaleAnimation ballAnimation = new ScaleAnimation(1.0f, 1.5f, 1.0f, 1.5f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        ballAnimation.setDuration(250);

        Bank bank = new Bank();
        bank.start();

        powerUps2 = new ArrayList<>();
        powerUps2.add(new MainActivity.PowerUps("Tennis Academy", 160, 50000, 0, R.drawable.evert));
        powerUps2.add(new MainActivity.PowerUps("Ball Machine", 80, 32000, 0, R.drawable.ballmachine));
        powerUps2.add(new MainActivity.PowerUps("Novak Djokovic", 40, 8000, 0, R.drawable.djokovic));
        powerUps2.add(new MainActivity.PowerUps("Coach", 20, 2000, 0, R.drawable.coach));
        powerUps2.add(new MainActivity.PowerUps("Hitting Partner", 10, 500, 0, R.drawable.waw));
        powerUps2.add(new MainActivity.PowerUps("Tennis Racket", 3, 50, 0, R.drawable.tennisracket));

        if(upgradeFader||upgradeFader2||upgradeFader3||upgradeFader4||upgradeFader5||upgradeFader6) {
            UpgradeFadeIn upgradeFadeIn = new UpgradeFadeIn();
            upgradeFadeIn.start();
        }

        ball.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    ballCounter.getAndAdd(1);
                    counterTv.setText(ballCounter+" Balls");
                    TextView addingBalls = new TextView(MainActivity.this);
                    addingBalls.setId(View.generateViewId());

                    addingBalls.setText("+1");
                    addingBalls.setTypeface(Typeface.DEFAULT_BOLD);
                    addingBalls.setTextSize(22);
                    addingBalls.setTextColor(Color.RED);
                    if((int)(Math.random()*2)==0) {
                        addingBalls.setX(event.getX() + 420 + (int) (Math.random() * 100));
                    }
                    else{
                        addingBalls.setX(event.getX() + 420 - (int) (Math.random() * 100));
                    }
                    if((int)(Math.random()*2)==0) {
                        addingBalls.setY(event.getY() + 500 + (int) (Math.random() * 100));
                    }
                    else{
                        addingBalls.setY(event.getY() + 500 - (int) (Math.random() * 100));
                    }
                    ball.startAnimation(ballAnimation);
                    constraintLayout.addView(addingBalls);

                    AnimationSet plusOneAnim = new AnimationSet(true);
                    final TranslateAnimation translateAnimation = new TranslateAnimation(0,0,50,-2000);
                    translateAnimation.setDuration(3000);
                    fadeIn = new AlphaAnimation(0,1);
                    fadeIn.setInterpolator(new DecelerateInterpolator());
                    fadeIn.setDuration(500);
                    fadeOut = new AlphaAnimation(1,0);
                    fadeOut.setInterpolator(new AccelerateInterpolator());
                    fadeOut.setDuration(1750);
                    plusOneAnim.addAnimation(translateAnimation);
                    plusOneAnim.addAnimation(fadeIn);
                    plusOneAnim.addAnimation(fadeOut);
                    addingBalls.startAnimation(plusOneAnim);
                    addingBalls.setVisibility(View.INVISIBLE);

                }
                return false;
            }
        });

        menuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ActionBar.class);
                startActivity(intent);
            }
        });

    }

    public class Bank extends Thread {
        @Override
        public void run() {
            while (true) {
                powerUps = ActionBar.powerUps;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (powerUps!=null && powerUps.size() > 0) {
                            for (int i = 0; i < powerUps.size(); i++) {
                                ballCounter.getAndAdd(powerUps.get(i).getCounter() * powerUps.get(i).getBallspersecond());
                                //ballspersecond = powerUps.get(i).getCounter() * powerUps.get(i).getBallspersecond();
                                counterTv.setText(ballCounter+" Balls");
                                bpsTv.setText(ballspersecond+" Balls Per Second");
                            }
                        }
                    }
                });
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public class UpgradeFadeIn extends Thread {
        @Override
        public void run() {
            while(true) {
                if (ballCounter.get() >= powerUps2.get(5).getPrice() && upgradeFader) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                final ImageView imageView = new ImageView(MainActivity.this);
                                imageView.setId(View.generateViewId());
                                imageView.setImageResource(R.drawable.tennisracket);
                                ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(250, 250);
                                imageView.setLayoutParams(params);
                                constraintLayout.addView(imageView);

                                ConstraintSet constraintSet = new ConstraintSet();
                                constraintSet.clone(constraintLayout);

                                constraintSet.connect(imageView.getId(), ConstraintSet.TOP, constraintLayout.getId(), ConstraintSet.TOP);
                                constraintSet.connect(imageView.getId(), ConstraintSet.BOTTOM, constraintLayout.getId(), ConstraintSet.BOTTOM);
                                constraintSet.connect(imageView.getId(), ConstraintSet.LEFT, constraintLayout.getId(), ConstraintSet.LEFT);
                                constraintSet.connect(imageView.getId(), ConstraintSet.RIGHT, constraintLayout.getId(), ConstraintSet.RIGHT);

                                constraintSet.setHorizontalBias(imageView.getId(), .0f);
                                constraintSet.setVerticalBias(imageView.getId(), .75f);

                                constraintSet.applyTo(constraintLayout);

                                AnimationSet upgrade = new AnimationSet(true);
                                fadeIn = new AlphaAnimation(0, 1);
                                fadeIn.setInterpolator(new DecelerateInterpolator());
                                fadeIn.setDuration(1500);
                                upgrade.addAnimation(fadeIn);
                                imageView.startAnimation(upgrade);
                                Toast add = Toast.makeText(MainActivity.this, "Unlocked Tennis Racket Power-Up!", Toast.LENGTH_SHORT);
                                add.setGravity(Gravity.CENTER, 0, 0);
                                add.show();
                                imageView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        ViewGroup parent = (ViewGroup) imageView.getParent();
                                        parent.removeView(imageView);
                                    }
                                });
                                upgradeFader = false;
                            }
                        });
                    } else if (ballCounter.get() >= powerUps2.get(4).getPrice() && upgradeFader2) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                final ImageView imageView = new ImageView(MainActivity.this);
                                imageView.setId(View.generateViewId());
                                imageView.setImageResource(R.drawable.waw);
                                ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(250, 250);
                                imageView.setLayoutParams(params);
                                constraintLayout.addView(imageView);

                                ConstraintSet constraintSet = new ConstraintSet();
                                constraintSet.clone(constraintLayout);

                                constraintSet.connect(imageView.getId(), ConstraintSet.TOP, constraintLayout.getId(), ConstraintSet.TOP);
                                constraintSet.connect(imageView.getId(), ConstraintSet.BOTTOM, constraintLayout.getId(), ConstraintSet.BOTTOM);
                                constraintSet.connect(imageView.getId(), ConstraintSet.LEFT, constraintLayout.getId(), ConstraintSet.LEFT);
                                constraintSet.connect(imageView.getId(), ConstraintSet.RIGHT, constraintLayout.getId(), ConstraintSet.RIGHT);

                                constraintSet.setHorizontalBias(imageView.getId(), .20f);
                                constraintSet.setVerticalBias(imageView.getId(), .75f);

                                constraintSet.applyTo(constraintLayout);

                                AnimationSet upgrade = new AnimationSet(true);
                                fadeIn = new AlphaAnimation(0, 1);
                                fadeIn.setInterpolator(new DecelerateInterpolator());
                                fadeIn.setDuration(1500);
                                upgrade.addAnimation(fadeIn);
                                imageView.startAnimation(upgrade);
                                Toast add = Toast.makeText(MainActivity.this, "Unlocked Hitting Partner Power-Up!", Toast.LENGTH_SHORT);
                                add.setGravity(Gravity.CENTER, 0, 0);
                                add.show();
                                imageView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        ViewGroup parent = (ViewGroup) imageView.getParent();
                                        parent.removeView(imageView);
                                    }
                                });
                                upgradeFader2 = false;
                            }
                        });
                    } else if (ballCounter.get() >= powerUps2.get(3).getPrice() && upgradeFader3) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                final ImageView imageView = new ImageView(MainActivity.this);
                                imageView.setId(View.generateViewId());
                                imageView.setImageResource(R.drawable.coach);
                                ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(250, 250);
                                imageView.setLayoutParams(params);
                                constraintLayout.addView(imageView);

                                ConstraintSet constraintSet = new ConstraintSet();
                                constraintSet.clone(constraintLayout);

                                constraintSet.connect(imageView.getId(), ConstraintSet.TOP, constraintLayout.getId(), ConstraintSet.TOP);
                                constraintSet.connect(imageView.getId(), ConstraintSet.BOTTOM, constraintLayout.getId(), ConstraintSet.BOTTOM);
                                constraintSet.connect(imageView.getId(), ConstraintSet.LEFT, constraintLayout.getId(), ConstraintSet.LEFT);
                                constraintSet.connect(imageView.getId(), ConstraintSet.RIGHT, constraintLayout.getId(), ConstraintSet.RIGHT);

                                constraintSet.setHorizontalBias(imageView.getId(), .40f);
                                constraintSet.setVerticalBias(imageView.getId(), .75f);

                                constraintSet.applyTo(constraintLayout);

                                AnimationSet upgrade = new AnimationSet(true);
                                fadeIn = new AlphaAnimation(0, 1);
                                fadeIn.setInterpolator(new DecelerateInterpolator());
                                fadeIn.setDuration(1500);
                                upgrade.addAnimation(fadeIn);
                                imageView.startAnimation(upgrade);
                                Toast add = Toast.makeText(MainActivity.this, "Unlocked Coach Power-Up!", Toast.LENGTH_SHORT);
                                add.setGravity(Gravity.CENTER, 0, 0);
                                add.show();
                                imageView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        ViewGroup parent = (ViewGroup) imageView.getParent();
                                        parent.removeView(imageView);
                                    }
                                });
                                upgradeFader3 = false;
                            }
                        });
                    } else if (ballCounter.get() >= powerUps2.get(2).getPrice() && upgradeFader4) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                final ImageView imageView = new ImageView(MainActivity.this);
                                imageView.setId(View.generateViewId());
                                imageView.setImageResource(R.drawable.djokovic);
                                ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(250, 250);
                                imageView.setLayoutParams(params);
                                constraintLayout.addView(imageView);

                                ConstraintSet constraintSet = new ConstraintSet();
                                constraintSet.clone(constraintLayout);

                                constraintSet.connect(imageView.getId(), ConstraintSet.TOP, constraintLayout.getId(), ConstraintSet.TOP);
                                constraintSet.connect(imageView.getId(), ConstraintSet.BOTTOM, constraintLayout.getId(), ConstraintSet.BOTTOM);
                                constraintSet.connect(imageView.getId(), ConstraintSet.LEFT, constraintLayout.getId(), ConstraintSet.LEFT);
                                constraintSet.connect(imageView.getId(), ConstraintSet.RIGHT, constraintLayout.getId(), ConstraintSet.RIGHT);

                                constraintSet.setHorizontalBias(imageView.getId(), .60f);
                                constraintSet.setVerticalBias(imageView.getId(), .75f);

                                constraintSet.applyTo(constraintLayout);

                                AnimationSet upgrade = new AnimationSet(true);
                                fadeIn = new AlphaAnimation(0, 1);
                                fadeIn.setInterpolator(new DecelerateInterpolator());
                                fadeIn.setDuration(1500);
                                upgrade.addAnimation(fadeIn);
                                imageView.startAnimation(upgrade);
                                Toast add = Toast.makeText(MainActivity.this, "Unlocked Novak Djokovic Power-Up!", Toast.LENGTH_SHORT);
                                add.setGravity(Gravity.CENTER, 0, 0);
                                add.show();
                                imageView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        ViewGroup parent = (ViewGroup) imageView.getParent();
                                        parent.removeView(imageView);
                                    }
                                });
                                upgradeFader4 = false;
                            }
                        });
                    } else if (ballCounter.get() >= powerUps2.get(1).getPrice() && upgradeFader5) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                final ImageView imageView = new ImageView(MainActivity.this);
                                imageView.setId(View.generateViewId());
                                imageView.setImageResource(R.drawable.ballmachine);
                                ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(250, 250);
                                imageView.setLayoutParams(params);
                                constraintLayout.addView(imageView);

                                ConstraintSet constraintSet = new ConstraintSet();
                                constraintSet.clone(constraintLayout);

                                constraintSet.connect(imageView.getId(), ConstraintSet.TOP, constraintLayout.getId(), ConstraintSet.TOP);
                                constraintSet.connect(imageView.getId(), ConstraintSet.BOTTOM, constraintLayout.getId(), ConstraintSet.BOTTOM);
                                constraintSet.connect(imageView.getId(), ConstraintSet.LEFT, constraintLayout.getId(), ConstraintSet.LEFT);
                                constraintSet.connect(imageView.getId(), ConstraintSet.RIGHT, constraintLayout.getId(), ConstraintSet.RIGHT);

                                constraintSet.setHorizontalBias(imageView.getId(), .80f);
                                constraintSet.setVerticalBias(imageView.getId(), .75f);

                                constraintSet.applyTo(constraintLayout);

                                AnimationSet upgrade = new AnimationSet(true);
                                fadeIn = new AlphaAnimation(0, 1);
                                fadeIn.setInterpolator(new DecelerateInterpolator());
                                fadeIn.setDuration(1500);
                                upgrade.addAnimation(fadeIn);
                                imageView.startAnimation(upgrade);
                                Toast add = Toast.makeText(MainActivity.this, "Unlocked Ball Machine Power-Up!", Toast.LENGTH_SHORT);
                                add.setGravity(Gravity.CENTER, 0, 0);
                                add.show();
                                imageView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        ViewGroup parent = (ViewGroup) imageView.getParent();
                                        parent.removeView(imageView);
                                    }
                                });
                                upgradeFader5 = false;
                            }
                        });
                    } else if (ballCounter.get() >= powerUps2.get(0).getPrice() && upgradeFader6) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                final ImageView imageView = new ImageView(MainActivity.this);
                                imageView.setId(View.generateViewId());
                                imageView.setImageResource(R.drawable.evert);
                                ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(250, 250);
                                imageView.setLayoutParams(params);
                                constraintLayout.addView(imageView);

                                ConstraintSet constraintSet = new ConstraintSet();
                                constraintSet.clone(constraintLayout);

                                constraintSet.connect(imageView.getId(), ConstraintSet.TOP, constraintLayout.getId(), ConstraintSet.TOP);
                                constraintSet.connect(imageView.getId(), ConstraintSet.BOTTOM, constraintLayout.getId(), ConstraintSet.BOTTOM);
                                constraintSet.connect(imageView.getId(), ConstraintSet.LEFT, constraintLayout.getId(), ConstraintSet.LEFT);
                                constraintSet.connect(imageView.getId(), ConstraintSet.RIGHT, constraintLayout.getId(), ConstraintSet.RIGHT);

                                constraintSet.setHorizontalBias(imageView.getId(), 1f);
                                constraintSet.setVerticalBias(imageView.getId(), .75f);

                                constraintSet.applyTo(constraintLayout);

                                AnimationSet upgrade = new AnimationSet(true);
                                fadeIn = new AlphaAnimation(0, 1);
                                fadeIn.setInterpolator(new DecelerateInterpolator());
                                fadeIn.setDuration(1500);
                                upgrade.addAnimation(fadeIn);
                                imageView.startAnimation(upgrade);
                                Toast add = Toast.makeText(MainActivity.this, "Unlocked Tennis Academy Power-Up!", Toast.LENGTH_SHORT);
                                add.setGravity(Gravity.CENTER, 0, 0);
                                add.show();
                                imageView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        ViewGroup parent = (ViewGroup) imageView.getParent();
                                        parent.removeView(imageView);
                                    }
                                });
                                upgradeFader6 = false;
                            }
                        });
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
            }
        }
    }

    public static class PowerUps {

        String name;
        int ballspersecond;
        int price;
        int counter;
        int image;


        public PowerUps(String name, int ballspersecond, int price, int counter, int image) {
            this.name = name;
            this.ballspersecond = ballspersecond;
            this.price = price;
            this.counter = counter;
            this.image = image;
        }

        public int getPrice() {
            return price;
        }

        public void setPrice(int pr) { this.price = pr; }

        public String getName() {
            return name;
        }

        public int getBallspersecond(){
            return ballspersecond;
        }

        public int getCounter() {
            return counter;
        }

        public void addCounter() { counter++; }

        public int getImage() {
            return image;
        }

        public void setCounter(int counter) {
            this.counter = counter;
        }

    }

}
