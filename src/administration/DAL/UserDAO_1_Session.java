package administration.DAL;

import administration.DTO.UserDTO;

import java.util.ArrayList;
import java.util.List;

public class UserDAO_1_Session implements IUserDAO {
    private ArrayList<UserDTO> users = new ArrayList<>();

    @Override
    public UserDTO getUser(int userId) throws DALException {
        for(UserDTO tempUser: users){
            if(tempUser.getUserId() == userId){
                return tempUser;
            }
        }
        return null;
    }

    @Override
    public UserDTO getUser(String username) throws DALException {
        for(UserDTO tempUser: users){
            if(tempUser.getUserName().toLowerCase().equals(username.toLowerCase())){
                return tempUser;
            }
        }
        return null;
    }

    @Override
    public List<UserDTO> getUserList() throws DALException {
        return users;
    }

    @Override
    public void createUser(UserDTO user) throws DALException {
        if(getUser(user.getUserId()) != null){
            throw new DALException("Bruger med det ID eksisterer allerede");
        }
        for (UserDTO tempUser: users) {
            if(tempUser.getUserName().equals(user.getUserName())){
                throw new DALException("Bruger med det Brugernavn eksisterer allerede");
            }
            if(tempUser.getCpr().equals(user.getCpr())){
                throw new DALException("Bruger med det CPR nummer eksisterer allerede");
            }
        }
        users.add(user);
    }

    @Override
    public void updateUser(UserDTO user) throws DALException {
        deleteUser(user.getUserId());
        users.add(user);
    }

    @Override
    public void deleteUser(int userId) throws DALException {
        for(UserDTO tempUser : users){
            if(tempUser.getUserId() == userId){
                users.remove(tempUser);
                return;
            }
        }
        throw new DALException("Brugeren eksisterer ikke og derfor kunne ikke fjernes");
    }
}
