package Strategy;

import User.User;

public class ValidateUsername implements ValidateData {
    @Override
    public boolean validate(Object d) {
        String username = d.toString();
        for (User user : db.getUsers()) {
            if (user.getUsername().compareTo(username)==0)
                return false;
        }
        return true;
    }
}
