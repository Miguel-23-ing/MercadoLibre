package model;

import exceptions.InvalidQuantityException;
import java.time.LocalDate;
import java.util.*;

public class MercadoLibreControler {

    private int lastOrder = 1;

    private ArrayList<Product> products;
    private HashMap<String, Order> orders;

    public MercadoLibreControler() {
        this.products = new ArrayList<Product>();
        this.orders = new HashMap<>();
    }

    public MercadoLibreControler(int test) {
        this.products = new ArrayList<Product>();
        this.orders = new HashMap<>();
    }


    public String showProductType() {
        String typeList = "";
        ProductType[] types = ProductType.values();
        for (int i = 0; i < types.length; i++) {
            typeList += (i+1) + ". " + types[i] + "\n";
        }
        return typeList;
    }
    public String registerProduct(String input) {
        String[] data = input.split("::");
        double price = Double.parseDouble(data[2]);
        int stock = Integer.parseInt(data[3]);
        int category = Integer.parseInt(data[4]) - 1;
        if(checkByNameAndPrice(data[0], price, stock)){
            return "There was already a product with the same name and price, so we increased its stock quantity according to the number entered. Thank you for using MercadoLibre.";
        } else {
            products.add(
                    new Product(data[0].trim(), data[1], price, stock, category)
            );
        } return "Product registered successfully. Thank you for using MercadoLibre.";
    }

    public void registerOrder(String bName){
        orders.put(bName, new Order(bName));
        lastOrder++;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }

    public HashMap<String, Order> getOrders() {
        return orders;
    }

    public void setOrders(HashMap<String, Order> orders) {
        this.orders = orders;
    }

    public boolean checkByNameAndPrice( String name, double price, int stock) {
        Collections.sort(products, Comparator.comparing(Product::getName));

        int begin = 0;
        int end = products.size() - 1;

        while (begin <= end) {
            int mid = (begin + end) / 2;
            Product midProduct = products.get(mid);

            if (midProduct.getName().equalsIgnoreCase(name) && midProduct.getPrice() == price) {
                products.get(mid).setQuantityAvailable(products.get(mid).getQuantityAvailable() + stock);
                return true;

            } else if (midProduct.getName().compareTo(name) < 0 || (midProduct.getName().equals(name))) {
                begin = mid + 1;
            } else {
                end = mid - 1;
            }
        }
        return false;
    }
    public Product addByNameAndPrice(String name, double price, int quantity) throws InvalidQuantityException {
        Collections.sort(products, Comparator.comparing(Product::getName)) ;

        int begin = 0;
        int end = products.size() - 1;
        while (begin <= end) {
            int mid = (begin + end) / 2;
            Product midProduct = products.get(mid);

            if (midProduct.getName().equalsIgnoreCase(name) && midProduct.getPrice() == price) {
                if(products.get(mid).getQuantityAvailable() < quantity){
                    throw new InvalidQuantityException("Not enough items to add");
                }
                return midProduct;
            } else if (midProduct.getName().compareTo(name) < 0 || (midProduct.getName().equals(name))) {
                begin = mid + 1;
            } else {
                end = mid - 1;
            }
        }
        return null;
    }

    public void deleteOrder(String name){
        orders.remove(name);
    }

    public void updateOrder(String name, Product product, int quantity){
        ArrayList<Item> arr = orders.get(name).getProductList();
        arr.add(new Item(quantity, product));
        orders.get(name).setProductList(arr);
        orders.get(name).calculateTotalPrice();
        product.setPurchaseNumber(product.getPurchaseNumber()+quantity);
        product.setQuantityAvailable(product.getQuantityAvailable()-quantity);
        if(product.getQuantityAvailable()==0){
            deleteProduct(product);
        }
    }

    public void deleteProduct(Product product){
        products.remove(product);
    }

