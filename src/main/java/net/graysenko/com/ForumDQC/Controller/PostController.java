package net.graysenko.com.ForumDQC.Controller;

import net.graysenko.com.ForumDQC.Enums.CommentStatus;
import net.graysenko.com.ForumDQC.Enums.PostStatus;
import net.graysenko.com.ForumDQC.Models.Comments;
import net.graysenko.com.ForumDQC.Models.Media;
import net.graysenko.com.ForumDQC.Models.Post;
import net.graysenko.com.ForumDQC.Models.UserINF;
import net.graysenko.com.ForumDQC.Services.CommentService;
import net.graysenko.com.ForumDQC.Services.MediaService;
import net.graysenko.com.ForumDQC.Services.PostService;
import net.graysenko.com.ForumDQC.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URLConnection;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/post")
public class PostController {

    private final PostService postService;
    private final MediaService mediaService;
    private final CommentService commentService;
    private final UserService userService;

    @Autowired
    public PostController(PostService postService, MediaService mediaService, CommentService commentService, UserService userService) {
        this.postService = postService;
        this.mediaService = mediaService;
        this.commentService = commentService;
        this.userService = userService;
    }

    @GetMapping("/new")
    public String newPost(@AuthenticationPrincipal UserDetails userDetails, @ModelAttribute("post") Post post, Model model) {
        model.addAttribute("user", userDetails);
        return "post/New";
    }


    @GetMapping("/media/{id}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable Long id) {
        Media media = mediaService.findById(id);
        if (media == null) return ResponseEntity.notFound().build();

        String mimeType = URLConnection.guessContentTypeFromName(media.getFilename());
        if (mimeType == null) {
            mimeType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + media.getFilename() + "\"")
                .contentType(MediaType.parseMediaType(mimeType))
                .body(new InputStreamResource(new ByteArrayInputStream(media.getMediaURL())));
    }


    @GetMapping("/{id}")
    public String postDetails(@AuthenticationPrincipal UserDetails userDetails, @PathVariable("id") Long postId, Model model) {

        Post post = postService.findById(postId).orElseThrow(() -> new RuntimeException("Post was not found"));
        if (post == null) {
            return "redirect:/forum/forum?error=post_not_found";
        }

        UserINF user = userService.getUserByUsername(userDetails.getUsername()).orElseThrow(() -> new RuntimeException("User was not found"));

        List<Comments> comments = commentService.getCommentsByPostId(postId);

        List<Media> mediaList = mediaService.findByPostId(postId);

        model.addAttribute("user", user);
        model.addAttribute("post", post);
        model.addAttribute("comments", comments);
        model.addAttribute("mediaList", mediaList);

        return "post/posts";
    }


    @PostMapping("/new")
    public String createPost(@AuthenticationPrincipal UserDetails userDetails,
                             @RequestParam String title,
                             @RequestParam(required = false) String descr,
                             @RequestParam String content,
                             @RequestParam(value = "media", required = false) MultipartFile[] mediaFiles, Model model) {
        if (title == null || title.isEmpty() || content == null || content.isEmpty()) {
            model.addAttribute("error", "Title and Content cannot be empty.");
            return "redirect:/post/new";
        }

        UserINF user = userService.getUserByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User was not found"));

        Date date = new Date();
        Post newPost = new Post(user, title, descr, content, date, date, PostStatus.active);
        postService.save(newPost);

        final long MAX_FILE_SIZE = 100 * 1024 * 1024; // 100 MB

        if (mediaFiles != null) {
            for (MultipartFile mediaFile : mediaFiles) {
                if (!mediaFile.isEmpty()) {
                    if (mediaFile.getSize() > MAX_FILE_SIZE) {
                        model.addAttribute("error", "File " + mediaFile.getOriginalFilename() + " is too large. Maximum size is 100 MB.");
                        return "post/New";
                    }

                    String contentType = mediaFile.getContentType();
                    if (contentType == null) {
                        model.addAttribute("error", "Could not determine the file type for: " + mediaFile.getOriginalFilename());
                        return "post/New";
                    }

                    if (contentType.startsWith("image/") || contentType.startsWith("video/")) {
                        try {
                            byte[] mediaBytes = mediaFile.getBytes();
                            Media media = new Media(newPost, mediaBytes, contentType, mediaFile.getOriginalFilename()); // Сохраняем MIME-тип
                            mediaService.save(media);
                        } catch (IOException e) {
                            e.printStackTrace();
                            model.addAttribute("error", "Error saving file: " + mediaFile.getOriginalFilename());
                            return "post/New";
                        }
                    } else {
                        model.addAttribute("error", "Unsupported media type: " + contentType + " for file: " + mediaFile.getOriginalFilename());
                        return "post/New";
                    }
                }
            }
        }

        return "redirect:/post/" + newPost.getId();
    }


    @PostMapping("/{id}/comment")
    public String addComment(@PathVariable Long id,
                             @RequestParam String content,
                             @AuthenticationPrincipal UserDetails userDetails,
                             Model model) {
        Date date = new Date();
        Post post = postService.findById(id).orElseThrow(() -> new RuntimeException("Post was not found"));
        UserINF user = userService.getUserByUsername(userDetails.getUsername()).orElseThrow(() -> new RuntimeException("User was not found"));
        Comments comment = new Comments(post, user, content, date, date, CommentStatus.active);
        commentService.saveComment(comment);
        return  "redirect:/post/" + id;
    }
}

