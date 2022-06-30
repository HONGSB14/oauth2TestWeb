package oauth2web.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor //필수 인수가 있는 생성자를 생성하기 위한 어노테이션이다.
@Getter
public enum Role {  //열거형 클래스

    MEMBER("ROLE_MEMBER", "회원"), //열거형을 지정한다.
    ADMIN("ROLE_ADMIN", "관리자");    //열거형을 지정한다.

    private final String key;             //열거형에 들어가는 필드 항목들로 (key) 이다.
    private final String keyword;    //열거형에 들어가는 필드 항목들로 (keyword) 이다.
}
