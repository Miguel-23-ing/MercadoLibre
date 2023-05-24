package ui;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import exceptions.InvalidQuantityException;
import exceptions.ProductNotFoundException;
import model.LocalDateAdapter;
import model.MercadoLibreControler;
import model.Product;
import model.ProductType;

import java.io.*;
import java.time.LocalDate;
import java.util.*;

public class MercadoLibreManager {

    public MercadoLibreManager(){
        mlc = new MercadoLibreControler();
        load();
    }
    public static Scanner sc = new Scanner(System.in);

    public final Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .create();
    public static MercadoLibreControler mlc;
    static String folder = "data";
    static String path = "data/products.txt";
    public static void main(String[] args) {
        MercadoLibreManager mlm = new MercadoLibreManager();
        mlm.menu();
    }
    public void menu() {
        System.out.println("\n¡Welcome to the virtual store of MercadoLibre!");
        boolean exit = false;
        try {
        while (!exit) {
            System.out.println("""
                
                Enter the number according to the option you wish to choose:
                1. Register Product
                2. Register Order
                3. Search Product
                4. Search Order
                5. Exit
                Selection: \s""");
                int option = sc.nextInt();

                switch (option) {
                    case 1 -> registerProduct();
                    case 2 -> registerOrder();
                    case 3 -> searchProductMenu();
                    case 4 -> searchOrderMenu();
                    case 5 -> {
                        System.out.println("Thank you for using the MercadoLibre virtual store. Come back soon!");
                        exit = true;
                    }
                    default -> exit = true;
                }
            }
        }catch (InputMismatchException e) {
            System.out.println("Invalid input");
            sc.nextLine(); // Limpiar token inválido
            menu();
        }
    }

