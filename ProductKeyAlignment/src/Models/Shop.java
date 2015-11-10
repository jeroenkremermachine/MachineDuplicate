package Models;

import java.util.ArrayList;

public class Shop
{
	private String name;
	private ArrayList<Key> keys;
	private int producten;

	public Shop(String shopName, ArrayList<Key> KeyList,int a)
	{
		name = shopName;
		keys = KeyList;
		producten=a;
	}

	public String getName()
	{
		return name;
	}

	public ArrayList<Key> getKey()
	{
		return keys;
	}
	public int getNrProducten()
	{
		return producten;
	}
	
}

