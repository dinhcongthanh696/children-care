package childrencare.app.service;

import childrencare.app.model.PostCategoryModel;
import childrencare.app.repository.BlogCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BlogCategoryService {


    private BlogCategoryRepository blogCategoryRepository;

    @Autowired
    public BlogCategoryService(BlogCategoryRepository blogCategoryRepository) {
        this.blogCategoryRepository = blogCategoryRepository;
    }

    public List<PostCategoryModel> findAll() {
        return (List<PostCategoryModel>) blogCategoryRepository.findAll();
    }
}
