package com.bankos;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.Scanner;

public class Driver {
    public static void main(String[] args) {
        BankApplication app = new BankApplication();

        File file = new File("src/main/resources/input.txt");
        try (Scanner sc = new Scanner(file)) {

            while (sc.hasNextLine()) {
                String inputLine = sc.nextLine();
                String[] inputArray  = inputLine.split(" ");

                String switchString = inputArray[0];

                switch (switchString){
                    case "CREATE":
                        System.out.println(app.create(inputArray[1]));
                        break;

                    case "DEPOSIT":
                        try{
                            System.out.println(app.deposit(Integer.parseInt(inputArray[1]),new BigDecimal(inputArray[2])));
                        }catch (RuntimeException re){
                            System.out.println(re.getMessage());
                        }
                        break;

                    case "BALANCE":
                        try{
                            System.out.println(app.getBalance(Integer.parseInt(inputArray[1])));
                        }catch (RuntimeException e){
                            System.out.println(e.getMessage());
                        }
                        break;


                    case "WITHDRAW":
                        try{
                            System.out.println(app.withdraw(Integer.parseInt(inputArray[1]),new BigDecimal(inputArray[2])));
                        }catch (RuntimeException re){
                            System.out.println(re.getMessage());
                        }
                        break;

                    case "TRANSFER":
                        try{
                            System.out.println(app.transfer(Integer.parseInt(inputArray[1]),Integer.parseInt(inputArray[2]),new BigDecimal(inputArray[3])));
                        }catch (RuntimeException re){
                            System.out.println(re.getMessage());
                        }
                        break;

                    default :
                        System.out.println("Please try again with an valid option");
                        break;
                }

            }
        }catch (FileNotFoundException fileEx){
            System.out.println(fileEx.getMessage());
        }

    }
}
