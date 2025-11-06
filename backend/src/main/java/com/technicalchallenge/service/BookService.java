package com.technicalchallenge.service;

import com.technicalchallenge.dto.BookDTO;
import com.technicalchallenge.mapper.BookMapper;
import com.technicalchallenge.model.Book;
import com.technicalchallenge.repository.BookRepository;
import com.technicalchallenge.repository.CostCenterRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class BookService {
    private static final Logger logger = LoggerFactory.getLogger(BookService.class);


    private final BookRepository bookRepository;
    private final CostCenterRepository costCenterRepository;
    private final BookMapper bookMapper;

    public List<BookDTO> getAllBooks() {
        logger.info("Retrieving all books");
        return bookRepository.findAll().stream()
               // .map(bookMapper::toDto)
                .map(this::toDtoSafe) 
                .toList();
    }

    public Optional<BookDTO> getBookById(Long id) {
        logger.debug("Retrieving book by id: {}", id);
       // return bookRepository.findById(id).map(bookMapper::toDto);
       return bookRepository.findById(id).map(this::toDtoSafe);
    }

    public void populateReferenceDataByName(Book book, BookDTO dto) {
        if (dto.getCostCenterName() != null && !dto.getCostCenterName().isBlank()) {
            var costCenter = costCenterRepository.findAll().stream()
                .filter(c -> c.getCostCenterName().equalsIgnoreCase(dto.getCostCenterName()))
                .findFirst().orElse(null);
            if (costCenter == null) throw new IllegalArgumentException("CostCenter '" + dto.getCostCenterName() + "' does not exist");
            book.setCostCenter(costCenter);
        }
        // If costCenterName is null or blank, do not modify the current costCenter
    }

    public BookDTO saveBook(BookDTO dto) {
        logger.info("Saving book: {}", dto.toString());
       // var entity = bookMapper.toEntity(dto);
        var entity = toEntitySafe(dto);  
        logger.debug("Saving book Entity: {}", entity);
        populateReferenceDataByName(entity, dto);
        var saved = bookRepository.save(entity);

       // return bookMapper.toDto(saved);
        return toDtoSafe(saved);
    }

    public void deleteBook(Long id) {
        logger.warn("Deleting book with id: {}", id);
        bookRepository.deleteById(id);
    }
        // ---------- Null-safe mapping helpers ----------
    private Book toEntitySafe(BookDTO dto) {
        if (bookMapper != null) {
            return bookMapper.toEntity(dto);
        }
        // Minimal fallback for tests without Spring context
        if (dto == null) return null;
        Book b = new Book();
        b.setId(dto.getId());
        b.setBookName(dto.getBookName());
        return b;
    }

    private BookDTO toDtoSafe(Book book) {
        if (bookMapper != null) {
            return bookMapper.toDto(book);
        }
        // Minimal fallback for tests without Spring context
        if (book == null) return null;
        BookDTO dto = new BookDTO();
        dto.setId(book.getId());
        dto.setBookName(book.getBookName());
        return dto;
    }
}
