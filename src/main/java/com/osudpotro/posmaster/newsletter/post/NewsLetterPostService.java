//package com.osudpotro.posmaster.newsletter.post;
//
//import com.osudpotro.posmaster.auth.AuthService;
//import com.osudpotro.posmaster.newsletter.post.NewsLetterPostDTO;
//import com.osudpotro.posmaster.newsletter.page.NewsLetterPage;
//import com.osudpotro.posmaster.newsletter.page.NewsLetterPageRepository;
//import com.osudpotro.posmaster.picture.Picture;
//import com.osudpotro.posmaster.picture.PictureRepository;
//import com.osudpotro.posmaster.user.User;
//import lombok.RequiredArgsConstructor;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.stereotype.Service;
//
//@Service
//@RequiredArgsConstructor
//public class NewsLetterPostService {
//
//    private final NewsLetterPostRepository repository;
//    private final NewsLetterPageRepository pageRepository;
//    private final PictureRepository pictureRepository;
//    private final AuthService authService;
//
//    // Create
//    public NewsLetterPost createPost(NewsLetterPostCreateRequest request) {
//        NewsLetterPost post = new NewsLetterPost();
//
//        post.setTitle(request.getTitle());
//        post.setSortDesc(request.getSortDesc());
//        post.setLongDesc(request.getLongDesc());
//        post.setLink(request.getLink());
//        User user=authService.getCurrentUser();
//        post.setCreatedBy(user);
//
//        if (request.getImageId() != null) {
//            Picture img = pictureRepository.findById(request.getImageId())
//                    .orElseThrow(() -> new RuntimeException("Picture not found"));
//            post.setImage(img);
//        }
//
//        NewsLetterPage page = pageRepository.findById(request.getPageId())
//                .orElseThrow(() -> new RuntimeException("Page not found"));
//        post.setNewsLetterPage(page);
//
//        return repository.save(post);
//    }
//
//    // Update
//    public NewsLetterPost updatePost(Long id, NewsLetterPostUpdateRequest request) {
//        NewsLetterPost post = repository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Post not found"));
//
//        post.setTitle(request.getTitle());
//        post.setSortDesc(request.getSortDesc());
//        post.setLongDesc(request.getLongDesc());
//        post.setLink(request.getLink());
//        User user=authService.getCurrentUser();
//        post.setUpdatedBy(user);
//
//        if (request.getImageId() != null) {
//            Picture img = pictureRepository.findById(request.getImageId())
//                    .orElseThrow(() -> new RuntimeException("Picture not found"));
//            post.setImage(img);
//        }
//
//        NewsLetterPage page = pageRepository.findById(request.getPageId())
//                .orElseThrow(() -> new RuntimeException("Page not found"));
//        post.setNewsLetterPage(page);
//
//        return repository.save(post);
//    }
//
//    public void deletePost(Long id) {
//        repository.deleteById(id);
//    }
//
//    public NewsLetterPost getPost(Long id) {
//        return repository.findById(id).orElseThrow(() -> new RuntimeException("Post not found"));
//    }
//
//    public Page<NewsLetterPost> getAllPosts(int page, int size) {
//        return repository.findAll(PageRequest.of(page, size));
//    }
//}


package com.osudpotro.posmaster.newsletter.post;

import com.osudpotro.posmaster.auth.AuthService;
import com.osudpotro.posmaster.newsletter.page.NewsLetterPage;
import com.osudpotro.posmaster.newsletter.page.NewsLetterPageRepository;
import com.osudpotro.posmaster.picture.Picture;
import com.osudpotro.posmaster.picture.PictureRepository;
import com.osudpotro.posmaster.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NewsLetterPostService {

    private final NewsLetterPostRepository repository;
    private final NewsLetterPageRepository pageRepository;
    private final PictureRepository pictureRepository;
    private final AuthService authService;

    // Create
    public NewsLetterPost createPost(NewsLetterPostCreateRequest request) {
        NewsLetterPost post = new NewsLetterPost();
        post.setTitle(request.getTitle());
        post.setSortDesc(request.getSortDesc());
        post.setLongDesc(request.getLongDesc());
        post.setLink(request.getLink());

        User user = authService.getCurrentUser();
        post.setCreatedBy(user);

        if (request.getImageId() != null) {
            Picture img = pictureRepository.findById(request.getImageId())
                    .orElseThrow(() -> new PictureNotFoundException(request.getImageId()));
            post.setImage(img);
        }

        NewsLetterPage page = pageRepository.findById(request.getPageId())
                .orElseThrow(() -> new NewsLetterPageNotFoundException(request.getPageId()));
        post.setNewsLetterPage(page);

        return repository.save(post);
    }

    // Update
    public NewsLetterPost updatePost(Long id, NewsLetterPostUpdateRequest request) {
        NewsLetterPost post = repository.findById(id)
                .orElseThrow(() -> new NewsLetterPostNotFoundException(id));

        post.setTitle(request.getTitle());
        post.setSortDesc(request.getSortDesc());
        post.setLongDesc(request.getLongDesc());
        post.setLink(request.getLink());

        User user = authService.getCurrentUser();
        post.setUpdatedBy(user);

        if (request.getImageId() != null) {
            Picture img = pictureRepository.findById(request.getImageId())
                    .orElseThrow(() -> new PictureNotFoundException(request.getImageId()));
            post.setImage(img);
        }

        NewsLetterPage page = pageRepository.findById(request.getPageId())
                .orElseThrow(() -> new NewsLetterPageNotFoundException(request.getPageId()));
        post.setNewsLetterPage(page);

        return repository.save(post);
    }

    // Delete
    public void deletePost(Long id) {
        if (!repository.existsById(id)) {
            throw new NewsLetterPostNotFoundException(id);
        }
        repository.deleteById(id);
    }

    // Get one
    public NewsLetterPost getPost(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NewsLetterPostNotFoundException(id));
    }

    // Get all with pagination
    public Page<NewsLetterPost> getAllPosts(int page, int size) {
        return repository.findAll(PageRequest.of(page, size));
    }
}