    public void save(){
        File file = new File(path);
        String data = gson.toJson(mlc);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fos));
            writer.write(data);
            writer.flush();
            fos.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    public void load(){
        File file = new File(path);
        try {
            if (file.exists()) {
                FileInputStream fis = new FileInputStream(file);
                BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
                String content = "";
                String line = "";
                while ((line = reader.readLine()) != null) {
                    content += line + "\n";
                }
                mlc = gson.fromJson(content, MercadoLibreControler.class);
                fis.close();
            }else{
                File f = new File(folder);
                if(!f.exists()){
                    f.mkdirs();
                }
                file.createNewFile();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void searchProductMenu(){
        sc.nextLine();
        try{
            boolean exit = false;

            while(!exit){

                System.out.println("""
                
                Enter the number according to the option you wish to choose:
                1. Search by Name
                2. Search by Price
                3. Search by Category
                4. Search by Number of Times Purchased
                5. Search by Quantity Available
                6. Search by Price in Range
                7. Search by Number of Times Purchased in Range
                8. Search by Quantity Available in Range
                9. Search Name by Prefix
                10. Search Name by Prefix Interval
                11. Exit
                Selection: \s""");
                int option = sc.nextInt();

                try{
                    switch (option) {
                        case 1:
                            searchProductByName();
                            break;
                        case 2:
                            searchProductByPrice();
                            break;
                        case 3:
                            searchProductByCategory();
                            break;
                        case 4:
                            searchProductByPurchaseNumber();
                            break;
                        case 5:
                            searchByQuantityAvailable();
                        case 6:
                            searchByPriceRange();
                            break;
                        case 7:
                            searchByPurchaseNumberRange();
                            break;
                        case 8:
                            searchByQuantityAvailableRange();
                            break;
                        case 9:
                            searchNameByPrefix();
                            break;
                        case 10:
                            searchNameByPrefixRange();
                            break;
                        case 11:
                            exit = true;
                            break;
                        default:
                            exit = true;
                            break;
                    }

                } catch (ProductNotFoundException e){
                    System.out.println(e.getMessage());
                }
            }
        }catch (InputMismatchException e){
            System.out.println("Ingreso Invalido");
            menu();
        }

    }

    public void searchOrderMenu(){
        sc.nextLine();
        try{
            boolean exit = false;

            while(!exit){

                System.out.println("""
                
                Enter the number according to the option you wish to choose:
                1. Search by Buyer Name
                2. Search by Total Price
                3. Search by Date of Purchase
                4. Search by Total price in Range
                5. Search by Date of Purchase in Range
                6. Search Buyer Name by Prefix
                7. Search Buyer Name by Prefix Range
                8. Exit
                Selection: \s""");
                int option = sc.nextInt();

                try{
                    switch (option) {
                        case 1:
                            searchByBuyerName();
                            break;
                        case 2:
                            searchOrderByTotalPrice();
                            break;
                        case 3:
                            searchByPurchaseDate();
                            break;
                        case 4:
                            searchOrderByTotalPriceRange();
                            break;
                        case 5:
                            searchByPurchaseDateRange();
                        case 6:
                            searchBuyerNameByPrefix();
                            break;
                        case 7:
                            searchBuyerNameByPrefixRange();
                            break;
                        case 8:
                            exit = true;
                            break;
                        default:
                            exit = true;
                            break;
                    }

                } catch (ProductNotFoundException e){
                    System.out.println(e.getMessage());
                }

            }
        }catch (InputMismatchException e){
            System.out.println("Ingreso Invalido");
            menu();
        }

    }

    private void registerProduct() {
        sc.nextLine();
        System.out.println("""
        
        Please write the product you wish to register in the following format: Name::Description::Price::Quantity Available::Category);
        For the category, please type the corresponding number according to the following list: """);
        System.out.println(mlc.showProductType());
        String input = sc.nextLine();
        System.out.println("\n" + mlc.registerProduct(input));
        save();

    }
    private void registerOrder() {
        sc.nextLine();
        System.out.println("Please, write the name of the buyer");
        String bName = sc.nextLine();
        int option = 1;
        int counter = 0;
        mlc.registerOrder(bName);
        System.out.println("Order created successfully.");
        while (option == 1){
            System.out.println("Please, write te name of the product you wish to buy");
            String pName = sc.nextLine();
            System.out.println("Please write the price of the product you wish to buy");
            double price = sc.nextDouble();
            System.out.println("Please, write the quantity of the product you wish to buy");
            int quantity = sc.nextInt();
            Product product = null;
            try {
                product = mlc.addByNameAndPrice(pName, price, quantity);
                if(product==null){
                    throw new ProductNotFoundException("No se ha encontrado el producto");
                }
                mlc.updateOrder(bName, product, quantity);
                counter++;
            } catch (RuntimeException | ProductNotFoundException | InvalidQuantityException e) {
                System.out.println(e.getMessage());

            }
            System.out.println("Would you like to add another product to the order?\n Enter 1 if you wish to do so. \n Enter 2 if you wish to terminate the order.");
            option = sc.nextInt();
        }
        if(counter==0){
            mlc.deleteOrder(bName);
        }
        System.out.println("Order registered successfully. Thank you for using MercadoLibre.");
        save();
    }

    private void searchProductByName() throws ProductNotFoundException {
        sc.nextLine();
        System.out.println("Please, write the name of the product you wish to search for");
        String name = sc.nextLine();
        System.out.println("How do you want the products to be displayed?\nType 1 if you want it to be in ascending order.\nType 2 if you want it to be in descending order.");
        int op = sc.nextInt();
        if(mlc.searchByName(name, op) == null){
            throw new ProductNotFoundException("The product with the name " + name + " is not registered");
        }else {
            System.out.println("\nProducts:\n");
            System.out.println(mlc.searchByName(name,op).toString());
        }
    }

    private void searchProductByPrice() throws ProductNotFoundException {
        sc.nextLine();
        System.out.println("Please write the price of the product you wish to search for");
        double price = sc.nextDouble();
        System.out.println("How do you want the products to be displayed?\nType 1 if you want it to be in ascending order.\nType 2 if you want it to be in descending order.");
        int op = sc.nextInt();
        if(mlc.searchByPrice(price, op) == null){
            throw new ProductNotFoundException("The product with the price $" + price + " is not registered");
        }else {
            System.out.println("\nProducts:\n");
            System.out.println(mlc.searchByPrice(price, op).toString());
        }
    }

    private void searchProductByCategory() throws ProductNotFoundException {
        sc.nextLine();
        System.out.println("Please type the corresponding number according to the following list:");
        System.out.println(mlc.showProductType());
        int category = sc.nextInt();
        System.out.println("How do you want the products to be displayed?\nType 1 if you want it to be in alphabetical ascending order.\nType 2 if you want it to be in alphabetical descending order.");
        int op = sc.nextInt();
        if(mlc.searchByCategory(category, op) == null){
            throw new ProductNotFoundException("The product with the number of purchase " + ProductType.values()[category] + " is not registered");
        }else {
            System.out.println("\nProducts:\n");
            System.out.println(mlc.searchByCategory(category, op).toString());
        }
    }

    private void searchProductByPurchaseNumber() throws ProductNotFoundException {
        sc.nextLine();
        System.out.println("Please write the number of times purchased of the product you wish to search for");
        int purchaseN = sc.nextInt();
        System.out.println("How do you want the products to be displayed?\nType 1 if you want it to be in ascending order.\nType 2 if you want it to be in descending order.");
        int op = sc.nextInt();
        if(mlc.searchByPurchaseNumber(purchaseN, op) == null){
            throw new ProductNotFoundException("The product with the number of purchase " + purchaseN + " is not registered");
        }else {
            System.out.println("\nProducts:\n");
            System.out.println(mlc.searchByPurchaseNumber(purchaseN, op).toString());
        }
    }

    private void searchByPurchaseNumberRange() throws ProductNotFoundException {
        sc.nextLine();
        System.out.println("Please write the minimum number of times purchased of the product you wish to search for");
        int min = sc.nextInt();
        System.out.println("Please write the maximum number of times purchased of the product you wish to search for");
        int max = sc.nextInt();
        System.out.println("How do you want the products to be displayed?\nType 1 if you want it to be in ascending order.\nType 2 if you want it to be in descending order.");
        int op = sc.nextInt();
        if(mlc.searchByPurchaseNumberRange(min, max, op) == null){
            throw new ProductNotFoundException("The product with the number of times purchased between " + min + " And " + max +  " is not registered");
        }else {
            System.out.println("\nProducts:\n");
            System.out.println(mlc.searchByPurchaseNumberRange(min, max, op).toString());
        }

    }

    private void searchByQuantityAvailable() throws ProductNotFoundException {
        sc.nextLine();
        System.out.println("Please write the stock of the product you wish to search for");
        int quantity = sc.nextInt();
        System.out.println("How do you want the products to be displayed?\nType 1 if you want it to be in ascending order.\nType 2 if you want it to be in descending order.");
        int op = sc.nextInt();
        if(mlc.searchByQuantityAvailable(quantity, op) == null){
            throw new ProductNotFoundException("Products with quantity available " + quantity + " not found.");
        }else {
            System.out.println("\nProducts:\n");
            System.out.println(mlc.searchByQuantityAvailable(quantity, op).toString());
        }
    }

    private void searchByPriceRange() throws ProductNotFoundException {
        sc.nextLine();
        System.out.println("Please write the minimum price of the product you wish to search for");
        int min = sc.nextInt();
        System.out.println("Please write the maximum price of the product you wish to search for");
        int max = sc.nextInt();
        System.out.println("How do you want the products to be displayed?\nType 1 if you want it to be in ascending order.\nType 2 if you want it to be in descending order.");
        int op = sc.nextInt();
        if(mlc.searchByPriceRange(min, max, op) == null){
            throw new ProductNotFoundException("The product with the price between  $" + min + " And $" + max +  " is not registered");
        }else {
            System.out.println("\nProducts:\n");
            System.out.println(mlc.searchByPriceRange(min, max, op).toString());
        }
    }

    private void searchByQuantityAvailableRange() throws ProductNotFoundException {
        sc.nextLine();
        System.out.println("Please enter the minimum quantity of the product you wish to search for");
        int min = sc.nextInt();
        System.out.println("Please enter the minimum quantity of the product you wish to search for");
        int max = sc.nextInt();
        System.out.println("How do you want the products to be displayed?\nType 1 if you want it to be in ascending order.\nType 2 if you want it to be in descending order.");
        int op = sc.nextInt();
        if(mlc.searchByQuantityAvailableRange(min, max, op) == null){
            throw new ProductNotFoundException("The product with the quantity available between " + min + " And " + max +  " is not registered");
        }else {
            System.out.println("\nProducts:\n");
            System.out.println(mlc.searchByQuantityAvailableRange(min, max, op).toString());
        }
    }

    private void searchByBuyerName() throws ProductNotFoundException {
        sc.nextLine();
        System.out.println("Please, write the buyer name of the order you wish to search for");
        String name = sc.nextLine();
        System.out.println("How do you want the orders to be displayed?\nType 1 if you want it to be in alphabetical ascending order.\nType 2 if you want it to be in alphabetical descending order.");
        int op = sc.nextInt();
        if(mlc.searchByBuyerName(name, op) == null){
            throw new ProductNotFoundException("The order with the buyer name " + name + " is not registered");
        }else {
            System.out.println("\nOrders:\n");
            System.out.println(mlc.searchByBuyerName(name, op).toString());
        }
    }

    private void searchBuyerNameByPrefix() throws ProductNotFoundException {
        sc.nextLine();
        System.out.println("Please, write the prexix of the buyer name of the order you wish to search for");
        String name = sc.nextLine();
        System.out.println("How do you want the orders to be displayed?\nType 1 if you want it to be in alphabetical ascending order.\nType 2 if you want it to be in alphabetical descending order.");
        int op = sc.nextInt();
        if(mlc.searchBuyerNameByPrefix(name, op) == null){
            throw new ProductNotFoundException("The order with the prefix of the buyer name " + name + " is not registered");
        }else {
            System.out.println("\nOrders:\n");
            System.out.println(mlc.searchBuyerNameByPrefix(name, op).toString());
        }
    }

    private void searchBuyerNameByPrefixRange() throws ProductNotFoundException {
        sc.nextLine();
        System.out.println("Please write the initial prefix of the buyer name of the order you wish to search for");
        String initialPrefix = sc.nextLine();
        System.out.println("Please write the final prefix of the buyer name of the order you wish to search for");
        String finalPrefix = sc.nextLine();
        System.out.println("How do you want the orders to be displayed?\nType 1 if you want it to be in alphabetical ascending order.\nType 2 if you want it to be in alphabetical descending order.");
        int op = sc.nextInt();
        if(mlc.searchByBuyerNamePrefixRange(initialPrefix, finalPrefix, op) == null){
            throw new ProductNotFoundException("The order with the prefix of the buyer name between " + initialPrefix + " And " + finalPrefix +  " is not registered");
        }else {
            System.out.println("\nOrder:\n");
            System.out.println(mlc.searchByBuyerNamePrefixRange(initialPrefix, finalPrefix, op).toString());
        }
    }

    private void searchOrderByTotalPrice() throws ProductNotFoundException {
        sc.nextLine();
        System.out.println("Please write the total price of the order you wish to search for");
        double price = sc.nextDouble();
        System.out.println("How do you want the orders to be displayed?\nType 1 if you want it to be in ascending order.\nType 2 if you want it to be in descending order.");
        int op = sc.nextInt();
        if(mlc.searchByTotalPrice(price, op) == null){
            throw new ProductNotFoundException("The order with the total price $" + price + " is not registered");
        }else {
            System.out.println("\nOrders:\n");
            System.out.println(mlc.searchByTotalPrice(price, op).toString());
        }
    }

    private void searchOrderByTotalPriceRange() throws ProductNotFoundException {
        sc.nextLine();
        System.out.println("Please write the minimum price of the order you wish to search for");
        int min = sc.nextInt();
        System.out.println("Please write the maximum price of the order you wish to search for");
        int max = sc.nextInt();
        System.out.println("How do you want the orders to be displayed?\nType 1 if you want it to be in ascending order.\nType 2 if you want it to be in descending order.");
        int op = sc.nextInt();
        if(mlc.searchByTotalPriceRange(min, max, op) == null){
            throw new ProductNotFoundException("The order with the price between  $" + min + " And $" + max +  " is not registered");
        }else {
            System.out.println("\nOrder:\n");
            System.out.println(mlc.searchByTotalPriceRange(min, max, op).toString());
        }
    }

    private void searchNameByPrefix() throws ProductNotFoundException {
        sc.nextLine();
        System.out.println("Please, write the prefix of the name of the product you wish to search for");
        String name = sc.nextLine();
        System.out.println("How do you want the products to be displayed?\nType 1 if you want it to be in alphabetical ascending order.\nType 2 if you want it to be in alphabetical descending order.");
        int op = sc.nextInt();
        if(mlc.searchByPrefix(name, op) == null){
            throw new ProductNotFoundException("The product with the prefix of the name " + name + " is not registered");
        }else {
            System.out.println("\nProducts:\n");
            System.out.println(mlc.searchByPrefix(name, op).toString());
        }
    }

    private void searchNameByPrefixRange() throws ProductNotFoundException {
        sc.nextLine();
        System.out.println("Please write the initial prefix of the name of the product you wish to search for");
        String initialPrefix = sc.nextLine();
        System.out.println("Please write the final prefix of the name of the product you wish to search for");
        String finalPrefix = sc.nextLine();
        System.out.println("How do you want the products to be displayed?\nType 1 if you want it to be in alphabetical ascending order.\nType 2 if you want it to be in alphabetical descending order.");
        int op = sc.nextInt();
        if(mlc.searchByPrefixRange(initialPrefix, finalPrefix, op) == null){
            throw new ProductNotFoundException("The product with the prefix of the name between " + initialPrefix + " And " + finalPrefix +  " is not registered");
        }else {
            System.out.println("\nOrder:\n");
            System.out.println(mlc.searchByPrefixRange(initialPrefix, finalPrefix, op).toString());
        }
    }

    private void searchByPurchaseDate() throws ProductNotFoundException {
        sc.nextLine();
        System.out.println("Please write the purchase date of the order you wish to search for in the following format: \nAño-Mes-Día");
        String temp = sc.nextLine();
        String[] data = temp.split("-");
        System.out.println("How do you want the orders to be displayed?\nType 1 if you want it to be in ascending order.\nType 2 if you want it to be in descending order.");
        int op = sc.nextInt();
        if(mlc.searchByPurchaseDate(Integer.parseInt(data[0]), Integer.parseInt(data[1]), Integer.parseInt(data[2]), op) == null){
            throw new ProductNotFoundException("The order with the purchase date " + LocalDate.of(Integer.parseInt(data[0]), Integer.parseInt(data[1]), Integer.parseInt(data[2])) + " is not registered");
        }else {
            System.out.println("\nOrders:\n");
            System.out.println(mlc.searchByPurchaseDate(Integer.parseInt(data[0]), Integer.parseInt(data[1]), Integer.parseInt(data[2]), op).toString());
        }
    }

    private void searchByPurchaseDateRange() throws ProductNotFoundException {
        sc.nextLine();
        System.out.println("Please write the minimum purchase date of the order you wish to search for in the following format: \nAño-Mes-Día");
        String temp = sc.nextLine();
        System.out.println("Please write the maximum purchase date of the order you wish to search for in the following format: \nAño-Mes-Día");
        String temp1 = sc.nextLine();
        String[] data = temp.split("-");
        String[] data2 = temp1.split("-");
        System.out.println("How do you want the orders to be displayed?\nType 1 if you want it to be in ascending order.\nType 2 if you want it to be in descending order.");
        int op = sc.nextInt();
        if(mlc.searchByDateRange(Integer.parseInt(data[0]), Integer.parseInt(data[1]), Integer.parseInt(data[2]), Integer.parseInt(data2[0]), Integer.parseInt(data2[1]), Integer.parseInt(data2[2]), op) == null){
            throw new ProductNotFoundException("There are no orders registered within the range " + LocalDate.of(Integer.parseInt(data[0]), Integer.parseInt(data[1]), Integer.parseInt(data[2])) + " / " + LocalDate.of(Integer.parseInt(data2[0]), Integer.parseInt(data2[1]), Integer.parseInt(data2[2])));
        }else {
            System.out.println("\nOrders:\n");
            System.out.println(mlc.searchByDateRange(Integer.parseInt(data[0]), Integer.parseInt(data[1]), Integer.parseInt(data[2]), Integer.parseInt(data2[0]), Integer.parseInt(data2[1]), Integer.parseInt(data2[2]), op).toString());
        }
    }


}