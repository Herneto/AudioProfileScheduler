package ao.co.laboro.audioprofilescheduler;

import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.TransitionManager;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class DetailsActivity extends AppCompatActivity{

    private TextView hourOfTheDay;
    private TextView description;
    private TextView status;
    private TextView amORPm;
    private Switch active;
    private CheckBox ckbRepeat;
    private LinearLayout week_days;
    private Spinner profile;
    private EditText editDesc;

    private CheckBox ckbS;
    private CheckBox ckbM;
    private CheckBox ckbTu;
    private CheckBox ckbW;
    private CheckBox ckbTh;
    private CheckBox ckbF;
    private CheckBox ckbSa;

    private Button buttonDelete;
    private Button buttonSave;

    ViewGroup transitionsContainer;
    ProfileItem item;
    Db mDb;

    List<String> arraySpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);


        if (savedInstanceState != null) {
            item = (ProfileItem) savedInstanceState.getSerializable("item");
        }else{
            Bundle bundle = getIntent().getExtras();
            item = (ProfileItem) bundle.getSerializable("item");
            hideInput();
        }

        mDb = new Db(getApplicationContext());

        week_days = (LinearLayout)findViewById(R.id.week_days);
        transitionsContainer = (ViewGroup)findViewById(R.id.main);
        editDesc = (EditText)findViewById(R.id.edit_desc);

        ckbRepeat = (CheckBox)findViewById(R.id.ckb_repeat);
        ckbRepeat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                TransitionManager.beginDelayedTransition(transitionsContainer);
                week_days.setVisibility(isChecked ? View.VISIBLE : View.GONE);
                item.setRepeat(isChecked);

            }
        });



        hourOfTheDay = (TextView)findViewById(R.id.time);
        description = (TextView)findViewById(R.id.desc);
        status = (TextView)findViewById(R.id.status);
        amORPm = (TextView)findViewById(R.id.part);
        active = (Switch)findViewById(R.id.ativo);

        fillFields();

        arraySpinner = new ArrayList<>();
        arraySpinner.add("Silent");
        arraySpinner.add("Normal");
        arraySpinner.add("Vibrate");

        editDesc.addTextChangedListener(new TextWatcher() {
            boolean ignoreChange = false;
            String before;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                before = s.toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String a = s.toString();
                if(!a.equals(before)) {
                    item.setDesc(s.toString());
                    description.setText(s.toString());
                }
            }
        });

        final View.OnClickListener timeClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final TimePickerFragment timePickerFragment = new TimePickerFragment();
                timePickerFragment.setTimeListener(new TimeListener() {
                    @Override
                    public void onSetTime(int hourOfDay, int minute) {
                        item.setTime(timePickerFragment.format(timePickerFragment.get12h()) + ":" + timePickerFragment.format(minute));
                        item.setPart(timePickerFragment.day_period());
                        fillFields();
                    }
                });
                DialogFragment newFragment = timePickerFragment;
                newFragment.show(getSupportFragmentManager(), "timePicker");
            }
        };

        hourOfTheDay.setOnClickListener(timeClick);

        amORPm.setOnClickListener(timeClick);


        active.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                item.setAtivo(isChecked);
            }
        });


        profile = (Spinner) findViewById(R.id.profiles);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arraySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        profile.setAdapter(adapter);
        profile.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(view == null) return;
                item.setStatus(((TextView)view).getText().toString());
                fillFields();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        profile.setSelection(arraySpinner.indexOf(item.getStatus()));
        setDaysOfTheWeek(item.getDays_to_repeat());

        buttonSave = (Button)findViewById(R.id.save);
        buttonDelete = (Button)findViewById(R.id.delete);
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDb.delete(item.getId());
                finish();
                NotifyChange.getIntance().setChanged(true);
                Toast.makeText(getApplicationContext(), "Deleted", Toast.LENGTH_SHORT).show();
            }
        });

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDb.update(item);
                finish();
                NotifyChange.getIntance().setChanged(true);
                Toast.makeText(getApplicationContext(), "Deleted", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setDaysOfTheWeek(String week_days){
        ckbS = (CheckBox)findViewById(R.id.ckb_s);
        ckbM = (CheckBox)findViewById(R.id.ckb_m);
        ckbTu = (CheckBox)findViewById(R.id.ckb_t);
        ckbW = (CheckBox)findViewById(R.id.ckb_w);
        ckbTh = (CheckBox)findViewById(R.id.ckb_th);
        ckbF = (CheckBox)findViewById(R.id.ckb_f);
        ckbSa = (CheckBox)findViewById(R.id.ckb_sa);

        CompoundButton.OnCheckedChangeListener onCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Integer i = (Integer)buttonView.getTag();
                String days = item.getDays_to_repeat();
                char[] chars_day = days.toCharArray();
                chars_day[i] = isChecked?'1':'0';
                item.setDays_to_repeat(String.valueOf(chars_day));
            }
        };

        ckbS.setTag(0);
        ckbM.setTag(1);
        ckbTu.setTag(2);
        ckbW.setTag(3);
        ckbTh.setTag(4);
        ckbF.setTag(5);
        ckbSa.setTag(6);

        ckbS.setChecked(mark(week_days.charAt(0)));
        ckbM.setChecked(mark(week_days.charAt(1)));
        ckbTu.setChecked(mark(week_days.charAt(2)));
        ckbW.setChecked(mark(week_days.charAt(3)));
        ckbTh.setChecked(mark(week_days.charAt(4)));
        ckbF.setChecked(mark(week_days.charAt(5)));
        ckbSa.setChecked(mark(week_days.charAt(6)));


        ckbS.setOnCheckedChangeListener(onCheckedChangeListener);
        ckbM.setOnCheckedChangeListener(onCheckedChangeListener);
        ckbTu.setOnCheckedChangeListener(onCheckedChangeListener);
        ckbW.setOnCheckedChangeListener(onCheckedChangeListener);
        ckbTh.setOnCheckedChangeListener(onCheckedChangeListener);
        ckbF.setOnCheckedChangeListener(onCheckedChangeListener);
        ckbSa.setOnCheckedChangeListener(onCheckedChangeListener);


    }

    private boolean mark(char c){
        return c == '1';
    }


    private void fillFields(){
        hourOfTheDay.setText(item.getTime());
        description.setText(item.getDesc());
        status.setText(item.getStatus());
        amORPm.setText(item.getPart());
        active.setChecked(item.getAtivo());
        editDesc.setText(item.getDesc());
        ckbRepeat.setChecked(item.getRepeat());
    }

    private void hideInput(){
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putSerializable("item", item);
    }
}
