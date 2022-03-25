package com.example.board1.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name="tbl_board")
@ToString
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boardId;

    @Column(length = 200, nullable = false)
    private String title;

    @Column(length = 255, nullable = false)
    private String content;

}
