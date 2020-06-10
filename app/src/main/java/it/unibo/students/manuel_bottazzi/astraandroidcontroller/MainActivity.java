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

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import it.unibo.students.manuel_bottazzi.astraandroidcontroller.adapter.PatientDataListAdapter;
import it.unibo.students.manuel_bottazzi.astraandroidcontroller.layout_manager.BasePanel;
import it.unibo.students.manuel_bottazzi.astraandroidcontroller.layout_manager.DisplayPosition;
import it.unibo.students.manuel_bottazzi.astraandroidcontroller.layout_manager.ParentPanel;
import it.unibo.students.manuel_bottazzi.astraandroidcontroller.layout_manager.ScreenPanel;
import it.unibo.students.manuel_bottazzi.astraandroidcontroller.layout_manager.SystemPanel;
import it.unibo.students.manuel_bottazzi.astraandroidcontroller.model.PatientData;

public class MainActivity extends AppCompatActivity implements View.OnDragListener{

    private static final int PARENT_ID = 42;

    private ScreenPanel screenPanel;

    private RecyclerView serviceListView;
    private PatientDataListAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private List<PatientData> serviceList;

    private String hostAddress;
    private String port;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_custom_layout);

        this.hostAddress = "192.168.1.120";
        this.port = "3010";

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

        LinearLayout screenLayout = findViewById(R.id.screen_layout);
        screenLayout.setBackgroundColor(Color.BLUE);

        this.screenPanel = new ScreenPanel(this, PARENT_ID);

        ParentPanel topScreenPanel = new ParentPanel(this, PARENT_ID, 2048,150, LinearLayout.HORIZONTAL);

        BasePanel top1 = new BasePanel(this, 1, 700, 150);
        top1.setOnDragListener(this);

        BasePanel top2 = new BasePanel(this, 2, 700, 150);
        top2.setOnDragListener(this);

        BasePanel top3 = new BasePanel(this, 3, 700, 150);
        top3.setOnDragListener(this);

        topScreenPanel.addChildPanel(top1);
        topScreenPanel.addChildPanel(top2);
        topScreenPanel.addChildPanel(top3);

        ParentPanel bottomScreenPanel = new ParentPanel(this, PARENT_ID, 2048,1250, LinearLayout.HORIZONTAL);

        BasePanel leftScreenPanel = new BasePanel(this, 4, 1100, 1250);
        leftScreenPanel.setOnDragListener(this);

        ParentPanel rightScreenPanel = new ParentPanel(this, PARENT_ID, 1024,1250, LinearLayout.VERTICAL);

        BasePanel rightPanel1 = new BasePanel(this, 5,  1024, 125);
        rightPanel1.setOnDragListener(this);
        rightScreenPanel.addChildPanel(rightPanel1);

        BasePanel rightPanel2 = new BasePanel(this, 6, 1024, 125);
        rightPanel2.setOnDragListener(this);
        rightScreenPanel.addChildPanel(rightPanel2);

        SystemPanel rightPanel3 = new SystemPanel(this,7, 1024, 125);
        rightScreenPanel.addChildPanel(rightPanel3);

        bottomScreenPanel.addChildPanel(leftScreenPanel);
        bottomScreenPanel.addChildPanel(rightScreenPanel);

        this.screenPanel.addChildPanel(topScreenPanel);
        this.screenPanel.addChildPanel(bottomScreenPanel);

        screenLayout.addView(this.screenPanel.getPanel());
    }

    @NotNull
    private List<PatientData> retrieveAvailableData(){
        List<PatientData> lst = new ArrayList<>();

        //TODO: recuperare i dati disponibili dal sistema -> aggiornare man mano che diventano disponibili
        // DATI DI ESEMPIO

        // Vital Parameters
        lst.add(new PatientData("Pressione Arteriosa", "blood_pressure"));
        lst.add(new PatientData("Saturazione", "spO2"));
        lst.add(new PatientData("Frequenza Cardiaca", "heart_rate"));
        lst.add(new PatientData("Temperatura", "temperature"));

        //Biometrical Data
        lst.add(new PatientData("Livello CO2", "CO2_level", PatientData.DataCategory.MONITORING_ONLY));
        lst.add(new PatientData("Emogas Analisi", "ega", PatientData.DataCategory.MONITORING_ONLY));
        lst.add(new PatientData("Rotem", "rotem", PatientData.DataCategory.MONITORING_ONLY));

        //Diagnostic data
        lst.add(new PatientData("RX Torace", "chest_rx", PatientData.DataCategory.VISUALISATION_ONLY));
        lst.add(new PatientData("TAC", "tac", PatientData.DataCategory.VISUALISATION_ONLY));
        lst.add(new PatientData("ECG", "ecg"));
        lst.add(new PatientData("Esami del sangue", "blood_tests", PatientData.DataCategory.VISUALISATION_ONLY));

        //Temporal Data
        //lst.add(new PatientData("Tempo Stimato di arrivo", "eta", PatientData.DataCategory.MONITORING_ONLY));
        lst.add(new PatientData("Tempo Totale", "total_time", PatientData.DataCategory.MONITORING_ONLY));
        lst.add(new PatientData("Tempo Procedura", "procedure_time", PatientData.DataCategory.MONITORING_ONLY));

        //Personal Data
        lst.add(new PatientData("Dati anagrafici del paziente", "patient_details", PatientData.DataCategory.VISUALISATION_ONLY));

        //Trauma Tracker Data
        lst.add(new PatientData("Report Trauma Tracker", "tt_report"));

        //Environment Data
        lst.add(new PatientData("Sacche di sangue utilizzate", "used_blood_unit"));

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
                    DisplayPosition p = this.screenPanel.getChildPanel(c.getId());
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

        Toast.makeText(this, "Data " + displayPosition.getBoundedData().getDataType() + " is dragged to " + displayPosition.getId(), Toast.LENGTH_SHORT).show();

        //Invio della richiesta al servizio web.
        final Context ctx = this;

        RequestQueue queue = Volley.newRequestQueue(this);

        String url ="http://" + this.hostAddress + ":" + this.port + "/api/commands";

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
            paramsData.put("value", displayPosition.getBoundedData().getDataType());
            paramsData.put("position", displayPosition.getId());

            requestData.put("type", displayPosition.getBoundedData().getSelectedOperation());
            requestData.put("category", "room");
            requestData.put("target", "display_sr");
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
