package com.micmr0.runassistant.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.location.Location
import android.util.Log
import com.google.android.gms.maps.model.LatLng
import com.micmr0.runassistant.util.Achievement
import java.text.SimpleDateFormat

import com.micmr0.runassistant.util.Logger
import com.micmr0.runassistant.util.SharedPreferencesHelper
import com.micmr0.runassistant.util.weather.Weather
import java.util.*

/**
 * Created by Michał on 04.09.2015.
 */
class DatabaseHelper(private val mContext: Context) : SQLiteOpenHelper(mContext, GPStracking.DATABASE_NAME, null, GPStracking.DATABASE_VERSION) {

    //Log.d(TAG, "Cursor is: " + c);
    // dodawanie wszystkich planow do listy
    // Adding contact to list
    val plans: List<PlanDao>
        get() {
            val sqldb = writableDatabase
            val values = ContentValues()

            val plans = ArrayList<PlanDao>()

            val projection_plan = arrayOf<String>(GPStracking.Plans._ID, GPStracking.Plans.TAB)

            val c = sqldb.rawQuery("SELECT * FROM " + GPStracking.Plans.TABLE, null)
            if (c.moveToFirst() && c.count != 0) {
                do {
                    val planId = c.getInt(c.getColumnIndexOrThrow(GPStracking.Plans._ID))
                    val plan = PlanDao(planId)

                    Log.v(TAG, "Plan mId: $planId")


                    val tableId = c.getLong(c.getColumnIndexOrThrow(GPStracking.Plans.TAB))

                    Log.v(TAG, "Table mId: $tableId")

                    val d = sqldb.rawQuery("SELECT * FROM " + GPStracking.Tables.TABLE + " WHERE " + GPStracking.Tables._ID + "=" + tableId, null)

                    d.moveToFirst()

                    val table = TableDao()

                    table.id = (d.getLong(d.getColumnIndexOrThrow(GPStracking.Tables._ID)))
                    table.title = (d.getString(d.getColumnIndexOrThrow(GPStracking.Tables.TITLE)))
                    table.goal = (d.getString(d.getColumnIndexOrThrow(GPStracking.Tables.GOAL)))
                    table.description = (d.getString(d.getColumnIndexOrThrow(GPStracking.Tables.DESCRIPTION)))
                    table.days = (d.getInt(d.getColumnIndexOrThrow(GPStracking.Tables.DAYS)))

                    d.close()

                    Logger.d(this, "ID: ${table.id}  title: ${table.title}")

                    plan.table = (table)
                    plans.add(plan)
                } while (c.moveToNext())

            }

            sqldb.close()

            c.close()

            return plans

        }

    // dodawanie wszystkich treningow do listy
    // Adding contact to list
    val trainings: List<TrainingDao>
        get() {
            val sqldb = writableDatabase
            val values = ContentValues()

            val trainings = ArrayList<TrainingDao>()


            val c = sqldb.rawQuery("SELECT * FROM " + GPStracking.Trainings.TABLE, null)
            if (c.moveToFirst() && c.count != 0) {
                do {
                    val trainingId = c.getLong(c.getColumnIndexOrThrow(GPStracking.Trainings._ID))
                    val training = TrainingDao(trainingId)

                    training.mName = (c.getString(c.getColumnIndexOrThrow(GPStracking.Trainings.NAME)))
                    training.mCreationDate = (c.getString(c.getColumnIndexOrThrow(GPStracking.Trainings.CREATION_DATE)))
                    val trainingType = c.getString(c.getColumnIndexOrThrow(GPStracking.Trainings.TRAINING_TYPE))
                    training.mType = if (trainingType == "running") {
                        TrainingDao.TrainingType.RUNNING
                    } else {
                        TrainingDao.TrainingType.CYCLING
                    }
                    training.mTime = (c.getInt(c.getColumnIndexOrThrow(GPStracking.Trainings.TIME)))
                    training.mDistance = (c.getFloat(c.getColumnIndexOrThrow(GPStracking.Trainings.DISTANCE)))
                    training.mLocation = (c.getString(c.getColumnIndexOrThrow(GPStracking.Trainings.LOCATION)))
                    training.mCalories = (c.getInt(c.getColumnIndexOrThrow(GPStracking.Trainings.CALORIES)))
                    trainings.add(training)
                } while (c.moveToNext())

            }

            sqldb.close()

            c.close()

            return trainings

        }

