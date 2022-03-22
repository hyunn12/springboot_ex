package com.example.board1.repository;

import com.example.board1.entity.Board;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

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
        Long board_id = 105L;
        Optional<Board> result = boardRepository.findById(board_id);
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
        Long board_id = 105L;
        Board board = boardRepository.getOne(board_id);
        System.out.println("==============================");
        System.out.println(board);
    }

    @Test
    @DisplayName("BoardUpdateTest")
    void testUpdate() {
        Board board = Board.builder()
                .board_id(100L)
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
        Long board_id = 101L;
        boardRepository.deleteById(board_id);
        // 일단 한 번 select 해서 해당 객체가 존재하면 delete 진행
        //  해당 데이터가 존재하지 않으면 EmptyResultDataAccessException 예외 발생
    }

    @Test
    void testPageDefault() {
        // 1페이지 10개
        Pageable pageable = PageRequest.of(0, 10);

        Page<Board> result = boardRepository.findAll(pageable);

        System.out.println(result);
    }

}