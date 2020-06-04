package it.unibo.students.manuel_bottazzi.astraandroidcontroller.layout_manager;

import android.content.Context;
import android.graphics.Color;

public class SystemPanel  extends DisplayPosition{

    public SystemPanel(Context context, int id, int width, int height) {
        super(context, id, width, height);
        this.panelLayout.setBackgroundColor(Color.GRAY);
    }

}
