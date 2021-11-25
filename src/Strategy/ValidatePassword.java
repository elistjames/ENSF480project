package Strategy;

import User.User;

public class ValidatePassword implements ValidateData{
    @Override
    public boolean validate(Object d) {
        String password = d.toString();
        for (User user : db.getUsers()) {
            if (user.getPassword().compareTo(password)==0)
                return false;
        }
        return true;
    }
}
