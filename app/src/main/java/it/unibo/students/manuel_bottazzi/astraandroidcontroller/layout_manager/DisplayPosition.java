package it.unibo.students.manuel_bottazzi.astraandroidcontroller.layout_manager;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import it.unibo.students.manuel_bottazzi.astraandroidcontroller.model.PatientData;

public abstract class DisplayPosition{

    LinearLayout panelLayout;

    private int width;
    private int height;

    private int id;

    private PatientData boundedData;

    public DisplayPosition(Context context, int id, int width, int height) {

        this.height = height;
        this.width = width;
        this.boundedData = null;
        this.id = id;

        this.panelLayout = new LinearLayout(context);

        this.panelLayout.setId(id);
        this.panelLayout.setMinimumHeight(this.height);
        this.panelLayout.setMinimumWidth(this.width);
    }

    public int getId() {
        return this.id;
    }

    public LinearLayout getPanel() {
        return this.panelLayout;
    }

    public void boundData(PatientData data) {
        this.boundedData = data;
    }

    public PatientData getBoundedData() {
        return this.boundedData;
    }
}
