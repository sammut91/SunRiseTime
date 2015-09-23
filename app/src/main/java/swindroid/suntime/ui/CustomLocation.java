package swindroid.suntime.ui;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import junit.framework.Test;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import swindroid.suntime.R;

public class CustomLocation extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_location);
        InitialiseUI();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_custom_location, menu);
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

    private void InitialiseUI()
    {
        final Double steps = 0.5;
        final TextView Lat = (TextView)findViewById(R.id.loc_latitude_input);
        final TextView Long = (TextView)findViewById(R.id.loc_longitude_input);
        Button submit_btn = (Button)findViewById(R.id.loc_submit_btn);
        submit_btn.setOnClickListener(onSubmitListener);
        SeekBar sblong = (SeekBar)findViewById(R.id.longtiude_bar);
        SeekBar sbLat = (SeekBar)findViewById(R.id.latitude_bar);
        sblong.setMax(723);
        sblong.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (i > 360) {
                    Double value = (i * steps) - 180;
                    Long.setText(value.toString());
                } else {
                    Double value = -(180 - (i * steps));
                    Long.setText(value.toString());
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        sbLat.setMax(723);
        sbLat.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (i > 360) {
                    Double value = (i * steps)-180;
                    Lat.setText(value.toString());
                } else {
                    Double value = -(180 - (i * steps));
                    Lat.setText(value.toString());
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    public View.OnClickListener onSubmitListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            onSubmitPressed();
        }
    };

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    public void onSubmitPressed()
    {
        String input = " ";
        TextView loc_name = (TextView)findViewById(R.id.loc_name_input);
        TextView loc_longitude = (TextView)findViewById(R.id.loc_longitude_input);
        TextView loc_latidtude = (TextView)findViewById(R.id.loc_latitude_input);
        TextView loc_timezone = (TextView)findViewById(R.id.loc_timezone_input);

        String location_name = loc_name.getText().toString();
        double location_longitude = Double.parseDouble(loc_longitude.getText().toString());
        double location_latitude = Double.parseDouble(loc_latidtude.getText().toString());
        String location_zone = loc_timezone.getText().toString();

        if(!location_name.isEmpty() && !location_zone.isEmpty()
                && !loc_latidtude.getText().toString().isEmpty()
                && !loc_longitude.getText().toString().isEmpty()) {
            input = location_name+","+location_longitude+","+location_latitude+","
                    +location_zone;
            writeToFile(input);
        }
        Intent intent = new Intent(this,StateSunRise.class);
        Bundle data = new Bundle();
        data.putString("info",input);
        intent.putExtras(data);
        startActivity(intent);
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    @Override
    public void onBackPressed()
    {
        TextView loc_name = (TextView)findViewById(R.id.loc_name_input);
        TextView loc_longitude = (TextView)findViewById(R.id.loc_longitude_input);
        TextView loc_latidtude = (TextView)findViewById(R.id.loc_latitude_input);
        TextView loc_timezone = (TextView)findViewById(R.id.loc_timezone_input);

        String location_name = loc_name.getText().toString();
        double location_longitude = Double.parseDouble(loc_longitude.getText().toString());
        double location_latitude = Double.parseDouble(loc_latidtude.getText().toString());
        String location_zone = loc_timezone.getText().toString();

        if(!location_name.isEmpty() && !location_zone.isEmpty()
                && !loc_latidtude.getText().toString().isEmpty()
                && !loc_longitude.getText().toString().isEmpty()) {
            String input = location_name + "," + location_longitude + "," + location_latitude + ","
                    + location_zone;
            writeToFile(input);
        }
        super.onBackPressed();
    }

    private void writeToFile(String data)
    {
        try{
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput("custom_locations.txt",Context.MODE_APPEND));
            outputStreamWriter.write(data +"\n");
            outputStreamWriter.close();

        }catch(IOException ioe)
        {
            Log.e("Exception", "File write failed: " + ioe.toString());
        }
    }
}
