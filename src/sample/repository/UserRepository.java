package sample.repository;

import org.hibernate.Session;
import sample.HibernateUtil;
import sample.model.User;

import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class UserRepository implements IUserRepository {
    @Override
    public User getUserByUsername(String username) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        CriteriaQuery<User> criteriaQuery = session.getCriteriaBuilder().createQuery(User.class);
        Root<User> from = criteriaQuery.from(User.class);
        Predicate condition = session.getCriteriaBuilder().equal(from.get("username"), username);
        criteriaQuery.where(condition);
        User user = session.createQuery(criteriaQuery).getSingleResult();
        session.close();

        return user;
    }

    @Override
    public User getUserById(int id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        CriteriaQuery<User> criteriaQuery = session.getCriteriaBuilder().createQuery(User.class);
        Root<User> from = criteriaQuery.from(User.class);
        Predicate condition = session.getCriteriaBuilder().equal(from.get("id"), id);
        criteriaQuery.where(condition);
        User user = session.createQuery(criteriaQuery).getSingleResult();
        session.close();

        return user;
    }

    @Override
    public void createUser(User user) {
        HibernateUtil.create(user);
    }

    @Override
    public void deleteUser(User user) {
        user.getFields().forEach(field -> field.getAccounts().forEach(account -> HibernateUtil.delete(account)));
        user.getFields().forEach(field -> HibernateUtil.delete(field));
        HibernateUtil.delete(user);
    }

    @Override
    public void updateUser(User user) {
        HibernateUtil.update(user);
    }
}
