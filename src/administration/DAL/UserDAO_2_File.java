package administration.DAL;

import administration.DTO.UserDTO;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class UserDAO_2_File implements IUserDAO {
    @Override
    public UserDTO getUser(int userId) throws DALException {
        List<UserDTO> users = readObjectFile();
        for(UserDTO tempUser: users){
            if(tempUser.getUserId() == userId){
                return tempUser;
            }
        }
        return null;
    }

    @Override
    public UserDTO getUser(String username) throws DALException {
        List<UserDTO> users = readObjectFile();
        for(UserDTO tempUser: users){
            if(tempUser.getUserName().toLowerCase().equals(username.toLowerCase())){
                return tempUser;
            }
        }
        return null;
    }

    @Override
    public List<UserDTO> getUserList() throws DALException {
        return readObjectFile();
    }

    @Override
    public void createUser(UserDTO user) throws DALException {
        List<UserDTO> users = readObjectFile();

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
        writeObjectFile(users);
    }

    @Override
    public void updateUser(UserDTO user) throws DALException {
        deleteUser(user.getUserId());
        createUser(user);
    }

    @Override
    public void deleteUser(int userId) throws DALException {
        List<UserDTO> users = readObjectFile();
        for(UserDTO tempUser : users){
            if(tempUser.getUserId() == userId){
                users.remove(tempUser);
                writeObjectFile(users);
                return;
            }
        }
        throw new DALException("Brugeren eksisterer ikke og derfor kunne ikke fjernes");
    }

    public void writeObjectFile(List<UserDTO> users) {
        try(
                FileOutputStream fout = new FileOutputStream("users.ser", false);
                ObjectOutputStream oos = new ObjectOutputStream(fout);
        ){
            for (UserDTO user:users) {
                oos.writeObject(user);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public List<UserDTO> readObjectFile() {
        List<UserDTO> list = new ArrayList<>();
        ObjectInputStream inputStream = null;
        try {
            inputStream = new ObjectInputStream(new FileInputStream("users.ser"));
            while (true) {
                UserDTO p = (UserDTO) inputStream.readObject();
                list.add(p);
            }
        } catch (EOFException eofException) {
            return list;
        } catch (ClassNotFoundException classNotFoundException) {
            System.err.println("Object creation failed.");
        } catch (FileNotFoundException fileNotFound){
            System.out.println("Filen eksister ikke så derfor bliver den oprettet..");
        } catch (IOException ioException) {
            System.err.println("Error opening file.");
        } finally {
            try {
                if (inputStream != null)
                    inputStream.close();
            } catch (IOException ioException) {
                System.err.println("Error closing file.");
            }
        }
        return list;
    }
}
