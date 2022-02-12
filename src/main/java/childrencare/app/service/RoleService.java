package childrencare.app.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import childrencare.app.model.RoleModel;
import childrencare.app.repository.RoleRepository;

@Service
public class RoleService {
	private final RoleRepository roleRepository;
	
	public RoleService(RoleRepository roleRepository) {
		this.roleRepository = roleRepository;
	}
	
	public List<RoleModel> getAllRoles(){
		return roleRepository.findAll();
	}
	
	public void saveRole(RoleModel roleModel) {
		roleRepository.save(roleModel);
	}
	
	
}