    public ArrayList<Product> searchByName(String name, int op){
        Collections.sort(products, Comparator.comparing(Product::getName));
        int begin = 0;
        int end = products.size() - 1;
        ArrayList<Product> productsByName = new ArrayList<>();

        while (begin <= end) {
            int mid = (begin + end) / 2;
            Product midProduct = products.get(mid);
            if (midProduct.getName().trim().equalsIgnoreCase(name.trim())) {
                productsByName.add(midProduct);
                int left = mid - 1;
                while (left >= 0 && products.get(left).getName().trim().equalsIgnoreCase(name.trim())) {
                    productsByName.add(products.get(left));
                    left--;
                }
                int right = mid + 1;
                while (right < products.size() && products.get(right).getName().equalsIgnoreCase(name.trim())) {
                    productsByName.add(products.get(right));
                    right++;
                }
                if(op == 1){
                    Collections.sort(productsByName, Comparator.comparing(Product::getName));
                }else{
                    Collections.reverse(productsByName);
                }
                return productsByName;
            } else if (midProduct.getName().compareToIgnoreCase(name) <= 0) {
                begin = mid + 1;
            } else {
                end = mid - 1;
            }
        }
        return null;
    }

    public ArrayList<Product> searchByPrefix(String prefix, int op) {
        Collections.sort(products, Comparator.comparing(Product::getName));
        int begin = 0;
        int end = products.size() - 1;
        ArrayList<Product> productsByPrefix = new ArrayList<>();

        while (begin <= end) {
            int mid = (begin + end) / 2;
            Product midProduct = products.get(mid);
            String midName = midProduct.getName().trim();
            if (midName.startsWith(prefix)) {
                productsByPrefix.add(midProduct);
                int left = mid - 1;
                while (left >= 0 && products.get(left).getName().startsWith(prefix)) {
                    productsByPrefix.add(products.get(left));
                    left--;
                }
                int right = mid + 1;
                while (right < products.size() && products.get(right).getName().startsWith(prefix)) {
                    productsByPrefix.add(products.get(right));
                    right++;
                }
                if(op == 1){
                    Collections.sort(productsByPrefix, Comparator.comparing(Product::getName));
                }else{
                    Collections.reverse(productsByPrefix);
                }
                return productsByPrefix;
            } else if (midName.compareToIgnoreCase(prefix) < 0) {
                begin = mid + 1;
            } else {
                end = mid - 1;
            }
        }
        return productsByPrefix;
    }

    public ArrayList<Product> searchByPrefixRange(String prefixInitial, String prefixFinal, int op) {
        Collections.sort(products, Comparator.comparing(Product::getName));
        int begin = 0;
        int end = products.size() - 1;
        ArrayList<Product> productsByPrefix = new ArrayList<>();

        while (begin <= end) {
            int mid = (begin + end) / 2;
            Product midProduct = products.get(mid);
            String midName = midProduct.getName().trim();
            if (midName.compareToIgnoreCase(prefixInitial)>=0 && midName.compareToIgnoreCase(prefixFinal) <= 0) {
                productsByPrefix.add(midProduct);
                int left = mid - 1;
                while (left >= 0 && products.get(left).getName().startsWith(prefixInitial)) {
                    Product leftProduct = products.get(left);
                    String leftName = leftProduct.getName().trim();
                    if (leftName.compareToIgnoreCase(prefixFinal) <= 0) {
                        productsByPrefix.add(leftProduct);
                    } else {
                        break;
                    }
                    left--;
                }
                int right = mid + 1;
                while (right < products.size() && products.get(right).getName().startsWith(prefixInitial)) {
                    Product rightProduct = products.get(right);
                    String rightName = rightProduct.getName().trim();
                    if (rightName.compareToIgnoreCase(prefixFinal) <= 0) {
                        productsByPrefix.add(rightProduct);
                    } else {
                        break;
                    }
                    right++;
                }
                if(op == 1){
                    Collections.sort(productsByPrefix, Comparator.comparing(Product::getName));
                }else{
                    Collections.reverse(productsByPrefix);
                }
                return productsByPrefix;
            } else if (midName.compareToIgnoreCase(prefixInitial) < 0) {
                begin = mid + 1;
            } else {
                end = mid - 1;
            }
        }
        return null;
    }

