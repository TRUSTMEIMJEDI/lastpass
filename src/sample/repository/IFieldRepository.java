package sample.repository;

import sample.model.Account;
import sample.model.Field;

import java.util.List;

public interface IFieldRepository {
    List<Field> getFields();
    void createField(Field field);
    Field getFieldById(int id);
    void deleteField(Field field);
    void updateField(Field field);
    void addAccount(Field field, Account account);
    void deleteAccount(Field field, Account account);
}
