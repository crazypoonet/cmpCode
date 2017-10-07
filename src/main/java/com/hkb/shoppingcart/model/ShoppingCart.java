package com.hkb.shoppingcart.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.HashMap;

@Document(collection = "ShoppingCarts")
public class ShoppingCart {

    public static String ORDERED = "ordered";
    public static String PENDING = "pending";

    @Id
    private String id;

    public String status; //pending, ordered

    public String userName;

    public HashMap<String, Product> products;

    public HashMap<String, Integer> productQuantities;

//    @JsonDeserialize(using = MongoDateConverter.class)
    public Date lastModified;

//    @JsonDeserialize(using = MongoDateConverter.class)
    public Date orderDate;

    //total price
    public float totalPrice = 0;

    public ShoppingCart(){}

    public ShoppingCart(String status, String userName,
                        HashMap<String, Product> products,
                        HashMap<String, Integer> productQuantities,
                        Date orderDate, Date lastModified, float totalPrice){
        this.status = status;
        this.userName = userName;
        this.products = products;
        this.productQuantities = productQuantities;
        this.orderDate = orderDate;
        this.lastModified = lastModified;
        this.totalPrice = totalPrice;
    }

    public String getId(){
        return this.id;
    }

    public Product getProductFromId(String productId){
        return this.products.get(productId);
    }

    public void addProduct(Product product){

        //Check if the product is in the HashMap
        Product fromCart = this.products.get(product.getId());

        if(fromCart == null){
            this.products.put(product.getId(), product);
        }

    }

    public void addProductQuantity(Product product){
        if(this.productQuantities.containsKey(product.getId())){
            int quantity = this.productQuantities.get(product.getId());
            quantity++;
            this.productQuantities.put(product.getId(), quantity);
        }
        else {
            // init the product quantities if key not found
            this.productQuantities.put(product.getId(), 1);
        }
    }

    public void removeProduct(String productId){
        if(this.products.containsKey(productId)){
            this.products.remove(productId);
        }
    }

    public void removeProductQuantity(String productId){

        if(this.productQuantities.containsKey(productId)){
            int quantity = this.productQuantities.get(productId);
            quantity--;
            //remove datas from the HashMaps when quantity is too low
            if(quantity <1){
                this.productQuantities.remove(productId);
                this.removeProduct(productId);
            }
            else {
                this.productQuantities.put(productId, quantity);
            }
        }
    }
}
