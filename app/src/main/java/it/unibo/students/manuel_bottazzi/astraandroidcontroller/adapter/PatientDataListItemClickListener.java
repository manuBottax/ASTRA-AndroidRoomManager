package it.unibo.students.manuel_bottazzi.astraandroidcontroller.adapter;

import android.content.ClipData;
import android.content.ClipDescription;

import android.content.Intent;
import android.os.Build;
import android.view.View;

import androidx.fragment.app.FragmentActivity;

import it.unibo.students.manuel_bottazzi.astraandroidcontroller.dialog.PatientDataOptionsDialog;
import it.unibo.students.manuel_bottazzi.astraandroidcontroller.model.PatientData;

public class PatientDataListItemClickListener implements View.OnLongClickListener, View.OnClickListener{

    private final FragmentActivity context;
    PatientData patientData;
    int position;

    public PatientDataListItemClickListener (FragmentActivity context, PatientData data, int listPosition){
        this.context= context;
        this.patientData = data;
        this.position = listPosition;
    }

    // make the object draggable
    @Override
    public boolean onLongClick(View view) {

        //collect the needed data via intent
        Intent patientIntent = new Intent();
        patientIntent.putExtra("patientDataName", this.patientData.getName());
        patientIntent.putExtra("patientDataType", this.patientData.getDataType());
        patientIntent.putExtra("patientDataSelectedOperation", this.patientData.getSelectedOperation());
        patientIntent.putExtra( "patientDataPosition", this.position);
        ClipData.Item item = new ClipData.Item(patientIntent);

        // the type of the dragged data
        String[] mimeTypes = {ClipDescription.MIMETYPE_TEXT_INTENT};

        // use the name as label in first parameter --> in futore could be the id
        ClipData data = new ClipData(patientData.getName(), mimeTypes, item);

        // Instantiates the drag shadow builder.

        View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);

        // Starts the drag
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            view.startDragAndDrop(data//data to be dragged
                    , shadowBuilder //drag shadow
                    , view//local data about the drag and drop operation
                    , 0//no needed flags
            );
        } else {
            view.startDrag(data, shadowBuilder,view , 0);
        }

        //Set view visibility to INVISIBLE as we are going to drag the view
        view.setVisibility(View.INVISIBLE);
        this.patientData.setInUse(true);
        return true;
    }

    @Override
    public void onClick(View view) {
        if (this.patientData.isInUse()){

            PatientDataOptionsDialog dialog = new PatientDataOptionsDialog(this.patientData);
            dialog.show(this.context.getSupportFragmentManager(),"test");
        }
    }
}
