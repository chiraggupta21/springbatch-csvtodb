package com.example.demo.config;

import java.util.HashMap;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import com.example.demo.model.User;

@Component
public class Processor implements ItemProcessor<User, User> {

	HashMap<String, String> hashmap=new HashMap<>();
	public Processor() {
		hashmap.put("a", "A For Apple");
		hashmap.put("b", "B For Ball");
	}

	@Override
	public User process(User item) throws Exception {
		System.out.println("Item Processor"+item);
		item.setDepartment(hashmap.get(item.getDepartment()));
		return item;
	}

	
}
