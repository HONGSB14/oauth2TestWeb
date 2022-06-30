package oauth2web.dto;

import lombok.*;
import oauth2web.entity.MemberEntity;
import oauth2web.entity.Role;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.Entity;


@AllArgsConstructor
@Getter
@Setter
@Builder
@NoArgsConstructor
public class MemberDto {
    private int mno;        //유저번호
    private String mid;   //유저아이디
    private String mpassword;   //유저 비밀번호

    public MemberEntity toEntity(){ //Dto -> Entity 변환 메소드

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();    //패스 워드 암호화를 위한  BCryptPasswordEncoder 객체 생성

        return MemberEntity.builder().
                mid(this.mid)
                .mpassword(encoder.encode(this.mpassword))
                .role(Role.MEMBER)
                .build();

    }
}
