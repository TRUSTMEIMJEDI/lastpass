package sample.repository;

import sample.model.Account;

import java.util.List;

public interface IAccountRepository {
    List<Account> getAllAccounts();
    void createAccount(Account account);
    Account getAccountById(int id);
    void deleteAccount(Account account);
    void updateAccount(Account account);
}
