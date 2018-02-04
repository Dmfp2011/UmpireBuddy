package com.example.dylan.umpirebuddy;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {


    int StrikeCount = 0;
    int BallCount = 0;
    TextView StrikeCounterView;
    TextView BallCounterView;
    TextView Total_Outs;
    int outs;


    //Creating methods to condense code for resetting the app
    public void Reset()
    {
        StrikeCount = 0;
        BallCount = 0;
        StrikeCounterView.setText(String.valueOf(StrikeCount));
        BallCounterView.setText(String.valueOf(BallCount));
    }

    //Creating the popup window
    public void pop_up(int x) {
        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);

            View mView = getLayoutInflater().inflate(R.layout.popupwindow, null);

            //Defining the views (Buttons, or text fields and whatnot) inside out_dialog
            Button mButton = mView.findViewById(R.id.PopButt);
            TextView Pop_dialog = mView.findViewById(R.id.textView2);
            if (x == 0)
            {
                Pop_dialog.setText(R.string.youre_out);
            }
            else
                Pop_dialog.setText(R.string.you_walk);


            //Setting what view is shown from the pop up (MOST IMPORTANT)
            mBuilder.setView(mView);

            //Creating the alert dialog
            final AlertDialog dialog = mBuilder.create();

            //Showing the AlertDialog (the popup)
            dialog.show();

        //Setting a listener for the button on the popup
        mButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                //closing the popup
                dialog.cancel();
            }
        });

    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

            Toolbar toolbar = findViewById(R.id.action_bar);
            setSupportActionBar(toolbar);


            //Linking views to java code
            StrikeCounterView = findViewById(R.id.StrikeCounterviewMain);
            BallCounterView = findViewById(R.id.BallCounterViewMain);
            Total_Outs = findViewById(R.id.TotalOuts);


            //Setting the numbers for ball and strike in the view
            StrikeCounterView.setText(String.valueOf(StrikeCount));
            BallCounterView.setText(String.valueOf(BallCount));

            //Creating a window to look into the shared value of outs
            SharedPreferences sharedpref = getSharedPreferences("Outs_file", Context.MODE_PRIVATE);

            //displaying the number of outs from the saved file to the screen
            String outta = "Total outs:  " + String.valueOf(sharedpref.getInt("Ttl Outs",0));
            Total_Outs.setText(outta);

            //Adding the view to the buttons
            Button strike_button = findViewById(R.id.strikebtn);
            strike_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //If Clicked, increment the number of strikes
                    StrikeCount++;
                    //If the strike count is greater or equal to 3 pop up out text
                    if (StrikeCount >= 3) {

                        //Display Out message and increments outs
                        pop_up(0);

                        //Creating a shared preference and telling it what file to save to and the privacy mode
                        SharedPreferences sharedpref = getSharedPreferences("Outs_file", Context.MODE_PRIVATE);

                        //Allowing an editor object to edit the variable created above
                        SharedPreferences.Editor editor = sharedpref.edit();

                        //retrieving the previous value for outs and increment
                        outs = sharedpref.getInt("Ttl Outs",0);
                        outs++;

                        //Creating a key value pair for storage in the file
                        editor.putInt("Ttl Outs",outs);
                        editor.apply();

                        //displaying the number of outs from the saved file to the screen
                        String outta = "Total outs:  " + String.valueOf(sharedpref.getInt("Ttl Outs",0));
                        Total_Outs.setText(outta);

                        Reset();
                    }
                    //If strikes are less than 3, update the Text view for strikes
                    else{
                        StrikeCounterView.setText(String.valueOf(StrikeCount));
                }
                }
            });

            Button ball_button = findViewById(R.id.ballbtn);
            ball_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //If Clicked, increment the number of strikes
                    BallCount++;
                    //If ball count is 4 or more, display walk window
                    if (BallCount >= 4){
                        //Starts up the pop up window
                        pop_up(1);
                        Reset();
                    }
                    //If the ball count is less than 4, update view
                    else {
                        BallCounterView.setText(String.valueOf(BallCount));
                    }
                }
            });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.about_AB:
                // User chose about tab in menu
                Intent about = new Intent(this,AboutActivity.class);
                startActivity(about);
                return true;

            case R.id.action_reset:
                // User chose the "Reset" action, mark the current item
                //Creating a shared preference and telling it what file to save to and the privacy mode
                SharedPreferences sharedpref = getSharedPreferences("Outs_file", Context.MODE_PRIVATE);

                //Allowing an editor object to edit the variable created above
                SharedPreferences.Editor editor = sharedpref.edit();

                //clearing the shared storage for the number of outs
                editor.clear();
                editor.apply();

                //refreshing the total number of outs after the reset button has been clicked
                String outta = "Total outs:  " + String.valueOf(sharedpref.getInt("Ttl Outs",0));
                Total_Outs.setText(outta);

                //Shows the user that the data has been cleared
                Toast.makeText(getApplicationContext(),"Number of outs reset",Toast.LENGTH_SHORT).show();

                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }


}