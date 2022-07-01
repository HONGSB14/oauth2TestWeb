package oauth2web.dto;

import lombok.*;
import oauth2web.entity.MemberEntity;
import oauth2web.entity.Role;

import java.util.Map;
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Oauth2Dto {    //각각의 경로를 저장하기위한 Oauth2Dto 생성한다.

    private String mid;                       //유저 아이디
    private String registrationId;      //클라이언트 아이디
    private Map<String,Object > attributes; //로그인 결과
    private String nameAttributeKey;         //각 회사에마다 사용되고 있는 json 키

    public static Oauth2Dto sns확인(String registrationId , Map<String, Object>  attributes , String nameAttributeKey){   //경로 반환값을 설정 메소드

        if (registrationId.equals("naver")){    //만약  registrationId 가 " 네이버" 라면
            return NAVER(registrationId,attributes,nameAttributeKey);
        }else if(registrationId.equals("kakao")) {  // 만약  registrationId 가 "카카오 라면
            return KAKAO(registrationId,attributes,nameAttributeKey);
        }else{  //아무것도 아니라면
            return null;
        }
    }

    public static Oauth2Dto NAVER(String registrationId , Map<String, Object>  attributes , String nameAttributeKey){   //네이버 로그인 시  메소드
        Map<String,Object> response =(Map<String,Object>) attributes.get(nameAttributeKey);
        return Oauth2Dto.builder()
                .mid((String) response.get("email"))
                .build();
    }
    public static Oauth2Dto KAKAO(String registrationId , Map<String, Object>  attributes , String nameAttributeKey){ //카카오 로그인 시  메소드
        Map <String,Object> kakao_account=(Map<String, Object>) attributes.get(nameAttributeKey);
        return Oauth2Dto.builder()
                .mid((String)kakao_account.get("email"))
                .build();
    }

    public MemberEntity toEntity(){ // dto-> entity변환 메소드
        return MemberEntity.builder()
                .mid(this.mid)
                .role(Role.MEMBER)
                .build();
    }
}
