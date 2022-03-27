package com.company;
import java.util.*;
import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    private static final String DATA_PATH = "src/com.company/contacts";

    private static void saveContacts(Map<String, List<String>> contacts) {
        try (PrintWriter writer = new PrintWriter(DATA_PATH)) {
            if (!contacts.isEmpty()) {
                for (Map.Entry<String, List<String>> entry : contacts.entrySet()) {
                    String line = String.format("%s,\"%s\"",
                            entry.getKey(), entry.getValue().toString().replaceAll("[\\[\\]]", ""));
                    writer.println(line);
                }
            }

        } catch (IOException ibex) {
            System.err.println(ibex.getMessage());
        }
    }


    private static void loadContacts(Map<String, List<String>> contacts) {
        try (BufferedReader reader = new BufferedReader(new FileReader(DATA_PATH))) {

            Pattern pattern = Pattern.compile("^([^,\"]{2,50}),\"([0-9+, ]+)\"$");

            while (true) {
                String line = reader.readLine();
                if (line == null) {
                    break;
                }

                Matcher matcher = pattern.matcher(line);
                if (matcher.find()) {
                    String[] numbers = matcher.group(2).split(",\\s*");
                    contacts.put(matcher.group(1), Arrays.asList(numbers));
                }
            }

        } catch (IOException ibex) {
            System.err.println("Could not load contacts, phone book is empty!");
        }
    }


    private static void addRecord(Scanner input)
    {
        System.out.println("You are about to add a new contact to the phone book.");
        String name;
        String number;

        while (true)
        {
            System.out.println("Enter contact name:");
            name = input.nextLine().trim();
            if (name.matches("^.{2,50}$")) {
                break;
            } else {
                System.out.println("Name must be in range 2 - 50 symbols.");
            }
            while (true) {
                System.out.println("Enter contact number:");
                number = input.nextLine().trim();
                if (number.matches("^\\+?[0-9 ]{3,25}$")) {
                    break;
                } else {
                    System.out.println("Number may contain only '+', spaces and digits. Min length 3, max length 25.");
                }
            }

     }

    }

        private static void searchSub(Map<String, List<String>> contacts, Scanner input) {
            System.out.println("Enter the name you are looking for:");
            String name = input.nextLine().trim();

            if (contacts.containsKey(name)) {
                System.out.println(name);
                for (String number : contacts.get(name)) {
                    System.out.println(number);
                }
            } else {
                System.out.println("Sorry, nothing found!");
            }
        }

        private static void searchPhone(Map<String, List<String>> contacts, Scanner input) {
            System.out.println("Enter a number to see to whom does it belong:");
            String number = input.nextLine().trim();

            while (!number.matches("^\\+?[0-9 ]{3,25}$")) {
                System.out.println("Invalid number! May contain only digits, spaces and '+'. Min length 3, max length 25.");
                System.out.println("Enter number:");
                number = input.nextLine().trim();
            }

            for (Map.Entry<String, List<String>> entry : contacts.entrySet()) {
                if (entry.getValue().contains(number)) {
                    System.out.println(entry.getKey());
                    System.out.println(number);
                }
            }
        }

        private static void showRecords(Map<String, List<String>> contacts) {
            if (!contacts.isEmpty()) {
                for (Map.Entry<String, List<String>> entry : contacts.entrySet()) {
                    System.out.println(entry.getKey());
                    for (String number : entry.getValue()) {
                        System.out.println(number);
                    }
                    System.out.println();
                }
            } else {
                System.out.println("No records found, the phone book is empty!");
            }
        }

        private static void deleteRecord(Map<String, List<String>> contacts, Scanner input) {
            System.out.println("Enter name of the contact to be deleted:");
            String name = input.nextLine().trim();

            if (contacts.containsKey(name)) {
                System.out.printf("Contact '%s' will be deleted. Are you sure? [Y/N]:\n", name);
                String confirmation = input.nextLine().trim().toLowerCase();
                confirm:
                while (true) {
                    switch (confirmation) {
                        case "y":
                            contacts.remove(name);
                            saveContacts(contacts);
                            System.out.println("Contact was deleted successfully!");
                            break confirm;
                        case "n":
                            break confirm;
                        default:
                            System.out.println("Delete contact? [Y/N]:");
                            break;
                    }
                    confirmation = input.nextLine().trim().toLowerCase();
                }

            } else {
                System.out.println("Sorry, name not found!");
            }
        }



    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        String menuOption;

        Map<String, List<String>> contacts = new TreeMap<>();
        loadContacts(contacts);

        do {

            System.out.println("1, Add a new record");
            System.out.println("2, Search by subscriber");
            System.out.println("3, Search by phone number");
            System.out.println("4, Show records");
            System.out.println("5, Delete record");
            System.out.println("6, Exit");
            menuOption = input.nextLine();

            switch (menuOption) {
                case "1":
                    addRecord(input);
                    break;
                case "2":
                    searchSub(contacts, input);
                    break;
                case "3":
                    searchPhone(contacts, input);
                    break;
                case "4":
                    showRecords(contacts);
                    break;
                case "5":
                    deleteRecord(contacts, input);
                    break;

            }


        } while (!menuOption.equals("6"));
    }

    }

