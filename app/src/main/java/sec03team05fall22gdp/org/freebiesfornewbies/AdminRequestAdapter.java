package sec03team05fall22gdp.org.freebiesfornewbies;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdminRequestAdapter extends RecyclerView.Adapter<AdminRequestAdapter.RequestHolder>{

    Context context;
    private AdminRequestModel reqModel;
    public AdminRequestAdapter(Context context, AdminRequestModel reqModel){
        this.context= context;
        this.reqModel = reqModel;
    }

    @NonNull
    @Override
    public RequestHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.admin_recyclerview_row,parent,false);
        return new AdminRequestAdapter.RequestHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestHolder holder, int position) {
        holder.reqTypeTV.setText(reqModel.reqList.get(position).requestType);
        holder.reqCountTV.setText(reqModel.reqList.get(position).requestCount);
        Log.v("position", String.valueOf(position));

    }

    @Override
    public int getItemCount() {
        return reqModel.reqList.size();
    }

    public static class RequestHolder extends RecyclerView.ViewHolder{

        TextView reqTypeTV, reqCountTV;
        public RequestHolder(@NonNull View itemView) {
            super(itemView);
            reqTypeTV = itemView.findViewById(R.id.tvRequestType);
            reqCountTV = itemView.findViewById(R.id.tvReqCntValue);
        }
    }
}
