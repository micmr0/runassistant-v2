package com.micmr0.runassistant.util.weather

import android.content.Context
import com.micmr0.runassistant.R
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import org.json.JSONObject
import org.json.JSONException

class WeatherUtil {
    companion object {

        private val IMG_URL = "http://openweathermap.org/img/w/"

        fun getWeather(pContext : Context, pLocation : String) : Weather {
            val data = getWeatherData(pContext, pLocation)

            val jObj = JSONObject(data)

            // We start extracting the info
            val loc = Location()

            val coordObj = getObject("coord", jObj)

            loc.coordinates = Coordinates()
            loc.coordinates.lat = (getFloat("lat", coordObj))
            loc.coordinates.lon = (getFloat("lon", coordObj))

            val sysObj = getObject("sys", jObj)
            loc.country = (getString("country", sysObj))
            loc.name = (getString("name", jObj))
            //weather.location = loc

            val weather = Weather()

            // We get weather info (This is an array)
            val jArr = jObj.getJSONArray("weather")

            // We use only the first value
            val jsonObject = jArr.getJSONObject(0)
            weather.mCurrentCondition = CurrentCondition()
            weather.mCurrentCondition.mWeatherId = getInt("id", jsonObject)
            weather.mCurrentCondition.mDescription = getString("description", jsonObject)

            when(weather.mCurrentCondition.mDescription.toLowerCase()) {
                "few clouds" -> weather.mCurrentCondition.mDescription = pContext.getString(R.string.few_clouds)
                "scattered clouds" -> weather.mCurrentCondition.mDescription = pContext.getString(R.string.scattered_clouds)
                "broken clouds" -> weather.mCurrentCondition.mDescription = pContext.getString(R.string.broken_clouds)
                "overcast clouds" -> weather.mCurrentCondition.mDescription = pContext.getString(R.string.overcast_clouds)
                "clear sky" -> weather.mCurrentCondition.mDescription = pContext.getString(R.string.clear_sky)
                "heavy intensity rain" -> weather.mCurrentCondition.mDescription = pContext.getString(R.string.heavy_intensity_rain)
                "light rain" -> weather.mCurrentCondition.mDescription = pContext.getString(R.string.light_rain)
            }

            weather.mCurrentCondition.mCondition = getString("main", jsonObject)
            weather.mCurrentCondition.mIcon = getString("icon", jsonObject)

            val mainObj = getObject("main", jObj)
            weather.mCurrentCondition.mHumidity = getInt("humidity", mainObj)
            weather.mCurrentCondition.mPressure = getInt("pressure", mainObj)
            weather.mTemperature = Temperature()
            weather.mTemperature.mMax = getFloat("temp_max", mainObj)
            weather.mTemperature.mMin = getFloat("temp_min", mainObj)
            weather.mTemperature.mTemp = getFloat("temp", mainObj)

            // Wind
            val wObj = getObject("wind", jObj)
            weather.mWind = Wind()
            weather.mWind.mSpeed = getFloat("speed", wObj)

            // Clouds
            val cObj = getObject("clouds", jObj)
            weather.mClouds = getInt("all", cObj)

            return weather
        }

        @Throws(JSONException::class)
        private fun getObject(tagName: String, jObj: JSONObject): JSONObject {
            return jObj.getJSONObject(tagName)
        }

        @Throws(JSONException::class)
        private fun getString(tagName: String, jObj: JSONObject): String {
            return jObj.getString(tagName)
        }

        @Throws(JSONException::class)
        private fun getFloat(tagName: String, jObj: JSONObject): Float {
            return jObj.getDouble(tagName).toFloat()
        }

        @Throws(JSONException::class)
        private fun getInt(tagName: String, jObj: JSONObject): Int {
            return jObj.getInt(tagName)
        }

        private fun getWeatherData(pContext : Context, location: String): String? {
            var con: HttpURLConnection? = null
            var `is`: InputStream? = null

            try {
                con = URL(pContext.getString(R.string.weather_api_link, location)).openConnection() as HttpURLConnection
                con.requestMethod = "GET"
                con.doInput = true
                con.doOutput = true
                con.connect()

                // Let's read the response
                val buffer = StringBuffer()
                `is` = con.inputStream
                val br = BufferedReader(InputStreamReader(`is`))
                var line: String? = br.readLine()
                while (line != null) {
                    buffer.append(line + "rn")
                    line = br.readLine()
                }

                `is`!!.close()
                con.disconnect()
                return buffer.toString()
            } catch (t: Throwable) {
                t.printStackTrace()
            } finally {
                try {
                    `is`!!.close()
                } catch (t: Throwable) {
                }

                try {
                    con!!.disconnect()
                } catch (t: Throwable) {
                }
            }
            return null
        }
    }
}