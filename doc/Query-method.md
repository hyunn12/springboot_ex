# Query Methods 와 @Query

특정 범위의 객체를 검색하거나, like 처리가 필요한 경우 여러 검색 조건이 필요하게됨

Spring Data JPA에서 처리하는 방법
- **쿼리 메소드**: 메소드 이름 자체가 쿼리 구문으로 처리
- **@Query**: SQL과 유사하게 엔티티 클래스의 정보를 이용해 쿼리 작성
- **Querydsl** 등의 동적 쿼리 처리 기능


### 쿼리 메소드 Query Methods
이름 자체가 쿼리문이 되는 기능

주로 `findBy...`, `getBy...` 로 시작

이후에 And, Or 등 키워드 추가해 이름 자체로 질의 조건 만듦

사용하는 키워드 따라 파라미터 개수 결정

리턴타입도 자유로운편
- select를 하는 작업인 경우 List 타입이나 배열 사용 가능
- 파라미터에 Pageable 타입을 넣는 경우 무조건 Page<E> 타입

#### ex
```java
public interface BoardRepository extends JpaRepository<Board, Long> {
    
    List<Board> findByBoardIdBetweenOrderByBoardIdDesc(Long from, Long to);

}
```

테스트코드
```java
@Test
@DisplayName("Query Methods test")
void testQueryMethods() {
    
    List<Board> list = boardRepository.findByBoardIdBetweenOrderByBoardIdDesc(170L, 180L);
    
    list.forEach(System.out::println);
}
```

결과
```
select
    board0_.board_id as board_id1_0_,
    board0_.content as content2_0_,
    board0_.title as title3_0_ 
from
    tbl_board board0_ 
where
    board0_.board_id between ? and ? 
order by
    board0_.board_id desc
```

작성한 메소드의 이름이 쿼리문으로 그대로 반영됨

#### Delete
```java
public interface BoardRepository extends JpaRepository<Board, Long> {

    void deleteBoardByBoardIdLessThan(Long num);

}
```

테스트코드
```java
@Test
@DisplayName("Query Methods Delete test")
@Transactional
@Commit
void testDeleteQueryMethods() {
    boardRepository.deleteBoardByBoardIdLessThan(110L);
}
```
deleteBy 인 경우 select로 해당 객체를 불러온 뒤 delete 작업 진행됨

`@Transactional` 이 있어야 `cannot reliably process 'remove' call; TransactionRequiredException` 에러가 발생하지 않음

`@Commit` 은 최종 결과를 커밋하기 위해 사용됨

deleteBy는 기본적으로 **_rollback_** 되기때문에 결과가 반영되지않음

또한 delete 진행 시 한번에 이뤄지는 것이 아닌 개별 delete 가 호출되어 비효율적이기에 잘 사용되지 않음

이런 경우 deleteBy 보단 `@Query`를 이용


