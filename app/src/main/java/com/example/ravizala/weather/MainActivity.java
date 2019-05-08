package com.example.ravizala.weather;

// Zala Ravi Tulsi                  B00805073



import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class MainActivity extends AppCompatActivity {

    //AutoCompleteTextView is same as edit text but shows the list of suggestions on inputting a character
    private AutoCompleteTextView etCity;
    public static Button btWeather;
    public static TextView tvCity,tvTemp,tvMinTempValue,tvMaxTempValue,tvAtmosphere,tvAtmosphereDesc,tvHumidity,tvCloudsPercent,tvHumidityText,tvCloudsText;
    public static String cityName;
    public static LinearLayout lv_weather;
    View div,div1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etCity = findViewById(R.id.et_city);
        btWeather = findViewById(R.id.bt_weather);
        tvCity = findViewById(R.id.tv_city);
        tvTemp = findViewById(R.id.tv_temp);
        tvMinTempValue = findViewById(R.id.tv_minvalue);
        tvMaxTempValue = findViewById(R.id.tv_maxvalue);
        tvAtmosphere = findViewById(R.id.tv_atmosphere);
        tvAtmosphereDesc= findViewById(R.id.tv_description);
        tvHumidity = findViewById(R.id.tv_humidity);
        tvCloudsPercent = findViewById(R.id.tv_clouds);
        tvHumidityText=findViewById(R.id.tv_hum_text);
        tvCloudsText=findViewById(R.id.tv_clouds_text);
        lv_weather=findViewById(R.id.lv_weather);
        div=findViewById(R.id.div);
        div1=findViewById(R.id.div1);

        //this four lines of code is used for setting the default weather information for Halifax city
        // by executing the tsk using execute() method


        cityName = "Halifax";
        String base_url = "https://api.openweathermap.org/data/2.5/weather?q="+cityName+"&appid=e67c42416036b36a0431a45dcfca4752&units=metric";
        new getCityWeather().execute(base_url);

        //Array adpater converts the array of strings into view items loaded into AutocompleteTextView
        //In setThreshold , specify the number of characters user has to type for suggestions

        String[] city=getResources().getStringArray(R.array.name_cities);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this,android.R.layout.select_dialog_item, city);
        etCity.setThreshold(1);
        etCity.setAdapter(adapter);

        //When the user clicks on the button , listener will check if the cityname is empty or starting witb spaces or containing numbers
        //This method gets the cityname and executes the API url for Asynctask
        btWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                    //we are connected to a network
                    if(etCity.getText().toString().equals(""))
                    {
                        Toast.makeText(MainActivity.this, "City name can not be empty  !!!", Toast.LENGTH_SHORT).show();
                        setWidgetsNull();
                    }
                    else if(etCity.getText().toString().startsWith(" "))
                    {
                        Toast.makeText(MainActivity.this, "City name can not start with spaces  !!!", Toast.LENGTH_SHORT).show();
                        setWidgetsNull();
                    }
                    else if(etCity.getText().toString().matches("[0-9]+"))
                    {
                        Toast.makeText(MainActivity.this, "City name can not contain numbers  !!!", Toast.LENGTH_SHORT).show();
                        setWidgetsNull();
                    }
                    else {
                        cityName = etCity.getText().toString();
                        new getCityWeather().execute("https://api.openweathermap.org/data/2.5/weather?q="+cityName+"&appid=e67c42416036b36a0431a45dcfca4752&units=metric");
                    }
            }
        });
    }

    //This method sets the widgets to nulls when there is a toast alert like empty city name, invalid city name etc

    public void setWidgetsNull()
    {

        tvCity.setText("");
        tvAtmosphere.setText("");
        tvAtmosphereDesc.setText("");
        tvCloudsPercent.setText("");
        tvHumidity.setText("");
        tvHumidityText.setText("");
        tvMaxTempValue.setText("");
        tvMinTempValue.setText("");
        tvTemp.setText("");
        tvCloudsText.setText("");
        div.setVisibility(View.INVISIBLE);
        div1.setVisibility(View.INVISIBLE);

    }

    //Got help from https://developer.android.com/reference/java/net/HttpURLConnection and https://developer.android.com/reference/android/os/AsyncTask
    //Asynctask is used for short running task eg connection to APIs.It is also used for background task
    //so that I won't block main UI thread
    //AsyncTask<String, String, String>, first String is used in String.. urls, second is used in onProgressUpdate, third string is used in
    //onPostExecute as a result obtained from doInBackground.

    @SuppressLint("StaticFieldLeak")
    public class getCityWeather extends AsyncTask<String, String, String> {

        HttpURLConnection httpconnection = null;
        String weather_data="";
        URL url = null;

        @Override
        protected String doInBackground(String... urls) {
            StringBuilder stringBuilder = new StringBuilder();
            try {
                //http get request to API url
                url = new URL(urls[0]);
                httpconnection = (HttpURLConnection) url.openConnection();
                httpconnection.setRequestMethod("GET");
                httpconnection.connect();

                //Getting the weather data in buffered reader

                BufferedReader data_reader = new BufferedReader(new InputStreamReader(httpconnection.getInputStream()));
                while (weather_data!=null)
                {
                    weather_data = data_reader.readLine();
                    stringBuilder.append(weather_data);
                }

                //Streams must be flushed before exiting the program. This helps freeing up the resources faster.
                httpconnection.getInputStream().close();
                data_reader.close();
            }

            catch (Exception e) {
                e.printStackTrace();


            }
            return stringBuilder.toString();
        }


        //Got help from https://www.tutorialspoint.com/android/android_json_parser.htm
        //onPostExecute uses the result obtained from doInBackground(). Stops the Asynctask process
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                //Set the divider visibility on as we are making null in the setWidgetsNull method
                div.setVisibility(View.VISIBLE);
                div1.setVisibility(View.VISIBLE);

                //Json object to parse the strings and arrays
                JSONObject origin = new JSONObject(result);
                String country="";
                JSONObject sysResponse = origin.getJSONObject("sys");

                for(int i=0;i<sysResponse.names().length();i++) {


                    String key = sysResponse.names().getString(i);
                    String val = sysResponse.get(key).toString();


                    if(key.equals("country"))
                    {
                        country =   val;
                    }
                }


                tvCity.setText(cityName.toUpperCase() +  " , "+ country);

                JSONObject mainResponse = origin.getJSONObject("main");
                //this loop will set the mininmum temperature, maximum temperature, temperature, humidity to particular textviews
                for(int i=0;i<mainResponse.names().length();i++)
                {
                    String key = mainResponse.names().getString(i);
                    String valueTemp;
                    Double value = Double.parseDouble(mainResponse.get(mainResponse.names().getString(i)).toString());
                    valueTemp = String.format("%.1f",value);
                    if(key.equals("temp_min"))
                    {
                        tvMinTempValue.setText("MIN: "+ valueTemp +"°C");
                    }
                    if(key.equals("temp_max"))
                    {
                        tvMaxTempValue.setText("MAX: "+ valueTemp + "°C");
                    }
                    if(key.equals("temp"))
                    {
                        tvTemp.setText(valueTemp + "°C");
                    }
                    if(key.equals("humidity"))
                    {
                        tvHumidity.setText(mainResponse.get(key).toString());
                        tvHumidityText.setText("Humidity");
                    }
                }

                //get resources to set the dynamic backgrounds according to weather conditions
                Resources res  = getResources();

                //JSONArray for weather key in json data and loop it for weather condition and its description
                JSONArray weather = origin.getJSONArray("weather");
                for (int i=0;i<weather.length();i++)
                {
                    JSONObject weatherItem = weather.getJSONObject(i);
                    tvAtmosphere.setText(weatherItem.getString("main"));
                    tvAtmosphereDesc.setText(weatherItem.getString("description"));
                }

                //Else-if ladder is used here for checking the type of conditions and setting the background according to weather
                //Get the drawable first, then set the image in drawable, get the layout in view and set the drawable in view to set the background
                if(tvAtmosphere.getText().toString().toLowerCase().contains("clouds") | tvAtmosphere.getText().toString().toLowerCase().contains("fog") | tvAtmosphere.getText().toString().toLowerCase().contains("haze"))
                {
                    Drawable drawable = ResourcesCompat.getDrawable(res,R.drawable.cloudy,null);
                    View view = (LinearLayout) findViewById( R.id.lv_weather);
                    view.setBackground(drawable);
                    view.getBackground().setAlpha(170);
                }
                else if(tvAtmosphere.getText().toString().toLowerCase().contains("rain") | tvAtmosphere.getText().toString().toLowerCase().contains("mist"))
                {
                    Drawable drawable = ResourcesCompat.getDrawable(res,R.drawable.rainy,null);
                    View view = (LinearLayout) findViewById( R.id.lv_weather);
                    view.setBackground(drawable);
                    view.getBackground().setAlpha(215);
                }
                else if(tvAtmosphere.getText().toString().toLowerCase().contains("thunderstorm"))
                {
                    Drawable drawable = ResourcesCompat.getDrawable(res,R.drawable.thunderstorm,null);
                    View view = (LinearLayout) findViewById( R.id.lv_weather);
                    view.setBackground(drawable);
                    view.getBackground().setAlpha(170);

                }
                else if(tvAtmosphere.getText().toString().toLowerCase().contains("sunny"))
                {
                    Drawable drawable = ResourcesCompat.getDrawable(res,R.drawable.sunny_weather,null);
                    View view = (LinearLayout) findViewById(R.id.lv_weather);
                    view.setBackground(drawable);
                    view.getBackground().setAlpha(170);
                }
                else if(tvAtmosphere.getText().toString().toLowerCase().contains("clear"))
                {
                    Drawable drawable = ResourcesCompat.getDrawable(res,R.drawable.clear,null);
                    View view = (LinearLayout) findViewById( R.id.lv_weather);
                    view.setBackground(drawable);
                    view.getBackground().setAlpha(170);

                }
                else if(tvAtmosphere.getText().toString().toLowerCase().contains("snow"))
                {
                    Drawable drawable = ResourcesCompat.getDrawable(res,R.drawable.snowy,null);
                    View view = (LinearLayout) findViewById( R.id.lv_weather);
                    view.setBackground(drawable);
                    view.getBackground().setAlpha(170);
                }
                //Get the jsonobject clouds and iterate through it to get the clouds percent and set it to textview
                JSONObject clouds= origin.getJSONObject("clouds");

                for(int i=0;i<clouds.names().length();i++)
                {
                    String key = clouds.names().getString(i);
                    if(key.equals("all"))
                    {
                        tvCloudsPercent.setText(clouds.get(key).toString());
                        tvCloudsText.setText("Clouds");
                    }
                }
            }
            catch (Exception e) {

                //if the cityname is not present in API , handling the error here
                e.printStackTrace();

                Toast.makeText(MainActivity.this, "Invalid City Name!", Toast.LENGTH_SHORT).show();
                setWidgetsNull();
            }
        }
    }
}

