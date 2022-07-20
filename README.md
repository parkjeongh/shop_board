# Spring boot shopping mall project

- IntelliJ  
- Spring boot 2.7.1  
- Gradle

## 1. 상품 엔티티 설계
- ItemSellStatus (enum class)  
  - SELL, SOLD_OUT

- Item (Entity class)  
  - 상품코드 Long id - PK  
  - 상품명 String itemNm  
  - 가격 int price  
  - 재고수량 int stockNumber  
  - 상품상세설명 String itemDetail  
  - 상품판매상태 ItemSellStatus itemSellStatus
  
## 2. Repository 설계

- ItemRepository extends JpaRepository<Item, Long>  
  - JpaRepository 를 상속 받는 인터페이스
  - Item : 엔티티 클래스
  - Long : PK 타입
  
## 3. Query Method

`find + (엔티티 이름) + By + 변수이름`

- findByItemNm
- findByItemNmOrItemDetail
- findByPriceLessThan
- findByPriceLessThanOrderByPriceDesc

## 4. Spring Data JPA @Query Annotation

`@Query("JPQL 쿼리문")`

- findByItemDetail
- findByItemDetailByNative

## 5. Spring Data JPA Querydsl

Querydsl : JPQL -> 코드로 작성할 수 있도록 도와주는 빌더 API

- dependencies 추가   
  `implementation 'com.querydsl:querydsl-jpa'`  
  `annotationProcessor "com.querydsl:querydsl-apt:5.0.0:jpa"`  
  `annotationProcessor "jakarta.persistence:jakarta.persistence-api"`  
  `annotationProcessor "jakarta.annotation:jakarta.annotation-api"`

- 빌드 후 `Qitem.java` 생성 확인

## 6. Spring Security 설정

- dependency 추가  
`implementation 'org.springframework.boot:spring-boot-starter-security'`

- SecurityConfig : Spring Security 설정 class
  - WebSecurityConfigurerAdapter 상속 -> configure() 오버라이딩

## 7. 회원 가입 기능 구현
- Role (enum class)  
  - USER, ADMIN  

- MemberFormDto : 회원 가입 화면에서 넘어오는 정보를 담아놓는 dto
  - String name
  - String email
  - String password
  - String address

- Member (Entity Class) : 회원 정보 저장하는 class  
  - Long id - pk
  - String name
  - String email - unique
  - String password
  - String address
  - Role role
  - Member createMember() (Member 엔티티 생성 메서드)

- MemberRepository : Member 엔티티를 데이터베이스에 저장하는 interface
  - JpaRepository 상속
  - Member findByEmail() (중복회원 검사 쿼리 메서드)

- MemberService
  - MemberRepository 를 주입 받는다.
  - Member joinMember()
  - validateDuplicateMember() (회원 중복 검사)

- MemberController
  - MemberService 를 주입 받는다.
  - memberForm.html 에서 입력한 정보를 MemberFormDto 에 담는다.
  - Member.createMember() -> memberService.joinMember()

## 8. 로그인, 로그아웃 구현

- MemberService
  - 인터페이스 UserDetailsService 구현 -> loadUserByUsername() 오버라이딩 (parameter : email)
  - User(email, password, role) 객체 반환

- SecurityConfig 에서 http.formLogin() 로그인, 로그아웃 설정
- MemberController - 로그인 경로 설정

## 9. 페이지 권한 설정

- ItemController
- CustomAuthenticationEntryPoint : 미인증 사용자 요청 -> Unauthorized 에러 발생
  - 인터페이스 AuthenticationEntryPoint 구현
- SecurityConfig
  - http.authorizeRequest().mvcMatchers(경로패턴).권한설정
