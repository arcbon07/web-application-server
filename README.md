# 실습을 위한 개발 환경 세팅
* https://github.com/slipp/web-application-server 프로젝트를 자신의 계정으로 Fork한다. Github 우측 상단의 Fork 버튼을 클릭하면 자신의 계정으로 Fork된다.
* Fork한 프로젝트를 eclipse 또는 터미널에서 clone 한다.
* Fork한 프로젝트를 eclipse로 import한 후에 Maven 빌드 도구를 활용해 eclipse 프로젝트로 변환한다.(mvn eclipse:clean eclipse:eclipse)
* 빌드가 성공하면 반드시 refresh(fn + f5)를 실행해야 한다.

# 웹 서버 시작 및 테스트
* webserver.WebServer 는 사용자의 요청을 받아 RequestHandler에 작업을 위임하는 클래스이다.
* 사용자 요청에 대한 모든 처리는 RequestHandler 클래스의 run() 메서드가 담당한다.
* WebServer를 실행한 후 브라우저에서 http://localhost:8080으로 접속해 "Hello World" 메시지가 출력되는지 확인한다.

# 각 요구사항별 학습 내용 정리
* 구현 단계에서는 각 요구사항을 구현하는데 집중한다. 
* 구현을 완료한 후 구현 과정에서 새롭게 알게된 내용, 궁금한 내용을 기록한다.
* 각 요구사항을 구현하는 것이 중요한 것이 아니라 구현 과정을 통해 학습한 내용을 인식하는 것이 배움에 중요하다. 

### 요구사항 1 - http://localhost:8080/index.html로 접속시 응답
* in, out  InputSream -> InputStreamReader -> BufferedReader
* byte[] body Files.readAllBytes(new File("./webapp" + url).toPath())
* DataOutputStream
* response200Header, responseBody

### 요구사항 2 - get 방식으로 회원가입
* parseQueryString
* get은 주소줄에 값이 ?뒤에 쌍으로 이어붙는다.
* get은 url에 정보를 이어 붙이기 때문에 길이제한이 있어서 많은 양의 데이터는 보내기 어렵다.
* get은 select적인 성향을 가지고 잇어 어떤 데이터를 가져올지 선택하는 데 유용하다.

### 요구사항 3 - post 방식으로 회원가입
* post는 data가 숨겨져서 body안에 보내진다.
* 만은 양의 데이터를 보낼 수 있다.
* form을 이용하여 method를 post 지정하여 사용한다.
* post는 서버의 값이나 상태를 바꾸기 위해서 사용한다.

### 요구사항 4 - redirect 방식으로 이동
* 302 방식은 client에게 url을 전달한다. client는 전달받은 url로 다시 server에 요청한다.

### 요구사항 5 - cookie
* 

### 요구사항 6 - stylesheet 적용
* 

### heroku 서버에 배포 후
* 