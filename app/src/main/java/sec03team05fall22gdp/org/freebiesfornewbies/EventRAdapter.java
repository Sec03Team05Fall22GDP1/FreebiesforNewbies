package sec03team05fall22gdp.org.freebiesfornewbies;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class EventRAdapter extends RecyclerView.Adapter<EventRAdapter.MyViewHolder> {

    Context context;
    private EventModel myModel;

    public EventRAdapter(Context context, EventModel myModel){
        this.context= context;
        this.myModel = myModel;
    }

    @NonNull
    @Override
    public EventRAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.event_recylcerview_row,parent,false);
        return new EventRAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventRAdapter.MyViewHolder holder, int position) {
        holder.tvEventName.setText(myModel.eventsList.get(position).eventName);
        holder.tvEventStartDT.setText(myModel.eventsList.get(position).eventStDT);
        holder.tvEventEndDT.setText(myModel.eventsList.get(position).eventEndDt);
        String eLocation = myModel.eventsList.get(position).eventAddressLine1
                +", "+myModel.eventsList.get(position).eventCity
                +", "+myModel.eventsList.get(position).eventState+" "+myModel.eventsList.get(position).eventZipcode;
        holder.tvEventLocation.setText(eLocation);
        holder.tvEventDesc.setText(myModel.eventsList.get(position).eventDescription);
        Log.v("position", String.valueOf(position));
    }

    @Override
    public int getItemCount() {
        return myModel.eventsList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tvEventName, tvEventStartDT, tvEventEndDT, tvEventLocation, tvEventDesc;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvEventName = itemView.findViewById(R.id.tvEvent);
            tvEventStartDT = itemView.findViewById(R.id.tvEventStDt);
            tvEventEndDT = itemView.findViewById(R.id.tvEventEndDt);
            tvEventLocation = itemView.findViewById(R.id.tvEventLoc);
            tvEventDesc = itemView.findViewById(R.id.tvEventDesc);
        }
    }
}
