package com.example.kendalsasus.androidlabs;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static java.lang.System.in;

public class WeatherForecast extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_forecast);
        ProgressBar progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        ForecastQuery fq = new ForecastQuery();
        fq.execute("http://api.openweathermap.org/data/2.5/weather?q=ottawa,ca&APPID=7e943c97096a9784391a981c4d878b22&mode=xml&units=metric");

    }

    public class ForecastQuery extends AsyncTask<String, Integer, String> {
        public String min, max, current, iconName;

        public Bitmap weatherPic;

        @Override
        public String doInBackground(String... args) {
            InputStream iStream;
            float i = 0;
            while (i < args.length) {
                try {
                    URL url = new URL(args[(int) i]);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    iStream = urlConnection.getInputStream();
                    XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                    factory.setNamespaceAware(false);
                    XmlPullParser xpp = factory.newPullParser();
                    xpp.setInput(iStream, "UTF-8");

                    String currentTag = "";


                    while (xpp.getEventType() != XmlPullParser.END_DOCUMENT) {


                        switch (xpp.getEventType()) {
                            case XmlPullParser.START_DOCUMENT:
                                currentTag = xpp.getName();
                                String parameter = xpp.getAttributeValue(null, "message");
                                Log.i("First open tag", currentTag + " message:" + parameter);
                                break;
                            case XmlPullParser.START_TAG:
                                currentTag = xpp.getName();
                                Log.i("Opening tag", currentTag);
                            case XmlPullParser.END_TAG:
                                Log.i("Closing tag", currentTag);
                                break;
                            case XmlPullParser.TEXT:
                                currentTag = xpp.getText();
                                Log.i("text tag", currentTag);
                                break;

                        }


                        if (currentTag != null && currentTag.equals("temperature") && xpp.getEventType() == XmlPullParser.START_TAG) {
                            current = xpp.getAttributeValue(null, "value");
                            publishProgress(25);
                            min = xpp.getAttributeValue(null, "min");
                            max = xpp.getAttributeValue(null, "max");
                            publishProgress(50);
                        } else if (currentTag != null && currentTag.equals("weather") && iconName == null) {
                            iconName = xpp.getAttributeValue(null, "icon") + ".png";
                            publishProgress(75);
                        }
                        xpp.next();
                        i++;
                    }

                } catch (XmlPullParserException xppe) {
                    Log.e("ERROR", "ParserException");

                } catch (MalformedURLException mue) {
                    Log.e("ERROR", "MalformedURLException");
                } catch (IOException ioe) {
                    Log.e("ERROR", "IOException");
                }

            }

            if(fileExistence(iconName)){
                FileInputStream fis = null;
                try {    fis = openFileInput(iconName);   }
                catch (FileNotFoundException e) {    e.printStackTrace();  }
                Bitmap bm = BitmapFactory.decodeStream(fis);
                weatherPic = bm;
                Log.i("Load", "File was loaded from directory");
                publishProgress(100);
            }
            else {
                try {
                    HttpUtils httpUtils = new HttpUtils();
                    weatherPic = httpUtils.getImage("http://openweathermap.org/img/w/" + iconName);
                    FileOutputStream outputStream = openFileOutput(iconName, Context.MODE_PRIVATE);
                    weatherPic.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
                    outputStream.flush();
                    outputStream.close();
                    Log.i("Download", "File was downloaded");
                    publishProgress(100);
                } catch (IOException e) {


                }
            }
            return "Go to OnPostExecute";
        }

        public void onProgressUpdate(Integer... value) {
            ProgressBar progressBar = findViewById(R.id.progressBar);
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(value[0]);
        }

        public void onPostExecute(String s) {
            TextView currentWeather = findViewById(R.id.currentTemp);
            current = currentWeather.getText() + current;
            currentWeather.setText(current);

            TextView minWeather = findViewById(R.id.minTemp);
            min = minWeather.getText() + min;
            minWeather.setText(min);

            TextView maxWeather = findViewById(R.id.maxTemp);
            max = maxWeather.getText() + max;
            maxWeather.setText(max);

            //update the bitmap image once you have done that part
            ImageView iv = findViewById(R.id.weatherImageView);
            iv.setImageBitmap(weatherPic);


            //sets the progress bar to invisible
            ProgressBar progressBar = findViewById(R.id.progressBar);
            progressBar.setVisibility(View.INVISIBLE);

        }

        public boolean fileExistence(String fname){
            File file = getBaseContext().getFileStreamPath(fname);
            return file.exists();   }



    }


    class HttpUtils {
        public Bitmap getImage(URL url) {
            HttpURLConnection connection = null;
            try {
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                int responseCode = connection.getResponseCode();
                if (responseCode == 200) {
                    return BitmapFactory.decodeStream(connection.getInputStream());
                } else
                    return null;
            } catch (Exception e) {
                return null;
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        }

        public Bitmap getImage(String urlString) {
            try {
                URL url = new URL(urlString);
                return getImage(url);
            } catch (MalformedURLException e) {
                return null;
            }
        }

    }
}


