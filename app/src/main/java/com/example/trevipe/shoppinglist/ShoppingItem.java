package com.example.trevipe.shoppinglist;
/*
 * ShoppingItem was created to have an object that will contain all relevant data about itself.  
 */
public class ShoppingItem implements Item{
	int priority;
	double price;
	String item;
	int quantity;

	public ShoppingItem(String inItem, double inPrice, int inPriority, int inQuantity){
		item=inItem;
		price=inPrice;
		priority=inPriority;
		quantity=inQuantity;
	}
	public ShoppingItem(){
		item="blank";
		price=-1;
		priority=-1;
		quantity=-1;
	}
	public double getPrice(){
		return price;
	}
	public int getPriority(){
		return priority;
	}
	public String getItem(){
		return item;
	}
	public int getQuantity(){
		return quantity;
	}
	public void setQuantity(int inQuantity){
		quantity=inQuantity;
	}
	public void setItem(String inItem){
		item=inItem;
	}
	public void setPriority(int inPriority){
		priority = inPriority;
	}
	public void setPrice (double inPrice){
		price = inPrice;
	}
	public ShoppingItem getShoppingItem(){
		return this;
	}
	public String toString(){
		String returnSt = item;
		return returnSt;
	}
	public String outString(){
		String st = "";
		st = item+","+price+","+quantity+","+priority;
		return st;
	}
}
