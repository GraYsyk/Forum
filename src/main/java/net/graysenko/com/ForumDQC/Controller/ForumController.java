package net.graysenko.com.ForumDQC.Controller;

import net.graysenko.com.ForumDQC.Models.Post;
import net.graysenko.com.ForumDQC.Models.UserINF;
import net.graysenko.com.ForumDQC.Services.PostService;
import net.graysenko.com.ForumDQC.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequestMapping("/forum")
public class ForumController {

    private final UserService userService;
    private final PostService postService;

    @Autowired
    public ForumController(UserService userService, PostService postService) {
        this.userService = userService;
        this.postService = postService;
    }

    /////////////////////////////////////////////////////////////////
    //Basic Get's

    @GetMapping("/forum")
    public String forumPage(@AuthenticationPrincipal UserDetails userDetails,
                       @RequestParam(name = "page", defaultValue = "0") int page,
                       @RequestParam(name = "size", defaultValue = "10") int size,
                       @RequestParam(name = "search", required = false) String search,
                       Model model) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "creationDate"));
        Page<Post> posts;

        if (search != null && !search.isEmpty()) {
            posts = postService.findByTitel(search, pageable);
            if (search.startsWith("#")) {
                search = "#" + search.substring(1);
                posts = postService.findByDescription(search, pageable);
            }
        } else {
            posts = postService.findAll(pageable);
        }

        model.addAttribute("user", userDetails);
        model.addAttribute("posts", posts);
        return "forum/forums";
    }

    @GetMapping("/detail")
    public String detail() {
        return "forum/detail";
    }

    ////////////////////////////////////////////////////////////////////
    //Avatar & Profile configuration

    @PostMapping("/upload-avatar/{id}")
    public String uploadAvatar(@PathVariable("id") long id,
                               @RequestParam("file") MultipartFile file) {
        try {
            UserINF user = userService.getUserById(id);

            if (file != null && !file.isEmpty()) {
                byte[] avatarBytes = file.getBytes();
                user.setAvatar(avatarBytes);
                userService.saveUser(user);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "redirect:/forum/profile/" + id + "?error=io_exception";
        }
        return "redirect:/forum/profile";
    }

    @GetMapping("/avatar/{id}")
    @ResponseBody
    public ResponseEntity<byte[]> getAvatar(@PathVariable long id) {
        byte[] avatar = userService.getUserAvatar(id);

        if(avatar == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(avatar);
    }

    @GetMapping("/profile")
    public String profile(@AuthenticationPrincipal UserDetails userDetails, Model model) throws UsernameNotFoundException {

        int posts = userService.getUserByUsername(userDetails.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User was not Found")).getPosts().size();
        UserINF user = userService.getUserByUsername(userDetails.getUsername()).orElseThrow(() -> new UsernameNotFoundException("User was not Found"));

        model.addAttribute("user", user);
        model.addAttribute("posts", posts);
        return "forum/profile";
    }

    @GetMapping("/profile/{id}")
    public String otherProfile(@PathVariable long id,
                               @AuthenticationPrincipal UserDetails userDetails,
                               Model model) throws UsernameNotFoundException {

        UserINF currentUser = userService.getUserByUsername(userDetails.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User was not Found"));

        UserINF targetUser = userService.getUserById(id);

        if (currentUser.getId() != targetUser.getId()) {
            model.addAttribute("currentUser", currentUser);
            model.addAttribute("otherUser", targetUser);
            model.addAttribute("totalPosts",targetUser.getPosts().size());
            return "forum/otherProfile";
        }

        model.addAttribute("user", currentUser);
        return "forum/profile";
    }
}
