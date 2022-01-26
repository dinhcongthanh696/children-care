package childrencare.app.service;

import childrencare.app.model.PostModel;
import childrencare.app.repository.BlogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BlogService {
    @Autowired
    private BlogRepository blogRepository;

    public List<PostModel> findAll() {
        return (List<PostModel>) blogRepository.findAll();
    }
}
