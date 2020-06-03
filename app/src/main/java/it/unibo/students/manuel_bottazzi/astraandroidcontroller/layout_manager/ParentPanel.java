package it.unibo.students.manuel_bottazzi.astraandroidcontroller.layout_manager;

import android.content.Context;
import android.graphics.Color;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import it.unibo.students.manuel_bottazzi.astraandroidcontroller.model.PatientData;

public class ParentPanel extends DisplayPosition {

    List<DisplayPosition> childList;

    public ParentPanel(Context context, int id) {
        super(context, id, 1024, 1024);

        this.childList = new ArrayList<>();

        this.panelLayout.setBackgroundColor(Color.BLUE);
        this.panelLayout.setOrientation(LinearLayout.VERTICAL);
    }

    public void addChildPanel(BasePanel child) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(10, 10, 10, 10);

        this.childList.add(child);

        this.panelLayout.addView(child.getPanel(), layoutParams);
    }

    public DisplayPosition getChildByID(int id) {
        DisplayPosition result = null;
        for (int i = 0; i < this.childList.size(); i ++){
            DisplayPosition c = this.childList.get(i);
            if (c.getId() == id){
                result = c;
            }
        }

        return result;
    }

    @Override
    public void boundData(PatientData data) {
        throw new UnsupportedOperationException("Cannot bound data to that component");
    }
}
