package com.group.libraryapp.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// JPARepository를 상속! + <Entity 객체, Id값의 타입>
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByName(String name);
    // find = 1개의 데이터만 가져온다
    // By 뒤에 붙는 필드 이름으로 Select 쿼리의 Where 문이 작성된다
    // SELECT * FROM user Where name = ?

    // ---- By 앞에 들어올 수 있는 기능 ----

    // find : 1건을 가져온다. 반환 타입은 객체가 될 수도 있고, Optional<타입>이 될 수도 있다.
    // findAll : 쿼리의 결과물이 N개인 경우 사용. List<타입> 반환.
    // exists : 쿼리 결과가 존재하는지 확인. 반환 타입은 boolean
    // count : SQL의 결과 개수를 센다. 반환 타입은 long
    
    // ---- By 뒤에 들어올 수 있는 기능 ----
    // 각 구절은 And나 Or로 조합할 수 있다.
    // ex) findAllByNameAndAge
    // GreaterThan : 초과 / GreaterThanEqual : 이상 / LessThan : 미만 / LessThanEqual : 이하
    // Between : 사이에
    // StartWith: ~로 시작하는 / EndsWith: ~로 끝나는
    // ex) List<User> findAllByAgeBetween(int startAge, int endAge);
    // SELECT * FROM user WHERE age BETWEEN ? AND ?
}
