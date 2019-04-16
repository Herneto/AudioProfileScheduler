package ao.co.laboro.audioprofilescheduler;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ProfileViewHolder> {

    private ArrayList<ProfileItem> mProfileList;
    private OnItemClickListener mListener;



    public ProfileAdapter(ArrayList<ProfileItem> profileItems){
        mProfileList = profileItems;
    }

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public static class ProfileViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener {
        public TextView time;
        public  TextView desc;
        public  TextView status;
        public TextView part;
        public Switch ativo;
        public TextView id;


        public ProfileViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            time = (TextView)itemView.findViewById(R.id.time);
            desc = (TextView)itemView.findViewById(R.id.desc);
            status = (TextView)itemView.findViewById(R.id.status);
            part = (TextView)itemView.findViewById(R.id.part);
            ativo = (Switch)itemView.findViewById(R.id.ativo);
            id = (TextView)itemView.findViewById(R.id.id);

            ativo.setClickable(false);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }

        @Override
        public void onClick(View v) {
            Toast.makeText(v.getContext(), "Click", Toast.LENGTH_SHORT).show();

        }

        @Override
        public boolean onLongClick(View v) {
            Toast.makeText(v.getContext(), "Long Click", Toast.LENGTH_SHORT).show();

            return false;
        }
    }

    @NonNull
    @Override
    public ProfileViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.profile_item, viewGroup, false);
        ProfileViewHolder pvh = new ProfileViewHolder(v, mListener);
        return pvh;
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileViewHolder profileViewHolder, int i) {
        ProfileItem currentItem = mProfileList.get(i);
        profileViewHolder.time.setText(currentItem.getTime());
        profileViewHolder.desc.setText(currentItem.getDesc());
        profileViewHolder.status.setText(currentItem.getStatus());
        profileViewHolder.part.setText(currentItem.getPart());
        profileViewHolder.ativo.setChecked(currentItem.getAtivo());
    }

    @Override
    public int getItemCount() {
        return mProfileList.size();
    }
}
