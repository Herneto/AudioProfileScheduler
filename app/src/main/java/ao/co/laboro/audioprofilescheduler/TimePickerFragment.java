package ao.co.laboro.audioprofilescheduler;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import java.util.ArrayList;
import java.util.Calendar;

public class TimePickerFragment extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener {

    ArrayList<ProfileItem> items;
    RecyclerView.Adapter mAdapter;
    TimeListener listener;
    int hourOfDay;
    int minute;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }


    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        this.hourOfDay = hourOfDay;
        this.minute = minute;
        listener.onSetTime(hourOfDay,minute);
    }

    public void setTimeListener(TimeListener listener){
        this.listener = listener;
    }

    public int get12h(){
        return hourOfDay > 12? hourOfDay - 12:hourOfDay;
    }

    public String day_period(){
        return hourOfDay > 12? "PM":"AM";
    }

    public String format(int number){
        if(number < 10)
            return "0"+number;
        return ""+number;
    }


}
