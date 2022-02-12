package childrencare.app.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import childrencare.app.model.ScreenModel;
import childrencare.app.repository.ScreenRepository;

@Service
public class ScreenService {
	private final ScreenRepository screenRepository;
	
	public ScreenService(ScreenRepository screenRepository) {
		this.screenRepository = screenRepository;
	}
	
	public Page<ScreenModel> getPartialScreens(int size,int page,String search){
		if(page < 0) {
			page = 0;
		}
		Page<ScreenModel> screens = screenRepository.findByNameContaining("%"+search+"%",PageRequest.of(page, size , Sort.by(Direction.ASC, "screen_name")));
		if(screens.getTotalPages() > 0 && page >= screens.getTotalPages()) {
			page = screens.getTotalPages() - 1;
			screens = screenRepository.findByNameContaining("%"+search+"%",PageRequest.of(page, size , Sort.by(Direction.ASC, "screen_name")));
		}
		return screens;
	}
}
