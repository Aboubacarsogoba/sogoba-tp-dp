package dao;

import model.Employee;
import java.util.List;

public interface EmployeeDAO {
    void ajouter(Employee e);
    List<Employee> lister();
    Employee getParId(int id);
    Employee getParEmail(String email); // Nouvelle méthode
    void desabonner(int id);
    void desabonnerParEmail(String email);
    List<Employee> getEmployesAbonnesExcluant(int id);
    List<Employee> listerAbonnes();
}
