package administration.DAL;

import administration.DTO.UserDTO;

import java.util.List;

public class UserDAO_2_File implements IUserDAO {
    @Override
    public UserDTO getUser(int userId) throws DALException {
        return null;
    }

    @Override
    public UserDTO getUser(String username) throws DALException {
        return null;
    }

    @Override
    public List<UserDTO> getUserList() throws DALException {
        return null;
    }

    @Override
    public void createUser(UserDTO user) throws DALException {

    }

    @Override
    public void updateUser(UserDTO user) throws DALException {

    }

    @Override
    public void deleteUser(int userId) throws DALException {

    }
}