    public ArrayList<Product> searchByPrice(double price, int op){
        Collections.sort(products, Comparator.comparing(Product::getPrice));
        ArrayList<Product> productsByPrice = new ArrayList<>();
        int begin = 0;
        int end = products.size() - 1;

        while (begin <= end) {
            int mid = (begin + end) / 2;
            Product midProduct = products.get(mid);
            if (midProduct.getPrice() == price) {
                productsByPrice.add(midProduct);
                int left = mid - 1;
                while (left >= 0 && products.get(left).getPrice() == price) {
                    productsByPrice.add(products.get(left));
                    left--;
                }

                int right = mid + 1;
                while (right < products.size() && products.get(right).getPrice() == price) {
                    productsByPrice.add(products.get(right));
                    right++;
                }
                if(op == 1){
                    Collections.sort(productsByPrice, Comparator.comparing(Product::getPrice));
                }else{
                    Collections.reverse(productsByPrice);
                }
                return productsByPrice;
            } else if (midProduct.getPrice() < price) {
                begin = mid + 1;
            } else {
                end = mid - 1;
            }
        }
        return null;
    }

    public ArrayList<Product> searchByCategory(int category, int op){
        Collections.sort(products, Comparator.comparing(Product::getCategory));
        int begin = 0;
        int end = products.size() - 1;

        ProductType productCategory = ProductType.values()[category-1];

        while (begin <= end) {
            int mid = (begin + end) / 2;
            Product midProduct = products.get(mid);
            if (midProduct.getCategory() == productCategory) {
                ArrayList<Product> productList = new ArrayList<>();
                productList.add(midProduct);

                int left = mid - 1;
                while (left >= 0 && products.get(left).getCategory() == productCategory) {
                    productList.add(products.get(left));
                    left--;
                }

                int right = mid + 1;
                while (right < products.size() && products.get(right).getCategory() == productCategory) {
                    productList.add(products.get(right));
                    right++;
                }
                if(op == 1){
                    Collections.sort(productList, Comparator.comparing(Product::getName));
                }else{
                    Collections.reverse(productList);
                }
                return productList;
            } else if (midProduct.getCategory().compareTo(productCategory) <= 0) {
                begin = mid + 1;
            } else {
                end = mid - 1;
            }
        }
        return null;
    }

    public ArrayList<Product> searchByPriceRange(double minPrice, double maxPrice, int op){
        Collections.sort(products, Comparator.comparing(Product::getPrice));
        int begin = 0;
        int end = products.size() - 1;
        ArrayList<Product> productsInRange = new ArrayList<>();
        while (begin <= end) {
            int mid = (begin + end) / 2;
            Product midProduct = products.get(mid);
            double midProductPrice = midProduct.getPrice();
            if (midProductPrice >= minPrice && midProductPrice <= maxPrice) {
                productsInRange.add(midProduct);
                int left = mid - 1;
                while (left >= 0 && products.get(left).getPrice() >= minPrice && products.get(left).getPrice() <= maxPrice) {
                    productsInRange.add(products.get(left));
                    left--;
                }
                int right = mid + 1;
                while (right < products.size() && products.get(right).getPrice() >= minPrice && products.get(right).getPrice() <= maxPrice) {
                    productsInRange.add(products.get(right));
                    right++;
                }
                if(op == 1){
                    Collections.sort(productsInRange, Comparator.comparing(Product::getPrice));
                }else{
                    Collections.reverse(productsInRange);
                }
                return productsInRange;
            }
            if (midProductPrice < minPrice) {
                begin = mid + 1;
            } else {
                end = mid - 1;
            }
        }
        return null;
    }

