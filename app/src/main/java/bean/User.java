package bean;

import org.litepal.crud.LitePalSupport;

import java.io.Serializable;

public class User extends LitePalSupport implements Serializable {
    private long id;
    private String name;
    private String password;
    private String gender;
    private String birthday;
    private String constellation;
    private int astroid;

    public User() {
    }

    public User(String name, String password, String gender, String birthday, String constellation, int astroid) {
        this.name = name;
        this.password = password;
        this.gender = gender;
        this.birthday = birthday;
        this.constellation = constellation;
        this.astroid = astroid;
    }

    public long getId() {
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getConstellation() {
        return constellation;
    }

    public void setConstellation(String constellation) {
        this.constellation = constellation;
    }

    public int getAstroid() {
        return astroid;
    }

    public void setAstroid(int astroid) {
        this.astroid = astroid;
    }
}
