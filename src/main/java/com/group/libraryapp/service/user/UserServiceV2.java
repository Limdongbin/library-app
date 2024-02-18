package com.group.libraryapp.service.user;

import com.group.libraryapp.domain.user.User;
import com.group.libraryapp.domain.user.UserRepository;
import com.group.libraryapp.dto.user.request.UserCreateRequest;
import com.group.libraryapp.dto.user.request.UserUpdateRequest;
import com.group.libraryapp.dto.user.response.UserResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceV2 {

    private final UserRepository userRepository;

    public UserServiceV2(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // @Transactional = 아래 있는 함수가 시작될 때 start transaction; 을 해준다 (트랜잭션 시작!)
    // 함수가 예외 없이 잘 끝났다면 commit
    // 혹시라도 문제가 있다면 rollback
    @Transactional
    public void saveUser(UserCreateRequest request) {
        userRepository.save(new User(request.getName(), request.getAge()));
        // save 메서드에 객체를 넣어주면 INSERT SQL이 자동으로 날라간다.
        // save 되고 난 후 User는 Id가 들어있게 된다.
    }

    @Transactional(readOnly = true)
    // java8의 Stream을 활용한 코드 (좀 어렵다...)
    // 기억할 것 = findAll() -> 모든 모든 데이터를 가져온다 (Select * from User)
    public List<UserResponse> getUsers() {
        return userRepository.findAll().stream()
                .map(UserResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public void updateUser(UserUpdateRequest request) {
        // select * from User where id = ?;
        // Optional<User>
        User user = userRepository.findById(request.getId())
                .orElseThrow(IllegalArgumentException::new);
        // orElseThrow? 있다면 진행, 없다면 예외처리

        // 객체를 업데이트 해주고, save 메서드를 호출
        // 자동으로 Update SQL이 날라가게 됨.
        user.updateName(request.getName());
        userRepository.save(user);
    }

    @Transactional
    public void deleteUser(String name) {
        // Select
        User user = userRepository.findByName(name)
                .orElseThrow(IllegalArgumentException::new);

        userRepository.delete(user);
    }
}
