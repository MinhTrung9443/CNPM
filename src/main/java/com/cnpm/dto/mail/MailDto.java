package com.cnpm.dto.mail;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MailDto {
	public String name;
	public String email;
	public String description;
}
