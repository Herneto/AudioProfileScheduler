package ao.co.laboro.audioprofilescheduler;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import ao.co.laboro.audioprofilescheduler.service.MonitorService;

public class MainActivity extends AppCompatActivity {
    ArrayList<ProfileItem> mItems;
    Db mDb;

    private RecyclerView mRecyclerView;
    private ProfileAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private  int deleteIndex;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private static final String ORDER_BY_PREFERENCE = "OrderByPreference";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDb = new Db(getApplicationContext());

        mItems = mDb.getAll();
        deleteIndex = -1;
        mRecyclerView = findViewById(R.id.recyclerview);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new ProfileAdapter(mItems);
        mAdapter.setOnItemClickListener(new ProfileAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
                ProfileItem item = mItems.get(position);
                deleteIndex = position;
                intent.putExtra("item", item);
                startActivity(intent);
            }
        });

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        sharedPreferences = getSharedPreferences(ORDER_BY_PREFERENCE,0);
        editor = sharedPreferences.edit();
        orderBy(sharedPreferences.getString("orderBy", "date"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.add:
                final TimePickerFragment timePickerFragment = new TimePickerFragment();
                timePickerFragment.setTimeListener(new TimeListener() {
                    @Override
                    public void onSetTime(int hourOfDay, int minute) {
                        ProfileItem item = new ProfileItem(timePickerFragment.format(timePickerFragment.get12h())+":"+timePickerFragment.format(minute), timePickerFragment.day_period(), "Normal", true, "Lunch", true, "1111111");
                        mDb.insert(item);
                        mItems.add(item);
                        orderBy(sharedPreferences.getString("orderBy", "date"));
                        mAdapter.notifyItemInserted(mItems.size()-1);
                    }
                });
                DialogFragment newFragment = timePickerFragment;
                newFragment.show(getSupportFragmentManager(), "timePicker");
                break;
            case R.id.hour:
                orderBy("hour");
                mAdapter.notifyDataSetChanged();
                editor.putString("orderBy", "hour");
                editor.commit();

                break;
            case R.id.profile:
                orderBy("profile");
                mAdapter.notifyDataSetChanged();
                editor.putString("orderBy", "profile");
                editor.commit();

                break;
            case R.id.status:
                orderBy("status");
                mAdapter.notifyDataSetChanged();
                editor.putString("orderBy", "status");
                editor.commit();

                break;
            case R.id.date:
                orderBy("date");
                mAdapter.notifyDataSetChanged();
                editor.putString("orderBy", "date");
                editor.commit();

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        if(NotifyChange.getIntance().isChanged() && deleteIndex != -1) {
            if(mDb.count() == mItems.size()){
                mItems.set(deleteIndex,mDb.getItem(mItems.get(deleteIndex).getId()));
                orderBy(sharedPreferences.getString("orderBy", "date"));
            }else{
                mItems.remove(deleteIndex);
            }
            mAdapter.notifyDataSetChanged();
            NotifyChange.getIntance().setChanged(false);
        }
        super.onResume();
    }

    private void orderBy(String term){
        if(term.equals("hour")){
            Collections.sort(mItems, new Comparator<ProfileItem>() {
                @Override
                public int compare(ProfileItem o1, ProfileItem o2) {
                    return o1.getTime().compareTo(o2.getTime());
                }
            });
        }else if(term.equals("status")){
            Collections.sort(mItems, new Comparator<ProfileItem>() {
                @Override
                public int compare(ProfileItem o1, ProfileItem o2) {
                    return o1.getAtivo().compareTo(o2.getAtivo());
                }
            });
        }else if(term.equals("profile")){
            Collections.sort(mItems, new Comparator<ProfileItem>() {
                @Override
                public int compare(ProfileItem o1, ProfileItem o2) {
                    return o1.getStatus().compareTo(o2.getStatus());
                }
            });
        }else{
            Collections.sort(mItems, new Comparator<ProfileItem>() {
                @Override
                public int compare(ProfileItem o1, ProfileItem o2) {
                    return new Integer(o1.getId()).compareTo( new Integer(o2.getId()));
                }
            });
        }
    }

}
