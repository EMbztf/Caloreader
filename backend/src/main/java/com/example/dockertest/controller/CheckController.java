package com.example.dockertest.controller;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class CheckController {

	@GetMapping("/check")
	public ResponseEntity<Resource> check() {
		return ResponseEntity.ok().build();
	}
}
