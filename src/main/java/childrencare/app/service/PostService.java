package childrencare.app.service;

import childrencare.app.model.PostCategoryModel;
import childrencare.app.model.PostModel;
import childrencare.app.repository.BlogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    @Autowired
    private BlogRepository blogRepository;

    public Page<PostModel> findAllAndSearch(String title, int postCategoryId, String email, int status, String typeOrder, int index, int size) {
        return blogRepository.findAllBy("%" + title + "%", postCategoryId, "%" + email + "%", status, typeOrder, PageRequest.of(index, size));
    }
    public Page<PostModel> findAllByCategoryID(int cateID, int index, int size){
        return blogRepository.findAllByPostCategory(cateID, PageRequest.of(index, size));
    }

}
