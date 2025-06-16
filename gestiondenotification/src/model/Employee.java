
package model;

import dao.EmployeeDAO;

public  class Employee   {
    private int id;
    private String nom;
    private String email;
    private boolean estAbonne;

    // Constructeurs
    public Employee() {
    }

    public Employee(int id, String nom, String email, boolean estAbonne) {
        this.id = id;
        this.nom = nom;
        this.email = email;
        this.estAbonne = estAbonne;
    }

    // Getters
    public int getId() {
        return this.id;
    }

    public String getNom() {
        return this.nom;
    }

    public String getEmail() {
        return this.email;
    }

    public boolean isEstAbonne() {
        return this.estAbonne;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setEstAbonne(boolean estAbonne) {
        this.estAbonne = estAbonne;
    }

    @Override
    public String toString() {
        return id + " - " + nom + " - " + email + " - Abonné: " + estAbonne;
    }
}