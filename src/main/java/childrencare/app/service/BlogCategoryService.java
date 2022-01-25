package childrencare.app.service;

import childrencare.app.model.PostCategoryModel;
import childrencare.app.repository.BlogCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BlogCategoryService {

    @Autowired
    private BlogCategoryRepository blogCategoryRepository;

    public List<PostCategoryModel> findAll() {
        return (List<PostCategoryModel>) blogCategoryRepository.findAll();
    }
}
