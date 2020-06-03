package it.unibo.students.manuel_bottazzi.astraandroidcontroller.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import it.unibo.students.manuel_bottazzi.astraandroidcontroller.R;
import it.unibo.students.manuel_bottazzi.astraandroidcontroller.model.PatientData;

public class PatientDataListAdapter extends RecyclerView.Adapter<PatientDataListAdapter.PatientDataViewHolder> {

    private final FragmentActivity context;
    private List<PatientData> mDataset;


    // Provide a reference to the views for each data item
// Complex data items may need more than one view per item, and
// you provide access to all the views for a data item in a view holder
    public static class PatientDataViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        private TextView nameTextView;
        private LinearLayout view;

        public PatientDataViewHolder(LinearLayout listElement) {
            super(listElement);
            this.nameTextView = listElement.findViewById(R.id.patient_data_name_label);
            this.view = listElement;
        }

        public void setNameTextView (TextView tv){
            this.nameTextView = tv;
        }

        public TextView getNameTextView() {
            return this.nameTextView;
        }

        public LinearLayout getView() {
            return view;
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public PatientDataListAdapter(FragmentActivity context, List<PatientData> patientDataList) {
        this.context = context;
        this.mDataset = patientDataList;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public PatientDataListAdapter.PatientDataViewHolder onCreateViewHolder(ViewGroup parent,
                                                                           int viewType) {
        // create a new view
        LinearLayout listElement = (LinearLayout) LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.patient_data_list_element, parent, false);

        PatientDataViewHolder vh = new PatientDataViewHolder(listElement);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(PatientDataViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.getNameTextView().setText(mDataset.get(position).getName());

        PatientDataListItemClickListener listener = new PatientDataListItemClickListener(this.context, mDataset.get(position), position);
        holder.getView().setOnLongClickListener(listener);
        holder.getView().setOnClickListener(listener);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }


}
