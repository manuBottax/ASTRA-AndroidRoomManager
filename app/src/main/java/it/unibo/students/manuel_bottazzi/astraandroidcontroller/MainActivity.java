package it.unibo.students.manuel_bottazzi.astraandroidcontroller;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import it.unibo.students.manuel_bottazzi.astraandroidcontroller.adapter.PatientDataListAdapter;
import it.unibo.students.manuel_bottazzi.astraandroidcontroller.layout_manager.BasePanel;
import it.unibo.students.manuel_bottazzi.astraandroidcontroller.layout_manager.DisplayPosition;
import it.unibo.students.manuel_bottazzi.astraandroidcontroller.layout_manager.ParentPanel;
import it.unibo.students.manuel_bottazzi.astraandroidcontroller.layout_manager.ScreenPanel;
import it.unibo.students.manuel_bottazzi.astraandroidcontroller.model.PatientData;

public class MainActivity extends AppCompatActivity implements View.OnDragListener{

    private LinearLayout screenLayout;
    private ScreenPanel screenPanel;

    private BasePanel leftScreenPanel;
    private ParentPanel rightScreenPanel;

    private BasePanel rightPanel1;
    private BasePanel rightPanel2;
    private BasePanel rightPanel3;

    private RecyclerView serviceListView;
    private PatientDataListAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private List<PatientData> serviceList;

    private String hostAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_custom_layout);

        this.hostAddress = "192.168.1.249";

        // get the screen always on --> no extra time to unlock
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        loadCustomLayout();

        this.serviceList = retrieveAvailableData();

        this.serviceListView = findViewById(R.id.availableItemsRecyclerView);
        this.serviceListView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        this.serviceListView.setLayoutManager(layoutManager);

        mAdapter = new PatientDataListAdapter(this, this.serviceList);

        this.serviceListView.setAdapter(mAdapter);


    }

    private void loadCustomLayout() {

        this.screenLayout = findViewById(R.id.screen_layout);
        this.screenLayout.setBackgroundColor(Color.BLUE);

        this.screenPanel = new ScreenPanel(this, R.id.screen_layout);

        this.leftScreenPanel = new BasePanel(this, 1, 1024, 1024);
        this.leftScreenPanel.setOnDragListener(this);


        this.rightScreenPanel = new ParentPanel(this, R.id.right_panel_layout );

        this.rightPanel1 = new BasePanel(this, 2,  1024, 300);
        this.rightPanel1.setOnDragListener(this);
        this.rightScreenPanel.addChildPanel(this.rightPanel1);


        this.rightPanel2 = new BasePanel(this, 3, 1024, 300);
        this.rightPanel2.setOnDragListener(this);
        this.rightScreenPanel.addChildPanel(this.rightPanel2);

        this.rightPanel3 = new BasePanel(this,4, 1024, 300);
        this.rightPanel3.setOnDragListener(this);
        this.rightScreenPanel.addChildPanel(this.rightPanel3);

        this.screenPanel.addChildPanel(leftScreenPanel);
        this.screenPanel.addChildPanel(rightScreenPanel);

        this.screenLayout.addView(this.screenPanel.getPanel());
    }

    @NotNull
    private List<PatientData> retrieveAvailableData(){
        List<PatientData> lst = new ArrayList<>();

        //TODO: recuperare i dati disponibili dal sistema -> aggiornare man mano che diventano disponibili
        // DATI DI ESEMPIO
        lst.add(new PatientData("ECG", "ecg"));
        lst.add(new PatientData("Parametri Vitali", "vital_parameters"));
        lst.add(new PatientData("Lastra Torace", "chest_rx"));
        lst.add(new PatientData("Esami del sangue", "blood_exam"));
        for(int i = 0; i < 5 ; i ++){
            lst.add(new PatientData("Altre Info " + i , "others_" + i));
        }

        return lst;
    }

    @Override
    public boolean onDrag(View view, DragEvent event) {

        // Defines a variable to store the action type for the incoming event
        int action = event.getAction();

        // Handles each of the expected events
        switch (action) {
            case DragEvent.ACTION_DRAG_STARTED:
                // Determines if this View can accept the dragged data
                if (event.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_INTENT)) {
                    // returns true to indicate that the View can accept the dragged data.
                    return true;
                }
                // Returns false. During the current drag and drop operation, this View will not receive events again until ACTION_DRAG_ENDED is sent.
                return false;

            case DragEvent.ACTION_DRAG_ENTERED:
                view.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_IN);
                view.invalidate();
                return true;

            case DragEvent.ACTION_DRAG_LOCATION:
                // Ignore the event
                return true;

            case DragEvent.ACTION_DRAG_EXITED:

                view.getBackground().clearColorFilter();
                view.invalidate();

                return true;

            case DragEvent.ACTION_DROP:

                // Gets the item containing the dragged data
                ClipData.Item item = event.getClipData().getItemAt(0);
                String dataName = item.getIntent().getStringExtra("patientDataName");
                String dataType = item.getIntent().getStringExtra("patientDataType");
                String dataOp = item.getIntent().getStringExtra("patientDataSelectedOperation");


                PatientData draggedData = new PatientData(dataName, dataType);
                draggedData.setSelectedOperation(dataOp);

                // the position is used to update the list if you want to remove the showed data
