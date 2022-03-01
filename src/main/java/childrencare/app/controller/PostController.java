package childrencare.app.controller;

import childrencare.app.model.PostCategoryModel;
import childrencare.app.model.PostModel;
import childrencare.app.service.BlogCategoryService;
import childrencare.app.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/manager")
public class PostController {

    @Autowired
    private BlogCategoryService blogCategoryService;

    @Autowired
    private PostService postService;

    @RequestMapping("/post")
    public String reservationInfor(Model model
            , @RequestParam(name = "page", required = false, defaultValue = "0") Optional<Integer> page
            , @RequestParam(name = "type", required = false, defaultValue = "-1") String type
            , @RequestParam(name = "categoryId", required = false, defaultValue = "-1") String categoryId) {
        List<PostCategoryModel> postCategoryModelList = blogCategoryService.findAll();


        int currentPage = page.orElse(0);
        Page<PostModel> postModels = null;
        if (categoryId.equals("-1") && type.equals("-1")) {
            postModels = postService.findAll(currentPage, 2);
        } else if (categoryId != "-1") {
            postModels = postService.findAllByCategoryID(Integer.parseInt(categoryId), currentPage, 2);
        } else if (type != "-1") {
            postModels = postService.findAllByStatus(Integer.parseInt(type), currentPage, 2);
        }


        int totalPages = postModels.getTotalPages();
        if (totalPages > 0) {
            int start = Math.max(0, currentPage - 2);
            int end = Math.min(currentPage + 2, totalPages - 1);
            if (totalPages > 5) {
                if (end == totalPages) start = end - 5;
                else if (start == 1) end = start + 5;

            }
            List<Integer> pageNumbers = IntStream.rangeClosed(start, end).boxed().collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        model.addAttribute("listPost", postModels.toList());
        model.addAttribute("postCategoryModelList", postCategoryModelList);
        model.addAttribute("categoryId", categoryId);

        model.addAttribute("pagingPost", postModels);
        model.addAttribute("type", type);
        model.addAttribute("categoryId", categoryId);

        return "post_manager";
    }
}
