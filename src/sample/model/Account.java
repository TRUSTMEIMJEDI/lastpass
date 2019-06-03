package sample.model;

import lombok.Getter;
import lombok.Setter;
import sample.security.Crypt;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
public class Account implements Serializable {
    private final String hiddenPassword = "********";
    @Id
    @GeneratedValue
    @Getter
    @Setter
    private int id;
    @Getter
    @Setter
    private String username;
    private String password;
    private String creationDate;

    public Account() {
        this.username = "";
        this.password = "";

        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        this.creationDate = date;
    }

    public Account(String username, String password) {
        this.username = username;
        try {
            this.password = Crypt.encrypt(password);
        } catch (Exception e) {
            System.out.println("Password error 1");
            e.printStackTrace();
        }

        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        this.creationDate = date;
    }


    public void setPassword(String password) {
        try {
            this.password = Crypt.encrypt(password);
        } catch (Exception e) {
            System.out.println("Password error 2");
            e.printStackTrace();
        }

    }

    public String getPassword(boolean b) {
        if (b) {
            return this.hiddenPassword;
        } else {
            try {
                return Crypt.decrypt(this.password);
            } catch (Exception e) {
                System.out.println("Password error 3");
                e.printStackTrace();
            }
            return this.hiddenPassword;
        }
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate() {
        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        this.creationDate = (date);
    }

    public Account replaceAccount(Account account) {
        Account account1 = new Account();
        account1.id = this.id;
        account1.username = account.username;
        account1.password = account.password;
        account1.creationDate = account.creationDate;
        return account1;
    }
}
