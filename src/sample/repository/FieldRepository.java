package sample.repository;

import org.hibernate.Session;
import sample.HibernateUtil;
import sample.model.Account;
import sample.model.Field;

import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

public class FieldRepository implements IFieldRepository {
    @Override
    public List<Field> getFields() {
        Session session = HibernateUtil.getSessionFactory().openSession();

        CriteriaQuery<Field> criteria = session.getCriteriaBuilder().createQuery(Field.class);
        criteria.from(Field.class);

        List<Field> fields = session.createQuery(criteria).getResultList();

        session.close();

        return fields;
    }

    @Override
    public void createField(Field field) {
        HibernateUtil.create(field);
    }

    @Override
    public Field getFieldById(int id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        CriteriaQuery<Field> criteriaQuery = session.getCriteriaBuilder().createQuery(Field.class);
        Root<Field> from = criteriaQuery.from(Field.class);
        Predicate condition = session.getCriteriaBuilder().equal(from.get("id"), id);
        criteriaQuery.where(condition);
        Field field = session.createQuery(criteriaQuery).getSingleResult();
        session.close();

        return field;
    }

    @Override
    public void addAccount(Field field, Account account) {
        field.addAccount(account);
        HibernateUtil.update(field);
    }

    @Override
    public void deleteAccount(Field field, Account account) {
        field.deleteAccount(account);
        HibernateUtil.update(field);
    }

    @Override
    public void deleteField(Field field) {
        field.getAccounts().forEach(account -> HibernateUtil.delete(account));
        HibernateUtil.delete(field);
    }

    @Override
    public void updateField(Field field) {
        HibernateUtil.update(field);
    }
}
