package oauth2web.dto;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

public class LoginDto implements UserDetails { //로그인 세션을 만들기 위한 UserDetails 를 구현 받고 있다.  UserDetails = Spring Security 에서 유저의 정보를 담는 인터페이스이다.
    private String mid;     // 유저 아이디
    private String mpassword;   //유저 패스워드
    private Set<GrantedAuthority> authorities;  //권한

    public LoginDto(String mid,String mpassword, Collection<? extends GrantedAuthority> authorities) {  //LoginDto 풀 생성자 생성
        this.mid = mid;
        this.mpassword=mpassword;
        this.authorities= Collections.unmodifiableSet(new LinkedHashSet<>(authorities));            //?
    }

    @Override //재정의
    public Collection<? extends GrantedAuthority> getAuthorities() {    //GrantedAuthority 에서 구현된 객체 반환 메소드

        return this.authorities;
    }

    @Override //재정의
    public String getPassword() {
        return this.mpassword;
    }   //UserDetails 구현 값

    @Override //재정의
    public String getUsername() {
        return this.getUsername();
    } //UserDetails 구현 값

    @Override //재정의
    public boolean isAccountNonExpired() {
        return true;
    } //UserDetails 구현 값

    @Override //재정의
    public boolean isAccountNonLocked() {
        return true;
    } //UserDetails 구현 값

    @Override //재정의
    public boolean isCredentialsNonExpired() {
        return true;
    } //UserDetails 구현 값

    @Override //재정의
    public boolean isEnabled() {
        return true;
    } //UserDetails 구현 값
}
