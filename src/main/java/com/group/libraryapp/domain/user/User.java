package com.group.libraryapp.domain.user;

import com.group.libraryapp.domain.user.loanHistory.UserLoanHistory;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
// 저장되고 관리되어야 하는 데이터
// 스프링에서는 트랜잭션을 사용하면 영속성 컨테스트가 생겨나고, 트랜잭션이 종료되면 영속성 컨테스트가 종료된다.
// 영속성 컨테스트의 특수 능력?
// 1. 변경 감지 (Dirty Check)
// -- 영속성 컨테스트 안에서 불러와진 Entity는 명시적으로 save 하지 않더라도, 변경을 감지해 자동으로 저장된다. (?)
// 2. 쓰기 지연
// -- DB의 INSERT / UPDATE / DELETE SQL을 바로 날리는 것이 아니라, 트랜잭션이 COMMIT 될때 모아서 한번만 날린다.
// 3. 1차 캐싱
// -- id를 기준으로 Entity를 기억한다.
// 4. 정말 꼭 필요한 순간에 데이터를 로딩한다 !!
// -- 지연로딩 (OneToMany = fetch 옵션 LAZY)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // auto_increment를 사용하는 것을 알려주는 어노테이션.
    // strategy = DB마다 해당하는 strategy가 있음 (MYSQL = identity)
    private Long id = null;

    @Column(nullable = false, length = 20) // name = varchar(20)
    private String name;
    private Integer age;

    // cascade = 연관관계에 놓인 테이블까지 함께 저장 또는 삭제가 이루어진다.
    // orphanRemoval = 객체간의 관계가 끊어진 데이터를 자동으로 제거하는 옵션
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserLoanHistory> userLoanHistories = new ArrayList<>();

    protected User() {}

    public User(String name, Integer age) {
        if(name == null || name.isBlank()) {
            throw new IllegalArgumentException(String.format("잘못된 name(%s)이 들어왔습니다", name));
        }
        this.name = name;
        this.age = age;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getAge() {
        return age;
    }

    public void updateName(String name) {
        this.name = name;
    }

    public void loanBook(String bookName) {
        this.userLoanHistories.add(new UserLoanHistory(this, bookName));
    }

    public void returnBook(String bookName) {
        // 함수형 프로그래밍 할 수 있게, stream을 시작한다.
        UserLoanHistory targetHistory = this.userLoanHistories.stream()
                // 들어오는 객체들 중에 다음 조건을 충족하는 것만 필터링. (userLoanHistory 중에 책 이름이 bookName이 같은걸 찾는다)
                .filter(history -> history.getBookName().equals(bookName))
                // 찾은 것들 중 첫번째 선택
                .findFirst()
                // 없다면 Exception 발생 (예외처리)
                .orElseThrow(IllegalArgumentException::new);
        // 반납처리!
        targetHistory.doReturn();
    }
}
