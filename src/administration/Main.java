package administration;

import administration.DAL.IUserDAO;
import administration.DAL.UserDAO_1_Session;
import administration.DAL.UserDAO_2_File;
import administration.DAL.UserDAO_3_Database;
import administration.DTO.UserDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public class Main {
    public static Scanner scanner;
    public static UserDAO_1_Session userDAO_1_session = new UserDAO_1_Session();
    public static UserDAO_2_File userDAO_2_file = new UserDAO_2_File();
    public static UserDAO_3_Database userDAO_3_database = new UserDAO_3_Database();

    public static void main(String[] args) {
        scanner = new Scanner(System.in);

        while(true){
            System.out.println("Skriv tallet for at udføre en kommando: ");
            System.out.println("1. Opret bruger");
            System.out.println("2. Rediger bruger");
            System.out.println("3. Slet bruger");
            System.out.println("4. Hent bruger");
            System.out.println("5. Hent alle brugere");
            System.out.println("9. Afslut programmet \n");


            String command = scanner.nextLine().toLowerCase();

            UserDTO userDTO = new UserDTO();
            if(command.equals("1")){
                userDTO.setUserName(enterUser());
                userDTO.setPassword(enterPassword());
                userDTO.setCpr(enterCPR());
                userDTO.setRoles(enterRoles());
                userDTO.setUserId(enterID());
                try{
                    userDAO_1_session.createUser(userDTO);
                }
                catch (IUserDAO.DALException exception){
                    System.out.println("Brugeren kunne ikke oprettes i SESSION ARRAYLIST fordi: " + exception.toString());
                    continue;
                }
                System.out.println("Brugeren er nu oprettet. Klik Enter for at forsætte");
                scanner.nextLine();
            }

            if(command.equals("2")){
                userDTO = enterFindUser();
                if(userDTO == null) continue;
                System.out.println("Bruger fundet : " + userDTO);
                System.out.println("Du kan nu opdatere brugerens oplysninger");

                userDTO.setUserName(enterUser());
                userDTO.setPassword(enterPassword());
                userDTO.setCpr(enterCPR());
                userDTO.setRoles(enterRoles());
                try{
                    userDAO_1_session.updateUser(userDTO);
                }
                catch (IUserDAO.DALException exception){
                    System.out.println("Brugeren kunne ikke opdateres i SESSION ARRAYLIST fordi: " + exception.toString());
                    continue;
                }
                System.out.println("Brugeren er nu opdateret. Klik Enter for at forsætte");
                scanner.nextLine();
            }

            if(command.equals("3")){
                userDTO = enterFindUser();
                if(userDTO == null) continue;

                System.out.println("Skriv Ja for at slette " + userDTO.getUserName() + " ellers bare klik enter");
                String confirm = scanner.nextLine();
                if(confirm.toLowerCase().equals("ja")){
                    try{
                        userDAO_1_session.deleteUser(userDTO.getUserId());
                        System.out.println("Brugeren er nu slettet. Klik Enter for at forsætte");
                        scanner.nextLine();
                    }
                    catch (IUserDAO.DALException exception){
                        System.out.println("Brugeren kunne ikke slettes " + exception.toString());
                    }
                }
            }

            if(command.equals("4")){
                userDTO = enterFindUser();
                if(userDTO == null) continue;

                System.out.println(userDTO.toString());
                System.out.println("Klik Enter for at forsætte");
                scanner.nextLine();
            }
            if(command.equals("5")){
                try {
                    List<UserDTO> users = userDAO_1_session.getUserList();
                    for(UserDTO user : users){
                        System.out.println(user);
                    }
                    System.out.println("Klik Enter for at forsætte");
                    scanner.nextLine();
                } catch (IUserDAO.DALException exception){
                    System.out.println("Brugerne kunne ikke findes");
                    continue;
                }
            }

            if(command.equals("9")){
                break;
            }
        }
    }

    public static String enterUser(){
        System.out.println("Indtast brugernavn:");
        String username;
        while(true){
            username = scanner.nextLine();
            if(username.length() < 3 || username.length() > 16){
                System.out.println("Brugernavnet skal være mellem 3-16 karekterer langt");
            }else {
                break;
            }
        }
        return username;
    }
    public static String enterPassword(){
        String pass1, pass2;
        while(true) {
            System.out.println("Indtast password");
            pass1 = scanner.nextLine();
            if(pass1.length() < 3 || pass1.length() > 16){
                System.out.println("Passwordet skal være mellem 3-16 karekterer langt");
                continue;
            }
            System.out.println("Indtast password igen for at bekræfte");
            pass2 = scanner.nextLine();
            if (pass1.equals(pass2)){
                break;
            }else{
                System.out.println("Passwordene skal være ens");
            }
        }
        return pass1;
    }
    public static String enterCPR(){
        System.out.println("Indtast CPR nummer");
        String cpr;
        while(true){
            cpr = scanner.nextLine();
            if(cpr.length() != 10){
                System.out.println("CPR nummeret skal være 10 karekterer langt");
            }else {
                break;
            }
        }
        return cpr;
    }

    public static ArrayList<String> enterRoles(){
        ArrayList<String> roller = new ArrayList<>();
        while(true) {
            System.out.println("Indtast roller separeret med mellemrum");
            System.out.println("- Admin");
            System.out.println("- Pharmacist");
            System.out.println("- Foreman");
            System.out.println("- Operator");
            String[] rollerString = scanner.nextLine().split(" ");
            boolean notARole = false;
            for (String rolle : rollerString) {
                rolle = rolle.trim().toLowerCase();
                if(!rolle.equals("admin") && !rolle.equals("pharmacist") && !rolle.equals("foreman") && !rolle.equals("operator")){
                    System.out.println("Du skal indtaste en rigtig rolle!");
                    notARole = true;
                    break;
                }
                roller.add(rolle);
            }
            if(!notARole) break;
        }
        return roller;
    }
    public static int enterID(){
        System.out.println("Indtast ønskede ID: ");
        int userId;
        while(true){
            userId = Integer.parseInt(scanner.nextLine());
            if(userId < 1){
                System.out.println("ID skal være større end 0");
            }else {
                break;
            }
        }
        return userId;
    }

    public static UserDTO enterFindUser(){
        System.out.println("Ønsker brugeren fundet via ID eller brugernavn?");
        System.out.println("1. ID");
        System.out.println("2. Brugernavn");
        int command = Integer.parseInt(scanner.nextLine());
        UserDTO userDTO = null;
        if(command == 1){
            System.out.println("Indtast ID for at finde brugeren");
            int userId = Integer.parseInt(scanner.nextLine());
            try{
                userDTO = userDAO_1_session.getUser(userId);
                if(userDTO == null){
                    System.out.println("Brugeren eksisterer ikke");
                }
            }
            catch(IUserDAO.DALException exception){
                System.out.println("Fejl ved hentning af bruger");
            }

        }else if(command == 2){
            System.out.println("Indtast brugernavn for at finde brugeren");
            String username = scanner.nextLine();
            try{
                userDTO = userDAO_1_session.getUser(username);
                if(userDTO == null){
                    System.out.println("Brugeren eksisterer ikke");
                }
            }
            catch(IUserDAO.DALException exception){
                System.out.println("Fejl ved hentning af bruger");
            }
        }
        return userDTO;
    }
}
