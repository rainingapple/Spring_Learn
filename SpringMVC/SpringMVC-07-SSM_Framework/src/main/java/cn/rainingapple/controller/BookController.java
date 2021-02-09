package cn.rainingapple.controller;


import cn.rainingapple.pojo.Books;
import cn.rainingapple.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class BookController {

    @Autowired
    @Qualifier("BookServiceImpl")
    private BookService bookService;

    @RequestMapping("/listbooks")
    public String listbooks(Model model){
        List<Books> books = bookService.queryAllBook();
        model.addAttribute("books",books);
        return "listedbooks";
    }

    @RequestMapping("/addbook")
    public String addbook(Model model){
        return "addBook";
    }

    @RequestMapping("/add")
    public String add(Books book){
        System.out.println(book);
        bookService.addBook(book);
        return "redirect:/listbooks";
    }

    @RequestMapping("/updatebook")
    public String updatebook(Model model){
        return "updateBook";
    }

    @RequestMapping("/update")
    public String update(Model model,Books book){
        System.out.println(book);
        bookService.updateBook(book);
        Books books = bookService.queryBookById(book.getBookID());
        model.addAttribute("books", books);
        return "redirect:/listbooks";
    }

    @RequestMapping("/delete/{id}")
    public String delete(@PathVariable int id){
        bookService.deleteBookById(id);
        return "redirect:/listbooks";
    }
}
