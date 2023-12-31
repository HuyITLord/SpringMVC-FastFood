package com.fastfood.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fastfood.dao.OrderDAO;
import com.fastfood.dao.OrderDetailDAO;
import com.fastfood.entity.Customer;
import com.fastfood.entity.Order;
import com.fastfood.entity.OrderDetail;
import com.fastfood.entity.Product;
import com.fastfood.service.CartService;

@Controller
public class OrderController {
	@Autowired
	 HttpSession session;
	@Autowired
	CartService cart;
	
	@Autowired
	OrderDAO dao;
	
	@Autowired
	OrderDetailDAO ddao;
	
	@GetMapping("/order/checkout")
	public String showForm(@ModelAttribute("order")Order order) {
		Customer user = (Customer) session.getAttribute("user");
		order.setOrderdate(new Date());
		order.setCustomer(user);
		order.setAmount(cart.getAmount());
		return "order/checkout";
	}
	
	@PostMapping("/order/checkout")
	public String order(Model model, @ModelAttribute("order")Order order) {
		Collection<Product> list = cart.getItems();
		List<OrderDetail> details = new ArrayList<>();
		for(Product product:list) {
			OrderDetail detail = new OrderDetail();
			detail.setOrder(order);
			detail.setProduct(product);
			detail.setPrice(product.getPrice());
			detail.setQuantity(product.getQuantity());
			details.add(detail);
		}
		dao.create(order,details);
		cart.clear();
		return "redirect:/order/list";
		
	}
	@GetMapping("/order/list")
	public String list(Model model) {
		Customer user = (Customer) session.getAttribute("user");
		List<Order> orders=dao.findByUser(user);
		model.addAttribute("orders",orders);
		return "order/list";
	}
	
	@GetMapping("/order/detail/{id}")
	public String detail(Model model, @PathVariable("id")Integer id) {
		Order order = dao.findById(id);
		List<OrderDetail> details=ddao.findByOrder(order);
		model.addAttribute("order",order);
		model.addAttribute("details",details);
		return "order/detail";
	}
	
}
