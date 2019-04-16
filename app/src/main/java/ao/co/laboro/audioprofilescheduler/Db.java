package ao.co.laboro.audioprofilescheduler;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.*;
import android.widget.Toast;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class Db {
    private SQLiteDatabase db;
    private Context context;

    public Db(Context context){
        this.context = context;
        this.db = context.openOrCreateDatabase("audio_profile_sheduler", MODE_PRIVATE, null);
        this.db.execSQL("CREATE TABLE IF NOT EXISTS profile_sheduler (id INTEGER PRIMARY KEY AUTOINCREMENT, time VARCHAR, description VARCHAR, day_period VARCHAR, active INT(1), profile VARCHAR, repeat INT(1), days_to_reapeat VARCHAR)");
    }

    public ProfileItem insert(ProfileItem item){
        String sql = String.format("INSERT INTO profile_sheduler (time, description, day_period, active, profile, repeat, days_to_reapeat) VALUES ('%s', '%s', '%s', %d, '%s', %d, '%s')", item.getTime(), item.getDesc(), item.getPart(), item.getAtivo()?1:0, item.getStatus(), item.getRepeat()?1:0, item.getDays_to_repeat());
        this.db.execSQL(sql);
        item.setId(lastID());
        return item;
    }

    private int lastID(){
        Cursor cursor = this.db.rawQuery("SELECT * FROM profile_sheduler", null);
        cursor.moveToLast();
        return cursor.getInt(cursor.getColumnIndex("id"));
    }

    public ProfileItem getItem(int id){
        ProfileItem item = null;
        Cursor cursor = this.db.rawQuery("SELECT * FROM profile_sheduler WHERE id = "+id, null);
        if(!cursor.moveToFirst()) return item;

        String time = cursor.getString(cursor.getColumnIndex("time"));
        String description = cursor.getString(cursor.getColumnIndex("description"));
        String day_period = cursor.getString(cursor.getColumnIndex("day_period"));
        boolean active = cursor.getInt(cursor.getColumnIndex("active")) > 0?true:false;
        String profile = cursor.getString(cursor.getColumnIndex("profile"));
        boolean repeat = cursor.getInt(cursor.getColumnIndex("repeat")) > 0?true:false;
        String days_to_repeat = cursor.getString(cursor.getColumnIndex("days_to_reapeat"));

        item = new ProfileItem(time, day_period, profile, active, description, repeat, days_to_repeat);
        item.setId(id);


        return item;
    }

    public ArrayList<ProfileItem> getAll(){
        ArrayList<ProfileItem> items = new ArrayList<>();
        Cursor cursor = this.db.rawQuery("SELECT * FROM profile_sheduler", null);
        cursor.moveToFirst();
        int i = 0;

        while (i++ < cursor.getCount()){
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            String time = cursor.getString(cursor.getColumnIndex("time"));
            String description = cursor.getString(cursor.getColumnIndex("description"));
            String day_period = cursor.getString(cursor.getColumnIndex("day_period"));
            boolean active = cursor.getInt(cursor.getColumnIndex("active")) > 0?true:false;
            String profile = cursor.getString(cursor.getColumnIndex("profile"));
            boolean repeat = cursor.getInt(cursor.getColumnIndex("repeat")) > 0?true:false;
            String days_to_repeat = cursor.getString(cursor.getColumnIndex("days_to_reapeat"));

            ProfileItem profileItem = new ProfileItem(time, day_period, profile, active, description, repeat, days_to_repeat);
            profileItem.setId(id);

            items.add(profileItem);
            cursor.moveToNext();
        }
        return items;
    }

    public void update(ProfileItem item){
        this.db.execSQL(String.format("UPDATE profile_sheduler SET time = '%s', description = '%s', day_period = '%s', active = %d, profile = '%s', repeat = %d, days_to_reapeat = '%s' WHERE id = %d",
                item.getTime(), item.getDesc(), item.getPart(), item.getAtivo()?1:0, item.getStatus(), item.getRepeat()?1:0, item.getDays_to_repeat(), item.getId()));
    }

    public void delete(int id){
        this.db.execSQL("DELETE FROM profile_sheduler WHERE id = "+id);
    }

    public int count(){
        Cursor cursor = this.db.rawQuery("SELECT * FROM profile_sheduler", null);
        return cursor.getCount();
    }

    public ProfileItem hasDayAndMinute(int hour, int minute){
        ProfileItem item = null;
        Cursor cursor = this.db.rawQuery(String.format("SELECT * FROM profile_sheduler WHERE time = '%s' AND  active = 1", hour+":"+minute), null);
        if(!cursor.moveToFirst()) return null;

        String time = cursor.getString(cursor.getColumnIndex("time"));
        String description = cursor.getString(cursor.getColumnIndex("description"));
        String day_period = cursor.getString(cursor.getColumnIndex("day_period"));
        boolean active = cursor.getInt(cursor.getColumnIndex("active")) > 0?true:false;
        String profile = cursor.getString(cursor.getColumnIndex("profile"));
        boolean repeat = cursor.getInt(cursor.getColumnIndex("repeat")) > 0?true:false;
        String days_to_repeat = cursor.getString(cursor.getColumnIndex("days_to_reapeat"));
        int id = cursor.getInt(cursor.getColumnIndex("active"));

        item = new ProfileItem(time, day_period, profile, active, description, repeat, days_to_repeat);
        item.setId(id);
        return item;
    }



}
