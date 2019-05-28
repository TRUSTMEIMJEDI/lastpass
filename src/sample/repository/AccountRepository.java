package sample.repository;

import org.hibernate.Session;
import sample.HibernateUtil;
import sample.model.Account;

import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

public class AccountRepository implements IAccountRepository {
    @Override
    public List<Account> getAllAccounts() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        CriteriaQuery<Account> criteria = session.getCriteriaBuilder().createQuery(Account.class);
        criteria.from(Account.class);
        List<Account> accounts = session.createQuery(criteria).getResultList();
        session.close();

        return accounts;
    }

    @Override
    public void createAccount(Account account) {
        HibernateUtil.create(account);
    }

    @Override
    public Account getAccountById(int id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        CriteriaQuery<Account> criteriaQuery = session.getCriteriaBuilder().createQuery(Account.class);
        Root<Account> from = criteriaQuery.from(Account.class);
        Predicate condition = session.getCriteriaBuilder().equal(from.get("id"), id);
        criteriaQuery.where(condition);
        Account account = session.createQuery(criteriaQuery).getSingleResult();
        session.close();

        return account;
    }

    @Override
    public void deleteAccount(Account account) {
        HibernateUtil.delete(account);
    }

    @Override
    public void updateAccount(Account account) {
        HibernateUtil.update(account);
    }
}
