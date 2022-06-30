package oauth2web.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity //JPA 어노테이션으로 해당 어노테이션 작성시 Entity 임을 선언한다.
@AllArgsConstructor
@Getter@Setter
@Builder
public class MemberEntity { //Entity 클래스이다.
    @Id //PK 어논테이션이다.
    @GeneratedValue(strategy = GenerationType.IDENTITY)    //자동번호 즉 auto-key 설정 어노테이션이다.
    private int mno;                         //유저번호
    private String mid;                    //유저 아이디
    private String mpassword;      //유저 비밀번호

    @Enumerated                          //열거형 유형을 매핑하는데 사용되는 어노테이션이다.
    private Role role;                      //열거형 클래스
}
