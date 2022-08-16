package edu.SOV.domain;

public enum StudentOrderStatus {
    START,CHECKED;

    public static StudentOrderStatus getStatusById(int id){
        for (StudentOrderStatus studentOrderStatus:StudentOrderStatus.values()){
            if(studentOrderStatus.ordinal()==id){
                return studentOrderStatus;
            }
        }
        throw new RuntimeException("Unknown value: " + id);
    }
}
