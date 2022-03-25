package com.example.board1.repository;

import com.example.board1.entity.Board;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
class BoardRepositoryTests {

    @Autowired
    BoardRepository boardRepository;

    @Test
    @DisplayName("class 출력 테스트")
    void testClass() {
        System.out.println(boardRepository.getClass().getName());
    }

    @Test
    @DisplayName("BoardInsertTest")
    void testInsertDummies() {
        IntStream.rangeClosed(1, 100).forEach(i -> {
            Board board = Board.builder()
                    .title("title" + i)
                    .content("content" + i)
                    .build();
            boardRepository.save(board);
        });
    }

    @Test
    @DisplayName("BoardSelectTest")
    void testSelect() {
        Long boardId = 105L;
        Optional<Board> result = boardRepository.findById(boardId);
        System.out.println("==============================");
        if (result.isPresent()) {
            Board board = result.get();
            System.out.println(board);
        }
    }

    @Transactional
    @Test
    @DisplayName("BoardSelectTest2")
    void testSelect2() {
        Long boardId = 105L;
        Board board = boardRepository.getOne(boardId);
        System.out.println("==============================");
        System.out.println(board);
    }

    @Test
    @DisplayName("BoardUpdateTest")
    void testUpdate() {
        Board board = Board.builder()
                .boardId(100L)
                .title("수정제목00")
                .content("수정내용00")
                .build();
        System.out.println(boardRepository.save(board));
        // JPA는 entity 객체를 메모리에 보관하려함
        // Repository.save 명령 실행 시
        // 특정 엔티티 객체가 있는지 확인하는 select(1) -> 그 후 존재하면 update(2)
        // 존재하지 않으면 insert(2) 실행됨
    }

    @Test
    @DisplayName("BoardDeleteTest")
    void deleteTest() {
        Long boardId = 101L;
        boardRepository.deleteById(boardId);
        // 일단 한 번 select 해서 해당 객체가 존재하면 delete 진행
        //  해당 데이터가 존재하지 않으면 EmptyResultDataAccessException 예외 발생
    }

    @Test
    @DisplayName("Pageable test")
    void testPageDefault() {
        // 1페이지 10개
        Pageable pageable = PageRequest.of(0, 10);

        Page<Board> result = boardRepository.findAll(pageable);

        System.out.println(result);
        // Page 1 of 10 containing com.example.board1.entity.Board instances
        System.out.println("------------------------------");

        //총 페이지 수
        System.out.println("Total Pages: " + result.getTotalPages());
        // Total Pages: 10

        // 전체 개수
        System.out.println("Total Count: " + result.getTotalElements());
        // Total Count: 100

        // 현재 페이지 번호
        System.out.println("Page Number: " + result.getNumber());
        // Page Number: 0

        // 페이지당 데이터 개수
        System.out.println("Page Size: " + result.getSize());
        // Page Size: 10

        // 다음 페이지 존재 여부 T/F
        System.out.println("has next page?: " + result.hasNext());
        // has next page?: true

        // 시작페이지(0) 여부 T/F
        System.out.println("first page?: " + result.isFirst());
        // first page?: true

        // 실제 페이지의 data 처리는 getContent() 통해서 List<Entity> 로 처리하거나
        // Stream<Entity> 을 반환하는 get() 이용
        System.out.println("------------------------------");
        result.getContent().forEach(System.out::println);
    }

    @Test
    @DisplayName("Sort1 test")
    void testSort() {
        // boardId 기준으로 DESC
        Sort sort1 = Sort.by("boardId").descending();

        // PageRequest.of(page, size, Sort)
        Pageable pageable = PageRequest.of(0, 10, sort1);

        Page<Board> result = boardRepository.findAll(pageable);

        result.get().forEach(System.out::println);
    }

    @Test
    @DisplayName("Sort2 test")
    void testSort2() {
        // boardId 기준으로 DESC
        Sort sort1 = Sort.by("boardId").descending();
        Sort sort2 = Sort.by("title").ascending();
        Sort sortAll = sort1.and(sort2);  // and 이용해 연결

        // PageRequest.of(page, size, Sort)
        Pageable pageable = PageRequest.of(0, 10, sortAll);

        Page<Board> result = boardRepository.findAll(pageable);

        result.get().forEach(System.out::println);

        /*
            [ 결과 ]
            order by
                board0_.board_id desc,
                board0_.title asc limit ?
         */
    }

    @Test
    @DisplayName("Query Methods test")
    void testQueryMethods() {

        List<Board> list = boardRepository.findByBoardIdBetweenOrderByBoardIdDesc(170L, 180L);

        list.forEach(System.out::println);
    }

    @Test
    @DisplayName("Query Methods Pageable test")
    void testQueryMethodsWithPageable() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("boardId").descending());

        Page<Board> result = boardRepository.findByBoardIdBetween(170L, 180L, pageable);

        result.get().forEach(System.out::println);
    }

    @Test
    @DisplayName("Query Methods Delete test")
//    @Transactional
//    @Commit
    void testDeleteQueryMethods() {
        boardRepository.deleteBoardByBoardIdLessThan(120L);
    }
}