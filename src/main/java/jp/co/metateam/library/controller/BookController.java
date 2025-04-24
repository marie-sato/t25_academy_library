package jp.co.metateam.library.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import jp.co.metateam.library.model.Account;
import jp.co.metateam.library.model.BookMst;
import jp.co.metateam.library.model.BookMstDto;
import jp.co.metateam.library.service.BookMstService;
import lombok.extern.log4j.Log4j2;

/**
 * 書籍関連クラス
 */
@Log4j2
@Controller
public class BookController {
    
    private final BookMstService bookMstService;

    @Autowired
    public BookController(BookMstService bookMstService){
        this.bookMstService = bookMstService;
    }

    @GetMapping("/book/index")
    public String index(Model model) {
        // 書籍を全件取得
        List<BookMstDto> bookMstList = this.bookMstService.findAvailableWithStockCount();
        
        model.addAttribute("bookMstList", bookMstList);

        return "book/index";
    }

    @GetMapping("/book/add")
    public String add(Model model) {
        if (!model.containsAttribute("bookMstDto")) {
            model.addAttribute("bookMstDto", new BookMstDto());
        }

        return "book/add";
    }
    
    @PostMapping("/book/add")
    public String register(@Valid @ModelAttribute BookMstDto bookMstDto, BindingResult result, RedirectAttributes ra){
        
        
        try{
            String title = bookMstDto.getTitle();
            String isbn = bookMstDto.getIsbn();

            boolean errIsbnFlg = false;
            boolean errTitleFlg = false;


            
            BookMst isbnExist=this.bookMstService.selectByIsbn(bookMstDto.getIsbn());
            BookMst titleExist=this.bookMstService.selectByTitle(bookMstDto.getTitle());
            
            if(title ==null || title.isEmpty()){
                result.rejectValue("title","error.value","書籍名は必須です。");
                errTitleFlg=true;
                
            }
            if(isbn ==null || isbn.isEmpty()){
                result.rejectValue("isbn","error.value","ISBNは必須です。");
                errIsbnFlg=true;

            }

            
            
            if(title.length() >255){
                result.rejectValue("title","error.value","書籍名は255文字以内で入力してください。");
                errTitleFlg=true;
                
            }
            if(!isbn.matches("\\d{13}")){
                result.rejectValue("isbn","error.value","ISBNは13桁の半角数字で入力してください。");
                errIsbnFlg = true;
            }


           
            if(errTitleFlg || errIsbnFlg){
                throw new Exception("Fiil out the form.");
            }

            if(isbnExist != null){
                result.rejectValue("isbn","error.value","このISBNは登録済みです。");
                errIsbnFlg=true;

            }            


            bookMstService.save(bookMstDto);
   
             return "redirect:/book/index";
            
        }catch (Exception e){
               log.error("登録失敗:"+e.getMessage());
               log.error(" 書籍情報の保存に失敗しました",e);
               ra.addFlashAttribute("bookMstDto", bookMstDto);
               ra.addFlashAttribute("org.springframework.validation.BindingResult.bookMstDto", result);
               return "redirect:/book/add";
            }
        }
}


