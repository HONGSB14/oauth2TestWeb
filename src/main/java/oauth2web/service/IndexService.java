package oauth2web.service;

import oauth2web.dto.LoginDto;
import oauth2web.dto.MemberDto;
import oauth2web.entity.MemberEntity;
import oauth2web.entity.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.Collections;


@Service  // SpringBootApplication 에서 설정된 어노테이션으로 해당 어노테이션이 클래스에 붙으면  그 클래스는 컨트롤러임을 나타낸다.
                  // @Service에는 @Component 가 달려있어 @ComponentScan을 통해 자동스캔되며 클래스경로 스캐닝을 통해 자동감지가 되도록 한다.
public class IndexService implements UserDetailsService { //UserDetailsSerivce 를 구현하는 IndexService 클래스이다.
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
        return new User( memberEntity.getMid() , memberEntity.getMpassword() , Collections.singleton( new SimpleGrantedAuthority( memberEntity.getRole().getKey() ) )    );     //싱글톤으로 권한 생성  (  memberEntity.getRole().getKey()  )
    }

}
