package childrencare.app.service;

import childrencare.app.model.PostModel;
import childrencare.app.repository.BlogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BlogService {

    private BlogRepository blogRepository;

    @Autowired
    public BlogService(BlogRepository blogRepository) {
        this.blogRepository = blogRepository;
    }

    public List<PostModel> findAllByUpdateAt() {
        return (List<PostModel>) blogRepository.findAll(Sort.by("updatedAt").descending());
    }
    public Page<PostModel> getBlogPaging(int index, int size, String title){
        Page<PostModel> listPostSearchByTitle = blogRepository.findAllByTitle("%"+title+"%", PageRequest.of(index,size));
        return listPostSearchByTitle;
    }

    public long count(){
        return blogRepository.count();
    }

}
