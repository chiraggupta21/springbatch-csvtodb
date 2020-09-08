package com.example.demo.config;

import java.util.List;

import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;

@Component
public class Writer implements ItemWriter<User>{

	@Autowired
	UserRepository userRepository;
	
	@Override
	public void write(List<? extends User> items) throws Exception {
		System.out.println("Writer"+items);
		userRepository.saveAll(items);
	}
}
