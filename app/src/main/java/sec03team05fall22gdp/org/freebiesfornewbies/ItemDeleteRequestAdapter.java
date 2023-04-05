package sec03team05fall22gdp.org.freebiesfornewbies;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ItemDeleteRequestAdapter extends RecyclerView.Adapter<ItemDeleteRequestAdapter.RequestHolder>{

    Context context;
    private ItemDeleteRequestModel reqModel;
    public ItemDeleteRequestAdapter(Context context, ItemDeleteRequestModel reqModel){
        this.context= context;
        this.reqModel = reqModel;
    }

    @NonNull
    @Override
    public RequestHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.delete_recyclerview_row,parent,false);
        return new ItemDeleteRequestAdapter.RequestHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestHolder holder, int position) {
        holder.delItemID.setText(reqModel.ItemDeleteRequestsList.get(position).ItemID);
        holder.delItemName.setText(reqModel.ItemDeleteRequestsList.get(position).ItemName);
        holder.delItemReason.setText(String.valueOf(reqModel.ItemDeleteRequestsList.get(position).deleteReason));
        Log.v("position", String.valueOf(position));
    }

    @Override
    public int getItemCount() {
        return reqModel.ItemDeleteRequestsList.size();
    }

    public static class RequestHolder extends RecyclerView.ViewHolder{

        TextView delItemID, delItemName, delItemReason;
        public RequestHolder(@NonNull View itemView) {
            super(itemView);
            delItemID = itemView.findViewById(R.id.tvID);
            delItemName = itemView.findViewById(R.id.tvName);
            delItemReason = itemView.findViewById(R.id.tvDeleteDesc);
        }
    }
}
