package com.example.trevipe.shoppinglist;

import java.util.ArrayList;

/**
 * Created by trevipe on 4/30/2016.
 */
public class ShoppingList extends ArrayList{
    ArrayList<ShoppingItem> list = new ArrayList<>();
    public ShoppingList(){
        list.clear();
    }
    public void add (ShoppingItem item){
        list.add(item);
    }
    public Object[] toArray(){
        Object test[] = new Object[list.size()];
        for(int a=0;a<list.size();a++){
            test[a]=list.get(a);
        }
        return test;
    }

}
