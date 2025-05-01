package io.blss.lab1.utils;

import io.blss.lab1.entity.XmlUser;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "users")
public class UsersWrapper {

    private List<XmlUser> users;

    @XmlElement(name = "user")
    public List<XmlUser> getUsers() {
        return users;
    }

    public void setUsers(List<XmlUser> users) {
        this.users = users;
    }

    public UsersWrapper() {
    }

    public UsersWrapper(List<XmlUser> users) {
        this.users = users;
    }
}