package main.java.com.hospital.model;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Appointment{
    private int id;
    private int patientId;
    private int doctorId;
    private Timestamp date;
    private int durationMinutes ;
    private String type;
    private String status;
    private String notes;

    public Appointment(){

    }
    public Appointment(int id,int patientId,int doctorId,Timestamp date,int durationMinutes,String type,String status,String notes){
        this.id =id;
        this.patientId = patientId;
        this.doctorId =doctorId;
        this.date=date;
        this.durationMinutes=durationMinutes;
        this.type =type;
        this.status=status;
        this.notes=notes;


    }
    //getters
    public int getId() {
        return id;
    }
    public int getPatientId() {
        return patientId;
    }
    public int getDoctorId() {
        return doctorId;
    }
    public Timestamp getDate() {
        return date;
    }
    public int getDurationMinutes() {
        return durationMinutes;
    }
    public String getType() {
        return type;
    }
    public String getStatus() {
        return status;
    }
    public String getNotes() {
        return notes;
    }
    //setters
    public void setId(int id) {
        this.id = id;
    }
    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }
    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }
    public void setDate(Timestamp date) {
        this.date = date;
    }
    public void setDurationMinutes(int durationMinutes) {
        this.durationMinutes = durationMinutes;
    }
    public void setType(String type) {
        this.type = type;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public void setNotes(String notes) {
        this.notes = notes;
    }
    
    //methodes
    /// Pour afficher la date f tableau (ex: 16/01/2026 14:00)
    public String getFormattedDate(){
        if(date == null ) return "";
        return new SimpleDateFormat("dd/MM/yyyy  HH:mm").format(date);
    }
    
    //But : T-calculer fuqach ghadi i-ssali rdv (Date de début + Duration).
    //Pourquoi : Mo-himma bzaf! Bach melli n-bghio n-diro rdv jdid l nefss tbib, n-checkiw wach ma-3ndouch deja chi rdv akhor f dak l-weqt
    public Timestamp getEndTimestamp() {
        if (date == null) return null;
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(date.getTime());
        cal.add(Calendar.MINUTE, durationMinutes);
        return new Timestamp(cal.getTime().getTime());
    }

    public boolean isPast() {
        return date != null && date.before(new Timestamp(System.currentTimeMillis()));
    }
    
    public boolean canBeModified() {
        return !"Terminé".equalsIgnoreCase(this.status) && !"Annulé".equalsIgnoreCase(this.status);
    }
    public boolean isFinished(){
        return "termine".equalsIgnoreCase(this.status);

    }
    public boolean isCancelled(){
        return "annule".equalsIgnoreCase(this.status);

    }
    public boolean isPending(){
        return "en attente".equalsIgnoreCase(this.status);

    }
    public  void markAsFinished(){
        this.status="termine";
    }

    public String toString() {
        return "RDV [ID=" + id + ", Patient=" + patientId + ", Doctor=" + doctorId + ", Date=" + getFormattedDate() + "]";
    }





}