package childrencare.app.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import childrencare.app.model.PermissionModel;
import childrencare.app.model.RoleModel;
import childrencare.app.model.ScreenModel;
import childrencare.app.repository.PermissionRepository;

@Service
public class PermissionService {
	private final PermissionRepository permissionRepository;
	
	public PermissionService(PermissionRepository permissonRepository) {
		this.permissionRepository = permissonRepository;
	}
	
	public void addPermission(int roleId, int screenId) {
		permissionRepository.addPermission(roleId, screenId);
	}
	
	public void removePermissionByRole(int roleId,List<Integer> screenIds) {
		if(screenIds.isEmpty()) return;
		permissionRepository.removeRoleScreens(roleId,screenIds);
	}

	public List<PermissionModel> loadByRole(int roleId){
		return permissionRepository.getByRole(roleId);
	}
}
