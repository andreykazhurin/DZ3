package dbService.dao;

import accounts.UserProfile;
import dbService.dataSets.UsersDataSet;
import dbService.executor.Executor;
import dbService.executor.ResultHandler;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;


public class UsersDAO {
    private Executor executor;

    public UsersDAO(Connection connection) {
        this.executor = new Executor(connection);
    }

    public UsersDataSet get(long id) throws SQLException {
        return executor.execQuery("select * from users where id=" + id, result -> {
            result.next();
            return new UsersDataSet(result.getLong(1), result.getString(2), result.getString(3));
        });
    }

    public long getUserId(String name) throws SQLException {
        return executor.execQuery("select * from users where login='" + name + "'", result -> {
            result.next();
            return result.getLong(1);
        });
    }

    public Map<String, UserProfile> getAllUsers() throws SQLException {
        return executor.execQuery("select * from users", new ResultHandler<Map<String, UserProfile>>() {
            @Override
            public Map<String, UserProfile> handle(ResultSet resultSet) throws SQLException {
                Map<String, UserProfile> userProfiles = new HashMap<>();
                while (resultSet.next()){
                    UserProfile userProfile = new UserProfile(resultSet.getString(2));
                    userProfiles.put(userProfile.getLogin(), userProfile);
                }
                return userProfiles;
            }
        });
    }


    public void insertUser(String name, String password) throws SQLException {
        executor.execUpdate("insert into users (login, password) values ('" + name + "', '" + password + "')");
    }

    public void createTable() throws SQLException {
        executor.execUpdate("create table if not exists users (id bigint auto_increment, login varchar(256), password varchar(256), primary key (id))");
    }

    public void dropTable() throws SQLException {
        executor.execUpdate("drop table users");
    }
}