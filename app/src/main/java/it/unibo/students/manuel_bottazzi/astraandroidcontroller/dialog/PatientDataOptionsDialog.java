package it.unibo.students.manuel_bottazzi.astraandroidcontroller.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import it.unibo.students.manuel_bottazzi.astraandroidcontroller.R;
import it.unibo.students.manuel_bottazzi.astraandroidcontroller.model.PatientData;

public class PatientDataOptionsDialog extends DialogFragment {

    private PatientData data;

    public PatientDataOptionsDialog(PatientData data){
        super();
        this.data = data;
    }

    @Override
    public @NonNull Dialog onCreateDialog( Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LinearLayout optionLayout = (LinearLayout) LayoutInflater
                .from(getContext())
                .inflate(R.layout.option_dialog_layout, null , false);

        // add a button for each option in the current patient data object
        for( final String option : this.data.getAvailableOperationList()){
            Button b = new Button(getContext());
            b.setText(option);
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    data.setSelectedOperation(option);
                    Toast.makeText(getContext(),"Selected option : " + option, Toast.LENGTH_SHORT).show();
                    dismiss();
                }
            });

            optionLayout.addView(b);
        }


        //create dialog;
        builder .setTitle("Opzioni disponibili per " + data.getName())
                .setView(optionLayout)
                .setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });

        return builder.create();
    }
/*
    @Override
    public void show(FragmentManager manager, String tag) {
        super.show(manager, tag);
    }
    
 */
}
