package com.rookies4.MySpringbootLab.controller;

import com.rookies4.MySpringbootLab.controller.dto.BookDTO;
import com.rookies4.MySpringbootLab.entity.Book;
import com.rookies4.MySpringbootLab.entity.BookDetail;
import com.rookies4.MySpringbootLab.exception.BusinessException;
import com.rookies4.MySpringbootLab.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    private final BookRepository bookRepository;

    /**
     * 모든 도서 조회
     */
    @GetMapping
    public List<BookDTO.Response> getAllBooks() {
        return bookRepository.findAll()
                .stream()
                .map(BookDTO.Response::fromEntity)
                .toList();
    }

    /**
     * ID로 도서 조회
     */
    @GetMapping("/{id}")
    public ResponseEntity<BookDTO.Response> getBookById(@PathVariable Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Book Not Found", HttpStatus.NOT_FOUND));
        return ResponseEntity.ok(BookDTO.Response.fromEntity(book));
    }

    /**
     * ISBN으로 도서 조회
     */
    @GetMapping("/isbn/{isbn}")
    public BookDTO.Response getBookByIsbn(@PathVariable String isbn) {
        Book book = bookRepository.findByIsbn(isbn)
                .orElseThrow(() -> new BusinessException("Book Not Found", HttpStatus.NOT_FOUND));
        return BookDTO.Response.fromEntity(book);
    }

    /**
     * 저자명으로 도서 조회
     */
    @GetMapping("/author/{author}")
    public List<BookDTO.Response> getBooksByAuthor(@PathVariable String author) {
        return bookRepository.findByAuthor(author)
                .stream()
                .map(BookDTO.Response::fromEntity)
                .toList();
    }

    /**
     * 도서 등록 (POST)
     */
    @PostMapping
    public ResponseEntity<BookDTO.Response> createBook(@RequestBody BookDTO.Request request) {
        // ISBN 중복 체크
        if (bookRepository.existsByIsbn(request.getIsbn())) {
            throw new BusinessException("ISBN already exists", HttpStatus.CONFLICT);
        }

        Book book = Book.builder()
                .title(request.getTitle())
                .author(request.getAuthor())
                .isbn(request.getIsbn())
                .price(request.getPrice())
                .publishDate(request.getPublishDate())
                .build();

        if (request.getDetailRequest() != null) {
            BookDetail detail = BookDetail.builder()
                    .description(request.getDetailRequest().getDescription())
                    .language(request.getDetailRequest().getLanguage())
                    .pageCount(request.getDetailRequest().getPageCount())
                    .publisher(request.getDetailRequest().getPublisher())
                    .coverImageUrl(request.getDetailRequest().getCoverImageUrl())
                    .edition(request.getDetailRequest().getEdition())
                    .build();
            book.setBookDetail(detail);
        }

        Book savedBook = bookRepository.save(book);
        return new ResponseEntity<>(BookDTO.Response.fromEntity(savedBook), HttpStatus.CREATED);
    }

    /**
     * 도서 전체 수정 (PUT)
     */
    @PutMapping("/{id}")
    public ResponseEntity<BookDTO.Response> updateBook(
            @PathVariable Long id,
            @RequestBody BookDTO.Request request) {

        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Book Not Found", HttpStatus.NOT_FOUND));

        // ISBN 중복 체크
        if (!book.getIsbn().equals(request.getIsbn()) &&
                bookRepository.existsByIsbn(request.getIsbn())) {
            throw new BusinessException("ISBN already exists", HttpStatus.CONFLICT);
        }

        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        book.setIsbn(request.getIsbn());
        book.setPrice(request.getPrice());
        book.setPublishDate(request.getPublishDate());

        if (request.getDetailRequest() != null) {
            BookDetail detail = book.getBookDetail();
            if (detail == null) {
                detail = new BookDetail();
                book.setBookDetail(detail);
            }
            detail.setDescription(request.getDetailRequest().getDescription());
            detail.setLanguage(request.getDetailRequest().getLanguage());
            detail.setPageCount(request.getDetailRequest().getPageCount());
            detail.setPublisher(request.getDetailRequest().getPublisher());
            detail.setCoverImageUrl(request.getDetailRequest().getCoverImageUrl());
            detail.setEdition(request.getDetailRequest().getEdition());
        }

        Book updatedBook = bookRepository.save(book);
        return ResponseEntity.ok(BookDTO.Response.fromEntity(updatedBook));
    }

    /**
     * 도서 부분 수정 (PATCH)
     */
    @PatchMapping("/{id}")
    public ResponseEntity<BookDTO.Response> patchBook(
            @PathVariable Long id,
            @RequestBody BookDTO.PatchRequest request) {

        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Book Not Found", HttpStatus.NOT_FOUND));

        if (request.getTitle() != null) book.setTitle(request.getTitle());
        if (request.getAuthor() != null) book.setAuthor(request.getAuthor());
        if (request.getIsbn() != null &&
                !book.getIsbn().equals(request.getIsbn())) {
            if (bookRepository.existsByIsbn(request.getIsbn())) {
                throw new BusinessException("ISBN already exists", HttpStatus.CONFLICT);
            }
            book.setIsbn(request.getIsbn());
        }
        if (request.getPrice() != null) book.setPrice(request.getPrice());
        if (request.getPublishDate() != null) book.setPublishDate(request.getPublishDate());

        if (request.getDetailRequest() != null) {
            BookDetail detail = book.getBookDetail();
            if (detail == null) {
                detail = new BookDetail();
                book.setBookDetail(detail);
            }
            if (request.getDetailRequest().getDescription() != null)
                detail.setDescription(request.getDetailRequest().getDescription());
            if (request.getDetailRequest().getLanguage() != null)
                detail.setLanguage(request.getDetailRequest().getLanguage());
            if (request.getDetailRequest().getPageCount() != null)
                detail.setPageCount(request.getDetailRequest().getPageCount());
            if (request.getDetailRequest().getPublisher() != null)
                detail.setPublisher(request.getDetailRequest().getPublisher());
            if (request.getDetailRequest().getCoverImageUrl() != null)
                detail.setCoverImageUrl(request.getDetailRequest().getCoverImageUrl());
            if (request.getDetailRequest().getEdition() != null)
                detail.setEdition(request.getDetailRequest().getEdition());
        }

        Book updatedBook = bookRepository.save(book);
        return ResponseEntity.ok(BookDTO.Response.fromEntity(updatedBook));
    }

    /**
     * BookDetail 부분 수정 (PATCH)
     */
    @PatchMapping("/{id}/detail")
    public ResponseEntity<BookDTO.Response> patchBookDetail(
            @PathVariable Long id,
            @RequestBody BookDTO.BookDetailPatchRequest request) {

        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Book Not Found", HttpStatus.NOT_FOUND));

        BookDetail detail = book.getBookDetail();
        if (detail == null) {
            detail = new BookDetail();
            book.setBookDetail(detail);
        }

        if (request.getDescription() != null) detail.setDescription(request.getDescription());
        if (request.getLanguage() != null) detail.setLanguage(request.getLanguage());
        if (request.getPageCount() != null) detail.setPageCount(request.getPageCount());
        if (request.getPublisher() != null) detail.setPublisher(request.getPublisher());
        if (request.getCoverImageUrl() != null) detail.setCoverImageUrl(request.getCoverImageUrl());
        if (request.getEdition() != null) detail.setEdition(request.getEdition());

        Book updatedBook = bookRepository.save(book);
        return ResponseEntity.ok(BookDTO.Response.fromEntity(updatedBook));
    }

    /**
     * 도서 삭제
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        if (!bookRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        bookRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
