package com.rookies4.MySpringbootLab.repository;

import com.rookies4.MySpringbootLab.entity.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void testCreateBook() {
        System.out.println("---- 도서 등록 테스트 ----");
        // given
        Book book = new Book();
        book.setTitle("스프링 부트 입문");
        book.setAuthor("홍길동");
        book.setIsbn("9788956746425");
        book.setPrice(30000);
        book.setPublishDate(LocalDate.of(2025, 5, 7));

        // when
        Book savedBook = bookRepository.save(book);

        // then
        assertNotNull(savedBook.getId());
        assertEquals("스프링 부트 입문", savedBook.getTitle());
        System.out.println("등록된 도서: " + savedBook);
        System.out.println("-------------------------");
    }

    @Test
    void testFindById() {
        System.out.println("---- ID로 도서 조회 테스트 ----");
        // given
        Book book = new Book();
        book.setTitle("스프링 부트 입문");
        book.setAuthor("홍길동");
        book.setIsbn("9788956746425");
        book.setPrice(30000);
        book.setPublishDate(LocalDate.of(2025, 5, 7));
        entityManager.persist(book);

        // when
        Optional<Book> foundBook = bookRepository.findById(book.getId());

        // then
        assertTrue(foundBook.isPresent());
        assertEquals("스프링 부트 입문", foundBook.get().getTitle());
        System.out.println("조회된 도서: " + foundBook.get());
        System.out.println("-------------------------");
    }

    @Test
    void testFindByIsbn() {
        System.out.println("---- ISBN으로 도서 조회 테스트 ----");
        // given
        Book book1 = new Book();
        book1.setTitle("스프링 부트 입문");
        book1.setAuthor("홍길동");
        book1.setIsbn("9788956746425");
        book1.setPrice(30000);
        book1.setPublishDate(LocalDate.of(2025, 5, 7));
        entityManager.persist(book1);

        Book book2 = new Book();
        book2.setTitle("JPA 프로그래밍");
        book2.setAuthor("박돌리");
        book2.setIsbn("9788956746432");
        book2.setPrice(35000);
        book2.setPublishDate(LocalDate.of(2025, 4, 30));
        entityManager.persist(book2);

        // when
        Optional<Book> foundBook = bookRepository.findByIsbn("9788956746425");

        // then
        assertTrue(foundBook.isPresent());
        assertEquals("스프링 부트 입문", foundBook.get().getTitle());
        System.out.println("조회된 도서: " + foundBook.get());
        System.out.println("-------------------------");
    }

    @Test
    void testFindByAuthor() {
        System.out.println("---- 저자명으로 도서 목록 조회 테스트 ----");
        // given
        Book book1 = new Book();
        book1.setTitle("스프링 부트 입문");
        book1.setAuthor("홍길동");
        book1.setIsbn("9788956746425");
        book1.setPrice(30000);
        book1.setPublishDate(LocalDate.of(2025, 5, 7));
        entityManager.persist(book1);

        Book book2 = new Book();
        book2.setTitle("JPA 프로그래밍");
        book2.setAuthor("박돌리");
        book2.setIsbn("9788956746432");
        book2.setPrice(35000);
        book2.setPublishDate(LocalDate.of(2025, 4, 30));
        entityManager.persist(book2);

        // when
        List<Book> books = bookRepository.findByAuthor("홍길동");

        // then
        assertFalse(books.isEmpty());
        assertEquals(1, books.size());
        assertEquals("스프링 부트 입문", books.get(0).getTitle());
        System.out.println("조회된 도서 목록: " + books);
        System.out.println("-------------------------");
    }

    @Test
    void testUpdateBook() {
        System.out.println("---- 도서 정보 수정 테스트 ----");
        // given
        Book book = new Book();
        book.setTitle("스프링 부트 입문");
        book.setAuthor("홍길동");
        book.setIsbn("9788956746425");
        book.setPrice(30000);
        book.setPublishDate(LocalDate.of(2025, 5, 7));
        entityManager.persistAndFlush(book);

        // when
        Optional<Book> foundBook = bookRepository.findByIsbn("9788956746425");
        assertTrue(foundBook.isPresent());
        Book bookToUpdate = foundBook.get();
        bookToUpdate.setPrice(32000);
        Book updatedBook = bookRepository.save(bookToUpdate);

        // then
        assertEquals(32000, updatedBook.getPrice());
        System.out.println("수정된 도서: " + updatedBook);
        System.out.println("-------------------------");
    }

    @Test
    void testDeleteBook() {
        System.out.println("---- 도서 삭제 테스트 ----");
        // given
        Book book = new Book();
        book.setTitle("스프링 부트 입문");
        book.setAuthor("홍길동");
        book.setIsbn("9788956746425");
        book.setPrice(30000);
        book.setPublishDate(LocalDate.of(2025, 5, 7));
        entityManager.persist(book);

        // when
        bookRepository.delete(book);
        Optional<Book> deletedBook = bookRepository.findByIsbn("9788956746425");

        // then
        assertFalse(deletedBook.isPresent());
        System.out.println("도서 삭제 성공 여부: " + !deletedBook.isPresent());
        System.out.println("-------------------------");
    }
}