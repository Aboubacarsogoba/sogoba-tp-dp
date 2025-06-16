package service;

import model.Employee;

public interface NotificationService {
    void notifier(Employee destinataire, String message);
}
