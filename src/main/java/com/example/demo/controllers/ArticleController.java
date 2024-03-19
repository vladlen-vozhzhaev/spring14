package com.example.demo.controllers;

import com.example.demo.model.Article;
import com.example.demo.model.Comment;
import com.example.demo.repo.ArticleRepo;
import com.example.demo.repo.CommentRepo;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Base64.Decoder;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
public class ArticleController {

    @Autowired
    private ArticleRepo articleRepo;
    @Autowired
    private CommentRepo commentRepo;
    @GetMapping("/hello") // /hello?name=Ivan
    public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {
        return String.format("Hello %s!", name);
    }
    @GetMapping("/")
    public String mainPage(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = (authentication.getName());
        Iterable<Article> articles = articleRepo.findAll();
        model.addAttribute("articles", articles);
        model.addAttribute("email", userEmail);
        return "index";
    }
    @GetMapping("/blog/{id}")
    public String showArticle(@PathVariable(value = "id") long id, Model model){
        Optional<Article> article = articleRepo.findById(id);
        model.addAttribute("article", article.get());
        List<Comment> comments = commentRepo.findByArticleId(id);
        model.addAttribute("comments", comments);
        return "article";
    }
    @GetMapping("/addArticle")
    public String addArticle(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = (authentication.getName());
        model.addAttribute("email", userEmail);
        return "addArticle";
    }
    @PostMapping("/addArticle")
    @ResponseBody
    public String addArticle(@RequestParam String title, @RequestParam String content, @RequestParam String author){
        Document document = Jsoup.parse(content);
        Element img = document.selectFirst("img");
        if(img != null){
            String src = img.attr("src");
            String base64String = (src.split(",")[1]);
            byte[] buff = Base64.getDecoder().decode(base64String);
            UUID uuid = UUID.randomUUID();
            String extension = src.split(",")[0].split("/")[1].split(";")[0];
            String fileName = uuid.toString()+"."+extension;
            String upload = "C:/java/files/"+fileName;
            img.attr("src", upload);
            try {
                FileOutputStream fos = new FileOutputStream(upload);
                fos.write(buff);
                fos.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            content = document.selectFirst("body").html();
        }
        Article article = new Article(title, content, author);
        articleRepo.save(article);
        return "{\"result\": \"success\"}";
    }
    @GetMapping("/editArticle/{id}")
    public String editArticle(@PathVariable(value = "id") long id, Model model){
        Optional<Article> article = articleRepo.findById(id);
        model.addAttribute("article", article.get());
        return "editArticle";
    }
    @PostMapping("/editArticle")
    public String editArticle(
            @RequestParam String title,
            @RequestParam String content,
            @RequestParam String author,
            @RequestParam Long id
    ){
        Article article = new Article(title, content, author);
        article.id = id;
        articleRepo.save(article);
        return "redirect:/blog/"+id;
    }
}
