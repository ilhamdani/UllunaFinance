package com.ulluna.ullunafinance;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.ThreadLocalRandom;


public class MainActivity extends ActionBarActivity {
    TextView stockView, sharesView, cashView;
    String stockName;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    int index, sharesNumber;
    float value, cash;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sp = getSharedPreferences("com.ulluna.ullunafinance",MODE_PRIVATE);
        editor = sp.edit();
        stockView = (TextView)findViewById(R.id.textView);
        Intent intent = getIntent();
        String message = intent.getStringExtra("CompanyName");
        index=sp.getInt(message, -1);
        if(index<0){
            Toast toast = new Toast(this);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setText("We have a problem. Company not found");
        }
        stockName="STOCK"+index;
        stockView.setText(message);
        sharesNumber = sp.getInt("POCKET"+index,-1);
        cash=sp.getFloat("WALLET", -1);
        cashView=(TextView)findViewById(R.id.textView4);
        sharesView = (TextView)findViewById(R.id.textView3);
        cashView.setText("Cash: " + cash + " USD");
        sharesView.setText("Number of shares: " + sharesNumber);

        Thread timer = new Thread() {
            public void run () {
                while (true) {
                    // do stuff in a separate thread
                    uiCallback.sendEmptyMessage(0);
                    try {
                        sleep(5000);    // sleep for 3 seconds
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        timer.start();

    }

    private Handler uiCallback = new Handler () {
        public void handleMessage (Message msg) {
            // do stuff with UI
            Log.i("TAG", "Updated values");

            value = sp.getFloat("VALUE"+index,10);
            float value2 = Stock.modifyValue(value)[0];
            editor.putFloat("VALUE"+index, value);

            float change = (value2-value)/value*100;
            change = Stock.roundPrice(change);
            value=value2;

            editor.commit();
            TextView stockChange = (TextView)findViewById(R.id.textView2);
            String toDisplay = value + " USD (";
            if(change>0){
                stockChange.setTextColor(Color.parseColor("#4CAF50"));
                toDisplay+="+";
            }
            else{
                stockChange.setTextColor(Color.parseColor("#F44336"));
            }
            toDisplay+= String.valueOf(change) + "%)";
            stockChange.setText(toDisplay);
        }
    };


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void buttonClicked(View view) {

        SharedPreferences sp = getSharedPreferences("com.ulluna.ullunafinance",MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putFloat(stockName, 10);
        editor.commit();
    }

    public void upadateStock(){
        Log.i("TAG", "Updated values");
        ThreadLocalRandom rand = ThreadLocalRandom.current();
        double change = rand.nextDouble(1)-0.5;
        TextView stockChange = (TextView)findViewById(R.id.textView2);

        stockChange.setText(String.valueOf(change));
        if(change>0){
            stockChange.setTextColor(Color.parseColor("#4CAF50"));
        }
        else{
            stockChange.setTextColor(Color.parseColor("#F44336"));
        }
    }

    public void buyShare(View view) {
        cash-=value;
        sharesNumber++;
        editor.putFloat("WALLET",cash);
        editor.putInt("POCKET"+index,sharesNumber);
        cashView.setText("Cash: " + cash + " USD");
        sharesView.setText("Number of shares: " + sharesNumber);
        editor.commit();
    }

    public void sellShare(View view) {
        cash+=value;
        sharesNumber--;
        editor.putFloat("WALLET",cash);
        editor.putInt("POCKET"+index,sharesNumber);
        cashView.setText("Cash: " + Stock.roundPrice(cash) + " USD");
        sharesView.setText("Number of shares: " + sharesNumber);
        editor.commit();
    }
}
