package com.adityaamk.ballclicker;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ActionBar extends AppCompatActivity {
    static ArrayList<MainActivity.PowerUps> powerUps;
    SharedPreferences sharedPreferences;
    RecyclerView recyclerView;
    RecyclerViewAdapter adapter;
    TranslateAnimation translateAnimation;
    //static ArrayList<MainActivity.PowerUps> powerUps2;

    public static String TENNISACADEMY_KEY = "TENNISACADEMY_KEY";
    public static String BALLMACHINE_KEY = "BALLMACHINE_KEY";
    public static String NOVAK_KEY = "NOVAK_KEY";
    public static String COACH_KEY = "COACH_KEY";
    public static String HITTING_KEY = "HITTING_KEY";
    public static String RACKET_KEY = "RACKET_KEY";

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(TENNISACADEMY_KEY, powerUps.get(0).getCounter());
        editor.putInt(BALLMACHINE_KEY, powerUps.get(1).getCounter());
        editor.putInt(NOVAK_KEY, powerUps.get(2).getCounter());
        editor.putInt(COACH_KEY, powerUps.get(3).getCounter());
        editor.putInt(HITTING_KEY, powerUps.get(4).getCounter());
        editor.putInt(RACKET_KEY, powerUps.get(5).getCounter());
        editor.apply();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        powerUps.get(0).setCounter(sharedPreferences.getInt(TENNISACADEMY_KEY, 0));
        powerUps.get(1).setCounter(sharedPreferences.getInt(BALLMACHINE_KEY, 0));
        powerUps.get(2).setCounter(sharedPreferences.getInt(NOVAK_KEY, 0));
        powerUps.get(3).setCounter(sharedPreferences.getInt(COACH_KEY, 0));
        powerUps.get(4).setCounter(sharedPreferences.getInt(HITTING_KEY, 0));
        powerUps.get(5).setCounter(sharedPreferences.getInt(RACKET_KEY, 0));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actionbar);
        getWindow().setLayout(1000, 700);
        getWindow().setBackgroundDrawableResource(R.drawable.background);
        sharedPreferences = getPreferences(Context.MODE_PRIVATE);

        powerUps = new ArrayList<>();
        powerUps.add(new MainActivity.PowerUps("Tennis Academy", 160, 50000, 0, R.drawable.evert));
        powerUps.add(new MainActivity.PowerUps("Ball Machine", 80, 32000, 0, R.drawable.ballmachine));
        powerUps.add(new MainActivity.PowerUps("Novak Djokovic", 40, 8000, 0, R.drawable.djokovic));
        powerUps.add(new MainActivity.PowerUps("Coach", 20, 2000, 0, R.drawable.coach));
        powerUps.add(new MainActivity.PowerUps("Hitting Partner", 10, 500, 0, R.drawable.waw));
        powerUps.add(new MainActivity.PowerUps("Tennis Racket", 3, 50, 0, R.drawable.tennisracket));

        UpdateAdapter updateAdapter = new UpdateAdapter();
        updateAdapter.start();

        recyclerView = findViewById(R.id.id_recyclerView);
        recyclerView.setNestedScrollingEnabled(false);
        adapter = new RecyclerViewAdapter(this, powerUps);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
        ArrayList<MainActivity.PowerUps> powerUps;
        Context context;

        public RecyclerViewAdapter(Context context, ArrayList<MainActivity.PowerUps> powerUps) {
            this.powerUps = powerUps;
            this.context = context;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            ImageView pic;
            TextView name, price, counter;
            Button btnBuy;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                pic = itemView.findViewById(R.id.id_iv);
                name = itemView.findViewById(R.id.id_name);
                price = itemView.findViewById(R.id.id_price);
                counter = itemView.findViewById(R.id.id_count);
                btnBuy = itemView.findViewById(R.id.id_buy);
            }
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.powerups, parent, false);
            ViewHolder holder = new ViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
            holder.pic.setImageResource(powerUps.get(position).getImage());
            holder.name.setText(powerUps.get(position).getName());
            holder.price.setText(""+powerUps.get(position).getPrice());
            holder.counter.setText(""+powerUps.get(position).getCounter());
            holder.btnBuy.getBackground().setAlpha(0);

            if (!(MainActivity.ballCounter.get() >= powerUps.get(position).getPrice())) {
                holder.btnBuy.setEnabled(false);
            } else {
                holder.btnBuy.setEnabled(true);
            }

            if(powerUps.get(position).getName().equals("Tennis Racket")&&powerUps.get(position).getCounter()>=10){
                holder.btnBuy.setEnabled(false);
            }
            if(powerUps.get(position).getName().equals("Hitting Partner")&&powerUps.get(position).getCounter()>=8){
                holder.btnBuy.setEnabled(false);
            }
            if(powerUps.get(position).getName().equals("Coach")&&powerUps.get(position).getCounter()>=6){
                holder.btnBuy.setEnabled(false);
            }


            holder.btnBuy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (MainActivity.ballCounter.get() >= powerUps.get(position).getPrice()) {
                        //powerUps.get(position).setPrice(((int)(powerUps.get(position).getPrice()*1.1)));
                        //holder.price.setText(""+powerUps.get(position).getPrice());
                        powerUps.get(position).addCounter();
                        holder.counter.setText(powerUps.get(position).getCounter()+"");
                        MainActivity.ballCounter.getAndAdd(0-powerUps.get(position).getPrice());
                        final ImageView imageView = new ImageView(ActionBar.this);
                        imageView.setId(View.generateViewId());
                        imageView.setImageResource(powerUps.get(position).getImage());
                        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(250, 250);
                        imageView.setLayoutParams(params);
                        MainActivity.constraintLayout.addView(imageView);

                        ConstraintSet constraintSet = new ConstraintSet();
                        constraintSet.clone(MainActivity.constraintLayout);

                        constraintSet.connect(imageView.getId(), ConstraintSet.TOP, MainActivity.constraintLayout.getId(), ConstraintSet.TOP);
                        constraintSet.connect(imageView.getId(), ConstraintSet.BOTTOM, MainActivity.constraintLayout.getId(), ConstraintSet.BOTTOM);
                        constraintSet.connect(imageView.getId(), ConstraintSet.LEFT, MainActivity.constraintLayout.getId(), ConstraintSet.LEFT);
                        constraintSet.connect(imageView.getId(), ConstraintSet.RIGHT, MainActivity.constraintLayout.getId(), ConstraintSet.RIGHT);

                        constraintSet.setHorizontalBias(imageView.getId(), (float) ((Math.random())));
                        constraintSet.setVerticalBias(imageView.getId(), .86f);

                        constraintSet.applyTo(MainActivity.constraintLayout);

                        AnimationSet slide = new AnimationSet(true);
                        if((int)(Math.random()*2)==0) {
                            translateAnimation = new TranslateAnimation(-2000,0,0,0);
                        }
                        else{
                            translateAnimation = new TranslateAnimation(2000,0,0,0);
                        }
                        translateAnimation.setDuration(600);
                        slide.addAnimation(translateAnimation);
                        AlphaAnimation fadeIn = new AlphaAnimation(0,1);
                        fadeIn.setInterpolator(new DecelerateInterpolator());
                        fadeIn.setDuration(1500);
                        slide.addAnimation(fadeIn);
                        imageView.startAnimation(slide);

                        if (!(MainActivity.ballCounter.get() >= powerUps.get(position).getPrice())) {
                            holder.btnBuy.setEnabled(false);
                        }
                    } else {
                        holder.btnBuy.setEnabled(false);
                    }
                }
            });

        }

        @Override
        public int getItemCount() {
            return powerUps.size();
        }

    }

    public class UpdateAdapter extends Thread {
        @Override
        public void run() {
            while (true) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
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

}
