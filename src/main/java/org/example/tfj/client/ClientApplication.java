package org.example.tfj.client;

import org.example.tfj.common.bean.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ClientApplication {
    public static String SERVER_URI = "http://localhost:8080/api/v1/users";
    public static String SERVER_URI_ID = "http://localhost:8080/api/v1/users/{id}";

    public static int printMenu() {
        System.out.println("=== USER API CLIENT ===\n");
        System.out.println("1 - Create a user");
        System.out.println("2 - Read a user (by Id)");
        System.out.println("3 - Read all users");
        System.out.println("4 - Update a user (by Id)");
        System.out.println("5 - Delete a user (by Id)");
        System.out.println("6 - Exit\n");
        System.out.print("Your choice: ");

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        int choice = -1;
        try {
            do {
                choice = Integer.parseInt(bufferedReader.readLine());
            } while(choice > 6 || choice < 1);

            return choice;
        } catch(IOException ioe) {
            System.out.println("Please select a number between 1 and 6!");
            return choice;
        }
    }

    public static User createUser(RestTemplate restTemplate) {
        User user = new User();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        try {
            System.out.println("Enter the last name: " );
            user.setLastName(bufferedReader.readLine());
            System.out.println("Enter the first name: ");
            user.setFirstName(bufferedReader.readLine());
            System.out.println("Enter the email: ");
            user.setEmail(bufferedReader.readLine());
        } catch(IOException ioe) {
            ioe.printStackTrace();
        }

        return restTemplate.postForObject(SERVER_URI, user, User.class);
    }

    public static void readUser(RestTemplate restTemplate) {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        try {
            System.out.println("Enter the ID: ");
            ResponseEntity<User> userResponseEntity =
                    restTemplate.getForEntity(SERVER_URI_ID, User.class, Integer.parseInt(bufferedReader.readLine()));
            System.out.println("Retrieved user: " + userResponseEntity.getBody().toString());
        } catch(NumberFormatException numberFormatException) {
            System.out.println("Please insert an Integer");
        } catch(HttpClientErrorException httpClientErrorException) {
            System.out.println("User not found");
        } catch(IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public static void readUserList(RestTemplate restTemplate) {
        User[] userList = restTemplate.getForObject(SERVER_URI, User[].class);
        System.out.println("Users founds: " + userList.length + " item(s)");
        for(User user : userList) {
            System.out.println(user.toString());
        }

    }

    public static void updateUser(RestTemplate restTemplate) {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        try {
            System.out.println("Enter the ID: ");
            int id = Integer.parseInt(bufferedReader.readLine());
            ResponseEntity<User> userResponseEntity = restTemplate.getForEntity(SERVER_URI_ID, User.class, id);
            User user = userResponseEntity.getBody();

            System.out.println("Enter the last name (" + user.getLastName() + "): " );
            String lastName = bufferedReader.readLine();
            if (!lastName.isEmpty()) {
                user.setLastName(lastName);
            }
            System.out.println("Enter the first name(" + user.getFirstName() + "): ");
            String firstName = bufferedReader.readLine();
            if (!firstName.isEmpty()) {
                user.setFirstName(firstName);
            }

            System.out.println("Enter the email(" + user.getEmail() + "): ");
            String email = bufferedReader.readLine();
            if (!email.isEmpty()) {
                user.setEmail(email);
            }

            restTemplate.put(SERVER_URI_ID, user, id);
        } catch(NumberFormatException numberFormatException) {
            System.out.println("Please insert an Integer");
        } catch(HttpClientErrorException httpClientErrorException) {
            System.out.println("User not found");
        } catch(IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public static void deleteUser(RestTemplate restTemplate) {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        try {
            System.out.println("Enter the ID: ");
            int id = Integer.parseInt(bufferedReader.readLine());
            ResponseEntity<User> userResponseEntity = restTemplate.getForEntity(SERVER_URI_ID, User.class, id);

            restTemplate.delete(SERVER_URI_ID, id);
        } catch(NumberFormatException numberFormatException) {
            System.out.println("Please insert an Integer");
        } catch(HttpClientErrorException httpClientErrorException) {
            System.out.println("User not found");
        } catch(IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public static void main(String[] args) {
        RestTemplate restTemplate = new RestTemplate();

        while(true) {
            int choice = printMenu();

            switch(choice) {
                case 1:
                    System.out.println("Creating a user...\n");
                    createUser(restTemplate);
                    break;
                case 2:
                    System.out.println("Reading a user from ID...\n");
                    readUser(restTemplate);
                    break;
                case 3:
                    System.out.println("Reading all users...\n");
                    readUserList(restTemplate);
                    break;
                case 4:
                    System.out.println("Updating a user from ID...");
                    updateUser(restTemplate);
                    break;
                case 5:
                    System.out.println("Deleting a user from ID...");
                    deleteUser(restTemplate);
                    break;
                case 6:
                    System.out.println("Thank you for using my client!");
                    System.exit(0);
            }
        }
    }
}