//                int position = item.getIntent().getIntExtra("patientDataPosition", -1);
//                this.serviceList.remove(position);
//                this.serviceListView.getAdapter().notifyDataSetChanged();

                // Turns off any color tints
                view.getBackground().clearColorFilter();

                // Invalidates the view to force a redraw
                view.invalidate();

                View v = (View) event.getLocalState();

                //remove the dragged view
                ViewGroup owner = (ViewGroup) v.getParent();
                owner.removeView(v);

                boolean isLinear = view instanceof LinearLayout;
                if( isLinear ) {
                    LinearLayout c = (LinearLayout) view;
                    DisplayPosition p = this.screenPanel.getChildByID(c.getId());
                    if (p == null) {
                        p = this.rightScreenPanel.getChildByID(c.getId());
                    }
                    p.boundData(draggedData);
                    sendCommandRequest(p);
                    c.removeAllViews();
                    c.addView(v);
                    v.setVisibility(View.VISIBLE);
                }

                return true;

            case DragEvent.ACTION_DRAG_ENDED:
                // Turns off any color tinting
                view.getBackground().clearColorFilter();
                view.invalidate();

                // Does a getResult(), and displays what happened.
                if (event.getResult()) {
                    Log.d("Drag&Drop : ", "Drop Handled correctly !");

                    this.serviceListView.getAdapter().notifyDataSetChanged();
                    this.serviceListView.invalidate();
                }
                else {
                    Log.d("Drag&Drop : ", "Dropped outside Draggable Area : Undo !");
                    ((View) event.getLocalState()).setVisibility(View.VISIBLE);
                }

                return true;

            // An unknown action type was received.
            default:
                Log.e("Drag&Drop :", "Unknown action type received by OnDragListener.");
                break;
        }
        return false;
    }


    private void sendCommandRequest(DisplayPosition displayPosition) {

        Toast.makeText(this, "Data " + displayPosition.getBoundedData().getType() + " is dragged to " + displayPosition.getId(), Toast.LENGTH_SHORT).show();

        //Invio della richiesta al servizio web.
        final Context ctx = this;

        RequestQueue queue = Volley.newRequestQueue(this);

        String url ="http://" + this.hostAddress + "/api/commands";

        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    Toast.makeText(ctx, response.get("value").toString(), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        } ;

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ctx, "ERRORE, QUALCOSA NON HA FUNZIONATO: " + error, Toast.LENGTH_SHORT).show();
            }
        };

        JSONObject requestData = new JSONObject();
        JSONObject paramsData = new JSONObject();

        try {
            paramsData.put("value", displayPosition.getBoundedData().getType());
            paramsData.put("position", displayPosition.getId());

            requestData.put("type", displayPosition.getBoundedData().getSelectedOperation());
            requestData.put("target", "display_SR");
            requestData.put("issuer", "ASTRA_ARM");
            requestData.put("params", paramsData);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("VOLLEY_TEST", "sendCommandRequest: "  + requestData);

        //Request a JSON !
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,url,requestData, listener, errorListener);

        // Add the request to the RequestQueue.
        queue.add(request);
    }

}
