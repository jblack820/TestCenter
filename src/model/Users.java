package model;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 *
 * @author takacs.gergely
 */
public class Users {

    private Set<User> userList = new LinkedHashSet<>();

    public Users() {
    }

    public void addUser(User u) {
        this.userList.add(u);
    }

    public void updateUsers(Set<User> users) {
        this.userList = users;
    }

    public Set<User> getUserList() {
        return userList;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Users{userList=").append(userList);
        sb.append('}');
        return sb.toString();
    }

    public boolean isUserExist(String userKey) {
        return userList.stream().filter(p -> p.getUserKey().equalsIgnoreCase(userKey)).collect(Collectors.toList()).size() > 0;
    }

    public List<User> getTesterList() {
        return this
                .getUserList()
                .stream()
                .filter(p -> p.getRole() == UserRole.TESTER)
                .collect(Collectors.toList());
    }
    
    public List<String> getTesterNamesList (){
        return getTesterList().stream().map(p->p.getFullname()).collect(Collectors.toList());
    }

    public User getUserByUserKey(String userKey) {

        User user = null;

        Optional<User> optional = userList
                .stream()
                .filter(p -> p.getUserKey().equalsIgnoreCase(userKey))
                .findFirst();

        if (optional.isPresent()) {
            user = optional.get();
        }
        return user;
    }
}
