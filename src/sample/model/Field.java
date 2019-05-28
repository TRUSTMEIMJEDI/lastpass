package sample.model;

import com.sun.istack.Nullable;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Data
@Entity
public class Field implements Serializable {
    @Id
    @GeneratedValue
    private int id;
    private String name;
    private String data;
    private String notes;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "field_id")
    private List<Account> accounts;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }

    public void addAccount(Account account) {
        this.accounts.add(account);
    }

    public void deleteAccount(Account account) {
        this.accounts.remove(account);
    }
}
