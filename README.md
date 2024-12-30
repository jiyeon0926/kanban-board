# 💡 kanban-board 
## ONE pick! 의 Mozzarello 🧀


- ### 프로젝트 :  모짜렐로
- #### 소개
    - 한 줄 정리 : ‘모’든 곳에서 일등을 원하는 진‘짜’들을 위한 협업 관리 서비스(Trello)
    
    - 내용 : 특정 분야에서 정점이 되고 싶은 고객들을 위한 협업 툴
        - 기존의 업무 효율성 향상을 위한 협업 툴을 뛰어넘어, 밀도 높은 업무 수행까지도 할 수 있도록 도움을 주는 기능을 추가하였다.
    
    - 기존 협업 툴과의 차별성
        - 로그인 알림 기능 : 팀원들이 얼마나 밀도 있게 업무에 몰입하는지를 실시간으로 확인할 수 있다.
        - 카드 첨부 파일 기능에 다양한 파일을 업로드함으로써 서로의 작업물들을 좀 더 쉽고 빠르게 확인할 수 있다.
------------
### 심화 프로젝트 Trello 만들기 🧑🏻‍💻

------------
###  프로젝트 기간
#### 🗓 2024.12.23 ~ 2024.12.31
------------
## 🛠 개발 환경 
- Tech : ![Spring](https://img.shields.io/badge/Spring-6DB33F.svg?&style=for-the-badge&logo=Spring&logoColor=white) 3.4.1
- IDE : ![IntelliJ IDEA](https://img.shields.io/badge/IntelliJIDEA-000000.svg?style=for-the-badge&logo=intellij-idea&logoColor=white)
- JDK : ![Java](https://img.shields.io/badge/Java-007396.svg?&style=for-the-badge&logo=Java&logoColor=white) v.17
- DB : ![MySQL](https://img.shields.io/badge/MySQL-4479A1.svg?&style=for-the-badge&logo=MySQL&logoColor=white) Ver 8+
------------
## 와이어프레임 📝

<details>
  <summary>  와이어프레임 </summary> 

![Figma](https://img.shields.io/badge/figma-%23F24E1E.svg?style=for-the-badge&logo=figma&logoColor=white)


  ## Wireframe
  ![image](https://github.com/user-attachments/assets/c2b233e1-1972-41e8-82f6-b631309f1339)


  ### User
  <img width="1401" alt="스크린샷 2024-12-31 오전 5 41 28" src="https://github.com/user-attachments/assets/5c274f7f-3adc-4b14-b002-f9e323673130" />

  ### Admin
  <img width="1376" alt="스크린샷 2024-12-31 오전 5 40 41" src="https://github.com/user-attachments/assets/50fb4b12-9bcd-4fe8-a2ff-2c608accf365" />

  ### Workspace
  <img width="1127" alt="스크린샷 2024-12-31 오전 5 44 02" src="https://github.com/user-attachments/assets/42c8eca9-92ff-45b0-9f7a-0898dca02079" />

  ### Board
  <img width="576" alt="스크린샷 2024-12-31 오전 5 45 39" src="https://github.com/user-attachments/assets/cf409e28-4554-4684-8da6-ddf403e0754c" />

  ### BoardList
  <img width="995" alt="스크린샷 2024-12-31 오전 5 46 00" src="https://github.com/user-attachments/assets/09655fe4-b435-4a56-a503-11a3cd40b409" />

  ### Card
  <img width="558" alt="스크린샷 2024-12-31 오전 5 46 24" src="https://github.com/user-attachments/assets/67bc9182-012c-4fee-b3bb-0381ba888a31" />

  ### Comment
  <img width="924" alt="스크린샷 2024-12-31 오전 5 46 53" src="https://github.com/user-attachments/assets/3a628c02-f349-45c9-8e56-674c7f336820" />

  ### Serch
  <img width="351" alt="스크린샷 2024-12-31 오전 5 47 18" src="https://github.com/user-attachments/assets/9e03656c-da43-426b-8b86-25489dbd0868" />

  ### Notification
  <img width="351" alt="스크린샷 2024-12-31 오전 5 47 38" src="https://github.com/user-attachments/assets/dd588e36-4c32-42aa-8954-c6f702e3719c" />


  </details>


------------
## ERD 📁

![image](https://github.com/user-attachments/assets/4715e634-5f58-47ef-bd51-c82511be1cbb)

------------
## API 명세서 📋

### [Postman Mozzarello Kanban API 명세서](https://documenter.getpostman.com/view/39375040/2sAYJ7fJja)
  
---------------
## 기능 구현 🖥  

### 공통
- 클라이언트가 서버로 요청을 보낼 때마다 해당 Cookie를 자동으로 포함시켜 로그인 인증 처리
- 수정, 삭제 시 Session 에 저장된 로그인된 유저 정보를 통해 본인만 수정 / 삭제 
- 회원, 가게, 메뉴 삭제할 때 Hard Delete 대신, DeleteMapping 후 Enum 상태 값을 변경하여 Soft Delete로 구현
- cascade 영속성 전이를 활용해 User 데이터가 삭제될 때 관련된 데이터가 같이 삭제
- GlobalExceptionHandler 를 구현하여 Error Code의 message와 status 를 일관되게 처리
- coupon type, coupon status, menu status, order status, point status, store status, user role, user status → Enum으로 관리 


### 회원가입, 로그인 `User`
- 로그인 후 서버는 클라이언트에게 JSESSIONID를 쿠키에 저장
- Enum으로 `UserRole`을 총 3가지로 구성하여 `User(고객)`, `Owner(사장)`, `Admin(관리자)` 권한 구분
- 이메일 중복 허용 불가, 탈퇴된 회원의 이메일로 재가입 불가
- `PasswordEncoder`를 만들어 비밀번호 `Bcrypt`로 암호화
- 정규식으로 비밀번호 유효성 검사(대소문자, 숫자, 특수문자 하나 이상 포함, 총 8자 이상)

### 가게 `Store`
- 사장님 `OWNER` 권한 : 가게 만들기, 가게 정보 수정, 가게 폐업 처리
- 사장 한 명은 3개 이하의 가게를 오픈 (폐업한 가게는 포함되지 않음)
- 고객은 가게 Id로 가게를 찾아볼 수 있음
  - 다건 조회시 메뉴를 볼 수 없음.
  - 단건 조회시 메뉴를 볼 수 있음.

### 메뉴 `Menu`
- 사장님 `OWNER` 권한 : 메뉴 만들기, 메뉴 수정, 메뉴 삭제
- 본인의 가게에만 메뉴를 등록할 수 있음
- 가게 조회 시 메뉴를 조회할 수 있음
- 메뉴가 품절된 경우 `MenuStatus`를 `OUT_OF_STOCK` 으로 변경
  - 품절된 메뉴는 주문 불가

### 주문 `Order`
- 고객님 `USER` 권한 : 주문 하기
- 바로 주문하기, 장바구니에서 주문하기
  - 주문에는 하나의 메뉴만 주문 가능
  - 최소 주문 금액을 넘기지 않으면 주문 불가
- 주문 시 메뉴와 수량, 총 가격을 확인
- 사장님 `OWNER` 권한 : `OrderStatus` 로 주문 상태 변경 가능 
  - `ORDER_COMPLETED`, `ACCEPT_ORDER`, `COOKING`, `COOKING_COMPLETED`, `ON_DELIVERY`, `DELIVERY_COMPLETED`, `REJECTED`
  - 주문 거절 `REJECTED` 로 변경된 주문은 다시 주문 수락 `ACCEPT_ORDER` 불가

### 리뷰 `Review`
- 고객님 `USER` 권한 : 리뷰 작성 하기
- 리뷰에 별점을 부여 (1~5)
- 배달 완료 `DELIVERY_COMPLETED` 상태가 아닌 주문에 대해 리뷰를 작성할 수 없음
- 가게에서 생성일자 기준 최신순으로 리뷰 다건 조회  `/reviews?storeId=1&sort=rating`

### 장바구니 `Cart`
- 고객님 `USER` 권한 : 장바구니
- 장바구니에 담아 둔 메뉴를 주문할 수 있음. 메뉴 하나(수량은 여러 개 가능)당 주문 하나
  - 다른 가게의 메뉴를 담을 수 없음
- 장바구니는 Cookie에 encoding 되어 저장, 하루가 지나면 자동으로 삭제
- 장바구니에 담긴 주문 전체 수정 가능
  - 메뉴, 수량 변경
- 장바구니에 새로운 메뉴를 추가
  - 장바구니에 없는 메뉴를 담았을 경우 메뉴가 추가됨
- 장바구니에서 주문 시 장바구니 초기화

### 대시보드 `Dashboard`
- 관리자 `ADMIN` 권한 : 통계 조회
  - 전체 일간 주문 건수/ 총액 조회
  - 전체 월간 주문 건수/총액 조회
  - 가게별 일간 주문 건수/총액 조회
  - 가게별 월간 주문 건수/총액 조회
  - 카테고리별 일간 주문 건수/총액 조회
  - 카테고리별 월간 주문 건수/총액 조회
 
### 포인트 및 쿠폰
- 사장님 `OWNER` 권한 : `Coupon` 생성 및 지정 고객에게 쿠폰 발급
- 고객님 `USER` 권한 : `Point`과 `Coupon` 을 사용하여 주문 가능, `Point` 보유 현황 조회
  - 쿠폰은 한 번만 사용할 수 있음. 사용 후에는 재발급 받아야 함.
- `Point` 는 주문 배달 완료 시 자동 적립 3%
  - `Potin`와 `Coupon`을 동시에 적용하여 주문 가능
- 유효기간이 만료되면 `Point` 차감 및 `Coupon` 소멸
- `Coupon` 은 정률(%) / 정액(₩) 두 종류
  - 총 발급 개수, 발급 개수 제한 가능
  
------------
## 시연 영상
### [ONE pick의 Mozzarello 시연 영상](https://youtu.be)

------------
## 팀원 👥

 |  |이름|역할|MBTI|Github|블로그|
 |------|------|------|------|------|------|
 |![2-276-2-324](https://github.com/user-attachments/assets/9e4bdcb9-f632-44f2-bce4-60680135079a)|김지연 |팀원|ISFP|[jiyeon0926](https://github.com/jiyeon0926))|[velog](https://velog.io/@yeoni9094/posts)|
 |![2-273-39-321](https://github.com/user-attachments/assets/cd4a49c9-4521-4702-b14f-e17ad7ad22c1)|장은영 |팀원|ESTJ|[eunyounging](https://github.com/eunyounging)|[tistory](https://write7551.tistory.com/)|
 |![2-269-61-313](https://github.com/user-attachments/assets/0262182a-412c-4a80-bccb-cd2f6d46071c)|김휘웅 |팀장|INTJ|[hwiung](https://github.com/hwiung)|[tistory](https://khu2172.tistory.com/)|
 |![2-274-2-322](https://github.com/user-attachments/assets/40501b7e-8e20-4b04-a129-650dbba3d7e9)|허수연 |팀원|INTJ|[sooyeoneo](https://github.com/sooyeoneo)|[tistory](https://sooyeoneo.tistory.com/)|

------------
## Tasks ✍🏻
### 김지연 
    1. 와이어프레임, ERD, API 설계
    2. 초기 설정 (환경변수, 엔티티, 패키지 정리)
    3. JWT
    4. 유저 CRUD
    5. 멤버 및 초대 보완
    6. 카드 검색
    7. 알림
    
### 장은영
    1. 와이어프레임, ERD, API 설계
    2. 리스트 CRUD
    3. 댓글 CRUD
    
### 김휘웅
    1. 와이어프레임, ERD, API 설계
    2. 워크스페이스 CRUD
    3. 초대
    
### 허수연
    1. 와이어프레임, ERD, API 설계
    2. 보드 CRUD
    3. 카드 CRUD
    4. 첨부파일 S3

------------

## 프로젝트를 마무리하며.. 🛎

* 김지연 : 구현을 하면서 설계의 부족함을 많이 느꼈고, 설계의 중요성을 다시 한 번 깨달을 수 있었습니다.
새로운 기능 구현도 좋지만 설계에 대한 학습 및 설계 연습을 많이 해보면서 성장해보려고 합니다.
협업 분위기가 좋아서 버틸 수 있었어요😊

* 장은영 : 팀원분들의 많은 도움 덕분에 프로젝트를 잘 완성할 수 있었습니다. 
또한 어려운 점이 생기면 공유하고 함께 고민해보는 시간을 통해 더 성장할 수 있었고, 제게 부족한 지식이 어떤 것인지 깨닫기도 했습니다. 
앞으로도 이러한 배움을 바탕으로 지속적으로 성장해 나가겠습니다🧸

* 김휘웅 : 여전히 어렵지만 그래도 조금씩이나마 성장하는 걸 느낄 수 있어서 좋았습니다.
또한, 넓은 시야에서 널리 내다보면서 설계를 하는 백엔드 개발이 매력있게 느껴지기 시작했습니다.
팀원들에게 많이 물어보면서 의지했지만, 나중엔 저도 팀원들에게 도움을 많이 주는 존재가 되고 싶습니다☃️

* 허수연 : S3 업로드 기능을 구현하면서 S3 권한 거부 오류가 발생하였습니다.
디버깅을 하지 않았다면 문제를 해결하지 못하였을 것 같네요. 이렇게까지 어렵게 S3를 연결하게 될 줄 몰랐는데, 개발은 언제나 새롭고 디버깅은 중요합니다. 
팀 프로젝트를 할 때마다 잠을 생략하는 삶을 살았는데, 이번에는 팀원 모두가 평일 주말 24시간 내내 프로젝트를 완성해가면서 진정으로 함께 프로젝트에 몰두한다고 느꼈습니다. 
물론 다음 팀프로젝트에서도 계속 밤을 새면 안 되겠지만요. 
함께해주신 ONE pick! 여러분들 감사합니다! 🌝

------------
## 트러블 슈팅 🎯
#### [[AWS] S3 FULL ACCESS 권한 오류 : 정책 권한 생성하기](https://sooyeoneo.tistory.com/110)



