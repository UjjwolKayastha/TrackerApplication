package kayastha.ujjwol.atrackerapp.friends;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import kayastha.ujjwol.atrackerapp.R;
import kayastha.ujjwol.atrackerapp.models.UserData;

public class RecyclerViewAdapter  extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    Context context;
    List<UserData> userDataList;

    public RecyclerViewAdapter(Context context, List<UserData> TempList) {
        this.userDataList = TempList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_list_design, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        UserData userData = userDataList.get(position);
        holder.nameTextView.setText(userData.getName());
        holder.emailTextView.setText(userData.getEmail());
    }

    @Override
    public int getItemCount() {
        return userDataList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTextView;
        public TextView emailTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            nameTextView =  itemView.findViewById(R.id.nameItem);
            emailTextView =  itemView.findViewById(R.id.emailItem);
        }
    }
}
