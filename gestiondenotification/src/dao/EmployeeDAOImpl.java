package dao;

import model.Employee;

import java.sql.*;
import java.util.*;

public class EmployeeDAOImpl implements EmployeeDAO {
    private Connection conn;

    public EmployeeDAOImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void ajouter(Employee e) {
        if (e == null || e.getNom() == null || e.getEmail() == null) {
            System.err.println("Erreur : L'employé ou ses attributs (nom, email) ne peuvent pas être nuls.");
            return;
        }
        String sql = "INSERT INTO Employee (nom, email, est_abonne) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, e.getNom());
            stmt.setString(2, e.getEmail());
            stmt.setBoolean(3, e.isEstAbonne());
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Employé ajouté avec succès.");
            } else {
                System.err.println("Échec de l'ajout de l'employé.");
            }
        } catch (SQLException ex) {
            System.err.println("Erreur SQL lors de l'ajout : " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    @Override
    public List<Employee> lister() {
        List<Employee> liste = new ArrayList<>();
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM Employee")) {
            while (rs.next()) {
                Employee e = new Employee();
                e.setId(rs.getInt("id"));
                e.setNom(rs.getString("nom"));
                e.setEmail(rs.getString("email"));
                e.setEstAbonne(rs.getBoolean("est_abonne"));
                liste.add(e);
            }
            if (liste.isEmpty()) {
                System.out.println("Aucun employé trouvé dans la base de données.");
            }
        } catch (SQLException ex) {
            System.err.println("Erreur SQL lors de la récupération de la liste : " + ex.getMessage());
            ex.printStackTrace();
        }
        return liste;
    }

    @Override
    public Employee getParId(int id) {
        String sql = "SELECT * FROM Employee WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Employee e = new Employee();
                    e.setId(rs.getInt("id"));
                    e.setNom(rs.getString("nom"));
                    e.setEmail(rs.getString("email"));
                    e.setEstAbonne(rs.getBoolean("est_abonne"));
                    return e;
                } else {
                    System.out.println("Aucun employé trouvé avec l'ID : " + id);
                }
            }
        } catch (SQLException ex) {
            System.err.println("Erreur SQL lors de la recherche par ID : " + ex.getMessage());
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public Employee getParEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            System.err.println("Erreur : L'email ne peut pas être vide.");
            return null;
        }
        String sql = "SELECT * FROM Employee WHERE email = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Employee e = new Employee();
                    e.setId(rs.getInt("id"));
                    e.setNom(rs.getString("nom"));
                    e.setEmail(rs.getString("email"));
                    e.setEstAbonne(rs.getBoolean("est_abonne"));
                    return e;
                } else {
                    System.out.println("Aucun employé trouvé avec l'email : " + email);
                }
            }
        } catch (SQLException ex) {
            System.err.println("Erreur SQL lors de la recherche par email : " + ex.getMessage());
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public void desabonner(int id) {
        String sql = "UPDATE Employee SET est_abonne = false WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Employé désabonné avec succès.");
            } else {
                System.err.println("Aucun employé trouvé avec l'ID : " + id);
            }
        } catch (SQLException ex) {
            System.err.println("Erreur SQL lors du désabonnement : " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    @Override
    public void desabonnerParEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            System.err.println("Erreur : L'email ne peut pas être vide.");
            return;
        }
        String sql = "UPDATE Employee SET est_abonne = false WHERE email = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Employé avec l'email " + email + " désabonné avec succès.");
            } else {
                System.err.println("Aucun employé trouvé avec l'email : " + email);
            }
        } catch (SQLException ex) {
            System.err.println("Erreur SQL lors du désabonnement par email : " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    @Override
    public List<Employee> getEmployesAbonnesExcluant(int id) {
        List<Employee> liste = new ArrayList<>();
        String sql = "SELECT * FROM Employee WHERE est_abonne = true AND id != ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Employee e = new Employee();
                    e.setId(rs.getInt("id"));
                    e.setNom(rs.getString("nom"));
                    e.setEmail(rs.getString("email"));
                    e.setEstAbonne(rs.getBoolean("est_abonne"));
                    liste.add(e);
                }
                if (liste.isEmpty()) {
                    System.out.println("Aucun employé abonné trouvé (excluant ID : " + id + ").");
                }
            }
        } catch (SQLException ex) {
            System.err.println("Erreur SQL lors de la récupération des abonnés : " + ex.getMessage());
            ex.printStackTrace();
        }
        return liste;
    }

    @Override
    public List<Employee> listerAbonnes() {
        List<Employee> liste = new ArrayList<>();
        String sql = "SELECT * FROM Employee WHERE est_abonne = true";
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Employee e = new Employee();
                e.setId(rs.getInt("id"));
                e.setNom(rs.getString("nom"));
                e.setEmail(rs.getString("email"));
                e.setEstAbonne(rs.getBoolean("est_abonne"));
                liste.add(e);
            }
            if (liste.isEmpty()) {
                System.out.println("Aucun employé abonné trouvé.");
            }
        } catch (SQLException ex) {
            System.err.println("Erreur SQL lors de la récupération des abonnés : " + ex.getMessage());
            ex.printStackTrace();
        }
        return liste;
    }
}
