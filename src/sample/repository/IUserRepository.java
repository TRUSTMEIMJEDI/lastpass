package sample.repository;

import sample.model.User;

public interface IUserRepository {
    User getUserById(int id);
    User getUserByUsername(String username);
    void createUser(User user);
    void deleteUser(User user);
    void updateUser(User user);
}
