package com.example.board1.repository;

import com.example.board1.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {

    List<Board> findByBoardIdBetweenOrderByBoardIdDesc(Long from, Long to);

    Page<Board> findByBoardIdBetween(Long from, Long to, Pageable pageable);

    void deleteBoardByBoardIdLessThan(Long num);

}