    val summary: TrainingDao
        get() {
            val sqldb = writableDatabase
            val values = ContentValues()


            val c = sqldb.rawQuery("SELECT * FROM " + GPStracking.Trainings.TABLE, null)

            val training_temp = TrainingDao()

            if (c.moveToFirst() && c.count != 0) {

                do {
                    training_temp.mTime = c.getInt(c.getColumnIndexOrThrow(GPStracking.Trainings.TIME))
                    training_temp.mDistance = c.getFloat(c.getColumnIndexOrThrow(GPStracking.Trainings.DISTANCE))
                    training_temp.mMaxSpeed = c.getInt(c.getColumnIndexOrThrow(GPStracking.Trainings.MAX_SPEED))


                } while (c.moveToNext())
            }

            c.close()
            sqldb.close()

            return training_temp

        }

    override fun onOpen(db: SQLiteDatabase) {
        super.onOpen(db)
        db.execSQL("PRAGMA foreign_keys=ON")
    }

    override fun onCreate(db: SQLiteDatabase) {

        db.execSQL(GPStracking.Plans.CREATE_STATEMENT)
        db.execSQL(GPStracking.PlanDays.CREATE_STATEMENT)
        db.execSQL(GPStracking.Tables.CREATE_STATEMENT)
        db.execSQL(GPStracking.TableDays.CREATE_STATEMENT)
        db.execSQL(GPStracking.Tips.CREATE_STATEMENT)

        db.execSQL(GPStracking.Tables.INSERT_STATEMENT)
        db.execSQL(GPStracking.Tables.INSERT_STATEMENT2)
        db.execSQL(GPStracking.Tables.INSERT_STATEMENT3)
        db.execSQL(GPStracking.Tables.INSERT_STATEMENT4)

        db.execSQL(GPStracking.TableDays.INSERT_STATEMENT)
        db.execSQL(GPStracking.TableDays.INSERT_STATEMENT2)
        db.execSQL(GPStracking.TableDays.INSERT_STATEMENT3)
        db.execSQL(GPStracking.TableDays.INSERT_STATEMENT4)

        db.execSQL(GPStracking.Tips.INSERT_STATEMENT)

        db.execSQL(GPStracking.Runpoints.CREATE_STATEMENT)
        db.execSQL(GPStracking.Laps.CREATE_STATEMENT)
        db.execSQL(GPStracking.Trainings.CREATE_STATEMENT)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {

    }

    fun createPlan5kmUk(): Long {
        var sqldb = writableDatabase

        val values = ContentValues()
        values.put("tab", 1)

        val planId = sqldb.insert("plans", null, values)

        sqldb.close()
        sqldb = writableDatabase

        val values_day = ContentValues()
        values_day.put("plan", planId)

        for (i in 1..56) {
            values_day.put("day", i)
            sqldb.insert("plandays", null, values_day)
        }

        sqldb.close()

        return planId
    }

    fun createPlan5km45min(): Long {
        var sqldb = writableDatabase

        val values = ContentValues()
        values.put("tab", 2)

        val planId = sqldb.insert("plans", null, values)

        sqldb.close()
        sqldb = writableDatabase

        val values_day = ContentValues()
        values_day.put("plan", planId)

        for (i in 1..56) {
            values_day.put("day", i)
            sqldb.insert("plandays", null, values_day)
        }

        sqldb.close()

        return planId
    }

    fun createPlan10kmUk(): Long {
        var sqldb = writableDatabase

        val values = ContentValues()
        values.put("tab", 3)

        val planId = sqldb.insert("plans", null, values)

        sqldb.close()
        sqldb = writableDatabase

        val values_day = ContentValues()
        values_day.put("plan", planId)

        for (i in 1..105) {
            values_day.put("day", i)
            sqldb.insert("plandays", null, values_day)
        }

        sqldb.close()

        return planId
    }

    fun createPlanPolMaratonUk(): Long {
        var sqldb = writableDatabase

        val values = ContentValues()
        values.put("tab", 4)

        val planId = sqldb.insert("plans", null, values)

        sqldb.close()
        sqldb = writableDatabase

        val values_day = ContentValues()
        values_day.put("plan", planId)

        for (i in 1..105) {
            values_day.put("day", i)
            sqldb.insert("plandays", null, values_day)
        }

        sqldb.close()

        return planId
    }

    fun getPlan(planId: Int): PlanDao {

        val sqldb = writableDatabase

        val plan = PlanDao(planId)

        val c = sqldb.rawQuery("SELECT * FROM " + GPStracking.Plans.TABLE + " WHERE " + GPStracking.Plans._ID + "=" + planId, null)

        Log.v(TAG, "Cursor is: $c")

        // dodawanie wszystkich planow do listy
        if (c.moveToFirst() && c.count != 0) {

            Log.v(TAG, "Plan mId: $planId")

            val tableId = c.getLong(c.getColumnIndexOrThrow(GPStracking.Plans.TAB))

            Log.v(TAG, "Table mId: $tableId")

            val d = sqldb.rawQuery("SELECT * FROM " + GPStracking.Tables.TABLE + " WHERE " + GPStracking.Tables._ID + "=" + tableId, null)

            d.moveToFirst()

            val table = TableDao()

            table.id = (d.getLong(d.getColumnIndexOrThrow(GPStracking.Tables._ID)))
            table.title = (d.getString(d.getColumnIndexOrThrow(GPStracking.Tables.TITLE)))
            table.goal = (d.getString(d.getColumnIndexOrThrow(GPStracking.Tables.GOAL)))
            table.description = (d.getString(d.getColumnIndexOrThrow(GPStracking.Tables.DESCRIPTION)))
            table.days = (d.getInt(d.getColumnIndexOrThrow(GPStracking.Tables.DAYS)))

            Logger.d(this, "ID: ${table.id} title: ${table.title} ")

            plan.table = (table)
        }

        sqldb.close()

        return plan

    }

    fun getPlanDay(planId: Int, day: Int): PlanDayDao {
        val sqldb = writableDatabase
        val values = ContentValues()

        val planDay = PlanDayDao()

        val b = sqldb.rawQuery("SELECT * FROM " + GPStracking.PlanDays.TABLE + " WHERE " + GPStracking.PlanDays.PLAN + "=" + planId + " AND " + GPStracking.PlanDays.DAY + "=" + day, null)
        b.moveToFirst()

        val planDayId = b.getInt(b.getColumnIndexOrThrow(GPStracking.PlanDays._ID))
        val training = b.getInt(b.getColumnIndexOrThrow(GPStracking.PlanDays.TRAINING))
        val date = b.getString(b.getColumnIndexOrThrow(GPStracking.PlanDays.DATE))

        val c = sqldb.rawQuery("SELECT * FROM " + GPStracking.Plans.TABLE + " WHERE " + GPStracking.Plans._ID + "=" + planId, null)


        //Log.d(TAG, "Cursor is: "+c);

        // dodawanie wszystkich planow do listy
        if (c.moveToFirst() && c.count != 0) {

            val table = c.getLong(c.getColumnIndexOrThrow(GPStracking.Plans.TAB))

            val d = sqldb.rawQuery("SELECT * FROM " + GPStracking.TableDays.TABLE + " WHERE " + GPStracking.TableDays.TAB + "=" + table + " AND " + GPStracking.TableDays.DAY + "=" + day, null)

            d.moveToFirst()


            val type = d.getInt(d.getColumnIndexOrThrow(GPStracking.TableDays.TYPE))
            val title = d.getString(d.getColumnIndexOrThrow(GPStracking.TableDays.TITLE))
            val distance = d.getFloat(d.getColumnIndexOrThrow(GPStracking.TableDays.DISTANCE))
            val time = d.getInt(d.getColumnIndexOrThrow(GPStracking.TableDays.TIME))
            val laps = d.getInt(d.getColumnIndexOrThrow(GPStracking.TableDays.LAPS))
            val tips = d.getInt(d.getColumnIndexOrThrow(GPStracking.TableDays.TIPS))

            d.close()

            planDay.id = (planDayId)
            planDay.plan = (planId)
            planDay.day = (day)
            planDay.training = (training)
            planDay.date = (date)
            planDay.type = (type)
            planDay.title = (title)
            planDay.distance = (distance)
            planDay.time = (time)
            planDay.laps = (laps)
            planDay.tips = (tips)
        }

        c.close()

        sqldb.close()

        return planDay

    }

    fun getPlanDays(planId: Int): ArrayList<PlanDayDao> {
        val sqldb = writableDatabase
        val values = ContentValues()

        val planDays = ArrayList<PlanDayDao>()

        val c = sqldb.rawQuery("SELECT * FROM " + GPStracking.PlanDays.TABLE + " WHERE " + GPStracking.PlanDays.PLAN + "=" + planId, null)


        //Log.d(TAG, "Cursor is: "+c);

        // dodawanie wszystkich planow do listy
        if (c.moveToFirst() && c.count != 0) {
            do {
                val planDayId = c.getInt(c.getColumnIndexOrThrow(GPStracking.PlanDays._ID))
                val plan = PlanDao(planId)

                Log.v(TAG, "Plan mId: $planId")

                val plan_id = c.getInt(c.getColumnIndexOrThrow(GPStracking.PlanDays.PLAN))
                val day = c.getInt(c.getColumnIndexOrThrow(GPStracking.PlanDays.DAY))
                val training = c.getInt(c.getColumnIndexOrThrow(GPStracking.PlanDays.TRAINING))
                val date = c.getString(c.getColumnIndexOrThrow(GPStracking.PlanDays.DATE))


                val d = sqldb.rawQuery("SELECT * FROM " + GPStracking.Plans.TABLE + " WHERE " + GPStracking.Plans._ID + "=" + plan_id, null)

                d.moveToFirst()

                val table_id = d.getLong(d.getColumnIndexOrThrow(GPStracking.Plans.TAB))

                d.close()

                val e = sqldb.rawQuery("SELECT * FROM " + GPStracking.TableDays.TABLE + " WHERE " + GPStracking.TableDays.TAB + "=" + table_id + " AND " + GPStracking.TableDays.DAY + "=" + day, null)


                //Log.d(TAG, "curosor e is " + e);

                e.moveToFirst()
                val type = e.getInt(e.getColumnIndexOrThrow(GPStracking.TableDays.TYPE))
                val title = e.getString(e.getColumnIndexOrThrow(GPStracking.TableDays.TITLE))
                val distance = e.getFloat(e.getColumnIndexOrThrow(GPStracking.TableDays.DISTANCE))
                val time = e.getInt(e.getColumnIndexOrThrow(GPStracking.TableDays.TIME))
                val laps = e.getInt(e.getColumnIndexOrThrow(GPStracking.TableDays.LAPS))
                val tips = e.getInt(e.getColumnIndexOrThrow(GPStracking.TableDays.TIPS))

                e.close()

                val f = sqldb.rawQuery("SELECT * FROM " + GPStracking.Tips.TABLE + " WHERE " + GPStracking.Tips._ID + "=" + tips, null)
                f.moveToFirst()
                val tipDescription = f.getString(f.getColumnIndexOrThrow(GPStracking.Tips.DESCRIPTION))

                f.close()

                val planDay = PlanDayDao()

                planDay.id = (planDayId)
                planDay.plan = (plan_id)
                planDay.day = (day)
                planDay.training = (training)
                planDay.date = (date)
                planDay.type = (type)
                planDay.title = (title)
                planDay.distance = (distance)
                planDay.time = (time)
                planDay.laps = (laps)
                planDay.tips = (tips)


                //Log.d(TAG, "Title: " + planDay.getTitle());

                //Log.d(TAG, "ID: " + planDay.getMId() + " title: " + planDay.getTitle());

                // Adding contact to list
                planDays.add(planDay)
            } while (c.moveToNext())

        }

        sqldb.close()

        c.close()

        return planDays

    }

    fun setPlanDayDate(planId: Int, day: Int): String {
        val sqldb = writableDatabase

        val b = sqldb.rawQuery("SELECT * FROM " + GPStracking.PlanDays.TABLE + " WHERE " + GPStracking.PlanDays.PLAN + "=" + planId + " AND " + GPStracking.PlanDays.DAY + "=" + day, null)

        b.moveToFirst()

        val plandayId = b.getInt(b.getColumnIndexOrThrow(GPStracking.PlanDays._ID))

        Log.v("PLANDAY", "Planday mId: $plandayId")

        val values = ContentValues()

        val now = Date()

        val formatter = SimpleDateFormat("EEEE ', ' dd-MM-yyyy 'godz. 'HH:mm")
        val currentDateTimeString = formatter.format(now)

        values.put("date", currentDateTimeString)

        sqldb.update("plandays", values, GPStracking.PlanDays._ID + " = " + plandayId, null)
        sqldb.close()

        b.close()

        return currentDateTimeString

    }

    fun getTip(tipId: Long): String {
        val sqldb = writableDatabase

        val b = sqldb.rawQuery("SELECT * FROM " + GPStracking.Tips.TABLE + " WHERE " + GPStracking.Tips._ID + "=" + tipId, null)
        b.moveToFirst()

        val description = b.getString(b.getColumnIndexOrThrow(GPStracking.Tips.DESCRIPTION))

        sqldb.close()

        b.close()

        return description

    }

    fun startNewTraining(name: String, pLocation: String): Long {

        var calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val mWeekOfYear = calendar.get(Calendar.WEEK_OF_YEAR)

        val sqldb = writableDatabase

        val trainingType = if (SharedPreferencesHelper.getActivityType() == 0) {
            "running"
        } else {
            "cycling"
        }

        val values = ContentValues()
        values.put(GPStracking.Trainings.NAME, name)
        values.put(GPStracking.Trainings.CREATION_DATE, calendar.timeInMillis.toString())
        values.put(GPStracking.Trainings.YEAR, year)
        values.put(GPStracking.Trainings.MONTH, month)
        values.put(GPStracking.Trainings.WEEK_OF_YEAR, mWeekOfYear)
        values.put(GPStracking.Trainings.TRAINING_TYPE, trainingType)
        values.put(GPStracking.Trainings.TIME, "")
        values.put(GPStracking.Trainings.DISTANCE, "")
        values.put(GPStracking.Trainings.AVERAGE_SPEED, "")
        values.put(GPStracking.Trainings.MAX_SPEED, "")
        values.put(GPStracking.Trainings.MIN_ALTITUDE, "")
        values.put(GPStracking.Trainings.MAX_ALTITUDE, "")
        values.put(GPStracking.Trainings.CALORIES, "")
        values.put(GPStracking.Trainings.LOCATION, pLocation)
        values.put(GPStracking.Trainings.WEATHER_TEMPERATURE, "")
        values.put(GPStracking.Trainings.WEATHER_DESCRIPTION, "")


        //Log.d(TAG , "tworze nowy trening");
        val trainingId = sqldb.insert("trainings", null, values)
        sqldb.close()

        return trainingId
    }

    fun startNewLap(training_id: Long): Long {

        val sqldb = writableDatabase

        val values = ContentValues()
        values.put("training", training_id)
        values.put("time", "")
        values.put("average_speed", "")

        val lapId = sqldb.insert("laps", null, values)
        sqldb.close()

        return lapId
    }

    fun insertNewRunpoint(last_lap_id: Long, loc: Location): Long {

        val sqldb = writableDatabase
        val values = ContentValues()
        values.put("lap", last_lap_id)
        values.put("time", loc.time)
        values.put("latitude", loc.latitude)
        values.put("longtitude", loc.longitude)
        values.put("speed", loc.speed)
        values.put("accuracy", loc.accuracy)
        values.put("altitude", loc.altitude)
        values.put("bearing", loc.bearing)

        val runPointId = sqldb.insert("runpoints", null, values)
        sqldb.close()

        return runPointId

    }

    fun saveLap(mLapId: Long, time: Long, average_speed: Long) {

        val sqldb = writableDatabase
        val values = ContentValues()

        values.put("time", time)
        values.put("average_speed", average_speed)

        sqldb.update("laps", values, GPStracking.Laps._ID + " = " + mLapId, null)
        sqldb.close()
    }

    fun saveTraining(mTrainingId: Long, time: Int, distance: Float, average_speed: Float, max_speed: Float, min_altitude: Double, max_altitude: Double, calories: Int) {

        Logger.d(this, "SAVE: training mId: $mTrainingId, mTime: $time, mDistance: $distance, mAverageSpeed: $average_speed, mMaxSpeed: $max_speed" +
                "mMinAltitude: $min_altitude, mMaxAltitude: $max_altitude, mCalories: $calories")

        val sqldb = writableDatabase
        val values = ContentValues()

        values.put(GPStracking.Trainings.TIME, time)
        values.put(GPStracking.Trainings.DISTANCE, distance)
        values.put(GPStracking.Trainings.AVERAGE_SPEED, average_speed)
        values.put(GPStracking.Trainings.MAX_SPEED, max_speed)
        values.put(GPStracking.Trainings.MIN_ALTITUDE, min_altitude)
        values.put(GPStracking.Trainings.MAX_ALTITUDE, max_altitude)
        values.put(GPStracking.Trainings.CALORIES, calories)

        sqldb.update("trainings", values, GPStracking.Trainings._ID + " = " + mTrainingId, null)
        sqldb.close()
    }

    fun saveTraining(mTrainingId: Long, time: Int, distance: Float, average_speed: Float, max_speed: Float, min_altitude: Double, max_altitude: Double, calories: Int, weather: Weather) {

        Logger.d(this, "SAVE: training mId: $mTrainingId, mTime: $time, mDistance: $distance, mAverageSpeed: $average_speed, mMaxSpeed: $max_speed" +
                "mMinAltitude: $min_altitude, mMaxAltitude: $max_altitude, mCalories: $calories")

        val sqldb = writableDatabase
        val values = ContentValues()

        values.put(GPStracking.Trainings.TIME, time)
        values.put(GPStracking.Trainings.DISTANCE, distance)
        values.put(GPStracking.Trainings.AVERAGE_SPEED, average_speed)
        values.put(GPStracking.Trainings.MAX_SPEED, max_speed)
        values.put(GPStracking.Trainings.MIN_ALTITUDE, min_altitude)
        values.put(GPStracking.Trainings.MAX_ALTITUDE, max_altitude)
        values.put(GPStracking.Trainings.CALORIES, calories)
        values.put(GPStracking.Trainings.WEATHER_TEMPERATURE, weather.mTemperature.mTemp)
        values.put(GPStracking.Trainings.WEATHER_WIND_SPEED, weather.mWind.mSpeed)
        values.put(GPStracking.Trainings.WEATHER_HUMIDITY, weather.mCurrentCondition.mHumidity)
        values.put(GPStracking.Trainings.WEATHER_PRESSURE, weather.mCurrentCondition.mPressure)
        values.put(GPStracking.Trainings.WEATHER_ICON, weather.mCurrentCondition.mIcon)
        values.put(GPStracking.Trainings.WEATHER_DESCRIPTION, weather.mCurrentCondition.mDescription)

        sqldb.update("trainings", values, GPStracking.Trainings._ID + " = " + mTrainingId, null)
        sqldb.close()
    }

    fun savePlanDay(planId: Int, day: Int, trainingId: Long) {

        val now = Date()

        val formatter = SimpleDateFormat("EEEE ', ' dd-MM-yyyy 'godz. 'HH:mm")
        val currentDateTimeString = formatter.format(now)

        val sqldb = writableDatabase

        val b = sqldb.rawQuery("SELECT * FROM " + GPStracking.PlanDays.TABLE + " WHERE " + GPStracking.PlanDays.PLAN + "=" + planId + " AND " + GPStracking.PlanDays.DAY + "=" + day, null)

        b.moveToFirst()

        val plandayId = b.getInt(b.getColumnIndexOrThrow(GPStracking.PlanDays._ID))

        Log.v("PLANDAY", "Planday mId: $plandayId")

        val values = ContentValues()

        values.put("training", trainingId)
        values.put("date", currentDateTimeString)

        sqldb.update("plandays", values, GPStracking.PlanDays._ID + " = " + plandayId, null)
        sqldb.close()

        b.close()

    }

    fun getWeekAchievments() : Achievement {

        val now = Calendar.getInstance()

        val sqldb = writableDatabase

        var b = sqldb.rawQuery("SELECT SUM(DISTANCE) FROM " + GPStracking.Trainings.TABLE + " WHERE " + GPStracking.Trainings.YEAR + "=" + now.get(Calendar.YEAR) + " AND " + GPStracking.Trainings.WEEK_OF_YEAR + "=" + now.get(Calendar.WEEK_OF_YEAR), null)

        b.moveToFirst()

        val distance = b.getFloat(b.getColumnIndexOrThrow("SUM(DISTANCE)"))

        b.close()

        b = sqldb.rawQuery("SELECT SUM(TIME) FROM " + GPStracking.Trainings.TABLE + " WHERE " + GPStracking.Trainings.YEAR + "=" + now.get(Calendar.YEAR) + " AND " + GPStracking.Trainings.WEEK_OF_YEAR + "=" + now.get(Calendar.WEEK_OF_YEAR), null)

        b.moveToFirst()

        val time = b.getInt(b.getColumnIndexOrThrow("SUM(TIME)"))

        b.close()

        b = sqldb.rawQuery("SELECT SUM(CALORIES) FROM " + GPStracking.Trainings.TABLE + " WHERE " + GPStracking.Trainings.YEAR + "=" + now.get(Calendar.YEAR) + " AND " + GPStracking.Trainings.WEEK_OF_YEAR + "=" + now.get(Calendar.WEEK_OF_YEAR), null)

        b.moveToFirst()

        val calories = b.getInt(b.getColumnIndexOrThrow("SUM(CALORIES)"))

        b.close()

        return Achievement(distance, time, calories)
    }

    fun getMonthAchievments() : Achievement {

        val now = Calendar.getInstance()

        val sqldb = writableDatabase

        var b = sqldb.rawQuery("SELECT SUM(DISTANCE) FROM " + GPStracking.Trainings.TABLE + " WHERE " + GPStracking.Trainings.YEAR + "=" + now.get(Calendar.YEAR) + " AND " + GPStracking.Trainings.MONTH + "=" + now.get(Calendar.MONTH), null)

        b.moveToFirst()

        val distance = b.getFloat(b.getColumnIndexOrThrow("SUM(DISTANCE)"))

        b.close()

        b = sqldb.rawQuery("SELECT SUM(TIME) FROM " + GPStracking.Trainings.TABLE + " WHERE " + GPStracking.Trainings.YEAR + "=" + now.get(Calendar.YEAR) + " AND " + GPStracking.Trainings.MONTH + "=" + now.get(Calendar.MONTH), null)

        b.moveToFirst()

        val time = b.getInt(b.getColumnIndexOrThrow("SUM(TIME)"))

        b.close()

        b = sqldb.rawQuery("SELECT SUM(CALORIES) FROM " + GPStracking.Trainings.TABLE + " WHERE " + GPStracking.Trainings.YEAR + "=" + now.get(Calendar.YEAR) + " AND " + GPStracking.Trainings.MONTH + "=" + now.get(Calendar.MONTH), null)

        b.moveToFirst()

        val calories = b.getInt(b.getColumnIndexOrThrow("SUM(CALORIES)"))

        b.close()

        return Achievement(distance, time, calories)
    }

    fun deleteTraining(trainingId: Long) {

        val sqldb = writableDatabase

        sqldb.delete("trainings", GPStracking.Trainings._ID + " = " + trainingId, null)

        val c = sqldb.rawQuery("SELECT * FROM " + GPStracking.PlanDays.TABLE + " WHERE " + GPStracking.PlanDays.TRAINING + "=" + trainingId, null)

        if (c.moveToFirst() && c.count != 0) {
            val planDayId = c.getLong(c.getColumnIndexOrThrow(GPStracking.PlanDays._ID))

            val values = ContentValues()
            values.putNull("training")
            values.putNull("date")

            sqldb.update("plandays", values, GPStracking.PlanDays._ID + " = " + planDayId, null)
        }

        c.close()

        sqldb.close()
    }

    fun deletePlan(planId: Int) {

        val sqldb = writableDatabase

        sqldb.delete("plans", GPStracking.Plans._ID + " = " + planId, null)

        sqldb.close()
    }

    fun getTrainingInfo(training_id: Long): TrainingDao {
        val sqldb = writableDatabase
        val values = ContentValues()

        val training_temp = TrainingDao(training_id)

        val projection_training = arrayOf<String>(GPStracking.Trainings._ID, GPStracking.Trainings.NAME, GPStracking.Trainings.CREATION_DATE, GPStracking.Trainings.YEAR, GPStracking.Trainings.MONTH, GPStracking.Trainings.WEEK_OF_YEAR, GPStracking.Trainings.TIME, GPStracking.Trainings.DISTANCE, GPStracking.Trainings.AVERAGE_SPEED, GPStracking.Trainings.MAX_SPEED, GPStracking.Trainings.MIN_ALTITUDE, GPStracking.Trainings.MAX_ALTITUDE, GPStracking.Trainings.CALORIES, GPStracking.Trainings.LOCATION, GPStracking.Trainings.WEATHER_TEMPERATURE, GPStracking.Trainings.WEATHER_HUMIDITY, GPStracking.Trainings.WEATHER_PRESSURE, GPStracking.Trainings.WEATHER_ICON, GPStracking.Trainings.WEATHER_WIND_SPEED, GPStracking.Trainings.WEATHER_DESCRIPTION)


        var c = sqldb.query(
                GPStracking.Trainings.TABLE, // The table to query
                projection_training, // The columns to return
                GPStracking.Trainings._ID + "=?", // The columns for the WHERE clause
                arrayOf(java.lang.Long.toString(training_id)), null, null, null, null
        )// The values for the WHERE clause
        // don't group the rows
        // don't filter by row groups
        // The sort order

        c.moveToFirst()
        training_temp.mId = (c.getLong(c.getColumnIndexOrThrow(GPStracking.Trainings._ID)))
        training_temp.mName = (c.getString(c.getColumnIndexOrThrow(GPStracking.Trainings.NAME)))
        training_temp.mCreationDate = (c.getString(c.getColumnIndexOrThrow(GPStracking.Trainings.CREATION_DATE)))
        training_temp.mYear = (c.getString(c.getColumnIndexOrThrow(GPStracking.Trainings.YEAR)))
        training_temp.mMonth = (c.getString(c.getColumnIndexOrThrow(GPStracking.Trainings.MONTH)))
        training_temp.mWeekOfYear = (c.getString(c.getColumnIndexOrThrow(GPStracking.Trainings.WEEK_OF_YEAR)))
        training_temp.mTime = (c.getInt(c.getColumnIndexOrThrow(GPStracking.Trainings.TIME)))
        training_temp.mDistance = (c.getFloat(c.getColumnIndexOrThrow(GPStracking.Trainings.DISTANCE)))
        training_temp.mAverageSpeed = (c.getInt(c.getColumnIndexOrThrow(GPStracking.Trainings.AVERAGE_SPEED)))
        training_temp.mMaxSpeed = (c.getInt(c.getColumnIndexOrThrow(GPStracking.Trainings.MAX_SPEED)))
        training_temp.mMinAltitude = (c.getInt(c.getColumnIndexOrThrow(GPStracking.Trainings.MIN_ALTITUDE)))
        training_temp.mMaxAltitude = (c.getInt(c.getColumnIndexOrThrow(GPStracking.Trainings.MAX_ALTITUDE)))
        training_temp.mCalories = (c.getInt(c.getColumnIndexOrThrow(GPStracking.Trainings.CALORIES)))
        training_temp.mLocation = (c.getString(c.getColumnIndexOrThrow(GPStracking.Trainings.LOCATION)))
        training_temp.mWeatherTemperature = (c.getFloat(c.getColumnIndexOrThrow(GPStracking.Trainings.WEATHER_TEMPERATURE)))
        training_temp.mWeatherHumidity = (c.getInt(c.getColumnIndexOrThrow(GPStracking.Trainings.WEATHER_HUMIDITY)))
        training_temp.mWeatherPressure = (c.getInt(c.getColumnIndexOrThrow(GPStracking.Trainings.WEATHER_PRESSURE)))
        training_temp.mWeatherIcon = (c.getString(c.getColumnIndexOrThrow(GPStracking.Trainings.WEATHER_ICON)))
        training_temp.mWeatherWindSpeed = (c.getFloat(c.getColumnIndexOrThrow(GPStracking.Trainings.WEATHER_WIND_SPEED)))
        training_temp.mWeatherDescription = (c.getString(c.getColumnIndexOrThrow(GPStracking.Trainings.WEATHER_DESCRIPTION)))

        c.close()

        val projection_lap = arrayOf(GPStracking.Laps._ID, GPStracking.Laps.TRAINING, GPStracking.Laps.TIME, GPStracking.Laps.AVERAGE_SPEED)

        c = sqldb.query(
                GPStracking.Laps.TABLE, // The table to query
                projection_lap, // The columns to return
                GPStracking.Laps.TRAINING + "=?", // The columns for the WHERE clause
                arrayOf(java.lang.Long.toString(training_id)), null, null, null// The sort order
        )// The values for the WHERE clause
        // don't group the rows
        // don't filter by row groups


        val laps_temp = ArrayList<LapDao>()


        // dodawanie wszystkich okrązen do listy
        if (c.moveToFirst() && c.count != 0) {
            do {
                val lapId = c.getLong(c.getColumnIndexOrThrow(GPStracking.Laps._ID))
                val lap = LapDao(lapId)
                lap.training_id = (c.getLong(c.getColumnIndexOrThrow(GPStracking.Laps.TRAINING)))
                lap.time = (c.getInt(c.getColumnIndexOrThrow(GPStracking.Laps.TIME)))
                lap.average_speed = (c.getFloat(c.getColumnIndexOrThrow(GPStracking.Laps.AVERAGE_SPEED)))

                // Adding contact to list
                laps_temp.add(lap)
            } while (c.moveToNext())

            training_temp.mLaps = (laps_temp)
        }

        sqldb.close()

        return training_temp

    }

    fun getTrainingRoute(trainingId: Long): List<LatLng> {

        val sqldb = writableDatabase

        val route = ArrayList<LatLng>()

        val c = sqldb.rawQuery("SELECT * FROM " + GPStracking.Laps.TABLE + " WHERE " + GPStracking.Laps.TRAINING + "=" + trainingId, null)

        // dodawanie wszystkich punktow do listy
        if (c.moveToFirst() && c.count != 0) {
            do {
                val lapId = c.getLong(c.getColumnIndexOrThrow(GPStracking.Laps._ID))

                val d = sqldb.rawQuery("SELECT * FROM " + GPStracking.Runpoints.TABLE + " WHERE " + GPStracking.Runpoints.LAP + "=" + lapId, null)

                if (d.moveToFirst() && c.count != 0) {
                    do {
                        val latitude = d.getFloat(d.getColumnIndexOrThrow(GPStracking.Runpoints.LATITUDE))
                        val longtitude = d.getFloat(d.getColumnIndexOrThrow(GPStracking.Runpoints.LONGTITUDE))
                        val latLng = LatLng(latitude.toDouble(), longtitude.toDouble())

                        route.add(latLng)

                    } while (d.moveToNext())

                    d.close()
                }
            } while (c.moveToNext())
            c.close()
        }

        sqldb.close()

        return route
    }

    companion object {
        val TAG = "DatabaseHelper"
    }
}
