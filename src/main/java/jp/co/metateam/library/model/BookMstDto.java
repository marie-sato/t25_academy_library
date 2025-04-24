package jp.co.metateam.library.model;

import java.security.Timestamp;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * 書籍マスタDTO
 */
@Getter
@Setter
public class BookMstDto {

    // @NotEmpty(message = "書籍名は必須です")
    // @Size(max = 255)
    private Long id; 
    
    // @NotEmpty(message = "")
    // @Size(max = 255)
    private String isbn;

    private String title;
    
    private Timestamp deletedAt;

    private BookMst bookMst;
}
