package oauth2web.service;

import oauth2web.dto.MemberDto;
import oauth2web.dto.Oauth2Dto;
import oauth2web.entity.MemberEntity;
import oauth2web.entity.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.*;


@Service  // SpringBootApplication 에서 설정된 어노테이션으로 해당 어노테이션이 클래스에 붙으면  그 클래스는 컨트롤러임을 나타낸다.
                  // @Service에는 @Component 가 달려있어 @ComponentScan을 통해 자동스캔되며 클래스경로 스캐닝을 통해 자동감지가 되도록 한다.
public class IndexService implements UserDetailsService , OAuth2UserService<OAuth2UserRequest , OAuth2User> { //UserDetailsSerivce ,  OAuth2UserService 를 구현하는 IndexService 클래스이다.
    /*
        해당 클래스는 2가지 기능을 가지고 있다.
        IndexService  = 컨트롤러에서 값을 받아 DB와 연결한다.
        UserDetailsService =  Spring Security 에서 유저의 정보를 가져온다.
     */

    @Autowired  //Bean 을 주입한다.
    private MemberRepository memberRepository;


    public Boolean signup(MemberDto memberDto){ //MemberRepository 와 IndexController 와 통신하며 회원을 저장하기 위한 boolean값을 리턴하는 메소드이다.
        memberRepository.save( memberDto.toEntity() );  //주어진 entity값을 저장한다.  (저장값 = memberDto.toEntity )
        return true;        //true 를 반환한다.
    }


    @Override  //재정의
    public UserDetails loadUserByUsername(String mid) throws UsernameNotFoundException{ // UserDetailsService 구현 메소드로 커스텀 유저를 정의한다.

        MemberEntity memberEntity=memberRepository.findByMid(mid).get();                   //JPA를 이용하여 원하는 값을 가져온다.
        //Custom User  재정의

        return new User( memberEntity.getMid() ,    //UserDetails 중 username
                memberEntity.getMpassword() ,              //UserDetails 중 userpassword
                Collections.singleton( new SimpleGrantedAuthority( memberEntity.getRole().getKey() ) )    );     //UserDetails 중 role (권한) 싱글톤으로 권한 생성(UserDetails  API에서 정해놓은 규칙으로 Collection 으로 반환)  (  memberEntity.getRole().getKey()  )
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {    // OAuth2UserService 구현 메소드

        OAuth2UserService oAuth2UserService = new DefaultOAuth2UserService();        // OAuth2User 설정값을 바꾸기위한 기본 OAuth2UserService 객체
        OAuth2User oAuth2User = oAuth2UserService.loadUser(userRequest);                 // OAuth2User  설정값을 바꾸기위한  OAuth2User 객체

        /*
            이 메소드의 목적은 OAuth2User를 재정의하기 위한 메소드로 OAuth2User 는
            ClientRegistration clientRegistration
             OAuth2AccessToken accessToken,
			Map<String, Object> additionalParameters  으로 구현 해줘야 한다.
			그러기 위해 사용자는 아래와 같이 재정의한다.
         */
        String registrationId= userRequest.getClientRegistration().getRegistrationId();    // OAuth2User 설정값을 바꾸기위한  OAuth2User 중 ClientRegistration clientRegistration 객체 선언
        String nameAttributeKey=userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();  // OAuth2User 유저의 속성값을 가지고있다.
        Map<String, Object> attributes=oAuth2User.getAttributes();                                   // OAuth2User 설정값을 바꾸기위한  OAuth2User 중 Map<String, Object> additionalParameters 객체 선언

        Oauth2Dto oauth2Dto =Oauth2Dto.sns확인(registrationId,attributes,nameAttributeKey);       //Oauth2 유저의 중복확인위해 생성되었다.
        Optional<MemberEntity> optional=memberRepository.findByMid(oauth2Dto.getMid());       //중복확인을 위해 JPA를 통하여 DB에서 비교하기위한 아이디를 가져온다.

        if(!optional.isPresent()){  //로그인 한 적이 없으면 DB생성
            memberRepository.save(oauth2Dto.toEntity());    //JPA를 통하여 DB에 Oauth2 회원을 저장한다.
        }else{  //로그인이력이 있다면 업데이트

        }
        memberRepository.save(oauth2Dto.toEntity());    //JPA를 통하여 엔티티를 DB에 저장
        return new DefaultOAuth2User(Collections.singleton(new SimpleGrantedAuthority("MEMBER")) , attributes ,  nameAttributeKey );     // 사용자가 OAuth2User를 재정의 한 후  반환한다.
    }


    public String  getAuthenticationMid(){  //인증 아이디 불러오기 메소드
     Authentication authentication= SecurityContextHolder.getContext().getAuthentication();  //인증이 된 객체를 가져온다.
    Object principal=authentication.getPrincipal(); //유저정보가 담겨있는 객체이다.

    String mid=null;
        if(principal!=null){  //인증 이 되어있는 상태
            if(principal instanceof UserDetails){   //만약 일반회원이라면
                mid =((UserDetails)principal).getUsername();
            }else if(principal instanceof OAuth2User){  //만약 오아스 유저라면
                Map<String,Object> attributes=((OAuth2User) principal).getAttributes();
                if(attributes.get("response") !=null){  //오아스 유저 중 네이버 회원이라면 ( "response" )
                    Map<String,Object> map=(Map<String, Object>) attributes.get("response");
                    mid=map.get("email").toString();
                }else if(attributes.get("kakao_account") !=null){ //오아스 유저 중 카카오 회원이라면 ( "kakao_account" )
                    Map<String,Object> map=(Map<String, Object>) attributes.get("kakao_account");
                    mid=map.get("email").toString();
                }else{  //오아스 유저도 아니라면
                }
            }
        }else{  //인증이 되어있지 않은 상태
        }
        return mid;     //mid를 반환
    }

}
