package com.fastfood.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import com.fastfood.dao.ProductDAO;
import com.fastfood.entity.Product;

@SessionScope
@Service
public class CartService {
	@Autowired
	ProductDAO dao;
	
	Map<Integer, Product> map = new HashMap<>();
	public void add(Integer id) {
		Product p = map.get(id);
		if(p==null) {
			p=dao.findById(id);
			p.setQuantity(1);
			map.put(id,p);
		}
		else {
			p.setQuantity(p.getQuantity()+1);
		}

	}

	public void remove(Integer id) {
		map.remove(id);
	}

	public void update(Integer id, int qty) {
		Product p = map.get(id);
		p.setQuantity(qty);
	}

	public void clear() {
		map.clear();
	}

	public int getCount() {
		Collection<Product> ps = this.getItems();
		int count = 0;
		for(Product p:ps){
			count +=p.getQuantity();
		};
		return count;
	}
	
	public int getAmount() {
		Collection<Product> ps = this.getItems();
		int amount = 0;
		for(Product p:ps){
			amount +=p.getQuantity()*p.getPrice();
		};
		return amount;
	}
	
	public Collection<Product> getItems(){
		return map.values();
	}
	
}
