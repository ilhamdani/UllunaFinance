package com.ulluna.ullunafinance;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Random;


public class StockList extends ActionBarActivity {
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_list);
        listView = (ListView) findViewById(R.id.listView);

        SharedPreferences sp = getSharedPreferences("com.ulluna.ullunafinance", MODE_PRIVATE);
        int i=0, count=0;

        if(sp.getString("STOCK0",null)==null){
            fistOpen();
        }

        while(sp.getString("STOCK"+count,null)!=null){
            count++;
        }

        String[] values = new String[count];
        String s=sp.getString("STOCK0",null);

        while(s!=null){
            values[i]=s;
            i++;
            s = sp.getString("STOCK"+i,null);
        }
        i=0;
        float cost = sp.getFloat("VALUE0",-1);
        float[] costs = new float[count];

        while (cost != -1.0){
            costs[i] = cost;
            i++;
            cost = sp.getFloat("VALUE" + i, -1);
        }

        final MySimpleArrayAdapter adapter = new MySimpleArrayAdapter(this, values, costs);
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
