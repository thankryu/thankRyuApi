# thankRyuApi

url 접근법</br>
1. Controller @RequestMapping 사용 </br>
2. 참조된 Service 메소드 및 서비스명 사용</br>
ex) @RequestMapping : common, 서비스명: commonService, 메소드명 : naverShortUrl</br>
= common/commonService/naverShortUrl 로 호출</br></br>

config.properties 설정파일</br>
naver.client = "네이버 API아이디"</br>
naver.secret = "네이버 API비밀번호"</br>
file.path = "파일 경로"</br>
