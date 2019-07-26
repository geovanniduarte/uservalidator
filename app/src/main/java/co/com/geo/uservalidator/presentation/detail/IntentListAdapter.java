package co.com.geo.uservalidator.presentation.detail;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import co.com.geo.uservalidator.R;
import co.com.geo.uservalidator.data.model.IntentEntity;

public class IntentListAdapter extends RecyclerView.Adapter<IntentListAdapter.IntentListViewHolder> {

    private List<IntentEntity> items = new ArrayList<IntentEntity>();

    @NonNull
    @Override
    public IntentListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_user, viewGroup, false);
        return new IntentListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IntentListViewHolder intentListViewHolder, int i) {
        intentListViewHolder.bind(items.get(i));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void submitList(List<IntentEntity> items) {
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    public static class IntentListViewHolder extends RecyclerView.ViewHolder {

        private View myView;
        public IntentListViewHolder(@NonNull View itemView) {
            super(itemView);
            myView = itemView;
        }

        public void bind(IntentEntity intentEntity) {

            TextView txtDate = itemView.findViewById(R.id.intent_date);
            TextView txtCountry = itemView.findViewById(R.id.intent_country);
            ImageView imageView = itemView.findViewById(R.id.intent_result);

            if (intentEntity.isResult()) {
                imageView.setImageResource(R.drawable.ic_done);
            }
            txtCountry.setText(intentEntity.getLatitude() + "," + intentEntity.getLongitude() );
            txtDate.setText(intentEntity.getDate());
        }
    }

}