    public ArrayList<Product> searchByQuantityAvailable(int quantity, int op){
        Collections.sort(products, Comparator.comparing(Product::getQuantityAvailable));
        ArrayList<Product> productsByQuantity = new ArrayList<>();
        int begin = 0;
        int end = products.size() - 1;

        while (begin <= end) {
            int mid = (begin + end) / 2;
            Product midProduct = products.get(mid);
            if (midProduct.getQuantityAvailable() == quantity) {
                productsByQuantity.add(midProduct);
                int left = mid - 1;
                while (left >= 0 && products.get(left).getQuantityAvailable() == quantity) {
                    productsByQuantity.add(products.get(left));
                    left--;
                }

                int right = mid + 1;
                while (right < products.size() && products.get(right).getQuantityAvailable() == quantity) {
                    productsByQuantity.add(products.get(right));
                    right++;
                }
                if(op == 1){
                    Collections.sort(productsByQuantity, Comparator.comparing(Product::getQuantityAvailable));
                }else{
                    Collections.reverse(productsByQuantity);
                }
                return productsByQuantity;
            } else if (midProduct.getQuantityAvailable() < quantity) {
                begin = mid + 1;
            } else {
                end = mid - 1;
            }
        }
        return null;
    }

    public ArrayList<Product> searchByQuantityAvailableRange(int minQuantity, int maxQuantity, int op) {
        Collections.sort(products, Comparator.comparing(Product::getQuantityAvailable));
        ArrayList<Product> productsByQuantity = new ArrayList<>();
        int begin = 0;
        int end = products.size() - 1;

        while (begin <= end) {
            int mid = (begin + end) / 2;
            Product midProduct = products.get(mid);
            int midQuantity = midProduct.getQuantityAvailable();

            if (midQuantity >= minQuantity && midQuantity <= maxQuantity) {
                productsByQuantity.add(midProduct);

                int left = mid - 1;
                while (left >= 0) {
                    if (products.get(left).getQuantityAvailable() < minQuantity) {
                        break;
                    }
                    productsByQuantity.add(products.get(left));
                    left--;
                }

                int right = mid + 1;
                while (right < products.size()) {
                    if (products.get(right).getQuantityAvailable() > maxQuantity) {
                        break;
                    }
                    productsByQuantity.add(products.get(right));
                    right++;
                }
                if(op == 1){
                    Collections.sort(productsByQuantity, Comparator.comparing(Product::getQuantityAvailable));
                }else{
                    Collections.reverse(productsByQuantity);
                }
                return productsByQuantity;
            } else if (midQuantity < minQuantity) {
                begin = mid + 1;
            } else {
                end = mid - 1;
            }
        }
        return null;
    }

    public ArrayList<Product> searchByPurchaseNumber(int purchaseNumber, int op){
        Collections.sort(products, Comparator.comparing(Product::getPurchaseNumber));
        int begin = 0;
        int end = products.size() - 1;
        ArrayList<Product> productsByPurchaseNumber = new ArrayList<>();

        while (begin <= end) {
            int mid = (begin + end) / 2;
            Product midProduct = products.get(mid);
            if (midProduct.getPurchaseNumber() == purchaseNumber) {
                productsByPurchaseNumber.add(midProduct);

                int left = mid - 1;
                while (left >= 0 && products.get(left).getPurchaseNumber() == purchaseNumber) {
                    productsByPurchaseNumber.add(products.get(left));
                    left--;
                }

                int right = mid + 1;
                while (right < products.size() && products.get(right).getPurchaseNumber() == purchaseNumber) {
                    productsByPurchaseNumber.add(products.get(right));
                    right++;
                }
                if(op == 1){
                    Collections.sort(productsByPurchaseNumber, Comparator.comparing(Product::getPurchaseNumber));
                }else{
                    Collections.reverse(productsByPurchaseNumber);
                }
                return productsByPurchaseNumber;
            } else if (midProduct.getPurchaseNumber() < purchaseNumber) {
                begin = mid + 1;
            } else {
                end = mid - 1;
            }
        }
        return null;
    }

