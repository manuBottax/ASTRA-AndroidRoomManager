package it.unibo.students.manuel_bottazzi.astraandroidcontroller.layout_manager;

import android.content.Context;
import android.graphics.Color;
import android.view.View;

public class BasePanel extends DisplayPosition{

    public BasePanel(Context context, int id, int width, int height) {
        super(context, id, width, height);
        this.panelLayout.setBackgroundColor(Color.YELLOW);
    }

    public void setOnDragListener(View.OnDragListener listener){
        this.panelLayout.setOnDragListener(listener);
    }

}
