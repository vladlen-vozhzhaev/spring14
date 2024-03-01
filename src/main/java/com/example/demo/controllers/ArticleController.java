package com.example.demo.controllers;

import com.example.demo.model.Article;
import com.example.demo.repo.ArticleRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class ArticleController {

    @Autowired
    private ArticleRepo articleRepo;
    @GetMapping("/hello") // /hello?name=Ivan
    public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {
        return String.format("Hello %s!", name);
    }
    @GetMapping("/")
    public String mainPage(Model model){
        Iterable<Article> articles = articleRepo.findAll();
        model.addAttribute("articles", articles);
        return "index";
    }
    @GetMapping("/blog/{id}")
    public String showArticle(@PathVariable(value = "id") long id, Model model){
        Optional<Article> article = articleRepo.findById(id);
        model.addAttribute("article", article.get());
        return "article";
    }
    @GetMapping("/addArticle")
    public String addArticle(){
        return "addArticle";
    }
    @PostMapping("/addArticle")
    public String addArticle(@RequestParam String title, @RequestParam String content, @RequestParam String author){
        Article article = new Article(title, content, author);
        articleRepo.save(article);
        return "redirect:/";
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
