package com.example.dockertest.controller;

import com.example.dockertest.model.dao.User;
import com.example.dockertest.model.error.ErrorMessage;
import com.example.dockertest.model.request.UpdateAgeRequest;
import com.example.dockertest.model.request.UpdateHeightRequest;
import com.example.dockertest.model.request.UpdatePasswordRequest;
import com.example.dockertest.model.request.UpdateSexRequest;
import com.example.dockertest.model.request.UpdateUsernameRequest;
import com.example.dockertest.model.request.UpdateWeightRequest;
import com.example.dockertest.repository.UserRepository;
import com.example.dockertest.security.services.UserDetailsImpl;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class UserController {

	@Autowired
	UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	/**
	 * Retrieves the authenticated user.
	 *
	 * @return         the authenticated user
	 */
	private User getAuthenticatedUser() {
		UserDetailsImpl user = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return userRepository.findById(user.getId()).get();
	}

	@GetMapping("/user")
	public ResponseEntity<?> getProfile() {
		User user = getAuthenticatedUser();
		return ResponseEntity.ok(user);
	}
	@PutMapping("/user/username")
	public ResponseEntity<?> updateUsername(@RequestBody UpdateUsernameRequest request) {
		ResponseEntity<?> response = validateUpdateUsernameRequest(request);
		if(response != null) {
			return response;
		}

		User userUpdated = getAuthenticatedUser();
		userUpdated.setUsername(request.getUsername());
		userRepository.save(userUpdated);
		return ResponseEntity.ok(userUpdated);
	}

	@PutMapping("/user/password")
	public ResponseEntity<?> updatePassword(@RequestBody UpdatePasswordRequest request) {
		ResponseEntity<?> response = validateUpdatePasswordRequest(request);
		if(response != null) {
			return response;
		}
		User userUpdated = getAuthenticatedUser();
		userUpdated.setPassword(passwordEncoder.encode(request.getNewPassword()));
		userRepository.save(userUpdated);
		return ResponseEntity.ok(userUpdated);
	}

	@PutMapping("/user/sex")
	public ResponseEntity<?> updateSex(@RequestBody UpdateSexRequest request) {
		if(!List.of("male", "female").contains(request.getSex().toLowerCase())) {
			return ResponseEntity.badRequest().body("Invalid sex!");
		}
		User userUpdated = getAuthenticatedUser();
		userUpdated.setSex(request.getSex().toLowerCase());
		userRepository.save(userUpdated);
		return ResponseEntity.ok(userUpdated);
	}

	@PutMapping("/user/height")
	public ResponseEntity<?> updateHeight(@RequestBody UpdateHeightRequest request) {
		ResponseEntity<?> response = validateUpdateHeightRequest(request);
		if(response != null) {
			return response;
		}
		User userUpdated = getAuthenticatedUser();
		userUpdated.setHeight(request.getHeight());
		userRepository.save(userUpdated);
		return ResponseEntity.ok(userUpdated);
	}

	@PutMapping("/user/weight")
	public ResponseEntity<?> updateWeight(@RequestBody UpdateWeightRequest request) {
		ResponseEntity<?> response = validateUpdateWeightRequest(request);
		if(response != null) {
			return response;
		}
		User userUpdated = getAuthenticatedUser();
		userUpdated.setWeight(request.getWeight());
		userRepository.save(userUpdated);
		return ResponseEntity.ok(userUpdated);
	}

	@PutMapping("/user/age")
	public ResponseEntity<?> updateAge(@RequestBody UpdateAgeRequest request) {
		ResponseEntity<?> response = validateUpdateAgeRequest(request);
		if(response != null) {
			return response;
		}
		User userUpdated = getAuthenticatedUser();
		userUpdated.setAge(request.getAge());
		userRepository.save(userUpdated);
		return ResponseEntity.ok(userUpdated);
	}

	private ResponseEntity<?> validateUpdateUsernameRequest(UpdateUsernameRequest updateUsernameRequest) {
		boolean hasError = false;
		UpdateUsernameRequest error = new UpdateUsernameRequest();
		if(updateUsernameRequest.getUsername() == null || updateUsernameRequest.getUsername().isEmpty()) {
			error.setUsername("Username cannot be empty!");
			hasError = true;
		} else {
			if (userRepository.existsByUsername(updateUsernameRequest.getUsername())) {
				error.setUsername("Username already exists!");
				hasError = true;
			}
		}
		if(updateUsernameRequest.getPassword() == null || updateUsernameRequest.getPassword().isEmpty()) {
			error.setPassword("Password cannot be empty!");
			hasError = true;
		} else {
			if (!passwordEncoder.matches(updateUsernameRequest.getPassword(), getAuthenticatedUser().getPassword())) {
				error.setPassword("Password is incorrect!");
				hasError = true;
			}
		}

		if(hasError) {
			return ResponseEntity.badRequest().body(error);
		}
		return null;
	}

	private ResponseEntity<?> validateUpdatePasswordRequest(UpdatePasswordRequest updatePasswordRequest) {
		boolean hasError = false;
		UpdatePasswordRequest error = new UpdatePasswordRequest();
		if(updatePasswordRequest.getPassword() == null || updatePasswordRequest.getPassword().isEmpty()) {
			error.setPassword("Password cannot be empty!");
			hasError = true;
		} else {
			if (!passwordEncoder.matches(updatePasswordRequest.getPassword(), getAuthenticatedUser().getPassword())) {
				error.setPassword("Password is incorrect!");
				hasError = true;
			}
		}
		if(updatePasswordRequest.getNewPassword() == null || updatePasswordRequest.getNewPassword().isEmpty()) {
			error.setNewPassword("New password cannot be empty!");
			hasError = true;
		} else {
			if(updatePasswordRequest.getNewPassword().length() < 8) {
				error.setNewPassword("New password must be at least 8 characters long!");
				hasError = true;
			}
		}

		if(hasError) {
			return ResponseEntity.badRequest().body(error);
		}
		return null;
	}

	private ResponseEntity<?> validateUpdateHeightRequest(UpdateHeightRequest updateHeightRequest) {
		boolean hasError = false;
		ErrorMessage error = new ErrorMessage();
		if(updateHeightRequest.getHeight() <= 0) {
			error.setMessage("Height must be greater than 0!");
			hasError = true;
		} else if(updateHeightRequest.getHeight() > 300) {
			error.setMessage("Height must be less than 300!");
			hasError = true;
		}
		if(hasError) {
			return ResponseEntity.badRequest().body(error);
		}
		return null;
	}

	private ResponseEntity<?> validateUpdateWeightRequest(UpdateWeightRequest updateWeightRequest) {
		boolean hasError = false;
		ErrorMessage error = new ErrorMessage();
		if(updateWeightRequest.getWeight() <= 0) {
			error.setMessage("Weight must be greater than 0!");
			hasError = true;
		} else if(updateWeightRequest.getWeight() > 300) {
			error.setMessage("Weight must be less than 300!");
			hasError = true;
		}
		if(hasError) {
			return ResponseEntity.badRequest().body(error);
		}
		return null;
	}

	private ResponseEntity<?> validateUpdateAgeRequest(UpdateAgeRequest updateAgeRequest) {
		boolean hasError = false;
		ErrorMessage error = new ErrorMessage();
		if(updateAgeRequest.getAge() <= 0) {
			error.setMessage("Age must be greater than 0!");
			hasError = true;
		} else if(updateAgeRequest.getAge() > 150) {
			error.setMessage("Age must be less than 150!");
			hasError = true;
		}
		if(hasError) {
			return ResponseEntity.badRequest().body(error);
		}
		return null;
	}
}
