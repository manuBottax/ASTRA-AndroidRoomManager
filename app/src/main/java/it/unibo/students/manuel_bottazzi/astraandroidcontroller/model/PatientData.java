package it.unibo.students.manuel_bottazzi.astraandroidcontroller.model;

import java.util.ArrayList;
import java.util.List;

public class PatientData {

    public enum DataCategory {
        VISUALISATION_ONLY,
        MONITORING_ONLY
    }

    private String name;
    private String dataType;
    private List<String> availableOperationList;
    private String selectedOperation;

    public PatientData(String name, String dataType){
        this.name = name;
        this.dataType = dataType;

        this.availableOperationList = new ArrayList<>();

        this.availableOperationList.add("Visualizza");
        this.availableOperationList.add("Monitora");

        this.selectedOperation = this.availableOperationList.get(0);
    }

    public PatientData(String name, String dataType, DataCategory category){
        this.name = name;
        this.dataType = dataType;

        this.availableOperationList = new ArrayList<>();

        if (category == DataCategory.MONITORING_ONLY){
            this.availableOperationList.add("Monitora");
        } else if (category == DataCategory.VISUALISATION_ONLY) {
            this.availableOperationList.add("Visualizza");
        }

        this.selectedOperation = this.availableOperationList.get(0);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDataType() {
        return this.dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public List<String> getAvailableOperationList() {
        return availableOperationList;
    }

    public void setAvailableOperationList(List<String> availableOperationList) {
        this.availableOperationList = availableOperationList;
        this.selectedOperation = this.availableOperationList.get(0);
    }

    public String getSelectedOperation() {
        if (this.selectedOperation.equals("Visualizza")) {
            return "visualisation";
        } else if (this.selectedOperation.equals("Monitora")){
            return "monitoring";
        } else {
            return this.selectedOperation;
        }
    }

    public void setSelectedOperation(String selectedOperation) {
        this.selectedOperation = selectedOperation;
    }
}
