package model;

import java.sql.Timestamp;

public class Notification {
    private int id;
    private String message;
    private Timestamp dateEnvoi;
    private int expediteurId;
    private String expediteurNom;
    private String expediteurEmail;

    // Constructeurs
    public Notification() {
    }

    public Notification(int id, String message, Timestamp dateEnvoi, int expediteurId, String expediteurNom, String expediteurEmail) {
        this.id = id;
        this.message = message;
        this.dateEnvoi = dateEnvoi;
        this.expediteurId = expediteurId;
        this.expediteurNom = expediteurNom;
        this.expediteurEmail = expediteurEmail;
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Timestamp getDateEnvoi() {
        return dateEnvoi;
    }

    public void setDateEnvoi(Timestamp dateEnvoi) {
        this.dateEnvoi = dateEnvoi;
    }

    public int getExpediteurId() {
        return expediteurId;
    }

    public void setExpediteurId(int expediteurId) {
        this.expediteurId = expediteurId;
    }

    public String getExpediteurNom() {
        return expediteurNom;
    }

    public void setExpediteurNom(String expediteurNom) {
        this.expediteurNom = expediteurNom;
    }

    public String getExpediteurEmail() {
        return expediteurEmail;
    }

    public void setExpediteurEmail(String expediteurEmail) {
        this.expediteurEmail = expediteurEmail;
    }

    @Override
    public String toString() {
        return "Notification #" + id + ": " + message + " (Envoyée le " + dateEnvoi + " par " + expediteurNom + " <" + expediteurEmail + ">)";
    }
}