    public ArrayList<Product> searchByPurchaseNumberRange(int min, int max, int op) {
        Collections.sort(products, Comparator.comparing(Product::getPurchaseNumber));
        int begin = 0;
        int end = products.size() - 1;
        ArrayList<Product> productsInRange = new ArrayList<>();

        while (begin <= end) {
            int mid = (begin + end) / 2;
            Product midProduct = products.get(mid);
            int midPurchaseNumber = midProduct.getPurchaseNumber();

            if (midPurchaseNumber >= min && midPurchaseNumber <= max) {
                productsInRange.add(midProduct);

                int left = mid - 1;
                while (left >= 0 && products.get(left).getPurchaseNumber() >= min && products.get(left).getPurchaseNumber() <= max) {
                    productsInRange.add(products.get(left));
                    left--;
                }

                int right = mid + 1;
                while (right < products.size() && products.get(right).getPurchaseNumber() >= min && products.get(right).getPurchaseNumber() <= max) {
                    productsInRange.add(products.get(right));
                    right++;
                }
                if(op == 1){
                    Collections.sort(productsInRange, Comparator.comparing(Product::getPurchaseNumber));
                }else{
                    Collections.reverse(productsInRange);
                }
                return productsInRange;
            }

            if (midPurchaseNumber < min) {
                begin = mid + 1;
            } else {
                end = mid - 1;
            }
        }

        return null;
    }

    public ArrayList<Order> searchByTotalPrice(double price, int op) {
        List<Order> orderList = new ArrayList<>(orders.values());
        Collections.sort(orderList, Comparator.comparing(Order::calculateTotalPrice));

        ArrayList<Order> ordersByTotalPrice = new ArrayList<>();
        int begin = 0;
        int end = orders.size() - 1;

        while (begin <= end) {
            int mid = (begin + end) / 2;
            Order midOrder = orderList.get(mid);
            if (midOrder.getTotalPrice() == price) {
                ordersByTotalPrice.add(midOrder);
                int left = mid - 1;
                while (left >= 0 && orderList.get(left).getTotalPrice() == price) {
                    ordersByTotalPrice.add(orderList.get(left));
                    left--;
                }

                int right = mid + 1;
                while (right < orders.size() && orderList.get(right).getTotalPrice() == price) {
                    ordersByTotalPrice.add(orderList.get(right));
                    right++;
                }
                if(op == 1){
                    Collections.sort(ordersByTotalPrice, Comparator.comparing(Order::getTotalPrice));
                }else{
                    Collections.reverse(ordersByTotalPrice);
                }
                return ordersByTotalPrice;
            } else if (midOrder.getTotalPrice() < price) {
                begin = mid + 1;
            } else {
                end = mid - 1;
            }
        }

        return null;
    }

    public ArrayList<Order> searchByPurchaseDate(int year, int month, int day, int op) {
        List<Order> orderList = new ArrayList<>(orders.values());
        Collections.sort(orderList, Comparator.comparing(Order::getPurchaseDate));

        ArrayList<Order> ordersByDate = new ArrayList<>();
        int begin = 0;
        int end = orderList.size() - 1;

        while (begin <= end) {
            int mid = (begin + end) / 2;
            Order midOrder = orderList.get(mid);
            LocalDate midDate = midOrder.getPurchaseDate();
            if (midDate.getYear() == year && midDate.getMonthValue() == month && midDate.getDayOfMonth() == day) {
                ordersByDate.add(midOrder);
                int left = mid - 1;
                while (left >= 0 && orderList.get(left).getPurchaseDate().equals(midDate)) {
                    ordersByDate.add(orderList.get(left));
                    left--;
                }
                int right = mid + 1;
                while (right < orderList.size() && orderList.get(right).getPurchaseDate().equals(midDate)) {
                    ordersByDate.add(orderList.get(right));
                    right++;
                }
                if(op == 1){
                    Collections.sort(ordersByDate, Comparator.comparing(Order::getPurchaseDate));
                }else{
                    Collections.reverse(ordersByDate);
                }
                return ordersByDate;
            } else if (midDate.isBefore(LocalDate.of(year, month, day))) {
                begin = mid + 1;
            } else {
                end = mid - 1;
            }
        }

        return null;
    }

