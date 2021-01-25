import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;
import java.io.*;
import java.lang.*;

public class mainonediffp {

    public static void main(String args[]) throws Exception {

        System.out.println("Welcome peeps. \nPress '1' to log in to your account. Press '2' to create an account.");
        Scanner sc = new Scanner(System.in);
        int whatToDo = 0;
        String information = "";
        int at = 0;
        int x = 0;
        int y = 0;
        int z = 0;
        String written = "";
        String hashed = "";

        while (whatToDo == 0) {
            try {
                whatToDo = sc.nextInt();
            } catch (Exception e) {
                System.out.println("No. Try again.");
                whatToDo = sc.nextInt();
            }
            if (whatToDo > 2) {
                System.out.println("Please enter a valid input");
                whatToDo = 0;
            }
        }

        switch(whatToDo) {
            case 1:
                while(z == 0) {
                    System.out.println("Enter your username.");
                    Scanner newInput = new Scanner(System.in);
                    information = newInput.nextLine();
                    if ((checkUN(information))) { //the username exists
                        x = 1;
                    }
                    else {
                        x = -1;
                    }

                    System.out.println("Enter your password.");
                    String info = "";
                    info = newInput.nextLine();
                    if ((checkPW(info))) { //the password exists
                        y = 0;
                        if(findUNLine(information) == findPWLine(info)){ //the username and password are together
                            y = 1;
                        }
                    } else {
                        y = -1;
                    }

                    if ((x == -1) || (y == -1) || (y == 0)) {
                        System.out.println("Your username and/or password was invalid. Try again.");
                        z = 0; //loop to beginning of case
                    }
                    else if ((x == 1) && (y == 1)){
                        BufferedReader reader = new BufferedReader(new FileReader("storedInfo2.txt"));
                        String b = reader.readLine();
                        while(b != null){
                            if(checkUN(information)){
                                int c = b.indexOf(","); //after = username
                                int d = b.indexOf(",", c+1); //after = password
                                int e = b.indexOf(",", d+1); //after = account type

                                if(b.substring(e+2).equals("u")){
                                    userMenu();
                                    break;
                                }
                                else{
                                    adminMenu();
                                    break;
                                }

                            }
                        }
                        z = 3;
                    }
                    else{
                        z = 0;
                    }
                }
                break;
            case 2:
                BufferedWriter writer = new BufferedWriter(new FileWriter("storedInfo2.txt", true));

                System.out.println("What is your name?");
                String n = sc.nextLine();
                n = sc.nextLine();

                System.out.println("What is your username?");
                String u = sc.nextLine();
                //u = sc.nextLine();
                String s = "";
                int h = 0;

                if(checkUN(u)){ //true = the username exists
                    h++;
                    s = checkLoop(checkUN(u));
                }
                else{
                    written = n + ", " + u + ", ";
                }

                if(h > 0){
                    written = n + ", " + s + ", ";
                    System.out.println("written = " + written);
                }

                System.out.println("What is your password?");
                String p = sc.next();
                hashed = toHexString(getSHA(p));
                written = written + hashed + ", ";
                writer.close();

                System.out.println("What type of account do you have?" +
                        "\nIf you would like to create an user account, press '1'." +
                        "\nIf you would like to create an administrator account, press '2'.");

        }

        while(at == 0) {
            try {
                at = sc.nextInt();
            } catch (Exception c) {
                System.out.println("No. Try again.");
                at = sc.nextInt(); //loop it
            }
            if(at > 2){
                System.out.println("Please enter a valid input");
                at = 0;
            }
        }

        switch(at){
            case 1:
                BufferedWriter writer = new BufferedWriter(new FileWriter("storedInfo2.txt", true));
                written = written + "u";
                writer.write(written);
                writer.newLine(); // starts a new line, NOTE there is an extra line
                writer.close();
                break;

            case 2:
                BufferedWriter writer2 = new BufferedWriter(new FileWriter("storedInfo2.txt", true));
                written = written + "a";
                writer2.write(written);
                writer2.newLine(); // starts a new line, NOTE there is an extra line
                writer2.close();
                break;
        }

    }


