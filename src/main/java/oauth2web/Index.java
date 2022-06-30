package oauth2web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication  //Spring Framework 중 하나인 MVC 모델 적용이 가능한  웹 프레임 워크 설정 어노테이션
public class Index {    //인덱스 클래스

    public static void main(String[] args){       // 메인 메소드(쓰레드를 생성한다.)
        SpringApplication.run(Index.class);      //Application.properties 의 값을 로딩 후 서버실행 ( Index 클래스를 실행한다. )

    }
}