    public ArrayList<Order> searchByBuyerName(String buyerName, int op) {
        List<Order> orderList = new ArrayList<>(orders.values());
        Collections.sort(orderList, Comparator.comparing(Order::getBuyerName));

        ArrayList<Order> ordersByBuyerName = new ArrayList<>();
        int begin = 0;
        int end = orderList.size() - 1;

        while (begin <= end) {
            int mid = (begin + end) / 2;
            Order midOrder = orderList.get(mid);
            if (midOrder.getBuyerName().equals(buyerName)) {
                ordersByBuyerName.add(midOrder);
                int left = mid - 1;
                while (left >= 0 && orderList.get(left).getBuyerName().equals(buyerName)) {
                    ordersByBuyerName.add(orderList.get(left));
                    left--;
                }

                int right = mid + 1;
                while (right < orderList.size() && orderList.get(right).getBuyerName().equals(buyerName)) {
                    ordersByBuyerName.add(orderList.get(right));
                    right++;
                }
                if(op == 1){
                    Collections.sort(ordersByBuyerName, Comparator.comparing(Order::getBuyerName));
                }else{
                    Collections.reverse(ordersByBuyerName);
                }
                return ordersByBuyerName;
            } else if (midOrder.getBuyerName().compareTo(buyerName) < 0) {
                begin = mid + 1;
            } else {
                end = mid - 1;
            }
        }

        return null;
    }

    public ArrayList<Order> searchBuyerNameByPrefix(String prefix, int op) {
        List<Order> orderList = new ArrayList<>(orders.values());
        Collections.sort(orderList, Comparator.comparing(Order::getBuyerName));
        int begin = 0;
        int end = orderList.size() - 1;
        ArrayList<Order> ordersByPrefix = new ArrayList<>();

        while (begin <= end) {
            int mid = (begin + end) / 2;
            Order midOrder = orderList.get(mid);
            String midName = midOrder.getBuyerName().trim();
            if (midName.startsWith(prefix)) {
                ordersByPrefix.add(midOrder);
                int left = mid - 1;
                while (left >= 0 && orderList.get(left).getBuyerName().startsWith(prefix)) {
                    ordersByPrefix.add(orderList.get(left));
                    left--;
                }
                int right = mid + 1;
                while (right < orderList.size() && orderList.get(right).getBuyerName().startsWith(prefix)) {
                    ordersByPrefix.add(orderList.get(right));
                    right++;
                }
                if(op == 1){
                    Collections.sort(ordersByPrefix, Comparator.comparing(Order::getBuyerName));
                }else{
                    Collections.reverse(ordersByPrefix);
                }
                return ordersByPrefix;
            } else if (midName.compareToIgnoreCase(prefix) < 0) {
                begin = mid + 1;
            } else {
                end = mid - 1;
            }
        }
        return null;
    }

