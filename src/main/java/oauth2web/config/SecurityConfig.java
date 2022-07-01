package oauth2web.config;

import oauth2web.service.IndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration                                                                                                                    //해당 클래스가 XML을 대체하는 설정파일임을 설정하는 어노테이션이다.
    public class SecurityConfig extends WebSecurityConfigurerAdapter {                    //WebSecurityConfigurerAdapter 상속받은 클래스이다.

        @Override                                                                                                                         //  WebSecurityConfigurerAdapter 클래스를 재정의한다.
        protected void configure(HttpSecurity http) throws Exception {                               //HTTP(URL) 관련 시큐리티 보안구성 메소드
            http.authorizeHttpRequests().antMatchers("/").permitAll()            // 모든 페이지의 권한을 열어준다.
                    .and()                                                                                                                    //해당 메소드가 사용이 완료되면 HttpSecurityBuilder 를 반환하여 메소드체인징시켜준다.
                    .formLogin()                                                                                                        //로그인 페이지 인증 양식을 제공한다.
                    .loginPage("/member/login")                                                                             //로그인페이지로 사용자 이름을 찾는 HTTP매개 변수이다.
                    .loginProcessingUrl("/member/loginController")                                           //로그인 인증에 관한 유효성 검사를 할 URL를 지정한다.
                    .usernameParameter("mid")                                                                            //로그인 시 사용 될 id값을 파라미터로 지정한다.
                    .passwordParameter("mpassword")                                                                //로그인 시 사용 될 password값을 파라미터로 지정한다.
                    .failureUrl("/member/login/error")                           //로그인 실패시 사용자에게 보낼 URL주소를 설정한다.
                    .and()                                                                                                                  //해당 메소드가 사용이 완료되면 HttpSecurityBuilder 를 반환하여 메소드체인징시켜준다.
                    .logout()                                                                                                              //로그아웃 지원을 제공한다.
                    .logoutRequestMatcher( new AntPathRequestMatcher("/member/logout")) //로그아웃을 실행시킨다. ( 새롭게 매처를 만들어 해당 패턴값을 로그아웃 페이지로 지정한다.)
                    .logoutSuccessUrl("/")                                                                                       // 로그아웃 성공시 리디렉션 할 URL을 지정한다.
                    .invalidateHttpSession(true)                                                                           // 로그아웃 시 http세션값을 무효화 시킨다. 무효화  ( true ) 그렇지 않다면 ( false )
                    .and()                                                                                                                 //해당 메소드가 사용이 완료되면 HttpSecurityBuilder 를 반환하여 메소드체인징시켜준다.
                    .csrf()                                                                                                                 //csrf 보호를 활성화 시킨다.
                    .ignoringAntMatchers("/member/logincontroller")             //해당 URL이 RequestMatcher 와 일치하더라도  csrf의 보호무시를 할 수 있게 지정한다.
                    .ignoringAntMatchers("/member/signupcontroller")          //해당 URL이 RequestMatcher 와 일치하더라도  csrf의 보호무시를 할 수 있게 지정한다.
                    .and()                                                                                                                //해당 메소드가 사용이 완료되면 HttpSecurityBuilder 를 반환하여 메소드체인징시켜준다.
                    .oauth2Login()                                                                                                 //Oauth2 인증지원 양식을 제공한다.
                    .userInfoEndpoint()                                                                                         //인증의 끝 지점을 선언한다.
                    .userService(indexService);                                                                            //Oauth2 서비스를 선언한다. 단, userInfoEndpoint()  뒤에 선언되어야한다.
        }
        @Autowired     //Bean 을 주입한다.
        private IndexService indexService;

        @Override   //재정의 한다.
        protected void configure( AuthenticationManagerBuilder AMB ) throws Exception{  //Security기본값 설정 대신 사용자가원하는 커스텀으로 만들기 위한 메소드 정의
            AMB.userDetailsService(indexService).passwordEncoder(new BCryptPasswordEncoder());  //패스워드는 BCryptPassword 로하는 USER 커스텀 정의한다.
        }
}
