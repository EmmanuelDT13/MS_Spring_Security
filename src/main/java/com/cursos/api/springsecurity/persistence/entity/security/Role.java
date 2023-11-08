package com.cursos.api.springsecurity.persistence.entity.security;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Role {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	private String name;
	
	@OneToMany(mappedBy="role", fetch = FetchType.EAGER)
	private List<GranthedPermission> permissions;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<GranthedPermission> getPermissions() {
		return permissions;
	}

	public void setPermissions(List<GranthedPermission> permissions) {
		this.permissions = permissions;
	}
		
}
