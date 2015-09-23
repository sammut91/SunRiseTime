package swindroid.suntime.ui;

import java.io.CharArrayReader;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.Vector;

import swindroid.suntime.R;
import swindroid.suntime.calc.AstronomicalCalendar;
import swindroid.suntime.calc.GeoLocation;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.TextView;

import org.w3c.dom.Text;

public class StateSunRise extends Activity
{
	String state = new String();
	String splitString[];
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        initializeUI();
    }

	private void initializeUI()
	{
		Bundle extras = getIntent().getExtras();
		state = extras.getString("info");
		splitString = state.split(",");
        TextView locName = (TextView)findViewById(R.id.locationTV);
        locName.setText(splitString[0]);
		DatePicker dp = (DatePicker) findViewById(R.id.datePicker);
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);
		int day = cal.get(Calendar.DAY_OF_MONTH);
		dp.init(year, month, day, dateChangeHandler); // setup initial values and reg. handler
		updateTime(year, month, day); // insert the text from the bundle here

	}

	//add an additional variable text
	private void updateTime(int year, int monthOfYear, int dayOfMonth)
	{
		double loc1 = Double.parseDouble(splitString[1]);
		double loc2 = Double.parseDouble(splitString[2]);
		TimeZone tz = TimeZone.getTimeZone(splitString[3]); //change this to accept the text from the bundle
		GeoLocation geolocation = new GeoLocation(splitString[0], loc1, loc2, tz);
		AstronomicalCalendar ac = new AstronomicalCalendar(geolocation);
		ac.getCalendar().set(year, monthOfYear, dayOfMonth);
		Date srise = ac.getSunrise();
		Date sset = ac.getSunset();
		
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		
		TextView sunriseTV = (TextView) findViewById(R.id.sunriseTimeTV);
		TextView sunsetTV = (TextView) findViewById(R.id.sunsetTimeTV);
		Log.d("SUNRISE Unformatted", srise+"");
		
		sunriseTV.setText(sdf.format(srise));
		sunsetTV.setText(sdf.format(sset));		
	}
	
	OnDateChangedListener dateChangeHandler = new OnDateChangedListener()
	{
		public void onDateChanged(DatePicker dp, int year, int monthOfYear, int dayOfMonth)
		{
			updateTime(year, monthOfYear, dayOfMonth);
		}	
	};

    @Override
    public void onBackPressed() {
        splitString = new String[0];
		Intent intent = new Intent(this,MainActivity.class);
        super.onBackPressed();
		startActivity(intent);
    }
	
}