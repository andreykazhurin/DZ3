package accounts;

import dbService.DBService;
import dbService.dao.UsersDAO;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class AccountService {

    private final Map<String, UserProfile> loginToProfile;
    private final Map<String, UserProfile> sessionIdToProfile;
    private static UsersDAO usersDAO = new UsersDAO(DBService.getH2Connection());

    public AccountService() throws SQLException {
        sessionIdToProfile = new HashMap<>();
        loginToProfile = usersDAO.getAllUsers();
    }

    public void addNewUser(UserProfile userProfile)  {
        if(!loginToProfile.containsKey(userProfile.getLogin())) {
            loginToProfile.put(userProfile.getLogin(), userProfile);
            try {
                usersDAO.insertUser(userProfile.getLogin(), userProfile.getPass());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public UserProfile getUserByLogin(String login) {
        return loginToProfile.get(login);
    }

    public UserProfile getUserBySessionId(String sessionId) {
        return sessionIdToProfile.get(sessionId);
    }

    public void addSession(String sessionId, UserProfile userProfile) {
        sessionIdToProfile.put(sessionId, userProfile);
    }

    public void deleteSession(String sessionId) {
        sessionIdToProfile.remove(sessionId);
    }


}