    public ArrayList<Order> searchByBuyerNamePrefixRange(String prefixInitial, String prefixFinal, int op) {
        List<Order> orderList = new ArrayList<>(orders.values());
        Collections.sort(orderList, Comparator.comparing(Order::getBuyerName));
        int begin = 0;
        int end = orderList.size() - 1;
        ArrayList<Order> ordersByBuyerNamePrefix = new ArrayList<>();

        while (begin <= end) {
            int mid = (begin + end) / 2;
            Order midOrder = orderList.get(mid);
            String midBuyerName = midOrder.getBuyerName().trim();
            if (midBuyerName.compareToIgnoreCase(prefixInitial) >= 0 && midBuyerName.compareToIgnoreCase(prefixFinal) <= 0) {
                ordersByBuyerNamePrefix.add(midOrder);
                int left = mid - 1;
                while (left >= 0 && orderList.get(left).getBuyerName().startsWith(prefixInitial)) {
                    Order leftOrder = orderList.get(left);
                    String leftBuyerName = leftOrder.getBuyerName().trim();
                    if (leftBuyerName.compareToIgnoreCase(prefixFinal) <= 0) {
                        ordersByBuyerNamePrefix.add(leftOrder);
                    } else {
                        break;
                    }
                    left--;
                }
                int right = mid + 1;
                while (right < orders.size() && orderList.get(right).getBuyerName().startsWith(prefixInitial)) {
                    Order rightOrder = orderList.get(right);
                    String rightBuyerName = rightOrder.getBuyerName().trim();
                    if (rightBuyerName.compareToIgnoreCase(prefixFinal) <= 0) {
                        ordersByBuyerNamePrefix.add(rightOrder);
                    } else {
                        break;
                    }
                    right++;
                }
                if(op == 1){
                    Collections.sort(ordersByBuyerNamePrefix, Comparator.comparing(Order::getBuyerName));
                }else{
                    Collections.reverse(ordersByBuyerNamePrefix);
                }
                return ordersByBuyerNamePrefix;
            } else if (midBuyerName.compareToIgnoreCase(prefixInitial) < 0) {
                begin = mid + 1;
            } else {
                end = mid - 1;
            }
        }
        return null;
    }
    public ArrayList<Order> searchByTotalPriceRange(double minPrice, double maxPrice, int op) {
        List<Order> orderList = new ArrayList<>(orders.values());
        Collections.sort(orderList, Comparator.comparing(Order::getTotalPrice));

        int begin = 0;
        int end = orderList.size() - 1;
        ArrayList<Order> ordersInRange = new ArrayList<>();

        while (begin <= end) {
            int mid = (begin + end) / 2;
            Order midOrder = orderList.get(mid);
            double midPrice = midOrder.getTotalPrice();

            if (midPrice >= minPrice && midPrice <= maxPrice) {
                ordersInRange.add(midOrder);

                int left = mid - 1;
                while (left >= 0 && orderList.get(left).getTotalPrice() >= minPrice) {
                    ordersInRange.add(orderList.get(left));
                    left--;
                }

                int right = mid + 1;
                while (right < orderList.size() && orderList.get(right).getTotalPrice() <= maxPrice) {
                    ordersInRange.add(orderList.get(right));
                    right++;
                }
                if(op == 1){
                    Collections.sort(ordersInRange, Comparator.comparing(Order::getTotalPrice));
                }else{
                    Collections.reverse(ordersInRange);
                }
                return ordersInRange;
            }

            if (midPrice < minPrice) {
                begin = mid + 1;
            } else {
                end = mid - 1;
            }
        }

        return null;
    }

    public ArrayList<Order> searchByDateRange(int yearMin, int monthMin, int dayMin, int yearMax, int monthMax, int dayMax, int op) {
        List<Order> orderList = new ArrayList<>(orders.values());
        Collections.sort(orderList, Comparator.comparing(Order::getPurchaseDate));

        int begin = 0;
        int end = orderList.size() - 1;
        ArrayList<Order> ordersInRange = new ArrayList<>();

        while (begin <= end) {
            int mid = (begin + end) / 2;
            Order midOrder = orderList.get(mid);
            LocalDate midDate = midOrder.getPurchaseDate();

            if ((midDate.isEqual(LocalDate.of(yearMin, monthMin, dayMin)) || midDate.isAfter(LocalDate.of(yearMin, monthMin, dayMin)))
                    && (midDate.isEqual(LocalDate.of(yearMax, monthMax, dayMax)) || midDate.isBefore(LocalDate.of(yearMax, monthMax, dayMax)))) {
                ordersInRange.add(midOrder);

                int left = mid - 1;
                while (left >= 0 ) {
                    if (orderList.get(left).getPurchaseDate().isBefore(LocalDate.of(yearMin, monthMin, dayMin))) {
                        break;
                    }
                    ordersInRange.add(orderList.get(left));
                    left--;
                }

                int right = mid + 1;
                while (right < orderList.size()) {
                    if (orderList.get(right).getPurchaseDate().isAfter(LocalDate.of(yearMax, monthMax, dayMax))) {
                        break;
                    }
                    ordersInRange.add(orderList.get(right));
                    right++;
                }

                break;
            }

            if (midDate.isBefore(LocalDate.of(yearMin, monthMin, dayMin))) {
                begin = mid + 1;
            } else {
                end = mid - 1;
            }
        }

        if (ordersInRange.isEmpty()) {
            return null;
        }

        if(op == 1){
            Collections.sort(ordersInRange, Comparator.comparing(Order::getPurchaseDate));
        }else{
            Collections.reverse(ordersInRange);
        }

        return ordersInRange;
    }

}


