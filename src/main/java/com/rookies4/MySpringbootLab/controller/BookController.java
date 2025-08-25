package com.rookies4.MySpringbootLab.controller;

import com.rookies4.MySpringbootLab.entity.Book;
import com.rookies4.MySpringbootLab.exception.BusinessException;
import com.rookies4.MySpringbootLab.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {
    private final BookRepository bookRepository;
    
    // 모든 도서 조회
    @GetMapping
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }
    
    // ID로 도서 조회
    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        Optional<Book> optionalBook = bookRepository.findById(id);
        //map(Function) T -> R
        return optionalBook.map(book -> ResponseEntity.ok(book))
                //.map(ResponseEntity::ok)
                //.orElse(ResponseEntity.notFound().build()); //Body가 없고, 404 status code만 반환됨
                .orElseThrow(() -> new BusinessException("Book Not Found", HttpStatus.NOT_FOUND));
    }
    
    // ISBN으로 도서 조회
    @GetMapping("/isbn/{isbn}")
    public Book getBookByIsbn(@PathVariable String isbn) {
        return bookRepository.findByIsbn(isbn)
                .orElseThrow(() -> new BusinessException("Book Not Found", HttpStatus.NOT_FOUND));
    }
    
    // 저자명으로 도서 조회
    @GetMapping("/author/{author}")
    public List<Book> getBooksByAuthor(@PathVariable String author) {
        return bookRepository.findByAuthor(author);
    }
    
    // 도서 등록
    @PostMapping
    public ResponseEntity<Book> createBook(@RequestBody Book book) {
        Book savedBook = bookRepository.save(book);
        return new ResponseEntity<>(savedBook, HttpStatus.CREATED); //CREATED 201
    }
    
    // 도서 정보 수정
    @PatchMapping("/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable Long id, @RequestBody Book bookDetail) {
        Book existBook = bookRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Book Not Found", HttpStatus.NOT_FOUND));
        //가격만 변경
        existBook.setPrice(bookDetail.getPrice());

        Book updatedBook = bookRepository.save(existBook);
        return ResponseEntity.ok(updatedBook);
    }
    
    // 도서 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        //매칭돠는 Book 이 없으면
        if (!bookRepository.existsById(id)) {
            return ResponseEntity.notFound().build(); //body는 없고, 404 status code만 반환
        }
        bookRepository.deleteById(id);
        return ResponseEntity.noContent().build(); ////body는 없고, 204 status code만 반환
    }
}