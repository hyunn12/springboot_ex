# @Query

기본적으로 제공되는 쿼리메소드는 간편하지만 조인 등 복잡한 조건을 처리하는 경우 메소드명도 너무 길어지고 복잡해지기에 불편

간단한 처리일 경우엔 쿼리메소드를 이용하되 복잡한 경우 @Query 를 이용하는 것이 나음

@Query는 JPQL (Java Persistence Query Language) 로 작성 (객체지향쿼리)

- 필요한 data만 선별적으로 추출 가능
- DB에 맞는 native SQL 사용 가능
- select를 제외한 DML등 처리 기능 (@Modifying 과 함께 사용)

객체지향쿼리는 테이블 대신 엔티티클래스 이용, 컬럼대신 클래스 필드 이용

```java
@Query("select b from Board b order by b.boardId desc")
List<Board> getListDesc();
```
실제 SQL에서 사용되는 함수도 사용 가능


### 파라미터 바인딩

where 구문 및 파라미터 처리할 경우

- '?1, ?2' 와 1부터 시작하는 파라미터 순서 이용
- '.parameter' 이용
- ':#{ }' 자바 빈 스타일 이용

```java
// :parameter
@Transactional
@Modifying
@Query("update Board b set b.title = :title where b.boardId = :boardId")
int updateTitle(@Param("boardId") Long boardId, @Param("title") String title);

// :#{ parameter}
@Transactional
@Modifying
@Query("update Board b set b.title = :#{#param.title} where b.boardId = :#{#param.boardId}")
int updateTitle(@Param("param") Board board);
```


### Object[] 리턴
쿼리메소드는 엔티티타입 데이터만 추출하지만 @Query는 Object[] 로 추출가능

join이나 group by 사용할 경우 적당한 entity type이 없는 경우 있을 수 있음<br>
-> 이럴 때 유용

```java
@Query("select b.boardId, b.title, CURRENT_DATE from Board b where b.boardId > :boardId"
        , countQuery = "select count(b) from Board b where b.boardId > :boardId")
Page<Object[]> getListWithQueryObject(Long boardId, Pageable pageable);
```

### Native SQL
DB 고유의 SQL문 활용 가능

BUT JPA자체가 DB 독립적으로 구현 가능하다는 장점 잃음

경우에 따라 복잡한 join 등을 사용해야하는 경우에만 사용하는 것이 좋음

```java
@Query(value = "select * from board where boardId > 0", nativeQuery = true)
List<Object[]> getNativeResult();
```
nativeQuery의 값을 true로 설정하면 일반 SQL문 그대로 사용 가능함




