package childrencare.app.service;

import childrencare.app.model.PostCategoryModel;
import childrencare.app.model.PostModel;
import childrencare.app.model.UserModel;
import childrencare.app.repository.BlogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    @Autowired
    private BlogRepository blogRepository;

    public Page<PostModel> findAllAndSearch(String title, String catID, String type, String sortBy, int index, int size) {
        return blogRepository.findAllBy(title, catID, type, PageRequest.of(index, size, Sort.by(sortBy).ascending()));
    }

    public long count() {
        return blogRepository.count();
    }

    public int getMaxPostId() {
        return blogRepository.maxPostID();
    }

    public Page<PostModel> findAll(int index, int size) {
        return blogRepository.findAll(PageRequest.of(index, size));
    }

    public void changeStatusPost(int status, int rid) {
        blogRepository.changeStatusPost(status, rid);
    }


    public void addNewPost(String brefinfo, Date create, String detail, byte[] img, String title, Date updateAt, String author, int category, boolean status) {
        blogRepository.addPost(brefinfo, create, detail, img, title, updateAt, author, category, status);
    }

    public void upDatePost(String brefinfo, String detail, byte[] img, String title, Date updateAt, String author, int category, boolean status, int postId) {
        blogRepository.upDatePost(brefinfo, detail, img, title, updateAt, author, category, status, postId);
    }

    public PostModel getPostDetail(int id){
        return blogRepository.getPostModelByPostId(id);
    }

}
