package com.gccws.common.model;

import lombok.Data;

import javax.validation.constraints.*;
import java.util.Date;

/**
 * @Author    Md. Mizanur Rahman
 * @Since     August 1, 2022
 * @version   1.0.0
 */

// test model with valid data json

//{
//	"name": "Mizan",
//	"phone": "123",
//	"age": 10,
//	"salary": 12345.89,
//	"email": "info@gmail.com",
//	"joinDate": "2023-08-16",
//	"dateOfBirth": "1990-05-16",
//	"description": "<p>Hello</p>"
//}

@Data
public class WelcomeModel {

	@NotNull
	@Pattern(regexp = "[a-zA-Z0-9]*")
	private String name;

	@Pattern(regexp = "[0-9]*")
	private String phone;

	@Min(value = 10)
	@Max(value = 100)
	@Positive
	private Integer age;

	@Digits(integer = 5, fraction = 2)
	private Double salary;

	@Email(regexp ="(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])")
	@NotEmpty
	private String email;

	@FutureOrPresent
	private Date joinDate;

	@Past
	private Date dateOfBirth;

	private String description;
	
}
