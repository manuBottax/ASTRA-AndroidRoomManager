package it.unibo.students.manuel_bottazzi.astraandroidcontroller.model;

import java.util.ArrayList;
import java.util.List;

public class PatientData {

    private String name;
    private String type;
    private boolean inUse;
    private List<String> availableOperationList;
    private String selectedOperation;

    public PatientData(String name, String type){
        this.name = name;
        this.type = type;
        this.inUse = false;

        this.availableOperationList = new ArrayList<>();

        this.availableOperationList.add("Visualizza");
        this.availableOperationList.add("Monitora");

        this.selectedOperation = this.availableOperationList.get(0);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setInUse(boolean inUse) {
        this.inUse = inUse;
    }

    public boolean isInUse() {
        return inUse;
    }

    public List<String> getAvailableOperationList() {
        return availableOperationList;
    }

    public void setAvailableOperationList(List<String> availableOperationList) {
        this.availableOperationList = availableOperationList;
    }

    public String getSelectedOperation() {
        if (this.selectedOperation.equals("Visualizza")) {
            return "visualise";
        } else if (this.selectedOperation.equals("Monitora")){
            return "monitor";
        } else {
            return this.selectedOperation;
        }
    }

    public void setSelectedOperation(String selectedOperation) {
        this.selectedOperation = selectedOperation;
    }
}
