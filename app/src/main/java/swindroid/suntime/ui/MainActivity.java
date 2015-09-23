package swindroid.suntime.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Vector;

import android.widget.TextView;

import swindroid.suntime.R;

public class MainActivity extends Activity
{
    List<String> listFull = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        InitialiseUI();
    }

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

    public View.OnClickListener onTempClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onTempClick();
        }
    };

    public View.OnClickListener onAddClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            onAddCustomClick();
        }
    };

    private void InitialiseUI()
    {
        Vector<String> splitstring = new Vector<String>();
        try{
            String line;
            InputStream file = getAssets().open("au_locations.txt");
            FileOutputStream fos = openFileOutput("custom_locations.txt", Context.MODE_APPEND);
            fos.close();
            FileInputStream fis = openFileInput("custom_locations.txt");
            final Spinner locations = (Spinner)findViewById(R.id.location_spinner);
            List<String> list = new ArrayList<String>();
            Scanner scans = new Scanner(file);
            Scanner scanCustom = new Scanner(fis);
            final TextView text_selected = (TextView)findViewById(R.id.selectedText);

            Button check_btn = (Button)findViewById(R.id.check_temp_btn);
            Button add_custom = (Button)findViewById(R.id.customLocBtn);
            add_custom.setOnClickListener(onAddClickListener);
            check_btn.setOnClickListener(onTempClickListener);

            while(scans.hasNext())
            {
                line = scans.nextLine();
                listFull.add(line);
                String splitline[]  = line.split(",");
                for (String item:splitline)
                {
                    splitstring.add(item);
                }
                list.add(splitstring.firstElement() + "," +splitstring.lastElement());
                splitstring.clear();
 //need to only read the first part of the string here.
            }
            while(scanCustom.hasNext())
            {
                line = scanCustom.nextLine();
                listFull.add(line);
                String splitline[]  = line.split(",");
                for (String item:splitline)
                {
                    splitstring.add(item);
                }
                list.add(splitstring.firstElement() + "," +splitstring.lastElement());
                splitstring.clear();
            }
            fis.close();
            file.close();
            ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,list);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            locations.setAdapter(adapter);

            locations.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener(){
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
                {
                    locations.setSelection(i);
                    String selectedText;
                    selectedText = (String)locations.getSelectedItem();
                    text_selected.setText(selectedText);
                }
                public void onNothingSelected(AdapterView<?> adapterView)
                {
                    return;
                }
            });

        }catch(IOException ioe){
            ioe.printStackTrace();
        }
    }
    private void onTempClick()
    {
        TextView selected = (TextView)findViewById(R.id.selectedText);
        String selectedText = selected.getText().toString();
        String stateFinal = " ";
        for (String str: listFull
             ) {
            String[] split = str.split(",");
            String compare = split[0]+","+split[3];
            if(compare.matches(selectedText))
            {
                stateFinal = str;
                break;
            }
        }
        Intent intent = new Intent(this, StateSunRise.class);
        Bundle data = new Bundle();
        data.putString("info",stateFinal);
        intent.putExtras(data);
        startActivity(intent);
    }

    private void onAddCustomClick()
    {
        Intent intent = new Intent(this,CustomLocation.class);
        startActivity(intent);
    }

}
