package com.ulluna.ullunafinance;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Random;


public class StockList extends ActionBarActivity {
    ListView listView;
    MySimpleArrayAdapter adapter;
    float[] costs;
    String[] values;
    float[] changes;
    SharedPreferences.Editor editor;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_list);
        listView = (ListView) findViewById(R.id.listView);

        SharedPreferences sp = getSharedPreferences("com.ulluna.ullunafinance", MODE_PRIVATE);
        editor = sp.edit();
        int i=0, count=0;

        if(sp.getString("STOCK0",null)==null)
            fistOpen();

        while(sp.getString("STOCK"+count,null)!=null)
            count++;

        values = new String[count];
        String s=sp.getString("STOCK0",null);

        while(s!=null){
            values[i]=s;
            i++;
            s = sp.getString("STOCK"+i,null);
        }

        i=0;
        float cost = sp.getFloat("VALUE0",-1);
        costs = new float[count];
        changes = new float[count];
        while (cost != -1.0){
            costs[i] = cost;
            i++;
            cost = sp.getFloat("VALUE" + i, -1);
        }

        adapter = new MySimpleArrayAdapter(this, values, costs);
        listView.setAdapter(adapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                String selected = ((TextView) view.findViewById(R.id.firstLine)).getText().toString();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("CompanyName", selected);
                startActivity(intent);
            }
        });
        timer.start();

    }

    private Handler uiUpdater = new Handler(){
        public void handleMessage(Message msg){
            float temp[] = new float[2];
            for(int i=0; i<costs.length; i++){
                temp = Stock.modifyValue(costs[i]);
                costs[i]=temp[0];
                changes[i]=temp[1];
                editor.putFloat("VALUE"+i, costs[i]);
            }
            editor.commit();

            adapter.updateData(getApplicationContext(), values, costs, changes);
        }
    };
    Thread timer = new Thread() {
        public void run () {
            while (!Thread.currentThread().isInterrupted()) {
                // do stuff in a separate thread
                uiUpdater.sendEmptyMessage(0);
                try {
                    sleep(5000);    // sleep for 3 seconds
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        timer.interrupt();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!timer.isAlive())
            timer.start();
    }

    private void fistOpen(){
        SharedPreferences sp = getSharedPreferences("com.ulluna.ullunafinance", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putFloat("WALLET", 100);
        Random random = new Random();
        String[] companies = {"ULLUNA", "Novi", "Impossible Inc", "Tomasz & Co", "SECOND", "MasterKey", "Michigan Codes", "Martin Mobile", "Channel-"};
        for(int i = 0; i<companies.length; i++){
            editor.putString("STOCK"+i, companies[i]);
            editor.putInt(companies[i], i);
            editor.putFloat("VALUE" +i, random.nextFloat()*30+10);
            editor.putInt("POCKET" + i, 0);
        }
        editor.commit();
    }
}
