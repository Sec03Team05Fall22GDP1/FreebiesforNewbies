package sec03team05fall22gdp.org.freebiesfornewbies;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class EventUpdateRequestAdapter extends RecyclerView.Adapter<EventUpdateRequestAdapter.MyViewHolder> {

    Context context;
    private EventUpdateRequestModel myModel;

    public EventUpdateRequestAdapter(Context context, EventUpdateRequestModel myModel){
        this.context= context;
        this.myModel = myModel;
    }

    @NonNull
    @Override
    public EventUpdateRequestAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.event_recylcerview_row,parent,false);
        return new EventUpdateRequestAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventUpdateRequestAdapter.MyViewHolder holder, int position) {
        holder.tvEventName.setText(myModel.EventUpdateRequestsList.get(position).eventName);
        holder.tvEventStartDT.setText(myModel.EventUpdateRequestsList.get(position).eventStDT);
        holder.tvEventEndDT.setText(myModel.EventUpdateRequestsList.get(position).eventEndDt);
        String eLocation = myModel.EventUpdateRequestsList.get(position).eventAddressLine1
                +", "+myModel.EventUpdateRequestsList.get(position).eventCity
                +", "+myModel.EventUpdateRequestsList.get(position).eventState+" "+myModel.EventUpdateRequestsList.get(position).eventZipcode;
        holder.tvEventLocation.setText(eLocation);
        holder.tvEventDesc.setText(myModel.EventUpdateRequestsList.get(position).eventDescription);
        Log.v("position", String.valueOf(position));
    }

    @Override
    public int getItemCount() {
        return myModel.EventUpdateRequestsList.size();
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