    public static boolean checkUN(String username) throws Exception{
        String lc = username.toLowerCase();
        BufferedReader reader = new BufferedReader(new FileReader("storedInfo2.txt"));
        String at = reader.readLine();

        while(at != null) {

            for (int x = at.indexOf(","); x < at.length()-lc.length() - 1; x++) {

                if (at.substring(x + 2, lc.length() + x + 2).equals(lc)) {
                    if(at.substring(lc.length() + x + 2, lc.length() + x + 3).equals(",")) { //ensures that it is the entire username and not just a part of it
                        reader.close();
                        return true;
                    }
                }

            }
            at = reader.readLine();
        }

        reader.close();
        return false;
    }

    public static int findUNLine(String username) throws Exception{
        String lc = username.toLowerCase();
        BufferedReader reader = new BufferedReader(new FileReader("storedInfo2.txt"));
        String at = reader.readLine();
        int num = 0;

        while(at != null) {

            for (int x = at.indexOf(","); x < at.length()-lc.length() - 1; x++) {
                //System.out.println(x + " " + at.substring(x + 2, lc.length() + x + 2));
                if (at.substring(x + 2, lc.length() + x + 2).equals(lc)) {
                    reader.close();
                    return num;
                }
                else{
                    break;
                }
            }
            at = reader.readLine();
            num++;
        }

        reader.close();
        System.out.println("findUNLine = "+num);
        return num;
    }

    public static String checkLoop(boolean exists) throws Exception{
        Scanner sc = new Scanner(System.in);
        String newInput = "";

        int num = 0;
        while (newInput.equals("")) {
            if(num == 0) {
                if (exists) {
                    System.out.println("Sorry. That username has been taken already. Choose another username.");
                    newInput = "";
                }
                else{
                    return newInput;
                }
            }
            else {
                try {
                    newInput = sc.nextLine();
                } catch (Exception e) {
                    System.out.println("That username has been taken already. Choose another username.");
                    newInput = sc.nextLine(); //loop it
                }

                if (checkUN(newInput)){
                    System.out.println("Sorry That username has been taken already. Choose another username.");
                    newInput = "";
                }
                else{
                    return newInput;
                }
            }
            num++;
        }

        return newInput;
    }

    public static boolean checkPW(String password) throws Exception{
        String lc = password.toLowerCase();
        String fin = toHexString(getSHA(lc));
        BufferedReader reader = new BufferedReader(new FileReader("storedInfo2.txt"));
        String at = reader.readLine();

        while(at != null) {

            int a = at.indexOf(",");

            for (int x = at.indexOf(",", a+1); x < at.length()-fin.length() - 1; x++) {

                if (at.substring(x + 2, x + 2 + fin.length()).equals(fin)) {
                    reader.close();
                    if(at.substring(fin.length() + x + 2, fin.length() + x + 3).equals(",")) { //ensures that it is the entire password and not just a part of it
                        return true;
                    }
                }
                else{
                    break;
                }

            }
            at = reader.readLine();
        }

        reader.close();
        return false;
    }

    public static int findPWLine(String password) throws Exception{
        String lc = password.toLowerCase();
        String fin = toHexString(getSHA(lc));
        BufferedReader reader = new BufferedReader(new FileReader("storedInfo2.txt"));
        String at = reader.readLine();
        int num = 0;

        while(at != null) {

            int a = at.indexOf(",");

            for (int x = at.indexOf(",", a+1); x < at.length()-fin.length() - 1; x++) {

                if (at.substring(x + 2, x + 2 + fin.length()).equals(fin)) {
                    reader.close();
                    return num;
                }
                else{
                    break;
                }

            }
            at = reader.readLine();
        }

        reader.close();
        return num;
    }


    public static byte[] getSHA(String input) throws NoSuchAlgorithmException
    {
        // Static getInstance method is called with hashing SHA
        MessageDigest md = MessageDigest.getInstance("SHA-256");

        // digest() method called
        // to calculate message digest of an input
        // and return array of byte
        return md.digest(input.getBytes(StandardCharsets.UTF_8));
    }

    public static String toHexString(byte[] hash) {
        // Convert byte array into signum representation
        BigInteger number = new BigInteger(1, hash);

        // Convert message digest into hex value
        StringBuilder hexString = new StringBuilder(number.toString(16));

        // Pad with leading zeros
        while (hexString.length() < 32) {
            hexString.insert(0, '0');
        }

        return hexString.toString();
    }

    public static void userMenu(){
        System.out.println("You get a high five!");
    }

    public static void adminMenu(){
        System.out.println("You just got bamboozled >:(");
    }
}