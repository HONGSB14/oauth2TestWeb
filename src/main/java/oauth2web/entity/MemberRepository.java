package oauth2web.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<MemberEntity, Integer> {    //JPA로 DB와 연결하기 위한 구현클래스

      Optional<MemberEntity> findByMid(String mid) ;  //DB에서 원하는 값을 찾기 위해 만든 필드


}
