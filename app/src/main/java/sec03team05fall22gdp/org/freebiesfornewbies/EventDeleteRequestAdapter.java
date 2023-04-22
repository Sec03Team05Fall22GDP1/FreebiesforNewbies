package sec03team05fall22gdp.org.freebiesfornewbies;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class EventDeleteRequestAdapter extends RecyclerView.Adapter<EventDeleteRequestAdapter.RequestHolder>{

    Context context;
    private EventDeleteRequestModel reqModel;
    public EventDeleteRequestAdapter(Context context, EventDeleteRequestModel reqModel){
        this.context= context;
        this.reqModel = reqModel;
    }

    @NonNull
    @Override
    public RequestHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.delete_recyclerview_row,parent,false);
        return new EventDeleteRequestAdapter.RequestHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull RequestHolder holder, int position) {
        holder.delEventID.setText(reqModel.EventDeleteRequestsList.get(position).eventID);
        holder.delEventName.setText(reqModel.EventDeleteRequestsList.get(position).eventName);
        holder.delEventReason.setText(String.valueOf(reqModel.EventDeleteRequestsList.get(position).deleteReason));
        Log.v("position", String.valueOf(position));
    }

    @Override
    public int getItemCount() {
        return reqModel.EventDeleteRequestsList.size();
    }

    public static class RequestHolder extends RecyclerView.ViewHolder{

        TextView delEventID, delEventName, delEventReason;
        public RequestHolder(@NonNull View itemView) {
            super(itemView);
            delEventID = itemView.findViewById(R.id.tvID);
            delEventName = itemView.findViewById(R.id.tvName);
            delEventReason = itemView.findViewById(R.id.tvDeleteDesc);
        }
    }
}
