package Strategy;

import SingletonDatabase.Database;

public interface ValidateData {
    Database db = Database.getOnlyInstance();
    public boolean validate(Object d);
}
