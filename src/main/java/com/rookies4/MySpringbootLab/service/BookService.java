package com.rookies4.MySpringbootLab.service;

import com.rookies4.MySpringbootLab.controller.dto.BookDTO;
import com.rookies4.MySpringbootLab.entity.Book;
import com.rookies4.MySpringbootLab.exception.BusinessException;
import com.rookies4.MySpringbootLab.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    // 전체 조회
    public List<BookDTO.BookResponse> getAllBooks() {
        return bookRepository.findAll()
                .stream()
                .map(BookDTO.BookResponse::from)
                .toList();
    }

    // ID로 조회
    public BookDTO.BookResponse getBookById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BusinessException("도서를 찾을 수 없습니다.", HttpStatus.NOT_FOUND));
        return BookDTO.BookResponse.from(book);
    }

    // ISBN으로 조회
    public BookDTO.BookResponse getBookByIsbn(String isbn) {
        Book book = bookRepository.findByIsbn(isbn)
                .orElseThrow(() -> new BusinessException("해당 ISBN의 도서를 찾을 수 없습니다.", HttpStatus.NOT_FOUND));
        return BookDTO.BookResponse.from(book);
    }

    // 저자로 조회
    public List<BookDTO.BookResponse> getBooksByAuthor(String author) {
        return bookRepository.findByAuthor(author)
                .stream()
                .map(BookDTO.BookResponse::from)
                .toList();
    }

    // 도서 등록
    @Transactional
    public BookDTO.BookResponse createBook(BookDTO.BookCreateRequest request) {
        Book savedBook = bookRepository.save(request.toEntity());
        return BookDTO.BookResponse.from(savedBook);
    }

    // 도서 수정
    @Transactional
    public BookDTO.BookResponse updateBook(Long id, BookDTO.BookUpdateRequest request) {
        Book existBook = bookRepository.findById(id)
                .orElseThrow(() -> new BusinessException("도서를 찾을 수 없습니다.", HttpStatus.NOT_FOUND));

        if (request.getTitle() != null) existBook.setTitle(request.getTitle());
        if (request.getAuthor() != null) existBook.setAuthor(request.getAuthor());
        if (request.getPrice() != null) existBook.setPrice(request.getPrice());
        if (request.getPublishDate() != null) existBook.setPublishDate(request.getPublishDate());

        return BookDTO.BookResponse.from(existBook);
    }

    // 도서 삭제
    @Transactional
    public void deleteBook(Long id) {
        Book existBook = bookRepository.findById(id)
                .orElseThrow(() -> new BusinessException("도서를 찾을 수 없습니다.", HttpStatus.NOT_FOUND));
        bookRepository.delete(existBook);
    }
}