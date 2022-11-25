package sec03team05fall22gdp.org.freebiesfornewbies;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.MyViewHolder> {

    Context context;
    private ItemModel myModel;

    public ItemAdapter(Context context, ItemModel myModel){
        this.context= context;
        this.myModel = myModel;
    }

    @NonNull
    @Override
    public ItemAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_recylcerview_row,parent,false);
        return new ItemAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemAdapter.MyViewHolder holder, int position) {
        holder.tvItemName.setText(myModel.itemsList.get(position).itemName);
        String eLocation = myModel.itemsList.get(position).itemAddressLine1
                +", "+myModel.itemsList.get(position).itemCity
                +", "+myModel.itemsList.get(position).itemState+" "+myModel.itemsList.get(position).itemZipcode;
        holder.tvItemLocation.setText(eLocation);
        holder.tvItemURL.setText(myModel.itemsList.get(position).itemURL);
        Log.v("position", String.valueOf(position));
    }

    @Override
    public int getItemCount() {
        return myModel.itemsList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tvItemName, tvItemStartDT, tvItemEndDT, tvItemLocation, tvItemURL;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvItemName = itemView.findViewById(R.id.tvItem);
            tvItemLocation = itemView.findViewById(R.id.tvItemLoc);
            tvItemURL = itemView.findViewById(R.id.tvItemURL);
        }
    }
}

